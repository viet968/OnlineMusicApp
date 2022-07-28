package com.example.onlinemusicapp.Model;

import com.squareup.moshi.Json;

public class Streaming {
    @Json(name = "mp4")
    private Mp4 mp4;

    /**
     * No args constructor for use in serialization
     *
     */
    public Streaming() {
    }

    /**
     *
     * @param mp4

     */
    public Streaming(Mp4 mp4) {
        super();
        this.mp4 = mp4;

    }

    public Mp4 getMp4() {
        return mp4;
    }

    public void setMp4(Mp4 mp4) {
        this.mp4 = mp4;
    }


}
