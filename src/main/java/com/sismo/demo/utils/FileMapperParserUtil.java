package com.sismo.demo.utils;

import java.util.HashMap;
import java.util.Map;

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
}