package com.newplayer.april23rd.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.newplayer.april23rd.AdsManger.AdController23rd;
import com.newplayer.april23rd.R;

import com.newplayer.april23rd.activity.FolderVideoList23rdActivity;
import com.newplayer.april23rd.model.GalleryVideoFolderDetails23rd;

import java.io.File;
import java.util.ArrayList;

public class Folder23rdAdapter extends RecyclerView.Adapter<Folder23rdAdapter.ViewHolder> {

    Activity activity;
    ArrayList<GalleryVideoFolderDetails23rd> galleryVideoFolderDetails23rdArrayList;
    public static final int LAYOUT_VERTICAL = 0;
    public static final int LAYOUT_GRID = 1;
    private int layoutType = LAYOUT_VERTICAL;
    AdController23rd adController23rd;

    public Folder23rdAdapter(Activity activity, ArrayList<GalleryVideoFolderDetails23rd> galleryVideoFolderDetails23rdArrayList) {
        this.activity = activity;
        this.galleryVideoFolderDetails23rdArrayList = galleryVideoFolderDetails23rdArrayList;

        adController23rd = AdController23rd.getInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (layoutType == LAYOUT_GRID) {
            view = LayoutInflater.from(activity).inflate(R.layout.folder_grid_layout_23rd, parent, false);
        } else {
            view = LayoutInflater.from(activity).inflate(R.layout.folder_list_layout_23rd, parent, false);
        }
        return new ViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        return layoutType;
    }


    public void setLayoutType(int layoutType) {
        this.layoutType = layoutType;
        notifyDataSetChanged();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GalleryVideoFolderDetails23rd galleryVideoFolderDetails23rd1 = galleryVideoFolderDetails23rdArrayList.get(position);
        String fileName = new File(galleryVideoFolderDetails23rd1.getFolderPath()).getName();
        holder.folderNameTextView.setText(fileName);
        holder.folderCountTextView.setText(galleryVideoFolderDetails23rd1.getFolderInfo().getVideoCount() + " Videos");

        holder.itemView.setOnClickListener(v -> adController23rd.showInterAdCallBack(activity, () -> {
            Intent intent = new Intent(activity, FolderVideoList23rdActivity.class);
            intent.putExtra("folder_path", fileName);
            activity.startActivity(intent);
        }));
    }

    @Override
    public int getItemCount() {
        return galleryVideoFolderDetails23rdArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView folderNameTextView, folderCountTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            folderNameTextView = itemView.findViewById(R.id.folderNameTextView);
            folderCountTextView = itemView.findViewById(R.id.folderCountTextView);
        }
    }
}

