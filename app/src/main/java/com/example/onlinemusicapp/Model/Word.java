package com.example.onlinemusicapp.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.squareup.moshi.Json;

public class Word {

    @Json(name ="startTime")
    
    private int startTime;
    @Json(name ="endTime")
    
    private int endTime;
    @Json(name ="data")
    
    private String data;

    /**
     * No args constructor for use in serialization
     *
     */
    public Word() {
    }

    /**
     *
     * @param data
     * @param startTime
     * @param endTime
     */
    public Word(int startTime, int endTime, String data) {
        super();
        this.startTime = startTime;
        this.endTime = endTime;
        this.data = data;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

}