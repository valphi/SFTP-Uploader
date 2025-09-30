@echo off
setlocal enabledelayedexpansion

REM Resolve important dirs
set SCRIPT_DIR=%~dp0
REM SCRIPT_DIR ends with backslash
set ROOT_DIR=%SCRIPT_DIR:~0,-1%
for %%i in ("%ROOT_DIR%") do set ROOT_PARENT=%%~dpi
cd /d "%ROOT_PARENT%"

REM Optional: show current directory
echo Working directory: %cd%

REM Validate source folder with batch files
if not exist "run-scripts-windows" (
    echo ❌ Folder run-scripts-windows not found in %cd%.
    exit /b 1
)

REM Copy batch files into root (overwrite silently)
for %%F in (run_windows.bat schedule_windows.bat remove_schedule_windows.bat) do (
    if not exist "run-scripts-windows\%%F" (
        echo ❌ Missing file: run-scripts-windows\%%F
        exit /b 1
    )
    copy /y "run-scripts-windows\%%F" "%%F" >nul
)

set ZIP_FILE=archive_windows.zip
set JRE_ORIGINAL_FOLDER=jre_21_windows
set JRE_TEMP_FOLDER=jre

REM Clean previous archive
if exist "%ZIP_FILE%" (
    del "%ZIP_FILE%"
    echo ✅ Previous zip file "%ZIP_FILE%" removed.
)

REM Build (ensure Gradle wrapper executable on Windows)
call gradlew shadowJar || (
    echo ❌ Gradle build failed.
    exit /b 1
)

REM Copy jar
if exist "build\libs\SftpClient.jar" (
    copy /y "build\libs\SftpClient.jar" "SftpClient.jar" >nul
    echo ✅ SftpClient.jar copied.
) else (
    echo ❌ SftpClient.jar not found in build\libs.
    exit /b 1
)

REM Check JRE folder
if not exist "%JRE_ORIGINAL_FOLDER%" (
    echo ❌ Folder "%JRE_ORIGINAL_FOLDER%" not found.
    exit /b 1
)

REM Rename JRE -> jre
ren "%JRE_ORIGINAL_FOLDER%" "%JRE_TEMP_FOLDER%" || (
    echo ❌ Failed to rename JRE folder.
    exit /b 1
)

REM Ensure required data folders
for %%D in (user_indicator macro_indicator portfolio) do (
    if not exist "%%D" mkdir "%%D"
)

REM Create archive
powershell -NoLogo -NoProfile -Command ^
  "Compress-Archive -Path 'run_windows.bat','schedule_windows.bat','remove_schedule_windows.bat','SftpClient.jar','%JRE_TEMP_FOLDER%','docx-readme\README_WINDOWS.docx','pdf-readme\README_WINDOWS.pdf','user_indicator','macro_indicator','portfolio' -DestinationPath '%ZIP_FILE%' -Force" || (
    echo ❌ Compress-Archive failed.
    ren "%JRE_TEMP_FOLDER%" "%JRE_ORIGINAL_FOLDER%"
    exit /b 1
)

if exist "%ZIP_FILE%" (
    echo ✅ Zip file "%ZIP_FILE%" created.
) else (
    echo ❌ Zip file not created.
    ren "%JRE_TEMP_FOLDER%" "%JRE_ORIGINAL_FOLDER%"
    exit /b 1
)

REM Restore JRE folder name
ren "%JRE_TEMP_FOLDER%" "%JRE_ORIGINAL_FOLDER%" || (
    echo ❌ Failed to restore JRE folder name.
    exit /b 1
)
echo ✅ JRE folder name restored.

REM Cleanup copied batch files
del run_windows.bat
del schedule_windows.bat
del remove_schedule_windows.bat

echo ✅ Done.
endlocal