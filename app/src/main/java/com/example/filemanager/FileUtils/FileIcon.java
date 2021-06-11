package com.example.filemanager.FileUtils;

import android.util.Log;

import com.example.filemanager.R;

import java.io.File;

public class FileIcon {

    private static final String TAG = "FileIcon";

    static public int whichIcon(File file) {
        int icon = R.drawable.ic_folder;
        if (file.isDirectory()) {
            return icon;
        }

        String ext = getFileExt(file);
        switch (ext) {
            // music extensions
            case "aif":
            case "cda":
            case "mid":
            case "midi":
            case "mp3":
            case "mpa":
            case "wav":
            case "wma":
            case "wpl":
                icon = R.drawable.ic_music;
                break;
                // compressed file extensions
            case "7z" :
            case "arj" :
            case "deb" :
            case "pkg" :
            case "rar" :
            case "rpm" :
            case "gz" :
            case "z" :
            case "zip" :
                icon = R.drawable.ic_zip;
                break;
                // file iso extensions
            case "bin" :
            case "dmg" :
            case "iso" :
            case "toast" :
            case "vcd" :
                icon = R.drawable.ic_disk;
                break;
            case "csv" :
                icon = R.drawable.ic_csv ;
                break;
            case "sql" :
                icon = R.drawable.ic_sql;
                break;
            case "apk" :
                icon = R.drawable.ic_apk;
                break;
            // image file formart
            case "ai" :
            case "bmp" :
            case "gif" :
            case "ico" :
            case "jpeg" :
            case "jpg" :
            case "png" :
            case "ps" :
            case "psd" :
            case "svg" :
                icon = R.drawable.ic_image;
                break;
            case "ppt" :
            case "pptx" :
                icon = R.drawable.ic_ppt;
                break;
            case "doc" :
            case "docx" :
                icon = R.drawable.ic_doc;
                break;
            case "xls" :
            case "xlsx" :
                icon = R.drawable.ic_xls;
                break;
            case "txt" :
                icon = R.drawable.ic_txt;
                break;
            case "epub" :
                icon = R.drawable.ic_epub;
                break;
            case "exe" :
                icon = R.drawable.ic_exe;
                break;
            case "pdf" :
                icon = R.drawable.ic_pdf;
                break;
                // VIdeo files
            case "3g2":
            case "3gp":
            case "avi":
            case "flv":
            case "h264":
            case "m4v":
            case "mkv":
            case "mov":
            case "mp4":
            case "mpg":
            case "mpeg":
            case "rm":
            case "swf":
            case "vob":
            case "wmv":
                icon = R.drawable.ic_video;
                break;
            default:
                icon = R.drawable.ic_common_file;
                break;
        }
        return icon;
    }

    static public String getFileExt(File file) {
        Log.e(TAG, "getFileExt: "+file.getAbsolutePath());
        String ext = "";
        ext = file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf('.') + 1);
        return ext;
    }
}
