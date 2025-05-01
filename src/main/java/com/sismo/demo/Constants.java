package com.sismo.demo;

import java.util.Set;

public class Constants {
    public static final String LOG_FILE_NAME = "log.txt";
    public static final String ARCHIVE_PREFIX = "archive";
    public static final String ARCHIVE_FOLDER = "archived";
    public static final String ZIP_EXTENSION = ".zip";
    public static final String CSV_EXTENSION = ".csv";
    public static final String DATE_FORMAT = "yyyyMMdd_HHmmss";

    public static final String SERVER = System.getenv("SFTP_SERVER");
    public static final String USER = System.getenv("SFTP_USER");
    public static final String PHRASE = System.getenv("SFTP_PHRASE");
    public static final String PRIVATE_KEY = System.getenv("SFTP_PRIVATE_KEY");
    public static final String LOCAL_USER_INDICATOR_DIRECTORY = System.getenv("SFTP_LOCAL_USER_INDICATOR_DIRECTORY");
    public static final String LOCAL_MACRO_INDICATOR_DIRECTORY = System.getenv("SFTP_LOCAL_MACRO_INDICATOR_DIRECTORY");
    public static final String LOCAL_PORTFOLIO_DIRECTORY = System.getenv("SFTP_LOCAL_PORTFOLIO_DIRECTORY");
    public static final String PORTFOLIO_FILE_MAPPER = System.getenv("SFTP_PORTFOLIO_FILE_MAPPER");
    public static final String PORTFOLIO_FILE_MAPPER_DEFAULT_OPERATION = System.getenv("SFTP_PORTFOLIO_FILE_MAPPER_DEFAULT_OPERATION");
    public static final String USER_INDICATOR_FILE_MAPPER = System.getenv("SFTP_USER_INDICATOR_FILE_MAPPER");
    public static final String USER_INDICATOR_FILE_MAPPER_DEFAULT_OPERATION = System.getenv("SFTP_USER_INDICATOR_FILE_MAPPER_DEFAULT_OPERATION");
    public static final String MACRO_INDICATOR_MAPPER = System.getenv("SFTP_MACRO_INDICATOR_FILE_MAPPER");
    public static final String MACRO_INDICATOR_FILE_MAPPER_DEFAULT_OPERATION = System.getenv("SFTP_MACRO_INDICATOR_FILE_MAPPER_DEFAULT_OPERATION");

    public static final String SFTP_USER_INDICATOR_DIRECTORY = "/user_indicator";
    public static final String SFTP_MACRO_INDICATOR_DIRECTORY = "/macro_indicator";
    public static final String SFTP_PORTFOLIO_DIRECTORY = "/portfolio";

    public static final Set<String> TO_ARCHIVE_FILES_IN_DIRECTORY = Set.of(SFTP_USER_INDICATOR_DIRECTORY);
}