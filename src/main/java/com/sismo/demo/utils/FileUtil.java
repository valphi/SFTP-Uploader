package com.sismo.demo.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static com.sismo.demo.Constants.ARCHIVE_FOLDER;
import static com.sismo.demo.Constants.ARCHIVE_PREFIX;
import static com.sismo.demo.Constants.DATE_FORMAT;
import static com.sismo.demo.Constants.LOCAL_MACRO_INDICATOR_DIRECTORY;
import static com.sismo.demo.Constants.LOCAL_PORTFOLIO_DIRECTORY;
import static com.sismo.demo.Constants.LOCAL_USER_INDICATOR_DIRECTORY;
import static com.sismo.demo.Constants.LOG_FILE_NAME;
import static com.sismo.demo.Constants.USERS;
import static com.sismo.demo.Constants.ZIP_EXTENSION;
import static com.sismo.demo.utils.LogUtil.log;

public class FileUtil {

    public static void prepareUserDirectories() {
        copyMainDirectoryFilesToUserDirectories(LOCAL_USER_INDICATOR_DIRECTORY);
        copyMainDirectoryFilesToUserDirectories(LOCAL_MACRO_INDICATOR_DIRECTORY);
        copyMainDirectoryFilesToUserDirectories(LOCAL_PORTFOLIO_DIRECTORY);
    }

    public static void zipAndDeleteFile(String sourceFilePath, String zipFilePath) {
        if (zipSingleFile(sourceFilePath, zipFilePath)) {
            deleteFile(new File(sourceFilePath));
        }
    }

    public static void zipAndDeleteFile(File file, File zipFile) {
        if (file == null || !file.isFile()) {
            log("Invalid file to archive: " + (file != null ? file.getName() : "null"), LOG_FILE_NAME);
            return;
        }

        if (zipSingleFile(file.getAbsolutePath(), zipFile.getAbsolutePath())) {
            deleteFile(file);
        }
    }

    public static File createArchiveFile(String localDirectory) {
        String timestamp = new SimpleDateFormat(DATE_FORMAT).format(new Date());
        String archiveFileName = ARCHIVE_PREFIX + "_" + timestamp + ZIP_EXTENSION;
        File archiveDir = new File(localDirectory, ARCHIVE_FOLDER);
        if (!archiveDir.exists() && !archiveDir.mkdir()) {
            log("Failed to create archive directory: " + archiveDir.getPath(), LOG_FILE_NAME);
        }
        return new File(archiveDir, archiveFileName);
    }

    public static void deleteFile(File file) {
        if (file.delete()) {
            log("File deleted successfully: " + file.getName(), LOG_FILE_NAME);
        } else {
            log("Failed to delete file: " + file.getName(), LOG_FILE_NAME);
        }
    }

    private static boolean zipSingleFile(String sourceFilePath, String zipFilePath) {
        try (FileOutputStream fos = new FileOutputStream(zipFilePath);
             ZipOutputStream zos = new ZipOutputStream(fos)) {
            File sourceFile = new File(sourceFilePath);
            zipFile(sourceFile, sourceFile.getName(), zos);
            log("File " + sourceFile.getName() + " archived successfully: " + zipFilePath, LOG_FILE_NAME);
            return true;
        } catch (IOException e) {
            log("Error archiving file " + sourceFilePath + ": " + e.getMessage(), LOG_FILE_NAME);
            return false;
        }
    }

    public static void zipFile(File file, String entryName, ZipOutputStream zos) throws IOException {
        try (FileInputStream fis = new FileInputStream(file)) {
            zos.putNextEntry(new ZipEntry(entryName));
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                zos.write(buffer, 0, length);
            }
            zos.closeEntry();
        }
    }

    public static String getFileExtension(String fileName) {
        return (fileName == null || fileName.lastIndexOf('.') == -1) ? "" : fileName.substring(fileName.lastIndexOf('.') + 1);
    }

    public static File renameFile(File file, String externalId, String operationType) {
        String newFileName = generateNewFileName(file.getName(), externalId, operationType);
        File renamedFile = new File(file.getParent(), newFileName);

        if (file.renameTo(renamedFile)) {
            log("File " + file.getName() + " renamed to " + newFileName, LOG_FILE_NAME);
            return renamedFile;
        } else {
            log("Failed to rename file: " + file.getName(), LOG_FILE_NAME);
            return null;
        }
    }

    public static Optional<String> findSubName(String fileName, Set<String> subNames) {
        return subNames.stream().filter(fileName::contains).findFirst();
    }

    private static String generateNewFileName(String originalName, String externalId, String operationType) {
        long epochMillis = System.currentTimeMillis();
        String fileExtension = getFileExtension(originalName);
        return externalId + "-" + epochMillis + "-" + operationType + (fileExtension.isEmpty() ? "" : "." + fileExtension);
    }

    private static void ensureUserDirectoriesExist(String baseDirectory) {
        for (String user : USERS) {
            String userDirectory = baseDirectory + File.separator + user;
            File userDir = new File(userDirectory);

            if (!userDir.exists()) {
                if (userDir.mkdirs()) {
                    log("Created user directory: " + userDirectory, LOG_FILE_NAME);
                } else {
                    log("Failed to create user directory: " + userDirectory, LOG_FILE_NAME);
                }
            }
        }
    }

    private static void copyMainDirectoryFilesToUserDirectories(String localDirectory) {
        File mainDir = validateDirectory(localDirectory);
        if (mainDir == null) {
            log("Invalid directory for copying: " + localDirectory, LOG_FILE_NAME);
            return;
        }

        ensureUserDirectoriesExist(localDirectory);

        // Get all files in the main directory (excluding special files)
        File[] files = mainDir.listFiles(file ->
                file.isFile() &&
                !file.getName().startsWith(ARCHIVE_PREFIX) &&
                !file.getName().equals(LOG_FILE_NAME)
        );

        if (files == null || files.length == 0) {
            return;
        }

        log("Found " + files.length + " files in main directory to distribute to user directories", LOG_FILE_NAME);

        // Copy each file to each user's directory
        for (String user : USERS) {
            String userDirectory = localDirectory + File.separator + user;
            File userDir = new File(userDirectory);

            // Copy each file
            for (File file : files) {
                try {
                    File destFile = new File(userDir, file.getName());
                    Files.copy(file.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    log("Copied file " + file.getName() + " to user directory: " + userDirectory, LOG_FILE_NAME);
                } catch (IOException e) {
                    log("Error copying file " + file.getName() + " to user directory: " + e.getMessage(), LOG_FILE_NAME);
                }
            }
        }

        // Delete the original files after they've been copied to all user directories
        for (File file : files) {
            try {
                if (file.delete()) {
                    log("Deleted original file after copying to all users: " + file.getName(), LOG_FILE_NAME);
                } else {
                    log("Failed to delete original file: " + file.getName(), LOG_FILE_NAME);
                }
            } catch (Exception e) {
                log("Error deleting original file " + file.getName() + ": " + e.getMessage(), LOG_FILE_NAME);
            }
        }
    }

    private static File validateDirectory(String directoryPath) {
        if (directoryPath == null || directoryPath.isEmpty()) {
            return null;
        }

        File directory = new File(directoryPath);
        return directory.exists() && directory.isDirectory() ? directory : null;
    }

}
