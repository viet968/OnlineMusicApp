package com.example.onlinemusicapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.onlinemusicapp.Model.Album;
import com.example.onlinemusicapp.R;

import java.util.List;

public class AlbumSpinnerAdapter extends ArrayAdapter<Album> {

    public AlbumSpinnerAdapter(@NonNull Context context, int resource, @NonNull List<Album> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_item_spinner,parent,false);
        TextView txtAlbumName = (TextView) convertView.findViewById(R.id.txtAlbumName);
        ImageView imgViewAlbumImage = (ImageView) convertView.findViewById(R.id.imgAlbumLogo);
        Album album = this.getItem(position);
        if(album!=null){
            txtAlbumName.setText(album.getName());
//            imgViewAlbumImage.setIma
            Glide.with(getContext()).load(album.getImageUri()).into(imgViewAlbumImage);
        }
        return convertView;
    }


}
