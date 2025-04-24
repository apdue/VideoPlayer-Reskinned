package com.newplayer.april23rd.activity;

import static com.newplayer.april23rd.AdsManger.Splash23rdActivity.TAG_NATIVEIDD;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.newplayer.april23rd.AdsManger.AdController23rd;
import com.newplayer.april23rd.AdsManger.PrefManagerVideo23rd;
import com.newplayer.april23rd.R;
import com.newplayer.april23rd.adapter.DownloadVideo23rdAdapter;
import com.newplayer.april23rd.model.GalleryVideoInformation23rd;
import com.newplayer.april23rd.utils.FetchStorageVideo23rd;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TrendingDownload23rdActivity extends AppCompatActivity {

    FrameLayout flSmallSizeNative;
    Context context;
    PrefManagerVideo23rd prefManagerVideo22nd;
    AdController23rd adController23rd;
    boolean shouldExecuteOnResume;
    RecyclerView rvDownloadList;
    ArrayList<GalleryVideoInformation23rd> informationArrayList;
    DownloadVideo23rdAdapter downloadVideoAdapter;
    ImageView ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trending_download_23rd);

        initViews();
        clickViews();
        loadShowAds();
        loadData();
    }

    private void initViews() {
        context = getApplicationContext();
        prefManagerVideo22nd = new PrefManagerVideo23rd(context);
        adController23rd = AdController23rd.getInstance();
        flSmallSizeNative = findViewById(R.id.flSmallSizeNative);

        ivBack = findViewById(R.id.ivBack);
        rvDownloadList = findViewById(R.id.rvDownloadList);
    }

    private void loadShowAds() {
        adController23rd.newreleasenativePreloadExtra(this, prefManagerVideo22nd.getString(TAG_NATIVEIDD), 2, flSmallSizeNative, true);

    }

    private void clickViews() {
        informationArrayList = new ArrayList<>();
        downloadVideoAdapter = new DownloadVideo23rdAdapter(TrendingDownload23rdActivity.this, informationArrayList);
        rvDownloadList.setLayoutManager(new LinearLayoutManager(TrendingDownload23rdActivity.this, RecyclerView.VERTICAL, false));
        rvDownloadList.setAdapter(downloadVideoAdapter);

        ivBack.setOnClickListener(v -> {
            onBackPressed();
        });
    }

    private void loadData() {
        FetchStorageVideo23rd.showLoader(TrendingDownload23rdActivity.this);
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                ArrayList<GalleryVideoInformation23rd> videoInformationArrayList = FetchStorageVideo23rd.getVideoDetails(TrendingDownload23rdActivity.this);

                runOnUiThread(new Runnable() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void run() {
                        FetchStorageVideo23rd.dismissLoader();
                        informationArrayList.addAll(videoInformationArrayList);
                        downloadVideoAdapter.notifyDataSetChanged();

                        if (videoInformationArrayList.isEmpty()){
                            findViewById(R.id.noDownloads).setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
        });
    }

    public void loadAdNextTime() {
        if (shouldExecuteOnResume) {
            if (prefManagerVideo22nd.getString("extrascreennative").contains("1")) {
                if (flSmallSizeNative != null) {
                    flSmallSizeNative.removeAllViews();
                }
                if ((flSmallSizeNative != null) && (flSmallSizeNative.getChildCount() == 0)) {
                    adController23rd.newreleasenativePreloadExtra(TrendingDownload23rdActivity.this, prefManagerVideo22nd.getString(TAG_NATIVEIDD), 2, flSmallSizeNative, true);
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
        adController23rd.showInterAdCallBack(TrendingDownload23rdActivity.this, this::superOnBackPressed);
    }

    private void superOnBackPressed() {
        super.onBackPressed();
    }

    @SuppressLint("NotifyDataSetChanged")
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == DownloadVideo23rdAdapter.DELETE_REQUEST_CODE) {
            if (resultCode != 0) {
                downloadVideoAdapter.notifyItemRemoved(downloadVideoAdapter.getPos());
                downloadVideoAdapter.notifyDataSetChanged();
                Toast.makeText(TrendingDownload23rdActivity.this, "Video Deleted successfully", Toast.LENGTH_SHORT).show();
            }
        }
    }
}