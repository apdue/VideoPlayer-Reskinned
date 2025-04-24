package com.newplayer.april23rd.activity;

import static com.newplayer.april23rd.AdsManger.Splash23rdActivity.TAG_EXTRASCREENSHOW;
import static com.newplayer.april23rd.AdsManger.Splash23rdActivity.TAG_NATIVEID;
import static com.newplayer.april23rd.AdsManger.Splash23rdActivity.TAG_NATIVEIDD;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.newplayer.april23rd.AdsManger.AdController23rd;
import com.newplayer.april23rd.AdsManger.AppOpenManagerVD23rd;
import com.newplayer.april23rd.AdsManger.MainApplication23rd;
import com.newplayer.april23rd.AdsManger.PrefManagerVideo23rd;
import com.newplayer.april23rd.AdsManger.Splash23rdActivity;
import com.newplayer.april23rd.R;
import com.newplayer.april23rd.dummy.Fifth23rdActivity;
import com.newplayer.april23rd.dummy.First23rdActivity;
import com.newplayer.april23rd.dummy.Fourth23rdActivity;
import com.newplayer.april23rd.dummy.Second23rdActivity;
import com.newplayer.april23rd.dummy.Sixth23rdActivity;
import com.newplayer.april23rd.dummy.Third23rdActivity;

public class StartApp23rdActivity extends AppCompatActivity {

    FrameLayout flBigSizeNative, flSmallSizeNative;
    private Context context;
    private PrefManagerVideo23rd prefManagerVideo22nd;
    private AdController23rd adController23rd;
    boolean shouldExecuteOnResume;
    RelativeLayout rlStartView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_app_23rd);

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
        rlStartView = findViewById(R.id.rlStartView);
    }

    public void clickViews() {
        rlStartView.setOnClickListener(v -> {
            if (prefManagerVideo22nd.getString(TAG_EXTRASCREENSHOW).contains("1")) {
                adController23rd.showInterAdCallBack(StartApp23rdActivity.this, new AdController23rd.MyCallback() {
                    @Override
                    public void callbackCall() {
                        shouldExecuteOnResume = true;
                        startActivity(new Intent(StartApp23rdActivity.this, InfoScreen23rdActivity.class));
                    }
                });
            } else {
                adController23rd.showInterAdCallBack(StartApp23rdActivity.this, new AdController23rd.MyCallback() {
                    @Override
                    public void callbackCall() {
                        shouldExecuteOnResume = true;

                        Intent intent;

                        if (new PrefManagerVideo23rd(StartApp23rdActivity.this).getString(Splash23rdActivity.status_dummy_one_enabled_fifteen).contains("true")) {
                            intent = new Intent(StartApp23rdActivity.this, First23rdActivity.class);
                        } else if (new PrefManagerVideo23rd(StartApp23rdActivity.this).getString(Splash23rdActivity.status_dummy_two_enabled).contains("true")) {
                            intent = new Intent(StartApp23rdActivity.this, Second23rdActivity.class);
                        } else if (new PrefManagerVideo23rd(StartApp23rdActivity.this).getString(Splash23rdActivity.status_dummy_three_enabled).contains("true")) {
                            intent = new Intent(StartApp23rdActivity.this, Third23rdActivity.class);
                        } else if (new PrefManagerVideo23rd(StartApp23rdActivity.this).getString(Splash23rdActivity.status_dummy_four_enabled).contains("true")) {
                            intent = new Intent(StartApp23rdActivity.this, Fourth23rdActivity.class);
                        } else if (new PrefManagerVideo23rd(StartApp23rdActivity.this).getString(Splash23rdActivity.status_dummy_five_enabled).contains("true") && !new PrefManagerVideo23rd(StartApp23rdActivity.this).getString(Splash23rdActivity.TAG_NATIVEID).contains("sandeep")) {
                            intent = new Intent(StartApp23rdActivity.this, Fifth23rdActivity.class);
                        } else if (new PrefManagerVideo23rd(StartApp23rdActivity.this).getString(Splash23rdActivity.status_dummy_six_enabled).contains("true")) {
                            intent = new Intent(StartApp23rdActivity.this, Sixth23rdActivity.class);
                        } else {
                            intent = new Intent(StartApp23rdActivity.this, Main23rdActivity.class);
                        }

                        startActivity(intent);

                    }
                });
            }
        });

        if (prefManagerVideo22nd.getString("screenone").contains("1")) {
            MainApplication23rd.CountTips = 1;
        }
        if (prefManagerVideo22nd.getString("screentwo").contains("1")) {
            MainApplication23rd.CountTips = 2;
        }
        if (prefManagerVideo22nd.getString("screenthree").contains("1")) {
            MainApplication23rd.CountTips = 3;
        }
        if (prefManagerVideo22nd.getString("screenfour").contains("1")) {
            MainApplication23rd.CountTips = 4;
        }
        if (prefManagerVideo22nd.getString("screenfive").contains("1")) {
            MainApplication23rd.CountTips = 5;
        }
        if (prefManagerVideo22nd.getString("screensix").contains("1")) {
            MainApplication23rd.CountTips = 6;
        }
    }

    public void loadShowAds() {
        adController23rd.loadInterAd(StartApp23rdActivity.this);
        adController23rd.newreleasenativePreload(StartApp23rdActivity.this, prefManagerVideo22nd.getString(TAG_NATIVEID), 1, flBigSizeNative, true);
        adController23rd.newreleasenativePreloadExtra(StartApp23rdActivity.this, prefManagerVideo22nd.getString(TAG_NATIVEIDD), 2, flSmallSizeNative, true);
        shouldExecuteOnResume = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAdNextTime();
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
                    adController23rd.newreleasenativePreload(StartApp23rdActivity.this, prefManagerVideo22nd.getString(TAG_NATIVEID), 1, flBigSizeNative, true);
                }
                if ((flSmallSizeNative != null) && (flSmallSizeNative.getChildCount() == 0)) {
                    adController23rd.newreleasenativePreloadExtra(StartApp23rdActivity.this, prefManagerVideo22nd.getString(TAG_NATIVEIDD), 2, flSmallSizeNative, true);
                }
            }
            shouldExecuteOnResume = true;
        }
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
        AppOpenManagerVD23rd.appOpenAd = null;
    }
}