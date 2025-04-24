package com.newplayer.april23rd.activity;

import static com.newplayer.april23rd.AdsManger.Splash23rdActivity.TAG_NATIVEIDD;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.newplayer.april23rd.AdsManger.AdController23rd;
import com.newplayer.april23rd.AdsManger.PrefManagerVideo23rd;
import com.newplayer.april23rd.R;
import com.newplayer.april23rd.adapter.Videos23rdAdapter;
import com.newplayer.april23rd.model.GalleryVideoInformation23rd;
import com.newplayer.april23rd.utils.FetchStorageVideo23rd;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Videos23rdActivity extends AppCompatActivity {

    FrameLayout flSmallSizeNative;
    Context context;
    PrefManagerVideo23rd prefManagerVideo22nd;
    AdController23rd adController23rd;
    boolean shouldExecuteOnResume;
    ImageView ivBack, ivSearch, ivGrid, ivSort;
    ArrayList<GalleryVideoInformation23rd> videoInformationArrayList;
    Videos23rdAdapter videosAdapter;
    RecyclerView rvVideoList;
    boolean isGrid;
    String selectedSortType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videos_23rd);

        initViews();
        clickViews();
        loadShowAds();
        loadData();
    }

    public void initViews() {
        context = getApplicationContext();
        prefManagerVideo22nd = new PrefManagerVideo23rd(context);
        adController23rd = AdController23rd.getInstance();
        flSmallSizeNative = findViewById(R.id.flSmallSizeNative);

        ivBack = findViewById(R.id.ivBack);
        ivSearch = findViewById(R.id.ivSearch);
        ivGrid = findViewById(R.id.ivGrid);
        ivSort = findViewById(R.id.ivSort);
        rvVideoList = findViewById(R.id.rvVideoList);
    }

    public void clickViews() {
        videoInformationArrayList = new ArrayList<>();
        videosAdapter = new Videos23rdAdapter(videoInformationArrayList, Videos23rdActivity.this);
        rvVideoList.setLayoutManager(new LinearLayoutManager(Videos23rdActivity.this, LinearLayoutManager.VERTICAL, false));
        rvVideoList.setAdapter(videosAdapter);

        ivBack.setOnClickListener(v -> {
            onBackPressed();
        });

        ivSearch.setOnClickListener(v -> {
            Intent intent = new Intent(Videos23rdActivity.this, SearchVideo23rdActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("videoArrayList", videoInformationArrayList);
            intent.putExtras(bundle);
            startActivity(intent);
        });

        ivGrid.setOnClickListener(v -> {
            isGrid = !isGrid;
            videosAdapter.setLayoutType(isGrid ? Videos23rdAdapter.LAYOUT_GRID : Videos23rdAdapter.LAYOUT_VERTICAL);
            if (isGrid) {
                ivGrid.setImageDrawable(ContextCompat.getDrawable(Videos23rdActivity.this, R.drawable.ic_list_23rd));
            } else {
                ivGrid.setImageDrawable(ContextCompat.getDrawable(Videos23rdActivity.this, R.drawable.ic_grid_23rd));
            }
            setLayoutManager();
        });

        ivSort.setOnClickListener(v -> {
            sortBottomSheet();
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void sortBottomSheet() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.sort_bottom_sheet_23rd);

        TextView tvNewToOldDate = bottomSheetDialog.findViewById(R.id.tvNewToOldDate);
        TextView tvOldToNewDate = bottomSheetDialog.findViewById(R.id.tvOldToNewDate);
        TextView tvNameAtoZ = bottomSheetDialog.findViewById(R.id.tvNameAtoZ);
        TextView tvNameZtoA = bottomSheetDialog.findViewById(R.id.tvNameZtoA);
        TextView tvSmallToBigSize = bottomSheetDialog.findViewById(R.id.tvSmallToBigSize);
        TextView tvBigToSmallSize = bottomSheetDialog.findViewById(R.id.tvBigToSmallSize);
        TextView tvDone = bottomSheetDialog.findViewById(R.id.tvDone);
        ImageView ivClose = bottomSheetDialog.findViewById(R.id.ivClose);

        ivClose.setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
        });

        tvDone.setOnClickListener(v -> {
            prefManagerVideo22nd.setString("selectedSortType", selectedSortType);
            sortVideos(selectedSortType);
            videosAdapter.notifyDataSetChanged();
            bottomSheetDialog.dismiss();
        });

        selectedSortType = prefManagerVideo22nd.getString("selectedSortType");
        if (selectedSortType.isEmpty()) {
            selectedSortType = "newToOld";
            tvNewToOldDate.setTextColor(Color.parseColor("#FFFFFF"));
            tvNewToOldDate.setBackground(ContextCompat.getDrawable(Videos23rdActivity.this, R.drawable.ic_install_bg_23rd));
            tvOldToNewDate.setBackground(ContextCompat.getDrawable(Videos23rdActivity.this, R.drawable.stoke_bg_23rd));
            tvNameAtoZ.setBackground(ContextCompat.getDrawable(Videos23rdActivity.this, R.drawable.stoke_bg_23rd));
            tvNameZtoA.setBackground(ContextCompat.getDrawable(Videos23rdActivity.this, R.drawable.stoke_bg_23rd));
            tvSmallToBigSize.setBackground(ContextCompat.getDrawable(Videos23rdActivity.this, R.drawable.stoke_bg_23rd));
            tvBigToSmallSize.setBackground(ContextCompat.getDrawable(Videos23rdActivity.this, R.drawable.stoke_bg_23rd));
        } else if (selectedSortType.equals("newToOld")) {
            tvNewToOldDate.setTextColor(Color.parseColor("#FFFFFF"));
            tvNewToOldDate.setBackground(ContextCompat.getDrawable(Videos23rdActivity.this, R.drawable.ic_install_bg_23rd));
            tvOldToNewDate.setBackground(ContextCompat.getDrawable(Videos23rdActivity.this, R.drawable.stoke_bg_23rd));
            tvNameAtoZ.setBackground(ContextCompat.getDrawable(Videos23rdActivity.this, R.drawable.stoke_bg_23rd));
            tvNameZtoA.setBackground(ContextCompat.getDrawable(Videos23rdActivity.this, R.drawable.stoke_bg_23rd));
            tvSmallToBigSize.setBackground(ContextCompat.getDrawable(Videos23rdActivity.this, R.drawable.stoke_bg_23rd));
            tvBigToSmallSize.setBackground(ContextCompat.getDrawable(Videos23rdActivity.this, R.drawable.stoke_bg_23rd));
        } else if (selectedSortType.equals("oldToNew")) {
            tvOldToNewDate.setTextColor(Color.parseColor("#FFFFFF"));
            tvNewToOldDate.setBackground(ContextCompat.getDrawable(Videos23rdActivity.this, R.drawable.stoke_bg_23rd));
            tvOldToNewDate.setBackground(ContextCompat.getDrawable(Videos23rdActivity.this, R.drawable.ic_install_bg_23rd));
            tvNameAtoZ.setBackground(ContextCompat.getDrawable(Videos23rdActivity.this, R.drawable.stoke_bg_23rd));
            tvNameZtoA.setBackground(ContextCompat.getDrawable(Videos23rdActivity.this, R.drawable.stoke_bg_23rd));
            tvSmallToBigSize.setBackground(ContextCompat.getDrawable(Videos23rdActivity.this, R.drawable.stoke_bg_23rd));
            tvBigToSmallSize.setBackground(ContextCompat.getDrawable(Videos23rdActivity.this, R.drawable.stoke_bg_23rd));
        } else if (selectedSortType.equals("AToZ")) {
            tvNameAtoZ.setTextColor(Color.parseColor("#FFFFFF"));
            tvNewToOldDate.setBackground(ContextCompat.getDrawable(Videos23rdActivity.this, R.drawable.stoke_bg_23rd));
            tvOldToNewDate.setBackground(ContextCompat.getDrawable(Videos23rdActivity.this, R.drawable.stoke_bg_23rd));
            tvNameAtoZ.setBackground(ContextCompat.getDrawable(Videos23rdActivity.this, R.drawable.ic_install_bg_23rd));
            tvNameZtoA.setBackground(ContextCompat.getDrawable(Videos23rdActivity.this, R.drawable.stoke_bg_23rd));
            tvSmallToBigSize.setBackground(ContextCompat.getDrawable(Videos23rdActivity.this, R.drawable.stoke_bg_23rd));
            tvBigToSmallSize.setBackground(ContextCompat.getDrawable(Videos23rdActivity.this, R.drawable.stoke_bg_23rd));
        } else if (selectedSortType.equals("ZToA")) {
            tvNameZtoA.setTextColor(Color.parseColor("#FFFFFF"));
            tvNewToOldDate.setBackground(ContextCompat.getDrawable(Videos23rdActivity.this, R.drawable.stoke_bg_23rd));
            tvOldToNewDate.setBackground(ContextCompat.getDrawable(Videos23rdActivity.this, R.drawable.stoke_bg_23rd));
            tvNameAtoZ.setBackground(ContextCompat.getDrawable(Videos23rdActivity.this, R.drawable.stoke_bg_23rd));
            tvNameZtoA.setBackground(ContextCompat.getDrawable(Videos23rdActivity.this, R.drawable.ic_install_bg_23rd));
            tvSmallToBigSize.setBackground(ContextCompat.getDrawable(Videos23rdActivity.this, R.drawable.stoke_bg_23rd));
            tvBigToSmallSize.setBackground(ContextCompat.getDrawable(Videos23rdActivity.this, R.drawable.stoke_bg_23rd));
        } else if (selectedSortType.equals("smallToBig")) {
            tvSmallToBigSize.setTextColor(Color.parseColor("#FFFFFF"));
            tvNewToOldDate.setBackground(ContextCompat.getDrawable(Videos23rdActivity.this, R.drawable.stoke_bg_23rd));
            tvOldToNewDate.setBackground(ContextCompat.getDrawable(Videos23rdActivity.this, R.drawable.stoke_bg_23rd));
            tvNameAtoZ.setBackground(ContextCompat.getDrawable(Videos23rdActivity.this, R.drawable.stoke_bg_23rd));
            tvNameZtoA.setBackground(ContextCompat.getDrawable(Videos23rdActivity.this, R.drawable.stoke_bg_23rd));
            tvSmallToBigSize.setBackground(ContextCompat.getDrawable(Videos23rdActivity.this, R.drawable.ic_install_bg_23rd));
            tvBigToSmallSize.setBackground(ContextCompat.getDrawable(Videos23rdActivity.this, R.drawable.stoke_bg_23rd));
        } else if (selectedSortType.equals("bigToSmall")) {
            tvBigToSmallSize.setTextColor(Color.parseColor("#FFFFFF"));
            tvNewToOldDate.setBackground(ContextCompat.getDrawable(Videos23rdActivity.this, R.drawable.stoke_bg_23rd));
            tvOldToNewDate.setBackground(ContextCompat.getDrawable(Videos23rdActivity.this, R.drawable.stoke_bg_23rd));
            tvNameAtoZ.setBackground(ContextCompat.getDrawable(Videos23rdActivity.this, R.drawable.stoke_bg_23rd));
            tvNameZtoA.setBackground(ContextCompat.getDrawable(Videos23rdActivity.this, R.drawable.stoke_bg_23rd));
            tvSmallToBigSize.setBackground(ContextCompat.getDrawable(Videos23rdActivity.this, R.drawable.stoke_bg_23rd));
            tvBigToSmallSize.setBackground(ContextCompat.getDrawable(Videos23rdActivity.this, R.drawable.ic_install_bg_23rd));
        }

        tvNewToOldDate.setOnClickListener(v -> {
            selectedSortType = "newToOld";

            tvNewToOldDate.setTextColor(Color.parseColor("#FFFFFF"));
            tvOldToNewDate.setTextColor(Color.parseColor("#7A7A7A"));
            tvNameAtoZ.setTextColor(Color.parseColor("#7A7A7A"));
            tvNameZtoA.setTextColor(Color.parseColor("#7A7A7A"));
            tvSmallToBigSize.setTextColor(Color.parseColor("#7A7A7A"));
            tvBigToSmallSize.setTextColor(Color.parseColor("#7A7A7A"));

            tvNewToOldDate.setBackground(ContextCompat.getDrawable(Videos23rdActivity.this, R.drawable.ic_install_bg_23rd));
            tvOldToNewDate.setBackground(ContextCompat.getDrawable(Videos23rdActivity.this, R.drawable.stoke_bg_23rd));
            tvNameAtoZ.setBackground(ContextCompat.getDrawable(Videos23rdActivity.this, R.drawable.stoke_bg_23rd));
            tvNameZtoA.setBackground(ContextCompat.getDrawable(Videos23rdActivity.this, R.drawable.stoke_bg_23rd));
            tvSmallToBigSize.setBackground(ContextCompat.getDrawable(Videos23rdActivity.this, R.drawable.stoke_bg_23rd));
            tvBigToSmallSize.setBackground(ContextCompat.getDrawable(Videos23rdActivity.this, R.drawable.stoke_bg_23rd));
        });
        tvOldToNewDate.setOnClickListener(v -> {
            selectedSortType = "oldToNew";

            tvNewToOldDate.setTextColor(Color.parseColor("#7A7A7A"));
            tvOldToNewDate.setTextColor(Color.parseColor("#FFFFFF"));
            tvNameAtoZ.setTextColor(Color.parseColor("#7A7A7A"));
            tvNameZtoA.setTextColor(Color.parseColor("#7A7A7A"));
            tvSmallToBigSize.setTextColor(Color.parseColor("#7A7A7A"));
            tvBigToSmallSize.setTextColor(Color.parseColor("#7A7A7A"));

            tvNewToOldDate.setBackground(ContextCompat.getDrawable(Videos23rdActivity.this, R.drawable.stoke_bg_23rd));
            tvOldToNewDate.setBackground(ContextCompat.getDrawable(Videos23rdActivity.this, R.drawable.ic_install_bg_23rd));
            tvNameAtoZ.setBackground(ContextCompat.getDrawable(Videos23rdActivity.this, R.drawable.stoke_bg_23rd));
            tvNameZtoA.setBackground(ContextCompat.getDrawable(Videos23rdActivity.this, R.drawable.stoke_bg_23rd));
            tvSmallToBigSize.setBackground(ContextCompat.getDrawable(Videos23rdActivity.this, R.drawable.stoke_bg_23rd));
            tvBigToSmallSize.setBackground(ContextCompat.getDrawable(Videos23rdActivity.this, R.drawable.stoke_bg_23rd));
        });
        tvNameAtoZ.setOnClickListener(v -> {
            selectedSortType = "AToZ";

            tvNewToOldDate.setTextColor(Color.parseColor("#7A7A7A"));
            tvOldToNewDate.setTextColor(Color.parseColor("#7A7A7A"));
            tvNameAtoZ.setTextColor(Color.parseColor("#FFFFFF"));
            tvNameZtoA.setTextColor(Color.parseColor("#7A7A7A"));
            tvSmallToBigSize.setTextColor(Color.parseColor("#7A7A7A"));
            tvBigToSmallSize.setTextColor(Color.parseColor("#7A7A7A"));

            tvNewToOldDate.setBackground(ContextCompat.getDrawable(Videos23rdActivity.this, R.drawable.stoke_bg_23rd));
            tvOldToNewDate.setBackground(ContextCompat.getDrawable(Videos23rdActivity.this, R.drawable.stoke_bg_23rd));
            tvNameAtoZ.setBackground(ContextCompat.getDrawable(Videos23rdActivity.this, R.drawable.ic_install_bg_23rd));
            tvNameZtoA.setBackground(ContextCompat.getDrawable(Videos23rdActivity.this, R.drawable.stoke_bg_23rd));
            tvSmallToBigSize.setBackground(ContextCompat.getDrawable(Videos23rdActivity.this, R.drawable.stoke_bg_23rd));
            tvBigToSmallSize.setBackground(ContextCompat.getDrawable(Videos23rdActivity.this, R.drawable.stoke_bg_23rd));
        });
        tvNameZtoA.setOnClickListener(v -> {
            selectedSortType = "ZToA";

            tvNewToOldDate.setTextColor(Color.parseColor("#7A7A7A"));
            tvOldToNewDate.setTextColor(Color.parseColor("#7A7A7A"));
            tvNameAtoZ.setTextColor(Color.parseColor("#7A7A7A"));
            tvNameZtoA.setTextColor(Color.parseColor("#FFFFFF"));
            tvSmallToBigSize.setTextColor(Color.parseColor("#7A7A7A"));
            tvBigToSmallSize.setTextColor(Color.parseColor("#7A7A7A"));

            tvNewToOldDate.setBackground(ContextCompat.getDrawable(Videos23rdActivity.this, R.drawable.stoke_bg_23rd));
            tvOldToNewDate.setBackground(ContextCompat.getDrawable(Videos23rdActivity.this, R.drawable.stoke_bg_23rd));
            tvNameAtoZ.setBackground(ContextCompat.getDrawable(Videos23rdActivity.this, R.drawable.stoke_bg_23rd));
            tvNameZtoA.setBackground(ContextCompat.getDrawable(Videos23rdActivity.this, R.drawable.ic_install_bg_23rd));
            tvSmallToBigSize.setBackground(ContextCompat.getDrawable(Videos23rdActivity.this, R.drawable.stoke_bg_23rd));
            tvBigToSmallSize.setBackground(ContextCompat.getDrawable(Videos23rdActivity.this, R.drawable.stoke_bg_23rd));
        });
        tvSmallToBigSize.setOnClickListener(v -> {
            selectedSortType = "smallToBig";

            tvNewToOldDate.setTextColor(Color.parseColor("#7A7A7A"));
            tvOldToNewDate.setTextColor(Color.parseColor("#7A7A7A"));
            tvNameAtoZ.setTextColor(Color.parseColor("#7A7A7A"));
            tvNameZtoA.setTextColor(Color.parseColor("#7A7A7A"));
            tvSmallToBigSize.setTextColor(Color.parseColor("#FFFFFF"));
            tvBigToSmallSize.setTextColor(Color.parseColor("#7A7A7A"));

            tvNewToOldDate.setBackground(ContextCompat.getDrawable(Videos23rdActivity.this, R.drawable.stoke_bg_23rd));
            tvOldToNewDate.setBackground(ContextCompat.getDrawable(Videos23rdActivity.this, R.drawable.stoke_bg_23rd));
            tvNameAtoZ.setBackground(ContextCompat.getDrawable(Videos23rdActivity.this, R.drawable.stoke_bg_23rd));
            tvNameZtoA.setBackground(ContextCompat.getDrawable(Videos23rdActivity.this, R.drawable.stoke_bg_23rd));
            tvSmallToBigSize.setBackground(ContextCompat.getDrawable(Videos23rdActivity.this, R.drawable.ic_install_bg_23rd));
            tvBigToSmallSize.setBackground(ContextCompat.getDrawable(Videos23rdActivity.this, R.drawable.stoke_bg_23rd));
        });
        tvBigToSmallSize.setOnClickListener(v -> {
            selectedSortType = "bigToSmall";

            tvNewToOldDate.setTextColor(Color.parseColor("#7A7A7A"));
            tvOldToNewDate.setTextColor(Color.parseColor("#7A7A7A"));
            tvNameAtoZ.setTextColor(Color.parseColor("#7A7A7A"));
            tvNameZtoA.setTextColor(Color.parseColor("#7A7A7A"));
            tvSmallToBigSize.setTextColor(Color.parseColor("#7A7A7A"));
            tvBigToSmallSize.setTextColor(Color.parseColor("#FFFFFF"));

            tvNewToOldDate.setBackground(ContextCompat.getDrawable(Videos23rdActivity.this, R.drawable.stoke_bg_23rd));
            tvOldToNewDate.setBackground(ContextCompat.getDrawable(Videos23rdActivity.this, R.drawable.stoke_bg_23rd));
            tvNameAtoZ.setBackground(ContextCompat.getDrawable(Videos23rdActivity.this, R.drawable.stoke_bg_23rd));
            tvNameZtoA.setBackground(ContextCompat.getDrawable(Videos23rdActivity.this, R.drawable.stoke_bg_23rd));
            tvSmallToBigSize.setBackground(ContextCompat.getDrawable(Videos23rdActivity.this, R.drawable.stoke_bg_23rd));
            tvBigToSmallSize.setBackground(ContextCompat.getDrawable(Videos23rdActivity.this, R.drawable.ic_install_bg_23rd));
        });

        bottomSheetDialog.show();
    }

    private void sortVideos(String sortType) {
        switch (sortType) {
            case "newToOld":
                videoInformationArrayList.sort((v1, v2) -> Long.compare(v2.getDateAdded(), v1.getDateAdded()));
                break;
            case "oldToNew":
                videoInformationArrayList.sort(Comparator.comparingLong(GalleryVideoInformation23rd::getDateAdded));
                break;
            case "AToZ":
                videoInformationArrayList.sort((v1, v2) -> v1.getTitle().compareToIgnoreCase(v2.getTitle()));
                break;
            case "ZToA":
                videoInformationArrayList.sort((v1, v2) -> v2.getTitle().compareToIgnoreCase(v1.getTitle()));
                break;
            case "smallToBig":
                videoInformationArrayList.sort(Comparator.comparingLong(GalleryVideoInformation23rd::getSize));
                break;
            case "bigToSmall":
                videoInformationArrayList.sort((v1, v2) -> Long.compare(v2.getSize(), v1.getSize()));
                break;
        }
    }

    private void setLayoutManager() {
        if (isGrid) {
            int numberOfColumns = 2;
            rvVideoList.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
        } else {
            rvVideoList.setLayoutManager(new LinearLayoutManager(this));
        }
    }

    public void loadShowAds() {
        adController23rd.newreleasenativePreloadExtra(this, prefManagerVideo22nd.getString(TAG_NATIVEIDD), 2, flSmallSizeNative, true);
    }

    public void loadAdNextTime() {
        if (shouldExecuteOnResume) {
            if (prefManagerVideo22nd.getString("extrascreennative").contains("1")) {
                if (flSmallSizeNative != null) {
                    flSmallSizeNative.removeAllViews();
                }
                if ((flSmallSizeNative != null) && (flSmallSizeNative.getChildCount() == 0)) {
                    adController23rd.newreleasenativePreloadExtra(Videos23rdActivity.this, prefManagerVideo22nd.getString(TAG_NATIVEIDD), 2, flSmallSizeNative, true);
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
        adController23rd.showInterAdCallBack(Videos23rdActivity.this, this::superOnBackPressed);
    }

    private void superOnBackPressed() {
        super.onBackPressed();
    }

    public void loadData() {
        FetchStorageVideo23rd.showLoader(Videos23rdActivity.this);
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                ArrayList<GalleryVideoInformation23rd> videoFolders = FetchStorageVideo23rd.getGalleryAllVideos(Videos23rdActivity.this);

                if (prefManagerVideo22nd.getString("selectedSortType").isEmpty()) {
                    videoFolders.sort(new Comparator<GalleryVideoInformation23rd>() {
                        @Override
                        public int compare(GalleryVideoInformation23rd v1, GalleryVideoInformation23rd v2) {
                            return Long.compare(v2.getDateAdded(), v1.getDateAdded());
                        }
                    });
                } else {
                    if (prefManagerVideo22nd.getString("selectedSortType").equals("newToOld")) {
                        videoFolders.sort((v1, v2) -> Long.compare(v2.getDateAdded(), v1.getDateAdded()));
                    } else if (prefManagerVideo22nd.getString("selectedSortType").equals("oldToNew")) {
                        videoFolders.sort(Comparator.comparingLong(GalleryVideoInformation23rd::getDateAdded));
                    } else if (prefManagerVideo22nd.getString("selectedSortType").equals("AToZ")) {
                        videoFolders.sort((v1, v2) -> v1.getTitle().compareToIgnoreCase(v2.getTitle()));
                    } else if (prefManagerVideo22nd.getString("selectedSortType").equals("ZToA")) {
                        videoFolders.sort((v1, v2) -> v2.getTitle().compareToIgnoreCase(v1.getTitle()));
                    } else if (prefManagerVideo22nd.getString("selectedSortType").equals("smallToBig")) {
                        videoFolders.sort(Comparator.comparingLong(GalleryVideoInformation23rd::getSize));
                    } else if (prefManagerVideo22nd.getString("selectedSortType").equals("bigToSmall")) {
                        videoFolders.sort((v1, v2) -> Long.compare(v2.getSize(), v1.getSize()));
                    }
                }

                runOnUiThread(new Runnable() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void run() {
                        FetchStorageVideo23rd.dismissLoader();
                        videoInformationArrayList.addAll(videoFolders);
                        videosAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Videos23rdAdapter.DELETE_REQUEST_CODE) {
            if (resultCode != 0) {
                videosAdapter.notifyItemRemoved(videosAdapter.getPos());
                videosAdapter.notifyDataSetChanged();
                Toast.makeText(Videos23rdActivity.this, "Video Deleted successfully", Toast.LENGTH_SHORT).show();
            }
        }
    }

}