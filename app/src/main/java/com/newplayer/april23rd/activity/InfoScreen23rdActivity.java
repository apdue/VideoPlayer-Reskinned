package com.newplayer.april23rd.activity;

import static com.newplayer.april23rd.AdsManger.Splash23rdActivity.TAG_NATIVEIDD;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.newplayer.april23rd.AdsManger.AdController23rd;
import com.newplayer.april23rd.AdsManger.MainApplication23rd;
import com.newplayer.april23rd.AdsManger.PrefManagerVideo23rd;
import com.newplayer.april23rd.AdsManger.Splash23rdActivity;
import com.newplayer.april23rd.R;
import com.newplayer.april23rd.adapter.CustomPager23rdAdapter;
import com.newplayer.april23rd.dummy.Fifth23rdActivity;
import com.newplayer.april23rd.dummy.First23rdActivity;
import com.newplayer.april23rd.dummy.Fourth23rdActivity;
import com.newplayer.april23rd.dummy.Second23rdActivity;
import com.newplayer.april23rd.dummy.Sixth23rdActivity;
import com.newplayer.april23rd.dummy.Third23rdActivity;
import com.newplayer.april23rd.fragment.Info123rdFragment;
import com.newplayer.april23rd.fragment.Info223rdFragment;
import com.newplayer.april23rd.fragment.Info323rdFragment;
import com.newplayer.april23rd.fragment.Info423rdFragment;
import com.newplayer.april23rd.fragment.Info523rdFragment;
import com.newplayer.april23rd.fragment.Info623rdFragment;

import java.util.ArrayList;
import java.util.List;

public class InfoScreen23rdActivity extends AppCompatActivity {

    private LinearLayout dotLayout;
    private int dotCount = MainApplication23rd.CountTips;
    TextView tvNext;
    FrameLayout flSmallSizeNative;
    Context context;
    PrefManagerVideo23rd prefManagerVideo22nd;
    AdController23rd adController23rd;
    boolean shouldExecuteOnResume;
    ViewPager2 viewPager;
    private int currentFragmentIndex = 0;
    List<Fragment> fragmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_screen_23rd);

        initViews();
        clickViews();
        loadShowAds();

        fragmentList = new ArrayList<>();

        switch (MainApplication23rd.CountTips) {
            case 1:
                fragmentList.add(new Info123rdFragment());
                break;
            case 2:
                fragmentList.add(new Info123rdFragment());
                fragmentList.add(new Info223rdFragment());
                break;
            case 3:
                fragmentList.add(new Info123rdFragment());
                fragmentList.add(new Info223rdFragment());
                fragmentList.add(new Info323rdFragment());
                break;
            case 4:
                fragmentList.add(new Info123rdFragment());
                fragmentList.add(new Info223rdFragment());
                fragmentList.add(new Info323rdFragment());
                fragmentList.add(new Info423rdFragment());
                break;
            case 5:
                fragmentList.add(new Info123rdFragment());
                fragmentList.add(new Info223rdFragment());
                fragmentList.add(new Info323rdFragment());
                fragmentList.add(new Info423rdFragment());
                fragmentList.add(new Info523rdFragment());
                break;
            case 6:
                fragmentList.add(new Info123rdFragment());
                fragmentList.add(new Info223rdFragment());
                fragmentList.add(new Info323rdFragment());
                fragmentList.add(new Info423rdFragment());
                fragmentList.add(new Info523rdFragment());
                fragmentList.add(new Info623rdFragment());
                break;
            default:
                break;
        }

        CustomPager23rdAdapter adapter = new CustomPager23rdAdapter(this, fragmentList);
        viewPager.setAdapter(adapter);

        dotCount = fragmentList.size();
        dotLayout.post(() -> setupDots(0));

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setupDots(position);
                currentFragmentIndex = position;
            }
        });
    }

    private void loadShowAds() {
        adController23rd.newreleasenativePreloadExtra(this, prefManagerVideo22nd.getString(TAG_NATIVEIDD), 2, flSmallSizeNative, true);
    }

    private void clickViews() {
        tvNext.setOnClickListener(v -> {
            if (currentFragmentIndex < fragmentList.size() - 1) {
                currentFragmentIndex++;
                adController23rd.showInterAdCallBack(InfoScreen23rdActivity.this, new AdController23rd.MyCallback() {
                    @Override
                    public void callbackCall() {
                        viewPager.setCurrentItem(currentFragmentIndex, true);
                    }
                });
            } else {
                adController23rd.showInterAdCallBack(InfoScreen23rdActivity.this, new AdController23rd.MyCallback() {
                    @Override
                    public void callbackCall() {
                        Intent intent;

                        if (new PrefManagerVideo23rd(InfoScreen23rdActivity.this).getString(Splash23rdActivity.status_dummy_one_enabled_fifteen).contains("true")) {
                            intent = new Intent(InfoScreen23rdActivity.this, First23rdActivity.class);
                        } else if (new PrefManagerVideo23rd(InfoScreen23rdActivity.this).getString(Splash23rdActivity.status_dummy_two_enabled).contains("true")) {
                            intent = new Intent(InfoScreen23rdActivity.this, Second23rdActivity.class);
                        } else if (new PrefManagerVideo23rd(InfoScreen23rdActivity.this).getString(Splash23rdActivity.status_dummy_three_enabled).contains("true")) {
                            intent = new Intent(InfoScreen23rdActivity.this, Third23rdActivity.class);
                        } else if (new PrefManagerVideo23rd(InfoScreen23rdActivity.this).getString(Splash23rdActivity.status_dummy_four_enabled).contains("true")) {
                            intent = new Intent(InfoScreen23rdActivity.this, Fourth23rdActivity.class);
                        } else if (new PrefManagerVideo23rd(InfoScreen23rdActivity.this).getString(Splash23rdActivity.status_dummy_five_enabled).contains("true") && !new PrefManagerVideo23rd(InfoScreen23rdActivity.this).getString(Splash23rdActivity.TAG_NATIVEID).contains("sandeep")) {
                            intent = new Intent(InfoScreen23rdActivity.this, Fifth23rdActivity.class);
                        } else if (new PrefManagerVideo23rd(InfoScreen23rdActivity.this).getString(Splash23rdActivity.status_dummy_six_enabled).contains("true")) {
                            intent = new Intent(InfoScreen23rdActivity.this, Sixth23rdActivity.class);
                        } else {
                            intent = new Intent(InfoScreen23rdActivity.this, Main23rdActivity.class);
                        }
                        
                        startActivity(intent);
                    }
                });
            }
        });
    }

    public void initViews() {
        context = getApplicationContext();
        prefManagerVideo22nd = new PrefManagerVideo23rd(context);
        adController23rd = AdController23rd.getInstance();
        flSmallSizeNative = findViewById(R.id.flSmallSizeNative);

        tvNext = findViewById(R.id.tvNext);
        viewPager = findViewById(R.id.viewPager);
        dotLayout = findViewById(R.id.dotLayout);
    }

    private void setupDots(int currentPosition) {
        dotLayout.removeAllViews();

        for (int i = 0; i < dotCount; i++) {
            ImageView dot = new ImageView(this);
            if (i == currentPosition) {
                dot.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.dot_active_23rd));
            } else {
                dot.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.dot_inactive_23rd));
            }

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(8, 0, 8, 0);
            dot.setLayoutParams(params);
            dotLayout.addView(dot);
        }

        boolean isLastDot = currentPosition == dotCount - 1;
        if (isLastDot) {
            tvNext.setText(getString(R.string.get_started));
        } else {
            tvNext.setText(getString(R.string.next));
        }
    }

    public void loadAdNextTime() {
        if (shouldExecuteOnResume) {
            if (prefManagerVideo22nd.getString("extrascreennative").contains("1")) {
                if (flSmallSizeNative != null) {
                    flSmallSizeNative.removeAllViews();
                }
                if ((flSmallSizeNative != null) && (flSmallSizeNative.getChildCount() == 0)) {
                    adController23rd.newreleasenativePreloadExtra(this, prefManagerVideo22nd.getString(TAG_NATIVEIDD), 2, flSmallSizeNative, true);
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


    public void bckpressed() {
        super.onBackPressed();
    }

    @Override
    public void onBackPressed() {
        adController23rd.showInterAdCallBack(InfoScreen23rdActivity.this, new AdController23rd.MyCallback() {
            @Override
            public void callbackCall() {
                if (currentFragmentIndex > 0) {
                    currentFragmentIndex--;  // Move to the previous fragment
                    viewPager.setCurrentItem(currentFragmentIndex, true);
                } else {
                    bckpressed();
                }
            }
        });
    }
}