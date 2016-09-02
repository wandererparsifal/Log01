package com.parsifal.log01.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by YangMing on 2016/9/2 10:33.
 */
public class ToastUtil {

    private static String oldMsg;
    protected static Toast mToast = null;
    private static long oneTime = 0;
    private static long twoTime = 0;

    public static void show(Context context, String s) {
        if (mToast == null) {
            mToast = Toast.makeText(context, s, Toast.LENGTH_SHORT);
            mToast.show();
            oneTime = System.currentTimeMillis();
        } else {
            twoTime = System.currentTimeMillis();
            if (s.equals(oldMsg)) {
                if (twoTime - oneTime > Toast.LENGTH_SHORT) {
                    mToast.show();
                }
            } else {
                oldMsg = s;
                mToast.setText(s);
                mToast.show();
            }
        }
        oneTime = twoTime;
    }

    public static void show(Context context, int resId) {
        show(context, context.getString(resId));
    }
}
