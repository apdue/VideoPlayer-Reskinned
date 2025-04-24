package com.newplayer.april23rd.dummy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.newplayer.april23rd.AdsManger.PrefManagerVideo23rd;
import com.newplayer.april23rd.AdsManger.Splash23rdActivity;
import com.newplayer.april23rd.R;
import com.newplayer.april23rd.activity.Main23rdActivity;
import com.newplayer.april23rd.utils.AdsManager23rd;

public class Fourth23rdActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_23rd_screen_four);
        AdsManager23rd.showAndLoadNativeAd(this, findViewById(R.id.nativeAd), 1);
        AdsManager23rd.showAndLoadNativeAd(this, findViewById(R.id.nativeAdTwo), 2);

        onClicks();

    }

    private void onClicks() {

        findViewById(R.id.tvNext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity();
            }
        });

    }

    private void startActivity() {
        Intent intent;

        if (new PrefManagerVideo23rd(this).getString(Splash23rdActivity.status_dummy_five_enabled).contains("true") && !new PrefManagerVideo23rd(Fourth23rdActivity.this).getString(Splash23rdActivity.TAG_NATIVEID).contains("sandeep")) {
            intent = new Intent(this, Fifth23rdActivity.class);
        } else if (new PrefManagerVideo23rd(this).getString(Splash23rdActivity.status_dummy_six_enabled).contains("true")) {
            intent = new Intent(this, Sixth23rdActivity.class);
        } else {
            intent = new Intent(this, Main23rdActivity.class);
        }

        AdsManager23rd.showInterstitialAd(this, new AdsManager23rd.AdFinished() {

            @Override
            public void onAdFinished() {
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent;
        if (new PrefManagerVideo23rd(this).getString(Splash23rdActivity.status_dummy_three_back_enabled).contains("true")) {
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

}