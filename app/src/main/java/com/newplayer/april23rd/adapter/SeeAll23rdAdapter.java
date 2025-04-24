package com.newplayer.april23rd.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.newplayer.april23rd.R;
import com.newplayer.april23rd.activity.TrendVideoPlayer23rdActivity;
import com.newplayer.april23rd.model.TrendVideoData23rd;
import com.newplayer.april23rd.utils.FetchStorageVideo23rd;

import java.util.ArrayList;

public class SeeAll23rdAdapter extends RecyclerView.Adapter<SeeAll23rdAdapter.ViewHolder> {
    ArrayList<TrendVideoData23rd> videoArrayList = new ArrayList<>();
    Activity activity;
    public SeeAll23rdAdapter(ArrayList<TrendVideoData23rd> videoArrayList, Activity activity) {
        this.videoArrayList = videoArrayList;
        this.activity = activity;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(activity).inflate(R.layout.see_all_list_23rd, parent, false));
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.titleTextView.setText(videoArrayList.get(position).getVideoTitle());
        Glide.with(activity)
                .asBitmap()
                .load(videoArrayList.get(position).getThumbNailImage())
                .placeholder(R.drawable.ic_logo_23rd)
                .error(R.drawable.ic_logo_23rd)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.videoImageView);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(activity, TrendVideoPlayer23rdActivity.class);
            intent.putParcelableArrayListExtra(FetchStorageVideo23rd.DATA_LIST, videoArrayList);
            intent.putExtra(FetchStorageVideo23rd.SELECTED_VIDEO_POSITION, position);
            activity.startActivity(intent);
        });
    }
    @Override
    public int getItemCount() {
        return videoArrayList.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView videoImageView;
        TextView titleTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            videoImageView = itemView.findViewById(R.id.videoImageView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
        }
    }
}
