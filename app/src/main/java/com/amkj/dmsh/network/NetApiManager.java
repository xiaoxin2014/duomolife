package com.amkj.dmsh.network;

import com.alibaba.fastjson.support.retrofit.Retrofit2ConverterFactory;
import com.amkj.dmsh.constant.Url;

import java.io.File;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
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
        if (isDebugTag) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
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

    /**
     * 无缓存请求
     *
     * @return
     */
    public static NetApiService getNetApiService() {
        if (netApiService == null) {
            synchronized (NetApiService.class) {
                if (retrofit == null) {
                    throw new NullPointerException("retrofit未初始化！");
                }
                netApiService = retrofit.create(NetApiService.class);
            }
        }
        return netApiService;
    }

    /**
     * 获取带缓存的get请求 每次都重新创建
     *
     * @return
     */
    public static NetApiService getNetCacheApiService(boolean isForceNet) {
        /**
         * 初始化Cache
         */
        NoNetCacheInterceptor cacheInterceptor = new NoNetCacheInterceptor(isForceNet);
        ConnectNetCacheInterceptor cacheConnectInterceptor = new ConnectNetCacheInterceptor(isForceNet);
        OkHttpClient.Builder cacheClientBuilder = new OkHttpClient.Builder()
                .addInterceptor(cacheInterceptor)
                .addNetworkInterceptor(cacheConnectInterceptor);
        if (isDebugTag) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            cacheClientBuilder.addInterceptor(logging);
        }
        File file = new File(mAppContext.getCacheDir(), "NetCache");
        //缓存大小10M
        int cacheSize = 10 * 1024 * 1024;
        Cache cache = new Cache(file, cacheSize);
        OkHttpClient cacheOkHttpClient = cacheClientBuilder
                .cache(cache)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .client(cacheOkHttpClient)
                .baseUrl(Url.BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(new Retrofit2ConverterFactory())
                .build();
        return retrofit.create(NetApiService.class);
    }
}
