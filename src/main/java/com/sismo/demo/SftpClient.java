package com.sismo.demo;

import com.sismo.demo.service.FileProcessingStats;
import com.sismo.demo.service.SftpConnectionManager;
import com.sismo.demo.service.SftpFileUploader;
import net.schmizz.sshj.SSHClient;

import java.io.IOException;

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
import static com.sismo.demo.Constants.USERS;
import static com.sismo.demo.Constants.USER_INDICATOR_FILE_MAPPER;
import static com.sismo.demo.Constants.USER_INDICATOR_FILE_MAPPER_DEFAULT_OPERATION;
import static com.sismo.demo.Constants.isMultiUserMode;
import static com.sismo.demo.utils.FileUtil.prepareUserDirectories;
import static com.sismo.demo.utils.LogUtil.log;

public class SftpClient {
    public static void main(String[] args) {
        if (isMultiUserMode()) {
            prepareUserDirectories();
        }
        FileProcessingStats aggregatedStats = new FileProcessingStats();
        for (String user : USERS) {
            log("\n===== Processing files for user (" + user + ") ======", LOG_FILE_NAME);
            SSHClient ssh = null;
            try {
                SftpConnectionManager connectionManager = new SftpConnectionManager(SERVER, user, PRIVATE_KEY, PHRASE);
                ssh = connectionManager.connect();
                SftpFileUploader uploader = new SftpFileUploader(TO_ARCHIVE_FILES_IN_DIRECTORY);
                FileProcessingStats portfolioStats = new FileProcessingStats();
                processDirectory(user, ssh, uploader, LOCAL_PORTFOLIO_DIRECTORY, SFTP_PORTFOLIO_DIRECTORY, PORTFOLIO_FILE_MAPPER, PORTFOLIO_FILE_MAPPER_DEFAULT_OPERATION, portfolioStats);
                aggregatedStats.merge(portfolioStats);
                FileProcessingStats userIndicatorStats = new FileProcessingStats();
                processDirectory(user, ssh, uploader, LOCAL_USER_INDICATOR_DIRECTORY, SFTP_USER_INDICATOR_DIRECTORY, USER_INDICATOR_FILE_MAPPER, USER_INDICATOR_FILE_MAPPER_DEFAULT_OPERATION, userIndicatorStats);
                aggregatedStats.merge(userIndicatorStats);
                FileProcessingStats macroIndicatorStats = new FileProcessingStats();
                processDirectory(user, ssh, uploader, LOCAL_MACRO_INDICATOR_DIRECTORY, SFTP_MACRO_INDICATOR_DIRECTORY, MACRO_INDICATOR_MAPPER, MACRO_INDICATOR_FILE_MAPPER_DEFAULT_OPERATION, macroIndicatorStats);
                aggregatedStats.merge(macroIndicatorStats);

            } catch (IOException e) {
                log("Error connecting to SFTP server for user: " + user, e, LOG_FILE_NAME);
            } finally {
                if (ssh != null) {
                    try {
                        ssh.disconnect();
                    } catch (IOException e) {
                        log("Error disconnecting from SFTP server", e, LOG_FILE_NAME);
                    }
                }
            }
        }
        log("\n" + aggregatedStats.generateAggregatedSummaryReport(), LOG_FILE_NAME);
    }

    private static void processDirectory(String user, SSHClient ssh, SftpFileUploader uploader,
                                         String localBaseDir, String remoteDir,
                                         String fileMapper, String defaultOperation,
                                         FileProcessingStats stats) {
        String localDir = isMultiUserMode() ? localBaseDir + "/" + user : localBaseDir;
        uploader.uploadFilesAndArchive(ssh, localDir, remoteDir, fileMapper, defaultOperation, stats);
    }
}