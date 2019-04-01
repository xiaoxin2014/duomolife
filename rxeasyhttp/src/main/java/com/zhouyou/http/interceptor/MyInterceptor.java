package com.zhouyou.http.interceptor;

import android.support.annotation.NonNull;
import android.util.Log;

import com.zhouyou.http.BuildConfig;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by xiaoxin on 2018/1/16 0016
 */

public class MyInterceptor implements Interceptor {
    private final String mDomoCommon;

    public MyInterceptor(String commonApiParameterJson) {
        mDomoCommon = commonApiParameterJson;
    }

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();
        Request.Builder builder = request.newBuilder();
        builder.addHeader("domo-custom", mDomoCommon);
//        String token = (String) SharedPreUtils.getParam(ShareConstants.TOKEN, "");
//        if (!TextUtils.isEmpty(token) && request.url().toString().startsWith(UrlConstants.BASE_URL + "/oauthapi")) {
//            builder.addHeader("Authorization", token);
//        }

        Response response = chain.proceed(builder.build());

        //打印响应结果
        if (BuildConfig.DEBUG) {
            ResponseBody body = response.peekBody(1024 * 1024);
            String ss = body.string();

            Log.d("retrofit", "----------Start-----------");
            //打印请求Ulr
            Log.d("retrofitRequest", String.format("Sending request %s",
                    request.url()));

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

//        //判断token是否过期
//        if (response.code() == 401) {
//            Request.Builder newbuilder = request.newBuilder();
//            Response newresponse = chain.proceed(newbuilder.build());
//            SharedPreUtils.setParam(ShareConstants.TOKEN, "");
//            return newresponse;
//        }
        return response;
    }
}
