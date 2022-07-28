package com.example.onlinemusicapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.onlinemusicapp.Interface.TopMVItemClick;
import com.example.onlinemusicapp.Model.Item;
import com.example.onlinemusicapp.R;
import com.makeramen.roundedimageview.RoundedImageView;


import java.util.List;

public class MVAdapter extends RecyclerView.Adapter<MVAdapter.ViewHolder> {
    private List<Item> mvs;
    private TopMVItemClick itemClick;
    public MVAdapter(List<Item> mvs, TopMVItemClick itemClick){
        this.mvs = mvs;
        this.itemClick = itemClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.mv_items, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Item item = mvs.get(position);
        Glide.with(holder.itemView.getContext()).load(item.getThumbnailM()).into(holder.imgMVImg);
        holder.txtMVTitle.setText(item.getTitle());
        holder.txtArtistMV.setText(item.getArtistsNames());
        holder.itemView.startAnimation(AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.layout_recycle_anim));
    }

    @Override
    public int getItemCount() {
        return mvs.size();
    }

    class ViewHolder extends  RecyclerView.ViewHolder{
        private RoundedImageView imgMVImg;
        private TextView txtMVTitle,txtArtistMV;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgMVImg = itemView.findViewById(R.id.imgMVAvt);
            txtMVTitle = itemView.findViewById(R.id.txtVideoTitle);
            txtArtistMV = itemView.findViewById(R.id.txtArtistMV);
            itemView.setOnClickListener(view ->{
                itemClick.onClick(getAbsoluteAdapterPosition());
            });
        }
    }
}
