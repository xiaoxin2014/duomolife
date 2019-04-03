package com.amkj.dmsh.rxeasyhttp.interceptor;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Base64;
import android.util.Log;

import com.amkj.dmsh.BuildConfig;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.rxeasyhttp.utils.DeviceUtils;
import com.amkj.dmsh.utils.SharedPreUtils;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

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

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();
        Request.Builder builder = request.newBuilder();
        Map<String, Object> newMap = new HashMap<>(mDomoCommon);
        if (ConstantMethod.userId > 0) {
            newMap.put("uid", ConstantMethod.userId);
            String token = (String) SharedPreUtils.getParam(TOKEN, "");
            newMap.put("token", token);
        }
        String DomoJson = new JSONObject(newMap).toString();
        builder.addHeader("domo-custom", Base64.encodeToString(DomoJson.getBytes(), Base64.NO_WRAP));
        Response response = chain.proceed(builder.build());
        //打印响应结果
        httpLog(request, DomoJson, response);
        return response;
    }

    private void httpLog(Request request, String domoJson, Response response) throws IOException {
        if (BuildConfig.DEBUG) {
            ResponseBody body = response.peekBody(1024 * 1024);
            String ss = body.string();

            Log.d("retrofit", "----------Start-----------");
            //打印请求Ulr
            Log.d("retrofitRequest", String.format("Sending request %s",
                    request.url()));

            Log.d("retrofitHeaderJson", domoJson + "----" + Base64.encodeToString(domoJson.getBytes(), Base64.NO_WRAP));
            //打印请求参数
            String method = request.method();
            if ("POST".equals(method)) {
                StringBuilder sb = new StringBuilder();
                if (request.body() instanceof FormBody) {
                    FormBody formBody = (FormBody) request.body();
                    for (int i = 0; i < formBody.size(); i++) {
                        sb.append(formBody.encodedName(i) + "=" + formBody.encodedValue(i) + ",");
                    }
                    sb.delete(sb.length() - 1, sb.length());
                    Log.d("retrofitRequestBody", "{" + sb.toString() + "}");
                }
            }

            Log.d("retrofitResponse", ss);
            Log.d("retrofit", "----------end-----------");
            Log.d("retrofit", "                        ");
        }
    }

    //多么生活api通用参数
    private static Map<String, Object> getCommonApiParameter(Context context) {
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
