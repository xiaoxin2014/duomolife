package com.amkj.dmsh.network;

import android.content.Context;
import android.support.annotation.Nullable;
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
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.cache.model.CacheResult;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;
import com.zhouyou.http.model.HttpParams;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.getStringMapValue;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.ERROR_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.zhouyou.http.cache.model.CacheMode.CACHEANDREMOTEDISTINCT;
import static com.zhouyou.http.cache.model.CacheMode.FIRSTREMOTE;
import static com.zhouyou.http.cache.model.CacheMode.ONLYCACHE;

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

    /**post 请求
     * @param context
     * @param url
     * @param params 兼容以前map<></>
     * @param netLoadListener
     */
    public void loadNetDataPost(Context context, String url, Map<String, Object> params, NetLoadListener netLoadListener) {
        if (NetWorkUtils.checkNet(context)) {
//            先进行框架初始化
            NetApiManager.getInstance().initNetInstance();
            HttpParams httpParams = getHttpParams(params);
            EasyHttp.post(url).params(httpParams).execute(new SimpleCallBack<String>() {
                @Override
                public void onError(ApiException e) {
                    if (netLoadListener != null) {
                        netLoadListener.onNotNetOrException();
                        netLoadListener.onError(e);
                    }
                }

                @Override
                public void onSuccess(String result) {
                    try {
                        if (netLoadListener != null) {
                            netLoadListener.onSuccess(result);
                        }
                    } catch (Exception e) {
                        netLoadListener.onNotNetOrException();
                        netLoadListener.onError(e);
                    }
                }
            });
        } else {
            if (netLoadListener != null) {
                netLoadListener.onNotNetOrException();
                netLoadListener.netClose();
            }
        }
    }

    /**
     * 获取适配框架的参数
     *
     * @param params
     * @return
     */
    @Nullable
    private HttpParams getHttpParams(Map<String, Object> params) {
        HttpParams httpParams = null;
        if (params != null) {
            httpParams = new HttpParams();
            for (Map.Entry<String, Object> next1 : params.entrySet()) {
                String key = next1.getKey();
                String mapValue = getStringMapValue(next1.getValue());
                httpParams.put(key, mapValue);
            }
        }
        return httpParams;
    }

    /**
     * @param context
     * @param url
     */
    public void loadNetDataPostSync(Context context, String url) throws IOException {
        loadNetDataPostSync(context, url, null);
    }

    /**
     * post 同步方法
     *
     * @param context
     * @param url
     * @param params
     */
    public String loadNetDataPostSync(Context context, String url, Map<String, Object> params) {
        if (NetWorkUtils.checkNet(context)) {
            //            先进行框架初始化
            NetApiManager.getInstance().initNetInstance();
            HttpParams httpParams = getHttpParams(params);
            final String[] resultSuccess = {null};
            EasyHttp.post(url).params(httpParams).syncRequest(true).execute(new SimpleCallBack<String>() {
                @Override
                public void onError(ApiException e) {
                    resultSuccess[0] = null;
                }

                @Override
                public void onSuccess(String s) {
                    resultSuccess[0] = s;
                }
            });
            return resultSuccess[0];
        } else {
            return null;
        }
    }

    public void loadNetDataGetCache(String url, boolean isForceNet, NetCacheLoadListener netCacheLoadListener) {
        loadNetDataGetCache(url, null, isForceNet, netCacheLoadListener);
    }

    /**
     * 缓存策略-->
     * 是否有网络-->有网-->是否强制刷新数据-->是，先请求网络，请求网络失败后再加载缓存
     * 否，先使用缓存，不管是否存在，仍然请求网络
     * -->无网-->只读取缓存，缓存没有会返回null
     *
     * @param url 必须是正常网址 区分正式测试库
     * @param params
     * @param isForceNet
     * @param netCacheLoadListener
     */
    public void loadNetDataGetCache(String url, LinkedHashMap<String, String> params, boolean isForceNet, NetCacheLoadListener netCacheLoadListener) {
        if (!TextUtils.isEmpty(url)) {
            //            先进行框架初始化
            NetApiManager.getInstance().initNetInstance();
            HttpParams httpParams = new HttpParams();
            httpParams.put(params);
            EasyHttp.get(url).params(httpParams)
//                    cachekey 默认网址加参数
                    .cacheKey(getUrlNameKey(url, params))
//                    缓存过期时间 访问有网络，有效5分钟
//                    .cacheTime(NetWorkUtils.isConnectedByState(mAppContext) ? 5 * 60 * 1000 : 24 * 60 * 60 * 1000)
                    .cacheMode(NetWorkUtils.isConnectedByState(mAppContext) ? (isForceNet ? FIRSTREMOTE : CACHEANDREMOTEDISTINCT) : ONLYCACHE)
                    .execute(new SimpleCallBack<CacheResult<String>>() {
                        @Override
                        public void onError(ApiException e) {
                            if (netCacheLoadListener != null) {
                                netCacheLoadListener.onError(e);
                                netCacheLoadListener.onNotNetOrException();
                            }
                        }

                        @Override
                        public void onSuccess(CacheResult<String> cacheResult) {
                            if(cacheResult==null){
                                if (netCacheLoadListener != null) {
                                    netCacheLoadListener.onError(new Throwable("数据为空！"));
                                    netCacheLoadListener.onNotNetOrException();
                                }
                                return;
                            }
                            String result = cacheResult.data;
                            if (!TextUtils.isEmpty(result)) {
                                if (netCacheLoadListener != null) {
                                    netCacheLoadListener.onSuccess(result);
                                    netCacheLoadListener.onSuccessCacheResult(cacheResult);
                                }
                            } else {
                                if (netCacheLoadListener != null) {
                                    netCacheLoadListener.onError(new Throwable("缓存为空！"));
                                    netCacheLoadListener.onNotNetOrException();
                                }
                            }
                        }
                    });
        } else {
            if (netCacheLoadListener != null) {
                netCacheLoadListener.onError(new Throwable("url为空"));
            }
        }
    }

    private String getUrlNameKey(String url, LinkedHashMap<String, String> linkedHashMap) {
        try {
            if (TextUtils.isEmpty(url)) {
                throw new IllegalAccessException("url为空");
            }
            // 添加url参数
            if (linkedHashMap != null) {
                Iterator<String> it = linkedHashMap.keySet().iterator();
                StringBuilder sb = null;
                while (it.hasNext()) {
                    String key = it.next();
                    String value = linkedHashMap.get(key);
                    if (sb == null) {
                        sb = new StringBuilder();
                        sb.append("?");
                    } else {
                        sb.append("&");
                    }
                    sb.append(key);
                    sb.append("=");
                    sb.append(value);
                }
                url += (sb == null ? "" : sb);
            }
            return url;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 下载文件
     *
     * @param url
     * @param fileDir
     * @param netLoadProgressListener
     */
    public void downFile(String url, String fileDir, DownloadListener netLoadProgressListener) {
        if (NetWorkUtils.checkNet(mAppContext)) {
            if (netLoadProgressListener != null) {
                int lastCode = url.lastIndexOf("/");
//                baseUrl 必须填写
                if (lastCode == -1 || lastCode + 1 >= url.length()) {
                    showToast(mAppContext, "地址错误！");
                    return;
                }
                String baseUrl = url.substring(0, lastCode + 1);
                try {
                    DownloadHelper downloadHelper = new DownloadHelper(netLoadProgressListener, baseUrl);
                    downloadHelper.downloadFile(url, fileDir);
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
     * 集合数据
     *
     * @param loadService
     * @param list
     * @param resultClass
     */
    @SuppressWarnings("unchecked")
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
    @SuppressWarnings("unchecked")
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
    @SuppressWarnings("unchecked")
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
    @SuppressWarnings("unchecked")
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
    @SuppressWarnings("unchecked")
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
