package com.newplayer.april23rd.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.newplayer.april23rd.fragment.TrendingVideo23rdFragment;
import com.newplayer.april23rd.model.TrendVideoData23rd;

import java.util.ArrayList;

public class TrendingVideo23rdAdapter extends FragmentPagerAdapter {
    private ArrayList<TrendVideoData23rd> trendVideoData23rdArrayList;
    public TrendingVideo23rdAdapter(FragmentManager fm, ArrayList<TrendVideoData23rd> trendVideoData23rdArrayList) {
        super(fm);
        this.trendVideoData23rdArrayList = trendVideoData23rdArrayList;
    }
    @NonNull
    @Override
    public Fragment getItem(int position) {
        return TrendingVideo23rdFragment.newInstance(trendVideoData23rdArrayList.get(position));
    }
    @Override
    public int getCount() {
        return trendVideoData23rdArrayList.size();
    }
}
