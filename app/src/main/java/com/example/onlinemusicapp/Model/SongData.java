package com.example.onlinemusicapp.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.squareup.moshi.Json;

public class SongData {

    @Json(name ="err")
    
    private int err;
    @Json(name ="msg")
    
    private String msg;
    @Json(name ="url")
    
    private String url;
    @Json(name ="data")
    
    private Data data;
    @Json(name ="timestamp")
    
    private long timestamp;

    /**
     * No args constructor for use in serialization
     */
    public SongData() {
    }

    /**
     * @param msg
     * @param err
     * @param data
     * @param timestamp
     */
    public SongData(int err, String msg, Data data, long timestamp) {
        super();
        this.err = err;
        this.msg = msg;
        this.data = data;
        this.timestamp = timestamp;
    }

    /**
     * @param msg
     * @param err
     * @param url
     * @param timestamp
     */
    public SongData(int err, String msg, String url, long timestamp) {
        this.err = err;
        this.msg = msg;
        this.url = url;
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

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
