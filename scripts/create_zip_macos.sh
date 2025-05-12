#!/bin/bash

# Define variables
ZIP_FILE="archive_macos.zip"
JRE_ORIGINAL_FOLDER="jre_21_macos" # macOS-specific JRE folder
JRE_TEMP_FOLDER="jre"

cd ..

# Copy macOS command files to root directory
cp run-scripts-macos/run_macos.command .
cp run-scripts-macos/schedule_macos.command .
cp run-scripts-macos/remove_schedule_macos.command .

# Remove the previous zip file if it exists
if [ -f "$ZIP_FILE" ]; then
    rm "$ZIP_FILE"
    echo "✅ Previous zip file '$ZIP_FILE' removed."
fi

# Build the project using Gradle
./gradlew build

# Copy the JAR file to the root directory, replacing it if it exists
if [ -f "build/libs/SftpClient.jar" ]; then
    cp "build/libs/SftpClient.jar" "SftpClient.jar"
    echo "✅ SftpClient.jar copied to root directory."
else
    echo "❌ SftpClient.jar not found in /build/libs."
    exit 1
fi

# Check if the original JRE folder exists
if [ ! -d "$JRE_ORIGINAL_FOLDER" ]; then
    echo "❌ Folder '$JRE_ORIGINAL_FOLDER' not found."
    exit 1
fi

# Rename the JRE folder to "jre"
mv "$JRE_ORIGINAL_FOLDER" "$JRE_TEMP_FOLDER"
if [ $? -ne 0 ]; then
    echo "❌ Failed to rename folder '$JRE_ORIGINAL_FOLDER' to '$JRE_TEMP_FOLDER'."
    exit 1
fi

# Create temporary directories if they don't exist
TEMP_DIRS=()
for dir in user_indicator macro_indicator portfolio; do
    if [ ! -d "$dir" ]; then
        mkdir "$dir"
        TEMP_DIRS+=("$dir")
    fi
done

# Copy README files to the root directory
cp pdf-readme/README_MACOS.pdf README_MACOS.pdf

# Create the zip file
zip -r "$ZIP_FILE" run_macos.command schedule_macos.command remove_schedule_macos.command SftpClient.jar "$JRE_TEMP_FOLDER" user_indicator macro_indicator portfolio README_MACOS.md README_MACOS.pdf
if [ $? -eq 0 ]; then
    echo "✅ Zip file '$ZIP_FILE' created successfully."
else
    echo "❌ Failed to create zip file '$ZIP_FILE'."
    mv "$JRE_TEMP_FOLDER" "$JRE_ORIGINAL_FOLDER"
    for dir in "${TEMP_DIRS[@]}"; do
        rm -r "$dir"
    done
    exit 1
fi

# Restore the original folder name
mv "$JRE_TEMP_FOLDER" "$JRE_ORIGINAL_FOLDER"
if [ $? -ne 0 ]; then
    echo "❌ Failed to restore folder name from '$JRE_TEMP_FOLDER' to '$JRE_ORIGINAL_FOLDER'."
    exit 1
fi

# Remove temporary directories
for dir in "${TEMP_DIRS[@]}"; do
    rm -r "$dir"
done

# Remove README files from the root directory
rm README_MACOS.pdf

# Remove macOS command files from root directory
rm run_macos.command
rm schedule_macos.command
rm remove_schedule_macos.command

echo "✅ Folder name restored to '$JRE_ORIGINAL_FOLDER'."