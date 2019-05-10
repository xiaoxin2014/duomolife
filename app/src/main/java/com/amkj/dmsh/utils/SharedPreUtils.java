package com.amkj.dmsh.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.amkj.dmsh.base.TinkerBaseApplicationLike;

import io.reactivex.annotations.NonNull;

/**
 * Created by Xiaoxin on 2017/10/15 0015.
 */

public class SharedPreUtils {
    private static Context mContext = TinkerBaseApplicationLike.mAppContext;

    /**
     * 保存在手机里面的文件名
     */
    private static final String DEFAULT_FILE_NAME = "loginStatus";

    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     */
    public static void setParam(String fileName, String key, Object object) {
        String type = object.getClass().getSimpleName();
        SharedPreferences sp = mContext.getSharedPreferences(TextUtils.isEmpty(fileName) ? DEFAULT_FILE_NAME : fileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        if ("String".equals(type)) {
            editor.putString(key, (String) object);
        } else if ("Integer".equals(type)) {
            editor.putInt(key, (Integer) object);
        } else if ("Boolean".equals(type)) {
            editor.putBoolean(key, (Boolean) object);
        } else if ("Float".equals(type)) {
            editor.putFloat(key, (Float) object);
        } else if ("Long".equals(type)) {
            editor.putLong(key, (Long) object);
        }

        editor.commit();
    }


    public static void setParam(String key, Object object) {
        setParam(null, key, object);
    }


    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     */
    public static Object getParam(String fileName, String key, @NonNull Object defaultObject) {
        String type = defaultObject.getClass().getSimpleName();
        SharedPreferences sp = mContext.getSharedPreferences(TextUtils.isEmpty(fileName) ? DEFAULT_FILE_NAME : fileName, Context.MODE_PRIVATE);

        if ("String".equals(type)) {
            return sp.getString(key, (String) defaultObject);
        } else if ("Integer".equals(type)) {
            return sp.getInt(key, (Integer) defaultObject);
        } else if ("Boolean".equals(type)) {
            return sp.getBoolean(key, (Boolean) defaultObject);
        } else if ("Float".equals(type)) {
            return sp.getFloat(key, (Float) defaultObject);
        } else if ("Long".equals(type)) {
            return sp.getLong(key, (Long) defaultObject);
        }

        return defaultObject;
    }


    public static Object getParam(String key, @NonNull Object defaultObject) {
        return getParam(null, key, defaultObject);
    }

    /**
     * 清除所有数据
     */
    public static void clearAll(String fileName) {
        SharedPreferences sp = mContext.getSharedPreferences(TextUtils.isEmpty(fileName) ? DEFAULT_FILE_NAME : fileName,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear().commit();
    }

    public static void clearAll() {
        clearAll(null);
    }

    /**
     * 清除指定key的数据
     */
    public static void clear(String fileName, String key) {
        SharedPreferences sp = mContext.getSharedPreferences(TextUtils.isEmpty(fileName) ? DEFAULT_FILE_NAME : fileName,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        editor.commit();
    }

    public static void clear(String key) {
        clear(null, key);
    }
}
