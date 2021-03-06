package com.amkj.dmsh.network;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.BaseEntity;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.bean.CommunalUserInfoEntity.CommunalUserInfoBean;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.ConstantVariable;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.netloadpage.NetEmptyCallback;
import com.amkj.dmsh.netloadpage.NetErrorCallback;
import com.amkj.dmsh.netloadpage.NetLoadCallback;
import com.amkj.dmsh.netloadpage.NetLoadTranslucenceCallback;
import com.amkj.dmsh.network.downfile.DownloadHelper;
import com.amkj.dmsh.network.downfile.DownloadListener;
import com.amkj.dmsh.rxeasyhttp.EasyHttp;
import com.amkj.dmsh.rxeasyhttp.cache.model.CacheMode;
import com.amkj.dmsh.rxeasyhttp.cache.model.CacheResult;
import com.amkj.dmsh.rxeasyhttp.callback.SimpleCallBack;
import com.amkj.dmsh.rxeasyhttp.exception.ApiException;
import com.amkj.dmsh.rxeasyhttp.model.HttpParams;
import com.amkj.dmsh.utils.NetWorkUtils;
import com.amkj.dmsh.utils.SharedPreUtils;
import com.amkj.dmsh.views.alertdialog.AlertDialogHelper;
import com.amkj.dmsh.utils.gson.GsonUtils;
import com.kingja.loadsir.callback.Callback;
import com.kingja.loadsir.callback.SuccessCallback;
import com.kingja.loadsir.core.Convertor;
import com.kingja.loadsir.core.LoadService;

import org.greenrobot.eventbus.EventBus;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.Nullable;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.getStringMapValue;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.ERROR_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.TOKEN_EXPIRE_TIME;
import static com.amkj.dmsh.dao.UserDao.savePersonalInfoCache;
import static com.amkj.dmsh.rxeasyhttp.cache.model.CacheMode.CACHEANDREMOTE;
import static com.amkj.dmsh.rxeasyhttp.cache.model.CacheMode.FIRSTREMOTE;
import static com.amkj.dmsh.rxeasyhttp.cache.model.CacheMode.ONLYCACHE;

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
    private static boolean confirmIng = true;  //避免确认token过期接口重复调用
    private AlertDialogHelper mNotificationAlertDialogHelper;
    private long mLastTime;
    //需要添加埋点的接口（收藏商品，加购以及创建订单）
    private String[] sourceUrl = new String[]{Url.Q_COMBINE_PRODUCT_ADD_CAR, Url.Q_SHOP_DETAILS_ADD_CAR, Url.Q_SP_DETAIL_PRO_COLLECT, Url.Q_CREATE_GROUP_NEW_INDENT, Url.Q_CREATE_INDENT};

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
    public void loadNetDataPost(Activity context, String url, NetLoadListener netLoadListener) {
        //调用接口之前判断token是否过期,如果登录条件下过期，不调用接口
        if (checkTokenExpire(context, url)) {
            loadNetDataPost(context, url, null, netLoadListener);
        } else {
            netLoadListener.onNotNetOrException();
        }
    }

    /**
     * post 请求
     *
     * @param context
     * @param url
     * @param params          兼容以前map<></>
     * @param netLoadListener
     */
    public void loadNetDataPost(Activity context, String url, Map<String, Object> params, NetLoadListener netLoadListener) {
        Map<String, Object> map = new HashMap<>();
        if (params != null) {
            map.putAll(params);
        }
        if (context instanceof BaseActivity) {
            map.putAll(((BaseActivity) context).commonMap);
        }

        //添加埋点来源参数
        if (Arrays.asList(sourceUrl).contains(url)) {
            ConstantMethod.addSourceParameter(map);
        }

        if (NetWorkUtils.checkNet(context)) {
            HttpParams httpParams = getHttpParams(map);
            EasyHttp.post(url).params(httpParams).execute(context, new SimpleCallBack<String>() {
                @Override
                public void onError(ApiException e) {
                    try {
                        if (netLoadListener != null) {
                            netLoadListener.onNotNetOrException();
                            netLoadListener.onError(e);
                        }
                    } catch (Exception ignored) {
                    }
                }

                @Override
                public void onSuccess(String result) {
                    try {
                        //token校验失败，清除登录信息提示用户重新登录
                        BaseEntity baseEntity = GsonUtils.fromJson(result, BaseEntity.class);
                        if (baseEntity != null) {
                            String code = baseEntity.getCode();
                            if ("52".equals(code)) {
                                exitLogin(context);
                                netLoadListener.onNotNetOrException();
                                return;
                            }
                        }
                        if (netLoadListener != null) {
                            netLoadListener.onSuccess(result);
                        }
                    } catch (Exception e) {
                        try {
                            netLoadListener.onNotNetOrException();
                            netLoadListener.onError(e);
                        } catch (Exception ignored) {
                        }
                    }
                }
            });
        } else {
            try {
                showToast(R.string.unConnectedNetwork);
                if (netLoadListener != null) {
                    netLoadListener.onNotNetOrException();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * post 请求
     *
     * @param context
     * @param url
     * @param params          兼容以前map<></>
     * @param netLoadListener
     */
    public void loadNetDataPost(Context context, String url, Map<String, Object> params, NetLoadListener netLoadListener) {
        WeakReference<Context> weakReference = new WeakReference<>(context);
        if (NetWorkUtils.checkNet(context)) {
            HttpParams httpParams = getHttpParams(params);
            EasyHttp.post(url).params(httpParams).execute(context, new SimpleCallBack<String>() {
                @Override
                public void onError(ApiException e) {
                    if (weakReference.get() != null) {
                        try {
                            if (netLoadListener != null) {
                                netLoadListener.onNotNetOrException();
                                netLoadListener.onError(e);
                            }
                        } catch (Exception e1) {
                        }
                    }
                }

                @Override
                public void onSuccess(String result) {
                    if (weakReference.get() != null) {
                        try {
                            if (netLoadListener != null) {
                                netLoadListener.onSuccess(result);
                            }
                        } catch (Exception e) {
                            try {
                                netLoadListener.onNotNetOrException();
                                netLoadListener.onError(e);
                            } catch (Exception e1) {
                            }
                        }
                    }
                }
            });
        } else {
            try {
                showToast(R.string.unConnectedNetwork);
                if (netLoadListener != null) {
                    netLoadListener.onNotNetOrException();
                }
            } catch (Exception e) {
                e.printStackTrace();
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
     * post同步方法
     * @param context
     * @param url
     * @return 返回值为json值
     */
    public String loadNetDataPostSync(Context context, String url) {
        return loadNetDataPostSync(context, url, null);
    }

    public String loadNetDataPostSync(Context context, String url, Map<String, Object> params) {
        if (NetWorkUtils.checkNet(context)) {
            HttpParams httpParams = getHttpParams(params);
            final String[] resultSuccess = {null};
            EasyHttp.post(url).params(httpParams).syncRequest(true).execute(context, new SimpleCallBack<String>() {
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


    /**
     * 缓存策略-->
     * 是否有网络-->有网-->是否强制刷新数据-->是，先请求网络，请求网络失败后再加载缓存
     * 否，先使用缓存，不管是否存在，仍然请求网络
     * -->无网-->只读取缓存，缓存没有会返回null
     *
     * @param url                  必须是正常网址 区分正式测试库
     * @param params
     * @param isForceNet
     * @param netCacheLoadListener
     */
    public void loadNetDataGetCache(Activity activity, String url, LinkedHashMap<String, String> params, boolean isForceNet, NetCacheLoadListener netCacheLoadListener) {
        if (!TextUtils.isEmpty(url)) {
            Map<String, String> map = new HashMap<>();
            if (params != null) {
                map.putAll(params);
            }
            if (activity instanceof BaseActivity) {
                String reqId = (String) ((BaseActivity) activity).commonMap.get("reqId");
                if (!TextUtils.isEmpty(reqId)) map.put("reqId", reqId);
            }

            HttpParams httpParams = new HttpParams();
            httpParams.put(map);
            CacheMode cacheMode = NetWorkUtils.isConnectedByState(mAppContext) ? (isForceNet ? FIRSTREMOTE : CACHEANDREMOTE) : ONLYCACHE;
            EasyHttp.get(url).params(httpParams)
//                    cachekey 默认网址加参数
                    .cacheKey(getUrlNameKey(url, params))
//                    缓存过期时间 访问有网络，有效5分钟
//                    .cacheTime(NetWorkUtils.isConnectedByState(mAppContext) ? 5 * 60 * 1000 : 24 * 60 * 60 * 1000)
                    .cacheMode(cacheMode)
                    .execute(new SimpleCallBack<CacheResult<String>>() {
                        @Override
                        public void onError(ApiException e) {
                            try {
                                if (netCacheLoadListener != null) {
                                    netCacheLoadListener.onError(e);
                                    netCacheLoadListener.onNotNetOrException();
                                }
                            } catch (Exception e1) {
                            }
                        }

                        @Override
                        public void onSuccess(CacheResult<String> cacheResult) {
                            try {
                                if (cacheResult == null) {
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
                            } catch (Exception e) {
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
                    showToast("地址错误！");
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
            if (resultClass != null) {
                if (list != null && list.size() > 0) {
                    loadService.showWithConvertor(SUCCESS_CODE);
                } else if (!EMPTY_CODE.equals(resultClass.getCode()) && !SUCCESS_CODE.equals(resultClass.getCode())) {
                    loadService.showWithConvertor(ERROR_CODE);
                } else {
                    loadService.showWithConvertor(EMPTY_CODE);
                }
            } else {
                loadService.showWithConvertor(ERROR_CODE);
            }
        }
    }


    /**
     * 集合数据
     *
     * @param loadService
     * @param listNotNull 集合数据是否为空，针对多个集合数据
     * @param resultClass
     */
    @SuppressWarnings("unchecked")
    public void showLoadSir(LoadService loadService, boolean listNotNull, E resultClass) {
        if (loadService != null) {
            if (resultClass != null) {
                if (listNotNull) {
                    loadService.showWithConvertor(SUCCESS_CODE);
                } else if (ERROR_CODE.equals(resultClass.getCode())) {
                    loadService.showWithConvertor(ERROR_CODE);
                } else {
                    loadService.showWithConvertor(EMPTY_CODE);
                }
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
     */
    @SuppressWarnings("unchecked")
    public void showLoadSir(LoadService loadService, List<T> list) {
        if (loadService != null) {
            if (list != null && list.size() > 0) {
                loadService.showWithConvertor(SUCCESS_CODE);
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

    //检验token是否过期
    private boolean checkTokenExpire(Activity activity, String url) {
        if (ConstantMethod.userId > 0) {
            long tokenExpireTime = (long) SharedPreUtils.getParam(TOKEN_EXPIRE_TIME, 0L);
            long currentTimeMillis = System.currentTimeMillis();
            if (confirmIng && currentTimeMillis > tokenExpireTime && !(url.contains(Url.CONFIRM_LOGIN_TOKEN_EXPIRE) || url.contains(Url.FLUSH_LOGIN_TOKEN) || url.contains(Url.LOG_OUT))) {
                confirmIng = false;
                checkToken(activity);
                return false;
            }
        }
        return true;
    }

    private void checkToken(Activity mContext) {
        //请求接口判断是否真的过期
        NetLoadUtils.getNetInstance().loadNetDataPost(mContext, Url.CONFIRM_LOGIN_TOKEN_EXPIRE, null, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                CommunalUserInfoBean tokenExpireBean = GsonUtils.fromJson(result, CommunalUserInfoBean.class);
                if (tokenExpireBean != null) {
                    //未过期(更新本地过期时间)
                    if (tokenExpireBean.getStatus() == 1) {
                        SharedPreUtils.setParam(TOKEN_EXPIRE_TIME, System.currentTimeMillis() + tokenExpireBean.getExpireTime());
                    } else {
                        exitLogin(mContext);
                    }
                }
                confirmIng = true;
            }

            @Override
            public void onNotNetOrException() {
                confirmIng = true;
                super.onNotNetOrException();
            }
        });
    }

    //提示登录
    private void exitLogin(Activity mContext) {
        //判断条件是为了避免重复调用
        long currentTime = System.currentTimeMillis();
        if (ConstantMethod.userId > 0 && currentTime - mLastTime > 3000) {
            mLastTime = System.currentTimeMillis();
            //清除本地登录信息
            savePersonalInfoCache(mContext, null);
            //通知我的界面刷新
            EventBus.getDefault().post(new EventMessage(ConstantVariable.TOKEN_EXPIRE_LOG_OUT, ""));
            //提示用户登录
            if (mNotificationAlertDialogHelper == null) {
                mNotificationAlertDialogHelper = new AlertDialogHelper(mContext);
                mNotificationAlertDialogHelper.setAlertListener(new AlertDialogHelper.AlertConfirmCancelListener() {
                    @Override
                    public void confirm() {
                        ConstantMethod.getLoginStatus(mContext);
                    }

                    @Override
                    public void cancel() {
                    }
                });

                mNotificationAlertDialogHelper.setTitle("提醒")
                        .setMsg("长期未登录，请重新登录")
                        .setCancelText("取消")
                        .setMsgTextGravity(Gravity.CENTER)
                        .setConfirmText("登录");
            }
            mNotificationAlertDialogHelper.show();
        }
    }
}
