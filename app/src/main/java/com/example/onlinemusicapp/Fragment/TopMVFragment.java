package com.example.onlinemusicapp.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.onlinemusicapp.Activity.PlayingMVActivity;
import com.example.onlinemusicapp.Adapter.LinearLayoutManagerWrapper;
import com.example.onlinemusicapp.Dialog.CustomProgressDialog;
import com.example.onlinemusicapp.Interface.IGetSongAPI;
import com.example.onlinemusicapp.Interface.TopMVItemClick;
import com.example.onlinemusicapp.Model.GetMV;
import com.example.onlinemusicapp.Model.GetMVData;
import com.example.onlinemusicapp.Model.Item;
import com.example.onlinemusicapp.Model.MV;
import com.example.onlinemusicapp.Adapter.MVAdapter;
import com.example.onlinemusicapp.Model.MVData;
import com.example.onlinemusicapp.Model.Mp4;
import com.example.onlinemusicapp.Model.Streaming;
import com.example.onlinemusicapp.R;
import com.example.onlinemusicapp.RetrofitClient.RetrofitClientGetSong;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TopMVFragment extends Fragment implements TopMVItemClick {
    private RecyclerView topMvViewRecy;
    private  IGetSongAPI getSongAPI;
    private List<Item> mvItem;
    private CustomProgressDialog loadingDialog;
    public TopMVFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_top_m_v, container, false);
        // anh xa
        topMvViewRecy = view.findViewById(R.id.topMvView);
        topMvViewRecy.setLayoutManager(new LinearLayoutManagerWrapper(getContext(),LinearLayoutManager.VERTICAL,false));

        getSongAPI = RetrofitClientGetSong.getInstance().create(IGetSongAPI.class);
        loadingDialog = new CustomProgressDialog(requireContext());
        getTopMV();
        return view;
    }

    private void getTopMV(){
        if( mvItem == null) {
            loadingDialog.show();
            Call<MVData> getTopMV = getSongAPI.getTopMV();
            getTopMV.enqueue(new Callback<MVData>() {
                @Override
                public void onResponse(Call<MVData> call, Response<MVData> response) {
                    if (response.code() == 200) {
                        MVData data = response.body();
                        MV mv = data.getData();
                        mvItem = mv.getItems();
                        topMvViewRecy.setAdapter(new MVAdapter(mvItem, TopMVFragment.this));
                        loadingDialog.cancel();
                    }
                }

                @Override
                public void onFailure(Call<MVData> call, Throwable t) {

                }
            });
        }else{
            topMvViewRecy.setAdapter(new MVAdapter(mvItem, TopMVFragment.this));
        }
    }

    @Override
    public void onClick(int position) {
        Call<GetMVData> getMvDataCall = getSongAPI.getMV(mvItem.get(position).getEncodeId());
        getMvDataCall.enqueue(new Callback<GetMVData>() {
            @Override
            public void onResponse(Call<GetMVData> call, Response<GetMVData> response) {
                if(response.code() == 200) {
                    GetMVData data = response.body();
                    GetMV getMVData = data.getData();
                    String titleMV = getMVData.getTitle();
                    String artistMV = getMVData.getArtistsNames();
                    int view = getMVData.getListen();
                    int like = getMVData.getLike();
                    Streaming streaming = getMVData.getStreaming();
                    Mp4 mp4 = streaming.getMp4();
                    String url720 = mp4.get720p();
//                    Toast.makeText(TopMVActivity.this, url720, Toast.LENGTH_SHORT).show();
////                    Log.d("videourl",url);
                    Intent intent = new Intent(requireActivity(), PlayingMVActivity.class);
                    Bundle sendDataBundle = new Bundle();
                    sendDataBundle.putString("title",titleMV);
                    sendDataBundle.putString("artistMV",artistMV);
                    sendDataBundle.putInt("view",view);
                    sendDataBundle.putInt("like",like);
                    sendDataBundle.putString("url",url720);
                    intent.putExtra("data",sendDataBundle);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<GetMVData> call, Throwable t) {

            }
        });
    }
}