package com.example.filemanager.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.filemanager.Adapters.FileAdapter;
import com.example.filemanager.BuildConfig;
import com.example.filemanager.FileUtils.FileConstants;
import com.example.filemanager.FileUtils.FileOperations;
import com.example.filemanager.Interface.OnClick;
import com.example.filemanager.R;

import java.io.File;
import java.net.URLConnection;
import java.util.List;

public class FileActivity extends AppCompatActivity implements OnClick, View.OnClickListener {

    TextView toolbarTitle;
    LinearLayout bottomBarLL;
    LinearLayout noFileLayoutLL;
    ImageView backArrow;
    RecyclerView fileRecyclerView;
    FileAdapter fileAdapter;
    List<File> fileList;

    private final int STORAGE_PERMISSION_CODE = 1111;
    private String currentPath = FileConstants.HOME_DIRECTORY;

    @Override
    protected void onResume() {
        super.onResume();
        if (isPermissionAreGranted()) {
            initializeFileManager(FileConstants.HOME_DIRECTORY);
        } else {
            requestForPermission();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file);
        init();
    }

    private void init() {
        bottomBarLL = findViewById(R.id.bottom_bar_ll);
        fileRecyclerView = findViewById(R.id.file_recyclerview);
        toolbarTitle = findViewById(R.id.toolbar_title_tv);
        backArrow = findViewById(R.id.back_arrow_iv);
        noFileLayoutLL = findViewById(R.id.no_file_layout_ll);

        backArrow.setOnClickListener(this);
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
            fileAdapter = new FileAdapter(fileList, this, this);
            fileRecyclerView.setAdapter(fileAdapter);
            showList();
        } else {
            hideList();
        }
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
            shareFile(fileList.get(position));
        }
    }

    private void shareFile(File file) {
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
    public void OnItemLongClick(int position) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_arrow_iv: {
                handleOnBackPressed();
            }
            break;
        }
    }

    private void handleOnBackPressed() {
        if (currentPath.equals(FileConstants.HOME_DIRECTORY)) {
            onBackPressed();
        } else {
            currentPath = FileOperations.goToBackFolder(currentPath);
            initializeFileManager(currentPath);
            setToolbar();
        }
    }

    @Override
    public void onBackPressed() {
        handleOnBackPressed();
    }
}