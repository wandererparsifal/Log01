package com.parsifal.log01.utils;

import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by YangMing on 2016/8/24 14:37.
 */
public class FileUtil {

    private static final String TAG = FileUtil.class.getSimpleName();

    private static final String CHARSET_NAME = "utf-8";

    private String mPath = null;

    private FileUtil() {
        File sdDir = Environment.getExternalStorageDirectory();
        mPath = sdDir.getAbsolutePath() + "/TEC_WORK_LOG.log";
    }

    private static class SingletonHolder {
        private final static FileUtil INSTANCE = new FileUtil();
    }

    public static FileUtil getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void save(String json) {
        try {
            File file = new File(mPath);
            if (!file.exists()) {
                file.createNewFile();
            }
            OutputStream out = new FileOutputStream(file);
            out.write(json.getBytes());
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String load() {
        File file = new File(mPath);
        if (!file.exists()) {
            return null;
        }
        String result = null;
        try {
            InputStream in = new FileInputStream(file);
            int len = in.available();
            byte[] buf = new byte[len];
            in.read(buf, 0, len);
            result = new String(buf, CHARSET_NAME);
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
