package com.example.onlinemusicapp.Model;

import com.squareup.moshi.Json;

public class GetMV {
    @Json(name = "encodeId")
    private String encodeId;
    @Json(name = "title")
    private String title;
    @Json(name = "isOffical")
    private boolean isOffical;
    @Json(name = "artistsNames")
    private String artistsNames;
    @Json(name = "thumbnailM")
    private String thumbnailM;
    @Json(name = "thumbnail")
    private String thumbnail;
    @Json(name = "duration")
    private int duration;
    @Json(name = "streamingStatus")
    private int streamingStatus;
    @Json(name = "like")
    private int like;
    @Json(name = "listen")
    private int listen;
    @Json(name = "streaming")
    private Streaming streaming;

    /**
     * No args constructor for use in serialization
     *
     */
    public GetMV() {
    }

    /**
     *
     * @param duration
     * @param thumbnail
     * @param streaming
     * @param like
     * @param streamingStatus
     * @param thumbnailM
     * @param isOffical
     * @param title
     * @param artistsNames
     * @param encodeId
     * @param listen
     */
    public GetMV(String encodeId, String title, boolean isOffical, String artistsNames, String thumbnailM, String thumbnail, int duration, int streamingStatus, int like, int listen, Streaming streaming) {
        super();
        this.encodeId = encodeId;
        this.title = title;
        this.isOffical = isOffical;
        this.artistsNames = artistsNames;
        this.thumbnailM = thumbnailM;
        this.thumbnail = thumbnail;
        this.duration = duration;
        this.streamingStatus = streamingStatus;
        this.like = like;
        this.listen = listen;
        this.streaming = streaming;
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

    public boolean isIsOffical() {
        return isOffical;
    }

    public void setIsOffical(boolean isOffical) {
        this.isOffical = isOffical;
    }

    public String getArtistsNames() {
        return artistsNames;
    }

    public void setArtistsNames(String artistsNames) {
        this.artistsNames = artistsNames;
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

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }

    public int getListen() {
        return listen;
    }

    public void setListen(int listen) {
        this.listen = listen;
    }

    public Streaming getStreaming() {
        return streaming;
    }

    public void setStreaming(Streaming streaming) {
        this.streaming = streaming;
    }

}
