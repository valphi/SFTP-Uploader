@echo off

:: Define the task name
set TASK_NAME=RunSftpClientDaily

:: Remove the scheduled task
powershell -Command ^
    "if (Get-ScheduledTask -TaskName '%TASK_NAME%' -ErrorAction SilentlyContinue) {" ^
    "    Unregister-ScheduledTask -TaskName '%TASK_NAME%' -Confirm:$false;" ^
    "    Write-Host '✅ Scheduled task removed successfully.';" ^
    "} else {" ^
    "    Write-Host '❌ Task not found: %TASK_NAME%';" ^
    "}"