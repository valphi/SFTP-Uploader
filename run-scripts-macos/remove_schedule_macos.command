#!/bin/bash

# Define variables
PLIST_FILE=~/Library/LaunchAgents/com.user.sftpclient.plist

# Unload the plist file from launchd
if [ -f "$PLIST_FILE" ]; then
    launchctl unload "$PLIST_FILE"
    if [ $? -eq 0 ]; then
        echo "✅ Task unloaded successfully."
    else
        echo "❌ Failed to unload the task. Please check permissions."
    fi

    # Remove the plist file
    rm "$PLIST_FILE"
    if [ $? -eq 0 ]; then
        echo "✅ Task file removed successfully."
    else
        echo "❌ Failed to remove the task file. Please check permissions."
    fi
else
    echo "❌ Task file not found: $PLIST_FILE"
fi