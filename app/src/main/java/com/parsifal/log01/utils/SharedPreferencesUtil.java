package com.parsifal.log01.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by yangming on 16-9-1.
 */
public class SharedPreferencesUtil {

    private static final String TAG = SharedPreferencesUtil.class.getSimpleName();

    private static final String FILE_NAME = "statistics_data";

    private Context mContext = null;

    private boolean isInitialized = false;

    private SharedPreferencesUtil() {
    }

    private static class SingletonHolder {
        private final static SharedPreferencesUtil INSTANCE = new SharedPreferencesUtil();
    }

    public static SharedPreferencesUtil getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void init(Context context) {
        mContext = context;
        isInitialized = true;
    }

    public void save(String key, String data) {
        if (isInitialized) {
            SharedPreferences sharedPreferences = mContext
                    .getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(key, data);
            editor.commit();
        } else {
            LogUtil.w(TAG, "SharedPreferencesUtil is uninitialized.");
        }
    }

    public String load(String key) {
        if (isInitialized) {
            SharedPreferences sharedPreferences = mContext.getSharedPreferences
                    (FILE_NAME, Context.MODE_PRIVATE);
            return sharedPreferences.getString(key, null);
        } else {
            LogUtil.w(TAG, "SharedPreferencesUtil is uninitialized.");
            return null;
        }
    }
}
