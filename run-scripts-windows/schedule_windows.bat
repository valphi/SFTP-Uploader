@echo off

:: Define variables
set SCRIPT_PATH=%~dp0run_windows.bat
set TASK_NAME=RunSftpClientDaily

:: Create a Task Scheduler task to run the batch file daily at 05:00 AM
powershell -Command ^
    "$Action = New-ScheduledTaskAction -Execute '%SCRIPT_PATH%';" ^
    "$Trigger = New-ScheduledTaskTrigger -Daily -At 5:00AM;" ^
    "$Settings = New-ScheduledTaskSettingsSet -AllowStartIfOnBatteries -DontStopIfGoingOnBatteries;" ^
    "Register-ScheduledTask -TaskName '%TASK_NAME%' -Action $Action -Trigger $Trigger -Settings $Settings -Force;" ^
    "if (Get-ScheduledTask -TaskName '%TASK_NAME%' -ErrorAction SilentlyContinue) { Write-Host '✅ Scheduled task created successfully.' } else { Write-Host '❌ Failed to create scheduled task.' }"