#!/bin/bash

# Define variables
RUN_SCRIPT_PATH="$(cd "$(dirname "$0")" && pwd)/run_ubuntu.sh"

# Remove the cron job
crontab -l 2>/dev/null | grep -v "$RUN_SCRIPT_PATH" | crontab -

# Verify the cron job is removed
if crontab -l | grep -q "$RUN_SCRIPT_PATH"; then
    echo "❌ Failed to remove the scheduled task. Please check cron configuration."
else
    echo "✅ Scheduled task removed successfully."
fi