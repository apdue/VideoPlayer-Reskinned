package com.newplayer.april23rd.dummy;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.newplayer.april23rd.AdsManger.PrefManagerVideo23rd;
import com.newplayer.april23rd.AdsManger.Splash23rdActivity;
import com.newplayer.april23rd.R;
import com.newplayer.april23rd.activity.Main23rdActivity;
import com.newplayer.april23rd.utils.AdsManager23rd;

public class Sixth23rdActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_23rd_sixth);

        AdsManager23rd.showAndLoadNativeAd(this, findViewById(R.id.nativeAd), 1);
        AdsManager23rd.loadBannerAdForNative(this, findViewById(R.id.nativeAdTwo));


        findViewById(R.id.tvPP).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(getResources().getString(R.string.privacy_policy_url)));
                startActivity(intent);
            }
        });

        findViewById(R.id.btnNext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;

                intent = new Intent(Sixth23rdActivity.this, Main23rdActivity.class);

                AdsManager23rd.showInterstitialAd(Sixth23rdActivity.this, new AdsManager23rd.AdFinished() {
                    @Override
                    public void onAdFinished() {
                        startActivity(intent);
                    }
                });
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent;
        if (new PrefManagerVideo23rd(this).getString(Splash23rdActivity.status_dummy_four_back_enabled).contains("true")) {
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
}