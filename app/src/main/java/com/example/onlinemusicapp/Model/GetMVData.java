package com.example.onlinemusicapp.Model;

import com.squareup.moshi.Json;

public class GetMVData {
    @Json(name = "err")
    private int err;
    @Json(name = "msg")
    private String msg;
    @Json(name = "data")
    private GetMV data;
    @Json(name = "timestamp")
    private long timestamp;

    /**
     * No args constructor for use in serialization
     *
     */
    public GetMVData() {
    }

    /**
     *
     * @param msg
     * @param err
     * @param data
     * @param timestamp
     */
    public GetMVData(int err, String msg, GetMV data, long timestamp) {
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

    public GetMV getData() {
        return data;
    }

    public void setData(GetMV data) {
        this.data = data;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

}
