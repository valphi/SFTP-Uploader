package com.sismo.demo.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static com.sismo.demo.Constants.ARCHIVE_FOLDER;
import static com.sismo.demo.Constants.ARCHIVE_PREFIX;
import static com.sismo.demo.Constants.DATE_FORMAT;
import static com.sismo.demo.Constants.LOG_FILE_NAME;
import static com.sismo.demo.Constants.ZIP_EXTENSION;
import static com.sismo.demo.utils.LogUtil.log;

public class FileUtil {

    public static void deleteFile(File file) {
        if (file.delete()) {
            System.out.println("File " + file.getName() + " deleted successfully.");
        } else {
            System.out.println("Failed to delete file " + file.getName());
        }
    }

    public static void compressAndClearInitFile(String sourceFilePath, String zipFilePath, String localDirectory) {
        try {
            zipSingleFile(sourceFilePath, zipFilePath);
            log("File compressed successfully: " + zipFilePath, localDirectory + "/" + LOG_FILE_NAME);
        } catch (IOException e) {
            log("Error compressing file: " + e.getMessage(), localDirectory + "/" + LOG_FILE_NAME);
        }
        deleteFile(new File(sourceFilePath));
    }

    public static void clearAndArchiveDirectory(String localDirectory) {
        File folder = validateDirectory(localDirectory);
        if (folder == null) return;

        File[] files = folder.listFiles(File::isFile);
        if (files == null || files.length == 0) {
            log("No files or folders to process in: " + localDirectory, localDirectory + "/" + LOG_FILE_NAME);
            return;
        }

        if (files.length == 1 && files[0].getName().equals(LOG_FILE_NAME)) {
            deleteFile(files[0]);
            return;
        }

        File archiveFile = createArchiveFile(localDirectory);
        try (FileOutputStream fos = new FileOutputStream(archiveFile);
             ZipOutputStream zos = new ZipOutputStream(fos)) {

            for (File file : files) {
                zipFile(file, file.getName(), zos);
            }

            log("Archive created successfully: " + archiveFile.getPath(), localDirectory + "/" + LOG_FILE_NAME);
        } catch (IOException e) {
            log("Error creating archive: " + e.getMessage(), localDirectory + "/" + LOG_FILE_NAME);
        }

        deleteNonArchivedFiles(files);
    }

    private static File createArchiveFile(String localDirectory) {
        String timestamp = new SimpleDateFormat(DATE_FORMAT).format(new Date());
        String archiveFileName = ARCHIVE_PREFIX + "_" + timestamp + ZIP_EXTENSION;
        File archiveDir = new File(localDirectory, ARCHIVE_FOLDER);
        if (!archiveDir.exists()) {
            archiveDir.mkdir();
        }
        return new File(archiveDir, archiveFileName);
    }

    private static void zipSingleFile(String sourceFilePath, String zipFilePath) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(zipFilePath);
             ZipOutputStream zos = new ZipOutputStream(fos)) {
            zipFile(new File(sourceFilePath), new File(sourceFilePath).getName(), zos);
        }
    }

    private static void zipFile(File file, String entryName, ZipOutputStream zos) throws IOException {
        try (FileInputStream fis = new FileInputStream(file)) {
            ZipEntry zipEntry = new ZipEntry(entryName);
            zos.putNextEntry(zipEntry);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                zos.write(buffer, 0, length);
            }
            zos.closeEntry();
        }
    }

    private static void deleteNonArchivedFiles(File[] files) {
        for (File file : files) {
            if (file.isFile() && !file.getName().startsWith(ARCHIVE_PREFIX)) {
                deleteFile(file);
            }
        }
    }

    public static String getFileExtension(String fileName) {
        if (fileName == null || fileName.lastIndexOf('.') == -1) {
            return ""; // No extension found
        }
        return fileName.substring(fileName.lastIndexOf('.') + 1);
    }


    public static File renameFile(File file, String externalId, String operationType, String localDirectory) {
        long epochMillis = System.currentTimeMillis();
        String fileName = file.getName();
        String fileExtension = getFileExtension(fileName);
        String newFileName = externalId + "-" + epochMillis + "-" + operationType + "." + fileExtension;
        File renamedFile = new File(file.getParent(), newFileName);

        if (file.renameTo(renamedFile)) {
            log("File " + fileName + " renamed to: " + newFileName, localDirectory + "/" + LOG_FILE_NAME);
            return renamedFile;
        } else {
            log("Failed to rename file: " + fileName, localDirectory + "/" + LOG_FILE_NAME);
            return null;
        }
    }


    public static Optional<String> findSubName(String fileName, Set<String> subNames) {
        return subNames.stream().filter(fileName::contains).findFirst();
    }

    public static File validateDirectory(String localDirectory) {
        File folder = new File(localDirectory);
        if (!folder.exists() || !folder.isDirectory()) {
            log("Invalid directory: " + localDirectory, localDirectory + "/" + LOG_FILE_NAME);
            return null;
        }
        return folder;
    }

}
