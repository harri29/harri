from __future__ import annotations

import argparse
import asyncio
import re
import shutil
import subprocess
from pathlib import Path

import edge_tts

try:
    from docx import Document
except ImportError:
    Document = None


def read_text(path: Path) -> str:
    ext = path.suffix.lower()
    if ext in {".txt", ".md"}:
        return path.read_text(encoding="utf-8-sig")
    if ext == ".docx":
        if Document is None:
            raise RuntimeError("Thiếu python-docx. Chạy: py -m pip install python-docx")
        doc = Document(str(path))
        return "\n".join(p.text for p in doc.paragraphs if p.text.strip())
    raise ValueError("Chỉ hỗ trợ .txt, .md, .docx")


def clean_text(text: str) -> str:
    text = text.replace("\u00a0", " ")
    text = re.sub(r"[ \t]+", " ", text)
    text = re.sub(r"\n{3,}", "\n\n", text)
    return text.strip()


def split_sentences(text: str) -> list[str]:
    # Giữ dấu câu ở cuối câu, phù hợp tiếng Việt.
    parts = re.split(r"(?<=[.!?…])\s+|\n+", text)
    return [p.strip() for p in parts if p.strip()]


def chunk_text(text: str, max_chars: int = 2400) -> list[str]:
    sentences = split_sentences(text)
    chunks: list[str] = []
    current: list[str] = []
    size = 0

    for sentence in sentences:
        if len(sentence) > max_chars:
            # Trường hợp một câu/đoạn quá dài: chia mềm theo dấu phẩy/chấm phẩy.
            subparts = re.split(r"(?<=[,;:])\s+", sentence)
        else:
            subparts = [sentence]

        for part in subparts:
            part = part.strip()
            if not part:
                continue
            add = len(part) + (1 if current else 0)
            if current and size + add > max_chars:
                chunks.append(" ".join(current))
                current = [part]
                size = len(part)
            else:
                current.append(part)
                size += add

    if current:
        chunks.append(" ".join(current))
    return chunks


async def synthesize(text: str, output: Path, voice: str, rate: str, pitch: str) -> None:
    communicate = edge_tts.Communicate(text=text, voice=voice, rate=rate, pitch=pitch)
    await communicate.save(str(output))


def ffmpeg_concat(parts: list[Path], output: Path) -> None:
    if not shutil.which("ffmpeg"):
        raise RuntimeError("Không tìm thấy FFmpeg. Cài bằng: winget install --id Gyan.FFmpeg -e")

    list_file = output.parent / "concat.txt"
    lines = []
    for p in parts:
        safe = str(p.resolve()).replace("'", "'\\''")
        lines.append(f"file '{safe}'")
    list_file.write_text("\n".join(lines), encoding="utf-8")

    cmd = [
        "ffmpeg", "-y",
        "-f", "concat", "-safe", "0",
        "-i", str(list_file),
        "-c:a", "libmp3lame", "-b:a", "128k",
        str(output),
    ]
    subprocess.run(cmd, check=True)
    list_file.unlink(missing_ok=True)


async def main_async(args) -> None:
    src = Path(args.input).expanduser().resolve()
    out_dir = Path(args.output).expanduser().resolve()
    parts_dir = out_dir / "parts"
    parts_dir.mkdir(parents=True, exist_ok=True)

    text = clean_text(read_text(src))
    if not text:
        raise RuntimeError("Không tìm thấy nội dung văn bản.")

    chunks = chunk_text(text, args.max_chars)
    print(f"Tổng số khối TTS: {len(chunks)}")

    part_files: list[Path] = []
    for i, chunk in enumerate(chunks, 1):
        part = parts_dir / f"part_{i:04d}.mp3"
        part_files.append(part)
        if part.exists() and not args.force:
            print(f"[{i}/{len(chunks)}] Bỏ qua (đã có): {part.name}")
            continue
        print(f"[{i}/{len(chunks)}] Đang tạo: {part.name}")
        await synthesize(chunk, part, args.voice, args.rate, args.pitch)

    final = out_dir / "audiobook.mp3"
    ffmpeg_concat(part_files, final)
    print(f"Hoàn tất: {final}")


def parse_args():
    p = argparse.ArgumentParser(description="Tạo audiobook tiếng Việt miễn phí bằng Edge-TTS.")
    p.add_argument("input", help="TXT, MD hoặc DOCX")
    p.add_argument("-o", "--output", default="audio", help="Thư mục đầu ra")
    p.add_argument("--voice", default="vi-VN-NamMinhNeural")
    p.add_argument("--rate", default="-8%")
    p.add_argument("--pitch", default="+0Hz")
    p.add_argument("--max-chars", type=int, default=2400)
    p.add_argument("--force", action="store_true", help="Tạo lại các part đã có")
    return p.parse_args()


if __name__ == "__main__":
    asyncio.run(main_async(parse_args()))
