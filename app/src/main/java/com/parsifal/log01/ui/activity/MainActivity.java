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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;

import com.google.gson.Gson;
import com.parsifal.log01.LogApplication;
import com.parsifal.log01.service.LocateService;
import com.parsifal.log01.R;
import com.parsifal.log01.ui.view.StatisticsData;
import com.parsifal.log01.utils.SharedPreferencesUtil;

import java.util.Calendar;
import java.util.Date;

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
        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.i(TAG, "onCheckedChanged " + isChecked);
                if (isChecked) {
                    String json1 = SharedPreferencesUtil.getInstance().load("data_work");
                    String json2 = SharedPreferencesUtil.getInstance().load("data_home");
                    StatisticsData data1 = null;
                    StatisticsData data2 = null;
                    int time1 = 0;
                    int time2 = 0;
                    if (null != json1) {
                        data1 = new Gson().fromJson(json1, StatisticsData.class);
                        time1 = (int) data1.ц;
                    }
                    if (null != json2) {
                        data2 = new Gson().fromJson(json2, StatisticsData.class);
                        time2 = (int) data2.ц;
                    }
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(new Date());
                    int hour = calendar.get(Calendar.HOUR_OF_DAY);
                    if (12 > hour) {
                        if (0 != time2) {
                            calendar.set(Calendar.HOUR_OF_DAY, time2 / 60);
                            calendar.set(Calendar.MINUTE, time2 % 60);
                            mApplication.setAlarm(calendar);
                        } else {
                            if (0 != time1) {
                                calendar.add(Calendar.DAY_OF_MONTH, 1);
                                calendar.set(Calendar.HOUR_OF_DAY, time1 / 60);
                                calendar.set(Calendar.MINUTE, time1 % 60);
                                mApplication.setAlarm(calendar);
                            }
                        }
                    } else {
                        if (0 != time1) {
                            calendar.add(Calendar.DAY_OF_MONTH, 1);
                            calendar.set(Calendar.HOUR_OF_DAY, time1 / 60);
                            calendar.set(Calendar.MINUTE, time1 % 60);
                            mApplication.setAlarm(calendar);
                        } else {
                            if (0 != time2) {
                                calendar.add(Calendar.DAY_OF_MONTH, 1);
                                calendar.set(Calendar.HOUR_OF_DAY, time2 / 60);
                                calendar.set(Calendar.MINUTE, time2 % 60);
                                mApplication.setAlarm(calendar);
                            }
                        }
                    }
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
        Log.i(TAG, "onRequestPermissionsResult");
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        allGranted = true;
        for (int i = 0; i < permissions.length; i++) {
            if (PackageManager.PERMISSION_GRANTED != grantResults[i]) {
                allGranted = false;
                Log.e(TAG, "Permission is Denied " + permissions[i]);
            }
        }
        if (allGranted) {
            Log.i(TAG, "Permission ok.");
            mBtnWork.setEnabled(true);
            mBtnHome.setEnabled(true);
        } else {
            Log.w(TAG, "allGranted " + allGranted);
            mBtnWork.setEnabled(false);
            mBtnHome.setEnabled(false);
        }
    }
}
