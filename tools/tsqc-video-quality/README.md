# TSQC video quality pipeline — Real-ESRGAN AI

Bộ xử lý **24 video ngang 16:9** theo kịch bản giới thiệu Trường Sĩ quan Chính trị. Đã bổ sung AI super-resolution mã nguồn mở, thay thế cách làm nét bằng FFmpeg khi có GPU.

## Real-ESRGAN nguồn mở

- [Real-ESRGAN](https://github.com/xinntao/Real-ESRGAN) (BSD-3-Clause); dùng mô hình **realesrgan-x4plus** cho video người thật.
- [Real-ESRGAN ncnn Vulkan](https://github.com/xinntao/Real-ESRGAN-ncnn-vulkan) (MIT); cung cấp bản portable Windows cho Intel/AMD/NVIDIA GPU có Vulkan.
- [FFmpeg](https://ffmpeg.org/) tách khung hình, crop 16:9 và xuất video H.264 Full HD.
- Không dùng model chuyên anime cho phim tài liệu có người thật.

## Cách chạy (Windows)

1. Cài Python 3 và FFmpeg, cập nhật driver đồ họa Vulkan.
2. Tải/giải nén thư mục `tools/tsqc-video-quality` và giữ bốn file `ai_upscale.py`, `setup_windows.ps1`, `RUN_AI_WINDOWS.cmd`, `DANH_MUC_CLIP.csv` trong cùng thư mục.
3. Kéo thả **phim gốc** `Nửa thế kỷ đào tạo cán bộ chính trị cấp phân đội tại Trường Sĩ quan Chính trị.mp4` lên `RUN_AI_WINDOWS.cmd`.
4. Trình khởi chạy tự tải [bản Real-ESRGAN ncnn Vulkan Windows chính thức v0.2.0](https://github.com/xinntao/Real-ESRGAN-ncnn-vulkan/releases/tag/v0.2.0) và chỉ xử lý video **trên máy của bạn**, không tự tải video nguồn lên GitHub.
5. Kết quả trong `OUTPUT_AI` theo tên phân cảnh và mốc cắt từ danh mục. Video đầu ra 1920×1080, 30fps, không có tiếng để tiện ghép lời bình.

Mặc định cảnh tư liệu lịch sử 2, 3, 4 dùng phương án **không sinh chi tiết AI** để tránh thay đổi diện mạo, chữ và tư liệu thật. Các cảnh quay học viên/giảng đường/cơ sở hiện nay được tăng nét bằng AI.

## Chạy thử một clip / ít VRAM

```powershell
python ai_upscale.py "C:\video\tsqc_goc.mp4" DANH_MUC_CLIP.csv OUTPUT_AI --ncnn "C:\AI\realesrgan-ncnn-vulkan.exe" --scene 1 --limit 1
```

Nếu GPU báo hết VRAM, thêm `--tile 64`. Để tiếp tục bỏ qua tệp đã xong: `--resume`. Nếu chạy mã Python trực tiếp và **không** truyền `--no-ai-scene`, nó sẽ dùng AI với mọi cảnh kể cả hình tư liệu: cần kiểm tra từng frame trước khi công bố.

Xem kế hoạch không cần model hay GPU:

```powershell
python ai_upscale.py "C:\video\tsqc_goc.mp4" DANH_MUC_CLIP.csv OUTPUT_AI --ncnn "realesrgan-ncnn-vulkan.exe" --dry-run
```

Công cụ `enhance.py` cũ vẫn có thể sử dụng để cắt lại và làm nét FFmpeg **không có AI**.

**Giới hạn:** AI có thể tạo chi tiết không tồn tại trong khung hình gốc và gây nhấp nháy khung hình; chất lượng thực tế tùy phim gốc. GitHub Actions runner tiêu chuẩn không cung cấp GPU phù hợp để chạy khối lượng này, nên xử lý thực tế trên Windows có Vulkan GPU. Mã nguồn đã được kiểm tra cú pháp và logic xử lý trên video thử tổng hợp nhưng không phải kết quả AI thật khi chưa chạy với model/GPU.

**Nguồn tư liệu và quyền tác giả:** Kho công khai chỉ chứa mã xử lý và mốc cắt; không tải video của Báo Quân đội nhân dân lên kho khi chưa được cho phép. Việc crop logo không thay đổi quyền sử dụng; giữ ghi nguồn và xin phép đơn vị sở hữu trước khi công bố.
