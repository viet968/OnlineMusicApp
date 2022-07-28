package com.example.onlinemusicapp.Presenter;

import com.example.onlinemusicapp.View.ItemClickView;

public class ItemClickPresenter {
    private ItemClickView itemClickView;

    public ItemClickPresenter(ItemClickView itemClickView) {
        this.itemClickView = itemClickView;
    }

    public void onClick(int position){
        itemClickView.onClick(position);
    }
}
