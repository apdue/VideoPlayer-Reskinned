package com.newplayer.april23rd.activity;


import static com.newplayer.april23rd.AdsManger.Splash23rdActivity.TAG_NATIVEID;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.newplayer.april23rd.AdsManger.AdController23rd;
import com.newplayer.april23rd.AdsManger.PrefManagerVideo23rd;
import com.newplayer.april23rd.R;
import com.newplayer.april23rd.adapter.TrendingVideo23rdAdapter;
import com.newplayer.april23rd.fragment.TrendingVideo23rdFragment;
import com.newplayer.april23rd.model.TrendVideoData23rd;
import com.newplayer.april23rd.utils.FetchStorageVideo23rd;
import com.newplayer.april23rd.utils.VerticalViewPager23rd;

import java.util.ArrayList;

public class TrendVideoPlayer23rdActivity extends AppCompatActivity {

    private VerticalViewPager23rd viewPager;
    ArrayList<TrendVideoData23rd> subVideoArrayList = new ArrayList<>();
    int receivedValue;
    AdController23rd adController23rdGold;
    private FrameLayout nativeSmallFLayout;
    private PrefManagerVideo23rd prf;
    private Context context;
    boolean shouldExecuteOnResume;
    int nativeCounter = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trend_video_player_23rd);
        Intent intent = getIntent();
        if (intent != null) {
            subVideoArrayList = intent.getParcelableArrayListExtra(FetchStorageVideo23rd.DATA_LIST);
            receivedValue = getIntent().getIntExtra(FetchStorageVideo23rd.SELECTED_VIDEO_POSITION, 0);
        }
        context = TrendVideoPlayer23rdActivity.this;
        prf = new PrefManagerVideo23rd(context);
        adController23rdGold = AdController23rd.getInstance();
        nativeSmallFLayout = findViewById(R.id.nativeSmallFLayout);
        adController23rdGold.newreleasenativePreload(TrendVideoPlayer23rdActivity.this, prf.getString(TAG_NATIVEID), 2, nativeSmallFLayout, true);
        shouldExecuteOnResume = false;


        viewPager = findViewById(R.id.viewpager);
        TrendingVideo23rdAdapter adapter = new TrendingVideo23rdAdapter(getSupportFragmentManager(), subVideoArrayList);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(1);
        viewPager.setCurrentItem(receivedValue);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                nativeCounter++;
                if (nativeCounter == 4) {
                    if (adController23rdGold != null) {
                        nativeCounter = 1;
                        adController23rdGold.newreleasenativePreload(TrendVideoPlayer23rdActivity.this, prf.getString(TAG_NATIVEID), 2, nativeSmallFLayout, true);
                    } else {
                        Log.e("TAG", "adControllerGold is null");
                    }
                }

                if (position == 0 && (adapter != null && adapter.getCount() > 0)) {
                    TrendingVideo23rdFragment fragment = (TrendingVideo23rdFragment) adapter.getItem(viewPager.getCurrentItem());
                    fragment.setData();
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            fragment.setPlayer(true);
                        }
                    }, 200);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });


    }

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
    }

    public void bckpressed() {
        super.onBackPressed();
    }

    @Override
    public void onBackPressed() {
        adController23rdGold.showInterAdCallBack(TrendVideoPlayer23rdActivity.this, new AdController23rd.MyCallback() {
            @Override
            public void callbackCall() {
                bckpressed();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        if (shouldExecuteOnResume) {
            if ((nativeSmallFLayout != null) && (nativeSmallFLayout.getChildCount() == 0)) {
                adController23rdGold.newreleasenativePreload(TrendVideoPlayer23rdActivity.this, prf.getString(TAG_NATIVEID), 2, nativeSmallFLayout, true);
            }
        } else {
            shouldExecuteOnResume = true;
        }
    }
}