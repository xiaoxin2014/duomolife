package com.amkj.dmsh.constant;

import org.xutils.common.Callback;
import org.xutils.common.Callback.Cancelable;
import org.xutils.common.Callback.CommonCallback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.Map;

public class XUtil {
    /**
     * 发送get请求
     *
     * @param <T>
     */
    public static <T> Cancelable Get(String url, Map<String, String> map, CommonCallback<T> callback) {
        RequestParams params = new RequestParams(url);
        if (null != map) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                params.addQueryStringParameter(entry.getKey(), entry.getValue());
            }
        }
        Cancelable cancelable = x.http().get(params, callback);
        return cancelable;
    }

    /**
     * 发送post请求
     *
     * @param <T>
     */
    public static <T> Cancelable Post(String url, Map<String, Object> map, CommonCallback<T> callback) {
        RequestParams params = new RequestParams(url);
        if (null != map) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                params.addParameter(entry.getKey(), entry.getValue());
            }
        }
        Cancelable cancelable = x.http().post(params, callback);
        return cancelable;
    }

    /**
     * 同步方法
     * @param url
     * @param map
     * @param resultClass
//     * @param <T>
     * @return
     */
    public static <T> T PostSync(String url, Map<String, Object> map, Class<T> resultClass) {
        RequestParams params = new RequestParams(url);
        if (null != map) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                params.addParameter(entry.getKey(), entry.getValue());
            }
        }
        T classBean = null;
        try {
            classBean = x.http().postSync(params, resultClass);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return classBean;
    }
    /**
     * 发送get请求 缓存数据
     *
     * @param <T>
     */
    public static <T> Cancelable GetCache(String url, int timeout, Map<String, String> map, Callback.CacheCallback<T> callback) {
        RequestParams params = new RequestParams(url);
        params.setCacheMaxAge(1000 * 60 * 60 * 24);
        params.setCacheDirName("cacheDataDir");
        if (timeout != 0) {
            params.setConnectTimeout(1000 * timeout);
        }
        if (null != map) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                params.addQueryStringParameter(entry.getKey(), entry.getValue());
            }
        }
        Cancelable cancelable = x.http().get(params, callback);
        return cancelable;
    }

    /**
     * 上传文件
     *
     * @param <T>
     */
    public static <T> Cancelable UpLoadFile(String url, Map<String, Object> map, CommonCallback<T> callback) {
        RequestParams params = new RequestParams(url);
        if (null != map) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                params.addParameter(entry.getKey(), entry.getValue());
            }
        }
        params.setMultipart(true);
        Cancelable cancelable = x.http().post(params, callback);
        return cancelable;
    }

    /**
     * 下载文件
     *
     * @param <T>
     */
    public static <T> Cancelable DownLoadFile(String url, String filepath, CommonCallback<T> callback) {
        RequestParams params = new RequestParams(url);
        //设置断点续传
        params.setAutoResume(false);
        params.setSaveFilePath(filepath);
        Cancelable cancelable = x.http().get(params, callback);
        return cancelable;
    }
}
