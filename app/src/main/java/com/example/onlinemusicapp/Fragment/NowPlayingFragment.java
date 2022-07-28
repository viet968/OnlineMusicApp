package com.example.onlinemusicapp.Fragment;

import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.onlinemusicapp.Activity.HomeActivity;
import com.example.onlinemusicapp.R;
import com.makeramen.roundedimageview.RoundedImageView;

public class NowPlayingFragment extends Fragment {
    private View minimizePlayerView;
    private ImageButton imgBtPlayPauseSongMinimizePlayer,imgBtNextSongMinimizePlayer;
    private TextView txtSongNameMinimizePlayer,txtSingerNameSongMinimizePlayer;
    private  RoundedImageView imgSongPlayingMinimize;
    private MediaPlayer mediaPlayer = HomeFragment.mediaPlayer;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        minimizePlayerView = inflater.inflate(R.layout.fragment_now_playing, container, false);
        imgBtPlayPauseSongMinimizePlayer = minimizePlayerView.findViewById(R.id.imgPlayPauseSongMinimizePlayer);
        imgBtNextSongMinimizePlayer = minimizePlayerView.findViewById(R.id.imgNextSongMinimizePlayer);
        txtSongNameMinimizePlayer = minimizePlayerView.findViewById(R.id.txtSongNameMinimizePlayer);
        txtSingerNameSongMinimizePlayer = minimizePlayerView.findViewById(R.id.txtSingerNameSongMinimizePlayer);
        imgSongPlayingMinimize = minimizePlayerView.findViewById(R.id.imgSongMinimizePlayer);

        //minimize next song
        imgBtNextSongMinimizePlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        imgBtPlayPauseSongMinimizePlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                    imgBtPlayPauseSongMinimizePlayer.setImageResource(R.drawable.play_song_icon);
                }else{
                    mediaPlayer.start();
                    imgBtPlayPauseSongMinimizePlayer.setImageResource(R.drawable.pause_song_icon);
                }
            }
        });
        return  minimizePlayerView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(HomeActivity.show_minimize){
            txtSongNameMinimizePlayer.setText(HomeActivity.SONG_NAME);
            txtSingerNameSongMinimizePlayer.setText(HomeActivity.SINGER_NAME);
        }
        if(mediaPlayer.isPlaying()){
            imgBtPlayPauseSongMinimizePlayer.setImageResource(R.drawable.pause_song_icon);
        }else{
            imgBtPlayPauseSongMinimizePlayer.setImageResource(R.drawable.play_song_icon);
        }
    }
}