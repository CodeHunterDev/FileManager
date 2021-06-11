package com.example.filemanager.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.filemanager.FileUtils.FileIcon;
import com.example.filemanager.FileUtils.FileOperations;
import com.example.filemanager.Interface.OnClick;
import com.example.filemanager.R;

import java.io.File;
import java.util.List;

public class FileAdapter extends RecyclerView.Adapter<FileAdapter.ViewHodler> {

    private List<File> fileList;
    private Context mContext;
    private OnClick onItemListener;

    public FileAdapter(List<File> fileList, Context mContext, OnClick onItemListener) {
        this.fileList = fileList;
        this.mContext = mContext;
        this.onItemListener = onItemListener;
    }

    @NonNull
    @Override
    public ViewHodler onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.folder_list_item, parent, false);
        return new ViewHodler(view);
    }

    @Override
    public void onBindViewHolder(FileAdapter.ViewHodler holder, int position) {
        File singleFile = fileList.get(position);
        holder.fileFolderTV.setText(FileOperations.getFileName(singleFile));
        holder.fileFolderIV.setBackgroundResource(FileIcon.whichIcon(singleFile));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemListener.OnItemClick(position);
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onItemListener.OnItemLongClick(position);
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return fileList.size();
    }

    public class ViewHodler extends RecyclerView.ViewHolder {

        ImageView fileFolderIV;
        TextView fileFolderTV;

        public ViewHodler(@NonNull View itemView) {
            super(itemView);
            fileFolderIV = itemView.findViewById(R.id.file_folder_iv);
            fileFolderTV = itemView.findViewById(R.id.file_folder_tv);
        }
    }
}
