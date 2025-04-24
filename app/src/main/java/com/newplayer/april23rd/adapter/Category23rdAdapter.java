package com.newplayer.april23rd.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.newplayer.april23rd.AdsManger.AdController23rd;
import com.newplayer.april23rd.R;
import com.newplayer.april23rd.activity.SeeAllTrending23rdActivity;
import com.newplayer.april23rd.model.TrendVideoData23rd;

import java.util.ArrayList;

public class Category23rdAdapter extends RecyclerView.Adapter<Category23rdAdapter.ViewHolder> {
    ArrayList<TrendVideoData23rd> videoArrayList = new ArrayList<>();
    Activity activity;
    private AdController23rd adControllerlocalInstance23rd;

    public Category23rdAdapter(Activity activity) {
        this.activity = activity;
        adControllerlocalInstance23rd = AdController23rd.getInstance();
    }

    public void setVideoArrayList(ArrayList<TrendVideoData23rd> videoArrayList) {
        this.videoArrayList = videoArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(activity).inflate(R.layout.category_list_23rd, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.categoryNameTextView.setText(videoArrayList.get(position).getCategoryName());

        SubCategory23rdAdapter subCategoryAdapter = new SubCategory23rdAdapter(videoArrayList.get(position).getListHashMap(), activity);
        holder.videoRecyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
        holder.videoRecyclerView.setAdapter(subCategoryAdapter);

        holder.seeAllRelative.setOnClickListener(v -> {
            Intent intent = new Intent(activity, SeeAllTrending23rdActivity.class);
            intent.putExtra("categoryName", videoArrayList.get(position).getCategoryName());
            intent.putParcelableArrayListExtra("dataList", videoArrayList.get(position).getListHashMap());
            adControllerlocalInstance23rd.showInterAdCallBack(activity, new AdController23rd.MyCallback() {
                @Override
                public void callbackCall() {
                    activity.startActivity(intent);
                }
            });
        });
    }

    @Override
    public int getItemCount() {
        return videoArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView categoryNameTextView;
        RecyclerView videoRecyclerView;
        RelativeLayout seeAllRelative;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            categoryNameTextView = itemView.findViewById(R.id.categoryNameTextView);
            videoRecyclerView = itemView.findViewById(R.id.videoRecyclerView);
            seeAllRelative = itemView.findViewById(R.id.seeAllRelative);
        }
    }
}
