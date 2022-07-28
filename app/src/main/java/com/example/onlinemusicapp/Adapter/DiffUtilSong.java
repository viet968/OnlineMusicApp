package com.example.onlinemusicapp.Adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.example.onlinemusicapp.Model.SongDB;

public class DiffUtilSong extends DiffUtil.ItemCallback<SongDB> {
    @Override
    public boolean areItemsTheSame(@NonNull SongDB oldItem, @NonNull SongDB newItem) {
        return (oldItem.getSongName() == newItem.getSongName() && oldItem.getSingerName() == newItem.getSingerName()) ;
    }

    @Override
    public boolean areContentsTheSame(@NonNull SongDB oldItem, @NonNull SongDB newItem) {
        return areItemsTheSame(oldItem,newItem);
    }
}
