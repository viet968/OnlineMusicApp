package com.example.onlinemusicapp.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.squareup.moshi.Json;

public class Mp4 {
    @Json(name = "360p")
    private String _360p;
    @Json(name = "480p")
    private String _480p;
    @Json(name = "720p")
    private String _720p;
    @Json(name = "1080p")
    private String _1080p;

    /**
     * No args constructor for use in serialization
     *
     */
    public Mp4() {
    }

    /**
     *
     * @param _360p
     * @param _480p
     * @param _720p
     * @param _1080p
     */
    public Mp4(String _360p, String _480p, String _720p, String _1080p) {
        super();
        this._360p = _360p;
        this._480p = _480p;
        this._720p = _720p;
        this._1080p = _1080p;
    }

    public String get360p() {
        return _360p;
    }

    public void set360p(String _360p) {
        this._360p = _360p;
    }

    public String get480p() {
        return _480p;
    }

    public void set480p(String _480p) {
        this._480p = _480p;
    }

    public String get720p() {
        return _720p;
    }

    public void set720p(String _720p) {
        this._720p = _720p;
    }

    public String get1080p() {
        return _1080p;
    }

    public void set1080p(String _1080p) {
        this._1080p = _1080p;
    }

}
