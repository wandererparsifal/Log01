package com.parsifal.log01.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.parsifal.log01.presenter.LocatePresenter;
import com.parsifal.log01.presenter.LocatePresenterImpl;
import com.parsifal.log01.view.LocateView;

import java.util.Date;

/**
 * Created by YangMing on 2016/8/24 15:45.
 */
public class LocateService extends Service implements LocateView {

    private static final String TAG = LocateService.class.getSimpleName();

    private LocatePresenter mLocatePresenter = null;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.i(TAG, "onCreate");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand");
        mLocatePresenter = new LocatePresenterImpl(this, this);
        mLocatePresenter.locate(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                mLocatePresenter.saveToFile(new Date(), aMapLocation);
                stopSelf();
            }
        });
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy");
        mLocatePresenter.release();
    }
}
