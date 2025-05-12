#!/bin/bash

# Define variables
ZIP_FILE="archive_windows.zip"
JRE_ORIGINAL_FOLDER="jre_21_windows" # Windows-specific JRE folder
JRE_TEMP_FOLDER="jre"

cd ..

# Copy Windows batch files to root directory
cp run-scripts-windows/run_windows.bat .
cp run-scripts-windows/schedule_windows.bat .
cp run-scripts-windows/remove_schedule_windows.bat .

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
cp pdf-readme/README_WINDOWS.pdf README_WINDOWS.pdf

# Create the zip file
zip -r "$ZIP_FILE" run_windows.bat schedule_windows.bat remove_schedule_windows.bat SftpClient.jar "$JRE_TEMP_FOLDER" user_indicator macro_indicator portfolio README_WINDOWS.md README_WINDOWS.pdf
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
rm README_WINDOWS.pdf

# Remove Windows batch files from root directory
rm run_windows.bat
rm schedule_windows.bat
rm remove_schedule_windows.bat

echo "✅ Folder name restored to '$JRE_ORIGINAL_FOLDER'."