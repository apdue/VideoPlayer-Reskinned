package com.newplayer.april23rd.activity;

import static com.newplayer.april23rd.AdsManger.Splash23rdActivity.TAG_NATIVEIDD;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.newplayer.april23rd.AdsManger.AdController23rd;
import com.newplayer.april23rd.AdsManger.PrefManagerVideo23rd;
import com.newplayer.april23rd.R;
import com.newplayer.april23rd.adapter.Search23rdAdapter;
import com.newplayer.april23rd.model.GalleryVideoInformation23rd;

import java.util.ArrayList;

public class SearchVideo23rdActivity extends AppCompatActivity {

    LinearLayout llNoData;
    EditText etSearch;
    ArrayList<GalleryVideoInformation23rd> videoInformationArrayList = new ArrayList<>();
    Search23rdAdapter searchAdapter;
    RecyclerView rvSearch;
    ImageView ivBack;
    Context context;
    PrefManagerVideo23rd prefManagerVideo22nd;
    AdController23rd adController23rd;
    FrameLayout flSmallSizeNative;
    boolean shouldExecuteOnResume;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_video_23rd);

        initViews();
        clickViews();
        loadShowAds();
    }

    private void initViews() {
        context = getApplicationContext();
        prefManagerVideo22nd = new PrefManagerVideo23rd(context);
        adController23rd = AdController23rd.getInstance();
        flSmallSizeNative = findViewById(R.id.flSmallSizeNative);

        rvSearch = findViewById(R.id.rvSearch);
        ivBack = findViewById(R.id.ivBack);
        llNoData = findViewById(R.id.llNoData);
        etSearch = findViewById(R.id.etSearch);
    }

    private void clickViews() {
        videoInformationArrayList = getIntent().getExtras().getParcelableArrayList("videoArrayList");
        searchAdapter = new Search23rdAdapter(SearchVideo23rdActivity.this, videoInformationArrayList);
        rvSearch.setLayoutManager(new LinearLayoutManager(SearchVideo23rdActivity.this, LinearLayoutManager.VERTICAL, false));
        rvSearch.setAdapter(searchAdapter);

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        ivBack.setOnClickListener(v -> {
            onBackPressed();
        });
    }

    public void filter(String text) {
        ArrayList<GalleryVideoInformation23rd> filteredList = new ArrayList<>();

        if (text.isEmpty()) {
            searchAdapter.updateList(videoInformationArrayList);
            llNoData.setVisibility(View.GONE);
        } else {
            for (GalleryVideoInformation23rd video : videoInformationArrayList) {
                if (video.getTitle().toLowerCase().contains(text.toLowerCase())) {
                    filteredList.add(video);
                }
            }
            searchAdapter.updateList(filteredList);
            if (filteredList.isEmpty()) {
                llNoData.setVisibility(View.VISIBLE);
            } else {
                llNoData.setVisibility(View.GONE);
            }
        }
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
                    adController23rd.newreleasenativePreloadExtra(SearchVideo23rdActivity.this, prefManagerVideo22nd.getString(TAG_NATIVEIDD), 2, flSmallSizeNative, true);
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
        adController23rd.showInterAdCallBack(SearchVideo23rdActivity.this, this::superOnBackPressed);
    }

    private void superOnBackPressed() {
        super.onBackPressed();
    }
}