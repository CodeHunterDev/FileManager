package com.example.filemanager.FileUtils;

import android.os.Environment;
import android.os.StatFs;

import java.io.File;

public class StorageSize {

    private static final String TAG = "StorageSize";
    static final public String AVAILABLE_MEGABYETES = "AVAILABLE_MEGABYETES";
    static final public String ERROR = "ERROR";


    public static boolean externalMemoryAvailable() {
        return android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED);
    }

    public static String getAvailableInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSizeLong();
        long availableBlocks = stat.getAvailableBlocksLong();
        return formatSize(availableBlocks * blockSize);
    }


    public static int getAvailableInternalMemorySizeLong() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSizeLong();
        long availableBlocks = stat.getAvailableBlocksLong();
        return formatSizeLong(availableBlocks * blockSize);
    }

    public static int getTotalInternalMemorySize() {
        final File path = Environment.getDataDirectory();
        final StatFs stat = new StatFs(path.getPath());
        final long blockSize = stat.getBlockSize();
        final long totalBlocks = stat.getBlockCount();
        return formatSizeLong(totalBlocks * blockSize);
    }

    public static int getAvailableInternalOccupiedMemorySizeLong() {
        return getTotalInternalMemorySize() - getAvailableInternalMemorySizeLong();
    }

    public static String getInternalOccupiedMemoryPercentage(){
        int availableMemory = new StorageSize().getTotalInternalMemorySize();
        int occupiedMemory = new StorageSize().getAvailableInternalOccupiedMemorySizeLong();
        int percent = (occupiedMemory * 100) / availableMemory;
        return percent + " %";
    }

    public static String getAvailableExternalMemorySize() {
        if (externalMemoryAvailable()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSizeLong();
            long availableBlocks = stat.getAvailableBlocksLong();
            return formatSize(availableBlocks * blockSize);
        } else {
            return ERROR;
        }
    }

    public static String getTotalExternalMemorySize() {
        if (externalMemoryAvailable()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSizeLong();
            long totalBlocks = stat.getBlockCountLong();
            return formatSize(totalBlocks * blockSize);
        } else {
            return ERROR;
        }
    }

    public static String formatSize(long size) {
        String suffix = null;

        if (size >= 1024) {
            suffix = "KB";
            size /= 1024;
            if (size >= 1024) {
                suffix = "MB";
                size /= 1024;
            }
        }

        StringBuilder resultBuffer = new StringBuilder(Long.toString(size));

        int commaOffset = resultBuffer.length() - 3;
        while (commaOffset > 0) {
            resultBuffer.insert(commaOffset, ',');
            commaOffset -= 3;
        }

        if (suffix != null) resultBuffer.append(suffix);
        return resultBuffer.toString();
    }

    public static int formatSizeLong(long size) {
        String suffix = null;

        if (size >= 1024) {
            suffix = "KB";
            size /= 1024;
            if (size >= 1024) {
                suffix = "MB";
                size /= 1024;
            }
        }

        StringBuilder resultBuffer = new StringBuilder(Long.toString(size));

        int commaOffset = resultBuffer.length() - 3;
        while (commaOffset > 0) {
            resultBuffer.insert(commaOffset, ',');
            commaOffset -= 3;
        }

        if (suffix != null) resultBuffer.append(suffix);
        String rawvalue =  resultBuffer.toString();
        rawvalue = rawvalue.replace("," , "");
        rawvalue = rawvalue.replace("KB" , "");
        rawvalue = rawvalue.replace("MB" , "");
        return Integer.parseInt(rawvalue);
    }
}
