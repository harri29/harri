#!/usr/bin/env python3
"""Regenerate 24 16:9 high-quality silent B-roll clips from original TSQC documentary.
Requires ffmpeg/ffprobe. Do not publish source footage without permission.
"""
import argparse
import csv
import pathlib
import subprocess

def seconds(text):
    mm, ss = text.strip().split(":")
    return int(mm) * 60 + float(ss)

def main():
    p = argparse.ArgumentParser(description="Sharpen and high-quality recut TSQC footage")
    p.add_argument("source", type=pathlib.Path, help="Original documentary MP4")
    p.add_argument("manifest", type=pathlib.Path, help="DANH_MUC_CLIP.csv from prior ZIP")
    p.add_argument("output", type=pathlib.Path, help="Destination folder")
    p.add_argument("--crf", type=int, default=17, help="H.264 quality (lower=larger)")
    p.add_argument("--preset", choices=["veryfast","faster","fast","medium","slow"], default="fast")
    args = p.parse_args()
    if not args.source.is_file():
        p.error("Source MP4 does not exist")
    with args.manifest.open(encoding="utf-8-sig", newline="") as f:
        rows = list(csv.DictReader(f))
    args.output.mkdir(parents=True,exist_ok=True)
    # Original is 1920x1080; logo is near the upper-left corner.
    # Crop eliminates the logo and keeps 16:9, followed by modest sharpening.
    vf = "crop=1652:930:134:150,scale=1920:1080:flags=lanczos,unsharp=5:5:0.60:3:3:0.20,setsar=1"
    for index, row in enumerate(rows, 1):
        start, end = seconds(row["Bắt đầu nguồn"]), seconds(row["Kết thúc nguồn"])
        target = args.output / row["Tên tệp"]
        target.parent.mkdir(parents=True, exist_ok=True)
        command = [
            "ffmpeg","-hide_banner","-loglevel","error","-nostdin","-y",
            "-ss",str(start),"-i",str(args.source),"-t",str(end-start),
            "-map","0:v:0","-an","-vf",vf,
            "-c:v","libx264","-preset",args.preset,"-crf",str(args.crf),
            "-pix_fmt","yuv420p","-movflags","+faststart",str(target)
        ]
        print(f"[{index}/{len(rows)}] {target}", flush=True)
        subprocess.run(command, check=True)
    print("Done. Original source remains unchanged.")

if __name__ == "__main__":
    main()
