package com.example.onlinemusicapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.onlinemusicapp.Model.SongDB;
import com.example.onlinemusicapp.R;
import com.example.onlinemusicapp.Interface.ItemClick;
import com.makeramen.roundedimageview.RoundedImageView;

public class SongListAdapter extends ListAdapter<SongDB,SongListAdapter.SongItemViewHolder> {
    private ItemClick itemClick;
    public SongListAdapter(@NonNull DiffUtil.ItemCallback<SongDB> diffCallback, ItemClick itemClick) {
        super(diffCallback);
        this.itemClick = itemClick;
    }

    @NonNull
    @Override
    public SongItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SongItemViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.song_item,parent,false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull SongItemViewHolder holder, int position) {
        SongDB getSong = getItem(position);
        holder.txtItemSongName.setText(getSong.getSongName());
        holder.txtItemSingerName.setText(getSong.getSingerName());
        Glide.with(holder.itemView.getContext()).load(getSong.getImageSongLink()).into(holder.songImage);
        int index = getCurrentList().indexOf(getSong)+1;
        if(index < 10) {
            holder.txtSerial.setText(String.valueOf("0"+index++));
        }else holder.txtSerial.setText(String.valueOf(index++));
        holder.itemView.startAnimation(AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.layout_recycle_anim));
        }

        class SongItemViewHolder extends RecyclerView.ViewHolder{
            private TextView txtItemSongName,txtItemSingerName,txtSerial;
            private RoundedImageView songImage;
            public SongItemViewHolder(@NonNull View itemView) {
                super(itemView);
                txtItemSingerName = (TextView)  itemView.findViewById(R.id.txtItemSingerNameSong);
                txtItemSongName = (TextView)  itemView.findViewById(R.id.txtItemSongName);
                txtSerial = (TextView) itemView.findViewById(R.id.txtSerial);
                songImage = (RoundedImageView) itemView.findViewById(R.id.dialogImgSongImage);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(itemClick !=null){
                            int pos = getAbsoluteAdapterPosition();
                            if(pos != RecyclerView.NO_POSITION){
                                itemClick.onClick(pos);
                            }
                        }
                    }
                });
            }

        }
}
