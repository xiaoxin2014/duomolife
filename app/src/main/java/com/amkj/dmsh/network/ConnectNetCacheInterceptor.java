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
 * class description:缓存
 */
public class ConnectNetCacheInterceptor implements Interceptor {
    private final boolean isForceNet;
    public ConnectNetCacheInterceptor(boolean isForceNet) {
        this.isForceNet = isForceNet;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        boolean connectingByState = NetWorkUtils.isConnectedByState(mAppContext);
        if(isForceNet){
            request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_NETWORK)
                    .build();
        }
        Response response = chain.proceed(request);
        if (connectingByState) {
            response = response.newBuilder()
                    .removeHeader("Pragma")
                    // 有网络时 设置缓存超时时间1个小时
                    .header("Cache-Control", "public, max-age=" + 60 * 60)
                    .build();
        }
        return response;
    }
}


