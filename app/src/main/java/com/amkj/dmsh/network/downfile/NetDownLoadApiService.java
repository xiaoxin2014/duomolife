package com.amkj.dmsh.network.downfile;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Streaming;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/11/29
 * version 3.1.9
 * update 3.2.0 2018/12/27
 * class description:retrofit网络请求
 */
public interface NetDownLoadApiService {

    /**
     * 下载文件
     * 地址已加载在baseUrl
     *
     * @return
     */
    @Streaming
    @GET("{url}")
    Observable<Response<ResponseBody>> downLoadFile(@Path(value = "url",encoded = true) String url);

}
