package com.parsifal.log01.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parsifal.log01.R;
import com.parsifal.log01.ui.view.StatisticsSurfaceView;

/**
 * Created by YangMing on 2016/8/24.
 */
public class FragmentWork extends BaseFragment {

    private StatisticsSurfaceView mSSView = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_work, container, false);

        mSSView = (StatisticsSurfaceView) view.findViewById(R.id.sv_work);
        mSSView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.CornSilk));

        if (null != mLoadListener) {
            mLoadListener.onCreateViewComplete();
        }
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e("tag", "FragmentWork onStart");
    }

    @Override
    public void setDataSource(String[] src) {
        super.setDataSource(src);
        mSSView.setDataSource(src);
    }
}
