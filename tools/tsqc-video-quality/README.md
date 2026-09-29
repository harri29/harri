# TSQC video quality pipeline

Công cụ dựng lại **24 clip ngang 16:9** từ phim gốc do người dùng cung cấp, gắn với kịch bản giới thiệu Trường Sĩ quan Chính trị (6 phút 50 giây).

## Mục đích
Không nên tăng nét trên 24 file MP4 đã bị nén lần trước. Chương trình cắt lại **trực tiếp từ phim gốc 1920×1080**, crop vùng logo, phóng Lanczos và làm nét nhẹ (không làm bệt mặt người). Xuất H.264 CRF 17 mặc định, chỉ giữ hình để tiện ghép lời bình. Chất lượng thực tế giới hạn bởi phim nguồn và vùng hình đã phải cắt.

## Cách chạy trên máy Windows
1. Cài FFmpeg và thêm vào PATH.
2. Giải nén bộ clip cũ để lấy `DANH_MUC_CLIP.csv`.
3. Chạy:

```powershell
python tools/tsqc-video-quality/enhance.py "Nua-the-ky-goc.mp4" "DANH_MUC_CLIP.csv" "clips_nang_chat_luong"
```

Có thể thêm `--preset fast --crf 16` để tăng chất lượng (file sẽ lớn hơn). Script chỉ cần thư viện Python tiêu chuẩn.

## Nếu muốn khôi phục chi tiết bằng AI

Bản cắt này dùng bộ lọc nâng độ nét FFmpeg, **không phải AI super-resolution**. Real-ESRGAN là công cụ AI mã nguồn mở tại https://github.com/xinntao/Real-ESRGAN . Nếu xử lý bằng AI trên GPU, nên dựng clip **trực tiếp từ nguồn** trước rồi thử AI trên vài giây đại diện, so sánh khuôn mặt, chữ và tư liệu lịch sử. AI không thể bảo đảm khôi phục chi tiết lịch sử chính xác và có thể tạo chi tiết giả.

Các GitHub Actions runner tiêu chuẩn không có GPU chuyên dụng; chạy AI upscale 24 clip HD trên CPU có thể mất rất nhiều thời gian.

## Tôn trọng quyền sử dụng

Không tải lên kho GitHub công khai tệp phim nguồn hay các clip của đơn vị báo chí khi chưa có quyền. Việc cắt bỏ logo không thay đổi quyền tác giả. Ghi nguồn và xin phép phù hợp trước khi phát hành.
