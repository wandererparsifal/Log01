package com.parsifal.log01;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.parsifal.log01.view.StatisticsView;

/**
 * Created by YangMing on 2016/8/24 15:16.
 */
public class StatisticsActivity extends AppCompatActivity implements StatisticsView {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
    }
}
