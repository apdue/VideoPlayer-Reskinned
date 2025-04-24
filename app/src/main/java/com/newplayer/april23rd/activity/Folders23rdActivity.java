package com.newplayer.april23rd.activity;

import static com.newplayer.april23rd.AdsManger.Splash23rdActivity.TAG_NATIVEIDD;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.newplayer.april23rd.AdsManger.AdController23rd;
import com.newplayer.april23rd.AdsManger.PrefManagerVideo23rd;
import com.newplayer.april23rd.R;
import com.newplayer.april23rd.adapter.Folder23rdAdapter;
import com.newplayer.april23rd.model.GalleryVideoFolderDetails23rd;
import com.newplayer.april23rd.utils.FetchStorageVideo23rd;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Folders23rdActivity extends AppCompatActivity {

    FrameLayout flSmallSizeNative;
    Context context;
    PrefManagerVideo23rd prefManagerVideo22nd;
    AdController23rd adController23rd;
    boolean shouldExecuteOnResume;
    RecyclerView rvFolder;
    ImageView ivBack, ivGrid;
    boolean isGrid;
    ArrayList<GalleryVideoFolderDetails23rd> videoFolderDetailsArrayList;
    Folder23rdAdapter folderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folders_23rd);

        initViews();
        clickViews();
        loadShowAds();
        loadData();
    }

    private void initViews() {
        context = getApplicationContext();
        prefManagerVideo22nd = new PrefManagerVideo23rd(context);
        adController23rd = AdController23rd.getInstance();
        flSmallSizeNative = findViewById(R.id.flSmallSizeNative);

        rvFolder = findViewById(R.id.rvFolder);
        ivBack = findViewById(R.id.ivBack);
        ivGrid = findViewById(R.id.ivGrid);
    }

    private void clickViews() {
        videoFolderDetailsArrayList = new ArrayList<>();
        folderAdapter = new Folder23rdAdapter(Folders23rdActivity.this, videoFolderDetailsArrayList);
        rvFolder.setLayoutManager(new LinearLayoutManager(Folders23rdActivity.this, LinearLayoutManager.VERTICAL, false));
        rvFolder.setAdapter(folderAdapter);


        ivGrid.setOnClickListener(v -> {
            isGrid = !isGrid;
            folderAdapter.setLayoutType(isGrid ? Folder23rdAdapter.LAYOUT_GRID : Folder23rdAdapter.LAYOUT_VERTICAL);
            if (isGrid) {
                ivGrid.setImageDrawable(ContextCompat.getDrawable(Folders23rdActivity.this, R.drawable.ic_list_23rd));
            } else {
                ivGrid.setImageDrawable(ContextCompat.getDrawable(Folders23rdActivity.this, R.drawable.ic_grid_23rd));
            }
            setLayoutManager();
        });


        ivBack.setOnClickListener(v -> {
            onBackPressed();
        });
    }

    public void loadShowAds() {
        adController23rd.newreleasenativePreloadExtra(this, prefManagerVideo22nd.getString(TAG_NATIVEIDD), 2, flSmallSizeNative, true);
    }

    private void setLayoutManager() {
        if (isGrid) {
            int numberOfColumns = 2;
            rvFolder.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
        } else {
            rvFolder.setLayoutManager(new LinearLayoutManager(this));
        }
    }

    public void loadAdNextTime() {
        if (shouldExecuteOnResume) {
            if (prefManagerVideo22nd.getString("extrascreennative").contains("1")) {
                if (flSmallSizeNative != null) {
                    flSmallSizeNative.removeAllViews();
                }
                if ((flSmallSizeNative != null) && (flSmallSizeNative.getChildCount() == 0)) {
                    adController23rd.newreleasenativePreloadExtra(Folders23rdActivity.this, prefManagerVideo22nd.getString(TAG_NATIVEIDD), 2, flSmallSizeNative, true);
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
        adController23rd.showInterAdCallBack(Folders23rdActivity.this, this::superOnBackPressed);
    }

    private void superOnBackPressed() {
        super.onBackPressed();
    }

    private void loadData() {
        FetchStorageVideo23rd.showLoader(Folders23rdActivity.this);
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                ArrayList<GalleryVideoFolderDetails23rd> videoFolders = FetchStorageVideo23rd.getGalleryVideoFolders(Folders23rdActivity.this);

                runOnUiThread(new Runnable() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void run() {
                        FetchStorageVideo23rd.dismissLoader();
                        videoFolderDetailsArrayList.addAll(videoFolders);
                        folderAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }
}