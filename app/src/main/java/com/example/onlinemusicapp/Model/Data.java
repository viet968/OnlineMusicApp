package com.example.onlinemusicapp.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.squareup.moshi.Json;

public class Data {

    @Json(name = "128")
    private String _128;
    @Json(name = "320")
    private String _320;

    public Data() {
    }

    /**
     *
     * @param _128
     * @param _320
     */
    public Data(String _128, String _320) {
        super();
        this._128 = _128;
        this._320 = _320;
    }

    public String get128() {
        return _128;
    }

    public void set128(String _128) {
        this._128 = _128;
    }

    public String get320() {
        return _320;
    }

    public void set320(String _320) {
        this._320 = _320;
    }

}


