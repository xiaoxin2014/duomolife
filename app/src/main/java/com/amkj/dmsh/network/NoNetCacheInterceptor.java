package com.amkj.dmsh.network;

import com.amkj.dmsh.utils.NetWorkUtils;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/12/3
 * version 3.1.9
 * class description:无网络缓存
 */
public class NoNetCacheInterceptor implements Interceptor {

    private final boolean isForceNet;

    public NoNetCacheInterceptor(boolean isForceNet) {
        this.isForceNet = isForceNet;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response;
        boolean connectingByState = NetWorkUtils.isConnectedByState(mAppContext);
        if (!connectingByState) {
            request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build();
            response = chain.proceed(request).newBuilder()
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + 7 * 24 * 60 * 60)
                    .removeHeader("Pragma")
                    .build();
        }else{
            if(isForceNet){
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_NETWORK)
                        .build();
            }
            response = chain.proceed(request);
        }
        return response;
    }
}


