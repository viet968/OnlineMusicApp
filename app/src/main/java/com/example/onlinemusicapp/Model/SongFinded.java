package com.example.onlinemusicapp.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.squareup.moshi.Json;

import java.util.List;

public class SongFinded {

    @Json(name ="result")

    private boolean result;
    @Json(name ="data")

    private List<Datum> data = null;

    /**
     * No args constructor for use in serialization
     *
     */
    public SongFinded() {
    }

    /**
     *
     * @param result
     * @param data
     */
    public SongFinded(boolean result, List<Datum> data) {
        super();
        this.result = result;
        this.data = data;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

}
