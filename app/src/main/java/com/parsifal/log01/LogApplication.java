package com.parsifal.log01;

import android.app.Application;
import android.app.PendingIntent;
import android.content.Intent;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.services.core.LatLonPoint;
import com.parsifal.log01.bean.TimeAndPlace;
import com.parsifal.log01.broadcastreceiver.NotificationReceiver;
import com.parsifal.log01.ui.view.StatisticsData;
import com.parsifal.log01.utils.AlarmUtil;
import com.parsifal.log01.utils.FileUtil;
import com.parsifal.log01.utils.JsonUtil;
import com.parsifal.log01.utils.LogUtil;
import com.parsifal.log01.utils.MapBaseUtil;
import com.parsifal.log01.utils.MathUtil;
import com.parsifal.log01.utils.SharedPreferencesUtil;
import com.parsifal.log01.view.BaseView;
import com.parsifal.log01.view.StatisticsView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;

/**
 * Created by YangMing on 2016/9/1 12:02.
 */
public class LogApplication extends Application {

    private static final String TAG = LogApplication.class.getSimpleName();

    private BaseView mView = null;

    private JsonUtil mJsonUtil = null;

    private MapBaseUtil mMapUtil = null;

    private FileUtil mFileUtil = null;

    private AlarmUtil mAlarmUtil = null;

    private SharedPreferencesUtil mSPUtil = null;

    private SimpleDateFormat mDateFormat = null;

    private static final String PATTERN = "HH:mm";

    private static final String KEY_WORK = "data_work";

    private static final String KEY_HOME = "data_home";

    @Override
    public void onCreate() {
        super.onCreate();
        mJsonUtil = JsonUtil.getInstance();
        mMapUtil = new MapBaseUtil();
        mMapUtil.init(this);
        mFileUtil = FileUtil.getInstance();
        mAlarmUtil = AlarmUtil.getInstance();
        mAlarmUtil.init(this);
        mSPUtil = SharedPreferencesUtil.getInstance();
        mSPUtil.init(this);
        mDateFormat = new SimpleDateFormat(PATTERN, Locale.getDefault());
    }

    public void setView(BaseView view) {
        mView = view;
    }

    public void locate(AMapLocationListener aMapLocationListener) {
        mMapUtil.locate(aMapLocationListener);
    }

    public void release() {
        mMapUtil.release();
    }

    public void saveToFile(Date date, AMapLocation aMapLocation) {

        String json = mFileUtil.load();

        LatLonPoint latLonPoint = new LatLonPoint(aMapLocation.getLatitude(), aMapLocation.getLongitude());
        TimeAndPlace timeAndPlace = new TimeAndPlace(date, latLonPoint);
        if (null == json || 0 == json.length()) {
            TimeAndPlace[] timeAndPlaces = new TimeAndPlace[1];
            timeAndPlaces[0] = timeAndPlace;
            saveToSP(timeAndPlaces);
            json = mJsonUtil.toJson(timeAndPlaces, TimeAndPlace[].class);
            mFileUtil.save(json);
        } else {
            TimeAndPlace[] originalArray = mJsonUtil.fromJson(json, TimeAndPlace[].class);
            int originalLen = originalArray.length;
            TimeAndPlace[] newArray = new TimeAndPlace[originalLen + 1];
            for (int i = 0; i < originalLen; i++) {
                newArray[i] = originalArray[i];
            }
            newArray[originalArray.length] = timeAndPlace;
            saveToSP(newArray);
            json = mJsonUtil.toJson(newArray, TimeAndPlace[].class);
            mFileUtil.save(json);
        }
    }

    public void saveToSP(TimeAndPlace[] timeAndPlaces) {
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
        StatisticsData data1 = getStatisticsData(samples1);
        if (null != data1) {
            mSPUtil.save(KEY_WORK, mJsonUtil.toJson(data1, StatisticsData.class));
        }
        StatisticsData data2 = getStatisticsData(samples2);
        if (null != data2) {
            mSPUtil.save(KEY_HOME, mJsonUtil.toJson(data2, StatisticsData.class));
        }

        setAlarm();
    }

    private StatisticsData getStatisticsData(String[] samples) {
        if (null == samples) {
            return null;
        }
        StatisticsData data = new StatisticsData();
        int samplesCount = samples.length;
        double[] times = new double[samplesCount];
        Calendar calendar = Calendar.getInstance();
        for (int i = 0; i < samplesCount; i++) {
            try {
                String dateString = samples[i];
                Date date = mDateFormat.parse(dateString);
                calendar.setTime(date);
                times[i] = calendar.get(Calendar.HOUR_OF_DAY) * 60 + calendar.get(Calendar.MINUTE);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        double ц = MathUtil.getAverage(times);
        System.out.println("ц - " + ц);
        double σ = MathUtil.getStandardDeviation(times);
        System.out.println("σ - " + σ);

        double min = MathUtil.getMin(times);
        double max = MathUtil.getMax(times);
        data.ц = ц;
        data.σ = σ;
        data.min = min;
        data.max = max;
        data.times = times;
        return data;
    }

    public void loadFromSP() {
        String json1 = mSPUtil.load(KEY_WORK);
        String json2 = mSPUtil.load(KEY_HOME);
        StatisticsData data1 = null;
        StatisticsData data2 = null;
        if (null != json1) {
            data1 = mJsonUtil.fromJson(json1, StatisticsData.class);
        }
        if (null != json2) {
            data2 = mJsonUtil.fromJson(json2, StatisticsData.class);
        }
        if (mView instanceof StatisticsView) {
            ((StatisticsView) mView).drawGraphics(data1, data2);
        }
    }

    public boolean setAlarm() {
        LogUtil.i(TAG, "setAlarm");

        String json1 = mSPUtil.load(KEY_WORK);
        String json2 = mSPUtil.load(KEY_HOME);
        int time1 = 0;
        int time2 = 0;
        if (null != json1) {
            StatisticsData data1 = mJsonUtil.fromJson(json1, StatisticsData.class);
            time1 = (int) data1.ц;
        }
        if (null != json2) {
            StatisticsData data2 = mJsonUtil.fromJson(json2, StatisticsData.class);
            time2 = (int) data2.ц;
        }

        boolean valid = true;
        Calendar calendar = Calendar.getInstance();
        int timeNow = calendar.get(Calendar.HOUR_OF_DAY) * 60 + calendar.get(Calendar.MINUTE);
        if (0 != time1 && 0 != time2) {
            if (timeNow < time1) { // This Morning
                calendar.set(Calendar.HOUR_OF_DAY, time1 / 60);
                calendar.set(Calendar.MINUTE, time1 % 60);
                String alarmTime = mJsonUtil.toJson(calendar, Calendar.class);
                LogUtil.i(TAG, "setAlarm This Morning, alarmTime is " + alarmTime);
            } else if (timeNow < time2) { // This Afternoon
                calendar.set(Calendar.HOUR_OF_DAY, time2 / 60);
                calendar.set(Calendar.MINUTE, time2 % 60);
                String alarmTime = mJsonUtil.toJson(calendar, Calendar.class);
                LogUtil.i(TAG, "setAlarm This Afternoon, alarmTime is " + alarmTime);
            } else { // Next Morning
                getNextWorkDay(calendar);
                calendar.set(Calendar.HOUR_OF_DAY, time1 / 60);
                calendar.set(Calendar.MINUTE, time1 % 60);
                String alarmTime = mJsonUtil.toJson(calendar, Calendar.class);
                LogUtil.i(TAG, "setAlarm Next Morning, alarmTime is " + alarmTime);
            }
        } else if (0 != time1 && 0 == time2) {
            if (timeNow < time1) { // This Morning
                calendar.set(Calendar.HOUR_OF_DAY, time1 / 60);
                calendar.set(Calendar.MINUTE, time1 % 60);
                String alarmTime = mJsonUtil.toJson(calendar, Calendar.class);
                LogUtil.i(TAG, "setAlarm This Morning, alarmTime is " + alarmTime);
            } else { // Next Morning
                getNextWorkDay(calendar);
                calendar.set(Calendar.HOUR_OF_DAY, time1 / 60);
                calendar.set(Calendar.MINUTE, time1 % 60);
                String alarmTime = mJsonUtil.toJson(calendar, Calendar.class);
                LogUtil.i(TAG, "setAlarm Next Morning, alarmTime is " + alarmTime);
            }
        } else if (0 == time1 && 0 != time2) {
            if (timeNow < time2) { // This Afternoon
                calendar.set(Calendar.HOUR_OF_DAY, time2 / 60);
                calendar.set(Calendar.MINUTE, time2 % 60);
                String alarmTime = mJsonUtil.toJson(calendar, Calendar.class);
                LogUtil.i(TAG, "setAlarm This Afternoon, alarmTime is " + alarmTime);
            } else { // Next Afternoon
                getNextWorkDay(calendar);
                calendar.set(Calendar.HOUR_OF_DAY, time2 / 60);
                calendar.set(Calendar.MINUTE, time2 % 60);
                String alarmTime = mJsonUtil.toJson(calendar, Calendar.class);
                LogUtil.i(TAG, "setAlarm Next Afternoon, alarmTime is " + alarmTime);
            }
        } else {
            valid = false;
            LogUtil.i(TAG, "Time is not valid. Do nothing.");
        }

        if (valid) {
            Intent intent = new Intent(this, NotificationReceiver.class);
            PendingIntent contentIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
            mAlarmUtil.setAlarm(calendar, contentIntent);
        }
        return valid;
    }

    private Calendar getNextWorkDay(Calendar calendar) {
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        if (Calendar.SUNDAY == calendar.get(Calendar.DAY_OF_WEEK) ||
                Calendar.SATURDAY == calendar.get(Calendar.DAY_OF_WEEK)) {
            return getNextWorkDay(calendar);
        }
        return calendar;
    }

    public void cancelAlarm() {
        LogUtil.i(TAG, "cancelAlarm");
        mAlarmUtil.cancel();
    }
}
