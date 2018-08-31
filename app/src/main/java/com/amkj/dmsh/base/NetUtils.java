package com.amkj.dmsh.base;

import android.content.Context;

import com.amkj.dmsh.constant.XUtil;
import com.amkj.dmsh.utils.NetWorkUtils;
import com.amkj.dmsh.utils.inteface.MyCallBack;
import com.kingja.loadsir.core.LoadService;

import java.util.Map;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/8/31
 * version 3.1.6
 * class description:网络工具
 */
public class NetUtils {

    private static NetUtils netUtils;

    private NetUtils() {
    }

    public static NetUtils getQyInstance() {
        if (netUtils == null) {
            synchronized (NetUtils.class) {
                if (netUtils == null) {
                    netUtils = new NetUtils();
                }
            }
        }
        return netUtils;
    }

    /**
     *
     * @param context
     * @param url
     * @param params
     * @param loadService
     * @param netLoadListener
     */
    public void loadNetData(Context context, String url, Map<String, Object> params, LoadService loadService,NetLoadListener netLoadListener) {
        if (NetWorkUtils.checkNet(context)) {
            XUtil.Post(url, params, new MyCallBack<String>() {
                @Override
                public void onSuccess(String result) {

                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    // TODO: 2018/8/31 报错
                }
            });
        } else {
            // TODO: 2018/8/31 网络错误
        }
    }

    public interface NetLoadListener {
        void onSuccess();

        void loadFailed();

        void onError();
    }
}
