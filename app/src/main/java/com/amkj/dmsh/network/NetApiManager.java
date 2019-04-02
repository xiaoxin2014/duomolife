package com.amkj.dmsh.network;

import android.app.Application;

import com.amkj.dmsh.constant.ConstantMethod;
import com.tencent.bugly.beta.tinker.TinkerManager;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.cache.converter.GsonDiskConverter;

import java.io.File;

import static com.amkj.dmsh.constant.Url.BASE_URL;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/11/30
 * version 3.1.9
 * class description:net网络管理
 */
public class NetApiManager {
    private static NetApiManager mInstance;
    private boolean isInitNet;
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
    public void initNetInstance() {
        if(!isInitNet){
            Application application = TinkerManager.getTinkerApplicationLike().getApplication();
            EasyHttp.init(application,ConstantMethod.userId);//默认初始化
            String rxCacheFileName = application.getCacheDir() + "/rxCache";
            File rxCacheFile = new File(rxCacheFileName);
            if(!rxCacheFile.exists()){
                rxCacheFile.mkdir();
            }
            EasyHttp.getInstance().setBaseUrl(BASE_URL).setConnectTimeout(15 * 1000)
                    .setRetryDelay(500)//每次延时500ms重试
                    .setCacheDirectory(rxCacheFile) //设置缓存路径
                    .setCacheMaxSize(20*1024*1024)
                    .setCacheDiskConverter(new GsonDiskConverter());//设置缓存转换器
        }
    }
}
