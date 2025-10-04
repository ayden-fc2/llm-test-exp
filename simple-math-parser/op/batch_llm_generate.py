#!/usr/bin/env python3
# coding: utf-8

"""
批量运行：读取 prompts/nl 或 prompts/cl 下的所有提示词，调用 LLM 接口生成结果，
并把输出写入 out/hf-llmTest-origin（或 out/sk-llmTest-origin）下（按 nl/cl 分目录，镜像原始相对路径，扩展名改为 .out.txt）。

参考 qwen_cli.py 的调用方式，默认使用 HuggingFace 路由的 OpenAI 兼容接口。

使用：
  python3 op/batch_llm_generate.py --mode nl --provider hf
  python3 op/batch_llm_generate.py --mode cl --provider hf
  python3 op/batch_llm_generate.py --mode auto --provider hf   # 同时处理 nl 与 cl

可选参数：
  --provider     选择接口提供方：hf（HuggingFace 路由）或 sk（阿里云 DashScope 兼容），默认 hf
  --model        默认（hf）Qwen/Qwen3-Coder-480B-A35B-Instruct:fireworks-ai；（sk）qwen3-coder-plus
  --base-url     默认（hf）https://router.huggingface.co/v1；（sk）https://dashscope.aliyuncs.com/compatible-mode/v1
  --api-key-env  默认（hf）HF_API_KEY；（sk）SK_API_KEY（均回退尝试 OPENAI_API_KEY 等）
  --prompts-dir  默认 prompts
  --out-dir      默认（hf）out/hf-llmTest-origin；（sk）out/sk-llmTest-origin
  --max-files    限制最大处理文件数（调试用）
  --dry-run      仅打印将要处理的文件，不真正调用 API
  --system       追加 system 提示（默认：You are a helpful assistant.）
  --extra-body-json  透传给 chat.completions.create 的 extra_body（JSON 字符串）。
  --sk-enable-thinking 仅 sk 模式可用：true/false，对应 extra_body.enable_thinking
"""

import argparse
import os
import sys
import json
import time
import pathlib
from typing import List

from openai import OpenAI


def getenv_api_key_with_fallback(env_names) -> str:
    for name in env_names:
        if not name:
            continue
        val = os.getenv(name)
        if val:
            return val
    raise RuntimeError(
        f"未找到 API Key，请设置环境变量之一：{', '.join([n for n in env_names if n])}"
    )


def iter_prompt_files(prompts_dir: pathlib.Path, mode: str) -> List[pathlib.Path]:
    files: List[pathlib.Path] = []
    if mode in ("nl", "auto"):
        nl_dir = prompts_dir / "nl"
        if nl_dir.exists():
            for p in nl_dir.rglob("*.txt"):
                files.append(p)
            for p in nl_dir.rglob("*.md"):
                files.append(p)
    if mode in ("cl", "auto"):
        cl_dir = prompts_dir / "cl"
        if cl_dir.exists():
            for p in cl_dir.rglob("*.json"):
                files.append(p)
            for p in cl_dir.rglob("*.txt"):
                files.append(p)
    # 去重并按路径排序
    uniq = sorted({str(p): p for p in files}.values(), key=lambda x: str(x))
    return uniq


def make_output_path(out_root: pathlib.Path, prompts_dir: pathlib.Path, file_path: pathlib.Path) -> pathlib.Path:
    rel = file_path.relative_to(prompts_dir)
    # 顶层目录名即 nl 或 cl
    top = rel.parts[0] if len(rel.parts) > 0 else "misc"
    sub_rel = pathlib.Path(*rel.parts[1:]) if len(rel.parts) > 1 else pathlib.Path(rel.name)
    new_ext = ".out.txt"
    out_sub = sub_rel.with_suffix(new_ext)
    return out_root / top / out_sub


def read_text(path: pathlib.Path) -> str:
    with path.open("r", encoding="utf-8") as f:
        return f.read()


def write_text(path: pathlib.Path, text: str) -> None:
    path.parent.mkdir(parents=True, exist_ok=True)
    with path.open("w", encoding="utf-8") as f:
        f.write(text)


def call_llm(client: OpenAI, model: str, messages, extra_body=None, max_retries: int = 3, backoff: float = 2.0) -> str:
    last_err = None
    for attempt in range(1, max_retries + 1):
        try:
            resp = client.chat.completions.create(
                model=model,
                messages=messages,
                **({"extra_body": extra_body} if extra_body else {}),
            )
            # 兼容不同 SDK 字段
            msg = resp.choices[0].message
            content = getattr(msg, "content", None) or (msg.get("content") if isinstance(msg, dict) else None)
            if content is None:
                content = str(msg)
            return content
        except Exception as e:
            last_err = e
            if attempt < max_retries:
                time.sleep(backoff ** (attempt - 1))
            else:
                raise
    # 理论不可达
    raise last_err  # type: ignore


def main() -> None:
    parser = argparse.ArgumentParser(description="Batch LLM generation for NL/CL prompts (hf/sk providers)")
    parser.add_argument("--mode", choices=["nl", "cl", "auto"], default="auto")
    parser.add_argument("--provider", choices=["hf", "sk"], default="hf")
    parser.add_argument("--prompts-dir", default="prompts")
    parser.add_argument("--out-dir", default="out/hf-llmTest-origin")
    parser.add_argument("--model", default="Qwen/Qwen3-Coder-480B-A35B-Instruct:fireworks-ai")
    parser.add_argument("--base-url", default="https://router.huggingface.co/v1")
    parser.add_argument("--api-key-env", default="HF_API_KEY")
    parser.add_argument("--max-files", type=int, default=0)
    parser.add_argument("--dry-run", action="store_true")
    parser.add_argument("--system", default="You are a helpful assistant.")
    parser.add_argument("--extra-body-json", default="")
    parser.add_argument("--sk-enable-thinking", choices=["true", "false"], default=None)
    args = parser.parse_args()

    project_root = pathlib.Path(__file__).resolve().parents[1]
    prompts_dir = (project_root / args.prompts_dir).resolve()
    # 根据 provider 动态调整默认值（仅当仍为默认时才改写）
    default_hf_out = "out/hf-llmTest-origin"
    default_hf_url = "https://router.huggingface.co/v1"
    default_hf_model = "Qwen/Qwen3-Coder-480B-A35B-Instruct:fireworks-ai"

    if args.provider == "sk":
        if args.out_dir == default_hf_out:
            args.out_dir = "out/sk-llmTest-origin"
        if args.base_url == default_hf_url:
            args.base_url = "https://dashscope.aliyuncs.com/compatible-mode/v1"
        if args.model == default_hf_model:
            args.model = "qwen3-coder-plus"
        if args.api_key_env == "HF_API_KEY":
            args.api_key_env = "SK_API_KEY"

    out_root = (project_root / args.out_dir).resolve()

    # 选择 API Key 的环境变量优先级
    if args.provider == "hf":
        key = getenv_api_key_with_fallback([args.api_key_env, "OPENAI_API_KEY"])
    else:  # sk
        # 允许多种变量名：SK_API_KEY / DASHSCOPE_API_KEY / OPENAI_API_KEY
        pref = [args.api_key_env]
        if args.api_key_env != "DASHSCOPE_API_KEY":
            pref.append("DASHSCOPE_API_KEY")
        pref.append("OPENAI_API_KEY")
        key = getenv_api_key_with_fallback(pref)
    client = OpenAI(base_url=args.base_url, api_key=key)

    files = iter_prompt_files(prompts_dir, args.mode)
    if args.max_files and len(files) > args.max_files:
        files = files[: args.max_files]

    if args.dry_run:
        for p in files:
            outp = make_output_path(out_root, prompts_dir, p)
            print(f"[DRY] {p} -> {outp}")
        return

    # 解析额外 body（如 sk 的 enable_thinking）
    extra_body = None
    if args.extra_body_json:
        try:
            extra_body = json.loads(args.extra_body_json)
        except Exception:
            print("extra-body-json 不是合法 JSON，已忽略", file=sys.stderr)
            extra_body = None
    if args.provider == "sk" and args.sk_enable_thinking is not None:
        if extra_body is None:
            extra_body = {}
        extra_body["enable_thinking"] = (args.sk_enable_thinking == "true")

    processed = 0
    for p in files:
        prompt_text = read_text(p)
        out_path = make_output_path(out_root, prompts_dir, p)
        try:
            messages = []
            if args.system:
                messages.append({"role": "system", "content": args.system})
            messages.append({"role": "user", "content": prompt_text})
            content = call_llm(client, args.model, messages, extra_body=extra_body)
        except Exception as e:
            # 失败也落一份错误信息，便于排查
            err_text = f"ERROR calling model for {p}: {e}\n"
            write_text(out_path.with_suffix(".error.txt"), err_text)
            print(err_text, file=sys.stderr)
            continue

        # 写入结果与简单 meta
        write_text(out_path, content)
        meta = {
            "source": str(p),
            "output": str(out_path),
            "model": args.model,
            "base_url": args.base_url,
            "provider": args.provider,
        }
        write_text(out_path.with_suffix(".meta.json"), json.dumps(meta, ensure_ascii=False, indent=2))
        processed += 1
        print(f"OK: {p} -> {out_path}")

    print(f"完成，共处理 {processed} 个文件；输出位于 {out_root}")


if __name__ == "__main__":
    main()


