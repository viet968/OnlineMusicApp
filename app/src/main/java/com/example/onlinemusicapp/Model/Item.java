package com.example.onlinemusicapp.Model;

import com.squareup.moshi.Json;

public class Item {
    @Json(name = "encodeId")
    private String encodeId;
    @Json(name = "title")
    private String title;
    @Json(name = "artistsNames")
    private String artistsNames;
    @Json(name = "isWorldWide")
    private boolean isWorldWide;
    @Json(name = "thumbnailM")
    private String thumbnailM;
    @Json(name = "thumbnail")
    private String thumbnail;
    @Json(name = "duration")
    private int duration;
    @Json(name = "streamingStatus")
    private int streamingStatus;

    /**
     * No args constructor for use in serialization
     *
     */
    public Item() {
    }

    /**
     *
     * @param isWorldWide
     * @param duration
     * @param thumbnail
     * @param streamingStatus
     * @param thumbnailM
     * @param artistsNames
     * @param title
     * @param encodeId
     */
    public Item(String encodeId, String title,String artistsNames, boolean isWorldWide, String thumbnailM, String thumbnail, int duration, int streamingStatus) {
        super();
        this.encodeId = encodeId;
        this.title = title;
        this.artistsNames = artistsNames;
        this.isWorldWide = isWorldWide;
        this.thumbnailM = thumbnailM;
        this.thumbnail = thumbnail;
        this.duration = duration;
        this.streamingStatus = streamingStatus;
    }

    public String getEncodeId() {
        return encodeId;
    }

    public void setEncodeId(String encodeId) {
        this.encodeId = encodeId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtistsNames() {
        return artistsNames;
    }

    public void setArtistsNames(String artistsNames) {
        this.artistsNames = artistsNames;
    }

    public boolean isIsWorldWide() {
        return isWorldWide;
    }

    public void setIsWorldWide(boolean isWorldWide) {
        this.isWorldWide = isWorldWide;
    }

    public String getThumbnailM() {
        return thumbnailM;
    }

    public void setThumbnailM(String thumbnailM) {
        this.thumbnailM = thumbnailM;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getStreamingStatus() {
        return streamingStatus;
    }

    public void setStreamingStatus(int streamingStatus) {
        this.streamingStatus = streamingStatus;
    }

}
