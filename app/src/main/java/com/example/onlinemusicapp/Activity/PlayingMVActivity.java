package com.example.onlinemusicapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.onlinemusicapp.R;

public class PlayingMVActivity extends AppCompatActivity {
    Bundle receivedDataBundle;
    private VideoView videoView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing_mvactivity);
        Window window = this.getWindow();
        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        // finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.black));

        receivedDataBundle = getIntent().getBundleExtra("data");
        String title = receivedDataBundle.getString("title","title");
        String artist = receivedDataBundle.getString("artistMV","artist");
        int view = receivedDataBundle.getInt("view",0);
        int like = receivedDataBundle.getInt("like",0);
        String url = receivedDataBundle.getString("url",null);

        videoView = findViewById(R.id.videoView);
        TextView txtView = findViewById(R.id.txtView);
        txtView.setText(view+"");
        TextView txtLike = findViewById(R.id.txtLike);
        txtLike.setText(like+"");
        TextView txtTitle = findViewById(R.id.txtMVTitlePlaying);
        txtTitle.setText(title);
        TextView txtArtist = findViewById(R.id.txtArtistMVPlaying);
        txtArtist.setText(artist);

        Uri uri = Uri.parse(url);
        videoView.setVideoURI(uri);
        MediaController controller = new MediaController(this);
        controller.setBackgroundColor(getResources().getColor(R.color.black, getTheme()));
        controller.setAnchorView(videoView);
        controller.setMediaPlayer(videoView);
        videoView.setMediaController(controller);
        videoView.start();

        findViewById(R.id.imgBtBack).setOnClickListener(v -> {videoView.pause(); finish();});
    }
}