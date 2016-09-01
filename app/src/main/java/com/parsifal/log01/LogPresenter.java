package com.parsifal.log01;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.parsifal.log01.view.BaseView;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by YangMing on 2016/9/1 12:51.
 */
public interface LogPresenter {

    void setView(BaseView view);

    void locate(AMapLocationListener aMapLocationListener);

    void release();

    void saveToFile(Date date, AMapLocation aMapLocation);

    void loadFromSP();

    void setAlarm(Calendar calendar);

    void cancelAlarm();
}
