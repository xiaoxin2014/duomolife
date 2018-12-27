package com.amkj.dmsh.network;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/12/27
 * version 3.2.0
 * class description:文件下载进度回调
 */
public interface NetLoadDownProgressListener {
    void onSuccess(String saveFilePath);
    void onDownFailed(Throwable e);
    void onProgress(int progress);
    void onStartDown();
}
