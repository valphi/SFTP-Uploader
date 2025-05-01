#!/bin/bash
export SFTP_SERVER=s-75f7a0622f7c4478a.server.transfer.eu-west-1.amazonaws.com
export SFTP_USER=
export SFTP_PHRASE=
export SFTP_PRIVATE_KEY=~/.ssh/id_rsa
export SFTP_LOCAL_USER_INDICATOR_DIRECTORY="$(pwd)/user_indicator"
export SFTP_LOCAL_MACRO_INDICATOR_DIRECTORY="$(pwd)/macro_indicator"
export SFTP_LOCAL_PORTFOLIO_DIRECTORY="$(pwd)/portfolio"
export SFTP_PORTFOLIO_FILE_MAPPER=
export SFTP_PORTFOLIO_FILE_MAPPER_DEFAULT_OPERATION=
export SFTP_USER_INDICATOR_FILE_MAPPER=
export SFTP_USER_INDICATOR_FILE_MAPPER_DEFAULT_OPERATION=
export SFTP_MACRO_INDICATOR_FILE_MAPPER=
export SFTP_MACRO_INDICATOR_FILE_MAPPER_DEFAULT_OPERATION=
#Uncomment the following lines instead of above lines to set the file mappers and default operations (F or M)
#export SFTP_PORTFOLIO_FILE_MAPPER=123AB-Original2,124AB-Original5,125AB-Original7
#export SFTP_PORTFOLIO_FILE_MAPPER_DEFAULT_OPERATION=F

DIR="$(cd "$(dirname "$0")" && pwd)"

# Check if Java runtime exists
if [ ! -f "$DIR/jre/bin/java" ]; then
  echo "❌ Java runtime not found in $DIR/jre/bin/java"
  exit 1
fi

# Run the Java application
"$DIR/jre/bin/java" -jar "$DIR/SftpClient.jar" "$@"