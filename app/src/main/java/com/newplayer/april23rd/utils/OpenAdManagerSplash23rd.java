package com.newplayer.april23rd.utils;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleObserver;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdValue;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.OnPaidEventListener;
import com.google.android.gms.ads.appopen.AppOpenAd;
import com.newplayer.april23rd.AdsManger.PrefManagerVideo23rd;
import com.newplayer.april23rd.AdsManger.Splash23rdActivity;

import java.util.Date;

public class OpenAdManagerSplash23rd implements LifecycleObserver, Application.ActivityLifecycleCallbacks {

    private static final String LOG_TAG = "AppOpenManager";
    public static boolean isLoaded = false;
    public static AppOpenAd appOpenAd = null;
    public static AppOpenAd.AppOpenAdLoadCallback loadCallback;
    public static long loadTime = 0;
    private final Application myApplication;
    private Activity currentActivity;

    public OpenAdManagerSplash23rd(Application myApplication) {
        this.myApplication = myApplication;
        this.myApplication.registerActivityLifecycleCallbacks(this);
    }
    public static void fetchAd(Activity activity, AdsManager23rd.AdFinished adFinished) {
        Log.d("OpenOpen", "show: ");


        PrefManagerVideo23rd prf = new PrefManagerVideo23rd(activity);

        if (isLoaded){
            appOpenAd.show(activity);
            appOpenAd.setOnPaidEventListener(new OnPaidEventListener() {
                @Override
                public void onPaidEvent(@NonNull AdValue adValue) {
                    AdsManager23rd.logAppOpenAdImpression(activity, adValue, appOpenAd);
                }
            });
            appOpenAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdImpression() {
                    super.onAdImpression();
                    AdsManager23rd.logAppOpenAdImpressionOnly(activity, prf.getString(Splash23rdActivity.TAG_OPENAPPID));
                }

                @Override
                public void onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent();
                    isLoaded = false;
                    fetchAd(activity);
                    adFinished.onAdFinished();
                }
            });
        } else {
            fetchAd(activity);
            adFinished.onAdFinished();
        }
    }


    public static void fetchAd(Activity activity) {
        loadCallback =
                new AppOpenAd.AppOpenAdLoadCallback() {

                    @Override
                    public void onAdLoaded(AppOpenAd ad) {
                        Log.d("OpenOpen", "onAdLoaded: ");
                        appOpenAd = ad;
                        isLoaded = true;
                        loadTime = (new Date()).getTime();
                    }

                    @Override
                    public void onAdFailedToLoad(LoadAdError loadAdError) {
                        // Handle the error.
                        appOpenAd = null;
                        isLoaded = false;
                        Log.d("OpenOpen", "loadAdError: "+loadAdError.toString());
                    }

                };
        if (Splash23rdActivity.isConnectedToInternet(activity)
                && !new PrefManagerVideo23rd(activity).getString(Splash23rdActivity.TAG_OPENAPPID).contains("sandeep")) {
            AdRequest request = getAdRequest();
            AppOpenAd.load(
                    activity, new PrefManagerVideo23rd(activity).getString(Splash23rdActivity.TAG_OPENAPPID), request,
                    AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT, loadCallback);
        }

    }

    public static AdRequest getAdRequest() {
        return new AdRequest.Builder().build();
    }

    /**
     * Utility method to check if ad was loaded more than n hours ago.
     */
    public static boolean wasLoadTimeLessThanNHoursAgo(long numHours) {
        long dateDifference = (new Date()).getTime() - loadTime;
        long numMilliSecondsPerHour = 3600000;
        return (dateDifference < (numMilliSecondsPerHour * numHours));
    }

    /**
     * Utility method that checks if ad exists and can be shown.
     */
    public static boolean isAdAvailable() {
        return appOpenAd != null && wasLoadTimeLessThanNHoursAgo(4);
    }

    /**
     * ActivityLifecycleCallback methods
     */
    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
    }

    @Override
    public void onActivityStarted(Activity activity) {
        currentActivity = activity;
    }

    @Override
    public void onActivityResumed(Activity activity) {
        currentActivity = activity;
    }

    @Override
    public void onActivityStopped(Activity activity) {
    }

    @Override
    public void onActivityPaused(Activity activity) {
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        currentActivity = null;
    }

}

