#!/bin/bash

# Delete previous log files
rm -f /tmp/sftpclient*.log

# Define log file with timestamp
LOG_FILE="/tmp/sftpclient_$(date '+%Y-%m-%d_%H-%M-%S').log"
DIR="$(cd "$(dirname "$0")" && pwd)"

{
  echo "🕓 Script started at $(date)"

  # Unquarantine all files in the directory
  if command -v xattr &>/dev/null; then
    sudo xattr -rd com.apple.quarantine "$DIR" 2>/dev/null
    echo "ℹ️ Removed quarantine flags from $DIR"
  fi

  # Set environment variables
  export SFTP_SERVER=
  export SFTP_USER=
  export SFTP_PHRASE=
  export SFTP_PRIVATE_KEY="$HOME/.ssh/id_rsa"
  export SFTP_LOCAL_USER_INDICATOR_DIRECTORY="$DIR/user_indicator"
  export SFTP_LOCAL_MACRO_INDICATOR_DIRECTORY="$DIR/macro_indicator"
  export SFTP_LOCAL_PORTFOLIO_DIRECTORY="$DIR/portfolio"
  export SFTP_PORTFOLIO_FILE_MAPPER=
  export SFTP_PORTFOLIO_FILE_MAPPER_DEFAULT_OPERATION=
  export SFTP_USER_INDICATOR_FILE_MAPPER=
  export SFTP_USER_INDICATOR_FILE_MAPPER_DEFAULT_OPERATION=
  export SFTP_MACRO_INDICATOR_FILE_MAPPER=
  export SFTP_MACRO_INDICATOR_FILE_MAPPER_DEFAULT_OPERATION=

  # Check Java quarantine again, just in case
  if xattr "$DIR/jre/bin/java" 2>/dev/null | grep -q "com.apple.quarantine"; then
    echo "⚠️ Java runtime is quarantined. Please run:"
    echo "    sudo xattr -rd com.apple.quarantine \"$DIR/jre\""
    exit 1
  fi

  # Run the Java app
  "$DIR/jre/bin/java" -jar "$DIR/SftpClient.jar"

  echo "✅ Script finished at $(date)"
} >> "$LOG_FILE" 2>&1
