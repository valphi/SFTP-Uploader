## License

This project is licensed under the MIT License - see the [LICENSE](https://github.com/valphi/SFTP-Uploader/blob/main/LICENSE) file for details. You are free to use, modify, and distribute it, but the software is provided as is, without warranty. The authors accept no liability for any damages.

---

# SftpClient Setup and Usage Guide - Ubuntu

This guide explains how to prepare, configure, and run SftpClient.jar on Ubuntu. It also includes instructions for scheduling the application to run daily at 05:00 AM and for removing the scheduled task.

**Note:** *SftpClient is a Java-based application that connects to an SFTP server and uploads files to a specified directory.*

---

## Prerequisites

1. **Java Runtime Environment (JRE)**
   Ensure the jre folder is included in the same directory as the SftpClient.jar.

2. **Environment Variables**
   The run_ubuntu.sh script requires several environment variables to be set. Update these to match your environment before execution.

3. **Required Folders**
   The following folders in the root directory of the application (created during the zip packaging) are used by default for file uploads:
    - `user_indicator`: for stock-related indicator files.
    - `macro_indicator`: for macro indicator files.
    - `portfolio`: for portfolio files.

You may create your own folders and update the environment variables in run_ubuntu.sh accordingly.

---

## Environment Variables

Below are key environment variables, their purposes, and usage examples:

#### SFTP_PORTFOLIO_FILE_MAPPER

- **Purpose**: Maps external IDs to substrings in file names for portfolios, enabling automatic renaming before upload.
- **Example**: *PortfolioExternalId1-Portfolio1*, *PortfolioExternalId2-Portfolio2*
- **Mock File Name**: `Portfolio1_Report.csv` renamed to `PortfolioExternalId1-[timestamp]-[operation].csv`
  (The substring Portfolio1 in the file name matches the mapping PortfolioExternalId1-Portfolio1. The file is renamed to `PortfolioExternalId1-[timestamp]-[operation].csv` before being uploaded)

#### SFTP_PORTFOLIO_FILE_MAPPER_DEFAULT_OPERATION

- **Purpose**: Specifies the default operation for portfolio files.
    - F = replace file and delete history
    - M = modify file
- **Example:** M
  Files will be uploaded with the "modify" (update) operation unless specified otherwise.

#### SFTP_USER_INDICATOR_FILE_MAPPER

- **Purpose**: Same logic as for portfolios, but applies to user indicator files.
- **Example**: *UserExternalId1-UserIndicator1*, *UserExternalId2-UserIndicator2*
- **Mock File Name**: `UserIndicator1_Data.csv` renamed to `UserExternalId1-[timestamp]-[operation].csv` and zipped before upload

#### SFTP_USER_INDICATOR_FILE_MAPPER_DEFAULT_OPERATION

- **Purpose**: Default operation for user indicator files.
- **Example**: M
  Files will be uploaded with the "modify" operation unless specified otherwise.

#### SFTP_MACRO_INDICATOR_FILE_MAPPER

- **Purpose**: Same mapping logic, applied to macro indicator files.
- **Example**: *MacroExternalId1-MacroIndicator1*, *MacroExternalId2-MacroIndicator2*
- **Mock File Name**: `MacroIndicator1_Stats.csv` 🡺 renamed to `MacroExternalId1-[timestamp]-[operation].csv`

#### SFTP_MACRO_INDICATOR_FILE_MAPPER_DEFAULT_OPERATION

- **Purpose**: Default operation for macro indicator files. F means replace the file, and M means modify the file.
- **Example**: F
  Files will be uploaded with the "replace" operation unless specified otherwise.

✅ *Ensure all environment variables are correctly set in run_ubuntu.bat before running the application.* If you encounter issues, check the log file (`log.txt`) in the application's directory for details.

---

## **Manual Execution Steps**

1. **Edit the Script**

   ⚠️️ Open run_ubuntu.sh in a text editor and update environment variables with values specific to your setup.

2. **Make the File Executable**:
    - Open the terminal and navigate to the directory containing `run_ubuntu.sh`.
    - Run the following command to ensure it is executable:
   ```bash
      chmod +x run_ubuntu.sh
   ```

3. **Run the Script:**
    - Execute the script:
      ```bash
      ./run_ubuntu.sh
      ```

3. **Verify Execution:**
    - The script will check for the presence of the Java runtime and execute `SftpClient.jar`.

---

## Schedule `SftpClient.jar` to Run Daily at 05:00 AM

1. **Prepare the Script:**
    - Ensure `schedule_ubuntu.sh` is in the same directory as `run_ubuntu.sh`.

2. **Run the Scheduler Script:**
    - Execute `schedule_ubuntu.sh` to create a cron job:
      ```bash
      ./schedule_ubuntu.sh
      ```

3. **Verify the Task:**
    - Check the cron jobs to confirm the task is scheduled:
      ```bash
      crontab -l
      ```

---

## Remove the Scheduled Task

1. **Run the Removal Script:**
    - Execute `remove_schedule_ubuntu.sh` to remove the cron job:
      ```bash
      ./remove_schedule_ubuntu.sh
      ```

2. **Verify Removal:**
    - Check the cron jobs to confirm the task is removed:
      ```bash
      crontab -l
      ```

---

## Schedule `SftpClient.jar` to Run Daily at 05:00 AM

1. **Prepare the Script:**
    - Ensure `schedule_ubuntu.sh` is in the same directory as `run_ubuntu.sh`.

2. **Run the Scheduler Script:**
    - Execute `schedule_ubuntu.sh` to create a `cron` task:
      ```bash
      ./schedule_ubuntu.sh
      ```

3. **Verify the Task:**
    - Check if the task is loaded:
      ```bash
      crontab -l | grep run_ubuntu.sh
      ```

---

## Remove the Scheduled Task

1. **Run the Removal Script:**
    - Execute `remove_schedule_ubuntu.sh` to remove the `cron` task:
      ```bash
      ./remove_schedule_ubuntu.sh
      ```

2. **Verify Removal:**
    - Check if the task is unloaded:
      ```bash
      crontab -l | grep run_ubuntu.sh
      ```

---

## **Important Notes**
⚠️⚠️⚠️

- All .sh scripts (`run_ubuntu.sh`, `schedule_ubuntu.sh`, `remove_schedule_ubuntu.sh`) must be in the same directory as `SftpClient.jar` and the `jre` folder.
- The folders `user_indicator`, `macro_indicator`, and `portfolio` are created during packaging. You may use custom folders by updating `run_ubuntu.sh`.
- If you encounter errors, check logs in the Task Scheduler or in `log.txt`.

---

## File Structure

The directory should have the following structure:
```
/your-directory
├── run_ubuntu.sh
├── schedule_ubuntu.sh
├── remove_schedule_ubuntu.sh
├── SftpClient.jar
├── README_UBUNTU.md
├── README_UBUNTU.pdf
├── jre/
├── user_indicator/
├── macro_indicator/
├── portfolio/
```

---

## Troubleshooting

- **Java Runtime Not Found**
  Ensure the jre folder is present and correctly populated.

- **Task Scheduler Errors**
  Confirm you have the required permissions. Check the History tab for details.

- **Incorrect Environment Variables**
  Double-check the variables in run_ubuntu.sh for typos or invalid values.