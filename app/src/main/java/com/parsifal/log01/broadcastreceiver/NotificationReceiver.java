package com.parsifal.log01.broadcastreceiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.google.gson.Gson;
import com.parsifal.log01.LogPresenter;
import com.parsifal.log01.R;
import com.parsifal.log01.ui.activity.MainActivity;
import com.parsifal.log01.ui.view.StatisticsData;
import com.parsifal.log01.utils.SharedPreferencesUtil;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by YangMing on 2016/9/1 14:49.
 */
public class NotificationReceiver extends BroadcastReceiver {

    private static final String TAG = NotificationReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        int NOTIFICATION_BASE_NUMBER = 110;
        NotificationManager nm = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(context);
        Intent notificationIntent = new Intent(context, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
        CharSequence remind = context.getText(R.string.remind);
        int unicodeJoy = 0x1F612;
        String emojiString = new String(Character.toChars(unicodeJoy));
        builder.setContentIntent(contentIntent).
                setSmallIcon(R.drawable.icon)
                .setAutoCancel(true)
                .setContentTitle(context.getText(R.string.app_name))
                .setContentText(remind + emojiString);
        Notification n = builder.getNotification();
        n.defaults = Notification.DEFAULT_SOUND;
        nm.notify(NOTIFICATION_BASE_NUMBER, n);

        LogPresenter mLogPresenter = (LogPresenter) context.getApplicationContext();
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
                mLogPresenter.setAlarm(calendar);
            } else {
                if (0 != time1) {
                    calendar.add(Calendar.DAY_OF_MONTH, 1);
                    calendar.set(Calendar.HOUR_OF_DAY, time1 / 60);
                    calendar.set(Calendar.MINUTE, time1 % 60);
                    mLogPresenter.setAlarm(calendar);
                }
            }
        } else {
            if (0 != time1) {
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                calendar.set(Calendar.HOUR_OF_DAY, time1 / 60);
                calendar.set(Calendar.MINUTE, time1 % 60);
                mLogPresenter.setAlarm(calendar);
            } else {
                if (0 != time2) {
                    calendar.add(Calendar.DAY_OF_MONTH, 1);
                    calendar.set(Calendar.HOUR_OF_DAY, time2 / 60);
                    calendar.set(Calendar.MINUTE, time2 % 60);
                    mLogPresenter.setAlarm(calendar);
                }
            }
        }
    }
}
