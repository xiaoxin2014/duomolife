package com.amkj.dmsh.rxeasyhttp.interceptor;

import android.content.Context;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import com.amkj.dmsh.BuildConfig;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.rxeasyhttp.model.HttpHeaders;
import com.amkj.dmsh.rxeasyhttp.utils.DeviceUtils;
import com.amkj.dmsh.utils.EncodeUtils;
import com.amkj.dmsh.utils.SharedPreUtils;
import com.tencent.bugly.crashreport.CrashReport;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import static com.amkj.dmsh.constant.ConstantVariable.TOKEN;

/**
 * Created by xiaoxin on 2018/1/16 0016
 */

public class MyInterceptor implements Interceptor {
    private Context mContext;
    private Map<String, Object> mDomoCommon;

    //该构造方法只会调用一次
    public MyInterceptor(Context context) {
        mContext = context;
        mDomoCommon = getCommonApiParameter(mContext);
    }

    @Override
    public Response intercept(Chain chain) {
        Response response = null;
        try {
            Request request = chain.request();
            Request.Builder builder = request.newBuilder();
            Map<String, Object> newMap = new HashMap<>(mDomoCommon);
            if (ConstantMethod.userId > 0) {
                //登录情况下传uid和token
                newMap.put("uid", ConstantMethod.userId);
                String token = (String) SharedPreUtils.getParam(TOKEN, "");
                newMap.put("token", token);
            }

            String DomoJson = new JSONObject(newMap).toString();
            //添加公共请求参数
            String base64 = getBase64(newMap);
            builder.addHeader("domo-custom", base64);
            //默认添加 Accept -Language
            String acceptLanguage = HttpHeaders.getAcceptLanguage();
            if (!TextUtils.isEmpty(acceptLanguage)) {
                builder.addHeader(HttpHeaders.HEAD_KEY_ACCEPT_LANGUAGE, acceptLanguage);
            }

            //默认添加 User-Agent
            String userAgent = HttpHeaders.getUserAgent();
            if (!TextUtils.isEmpty(userAgent)) {
                builder.addHeader(HttpHeaders.HEAD_KEY_USER_AGENT, userAgent);
            }

            response = chain.proceed(builder.build());
            String responseInfo = response.peekBody(1024 * 1024).string();
            //打印响应结果
            httpLog(request, DomoJson, responseInfo);
        } catch (Exception e) {
            //上报异常
            CrashReport.postCatchedException(new Exception(
                    e.getMessage(), e.getCause()));
        }


        return response;
    }

    private String getBase64(Map map) {
        return Base64.encodeToString(new JSONObject(map).toString().getBytes(), Base64.NO_WRAP);
    }

    private void httpLog(Request request, String domoJson, String responseInfo) {
        if (BuildConfig.DEBUG) {
            Log.d("retrofit", "----------Start-----------");
            //打印请求Ulr
            Log.d("retrofitRequest", String.format("Sending request %s",
                    request.url()));

            Log.d("retrofitHeaderJson", domoJson);
            //打印请求参数
            String method = request.method();
            if ("POST".equals(method)) {
                StringBuilder sb = new StringBuilder();
                if (request.body() instanceof FormBody) {
                    FormBody formBody = (FormBody) request.body();
                    for (int i = 0; i < formBody.size(); i++) {
                        sb.append(formBody.encodedName(i)).append("=").append(EncodeUtils.urlDecode(formBody.encodedValue(i)).replace("<", "<").replace(">", ">").replace("%24", "$")).append(",");
                    }

                    if (sb.length() > 1) {
                        sb.delete(sb.length() - 1, sb.length());
                    }
                    Log.d("retrofitRequestBody", "{" + sb.toString() + "}");
                }
            }
            Log.d("retrofitResponse", responseInfo);
//            Log.d("retrofitResponseCode", "响应码:" + response.code());
            Log.d("retrofit", "----------end-----------");
        }
    }

    //多么生活api通用参数
    public static Map<String, Object> getCommonApiParameter(Context context) {
        Map<String, Object> map = new HashMap<>();
        map.put("appVersion", DeviceUtils.getVersionName(context));
        map.put("device", DeviceUtils.getModel());
        map.put("sysVersion", DeviceUtils.getSDKVersion());
        map.put("timestamp", System.currentTimeMillis());
        map.put("source", 1);//访问来源 0：ios  1:android 2:移动web 3：小程序 4：官网
        map.put("clientIp", DeviceUtils.getIpAddress(context));
        map.put("clientMac", DeviceUtils.getMacAddress(context));
        map.put("deviceId", DeviceUtils.getAndroidID(context));
        return map;
    }
}
