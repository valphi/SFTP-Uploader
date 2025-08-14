package com.sismo.demo.utils;

import java.util.HashMap;
import java.util.Map;

import static com.sismo.demo.Constants.LOG_FILE_NAME;
import static com.sismo.demo.utils.LogUtil.log;

public class FileMapperParserUtil {

    public static Map<String, String> parseFileMapper(String fileMapper) {
        Map<String, String> result = new HashMap<>();
        if (fileMapper == null || fileMapper.isEmpty()) {
            return result;
        }

        String[] mappings = fileMapper.split(",");
        for (String mapping : mappings) {
            String[] parts = mapping.split("-");
            if (parts.length == 2) {
                String externalId = parts[0];
                String subName = parts[1];

                // Check if subName conflicts with existing keys
                if (result.keySet().stream().anyMatch(key -> key.contains(subName) || subName.contains(key))) {
                    return new HashMap<>(); // Return empty map if conflict is found
                }

                result.put(subName, externalId);
            }
        }

        return result;
    }

    public static void validateFileMapper(Map<String, String> map, String fileMapper) {
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

    public static boolean isNotValidFileMapper(String fileMapper, Map<String, String> subNameByExternalId) {
        return fileMapper != null && !fileMapper.isEmpty() && subNameByExternalId == null;
    }
}