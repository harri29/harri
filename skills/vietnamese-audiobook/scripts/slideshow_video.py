from __future__ import annotations

import argparse
import json
import shutil
import subprocess
from pathlib import Path


def probe_duration(audio: Path) -> float:
    if not shutil.which("ffprobe"):
        raise RuntimeError("Không tìm thấy ffprobe/FFmpeg.")
    cmd = [
        "ffprobe", "-v", "error",
        "-show_entries", "format=duration",
        "-of", "json", str(audio)
    ]
    r = subprocess.run(cmd, capture_output=True, text=True, check=True)
    return float(json.loads(r.stdout)["format"]["duration"])


def main():
    ap = argparse.ArgumentParser(description="Ghép ảnh tĩnh với audiobook thành video YouTube.")
    ap.add_argument("--images", required=True)
    ap.add_argument("--audio", required=True)
    ap.add_argument("--output", default="audiobook_youtube.mp4")
    ap.add_argument("--width", type=int, default=1920)
    ap.add_argument("--height", type=int, default=1080)
    ap.add_argument("--fps", type=int, default=30)
    args = ap.parse_args()

    if not shutil.which("ffmpeg"):
        raise RuntimeError("Không tìm thấy FFmpeg.")

    img_dir = Path(args.images).resolve()
    audio = Path(args.audio).resolve()
    output = Path(args.output).resolve()

    images = sorted([
        p for p in img_dir.iterdir()
        if p.suffix.lower() in {".png", ".jpg", ".jpeg", ".webp"}
    ])
    if not images:
        raise RuntimeError("Thư mục ảnh trống.")

    total = probe_duration(audio)
    per = total / len(images)

    list_file = output.with_suffix(".images.txt")
    lines = []
    for image in images:
        lines.append(f"file '{str(image).replace(chr(39), chr(39)+chr(92)+chr(39)+chr(39))}'")
        lines.append(f"duration {per:.3f}")
    lines.append(f"file '{str(images[-1]).replace(chr(39), chr(39)+chr(92)+chr(39)+chr(39))}'")
    list_file.write_text("\n".join(lines), encoding="utf-8")

    vf = (
        f"scale={args.width}:{args.height}:force_original_aspect_ratio=increase,"
        f"crop={args.width}:{args.height},"
        f"fps={args.fps},format=yuv420p"
    )

    cmd = [
        "ffmpeg", "-y",
        "-f", "concat", "-safe", "0", "-i", str(list_file),
        "-i", str(audio),
        "-vf", vf,
        "-c:v", "libx264", "-preset", "medium", "-crf", "20",
        "-c:a", "aac", "-b:a", "160k",
        "-shortest", "-movflags", "+faststart",
        str(output)
    ]
    subprocess.run(cmd, check=True)
    list_file.unlink(missing_ok=True)
    print(f"Hoàn tất: {output}")


if __name__ == "__main__":
    main()
