package com.example.filemanager.FileUtils;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class FileOperations {

    private static final String TAG = "FileOperations";

    public static List<File> getFiles(String path) {
        try {
            List<File> fileList = new ArrayList<>();
            fileList.clear();

            final File dir = new File(path);
            File[] files = dir.listFiles();
            files = sortFilesByName(files);
            fileList = filterFileList(files);
            return fileList;
        } catch (Exception e) {
            Log.e(TAG, "getFiles: Exception occurred");
            return new ArrayList<>();
        }
    }

    private static List<File> filterFileList(File[] files) {
        List<File> fileList = new ArrayList<>();
        if (files != null && files.length > 0) {
            for (File singleFile : files) {
                if (!singleFile.isHidden())
                    fileList.add(singleFile);
            }
        }
        return fileList;
    }

    private static File[] sortFilesByName(File[] files) {
        if (files != null && files.length > 1)
            Arrays.sort(files, (object1, object2) -> object1.getName().toLowerCase().compareTo(object2.getName().toLowerCase()));
        return files;
    }

    public static String getFileName(File file) {
        if (file == null) return "Corrupt file";
        String fileName = file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf('/') + 1);
        return fileName;
    }

    public static String getFileName(String file) {
        if (file == null) return "Corrupt file";
        String fileName = file.substring(file.lastIndexOf('/') + 1);
        return fileName;
    }

    public static String goToNextFolder(File file, String currentPath) {
        String folder = getFileName(file);
        String newPath = currentPath + "/" + folder;
        return newPath;
    }

    public static String goToBackFolder(String currentPath) {
        String path = currentPath.substring(0, currentPath.lastIndexOf('/'));
        return path;
    }

    public static String getStringSizeLengthFile(long size) {

        DecimalFormat df = new DecimalFormat("0.00");

        float sizeKb = 1024.0f;
        float sizeMb = sizeKb * sizeKb;
        float sizeGb = sizeMb * sizeKb;
        float sizeTerra = sizeGb * sizeKb;


        if (size < sizeMb)
            return df.format(size / sizeKb) + " Kb";
        else if (size < sizeGb)
            return df.format(size / sizeMb) + " Mb";
        else if (size < sizeTerra)
            return df.format(size / sizeGb) + " Gb";

        return "";
    }

    public static String gesStringLastModifiedDate(long timestamp) {
        Date d = new Date(timestamp);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yyyy");
        return simpleDateFormat.format(d);
    }

    public static void createNewFolder(String path, String name) {
        Log.e(TAG, "createNewFolder: " + path + File.separator + name);
        File file = new File(path + File.separator + name);
        if (!file.exists())
            file.mkdir();
    }

    public static void deleteFileOrFolder(File fileOrFolder) {
        if (fileOrFolder.isDirectory()) {
            for (File singleFile :
                    fileOrFolder.listFiles()) {
                deleteFileOrFolder(singleFile);
            }
        }
        fileOrFolder.delete();
    }

    public static void copy(String origin, String dest) {
        File originFile = new File(origin);
        File destFile = new File(dest);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                Files.copy(originFile.toPath(), destFile.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
