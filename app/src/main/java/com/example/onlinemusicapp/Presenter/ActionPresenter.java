package com.example.onlinemusicapp.Presenter;

import android.media.MediaPlayer;
import android.util.Log;
import android.widget.Toast;

import com.example.onlinemusicapp.Interface.IGetSongAPI;
import com.example.onlinemusicapp.Model.Data;
import com.example.onlinemusicapp.Model.Song;
import com.example.onlinemusicapp.Model.SongDB;
import com.example.onlinemusicapp.Model.SongData;
import com.example.onlinemusicapp.Notification.MusicService;
import com.example.onlinemusicapp.R;
import com.example.onlinemusicapp.View.ActionView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActionPresenter {
    private ActionView actionView;
    private MediaPlayer mediaPlayer;
    private List<SongDB> playList;
    private List<Song> songResults;
    private IGetSongAPI getSongAPI;

    private String createImageURl = "https://photo-resize-zmp3.zmdcdn.me/";
    public ActionPresenter(ActionView actionView,MediaPlayer mediaPlayer) {
        this.actionView = actionView;
        this.mediaPlayer = mediaPlayer;

    }

    public int nextAction(int position){
        mediaPlayer.reset();
        if(playList!=null && songResults == null) {
            if (position < playList.size() - 1) {
                position++;
            } else {
                position = 0;
            }
            actionView.setDataOnView(playList.get(position));
            actionView.playSongWhenClickSongItem(playList.get(position));

        }else if(playList==null && songResults != null) {
            if (position < songResults.size() - 1) {
                position++;
            } else {
                position = 0;
            }
            getSongLink(songResults.get(position));
        }
        return position;
    }


    public int prevAction(int position){
        mediaPlayer.reset();
        if(playList!=null && songResults == null) {
            if (position == 0) {
                position = playList.size() - 1;
            } else {
                position--;
            }
            actionView.setDataOnView(playList.get(position));
            actionView.playSongWhenClickSongItem(playList.get(position));

        }else if(playList==null && songResults != null) {
            if (position == 0) {
                position = songResults.size() - 1;
            } else {
                position--;
            }
            getSongLink(songResults.get(position));
        }

        return position;
    }

    public void playAction(){
        mediaPlayer.start();
    }

    public void pauseAction(){
        mediaPlayer.pause();
    }

    private void getSongLink(Song song){
        Call<SongData> getSongData = getSongAPI.getSongData(song.getId());
        getSongData.enqueue(new Callback<SongData>() {
            @Override
            public void onResponse(Call<SongData> call, Response<SongData> response) {
                int code = response.code();
                if(code == 200){
                    SongData songData = response.body();
                    if(songData.getErr()==0){
                        Data data = songData.getData();
                        actionView.setDataOnView(song);
                        actionView.playSongWhenClickSongItem(song.getName(),song.getArtist(),
                                createImageURl+song.getThumb(),data.get128());

                    }else{
                        actionView.getMessage(songData.getMsg());
                    }
                }
            }

            @Override
            public void onFailure(Call<SongData> call, Throwable t) {
                actionView.getError(t.getMessage());
            }
        });
    }

    public void setPlayList(List<SongDB> playList) {
        this.playList = playList;
    }

    public void setSongResults(List<Song> results){this.songResults = results;}

    public List<SongDB> getPlayList() {
        return playList;
    }

    public List<Song> getSongResults() {
        return songResults;
    }

    public void setGetSongAPI(IGetSongAPI getSongAPI) {
        this.getSongAPI = getSongAPI;
    }
}
