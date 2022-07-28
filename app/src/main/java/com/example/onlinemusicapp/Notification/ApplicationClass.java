package com.example.onlinemusicapp.Notification;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;


public class ApplicationClass extends Application {
    public static final String CHANNEL_ID = "Music";
    public static final String ACTION_PREV = "PREVIOUS";
    public static final String ACTION_NEXT = "NEXT";
    public static final String ACTION_PLAY = "PLAY";
    public static final String ACTION_PAUSE = "PAUSE";


    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChanel();
    }

    private void createNotificationChanel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            NotificationChannel channelSong = new NotificationChannel
                    (CHANNEL_ID,"Song Chanel", NotificationManager.IMPORTANCE_DEFAULT);
            channelSong.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            channelSong.enableLights(true);
            NotificationManager manager =  getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channelSong);
        }
    }
}
