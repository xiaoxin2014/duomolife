package com.amkj.dmsh.network;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/12/10
 * version 3.1.9
 * class description:网络加载回调
 */
public interface NetLoadListener {
    /**
     * 请求成功
     * @param result
     */
    void onSuccess(String result);

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
