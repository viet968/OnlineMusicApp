package com.example.onlinemusicapp.Model;

import java.io.Serializable;
import java.util.Objects;

public class SongDB implements Serializable {
    private String songName,singerName,albumName,songLink,imageSongLink;

    public SongDB(String songName, String singerName, String albumName, String songLink) {
        this.songName = songName;
        this.singerName = singerName;
        this.albumName = albumName;
        this.songLink = songLink;
    }

    public SongDB(String songName, String singerName, String albumName, String songLink, String songImageLink) {
        this.songName = songName;
        this.singerName = singerName;
        this.albumName = albumName;
        this.songLink = songLink;
        this.imageSongLink  = songImageLink;
    }

    public SongDB(String songName, String singerName){
        this.songName = songName;
        this.singerName = singerName;
    }

    public SongDB(){

    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getSongLink() {
        return songLink;
    }

    public void setSongLink(String songLink) {
        this.songLink = songLink;
    }

    public String getSingerName() {
        return singerName;
    }

    public void setSingerName(String singerName) {
        this.singerName = singerName;
    }

    public String getImageSongLink() {
        return imageSongLink;
    }

    public void setImageSongLink(String imageSongLink) {
        this.imageSongLink = imageSongLink;
    }

    @Override
    public String toString() {
        return songName+"\n"+singerName+"\n"+albumName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SongDB song = (SongDB) o;
        return Objects.equals(songName, song.songName) && Objects.equals(singerName, song.singerName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(songName, singerName);
    }
}
