package com.example.onlinemusicapp.RetrofitClient;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class RetrofitClientFindSong {
    private  static Retrofit instance;

    public static Retrofit getInstance(){
        if(instance == null){
            instance = new Retrofit.Builder().baseUrl("http://ac.zingmp3.vn").
                    addConverterFactory(MoshiConverterFactory.create()).
                    addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build();
        }
        return instance;
    }
}
