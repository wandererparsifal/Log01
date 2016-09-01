package com.parsifal.log01.broadcastreceiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.parsifal.log01.R;
import com.parsifal.log01.ui.activity.MainActivity;

/**
 * Created by YangMing on 2016/9/1 14:49.
 */
public class NotificationReceiver extends BroadcastReceiver {

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
    }
}
