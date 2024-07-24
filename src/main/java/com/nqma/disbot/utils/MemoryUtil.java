package com.nqma.disbot.utils;

import discord4j.core.spec.EmbedCreateSpec;

public abstract class MemoryUtil {

    public static EmbedCreateSpec getMemoryUsage() {
        Runtime runtime = Runtime.getRuntime();
        long totalMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        long usedMemory = totalMemory - freeMemory;
        long maxMemory = runtime.maxMemory();
        long usedMemoryPercentage = (usedMemory * 100) / totalMemory;
        long freeMemoryPercentage = (freeMemory * 100) / totalMemory;
        return EmbedCreateSpec.builder()
                .title("Memory Usage")
                .addField("Total Memory", formatBytes(totalMemory), true)
                .addField("Used Memory", formatBytes(usedMemory), true)
                .addField("Free Memory", formatBytes(freeMemory), true)
                .addField("Max Memory", formatBytes(maxMemory), true)
                .addField("Used Memory Percentage", usedMemoryPercentage + "%", true)
                .addField("Free Memory Percentage", freeMemoryPercentage + "%", true)
                .build();
    }

    private static String formatBytes(long bytes) {
        int unit = 1024;
        if (bytes < unit) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = "KMGTPE".charAt(exp - 1) + "i";
        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }
}
