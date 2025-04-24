package com.newplayer.april23rd.activity;

import static com.newplayer.april23rd.AdsManger.Splash23rdActivity.TAG_NATIVEID;
import static com.newplayer.april23rd.AdsManger.Splash23rdActivity.TAG_NATIVEIDD;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.newplayer.april23rd.AdsManger.AdController23rd;
import com.newplayer.april23rd.AdsManger.PrefManagerVideo23rd;
import com.newplayer.april23rd.R;

public class Settings23rdActivity extends AppCompatActivity {

    FrameLayout flBigSizeNative, flSmallSizeNative;
    private Context context;
    private PrefManagerVideo23rd prefManagerVideo22nd;
    private AdController23rd adController23rd;
    boolean shouldExecuteOnResume;
    ImageView ivBackView;
    LinearLayout llShareView, llRateView, llPolicyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_23rd);

        initViews();
        clickViews();
        loadShowAds();
    }

    private void loadShowAds() {
        adController23rd.newreleasenativePreload(this, prefManagerVideo22nd.getString(TAG_NATIVEID), 1, flBigSizeNative, true);
        adController23rd.newreleasenativePreloadExtra(this, prefManagerVideo22nd.getString(TAG_NATIVEIDD), 2, flSmallSizeNative, true);
    }

    private void clickViews() {
        ivBackView.setOnClickListener(v -> {
            onBackPressed();
        });

        llShareView.setOnClickListener(v -> {
            String appPackageName = getPackageName();
            String appName = getString(R.string.app_name);
            String shareBody = appName + "https://play.google.com/store/apps/details?id=" + appPackageName;

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "App Recommendation");
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareBody);

            startActivity(Intent.createChooser(shareIntent, "Share via"));
        });
        llRateView.setOnClickListener(v -> {
            Uri uri = Uri.parse("market://details?id=" + getPackageName());
            Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
            try {
                startActivity(myAppLinkToMarket);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(Settings23rdActivity.this, " unable to find market app", Toast.LENGTH_LONG).show();
            }
        });
        llPolicyView.setOnClickListener(v -> {
            try {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.privacypolicyapp)));
                startActivity(browserIntent);
            } catch (Exception e) {
                Toast.makeText(Settings23rdActivity.this, "No Any Web Browser Found to Open this Privacy Policy", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void initViews() {
        context = getApplicationContext();
        prefManagerVideo22nd = new PrefManagerVideo23rd(context);
        adController23rd = AdController23rd.getInstance();
        flBigSizeNative = findViewById(R.id.flBigSizeNative);
        flSmallSizeNative = findViewById(R.id.flSmallSizeNative);

        ivBackView = findViewById(R.id.ivBackView);
        llShareView = findViewById(R.id.llShareView);
        llRateView = findViewById(R.id.llRateView);
        llPolicyView = findViewById(R.id.llPolicyView);
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
                    adController23rd.newreleasenativePreload(Settings23rdActivity.this, prefManagerVideo22nd.getString(TAG_NATIVEID), 1, flBigSizeNative, true);
                }
                if ((flSmallSizeNative != null) && (flSmallSizeNative.getChildCount() == 0)) {
                    adController23rd.newreleasenativePreloadExtra(Settings23rdActivity.this, prefManagerVideo22nd.getString(TAG_NATIVEIDD), 2, flSmallSizeNative, true);
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
        adController23rd.showInterAdCallBack(Settings23rdActivity.this, this::superOnBackPressed);
    }

    private void superOnBackPressed() {
        super.onBackPressed();
    }
}