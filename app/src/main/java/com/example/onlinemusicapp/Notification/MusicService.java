package com.example.onlinemusicapp.Notification;

import static com.example.onlinemusicapp.Notification.ApplicationClass.ACTION_NEXT;
import static com.example.onlinemusicapp.Notification.ApplicationClass.ACTION_PAUSE;
import static com.example.onlinemusicapp.Notification.ApplicationClass.ACTION_PLAY;
import static com.example.onlinemusicapp.Notification.ApplicationClass.ACTION_PREV;
import static com.example.onlinemusicapp.Notification.ApplicationClass.CHANNEL_ID;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.media.session.MediaSessionCompat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.onlinemusicapp.Activity.HomeActivity;
import com.example.onlinemusicapp.Model.Song;
import com.example.onlinemusicapp.Model.SongDB;
import com.example.onlinemusicapp.Presenter.ActionPresenter;
import com.example.onlinemusicapp.R;


public class MusicService extends Service {
    private IBinder iBinder = new MyBinder();
    private ActionPresenter actionPresenter;
    private MediaPlayer mediaPlayer;
    private MediaSessionCompat mediaSession;
    private HomeActivity context;
    private String createImageURl = "https://photo-resize-zmp3.zmdcdn.me/";
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return iBinder;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        mediaSession = new MediaSessionCompat(this,"Player Mp3");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String actionName = intent.getStringExtra("ActionName");
        if(actionName!=null){
            switch (actionName){
                case "play":

                case "pause":
                    if(actionPresenter!=null){
                        context.play_pause();
                    }
                    break;

                case "next":
                    if(actionPresenter!=null){
                        context.next();
                    }
                    break;

                case "previous":

                    if(actionPresenter!=null){
                        context.previous();
                    }
                    break;


            }
        }
        return START_STICKY;

    }

    public void setCallback(ActionPresenter actionPresenter){
        this.actionPresenter = actionPresenter;
    }


    public void setCallback(MediaPlayer mediaPlayer){
        this.mediaPlayer = mediaPlayer;
    }

    public void setContext(HomeActivity homeActivity){
        this.context = homeActivity;
    }


    public void showNotification(int playPauseButton, SongDB noticeSong){

        Intent intent = new Intent(this, HomeActivity.class);
        PendingIntent content = PendingIntent.getActivity(this,0,intent,0);

        Intent prevIntent = new Intent(this, NotificationReceiver.class).setAction(ACTION_PREV);
        PendingIntent prevPendingIntent = PendingIntent.
                getBroadcast(this,0,prevIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        Intent playIntent = new Intent(this, NotificationReceiver.class).setAction(ACTION_PLAY);
        PendingIntent playPendingIntent = PendingIntent.
                getBroadcast(this,0,playIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        Intent nextIntent = new Intent(this, NotificationReceiver.class).setAction(ACTION_NEXT);
        PendingIntent nextPendingIntent = PendingIntent.
                getBroadcast(this,0,nextIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        Intent pauseIntent = new Intent(this, NotificationReceiver.class).setAction(ACTION_PAUSE);
        PendingIntent resumePendingIntent = PendingIntent.
                getBroadcast(this,0,pauseIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this,CHANNEL_ID)
                .setSmallIcon(R.drawable.music_login_logo_2).setContentTitle(noticeSong.getSongName())
                .setContentText(noticeSong.getSingerName()).addAction(R.drawable.ic_baseline_skip_previous_24,"Previous",
                        prevPendingIntent);
                if(!mediaPlayer.isPlaying()){
                    //pausing
                    notificationBuilder.addAction(playPauseButton,"Pause",playPendingIntent);
                }else{
                    notificationBuilder.addAction(playPauseButton,"Play",resumePendingIntent);
                }

                Glide.with(getApplicationContext()).asBitmap().load(noticeSong.getImageSongLink()).into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        notificationBuilder.setLargeIcon(resource);
                        notificationBuilder.addAction(R.drawable.ic_baseline_skip_next_24,"Next",nextPendingIntent)
                                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                                        .setMediaSession(mediaSession.getSessionToken()))
                                .setPriority(NotificationCompat.PRIORITY_LOW).setContentIntent(content)
                                .setOnlyAlertOnce(true).setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
                        Notification notification = notificationBuilder.build();
                        startForeground(2, notification);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });

    }

    public void showNotification(int playPauseButton,Song noticeSong){

        Intent intent = new Intent(this, HomeActivity.class);
        PendingIntent content = PendingIntent.getActivity(this,0,intent,0);

        Intent prevIntent = new Intent(this, NotificationReceiver.class).setAction(ACTION_PREV);
        PendingIntent prevPendingIntent = PendingIntent.
                getBroadcast(this,0,prevIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        Intent playIntent = new Intent(this, NotificationReceiver.class).setAction(ACTION_PLAY);
        PendingIntent playPendingIntent = PendingIntent.
                getBroadcast(this,0,playIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        Intent nextIntent = new Intent(this, NotificationReceiver.class).setAction(ACTION_NEXT);
        PendingIntent nextPendingIntent = PendingIntent.
                getBroadcast(this,0,nextIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        Intent pauseIntent = new Intent(this, NotificationReceiver.class).setAction(ACTION_PAUSE);
        PendingIntent resumePendingIntent = PendingIntent.
                getBroadcast(this,0,pauseIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this,CHANNEL_ID)
                .setSmallIcon(R.drawable.music_login_logo_2).setContentTitle(noticeSong.getName())
                .setContentText(noticeSong.getArtist()).addAction(R.drawable.ic_baseline_skip_previous_24,"Previous",
                        prevPendingIntent);
        if(!mediaPlayer.isPlaying()){
            notificationBuilder.addAction(playPauseButton,"Play",playPendingIntent);
        }else{
            notificationBuilder.addAction(playPauseButton,"Pause",resumePendingIntent);
        }

        Glide.with(getApplicationContext()).asBitmap().load(createImageURl+noticeSong.getThumb()).into(new CustomTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                notificationBuilder.setLargeIcon(resource);
                notificationBuilder.addAction(R.drawable.ic_baseline_skip_next_24,"Next",nextPendingIntent)
                        .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                                .setMediaSession(mediaSession.getSessionToken()))
                        .setPriority(NotificationCompat.PRIORITY_LOW).setContentIntent(content).setAutoCancel(false).setShowWhen(true)
                        .setOnlyAlertOnce(true).setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
                Notification notification = notificationBuilder.build();
                startForeground(2, notification);
            }

            @Override
            public void onLoadCleared(@Nullable Drawable placeholder) {

            }
        });

    }


    public class MyBinder extends Binder{
        public MusicService getService(){
            return MusicService.this;
        }

    }
}
