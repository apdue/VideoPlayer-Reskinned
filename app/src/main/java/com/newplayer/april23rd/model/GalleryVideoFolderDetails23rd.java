package com.newplayer.april23rd.model;

public class GalleryVideoFolderDetails23rd {

    String videoFolderPath;
    GalleryVideoFolderInformation23rd galleryVideoFolderInformation23rd;

    public GalleryVideoFolderDetails23rd(String videoFolderPath, GalleryVideoFolderInformation23rd galleryVideoFolderInformation23rd) {
        this.videoFolderPath = videoFolderPath;
        this.galleryVideoFolderInformation23rd = galleryVideoFolderInformation23rd;
    }

    public String getFolderPath() {
        return videoFolderPath;
    }

    public GalleryVideoFolderInformation23rd getFolderInfo() {
        return galleryVideoFolderInformation23rd;
    }
}
