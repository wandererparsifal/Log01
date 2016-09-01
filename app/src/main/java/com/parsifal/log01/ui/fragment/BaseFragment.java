package com.parsifal.log01.ui.fragment;

import android.support.v4.app.Fragment;

import com.parsifal.log01.ui.view.StatisticsData;

/**
 * Created by YangMing on 2016/8/25 16:01.
 */
public class BaseFragment extends Fragment {

    protected OnFragmentLoadListener mLoadListener = null;

    public void setDataSource(StatisticsData data) {

    }

    public void setFragmentLoadListener(OnFragmentLoadListener loadListener) {
        mLoadListener = loadListener;
    }
}
