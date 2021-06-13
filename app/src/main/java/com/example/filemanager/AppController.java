package com.example.filemanager;

import android.app.Application;

import com.example.filemanager.Activity.FileActivity;

public class AppController extends Application {

    public static boolean isCopyOrCut = false;
    public static FileActivity.ACTION whichAction = FileActivity.ACTION.NONE;

    @Override
    public void onCreate() {
        super.onCreate();
    }

}
