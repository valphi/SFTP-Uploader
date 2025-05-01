package com.sismo.demo.utils;

import java.io.FileWriter;
import java.io.IOException;

import static java.time.LocalDateTime.now;

public class LogUtil {
    public static void log(String message, String logFilePath) {
        try (FileWriter writer = new FileWriter(logFilePath, true)) {
            String timestampedMessage = "[" + now() + "] " + message;
            writer.write(timestampedMessage + "\n");
            System.out.println(timestampedMessage);
        } catch (IOException e) {
            System.err.println("Error writing to log file: " + e.getMessage());
        }
    }
}