package com.parsifal.log01;

import android.app.Application;
import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.services.core.LatLonPoint;
import com.google.gson.Gson;
import com.parsifal.log01.bean.TimeAndPlace;
import com.parsifal.log01.broadcastreceiver.NotificationReceiver;
import com.parsifal.log01.utils.AlarmUtil;
import com.parsifal.log01.utils.FileUtil;
import com.parsifal.log01.utils.LogUtil;
import com.parsifal.log01.utils.MapBaseUtil;
import com.parsifal.log01.view.BaseView;
import com.parsifal.log01.view.StatisticsView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;

/**
 * Created by YangMing on 2016/9/1 12:02.
 */
public class LogApplication extends Application implements LogPresenter {

    private static final String TAG = LogApplication.class.getSimpleName();

    private BaseView mView = null;

    private MapBaseUtil mMapUtil = null;

    private FileUtil mFileUtil = null;

    private AlarmUtil mAlarmUtil = null;

    private SimpleDateFormat mDateFormat = null;

    private static final String PATTERN = "HH:mm";

    @Override
    public void onCreate() {
        super.onCreate();
        mMapUtil = new MapBaseUtil();
        mMapUtil.init(this);
        mFileUtil = FileUtil.getInstance();
        mAlarmUtil = AlarmUtil.getInstance();
        mAlarmUtil.init(this);
        mDateFormat = new SimpleDateFormat(PATTERN, Locale.getDefault());
    }

    @Override
    public void setView(BaseView view) {
        mView = view;
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

        String json = mFileUtil.load();

        LatLonPoint latLonPoint = new LatLonPoint(aMapLocation.getLatitude(), aMapLocation.getLongitude());
        TimeAndPlace timeAndPlace = new TimeAndPlace(date, latLonPoint);
        if (null == json || 0 == json.length()) {
            TimeAndPlace[] timeAndPlaces = new TimeAndPlace[1];
            timeAndPlaces[0] = timeAndPlace;
            json = new Gson().toJson(timeAndPlaces, TimeAndPlace[].class);
            mFileUtil.save(json);
        } else {
            TimeAndPlace[] originalArray = new Gson().fromJson(json, TimeAndPlace[].class);
            int originalLen = originalArray.length;
            TimeAndPlace[] newArray = new TimeAndPlace[originalLen + 1];
            for (int i = 0; i < originalLen; i++) {
                newArray[i] = originalArray[i];
            }
            newArray[originalArray.length] = timeAndPlace;
            json = new Gson().toJson(newArray, TimeAndPlace[].class);
            mFileUtil.save(json);
        }
    }

    @Override
    public void loadFromFile() {
        String json = mFileUtil.load();
        Log.i(TAG, "loadFromFile json " + json);
        if (null != json) {
            TimeAndPlace[] timeAndPlaces = new Gson().fromJson(json, TimeAndPlace[].class);
            int length = timeAndPlaces.length;
            LinkedList<Calendar> calendars = new LinkedList<Calendar>();

            LinkedList<String> list_work = new LinkedList<String>();
            LinkedList<String> list_home = new LinkedList<String>();
            TimeAndPlace timeAndPlace = null;
            int day_before = 0;
            for (int i = 0; i < length; i++) {
                timeAndPlace = timeAndPlaces[i];
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(timeAndPlace.date);
                day_before = calendar.get(Calendar.DAY_OF_MONTH);
                if (0 == calendars.size() || calendars.getLast().get(Calendar.DAY_OF_MONTH) == day_before) {
                    calendars.add(calendar);
                } else {
                    Calendar first = calendars.getFirst();
                    int hour_first = first.get(Calendar.HOUR_OF_DAY);
                    if (12 > hour_first) {
                        list_work.add(mDateFormat.format(first.getTime()));
                    }
                    Calendar last = calendars.getLast();
                    int hour_last = last.get(Calendar.HOUR_OF_DAY);
                    if (12 <= hour_last) {
                        list_home.add(mDateFormat.format(last.getTime()));
                    }
                    calendars.clear();
                    calendars.add(calendar);
                }
                if (i == length - 1) {
                    Calendar first = calendars.getFirst();
                    int hour_first = first.get(Calendar.HOUR_OF_DAY);
                    if (12 > hour_first) {
                        list_work.add(mDateFormat.format(first.getTime()));
                    }
                    Calendar last = calendars.getLast();
                    int hour_last = last.get(Calendar.HOUR_OF_DAY);
                    if (12 <= hour_last) {
                        list_home.add(mDateFormat.format(last.getTime()));
                    }
                }
            }
            String[] samples1 = null;
            String[] samples2 = null;
            if (0 != list_work.size()) {
                samples1 = new String[list_work.size()];
                samples1 = list_work.toArray(samples1);
            }
            if (0 != list_home.size()) {
                samples2 = new String[list_home.size()];
                samples2 = list_home.toArray(samples2);
            }
            if (mView instanceof StatisticsView) {
                ((StatisticsView) mView).drawGraphics(samples1, samples2);
            }
        }
    }

    @Override
    public void setAlarm() {
        LogUtil.i(TAG, "setAlarm");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.SECOND, 5);

        Intent intent = new Intent(this, NotificationReceiver.class);
        PendingIntent contentIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

        mAlarmUtil.setAlarm(calendar, contentIntent);
    }

    @Override
    public void cancelAlarm() {
        LogUtil.i(TAG, "cancelAlarm");
        mAlarmUtil.cancel();
    }
}
