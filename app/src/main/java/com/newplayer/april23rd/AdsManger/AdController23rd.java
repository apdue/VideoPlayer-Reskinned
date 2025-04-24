package com.newplayer.april23rd.AdsManger;

import static com.newplayer.april23rd.AdsManger.AppOpenManagerVD23rd.appOpenAd;
import static com.newplayer.april23rd.AdsManger.Splash23rdActivity.TAG_ADMOB_APPOPEN_FREQUENCY;
import static com.newplayer.april23rd.AdsManger.Splash23rdActivity.TAG_BANNER;
import static com.newplayer.april23rd.AdsManger.Splash23rdActivity.TAG_BANNERMAIN;
import static com.newplayer.april23rd.AdsManger.Splash23rdActivity.TAG_FIRST_TRIAL;
import static com.newplayer.april23rd.AdsManger.Splash23rdActivity.TAG_INTERPRELOAD;
import static com.newplayer.april23rd.AdsManger.Splash23rdActivity.TAG_INTERSDIALOG;
import static com.newplayer.april23rd.AdsManger.Splash23rdActivity.TAG_INTERSDIALOG_MAX_TIME;
import static com.newplayer.april23rd.AdsManger.Splash23rdActivity.TAG_INTERSTITIAL;
import static com.newplayer.april23rd.AdsManger.Splash23rdActivity.TAG_INTERSTITIALMAIN;
import static com.newplayer.april23rd.AdsManger.Splash23rdActivity.TAG_INTERSTITIALTYPE;
import static com.newplayer.april23rd.AdsManger.Splash23rdActivity.TAG_NATIVE;
import static com.newplayer.april23rd.AdsManger.Splash23rdActivity.TAG_NATIVEIDR;
import static com.newplayer.april23rd.AdsManger.Splash23rdActivity.TAG_NATIVE_ADS_ENABLED;
import static com.newplayer.april23rd.AdsManger.Splash23rdActivity.TAG_OPENAPPID;
import static com.newplayer.april23rd.AdsManger.Splash23rdActivity.TAG_PRO;
import static com.newplayer.april23rd.AdsManger.Splash23rdActivity.TAG_TRIAL;
import static com.newplayer.april23rd.AdsManger.Splash23rdActivity.TAG_WHATSAPP_SHARE_FREQUENCY;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.appopen.AppOpenAd;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.nativead.NativeAdOptions;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.newplayer.april23rd.R;

import java.util.concurrent.atomic.AtomicBoolean;

public class AdController23rd {

    //Prefrance
    private static PrefManagerVideo23rd prf;

    //rajanads
    // ad will be shown after each x url loadings or clicks on navigation drawer menu
    private static final int ADMOB_INTERSTITIAL_FREQUENCY = 3;
    private static int sInterstitialCounter = 1;
    private static final int ADMOB_APPOPEN_FREQUENCY = 3;
    private static int sAppopenCounter = 1;
    public static final int WHATSAPP_SHARE_FREQUENCY = 3;
    public static int sWhatsappCounter = 1;
    public static final String TAG_WHATSAPP_COUNTER = "whatsapp_counter";

    public static final int PRO_SHARE_FREQUENCY = 3;
    public static int sProCounter = 1;
    public static final String TAG_PRO_COUNTER = "pro_counter";

    private static volatile AdController23rd adController23rdGoldInstance = null;

    private MyCallback myCallback;

    public interface MyCallback {
        void callbackCall();
    }

    private AdController23rd() {}

    public static AdController23rd getInstance() {
        if (adController23rdGoldInstance == null) {
            synchronized(AdController23rd.class) {
                if (adController23rdGoldInstance == null) {
                    adController23rdGoldInstance = new AdController23rd();
                }
            }
        }
        return adController23rdGoldInstance;
    }

    public static void initAd(Context context) {
        prf = new PrefManagerVideo23rd(context);
        try {
            MobileAds.initialize(context, initializationStatus -> {
            });
        } catch (Exception e) {
            //FirebaseCrashlytics.getInstance().recordException(e);
            //e.printStackTrace();
        }
    }

    public void loadBannerAdforNative(Activity context, int nativesize, FrameLayout bannerAdContainer) {
        AdView mAdView = null;
        AdRequest adRequest = null;

        if(prf.getString(TAG_BANNER).equalsIgnoreCase("admob")) {
            adRequest = new AdRequest.Builder().build();
        }

        if (prf.getString(TAG_BANNER).equalsIgnoreCase("admob")) {
//        mAdView = (AdView) findViewById(R.id.adView_view);
            mAdView = new AdView(context);

//            if (nativesize == 0) {
//                mAdView.setAdSize(AdSize.SMART_BANNER);
//            } else if (nativesize == 2) {
//                mAdView.setAdSize(AdSize.SMART_BANNER);
//            } else {
//                mAdView.setAdSize(AdSize.MEDIUM_RECTANGLE);
//            }
            AdSize adSize = getAdSize(context, bannerAdContainer);
            mAdView.setAdSize(adSize);

            mAdView.setAdUnitId(prf.getString(TAG_BANNERMAIN));
            ((FrameLayout) bannerAdContainer).addView(mAdView);
            if (prf.getString("SUBSCRIBED").equals("FALSE")) {
                mAdView.loadAd(adRequest);
            }
        }

        if(prf.getString(TAG_BANNER).equalsIgnoreCase("fb")) {
            //facebook banner ads
        }
    }

    static AdSize getAdSize(Activity context, FrameLayout bannerAdContainer) {
        // Determine the screen width (less decorations) to use for the ad width.
        Display display = context.getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float density = outMetrics.density;

        float adWidthPixels = bannerAdContainer.getWidth();

        // If the ad hasn't been laid out, default to the full screen width.
        if (adWidthPixels == 0) {
            adWidthPixels = outMetrics.widthPixels;
        }

        int adWidth = (int) (adWidthPixels / density);
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(context, adWidth);
    }

    private InterstitialAd mInterstitialAd;

    public void loadInterAd(Context context) {
        if (prf.getString(TAG_INTERSTITIALMAIN).contains("rajan")) {
            return;
        }

        if (prf.getString(TAG_INTERPRELOAD).contains("no") && prf.getString(TAG_INTERSDIALOG).contains("yes")) {
            return;
        }

        if(prf.getString(TAG_INTERSTITIAL).equalsIgnoreCase("admob")) {
            if (mInterstitialAd == null) {
                AdRequest adRequest = new AdRequest.Builder().build();
                InterstitialAd.load(context, prf.getString(TAG_INTERSTITIALMAIN), adRequest, new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        super.onAdLoaded(interstitialAd);
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        super.onAdFailedToLoad(loadAdError);
                        // Handle the error
                        mInterstitialAd = null;
                        //reload
                        // loadInterAd(context);
                    }
                });
            }
        } else if(prf.getString(TAG_INTERSTITIAL).equalsIgnoreCase("fb")) {
            //load facebook inter ads
        }
    }

    public void loadInterAdWriteCallBackAtLoadTime(Activity context) {
        if (prf.getString(TAG_INTERSTITIALMAIN).contains("rajan")) {
            return;
        }
        if(prf.getString(TAG_INTERSTITIAL).equalsIgnoreCase("admob")) {
            if (mInterstitialAd == null) {

                AdRequest adRequest = new AdRequest.Builder().build();
                InterstitialAd.load(context, prf.getString(TAG_INTERSTITIALMAIN), adRequest, new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        super.onAdLoaded(interstitialAd);
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                        //new
                        //write call back declare here
                        mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdDismissedFullScreenContent() {
                                // Called when fullscreen content is dismissed.
                                // Make sure to set your reference to null so you don't
                                // show it a second time.
                                mInterstitialAd = null;
                                loadInterAdWriteCallBackAtLoadTime(context);
                                // startActivity(context, intent, requstCode);
                                if (myCallback != null) {
                                    myCallback.callbackCall();
                                    myCallback = null;
                                }
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(AdError adError) {
                                // Called when fullscreen content failed to show.
                                // Make sure to set your reference to null so you don't
                                // show it a second time.
                                mInterstitialAd = null;
                                loadInterAdWriteCallBackAtLoadTime(context);
                                // startActivity(context, intent, requstCode);
                                if (myCallback != null) {
                                    myCallback.callbackCall();
                                    myCallback = null;
                                }
                            }

                            @Override
                            public void onAdShowedFullScreenContent() {
                                // Called when fullscreen content is shown.
                                // Log.d("TAG", "The ad was shown.");
                            }
                        });
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        super.onAdFailedToLoad(loadAdError);
                        // Handle the error
                        mInterstitialAd = null;
                    }
                });
            }
        } else if(prf.getString(TAG_INTERSTITIAL).equalsIgnoreCase("fb")) {
            //load facebook inter ads
        }
    }

    private InterstitialAd mInterstitialAdDialog;
    private android.app.AlertDialog dialog = null;
    public void loadAndShowInterAdWithDialog(Activity context, MyCallback myCallback2) {
        //note: ads counter already checked in parent method so not check again

        myCallback = myCallback2;

        if (prf.getString(TAG_INTERSTITIALMAIN).contains("rajan")) {
            if (myCallback != null) {
                myCallback.callbackCall();
                myCallback = null;
            }
            return;
        }
        //final android.app.AlertDialog alert_dialog;
        //android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        //View view = LayoutInflater.from(this).inflate(R.layout.feedback_dialog, null);
        //builder.setView(view);
        //alert_dialog = builder.create();
        //alert_dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.ad_progress_dialog_23rd, null);
        builder.setView(view);
        dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);

        //Window window = dialog.getWindow();
        //window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.show();

        if(prf.getString(TAG_INTERSTITIAL).equalsIgnoreCase("admob")) {
            if (mInterstitialAdDialog == null) {
                AdRequest adRequest = new AdRequest.Builder().build();
                InterstitialAd.load(context, prf.getString(TAG_INTERSTITIALMAIN), adRequest, new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        super.onAdLoaded(interstitialAd);
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAdDialog = interstitialAd;
                        //new
                        //check already dialog is dismissed by countdown or not
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();

                            //write call back declare here
                            mInterstitialAdDialog.setFullScreenContentCallback(new FullScreenContentCallback() {
                                @Override
                                public void onAdDismissedFullScreenContent() {
                                    // Called when fullscreen content is dismissed.
                                    // Make sure to set your reference to null so you don't
                                    // show it a second time.
                                    mInterstitialAdDialog = null;
                                    // No preload for Dialog bqz orignal mInterstitialAd object
                                    // preload running j che so
                                    // loadInterAd(context);
                                    if (myCallback != null) {
                                        myCallback.callbackCall();
                                        myCallback = null;
                                    }
                                }

                                @Override
                                public void onAdFailedToShowFullScreenContent(AdError adError) {
                                    // Called when fullscreen content failed to show.
                                    // Make sure to set your reference to null so you don't
                                    // show it a second time.
                                    mInterstitialAdDialog = null;
                                    // No preload for Dialog bqz orignal mInterstitialAd object
                                    // preload running j che so
                                    // loadInterAd(context);
                                    if (myCallback != null) {
                                        myCallback.callbackCall();
                                        myCallback = null;
                                    }
                                }

                                @Override
                                public void onAdShowedFullScreenContent() {
                                    // Called when fullscreen content is shown.
                                    // Log.d("TAG", "The ad was shown.");
                                }
                            });

                            //show ads need to pass Activity object
                            mInterstitialAdDialog.show((Activity) context);
                        }
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        super.onAdFailedToLoad(loadAdError);
                        // Handle the error
                        mInterstitialAdDialog = null;
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        if (myCallback != null) {
                            myCallback.callbackCall();
                            myCallback = null;
                        }
                        //ad not showed so do not increment counter
                        checkfbAdMinusOne();
                    }
                });
            } else {
                // count down na lidhe load thyeli ads show na thy hoi tyre aa execute thshe
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();

                    //write call back declare here
                    mInterstitialAdDialog.setFullScreenContentCallback(new FullScreenContentCallback() {
                        @Override
                        public void onAdDismissedFullScreenContent() {
                            // Called when fullscreen content is dismissed.
                            // Make sure to set your reference to null so you don't
                            // show it a second time.
                            mInterstitialAdDialog = null;
                            // No preload for Dialog bqz orignal mInterstitialAd object
                            // preload running j che so
                            // loadInterAd(context);
                            if (myCallback != null) {
                                myCallback.callbackCall();
                                myCallback = null;
                            }
                        }

                        @Override
                        public void onAdFailedToShowFullScreenContent(AdError adError) {
                            // Called when fullscreen content failed to show.
                            // Make sure to set your reference to null so you don't
                            // show it a second time.
                            mInterstitialAdDialog = null;
                            // No preload for Dialog bqz orignal mInterstitialAd object
                            // preload running j che so
                            // loadInterAd(context);
                            if (myCallback != null) {
                                myCallback.callbackCall();
                                myCallback = null;
                            }
                        }

                        @Override
                        public void onAdShowedFullScreenContent() {
                            // Called when fullscreen content is shown.
                            // Log.d("TAG", "The ad was shown.");
                        }
                    });

                    //show ads need to pass Activity object
                    mInterstitialAdDialog.show((Activity) context);
                }
            }
        } else if(prf.getString(TAG_INTERSTITIAL).equalsIgnoreCase("fb")) {
            //load facebook inter ads
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
            if (myCallback != null) {
                myCallback.callbackCall();
                myCallback = null;
            }
        }

        new CountDownTimer(Integer.parseInt(prf.getString(TAG_INTERSDIALOG_MAX_TIME)) * 1000, 10) {
            @Override
            public void onTick(long millisUntilFinished) {
                double time = (millisUntilFinished / 10) / Integer.parseInt(prf.getString(TAG_INTERSDIALOG_MAX_TIME));
            }

            @Override
            public void onFinish() {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                    // Called when fullscreen content is dismissed.
                    // Make sure to set your reference to null so you don't
                    // show it a second time.
                    // mInterstitialAd = null;
                    if (myCallback != null) {
                        myCallback.callbackCall();
                        myCallback = null;
                    }
                    // startActivity(context, intent, requstCode);
                    //not loaded so reload it.
                    // loadInterAd(context);
                }
            }
        }.start();
    }

    public void showInterAd(final Activity context, final Intent intent, final int requstCode) {
        if(prf.getString(TAG_INTERSTITIAL).equalsIgnoreCase("admob")) {
            try {
                if (mInterstitialAd != null) {
                    if (AdController23rd.checkfbAd()) {
                        mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdDismissedFullScreenContent() {
                                // Called when fullscreen content is dismissed.
                                // Make sure to set your reference to null so you don't
                                // show it a second time.
                                mInterstitialAd = null;
                                loadInterAd(context);
                                startActivity(context, intent, requstCode);
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(AdError adError) {
                                // Called when fullscreen content failed to show.
                                // Make sure to set your reference to null so you don't
                                // show it a second time.
                                mInterstitialAd = null;
                                loadInterAd(context);
                                startActivity(context, intent, requstCode);
                            }

                            @Override
                            public void onAdShowedFullScreenContent() {
                                // Called when fullscreen content is shown.
                                // Log.d("TAG", "The ad was shown.");
                            }
                        });
                        mInterstitialAd.show((Activity) context);
                    } else {
                        startActivity(context, intent, requstCode);
                    }
                } else {
                    startActivity(context, intent, requstCode);
                    loadInterAd(context);
                }
            } catch (Exception e) {
                startActivity(context, intent, requstCode);
//            e.printStackTrace();
            }
        } else if(prf.getString(TAG_INTERSTITIAL).equalsIgnoreCase("fb")) {
            startActivity(context, intent, requstCode);
        }
    }

    private void startActivity(Activity context, Intent intent, int requestCode) {
        if (intent != null) {
            context.startActivityForResult(intent, requestCode);
        }
    }

    private void showInterAd(final Fragment context, final Intent intent, final int requstCode) {
        if(prf.getString(TAG_INTERSTITIAL).equalsIgnoreCase("admob")) {
            try {
                if (mInterstitialAd != null) {
                    if (AdController23rd.checkfbAd()) {
                        mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdDismissedFullScreenContent() {
                                // Called when fullscreen content is dismissed.
                                // Make sure to set your reference to null so you don't
                                // show it a second time.
                                mInterstitialAd = null;
                                // Log.d("TAG", "The ad was dismissed.");
                                loadInterAd(context.getContext());
                                startActivity(context, intent, requstCode);
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(AdError adError) {
                                // Called when fullscreen content failed to show.
                                // Make sure to set your reference to null so you don't
                                // show it a second time.
                                mInterstitialAd = null;
                                // Log.d("TAG", "The ad failed to show.");
                                loadInterAd(context.getContext());
                                startActivity(context, intent, requstCode);
                            }


                            @Override
                            public void onAdShowedFullScreenContent() {
                                // Called when fullscreen content is shown.
                                // Log.d("TAG", "The ad was shown.");
                            }
                        });
                        mInterstitialAd.show(context.getActivity());
                    } else {
                        startActivity(context, intent, requstCode);
                    }
                } else {
                    startActivity(context, intent, requstCode);
                    //not loaded so reload it.
                    loadInterAd(context.getContext());
                }
            } catch (Exception e) {
                startActivity(context, intent, requstCode);
//            e.printStackTrace();
            }
        } else if(prf.getString(TAG_INTERSTITIAL).equalsIgnoreCase("fb")) {
            startActivity(context, intent, requstCode);
        }
    }

    private void startActivity(Fragment context, Intent intent, int requestCode) {
        if (intent != null) {
            context.startActivityForResult(intent, requestCode);
        }
    }

    public void showInterAdOnly(final Activity context, final int requstCode) {
        if(prf.getString(TAG_INTERSTITIAL).equalsIgnoreCase("admob")) {
            try {
                if (mInterstitialAd != null) {
                    if (AdController23rd.checkfbAd()) {
                        mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdDismissedFullScreenContent() {
                                // Called when fullscreen content is dismissed.
                                // Make sure to set your reference to null so you don't
                                // show it a second time.
                                mInterstitialAd = null;
                                loadInterAd(context);
                                // startActivity(context, intent, requstCode);
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(AdError adError) {
                                // Called when fullscreen content failed to show.
                                // Make sure to set your reference to null so you don't
                                // show it a second time.
                                mInterstitialAd = null;
                                loadInterAd(context);
                                // startActivity(context, intent, requstCode);
                            }


                            @Override
                            public void onAdShowedFullScreenContent() {
                                // Called when fullscreen content is shown.
                                // Log.d("TAG", "The ad was shown.");
                            }
                        });
                        mInterstitialAd.show((Activity) context);
                    } else {
//                        startActivity(context, intent, requstCode);
                    }
                } else {
//                    startActivity(context, intent, requstCode);
                    //not loaded so reload it.
                    loadInterAd(context);
                }
            } catch (Exception e) {
//                startActivity(context, intent, requstCode);
//            e.printStackTrace();
            }
        } else if(prf.getString(TAG_INTERSTITIAL).equalsIgnoreCase("fb")) {
//            startActivity(context, intent, requstCode);
        }
    }

    public void showInterAdCallBack(final Activity context, final MyCallback myCallback2) {
        myCallback = myCallback2;

        if (AdController23rd.checkfbAd()) {
            //continue next for show ad
            if (AdController23rd.checkfbAdAppopen()) {
                prf.setString(TAG_INTERSTITIALTYPE,"appopen");
            } else {
                prf.setString(TAG_INTERSTITIALTYPE,"inter");
            }
        } else {
            // startActivity(context, intent, requstCode);
            myCallback.callbackCall();
            myCallback = null;
            return;
        }

        if (prf.getString(TAG_INTERSTITIAL).equalsIgnoreCase("admob") && prf.getString(TAG_INTERSTITIALTYPE).equalsIgnoreCase("inter")) {
            try {
                if (mInterstitialAd != null) {
                    //ads loaded so directly show it no need for dialog yes or no check
                    mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                        @Override
                        public void onAdDismissedFullScreenContent() {
                            // Called when fullscreen content is dismissed.
                            // Make sure to set your reference to null so you don't
                            // show it a second time.
                            mInterstitialAd = null;
                            loadInterAd(context);
                            // startActivity(context, intent, requstCode);
                            myCallback.callbackCall();
                            myCallback = null;
                        }

                        @Override
                        public void onAdFailedToShowFullScreenContent(AdError adError) {
                            // Called when fullscreen content failed to show.
                            // Make sure to set your reference to null so you don't
                            // show it a second time.
                            mInterstitialAd = null;
                            loadInterAd(context);
                            // startActivity(context, intent, requstCode);
                            myCallback.callbackCall();
                            myCallback = null;
                        }

                        @Override
                        public void onAdShowedFullScreenContent() {
                            // Called when fullscreen content is shown.
                            // Log.d("TAG", "The ad was shown.");
                        }
                    });
                    //show ads need to pass Activity object
                    mInterstitialAd.show((Activity) context);
                } else {
                    //ads not loaded so check for dialog yes or no than do next
                    if (prf.getString(TAG_INTERSDIALOG).equalsIgnoreCase("yes")) {
                        //preload with dialog(show dialog if ad not loaded)
                        //ad not loaded so show dialog till ad loaded
                        loadAndShowInterAdWithDialog(context, myCallback2);
                    } else {
                        //preload with no dialog
                        myCallback.callbackCall();
                        myCallback = null;
                        // startActivity(context, intent, requstCode);
                        //not loaded so reload it.
                        loadInterAd(context);
                        //ad not showed so do not increment counter
                        checkfbAdMinusOne();
                    }
                }
            } catch (Exception e) {
                myCallback.callbackCall();
                myCallback = null;
                //FirebaseCrashlytics.getInstance().recordException(e);
                e.printStackTrace();
            }
        } else if (prf.getString(TAG_INTERSTITIAL).equalsIgnoreCase("fb")) {
            myCallback.callbackCall();
            myCallback = null;
            // startActivity(context, intent, requstCode);
        } else if (prf.getString(TAG_INTERSTITIAL).equalsIgnoreCase("admob") && prf.getString(TAG_INTERSTITIALTYPE).equalsIgnoreCase("appopen")) {
            // Only show ad if there is not already an app open ad currently showing
            // and an ad is available.
            if (appOpenAd != null) {
                FullScreenContentCallback fullScreenContentCallback =
                        new FullScreenContentCallback() {
                            @Override
                            public void onAdDismissedFullScreenContent() {
                                // Set the reference to null so isAdAvailable() returns false.
                                appOpenAd = null;
                                AppOpenManagerVD23rd.isShowingAd = false;

                                myCallback.callbackCall();
                                myCallback = null;

                                AppOpenManagerVD23rd.fetchAd();
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(AdError adError) {
                                // Set the reference to null so isAdAvailable() returns false.
                                appOpenAd = null;
                                AppOpenManagerVD23rd.isShowingAd = false;

                                myCallback.callbackCall();
                                myCallback = null;

                                AppOpenManagerVD23rd.fetchAd();
                            }

                            @Override
                            public void onAdShowedFullScreenContent() {
                                AppOpenManagerVD23rd.isShowingAd = true;
                            }
                        };

                appOpenAd.setFullScreenContentCallback(fullScreenContentCallback);
                appOpenAd.show(context);
            } else {
                if (prf.getString(TAG_INTERSDIALOG).equalsIgnoreCase("yes")) {
                    loadAndShowAppopenAdWithDialog(context,myCallback);
                } else {
                    myCallback.callbackCall();
                    myCallback = null;
                }
            }
        }
    }

    private AppOpenAd appOpenAdDialog = null;
    //private android.app.AlertDialog dialog = null;
    public void loadAndShowAppopenAdWithDialog(Activity context, MyCallback myCallback2) {
        //note: ads counter already checked in parent method so not check again

        myCallback = myCallback2;

        if (prf.getString(TAG_INTERSTITIALMAIN).contains("rajan")) {
            if (myCallback != null) {
                myCallback.callbackCall();
                myCallback = null;
            }
            return;
        }

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.ad_progress_dialog_23rd, null);
        builder.setView(view);
        dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);

        //Window window = dialog.getWindow();
        //window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.show();

        AppOpenAd.AppOpenAdLoadCallback loadCallback = new AppOpenAd.AppOpenAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull AppOpenAd ad) {
                appOpenAdDialog = ad;
                if (appOpenAdDialog != null) {
                    FullScreenContentCallback fullScreenContentCallback =
                            new FullScreenContentCallback() {
                                @Override
                                public void onAdDismissedFullScreenContent() {
                                    appOpenAdDialog = null;

                                    appOpenAd = null;
                                    AppOpenManagerVD23rd.isShowingAd = false;

                                    // preload running j che so
                                    if (myCallback != null) {
                                        myCallback.callbackCall();
                                        myCallback = null;
                                    }
                                }
                                @Override
                                public void onAdFailedToShowFullScreenContent(AdError adError) {
                                    super.onAdFailedToShowFullScreenContent(adError);
                                    appOpenAdDialog = null;

                                    AppOpenManagerVD23rd.isShowingAd = false;

                                    // No preload for Dialog bqz orignal mInterstitialAd object
                                    // preload running j che so
                                    if (myCallback != null) {
                                        myCallback.callbackCall();
                                        myCallback = null;
                                    }
                                }
                                @Override
                                public void onAdShowedFullScreenContent() {
                                    AppOpenManagerVD23rd.isShowingAd = true;
                                }
                            };

                    //check already dialog is dismissed by countdown or not
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                        appOpenAdDialog.setFullScreenContentCallback(fullScreenContentCallback);
                        appOpenAdDialog.show(context);
                    }
                } else {
                    appOpenAdDialog = null;
                    // No preload for Dialog bqz orignal mInterstitialAd object
                    // preload running j che so
                    if (myCallback != null) {
                        myCallback.callbackCall();
                        myCallback = null;
                    }
                }
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                // Handle the error
                appOpenAdDialog = null;
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                if (myCallback != null) {
                    myCallback.callbackCall();
                    myCallback = null;
                }
                //ad not showed so do not increment counter
                checkfbAdMinusOne();
            }
        };
        AdRequest request = new AdRequest.Builder().build();
        AppOpenAd.load(context, prf.getString(TAG_OPENAPPID),
                request, loadCallback);

        new CountDownTimer(Integer.parseInt(prf.getString(TAG_INTERSDIALOG_MAX_TIME)) * 1000, 10) {
            @Override
            public void onTick(long millisUntilFinished) {
                double time = (millisUntilFinished / 10) / Integer.parseInt(prf.getString(TAG_INTERSDIALOG_MAX_TIME));
            }

            @Override
            public void onFinish() {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                    // Called when fullscreen content is dismissed.
                    // Make sure to set your reference to null so you don't
                    // show it a second time.
                    // mInterstitialAd = null;
                    if (myCallback != null) {
                        myCallback.callbackCall();
                        myCallback = null;
                    }
                }
            }
        }.start();
    }

    public void newreleasenativeNoPreload(Activity activity, String nativeid, int iInstant, FrameLayout frameLayout1) {
        if (nativeid.contains("rajan")) {
            frameLayout1.setMinimumHeight(dpToPx(activity, 0));
            return;
        }
        if (prf.getString("nativeadsalltobanner").contains("1")) {
            loadBannerAdforNative(activity, iInstant, frameLayout1);
            return;
        }
        if (prf.getString("nativeadsbigtosmall").contains("1") && iInstant == 1) {
//            frameLayout1.setMinimumHeight((int) activity1.getResources().getDimension(R.dimen.smallnative_height));
            frameLayout1.setMinimumHeight(dpToPx(activity, 180));
        }
        if (prf.getString(TAG_NATIVE_ADS_ENABLED).equalsIgnoreCase("yes")){

            if (prf.getString(TAG_NATIVE).equalsIgnoreCase("admob")) {

                //admob
                AdLoader adLoader;
                // com.google.android.gms.ads.nativead.NativeAd nativeAdmob;
                FrameLayout frameLayout;

                frameLayout = frameLayout1;
                AdLoader.Builder builder = new AdLoader.Builder(activity, nativeid);

                builder.forNativeAd(
                        nativeAd -> {
                            // If this callback occurs after the activity is destroyed, you must call
                            // destroy and return or you may get a memory leak.

                            if (nativeAd == null) {
                                nativeAd.destroy();
                                return;
                            }

//                            nativeAdmob = nativeAd;
//                            FrameLayout frameLayout = findViewById(R.id.fl_adplaceholderrgold);
                            NativeAdView adView;
                            if (iInstant == 0) {
                                adView = (NativeAdView) activity.getLayoutInflater().inflate(R.layout.ad_layout_small_23rd, null);
                            } else if (iInstant == 2) {
                                adView = (NativeAdView) activity.getLayoutInflater().inflate(R.layout.ad_layout_extra_small_23rd, null);
                            } else {
                                if (prf.getString("nativeadsbigtosmall").contains("1")) {
                                     frameLayout.setBackgroundColor(activity.getResources().getColor(R.color.small_native_ad_bck_color));
                                    // frameLayout.setMinimumHeight((int) activity.getResources().getDimension(R.dimen.smallnative_height));
                                    adView = (NativeAdView) activity.getLayoutInflater().inflate(R.layout.ad_layout_small_23rd, null);
                                } else {
                                    // frameLayout.setBackgroundColor(activity.getResources().getColor(R.color.darkBlue));
                                    adView = (NativeAdView) activity.getLayoutInflater().inflate(R.layout.ad_layout_big_23rd, null);
                                }
                            }

                            populateNativeAdView(nativeAd, adView, iInstant);
                            if(frameLayout != null){
                                frameLayout.removeAllViews();
                                frameLayout.addView(adView);
                            }

                        });

                VideoOptions videoOptions =
                        new VideoOptions.Builder().setStartMuted(true).build();

                NativeAdOptions adOptions =
                        new NativeAdOptions.Builder().setVideoOptions(videoOptions).build();

                builder.withNativeAdOptions(adOptions);

                adLoader =
                        builder
                                .withAdListener(
                                        new AdListener() {
                                            @Override
                                            public void onAdFailedToLoad(LoadAdError loadAdError) {
                                                //native ads failed to load so set it to null
                                                //nativeAd = null;
                                            }
                                        })
                                .build();

                if (prf.getString("SUBSCRIBED").equals("FALSE")) {
                    adLoader.loadAd(new AdRequest.Builder().build());
                }

            }

            if (prf.getString(TAG_NATIVE).equalsIgnoreCase("fb")) {
//                //fb
            }
        }
    }

    public static int dpToPx(Activity activity, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, activity.getResources().getDisplayMetrics());
    }

    static private com.google.android.gms.ads.nativead.NativeAd nativeAdmob = null;
    static private AdLoader adLoader = null;

    //activity and frameLayout make here super variables because if method ma define kriye to to ae je activity ae aene call
    //kryu aene matej use krshe jyre ads load thy jashe tyre new call avi jashe tyre new value ne allocate ny kre
    static private Activity activity = null;
    static private FrameLayout frameLayout = null;
    static private int i = 0;
    static private AtomicBoolean isSameActivity = null;
    static private AdLoader.Builder builder = null;

    public void newreleasenativePreload(Activity activity1, String nativeid, int nativesize, FrameLayout frameLayout1, Boolean nextload) {
        if (nativeid.contains("rajan")) {
            frameLayout1.setMinimumHeight(dpToPx(activity1, 0));
            return;
        }
        if (prf.getString("nativeadsalltobanner").contains("1")) {
            loadBannerAdforNative(activity1, nativesize, frameLayout1);
            return;
        }
        if (prf.getString("nativeadsbigtosmall").contains("1") && nativesize == 1) {
//            frameLayout1.setMinimumHeight((int) activity1.getResources().getDimension(R.dimen.smallnative_height));
            frameLayout1.setMinimumHeight(dpToPx(activity1, 180));
        }
        activity = activity1;
        frameLayout = frameLayout1;
        i = nativesize;
        isSameActivity = new AtomicBoolean(false);

        if (prf.getString(TAG_NATIVE_ADS_ENABLED).equalsIgnoreCase("yes")){

            if (prf.getString(TAG_NATIVE).equalsIgnoreCase("admob")) {
                //check native is preloaded or not
                if (nativeAdmob == null) {
                    //admob
//                    FrameLayout frameLayout;
//
//                    frameLayout = frameLayout1;
                    //remaining to make public because every time new activity object use thy so
                    //old line, new line added for every time new context related change
                    //builder = new AdLoader.Builder(activity, prf.getString(LogicSplashActivity.TAG_NATIVEID));
                    builder = new AdLoader.Builder((MainApplication23rd) MainApplication23rd.getContext(), prf.getString(TAG_NATIVEIDR));

                    builder.forNativeAd(
                            nativeAd -> {
                                // If this callback occurs after the activity is destroyed, you must call
                                // destroy and return or you may get a memory leak.

                                if (nativeAd == null) {
                                    nativeAd.destroy();
                                    return;
                                }

                                // You must call destroy on old ads when you are done with them,
                                // otherwise you will have a memory leak.
                                if (nativeAdmob != null) {
                                    nativeAdmob.destroy();
                                }
                                nativeAdmob = nativeAd;

                                // FrameLayout frameLayout = findViewById(R.id.fl_adplaceholderrgold);
                                NativeAdView adView;
                                if (i == 0) {
                                    adView = (NativeAdView) activity.getLayoutInflater().inflate(R.layout.ad_layout_small_23rd, null);
                                } else if (i == 2) {
                                    adView = (NativeAdView) activity.getLayoutInflater().inflate(R.layout.ad_layout_extra_small_23rd, null);
                                } else {
                                    if (prf.getString("nativeadsbigtosmall").contains("1")) {
                                         frameLayout.setBackgroundColor(activity.getResources().getColor(R.color.small_native_ad_bck_color));
                                        // frameLayout.setMinimumHeight((int) activity.getResources().getDimension(R.dimen.smallnative_height));
                                        adView = (NativeAdView) activity.getLayoutInflater().inflate(R.layout.ad_layout_small_23rd, null);
                                    } else {
                                        // frameLayout.setBackgroundColor(activity.getResources().getColor(R.color.darkBlue));
                                        adView = (NativeAdView) activity.getLayoutInflater().inflate(R.layout.ad_layout_big_23rd, null);
                                    }
                                }

                                //new preload
                                if (activity.hasWindowFocus() && !(isSameActivity).get()) {
                                    //check framelayout already filled with native ads in oncreate or onresume or not
                                    if (frameLayout.getChildCount() == 0) {
                                        //activity is visible so show ads now
                                        populateNativeAdView(nativeAdmob, adView, i);
                                        if (frameLayout != null) {
                                            frameLayout.removeAllViews();
                                            frameLayout.addView(adView);
                                        }

                                        isSameActivity.set(true);
                                        //native ads populated so set it to null
                                        nativeAdmob = null;
                                        //start preloading ads
                                        if (prf.getString("SUBSCRIBED").equals("FALSE") && !adLoader.isLoading() && nextload) {
                                            adLoader = builder.withAdListener(new AdListener() {
                                                @Override
                                                public void onAdFailedToLoad(LoadAdError var1) {
                                                    //native ads failed to load so set it to null
                                                    nativeAdmob = null;
                                                }
                                            }).build();
                                            adLoader.loadAd(new AdRequest.Builder().build());
                                        }
                                    } else {
                                        //activity is switched so save native ads and do not populate and show it
                                        //already saved and assigned to nativeAdmob
                                        // nativeAdmob = nativeAd;
                                    }
                                } else {
                                    //activity is switched so save native ads and do not populate and show it
                                    //already saved and assigned to nativeAdmob
                                    // nativeAdmob = nativeAd;
                                }

                            });

                    VideoOptions videoOptions =
                            new VideoOptions.Builder().setStartMuted(true).build();

                    NativeAdOptions adOptions =
                            new NativeAdOptions.Builder().setVideoOptions(videoOptions).build();

                    builder.withNativeAdOptions(adOptions);

                    if (adLoader == null) {
                        adLoader = builder.withAdListener(new AdListener() {
                            @Override
                            public void onAdFailedToLoad(LoadAdError loadAdError) {
                                //native ads failed to load so set it to null
                                nativeAdmob = null;
                                Log.e("TAG", "onAdFailedToLoad: "+loadAdError );
                            }
                            @Override
                            public void onAdImpression() {
                            }
                        }).build();
                    }

                    if (prf.getString("SUBSCRIBED").equals("FALSE") && !adLoader.isLoading()) {
                        adLoader.loadAd(new AdRequest.Builder().build());
                    }
                } else {
                    //check frameLayout1 already filled with native ads in oncreate or onresume or not
                    //activity.hasWindowFocus() not written because activity onresume wrong value return for it
                    if (frameLayout.getChildCount() == 0) {
                        NativeAdView adView;
                        if (i == 0) {
                            adView = (NativeAdView) activity.getLayoutInflater().inflate(R.layout.ad_layout_small_23rd, null);
                        } else if (i == 2) {
                            adView = (NativeAdView) activity.getLayoutInflater().inflate(R.layout.ad_layout_extra_small_23rd, null);
                        } else {
                            if (prf.getString("nativeadsbigtosmall").contains("1")) {
                                 frameLayout.setBackgroundColor(activity.getResources().getColor(R.color.small_native_ad_bck_color));
                                // frameLayout.setMinimumHeight((int) activity.getResources().getDimension(R.dimen.smallnative_height));
                                adView = (NativeAdView) activity.getLayoutInflater().inflate(R.layout.ad_layout_small_23rd, null);
                            } else {
                                adView = (NativeAdView) activity.getLayoutInflater().inflate(R.layout.ad_layout_big_23rd, null);
                            }
                        }

                        populateNativeAdView(nativeAdmob, adView, i);
                        if (frameLayout != null) {
                            frameLayout.removeAllViews();
                            frameLayout.addView(adView);
                        }
                        nativeAdmob = null;
                        //start preloading ads
                        if (prf.getString("SUBSCRIBED").equals("FALSE") && nextload) {
                            adLoader = builder.withAdListener(new AdListener() {
                                @Override
                                public void onAdFailedToLoad(LoadAdError var1) {
                                    //native ads failed to load so set it to null
                                    nativeAdmob = null;
                                }
                            }).build();
                            adLoader.loadAd(new AdRequest.Builder().build());
                        }
                    } else {
                        //activity is switched so save native ads and do not populate and show it
                        //already saved
                        // nativeAdmob = nativeAdmob;
                    }
                }

            }

            if (prf.getString(TAG_NATIVE).equalsIgnoreCase("fb")) {
//                //fb
            }
        }
    }

    static private com.google.android.gms.ads.nativead.NativeAd nativeAdmobExtra = null;
    static private AdLoader adLoaderExtra = null;

    //activity and frameLayout make here super variables because if method ma define kriye to to ae je activity ae aene call
    //kryu aene matej use krshe jyre ads load thy jashe tyre new call avi jashe tyre new value ne allocate ny kre
    static private Activity activityExtra = null;
    static private FrameLayout frameLayoutExtra = null;
    static private int iExtra = 0;
    static private AtomicBoolean isSameActivityExtra = null;
    static private AdLoader.Builder builderExtra = null;

    public void newreleasenativePreloadExtra(Activity activity1, String nativeid, int nativesize, FrameLayout frameLayout1, Boolean nextload) {
        if (nativeid.contains("rajan")) {
            frameLayout1.setMinimumHeight(dpToPx(activity1, 0));
            return;
        }
        if (prf.getString("nativeadsalltobanner").contains("1")) {
            loadBannerAdforNative(activity1, nativesize, frameLayout1);
            return;
        }
        activityExtra = activity1;
        frameLayoutExtra = frameLayout1;
        iExtra = nativesize;
        isSameActivityExtra = new AtomicBoolean(false);

        if (prf.getString(TAG_NATIVE_ADS_ENABLED).equalsIgnoreCase("yes")){

            if (prf.getString(TAG_NATIVE).equalsIgnoreCase("admob")) {
                //check native is preloaded or not
                if (nativeAdmobExtra == null) {
                    //admob
//                    FrameLayout frameLayout;
//
//                    frameLayout = frameLayout1;
                    //remaining to make public because every time new activity object use thy so
                    //old line, new line added for every time new context related change
                    //builderExtra = new AdLoader.Builder(activityExtra, prf.getString(LogicSplashActivity.TAG_NATIVEID));
                    builderExtra = new AdLoader.Builder((MainApplication23rd) MainApplication23rd.getContext(), prf.getString(TAG_NATIVEIDR));

                    builderExtra.forNativeAd(
                            nativeAd -> {
                                // If this callback occurs after the activity is destroyed, you must call
                                // destroy and return or you may get a memory leak.

                                if (nativeAd == null) {
                                    nativeAd.destroy();
                                    return;
                                }

                                // You must call destroy on old ads when you are done with them,
                                // otherwise you will have a memory leak.
                                if (nativeAdmobExtra != null) {
                                    nativeAdmobExtra.destroy();
                                }
                                nativeAdmobExtra = nativeAd;

                                // FrameLayout frameLayout = findViewById(R.id.fl_adplaceholderrgold);
                                NativeAdView adView;
                                if (iExtra == 0) {
                                    adView = (NativeAdView) activityExtra.getLayoutInflater().inflate(R.layout.ad_layout_small_23rd, null);
                                } else if (iExtra == 2) {
                                    adView = (NativeAdView) activityExtra.getLayoutInflater().inflate(R.layout.ad_layout_extra_small_23rd, null);
                                } else {
                                    if (prf.getString("nativeadsbigtosmall").contains("1")) {
                                         frameLayoutExtra.setBackgroundColor(activityExtra.getResources().getColor(R.color.small_native_ad_bck_color));
                                        // frameLayout.setMinimumHeight((int) activityExtra.getResources().getDimension(R.dimen.smallnative_height));
                                        adView = (NativeAdView) activityExtra.getLayoutInflater().inflate(R.layout.ad_layout_small_23rd, null);
                                    } else {
                                        adView = (NativeAdView) activityExtra.getLayoutInflater().inflate(R.layout.ad_layout_big_23rd, null);
                                    }
                                }

                                //new preload
                                if (activityExtra.hasWindowFocus() && !(isSameActivityExtra.get())) {
                                    //check framelayout already filled with native ads in oncreate or onresume or not
                                    if (frameLayoutExtra.getChildCount() == 0) {
                                        //activity is visible so show ads now
                                        populateNativeAdView(nativeAdmobExtra, adView, iExtra);
                                        if (frameLayoutExtra != null) {
                                            frameLayoutExtra.removeAllViews();
                                            frameLayoutExtra.addView(adView);
                                        }

                                        isSameActivityExtra.set(true);
                                        //native ads populated so set it to null
                                        nativeAdmobExtra = null;
                                        //start preloading ads
                                        if (prf.getString("SUBSCRIBED").equals("FALSE") && !adLoaderExtra.isLoading() && nextload) {
                                            adLoaderExtra = builderExtra.withAdListener(new AdListener() {
                                                @Override
                                                public void onAdFailedToLoad(LoadAdError var1) {
                                                    //native ads failed to load so set it to null
                                                    nativeAdmobExtra = null;
                                                }
                                            }).build();
                                            adLoaderExtra.loadAd(new AdRequest.Builder().build());
                                        }
                                    } else {
                                        //activity is switched so save native ads and do not populate and show it
                                        //already saved and assigned to nativeAdmobExtra
                                        // nativeAdmobExtra = nativeAd;
                                    }
                                } else {
                                    //activity is switched so save native ads and do not populate and show it
                                    //already saved and assigned to nativeAdmobExtra
                                    // nativeAdmobExtra = nativeAd;
                                }

                            });

                    VideoOptions videoOptions =
                            new VideoOptions.Builder().setStartMuted(true).build();

                    NativeAdOptions adOptions =
                            new NativeAdOptions.Builder().setVideoOptions(videoOptions).build();

                    builderExtra.withNativeAdOptions(adOptions);

                    if (adLoaderExtra == null) {
                        adLoaderExtra = builderExtra.withAdListener(new AdListener() {
                            @Override
                            public void onAdFailedToLoad(LoadAdError loadAdError) {
                                //native ads failed to load so set it to null
                                nativeAdmobExtra = null;
                            }
                        }).build();
                    }

                    if (prf.getString("SUBSCRIBED").equals("FALSE") && !adLoaderExtra.isLoading()) {
                        adLoaderExtra.loadAd(new AdRequest.Builder().build());
                    }
                } else {
                    //check frameLayout1 already filled with native ads in oncreate or onresume or not
                    //activity.hasWindowFocus() not written because activity onresume wrong value return for it
                    if (frameLayoutExtra.getChildCount() == 0) {
                        NativeAdView adView;
                        if (iExtra == 0) {
                            adView = (NativeAdView) activityExtra.getLayoutInflater().inflate(R.layout.ad_layout_small_23rd, null);
                        } else if (iExtra == 2) {
                            adView = (NativeAdView) activityExtra.getLayoutInflater().inflate(R.layout.ad_layout_extra_small_23rd, null);
                        } else {
                            if (prf.getString("nativeadsbigtosmall").contains("1")) {
                                 frameLayoutExtra.setBackgroundColor(activityExtra.getResources().getColor(R.color.small_native_ad_bck_color));
                                // frameLayout.setMinimumHeight((int) activity.getResources().getDimension(R.dimen.smallnative_height));
                                adView = (NativeAdView) activityExtra.getLayoutInflater().inflate(R.layout.ad_layout_small_23rd, null);
                            } else {
                                adView = (NativeAdView) activityExtra.getLayoutInflater().inflate(R.layout.ad_layout_big_23rd, null);
                            }
                        }

                        populateNativeAdView(nativeAdmobExtra, adView, iExtra);
                        if (frameLayoutExtra != null) {
                            frameLayoutExtra.removeAllViews();
                            frameLayoutExtra.addView(adView);
                        }
                        nativeAdmobExtra = null;
                        //start preloading ads
                        if (prf.getString("SUBSCRIBED").equals("FALSE") && nextload) {
                            adLoaderExtra = builderExtra.withAdListener(new AdListener() {
                                @Override
                                public void onAdFailedToLoad(LoadAdError var1) {
                                    //native ads failed to load so set it to null
                                    nativeAdmobExtra = null;
                                }
                            }).build();
                            adLoaderExtra.loadAd(new AdRequest.Builder().build());
                        }
                    } else {
                        //activity is switched so save native ads and do not populate and show it
                        //already saved
                        // nativeAdmob = nativeAdmob;
                    }
                }

            }

            if (prf.getString(TAG_NATIVE).equalsIgnoreCase("fb")) {
//                //fb
            }
        }
    }


    private void populateNativeAdView(com.google.android.gms.ads.nativead.NativeAd nativeAd, NativeAdView adView, int nativesize) {
        //hidemediaview or not logic - not run mediaview code
        boolean hidemediaview = false;
        if (nativesize == 0) {
            //small native
            hidemediaview = false;
        } else if (nativesize == 2) {
            //extrasmall native
            hidemediaview = true;
        } else {
            //big native
            hidemediaview = false;
        }

        // Set the media view. Media content will be automatically populated in the media view once
        // adView.setNativeAd() is called.
        if (!hidemediaview) {
//            //old code uncomment jo scaletype fitxy set krvi hoi to
//            com.google.android.gms.ads.nativead.MediaView mediaView = adView.findViewById(R.id.ad_media);
//            mediaView.setOnHierarchyChangeListener(new ViewGroup.OnHierarchyChangeListener() {
//                @Override
//                public void onChildViewAdded(View parent, View child) {
//                    if (child instanceof ImageView) {
//                        ImageView imageView = (ImageView) child;
//                        imageView.setAdjustViewBounds(true);
//                        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
//                    }
//                }
//
//                @Override
//                public void onChildViewRemoved(View parent, View child) {
//                }
//            });

            // Set the media view.
            adView.setMediaView((com.google.android.gms.ads.nativead.MediaView) adView.findViewById(R.id.ad_media));

            adView.getMediaView().setMediaContent(nativeAd.getMediaContent());
        }

        // Set other ad assets.
        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
        adView.setPriceView(adView.findViewById(R.id.ad_price));
        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
        adView.setStoreView(adView.findViewById(R.id.ad_store));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));

        // The headline and mediaContent are guaranteed to be in every NativeAd.
        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
        //moved this line at hide block
        //adView.getMediaView().setMediaContent(nativeAd.getMediaContent());

        // These assets aren't guaranteed to be in every NativeAd, so it's important to
        // check before trying to display them.
        if (nativeAd.getBody() == null) {
            adView.getBodyView().setVisibility(View.INVISIBLE);
        } else {
            adView.getBodyView().setVisibility(View.VISIBLE);
            ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        }

        if (nativeAd.getCallToAction() == null) {
            adView.getCallToActionView().setVisibility(View.INVISIBLE);
        } else {
            adView.getCallToActionView().setVisibility(View.VISIBLE);
            ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
        }

        if (nativeAd.getIcon() == null) {
            adView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(
                    nativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getPrice() == null) {
            adView.getPriceView().setVisibility(View.INVISIBLE);
        } else {
            adView.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
        }

        if (nativeAd.getStore() == null) {
            adView.getStoreView().setVisibility(View.INVISIBLE);
        } else {
            adView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
        }

        if (nativeAd.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) adView.getStarRatingView())
                    .setRating(nativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getAdvertiser() == null) {
            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }

        // This method tells the Google Mobile Ads SDK that you have finished populating your
        // native ad view with this native ad.
        adView.setNativeAd(nativeAd);

        // Get the video controller for the ad. One will always be provided, even if the ad doesn't
        // have a video asset.
        VideoController vc = nativeAd.getMediaContent().getVideoController();

        // Updates the UI to say whether or not this ad has a video asset.
        if (vc.hasVideoContent()) {

            // Create a new VideoLifecycleCallbacks object and pass it to the VideoController. The
            // VideoController will call methods on this object when events occur in the video
            // lifecycle.
            vc.setVideoLifecycleCallbacks(new VideoController.VideoLifecycleCallbacks() {
                @Override
                public void onVideoEnd() {
                    // Publishers should allow native ads to complete video playback before
                    // refreshing or replacing them with another ad in the same UI location.

                    super.onVideoEnd();
                }
            });
        } else {

        }
    }

    public static boolean checkfbAdAppopen() {
        if (prf.getString("SUBSCRIBED").equals("TRUE")) {
            return false;
        }
        if(prf.getString(TAG_ADMOB_APPOPEN_FREQUENCY) != "") {
            if (Integer.parseInt(prf.getString(TAG_ADMOB_APPOPEN_FREQUENCY)) > 0 && sAppopenCounter % Integer.parseInt(prf.getString(TAG_ADMOB_APPOPEN_FREQUENCY)) == 0) {
                sAppopenCounter++;
                return true;
            } else {
                sAppopenCounter++;
                return false;
            }
        } else {
            if (AdController23rd.ADMOB_APPOPEN_FREQUENCY > 0 && sAppopenCounter % AdController23rd.ADMOB_APPOPEN_FREQUENCY == 0) {
                sAppopenCounter++;
                return true;
            } else {
                sAppopenCounter++;
                return false;
            }
        }
    }

    public static boolean checkfbAd() {
        if (prf.getString("SUBSCRIBED").equals("TRUE")) {
            return false;
        }
        if(prf.getInt(Splash23rdActivity.ADMOB_INTERSTITIAL_FREQUENCY) != 0) {
            if (prf.getInt(Splash23rdActivity.ADMOB_INTERSTITIAL_FREQUENCY) > 0 && sInterstitialCounter %prf.getInt(Splash23rdActivity.ADMOB_INTERSTITIAL_FREQUENCY) == 0) {
                sInterstitialCounter++;
                return true;
            } else {
                sInterstitialCounter++;
                return false;
            }
        } else {
            if (AdController23rd.ADMOB_INTERSTITIAL_FREQUENCY > 0 && sInterstitialCounter % AdController23rd.ADMOB_INTERSTITIAL_FREQUENCY == 0) {
                sInterstitialCounter++;
                return true;
            } else {
                sInterstitialCounter++;
                return false;
            }
        }
    }

    public static void checkfbAdMinusOne() {
//        sInterstitialCounter--;
    }

    public static boolean checkwhtsp() {
        try {
            if (prf.getString("SUBSCRIBED").equals("TRUE")) {
                return false;
            }
            if (prf.getInt(TAG_WHATSAPP_COUNTER) == 0) {
                prf.setInt(TAG_WHATSAPP_COUNTER, 1);
                return false;
            }
            if (prf.getString(TAG_WHATSAPP_SHARE_FREQUENCY) != "") {
                if (Integer.parseInt(prf.getString(TAG_WHATSAPP_SHARE_FREQUENCY)) > 0 && prf.getInt(TAG_WHATSAPP_COUNTER) % Integer.parseInt(prf.getString(TAG_WHATSAPP_SHARE_FREQUENCY)) == 0) {
//            sWhatsappCounter++;
                    return true;
                } else {
                    prf.setInt(TAG_WHATSAPP_COUNTER, prf.getInt(TAG_WHATSAPP_COUNTER) + 1);
                    return false;
                }
            } else {
                if (WHATSAPP_SHARE_FREQUENCY > 0 && prf.getInt(TAG_WHATSAPP_COUNTER) % WHATSAPP_SHARE_FREQUENCY == 0) {
//            sWhatsappCounter++;
                    return true;
                } else {
                    prf.setInt(TAG_WHATSAPP_COUNTER, prf.getInt(TAG_WHATSAPP_COUNTER) + 1);
                    return false;
                }
            }
        } catch (Exception e) {
            //FirebaseCrashlytics.getInstance().recordException(e);
                                    e.printStackTrace();

            return false;
        }
    }

    public static boolean checkpro() {
        if (prf.getString("SUBSCRIBED").equals("TRUE")) {
            return false;
        }
        if (prf.getBoolean(TAG_FIRST_TRIAL)) {
            prf.setBoolean(TAG_FIRST_TRIAL, false);
            return false;
        }
        if (prf.getInt(TAG_PRO_COUNTER) == 0) {
            prf.setInt(TAG_PRO_COUNTER, 1);
            return false;
        }
        if (prf.getString(TAG_PRO) != "") {
            if (Integer.parseInt(prf.getString(TAG_PRO)) > 0 && prf.getInt(TAG_PRO_COUNTER) > Integer.parseInt(prf.getString(TAG_PRO))) {
//            sProCounter++;
                return true;
            } else {
                prf.setInt(TAG_PRO_COUNTER, prf.getInt(TAG_PRO_COUNTER) + 1);
                return false;
            }
        } else {
            if (PRO_SHARE_FREQUENCY > 0 && prf.getInt(TAG_PRO_COUNTER) > PRO_SHARE_FREQUENCY) {
//            sProCounter++;
                return true;
            } else {
                prf.setInt(TAG_PRO_COUNTER, prf.getInt(TAG_PRO_COUNTER) + 1);
                return false;
            }
        }
    }

    public static boolean checktrial() {
        if (prf.getString("SUBSCRIBED").equals("TRUE")) {
            return false;
        }
        if (prf.getString(TAG_TRIAL) == null) {
            prf.setString(TAG_TRIAL, "1");
            return true;
        }
        if (prf.getString(TAG_TRIAL).equals("1")) {
            return true;
        } else {
            return false;
        }
    }

}
