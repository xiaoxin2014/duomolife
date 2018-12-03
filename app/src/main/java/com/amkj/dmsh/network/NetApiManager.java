package com.amkj.dmsh.network;

import com.alibaba.fastjson.support.retrofit.Retrofit2ConverterFactory;
import com.amkj.dmsh.constant.Url;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import static com.amkj.dmsh.constant.ConstantVariable.isDebugTag;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/11/30
 * version 3.1.9
 * class description:net网络管理
 */
public class NetApiManager {
    private static NetApiManager mInstance;
    private static Retrofit retrofit;
    private static NetApiService netApiService = null;

    public static NetApiManager getInstance() {
        if (mInstance == null) {
            synchronized (NetApiManager.class) {
                if (mInstance == null) {
                    mInstance = new NetApiManager();
                }
            }
        }
        return mInstance;
    }

    /**
     * 初始化必要对象和参数
     */
    public void init() {
        // 初始化okhttp
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor logging = null;
        if (isDebugTag) {
            logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(logging);
        }
        OkHttpClient client = builder.build();
        // 初始化Retrofit
        retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(Url.BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(new Retrofit2ConverterFactory())
                .build();
    }

    public static NetApiService getNetApiService() {
        if (netApiService == null) {
            synchronized (NetApiService.class) {
                if(retrofit==null){
                    throw new NullPointerException("retrofit未初始化！");
                }
                netApiService = retrofit.create(NetApiService.class);
            }
        }
        return netApiService;
    }
}
