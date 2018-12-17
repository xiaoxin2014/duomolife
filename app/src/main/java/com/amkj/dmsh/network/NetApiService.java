package com.amkj.dmsh.network;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
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
     *
     * @param url 地址
     * @return
     */
    @POST()
    Observable<String> getPostNetData(@Url String url);

    /**
     * 获取同步方法
     * @param url
     * @return
     */
    @POST
    Call<String> getPostSyncNetData(@Url String url);

    /**
     * @param url    地址
     * @param params 参数
     * @return
     */
    @POST()
    Observable<String> getPostNetData(@Url String url, @QueryMap Map<String, Object> params);

    /**
     * 获取同步方法
     * @param url
     * @param params
     * @return
     */
    @POST()
    Call<String> getPostSyncNetData(@Url String url, @QueryMap Map<String, Object> params);

    /**
     * Get请求
     *
     * @param url
     * @return
     */
    @GET()
    Observable<String> getGetNetData(@Url String url);

    /**
     * Get请求
     *
     * @param url
     * @return
     */
    @GET()
    Observable<String> getGetNetData(@Url String url, @QueryMap Map<String, String> params);

    /**
     * 下载文件
     * 地址已加载在baseUrl
     *
     * @return
     */
    @Streaming
    @POST("{url}")
    Observable<ResponseBody> downLoadFile(@Path(value = "url",encoded = true) String url);

}
