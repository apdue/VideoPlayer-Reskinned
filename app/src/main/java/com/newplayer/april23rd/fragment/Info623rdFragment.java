package com.newplayer.april23rd.fragment;

import static com.newplayer.april23rd.AdsManger.Splash23rdActivity.TAG_NATIVEID;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.newplayer.april23rd.AdsManger.AdController23rd;
import com.newplayer.april23rd.AdsManger.PrefManagerVideo23rd;
import com.newplayer.april23rd.R;

public class Info623rdFragment extends Fragment {
    View view;
    FrameLayout flBigSizeNative;
    Context context;
    PrefManagerVideo23rd prefManagerVideo22nd;
    AdController23rd adController23rd;
    Activity activity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.info_6_layout_23rd, container, false);

        initViews();
        loadShowAds();
        return view;
    }

    public void initViews() {
        context = activity.getApplicationContext();
        prefManagerVideo22nd = new PrefManagerVideo23rd(context);
        adController23rd = AdController23rd.getInstance();
        flBigSizeNative = view.findViewById(R.id.flBigSizeNative);
    }

    public void loadShowAds() {
        adController23rd.newreleasenativePreload(activity, prefManagerVideo22nd.getString(TAG_NATIVEID), 1, flBigSizeNative, true);
    }
}