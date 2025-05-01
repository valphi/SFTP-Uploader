```markdown
## License

This project is licensed under the MIT License – see the [LICENSE](LICENSE) file for details. You are free to use, modify, and distribute it, but it comes with no warranty and the authors take no liability for any damages.

---

# SftpClient Setup and Usage Guide (Windows)

This document provides all the necessary information to prepare, configure, and run `SftpClient.jar` on Windows. It also explains how to schedule the application to run daily at 05:00 AM and how to remove the scheduled task.

The `SftpClient` is a Java-based application that connects to an SFTP server, generates a CSV file with portfolio data, and uploads it to a specified directory on the server.

---

## Prerequisites

1. **Java Runtime Environment (JRE):**
   - Ensure the `jre` folder is included in the same directory as the `SftpClient.jar`.

2. **Environment Variables:**
   - The `run_windows.bat` script requires several environment variables to be set. These variables must be adjusted to match your environment before running the script.

3. **Required Folders:**
   - The following folders in the root directory of the application can be used as default:
      - `user_indicator`: Used for storing user indicator files.
      - `macro_indicator`: Used for storing macro indicator files.
      - `portfolio`: Used for storing portfolio files.
   - These folders are automatically created during the zip creation process.
   - You can create your own folders and set the environment variables accordingly in the `run_windows.bat`.

---

## Environment Variables

Below is a description of the environment variables used in `run_windows.bat`:

| Variable Name                          | Description                                                                                       |
|----------------------------------------|---------------------------------------------------------------------------------------------------|
| `SFTP_SERVER`                          | The hostname or IP address of the SFTP server.                                                   |
| `SFTP_USER`                            | The username for authentication on the SFTP server.                                              |
| `SFTP_PHRASE`                          | The password or passphrase for authentication.                                                   |
| `SFTP_PRIVATE_KEY`                     | The path to the private key file for key-based authentication.                                   |
| `SFTP_LOCAL_USER_INDICATOR_DIRECTORY`  | The local directory where user indicator files are stored.                                       |
| `SFTP_LOCAL_MACRO_INDICATOR_DIRECTORY` | The local directory where macro indicator files are stored.                                      |
| `SFTP_LOCAL_PORTFOLIO_DIRECTORY`       | The local directory where portfolio files are stored.                                            |
| `SFTP_PORTFOLIO_FILE_MAPPER`           | A comma-separated list of portfolio file mappings (e.g., `123AB-IsinHistory5,124AB-IsinHistory6`).|
| `SFTP_PORTFOLIO_FILE_MAPPER_DEFAULT_OPERATION` | The default operation for portfolio files (`F` for replace, `M` for modify).              |
| `SFTP_USER_INDICATOR_FILE_MAPPER`      | A comma-separated list of user indicator file mappings.                                          |
| `SFTP_USER_INDICATOR_FILE_MAPPER_DEFAULT_OPERATION` | The default operation for user indicator files (`F` or `M`).                         |
| `SFTP_MACRO_INDICATOR_FILE_MAPPER`     | A comma-separated list of macro indicator file mappings.                                         |
| `SFTP_MACRO_INDICATOR_FILE_MAPPER_DEFAULT_OPERATION` | The default operation for macro indicator files (`F` or `M`).                       |

Ensure these variables are configured to match your environment in the `run_windows.bat` script before running it.

---

## Steps to Run `SftpClient.jar` Manually

1. **Adjust Environment Variables:**
   - Open `run_windows.bat` in a text editor.
   - Update the environment variables with the correct values for your environment.

2. **Run the Script:**
   - Open a Command Prompt (cmd.exe).
   - Navigate to the directory containing `run_windows.bat`.
   - Execute the script:
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
   - Open a Command Prompt (cmd.exe).
   - Navigate to the directory containing `schedule_windows.bat`.
   - Execute the script:
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
   - Open a Command Prompt (cmd.exe).
   - Navigate to the directory containing `remove_schedule_windows.bat`.
   - Execute the script:
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
```