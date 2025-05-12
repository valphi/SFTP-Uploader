@echo off
setlocal

:: Hardcoded values
set "SERVICE_NAME=SftpClientService"
set "SERVICE_DIR=%~dp0"
set "NSSM_DIR=%SERVICE_DIR%nssm"
set "NSSM_EXE=%NSSM_DIR%\nssm.exe"

:: Step 1: Check if NSSM exists
if not exist "%NSSM_EXE%" (
    echo ❌ NSSM executable not found in "%NSSM_EXE%".
    exit /b 1
)

:: Step 2: Stop the service
echo Stopping service %SERVICE_NAME%...
net stop %SERVICE_NAME%

:: Step 3: Remove the service
echo Removing service %SERVICE_NAME%...
"%NSSM_EXE%" remove %SERVICE_NAME% confirm

echo Done!
pause
endlocal