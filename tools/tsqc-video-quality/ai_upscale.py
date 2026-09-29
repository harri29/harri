#!/usr/bin/env python3
"""AI restore TSQC shots from original MP4. Requires FFmpeg + Real-ESRGAN ncnn Vulkan."""
import argparse
import csv
import shutil
import subprocess
import tempfile
from pathlib import Path


def seconds(stamp):
    result = 0.0
    for section in stamp.split(':'):
        result = result * 60 + float(section)
    return result


def run(*args, cwd=None):
    subprocess.run(list(map(str, args)), check=True, cwd=cwd)


def main():
    p = argparse.ArgumentParser(description='AI restore 24 TSQC clips from original documentary')
    p.add_argument('source', type=Path)
    p.add_argument('manifest', type=Path)
    p.add_argument('output', type=Path)
    p.add_argument('--ncnn', required=True, help='Path to realesrgan-ncnn-vulkan[.exe]')
    p.add_argument('--tile', default=128, type=int)
    p.add_argument('--crop', default='1652:930:134:150')
    p.add_argument('--chunk-seconds', default=.7, type=float)
    p.add_argument('--scene', action='append')
    p.add_argument('--no-ai-scene', action='append', default=[])
    p.add_argument('--limit', type=int)
    p.add_argument('--resume', action='store_true')
    p.add_argument('--dry-run', action='store_true')
    a = p.parse_args()
    if a.chunk_seconds <= 0: p.error('chunk seconds must be positive')
    with a.manifest.open(encoding='utf-8-sig', newline='') as f:
        shots = list(csv.DictReader(f))
    if a.scene: shots = [s for s in shots if s['Cảnh kịch bản'] in a.scene]
    if a.limit: shots = shots[:a.limit]
    for idx, shot in enumerate(shots, 1):
        start = seconds(shot['Bắt đầu nguồn'])
        end = seconds(shot['Kết thúc nguồn'])
        if end <= start: raise ValueError('Invalid start/end time')
        rel = Path(shot['Tên tệp'])
        if rel.is_absolute() or '..' in rel.parts: raise ValueError('Unsafe output name')
        target = a.output / rel
        use_ai = shot['Cảnh kịch bản'] not in a.no_ai_scene
        print(f'[{idx}/{len(shots)}] {rel} {start:.2f}-{end:.2f} AI={use_ai}', flush=True)
        if a.dry_run or (a.resume and target.exists() and target.stat().st_size): continue
        if not a.source.is_file(): p.error('Source MP4 missing')
        target.parent.mkdir(parents=True, exist_ok=True)
        with tempfile.TemporaryDirectory(prefix='tsqc_ai_') as work:
            wd = Path(work)
            total = round((end-start)*30)
            chunk_frames = max(1, round(a.chunk_seconds*30))
            parts = []
            for block, first in enumerate(range(0, total, chunk_frames)):
                count = min(chunk_frames, total-first)
                source_frames, result_frames = wd/'in', wd/'out'
                source_frames.mkdir()
                result_frames.mkdir()
                run('ffmpeg','-v','error','-y','-ss',f'{start+first/30:.6f}',
                    '-i',a.source.resolve(),'-vf',f'crop={a.crop},fps=30',
                    '-frames:v',count,'-start_number','0',source_frames/'%08d.png')
                if len(list(source_frames.glob('*.png'))) != count:
                    raise RuntimeError('Incomplete source decode')
                if use_ai:
                    run(Path(a.ncnn).resolve(),'-i',source_frames,'-o',result_frames,
                        '-n','realesrgan-x4plus','-s','4','-t',a.tile,'-f','png',
                        cwd=Path(a.ncnn).resolve().parent)
                    if len(list(result_frames.glob('*.png'))) != count:
                        raise RuntimeError('AI model generated incomplete frames')
                    frames = result_frames
                else:
                    frames = source_frames
                part = wd / f'chunk{block:04}.mp4'
                run('ffmpeg','-v','error','-y','-framerate','30',
                    '-start_number','0','-i',frames/'%08d.png','-frames:v',count,
                    '-vf','scale=1920:1080:flags=lanczos,setsar=1','-an',
                    '-c:v','libx264','-preset','medium','-crf','16',
                    '-pix_fmt','yuv420p',part)
                parts.append(part)
                shutil.rmtree(source_frames)
                shutil.rmtree(result_frames)
            if len(parts) == 1: shutil.move(parts[0],target)
            else:
                txt = wd/'concat.txt'
                txt.write_text(''.join(f"file '{x.as_posix()}'\n" for x in parts), encoding='utf-8')
                run('ffmpeg','-v','error','-y','-f','concat','-safe','0',
                    '-i',txt,'-c','copy','-movflags','+faststart',target)
    print('Finished. Check historical faces/text and keep source attribution.')


if __name__ == '__main__':
    main()
