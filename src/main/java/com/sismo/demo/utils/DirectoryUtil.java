package com.sismo.demo.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipOutputStream;

import static com.sismo.demo.Constants.ARCHIVE_PREFIX;
import static com.sismo.demo.Constants.LOG_FILE_NAME;
import static com.sismo.demo.utils.FileUtil.createArchiveFile;
import static com.sismo.demo.utils.FileUtil.deleteFile;
import static com.sismo.demo.utils.FileUtil.zipFile;
import static com.sismo.demo.utils.LogUtil.log;

public class DirectoryUtil {

    public static void zipAndClearDirectory(String localDirectory) {
        File folder = validateDirectory(localDirectory);
        if (folder == null) {
            log("Invalid directory: " + localDirectory, LOG_FILE_NAME);
            return;
        }

        File[] files = folder.listFiles(File::isFile);
        if (isEmptyOrOnlyLogFile(files)) {
            log("No files or folders to process in: " + localDirectory, LOG_FILE_NAME);
            return;
        }

        File zipFile = createArchiveFile(localDirectory);
        try (FileOutputStream fos = new FileOutputStream(zipFile);
             ZipOutputStream zos = new ZipOutputStream(fos)) {

            for (File file : files) {
                zipFile(file, file.getName(), zos);
            }

            log("Archive created successfully: " + zipFile.getPath(), LOG_FILE_NAME);
        } catch (IOException e) {
            log("Error creating archive: " + e.getMessage(), LOG_FILE_NAME);
        }

        deleteNonArchivedFiles(files);
    }

    public static File validateDirectory(String localDirectory) {
        File folder = new File(localDirectory);
        if (!folder.exists() || !folder.isDirectory()) {
            return null;
        }
        return folder;
    }

    private static void deleteNonArchivedFiles(File[] files) {
        for (File file : files) {
            if (file.isFile() && !file.getName().startsWith(ARCHIVE_PREFIX)) {
                deleteFile(file);
            }
        }
    }

    private static boolean isEmptyOrOnlyLogFile(File[] files) {
        return files == null || files.length == 0 || (files.length == 1 && files[0].getName().equals(LOG_FILE_NAME));
    }
}
