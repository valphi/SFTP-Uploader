#!/bin/bash

PLIST_FILE="$HOME/Library/LaunchAgents/com.user.sftpclient.plist"
SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
RUN_SCRIPT="$SCRIPT_DIR/run_macos.command"

# Ensure script is executable
chmod +x "$RUN_SCRIPT"

# Remove quarantine flags
xattr -rd com.apple.quarantine "$SCRIPT_DIR" 2>/dev/null

# Create plist
cat <<EOF > "$PLIST_FILE"
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE plist PUBLIC "-//Apple//DTD PLIST 1.0//EN" "http://www.apple.com/DTDs/PropertyList-1.0.dtd">
<plist version="1.0">
<dict>
    <key>Label</key>
    <string>com.user.sftpclient</string>
    <key>ProgramArguments</key>
    <array>
        <string>/bin/bash</string>
        <string>$RUN_SCRIPT</string>
    </array>
    <key>StartCalendarInterval</key>
    <dict>
        <key>Hour</key>
        <integer>5</integer>
        <key>Minute</key>
        <integer>0</integer>
    </dict>
    <key>StandardOutPath</key>
    <string>/tmp/sftpclient_\$(date '+%Y-%m-%d_%H-%M-%S').log</string>
    <key>StandardErrorPath</key>
    <string>/tmp/sftpclient_error_\$(date '+%Y-%m-%d_%H-%M-%S').log</string>
</dict>
</plist>
EOF

# Reload task
launchctl unload "$PLIST_FILE" 2>/dev/null
launchctl load "$PLIST_FILE"

# Confirm
if launchctl list | grep -q "com.user.sftpclient"; then
  echo "✅ Scheduled successfully."
else
  echo "❌ Scheduling failed. Check permissions or logs."
fi
