package com.example.onlinemusicapp.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.onlinemusicapp.Activity.HomeActivity;
import com.example.onlinemusicapp.Activity.LoginActivity;
import com.example.onlinemusicapp.Adapter.LinearLayoutManagerWrapper;
import com.example.onlinemusicapp.Adapter.SongFoundAdapter;
import com.example.onlinemusicapp.Adapter.SongListAdapter;

import com.example.onlinemusicapp.Interface.IFindSongAPI;
import com.example.onlinemusicapp.Model.Datum;
import com.example.onlinemusicapp.Model.Song;
import com.example.onlinemusicapp.Model.SongFinded;
import com.example.onlinemusicapp.R;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

/*
 * A fragment representing a list of Items.
 */
public class SearchResultFragment extends Fragment {
    private HomeActivity context;
    private RecyclerView resultListRecy;
//    private SongListAdapter resultListAdapter;
    private SongFoundAdapter songFounddAdapter;
//    private DatabaseReference searchDataRef = LoginActivity.databaseReference;
    private IFindSongAPI findSongAPI;
    public SearchResultFragment(HomeActivity context){
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_result_list, container, false);
        resultListRecy = view.findViewById(R.id.resultListRecy);
        resultListRecy.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
       findSongAPI = context.getFindSongAPI();

        Bundle getRequireData = getArguments();
        String require = getRequireData.getString("require");
        searchSong(require);
        return view;
    }


//    private void searchSong(String searchData){
////        List<Album> tempAlbum = new ArrayList<>();
//        List<SongDB> resultSearch = new ArrayList<>();
//        searchDataRef.child("albums").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                snapshot.getChildren().forEach(cSnap -> {
//                    Album currentAlbum = cSnap.getValue(Album.class);
//                    cSnap.child("song list").getChildren().forEach(cSongSnap -> {
//                        SongDB currentSong = cSongSnap.getValue(SongDB.class);
//                        if(currentSong.getSongName().toLowerCase().contains(searchData.toLowerCase())){
//                            resultSearch.add(currentSong);
//                        }
//                    });
//                });
//                DiffUtilSong searchDif = new DiffUtilSong();
//
//                resultListAdapter = new SongListAdapter(searchDif,SearchResultFragment.this);
//                resultListRecy.setLayoutManager(new LinearLayoutManagerWrapper(getActivity(), LinearLayoutManager.VERTICAL,false));
//                resultListRecy.setAdapter(resultListAdapter);
//                resultListAdapter.submitList(resultSearch);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//
//    }

    private void searchSong(String songName){
        List<Song> songFindedList = new ArrayList<>();
        Call<SongFinded> getSongData = findSongAPI.getSongFinded("song,key,code",20,songName);
        getSongData.enqueue(new Callback<SongFinded>() {
            @Override
            public void onResponse(Call<SongFinded> call, retrofit2.Response<SongFinded> response) {
                if(response.isSuccessful()){
                    SongFinded data = response.body();
                    List<Datum> datums = data.getData();
                    if(datums.size()>0) {
                        Datum datum = data.getData().get(0);

                        songFindedList.addAll(datum.getSong());

                        songFounddAdapter = new SongFoundAdapter(context);
                        context.getActionPresenter().setSongResults(songFindedList);
                        resultListRecy.setLayoutManager(new LinearLayoutManagerWrapper(getActivity(), LinearLayoutManager.VERTICAL, false));
                        resultListRecy.setAdapter(songFounddAdapter);
                        songFounddAdapter.submitList(songFindedList);
                    }else{
                        Toast.makeText(context, "No Found   ", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<SongFinded> call, Throwable t) {

            }
        });
    }


//    private void getSongList(String albumSelected, SongDB songSelected){
//        List<SongDB> res = new ArrayList<>();
//        searchDataRef.child("albums").child(albumSelected.trim()).child("song list").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                snapshot.getChildren().forEach(cSnap ->{
//                    SongDB getSong  = cSnap.getValue(SongDB.class);
//                    if(!res.contains(getSong)){
//                        res.add(getSong);
//                    }
//                });
//                context.getActionPresenter().setPlayList(res);
//                context.showPlayerLayout(context.getPlayerLayout());
//                context.setDataOnView(songSelected);
//                context.setGetCurrentPos(res.indexOf(songSelected));
//                context.playSongWhenClickSongItem(songSelected);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }
}