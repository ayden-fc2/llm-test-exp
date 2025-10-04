#!/usr/bin/env python3
# coding: utf-8

"""
汇总编译与测试结果，输出 CSV。

功能：
 - 遍历 tests-dir 下的所有 .java 测试文件，推断 FQN（package + class）
 - 逐文件尝试编译到临时目录，记录是否编译通过与首条编译报错（前 8 行）
 - 解析 JUnit Console 的 XML 报告（若无则尝试生成），统计每个 FQN 的总数、失败数、失败原因
 - 生成 csv/summary.csv（可自定义输出路径），一行对应一个“测试源文件”（用路径标识）

用法：
  python3 op/summarize_results.py \
    --tests-dir src/test \
    --out-root out/llmOut \
    --lib-jar lib/junit-platform-console-standalone.jar \
    --csv csv/summary.csv
"""

import argparse
import pathlib
import re
import subprocess
import sys
import xml.etree.ElementTree as ET
import csv
from typing import Dict, Tuple, List, Optional, Set


PKG_RE = re.compile(r"^\s*package\s+([A-Za-z0-9_.]+)\s*;\s*$")
CLASS_RE = re.compile(r"\bclass\s+([A-Za-z_][A-Za-z0-9_]*)")


def read_text(p: pathlib.Path) -> str:
    try:
        return p.read_text(encoding='utf-8', errors='ignore')
    except Exception:
        return ''


def parse_fqn(java_path: pathlib.Path) -> Tuple[str, str]:
    txt = read_text(java_path)
    pkg = None
    for line in txt.splitlines()[:100]:
        m = PKG_RE.match(line)
        if m:
            pkg = m.group(1)
            break
    cls = None
    m2 = CLASS_RE.search(txt)
    if m2:
        cls = m2.group(1)
    if not cls:
        cls = java_path.stem
    fqn = f"{pkg}.{cls}" if pkg else cls
    return fqn, cls


def run(cmd: List[str], cwd: pathlib.Path) -> Tuple[int, str]:
    proc = subprocess.Popen(cmd, cwd=str(cwd), stdout=subprocess.PIPE, stderr=subprocess.STDOUT, text=True)
    out, _ = proc.communicate()
    return proc.returncode, out or ''


def try_compile_each(tests: List[pathlib.Path], project_root: pathlib.Path, out_main: pathlib.Path, lib_jar: pathlib.Path, tmp_out: pathlib.Path) -> Dict[pathlib.Path, Tuple[bool, str]]:
    tmp_out.mkdir(parents=True, exist_ok=True)
    results: Dict[pathlib.Path, Tuple[bool, str]] = {}
    cp = f"{out_main}:{lib_jar}" if lib_jar.exists() else str(out_main)
    for jf in tests:
        rel_dir = jf.parent
        dest_dir = tmp_out / rel_dir.name
        dest_dir.mkdir(parents=True, exist_ok=True)
        code, log = run(['javac', '-cp', cp, '-d', str(dest_dir), str(jf.resolve())], project_root)
        ok = (code == 0)
        err_excerpt = ''
        if not ok:
            lines = [ln for ln in (log or '').splitlines() if ln.strip()]
            err_excerpt = '\n'.join(lines[:8])
        results[jf] = (ok, err_excerpt)
    return results


def ensure_reports(project_root: pathlib.Path, out_root: pathlib.Path, lib_jar: pathlib.Path) -> pathlib.Path:
    reports = out_root / 'reports'
    if reports.exists():
        return reports
    if not lib_jar.exists():
        return reports
    out_main = out_root / 'main'
    out_test = out_root / 'test'
    if not out_main.exists() or not out_test.exists():
        return reports
    reports.mkdir(parents=True, exist_ok=True)
    cmd = ['java', '-jar', str(lib_jar), '-cp', f'{out_main}:{out_test}', '--scan-classpath', '--reports-dir', str(reports)]
    run(cmd, project_root)
    return reports


# ---- JaCoCo XML 解析 ----
def parse_jacoco_counters(el: ET.Element) -> Dict[str, Tuple[int, int]]:
    counters: Dict[str, Tuple[int, int]] = {}
    for c in el.findall('.//counter'):
        typ = c.attrib.get('type')
        missed = int(c.attrib.get('missed', '0'))
        covered = int(c.attrib.get('covered', '0'))
        if typ:
            counters[typ.upper()] = (missed, covered)
    return counters


def load_jacoco(xml_path: pathlib.Path) -> Tuple[
    Dict[str, Dict[str, Tuple[int, int]]],
    Dict[Tuple[str, str], Dict[str, Tuple[int, int]]],
    Dict[str, str],
    Dict[Tuple[str, str], Dict[int, Tuple[int, int]]]
]:
    """
    返回 (class_counters, method_counters, class_to_sourcefile, sourcefile_lines)
    - class_counters: fqn -> { 'LINE': (missed, covered), 'BRANCH': ... }
    - method_counters: (fqn, method) -> counters
    - class_to_sourcefile: fqn -> sourcefilename（如 "MathTree.java"）
    - sourcefile_lines: (package, sourcefilename) -> { line -> (missed_instr, covered_instr) }
    说明：用于支持按源代码行判断某些“语义边界”（如 null 分支）的命中。
    """
    class_cov: Dict[str, Dict[str, Tuple[int, int]]] = {}
    method_cov: Dict[Tuple[str, str], Dict[str, Tuple[int, int]]] = {}
    class_src: Dict[str, str] = {}
    src_lines: Dict[Tuple[str, str], Dict[int, Tuple[int, int]]] = {}
    if not xml_path.exists():
        return class_cov, method_cov, class_src, src_lines
    try:
        tree = ET.parse(str(xml_path))
        root = tree.getroot()
    except Exception:
        return class_cov, method_cov, class_src, src_lines

    for pkg in root.findall('.//package'):
        pkg_attr = pkg.attrib.get('name', '')
        pkg_name = pkg_attr.replace('/', '.')
        # 记录 sourcefile 的每行覆盖
        for sf in pkg.findall('sourcefile'):
            sf_name = sf.attrib.get('name', '')
            key = (pkg_name, sf_name)
            line_map: Dict[int, Tuple[int, int]] = {}
            for ln in sf.findall('line'):
                try:
                    nr = int(ln.attrib.get('nr', '0'))
                    mi = int(ln.attrib.get('mi', '0'))
                    ci = int(ln.attrib.get('ci', '0'))
                    line_map[nr] = (mi, ci)
                except Exception:
                    continue
            if line_map:
                src_lines[key] = line_map
        for cls in pkg.findall('class'):
            cls_name = cls.attrib.get('name', '').replace('/', '.')
            if '.' in cls_name:
                fqn = cls_name
            else:
                fqn = f"{pkg_name}.{cls_name}" if pkg_name else cls_name
            srcfile = cls.attrib.get('sourcefilename') or ''
            if srcfile:
                class_src[fqn] = srcfile
            class_cov[fqn] = parse_jacoco_counters(cls)
            for m in cls.findall('method'):
                mname = m.attrib.get('name', '')
                if not mname:
                    continue
                method_cov[(fqn, mname)] = parse_jacoco_counters(m)
    return class_cov, method_cov, class_src, src_lines


def cov_rate(counters: Dict[str, Tuple[int, int]], key: str) -> float:
    missed, covered = counters.get(key, (0, 0))
    total = missed + covered
    if total == 0:
        return 0.0
    return round(covered * 100.0 / total, 2)


# ---- 测试->被测类/方法 映射 ----
TEST_SUFFIXES = ['GeneratedTest', 'Test']


def strip_test_suffix(name: str) -> str:
    for suf in TEST_SUFFIXES:
        if name.endswith(suf):
            return name[: -len(suf)]
    return name


def split_class_method(base: str) -> Tuple[str, Optional[str]]:
    """将如 AddCalculate -> (Add, calculate)。若无法拆分，返回 (base, None)。"""
    if not base:
        return base, None
    idx = None
    for i in range(len(base) - 1, -1, -1):
        ch = base[i]
        if ch.isupper() and i > 0:
            idx = i
            break
    if idx is None:
        return base, None
    cls = base[:idx]
    mth_cap = base[idx:]
    if not cls:
        return base, None
    method = mth_cap[0].lower() + mth_cap[1:]
    return cls, method


def guess_source_fqn(jf: pathlib.Path, tests_root: pathlib.Path) -> Tuple[Optional[str], Optional[str], Optional[str]]:
    """根据测试文件名与路径推测被测 (package, class, method)。"""
    _, cls_name = parse_fqn(jf)
    base = strip_test_suffix(cls_name)
    src_cls, src_method = split_class_method(base)
    try:
        rel = jf.relative_to(tests_root)
        parts = rel.parts
        pkg = None
        if len(parts) > 1:
            pkg_path = parts[:-1]
            norm = [p for p in pkg_path if re.match(r'^[A-Za-z_][A-Za-z0-9_]*$', p)]
            if norm:
                pkg = '.'.join(norm)
        return pkg, src_cls, src_method
    except Exception:
        return None, src_cls, src_method


def resolve_target_from_base(base: str, pkg_guess: Optional[str], class_cov: Dict[str, Dict[str, Tuple[int, int]]]) -> Tuple[Optional[str], Optional[str]]:
    """
    根据 base（去除测试后缀的类名，如 MathTreeToString），结合 JaCoCo 的 class_cov，
    穷举所有切分点，优先选择在 class_cov 中已存在的类名作为被测类；余下部分作为方法名（首字母小写）。
    返回 (target_fqn, method_name)。若找不到匹配类，仅返回 (None, None) 以便回退原有启发式。
    """
    if not base:
        return None, None
    # 收集候选 FQN
    def build_fqn(simple: str) -> str:
        return f"{pkg_guess}.{simple}" if pkg_guess else simple
    best_cls: Optional[str] = None
    best_method: Optional[str] = None
    for i in range(1, len(base)):
        cls = base[:i]
        tail = base[i:]
        fqn = build_fqn(cls)
        if any(k.endswith('.' + cls) or k == cls or k == fqn for k in class_cov.keys()):
            best_cls = cls
            best_method = (tail[0].lower() + tail[1:]) if tail else None
            break
    if best_cls:
        return build_fqn(best_cls), best_method
    return None, None


# ---- 自适应边界值集合判定与命中计算 ----
def load_source_for_class(project_root: pathlib.Path, main_src_dir: pathlib.Path, pkg: Optional[str], cls: Optional[str]) -> Tuple[Optional[pathlib.Path], List[str]]:
    """
    依据包名与类名定位 Java 源文件并读取内容。失败时返回 (None, []).
    """
    if not cls:
        return None, []
    if pkg:
        rel = pathlib.Path(*(pkg.split('.'))) / f"{cls}.java"
    else:
        rel = pathlib.Path(f"{cls}.java")
    p = (project_root / main_src_dir / rel).resolve()
    if not p.exists():
        return None, []
    try:
        txt = p.read_text(encoding='utf-8', errors='ignore')
        return p, txt.splitlines()
    except Exception:
        return None, []


def extract_method_block(lines: List[str], method: Optional[str]) -> Tuple[int, int]:
    """
    简单基于大括号计数抽取方法体范围，返回 (start_line_idx, end_line_idx)，找不到则 (-1, -1)。
    仅用于启发式分析边界适用性。
    """
    if not method:
        return -1, -1
    sig_re = re.compile(rf"\b(public|protected|private|static|final|synchronized|native|abstract|\s)+[\w\<\>\[\]]+\s+{re.escape(method)}\s*\(")
    # 容错：如果上面的正则过严，退化为方法名+括号匹配
    loose_re = re.compile(rf"\b{re.escape(method)}\s*\(")
    start = -1
    brace = 0
    for i, line in enumerate(lines):
        if start == -1 and (sig_re.search(line) or loose_re.search(line)):
            # 向后找到第一个 '{'
            # 可能出现在本行或下一行
            j = i
            found = False
            while j < len(lines) and j <= i + 5:
                if '{' in lines[j]:
                    start = j
                    brace = lines[j].count('{') - lines[j].count('}')
                    found = True
                    break
                j += 1
            if not found:
                return -1, -1
            # 继续向后配对大括号
            for k in range(j + 1, len(lines)):
                brace += lines[k].count('{')
                brace -= lines[k].count('}')
                if brace <= 0:
                    return j, k
            return j, len(lines) - 1
    return -1, -1


def determine_applicable_boundaries(method_sig_line: str, method_body: str) -> Dict[str, re.Pattern]:
    """
    规则（简化版）：
    - 启用 null_ref：方法体包含 '== null' 或 '!= null'
    - 启用 string_empty：签名含 'String' 或方法名为 'toString' 或方法体含 '""'
    - 启用 integer 集：方法体包含算术/比较或出现 int/long/Integer/Long/Number
    - 启用 float 集：方法体提及 double/float/Double/Float
    返回用于“测试源码静态命中”的正则集合。注意：null_ref 的命中优先用行覆盖判断，不依赖测试源码出现 'null' 字面量。
    """
    labels: Dict[str, re.Pattern] = {}
    body = method_body or ''
    sig = method_sig_line or ''
    # null 引用
    if ('== null' in body) or ('!= null' in body):
        labels['null_ref'] = re.compile(r'null')
    # 字符串空
    method_name = 'toString' if 'toString' in sig else ''
    if ('String' in sig) or (method_name == 'toString') or ('""' in body):
        labels['string_empty'] = re.compile(r'""')
    # 整数类
    if any(tok in body for tok in ['+', '-', '*', '/', '%', '>', '<', '>=', '<=']) or any(tok in sig for tok in ['int', 'long', 'Integer', 'Long', 'Number']):
        labels['int_zero'] = re.compile(r'(?<!\\d)0(?!\\d)')
        labels['int_one'] = re.compile(r'(?<!\\d)1(?!\\d)')
        labels['int_minus_one'] = re.compile(r'(?<!\\d)-1(?!\\d)')
        labels['int_min'] = re.compile(r'Integer\\.MIN_VALUE')
        labels['int_max'] = re.compile(r'Integer\\.MAX_VALUE')
    # 浮点类：签名/方法体涉及浮点，或返回 Number（常为 Double 实现），或方法体出现小数字面量
    has_decimal_literal = bool(re.search(r'\d+\.\d+', body))
    if (
        any(tok in body for tok in ['double', 'float', 'Double', 'Float']) or
        any(tok in sig for tok in ['double', 'float', 'Double', 'Float', 'Number']) or
        has_decimal_literal
    ):
        labels['float_zero'] = re.compile(r'(?<!\d)0\.0(?!\d)')
        labels['float_neg_zero'] = re.compile(r'(?<!\d)-0\.0(?!\d)')
        labels['double_min'] = re.compile(r'Double\.MIN_VALUE')
        labels['double_max'] = re.compile(r'Double\.MAX_VALUE')
        labels['float_pos_inf'] = re.compile(r'Double\.POSITIVE_INFINITY')
        labels['float_neg_inf'] = re.compile(r'Double\.NEGATIVE_INFINITY')
        labels['float_nan'] = re.compile(r'Double\.NaN')
    return labels


def read_method_signature_line(lines: List[str], method: Optional[str]) -> str:
    if not method:
        return ''
    pat = re.compile(rf"\b(public|protected|private|static|final|synchronized|native|abstract|\s)+[\w\<\>\[\]]+\s+{re.escape(method)}\s*\(")
    for line in lines[:200]:
        if pat.search(line):
            return line
    # 宽松：直接找方法名
    loose = re.compile(rf"\b{re.escape(method)}\s*\(")
    for line in lines[:200]:
        if loose.search(line):
            return line
    return ''


def find_null_branch_rep_line_idx(lines: List[str], body_start: int, body_end: int) -> int:
    """
    在方法体内启发式查找匹配 if (... null ...) { return ... } 的首个 return 所在行索引。
    若找不到返回 -1。
    """
    if body_start < 0 or body_end < 0 or body_start >= len(lines) or body_end >= len(lines):
        return -1
    null_if = re.compile(r'if\s*\(.*null.*\)')
    for i in range(body_start, body_end + 1):
        if null_if.search(lines[i]):
            # 向下搜索到对应块内的 return 行
            brace = lines[i].count('{') - lines[i].count('}')
            for j in range(i + 1, body_end + 1):
                brace += lines[j].count('{')
                brace -= lines[j].count('}')
                if re.search(r'\breturn\b', lines[j]):
                    return j
                if brace < 0:
                    break
    return -1


def is_line_covered(pkg: Optional[str], fqn: Optional[str], line_idx_0base: int, class_to_sourcefile: Dict[str, str], sourcefile_lines: Dict[Tuple[str, str], Dict[int, Tuple[int, int]]]) -> bool:
    """
    使用 JaCoCo XML 的 sourcefile/line 信息判断某一源文件行是否被覆盖（ci>0）。
    需要：包名、类 FQN、0-based 行索引。
    """
    if not fqn:
        return False
    srcfile = class_to_sourcefile.get(fqn)
    if not srcfile:
        return False
    pkg_name = pkg or '.'.join(fqn.split('.')[:-1])
    key = (pkg_name, srcfile)
    lines_map = sourcefile_lines.get(key)
    if not lines_map:
        return False
    # JaCoCo 行号从 1 开始
    mi_ci = lines_map.get(line_idx_0base + 1)
    if not mi_ci:
        return False
    mi, ci = mi_ci
    return ci > 0
def localname(tag: str) -> str:
    """Strip XML namespace, return local tag name."""
    if '}' in tag:
        return tag.split('}', 1)[1]
    return tag


def parse_reports(reports_dir: pathlib.Path) -> Dict[str, Dict[str, List[Tuple[str, str]]]]:
    results: Dict[str, Dict[str, List[Tuple[str, str]]]] = {}
    if not reports_dir.exists():
        return results
    for xmlp in reports_dir.rglob('*.xml'):
        try:
            tree = ET.parse(str(xmlp))
            root = tree.getroot()
        except Exception:
            continue
        # 逐 testcase 收集
        for el in root.iter():
            if localname(el.tag) != 'testcase':
                continue
            classname = el.attrib.get('classname') or el.attrib.get('class') or ''
            name = el.attrib.get('name') or ''
            f = results.setdefault(classname, {'total': 0, 'fails': []})
            f['total'] = f.get('total', 0) + 1
            status = (el.attrib.get('status') or '').lower()
            failed = (status == 'failed')
            fail_msg = ''
            # 查找 failure/error 子节点
            for ch in el:
                ln = localname(ch.tag)
                if ln in ('failure', 'error'):
                    failed = True
                    fail_msg = (ch.attrib.get('message') or '').strip() or (ch.text or '').strip()
                    break
            if failed:
                f['fails'].append((name, fail_msg))
        # 补充：若没有 testcase 失败节点，但 testsuite 上标了失败数，可作为兜底
        if not any(localname(ch.tag) == 'testcase' for ch in root.iter()):
            # 少见情况，忽略
            pass
    return results


# ---- 边界值覆盖（基于测试源码的静态匹配）----
def boundary_coverage_for_test(java_path: pathlib.Path, label_patterns: Dict[str, re.Pattern]) -> Tuple[int, int, Optional[float], List[str]]:
    """
    - 仅针对“适用集合”统计命中（总数为 label_patterns 的大小）。
    - 当 total==0 时返回 rate=None（CSV 可写为 N/A）。
    - 静态匹配：在测试源码中搜索字面量（如 "", 0, -1, Integer.MAX_VALUE 等）。
    """
    txt = read_text(java_path)
    hit_labels: Set[str] = set()
    for label, pat in label_patterns.items():
        if pat.search(txt):
            hit_labels.add(label)
    total = len(label_patterns)
    hits = len(hit_labels)
    rate = (round(hits * 100.0 / total, 2) if total > 0 else None)
    return hits, total, rate, sorted(hit_labels)


def main():
    parser = argparse.ArgumentParser(description='Summarize compile & test results into CSV (per source file)')
    parser.add_argument('--tests-dir', required=True)
    parser.add_argument('--out-root', required=True)
    parser.add_argument('--lib-jar', default='lib/junit-platform-console-standalone.jar')
    parser.add_argument('--csv', default='csv/summary.csv')
    parser.add_argument('--main-src-dir', default='src', help='主源码目录，默认 src，用于裁剪边界集合与定位代表行')
    args = parser.parse_args()

    project_root = pathlib.Path(__file__).resolve().parents[1]
    tests_dir = (project_root / args.tests_dir).resolve()
    out_root = (project_root / args.out_root).resolve()
    lib_jar = (project_root / args.lib_jar).resolve()
    main_src_dir = (project_root / args.main_src_dir).resolve()

    out_main = out_root / 'main'

    test_java_files = [p for p in tests_dir.rglob('*.java')]

    compile_results = try_compile_each(test_java_files, project_root, out_main, lib_jar, out_root / 'tmp-compile-check')

    reports_dir = ensure_reports(project_root, out_root, lib_jar)
    test_results = parse_reports(reports_dir)

    # 载入 JaCoCo 覆盖率
    jacoco_xml = (pathlib.Path(args.jacoco_xml).resolve() if getattr(args, 'jacoco_xml', None) else (out_root / 'jacoco.xml'))
    class_cov, method_cov, class_to_srcfile, srcfile_lines = load_jacoco(jacoco_xml)

    csv_path = (project_root / args.csv).resolve()
    csv_path.parent.mkdir(parents=True, exist_ok=True)
    with csv_path.open('w', encoding='utf-8', newline='') as f:
        w = csv.writer(f)
        w.writerow([
            'test_source_path', 'fqn', 'compiled', 'compile_error_excerpt',
            'tests_total', 'tests_failed', 'pass_rate', 'fail_reasons',
            'line_coverage_pct', 'branch_coverage_pct',
            'boundary_rate_pct', 'boundary_hits', 'boundary_total', 'boundary_labels'
        ])
        for jf in test_java_files:
            fqn, _ = parse_fqn(jf)
            compiled, err_excerpt = compile_results.get(jf, (False, 'N/A'))
            tr = test_results.get(fqn, {'total': 0, 'fails': []})
            total = tr.get('total', 0)
            fails = tr.get('fails', [])
            num_failed = len(fails)
            pass_rate = round((total - num_failed) * 100.0 / total, 2) if total > 0 else 0.0
            reasons = '; '.join([f"{m}:{(r or 'failed')}" for m, r in fails])

            # 编译失败：不计覆盖，直接写入 N/A（并跳过调试输出与边界覆盖）
            if not compiled:
                w.writerow([
                    str(jf.relative_to(project_root)),
                    fqn,
                    'fail',
                    err_excerpt,
                    total,
                    num_failed,
                    pass_rate,
                    reasons,
                    'N/A',
                    'N/A',
                    'N/A',
                    0,
                    0,
                    ''
                ])
                continue

            # 推测被测类/方法并匹配 JaCoCo 覆盖率（仅对编译通过的测试进行）
            pkg_guess, src_cls, src_method = guess_source_fqn(jf, tests_dir)
            target_fqn = None
            # 优先：基于现有 JaCoCo 类集合进行“类名-方法名”的自动切分解析
            target_fqn_try, method_try = resolve_target_from_base(strip_test_suffix(parse_fqn(jf)[1]), pkg_guess, class_cov)
            if target_fqn_try:
                target_fqn = target_fqn_try
                if not src_method:
                    src_method = method_try
            elif src_cls:
                if pkg_guess:
                    target_fqn = f"{pkg_guess}.{src_cls}"
                else:
                    tfqn, _ = parse_fqn(jf)
                    pkg_from_test = '.'.join(tfqn.split('.')[:-1]) if '.' in tfqn else None
                    target_fqn = f"{pkg_from_test}.{src_cls}" if pkg_from_test else src_cls

            line_pct = 0.0
            branch_pct = 0.0
            cov_source = 'none'
            used_counters = None
            if target_fqn:
                if src_method and (target_fqn, src_method) in method_cov:
                    counters = method_cov[(target_fqn, src_method)]
                    used_counters = counters
                    cov_source = 'method'
                    line_pct = cov_rate(counters, 'LINE')
                    _bm, _bc = counters.get('BRANCH', (0, 0))
                    branch_pct = ('N/A' if (_bm + _bc) == 0 else cov_rate(counters, 'BRANCH'))
                elif target_fqn in class_cov:
                    counters = class_cov[target_fqn]
                    used_counters = counters
                    cov_source = 'class'
                    line_pct = cov_rate(counters, 'LINE')
                    _bm, _bc = counters.get('BRANCH', (0, 0))
                    branch_pct = ('N/A' if (_bm + _bc) == 0 else cov_rate(counters, 'BRANCH'))

            # 调试：当 line 覆盖率为 0 时，打印匹配到的方法与源文件，便于排障
            if target_fqn and (line_pct == 0.0):
                srcfile = class_to_srcfile.get(target_fqn)
                lm, lc = (0, 0)
                if isinstance(used_counters, dict):
                    lm, lc = used_counters.get('LINE', (0, 0))
                try:
                    test_rel = str(jf.relative_to(project_root))
                except Exception:
                    test_rel = str(jf)
                print(f"[COV-DEBUG] test={test_rel} target_fqn={target_fqn} method={src_method or 'N/A'} srcfile={srcfile or 'N/A'} cov_source={cov_source} line_missed={lm} line_covered={lc}")

            # 边界值覆盖（自适应）
            # 基于源码签名与方法体裁剪适用集合
            pkg_name = pkg_guess or ('.'.join((target_fqn or fqn).split('.')[:-1]) if '.' in (target_fqn or fqn) else None)
            src_file, src_lines = load_source_for_class(project_root, main_src_dir, pkg_name, (target_fqn.split('.')[-1] if target_fqn else None))
            method_sig = read_method_signature_line(src_lines, src_method) if src_lines else ''
            bs, be = extract_method_block(src_lines, src_method) if src_lines else (-1, -1)
            body_text = '\n'.join(src_lines[bs:be+1]) if (src_lines and bs >= 0 and be >= 0) else ''
            label_patterns = determine_applicable_boundaries(method_sig, body_text)
            b_hits, b_total, b_rate, b_labels = boundary_coverage_for_test(jf, label_patterns)

            # 对 null_ref：若存在该标签且未静态命中，则用 JaCoCo 行覆盖辅助判断
            if 'null_ref' in label_patterns and ('null_ref' not in b_labels) and src_lines and bs >= 0:
                rep_line_idx = find_null_branch_rep_line_idx(src_lines, bs, be)
                if rep_line_idx >= 0 and is_line_covered(pkg_name, target_fqn, rep_line_idx, class_to_srcfile, srcfile_lines):
                    b_hits += 1
                    b_labels.append('null_ref')
                    # 重新计算 rate
                    b_rate = (round(b_hits * 100.0 / b_total, 2) if b_total > 0 else None)

            w.writerow([
                str(jf.relative_to(project_root)),
                fqn,
                'pass' if compiled else 'fail',
                err_excerpt,
                total,
                num_failed,
                pass_rate,
                reasons,
                line_pct,
                branch_pct,
                (b_rate if b_rate is not None else 'N/A'),
                b_hits,
                b_total,
                '|'.join(b_labels)
            ])

    print(f'已生成汇总：{csv_path}')


if __name__ == '__main__':
    main()


