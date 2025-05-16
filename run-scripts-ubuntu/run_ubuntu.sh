#!/bin/bash
# SFTP server address
export SFTP_SERVER=

# SFTP username for authentication
export SFTP_USER=

# Passphrase for the private key (if applicable)
export SFTP_PHRASE=

# Path to the private key used for SFTP authentication
export SFTP_PRIVATE_KEY=~/.ssh/id_rsa

# Directory for storing user indicator files
export SFTP_LOCAL_USER_INDICATOR_DIRECTORY="$(pwd)/user_indicator"

# Directory for storing macro indicator files
export SFTP_LOCAL_MACRO_INDICATOR_DIRECTORY="$(pwd)/macro_indicator"

# Directory for storing portfolio files
export SFTP_LOCAL_PORTFOLIO_DIRECTORY="$(pwd)/portfolio"

# Maps external IDs to substrings in portfolio file names.
# Example: PortfolioExternalId1-Portfolio1,PortfolioExternalId2-Portfolio2
# - PortfolioExternalId1: External ID of the first portfolio.
# - Portfolio1: Substring in the file name within the portfolio folder that matches the first portfolio.
# - PortfolioExternalId2: External ID of the second portfolio.
# - Portfolio2: Substring in the file name within the portfolio folder that matches the second portfolio.
# Dont use '-' character in the substring
export SFTP_PORTFOLIO_FILE_MAPPER=

# Specifies the default operation for portfolio files. F = replace, M = modify.
# Example: M
# ⚠️️ Pay attention to the default operation. If you set it to "F", portfolio will be replaced in Sismo even if there is only one date in the file.
export SFTP_PORTFOLIO_FILE_MAPPER_DEFAULT_OPERATION=

# Maps external IDs to substrings in user indicator file names.
# Example: UserExternalId1-UserIndicator1,UserExternalId2-UserIndicator2
# - UserExternalId1: External ID of the first user indicator.
# - UserIndicator1: Substring in the file name within the user indicator folder that matches the first user indicator.
# - UserExternalId2: External ID of the second user indicator.
# - UserIndicator2: Substring in the file name within the user indicator folder that matches the second user indicator.
# Dont use '-' character in the substring
export SFTP_USER_INDICATOR_FILE_MAPPER=

# Specifies the default operation for user indicator files. F = replace, M = modify.
# Example: M
# ⚠️️ Pay attention to the default operation. If you set it to "F", user indicator will be replaced in Sismo even if there is only one date in the file.
export SFTP_USER_INDICATOR_FILE_MAPPER_DEFAULT_OPERATION=

# Maps external IDs to substrings in macro indicator file names.
# Example: MacroExternalId1-MacroIndicator1,MacroExternalId2-MacroIndicator2
# - MacroExternalId1: External ID of the first macro indicator.
# - MacroIndicator1: Substring in the file name within the macro indicator folder that matches the first macro indicator.
# - MacroExternalId2: External ID of the second macro indicator.
# - MacroIndicator2: Substring in the file name within the macro indicator folder that matches the second macro indicator.
# Dont use '-' character in the substring
export SFTP_MACRO_INDICATOR_FILE_MAPPER=

# Specifies the default operation for macro indicator files. F = replace, M = modify.
# Example: M
# ⚠️️ Pay attention to the default operation. If you set it to "F", macro indicator will be replaced in Sismo even if there is only one date in the file.
export SFTP_MACRO_INDICATOR_FILE_MAPPER_DEFAULT_OPERATION=

DIR="$(cd "$(dirname "$0")" && pwd)"

# Check if Java runtime exists
if [ ! -f "$DIR/jre/bin/java" ]; then
  echo "❌ Java runtime not found in $DIR/jre/bin/java"
  exit 1
fi

# Run the Java application
"$DIR/jre/bin/java" -jar "$DIR/SftpClient.jar" "$@"