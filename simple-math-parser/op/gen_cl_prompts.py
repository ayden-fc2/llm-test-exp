#!/usr/bin/env python3
# coding: utf-8

"""
生成 CL（代码/结构化）提示词的脚本。

功能：
 - 扫描 src/ 下的 Java 源码（排除 src/test/），解析每个类的方法
 - 依据统一的 CL 模版：
   * 聚合生成 prompts/cl/all_targets.json
   * 同时按“每个方法一个文件”生成 prompts/cl/targets/<package>.<Class>.<method>.json

依赖：
 - pip install javalang

使用：
 - 在 simple-math-parser 项目根目录下运行：
   python3 op/gen_cl_prompts.py
 - 输出：
   * prompts/cl/all_targets.json（聚合）
   * prompts/cl/targets/*.json（每方法一文件）
"""

import os
import json
import pathlib
from typing import List, Dict, Any

import javalang


PROJECT_ROOT = pathlib.Path(__file__).resolve().parents[1]
SRC_DIR = PROJECT_ROOT / "src"
OUT_DIR = PROJECT_ROOT / "prompts" / "cl"
OUT_FILE = OUT_DIR / "all_targets.json"
TARGETS_DIR = OUT_DIR / "targets"


def read_text(p: pathlib.Path) -> str:
    with p.open("r", encoding="utf-8") as f:
        return f.read()


def type_to_str(t) -> str:
    if t is None:
        return "void"
    if isinstance(t, str):
        return t
    try:
        name = t.name
    except Exception:
        return str(t)
    # 维持简单可读性：不展开泛型
    if getattr(t, "dimensions", 0):
        name += "[]" * len(t.dimensions)
    return name


def method_signature(clazz, m) -> str:
    params = []
    for p in m.parameters:
        t = type_to_str(p.type)
        params.append(f"{t} {p.name}")
    ret = type_to_str(m.return_type)
    mods = " ".join(sorted(m.modifiers)) if getattr(m, "modifiers", None) else ""
    return f"{mods} {ret} {clazz.name}.{m.name}({', '.join(params)})".strip()


def slice_method_snippet(src_text: str, start_line: int, max_lines: int = 8) -> List[str]:
    lines = src_text.splitlines()
    i = max(0, start_line - 1)
    # 找到方法体起始的 '{'
    while i < len(lines) and "{" not in lines[i]:
        i += 1
    if i >= len(lines):
        chunk = lines[max(0, start_line - 1): start_line - 1 + max_lines]
        return [ln.rstrip() for ln in chunk]
    # 从 '{' 起取若干行作为 snippets（简化版）
    chunk = lines[i: i + max_lines]
    return [ln.rstrip() for ln in chunk]


def parse_java_file(path: pathlib.Path) -> Dict[str, Any]:
    src = read_text(path)
    try:
        tree = javalang.parse.parse(src)
    except Exception:
        return {"package": None, "types": []}
    pkg = getattr(tree.package, "name", "") if getattr(tree, "package", None) else ""
    # 收集文件级 import 列表
    file_imports: List[str] = []
    try:
        for imp in (tree.imports or []):
            path_str = getattr(imp, "path", None)
            if path_str:
                file_imports.append(f"import {path_str};")
    except Exception:
        pass
    results = []
    for t in (tree.types or []):
        if not isinstance(t, javalang.tree.ClassDeclaration):
            continue
        for m in (t.methods or []):
            if not isinstance(m, javalang.tree.MethodDeclaration):
                continue
            start_line = getattr(m, "position", None).line if getattr(m, "position", None) else 1
            snippet_lines = slice_method_snippet(src, start_line)
            sig = method_signature(t, m)
            # 针对该方法，给出推荐的源码 import（包含文件 import 与被测类 import）
            recommended_imports = list(file_imports)
            if pkg:
                recommended_imports.append(f"import {pkg}.{t.name};")
            results.append({
                "package": pkg or "(default)",
                "class": t.name,
                "function": m.name,
                "signature": sig,
                "file": str(path.relative_to(PROJECT_ROOT)),
                "snippets": [ln for ln in snippet_lines if ln.strip()],
                "imports": recommended_imports,
            })
    return {"package": pkg, "types": results}


def build_cl_prompt(targets: List[Dict[str, Any]]) -> Dict[str, Any]:
    """依据预先确定的 CL 模版构建聚合 JSON。"""
    # 针对本项目的现实构建方式（plain javac + JUnit Console）
    cl = {
        "task": "generate_tests",
        "language": "java",
        "framework": {
            "name": "junit-jupiter",
            "version": "5.10.2"
        },
        "build": {
            "tool": "plain-javac",
            "compile_cmd": "mkdir -p out/main && javac -d out/main $(find src -type f -name \"*.java\" ! -path \"*/test/*\")",
            "test_cmd": "mkdir -p out/test && javac -cp \"out/main:lib/junit-platform-console-standalone.jar\" -d out/test $(find src/test -type f -name \"*.java\") && java -jar lib/junit-platform-console-standalone.jar -cp \"out/main:out/test\" --scan-classpath",
            "java_version": "21",
            "dependencies": {
                "runtime": [],
                "test": [
                    {
                        "group": "org.junit.platform",
                        "artifact": "junit-platform-console-standalone",
                        "version": "1.10.2"
                    }
                ]
            },
            "stubs": []
        },
        "targets": [],
        "signals": {
            "failures": [],
            "coverage_gaps": [],
            "repro_cmd": ""
        },
        "constraints": {
            "no_io": True,
            "no_network": True,
            "deterministic": True,
            "isolation": "each_test_independent",
            "no_random_without_seed": True,
            "time_budget_ms": 5000
        },
        "preferences": {
            "mocks": "allow_minimal",
            "assertion_style": "native",
            "float_delta": 1e-9,
            "type_assertions": True,
            "naming": "test_<behavior>_<condition>",
            "test_style": ["unit", "parameterized"]
        },
        "coverage": {
            "types": ["statement", "branch", "exception", "boundary"],
            "branch_min": 90,
            "edge_values": [
                "0", "1", "-1", "Integer.MIN_VALUE", "Integer.MAX_VALUE",
                "Double.MIN_VALUE", "Double.MAX_VALUE", "very_small", "very_large"
            ]
        },
        "coverage_goals": {
            "statement_min": 90,
            "branch_min": 90,
            "must_hit_exceptions": []
        },
        "oracles": {
            "examples": [],
            "error_cases": [],
            "properties": []
        },
        "generation": {
            "num_tests": "auto",
            "package": "test",
            "test_class_suffix": "GeneratedTest",
            "path_hint": "src/test/java",
            "parameterized_threshold": 3
        },
        "guidance": "Generate tests to maximize branch coverage. Include edge cases (zero, negatives, max/min, large/small doubles). Use type assertions where result type varies. Use delta for floating assertions. Avoid I/O and nondeterminism. If external types are missing, create minimal stubs to compile.",
        "postchecks": [
            {"type": "compiles", "cmd": "javac -version"},
            {"type": "no_flaky_patterns"}
        ],
        "output": {
            "only_code": True,
            "group_by_target": True,
            "include_imports": True
        }
    }

    for t in targets:
        cl["targets"].append({
            "file": t["file"],
            "package": t["package"],
            "class": t["class"],
            "function": t["function"],
            "signature": t["signature"],
            "snippets": t["snippets"][:6],  # 保持精简
            "imports": t.get("imports", []),
            "collaborators": [],
            "semantics": {
                "preconditions": [],
                "postconditions": [],
                "properties": []
            }
        })

    return cl


def main() -> None:
    OUT_DIR.mkdir(parents=True, exist_ok=True)
    TARGETS_DIR.mkdir(parents=True, exist_ok=True)
    all_targets: List[Dict[str, Any]] = []

    if not SRC_DIR.exists():
        print(f"src 目录不存在：{SRC_DIR}")
        return

    for root, _, files in os.walk(SRC_DIR):
        # 跳过测试目录
        if "/test" in root.replace("\\", "/"):
            continue
        for fn in files:
            if not fn.endswith(".java"):
                continue
            p = pathlib.Path(root) / fn
            parsed = parse_java_file(p)
            for entry in parsed.get("types", []):
                all_targets.append(entry)

    cl_json = build_cl_prompt(all_targets)
    with OUT_FILE.open("w", encoding="utf-8") as f:
        json.dump(cl_json, f, ensure_ascii=False, indent=2)

    # 逐方法输出单文件 CL 提示
    for t in all_targets:
        single = build_cl_prompt([t])
        # 命名：<package>.<Class>.<method>.json
        pkg = (t.get("package") or "default").replace("/", ".")
        fname = f"{pkg}.{t.get('class')}.{t.get('function')}.json"
        # 防止奇异字符
        safe_fname = fname.replace(os.sep, ".")
        out_path = TARGETS_DIR / safe_fname
        with out_path.open("w", encoding="utf-8") as sf:
            json.dump(single, sf, ensure_ascii=False, indent=2)

    print(f"生成完成：{OUT_FILE}，以及 {TARGETS_DIR} 下的每方法 CL 提示词")


if __name__ == "__main__":
    main()


