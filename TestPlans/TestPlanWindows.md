### Test Plan for SFTP Client Application on Windows

#### **Objective**
To verify the functionality, reliability, and performance of the SFTP Client application on Windows, ensuring it meets the requirements and handles all edge cases.

---

### **1. Prerequisites**
- Ensure the following are installed and configured:
    - Java Runtime Environment (JRE)
    - `SftpClient.jar` and `jre` folder in the same directory
    - Required environment variables (`SFTP_SERVER`, `SFTP_USER`, etc.) are set in `run_windows.bat`
    - Necessary folders (`user_indicator`, `macro_indicator`, `portfolio`) exist in the root directory
    - Access to a test SFTP server with valid credentials
    - Test files for upload in the respective folders

---

### **2. Main Test Scenarios**

#### **2.1 Environment Setup**
- **Test Case 1**: Verify that the `run_windows.bat` script sets all environment variables correctly.
    - **Steps**:
        1. Run `run_windows.bat`.
    - **Expected Result**: All variables are set without errors.

#### **2.2 File Upload Functionality**
- **Test Case 2**: Upload files from the `user_indicator` folder.
    - **Steps**:
        1. Place test files in the `user_indicator` folder.
        2. Run `run_windows.bat`.
    - **Expected Result**: Files are uploaded to the SFTP server and archived locally.

- **Test Case 3**: Upload files from the `macro_indicator` folder.
    - **Steps**:
        1. Place test files in the `macro_indicator` folder.
        2. Run `run_windows.bat`.
    - **Expected Result**: Files are uploaded to the SFTP server and archived locally.

- **Test Case 4**: Upload files from the `portfolio` folder.
    - **Steps**:
        1. Place test files in the `portfolio` folder.
        2. Run `run_windows.bat`.
    - **Expected Result**: Files are uploaded to the SFTP server and archived locally.

#### **2.3 File Mapping**
- **Test Case 5**: Verify file renaming based on `SFTP_PORTFOLIO_FILE_MAPPER`.
    - **Steps**:
        1. Configure `SFTP_PORTFOLIO_FILE_MAPPER` in `run_windows.bat`.
        2. Place test files in the `portfolio` folder with matching substrings.
        3. Run `run_windows.bat`.
    - **Expected Result**: Files are renamed and uploaded correctly.

- **Test Case 6**: Verify default operation (`F` or `M`) for file uploads.
    - **Steps**:
        1. Set `SFTP_PORTFOLIO_FILE_MAPPER_DEFAULT_OPERATION` to `F` or `M`.
        2. Run `run_windows.bat`.
    - **Expected Result**: Files are processed according to the specified operation.

#### **2.4 Error Handling**
- **Test Case 7**: Test with invalid SFTP credentials.
    - **Steps**:
        1. Set incorrect `SFTP_SERVER`, `SFTP_USER`, `SFTP_PHRASE` in `run_windows.bat`.
        2. Run `run_windows.bat`.
    - **Expected Result**: Application logs an error and exits gracefully.

- **Test Case 8**: Test with missing private key in `SFTP_PRIVATE_KEY` .
    - **Steps**:
        1. Remove the private key file.
        2. Run `run_windows.bat`.
    - **Expected Result**: Application logs an error and exits gracefully.

- **Test Case 9**: Test with missing folders (`user_indicator`, `macro_indicator`, `portfolio`).
    - **Steps**:
        1. Delete one or more folders or change DIRECTORY parameter values.
        2. Run `run_windows.bat`.
    - **Expected Result**: Application logs an error and exits gracefully.

#### **2.5 Scheduling**
- **Test Case 10**: Schedule the application to run daily at 05:00 AM.
    - **Steps**:
        1. Run `schedule_windows.bat`.
        2. Verify the task in Task Scheduler.
    - **Expected Result**: Task is created and runs at the specified time.

- **Test Case 11**: Remove the scheduled task.
    - **Steps**:
        1. Run `remove_schedule_windows.bat`.
        2. Verify the task is removed from Task Scheduler. 
    - **Expected Result**: Task is removed successfully.

#### **2.6 Logging**
- **Test Case 12**: Verify log file creation and content.
    - **Steps**:
        1. Run `run_windows.bat`.
        2. Check the `log.txt` file.
    - **Expected Result**: Log file contains detailed information about the execution.

#### **2.7 Performance**
- **Test Case 13**: Test with a large number of files.
    - **Steps**:
        1. Place 100+ files in one of the folders.
        2. Run `run_windows.bat`.
    - **Expected Result**: Application processes all files without crashing.

---

### **3. Additional Test Cases for SFTP Client Application on Windows**

#### **3.1 File Name Handling**
- **Test Case 14**: Test file names with allowed special characters.
    - **Steps**:
        1. Place files with names like `user@indicator.csv`, `macro#indicator.csv`, and `portfolio$file.csv` in the `user_indicator`, `macro_indicator` and `portfolio` folders.
        2. Run `run_windows.bat`.
    - **Expected Result**: Files are renamed, zipped, and uploaded successfully without errors.

- **Test Case 15**: Test file names with spaces.
    - **Steps**:
        1. Place files with names like `user indicator.csv` and `macro indicator.csv` in the `user_indicator` folder.
        2. Run `run_windows.bat`.
    - **Expected Result**: Files are renamed, zipped, and uploaded successfully without errors.

- **Test Case 16**: Test file names with long paths.
    - **Steps**:
        1. Place files with long names (e.g., `user_indicator_with_a_very_long_name_that_exceeds_255_characters_and_includes_additional_details_to_make_it_even_longer_for_testing_purposes_12345678901234567890123456789012345678901234567890123456789012345678901234567890.csv`) in the `user_indicator` folder.
        2. Run `run_windows.bat`.
    - **Expected Result**: Application handles long file names gracefully, renaming and uploading them without errors.

#### **3.2 Handling Files Left After Renaming or Zipping**
- **Test Case 17**: Verify behavior when files are renamed but not uploaded.
    - **Steps**:
        1. Place files in the `user_indicator` folder.
        2. Simulate a failure in the upload process (e.g., disconnect the SFTP server or simulate the file to be renamed and zipped).
        3. Run `run_windows.bat`.
    - **Expected Result**: Renamed files remain in the folder, and the application logs the failure. Run `run_windows.bat` again to check if the renamed files are uploaded successfully.

- **Test Case 18**: Verify behavior when files are zipped but not uploaded.
    - **Steps**:
        1. Place `.csv` files in the `user_indicator` folder.
        2. Simulate a failure in the upload process (e.g., disconnect the SFTP server or simulate the file to be renamed but not zipped).
        3. Run `run_windows.bat`.
    - **Expected Result**: Zipped files remain in the folder, and the application logs the failure. Run `run_windows.bat` again to check if the zipped files are uploaded successfully.

- **Test Case 19**: Retry uploading files left after renaming or zipping.
    - **Steps**:
        1. Place files in the `user_indicator` folder.
        2. Simulate a failure in the upload process.
        3. Fix the issue (e.g., reconnect the SFTP server).
        4. Run `run_windows.bat` again.
    - **Expected Result**: Remaining files are uploaded successfully.

#### **3.3 Invalid File Names**
- **Test Case 21**: Test file names with trailing dots or spaces.
    - **Steps**:
        1. Place files with names like `user_indicator. .csv` and `macro_indicator..csv` in the `user_indicator` and `macro_indicator` folders.
        2. Run `run_windows.bat`.
    - **Expected Result**: Application logs an error for invalid file names and skips processing them.

#### **3.4 Partial Uploads**
- **Test Case 22**: Test partial uploads due to network interruptions.
    - **Steps**:
        1. Place multiple files in the `user_indicator` folder (Use first 10 files from Tast Case 13).
        2. Simulate a network interruption during the upload process (Just close terminal windows).
        3. Run `run_windows.bat`.
    - **Expected Result**: Application logs the failure for the interrupted files and retries uploading them in the next run.

- **Test Case 23**: Test partial uploads with large files.
    - **Steps**:
        1. Place large files (e.g., 200MB+) in the `portfolio` folder.
        2. Simulate an interruption during the upload process (Just close terminal windows).
        3. Run `run_windows.bat`.
    - **Expected Result**: Application logs the failure for the interrupted files and retries uploading them in the next run.

#### **3.5 Logging and Cleanup**
- **Test Case 24**: Verify log entries for files left after renaming or zipping.
    - **Steps**:
        1. Place files in the `user_indicator` folder.
        2. Simulate a failure in the upload process.
        3. Run `run_windows.bat`.
        4. Check the `log.txt` file.
    - **Expected Result**: Log file contains detailed entries for files left after renaming or zipping.

- **Test Case 25**: Verify cleanup of temporary files.
    - **Steps**:
        1. Place files in the `user_indicator` folder.
        2. Run `run_windows.bat`.
        3. Check the folder for temporary files (e.g., partially zipped files).
    - **Expected Result**: No temporary files are left in the folder after successful execution.

---

### **4. Test Data**
- Test SFTP server credentials
- Sample files for `user_indicator`, `macro_indicator`, and `portfolio` folders
- File mapper configurations for testing renaming

---

### **5. Tools**
- Windows Command Prompt
- Task Scheduler
- SFTP client for verification
- Log file (`log.txt`) for debugging

---

### **6. Expected Outcome**
All test cases should pass without errors. Any failures should be logged and addressed.