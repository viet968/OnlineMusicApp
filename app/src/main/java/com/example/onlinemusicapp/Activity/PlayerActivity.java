package com.example.onlinemusicapp.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.onlinemusicapp.Dialog.CustomProgressDialog;
import com.example.onlinemusicapp.Fragment.HomeFragment;
import com.example.onlinemusicapp.Model.Song;
import com.example.onlinemusicapp.Notification.MusicService;
import com.example.onlinemusicapp.Presenter.ActionPresenter;
import com.example.onlinemusicapp.R;
import com.example.onlinemusicapp.View.ActionView;

import java.io.IOException;
import java.text.SimpleDateFormat;

public class PlayerActivity extends AppCompatActivity implements ServiceConnection, ActionView {
    private TextView txtSongPlaying,txtAlbumPlaying,txtSingerSongPlaying,txtTimeSongPlaying;
    private ProgressBar playProgress;
    private ImageButton imgBtPrev,imgBtNext,imgBtPlay_Pause,imgButtonBack;
    private SeekBar skVolume;
    private MediaPlayer mediaPlayer = HomeFragment.mediaPlayer;
    private ImageView imgAlbumPlayingImage;
    private CustomProgressDialog loadingDialog;
    private Song giveSong;
    int getCurrentPos;
    private AudioManager audioManager;
    SimpleDateFormat format = new SimpleDateFormat("mm:ss");
    private MusicService musicService;
    private ActionPresenter actionPresenter;
    private String getAlbumImageLink;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        anhXa();
        setDataOnView(giveSong);
        // next song
        imgBtNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCurrentPos = actionPresenter.nextAction(getCurrentPos);
                musicService.showNotification(R.drawable.ic_baseline_pause_24,getAlbumImageLink);
            }
        });

        //prev song
        imgBtPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCurrentPos = actionPresenter.prevAction(getCurrentPos);
                musicService.showNotification(R.drawable.ic_baseline_pause_24,getAlbumImageLink);
            }
        });

        //pause_play song
        imgBtPlay_Pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaPlayer.isPlaying()){
                    imgBtPlay_Pause.setImageResource(R.drawable.play_song_icon);
                    actionPresenter.pauseAction();
                    musicService.showNotification(R.drawable.ic_baseline_play_arrow_24,getAlbumImageLink);
                }else{
                    imgBtPlay_Pause.setImageResource(R.drawable.pause_song_icon);
                    actionPresenter.playAction();
                    musicService.showNotification(R.drawable.ic_baseline_pause_24,getAlbumImageLink);
                }
            }
        });
        // play mp3 after load done
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                loadingDialog.dismiss();
                mediaPlayer.start();
                musicService.showNotification(R.drawable.ic_baseline_pause_24,getAlbumImageLink);
                upDateDuration();
                imgBtPlay_Pause.setImageResource(R.drawable.pause_song_icon);
            }
        });

        //back
        imgButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlayerActivity.super.onBackPressed();
            }
        });


        //seek bar volume event
        skVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,i,AudioManager.ADJUST_SAME);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        playSongWhenClickSongItem(giveSong);
    }

    private void anhXa(){
        txtAlbumPlaying = findViewById(R.id.txtAlbumNamePlaying);
        txtSongPlaying = findViewById(R.id.txtSongNamePlaying);
        txtSingerSongPlaying = findViewById(R.id.txtSingerNameSongPlaying);
        txtTimeSongPlaying = findViewById(R.id.txtTimeSongPlaying);
        playProgress = findViewById(R.id.playProgress);
        skVolume = findViewById(R.id.seekBarVolume);
        imgBtNext = findViewById(R.id.nextSongButton);
        imgBtPrev = findViewById(R.id.prevSongButton);
        imgBtPlay_Pause = findViewById(R.id.playSongButton);
        imgButtonBack =  findViewById(R.id.imgButtonBack);
        imgAlbumPlayingImage = findViewById(R.id.imgViewAlbumPlayingImage);
        loadingDialog = new CustomProgressDialog(this);
        giveSong = getSongData();
        audioManager = (AudioManager) this.getSystemService(AUDIO_SERVICE);
        skVolume.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        Intent serviceIntent = new Intent(this,MusicService.class);
        bindService(serviceIntent,this,BIND_AUTO_CREATE);

        actionPresenter = new ActionPresenter(this,mediaPlayer);

    }

    private Song getSongData(){
        Intent receiveSongData = getIntent();
        getCurrentPos = getIntent().getIntExtra("currentPosition",0);
        getAlbumImageLink = getIntent().getStringExtra("albumImageLink");
        return (Song) receiveSongData.getSerializableExtra("song");
    }

    @Override
    public void setDataOnView(Song giveSong){
        txtSongPlaying.setText(giveSong.getSongName());
        txtAlbumPlaying.setText(giveSong.getAlbumName());
        txtSingerSongPlaying.setText(giveSong.getSingerName());
    }

    @Override
    public void playSongWhenClickSongItem(Song song){
        mediaPlayer.setAudioAttributes(new AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build());

        mediaPlayer.reset();

        try {

            mediaPlayer.setDataSource(song.getSongLink());
            Glide.with(this).load(getAlbumImageLink).into(imgAlbumPlayingImage);
            mediaPlayer.prepareAsync();
            loadingDialog.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void upDateDuration(){
        playProgress.setProgress(0);
        playProgress.setMax(mediaPlayer.getDuration());

        Handler handler  = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                txtTimeSongPlaying.setText(format.format(mediaPlayer.getCurrentPosition()));
                setVolumeSeekBar(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
                setPlayProgress(mediaPlayer.getCurrentPosition());
                //start animation
                imgAlbumPlayingImage.animate().rotationBy(360).withEndAction(this).setDuration(10000)
                        .setInterpolator(new LinearInterpolator()).start();

                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        try {
                            mediaPlayer.reset();
                            getCurrentPos = actionPresenter.nextAction(getCurrentPos);
                        }catch (Exception e){
                            Toast.makeText(PlayerActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });

                handler.postDelayed(this,100);
            }
        },100);
    }

    private void stopAnimation(){
        playProgress.setProgress(0);
        imgAlbumPlayingImage.animate().cancel();
    }

    private void setPlayProgress(int progress){
        playProgress.setProgress(progress);
    }
    private void setVolumeSeekBar(int volume){
        skVolume.setProgress(volume);
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        MusicService.MyBinder binder = (MusicService.MyBinder) iBinder;
        musicService = binder.getService();
        musicService.setCallback(actionPresenter);
        musicService.setCallback(getCurrentPos);
        musicService.setCallback(mediaPlayer);
        musicService.showNotification(R.drawable.ic_baseline_pause_24,getAlbumImageLink);
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        musicService =null;
    }

}