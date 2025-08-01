package com.sismo.demo;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Constants {
    public static final String LOG_FILE_NAME = "log.txt";
    public static final String ARCHIVE_PREFIX = "archive";
    public static final String ARCHIVE_FOLDER = "archived";
    public static final String ZIP_EXTENSION = ".zip";
    public static final String CSV_EXTENSION = ".csv";
    public static final String DATE_FORMAT = "yyyyMMdd_HHmmss";

    public static final String SERVER = System.getenv("SFTP_SERVER");
    public static final String USER = System.getenv("SFTP_USER");
    public static final List<String> USERS = parseUsers(USER);
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
    public static final Set<String> ALLOWED_FILE_OPERATIONS = Set.of("F", "M", "D");

    public static final Set<String> TO_ARCHIVE_FILES_IN_DIRECTORY = Set.of(SFTP_USER_INDICATOR_DIRECTORY);

    private static List<String> parseUsers(String userEnv) {
        if (userEnv == null || userEnv.trim().isEmpty()) {
            return Collections.emptyList();
        }
        return Arrays.stream(userEnv.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
    }

    public static boolean isMultiUserMode() {
        return USERS.size() > 1;
    }
}