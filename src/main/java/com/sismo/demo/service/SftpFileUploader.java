package com.sismo.demo.service;

import com.sismo.demo.utils.FileMapperParserUtil;
import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.sftp.SFTPClient;

import java.io.File;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static com.sismo.demo.Constants.CSV_EXTENSION;
import static com.sismo.demo.Constants.LOG_FILE_NAME;
import static com.sismo.demo.Constants.ZIP_EXTENSION;
import static com.sismo.demo.utils.FileUtil.clearAndArchiveDirectory;
import static com.sismo.demo.utils.FileUtil.compressAndClearInitFile;
import static com.sismo.demo.utils.FileUtil.findSubName;
import static com.sismo.demo.utils.FileUtil.renameFile;
import static com.sismo.demo.utils.FileUtil.validateDirectory;
import static com.sismo.demo.utils.LogUtil.log;

public class SftpFileUploader {
    private final Set<String> toArchiveDirectories;

    public SftpFileUploader(Set<String> toArchiveDirectories) {
        this.toArchiveDirectories = toArchiveDirectories;
    }

    public void uploadFilesAndArchive(SSHClient ssh,
                                      String localDirectory,
                                      String sftpDirectory,
                                      String fileMapper,
                                      String operationType) {
        File folder = validateDirectory(localDirectory);
        if (folder == null) return;

        File[] files = folder.listFiles();
        if (files == null || files.length == 0) {
            log("No files found in directory: " + localDirectory, localDirectory + "/" + LOG_FILE_NAME);
            return;
        }

        Map<String, String> subNameByExternalId = null;
        if (fileMapper != null && !fileMapper.isEmpty()) {
            subNameByExternalId = FileMapperParserUtil.parseFileMapper(fileMapper);
            if (subNameByExternalId.isEmpty()) {
                log("File mapper is empty or contains conflicting mappings.", localDirectory + "/" + LOG_FILE_NAME);
                return;
            }
        }

        for (File file : files) {
            processFile(ssh, file, localDirectory, sftpDirectory, subNameByExternalId, operationType);
        }

        clearAndArchiveDirectory(localDirectory);
    }

    private void processFile(SSHClient ssh,
                             File file,
                             String localDirectory,
                             String sftpDirectory,
                             Map<String, String> subNameByExternalId,
                             String operationType) {
        String fileName = file.getName();
        if (!file.isFile() || (!fileName.endsWith(ZIP_EXTENSION) && !fileName.endsWith(CSV_EXTENSION))) {
            return;
        }

        if (subNameByExternalId != null && !subNameByExternalId.isEmpty()) {
            Optional<String> subNameOptional = findSubName(fileName, subNameByExternalId.keySet());
            if (subNameOptional.isEmpty()) {
                log("File name does not match any subName in the file mapper: " + fileName, localDirectory + "/" + LOG_FILE_NAME);
                return;
            }

            String subName = subNameOptional.get();
            String externalId = subNameByExternalId.get(subName);
            File renamedFile = renameFile(file, externalId, operationType, localDirectory);
            if (renamedFile == null) {
                return;
            } else {
                fileName = renamedFile.getName();
                file = renamedFile;
            }
        }

        if (shouldCompressFile(sftpDirectory, fileName)) {
            compressAndClearInitFile(file.getPath(),
                    file.getPath().replace(CSV_EXTENSION, ZIP_EXTENSION),
                    localDirectory);
            fileName = fileName.replace(CSV_EXTENSION, ZIP_EXTENSION);
        }

        uploadFileToSFTP(ssh, fileName, localDirectory, sftpDirectory);
    }

    private void uploadFileToSFTP(SSHClient ssh, String fileName, String localDirectory, String sftpDirectory) {
        try (SFTPClient sftp = ssh.newSFTPClient()) {
            sftp.put(localDirectory + "/" + fileName, sftpDirectory + "/" + fileName);
            log("File " + fileName + " uploaded successfully.", localDirectory + "/" + LOG_FILE_NAME);
        } catch (Exception e) {
            log("Error uploading file: " + e.getMessage(), localDirectory + "/" + LOG_FILE_NAME);
        }
    }

    private boolean shouldCompressFile(String sftpDirectory, String fileName) {
        return toArchiveDirectories.contains(sftpDirectory)
               && !fileName.endsWith("-D.csv")
               && !fileName.endsWith(ZIP_EXTENSION);
    }
}