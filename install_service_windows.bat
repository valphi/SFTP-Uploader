@echo off
setlocal

:: Hardcoded values
set "SERVICE_NAME=SftpClientService"
set "SERVICE_DIR=%~dp0"
set "NSSM_DIR=%SERVICE_DIR%nssm"
set "NSSM_EXE=%NSSM_DIR%\nssm.exe"
set "NSSM_URL=https://nssm.cc/release/nssm-2.24.zip"
set "NSSM_ZIP=%SERVICE_DIR%nssm.zip"
set "RUN_BAT=%SERVICE_DIR%run_windows.bat"

:: Step 1: Download NSSM if not present
if not exist "%NSSM_EXE%" (
    echo Downloading NSSM...
    powershell -Command "Invoke-WebRequest '%NSSM_URL%' -OutFile '%NSSM_ZIP%'"
    echo Extracting NSSM...
    powershell -Command "Expand-Archive -Path '%NSSM_ZIP%' -DestinationPath '%SERVICE_DIR%'"
    move "%SERVICE_DIR%nssm-2.24\win64" "%NSSM_DIR%" >nul
    rd /s /q "%SERVICE_DIR%nssm-2.24"
    del "%NSSM_ZIP%"
)

:: Step 2: Install the service
echo Installing service %SERVICE_NAME%...
"%NSSM_EXE%" install %SERVICE_NAME% "C:\Windows\System32\cmd.exe" /c "\"%RUN_BAT%\""
"%NSSM_EXE%" set %SERVICE_NAME% AppDirectory "%SERVICE_DIR%"
"%NSSM_EXE%" set %SERVICE_NAME% Start SERVICE_AUTO_START

:: Step 3: Start the service
echo Starting service %SERVICE_NAME%...
net start %SERVICE_NAME%

echo Done!
pause
endlocal