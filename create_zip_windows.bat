@echo off

:: Define variables
set ZIP_FILE=archive_windows.zip
set JRE_ORIGINAL_FOLDER=jre_21_windows
set JRE_TEMP_FOLDER=jre

:: Remove the previous zip file if it exists
if exist "%ZIP_FILE%" (
    del "%ZIP_FILE%"
    echo ✅ Previous zip file "%ZIP_FILE%" removed.
)

:: Build the project using Gradle
gradlew build

:: Copy the JAR file to the root directory, replacing it if it exists
if exist "build\libs\SftpClient.jar" (
    copy /y "build\libs\SftpClient.jar" "SftpClient.jar"
    echo ✅ SftpClient.jar copied to root directory.
) else (
    echo ❌ SftpClient.jar not found in /build/libs.
    exit /b 1
)

:: Check if the original JRE folder exists
if not exist "%JRE_ORIGINAL_FOLDER%" (
    echo ❌ Folder "%JRE_ORIGINAL_FOLDER%" not found.
    exit /b 1
)

:: Rename the JRE folder to "jre"
ren "%JRE_ORIGINAL_FOLDER%" "%JRE_TEMP_FOLDER%"
if errorlevel 1 (
    echo ❌ Failed to rename folder "%JRE_ORIGINAL_FOLDER%" to "%JRE_TEMP_FOLDER%".
    exit /b 1
)

:: Create the required folders if they don't exist
if not exist "user_indicator" mkdir user_indicator
if not exist "macro_indicator" mkdir macro_indicator
if not exist "portfolio" mkdir portfolio

:: Create the zip file
powershell -Command ^
    "Compress-Archive -Path 'run_windows.bat', 'schedule_windows.bat', 'remove_schedule_windows.bat', 'SftpClient.jar', '%JRE_TEMP_FOLDER%' -DestinationPath '%ZIP_FILE%' -Force"

:: Check if the zip file was created
if exist "%ZIP_FILE%" (
    echo ✅ Zip file "%ZIP_FILE%" created successfully.
) else (
    echo ❌ Failed to create zip file "%ZIP_FILE%".
    ren "%JRE_TEMP_FOLDER%" "%JRE_ORIGINAL_FOLDER%"
    exit /b 1
)

:: Restore the original folder name
ren "%JRE_TEMP_FOLDER%" "%JRE_ORIGINAL_FOLDER%"
if errorlevel 1 (
    echo ❌ Failed to restore folder name from "%JRE_TEMP_FOLDER%" to "%JRE_ORIGINAL_FOLDER%".
    exit /b 1
)

echo ✅ Folder name restored to "%JRE_ORIGINAL_FOLDER%".