package com.newplayer.april23rd.model;

public class MusicFile23rd {
    private long audioId;
    private String audioTitle;
    private String audioAlbum;
    private String audioArtist;
    private String audioPath;
    private long audioDuration; // Duration in milliseconds
    private long audioSize; // Size in bytes

    public MusicFile23rd(long audioId, String audioTitle, String audioAlbum, String audioArtist, String audioPath, long audioDuration, long audioSize) {
        this.audioId = audioId;
        this.audioTitle = audioTitle;
        this.audioAlbum = audioAlbum;
        this.audioArtist = audioArtist;
        this.audioPath = audioPath;
        this.audioDuration = audioDuration;
        this.audioSize = audioSize;
    }

    // Getters and setters
    public long getId() { return audioId; }
    public void setId(long id) { this.audioId = id; }
    public String getTitle() { return audioTitle; }
    public void setTitle(String title) { this.audioTitle = title; }
    public String getAlbum() { return audioAlbum; }
    public void setAlbum(String album) { this.audioAlbum = album; }
    public String getArtist() { return audioArtist; }
    public void setArtist(String artist) { this.audioArtist = artist; }
    public String getPath() { return audioPath; }
    public void setPath(String path) { this.audioPath = path; }
    public long getDuration() { return audioDuration; }
    public void setDuration(long duration) { this.audioDuration = duration; }
    public long getSize() { return audioSize; }
    public void setSize(long size) { this.audioSize = size; }
}
