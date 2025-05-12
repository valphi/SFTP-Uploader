## License

This project is licensed under the MIT License – see the [LICENSE](https://github.com/valphi/SFTP-Uploader/blob/main/LICENSE) file for details. You are free to use, modify, and distribute it, but it comes with no warranty and the authors take no liability for any damages.

---

# SftpClient Setup and Usage Guide (Windows)

This document provides all the necessary information to prepare, configure, and run `SftpClient.jar` on Windows. It also explains how to schedule the application to run daily at 05:00 AM and how to remove the scheduled task.

The `SftpClient` is a Java-based application that connects to an SFTP server and uploads it to a specified directory on the server.

---

## Prerequisites

1. **Java Runtime Environment (JRE):**
   - Ensure the `jre` folder is included in the same directory as the `SftpClient.jar`.

2. **Environment Variables:**
   - The `run_windows.bat` script requires several environment variables to be set. These variables must be adjusted to match your environment before running the script.

3. **Required Folders:**
   - The following folders in the root directory of the application can be used as default to upload files on the sftp server:
      - `user_indicator`: Used for storing user indicator files.
      - `macro_indicator`: Used for storing macro indicator files.
      - `portfolio`: Used for storing portfolio files.
   - These folders are automatically created during the zip creation process.
   - You can create your own folders and set the environment variables accordingly in the `run_windows.bat`.

---

## Environment Variables

### Examples and Explanations

#### `SFTP_PORTFOLIO_FILE_MAPPER`
- **Purpose**: Maps external IDs to portfolio file names. This is used to rename files before uploading them to the SFTP server.
- **Example**: `PortfolioExternalId1-Portfolio1,PortfolioExternalId2-Portfolio2`
    - `PortfolioExternalId1`: External ID of the first portfolio.
    - `Portfolio1`: Substring in the file name within the portfolio folder that matches the first portfolio.
    - `PortfolioExternalId2`: External ID of the second portfolio.
    - `Portfolio2`: Substring in the file name within the portfolio folder that matches the second portfolio.
- **Mock File Name**: `Portfolio1_Report.csv`
    - The substring `Portfolio1` in the file name matches the mapping `PortfolioExternalId1-Portfolio1`. The file is renamed to `PortfolioExternalId1-[timestamp]-[operation].csv` before being uploaded.

#### `SFTP_PORTFOLIO_FILE_MAPPER_DEFAULT_OPERATION`
- **Purpose**: Specifies the default operation for portfolio files. `F` means replace the file, and `M` means modify the file.
- **Example**: `F`
    - Files will be uploaded with the "replace" operation unless specified otherwise.

#### `SFTP_USER_INDICATOR_FILE_MAPPER`
- **Purpose**: Maps external IDs to user indicator file names. This is used to rename files before uploading them to the SFTP server.
- **Example**: `UserExternalId1-UserIndicator1,UserExternalId2-UserIndicator2`
    - `UserExternalId1`: External ID of the first user indicator.
    - `UserIndicator1`: Substring in the file name within the user indicator folder that matches the first user indicator.
    - `UserExternalId2`: External ID of the second user indicator.
    - `UserIndicator2`: Substring in the file name within the user indicator folder that matches the second user indicator.
- **Mock File Name**: `UserIndicator1_Data.csv`
    - The substring `UserIndicator1` in the file name matches the mapping `UserExternalId1-UserIndicator1`. The file is renamed to `UserExternalId1-[timestamp]-[operation].csv` before being uploaded. After that zip file will be created based on the csv file.

#### `SFTP_USER_INDICATOR_FILE_MAPPER_DEFAULT_OPERATION`
- **Purpose**: Specifies the default operation for user indicator files. `F` means replace the file, and `M` means modify the file.
- **Example**: `M`
    - Files will be uploaded with the "modify" operation unless specified otherwise.

#### `SFTP_MACRO_INDICATOR_FILE_MAPPER`
- **Purpose**: Maps external IDs to macro indicator file names. This is used to rename files before uploading them to the SFTP server.
- **Example**: `MacroExternalId1-MacroIndicator1,MacroExternalId2-MacroIndicator2`
    - `MacroExternalId1`: External ID of the first macro indicator.
    - `MacroIndicator1`: Substring in the file name within the macro indicator folder that matches the first macro indicator.
    - `MacroExternalId2`: External ID of the second macro indicator.
    - `MacroIndicator2`: Substring in the file name within the macro indicator folder that matches the second macro indicator.
- **Mock File Name**: `MacroIndicator1_Stats.csv`
    - The substring `MacroIndicator1` in the file name matches the mapping `MacroExternalId1-MacroIndicator1`. The file is renamed to `MacroExternalId1-[timestamp]-[operation].csv` before being uploaded.

#### `SFTP_MACRO_INDICATOR_FILE_MAPPER_DEFAULT_OPERATION`
- **Purpose**: Specifies the default operation for macro indicator files. `F` means replace the file, and `M` means modify the file.
- **Example**: `F`
    - Files will be uploaded with the "replace" operation unless specified otherwise.

---

### Notes for Users
- Ensure all environment variables are set correctly before running the application.
- The examples provided demonstrate how the variables are used to process, rename, and upload files to the SFTP server.
- If you encounter issues, check the log file (`log.txt`) in the application's directory for details.

Ensure these variables are configured to match your environment in the `run_windows.bat` script before running it.

---

## Steps to Run `SftpClient.jar` Manually

1. **Adjust Environment Variables:**
   - Open `run_windows.bat` in a text editor.
   - Update the environment variables with the correct values for your environment.

2. **Run the Script:**
   - Navigate to the directory containing `run_windows.bat`.
   - Click two times on the file to execute the script:
     ```cmd
     run_windows.bat
     ```

3. **Verify Execution:**
   - The script will check for the presence of the Java runtime and execute `SftpClient.jar`.

---

## Schedule `SftpClient.jar` to Run Daily at 05:00 AM

1. **Prepare the Script:**
   - Ensure `schedule_windows.bat` is in the same directory as `run_windows.bat`.

2. **Run the Scheduler Script:**
   - Navigate to the directory containing `schedule_windows.bat`.
   - Click two times on the file to execute the script:
     ```cmd
     schedule_windows.bat
     ```

3. **Verify the Task:**
   - Open Task Scheduler and check if the task `RunSftpClientDaily` is listed under "Task Scheduler Library".

4. **Log Files:**
   - The scheduled task writes logs to the following locations:
      - **Standard Output Log**: `C:\Windows\System32\Tasks\RunSftpClientDaily`
      - **Error Log**: If any errors occur, they will be displayed in the Task Scheduler history.

5. **Run Task Without User Login:**
   - To allow the task to run without requiring the user to log in:
     1. Open Task Scheduler.
     2. Locate the task `RunSftpClientDaily`.
     3. Right-click the task and select **Properties**.
     4. In the **General** tab, select **Run whether user is logged on or not**.
     5. Check the box **Do not store password** if you do not want to save the password (this may limit some functionality).
     6. If storing the password, uncheck the above box and click **OK**. You will be prompted to enter the user account password.
     7. Save the changes.

---

## Remove the Scheduled Task

1. **Run the Removal Script:**
   - Navigate to the directory containing `remove_schedule_windows.bat`.
   - Click two times on the file to execute the script:
     ```cmd
     remove_schedule_windows.bat
     ```

2. **Verify Removal:**
   - Open Task Scheduler and check if the task `RunSftpClientDaily` is no longer listed.

---

## Notes

- Ensure all `.bat` files (`run_windows.bat`, `schedule_windows.bat`, `remove_schedule_windows.bat`) are in the same directory as `SftpClient.jar` and the `jre` folder.
- If any issues occur, check the logs or error messages displayed in the Task Scheduler or Command Prompt.
- The folders `user_indicator`, `macro_indicator`, and `portfolio` are automatically created during the zip creation process or specified for the application in the `run_windows.bat` to function correctly.

---

## File Structure

The directory should have the following structure:
```
/your-directory
├── run_windows.bat
├── schedule_windows.bat
├── remove_schedule_windows.bat
├── SftpClient.jar
├── README_WINDOWS.md
├── README_WINDOWS.pdf
├── jre/
├── user_indicator/
├── macro_indicator/
├── portfolio/
```

---

## Troubleshooting

- **Java Runtime Not Found:**
   - Ensure the `jre` folder is present and contains the required Java runtime files.

- **Task Scheduler Errors:**
   - Ensure you have the necessary permissions to create or remove tasks in Task Scheduler.
   - If the task does not run, check the Task Scheduler history for error messages.

- **Environment Variable Issues:**
   - Double-check the values of the environment variables in `run_windows.bat` for correctness.