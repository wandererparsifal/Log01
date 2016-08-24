package com.parsifal.log01.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.parsifal.log01.service.LocateService;
import com.parsifal.log01.R;

public class LocateActivity extends AppCompatActivity {

    private static final String TAG = LocateActivity.class.getSimpleName();

    private Button mBtnWork = null;

    private Button mBtnHome = null;

    private Button mBtnStatistics = null;

    private View.OnClickListener mLocationOnClickListener = null;

    private View.OnClickListener mActivityOnClickListener = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBtnWork = (Button) findViewById(R.id.button_work);
        mBtnHome = (Button) findViewById(R.id.button_home);
        mBtnStatistics = (Button) findViewById(R.id.button_statistics);

        mLocationOnClickListener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startService(new Intent(LocateActivity.this, LocateService.class));
                finish();
            }
        };
        mActivityOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LocateActivity.this, StatisticsActivity.class));
            }
        };

        mBtnWork.setOnClickListener(mLocationOnClickListener);
        mBtnHome.setOnClickListener(mLocationOnClickListener);
        mBtnStatistics.setOnClickListener(mActivityOnClickListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
