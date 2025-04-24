package com.newplayer.april23rd.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class TrendVideoData23rd implements Parcelable {
    String categoryName;
    String videoUrl;
    String thumbNailImage;
    String videoTitle;
    ArrayList<TrendVideoData23rd> listHashMap;
    protected TrendVideoData23rd(Parcel in) {
        categoryName = in.readString();
        videoUrl = in.readString();
        thumbNailImage = in.readString();
        videoTitle = in.readString();
        listHashMap = in.createTypedArrayList(TrendVideoData23rd.CREATOR);
    }
    public static final Creator<TrendVideoData23rd> CREATOR = new Creator<TrendVideoData23rd>() {
        @Override
        public TrendVideoData23rd createFromParcel(Parcel in) {
            return new TrendVideoData23rd(in);
        }

        @Override
        public TrendVideoData23rd[] newArray(int size) {
            return new TrendVideoData23rd[size];
        }
    };
    public ArrayList<TrendVideoData23rd> getListHashMap() {
        return listHashMap;
    }
    public void setListHashMap(ArrayList<TrendVideoData23rd> listHashMap) {
        this.listHashMap = listHashMap;
    }
    public TrendVideoData23rd(String categoryName, String videoUrl, String thumbNailImage, String videoTitle) {
        this.categoryName=categoryName;
        this.videoUrl = videoUrl;
        this.thumbNailImage = thumbNailImage;
        this.videoTitle = videoTitle;
    }
    public TrendVideoData23rd() {

    }
    public String getCategoryName() {
        return categoryName;
    }
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
    public String getVideoUrl() {
        return videoUrl;
    }
    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }
    public String getThumbNailImage() {
        return thumbNailImage;
    }
    public void setThumbNailImage(String thumbNailImage) {
        this.thumbNailImage = thumbNailImage;
    }
    public String getVideoTitle() {
        return videoTitle;
    }
    public void setVideoTitle(String videoTitle) {
        this.videoTitle = videoTitle;
    }
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(categoryName);
        parcel.writeString(videoUrl);
        parcel.writeString(thumbNailImage);
        parcel.writeString(videoTitle);
        parcel.writeTypedList(listHashMap);
    }
}
