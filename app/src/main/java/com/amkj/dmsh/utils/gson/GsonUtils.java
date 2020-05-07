package com.amkj.dmsh.utils.gson;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.internal.bind.TypeAdapters;
import com.google.gson.stream.JsonReader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Reader;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.amkj.dmsh.constant.ConstantVariable.ERROR_CODE;

/**
 * Created by xiaoxin on 2020/5/7
 * Version:v4.5.1
 * ClassDescription :Gson解析工具类
 */
public class GsonUtils {
    private static Gson mGson = null;

    private GsonUtils() {
    }

    public static Gson getInstance() {
        if (mGson == null) {
            synchronized (GsonUtils.class) {
                if (mGson == null) {
                    //容错处理
                    mGson = new GsonBuilder()
                            .registerTypeAdapterFactory(TypeAdapters.newFactory(String.class, new StringTypeAdapter()))
                            .registerTypeAdapterFactory(TypeAdapters.newFactory(boolean.class, Boolean.class, new BooleanTypeAdapter()))
                            .registerTypeAdapterFactory(TypeAdapters.newFactory(int.class, Integer.class, new IntegerTypeAdapter()))
                            .registerTypeAdapterFactory(TypeAdapters.newFactory(long.class, Long.class, new LongTypeAdapter()))
                            .registerTypeAdapterFactory(TypeAdapters.newFactory(float.class, Float.class, new FloatTypeAdapter()))
                            .registerTypeAdapterFactory(TypeAdapters.newFactory(double.class, Double.class, new DoubleTypeAdapter()))
                            .registerTypeHierarchyAdapter(List.class, new ListTypeAdapter())
                            .create();
                }
            }
        }
        return mGson;
    }

    //普通对象
    public static <T> T fromJson(String result, Class<T> classOfT) {
        try {
            return getInstance().fromJson(result, classOfT);
        } catch (Exception ex) {
            //json解析失败时，取code和msg
            String code;
            String msg;
            try {
                JSONObject jsonObject = new JSONObject(result);
                code = (String) jsonObject.get("code");
                msg = (String) jsonObject.get("msg");

            } catch (JSONException e) {
                code = ERROR_CODE;
                msg = "请求失败，无效数据";
            }
            Map<String, Object> map = new HashMap<>();
            map.put("code", code);
            map.put("msg", msg);
            return getInstance().fromJson(JSON.toJSONString(map), classOfT);
        }
    }

    //集合数据
    public static <T> T fromJson(String result, Type typeToken) {
        return getInstance().fromJson(result, typeToken);
    }

    public static <T> T fromJson(JsonReader reader, Type typeOfT) {
        return getInstance().fromJson(reader, typeOfT);
    }


    public static <T> T fromJson(Reader json, Type typeOfT) {
        return getInstance().fromJson(json, typeOfT);
    }

    public static <T> T fromJson(Reader json, Class<T> classOfT) {
        return getInstance().fromJson(json, classOfT);
    }

    public static <T> T fromJson(JsonElement json, Type typeOf) {
        return getInstance().fromJson(json, typeOf);
    }


    public static String toJson(Object src) {
        try {
            return getInstance().toJson(src);
        } catch (Exception e) {
            return "";
        }
    }
}
