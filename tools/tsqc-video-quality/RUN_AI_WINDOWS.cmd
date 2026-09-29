@echo off
setlocal
chcp 65001 >nul
set "ROOT=%~dp0"
if "%~1"=="" (
 echo Keo tha video MP4 GOC vao RUN_AI_WINDOWS.cmd
 pause
 exit /b 1
)
where ffmpeg >nul 2>&1
if errorlevel 1 (
 echo Chua co ffmpeg trong PATH.
 pause
 exit /b 1
)
where py >nul 2>&1
if not errorlevel 1 (
 set "PYTHON=py -3"
) else (
 where python >nul 2>&1
 if errorlevel 1 (
  echo Chua co Python 3. Cai Python 3 tu python.org.
  pause
  exit /b 1
 )
 set "PYTHON=python"
)
powershell -NoProfile -ExecutionPolicy Bypass -File "%ROOT%setup_windows.ps1"
if errorlevel 1 (
 echo Khong tai duoc Real-ESRGAN. Kiem tra internet/driver Vulkan.
 pause
 exit /b 1
)
set /p REALEXE=<"%ROOT%REALESRGAN_PATH.txt"
rem Historical scenes are restored without generative AI to avoid falsifying archival details.
%PYTHON% "%ROOT%ai_upscale.py" "%~1" "%ROOT%DANH_MUC_CLIP.csv" "%ROOT%OUTPUT_AI" --ncnn "%REALEXE%" --no-ai-scene 2 --no-ai-scene 3 --no-ai-scene 4 --resume
if errorlevel 1 (
 echo Co loi. Neu GPU yeu, thu tham so --tile 64.
 pause
 exit /b 1
)
echo Hoan thanh. Video trong OUTPUT_AI.
pause
