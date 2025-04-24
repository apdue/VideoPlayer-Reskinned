package com.newplayer.april23rd.activity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.media3.common.MediaItem;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;

import com.newplayer.april23rd.R;

public class PlayMusic23rdActivity extends AppCompatActivity {

    private PlayerView ppPlayerView;
    String videoPath;
    ImageView ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_music_23rd);

        videoPath = getIntent().getStringExtra("videoPath");
        ppPlayerView = findViewById(R.id.ppPlayerView);
        ivBack = findViewById(R.id.ivBack);
        ivBack.setOnClickListener(v -> onBackPressed());

        androidx.media3.exoplayer.ExoPlayer player = new ExoPlayer.Builder(this).build();
        ppPlayerView.setPlayer(player);

        Uri uri = Uri.parse(videoPath);
        androidx.media3.common.MediaItem mediaItem = MediaItem.fromUri(uri);
        player.setMediaItem(mediaItem);
        player.prepare();
        player.play();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (ppPlayerView != null && ppPlayerView.getPlayer() != null) {
            ppPlayerView.getPlayer().release();
        }
    }
}