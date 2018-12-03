package com.amkj.dmsh.network;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/11/29
 * version 3.1.9
 * class description:retrofit网络请求
 */
public interface NetApiService {
    /**
     * 无参
     * @param url 地址
     * @return
     */
    @POST()
    Observable<String> getNetData(@Url String url);

    /**
     *
     * @param url 地址
     * @param params 参数
     * @return
     */
    @POST()
    Observable<String> getNetData(@Url String url, @QueryMap Map<String,Object> params);

}
