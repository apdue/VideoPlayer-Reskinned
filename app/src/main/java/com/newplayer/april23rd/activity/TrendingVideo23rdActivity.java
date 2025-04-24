package com.newplayer.april23rd.activity;


import static com.newplayer.april23rd.AdsManger.Splash23rdActivity.TAG_NATIVEID;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.newplayer.april23rd.AdsManger.AdController23rd;
import com.newplayer.april23rd.AdsManger.PrefManagerVideo23rd;
import com.newplayer.april23rd.R;
import com.newplayer.april23rd.adapter.Category23rdAdapter;
import com.newplayer.april23rd.model.TrendVideoData23rd;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TrendingVideo23rdActivity extends AppCompatActivity {

    RecyclerView videoRecyclerView;
    ImageView ivBack;
    Category23rdAdapter categoryAdapter;
    LinearLayout progressLinear, noDataLinear;
    private ArrayList<TrendVideoData23rd> videoDataArrayList;
    private PrefManagerVideo23rd prf;
    private Context context;
    private AdController23rd adControllerlocalInstance23rd;
    private FrameLayout mediumNativeFrameLayout;
    boolean shouldExecuteOnResume;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trending_video_23rd);
        context = getApplicationContext();
        prf = new PrefManagerVideo23rd(context);
        adControllerlocalInstance23rd = AdController23rd.getInstance();
        mediumNativeFrameLayout = findViewById(R.id.fl_adplaceholderrapp);
        adControllerlocalInstance23rd.newreleasenativePreload(TrendingVideo23rdActivity.this, prf.getString(TAG_NATIVEID), 0, mediumNativeFrameLayout, true);
        shouldExecuteOnResume = false;
        viewBinding();
    }

    public void viewBinding() {
        videoRecyclerView = findViewById(R.id.videoRecyclerView);
        progressLinear = findViewById(R.id.progressLinear);
        ivBack = findViewById(R.id.ivBack);
        noDataLinear = findViewById(R.id.noDataLinear);

        videoDataArrayList = new ArrayList<>();
        VideoDetailsTask task = new VideoDetailsTask();
        task.execute();
        categoryAdapter = new Category23rdAdapter(TrendingVideo23rdActivity.this);
        videoRecyclerView.setLayoutManager(new LinearLayoutManager(TrendingVideo23rdActivity.this, RecyclerView.VERTICAL, false));
        videoRecyclerView.setAdapter(categoryAdapter);

        ivBack.setOnClickListener(v -> {
            onBackPressed();
        });
    }

    @SuppressLint("StaticFieldLeak")
    public class VideoDetailsTask extends AsyncTask<Void, Void, ArrayList<TrendVideoData23rd>> {

        @Override
        protected ArrayList<TrendVideoData23rd> doInBackground(Void... voids) {
            OkHttpClient client = new OkHttpClient();
            ArrayList<TrendVideoData23rd> videoDataArrayList = new ArrayList<>();
            HashMap<String, ArrayList<TrendVideoData23rd>> listHashMap = new HashMap<>();

            RequestBody requestBody = new FormBody.Builder().build(); // Add parameters if needed
            Request request = new Request.Builder()
                    .url("https://tiktok.myappadmin.xyz/tiktokvideos/tiktokvideos.php")
                    .post(requestBody)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful() && response.body() != null) {
                    String responseBody = response.body().string();
                    JSONArray jsonArray = new JSONArray(responseBody);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject tagObject = jsonArray.getJSONObject(i);
                        String categoryName = tagObject.getString("category");
                        String videoUrl = tagObject.getString("videourl");
                        String videoThumbnail = tagObject.getString("thumbnailimg");
                        String videoTitle = tagObject.getString("title");

                        ArrayList<TrendVideoData23rd> videoDataList = listHashMap.get(categoryName);
                        if (videoDataList == null) {
                            videoDataList = new ArrayList<>();
                            listHashMap.put(categoryName, videoDataList);
                        }
                        videoDataList.add(new TrendVideoData23rd(categoryName, videoUrl, videoThumbnail, videoTitle));
                    }

                    for (Map.Entry<String, ArrayList<TrendVideoData23rd>> entry : listHashMap.entrySet()) {
                        TrendVideoData23rd videoData = new TrendVideoData23rd();
                        videoData.setCategoryName(entry.getKey());
                        videoData.setListHashMap(entry.getValue());
                        videoDataArrayList.add(videoData);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return videoDataArrayList;
        }

        @Override
        protected void onPostExecute(ArrayList<TrendVideoData23rd> videoDataArrayList) {
            super.onPostExecute(videoDataArrayList);
            handleVideoDetails(videoDataArrayList);
        }

        private void handleVideoDetails(ArrayList<TrendVideoData23rd> videoDetailsList) {
            runOnUiThread(() -> {
                if (videoDetailsList.size() != 0) {
                    categoryAdapter.setVideoArrayList(videoDetailsList);
                    categoryAdapter.notifyDataSetChanged();
                    noDataLinear.setVisibility(View.GONE);
                } else {
                    noDataLinear.setVisibility(View.VISIBLE);
                }
                progressLinear.setVisibility(View.GONE);
            });
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (shouldExecuteOnResume) {
            if ((mediumNativeFrameLayout != null) && (mediumNativeFrameLayout.getChildCount() == 0)) {
                adControllerlocalInstance23rd.newreleasenativePreload(TrendingVideo23rdActivity.this, prf.getString(TAG_NATIVEID), 0, mediumNativeFrameLayout, true);
            }
        } else {
            shouldExecuteOnResume = true;
        }
    }

    public void bckpressed() {
        super.onBackPressed();
    }

    @Override
    public void onBackPressed() {
        adControllerlocalInstance23rd.showInterAdCallBack(TrendingVideo23rdActivity.this, new AdController23rd.MyCallback() {
            @Override
            public void callbackCall() {
                bckpressed();
            }
        });
    }
}