#!/bin/bash

# Define variables
RUN_SCRIPT_PATH="$(cd "$(dirname "$0")" && pwd)/run_ubuntu.sh"
CRON_JOB="0 5 * * * /bin/bash $RUN_SCRIPT_PATH"

# Ensure the run_ubuntu.sh script is executable
chmod +x "$RUN_SCRIPT_PATH"

# Add the cron job
(crontab -l 2>/dev/null | grep -v "$RUN_SCRIPT_PATH"; echo "$CRON_JOB") | crontab -

# Verify the cron job is added
if crontab -l | grep -q "$RUN_SCRIPT_PATH"; then
    echo "✅ Scheduled run_ubuntu.sh to execute daily at 05:00 AM."
else
    echo "❌ Failed to schedule run_ubuntu.sh. Please check cron configuration."
fi