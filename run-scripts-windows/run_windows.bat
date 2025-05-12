@echo off
:: SFTP server address
:: Possible errors:
:: -
set SFTP_SERVER=

:: SFTP username for authentication
set SFTP_USER=

:: Passphrase for the private key (if applicable)
set SFTP_PHRASE=

:: Path to the private key used for SFTP authentication
set SFTP_PRIVATE_KEY=%USERPROFILE%\.ssh\id_rsa

:: Directory for storing user indicator files
set SFTP_LOCAL_USER_INDICATOR_DIRECTORY=%~dp0user_indicator

:: Directory for storing macro indicator files
set SFTP_LOCAL_MACRO_INDICATOR_DIRECTORY=%~dp0macro_indicator

:: Directory for storing portfolio files
set SFTP_LOCAL_PORTFOLIO_DIRECTORY=%~dp0portfolio

:: Maps external IDs to substrings in portfolio file names.
:: Example: PortfolioExternalId1-Portfolio1,PortfolioExternalId2-Portfolio2
:: - PortfolioExternalId1: External ID of the first portfolio.
:: - Portfolio1: Substring in the file name within the portfolio folder that matches the first portfolio.
:: - PortfolioExternalId2: External ID of the second portfolio.
:: - Portfolio2: Substring in the file name within the portfolio folder that matches the second portfolio.
:: Dont use '-' character in the substring
set SFTP_PORTFOLIO_FILE_MAPPER=

:: Specifies the default operation for portfolio files. F = replace, M = modify.
:: Example: F
set SFTP_PORTFOLIO_FILE_MAPPER_DEFAULT_OPERATION=

:: Maps external IDs to substrings in user indicator file names.
:: Example: UserExternalId1-UserIndicator1,UserExternalId2-UserIndicator2
:: - UserExternalId1: External ID of the first user indicator.
:: - UserIndicator1: Substring in the file name within the user indicator folder that matches the first user indicator.
:: - UserExternalId2: External ID of the second user indicator.
:: - UserIndicator2: Substring in the file name within the user indicator folder that matches the second user indicator.
:: Dont use '-' character in the substring
set SFTP_USER_INDICATOR_FILE_MAPPER=

:: Specifies the default operation for user indicator files. F = replace, M = modify.
:: Example: M
set SFTP_USER_INDICATOR_FILE_MAPPER_DEFAULT_OPERATION=

:: Maps external IDs to substrings in macro indicator file names.
:: Example: MacroExternalId1-MacroIndicator1,MacroExternalId2-MacroIndicator2
:: - MacroExternalId1: External ID of the first macro indicator.
:: - MacroIndicator1: Substring in the file name within the macro indicator folder that matches the first macro indicator.
:: - MacroExternalId2: External ID of the second macro indicator.
:: - MacroIndicator2: Substring in the file name within the macro indicator folder that matches the second macro indicator.
:: Dont use '-' character in the substring
set SFTP_MACRO_INDICATOR_FILE_MAPPER=

:: Specifies the default operation for macro indicator files. F = replace, M = modify.
:: Example: F
set SFTP_MACRO_INDICATOR_FILE_MAPPER_DEFAULT_OPERATION=

:: Define the directory of the script
set DIR=%~dp0

:: Check if Java runtime exists
if not exist "%DIR%jre\bin" (
    echo "❌ Java runtime not found in %DIR%jre\bin\java"
    exit /b 1
)

:: Run the Java application
"%DIR%jre\bin\java.exe" -jar "%DIR%SftpClient.jar"