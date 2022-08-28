package com.example.onlinemusicapp.Fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.onlinemusicapp.Activity.HomeActivity;
import com.example.onlinemusicapp.Model.Song;
import com.example.onlinemusicapp.Model.SongDB;
import com.example.onlinemusicapp.R;
import com.makeramen.roundedimageview.RoundedImageView;


public class MinimizePlayerFragment extends Fragment {
    private HomeActivity context;
    private View minimizePlayerView;
    private ImageButton imgBtPlayPauseSongMinimizePlayer,imgBtNextSongMinimizePlayer,imgBtClose;
    private TextView txtSongNameMinimizePlayer,txtSingerNameSongMinimizePlayer;
    private  RoundedImageView imgSongPlayingMinimize;
    private String createImageURl = "https://photo-resize-zmp3.zmdcdn.me/";
    SharedPreferences preferences;
    private  MediaPlayer mediaPlayer;// = HomeFragment.mediaPlayer;
    public String SONG_LAST_PLAYED = "LAST_PLAYED",SONG_NAME = "SONG_NAME",
            SINGER_NAME="SINGER_NAME",SONG_LINK="SONG_LINK",SONG_IMAGE_LINK="SONG_IMAGE_LINK";

    public MinimizePlayerFragment(HomeActivity context){this.context = context;}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        minimizePlayerView = inflater.inflate(R.layout.fragment_minimize_player, container, false);
        imgBtPlayPauseSongMinimizePlayer = minimizePlayerView.findViewById(R.id.imgPlayPauseSongMinimizePlayer);
        imgBtNextSongMinimizePlayer = minimizePlayerView.findViewById(R.id.imgNextSongMinimizePlayer);
        imgBtClose = minimizePlayerView.findViewById(R.id.imgBtClose);
        txtSongNameMinimizePlayer = minimizePlayerView.findViewById(R.id.txtSongNameMinimizePlayer);
        txtSingerNameSongMinimizePlayer = minimizePlayerView.findViewById(R.id.txtSingerNameSongMinimizePlayer);
        imgSongPlayingMinimize = minimizePlayerView.findViewById(R.id.imgSongMinimizePlayer);
        mediaPlayer = context.getMediaPlayer();
        preferences = context.getSharedPreferences(SONG_LAST_PLAYED,MODE_PRIVATE);

//        startAnim(mediaPlayer.isPlaying());
        getDataPre();
        //minimize next song
        imgBtNextSongMinimizePlayer.setOnClickListener(VIEW ->{
            context.next();
        });

        imgBtPlayPauseSongMinimizePlayer.setOnClickListener(view ->{
            context.play_pause();
        });
        //hide
        imgBtClose.setOnClickListener(view -> {
            context.hideMinimizePlayerLayout();
        });
        return  minimizePlayerView;
    }

    public void setMinimizeData(SongDB song){
        String songName = song.getSongName();
        String singerName = song.getSingerName();

        if(singerName.length()>=20){
            txtSingerNameSongMinimizePlayer.setText(singerName.substring(0,15)+"...");
        }else{
            txtSingerNameSongMinimizePlayer.setText(singerName);
        }

        if(songName.length() >=20){
            txtSongNameMinimizePlayer.setText(songName.substring(0,15)+"...");
        }else{
            txtSongNameMinimizePlayer.setText(songName);
        }

        Glide.with(context).load(song.getImageSongLink()).into(imgSongPlayingMinimize);
        startAnim(true);
        imgBtPlayPauseSongMinimizePlayer.setImageResource(R.drawable.ic_round_pause_32);
    }

    public void setMinimizeData(Song song){
        String songName = song.getName();
        String singerName = song.getArtist();

        if(singerName.length()>=20){
            txtSingerNameSongMinimizePlayer.setText(singerName.substring(0,15)+"...");
        }else{
            txtSingerNameSongMinimizePlayer.setText(singerName);
        }

        if(songName.length() >=20){
            txtSongNameMinimizePlayer.setText(songName.substring(0,15)+"...");
        }else{
            txtSongNameMinimizePlayer.setText(songName);
        }

        Glide.with(context).load(createImageURl+song.getThumb()).into(imgSongPlayingMinimize);
        startAnim(true);
        imgBtPlayPauseSongMinimizePlayer.setImageResource(R.drawable.ic_round_pause_32);
    }


    private void getDataPre(){
        String getSongNamePre = preferences.getString(SONG_NAME,null);
        String getSingerNamePre = preferences.getString(SINGER_NAME,null);
        String getSongLinkPre = preferences.getString(SONG_LINK,null);
        String getSongImageLinkPre = preferences.getString(SONG_IMAGE_LINK,null);
        if(getSingerNamePre == null && getSongNamePre == null){
            txtSongNameMinimizePlayer.setText("No Data!");
            txtSingerNameSongMinimizePlayer.setText("No Data!");
            imgSongPlayingMinimize.setImageResource(R.drawable.default_song_image);
        }else {
            txtSongNameMinimizePlayer.setText(getSongNamePre);
            txtSingerNameSongMinimizePlayer.setText(getSingerNamePre);
            Glide.with(context).load(getSongImageLinkPre).into(imgSongPlayingMinimize);
        }
    }

    private void startAnim(boolean playing){
        Runnable run = new Runnable() {
            @Override
            public void run() {
                imgSongPlayingMinimize.animate().rotationBy(360).withEndAction(this).setDuration(10000)
                        .setInterpolator(new LinearInterpolator()).start();
            }
        };
        if(playing) {
            run.run();
        }else{
            imgSongPlayingMinimize.animate().cancel();
        }
    }

    public void isPlaying(boolean playing){
        if(playing) {
            imgBtPlayPauseSongMinimizePlayer.setImageResource(R.drawable.ic_round_pause_32);
        }else{
            imgBtPlayPauseSongMinimizePlayer.setImageResource(R.drawable.ic_round_play_arrow_32);
        }
    }

}
