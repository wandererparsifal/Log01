package com.parsifal.log01.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
public class FragmentWork extends Fragment {

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
        String[] samples = {"07:55",
                "08:00", "07:56", "07:54", "07:55", "07:54",
                "07:53", "07:49", "07:46", "07:46", "07:53",
                "07:52", "07:54", "07:48", "07:57", "07:56",
                "07:46", "07:47", "07:49", "07:45", "07:53"};
        mSSView.setDataSource(samples);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e("tag", "FragmentHome onStart");
    }
}
