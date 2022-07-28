package com.example.onlinemusicapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.onlinemusicapp.Model.Album;
import com.example.onlinemusicapp.R;
import com.flaviofaria.kenburnsview.KenBurnsView;

import java.util.List;

public class AlbumSliderAdapter extends RecyclerView.Adapter<AlbumSliderAdapter.SlideViewHolder> {
    private List<Album> albumsSlide;

//    protected AlbumSliderAdapter(@NonNull DiffUtil.ItemCallback<Album> diffCallback) {
//        super(diffCallback);
//    }

    public AlbumSliderAdapter(List<Album> albumsSlide) {
        this.albumsSlide = albumsSlide;
    }


    @NonNull
    @Override
    public SlideViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SlideViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_image_view_viewpager,parent,false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull SlideViewHolder holder, int position) {
        holder.setSlide(albumsSlide.get(position));
    }

    @Override
    public int getItemCount() {
        return albumsSlide.size();
    }


    class SlideViewHolder extends RecyclerView.ViewHolder{
        private TextView txtAlbumNameViewPager;
        private KenBurnsView kbvAlbumImage;

        public SlideViewHolder(@NonNull View itemView) {
            super(itemView);
            txtAlbumNameViewPager = (TextView) itemView.findViewById(R.id.albumNameViewPager);
            kbvAlbumImage = (KenBurnsView) itemView.findViewById(R.id.kbvAlbumImage);
        }

        public void setSlide(@NonNull Album album){
            txtAlbumNameViewPager.setText(album.getName());
//            Picasso.get().load(album.getImageUri()).into(kbvAlbumImage);
            Glide.with(itemView.getContext()).load(album.getImageUri()).into(kbvAlbumImage);
        }

    }
}
