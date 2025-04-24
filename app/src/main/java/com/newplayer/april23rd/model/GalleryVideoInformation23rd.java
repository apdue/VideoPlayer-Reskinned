package com.newplayer.april23rd.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class GalleryVideoInformation23rd implements Parcelable {
    private long id;
    private String data;
    private String title;
    private long size;
    private long duration;
    private String bucketName;
    private long dateAdded;
    private String resolution;

    // Constructor
    public GalleryVideoInformation23rd(long id, String data, String title, long size, long duration, String bucketName, long dateAdded, String resolution) {
        this.id = id;
        this.data = data;
        this.title = title;
        this.size = size;
        this.duration = duration;
        this.bucketName = bucketName;
        this.dateAdded = dateAdded;
        this.resolution = resolution;
    }

    protected GalleryVideoInformation23rd(Parcel in) {
        id = in.readLong();
        data = in.readString();
        title = in.readString();
        size = in.readLong();
        duration = in.readLong();
        bucketName = in.readString();
        dateAdded = in.readLong();
        resolution = in.readString();
    }

    public static final Creator<GalleryVideoInformation23rd> CREATOR = new Creator<GalleryVideoInformation23rd>() {
        @Override
        public GalleryVideoInformation23rd createFromParcel(Parcel in) {
            return new GalleryVideoInformation23rd(in);
        }

        @Override
        public GalleryVideoInformation23rd[] newArray(int size) {
            return new GalleryVideoInformation23rd[size];
        }
    };

    // Getters and Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public long getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(long dateAdded) {
        this.dateAdded = dateAdded;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(data);
        parcel.writeString(title);
        parcel.writeLong(size);
        parcel.writeLong(duration);
        parcel.writeString(bucketName);
        parcel.writeLong(dateAdded);
        parcel.writeString(resolution);
    }
}
