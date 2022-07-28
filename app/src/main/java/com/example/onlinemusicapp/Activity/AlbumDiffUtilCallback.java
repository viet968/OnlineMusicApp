package com.example.onlinemusicapp.Activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import com.example.onlinemusicapp.Model.Album;

import java.util.List;

public class AlbumDiffUtilCallback extends DiffUtil.Callback {
    private List<Album> newList;
    private List<Album> oldList;

    public AlbumDiffUtilCallback(List<Album> newList, List<Album> oldList) {
        this.newList = newList;
        this.oldList = oldList;
    }

    @Override
    public int getOldListSize() {
        return (oldList!=null) ? oldList.size() : 0;
    }

    @Override
    public int getNewListSize() {
        return (newList!=null) ? newList.size() : 0;
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return newList.get(newItemPosition).getName().equals(oldList.get(oldItemPosition).getName());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        int result = newList.get(newItemPosition).compareTo(oldList.get(oldItemPosition));
        return result == 0;
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        Album newAlbum = newList.get(newItemPosition);
        Album oldAlbum = oldList.get(oldItemPosition);

        Bundle diff = new Bundle();

        if (newAlbum.getName() != (oldAlbum.getName())) {
            diff.putString("name", newAlbum.getName());
        }
        if (diff.size() == 0) {
            return null;
        }
        return diff;
    }
}
