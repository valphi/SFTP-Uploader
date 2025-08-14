package com.sismo.demo.service;

import com.sismo.demo.utils.FileMapperParserUtil;
import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.sftp.SFTPClient;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
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
import static com.sismo.demo.utils.FileMapperParserUtil.isNotValidFileMapper;
import static com.sismo.demo.utils.FileMapperParserUtil.validateFileMapper;
import static com.sismo.demo.utils.FileUtil.findSubName;
import static com.sismo.demo.utils.FileUtil.getProcessableFiles;
import static com.sismo.demo.utils.FileUtil.isProcessableFile;
import static com.sismo.demo.utils.FileUtil.renameFile;
import static com.sismo.demo.utils.FileUtil.verifyFileChecksum;
import static com.sismo.demo.utils.FileUtil.verifyFileSize;
import static com.sismo.demo.utils.FileUtil.zipAndDeleteFile;
import static com.sismo.demo.utils.LogUtil.log;

public class SftpFileUploader {
    private static final int FILE_SIZE_THRESHOLD = 20 * 1024 * 1024;
    private final Set<String> toArchiveDirectories;

    public SftpFileUploader(Set<String> toArchiveDirectories) {
        this.toArchiveDirectories = toArchiveDirectories;
    }

    public void uploadFilesAndArchive(SSHClient ssh,
                                      String localDirectory,
                                      String sftpDirectory,
                                      String fileMapper,
                                      String operationType,
                                      FileProcessingStats stats) {
        if (stats == null) {
            stats = new FileProcessingStats();
        }

        if (!ALLOWED_FILE_OPERATIONS.contains(operationType)) {
            log("Invalid operation type: " + operationType + " for " + sftpDirectory.replace("/", "") + ". Allowed operations are: " + ALLOWED_FILE_OPERATIONS, LOG_FILE_NAME);
            return;
        }

        File folder = validateDirectory(localDirectory);
        if (folder == null) {
            log("Invalid directory: " + localDirectory, LOG_FILE_NAME);
            return;
        }

        File[] files = folder.listFiles();
        if (files == null || files.length == 0) {
            log("No files or folders to process in: " + localDirectory, LOG_FILE_NAME);
            return;
        }

        // Filter out system files and directories
        File[] processableFiles = getProcessableFiles(files);
        if (processableFiles.length == 0) {
            log("No processable files found in directory: " + localDirectory, LOG_FILE_NAME);
            return;
        }

        Map<String, String> subNameByExternalId = parseFileMapper(fileMapper);
        if (isNotValidFileMapper(fileMapper, subNameByExternalId)) return;

        for (File file : processableFiles) {
            log("----- Processing file: " + file.getName() + " -----", LOG_FILE_NAME);
            stats.incrementTotal();
            processFile(ssh, file, localDirectory, sftpDirectory, subNameByExternalId, operationType, stats);
        }

        zipAndClearDirectory(localDirectory);
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
                             String operationType,
                             FileProcessingStats stats) {
        String fileName = file.getName();
        try {
            log("Starting to process file: " + fileName, LOG_FILE_NAME);

            if (file.isDirectory() || fileName.startsWith("archive") || fileName.equals(LOG_FILE_NAME)) {
                stats.addSkipped(fileName, "Not a regular file or excluded by name pattern");
                log("Skipping non-processable file: " + fileName, LOG_FILE_NAME);
                return;
            }

            if (!isProcessableFile(file, fileName)) {
                log("File is not a valid CSV or ZIP file: " + fileName, LOG_FILE_NAME);
                stats.addSkipped(fileName, "Not a valid CSV or ZIP file");
                return;
            }

            if (subNameByExternalId != null && !subNameByExternalId.isEmpty()) {
                Optional<String> subNameOptional = findSubName(fileName, subNameByExternalId.keySet());
                if (subNameOptional.isEmpty()) {
                    String[] fileNameSplit = fileName.split("-");
                    if (fileNameSplit.length != 3) {
                        log("File name does not match any subName in the mapping parameter. File isn't uploaded: " + fileName + ", subName: " + subNameByExternalId.values(), LOG_FILE_NAME);
                        stats.addSkipped(fileName, "Filename doesn't match any subName in mapping");
                        return;
                    }

                    String externalId = fileNameSplit[0];
                    Set<String> normalizedExternalIds = subNameByExternalId.values().stream()
                            .map(this::normalizeString)
                            .collect(Collectors.toSet());
                    String normalizedExternalId = normalizeString(externalId);
                    if (normalizedExternalId != null && !normalizedExternalIds.contains(normalizedExternalId)) {
                        stats.addSkipped(fileName, "External ID doesn't match any mapping");
                        log("File name does not match any subName in the mapping parameter. File isn't uploaded: " + fileName + ", subName: " + subNameByExternalId.values(), LOG_FILE_NAME);
                        return;
                    }
                } else {
                    String subName = subNameOptional.get();
                    String externalId = subNameByExternalId.get(subName);
                    File renamedFile = renameFile(file, externalId, operationType);
                    if (renamedFile == null) {
                        stats.addSkipped(fileName, "Failed to rename file");
                        log("Failed to rename file: " + fileName, LOG_FILE_NAME);
                        return;
                    }
                    fileName = renamedFile.getName();
                    file = renamedFile;
                }
            }

            if (shouldCompressFile(sftpDirectory, fileName)) {
                log("Compressing file before upload: " + fileName, LOG_FILE_NAME);
                zipAndDeleteFile(file.getPath(), file.getPath().replace(CSV_EXTENSION, ZIP_EXTENSION));
                fileName = fileName.replace(CSV_EXTENSION, ZIP_EXTENSION);
            }

            boolean success = uploadFileToSFTP(ssh, fileName, localDirectory, sftpDirectory);
            if (success) {
                stats.incrementSuccessful();
            } else {
                stats.addFailure(fileName, "File verification failed after upload");
                log("File upload failed or verification failed for: " + fileName, LOG_FILE_NAME);
            }
        } catch (Exception e) {
            log("Error processing file: " + fileName, e, LOG_FILE_NAME);
            stats.addFailure(fileName, "Processing error: " + e.getMessage());
        }
    }

    private boolean uploadFileToSFTP(SSHClient ssh, String fileName, String localDirectory, String sftpDirectory) {
        Path localFilePath = Path.of(localDirectory, fileName);
        String remotePath = sftpDirectory + "/" + fileName;
        File localFile = localFilePath.toFile();

        try (SFTPClient sftp = ssh.newSFTPClient()) {
            log("Starting upload of file: " + fileName + " to " + remotePath, LOG_FILE_NAME);

            ssh.getConnection().setTimeoutMs(60000);

            long startTime = System.currentTimeMillis();
            sftp.put(localFile.getAbsolutePath(), remotePath);
            long endTime = System.currentTimeMillis();
            log("Upload completed in " + (endTime - startTime) + "ms for " + fileName, LOG_FILE_NAME);

            // Verify the uploaded file
            log("Verifying file size for: " + fileName, LOG_FILE_NAME);
            if (!verifyFileSize(sftp, localFile, remotePath, fileName)) {
                return false;
            }

            // For larger files, just use size check
            long fileSize = localFile.length();
            if (fileSize > FILE_SIZE_THRESHOLD) {
                log("File " + fileName + " uploaded and verified successfully (size check only).", LOG_FILE_NAME);
                return true;
            }

            // For smaller files, also verify checksum
            log("Starting checksum verification for: " + fileName, LOG_FILE_NAME);
            boolean checksumResult = verifyFileChecksum(sftp, localFile, remotePath, fileName);
            log("Checksum verification " + (checksumResult ? "successful" : "failed") + " for " + fileName, LOG_FILE_NAME);
            return checksumResult;
        } catch (IOException e) {
            log("Error uploading file: " + fileName, e, LOG_FILE_NAME);
            return false;
        } catch (Exception e) {
            log("Unexpected error during file upload for: " + fileName, e, LOG_FILE_NAME);
            return false;
        }
    }

    private boolean shouldCompressFile(String sftpDirectory, String fileName) {
        return toArchiveDirectories.contains(sftpDirectory)
               && !fileName.endsWith("-D.csv")
               && !fileName.endsWith(ZIP_EXTENSION);
    }

    private String normalizeString(String input) {
        if (input == null) return null;
        return Normalizer.normalize(input.trim(), Normalizer.Form.NFC);
    }
}