package com.newplayer.april23rd.model;

public class GalleryVideoFolderInformation23rd {
    public long folderTotalSize;
    String folderTitle;
    long folderItemDuration;
    String folderBucketDisplayName;
    long folderDateAdded;
    String folderItemResolution;
    public int folderVideoCount;

    public GalleryVideoFolderInformation23rd(long folderTotalSize, String folderTitle, long folderItemDuration, String folderBucketDisplayName, long folderDateAdded, String folderItemResolution, int folderVideoCount) {
        this.folderTotalSize = folderTotalSize;
        this.folderTitle = folderTitle;
        this.folderItemDuration = folderItemDuration;
        this.folderBucketDisplayName = folderBucketDisplayName;
        this.folderDateAdded = folderDateAdded;
        this.folderItemResolution = folderItemResolution;
        this.folderVideoCount = folderVideoCount;
    }

    public long getTotalSize() {
        return folderTotalSize;
    }

    public String getTitle() {
        return folderTitle;
    }

    public long getDuration() {
        return folderItemDuration;
    }

    public String getBucketDisplayName() {
        return folderBucketDisplayName;
    }

    public long getDateAdded() {
        return folderDateAdded;
    }

    public String getResolution() {
        return folderItemResolution;
    }

    public int getVideoCount() {
        return folderVideoCount;
    }


}
