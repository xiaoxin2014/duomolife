package com.zhouyou.http.interceptor;

import android.support.annotation.NonNull;
import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by xiaoxin on 2018/1/16 0016
 */

public class MyIntercepter implements Interceptor {

    private final String mDomoCommon;

    public MyIntercepter(String commonApiParameterJson) {
        mDomoCommon = commonApiParameterJson;
    }

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();
        Log.d("retrofitRequest", String.format("Sending request %s",
                request.url()));

        Request.Builder builder = request.newBuilder();
        builder.addHeader("Domo-Custom", mDomoCommon);
//        String token = (String) SharedPreUtils.getParam(ShareConstants.TOKEN, "");
//        if (!TextUtils.isEmpty(token) && request.url().toString().startsWith(UrlConstants.BASE_URL + "/oauthapi")) {
//            builder.addHeader("Authorization", token);
//        }

        Response response = chain.proceed(builder.build());
        ResponseBody body = response.peekBody(1024 * 1024);
        String ss = body.string();
        Log.d("retrofitResponse", ss);
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
