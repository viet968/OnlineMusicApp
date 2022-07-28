package com.example.onlinemusicapp.Interface;

import com.example.onlinemusicapp.Model.GetLyrics;
import com.example.onlinemusicapp.Model.GetMVData;
import com.example.onlinemusicapp.Model.MVData;
import com.example.onlinemusicapp.Model.SongData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IGetSongAPI {
    @GET("/api/song")
    Call<SongData> getSongData(@Query("id") String id);

    @GET("/api/lyrics")
    Call<GetLyrics> getLyrics(@Query("id") String id);

    @GET("/api/topmv")
    Call<MVData> getTopMV();

    @GET("/api/getmv")
    Call<GetMVData> getMV(@Query("id") String id);
}
