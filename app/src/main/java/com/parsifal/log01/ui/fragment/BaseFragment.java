package com.parsifal.log01.ui.fragment;

import android.support.v4.app.Fragment;

/**
 * Created by YangMing on 2016/8/25 16:01.
 */
public class BaseFragment extends Fragment {

    protected OnFragmentLoadListener mLoadListener = null;

    public void setDataSource(String[] src) {

    }

    public void setFragmentLoadListener(OnFragmentLoadListener loadListener) {
        mLoadListener = loadListener;
    }
}
