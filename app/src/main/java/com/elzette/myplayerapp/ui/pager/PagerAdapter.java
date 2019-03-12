package com.elzette.myplayerapp.ui.pager;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.elzette.myplayerapp.R;
import com.elzette.myplayerapp.models.TabPageItemModel;
import com.elzette.myplayerapp.ui.SongListFragment;
import com.elzette.myplayerapp.ui.SongStatusFragment;

import java.util.ArrayList;
import java.util.List;

public class PagerAdapter extends FragmentPagerAdapter implements ViewPager.OnPageChangeListener {

    private final static int NUM_ITEMS = 4;


    private List<TabPageItemModel> mModels;

    PagerAdapter(FragmentManager fm) {
        super(fm);

        mModels = new ArrayList<>();
        mModels.add(new TabPageItemModel(0, "Current song", 0));
        mModels.add(new TabPageItemModel(1, "Songs", R.layout.song_item));
        mModels.add(new TabPageItemModel(2, "Albums", R.layout.album_item));
        mModels.add(new TabPageItemModel(3, "Artists", R.layout.artist_item));
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mModels.get(position).getTitle();
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
        switch (position) {
            case 0:
                fragment = new SongStatusFragment();
                break;
            default:
                fragment = new SongListFragment();
                break;
        }
        Bundle bundle = new Bundle();
//        bundle.putInt(LAYOUT_ID, mModels.get(position).getmViewHolderResourceId());
//        bundle.putInt(LAYOUT_POSITION, position);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {
    }

    @Override
    public void onPageSelected(int i) {

    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }
}