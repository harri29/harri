# Vietnamese Audiobook Skill

Skill tạo truyện audio tiếng Việt bằng Edge-TTS và ghép ảnh thành video YouTube bằng FFmpeg.

## Cài nhanh

```powershell
py -m pip install -r skills/vietnamese-audiobook/requirements.txt
winget install --id Gyan.FFmpeg -e
```

## Tạo audio

```powershell
py skills/vietnamese-audiobook/scripts/audiobook_tts.py "truyen.docx" -o audio --rate=-8%
```

## Tạo video

```powershell
py skills/vietnamese-audiobook/scripts/slideshow_video.py --images images --audio audio/audiobook.mp3 --output audiobook_youtube.mp4
```

Giọng mặc định: `vi-VN-NamMinhNeural`.
