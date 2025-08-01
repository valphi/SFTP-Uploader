package com.sismo.demo.service;

import com.sismo.demo.utils.FileMapperParserUtil;
import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.sftp.SFTPClient;

import java.io.File;
import java.text.Normalizer;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.sismo.demo.Constants.ALLOWED_FILE_OPERATIONS;
import static com.sismo.demo.Constants.CSV_EXTENSION;
import static com.sismo.demo.Constants.LOG_FILE_NAME;
import static com.sismo.demo.Constants.ZIP_EXTENSION;
import static com.sismo.demo.utils.DirectoryUtil.validateDirectory;
import static com.sismo.demo.utils.DirectoryUtil.zipAndClearDirectory;
import static com.sismo.demo.utils.FileUtil.findSubName;
import static com.sismo.demo.utils.FileUtil.renameFile;
import static com.sismo.demo.utils.FileUtil.zipAndDeleteFile;
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
        if (!ALLOWED_FILE_OPERATIONS.contains(operationType)) {
            log("Invalid operation type: " + operationType + " for "+ sftpDirectory.replace("/", "") +". Allowed operations are: " + ALLOWED_FILE_OPERATIONS, LOG_FILE_NAME);
            return;
        }

        File folder = validateDirectory(localDirectory);
        if (folder == null) {
            log("Invalid directory: " + localDirectory, LOG_FILE_NAME);
            return;
        }

        File[] files = folder.listFiles();
        if (files == null || files.length == 0) {
            log("No files found in directory: " + localDirectory, LOG_FILE_NAME);
            return;
        }

        Map<String, String> subNameByExternalId = parseFileMapper(fileMapper);
        if (isNotValidFileMapper(fileMapper, subNameByExternalId)) return;

        for (File file : files) {
            processFile(ssh, file, localDirectory, sftpDirectory, subNameByExternalId, operationType);
        }

        zipAndClearDirectory(localDirectory);
    }

    private static boolean isNotValidFileMapper(String fileMapper, Map<String, String> subNameByExternalId) {
        return fileMapper != null && !fileMapper.isEmpty() && subNameByExternalId == null;
    }

    private Map<String, String> parseFileMapper(String fileMapper) {
        if (fileMapper == null || fileMapper.isEmpty()) {
            return Map.of();
        }
        Map<String, String> normalizedMap = FileMapperParserUtil.parseFileMapper(fileMapper).entrySet().stream()
                .collect(Collectors.toMap(
                        e -> normalizeString(e.getKey()),
                        e -> normalizeString(e.getValue())
                ));

        validateFileMapper(normalizedMap, fileMapper);
        return Map.copyOf(normalizedMap);
    }

    private void processFile(SSHClient ssh,
                             File file,
                             String localDirectory,
                             String sftpDirectory,
                             Map<String, String> subNameByExternalId,
                             String operationType) {
        String fileName = file.getName();
        if (file.isDirectory() || fileName.startsWith("archive") || fileName.equals(LOG_FILE_NAME)) {
            return;
        }
        if (!isProcessableFile(file, fileName)) {
            log("File is not a valid CSV or ZIP file: " + fileName, LOG_FILE_NAME);
            return;
        }

        if (subNameByExternalId != null && !subNameByExternalId.isEmpty()) {
            Optional<String> subNameOptional = findSubName(fileName, subNameByExternalId.keySet());
            if (subNameOptional.isEmpty()) {
                String[] fileNameSplit = fileName.split("-");
                if (fileNameSplit.length != 3) {
                    log("File name does not match any subName in the mapping parameter. File isn't uploaded: " + fileName, LOG_FILE_NAME);
                    return;
                }
                String externalId = fileNameSplit[0];
                Set<String> normalizedExternalIds = subNameByExternalId.values().stream()
                        .map(this::normalizeString)
                        .collect(Collectors.toSet());
                String normalizedExternalId = normalizeString(externalId);
                if (normalizedExternalId != null && !normalizedExternalIds.contains(normalizedExternalId)) {
                    log("File name does not match any subName in the mapping parameter. File isn't uploaded: " + fileName, LOG_FILE_NAME);
                    return;
                }
            } else {
                String subName = subNameOptional.get();
                String externalId = subNameByExternalId.get(subName);
                File renamedFile = renameFile(file, externalId, operationType);
                if (renamedFile == null) return;
                fileName = renamedFile.getName();
                file = renamedFile;
            }
        }

        if (shouldCompressFile(sftpDirectory, fileName)) {
            zipAndDeleteFile(file.getPath(), file.getPath().replace(CSV_EXTENSION, ZIP_EXTENSION));
            fileName = fileName.replace(CSV_EXTENSION, ZIP_EXTENSION);
        }

        uploadFileToSFTP(ssh, fileName, localDirectory, sftpDirectory);
    }

    private boolean isProcessableFile(File file, String fileName) {
        return file.isFile() && (fileName.endsWith(ZIP_EXTENSION) || fileName.endsWith(CSV_EXTENSION));
    }

    private void uploadFileToSFTP(SSHClient ssh, String fileName, String localDirectory, String sftpDirectory) {
        try (SFTPClient sftp = ssh.newSFTPClient()) {
            sftp.put(localDirectory + "/" + fileName, sftpDirectory + "/" + fileName);
            log("File " + fileName + " uploaded successfully.", LOG_FILE_NAME);
        } catch (Exception e) {
            log("Error uploading file: " + e.getMessage(), LOG_FILE_NAME);
        }
    }

    private boolean shouldCompressFile(String sftpDirectory, String fileName) {
        return toArchiveDirectories.contains(sftpDirectory)
               && !fileName.endsWith("-D.csv")
               && !fileName.endsWith(ZIP_EXTENSION);
    }

    private void validateFileMapper(Map<String, String> map, String fileMapper) {
        if (map.isEmpty()) {
            log("Mapping parameter is empty or contains more than one similar subName in mappings: " + fileMapper, LOG_FILE_NAME);
            throw new IllegalArgumentException("Invalid file mapper: empty or duplicate subNames");
        }
        if (map.keySet().stream().anyMatch(subName -> subName.contains("-"))) {
            log("Mapping parameter contains invalid subName with '-' character: " + fileMapper, LOG_FILE_NAME);
            throw new IllegalArgumentException("Invalid file mapper: subName contains '-'");
        }
        if (map.values().stream().anyMatch(externalId -> externalId.contains("-") || externalId.contains("."))) {
            log("Mapping parameter contains invalid externalId with '-' or '.' character: " + fileMapper, LOG_FILE_NAME);
            throw new IllegalArgumentException("Invalid file mapper: externalId contains '-' or '.'");
        }
    }

    private String normalizeString(String input) {
        if (input == null) return null;
        return Normalizer.normalize(input.trim(), Normalizer.Form.NFC);
    }
}