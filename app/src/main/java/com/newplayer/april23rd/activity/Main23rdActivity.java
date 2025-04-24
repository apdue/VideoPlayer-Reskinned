package com.newplayer.april23rd.activity;

import static com.newplayer.april23rd.AdsManger.Splash23rdActivity.TAG_NATIVEID;
import static com.newplayer.april23rd.AdsManger.Splash23rdActivity.TAG_NATIVEIDD;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.newplayer.april23rd.AdsManger.AdController23rd;
import com.newplayer.april23rd.AdsManger.PrefManagerVideo23rd;
import com.newplayer.april23rd.AdsManger.Splash23rdActivity;
import com.newplayer.april23rd.R;
import com.newplayer.april23rd.dummy.First23rdActivity;
import com.newplayer.april23rd.dummy.Fourth23rdActivity;
import com.newplayer.april23rd.dummy.Second23rdActivity;
import com.newplayer.april23rd.dummy.Sixth23rdActivity;
import com.newplayer.april23rd.dummy.Third23rdActivity;
import com.newplayer.april23rd.utils.AdsManager23rd;
import com.newplayer.april23rd.utils.StoragePermission23rd;

public class Main23rdActivity extends AppCompatActivity {

    RelativeLayout tvClickFolderView, tvClickTrendView;
    RelativeLayout llVideoView, llMusicView, llSettingsView, llDownloadView;
    FrameLayout flBigSizeNative, flSmallSizeNative;
    private Context context;
    private PrefManagerVideo23rd prefManagerVideo22nd;
    private AdController23rd adController23rd;
    boolean shouldExecuteOnResume;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_23rd);

        initViews();
        clickViews();
        loadShowAds();
    }

    public void initViews() {
        context = getApplicationContext();
        prefManagerVideo22nd = new PrefManagerVideo23rd(context);
        adController23rd = AdController23rd.getInstance();
        flBigSizeNative = findViewById(R.id.flBigSizeNative);
        flSmallSizeNative = findViewById(R.id.flSmallSizeNative);

        tvClickFolderView = findViewById(R.id.rlFolderView);
        llVideoView = findViewById(R.id.llVideoView);
        llMusicView = findViewById(R.id.llMusicView);
        llSettingsView = findViewById(R.id.llSettingsView);
        tvClickTrendView = findViewById(R.id.rlTrending);
        llDownloadView = findViewById(R.id.llDownloadView);
    }

    public void clickViews() {
        tvClickFolderView.setOnClickListener(v -> {
            if (!StoragePermission23rd.isAllStoragePermissionsGranted(Main23rdActivity.this)) {
                StoragePermission23rd.requestAllStoragePermissions(Main23rdActivity.this, StoragePermission23rd.perRequest);
            } else {
                adController23rd.showInterAdCallBack(Main23rdActivity.this, () -> {
                    startActivity(new Intent(Main23rdActivity.this, Folders23rdActivity.class));
                });
            }
        });
        llVideoView.setOnClickListener(v -> {
            if (!StoragePermission23rd.isAllStoragePermissionsGranted(Main23rdActivity.this)) {
                StoragePermission23rd.requestAllStoragePermissions(Main23rdActivity.this, StoragePermission23rd.perRequest);
            } else {
                adController23rd.showInterAdCallBack(Main23rdActivity.this, () -> {
                    startActivity(new Intent(Main23rdActivity.this, Videos23rdActivity.class));
                });
            }
        });
        llMusicView.setOnClickListener(v -> {
            if (!StoragePermission23rd.isAllStoragePermissionsGranted(Main23rdActivity.this)) {
                StoragePermission23rd.requestAllStoragePermissions(Main23rdActivity.this, StoragePermission23rd.perRequest);
            } else {
                adController23rd.showInterAdCallBack(Main23rdActivity.this, () -> {
                    startActivity(new Intent(Main23rdActivity.this, Musics23rdActivity.class));
                });
            }
        });
        llSettingsView.setOnClickListener(v -> {
            if (!StoragePermission23rd.isAllStoragePermissionsGranted(Main23rdActivity.this)) {
                StoragePermission23rd.requestAllStoragePermissions(Main23rdActivity.this, StoragePermission23rd.perRequest);
            } else {
                adController23rd.showInterAdCallBack(Main23rdActivity.this, () -> {
                    startActivity(new Intent(Main23rdActivity.this, Settings23rdActivity.class));
                });
            }
        });

        tvClickTrendView.setOnClickListener(v -> {
            if (!StoragePermission23rd.isAllStoragePermissionsGranted(Main23rdActivity.this)) {
                StoragePermission23rd.requestAllStoragePermissions(Main23rdActivity.this, StoragePermission23rd.perRequest);
            } else {
                adController23rd.showInterAdCallBack(Main23rdActivity.this, () -> {
                    startActivity(new Intent(Main23rdActivity.this, TrendingVideo23rdActivity.class));
                });
            }
        });
        llDownloadView.setOnClickListener(v -> {
            if (!StoragePermission23rd.isAllStoragePermissionsGranted(Main23rdActivity.this)) {
                StoragePermission23rd.requestAllStoragePermissions(Main23rdActivity.this, StoragePermission23rd.perRequest);
            } else {
                adController23rd.showInterAdCallBack(Main23rdActivity.this, () -> {
                    startActivity(new Intent(Main23rdActivity.this, TrendingDownload23rdActivity.class));
                });
            }
        });

    }

    public void loadShowAds() {
        adController23rd.newreleasenativePreload(this, prefManagerVideo22nd.getString(TAG_NATIVEID), 1, flBigSizeNative, true);
        adController23rd.newreleasenativePreloadExtra(this, prefManagerVideo22nd.getString(TAG_NATIVEIDD), 2, flSmallSizeNative, true);
    }

    public void loadAdNextTime() {
        if (shouldExecuteOnResume) {
            if (prefManagerVideo22nd.getString("extrascreennative").contains("1")) {
                if (flBigSizeNative != null) {
                    flBigSizeNative.removeAllViews();
                }
                if (flSmallSizeNative != null) {
                    flSmallSizeNative.removeAllViews();
                }
                if ((flBigSizeNative != null) && (flBigSizeNative.getChildCount() == 0)) {
                    adController23rd.newreleasenativePreload(Main23rdActivity.this, prefManagerVideo22nd.getString(TAG_NATIVEID), 1, flBigSizeNative, true);
                }
                if ((flSmallSizeNative != null) && (flSmallSizeNative.getChildCount() == 0)) {
                    adController23rd.newreleasenativePreloadExtra(Main23rdActivity.this, prefManagerVideo22nd.getString(TAG_NATIVEIDD), 2, flSmallSizeNative, true);
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
        Intent intent;
        if (new PrefManagerVideo23rd(this).getString(Splash23rdActivity.status_dummy_six_back_enabled).contains("true")) {
            intent = new Intent(this, Sixth23rdActivity.class);
            AdsManager23rd.showInterstitialAd(this, new AdsManager23rd.AdFinished() {
                @Override
                public void onAdFinished() {
                    startActivity(intent);
                }
            });
        } else if (new PrefManagerVideo23rd(this).getString(Splash23rdActivity.status_dummy_four_back_enabled).contains("true")) {
            intent = new Intent(this, Fourth23rdActivity.class);
            AdsManager23rd.showInterstitialAd(this, new AdsManager23rd.AdFinished() {
                @Override
                public void onAdFinished() {
                    startActivity(intent);
                }
            });
        } else if (new PrefManagerVideo23rd(this).getString(Splash23rdActivity.status_dummy_three_back_enabled).contains("true")) {
            intent = new Intent(this, Third23rdActivity.class);
            AdsManager23rd.showInterstitialAd(this, new AdsManager23rd.AdFinished() {
                @Override
                public void onAdFinished() {
                    startActivity(intent);
                }
            });
        } else if (new PrefManagerVideo23rd(this).getString(Splash23rdActivity.status_dummy_two_back_enabled).contains("true")) {
            intent = new Intent(this, Second23rdActivity.class);
            AdsManager23rd.showInterstitialAd(this, new AdsManager23rd.AdFinished() {
                @Override
                public void onAdFinished() {
                    startActivity(intent);
                }
            });
        } else if (new PrefManagerVideo23rd(this).getString(Splash23rdActivity.status_dummy_one_back_enabled).contains("true")) {
            intent = new Intent(this, First23rdActivity.class);
            AdsManager23rd.showInterstitialAd(this, new AdsManager23rd.AdFinished() {
                @Override
                public void onAdFinished() {
                    startActivity(intent);
                }
            });
        } else {
            finishAffinity();
        }
    }

    private void superOnBackPressed() {
        super.onBackPressed();
    }
}