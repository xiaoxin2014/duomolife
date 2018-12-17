package com.amkj.dmsh.network;

import android.content.Context;
import android.text.TextUtils;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseEntity;
import com.amkj.dmsh.netloadpage.NetEmptyCallback;
import com.amkj.dmsh.netloadpage.NetErrorCallback;
import com.amkj.dmsh.netloadpage.NetLoadCallback;
import com.amkj.dmsh.netloadpage.NetLoadTranslucenceCallback;
import com.amkj.dmsh.network.downfile.DownloadHelper;
import com.amkj.dmsh.network.downfile.DownloadListener;
import com.amkj.dmsh.utils.NetWorkUtils;
import com.kingja.loadsir.callback.Callback;
import com.kingja.loadsir.callback.SuccessCallback;
import com.kingja.loadsir.core.Convertor;
import com.kingja.loadsir.core.LoadService;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import retrofit2.Response;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
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

    public static NetLoadUtils getNetInstance() {
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
     * @param netLoadListener
     */
    public void loadNetDataPost(Context context, String url, NetLoadListener netLoadListener) {
        loadNetDataPost(context, url, null, netLoadListener);
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
                    try {
                        if (netLoadListener != null) {
                            netLoadListener.onSuccess(result);
                        }
                    } catch (Exception e) {
                        netLoadListener.onNotNetOrException();
                        netLoadListener.onError(e);
                    }
                }

                @Override
                public void onError(Throwable exception) {
                    if (netLoadListener != null) {
                        netLoadListener.onNotNetOrException();
                        netLoadListener.onError(exception);
                    }
                }

                @Override
                public void onComplete() {

                }
            };
            if (params != null) {
                netApiService.getPostNetData(url, params).compose(getSchedulerObservableTransformer()).subscribe(observer);
            } else {
                netApiService.getPostNetData(url).compose(getSchedulerObservableTransformer()).subscribe(observer);
            }
        } else {
            if (netLoadListener != null) {
                netLoadListener.onNotNetOrException();
                netLoadListener.netClose();
            }
        }
    }

    /**
     * @param context
     * @param url
     */
    public void loadNetDataPostSync(Context context, String url)  throws IOException {
        loadNetDataPostSync(context, url, null);
    }

    /**
     * post 同步方法
     *
     * @param context
     * @param url
     * @param params
     */
    public Response<String> loadNetDataPostSync(Context context, String url, Map<String, Object> params) throws IOException {
        if (NetWorkUtils.checkNet(context)) {
            NetApiService netApiService = NetApiManager.getNetApiService();
            if (params != null) {
                return netApiService.getPostSyncNetData(url, params).execute();
            } else {
                return netApiService.getPostSyncNetData(url).execute();
            }
        } else {
            return null;
        }
    }

    /**
     * get请求
     *
     * @param context
     * @param url
     * @param netLoadListener
     */
    public void loadNetDataGet(Context context, String url, NetLoadListener netLoadListener) {
        loadNetDataGet(context, url, null, netLoadListener);
    }

    /**
     * get请求
     *
     * @param context
     * @param url
     * @param params
     * @param netLoadListener
     */
    public void loadNetDataGet(Context context, String url, Map<String, String> params, NetLoadListener netLoadListener) {
        if (NetWorkUtils.checkNet(context)) {
            NetApiService netApiService = NetApiManager.getNetApiService();
            Observer<String> observer = new Observer<String>() {
                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onNext(String result) {
                    try {
                        if (netLoadListener != null) {
                            netLoadListener.onSuccess(result);
                        }
                    } catch (Exception e) {
                        netLoadListener.onNotNetOrException();
                        netLoadListener.onError(e);
                    }
                }

                @Override
                public void onError(Throwable exception) {
                    if (netLoadListener != null) {
                        netLoadListener.onNotNetOrException();
                        netLoadListener.onError(exception);
                    }
                }

                @Override
                public void onComplete() {

                }
            };
            if (params != null) {
                netApiService.getGetNetData(url, params).compose(getSchedulerObservableTransformer()).subscribe(observer);
            } else {
                netApiService.getGetNetData(url).compose(getSchedulerObservableTransformer()).subscribe(observer);
            }
        } else {
            if (netLoadListener != null) {
                netLoadListener.onNotNetOrException();
                netLoadListener.netClose();
            }
        }
    }

    /**
     * 加载带缓存网络请求
     *
     * @param url
     * @param params
     * @param isForceNet
     * @param netLoadListener
     */
    // TODO: 2018/12/4 缓存待加入
    public void loadNetDataGetCache(String url, Map<String, String> params, boolean isForceNet, NetLoadListener netLoadListener) {
        if (!TextUtils.isEmpty(url)) {
            NetApiService netApiService = NetApiManager.getNetCacheApiService(isForceNet);
            Observer<String> observer = new Observer<String>() {
                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onNext(String result) {
                    try {
                        if (netLoadListener != null) {
                            netLoadListener.onSuccess(result);
                        }
                    } catch (Exception e) {
                        netLoadListener.onError(e);
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
            if (params != null) {
                netApiService.getGetNetData(url, params).compose(getSchedulerObservableTransformer()).subscribe(observer);
            } else {
                netApiService.getGetNetData(url).compose(getSchedulerObservableTransformer()).subscribe(observer);
            }
        } else {
            if (netLoadListener != null) {
                netLoadListener.onError(new Throwable("url为空"));
            }
        }
    }

    public void downFile(String url, String fileDir, String fileName, DownloadListener netLoadProgressListener) {
        if (NetWorkUtils.checkNet(mAppContext)) {
            if (netLoadProgressListener != null) {
                int lastCode = url.lastIndexOf("/");
                if (lastCode == -1 || lastCode + 1 >= url.length()) {
                    showToast(mAppContext, "地址错误！");
                    return;
                }
                String baseUrl = url.substring(0, lastCode + 1);
                try {
                    DownloadHelper downloadHelper = new DownloadHelper(netLoadProgressListener, baseUrl);
                    downloadHelper.downloadFile(url, fileDir, fileName);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            if (netLoadProgressListener != null) {
                netLoadProgressListener.onFail(new Throwable(mAppContext.getString(R.string.unConnectedNetwork)));
            }
        }
    }

    /**
     * 带progres接口
     */
    public interface NetLoadProgressListener {

        void onStart();

        void onSuccess(File result);

        void onLoading(long total, long current);

        void onError(Throwable throwable);

        void netClose();
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
     * @param resultCode  返回码
     */
    public void showLoadSirString(LoadService loadService, List<T> list, String resultCode) {
        if (loadService != null) {
            if (list != null && list.size() > 0) {
                loadService.showWithConvertor(SUCCESS_CODE);
            } else if (!TextUtils.isEmpty(resultCode)) {
                loadService.showWithConvertor(resultCode);
            } else {
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
     *
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
