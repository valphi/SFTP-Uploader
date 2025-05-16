## License

This project is licensed under the MIT License -- see the
[LICENSE](https://github.com/valphi/SFTP-Uploader/blob/main/LICENSE)
file for details. You are free to use, modify, and distribute it, but
the software is provided as is, without warranty. The authors accept no
liability for any damages.

# **SftpClient Setup and Usage Guide - Windows**

This guide explains how to prepare, configure, and run SftpClient.jar on
Windows. It also includes instructions for scheduling the application to
run daily at 05:00 AM and for removing the scheduled task.

**Note:** *SftpClient is a Java-based application that connects to an
SFTP server and uploads files to a specified directory.*

## **Prerequisites**

1.  **Java Runtime Environment (JRE)**

> Ensure the jre folder is included in the same directory as the
> SftpClient.jar.

2.  **Environment Variables**

> The run_windows.bat script requires several environment variables to
> be set. Update these to match your environment before execution.

3.  **Required Folders**

> The following folders in the root directory of the application
> (created during the zip packaging) are used by default for file
> uploads:

- user_indicator: for stock-related indicator files.

- macro_indicator: for macro indicator files.

- portfolio: for portfolio files.

> You may create your own folders and update the environment variables
> in run_windows.bat accordingly.

## **Environment Variables**

> Below are key environment variables, their purposes, and usage
> examples:

#### SFTP_PORTFOLIO_FILE_MAPPER {#sftp_portfolio_file_mapper-1}

- **Purpose**: Maps external IDs to substrings in file names for
  portfolios, enabling automatic renaming before upload.

- **Example**: *PortfolioExternalId1-Portfolio1*,
  *PortfolioExternalId2-Portfolio2*

- **Mock File Name**: "Portfolio1_Report.csv" 🡺 renamed to
  "PortfolioExternalId1-\[timestamp\]-\[operation\].csv"

> (The substring Portfolio1 in the file name matches the mapping
> PortfolioExternalId1-Portfolio1. The file is renamed to
> PortfolioExternalId1-\[timestamp\]-\[operation\].csv before being
> uploaded)

#### SFTP_PORTFOLIO_FILE_MAPPER_DEFAULT_OPERATION

- **Purpose**: Specifies the default operation for portfolio files.

  - F = replace file and delete history

  - M = modify file\
    **Example:** M

> Files will be uploaded with the "modify" (update) operation unless
> specified otherwise.

#### SFTP_USER_INDICATOR_FILE_MAPPER

- **Purpose**: Same logic as for portfolios, but applies to user
  indicator files.

- **Example**: *UserExternalId1-UserIndicator1*,
  *UserExternalId2-UserIndicator2*

- **Mock File Name**: "UserIndicator1_Data.csv" 🡺 renamed to
  "UserExternalId1-\[timestamp\]-\[operation\].csv" and zipped before
  upload

#### SFTP_USER_INDICATOR_FILE_MAPPER_DEFAULT_OPERATION

- **Purpose**: Default operation for user indicator files.

- **Example**: M

Files will be uploaded with the "modify" operation unless specified
otherwise.

#### SFTP_MACRO_INDICATOR_FILE_MAPPER

- **Purpose**: Same mapping logic, applied to macro indicator files.

- **Example**: *MacroExternalId1-MacroIndicator1*,
  *MacroExternalId2-MacroIndicator2*

<!-- -->

- **Mock File Name**: "MacroIndicator1_Stats.csv" 🡺 renamed to
  "MacroExternalId1-\[timestamp\]-\[operation\].csv"

#### SFTP_MACRO_INDICATOR_FILE_MAPPER_DEFAULT_OPERATION

- **Purpose**: Default operation for macro indicator files. F means
  replace the file, and M means modify the file.

- **Example**: F

Files will be uploaded with the "replace" operation unless specified
otherwise.

> ✅ *Ensure all environment variables are correctly set in
> run_windows.bat before running the application.* If you encounter
> issues, check the log file (log.txt) in the application's directory
> for details.

## **Manual Execution Steps**

1.  **Edit the Script**

> Open run_windows.bat in a text editor (through right click menu) and
> update environment variables with values specific to your setup.

2.  **Run the Script**

> Double-click run_windows.bat to launch the application.

3.  **Verify Execution**

> The script checks for the Java runtime and executes SftpClient.jar.

## **Scheduling the Application (Daily at 05:00 AM)**

1.  **Prepare the Scheduler Script**

> [Confirm that schedule_windows.bat is in the same folder as
> run_windows.bat.]{.underline}

2.  **Run the Scheduler**

> Double-click schedule_windows.bat.

3.  **Verify Scheduled Task**

> Open Task Scheduler and look under **Task Scheduler Library** for the
> task:
>
> **RunSftpClientDaily**

4.  **Log Locations**

> **Standard Output:** C:\\Windows\\System32\\Tasks\\RunSftpClientDaily
>
> **Errors:** Shown in Task Scheduler\'s History tab

5.  **Run Task Without User Login**

> Open Task Scheduler → Right-click RunSftpClientDaily → Properties
>
> In General tab:

- Select **Run whether user is logged on or not**

- If you don't wish to store a password, check **Do not store password**
  (may limit functionality)

- Otherwise, leave unchecked and enter credentials when prompted

## **Removing the Scheduled Task**

1.  **Run the Removal Script**

> Double-click **remove_schedule_windows.bat**

2.  **Verify Removal**

> Open Task Scheduler and confirm **RunSftpClientDaily** is no longer
> listed.

## **Important Notes**

- All .bat scripts (run_windows.bat, schedule_windows.bat,
  remove_schedule_windows.bat) must be in the same directory as
  SftpClient.jar and the jre folder.

- The folders user_indicator, macro_indicator, and portfolio are created
  during packaging. You may use custom folders by updating
  run_windows.bat.

- If you encounter errors, check logs in the Task Scheduler or in
  log.txt.

## **Directory Structure**

The directory should have the following structure:

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

## **Troubleshooting**

- **Java Runtime Not Found**

> Ensure the jre folder is present and correctly populated.

- **Task Scheduler Errors**

> Confirm you have the required permissions. Check the History tab for
> details.

- **Incorrect Environment Variables**

> Double-check the variables in run_windows.bat for typos or invalid
> values.
