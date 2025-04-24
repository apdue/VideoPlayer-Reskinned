package com.newplayer.april23rd.AdsManger;

import android.app.Application;
import android.content.Context;


public class MainApplication23rd extends Application {
    protected static final String TAG = MainApplication23rd.class.toString();
    public static int CountTips = 0;

    private static MainApplication23rd mainApplication23rd;

    public static MainApplication23rd getApp() {
        return mainApplication23rd;
    }

    private AdController23rd adController23rd;

//    public static FirebaseAnalytics mFirebaseAnalytics;

    public MainApplication23rd() {
        mainApplication23rd = this;
    }

    public static Context getContext() {
        return mainApplication23rd;
    }


    @Override
    public void onCreate() {
        super.onCreate();

        mainApplication23rd = this;

        try {
            adController23rd = AdController23rd.getInstance();

            adController23rd.initAd(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
