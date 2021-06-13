package com.example.filemanager.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.filemanager.Adapters.FileAdapter;
import com.example.filemanager.AppController;
import com.example.filemanager.BuildConfig;
import com.example.filemanager.CustomView.CreateFolderDialog;
import com.example.filemanager.FileUtils.FileConstants;
import com.example.filemanager.FileUtils.FileOperations;
import com.example.filemanager.Interface.OnClick;
import com.example.filemanager.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.FileAlreadyExistsException;
import java.util.ArrayList;
import java.util.List;

public class FileActivity extends AppCompatActivity implements OnClick, View.OnClickListener, CreateFolderDialog.DialogClick {

    public static enum ACTION {COPY, MOVE, NONE}

    private static final String TAG = "FileActivity";

    TextView toolbarTitle;
    LinearLayout bottomBarLL;
    LinearLayout confirmationControllerLL;
    LinearLayout actionControllerLL;
    LinearLayout noFileLayoutLL;
    ImageView backArrow;
    ImageView createNewFolder;
    AppCompatImageView deleteIv;
    AppCompatImageView copyIv;
    AppCompatImageView cutIv;
    AppCompatImageView shareIv;
    AppCompatImageView moreIv;
    AppCompatImageView cancelIv;
    AppCompatImageView pasteIV;


    RecyclerView fileRecyclerView;
    FileAdapter fileAdapter;

    List<File> fileList;
    List<File> copyOrMoveFileList = new ArrayList<>();

    boolean[] selectedPositions;
    ACTION copyOrCutAction = ACTION.NONE;

    private String currentPath = FileConstants.HOME_DIRECTORY;
    private final int STORAGE_PERMISSION_CODE = 1111;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file);
        init();
        if (!isPermissionAreGranted()) requestForPermission();
        else initializeFileManager(FileConstants.HOME_DIRECTORY);
    }

    private void init() {
        bottomBarLL = findViewById(R.id.bottom_bar_ll);
        fileRecyclerView = findViewById(R.id.file_recyclerview);
        toolbarTitle = findViewById(R.id.toolbar_title_tv);
        backArrow = findViewById(R.id.back_arrow_iv);
        createNewFolder = findViewById(R.id.create_folder_iv);
        noFileLayoutLL = findViewById(R.id.no_file_layout_ll);
        confirmationControllerLL = findViewById(R.id.confirmation_controller_ll);
        actionControllerLL = findViewById(R.id.main_controller_ll);
        deleteIv = findViewById(R.id.delete_iv);
        copyIv = findViewById(R.id.copy_iv);
        cancelIv = findViewById(R.id.cancel_iv);
        pasteIV = findViewById(R.id.paset_iv);
        cutIv = findViewById(R.id.cut_iv);
        shareIv = findViewById(R.id.share_iv);
        moreIv = findViewById(R.id.more_iv);

        backArrow.setOnClickListener(this);
        createNewFolder.setOnClickListener(this);
        deleteIv.setOnClickListener(this);
        copyIv.setOnClickListener(this);
        cutIv.setOnClickListener(this);
        shareIv.setOnClickListener(this);
        moreIv.setOnClickListener(this);
        cancelIv.setOnClickListener(this);
        pasteIV.setOnClickListener(this);

        setRecyclerView();
        setToolbar();
    }

    private void hideList() {
        fileRecyclerView.setVisibility(View.GONE);
        noFileLayoutLL.setVisibility(View.VISIBLE);
    }

    private void showList() {
        noFileLayoutLL.setVisibility(View.GONE);
        fileRecyclerView.setVisibility(View.VISIBLE);
    }

    private void setToolbar() {
        if (currentPath.equals(FileConstants.HOME_DIRECTORY)) {
            toolbarTitle.setText(getResources().getString(R.string.home));
        } else {
            toolbarTitle.setText(FileOperations.getFileName(currentPath));
        }
    }

    private void setRecyclerView() {
        fileRecyclerView.setHasFixedSize(true);
        fileRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initializeFileManager(String path) {
        fileList = FileOperations.getFiles(path);
        if (fileList != null && fileList.size() > 0) {
            setFileSelection();
            fileAdapter = new FileAdapter(fileList, this, this, selectedPositions);
            fileRecyclerView.setAdapter(fileAdapter);
            showList();
        } else {
            hideList();
        }
    }

    private void setFileSelection() {
        toggleBottomBar();
        selectedPositions = new boolean[fileList.size()];
        for (int i = 0; i < fileList.size(); i++)
            selectedPositions[i] = false;
    }

    boolean isPermissionAreGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if ((checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) &&
                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
            ) {
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    private void requestForPermission() {
        String PERMISSIONS[] = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(PERMISSIONS, STORAGE_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (isPermissionAreGranted()) {
            initializeFileManager(FileConstants.HOME_DIRECTORY);
        } else {
            Toast.makeText(this, getResources().getString(R.string.provide_storage_permission), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void OnItemClick(int position) {
        if (fileList.get(position).isDirectory()) {
            currentPath = FileOperations.goToNextFolder(fileList.get(position), currentPath);
            initializeFileManager(currentPath);
            setToolbar();
        } else {
            openFile(fileList.get(position));
        }
    }

    private void shareFile(File file) {
        Uri uri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID, file);
        String mime = getContentResolver().getType(uri);

        // Open file with user selected app
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setDataAndType(uri, mime);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(intent);
    }

    private void openFile(File file) {
        Uri uri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID, file);
        String mime = getContentResolver().getType(uri);

        // Open file with user selected app
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, mime);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(intent);
    }

    @Override
    public void OnItemLongClick(boolean[] selectedPositions) {
        this.selectedPositions = selectedPositions;
        checkForSelection();
    }

    private void checkForSelection() {
        boolean isSelected = false;
        for (int i = 0; i < selectedPositions.length; i++) {
            if (selectedPositions[i]) {
                isSelected = true;
                break;
            }
        }
        bottomBarLL.setVisibility(isSelected ? View.VISIBLE : View.GONE);
        toggleControllerBar();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_arrow_iv: {
                handleOnBackPressed();
            }
            break;
            case R.id.create_folder_iv: {
                createFolder();
            }
            break;
            case R.id.delete_iv: {
                deleteFile();
            }
            break;
            case R.id.cut_iv: {
                copyOrCutAction = ACTION.MOVE;
                AppController.isCopyOrCut = true;
                copyOrMoveFile();
            }
            break;
            case R.id.copy_iv: {
                copyOrCutAction = ACTION.COPY;
                AppController.isCopyOrCut = true;
                copyOrMoveFile();
            }
            break;
            case R.id.share_iv: {
                shareSelectedFiles();
            }
            break;
            case R.id.more_iv: {

            }
            break;
            case R.id.cancel_iv: {
                cancelPaste();
            }
            break;
            case R.id.paset_iv: {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    pasteFile();
                }
            }
            break;
        }
    }

    private void shareSelectedFiles() {
        for (int i = 0; i < selectedPositions.length; i++) {
            if (selectedPositions[i])
                shareFile(fileList.get(i));
        }
        setFileSelection();
        initializeFileManager(currentPath);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void pasteFile() {
        AppController.whichAction = copyOrCutAction;
        for (File singleFile : copyOrMoveFileList) {
            try {
                FileOperations.copyOrMove(singleFile.getAbsolutePath(), currentPath + File.separator + FileOperations.getFileName(singleFile));
            } catch (FileAlreadyExistsException e) {
                e.printStackTrace();
                Toast.makeText(this, "file already exists in selected folder", Toast.LENGTH_SHORT).show();
            }
        }
        AppController.isCopyOrCut = false;
        copyOrCutAction = ACTION.NONE;
        initializeFileManager(currentPath);
        toggleBottomBar();
    }

    private void cancelPaste() {
        AppController.isCopyOrCut = false;
        copyOrMoveFileList.clear();
        copyOrCutAction = ACTION.NONE;
        setFileSelection();
        initializeFileManager(currentPath);
        toggleBottomBar();
    }

    private void copyOrMoveFile() {
        copyOrMoveFileList.clear();
        for (int i = 0; i < selectedPositions.length; i++) {
            if (selectedPositions[i])
                copyOrMoveFileList.add(fileList.get(i));
        }
        toggleControllerBar();
    }

    private void toggleBottomBar() {
        bottomBarLL.setVisibility(copyOrCutAction == ACTION.NONE ? View.GONE : View.VISIBLE);
        toggleControllerBar();
    }

    private void toggleControllerBar() {
        confirmationControllerLL.setVisibility(copyOrCutAction == ACTION.NONE ? View.GONE : View.VISIBLE);
        actionControllerLL.setVisibility(copyOrCutAction == ACTION.NONE ? View.VISIBLE : View.GONE);
    }

    private void deleteFile() {

        for (int i = 0; i < selectedPositions.length; i++)
            if (selectedPositions[i]) {
                try {
                    FileOperations.deleteFileOrFolder(fileList.get(i));
                    Toast.makeText(this, "delete complete", Toast.LENGTH_SHORT).show();
                } catch (FileNotFoundException fileNotFoundException) {
                    fileNotFoundException.printStackTrace();
                    Toast.makeText(this, "selected file does not exist", Toast.LENGTH_SHORT).show();
                }
            }
        initializeFileManager(currentPath);
    }

    private void createFolder() {
        CreateFolderDialog createFolderDialog = new CreateFolderDialog(this, this);
        createFolderDialog.show();
    }

    private void handleOnBackPressed() {
        if (currentPath.equals(FileConstants.HOME_DIRECTORY)) {
            super.onBackPressed();
        } else {
            currentPath = FileOperations.goToBackFolder(currentPath);
            setFileSelection();
            initializeFileManager(currentPath);
            setToolbar();
        }
    }

    @Override
    public void onBackPressed() {
        handleOnBackPressed();
    }

    @Override
    public void onCreateClick(String fileName) {
        Log.e(TAG, "onCreateClick: " + fileName);
        FileOperations.createNewFolder(currentPath, fileName);
        initializeFileManager(currentPath);
    }
}