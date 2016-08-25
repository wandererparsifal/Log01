package com.parsifal.log01.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.parsifal.log01.ui.fragment.BaseFragment;

import java.util.List;


/**
 * Created by YangMing on 2016/8/24.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {

    private List<BaseFragment> fragments;

    public ViewPagerAdapter(FragmentManager fm, List<BaseFragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    public Fragment getItem(int fragment) {
        return fragments.get(fragment);
    }

    public int getCount() {
        return fragments.size();
    }
}