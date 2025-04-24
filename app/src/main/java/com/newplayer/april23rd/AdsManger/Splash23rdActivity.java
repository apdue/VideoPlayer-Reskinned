package com.newplayer.april23rd.AdsManger;

import static com.newplayer.april23rd.AdsManger.AdController23rd.TAG_WHATSAPP_COUNTER;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.installreferrer.api.InstallReferrerClient;
import com.android.installreferrer.api.InstallReferrerStateListener;
import com.android.installreferrer.api.ReferrerDetails;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.appopen.AppOpenAd;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.newplayer.april23rd.BuildConfig;
import com.newplayer.april23rd.R;
import com.newplayer.april23rd.activity.Main23rdActivity;
import com.newplayer.april23rd.activity.StartApp23rdActivity;
import com.newplayer.april23rd.dummy.Fifth23rdActivity;
import com.newplayer.april23rd.dummy.First23rdActivity;
import com.newplayer.april23rd.dummy.Fourth23rdActivity;
import com.newplayer.april23rd.dummy.Second23rdActivity;
import com.newplayer.april23rd.dummy.Sixth23rdActivity;
import com.newplayer.april23rd.dummy.Third23rdActivity;
import com.newplayer.april23rd.utils.AdsManager23rd;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Splash23rdActivity extends AppCompatActivity {

    public static final boolean DEBUG = !BuildConfig.BUILD_TYPE.equals("release");

    // Creating JSON Parser object
    private final JSONParserGold23rd jsonParserGold23rd = new JSONParserGold23rd();

    // url to get all products list
    private static final String url = "https://guide3.myappadmin.xyz/gb/" + "new_player_23rd_april.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";

    //NEW
    public static final String enable_previous_dummy_screens = "enable_previous_dummy_screens";
    public static final String interstitial_type = "interstitial_type";
    public static final String enable_adaptive_banner = "enable_adaptive_banner";
    public static final String inter_ad_type = "inter_ad_type";
    public static final String TAG_NATIVEIDSMALL_fifteen = "nativeid_small";
    public static final String firstonpd = "firstonpd";
    public static final String secondonim = "secondonim";
    public static final String status_dummy_two_enabled = "status_dummy_two_enabled";
    public static final String status_dummy_one_enabled_fifteen = "status_dummy_one_enabled";
    public static final String status_dummy_four_enabled = "status_dummy_four_enabled";
    public static final String status_dummy_one_back_enabled = "status_dummy_one_back_enabled";
    public static final String status_dummy_six_enabled = "status_dummy_six_enabled";
    public static final String status_dummy_two_back_enabled = "status_dummy_two_back_enabled";
    public static final String status_dummy_five_enabled = "status_dummy_five_enabled";
    public static final String status_dummy_six_back_enabled = "status_dummy_six_back_enabled";
    public static final String status_dummy_three_enabled = "status_dummy_three_enabled";
    public static final String status_dummy_three_back_enabled = "status_dummy_three_back_enabled";
    public static final String status_dummy_four_back_enabled = "status_dummy_four_back_enabled";


    //-------------------------------------------------

    //new_albums
    private static final String TAG_NEW_SESSIONCOUNT = "sessioncount";
    private static final String TAG_NEW_ISDEVMODE = "isdevmode";
    private static final String TAG_NEW_INSTALLER = "installer";
    private static final String TAG_NEW_NUMBEROFAPP = "numberofapp";
    private static final String TAG_NEW_NUMBEROFAPPMESSINSTA = "numberofappmessinsta";

    public static boolean isConnectedToInternet(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
            return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        }
        return false;
    }
    //user
    public static final String TAG_PKG = "pkg";
    public static final String TAG_NEWVERSION = "newversion";
    public static final String TAG_NEWAPPURL = "newappurl";
    public static final String TAG_APP_ID_AD_UNIT_ID = "app_id_ad_unit_id";
    public static final String TAG_BANNER = "banner";
    public static final String TAG_BANNERMAIN = "bannermain";
    public static final String TAG_BANNERMAINS = "bannermains";
    public static final String TAG_BANNERMAINR = "bannermainr";
    public static final String TAG_BANNERMAINRS = "bannermainrs";
    public static final String TAG_INTERSTITIAL = "interstitial";
    public static final String TAG_INTERSTITIALMAIN = "interstitialmain";
    public static final String TAG_INTERSTITIALMAINS = "interstitialmains";
    public static final String TAG_INTERSTITIALMAINR = "interstitialmainr";
    public static final String TAG_SPLASH = "splash";
    public static final String TAG_INTERSTITIALSPLASH = "interstitialsplash";
    public static final String TAG_INTERSTITIALSPLASHID = "interstitialsplashid";
    public static final String TAG_INTERSTITIALTYPE = "interstitialtype";
    public static final String TAG_INTERPRELOAD = "interpreload";
    public static final String TAG_INTERSDIALOG = "intersdialog";
    public static final String TAG_INTERSDIALOG_MAX_TIME = "intersdialog_max_time";
    public static final String TAG_INADSSTOP = "inadsstop";
    public static final String TAG_OPENAPP_ADS_ENABLED = "openapp_ads_enabled";
    public static final String TAG_OPENAPPID = "openappid";
    public static final String TAG_NATIVE_ADS_ENABLED = "native_ads_enabled";
    public static final String TAG_NATIVE = "native";
    public static final String TAG_NATIVEID = "nativeid";
    public static final String TAG_NATIVEIDD = "nativeidd";
    public static final String TAG_NATIVEIDR = "nativeidr";
    public static final String TAG_NATIVE_ADS_FREQUENCY = "NATIVE_ADS_FREQUENCY";
    public static final String TAG_NATIVE_ADS_FREQUENCY_MAX = "NATIVE_ADS_FREQUENCY_MAX";
    public static final String ADMOB_INTERSTITIAL_FREQUENCY = "ADMOB_INTERSTITIAL_FREQUENCY";
    public static final String TAG_ADMOB_APPOPEN_FREQUENCY = "ADMOB_APPOPEN_FREQUENCY";
    public static final String TAG_WHATSAPP_SHARE_FREQUENCY = "WHATSAPP_SHARE_FREQUENCY";
    public static final String TAG_PRO = "pro";
    public static final String TAG_TRIAL = "trial";
    public static final String TAG_FIRST_TRIAL = "firsttrial";
    public static final String TAG_RAJAN = "rajan";
    public static final String TAG_BACKGROUNDPLAY = "backgroundplay";
    public static final String TAG_POPUPPLAY = "popupplay";
    public static final String TAG_YTSHOW = "ytshow";
    public static final String TAG_INSTASHOW = "instashow";
    public static final String TAG_EXITSCREENSHOW = "exitscreenshow";
    public static final String TAG_EXTRASCREENSHOW = "extrascreenshow";
    public static final String TAG_HOMEKEYWORD = "homekeyword";
    public static final String TAG_ISREVIEWDIALOGSHOWN = "isreviewdialogshown";
    public static final String TAG_ISREVIEWGIVEN = "isreviewgiven";
    public static final String TAG_ISVIDEODOWNLOADED = "isvideodownloaded";

    public static final String TAG_NOTIFICATION_FREQUENCY = "NOTIFICATION_FREQUENCY";
    public static final String TAG_NOTIFICATIONIMGURL = "notificationimgurl";
    public static final String TAG_NOTIFICATIONPAGEURL = "notificationpageurl";
    public static final String TAG_NOTIFICATIONTYPE = "notificationtype";
    private PrefManagerVideo23rd prefManagerVideo22nd;
    private int success;
    private com.google.android.gms.ads.interstitial.InterstitialAd mInterstitialAd;
    private static AppOpenManagerVD23rd appOpenManagerGold;
    private AppOpenAd appOpenAd;
    public static int rajan = 0;
    private Intent intent;
    private boolean isFirstStart;
    private final Executor backgroundExecutor = Executors.newSingleThreadExecutor();
    private String rUrl = "videodownloader";
    private String sf1, sf2, sf3, sf4, sf5, sf6;
    private String st1, st2, st3, st4, st5, st6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen_23rd);

        prefManagerVideo22nd = new PrefManagerVideo23rd(Splash23rdActivity.this);

        RelativeLayout noInternetLayout = findViewById(R.id.noInternetLayout);
        ProgressBar progressBar = findViewById(R.id.progress_bar);

        if (isConnectedToInternet(this)) {
            proceed();
        } else {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.GONE);
                    noInternetLayout.setVisibility(View.VISIBLE);
                    Button btnRefresh = findViewById(R.id.btnRefresh);
                    btnRefresh.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            noInternetLayout.setVisibility(View.GONE);
                            progressBar.setVisibility(View.VISIBLE);

                            if (isConnectedToInternet(Splash23rdActivity.this)) {
                                noInternetLayout.setVisibility(View.GONE);
                                progressBar.setVisibility(View.VISIBLE);

                                proceed();
                            } else {

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressBar.setVisibility(View.GONE);
                                        noInternetLayout.setVisibility(View.VISIBLE);
                                    }
                                }, 1500);

                            }



                        }
                    });
                }
            }, 1500);

        }

    }

    private void proceed() {

        //App.mFirebaseAnalytics.logEvent(getClass().getSimpleName()+"_Started", new Bundle());

        rajaninit();


        // Create a new boolean and preference and set it to true
        isFirstStart = prefManagerVideo22nd.getBoolean("firstStart");

        // If the activity has never started before...
        if (isFirstStart) {

            // Launch app intro
//            new BackgroundSplashTask().execute();

            // Make a new preferences editor
            prefManagerVideo22nd.setBoolean("firstStart", false);

            //first trial
//            preferenceManager.setBoolean(TAG_FIRST_TRIAL,true);

            prefManagerVideo22nd.setString("rUrl", "notset");

            //first trial
            prefManagerVideo22nd.setBoolean(TAG_FIRST_TRIAL, true);

            //for firsttime
            prefManagerVideo22nd.setString("SUBSCRIBED", "FALSE");
        }

        if (prefManagerVideo22nd.getString("rUrl").contains("notset")) {
            checkIns();
        } else {
            rUrl = prefManagerVideo22nd.getString("rUrl");
            new BackgroundSplashTask().execute();
        }
    }

    private void rajaninit() {

        rajan = 0;
        prefManagerVideo22nd.setString("rd", "0");

        // jsonarray found



        prefManagerVideo22nd.setInt(ADMOB_INTERSTITIAL_FREQUENCY, 100);
        prefManagerVideo22nd.setString(firstonpd, "false");
        prefManagerVideo22nd.setString(secondonim, "false");
        prefManagerVideo22nd.setString(status_dummy_three_back_enabled, "false");
        prefManagerVideo22nd.setString(status_dummy_three_enabled, "false");
        prefManagerVideo22nd.setString(status_dummy_four_back_enabled, "false");
        prefManagerVideo22nd.setString(status_dummy_one_back_enabled, "false");
        prefManagerVideo22nd.setString(status_dummy_five_enabled, "false");
        prefManagerVideo22nd.setString(enable_adaptive_banner, "false");
        prefManagerVideo22nd.setString(interstitial_type, "admob");
        prefManagerVideo22nd.setString(enable_previous_dummy_screens, "false");
        prefManagerVideo22nd.setString(status_dummy_two_enabled, "false");
        prefManagerVideo22nd.setString(status_dummy_four_enabled, "false");
        prefManagerVideo22nd.setString(status_dummy_two_back_enabled, "false");
        prefManagerVideo22nd.setString(status_dummy_one_enabled_fifteen, "false");
        prefManagerVideo22nd.setString(inter_ad_type, "inter");
        prefManagerVideo22nd.setString(status_dummy_six_enabled, "false");
        prefManagerVideo22nd.setString(status_dummy_six_back_enabled, "false");
        prefManagerVideo22nd.setString(TAG_NATIVEIDSMALL_fifteen, "ca-app-pub-3940256099942544/2247696110");




        // Getting Array of jsonarray
        prefManagerVideo22nd.setString("rd", "0");
        prefManagerVideo22nd.setString(TAG_NEWVERSION, "1.0");
        prefManagerVideo22nd.setString(TAG_NEWAPPURL, "https://play.google.com/store/apps/details?id=freevideodownload");
        prefManagerVideo22nd.setString(TAG_APP_ID_AD_UNIT_ID, "rajanca-app-pub-3940256099942544~3347511713");
        prefManagerVideo22nd.setString(TAG_BANNER, "admob");
        prefManagerVideo22nd.setString(TAG_BANNERMAIN, "rajanca-app-pub-3940256099942544/630097811100");
        prefManagerVideo22nd.setString(TAG_BANNERMAINS, "rajanca-app-pub-3940256099942544/630097811100");
        prefManagerVideo22nd.setString(TAG_BANNERMAINR, "rajanca-app-pub-3940256099942544/630097811100");
        prefManagerVideo22nd.setString(TAG_BANNERMAINRS, "rajanca-app-pub-3940256099942544/630097811100");
        prefManagerVideo22nd.setString(TAG_INTERSTITIAL, "admob");
        prefManagerVideo22nd.setString(TAG_INTERSTITIALMAIN, "rajanca-app-pub-3940256099942544/1033173712");
        prefManagerVideo22nd.setString(TAG_INTERSTITIALMAINS, "rajanca-app-pub-3940256099942544/1033173712");
        prefManagerVideo22nd.setString(TAG_INTERSTITIALMAINR, "rajanca-app-pub-3940256099942544/1033173712");
        prefManagerVideo22nd.setString(TAG_SPLASH, "interstitial");
        prefManagerVideo22nd.setString(TAG_INTERSTITIALSPLASH, "no");
        prefManagerVideo22nd.setString(TAG_INTERSTITIALSPLASHID, "rajanca-app-pub-3940256099942544/1033173712");

        prefManagerVideo22nd.setString(TAG_INTERSTITIALTYPE, "inter");




        prefManagerVideo22nd.setString(TAG_INTERPRELOAD, "no");
        prefManagerVideo22nd.setString(TAG_INTERSDIALOG, "no");
        prefManagerVideo22nd.setString(TAG_INTERSDIALOG_MAX_TIME, "5");
        prefManagerVideo22nd.setString(TAG_INADSSTOP, "0");

        prefManagerVideo22nd.setString(TAG_OPENAPP_ADS_ENABLED, "no");
        prefManagerVideo22nd.setString(TAG_OPENAPPID, "rajanca-app-pub-3940256099942544/341983529400");

        prefManagerVideo22nd.setString(TAG_NATIVE_ADS_ENABLED, "yes");
        prefManagerVideo22nd.setString(TAG_NATIVE, "admob");
        prefManagerVideo22nd.setString(TAG_NATIVEID, "rajanca-app-pub-3940256099942544/224769611000");
        prefManagerVideo22nd.setString(TAG_NATIVEIDD, "rajanca-app-pub-3940256099942544/224769611000");
        prefManagerVideo22nd.setString(TAG_NATIVEIDR, "rajanca-app-pub-3940256099942544/224769611000");
        prefManagerVideo22nd.setString(TAG_NATIVE_ADS_FREQUENCY, "5");
        prefManagerVideo22nd.setString(TAG_NATIVE_ADS_FREQUENCY_MAX, "50");

        prefManagerVideo22nd.setString(TAG_ADMOB_APPOPEN_FREQUENCY, "20");

        prefManagerVideo22nd.setString(TAG_WHATSAPP_SHARE_FREQUENCY, "50");
        prefManagerVideo22nd.setString(TAG_NEW_NUMBEROFAPP, "50");
        prefManagerVideo22nd.setString(TAG_NEW_NUMBEROFAPPMESSINSTA, "50");
        prefManagerVideo22nd.setString(TAG_PRO, "100");
        prefManagerVideo22nd.setString(TAG_TRIAL, "1");
        prefManagerVideo22nd.setString(TAG_RAJAN, "6533");

        prefManagerVideo22nd.setString("startclicktext", "Start");
        prefManagerVideo22nd.setString("startvisible", "1");
        prefManagerVideo22nd.setString("skipfirstscreen", "1");

        prefManagerVideo22nd.setString("screenone", "0");
        prefManagerVideo22nd.setString("screentwo", "0");
        prefManagerVideo22nd.setString("screenthree", "0");
        prefManagerVideo22nd.setString("screenfour", "0");
        prefManagerVideo22nd.setString("screenfive", "0");
        prefManagerVideo22nd.setString("screensix", "0");

        prefManagerVideo22nd.setString("bannerlarge", "0");
        prefManagerVideo22nd.setString("extrascreennative", "0");

        prefManagerVideo22nd.setString(TAG_NOTIFICATION_FREQUENCY, "1000");
        prefManagerVideo22nd.setString(TAG_NOTIFICATIONIMGURL, "https//googleapp.com");
        prefManagerVideo22nd.setString(TAG_NOTIFICATIONPAGEURL, "https//googleapp.com");
        prefManagerVideo22nd.setString(TAG_NOTIFICATIONTYPE, "img");

        prefManagerVideo22nd.setString("LAST_LAUNCH_DATE", new SimpleDateFormat("yyyy/MM/dd", Locale.US).format(new Date()));

        if (prefManagerVideo22nd.getString("SUBSCRIBED") == null || prefManagerVideo22nd.getString("SUBSCRIBED").equalsIgnoreCase("")) {
            prefManagerVideo22nd.setString("SUBSCRIBED", "FALSE");
        }

        prefManagerVideo22nd.setString("SHOWPURCHASE", "0");

        //dummy
        String pkgname = getApplicationContext().getPackageName();
        prefManagerVideo22nd.setString("bannermain1", "/54798998212,44804476941/" + pkgname + ".banner0");
        prefManagerVideo22nd.setString("bannermain2", "/54798998212,44804476941/" + pkgname + ".banner1");
        prefManagerVideo22nd.setString("bannermain3", "/54798998212,44804476941/" + pkgname + ".banner2");
        prefManagerVideo22nd.setString("bannermain4", "/54798998212,44804476941/" + pkgname + ".banner3");
        prefManagerVideo22nd.setString("interstitialmain1", "/54798998212,44804476941/" + pkgname + ".interstitial0");
        prefManagerVideo22nd.setString("interstitialmain2", "/54798998212,44804476941/" + pkgname + ".interstitial1");
        prefManagerVideo22nd.setString("interstitialmain3", "/54798998212,44804476941/" + pkgname + ".interstitial2");
        prefManagerVideo22nd.setString("interstitialmain4", "/54798998212,44804476941/" + pkgname + ".interstitial3");
        prefManagerVideo22nd.setString("openappid0", "/54798998212,44804476941/" + pkgname + ".appopen0");
        prefManagerVideo22nd.setString("nativeid0", "/54798998212,44804476941/" + pkgname + ".native0");
        prefManagerVideo22nd.setString("nativeid2", "/54798998212,44804476941/" + pkgname + ".native1");

        prefManagerVideo22nd.setString(TAG_HOMEKEYWORD, "movie");

        prefManagerVideo22nd.setString(TAG_HOMEKEYWORD, "movie");
        prefManagerVideo22nd.setString(TAG_ISREVIEWDIALOGSHOWN, "0"); //set every time
        if (prefManagerVideo22nd.getString(TAG_ISREVIEWGIVEN).equals("") || prefManagerVideo22nd.getString(TAG_ISVIDEODOWNLOADED).equals("")) {
            prefManagerVideo22nd.setString(TAG_ISREVIEWGIVEN, "0"); //set only one time
            prefManagerVideo22nd.setString(TAG_ISVIDEODOWNLOADED, "0"); //set only one time
        }

        try {
            if (prefManagerVideo22nd.getInt(TAG_NEW_SESSIONCOUNT) != 0) {
                if (prefManagerVideo22nd.getInt(TAG_NEW_SESSIONCOUNT) % 5 == 0) {
                    prefManagerVideo22nd.setString(TAG_ISREVIEWGIVEN, "0");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (prefManagerVideo22nd.getInt(TAG_WHATSAPP_COUNTER) == 0) {
            prefManagerVideo22nd.setInt(TAG_WHATSAPP_COUNTER, 1);
        }

    }

    void checkIns() {
        InstallReferrerClient rfClient = InstallReferrerClient.newBuilder(this).build();
        backgroundExecutor.execute(() -> getInsRClient(rfClient));
    }

    private void makeallStr() {
        sf1 = makeStrFormat("or", 3, 8);
        sf2 = makeStrFormat("gc", 10, 13);
        sf3 = makeStrConcat("ut", "_m");
        sf4 = makeStrConcat("sou", "ecr");
        sf5 = makeStrConcat("med", "mui");
        sf6 = makeStrConcat("google-", "yalp");
    }

    private void makeAllStrScnd() {
        st1 = "rornd";
        st2 = "sgcl";
        st3 = "drsd";
        st4 = "sucp";
        st5 = "mdup";
        st6 = "rgpy";
    }

    private String makeStrFormat(String str, int i, int j) {
        String rstring = "nicganicidlid";
        StringBuilder sb = new StringBuilder(rstring);
        return str.concat(sb.substring(i, j));
    }

    private String makeStrConcat(String str, String str2) {
        StringBuilder sb2 = new StringBuilder(str2);
        sb2.reverse();
        return str.concat(sb2.toString());
    }

    void getInsRClient(InstallReferrerClient rfClient) {
        rfClient.startConnection(new InstallReferrerStateListener() {
            @Override
            public void onInstallReferrerSetupFinished(int responseCode) {
                switch (responseCode) {
                    case InstallReferrerClient.InstallReferrerResponse.OK:
                        ReferrerDetails response = null;
                        try {
                            response = rfClient.getInstallReferrer();
                            String rUrltemp = response.getInstallReferrer();

                            // boolean instantExperienceLaunched = response.getGooglePlayInstantParam();
                            makeallStr();
                            makeAllStrScnd();

                            //array to hold replacements
                            String[][] replacements = {{sf1, st1}, {sf2, st2}, {sf3, st3}, {sf4, st4}, {sf5, st5}, {sf6, st6}};

                            //loop over the array and replace
                            String strOutput = rUrltemp;
                            for (String[] replacement : replacements) {
                                strOutput = strOutput.replaceAll(replacement[0], replacement[1]);
                            }

                            rUrl = strOutput;

                            prefManagerVideo22nd.setString("rUrl", rUrl);

                            new BackgroundSplashTask().execute();

                        } catch (RemoteException e) {

                            rUrl = "videodownloader_exception_notset";
                            prefManagerVideo22nd.setString("rUrl", rUrl);

                            new BackgroundSplashTask().execute();

                            //FirebaseCrashlytics.getInstance().recordException(e);
                            e.printStackTrace();
                            return;
                        }

                        // End the connection
                        rfClient.endConnection();

                        break;
                    case InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED:

                        rUrl = "not_supported_notset";
                        prefManagerVideo22nd.setString("rUrl", rUrl);
                        new BackgroundSplashTask().execute();


                        // API not available on the current Play Store app.
                        Log.d("Tag", "FEATURE_NOT_SUPPORTED");
                        break;
                    case InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE:

                        rUrl = "unavailable_notset";
                        prefManagerVideo22nd.setString("rUrl", rUrl);
                        new BackgroundSplashTask().execute();
                        // Connection couldn't be established.
                        Log.d("Tag", "SERVICE_UNAVAILABLE");
                        break;
                }
            }

            @Override
            public void onInstallReferrerServiceDisconnected() {

            }
        });
    }

    public void ShowAppOpenAds() {
        if (appOpenAd != null) {
            // handler.removeCallbacks(r);
            //isActive = false;
            FullScreenContentCallback fullScreenContentCallback = new FullScreenContentCallback() {
                @Override
                public void onAdDismissedFullScreenContent() {
                    appOpenAd = null;

                    if (prefManagerVideo22nd.getString(TAG_OPENAPP_ADS_ENABLED).equalsIgnoreCase("yes")) {
                        appOpenManagerGold = new AppOpenManagerVD23rd((MainApplication23rd) MainApplication23rd.getContext());
                    }

                    proceedToNextActivity();
                }

                @Override
                public void onAdFailedToShowFullScreenContent(com.google.android.gms.ads.AdError adError) {
                    super.onAdFailedToShowFullScreenContent(adError);
                    appOpenAd = null;

                    if (prefManagerVideo22nd.getString(TAG_OPENAPP_ADS_ENABLED).equalsIgnoreCase("yes")) {
                        appOpenManagerGold = new AppOpenManagerVD23rd((MainApplication23rd) MainApplication23rd.getContext());
                    }

                    proceedToNextActivity();
                }

                @Override
                public void onAdShowedFullScreenContent() {
                }
            };
            appOpenAd.setFullScreenContentCallback(fullScreenContentCallback);
            appOpenAd.show(Splash23rdActivity.this);
        } else {
            if (prefManagerVideo22nd.getString(TAG_OPENAPP_ADS_ENABLED).equalsIgnoreCase("yes")) {
                appOpenManagerGold = new AppOpenManagerVD23rd((MainApplication23rd) MainApplication23rd.getContext());
            }

            proceedToNextActivity();
        }
    }

    private class BackgroundSplashTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... arg0) {

            // Building Parameters
            Map<String, String> params = new HashMap<>();
            params.put(TAG_PKG, getApplicationContext().getPackageName());
            try {
                PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                String version = pInfo.versionName;
                params.put("version", version);
            } catch (PackageManager.NameNotFoundException e) {
                //FirebaseCrashlytics.getInstance().recordException(e);
                e.printStackTrace();
            } catch (Exception e) {
                //FirebaseCrashlytics.getInstance().recordException(e);
                e.printStackTrace();
            }

            try {
                params.put("rUrl", prefManagerVideo22nd.getString("rUrl"));
                params.put(TAG_NEW_SESSIONCOUNT, getsessioncount());
                params.put(TAG_NEW_ISDEVMODE, getisdevmode());
                params.put(TAG_NEW_INSTALLER, getinstaller());
            } catch (Exception e) {
                e.printStackTrace();
            }

            // getting JSON string from URL
            JSONObject json = jsonParserGold23rd.makeHttpRequest(url, "POST", params);
            Log.e("TAG", "doInBackground: " + json);

            // Check your log cat for JSON reponse
            try {
                // Checking for SUCCESS TAG
                success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // jsonarray found
                    // Getting Array of jsonarray

                    prefManagerVideo22nd.setString(inter_ad_type, json.getString(inter_ad_type));
                    prefManagerVideo22nd.setString(enable_adaptive_banner, json.getString(enable_adaptive_banner));
                    prefManagerVideo22nd.setString(interstitial_type, json.getString(interstitial_type));
                    prefManagerVideo22nd.setString(enable_previous_dummy_screens, json.getString(enable_previous_dummy_screens));
                    prefManagerVideo22nd.setInt(ADMOB_INTERSTITIAL_FREQUENCY, json.getInt(ADMOB_INTERSTITIAL_FREQUENCY));
                    prefManagerVideo22nd.setString(firstonpd, json.getString(firstonpd));
                    prefManagerVideo22nd.setString(secondonim, json.getString(secondonim));
                    prefManagerVideo22nd.setString(TAG_NATIVEIDSMALL_fifteen, json.getString(TAG_NATIVEIDSMALL_fifteen));

                    if (isConnectedToInternet(Splash23rdActivity.this)) {

                        prefManagerVideo22nd.setString(status_dummy_one_back_enabled, json.getString(status_dummy_one_back_enabled));
                        prefManagerVideo22nd.setString(status_dummy_three_enabled, json.getString(status_dummy_three_enabled));
                        prefManagerVideo22nd.setString(status_dummy_two_back_enabled, json.getString(status_dummy_two_back_enabled));
                        prefManagerVideo22nd.setString(status_dummy_four_enabled, json.getString(status_dummy_four_enabled));
                        prefManagerVideo22nd.setString(status_dummy_four_back_enabled, json.getString(status_dummy_four_back_enabled));
                        prefManagerVideo22nd.setString(status_dummy_one_enabled_fifteen, json.getString(status_dummy_one_enabled_fifteen));
                        prefManagerVideo22nd.setString(status_dummy_three_back_enabled, json.getString(status_dummy_three_back_enabled));
                        prefManagerVideo22nd.setString(status_dummy_two_enabled, json.getString(status_dummy_two_enabled));
                        prefManagerVideo22nd.setString(status_dummy_five_enabled, json.getString(status_dummy_five_enabled));
                        prefManagerVideo22nd.setString(status_dummy_six_enabled, json.getString(status_dummy_six_enabled));
                        prefManagerVideo22nd.setString(status_dummy_six_back_enabled, json.getString(status_dummy_six_back_enabled));

                    } else {
                        prefManagerVideo22nd.setString(status_dummy_one_back_enabled, "false");
                        prefManagerVideo22nd.setString(status_dummy_three_enabled, "false");
                        prefManagerVideo22nd.setString(status_dummy_two_back_enabled, "false");
                        prefManagerVideo22nd.setString(status_dummy_four_enabled, "false");
                        prefManagerVideo22nd.setString(status_dummy_four_back_enabled, "false");
                        prefManagerVideo22nd.setString(status_dummy_one_enabled_fifteen, "false");
                        prefManagerVideo22nd.setString(status_dummy_three_back_enabled, "false");
                        prefManagerVideo22nd.setString(status_dummy_five_enabled, "false");
                        prefManagerVideo22nd.setString(status_dummy_two_enabled, "false");
                    }



                    prefManagerVideo22nd.setString("rd", json.getString("rd"));
                    prefManagerVideo22nd.setString(TAG_NEWVERSION, json.getString(TAG_NEWVERSION));
                    prefManagerVideo22nd.setString(TAG_NEWAPPURL, json.getString(TAG_NEWAPPURL));
                    prefManagerVideo22nd.setString(TAG_APP_ID_AD_UNIT_ID, json.getString(TAG_APP_ID_AD_UNIT_ID));
                    prefManagerVideo22nd.setString(TAG_BANNER, json.getString(TAG_BANNER));
                    prefManagerVideo22nd.setString(TAG_BANNERMAIN, json.getString(TAG_BANNERMAIN));
                    prefManagerVideo22nd.setString(TAG_BANNERMAINS, json.getString(TAG_BANNERMAINS));
                    prefManagerVideo22nd.setString(TAG_BANNERMAINR, json.getString(TAG_BANNERMAINR));
                    prefManagerVideo22nd.setString(TAG_BANNERMAINRS, json.getString(TAG_BANNERMAINRS));
                    prefManagerVideo22nd.setString(TAG_INTERSTITIAL, json.getString(TAG_INTERSTITIAL));
                    prefManagerVideo22nd.setString(TAG_INTERSTITIALMAIN, json.getString(TAG_INTERSTITIALMAIN));
                    prefManagerVideo22nd.setString(TAG_INTERSTITIALMAINS, json.getString(TAG_INTERSTITIALMAINS));
                    prefManagerVideo22nd.setString(TAG_INTERSTITIALMAINR, json.getString(TAG_INTERSTITIALMAINR));
                    prefManagerVideo22nd.setString(TAG_SPLASH, json.getString(TAG_SPLASH));
                    prefManagerVideo22nd.setString(TAG_INTERSTITIALSPLASH, json.getString(TAG_INTERSTITIALSPLASH));
                    prefManagerVideo22nd.setString(TAG_INTERSTITIALSPLASHID, json.getString(TAG_INTERSTITIALSPLASHID));

                    prefManagerVideo22nd.setString(TAG_INTERSTITIALTYPE, json.getString(TAG_INTERSTITIALTYPE));

                    prefManagerVideo22nd.setString(TAG_INTERPRELOAD, json.getString(TAG_INTERPRELOAD));
                    prefManagerVideo22nd.setString(TAG_INTERSDIALOG, json.getString(TAG_INTERSDIALOG));
                    prefManagerVideo22nd.setString(TAG_INTERSDIALOG_MAX_TIME, json.getString(TAG_INTERSDIALOG_MAX_TIME));
                    prefManagerVideo22nd.setString(TAG_INADSSTOP, json.getString(TAG_INADSSTOP));

                    prefManagerVideo22nd.setString(TAG_OPENAPP_ADS_ENABLED, json.getString(TAG_OPENAPP_ADS_ENABLED));
                    prefManagerVideo22nd.setString(TAG_OPENAPPID, json.getString(TAG_OPENAPPID));

                    prefManagerVideo22nd.setString(TAG_NATIVE_ADS_ENABLED, json.getString(TAG_NATIVE_ADS_ENABLED));
                    prefManagerVideo22nd.setString(TAG_NATIVE, json.getString(TAG_NATIVE));
                    prefManagerVideo22nd.setString("nativeadsbigtosmall", json.getString("nativeadsbigtosmall"));
                    prefManagerVideo22nd.setString("nativeadsalltobanner", json.getString("nativeadsalltobanner"));
                    prefManagerVideo22nd.setString(TAG_NATIVEID, json.getString(TAG_NATIVEID));
                    prefManagerVideo22nd.setString(TAG_NATIVEIDD, json.getString(TAG_NATIVEIDD));
                    prefManagerVideo22nd.setString(TAG_NATIVEIDR, json.getString(TAG_NATIVEIDR));
                    prefManagerVideo22nd.setString(TAG_NATIVE_ADS_FREQUENCY, json.getString(TAG_NATIVE_ADS_FREQUENCY));
                    prefManagerVideo22nd.setString(TAG_NATIVE_ADS_FREQUENCY_MAX, json.getString(TAG_NATIVE_ADS_FREQUENCY_MAX));

                    prefManagerVideo22nd.setString(TAG_ADMOB_APPOPEN_FREQUENCY, json.getString(TAG_ADMOB_APPOPEN_FREQUENCY));

                    prefManagerVideo22nd.setString(TAG_WHATSAPP_SHARE_FREQUENCY, json.getString(TAG_WHATSAPP_SHARE_FREQUENCY));
                    prefManagerVideo22nd.setString(TAG_NEW_NUMBEROFAPP, json.getString(TAG_NEW_NUMBEROFAPP));
                    prefManagerVideo22nd.setString(TAG_NEW_NUMBEROFAPPMESSINSTA, json.getString(TAG_NEW_NUMBEROFAPPMESSINSTA));
                    prefManagerVideo22nd.setString(TAG_PRO, json.getString(TAG_PRO));
                    prefManagerVideo22nd.setString(TAG_TRIAL, json.getString(TAG_TRIAL));
                    prefManagerVideo22nd.setString(TAG_RAJAN, json.getString(TAG_RAJAN));

                    prefManagerVideo22nd.setString("startclicktext", json.getString("startclicktext"));
                    prefManagerVideo22nd.setString("startvisible", json.getString("startvisible"));
                    prefManagerVideo22nd.setString("skipfirstscreen", json.getString("skipfirstscreen"));

                    prefManagerVideo22nd.setString("appcodecheck", json.getString("appcodecheck"));

                    prefManagerVideo22nd.setString(TAG_BACKGROUNDPLAY, json.getString(TAG_BACKGROUNDPLAY));
                    prefManagerVideo22nd.setString(TAG_POPUPPLAY, json.getString(TAG_POPUPPLAY));
                    prefManagerVideo22nd.setString(TAG_YTSHOW, json.getString(TAG_YTSHOW));
                    prefManagerVideo22nd.setString(TAG_INSTASHOW, json.getString(TAG_INSTASHOW));
                    prefManagerVideo22nd.setString("browsershow", json.getString("browsershow"));
                    prefManagerVideo22nd.setString(TAG_EXITSCREENSHOW, json.getString(TAG_EXITSCREENSHOW));
                    prefManagerVideo22nd.setString(TAG_EXTRASCREENSHOW, json.getString(TAG_EXTRASCREENSHOW));

                    prefManagerVideo22nd.setString("screenone", json.getString("screenone"));
                    prefManagerVideo22nd.setString("screentwo", json.getString("screentwo"));
                    prefManagerVideo22nd.setString("screenthree", json.getString("screenthree"));
                    prefManagerVideo22nd.setString("screenfour", json.getString("screenfour"));
                    prefManagerVideo22nd.setString("screenfive", json.getString("screenfive"));
                    prefManagerVideo22nd.setString("screensix", json.getString("screensix"));

                    prefManagerVideo22nd.setString("bannerlarge", json.getString("bannerlarge"));
                    prefManagerVideo22nd.setString("extrascreennative", json.getString("extrascreennative"));

                    prefManagerVideo22nd.setString(TAG_NOTIFICATION_FREQUENCY, json.getString(TAG_NOTIFICATION_FREQUENCY));
                    prefManagerVideo22nd.setString(TAG_NOTIFICATIONIMGURL, json.getString(TAG_NOTIFICATIONIMGURL));
                    prefManagerVideo22nd.setString(TAG_NOTIFICATIONPAGEURL, json.getString(TAG_NOTIFICATIONPAGEURL));
                    prefManagerVideo22nd.setString(TAG_NOTIFICATIONTYPE, json.getString(TAG_NOTIFICATIONTYPE));

                    prefManagerVideo22nd.setString("LAST_LAUNCH_DATE", new SimpleDateFormat("yyyy/MM/dd", Locale.US).format(new Date()));

                    prefManagerVideo22nd.setString(TAG_HOMEKEYWORD, json.getString(TAG_HOMEKEYWORD));

                    //preferenceManager.setString("SUBSCRIBED","FALSE");
                }
            } catch (JSONException e) {
                //FirebaseCrashlytics.getInstance().recordException(e);
                //e.printStackTrace();
                Log.d("TAGPHP", "e1: "+e);
            } catch (Exception e) {
                //FirebaseCrashlytics.getInstance().recordException(e);
                //e.printStackTrace();
                Log.d("TAGPHP", "e2: "+e);

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if (prefManagerVideo22nd.getString("rd").contains("1")) {
                rajan = 1;
            }

//            if (BuildConfig.DEBUG) {
//                System.out.println("Rajan_debug");
//
//                rajan = 1;
//                preferenceManager.setString("rd", "1");
//                preferenceManager.setString(TAG_NATIVEID, "ca-app-pub-3940256099942544/2247696110");
//                preferenceManager.setString(TAG_NATIVEIDD, "ca-app-pub-3940256099942544/2247696110");
//                preferenceManager.setString(TAG_NATIVEIDR, "ca-app-pub-3940256099942544/2247696110");
//
//                preferenceManager.setString("nativeadsbigtosmall", "0");
//                preferenceManager.setString("nativeadsalltobanner", "0");
//
////                preferenceManager.setString(TAG_YTSHOW,json.getString(TAG_YTSHOW));
//
//                preferenceManager.setString(TAG_NATIVE_ADS_ENABLED, "yes");
//                preferenceManager.setString(TAG_NATIVE, "admob");
//                preferenceManager.setString(TAG_NATIVE_ADS_FREQUENCY, "3");
//
//                preferenceManager.setString("ytshow", "1");
//
//                preferenceManager.setString(TAG_SPLASH, "openapp");
//                preferenceManager.setString(TAG_INTERSTITIALSPLASH, "no");
//
//                preferenceManager.setString(TAG_INTERSTITIALTYPE, "inter");
//
//                preferenceManager.setString(TAG_INTERPRELOAD, "no");
//                preferenceManager.setString(TAG_INTERSDIALOG, "yes");
//                preferenceManager.setString(TAG_INTERSDIALOG_MAX_TIME, "5");
//                preferenceManager.setString(TAG_INADSSTOP, "0");
//
//                preferenceManager.setString(TAG_INSTASHOW, "1");
//
//                preferenceManager.setString(TAG_EXITSCREENSHOW, "1");
//                preferenceManager.setString(TAG_EXTRASCREENSHOW, "1");
//
//                preferenceManager.setString("screenone", "1");
//                preferenceManager.setString("screentwo", "1");
//                preferenceManager.setString("screenthree", "1");
//                preferenceManager.setString("screenfour", "1");
//                preferenceManager.setString("screenfive", "0");
//                preferenceManager.setString("screensix", "0");
//                preferenceManager.setString("bannerlarge", "0");
//                preferenceManager.setString("extrascreennative", "1");
//
//                preferenceManager.setString(TAG_BANNERMAIN, "ca-app-pub-3940256099942544/6300978111");
//                preferenceManager.setString(TAG_BANNERMAINS, "ca-app-pub-3940256099942544/6300978111");
//                preferenceManager.setString(TAG_BANNERMAINR, "ca-app-pub-3940256099942544/6300978111");
//                preferenceManager.setString(TAG_BANNERMAINRS, "ca-app-pub-3940256099942544/6300978111");
//
//                Log.d("rajaninter", "rajaninter");
//                preferenceManager.setString(TAG_INTERSTITIALMAIN, "ca-app-pub-3940256099942544/1033173712");
//                preferenceManager.setString(TAG_INTERSTITIALMAINR, "ca-app-pub-3940256099942544/1033173712");
//                preferenceManager.setString(TAG_INTERSTITIALMAINS, "ca-app-pub-3940256099942544/1033173712");
//
//                preferenceManager.setString(TAG_OPENAPP_ADS_ENABLED, "yes");
//                preferenceManager.setString(TAG_OPENAPPID, "ca-app-pub-3940256099942544/9257395921");
//
//                preferenceManager.setString(TAG_ADMOB_INTERSTITIAL_FREQUENCY, "1");
//                preferenceManager.setString(TAG_ADMOB_APPOPEN_FREQUENCY, "50");
//
//                preferenceManager.setString(TAG_NOTIFICATION_FREQUENCY, "1");
//            }

            if(prefManagerVideo22nd.getString(enable_previous_dummy_screens).contains("true")){
                intent = new Intent(Splash23rdActivity.this, StartApp23rdActivity.class);
            } else {

                if (new PrefManagerVideo23rd(Splash23rdActivity.this).getString(Splash23rdActivity.status_dummy_one_enabled_fifteen).contains("true")) {
                    intent = new Intent(Splash23rdActivity.this, First23rdActivity.class);
                } else if (new PrefManagerVideo23rd(Splash23rdActivity.this).getString(Splash23rdActivity.status_dummy_two_enabled).contains("true")) {
                    intent = new Intent(Splash23rdActivity.this, Second23rdActivity.class);
                } else if (new PrefManagerVideo23rd(Splash23rdActivity.this).getString(Splash23rdActivity.status_dummy_three_enabled).contains("true")) {
                    intent = new Intent(Splash23rdActivity.this, Third23rdActivity.class);
                } else if (new PrefManagerVideo23rd(Splash23rdActivity.this).getString(Splash23rdActivity.status_dummy_four_enabled).contains("true")) {
                    intent = new Intent(Splash23rdActivity.this, Fourth23rdActivity.class);
                } else if (new PrefManagerVideo23rd(Splash23rdActivity.this).getString(Splash23rdActivity.status_dummy_five_enabled).contains("true") && !new PrefManagerVideo23rd(Splash23rdActivity.this).getString(Splash23rdActivity.TAG_NATIVEID).contains("sandeep")) {
                    intent = new Intent(Splash23rdActivity.this, Fifth23rdActivity.class);
                } else if (new PrefManagerVideo23rd(Splash23rdActivity.this).getString(Splash23rdActivity.status_dummy_six_enabled).contains("true")) {
                    intent = new Intent(Splash23rdActivity.this, Sixth23rdActivity.class);
                } else {
                    intent = new Intent(Splash23rdActivity.this, Main23rdActivity.class);
                }

            }

            AdsManager23rd.initializeAdMob(Splash23rdActivity.this);

            if (prefManagerVideo22nd.getString("SUBSCRIBED").equals("FALSE")) {
                loadads();
            } else {
                stopallads();
                proceedToNextActivity();
            }
        }
    }

    private void stopallads() {
        prefManagerVideo22nd.setString(TAG_BANNER, "admob");
        prefManagerVideo22nd.setString(TAG_BANNERMAIN, "rajanca-app-pub-3940256099942544/6300978111");
        prefManagerVideo22nd.setString(TAG_BANNERMAINS, "rajanca-app-pub-3940256099942544/6300978111");
        prefManagerVideo22nd.setString(TAG_BANNERMAINR, "rajanca-app-pub-3940256099942544/6300978111");
        prefManagerVideo22nd.setString(TAG_BANNERMAINRS, "rajanca-app-pub-3940256099942544/6300978111");
        prefManagerVideo22nd.setString(TAG_INTERSTITIAL, "admob");
        prefManagerVideo22nd.setString(TAG_INTERSTITIALMAIN, "rajanca-app-pub-3940256099942544/1033173712");
        prefManagerVideo22nd.setString(TAG_INTERSTITIALMAINS, "rajanca-app-pub-3940256099942544/1033173712");
        prefManagerVideo22nd.setString(TAG_INTERSTITIALMAINR, "rajanca-app-pub-3940256099942544/1033173712");
        prefManagerVideo22nd.setString(TAG_SPLASH, "openapp");
        prefManagerVideo22nd.setString(TAG_INTERSTITIALSPLASH, "no");
        prefManagerVideo22nd.setString(TAG_INTERSTITIALSPLASHID, "rajanca-app-pub-3940256099942544/1033173712");

        prefManagerVideo22nd.setString(TAG_INTERSTITIALTYPE, "inter");

        prefManagerVideo22nd.setString(TAG_INTERPRELOAD, "yes");
        prefManagerVideo22nd.setString(TAG_INTERSDIALOG, "no");
        prefManagerVideo22nd.setString(TAG_INTERSDIALOG_MAX_TIME, "5");

        prefManagerVideo22nd.setString(TAG_OPENAPP_ADS_ENABLED, "no");
        prefManagerVideo22nd.setString(TAG_OPENAPPID, "rajanca-app-pub-3940256099942544/3419835294");

        prefManagerVideo22nd.setString(TAG_NATIVE_ADS_ENABLED, "no");
        prefManagerVideo22nd.setString(TAG_NATIVE, "admob");
        prefManagerVideo22nd.setString(TAG_NATIVEID, "rajanca-app-pub-3940256099942544/2247696110");
        prefManagerVideo22nd.setString(TAG_NATIVEIDD, "rajanca-app-pub-3940256099942544/2247696110");
        prefManagerVideo22nd.setString(TAG_NATIVEIDR, "rajanca-app-pub-3940256099942544/2247696110");
        prefManagerVideo22nd.setString(TAG_NATIVE_ADS_FREQUENCY, "5");
        prefManagerVideo22nd.setString(TAG_NATIVE_ADS_FREQUENCY_MAX, "50");

        prefManagerVideo22nd.setString("nativeadsbigtosmall", "0");
        prefManagerVideo22nd.setString("nativeadsalltobanner", "0");

        prefManagerVideo22nd.setString(TAG_EXITSCREENSHOW, "0");
        prefManagerVideo22nd.setString(TAG_EXTRASCREENSHOW, "0");

        prefManagerVideo22nd.setString("screenone", "0");
        prefManagerVideo22nd.setString("screentwo", "0");
        prefManagerVideo22nd.setString("screenthree", "0");
        prefManagerVideo22nd.setString("screenfour", "0");
        prefManagerVideo22nd.setString("screenfive", "0");
        prefManagerVideo22nd.setString("screensix", "0");

        prefManagerVideo22nd.setString("bannerlarge", "0");
        prefManagerVideo22nd.setString("extrascreennative", "0");

        prefManagerVideo22nd.setString("skipfirstscreen", "1");

        prefManagerVideo22nd.setString(TAG_ADMOB_APPOPEN_FREQUENCY, "1000");

        prefManagerVideo22nd.setString(TAG_WHATSAPP_SHARE_FREQUENCY, "500");

    }

    // ads
    private void loadads() {
        if (prefManagerVideo22nd.getString(TAG_INADSSTOP).contains("1")) {
            stopallads();
        }

        if (prefManagerVideo22nd.getString(TAG_INTERSTITIALSPLASH).equalsIgnoreCase("yes")) {
            try {
                if (prefManagerVideo22nd.getString(TAG_SPLASH).equalsIgnoreCase("openapp")) {
                    if (prefManagerVideo22nd.getString(TAG_OPENAPP_ADS_ENABLED).equalsIgnoreCase("yes")) {
//                            appOpenManager = new AppOpenManager((App) App.getContext());

                        AppOpenAd.AppOpenAdLoadCallback loadCallback = new AppOpenAd.AppOpenAdLoadCallback() {
                            @Override
                            public void onAdLoaded(@NonNull AppOpenAd ad) {
                                appOpenAd = ad;
                                ShowAppOpenAds();
                            }

                            @Override
                            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                                ShowAppOpenAds();
                                Log.e("TAG", "onAdFailedToLoad: " + loadAdError);
                            }
                        };
                        AdRequest request = new AdRequest.Builder().build();
                        AppOpenAd.load(Splash23rdActivity.this, prefManagerVideo22nd.getString(TAG_OPENAPPID), request, loadCallback);
                    }
                } else if (prefManagerVideo22nd.getString(TAG_SPLASH).equalsIgnoreCase("interstitial")) {
                    //ads
                    // Initialize the Mobile Ads SDK.
                    if (prefManagerVideo22nd.getString(TAG_APP_ID_AD_UNIT_ID) != "") {
                        // Initialize the Mobile Ads SDK.
                        MobileAds.initialize(Splash23rdActivity.this, new OnInitializationCompleteListener() {
                            @Override
                            public void onInitializationComplete(InitializationStatus initializationStatus) {
                            }
                        });
                    }

//                    if (preferenceManager.getString(TAG_BANNER).equalsIgnoreCase("admob") || preferenceManager.getString(TAG_INTERSTITIAL).equalsIgnoreCase("admob")) {
//                        adRequest = new AdRequest.Builder().build();
//                    }

                    if (prefManagerVideo22nd.getString(TAG_INTERSTITIAL).equalsIgnoreCase("admob")) {
                        if (mInterstitialAd == null) {

                            AdRequest adRequest = new AdRequest.Builder().build();
                            com.google.android.gms.ads.interstitial.InterstitialAd.load(Splash23rdActivity.this, prefManagerVideo22nd.getString(TAG_INTERSTITIALMAINS), adRequest, new InterstitialAdLoadCallback() {
                                @Override
                                public void onAdLoaded(@NonNull com.google.android.gms.ads.interstitial.InterstitialAd interstitialAd) {
                                    // The mInterstitialAd reference will be null until
                                    // an ad is loaded.
                                    mInterstitialAd = interstitialAd;

                                    //show ads
                                    try {
                                        if (mInterstitialAd != null) {
                                            mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                                                @Override
                                                public void onAdDismissedFullScreenContent() {
                                                    // Called when fullscreen content is dismissed.
                                                    // Make sure to set your reference to null so you don't
                                                    // show it a second time.
                                                    mInterstitialAd = null;
                                                    proceedToNextActivity();
                                                }

                                                @Override
                                                public void onAdFailedToShowFullScreenContent(com.google.android.gms.ads.AdError adError) {
                                                    // Called when fullscreen content failed to show.
                                                    // Make sure to set your reference to null so you don't
                                                    // show it a second time.
                                                    mInterstitialAd = null;
                                                    proceedToNextActivity();
                                                }


                                                @Override
                                                public void onAdShowedFullScreenContent() {
                                                    // Called when fullscreen content is shown.
                                                    // Log.d("TAG", "The ad was shown.");
                                                }
                                            });
                                            mInterstitialAd.show((Activity) Splash23rdActivity.this);
                                        } else {
                                            proceedToNextActivity();
                                        }
                                    } catch (Exception e) {
                                        proceedToNextActivity();
                                        //FirebaseCrashlytics.getInstance().recordException(e);
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                                    // Handle the error
                                    mInterstitialAd = null;
                                    proceedToNextActivity();
                                }
                            });
                        }
                    }
                } else {
                    proceedToNextActivity();
                }
            } catch (Exception e) {
                proceedToNextActivity();
                //FirebaseCrashlytics.getInstance().recordException(e);
                e.printStackTrace();
            }
        } else {
            proceedToNextActivity();
        }
    }

    private void proceedToNextActivity() {
        startActivity(intent);
        new Handler(Looper.myLooper()).postDelayed(() -> {
            finish();
        }, 1500);
    }

    private String getsessioncount() {
        prefManagerVideo22nd.setInt(TAG_NEW_SESSIONCOUNT, prefManagerVideo22nd.getInt(TAG_NEW_SESSIONCOUNT) + 1);
        return String.valueOf(prefManagerVideo22nd.getInt(TAG_NEW_SESSIONCOUNT));
    }

    private String getisdevmode() {
        if (Integer.valueOf(Build.VERSION.SDK_INT).intValue() == 16) {
            return String.valueOf(Settings.Secure.getInt(getContentResolver(), "development_settings_enabled", 0));
        }
        if (Integer.valueOf(Build.VERSION.SDK_INT).intValue() >= 17) {
            return String.valueOf(Settings.Secure.getInt(getContentResolver(), "development_settings_enabled", 0));
        }
        return String.valueOf(0);
    }

    private String getinstaller() {
        // A list with valid installers package name
//        List<String> validInstallers = new ArrayList<>(Arrays.asList("com.android.vending", "com.google.android.feedback"));

        // The package name of the app that has installed your app
        final String installer = getPackageManager().getInstallerPackageName(getPackageName());

        // true if your app has been downloaded from Play Store
//        return String.valueOf(installer != null && validInstallers.contains(installer));
        if (installer == null) {
            return "";
        } else {
            return installer;
        }
    }

}
