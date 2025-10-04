#!/usr/bin/env python3
# coding: utf-8

"""
从 LLM 输出目录（如 out/sk-llmTest-origin）中批量提取 Java 代码，
使用 AST 校验后写入目标目录（按 package 建目录；若无 package，则直接写入目标根目录，
文件名取公共类或首个类名）。此举抹平源目录层级（不再保留 nl/cl/targets 等上层结构）。

用法：
  python3 op/extract_code_to_java.py --in-dir out/sk-llmTest-origin/cl --out-dir src/test/sk-llmTest/cl

依赖：
  pip install javalang
"""

import argparse
import os
import re
import pathlib
from typing import List, Optional, Tuple, NamedTuple

import javalang


CODE_FENCE_RE = re.compile(r"```([a-zA-Z0-9_-]+)?\n(.*?)```", re.DOTALL)
PACKAGE_RE = re.compile(r"\bpackage\s+([a-zA-Z0-9_.]+)\s*;")
FILENAME_META_RE = re.compile(r"^([A-Za-z0-9_.]+)\.([A-Za-z0-9_]+)\.([A-Za-z0-9_]+)\.out\.txt$")


class NameMeta(NamedTuple):
    pkg: Optional[str]
    cls: Optional[str]
    func: Optional[str]


def read_text(p: pathlib.Path) -> str:
    with p.open("r", encoding="utf-8") as f:
        return f.read()


def write_text(p: pathlib.Path, text: str) -> None:
    p.parent.mkdir(parents=True, exist_ok=True)
    with p.open("w", encoding="utf-8") as f:
        f.write(text)


def extract_code_blocks(text: str) -> List[Tuple[Optional[str], str]]:
    """返回 (lang, code) 列表，优先解析三引号代码块。若无，则尝试基于 package 片段回退。"""
    blocks: List[Tuple[Optional[str], str]] = []
    for m in CODE_FENCE_RE.finditer(text):
        lang = (m.group(1) or "").strip().lower() or None
        code = m.group(2)
        blocks.append((lang, code))

    if blocks:
        return blocks

    # fallback：尝试从包含 package 的整段文本中提取
    pkg_match = PACKAGE_RE.search(text)
    if pkg_match:
        # 从 package 行开始到文本末尾
        idx = pkg_match.start()
        code = text[idx:]
        return [(None, code)]

    # 再次 fallback：直接整段尝试
    return [(None, text)]


def try_parse_java(code: str):
    try:
        tree = javalang.parse.parse(code)
        return tree
    except Exception:
        return None


def choose_best_blocks(blocks: List[Tuple[Optional[str], str]]) -> List[str]:
    """选择可解析的代码块，优先 lang==java，其次其他；返回代码列表（可能多个）。"""
    # 先按语言分组
    java_first = sorted(blocks, key=lambda x: (0 if (x[0] == "java") else 1, -len(x[1])))
    chosen: List[str] = []
    for _, code in java_first:
        if try_parse_java(code):
            chosen.append(code)
    return chosen


def primary_type_name(tree) -> Optional[str]:
    if not tree or not getattr(tree, "types", None):
        return None
    # 优先选择 public 类型
    publics = []
    for t in tree.types:
        mods = getattr(t, "modifiers", set()) or set()
        if "public" in mods:
            publics.append(getattr(t, "name", None))
    if publics:
        return publics[0]
    # 其次选择第一个类型
    return getattr(tree.types[0], "name", None)


def package_name_from_code(code: str) -> Optional[str]:
    m = PACKAGE_RE.search(code)
    if not m:
        return None
    return m.group(1)


def parse_meta_from_filename(rel_in_file: pathlib.Path) -> NameMeta:
    name = rel_in_file.name
    m = FILENAME_META_RE.match(name)
    if not m:
        return NameMeta(None, None, None)
    pkg, cls, func = m.group(1), m.group(2), m.group(3)
    return NameMeta(pkg, cls, func)


def camel_case(name: str) -> str:
    parts = re.split(r"[^A-Za-z0-9]+", name)
    return "".join(p[:1].upper() + p[1:] for p in parts if p)


def derive_names(code: str, rel_in_file: pathlib.Path) -> Tuple[Optional[str], str]:
    meta = parse_meta_from_filename(rel_in_file)
    code_pkg = package_name_from_code(code)
    # 若代码包为 test/tests/generated 等，则忽略，优先用文件名中的包
    if code_pkg and code_pkg.lower() in {"test", "tests", "generated"}:
        code_pkg = None
    desired_pkg = meta.pkg or code_pkg

    # 目标类名：优先文件名元信息 <Class><Func>GeneratedTest
    base_cls = meta.cls or "LLMGenerated"
    base_func = meta.func or "Case"
    desired_class = f"{camel_case(base_cls)}{camel_case(base_func)}GeneratedTest"
    return desired_pkg, desired_class


def ensure_package_and_class(code: str, pkg: Optional[str], cls: str) -> str:
    # 1) 处理 package 行
    code_no_pkg = re.sub(r"^\s*package\s+[A-Za-z0-9_.]+\s*;\s*\n", "", code, count=1, flags=re.MULTILINE)
    if pkg:
        code_no_pkg = f"package {pkg};\n\n" + code_no_pkg

    # 2) 替换第一个 class 名称
    def repl(m):
        prefix = m.group(1) or ""
        return f"{prefix}class {cls}"

    code_out = re.sub(r"\b(public\s+)?class\s+[A-Za-z_][A-Za-z0-9_]*", repl, code_no_pkg, count=1)
    return code_out


def dest_path_for_names(src_root: pathlib.Path, pkg: Optional[str], cls: str) -> pathlib.Path:
    if pkg:
        pkg_dir = pathlib.Path(*pkg.split('.'))
        return src_root / pkg_dir / f"{cls}.java"
    return src_root / f"{cls}.java"


def process_one_file(in_dir: pathlib.Path, out_dir: pathlib.Path, path: pathlib.Path) -> List[pathlib.Path]:
    text = read_text(path)
    blocks = extract_code_blocks(text)
    codes = choose_best_blocks(blocks)
    written: List[pathlib.Path] = []
    if not codes:
        return written
    rel = path.relative_to(in_dir)
    for code in codes:
        desired_pkg, desired_cls = derive_names(code, rel)
        code_final = ensure_package_and_class(code, desired_pkg, desired_cls)
        dest = dest_path_for_names(out_dir, desired_pkg, desired_cls)
        if not dest:
            continue
        # 若已存在同名文件且内容不同，则追加数字后缀避免覆盖
        final_dest = dest
        suffix_i = 1
        while final_dest.exists():
            try:
                if read_text(final_dest) == code:
                    break
            except Exception:
                pass
            final_dest = dest.with_name(f"{dest.stem}_{suffix_i}{dest.suffix}")
            suffix_i += 1
        write_text(final_dest, code_final)
        written.append(final_dest)
    return written


def main():
    parser = argparse.ArgumentParser(description="Extract Java code from LLM outputs and write to src/llmTest/...")
    parser.add_argument("--in-dir", required=True, help="输入目录，如 out/sk-llmTest-origin")
    parser.add_argument("--out-dir", default="src/llmTest/sk-llmTest", help="输出根目录，默认 src/llmTest/sk-llmTest")
    args = parser.parse_args()

    in_dir = pathlib.Path(args.in_dir).resolve()
    out_dir = pathlib.Path(args.out_dir).resolve()

    if not in_dir.exists():
        raise SystemExit(f"输入目录不存在：{in_dir}")

    count = 0
    written_files: List[pathlib.Path] = []
    for p in in_dir.rglob("*.out.txt"):
        ws = process_one_file(in_dir, out_dir, p)
        if ws:
            count += 1
            written_files.extend(ws)

    print(f"完成：处理 {count} 个 .out.txt，写入 {len(written_files)} 个 .java 文件")
    # 可选：打印前若干条产物
    for f in written_files[:10]:
        print(f" -> {f}")


if __name__ == "__main__":
    main()


