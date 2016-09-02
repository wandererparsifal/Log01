package com.parsifal.log01.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;

import com.parsifal.log01.LogApplication;
import com.parsifal.log01.service.LocateService;
import com.parsifal.log01.R;
import com.parsifal.log01.utils.LogUtil;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private LogApplication mApplication = null;

    private Toolbar mToolbar = null;

    private SwitchCompat mSwitch = null;

    private Button mBtnWork = null;

    private Button mBtnHome = null;

    private Button mBtnStatistics = null;

    private View.OnClickListener mLocationOnClickListener = null;

    private View.OnClickListener mActivityOnClickListener = null;

    private boolean allGranted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApplication = (LogApplication) getApplication();
        if (23 <= Build.VERSION.SDK_INT) {
            verifyStoragePermissions(this);
        } else {
            allGranted = true;
        }
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setTitle(R.string.app_name);
        mBtnWork = (Button) findViewById(R.id.button_work);
        mBtnHome = (Button) findViewById(R.id.button_home);
        mBtnStatistics = (Button) findViewById(R.id.button_statistics);

        mLocationOnClickListener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startService(new Intent(MainActivity.this, LocateService.class));
                finish();
            }
        };
        mActivityOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, StatisticsActivity.class));
            }
        };

        mBtnWork.setOnClickListener(mLocationOnClickListener);
        mBtnHome.setOnClickListener(mLocationOnClickListener);
        mBtnStatistics.setOnClickListener(mActivityOnClickListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_switch, menu);
        MenuItem item = menu.findItem(R.id.switch_menu);
        item.setActionView(R.layout.layout_switch);
        mSwitch = (SwitchCompat) item.getActionView().findViewById(R.id.switchForActionBar);
        boolean isAlarmOn = mApplication.isAlarmOn();
        mSwitch.setChecked(isAlarmOn);
        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                LogUtil.i(TAG, "onCheckedChanged " + isChecked);
                if (isChecked) {
                    mApplication.setAlarmOn();
                    mApplication.setAlarm();
                } else {
                    mApplication.cancelAlarm();
                }
            }
        });
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_PHONE_STATE
    };

    public static void verifyStoragePermissions(Activity activity) {
        ActivityCompat.requestPermissions(
                activity,
                PERMISSIONS_STORAGE,
                REQUEST_EXTERNAL_STORAGE
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        LogUtil.i(TAG, "onRequestPermissionsResult");
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        allGranted = true;
        for (int i = 0; i < permissions.length; i++) {
            if (PackageManager.PERMISSION_GRANTED != grantResults[i]) {
                allGranted = false;
                LogUtil.e(TAG, "Permission is Denied " + permissions[i]);
            }
        }
        if (allGranted) {
            LogUtil.i(TAG, "Permission ok.");
            mBtnWork.setEnabled(true);
            mBtnHome.setEnabled(true);
        } else {
            LogUtil.w(TAG, "allGranted " + allGranted);
            mBtnWork.setEnabled(false);
            mBtnHome.setEnabled(false);
        }
    }
}
