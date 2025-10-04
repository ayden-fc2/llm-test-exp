#!/usr/bin/env python3
# coding: utf-8

"""
读取两份 CSV（由 summarize_results.py 生成），产出图表：
1) 按 FQN 的编译是否通过（柱状图，0/1）
2) 按 FQN 的测试通过率（柱状图）
3) 按 FQN 的测试用例数量 tests_total（柱状图）
4) 单 CSV 的编译通过率饼图（pass/fail 按行计数）
5) 单 CSV 的测试通过率饼图（按 tests_total 加权汇总）

用法：
  python3 op/plot_results.py \
    --csv-a csv/sk-llmOut/cl/summary.csv \
    --csv-b csv/sk-llmOut/nl/summary.csv \
    --out-dir out/charts \
    --label-a CL --label-b NL

依赖：
  pip install matplotlib
"""

import argparse
import csv
import pathlib
from typing import List, Dict, Any, Tuple

import matplotlib
matplotlib.use('Agg')  # 非交互环境
import matplotlib.pyplot as plt
import numpy as np


def read_csv_rows(csv_path: pathlib.Path) -> List[Dict[str, Any]]:
    rows: List[Dict[str, Any]] = []
    with csv_path.open('r', encoding='utf-8') as f:
        reader = csv.DictReader(f)
        for r in reader:
            rows.append(r)
    return rows


def to_float(val: Any, default: float = 0.0) -> float:
    try:
        return float(val)
    except Exception:
        return default


def to_int(val: Any, default: int = 0) -> int:
    try:
        return int(float(val))
    except Exception:
        return default


def ensure_out_dir(p: pathlib.Path) -> None:
    p.mkdir(parents=True, exist_ok=True)


def fig_size_for(n: int, base_w: float = 0.35, min_w: float = 12.0, h: float = 6.0) -> Tuple[float, float]:
    return (max(min_w, base_w * n), h)


def plot_bar(xs: List[str], ys: List[float], title: str, ylabel: str, out_path: pathlib.Path, rotate: int = 90) -> None:
    plt.figure(figsize=fig_size_for(len(xs)))
    plt.bar(range(len(xs)), ys)
    plt.xticks(range(len(xs)), xs, rotation=rotate, fontsize=8)
    plt.title(title)
    plt.ylabel(ylabel)
    plt.tight_layout()
    plt.savefig(out_path, dpi=200)
    plt.close()


def plot_pie(labels: List[str], sizes: List[float], title: str, out_path: pathlib.Path) -> None:
    plt.figure(figsize=(6, 6))
    plt.pie(sizes, labels=labels, autopct='%1.1f%%', startangle=140)
    plt.axis('equal')
    plt.title(title)
    plt.tight_layout()
    plt.savefig(out_path, dpi=200)
    plt.close()


def make_charts_for(csv_path: pathlib.Path, out_dir: pathlib.Path, tag: str) -> None:
    rows = read_csv_rows(csv_path)
    # 以 FQN 为横轴；如有重复 FQN，可保留第一条或合并，这里保留第一条
    seen: set = set()
    fqn_list: List[str] = []
    compiled_val: List[int] = []
    pass_rates: List[float] = []
    totals: List[int] = []
    line_covs: List[float] = []
    branch_covs: List[float] = []

    total_rows = 0
    compiled_pass = 0

    agg_total = 0
    agg_failed = 0

    for r in rows:
        fqn = r.get('fqn', '').strip()
        if not fqn:
            continue
        # 汇总统计
        total_rows += 1
        if r.get('compiled', '').strip().lower() == 'pass':
            compiled_pass += 1

        t_total = to_int(r.get('tests_total', 0))
        t_failed = to_int(r.get('tests_failed', 0))
        agg_total += t_total
        agg_failed += t_failed

        # 横轴按 FQN，避免重复
        if fqn in seen:
            continue
        seen.add(fqn)
        fqn_list.append(fqn)
        compiled_val.append(1 if r.get('compiled', '').strip().lower() == 'pass' else 0)
        pass_rates.append(to_float(r.get('pass_rate', 0.0)))
        totals.append(t_total)
        line_covs.append(to_float(r.get('line_coverage_pct', 0.0)))
        branch_covs.append(to_float(r.get('branch_coverage_pct', 0.0)))

    # 图表输出路径
    out_dir_tag = out_dir / tag
    ensure_out_dir(out_dir_tag)

    # 柱状：编译是否通过（0/1）
    plot_bar(fqn_list, compiled_val,
             title=f'Compile Pass (0/1) per FQN [{tag}]',
             ylabel='pass=1, fail=0',
             out_path=out_dir_tag / f'bar_compile_{tag}.png')

    # 柱状：测试通过率（%）
    plot_bar(fqn_list, pass_rates,
             title=f'Test Pass Rate (%) per FQN [{tag}]',
             ylabel='pass_rate[%]',
             out_path=out_dir_tag / f'bar_passrate_{tag}.png')

    # 柱状：测试用例数量
    plot_bar(fqn_list, [float(x) for x in totals],
             title=f'Generated Test Count per FQN [{tag}]',
             ylabel='tests_total',
             out_path=out_dir_tag / f'bar_tests_total_{tag}.png')

    # 柱状：行覆盖率（%）
    plot_bar(fqn_list, line_covs,
             title=f'Line Coverage (%) per FQN [{tag}]',
             ylabel='line_coverage[%]',
             out_path=out_dir_tag / f'bar_linecov_{tag}.png')

    # 柱状：分支覆盖率（%）
    plot_bar(fqn_list, branch_covs,
             title=f'Branch Coverage (%) per FQN [{tag}]',
             ylabel='branch_coverage[%]',
             out_path=out_dir_tag / f'bar_branchcov_{tag}.png')

    # 饼图：编译通过率（按行，不加权）
    compile_fail = total_rows - compiled_pass
    plot_pie(['compile pass', 'compile fail'],
             [compiled_pass, compile_fail],
             title=f'Compile Pass Ratio [{tag}]',
             out_path=out_dir_tag / f'pie_compile_{tag}.png')

    # 饼图：测试通过率（按 tests_total 加权）
    pass_cnt = max(0, agg_total - agg_failed)
    fail_cnt = agg_failed
    plot_pie(['tests pass', 'tests fail'],
             [pass_cnt, fail_cnt],
             title=f'Test Pass Ratio (weighted by tests_total) [{tag}]',
             out_path=out_dir_tag / f'pie_tests_{tag}.png')


def first_by_fqn(rows: List[Dict[str, Any]]) -> Dict[str, Dict[str, Any]]:
    seen: Dict[str, Dict[str, Any]] = {}
    for r in rows:
        fqn = r.get('fqn', '').strip()
        if not fqn or fqn in seen:
            continue
        seen[fqn] = r
    return seen


def make_combined_charts(csv_a: pathlib.Path, csv_b: pathlib.Path, out_dir: pathlib.Path, label_a: str, label_b: str) -> None:
    rows_a = read_csv_rows(csv_a)
    rows_b = read_csv_rows(csv_b)
    map_a = first_by_fqn(rows_a)
    map_b = first_by_fqn(rows_b)

    # 取共同 FQN 的有序列表（优先按 A 的顺序，若需要也可用并集）
    fqns = [f for f in map_a.keys() if f in map_b]
    if not fqns:
        # 退化：使用并集并排序
        fqns = sorted(set(map_a.keys()).union(map_b.keys()))

    comp_a = [(1 if map_a.get(f, {}).get('compiled', '').strip().lower() == 'pass' else 0) for f in fqns]
    comp_b = [(1 if map_b.get(f, {}).get('compiled', '').strip().lower() == 'pass' else 0) for f in fqns]
    rate_a = [to_float(map_a.get(f, {}).get('pass_rate', 0.0)) for f in fqns]
    rate_b = [to_float(map_b.get(f, {}).get('pass_rate', 0.0)) for f in fqns]
    totl_a = [float(to_int(map_a.get(f, {}).get('tests_total', 0))) for f in fqns]
    totl_b = [float(to_int(map_b.get(f, {}).get('tests_total', 0))) for f in fqns]
    line_a = [to_float(map_a.get(f, {}).get('line_coverage_pct', 0.0)) for f in fqns]
    line_b = [to_float(map_b.get(f, {}).get('line_coverage_pct', 0.0)) for f in fqns]
    branch_a = [to_float(map_a.get(f, {}).get('branch_coverage_pct', 0.0)) for f in fqns]
    branch_b = [to_float(map_b.get(f, {}).get('branch_coverage_pct', 0.0)) for f in fqns]

    # 绘制并列柱状图
    def plot_dual(xs_fqn: List[str], ya: List[float], yb: List[float], title: str, ylabel: str, filename: str):
        x = np.arange(len(xs_fqn))
        width = 0.4
        plt.figure(figsize=fig_size_for(len(xs_fqn)))
        plt.bar(x - width/2, ya, width, label=label_a, color='tab:blue')
        plt.bar(x + width/2, yb, width, label=label_b, color='tab:red')
        plt.xticks(x, xs_fqn, rotation=90, fontsize=8)
        plt.title(title)
        plt.ylabel(ylabel)
        plt.legend()
        plt.tight_layout()
        plt.savefig(out_dir / filename, dpi=200)
        plt.close()

    out_dir.mkdir(parents=True, exist_ok=True)
    plot_dual(fqns, comp_a, comp_b, f'Compile Pass (0/1) per FQN [{label_a} vs {label_b}]', 'pass=1, fail=0', f'combined_bar_compile_{label_a}_vs_{label_b}.png')
    plot_dual(fqns, rate_a, rate_b, f'Test Pass Rate (%) per FQN [{label_a} vs {label_b}]', 'pass_rate[%]', f'combined_bar_passrate_{label_a}_vs_{label_b}.png')
    plot_dual(fqns, totl_a, totl_b, f'Generated Test Count per FQN [{label_a} vs {label_b}]', 'tests_total', f'combined_bar_tests_total_{label_a}_vs_{label_b}.png')
    plot_dual(fqns, line_a, line_b, f'Line Coverage (%) per FQN [{label_a} vs {label_b}]', 'line_coverage[%]', f'combined_bar_linecov_{label_a}_vs_{label_b}.png')
    plot_dual(fqns, branch_a, branch_b, f'Branch Coverage (%) per FQN [{label_a} vs {label_b}]', 'branch_coverage[%]', f'combined_bar_branchcov_{label_a}_vs_{label_b}.png')


def main():
    ap = argparse.ArgumentParser(description='Plot charts from two CSV summaries')
    ap.add_argument('--csv-a', required=True)
    ap.add_argument('--csv-b', required=True)
    ap.add_argument('--out-dir', default='out/charts')
    ap.add_argument('--label-a', default='A')
    ap.add_argument('--label-b', default='B')
    args = ap.parse_args()

    base = pathlib.Path(__file__).resolve().parents[1]
    csv_a = (base / args.csv_a).resolve()
    csv_b = (base / args.csv_b).resolve()
    out_dir = (base / args.out_dir).resolve()
    ensure_out_dir(out_dir)

    make_charts_for(csv_a, out_dir, args.label_a)
    make_charts_for(csv_b, out_dir, args.label_b)

    # 合并对比图（两份 CSV 共享同一横轴 FQN，一张图蓝/红两组柱）
    make_combined_charts(csv_a, csv_b, out_dir, args.label_a, args.label_b)

    print(f'Charts written to: {out_dir}')


if __name__ == '__main__':
    main()


