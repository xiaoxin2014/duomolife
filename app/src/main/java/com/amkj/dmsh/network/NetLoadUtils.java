package com.amkj.dmsh.network;

import android.content.Context;
import android.text.TextUtils;

import com.amkj.dmsh.base.BaseEntity;
import com.amkj.dmsh.constant.XUtil;
import com.amkj.dmsh.netloadpage.NetEmptyCallback;
import com.amkj.dmsh.netloadpage.NetErrorCallback;
import com.amkj.dmsh.netloadpage.NetLoadCallback;
import com.amkj.dmsh.netloadpage.NetLoadTranslucenceCallback;
import com.amkj.dmsh.utils.NetWorkUtils;
import com.amkj.dmsh.utils.inteface.MyCacheCallBack;
import com.kingja.loadsir.callback.Callback;
import com.kingja.loadsir.callback.SuccessCallback;
import com.kingja.loadsir.core.Convertor;
import com.kingja.loadsir.core.LoadService;

import java.util.List;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.ERROR_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.network.RxJavaTransformer.getSchedulerObservableTransformer;

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
     *
     * @param context
     * @param url
     * @param netLoadListener
     */
    public void loadNetDataPost(Context context, String url, NetLoadListener netLoadListener) {
        loadNetDataPost(context,url,null,netLoadListener);
    }
    /**
     * @param context
     * @param url
     * @param params
     * @param netLoadListener
     */
    public void loadNetDataPost(Context context, String url, Map<String, Object> params, NetLoadListener netLoadListener) {
        if (NetWorkUtils.checkNet(context)) {
            NetApiService netApiService = NetApiManager.getNetApiService();
            Observer<String> observer = new Observer<String>() {
                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onNext(String result) {
                    if (netLoadListener != null) {
                        netLoadListener.onSuccess(result);
                    }
                }

                @Override
                public void onError(Throwable exception) {
                    if (netLoadListener != null) {
                        netLoadListener.onError(exception);
                    }
                }

                @Override
                public void onComplete() {

                }
            };
            if(params!=null){
                netApiService.getNetData(url,params).compose(getSchedulerObservableTransformer()).subscribe(observer);
            }else{
                netApiService.getNetData(url).compose(getSchedulerObservableTransformer()).subscribe(observer);
            }
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
            if (list != null && list.size() > 0) {
                loadService.showWithConvertor(SUCCESS_CODE);
            } else if (resultClass != null) {
                loadService.showWithConvertor(resultClass.getCode());
            } else {
                loadService.showWithConvertor(ERROR_CODE);
            }
        }
    }

    /**
     * 集合数据
     *
     * @param loadService
     * @param list
     * @param resultCode 返回码
     */
    public void showLoadSirString(LoadService loadService, List<T> list, String resultCode) {
        if (loadService != null) {
            if (list != null && list.size() > 0) {
                loadService.showWithConvertor(SUCCESS_CODE);
            } else if (!TextUtils.isEmpty(resultCode)) {
                loadService.showWithConvertor(resultCode);
            }else{
                loadService.showWithConvertor(ERROR_CODE);
            }
        }
    }

    /**
     * @param loadService
     * @param resultClass
     */
    public void showLoadSir(LoadService loadService, T resultBean, E resultClass) {
        if (loadService != null) {
            if (resultBean != null) {
                loadService.showWithConvertor(SUCCESS_CODE);
            } else if (resultClass != null) {
                loadService.showWithConvertor(resultClass.getCode());
            } else {
                loadService.showWithConvertor(ERROR_CODE);
            }
        }
    }

    /**
     * 单数据
     *
     * @param loadService
     * @param resultClass
     */
    public void showLoadSir(LoadService loadService, E resultClass) {
        if (loadService != null) {
            if (resultClass != null) {
                loadService.showWithConvertor(resultClass.getCode());
            } else {
                loadService.showWithConvertor(ERROR_CODE);
            }
        }
    }

    /**
     * 返回码
     *
     * @param loadService
     * @param code
     */
    public void showLoadSir(LoadService loadService, String code) {
        if (loadService != null) {
            if (!TextUtils.isEmpty(code)) {
                loadService.showWithConvertor(code);
            } else {
                loadService.showWithConvertor(ERROR_CODE);
            }
        }
    }

    public void showLoadSirSuccess(LoadService loadService) {
        if (loadService != null) {
            loadService.showCallback(SuccessCallback.class);
        }
    }

    public void showLoadSirLoadFailed(LoadService loadService) {
        if (loadService != null) {
            loadService.showCallback(NetErrorCallback.class);
        }
    }

    public void showLoadSirLoading(LoadService loadService) {
        if (loadService != null) {
            loadService.showCallback(NetLoadCallback.class);
        }
    }

    /**
     * 半透明加载 用于已加载数据，再次加载请求数据
     * @param loadService
     */
    public void showLoadSirTranslucenceLoading(LoadService loadService) {
        if (loadService != null) {
            loadService.showCallback(NetLoadTranslucenceCallback.class);
        }
    }
    public void showLoadSirEmpty(LoadService loadService) {
        if (loadService != null) {
            loadService.showCallback(NetEmptyCallback.class);
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
                        case EMPTY_CODE:
                            resultCode = NetEmptyCallback.class;
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
