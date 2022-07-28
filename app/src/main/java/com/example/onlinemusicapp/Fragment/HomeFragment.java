package com.example.onlinemusicapp.Fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.example.onlinemusicapp.Activity.HomeActivity;
import com.example.onlinemusicapp.Activity.LoginActivity;
import com.example.onlinemusicapp.Adapter.AlbumSliderAdapter;
import com.example.onlinemusicapp.Adapter.DiffUtilSong;
import com.example.onlinemusicapp.Adapter.LinearLayoutManagerWrapper;
import com.example.onlinemusicapp.Adapter.SongListAdapter;
import com.example.onlinemusicapp.Model.Album;
import com.example.onlinemusicapp.Model.SongDB;
import com.example.onlinemusicapp.R;
import com.example.onlinemusicapp.Interface.ItemClick;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import org.jmusixmatch.MusixMatch;
import org.jmusixmatch.MusixMatchException;
import org.jmusixmatch.entity.lyrics.Lyrics;
import org.jmusixmatch.entity.track.Track;
import org.jmusixmatch.entity.track.TrackData;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements ItemClick {
    private HomeActivity context;
    private View homeView;
    private RelativeLayout playerLayout;
    private List<Album> albumsSlide;
    private List<SongDB> songList;
    private AlbumSliderAdapter slideAdapter;
    private ViewPager2 albumsViewpager;
    private RecyclerView recySongList;
    private SongListAdapter songsAdapter;
    public MediaPlayer mediaPlayer;
    private  DiffUtilSong diffUtilSong;

    public String SONG_LAST_PLAYED = "LAST_PLAYED",SONG_NAME = "SONG_NAME",
            SINGER_NAME="SINGER_NAME",SONG_LINK="SONG_LINK",SONG_IMAGE_LINK="SONG_IMAGE_LINK";

    private final DatabaseReference homeFragDataRef = LoginActivity.databaseReference;

    public HomeFragment(HomeActivity context){
        this.context =  context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        homeView = inflater.inflate(R.layout.fragment_home,container,false);
        anhXaViewHomeFragment();
        //add data into slide
        getListAlbumImage();

        //viewpager event
        albumsViewpager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                getSongListOfAlbum(albumsSlide.get(position));
            }
        });

        return  homeView;
    }

    private void anhXaViewHomeFragment(){
        albumsSlide = new ArrayList<>();
        albumsViewpager = homeView.findViewById(R.id.albumsViewpager);
        playerLayout = context.getPlayerLayout();
        mediaPlayer = context.getMediaPlayer();

    }

    private void getListAlbumImage(){
        homeFragDataRef.child("albums").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                snapshot.getChildren().forEach(cSnap->{
                    Album getAlbum = cSnap.getValue(Album.class);
                    if(!albumsSlide.contains(getAlbum)){
                        albumsSlide.add(getAlbum);
                    }
                });

                bindDataToViewPager(albumsSlide);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }

    private void getSongListOfAlbum(Album album){
        songList = new ArrayList<>();
        homeFragDataRef.child("albums").child(album.getName().trim()).child("song list").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                snapshot.getChildren().forEach(cSnap ->{
                    SongDB getSong  = cSnap.getValue(SongDB.class);
                    if(!songList.contains(getSong)){
                        songList.add(getSong);
                    }
                });

                bindDataToSongList(songList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void bindDataToSongList(List<SongDB> songsBind){
        recySongList = homeView.findViewById(R.id.recySongs);
        diffUtilSong = new DiffUtilSong();
        songsAdapter = new SongListAdapter(diffUtilSong,this);
        recySongList.setLayoutManager(new LinearLayoutManagerWrapper(getActivity(),LinearLayoutManager.VERTICAL,false));
        recySongList.setAdapter(songsAdapter);
        songsAdapter.submitList(songsBind);

    }

    private void bindDataToViewPager(List<Album> albumList){
        //adapter
        slideAdapter = new AlbumSliderAdapter(albumList);
        albumsViewpager.setAdapter(slideAdapter);
//        slideAdapter.notifyDataSetChanged();
        albumsViewpager.setClipToPadding(false);
        albumsViewpager.setClipChildren(false);
        albumsViewpager.setOffscreenPageLimit(3);
        albumsViewpager.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1-Math.abs(position);
                page.setScaleY(0.75f + r *0.25f);
            }
        });

        albumsViewpager.setPageTransformer(compositePageTransformer);
    }

    @Override
    public void onClick(int position) {
        context.setIsShow_minimize(true);
        context.showMinimizePlayerLayout();
        context.getActionPresenter().setPlayList(songList);
        context.getActionPresenter().setSongResults(null);
        context.showPlayerLayout(playerLayout);
        context.setDataOnView(songList.get(position));
        context.playSongWhenClickSongItem(songList.get(position));

        context.getMusicService().showNotification(R.drawable.ic_baseline_pause_24,songList.get(position));
    }

    public List<SongDB> getSongList() {
        return songList;
    }
}