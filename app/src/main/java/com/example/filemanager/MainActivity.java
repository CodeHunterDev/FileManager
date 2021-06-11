package com.example.filemanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.filemanager.CustomView.CustomCroller;
import com.example.filemanager.FileUtils.StorageSize;
import com.example.filemanager.Activity.FileActivity;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    CardView storageCard;
    private static final String TAG = "MainActivity";
    int availableMemory = 0;
    int occupiedMemory = 0;
    String occupiedMemoryPercante = "";

    // Views
    CustomCroller storageCroller;

    HashMap<String, Long> storageAvailability;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    void init() {
        storageCroller = findViewById(R.id.storage_croller);
        storageCard = findViewById(R.id.card_storage);

        storageCard.setOnClickListener(this);
        setProgressBar();
    }

    private void setProgressBar() {
        availableMemory = new StorageSize().getTotalInternalMemorySize();
        occupiedMemory = new StorageSize().getAvailableInternalOccupiedMemorySizeLong();
        occupiedMemoryPercante = new StorageSize().getInternalOccupiedMemoryPercentage();

        storageCroller.setMax(availableMemory);
        storageCroller.setProgress(occupiedMemory);
        storageCroller.setLabel(occupiedMemoryPercante);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.card_storage: {
                startActivity(new Intent(this , FileActivity.class));
            }
            break;
        }
    }
}