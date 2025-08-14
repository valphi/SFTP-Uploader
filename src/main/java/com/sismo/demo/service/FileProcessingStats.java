package com.sismo.demo.service;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class FileProcessingStats implements Serializable {
    private int totalFiles = 0;
    private int successfulUploads = 0;
    private int failedUploads = 0;
    private int skippedFiles = 0;
    private final Map<String, String> failureReasons = new HashMap<>();
    private final Map<String, String> skipReasons = new HashMap<>();

    public void incrementTotal() {
        totalFiles++;
    }

    public void incrementSuccessful() {
        successfulUploads++;
    }

    public void addFailure(String fileName, String reason) {
        failedUploads++;
        failureReasons.put(fileName, reason);
    }

    public void addSkipped(String fileName, String reason) {
        skippedFiles++;
        skipReasons.put(fileName, reason);
    }

    public void merge(FileProcessingStats other) {
        if (other == null) return;

        this.totalFiles += other.totalFiles;
        this.successfulUploads += other.successfulUploads;
        this.failedUploads += other.failedUploads;
        this.skippedFiles += other.skippedFiles;
        this.failureReasons.putAll(other.failureReasons);
        this.skipReasons.putAll(other.skipReasons);
    }

    public String generateAggregatedSummaryReport() {
        StringBuilder report = new StringBuilder();
        report.append("==== Aggregated File Upload Summary Report ====\n");
        appendStatistics(report);
        return report.toString();
    }

    private void appendStatistics(StringBuilder report) {
        report.append("Total files processed: ").append(totalFiles).append("\n");
        report.append("Successfully uploaded: ").append(successfulUploads).append("\n");
        report.append("Failed uploads: ").append(failedUploads).append("\n");
        report.append("Skipped files: ").append(skippedFiles).append("\n");

        if (!failureReasons.isEmpty()) {
            report.append("Failed uploads details:\n");
            failureReasons.forEach((file, reason) ->
                    report.append(" - ").append(file).append(": ").append(reason).append("\n"));
        }

        if (!skipReasons.isEmpty()) {
            report.append("Skipped files details:\n");
            skipReasons.forEach((file, reason) ->
                    report.append(" - ").append(file).append(": ").append(reason).append("\n"));
        }
    }
}