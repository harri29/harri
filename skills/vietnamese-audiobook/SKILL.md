---
name: vietnamese-audiobook
description: Tạo truyện audio tiếng Việt miễn phí bằng Edge-TTS và có thể ghép ảnh minh hoạ thành video YouTube bằng FFmpeg. Dùng khi người dùng yêu cầu đọc tiểu thuyết, tạo audiobook, tạo giọng kể tiếng Việt, hoặc làm video truyện từ ảnh tĩnh.
---

# Vietnamese Audiobook

## Mục tiêu

Tạo audio kể truyện tiếng Việt mà không cần API key trả phí.

Mặc định:
- TTS: Edge-TTS
- Giọng nam: `vi-VN-NamMinhNeural`
- Giọng nữ: `vi-VN-HoaiMyNeural`
- Tốc độ truyện thường: `-8%`
- Tốc độ truyện bí ẩn/kinh dị: `-12%`
- Pitch: `+0Hz`
- Audio: MP3
- Video: 1920×1080, 30fps, H.264 + AAC

## Quy trình

1. Nhận TXT, Markdown hoặc DOCX.
2. Giữ nguyên nội dung; chỉ làm sạch khoảng trắng và chia đoạn để TTS.
3. Nếu bản thảo dài, tạo mẫu 20–40 giây trước.
4. Chia văn bản theo câu thành các khối khoảng 1800–2600 ký tự.
5. Tạo từng MP3 bằng Edge-TTS.
6. Ghép các phần bằng FFmpeg.
7. Nếu có ảnh minh hoạ, ghép ảnh tĩnh + pan/zoom nhẹ + audio thành MP4.
8. Không clone hoặc giả mạo giọng của người thật. Chỉ mô tả phong cách kể chuyện chung.

## Cài đặt

Windows PowerShell:

```powershell
py -m pip install -r requirements.txt
winget install --id Gyan.FFmpeg -e
```

Kiểm tra:

```powershell
edge-tts --list-voices | findstr vi-VN
ffmpeg -version
```

## Tạo audio

```powershell
py scripts/audiobook_tts.py "truyen.docx" -o "audio" --voice vi-VN-NamMinhNeural --rate=-8%
```

Truyện bí ẩn:

```powershell
py scripts/audiobook_tts.py "truyen.docx" -o "audio" --voice vi-VN-NamMinhNeural --rate=-12%
```

Giọng nữ:

```powershell
py scripts/audiobook_tts.py "truyen.docx" -o "audio" --voice vi-VN-HoaiMyNeural --rate=-8%
```

Kết quả chính:

```text
audio/
  parts/
  audiobook.mp3
```

## Ghép ảnh + audio

Đặt ảnh theo thứ tự vào thư mục `images`:

```text
images/
  001.png
  002.png
  003.png
```

Sau đó:

```powershell
py scripts/slideshow_video.py --images images --audio audio/audiobook.mp3 --output audiobook_youtube.mp4
```

Script tự lấy độ dài audio và chia đều thời lượng cho ảnh.

## Nguyên tắc ảnh minh hoạ

- 16:9.
- Giữ nhân vật nhất quán.
- Khoảng 15–40 giây/ảnh cho video truyện đơn giản.
- Không cần AI video.
- Có thể tái sử dụng một số ảnh bằng crop hoặc pan/zoom khác nhau.
- Ưu tiên ảnh theo cảnh quan trọng hơn là ảnh cho từng câu.

## Khi người dùng chỉ nói “tạo audio”

Tự ưu tiên:
- `vi-VN-NamMinhNeural`
- `rate=-8%`
- tạo audio mẫu nếu nội dung dài
- sau khi mẫu đạt, xử lý toàn bộ.

## Lưu ý

Edge-TTS không cần API key nhưng cần kết nối Internet tới dịch vụ giọng nói Microsoft. Chính sách và khả năng truy cập dịch vụ có thể thay đổi.
