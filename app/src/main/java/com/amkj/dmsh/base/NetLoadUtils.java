package com.amkj.dmsh.base;

import android.content.Context;
import android.text.TextUtils;

import com.amkj.dmsh.constant.XUtil;
import com.amkj.dmsh.netloadpage.NetErrorCallback;
import com.amkj.dmsh.netloadpage.NetLoadCallback;
import com.amkj.dmsh.utils.NetWorkUtils;
import com.amkj.dmsh.utils.inteface.MyCacheCallBack;
import com.amkj.dmsh.utils.inteface.MyCallBack;
import com.kingja.loadsir.callback.Callback;
import com.kingja.loadsir.callback.SuccessCallback;
import com.kingja.loadsir.core.Convertor;
import com.kingja.loadsir.core.LoadService;

import java.util.List;
import java.util.Map;

import static com.amkj.dmsh.constant.ConstantMethod.createExecutor;
import static com.amkj.dmsh.constant.ConstantVariable.ERROR_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/8/31
 * version 3.1.6
 * class description:网络工具
 */
public class NetLoadUtils<T, E extends BaseEntity> {

    private static NetLoadUtils netLoadUtils;
    private Convertor<String> convertor;

    private NetLoadUtils() {
    }

    public static NetLoadUtils getQyInstance() {
        if (netLoadUtils == null) {
            synchronized (NetLoadUtils.class) {
                if (netLoadUtils == null) {
                    netLoadUtils = new NetLoadUtils();
                }
            }
        }
        return netLoadUtils;
    }

    /**
     * @param context
     * @param url
     * @param params
     * @param netLoadListener
     */
    public void loadNetDataPost(Context context, String url, Map<String, Object> params, NetLoadListener netLoadListener) {
        if (NetWorkUtils.checkNet(context)) {
            XUtil.Post(url, params, new MyCallBack<String>() {
                @Override
                public void onSuccess(String result) {
                    if (netLoadListener != null) {
                        netLoadListener.onSuccess(result);
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    if (netLoadListener != null) {
                        netLoadListener.onError(ex);
                    }
                }
            });
        } else {
            if (netLoadListener != null) {
                netLoadListener.netClose();
            }
        }
    }


    public void loadNetDataGetCache(Context context, String url, Map<String, String> params, NetLoadListener netLoadListener) {
        if (!TextUtils.isEmpty(url)) {
            XUtil.GetCache(url, 0, params, new MyCacheCallBack<String>() {
                private boolean hasError = false;
                private String result = null;
                private Throwable exception = null;

                @Override
                public boolean onCache(String result) { //得到缓存数据, 缓存过期后不会进入
                    this.result = result;
//                判断当前网络是否连接
                    return !NetWorkUtils.checkNet(context); //true: 信任缓存数据, 不再发起网络请求; false不信任缓存数据
                }

                @Override
                public void onSuccess(String result) {
                    //如果服务返回304或onCache选择了信任缓存,这时result为null
                    if (!TextUtils.isEmpty(result)) {
                        this.result = result;
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    hasError = true;
                    exception = ex;
                }

                @Override
                public void onCancelled(CancelledException cex) {
                }

                @Override
                public void onFinished() {
                    if (!hasError && !TextUtils.isEmpty(result)) {
                        if (netLoadListener != null) {
                            netLoadListener.onSuccess(result);
                        }
                    } else {
                        if (netLoadListener != null) {
                            netLoadListener.onError(exception);
                        }
                    }
                }
            });
        } else {
            if (netLoadListener != null) {
                netLoadListener.onError(null);
            }
        }
    }

    public interface NetLoadListener {
        void onSuccess(String result);

        void netClose();

        void onError(Throwable throwable);
    }

    /**
     * 集合数据
     *
     * @param loadService
     * @param list
     * @param resultClass
     */
    public void showLoadSir(LoadService loadService, List<T> list, E resultClass) {
        if (loadService != null) {
            createExecutor().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (list != null && list.size() > 0) {
                        loadService.showWithConvertor(SUCCESS_CODE);
                    } else if (resultClass != null) {
                        loadService.showWithConvertor(resultClass.getCode());
                    } else {
                        loadService.showWithConvertor(ERROR_CODE);
                    }
                }
            });
        }
    }

    /**
     * @param loadService
     * @param resultClass
     */
    public void showLoadSir(LoadService loadService, T resultBean, E resultClass) {
        if (loadService != null) {
            createExecutor().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (resultBean != null) {
                        loadService.showWithConvertor(SUCCESS_CODE);
                    } else if (resultClass != null) {
                        loadService.showWithConvertor(resultClass.getCode());
                    } else {
                        loadService.showWithConvertor(ERROR_CODE);
                    }
                }
            });
        }
    }

    /**
     * 单数据
     * @param loadService
     * @param resultClass
     */
    public void showLoadSir(LoadService loadService,E resultClass) {
        if (loadService != null) {
            createExecutor().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (resultClass != null) {
                        loadService.showWithConvertor(resultClass.getCode());
                    } else {
                        loadService.showWithConvertor(ERROR_CODE);
                    }
                }
            });
        }
    }

    /**
     * 返回码
     * @param loadService
     * @param code
     */
    public void showLoadSir(LoadService loadService,String code) {
        if (loadService != null) {
            createExecutor().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (!TextUtils.isEmpty(code)) {
                        loadService.showWithConvertor(code);
                    } else {
                        loadService.showWithConvertor(ERROR_CODE);
                    }
                }
            });
        }
    }
    public void showLoadSirSuccess(LoadService loadService){
        if(loadService!=null){
            loadService.showCallback(SuccessCallback.class);
        }
    }

    public void showLoadSirLoadFailed(LoadService loadService){
        if(loadService!=null){
            loadService.showCallback(NetErrorCallback.class);
        }
    }

    public void showLoadSirLoading(LoadService loadService){
        if(loadService!=null){
            loadService.showCallback(NetLoadCallback.class);
        }
    }
    /**
     * 配置数据 展示异常界面
     *
     * @return
     */
    public Convertor<String> getLoadSirCover() {
        if (convertor == null) {
            convertor = new Convertor<String>() {
                @Override
                public Class<? extends Callback> map(String baseEntity) {
                    Class<? extends Callback> resultCode = NetErrorCallback.class;
                    switch (baseEntity) {
                        case SUCCESS_CODE:
                            resultCode = SuccessCallback.class;
                            break;
                        default:
                            break;
                    }
                    return resultCode;
                }
            };
        }
        return convertor;
    }
}
