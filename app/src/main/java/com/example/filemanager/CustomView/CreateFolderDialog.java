package com.example.filemanager.CustomView;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;

import com.example.filemanager.R;

public class CreateFolderDialog extends Dialog implements View.OnClickListener {

    AppCompatButton cancelBtn;
    AppCompatButton createBtn;
    EditText newFolderNameEditText;

    Activity activity;
    DialogClick dialogClick;

    public CreateFolderDialog(@NonNull Activity activity , DialogClick dialogClick) {
        super(activity);
        this.dialogClick = dialogClick;
        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.create_folder_dialog);
        init();
    }

    private void init() {
        cancelBtn = findViewById(R.id.cancel_btn);
        createBtn = findViewById(R.id.create_btn);
        newFolderNameEditText = findViewById(R.id.folder_name_et);

        cancelBtn.setOnClickListener(this);
        createBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.create_btn: {
                String fileName = newFolderNameEditText.getText().toString();
                if(TextUtils.isEmpty(fileName)){
                    Toast.makeText(activity, activity.getResources().getString(R.string.file_cant_empty), Toast.LENGTH_SHORT).show();
                    return;
                }
                dialogClick.onCreateClick(fileName);
            }break;
        }
        dismiss();
    }

    public interface DialogClick{
        void onCreateClick(String fileName);
    }
}
