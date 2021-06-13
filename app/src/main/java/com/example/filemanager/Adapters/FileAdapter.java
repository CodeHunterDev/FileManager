package com.example.filemanager.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.filemanager.AppController;
import com.example.filemanager.FileUtils.FileIcon;
import com.example.filemanager.FileUtils.FileOperations;
import com.example.filemanager.Interface.OnClick;
import com.example.filemanager.R;

import java.io.File;
import java.text.ParseException;
import java.util.List;

public class FileAdapter extends RecyclerView.Adapter<FileAdapter.ViewHodler> {

    private List<File> fileList;
    private Context mContext;
    private OnClick onItemListener;
    private boolean[] selectedPositions;

    public FileAdapter(List<File> fileList, Context mContext, OnClick onItemListener, boolean[] selectedPositions) {
        this.fileList = fileList;
        this.mContext = mContext;
        this.onItemListener = onItemListener;
        this.selectedPositions = selectedPositions;
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
        holder.fileFolderSizeTV.setText(setFileSize(singleFile, holder.sizeDateBlockLL));
        holder.fileFolderCreationDateTV.setText("Last Modified : " + FileOperations.gesStringLastModifiedDate(singleFile.lastModified()));
        holder.mainContainerLL.setBackgroundColor(Color.parseColor(selectedPositions[position] ? "#ededed" : "#FFFFFF"));

        holder.itemView.setOnClickListener(v -> {
            if(isAnyFileSelected() && !AppController.isCopyOrCut){
                selectItem(position);
            }else{
                onItemListener.OnItemClick(position);
            }
        });
        holder.itemView.setOnLongClickListener(v -> {
            selectItem(position);
            return true;
        });
    }

    private void selectItem(int position){
        selectedPositions[position] = !selectedPositions[position];
        onItemListener.OnItemLongClick(selectedPositions);
        notifyDataSetChanged();
    }

    private boolean isAnyFileSelected(){
        boolean isFileSelected = false;
        for (int i = 0; i < selectedPositions.length; i++) {
            if (selectedPositions[i]) {
                isFileSelected = true;
                break;
            }
        }
        return isFileSelected;
    }

    private String setFileSize(File singleFile, LinearLayout block) {
        if (singleFile.isDirectory()) {
            block.setVisibility(View.GONE);
            return "";
        }
        String size = FileOperations.getStringSizeLengthFile(singleFile.length());
        block.setVisibility(View.VISIBLE);
        return size;
    }

    @Override
    public int getItemCount() {
        return fileList.size();
    }

    public class ViewHodler extends RecyclerView.ViewHolder {

        ImageView fileFolderIV;
        TextView fileFolderTV;
        TextView fileFolderSizeTV;
        TextView fileFolderCreationDateTV;
        LinearLayout sizeDateBlockLL;
        LinearLayout mainContainerLL;

        public ViewHodler(@NonNull View itemView) {
            super(itemView);
            fileFolderIV = itemView.findViewById(R.id.file_folder_iv);
            fileFolderTV = itemView.findViewById(R.id.file_folder_tv);
            fileFolderSizeTV = itemView.findViewById(R.id.file_size_tv);
            fileFolderCreationDateTV = itemView.findViewById(R.id.file_creation_date_tv);
            sizeDateBlockLL = itemView.findViewById(R.id.size_date_block_ll);
            mainContainerLL = itemView.findViewById(R.id.main_container_ll);
        }
    }
}
