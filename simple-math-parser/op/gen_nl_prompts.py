#!/usr/bin/env python3
# coding: utf-8
# 用途：从 src/ 扫描 Java 源，抽取每个方法并生成 NL 风格测试用例提示词
# 依赖：pip install javalang

import os, re, json, pathlib, textwrap
import javalang

ROOT = "/Users/shiqixuan/Documents/dev/llm-test-case-recovery/new/tasks/steps/1-MVP/simple-math-parser"
SRC_DIR = os.path.join(ROOT, "src")
OUT_DIR = os.path.join(ROOT, "prompts", "nl")

NL_TEMPLATE = """请为如下目标批量生成高质量、可编译运行的单元测试用例（优先语句/分支/异常覆盖，并尽可能覆盖边界与特殊取值）：

一、项目构建与依赖
- 语言/版本：Java 21
- 测试框架与版本：JUnit 5.10+
- 构建与运行：可使用 junit-platform-console 或 IDE 运行（无需网络/I/O）
- 依赖（至少以下可解析层级）：
  - 测试依赖：org.junit.jupiter:junit-jupiter
  - 源码依赖：本项目源码（无需第三方）
- 运行环境限制：无网络、无外部 I/O，测试互相独立、可重复

二、代码上下文
- 文件：{file_path}
- 包：{package}
- 类：{class_name}
- 方法签名：{method_signature}
- 抛出异常：{throws}
- 相关 import：{imports}
- 关键实现片段（用于理解控制流/分支；只读）：
```java
{method_snippet}
```

三、语义与需求（基于方法名/类型推断；若不适用可忽略）
- 输入/输出约束：基于签名类型的常规边界（空/零/负值/极值/小数）
- 浮点/数值断言：{float_hint}
- 不变量/性质（若适用）：交换性/幂等/单调性/边界闭合
- 代表性示例：请据实现片段推测正常/异常/边界输入各 1~2 例

四、故障/覆盖信号
- 目标覆盖：语句≥90%、分支≥90%、必触达异常/错误分支（若存在）
- 建议包含：参数化用例覆盖输入组合；异常/非法参数断言；属性/不变量验证（若适用）
- 边界/特殊值：0、1、-1、空/空集合、Integer.MIN/MAX、Double 极小/极大、very_small/very_large

五、约束与偏好
- Mock 策略：不使用外部 Mock，必要时提供最小桩（可编译级别）
- 断言风格：JUnit 原生断言；浮点比较使用 delta（如 1e-9）；必要时断言返回类型
- 测试命名：test_<行为>_<条件>，语义清晰；测试独立、无随机不设种子

六、输出要求
- 仅输出测试代码，包含必要的 package/import、测试类与 @Test 方法
- 每个测试独立构造被测对象，禁止共享可变状态
- 如需最小桩请一并生成并确保可编译

请据此为 {package}.{class_name}#{method_name} 生成测试代码。
"""

def read_text(p):
    with open(p, "r", encoding="utf-8") as f:
        return f.read()

def slice_method_snippet(src_text, start_line):
    lines = src_text.splitlines()
    # 找到方法体起始的 '{'
    i = start_line - 1
    while i < len(lines) and "{" not in lines[i]:
        i += 1
    if i >= len(lines):
        return "\n".join(lines[start_line-1:start_line+10])
    # 基于大括号配对截出方法体
    snippet = []
    brace = 0
    started = False
    for j in range(i, len(lines)):
        line = lines[j]
        snippet.append(line)
        brace += line.count("{")
        brace -= line.count("}")
        if not started and "{" in line:
            started = True
        if started and brace == 0:
            break
        if len(snippet) > 200:
            break
    # 限长，避免过大
    return "\n".join(snippet[:120])

def type_to_str(t):
    if t is None:
        return "void"
    if isinstance(t, str):
        return t
    try:
        name = t.name
    except Exception:
        name = str(t)
    if getattr(t, "dimensions", 0):
        name += "[]" * len(t.dimensions)
    if getattr(t, "arguments", None):
        # 泛型省略
        pass
    return name

def method_signature(clazz, m):
    params = []
    for p in m.parameters:
        t = type_to_str(p.type)
        params.append(f"{t} {p.name}")
    ret = type_to_str(m.return_type)
    mods = " ".join(sorted(m.modifiers)) if getattr(m, "modifiers", None) else ""
    return f"{mods} {ret} {clazz.name}.{m.name}({', '.join(params)})".strip()

def throws_list(m):
    if not getattr(m, "throws", None):
        return "无"
    return ", ".join(getattr(x, "name", str(x)) for x in m.throws)

def infer_float_hint(ret_type_str):
    rt = ret_type_str.lower()
    if any(k in rt for k in ["double", "float", "number", "bigdecimal"]):
        return "可能涉及浮点/数值；断言使用 delta（例如 1e-9）"
    return "常规精确断言即可"

def collect_imports(tree):
    try:
        imps = []
        for imp in (tree.imports or []):
            p = getattr(imp, "path", None)
            if p:
                imps.append(f"import {p};")
        return imps
    except Exception:
        return []

def main():
    os.makedirs(OUT_DIR, exist_ok=True)
    for dirpath, _, filenames in os.walk(SRC_DIR):
        if "/test" in dirpath.replace("\\", "/"):
            continue
        for fn in filenames:
            if not fn.endswith(".java"):
                continue
            path = os.path.join(dirpath, fn)
            src = read_text(path)
            try:
                tree = javalang.parse.parse(src)
            except Exception as e:
                # 跳过无法解析的文件
                continue
            imports = collect_imports(tree)
            pkg = getattr(tree.package, "name", "")
            for t in tree.types or []:
                if not isinstance(t, javalang.tree.ClassDeclaration):
                    continue
                for m in t.methods or []:
                    # 仅对可测试的实例或静态方法生成（跳过构造器/编译器生成）
                    if not isinstance(m, javalang.tree.MethodDeclaration):
                        continue
                    sig = method_signature(t, m)
                    throws = throws_list(m)
                    # 定位片段
                    start_line = getattr(m, "position", None).line if getattr(m, "position", None) else 1
                    snippet = slice_method_snippet(src, start_line)
                    float_hint = infer_float_hint(type_to_str(m.return_type))
                    prompt = NL_TEMPLATE.format(
                        file_path=os.path.relpath(path, ROOT),
                        package=pkg or "(default)",
                        class_name=t.name,
                        method_signature=sig,
                        throws=throws,
                        imports=" ".join(imports) if imports else "（无或不需要）",
                        method_snippet=snippet,
                        method_name=m.name,
                        float_hint=float_hint
                    )
                    # 输出文件
                    safe_name = f"{(pkg or 'default').replace('.', '_')}.{t.name}.{m.name}.txt"
                    out_path = os.path.join(OUT_DIR, safe_name)
                    with open(out_path, "w", encoding="utf-8") as f:
                        f.write(prompt)

if __name__ == "__main__":
    main()
