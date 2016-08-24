package com.parsifal.log01.presenter;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;

import java.util.Date;

/**
 * Created by YangMing on 2016/8/24 14:54.
 */
public interface LocatePresenter {

    void locate(AMapLocationListener aMapLocationListener);

    void release();

    void saveToFile(Date date, AMapLocation aMapLocation);
}
