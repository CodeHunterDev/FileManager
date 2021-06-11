package com.example.filemanager.FileUtils;

import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
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
        if (files != null && files.length > 1) {
            for (File singleFile : files) {
                String fileName = getFileName(singleFile);
                if (!(fileName.charAt(0) == '.')) {
                    if (singleFile.isFile()) {
                        if (fileName.contains(".")) {
                            fileList.add(singleFile);
                        }
                    }else{
                        fileList.add(singleFile);
                    }
                }
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
}
