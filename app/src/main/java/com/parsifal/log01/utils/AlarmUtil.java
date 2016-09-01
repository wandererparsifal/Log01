package com.parsifal.log01.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;

import java.util.Calendar;

/**
 * Created by YangMing on 2016/9/1 09:02.
 */
public class AlarmUtil {

    private static final String TAG = AlarmUtil.class.getSimpleName();

    private AlarmManager alarmManager = null;

    private boolean isInitialized = false;

    private AlarmUtil() {
    }

    private static class SingletonHolder {
        private final static AlarmUtil INSTANCE = new AlarmUtil();
    }

    public static AlarmUtil getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void init(Context context) {
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        isInitialized = true;
    }

    public void setAlarm(Calendar calendar, PendingIntent pendingIntent) {
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }
}
