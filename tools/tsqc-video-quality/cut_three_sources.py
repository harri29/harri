#!/usr/bin/env python3
"""Cut 3 user-provided TSQC MP4 videos according to DANH_MUC_BA_VIDEO.csv.

Default: faithful FFmpeg 1080p enhancement on CPU.
Optional: reuse ai_upscale.py with Real-ESRGAN Vulkan on supported GPU.
No source MP4 is uploaded to GitHub. Retain original source attribution.
"""
from __future__ import annotations
import argparse
import csv
import shutil
import subprocess
import sys
import tempfile
from pathlib import Path


def seconds(value: str) -> float:
    out = 0.0
    for part in value.split(":"):
        out = out * 60 + float(part)
    return out


def call(command):
    subprocess.run([str(x) for x in command], check=True)


def main():
    p = argparse.ArgumentParser()
    p.add_argument("--history", type=Path, required=True)
    p.add_argument("--conference", type=Path, required=True)
    p.add_argument("--award", type=Path, required=True)
    p.add_argument("--manifest", type=Path,
                   default=Path(__file__).with_name("DANH_MUC_BA_VIDEO.csv"))
    p.add_argument("--out", type=Path, default=Path("OUTPUT_THREE_SOURCES"))
    p.add_argument("--mode", choices=["ffmpeg", "ai"], default="ffmpeg")
    p.add_argument("--ncnn", type=Path, help="Required for AI: realesrgan-ncnn-vulkan executable")
    p.add_argument("--tile", type=int, default=128)
    p.add_argument("--keep-logo", action="store_true")
    p.add_argument("--crf", type=int, default=18)
    p.add_argument("--resume", action="store_true")
    args = p.parse_args()
    if args.mode == "ai" and (not args.ncnn or not args.ncnn.is_file()):
        p.error("Use --ncnn PATH with --mode ai on a Vulkan GPU machine")
    sources = {
        "VIDEO_NGAY_TRUYEN_THONG": args.history,
        "VIDEO_DON_NHAN_ANH_HUNG": args.award,
        "VIDEO_HOI_THAO_2024": args.conference,
    }
    with args.manifest.open(encoding="utf-8-sig", newline="") as file:
        shots = list(csv.DictReader(file))
    for index, shot in enumerate(shots, 1):
        src = sources[shot["Video gốc"]]
        name = Path(shot["Tên tệp"])
        if name.is_absolute() or ".." in name.parts:
            raise ValueError("Unsafe destination")
        start = seconds(shot["Bắt đầu nguồn"])
        duration = seconds(shot["Kết thúc nguồn"]) - start
        if duration <= 0:
            raise ValueError("Non-positive duration")
        target = args.out / name
        if args.resume and target.is_file() and target.stat().st_size > 0:
            continue
        target.parent.mkdir(parents=True, exist_ok=True)
        is_heritage = shot["Mã nguồn"] == "LS"
        use_ai = args.mode == "ai" and not is_heritage
        print(f"[{index}/{len(shots)}] {name} AI={use_ai}", flush=True)
        # Reconstruct only the broadcaster's top-left corner, preserving
        # full-frame 16:9 geometry; the patch can look soft in moving scenes.
        restore_logo = [] if args.keep_logo else [
            "delogo=x=15:y=15:w=365:h=155"
        ]
        if use_ai:
            with tempfile.TemporaryDirectory(prefix="tsqc_cut_") as td:
                work = Path(td)
                tempmanifest = work / "one_shot.csv"
                with tempmanifest.open("w", encoding="utf-8-sig", newline="") as file:
                    writer = csv.DictWriter(file, fieldnames=[
                        "Cảnh kịch bản", "Tên tệp", "Bắt đầu nguồn",
                        "Kết thúc nguồn"
                    ])
                    writer.writeheader()
                    writer.writerow({
                        "Cảnh kịch bản": shot["Cảnh trong kịch bản"],
                        "Tên tệp": shot["Tên tệp"],
                        "Bắt đầu nguồn": shot["Bắt đầu nguồn"],
                        "Kết thúc nguồn": shot["Kết thúc nguồn"],
                    })
                # Existing repo AI script; process full 1920x1080 frames
                # without the heavy 1652x930 crop used by the older pipeline.
                call([
                    sys.executable, Path(__file__).with_name("ai_upscale.py"),
                    src, tempmanifest, work / "ai", "--ncnn",
                    args.ncnn.resolve(), "--crop", "1920:1080:0:0",
                    "--tile", args.tile, "--chunk-seconds", 0.7
                ])
                restored = work / "ai" / name
                call([
                    "ffmpeg", "-v", "error", "-nostdin", "-y", "-i", restored,
                    "-an", "-vf", ",".join(restore_logo + ["setsar=1"]),
                    "-c:v", "libx264", "-preset", "veryfast",
                    "-threads", 3, "-crf", args.crf, "-pix_fmt", "yuv420p",
                    "-movflags", "+faststart", target
                ])
        else:
            # Historical footage: avoid AI-generated details or false
            # historic faces/inscriptions. General footage: mild restoration.
            filters = restore_logo + [
                "hqdn3d=0.8:0.7:1.8:1.5" if is_heritage
                else "hqdn3d=1.1:0.9:3.0:2.2",
                "unsharp=3:3:0.23:3:3:0.0" if is_heritage
                else "unsharp=3:3:0.46:3:3:0.0",
                "setsar=1",
            ]
            call([
                "ffmpeg", "-v", "error", "-nostdin", "-y",
                "-ss", start, "-threads", 3, "-i", src,
                "-t", duration, "-an", "-vf", ",".join(filters),
                "-c:v", "libx264", "-preset", "veryfast", "-threads", 3,
                "-crf", args.crf, "-pix_fmt", "yuv420p",
                "-movflags", "+faststart", target
            ])
        if not target.is_file() or not target.stat().st_size:
            raise RuntimeError(f"No output: {target}")
    print("Done. Sources unchanged. Obtain permissions and credit sources.")


if __name__ == "__main__":
    main()
