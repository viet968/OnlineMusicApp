package com.example.onlinemusicapp.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.squareup.moshi.Json;

public class Song {

    @Json(name ="thumb")
    
    private String thumb;
    @Json(name ="artist")
    
    private String artist;
    @Json(name ="artistIds")
    
    private String artistIds;
    @Json(name ="duration")
    
    private String duration;
    @Json(name ="block")
    
    private String block;
    @Json(name ="id")
    
    private String id;
    @Json(name ="hasVideo")
    
    private String hasVideo;
    @Json(name ="streamingStatus")
    
    private String streamingStatus;
    @Json(name ="thumbVideo")
    
    private String thumbVideo;
    @Json(name ="genreIds")
    
    private String genreIds;
    @Json(name ="disable_platform_web")
    
    private String disablePlatformWeb;
    @Json(name ="disSPlatform")
    
    private String disSPlatform;
    @Json(name ="is_official")
    
    private String isOfficial;
    @Json(name ="radioPid")
    
    private String radioPid;
    @Json(name ="zing_choice")
    
    private String zingChoice;
    @Json(name ="name")
    
    private String name;
    @Json(name ="disDPlatform")
    
    private String disDPlatform;

    /**
     * No args constructor for use in serialization
     *
     */
    public Song() {
    }

    /**
     *
     * @param hasVideo
     * @param thumb
     * @param artist
     * @param disablePlatformWeb
     * @param streamingStatus
     * @param thumbVideo
     * @param genreIds
     * @param artistIds
     * @param disSPlatform
     * @param isOfficial
     * @param duration
     * @param radioPid
     * @param name
     * @param block
     * @param id
     * @param disDPlatform
     * @param zingChoice
     */
    public Song(String thumb, String artist, String artistIds, String duration, String block, String id, String hasVideo, String streamingStatus, String thumbVideo, String genreIds, String disablePlatformWeb, String disSPlatform, String isOfficial, String radioPid, String zingChoice, String name, String disDPlatform) {
        super();
        this.thumb = thumb;
        this.artist = artist;
        this.artistIds = artistIds;
        this.duration = duration;
        this.block = block;
        this.id = id;
        this.hasVideo = hasVideo;
        this.streamingStatus = streamingStatus;
        this.thumbVideo = thumbVideo;
        this.genreIds = genreIds;
        this.disablePlatformWeb = disablePlatformWeb;
        this.disSPlatform = disSPlatform;
        this.isOfficial = isOfficial;
        this.radioPid = radioPid;
        this.zingChoice = zingChoice;
        this.name = name;
        this.disDPlatform = disDPlatform;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getArtistIds() {
        return artistIds;
    }

    public void setArtistIds(String artistIds) {
        this.artistIds = artistIds;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getBlock() {
        return block;
    }

    public void setBlock(String block) {
        this.block = block;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHasVideo() {
        return hasVideo;
    }

    public void setHasVideo(String hasVideo) {
        this.hasVideo = hasVideo;
    }

    public String getStreamingStatus() {
        return streamingStatus;
    }

    public void setStreamingStatus(String streamingStatus) {
        this.streamingStatus = streamingStatus;
    }

    public String getThumbVideo() {
        return thumbVideo;
    }

    public void setThumbVideo(String thumbVideo) {
        this.thumbVideo = thumbVideo;
    }

    public String getGenreIds() {
        return genreIds;
    }

    public void setGenreIds(String genreIds) {
        this.genreIds = genreIds;
    }

    public String getDisablePlatformWeb() {
        return disablePlatformWeb;
    }

    public void setDisablePlatformWeb(String disablePlatformWeb) {
        this.disablePlatformWeb = disablePlatformWeb;
    }

    public String getDisSPlatform() {
        return disSPlatform;
    }

    public void setDisSPlatform(String disSPlatform) {
        this.disSPlatform = disSPlatform;
    }

    public String getIsOfficial() {
        return isOfficial;
    }

    public void setIsOfficial(String isOfficial) {
        this.isOfficial = isOfficial;
    }

    public String getRadioPid() {
        return radioPid;
    }

    public void setRadioPid(String radioPid) {
        this.radioPid = radioPid;
    }

    public String getZingChoice() {
        return zingChoice;
    }

    public void setZingChoice(String zingChoice) {
        this.zingChoice = zingChoice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisDPlatform() {
        return disDPlatform;
    }

    public void setDisDPlatform(String disDPlatform) {
        this.disDPlatform = disDPlatform;
    }

    @Override
    public String toString() {
        return id+",\t"+name+",\t"+artist;
    }
}
