package com.example.onlinemusicapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.onlinemusicapp.Activity.HomeActivity;
import com.example.onlinemusicapp.Interface.IGetSongAPI;

import com.example.onlinemusicapp.Model.Data;
import com.example.onlinemusicapp.Model.Song;
import com.example.onlinemusicapp.Model.SongData;
import com.example.onlinemusicapp.R;
import com.makeramen.roundedimageview.RoundedImageView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SongFoundAdapter extends ListAdapter<Song, SongFoundAdapter.SongItemViewHolder> {
    private String createImageURl = "https://photo-resize-zmp3.zmdcdn.me/";
    private HomeActivity context;
    private IGetSongAPI getSongAPI;

    public SongFoundAdapter(HomeActivity context) {
        super(songFindedDiff);
        this.context = context;
        this.getSongAPI = context.getGetSongAPI();

    }

    private static final DiffUtil.ItemCallback<Song> songFindedDiff = new DiffUtil.ItemCallback<Song>() {
        @Override
        public boolean areItemsTheSame(@NonNull Song oldItem, @NonNull Song newItem) {
            return (oldItem.getId().equals(newItem.getId())) ;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Song oldItem, @NonNull Song newItem) {
            return areItemsTheSame(oldItem,newItem);
        }
    };

    @NonNull
    @Override
    public SongFoundAdapter.SongItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SongFoundAdapter.SongItemViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.song_item,parent,false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull SongItemViewHolder holder, int position) {
        Song song  = getItem(position);
        holder.txtItemSongName.setText(song.getName());
        holder.txtItemSingerName.setText(song.getArtist());
        Glide.with(holder.itemView.getContext()).load(createImageURl+song.getThumb()).into(holder.songImage);
        int index = getCurrentList().indexOf(song)+1;
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
                        int pos = getAbsoluteAdapterPosition();
                        if(pos != RecyclerView.NO_POSITION){
                            context.getLoadingDialog().show();
                            Song songSelected = getItem(pos);
                            Call<SongData> getSongData = getSongAPI.getSongData(songSelected.getId());
                            getSongData.enqueue(new Callback<SongData>() {
                                @Override
                                public void onResponse(Call<SongData> call, Response<SongData> response) {
                                    int code = response.code();
                                    if(code == 200){
                                        SongData songData = response.body();
                                        if(songData.getErr()==0){
                                            Data data = songData.getData();
                                            context.setIsShow_minimize(true);
                                            context.showMinimizePlayerLayout();
                                            context.getActionPresenter().setPlayList(null);
                                            context.getActionPresenter().setSongResults(getCurrentList());
                                            context.showPlayerLayout(context.getPlayerLayout());
                                            context.setDataOnView(songSelected);
                                            context.playSongWhenClickSongItem(songSelected.getName(),songSelected.getArtist(),
                                                    createImageURl+songSelected.getThumb(),data.get128());

                                        }else{
                                            context.getLoadingDialog().dismiss();
                                            Toast.makeText(context, songData.getMsg(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<SongData> call, Throwable t) {
                                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                            }
                }
            });
        }

    }


}
