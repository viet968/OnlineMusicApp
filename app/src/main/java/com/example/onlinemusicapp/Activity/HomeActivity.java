package com.example.onlinemusicapp.Activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.StrictMode;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.example.onlinemusicapp.Dialog.CustomProgressDialog;
import com.example.onlinemusicapp.Fragment.HomeFragment;
import com.example.onlinemusicapp.Fragment.MinimizePlayerFragment;
import com.example.onlinemusicapp.Fragment.SearchResultFragment;
import com.example.onlinemusicapp.Fragment.TopMVFragment;
import com.example.onlinemusicapp.Fragment.UploadFragment;
import com.example.onlinemusicapp.Interface.IFindSongAPI;
import com.example.onlinemusicapp.Interface.IGetSongAPI;
import com.example.onlinemusicapp.Model.GetLyrics;
import com.example.onlinemusicapp.Model.LyricsData;
import com.example.onlinemusicapp.Model.Sentence;
import com.example.onlinemusicapp.Model.Song;
import com.example.onlinemusicapp.Model.SongDB;
import com.example.onlinemusicapp.Model.Word;
import com.example.onlinemusicapp.Notification.MusicService;
import com.example.onlinemusicapp.Presenter.ActionPresenter;
import com.example.onlinemusicapp.R;
import com.example.onlinemusicapp.RetrofitClient.RetrofitClientFindSong;
import com.example.onlinemusicapp.RetrofitClient.RetrofitClientGetSong;
import com.example.onlinemusicapp.View.ActionView;
import com.example.onlinemusicapp.animations.RotateAnimation;

import org.jmusixmatch.MusixMatch;
import org.jmusixmatch.MusixMatchException;
import org.jmusixmatch.entity.lyrics.Lyrics;
import org.jmusixmatch.entity.track.Track;
import org.jmusixmatch.entity.track.TrackData;
import org.junit.rules.Stopwatch;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

import io.reactivex.schedulers.Schedulers;
import me.ibrahimsn.lib.OnItemSelectedListener;
import me.ibrahimsn.lib.SmoothBottomBar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity implements ServiceConnection,ActionView {
    private FrameLayout homeLayout,minimizeLayout;
    private RelativeLayout playerLayout;
    private SearchView edtSearch;
    private SmoothBottomBar btHomeNavigation;
    private Fragment uploadFragment, searchFrag;
    private HomeFragment homeFragment;
    private TopMVFragment topMVFragment;
    private MinimizePlayerFragment minimizeFragment;
    public static String SONG_LAST_PLAYED = "LAST_PLAYED",SONG_NAME = "SONG_NAME",
            SINGER_NAME="SINGER_NAME",SONG_LINK="SONG_LINK",SONG_IMAGE_LINK="SONG_IMAGE_LINK";
    public static boolean show_minimize = false;
    SharedPreferences preferences;
    private String getUsernameLogged;
    private MediaPlayer mediaPlayer;// = HomeFragment.mediaPlayer;
    // retroifit api
    private IFindSongAPI findSongAPI;
    private IGetSongAPI getSongAPI;
    private String createImageURl = "https://photo-resize-zmp3.zmdcdn.me/";
    //Musix Match
    private final String apiKey = "d0b392b484d86957bcb5f7e668773e48";
    private MusixMatch musixMatch;

    //view of the player activity
    ////////////////////////////////////////////////////////////////////////////
    private TextView txtSongPlaying,txtAlbumPlaying,txtSingerSongPlaying,txtTimeSongPlaying,txtLyrics;
    private ProgressBar playProgress;
    private ImageButton imgBtPrev,imgBtNext,imgBtPlay_Pause,imgButtonBack;
    private SeekBar skVolume;

    private ImageView imgAlbumPlayingImage;
    private CustomProgressDialog loadingDialog;
    int getCurrentPos;
    boolean isShow_minimize;
    private AudioManager audioManager;
    SimpleDateFormat format = new SimpleDateFormat("mm:ss");
    private MusicService musicService;
    private ActionPresenter actionPresenter;
    private RotateAnimation rotateAnimation;
    ExecutorService executorService;
    private final Handler handler = new Handler(Looper.getMainLooper());
    ////////////////////////////////////////////////////////////////////////////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.HomeActivity);
        Configuration config = getResources().getConfiguration();
        if (config.smallestScreenWidthDp >= 400) {
            setContentView(R.layout.activity_home_4xx);
        } else {
            setContentView(R.layout.activity_home);
        }

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        executorService = Executors.newSingleThreadExecutor();

        AnhXa();
        replaceFrag(homeFragment);
        addMinimizePlayerFragment();

        // when click item of the bottom navigation bar
        btHomeNavigation.setOnItemSelectedListener(new OnItemSelectedListener() {
            int index = 0;
            @Override
            public boolean onItemSelect(int i) {
                switch (i){
                    case 1:
//                        if(!mediaPlayer.isPlaying() && isShow_minimize ) {
//                            hideMinimizePlayerLayout();
//                        }
                        index = 1;
                        replaceFrag(topMVFragment);
                        break;
                    case 2:

                        if(getUsernameLogged.equals("admin")) {
//                            if(!mediaPlayer.isPlaying() && isShow_minimize ) {
//                                hideMinimizePlayerLayout();
//                            }
                            index = 2;
                            replaceFrag(uploadFragment);
                            edtSearch.setQuery("",false);
                        }else{
                            confirm();
                            btHomeNavigation.setItemActiveIndex(index);
                        }
                        break;

                    default:
                        index = 0;
                        if(!isShow_minimize && mediaPlayer.isPlaying()) {
                            showMinimizePlayerLayout();
                        }
                        replaceFrag(homeFragment);

                }
                return true;
            }
        });

        //search event
        edtSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                edtSearch.clearFocus();
                if(!query.isEmpty()){
                  Bundle searchData = new Bundle();
                  searchData.putString("require",query);
                  searchFrag = new SearchResultFragment(HomeActivity.this);
                  searchFrag.setArguments(searchData);
                  replaceFrag(searchFrag);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        //minimize layout event
        minimizeLayout.setOnClickListener(view ->{
            showPlayerLayout(playerLayout);
        });

        // play mp3 after load done
        mediaPlayer.setOnPreparedListener(mediaPlayer ->{
            loadingDialog.dismiss();
            mediaPlayer.start();
            upDateDuration();
            imgBtPlay_Pause.setImageResource(R.drawable.ic_round_pause_48);
        });


        // next song
        imgBtNext.setOnClickListener(view ->{
            next();
        });

        //prev song
        imgBtPrev.setOnClickListener(view ->{
            previous();
        });

        //pause_play song
        imgBtPlay_Pause.setOnClickListener(view ->{
            play_pause();
        });

        //seek bar volume event
        skVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,i,AudioManager.ADJUST_SAME);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        imgButtonBack.setOnClickListener(view ->{
            hidePlayerLayout(playerLayout);
        });
    }

    private void confirm(){
        AlertDialog.Builder builder = new AlertDialog.Builder(
                new ContextThemeWrapper(this, android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth));
        builder.setTitle("Note");
        builder.setIcon(R.drawable.ic_baseline_warning_24);
        builder.setMessage("You don't is ADMIN!!!");

        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        builder.show();
    }

    private void AnhXa(){
        homeLayout = findViewById(R.id.homeLayout);
        minimizeLayout = (FrameLayout) findViewById(R.id.minimizeLayout);
        playerLayout = findViewById(R.id.playerLayout);
        btHomeNavigation = (SmoothBottomBar) findViewById(R.id.btHomeNavigation);
        edtSearch = findViewById(R.id.edtSearch);
        edtSearch.setFocusable(false);
        musixMatch = new MusixMatch(apiKey);

        isShow_minimize = false;

        homeFragment = new HomeFragment(this);
        uploadFragment = new UploadFragment();
        topMVFragment = new TopMVFragment();

        mediaPlayer = new MediaPlayer();
        minimizeFragment = new MinimizePlayerFragment(HomeActivity.this);
        preferences = getSharedPreferences(SONG_LAST_PLAYED,MODE_PRIVATE);
        getUsernameLogged = getIntent().getStringExtra("usernameLogged");
        findSongAPI = RetrofitClientFindSong.getInstance().create(IFindSongAPI.class);
        getSongAPI = RetrofitClientGetSong.getInstance().create(IGetSongAPI.class);


        AnhXaPlayerActivity();
    }

    private void AnhXaPlayerActivity(){
        txtAlbumPlaying = findViewById(R.id.txtAlbumNamePlaying);
        txtSongPlaying = findViewById(R.id.txtSongNamePlaying);
        txtSingerSongPlaying = findViewById(R.id.txtSingerNameSongPlaying);
        txtTimeSongPlaying = findViewById(R.id.txtTimeSongPlaying);
        txtLyrics = findViewById(R.id.txtLyrics);
        playProgress = findViewById(R.id.playProgress);
        skVolume = findViewById(R.id.seekBarVolume);
        imgBtNext = findViewById(R.id.nextSongButton);
        imgBtPrev = findViewById(R.id.prevSongButton);
        imgBtPlay_Pause = findViewById(R.id.playSongButton);
        imgButtonBack =  findViewById(R.id.imgButtonBack);
        imgAlbumPlayingImage = findViewById(R.id.imgViewAlbumPlayingImage);
        loadingDialog = new CustomProgressDialog(this);
        audioManager = (AudioManager) this.getSystemService(AUDIO_SERVICE);
        skVolume.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));

        actionPresenter = new ActionPresenter(this,mediaPlayer);
        rotateAnimation = new RotateAnimation(imgAlbumPlayingImage);
        actionPresenter.setGetSongAPI(getSongAPI);
    }

    private void replaceFrag(Fragment frag){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right_frag_anim, R.anim.exit_to_right_frag_anim,
                R.anim.enter_from_right_frag_anim, R.anim.exit_to_right_frag_anim);
        transaction.replace(R.id.homeLayout,frag);
//        transaction.addToBackStack(null);
//        transaction.disallowAddToBackStack();
        transaction.commit();
    }

    private void hideNavigation(SmoothBottomBar view){
        view.animate().translationY(view.getHeight());
    }

    private void showNavigation(SmoothBottomBar view){
        view.animate().translationY(0);
    }

    public void showPlayerLayout(RelativeLayout view){
        if(isShow_minimize) {
            view.setVisibility(View.VISIBLE);

            view.setAlpha(0.f);
            hideNavigation(btHomeNavigation);
            if (view.getHeight() > 0) {
                slideUpNow(view);
            } else {
                // wait till height is measured
                view.post(new Runnable() {
                    @Override
                    public void run() {
                        slideUpNow(view);
                    }
                });
            }
        }

    }

    private void slideUpNow(RelativeLayout view){
        view.setTranslationY(view.getHeight());
        view.animate()
                .translationY(0)
                .alpha(1.f)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        view.setVisibility(View.VISIBLE);
                        view.setAlpha(1.f);
                        homeLayout.setVisibility(View.GONE);
                        minimizeLayout.setVisibility(View.GONE);
                        edtSearch.setVisibility(View.GONE);
                    }
                });
    }

    public void hideMinimizePlayerLayout(){
         LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) homeLayout.getLayoutParams();
         params1.weight=9.4f;
         homeLayout.setLayoutParams(params1);
         LinearLayout.LayoutParams params2 = (LinearLayout.LayoutParams) minimizeLayout.getLayoutParams();
         params2.weight=0f;
         minimizeLayout.setLayoutParams(params2);

    }

    public void showMinimizePlayerLayout(){
        LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) homeLayout.getLayoutParams();

        homeLayout.setLayoutParams(params1);
        LinearLayout.LayoutParams params2 = (LinearLayout.LayoutParams) minimizeLayout.getLayoutParams();

        minimizeLayout.setLayoutParams(params2);

        Configuration config = getResources().getConfiguration();
        if(config.smallestScreenWidthDp >= 400){
            params1.weight=8.7f;
            params2.weight=1.3f;
        }else{
            params1.weight=8.55f;
            params2.weight=1.55f;
        }
    }



    public void hidePlayerLayout(RelativeLayout view){
        showNavigation(btHomeNavigation);
        homeLayout.setVisibility(View.VISIBLE);
        minimizeLayout.setVisibility(View.VISIBLE);
        edtSearch.setVisibility(View.VISIBLE);
        view.animate()
                .translationY(view.getHeight())
                .alpha(0.f)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        // superfluous restoration
                        view.setVisibility(View.GONE);
                        view.setAlpha(1.f);
                        view.setTranslationY(0.f);

                    }
                });
    }

    private void addMinimizePlayerFragment(){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.minimizeLayout,minimizeFragment);
        transaction.commit();
    }

    public void next(){
        getCurrentPos = actionPresenter.nextAction(getCurrentPos);
        //stop animation
        rotateAnimation.stop();
    }

    public void previous(){
        getCurrentPos = actionPresenter.prevAction(getCurrentPos);
        //stop animation
        rotateAnimation.stop();
    }

    public void play_pause(){
        if(mediaPlayer.isPlaying()){
            imgBtPlay_Pause.setImageResource(R.drawable.ic_round_play_arrow_48);
            actionPresenter.pauseAction();
            minimizeFragment.isPlaying(false);

            if(actionPresenter.getPlayList() == null && actionPresenter.getSongResults() != null){
                musicService.showNotification(R.drawable.ic_baseline_play_arrow_24,actionPresenter.getSongResults().get(getCurrentPos));

            }else if(actionPresenter.getPlayList() != null && actionPresenter.getSongResults() == null){
                musicService.showNotification(R.drawable.ic_baseline_play_arrow_24,actionPresenter.getPlayList().get(getCurrentPos));
            }

        }else{
            imgBtPlay_Pause.setImageResource(R.drawable.ic_round_pause_48);
            actionPresenter.playAction();
            minimizeFragment.isPlaying(true);

            if(actionPresenter.getPlayList() == null && actionPresenter.getSongResults() != null){
                musicService.showNotification(R.drawable.ic_baseline_pause_24,actionPresenter.getSongResults().get(getCurrentPos));

            }else if(actionPresenter.getPlayList() != null && actionPresenter.getSongResults() == null){
                musicService.showNotification(R.drawable.ic_baseline_pause_24,actionPresenter.getPlayList().get(getCurrentPos));

            }
        }
    }

    @Override
    public void onBackPressed() {
        if(uploadFragment.isVisible()){
            replaceFrag(homeFragment);
            btHomeNavigation.setItemActiveIndex(0);
        }else if(homeFragment.isVisible() && playerLayout.getVisibility() == View.GONE){
            finish();
            System.exit(0);
        }else if(playerLayout.getVisibility() == View.VISIBLE ){
            hidePlayerLayout(playerLayout);
        }else if(topMVFragment.isVisible()){
            replaceFrag(homeFragment);
            btHomeNavigation.setItemActiveIndex(0);
        }
        else if(searchFrag.isVisible()){
            edtSearch.setQuery("",false);
            replaceFrag(homeFragment);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = new Intent(this,MusicService.class);
        bindService(intent,this,BIND_AUTO_CREATE);
        hideMinimizePlayerLayout();

    }


    public RelativeLayout getPlayerLayout() {
        return playerLayout;
    }

    @Override
    public void setDataOnView(SongDB giveSong){
//        CompletableFuture<String> getLyricsFuture = CompletableFuture.supplyAsync(() ->
//                findLyrics(giveSong),executorService);
//        try {
//            txtLyrics.setText(getLyricsFuture.get());
////            executorService.shutdown();
//        } catch (ExecutionException | InterruptedException e) {
//            e.printStackTrace();
//        }

        executorService.execute(() -> {
            try{
                String findLyricsResult = findLyrics(giveSong);
                handler.post( () ->{
                    txtLyrics.setText(findLyricsResult);
                });
            }catch (Exception e){
                e.printStackTrace();
                handler.post( () -> {
                    txtLyrics.setText("Can't find lyrics");
                });
            }
        });

        txtSongPlaying.setText(giveSong.getSongName());
        txtAlbumPlaying.setText(giveSong.getAlbumName());
        txtSingerSongPlaying.setText(giveSong.getSingerName());
        Glide.with(getApplicationContext()).load(giveSong.getImageSongLink()).into(imgAlbumPlayingImage);
        getCurrentPos = actionPresenter.getPlayList().indexOf(giveSong);
        minimizeFragment.setMinimizeData(giveSong);
        musicService.showNotification(R.drawable.ic_baseline_pause_24,actionPresenter.getPlayList().get(getCurrentPos));
    }


    @Override
    public void setDataOnView(Song giveSong){
        txtSongPlaying.setText(giveSong.getName());
        txtAlbumPlaying.setText("Zing MP3");
        txtSingerSongPlaying.setText(giveSong.getArtist());
        Glide.with(getApplicationContext()).load(createImageURl+giveSong.getThumb()).into(imgAlbumPlayingImage);
        getCurrentPos = actionPresenter.getSongResults().indexOf(giveSong);
        minimizeFragment.setMinimizeData(giveSong);
        getLyrics(giveSong);
        musicService.showNotification(R.drawable.ic_baseline_pause_24,actionPresenter.getSongResults().get(getCurrentPos));
    }

    public void getLyrics(Song song){
        Call<GetLyrics> getLyrics = getSongAPI.getLyrics(song.getId());
        getLyrics.enqueue(new Callback<GetLyrics>() {
            @Override
            public void onResponse(Call<GetLyrics> call, Response<GetLyrics> response) {
                if(response.code() == 200){
                    GetLyrics getLyrics = response.body();
                    StringBuilder allLyrics = new StringBuilder();
                    if(getLyrics.getErr() == 0){
                        LyricsData data = getLyrics.getData();
                        if(data.getSentences()!=null) {
                            for (Sentence cS : data.getSentences()) {
                                for (Word cW : cS.getWords()) {
                                    allLyrics.append(cW.getData()).append(" ");
                                }
                                allLyrics.append("\n");
                            }
                            txtLyrics.setText(allLyrics.toString());
                        }else{
                            txtLyrics.setText("No Lyrics");
                        }
                    }else{
                        Toast.makeText(HomeActivity.this, getLyrics.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<GetLyrics> call, Throwable t) {

            }
        });

    }

    @Override
    public void playSongWhenClickSongItem(SongDB song){
        putDataPreference(song.getSongName(),song.getSingerName(),song.getSongLink(),song.getImageSongLink());
        mediaPlayer.setAudioAttributes(new AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build());
        mediaPlayer.reset();
        try {

            mediaPlayer.setDataSource(song.getSongLink());
//            Glide.with(this).load(getSongImageLink).into(imgAlbumPlayingImage);
            mediaPlayer.prepareAsync();
            if(playerLayout.getVisibility() == View.VISIBLE) {
                loadingDialog.show();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void playSongWhenClickSongItem(String name,String artist,String thumb,String link){
        putDataPreference(name,artist,link,thumb);
        mediaPlayer.setAudioAttributes(new AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build());
        mediaPlayer.reset();

        try {

            mediaPlayer.setDataSource(link);
            mediaPlayer.prepareAsync();
            if(playerLayout.getVisibility() == View.VISIBLE) {
                loadingDialog.show();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void upDateDuration(){
        playProgress.setProgress(0);
        playProgress.setMax(mediaPlayer.getDuration());
        //start animation
        rotateAnimation.start();

        Handler handler  = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                txtTimeSongPlaying.setText(format.format(mediaPlayer.getCurrentPosition()));
                setVolumeSeekBar(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
                setPlayProgress(mediaPlayer.getCurrentPosition());
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        try {

                            //stop animation

                            imgBtPlay_Pause.setImageResource(R.drawable.ic_round_play_arrow_48);
                            minimizeFragment.isPlaying(false);

                            if(actionPresenter.getPlayList() == null && actionPresenter.getSongResults() != null){
                                musicService.showNotification(R.drawable.ic_baseline_play_arrow_24,actionPresenter.getSongResults().get(getCurrentPos));

                            }else if(actionPresenter.getPlayList() != null && actionPresenter.getSongResults() == null){
                                musicService.showNotification(R.drawable.ic_baseline_play_arrow_24,actionPresenter.getPlayList().get(getCurrentPos));

                            }

//                            mediaPlayer.reset();
                            if(actionPresenter.getPlayList()!=null ) {
                                rotateAnimation.stop();
                                getCurrentPos = actionPresenter.nextAction(getCurrentPos);
                            }

                        }catch (Exception e){
                            Toast.makeText(HomeActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            Log.d("ERROR UPDATE",e.getMessage());
                            e.printStackTrace();
                        }
                    }
                });

                handler.postDelayed(this,100);
            }
        },100);
    }

    private void setPlayProgress(int progress){
        playProgress.setProgress(progress);
    }
    private void setVolumeSeekBar(int volume){
        skVolume.setProgress(volume);
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        MusicService.MyBinder binder = (MusicService.MyBinder) iBinder;
        musicService = binder.getService();
        musicService.setCallback(actionPresenter);
        musicService.setCallback(mediaPlayer);
        musicService.setContext(this);
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        musicService =null;
    }

    public MediaPlayer getMediaPlayer(){
        return mediaPlayer;
    }
    public void setGetCurrentPos(int newCurrentPos){
        this.getCurrentPos = newCurrentPos;
    }


    private void putDataPreference(String songName, String singerName, String songLink, String songImageLink){
        SharedPreferences.Editor editor = this.getSharedPreferences(SONG_LAST_PLAYED,MODE_PRIVATE).edit();

        if(singerName.length()>=20){
            editor.putString(SINGER_NAME,singerName.substring(0,15)+"...");
        }else{
            editor.putString(SINGER_NAME,singerName);
        }

        if(songName.length() >=20){

            editor.putString(SONG_NAME,songName.substring(0,15)+"...");
        }else{
            editor.putString(SONG_NAME,songName);
        }
        editor.putString(SONG_LINK,songLink);
        editor.putString(SONG_IMAGE_LINK,songImageLink);
        editor.apply();

    }

    public ActionPresenter getActionPresenter() {
        return actionPresenter;
    }


    public MusicService getMusicService() {
        return musicService;
    }

    public IFindSongAPI getFindSongAPI() {
        return findSongAPI;
    }

    public IGetSongAPI getGetSongAPI() {
        return getSongAPI;
    }

    public TextView getTxtLyrics() {
        return txtLyrics;
    }

    public CustomProgressDialog getLoadingDialog() {
        return loadingDialog;
    }

    public boolean isShow_minimize() {
        return show_minimize;
    }

    public void setIsShow_minimize(boolean isShow_minimize){
        this.isShow_minimize = isShow_minimize;
    }

    private String findLyrics(SongDB songDB) {
        String trackName = songDB.getSongName();
        String artistName = songDB.getSingerName();

        try {
            Track track = musixMatch.getMatchingTrack(trackName, artistName);
            TrackData data = track.getTrack();
            int trackID = data.getTrackId();
            try {
                Lyrics lyrics = musixMatch.getLyrics(trackID);
                String getLyrics = lyrics.getLyricsBody();

                if(getLyrics!=null) {
                    return getLyrics;
                }else{
                    return "No Lyrics";
                }
            }catch (Exception e){
                return "Can't get lyrics";
            }

        } catch (MusixMatchException e) {
            e.printStackTrace();
            return "Can't get lyrics";
        }

    }


    @Override
    public void getMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

}