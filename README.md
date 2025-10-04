# 1. 项目、LLM选取

a. 项目选择
* 低依赖的简单的计算相关的java项目 - https://github.com/apainintheneck/simple-math-parser
* 包含复杂maven依赖和包导入的脚手架工程项目 - https://github.com/tuanzi-ne/Tuanzi

b. LLM选择

* Qwen3-Coder-480B-A35B-Instruct

# 2. 测试用例生成

a. prompt设计

* CL风格提示词
```
{
  "task": "generate_tests",
  "language": "java",
  "framework": { "name": "junit-jupiter", "version": "5.10.2" },
  "build": {
    "tool": "plain-javac",
    "compile_cmd": "mkdir -p out/main && javac -d out/main $(find src -type f -name \"*.java\" ! -path \"*/test/*\")",
    "test_cmd": "mkdir -p out/test && javac -cp \"out/main:lib/junit-platform-console-standalone.jar\" -d out/test $(find src/test -type f -name \"*.java\") && java -jar lib/junit-platform-console-standalone.jar -cp \"out/main:out/test\" --scan-classpath",
    "java_version": "21",
    "dependencies": {
      "runtime": [],
      "test": [ { "group": "org.junit.platform", "artifact": "junit-platform-console-standalone", "version": "1.10.2" } ]
    },
    "stubs": []
  },
  "targets": [
    {
      "file": "src/mathNode/Mult.java",
      "package": "mathNode",
      "class": "Mult",
      "function": "calculate",
      "signature": "public Number Mult.calculate()",
      "imports": ["import mathNode.Mult;"],
      "snippets": [
        "Number leftNum = getLeftNode().calculate();",
        "Number rightNum = getRightNode().calculate();"
      ],
      "collaborators": [],
      "semantics": { "preconditions": [], "postconditions": [], "properties": [] }
    }
  ],
  "signals": { "failures": [], "coverage_gaps": [], "repro_cmd": "" },
  "constraints": {
    "no_io": true,
    "no_network": true,
    "deterministic": true,
    "isolation": "each_test_independent",
    "no_random_without_seed": true,
    "time_budget_ms": 5000
  },
  "preferences": {
    "mocks": "allow_minimal",
    "assertion_style": "native",
    "float_delta": 1e-9,
    "type_assertions": true,
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
  "coverage_goals": { "statement_min": 90, "branch_min": 90, "must_hit_exceptions": [] },
  "oracles": { "examples": [], "error_cases": [], "properties": [] },
  "generation": {
    "num_tests": "auto", "package": "test", "test_class_suffix": "GeneratedTest", "path_hint": "src/test/java", "parameterized_threshold": 3
  },
  "guidance": "Generate tests to maximize branch coverage. Include edge cases (zero, negatives, max/min, large/small doubles). Use type assertions where result type varies. Use delta for floating assertions. Avoid I/O and nondeterminism. If external types are missing, create minimal stubs to compile.",
  "postchecks": [ { "type": "compiles", "cmd": "javac -version" }, { "type": "no_flaky_patterns" } ],
  "output": { "only_code": true, "group_by_target": true, "include_imports": true }
}
```
* NL风格提示词
```
请为如下目标批量生成高质量、可编译运行的单元测试用例（优先语句/分支/异常覆盖，并尽可能覆盖边界与特殊取值）：

一、项目构建与依赖
- 语言/版本：Java 21
- 测试框架与版本：JUnit 5.10+
- 构建与运行：可使用 junit-platform-console 或 IDE 运行（无需网络/I/O）
- 依赖（至少以下可解析层级）：
  - 测试依赖：org.junit.jupiter:junit-jupiter（或 console-standalone 打包）
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
```


b. 批量API调用
参考/op/batch_llm_generate.py，引入sk与hf两种模型API，cl与nl两种模式，对于被测目录下所有函数批量生成单元测试用例，结果存储进/out/

c. 代码转化
参考/op/extract_code_to_java.py，将指定目录下所有llm的输出转化为java文件，存储进src/llmTest/目录下

# 3. 生成结果
参考/op/run_tests.py，编译测试并运行后，统计编译通过率、通过率（即断言准确率）、分支覆盖率（TODO）、边界值覆盖率（TODO）

# 附录
1. 生成prompts
```
cd ./op
python ./gen_nl_prompts.py
python ./gen_cl_prompts.py
```

2. prompts生成测试用例代码
```
python batch_llm_generate.py --mode auto --provider sk
```

3. 测试用例代码解析到src
```
python extract_code_to_java.py --in-dir ../out/sk-llmTest-origin/cl/targets --out-dir ../src/test/sk-llmTest/cl

python extract_code_to_java.py --in-dir ../out/sk-llmTest-origin/nl --out-dir ../src/test/sk-llmTest/nl
```

4. 编译测试汇总
```
# 生成csv

cd ../
python3 op/run_tests.py \
  --tests-dir src/test/sk-llmTest/cl \
  --main-src-dir src \
  --lib-jar lib/junit-platform-console-standalone.jar \
  --out-root out/sk-llmOut/cl 

python3 op/summarize_results.py \
  --tests-dir src/test/sk-llmTest/cl \
  --out-root out/sk-llmOut/cl \
  --lib-jar lib/junit-platform-console-standalone.jar \
  --csv csv/sk-llmOut/cl/cl-summary.csv 

python3 op/run_tests.py \
  --tests-dir src/test/sk-llmTest/nl \
  --main-src-dir src \
  --lib-jar lib/junit-platform-console-standalone.jar \
  --out-root out/sk-llmOut/nl 

python3 op/summarize_results.py \
  --tests-dir src/test/sk-llmTest/nl \
  --out-root out/sk-llmOut/nl \
  --lib-jar lib/junit-platform-console-standalone.jar \
  --csv csv/sk-llmOut/nl/nl-summary.csv

# 数据可视化
python op/plot_results.py \
  --csv-a csv/sk-llmOut/cl/cl-summary.csv \
  --csv-b csv/sk-llmOut/nl/nl-summary.csv \
  --out-dir csv/sk-llmOut \
  --label-a CL --label-b NL

```