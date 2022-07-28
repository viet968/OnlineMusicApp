package com.example.onlinemusicapp.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.squareup.moshi.Json;

public class GetLyrics {

    @Json(name ="err")

    private int err;
    @Json(name ="msg")

    private String msg;
    @Json(name ="data")

    private LyricsData data;
    @Json(name ="timestamp")

    private long timestamp;

    /**
     * No args constructor for use in serialization
     *
     */
    public GetLyrics() {
    }

    /**
     *
     * @param msg
     * @param err
     * @param data
     * @param timestamp
     */
    public GetLyrics(int err, String msg, LyricsData data, long timestamp) {
        super();
        this.err = err;
        this.msg = msg;
        this.data = data;
        this.timestamp = timestamp;
    }

    public int getErr() {
        return err;
    }

    public void setErr(int err) {
        this.err = err;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public LyricsData getData() {
        return data;
    }

    public void setData(LyricsData data) {
        this.data = data;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

}
