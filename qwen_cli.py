import os
from openai import OpenAI

client = OpenAI(
    base_url="https://router.huggingface.co/v1",
    api_key="TODO",
)

completion = client.chat.completions.create(
    model="Qwen/Qwen3-Coder-480B-A35B-Instruct:fireworks-ai",
    messages=[
        {
            "role": "user",
            "content": "What is the capital of France?"
        }
    ],
)

print(completion.choices[0].message)