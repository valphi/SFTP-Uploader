package com.sismo.demo;

import com.sismo.demo.service.SftpConnectionManager;
import com.sismo.demo.service.SftpFileUploader;
import com.sismo.demo.utils.FileUtil;
import net.schmizz.sshj.SSHClient;

import java.io.File;

import static com.sismo.demo.Constants.LOCAL_MACRO_INDICATOR_DIRECTORY;
import static com.sismo.demo.Constants.LOCAL_PORTFOLIO_DIRECTORY;
import static com.sismo.demo.Constants.LOCAL_USER_INDICATOR_DIRECTORY;
import static com.sismo.demo.Constants.LOG_FILE_NAME;
import static com.sismo.demo.Constants.MACRO_INDICATOR_FILE_MAPPER_DEFAULT_OPERATION;
import static com.sismo.demo.Constants.MACRO_INDICATOR_MAPPER;
import static com.sismo.demo.Constants.PHRASE;
import static com.sismo.demo.Constants.PORTFOLIO_FILE_MAPPER;
import static com.sismo.demo.Constants.PORTFOLIO_FILE_MAPPER_DEFAULT_OPERATION;
import static com.sismo.demo.Constants.PRIVATE_KEY;
import static com.sismo.demo.Constants.SERVER;
import static com.sismo.demo.Constants.SFTP_MACRO_INDICATOR_DIRECTORY;
import static com.sismo.demo.Constants.SFTP_PORTFOLIO_DIRECTORY;
import static com.sismo.demo.Constants.SFTP_USER_INDICATOR_DIRECTORY;
import static com.sismo.demo.Constants.TO_ARCHIVE_FILES_IN_DIRECTORY;
import static com.sismo.demo.Constants.USER;
import static com.sismo.demo.Constants.USERS;
import static com.sismo.demo.Constants.USER_INDICATOR_FILE_MAPPER;
import static com.sismo.demo.Constants.USER_INDICATOR_FILE_MAPPER_DEFAULT_OPERATION;
import static com.sismo.demo.Constants.isMultiUserMode;
import static com.sismo.demo.utils.FileUtil.createArchiveFile;
import static com.sismo.demo.utils.FileUtil.prepareUserDirectories;
import static com.sismo.demo.utils.LogUtil.log;

public class SftpClient {

    public static void main(String[] args) {
        String curDir = System.getProperty("user.dir");
        File archiveLogFile = createArchiveFile(curDir);
        File logFile = new File(LOG_FILE_NAME);
        FileUtil.zipAndDeleteFile(logFile, archiveLogFile);

        if (isMultiUserMode()) {
            handleMultiUserMode();
        } else {
            handleSingleUserMode();
        }
    }

    private static void handleMultiUserMode() {
        log("Operating in multi-user mode with " + USERS.size() + " users", LOG_FILE_NAME);
        prepareUserDirectories();
        for (String user : USERS) {
            uploadFilesForUser(user);
        }
    }

    private static void handleSingleUserMode() {
        log("Operating in single-user mode for user: " + USER, LOG_FILE_NAME);
        uploadFilesForUser(USER);
    }

    private static void uploadFilesForUser(String user) {
        SftpConnectionManager connectionManager = new SftpConnectionManager(SERVER, user, PRIVATE_KEY, PHRASE);
        SftpFileUploader fileUploader = new SftpFileUploader(TO_ARCHIVE_FILES_IN_DIRECTORY);

        try (SSHClient ssh = connectionManager.connect()) {
            log("Connected to SFTP server: " + SERVER + " as user: " + user, LOG_FILE_NAME);

            String userIndicatorDir = isMultiUserMode() ? LOCAL_USER_INDICATOR_DIRECTORY + File.separator + user : LOCAL_USER_INDICATOR_DIRECTORY;
            String macroIndicatorDir = isMultiUserMode() ? LOCAL_MACRO_INDICATOR_DIRECTORY + File.separator + user : LOCAL_MACRO_INDICATOR_DIRECTORY;
            String portfolioDir = isMultiUserMode() ? LOCAL_PORTFOLIO_DIRECTORY + File.separator + user : LOCAL_PORTFOLIO_DIRECTORY;

            fileUploader.uploadFilesAndArchive(ssh, userIndicatorDir, SFTP_USER_INDICATOR_DIRECTORY, USER_INDICATOR_FILE_MAPPER, USER_INDICATOR_FILE_MAPPER_DEFAULT_OPERATION);
            fileUploader.uploadFilesAndArchive(ssh, macroIndicatorDir, SFTP_MACRO_INDICATOR_DIRECTORY, MACRO_INDICATOR_MAPPER, MACRO_INDICATOR_FILE_MAPPER_DEFAULT_OPERATION);
            fileUploader.uploadFilesAndArchive(ssh, portfolioDir, SFTP_PORTFOLIO_DIRECTORY, PORTFOLIO_FILE_MAPPER, PORTFOLIO_FILE_MAPPER_DEFAULT_OPERATION);

            log("Files uploaded successfully for user: " + user, LOG_FILE_NAME);
        } catch (Exception e) {
            log("Error processing user " + user + ": " + e.getMessage(), LOG_FILE_NAME);
        }
    }
}