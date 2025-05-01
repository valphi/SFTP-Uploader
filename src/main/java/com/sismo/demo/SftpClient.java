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
import static com.sismo.demo.Constants.USER_INDICATOR_FILE_MAPPER;
import static com.sismo.demo.Constants.USER_INDICATOR_FILE_MAPPER_DEFAULT_OPERATION;
import static com.sismo.demo.utils.LogUtil.log;

public class SftpClient {

    public static void main(String[] args) {
        SftpConnectionManager connectionManager = new SftpConnectionManager(SERVER, USER, PRIVATE_KEY, PHRASE);
        SftpFileUploader fileUploader = new SftpFileUploader(TO_ARCHIVE_FILES_IN_DIRECTORY);
        FileUtil.deleteFile(new File(LOG_FILE_NAME));
        try (SSHClient ssh = connectionManager.connect()) {
            log("Connected to SFTP server: " + SERVER, "./" + LOG_FILE_NAME);
            fileUploader.uploadFilesAndArchive(ssh, LOCAL_USER_INDICATOR_DIRECTORY, SFTP_USER_INDICATOR_DIRECTORY, USER_INDICATOR_FILE_MAPPER, USER_INDICATOR_FILE_MAPPER_DEFAULT_OPERATION);
            fileUploader.uploadFilesAndArchive(ssh, LOCAL_MACRO_INDICATOR_DIRECTORY, SFTP_MACRO_INDICATOR_DIRECTORY, MACRO_INDICATOR_MAPPER, MACRO_INDICATOR_FILE_MAPPER_DEFAULT_OPERATION);
            fileUploader.uploadFilesAndArchive(ssh, LOCAL_PORTFOLIO_DIRECTORY, SFTP_PORTFOLIO_DIRECTORY, PORTFOLIO_FILE_MAPPER, PORTFOLIO_FILE_MAPPER_DEFAULT_OPERATION);
            log("Files uploaded successfully.", "./" + LOG_FILE_NAME);
        } catch (Exception e) {
            log("Error: " + e.getMessage(), "./" + LOG_FILE_NAME);
        }
    }
}