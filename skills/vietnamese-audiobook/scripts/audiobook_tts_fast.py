from __future__ import annotations

import argparse
import asyncio
import re
import shutil
import subprocess
from pathlib import Path

import edge_tts


def clean_text(text: str) -> str:
    text = text.replace("\u00a0", " ")
    text = re.sub(r"[ \t]+", " ", text)
    text = re.sub(r"\n{3,}", "\n\n", text)
    return text.strip()


def split_sentences(text: str) -> list[str]:
    parts = re.split(r"(?<=[.!?…])\s+|\n+", text)
    return [p.strip() for p in parts if p.strip()]


def chunk_text(text: str, max_chars: int = 1100) -> list[str]:
    sentences = split_sentences(text)
    chunks, current, size = [], [], 0
    for sentence in sentences:
        pieces = re.split(r"(?<=[,;:])\s+", sentence) if len(sentence) > max_chars else [sentence]
        for part in pieces:
            part = part.strip()
            if not part:
                continue
            if current and size + len(part) + 1 > max_chars:
                chunks.append(" ".join(current))
                current, size = [part], len(part)
            else:
                current.append(part)
                size += len(part) + (1 if size else 0)
    if current:
        chunks.append(" ".join(current))
    return chunks


async def synth_one(index: int, text: str, path: Path, voice: str, rate: str, pitch: str, sem: asyncio.Semaphore):
    async with sem:
        if path.exists() and path.stat().st_size > 1000:
            print(f"[{index}] exists: {path.name}", flush=True)
            return
        last = None
        for attempt in range(1, 4):
            try:
                print(f"[{index}] attempt {attempt}", flush=True)
                comm = edge_tts.Communicate(text=text, voice=voice, rate=rate, pitch=pitch)
                await asyncio.wait_for(comm.save(str(path)), timeout=75)
                if path.exists() and path.stat().st_size > 1000:
                    return
                raise RuntimeError("empty TTS output")
            except Exception as e:
                last = e
                path.unlink(missing_ok=True)
                await asyncio.sleep(2 * attempt)
        raise RuntimeError(f"TTS part {index} failed: {last}")


async def run(args):
    src = Path(args.input).resolve()
    out = Path(args.output).resolve()
    parts = out / "parts"
    parts.mkdir(parents=True, exist_ok=True)
    text = clean_text(src.read_text(encoding="utf-8-sig"))
    chunks = chunk_text(text, args.max_chars)
    print(f"Chunks: {len(chunks)}", flush=True)
    sem = asyncio.Semaphore(args.concurrency)
    jobs = []
    paths = []
    for i, chunk in enumerate(chunks, 1):
        p = parts / f"part_{i:04d}.mp3"
        paths.append(p)
        jobs.append(synth_one(i, chunk, p, args.voice, args.rate, args.pitch, sem))
    await asyncio.gather(*jobs)

    if not shutil.which("ffmpeg"):
        raise RuntimeError("ffmpeg missing")
    concat = out / "concat.txt"
    concat.write_text("\n".join("file '" + str(p.resolve()).replace("'", "'\\''") + "'" for p in paths), encoding="utf-8")
    subprocess.run([
        "ffmpeg","-y","-f","concat","-safe","0","-i",str(concat),
        "-c:a","libmp3lame","-b:a","128k",str(out/"audiobook.mp3")
    ], check=True)
    print(out/"audiobook.mp3", flush=True)


def args():
    p=argparse.ArgumentParser()
    p.add_argument("input")
    p.add_argument("-o","--output",default="audio")
    p.add_argument("--voice",default="vi-VN-NamMinhNeural")
    p.add_argument("--rate",default="-8%")
    p.add_argument("--pitch",default="+0Hz")
    p.add_argument("--max-chars",type=int,default=1100)
    p.add_argument("--concurrency",type=int,default=4)
    return p.parse_args()


if __name__=="__main__":
    asyncio.run(run(args()))
