@echo off
chcp 65001 > nul

REM SFTP server address
REM Possible errors:
REM - Wrong host with error "Error: <wrong host name>"
set SFTP_SERVER=

REM SFTP username(s) for authentication
REM Example: SFTP_USER=username
REM For multiple users: separate with commas, e.g., "user1,user2,user3"
REM Example: SFTP_USER=user1,user2,user3
REM Note: If you use multiple users:
REM    - The only one pair of public and private keys will be used for all users.
REM    - User-specific subfolders can be automatically created in user_indicator,  macro_indicator, and portfolio directories for each user in the list.
REM Possible errors:
REM - Wrong user name with error "Exhausted available authentication methods"
set SFTP_USER=

REM Passphrase for the private key (if applicable)
REM Possible errors:
REM - Wrong phrase with error "Error: Exhausted available authentication methods"
set SFTP_PHRASE=

REM Path to the private key used for SFTP authentication
REM Possible errors:
REM - Wrong path to private key with error "Error: <wrong path to rsa file> (The system cannot find the file specified)"
set SFTP_PRIVATE_KEY=%USERPROFILE%\.ssh\id_rsa

REM Directory for storing user indicator files
REM Possible errors:
REM - Directory doesn't exist with error "Invalid directory: <path to wrong directory>"
set SFTP_LOCAL_USER_INDICATOR_DIRECTORY=%~dp0user_indicator

REM Directory for storing macro indicator files
REM Possible errors:
REM - Directory doesn't exist with error "Invalid directory: <path to wrong directory>"
set SFTP_LOCAL_MACRO_INDICATOR_DIRECTORY=%~dp0macro_indicator

REM Directory for storing portfolio files
REM Possible errors:
REM - Directory doesn't exist with error "Invalid directory: <path to wrong directory>"
set SFTP_LOCAL_PORTFOLIO_DIRECTORY=%~dp0portfolio

REM Maps external IDs to substrings in portfolio file names.
REM Example: PortfolioExternalId1-Portfolio1,PortfolioExternalId2-Portfolio2
REM - PortfolioExternalId1: External ID of the first portfolio.
REM - Portfolio1: Substring in the file name within the portfolio folder that matches the first portfolio.
REM - PortfolioExternalId2: External ID of the second portfolio.
REM - Portfolio2: Substring in the file name within the portfolio folder that matches the second portfolio.
REM Dont use '-' character in the substring
REM Possible errors:
REM - "Mapping parameter is empty or contains more then one similar subName in mappings"
REM   Make sure that the substring is unique in the mapping parameters.
REM   For example, if you have value "a-a,aa-aa" or "PortfolioExternalId-Portfolio,PortfolioExternalId1-Portfolio1" there will be an error.
set SFTP_PORTFOLIO_FILE_MAPPER=

REM Specifies the default operation for portfolio files. F = replace, M = modify.
REM Example: M
REM Pay attention to the default operation. If you set it to "F", portfolio will be replaced in Sismo even if there is only one date in the file.
set SFTP_PORTFOLIO_FILE_MAPPER_DEFAULT_OPERATION=

REM Maps external IDs to substrings in user indicator file names.
REM Example: UserExternalId1-UserIndicator1,UserExternalId2-UserIndicator2
REM - UserExternalId1: External ID of the first user indicator.
REM - UserIndicator1: Substring in the file name within the user indicator folder that matches the first user indicator.
REM - UserExternalId2: External ID of the second user indicator.
REM - UserIndicator2: Substring in the file name within the user indicator folder that matches the second user indicator.
REM Dont use '-' character in the substring
REM Possible errors:
REM - "Mapping parameter is empty or contains more then one similar subName in mappings"
REM   Make sure that the substring is unique in the mapping parameters.
REM   For example, if you have value "a-a,aa-aa" or "UserExternalId-UserIndicator,UserExternalId1-UserIndicator1" there will be an error.
set SFTP_USER_INDICATOR_FILE_MAPPER=

REM Specifies the default operation for user indicator files. F = replace, M = modify.
REM Example: M
REM Pay attention to the default operation. If you set it to "F", user indicator will be replaced in Sismo even if there is only one date in the file.
set SFTP_USER_INDICATOR_FILE_MAPPER_DEFAULT_OPERATION=

REM Maps external IDs to substrings in macro indicator file names.
REM Example: MacroExternalId1-MacroIndicator1,MacroExternalId2-MacroIndicator2
REM - MacroExternalId1: External ID of the first macro indicator.
REM - MacroIndicator1: Substring in the file name within the macro indicator folder that matches the first macro indicator.
REM - MacroExternalId2: External ID of the second macro indicator.
REM - MacroIndicator2: Substring in the file name within the macro indicator folder that matches the second macro indicator.
REM Dont use '-' character in the substring
REM Possible errors:
REM - "Mapping parameter is empty or contains more then one similar subName in mappings"
REM   Make sure that the substring is unique in the mapping parameters.
REM   For example, if you have value "a-a,aa-aa" or "MacroExternalId1-MacroIndicator1,MacroExternalId-MacroIndicator" there will be an error.
set SFTP_MACRO_INDICATOR_FILE_MAPPER=

REM Specifies the default operation for macro indicator files. F = replace, M = modify.
REM Example: M
REM Pay attention to the default operation. If you set it to "F", macro indicator will be replaced in Sismo even if there is only one date in the file.
set SFTP_MACRO_INDICATOR_FILE_MAPPER_DEFAULT_OPERATION=

REM Define the directory of the script
set DIR=%~dp0
cd /d "%DIR%"

REM Check if Java runtime exists
if not exist "%DIR%jre\bin" (
    echo Java runtime not found in %DIR%jre\bin\java
    exit /b 1
)

REM Run the Java application
"%DIR%jre\bin\java.exe" -Dfile.encoding=UTF-8 -Dconsole.encoding=UTF-8 -jar "%DIR%SftpClient.jar"

exit /b 0