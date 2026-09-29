# Download the official Real-ESRGAN ncnn Vulkan Windows portable release.
$ErrorActionPreference = 'Stop'
$root = $PSScriptRoot
$bin = Join-Path $root 'bin'
$exe = Get-ChildItem -Path $bin -Recurse -Filter 'realesrgan-ncnn-vulkan.exe' -File -ErrorAction SilentlyContinue | Select-Object -First 1
if (-not $exe) {
    New-Item -ItemType Directory -Force -Path $bin | Out-Null
    $archive = Join-Path $env:TEMP 'tsqc-realesrgan-20220424-windows-full.zip'
    $release = 'https://github.com/xinntao/Real-ESRGAN/releases/download/v0.2.5.0/realesrgan-ncnn-vulkan-20220424-windows.zip'
    Write-Host 'Downloading official Real-ESRGAN ncnn Vulkan Windows package...'
    Invoke-WebRequest -Uri $release -OutFile $archive -UseBasicParsing
    Expand-Archive -Path $archive -DestinationPath $bin -Force
    $exe = Get-ChildItem -Path $bin -Recurse -Filter 'realesrgan-ncnn-vulkan.exe' -File | Select-Object -First 1
}
if (-not $exe) { throw 'Official package did not contain realesrgan-ncnn-vulkan.exe' }
$model = Join-Path $exe.Directory.FullName 'models\realesrgan-x4plus.param'
if (-not (Test-Path $model)) { throw "Cannot find live-action model in $($exe.Directory.FullName). Check archive contents." }
$exe.FullName | Set-Content -Path (Join-Path $root 'REALESRGAN_PATH.txt') -Encoding Default
Write-Host "AI engine ready: $($exe.FullName)"
Write-Host 'The script leaves the source video on your computer.'
