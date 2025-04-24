package com.newplayer.april23rd.activity;

import static com.newplayer.april23rd.AdsManger.Splash23rdActivity.TAG_NATIVEIDD;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.newplayer.april23rd.AdsManger.AdController23rd;
import com.newplayer.april23rd.AdsManger.PrefManagerVideo23rd;
import com.newplayer.april23rd.R;
import com.newplayer.april23rd.adapter.Music23rdAdapter;
import com.newplayer.april23rd.adapter.Videos23rdAdapter;
import com.newplayer.april23rd.model.MusicFile23rd;
import com.newplayer.april23rd.utils.FetchStorageVideo23rd;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Musics23rdActivity extends AppCompatActivity {

    FrameLayout flSmallSizeNative;
    Context context;
    PrefManagerVideo23rd prefManagerVideo22nd;
    AdController23rd adController23rd;
    boolean shouldExecuteOnResume;
    ImageView ivBack, ivGrid;
    ArrayList<MusicFile23rd> musicFile23rdArrayList;
    Music23rdAdapter musicAdapter;
    RecyclerView rvAudioList;
    boolean isGrid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_musics_23rd);

        initViews();
        clickViews();
        loadShowAds();
        loadData();
    }

    public void initViews() {
        context = getApplicationContext();
        prefManagerVideo22nd = new PrefManagerVideo23rd(context);
        adController23rd = AdController23rd.getInstance();
        flSmallSizeNative = findViewById(R.id.flSmallSizeNative);

        ivBack = findViewById(R.id.ivBack);
        ivGrid = findViewById(R.id.ivGrid);
        rvAudioList = findViewById(R.id.rvAudioList);
    }

    public void clickViews() {
        musicFile23rdArrayList = new ArrayList<>();
        musicAdapter = new Music23rdAdapter(Musics23rdActivity.this, musicFile23rdArrayList);
        rvAudioList.setLayoutManager(new LinearLayoutManager(Musics23rdActivity.this, LinearLayoutManager.VERTICAL, false));
        rvAudioList.setAdapter(musicAdapter);

        ivBack.setOnClickListener(v -> {
            onBackPressed();
        });


        ivGrid.setOnClickListener(v -> {
            isGrid = !isGrid;
            musicAdapter.setLayoutType(isGrid ? Videos23rdAdapter.LAYOUT_GRID : Videos23rdAdapter.LAYOUT_VERTICAL);
            if (isGrid) {
                ivGrid.setImageDrawable(ContextCompat.getDrawable(Musics23rdActivity.this, R.drawable.ic_list_23rd));
            } else {
                ivGrid.setImageDrawable(ContextCompat.getDrawable(Musics23rdActivity.this, R.drawable.ic_grid_23rd));
            }
            setLayoutManager();
        });

    }

    private void setLayoutManager() {
        if (isGrid) {
            int numberOfColumns = 2;
            rvAudioList.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
        } else {
            rvAudioList.setLayoutManager(new LinearLayoutManager(this));
        }
    }

    public void loadShowAds() {
        adController23rd.newreleasenativePreloadExtra(this, prefManagerVideo22nd.getString(TAG_NATIVEIDD), 2, flSmallSizeNative, true);
    }

    public void loadAdNextTime() {
        if (shouldExecuteOnResume) {
            if (prefManagerVideo22nd.getString("extrascreennative").contains("1")) {
                if (flSmallSizeNative != null) {
                    flSmallSizeNative.removeAllViews();
                }
                if ((flSmallSizeNative != null) && (flSmallSizeNative.getChildCount() == 0)) {
                    adController23rd.newreleasenativePreloadExtra(Musics23rdActivity.this, prefManagerVideo22nd.getString(TAG_NATIVEIDD), 2, flSmallSizeNative, true);
                }
            }
            shouldExecuteOnResume = true;
        } else {
//            shouldExecuteOnResume = true;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAdNextTime();
    }

    @Override
    public void onBackPressed() {
        adController23rd.showInterAdCallBack(Musics23rdActivity.this, this::superOnBackPressed);
    }

    private void superOnBackPressed() {
        super.onBackPressed();
    }

    public void loadData() {
        FetchStorageVideo23rd.showLoader(Musics23rdActivity.this);
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                ArrayList<MusicFile23rd> videoFolders = FetchStorageVideo23rd.getGalleryMusicFiles(getContentResolver());

                runOnUiThread(new Runnable() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void run() {
                        FetchStorageVideo23rd.dismissLoader();
                        musicFile23rdArrayList.addAll(videoFolders);
                        musicAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Videos23rdAdapter.DELETE_REQUEST_CODE) {
            if (resultCode != 0) {
                musicAdapter.notifyItemRemoved(musicAdapter.getPos());
                musicAdapter.notifyDataSetChanged();
                Toast.makeText(Musics23rdActivity.this, "Video Deleted successfully", Toast.LENGTH_SHORT).show();
            }
        }
    }
}