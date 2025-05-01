
```markdown
## License

This project is licensed under the MIT License – see the [LICENSE](LICENSE) file for details. You are free to use, modify, and distribute it, but it comes with no warranty and the authors take no liability for any damages.

---

# SftpClient Setup and Usage Guide (macOS)

This document provides all the necessary information to prepare, configure, and run `SftpClient.jar` on macOS. It also explains how to schedule the application to run daily at 05:00 AM and how to remove the scheduled task. 

The `SftpClient` is a Java-based application that connects to an SFTP server, generates a CSV file with portfolio data, and uploads it to a specified directory on the server.

---

## Prerequisites

1. **Java Runtime Environment (JRE):**
    - Ensure the `jre` folder is included in the same directory as the `SftpClient.jar`.

2. **Environment Variables:**
    - The `run_macos.sh` script requires several environment variables to be set. These variables must be adjusted to match your environment before running the script.

3. **Required Folders:**
   - The following folders in the root directory of the application can be used as default:
      - `user_indicator`: Used for storing user indicator files.
      - `macro_indicator`: Used for storing macro indicator files.
      - `portfolio`: Used for storing portfolio files.
   - These folders are automatically created during the zip creation process.
   - You can create your own folders and set the environment variables accordingly in the run_macos.bat.

---

## Environment Variables

Below is a description of the environment variables used in `run_macos.sh`:

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

Ensure these variables are configured to match your environment in the 'run_macos.sh' script before running it.

---
## Steps to Run `SftpClient.jar` Manually using Finder

1. **Make the File Executable**:
    - Open the terminal and navigate to the directory containing `run_macos.command`.
    - Run the following command to ensure it is executable:
      ```bash
      chmod +x run_macos.command
      ```

2. **Run the File**:
    - Double-click the `run_macos.command` file in Finder.
    - A terminal window will open, and the script will execute.

3. **Check Logs**:
    - The script writes logs to `/tmp/sftpclient_debug.log`.
    - Open the log file to verify the execution or debug any issues:
      ```bash
      cat /tmp/sftpclient_debug.log
      ```


## Steps to Run `SftpClient.jar` Manually using Terminal

1. **Adjust Environment Variables:**
    - Open `run_macos.sh` in a text editor.
    - Update the environment variables with the correct values for your environment.

2. **Run the Script:**
    - Make the script executable:
      ```bash
      chmod +x run_macos.sh
      ```
    - Execute the script:
      ```bash
      ./run_macos.sh
      ```

3. **Verify Execution:**
    - The script will check for the presence of the Java runtime and execute `SftpClient.jar`.

---

## Schedule `SftpClient.jar` to Run Daily at 05:00 AM

1. **Prepare the Script:**
    - Ensure `schedule_macos.sh` is in the same directory as `run_macos.sh`.

2. **Run the Scheduler Script:**
    - Execute `schedule_macos.sh` to create a `launchd` task:
      ```bash
      ./schedule_macos.sh
      ```

3. **Verify the Task:**
    - Check if the task is loaded:
      ```bash
      launchctl list | grep com.user.sftpclient
      ```

---

## Remove the Scheduled Task

1. **Run the Removal Script:**
    - Execute `remove_schedule_macos.sh` to remove the `launchd` task:
      ```bash
      ./remove_schedule_macos.sh
      ```

2. **Verify Removal:**
    - Check if the task is unloaded:
      ```bash
      launchctl list | grep com.user.sftpclient
      ```

---

## Notes

- Ensure all `.sh` files (`run_macos.sh`, `schedule_macos.sh`, `remove_schedule_macos.sh`) are in the same directory as `SftpClient.jar` and the `jre` folder.
- If any issues occur, check the logs or error messages displayed in the terminal.
- The folders `user_indicator`, `macro_indicator`, and `portfolio` are automatically created during the zip creation process or specified for the application in the `run_macos.sh` to function correctly.

---

## File Structure

The directory should have the following structure:
    ```
/your-directory
├── run_macos.sh
├── schedule_macos.sh
├── remove_schedule_macos.sh
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
- **Launchd Errors:**
    - Ensure you have the necessary permissions to create or remove `launchd` tasks.
- **Environment Variable Issues:**
    - Double-check the values of the environment variables in `run_macos.sh` for correctness.
```