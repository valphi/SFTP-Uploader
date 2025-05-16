# 🤖 GitHub Copilot Instructions for SftpClient (Java 21, Gradle)

## 📌 Project Summary

SftpClient is a cross-platform Java 21 application for securely uploading files to an SFTP server. It is distributed as a pre-packaged zip for Windows, macOS, and Ubuntu, each including the Java runtime, scripts, and documentation. The app is CLI-based (no GUI) and uses environment variables for all runtime configuration.

---

## ✅ Copilot Goals

- Generate concise, idiomatic Java 21 code
- Use Gradle for builds and dependency management
- Ensure cross-platform compatibility (Windows, macOS, Ubuntu)
- Implement secure SFTP file upload using SSHJ (SSH key authentication preferred)
- Modularize logic: utilities, services, constants, and main entrypoint
- Use custom file-based logging (LogUtil) for all actions and errors
- Rely on environment variables for configuration (never hardcode credentials)
- Follow Java best practices for error handling and resource management

---

## ⚙️ Key Features

1. **SFTP Upload**
   - Connect using SSH key authentication (no hardcoded secrets)
   - Upload files from `user_indicator`, `macro_indicator`, and `portfolio` directories
   - Rename files based on mapping rules from environment variables
   - Archive processed files after upload

2. **Configuration**
   - All runtime config via environment variables (see below)
   - File mapping and default operations are configurable per file type

3. **Cross-Platform Packaging**
   - Scripts for building, copying, and zipping artifacts for each OS
   - Each distribution includes correct Java runtime, scripts, and documentation

4. **Task Scheduling**
   - Scripts for scheduling and removing scheduled tasks on all platforms

5. **Logging**
   - All actions and errors logged to `log.txt` (never log sensitive info)

---

## ❌ What to Avoid

- No GUI (no JavaFX/Swing)
- No database or cloud integration
- No AI or natural language features
- Never persist or log credentials

---

## 📁 Project Structure

```plaintext
src/main/java/com/sismo/demo/
  SftpClient.java           # Main entrypoint
  Constants.java            # All config/environment variables
  utils/LogUtil.java        # File-based logging
  utils/FileUtils.java      # File/directory operations
  service/SftpService.java  # SFTP upload logic
  ...
scripts/                   # OS-specific build/run/schedule scripts
jre/                       # Java runtime (per package)
user_indicator/
macro_indicator/
portfolio/
README_WINDOWS.md
README_MACOS.md
README_UBUNTU.md
README.md
build.gradle
```

---

## 🛠️ Environment Variables

- `SFTP_SERVER`, `SFTP_USER`, `SFTP_PRIVATE_KEY`, `SFTP_PHRASE`
- `SFTP_LOCAL_USER_INDICATOR_DIRECTORY`, `SFTP_LOCAL_MACRO_INDICATOR_DIRECTORY`, `SFTP_LOCAL_PORTFOLIO_DIRECTORY`
- `SFTP_PORTFOLIO_FILE_MAPPER`, `SFTP_PORTFOLIO_FILE_MAPPER_DEFAULT_OPERATION`
- `SFTP_USER_INDICATOR_FILE_MAPPER`, `SFTP_USER_INDICATOR_FILE_MAPPER_DEFAULT_OPERATION`
- `SFTP_MACRO_INDICATOR_FILE_MAPPER`, `SFTP_MACRO_INDICATOR_FILE_MAPPER_DEFAULT_OPERATION`

---

## 🧪 Testing

- Use JUnit 5 for unit tests
- Test config parsing, file listing, and SFTP upload logic (mocked)

---

## 🔍 Copilot Tips

- Use `java.nio.file` for file operations
- Use try-with-resources for streams and SFTP sessions
- Validate all inputs, especially file paths and server responses
- Modularize logic into small, testable methods
- Ask for clarification if requirements are ambiguous
- Please do the changes in agent mode without asking for confirmation only if you are sure 95%. If not, please ask for clarification.

---

Thank you for helping build a robust, secure, and maintainable SFTP client! 🔐📁