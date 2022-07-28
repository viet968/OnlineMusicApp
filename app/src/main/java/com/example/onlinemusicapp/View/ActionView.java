package com.example.onlinemusicapp.View;

import com.example.onlinemusicapp.Model.Song;
import com.example.onlinemusicapp.Model.SongDB;
import com.example.onlinemusicapp.Model.SongData;
import com.example.onlinemusicapp.Notification.MusicService;

public interface ActionView {
    public void setDataOnView(SongDB song);
    public void setDataOnView(Song song);
    public void playSongWhenClickSongItem(SongDB song);
    public void playSongWhenClickSongItem(String name,String artist,String thumb,String link);
    public void getMessage(String message);
    public void getError(String error);

}
