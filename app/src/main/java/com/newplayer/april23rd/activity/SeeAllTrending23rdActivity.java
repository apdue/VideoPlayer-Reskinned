package com.newplayer.april23rd.activity;

import static com.newplayer.april23rd.AdsManger.Splash23rdActivity.TAG_NATIVEIDD;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.newplayer.april23rd.AdsManger.AdController23rd;
import com.newplayer.april23rd.AdsManger.PrefManagerVideo23rd;
import com.newplayer.april23rd.R;
import com.newplayer.april23rd.adapter.SeeAll23rdAdapter;
import com.newplayer.april23rd.model.TrendVideoData23rd;
import java.util.ArrayList;

public class SeeAllTrending23rdActivity extends AppCompatActivity {

    FrameLayout flSmallSizeNative;
    Context context;
    PrefManagerVideo23rd prefManagerVideo22nd;
    AdController23rd adController23rd;
    boolean shouldExecuteOnResume;
    SeeAll23rdAdapter seeAllAdapter;
    ArrayList<TrendVideoData23rd> trendVideoData23rdArrayList = new ArrayList<>();
    String categoryName;
    TextView tvVideoName;
    RecyclerView rvVideoList;
    ImageView ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_all_trending_23rd);

        Intent intent = getIntent();
        if (intent != null) {
            trendVideoData23rdArrayList = intent.getParcelableArrayListExtra("dataList");
            categoryName = intent.getStringExtra("categoryName");
        }

        initViews();
        clickViews();
        loadShowAds();
    }

    private void initViews() {
        context = getApplicationContext();
        prefManagerVideo22nd = new PrefManagerVideo23rd(context);
        adController23rd = AdController23rd.getInstance();
        flSmallSizeNative = findViewById(R.id.flSmallSizeNative);

        tvVideoName = findViewById(R.id.tvVideoName);
        rvVideoList = findViewById(R.id.rvVideoList);
        ivBack = findViewById(R.id.ivBack);

        tvVideoName.setText(categoryName);

        SeeAll23rdAdapter subCategoryAdapter = new SeeAll23rdAdapter(trendVideoData23rdArrayList, SeeAllTrending23rdActivity.this);
        rvVideoList.setLayoutManager(new LinearLayoutManager(SeeAllTrending23rdActivity.this, LinearLayoutManager.VERTICAL, false));
        rvVideoList.setAdapter(subCategoryAdapter);

        ivBack.setOnClickListener(v -> {
            onBackPressed();
        });
    }

    private void clickViews() {
        seeAllAdapter = new SeeAll23rdAdapter(trendVideoData23rdArrayList, SeeAllTrending23rdActivity.this);
    }

    private void loadShowAds() {
        adController23rd.newreleasenativePreloadExtra(this, prefManagerVideo22nd.getString(TAG_NATIVEIDD), 2, flSmallSizeNative, true);
    }

    public void loadAdNextTime() {
        if (shouldExecuteOnResume) {
            if (prefManagerVideo22nd.getString("extrascreennative").contains("1")) {
                if (flSmallSizeNative != null) {
                    flSmallSizeNative.removeAllViews();
                }
                if ((flSmallSizeNative != null) && (flSmallSizeNative.getChildCount() == 0)) {
                    adController23rd.newreleasenativePreloadExtra(SeeAllTrending23rdActivity.this, prefManagerVideo22nd.getString(TAG_NATIVEIDD), 2, flSmallSizeNative, true);
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
        adController23rd.showInterAdCallBack(SeeAllTrending23rdActivity.this, this::superOnBackPressed);
    }

    private void superOnBackPressed() {
        super.onBackPressed();
    }
}