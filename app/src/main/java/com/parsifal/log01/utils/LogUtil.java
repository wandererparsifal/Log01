package com.parsifal.log01.utils;

import android.util.Log;

/**
 * Created by YangMing on 2016/6/29 14:06.
 */
public class LogUtil {

    private static final boolean VERBOSE = true;

    private static final boolean INFO = true;

    private static final boolean DEBUG = true;

    private static final boolean WARN = true;

    private static final boolean ERROR = true;

    public static void v(String tag, String msg) {
        if (VERBOSE) {
            Log.v(tag, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (INFO) {
            Log.i(tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (DEBUG) {
            Log.d(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (WARN) {
            Log.w(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (ERROR) {
            Log.e(tag, msg);
        }
    }
}
