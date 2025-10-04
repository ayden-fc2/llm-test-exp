#!/usr/bin/env python3
# coding: utf-8

"""
编译并运行指定目录下的测试代码（基于 javac + JUnit Console）。

示例：
  python3 op/run_tests.py \
    --tests-dir src/test \
    --main-src-dir src \
    --lib-jar lib/junit-platform-console-standalone.jar \
    --out-root out/run-default

说明：
 - 会编译 main 源码到 <out-root>/main（排除路径包含 /test/ 的文件）
 - 会编译 --tests-dir 下的测试源码到 <out-root>/test
 - 然后使用 JUnit Console 扫描 classpath 运行测试
 - 日志输出到 <out-root>/test-report.txt，XML 报告输出到 <out-root>/reports
"""

import argparse
import os
import subprocess
import sys
import pathlib
import re
from typing import List


def find_java_files(root: pathlib.Path, exclude_test_paths: bool = False) -> List[str]:
    files: List[str] = []
    for p in root.rglob('*.java'):
        rel = str(p)
        if exclude_test_paths and ('/test/' in rel.replace('\\', '/')):
            continue
        files.append(str(p))
    return files


def run(cmd: List[str], cwd: pathlib.Path, log_file: pathlib.Path) -> int:
    proc = subprocess.Popen(cmd, cwd=str(cwd), stdout=subprocess.PIPE, stderr=subprocess.STDOUT, text=True)
    out, _ = proc.communicate()
    log_file.parent.mkdir(parents=True, exist_ok=True)
    with log_file.open('a', encoding='utf-8') as f:
        f.write('$ ' + ' '.join(cmd) + '\n')
        f.write(out + '\n')
    return proc.returncode


def file_has_package(java_path: pathlib.Path) -> bool:
    try:
        with java_path.open('r', encoding='utf-8') as f:
            for i in range(50):
                line = f.readline()
                if not line:
                    break
                if re.match(r'^\s*package\s+[A-Za-z0-9_.]+\s*;\s*$', line):
                    return True
    except Exception:
        return False
    return False


def main():
    parser = argparse.ArgumentParser(description='Compile & run tests using javac + JUnit Console')
    parser.add_argument('--tests-dir', required=True, help='测试源码目录，例如 src/test 或 src/test/sk-llmTest/cl')
    parser.add_argument('--main-src-dir', default='src', help='主源码目录（用于编译被测代码），默认 src')
    parser.add_argument('--lib-jar', default='lib/junit-platform-console-standalone.jar', help='JUnit Console 独立 JAR 路径')
    parser.add_argument('--out-root', default='out/run', help='输出根目录，包含 main 与 test 编译产物和报告')
    # 覆盖率相关（自动检测 lib 下的 JaCoCo JAR；也允许显式指定）
    parser.add_argument('--jacoco-agent', default='lib/jacocoagent.jar', help='JaCoCo 代理 JAR 路径（可选，存在则启用）')
    parser.add_argument('--jacoco-cli', default='lib/jacococli.jar', help='JaCoCo CLI JAR 路径（可选，存在则生成报告）')
    parser.add_argument('--jacoco-includes', default='*', help='JaCoCo includes 过滤（例如 com.example.*，默认全量）')
    args = parser.parse_args()

    project_root = pathlib.Path(__file__).resolve().parents[1]
    tests_dir = (project_root / args.tests_dir).resolve()
    main_src_dir = (project_root / args.main_src_dir).resolve()
    lib_jar = (project_root / args.lib_jar).resolve()
    out_root = (project_root / args.out_root).resolve()

    out_main = out_root / 'main'
    out_test = out_root / 'test'
    out_reports = out_root / 'reports'
    report = out_root / 'test-report.txt'
    # 覆盖率产物
    jacoco_agent = (project_root / args.jacoco_agent).resolve()
    jacoco_cli = (project_root / args.jacoco_cli).resolve()
    jacoco_exec = out_root / 'jacoco.exec'
    jacoco_xml = out_root / 'jacoco.xml'
    jacoco_html = out_root / 'jacoco-html'
    if report.exists():
        report.unlink()

    if not tests_dir.exists():
        print(f'测试目录不存在: {tests_dir}', file=sys.stderr)
    if not main_src_dir.exists():
        print(f'主源码目录不存在: {main_src_dir}', file=sys.stderr)
    if not lib_jar.exists():
        print(f'缺少 JUnit Console JAR: {lib_jar}', file=sys.stderr)
        print('可执行: curl -L -o lib/junit-platform-console-standalone.jar \\\n  https://repo1.maven.org/maven2/org/junit/platform/junit-platform-console-standalone/1.10.2/junit-platform-console-standalone-1.10.2.jar')

    # 1) 编译主源码
    out_main.mkdir(parents=True, exist_ok=True)
    main_files = find_java_files(main_src_dir, exclude_test_paths=True) if main_src_dir.exists() else []
    main_ok = False
    if main_files:
        cmd = ['javac', '-d', str(out_main)] + main_files
        code = run(cmd, project_root, report)
        if code != 0:
            print(f'编译主源码失败（不中断），详见 {report}', file=sys.stderr)
        else:
            main_ok = True
            print(f'主源码编译完成: {len(main_files)} 个文件 -> {out_main}')
    else:
        print('未发现主源码 .java 文件（已跳过）')

    # 2) 编译测试源码
    out_test.mkdir(parents=True, exist_ok=True)
    test_files = find_java_files(tests_dir, exclude_test_paths=False) if tests_dir.exists() else []
    tests_ok = False
    compiled_count = 0
    if not test_files:
        print('未发现测试源码 .java 文件', file=sys.stderr)
    else:
        cp = f"{out_main}:{lib_jar}"
        pkg_files: List[str] = []
        nopkg_files: List[str] = []
        for tf in test_files:
            (pkg_files if file_has_package(pathlib.Path(tf)) else nopkg_files).append(tf)
        if pkg_files:
            code = run(['javac', '-cp', cp, '-d', str(out_test)] + pkg_files, project_root, report)
            if code != 0:
                print(f'批量编译（有包名）部分失败，尝试逐文件：{len(pkg_files)} 个', file=sys.stderr)
                for tf in pkg_files:
                    rc = run(['javac', '-cp', cp, '-d', str(out_test), tf], project_root, report)
                    if rc == 0:
                        compiled_count += 1
            else:
                compiled_count += len(pkg_files)
        for tf in nopkg_files:
            tf_path = pathlib.Path(tf)
            try:
                rel_dir = tf_path.parent.relative_to(tests_dir)
            except Exception:
                rel_dir = pathlib.Path('.')
            dest_dir = out_test / rel_dir
            dest_dir.mkdir(parents=True, exist_ok=True)
            rc = run(['javac', '-cp', cp, '-d', str(dest_dir), str(tf_path)], project_root, report)
            if rc == 0:
                compiled_count += 1
        if compiled_count > 0:
            tests_ok = True
            print(f'测试编译完成：成功 {compiled_count}/{len(test_files)} 个 -> {out_test}')
        else:
            print('测试编译全部失败', file=sys.stderr)

    # 3) 运行测试 + 输出 XML 报告（若存在 JaCoCo 则附加覆盖率采集）
    if lib_jar.exists() and main_ok and tests_ok:
        out_reports.mkdir(parents=True, exist_ok=True)
        cmd = ['java']
        # 挂载 JaCoCo 代理（若存在）
        if jacoco_agent.exists():
            agent_opts = f"destfile={jacoco_exec},append=false,includes={args.jacoco_includes}"
            cmd += [f'-javaagent:{jacoco_agent}={agent_opts}']
        # 运行 JUnit Console
        cmd += ['-jar', str(lib_jar), '-cp', f'{out_main}:{out_test}', '--scan-classpath', '--reports-dir', str(out_reports)]
        code = run(cmd, project_root, report)
        if code != 0:
            print(f'测试运行失败（不中断，退出码 {code}），详见 {report}', file=sys.stderr)
        else:
            print(f'测试运行完成，报告: {report}，XML: {out_reports}')
        # 4) 生成 JaCoCo 覆盖率报告（XML/HTML）
        if jacoco_agent.exists() and jacoco_cli.exists() and jacoco_exec.exists():
            jacoco_html.mkdir(parents=True, exist_ok=True)
            cmd_rep = [
                'java', '-jar', str(jacoco_cli), 'report', str(jacoco_exec),
                '--classfiles', str(out_main),
                '--sourcefiles', str(main_src_dir),
                '--xml', str(jacoco_xml),
                '--html', str(jacoco_html)
            ]
            code_rep = run(cmd_rep, project_root, report)
            if code_rep != 0:
                print(f'JaCoCo 报告生成失败（不中断），详见 {report}', file=sys.stderr)
            else:
                print(f'JaCoCo 报告已生成：XML={jacoco_xml}，HTML={jacoco_html}')
    else:
        print('跳过测试运行（缺少 JAR 或编译失败）')

    sys.exit(0)


if __name__ == '__main__':
    main()


