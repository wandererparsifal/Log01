package com.parsifal.log01.presenter;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.services.core.LatLonPoint;
import com.google.gson.Gson;
import com.parsifal.log01.bean.TimeAndPlace;
import com.parsifal.log01.utils.FileUtil;
import com.parsifal.log01.utils.MapBaseUtil;
import com.parsifal.log01.view.LocateView;

import java.io.File;
import java.util.Date;

/**
 * Created by YangMing on 2016/8/24 14:55.
 */
public class LocatePresenterImpl implements LocatePresenter {

    private static final String TAG = LocatePresenterImpl.class.getSimpleName();

    private MapBaseUtil mMapUtil = null;

    private LocateView mLocateView = null;

    public LocatePresenterImpl(Context context, LocateView locateView) {
        mMapUtil = new MapBaseUtil();
        mMapUtil.init(context);
        mLocateView = locateView;
    }

    @Override
    public void locate(AMapLocationListener aMapLocationListener) {
        mMapUtil.locate(aMapLocationListener);
    }

    @Override
    public void release() {
        mMapUtil.release();
    }

    @Override
    public void saveToFile(Date date, AMapLocation aMapLocation) {
        File sdDir = Environment.getExternalStorageDirectory();
        String path = sdDir.getAbsolutePath() + "/TEC_WORK_LOG.log";

        FileUtil fileUtil = new FileUtil();
        String json = fileUtil.load(path);

        LatLonPoint latLonPoint = new LatLonPoint(aMapLocation.getLatitude(), aMapLocation.getLongitude());
        TimeAndPlace timeAndPlace = new TimeAndPlace(date, latLonPoint);
        if (null == json || 0 == json.length()) {
            TimeAndPlace[] timeAndPlaces = new TimeAndPlace[1];
            timeAndPlaces[0] = timeAndPlace;
            json = new Gson().toJson(timeAndPlaces, TimeAndPlace[].class);
            fileUtil.save(path, json);
        } else {
            TimeAndPlace[] originalArray = new Gson().fromJson(json, TimeAndPlace[].class);
            int originalLen = originalArray.length;
            TimeAndPlace[] newArray = new TimeAndPlace[originalLen + 1];
            for (int i = 0; i < originalLen; i++) {
                newArray[i] = originalArray[i];
            }
            newArray[originalArray.length] = timeAndPlace;
            json = new Gson().toJson(newArray, TimeAndPlace[].class);
            fileUtil.save(path, json);
        }
    }
}
