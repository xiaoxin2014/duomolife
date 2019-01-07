package com.amkj.dmsh.network;

import com.zhouyou.http.cache.model.CacheResult;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/12/10
 * version 3.1.9
 * class description:网络加载回调
 */
public interface NetCacheLoadListener <T>{
    /**
     * 请求成功
     * @param result
     */
    void onSuccess(String result);

    /**
     * 携带数据来源 ？ 缓存 网络
     * @param result
     */
    void onSuccessCacheResult(CacheResult<T> result);
    /**
     * 无网络
     */
    void netClose();

    /**
     * 请求异常
     * @param throwable
     */
    void onError(Throwable throwable);

    /**
     * 新增异常无网络统一回调方法
     */
    void onNotNetOrException();
}
