package com.parsifal.log01.utils;

import com.google.gson.Gson;

/**
 * Created by YangMing on 2016/9/2 09:30.
 */
public class JsonUtil {

    private Gson mGson = null;

    private JsonUtil() {
        mGson = new Gson();
    }

    private static class SingletonHolder {
        private final static JsonUtil INSTANCE = new JsonUtil();
    }

    public static JsonUtil getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public String toJson(Object src, Class cls) {
        return mGson.toJson(src, cls);
    }

    public <T> T fromJson(String json, Class<T> cls) {
        return mGson.fromJson(json, cls);
    }
}
