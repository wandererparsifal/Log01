package com.parsifal.log01.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.parsifal.log01.LogApplication;
import com.parsifal.log01.utils.LogUtil;

import java.util.Date;

/**
 * Created by YangMing on 2016/8/24 15:45.
 */
public class LocateService extends Service {

    private static final String TAG = LocateService.class.getSimpleName();

    private LogApplication mApplication = null;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        LogUtil.i(TAG, "LocateService onCreate");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtil.i(TAG, "LocateService onStartCommand");
        mApplication = (LogApplication) getApplication();
        mApplication.locate(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                mApplication.saveToFile(new Date(), aMapLocation);
                stopSelf();
            }
        });
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        LogUtil.i(TAG, "LocateService onDestroy");
    }
}
