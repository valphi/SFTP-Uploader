@echo off
chcp 65001 > nul

REM Define the task name
set TASK_NAME=RunSftpClientDaily

REM Remove the scheduled task
powershell -Command ^
    "if (Get-ScheduledTask -TaskName '%TASK_NAME%' -ErrorAction SilentlyContinue) {" ^
    "    Unregister-ScheduledTask -TaskName '%TASK_NAME%' -Confirm:$false;" ^
    "    Write-Host 'Scheduled task removed successfully.';" ^
    "} else {" ^
    "    Write-Host 'Task not found: %TASK_NAME%';" ^
    "}"

exit /b 0
