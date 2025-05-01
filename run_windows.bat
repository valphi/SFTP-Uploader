@echo off
set SFTP_SERVER=
set SFTP_USER=
set SFTP_PHRASE=
set SFTP_PRIVATE_KEY=%USERPROFILE%\.ssh\id_rsa
set SFTP_LOCAL_USER_INDICATOR_DIRECTORY=%~dp0user_indicator
set SFTP_LOCAL_MACRO_INDICATOR_DIRECTORY=%~dp0macro_indicator
set SFTP_LOCAL_PORTFOLIO_DIRECTORY=%~dp0portfolio
set SFTP_PORTFOLIO_FILE_MAPPER=
set SFTP_PORTFOLIO_FILE_MAPPER_DEFAULT_OPERATION=
set SFTP_USER_INDICATOR_FILE_MAPPER=
set SFTP_USER_INDICATOR_FILE_MAPPER_DEFAULT_OPERATION=
set SFTP_MACRO_INDICATOR_FILE_MAPPER=
set SFTP_MACRO_INDICATOR_FILE_MAPPER_DEFAULT_OPERATION=

:: Uncomment the following lines instead of above lines to set the file mappers and default operations (F or M)
@REM set SFTP_USER_INDICATOR_FILE_MAPPER=123AB-Original2,124AB-Original5,125AB-Original7
@REM set SFTP_USER_INDICATOR_FILE_MAPPER_DEFAULT_OPERATION=F

:: Define the directory of the script
set DIR=%~dp0

:: Check if Java runtime exists
if not exist "%DIR%jre\bin" (
    echo "❌ Java runtime not found in $DIR/jre/bin/java"
    exit /b 1
)

:: Run the Java application
"%DIR%jre\bin\java.exe" -jar "%DIR%SftpClient.jar"
