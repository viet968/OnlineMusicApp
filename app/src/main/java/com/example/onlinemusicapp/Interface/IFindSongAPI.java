package com.example.onlinemusicapp.Interface;

import com.example.onlinemusicapp.Model.SongFinded;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IFindSongAPI {
    @GET("/complete")
    Call<SongFinded> getSongFinded(@Query("type") String type, @Query("num") int num, @Query("query") String query);
}
