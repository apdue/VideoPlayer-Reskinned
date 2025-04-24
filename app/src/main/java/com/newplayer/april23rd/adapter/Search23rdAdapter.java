package com.newplayer.april23rd.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.newplayer.april23rd.AdsManger.AdController23rd;
import com.newplayer.april23rd.R;
import com.newplayer.april23rd.activity.PlayVideo23rdActivity;
import com.newplayer.april23rd.model.GalleryVideoInformation23rd;

import java.util.ArrayList;
import java.util.Locale;

public class Search23rdAdapter extends RecyclerView.Adapter<Search23rdAdapter.ViewHolder> {
    Activity activity;
    ArrayList<GalleryVideoInformation23rd> galleryVideoInformation23rdArrayList;
    AdController23rd adsController;

    public Search23rdAdapter(Activity activity, ArrayList<GalleryVideoInformation23rd> galleryVideoInformation23rdArrayList) {
        this.activity = activity;
        this.galleryVideoInformation23rdArrayList = galleryVideoInformation23rdArrayList;
        adsController = AdController23rd.getInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(activity).inflate(R.layout.video_list_layout_23rd, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        GalleryVideoInformation23rd videoItem = galleryVideoInformation23rdArrayList.get(position);

        holder.tvTitle.setText(videoItem.getTitle());
        holder.tvVideoDuration.setText(formatDuration(videoItem.getDuration()));
        holder.tvSize.setText(formatVideoFileSize(videoItem.getSize()));

        Glide.with(activity)
                .asBitmap()
                .load(videoItem.getData())
                .placeholder(R.drawable.ic_logo_23rd)
                .error(R.drawable.ic_logo_23rd)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.ivIcon);


        holder.ivMore.setVisibility(View.GONE);

        holder.itemView.setOnClickListener(v -> {
            adsController.showInterAdCallBack(activity, new AdController23rd.MyCallback() {
                @Override
                public void callbackCall() {
                    Intent intent = new Intent(activity, PlayVideo23rdActivity.class);
                    intent.putExtra("position", position);
                    intent.putExtra("video_title", galleryVideoInformation23rdArrayList.get(position).getTitle());
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("videoArrayList", galleryVideoInformation23rdArrayList);
                    intent.putExtras(bundle);
                    activity.startActivity(intent);
                }
            });
        });
    }


    private String formatDuration(long duration) {
        long seconds = duration / 1000;
        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;
        long remainingSeconds = seconds % 60;

        return String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, remainingSeconds);
    }

    public String formatVideoFileSize(long size) {
        return Formatter.formatFileSize(activity, size);
    }

    @Override
    public int getItemCount() {
        return galleryVideoInformation23rdArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivIcon, ivMore;
        TextView tvVideoDuration, tvTitle, tvSize;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivIcon = itemView.findViewById(R.id.ivIcon);
            tvVideoDuration = itemView.findViewById(R.id.tvVideoDuration);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvSize = itemView.findViewById(R.id.tvSize);
            ivMore = itemView.findViewById(R.id.ivMore);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateList(ArrayList<GalleryVideoInformation23rd> newList) {
        galleryVideoInformation23rdArrayList = new ArrayList<>();
        galleryVideoInformation23rdArrayList.addAll(newList);
        notifyDataSetChanged();
    }
}

