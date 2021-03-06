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
 * class description:????????????
 */
public class NetLoadUtils<T, E extends BaseEntity> {

    private static NetLoadUtils netLoadUtils;
    private Convertor<String> convertor;
    private static boolean confirmIng = true;  //????????????token????????????????????????
    private AlertDialogHelper mNotificationAlertDialogHelper;
    private long mLastTime;
    //????????????????????????????????????????????????????????????????????????
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
        //????????????????????????token????????????,?????????????????????????????????????????????
        if (checkTokenExpire(context, url)) {
            loadNetDataPost(context, url, null, netLoadListener);
        } else {
            netLoadListener.onNotNetOrException();
        }
    }

    /**
     * post ??????
     *
     * @param context
     * @param url
     * @param params          ????????????map<></>
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

        //????????????????????????
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
                        //token?????????????????????????????????????????????????????????
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
     * post ??????
     *
     * @param context
     * @param url
     * @param params          ????????????map<></>
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
     * ???????????????????????????
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
     * post????????????
     * @param context
     * @param url
     * @return ????????????json???
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
     * ????????????-->
     * ???????????????-->??????-->????????????????????????-->????????????????????????????????????????????????????????????
     * ???????????????????????????????????????????????????????????????
     * -->??????-->???????????????????????????????????????null
     *
     * @param url                  ????????????????????? ?????????????????????
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
//                    cachekey ?????????????????????
                    .cacheKey(getUrlNameKey(url, params))
//                    ?????????????????? ????????????????????????5??????
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
                                        netCacheLoadListener.onError(new Throwable("???????????????"));
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
                                        netCacheLoadListener.onError(new Throwable("???????????????"));
                                        netCacheLoadListener.onNotNetOrException();
                                    }
                                }
                            } catch (Exception e) {
                            }
                        }
                    });
        } else {
            if (netCacheLoadListener != null) {
                netCacheLoadListener.onError(new Throwable("url??????"));
            }
        }
    }

    private String getUrlNameKey(String url, LinkedHashMap<String, String> linkedHashMap) {
        try {
            if (TextUtils.isEmpty(url)) {
                throw new IllegalAccessException("url??????");
            }
            // ??????url??????
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
     * ????????????
     *
     * @param url
     * @param fileDir
     * @param netLoadProgressListener
     */
    public void downFile(String url, String fileDir, DownloadListener netLoadProgressListener) {
        if (NetWorkUtils.checkNet(mAppContext)) {
            if (netLoadProgressListener != null) {
                int lastCode = url.lastIndexOf("/");
//                baseUrl ????????????
                if (lastCode == -1 || lastCode + 1 >= url.length()) {
                    showToast("???????????????");
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
     * ????????????
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
     * ????????????
     *
     * @param loadService
     * @param listNotNull ???????????????????????????????????????????????????
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
     * ????????????
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
     * ?????????
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
     * ??????????????? ????????????????????????????????????????????????
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
     * ???????????? ??????????????????
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

    //??????token????????????
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
        //????????????????????????????????????
        NetLoadUtils.getNetInstance().loadNetDataPost(mContext, Url.CONFIRM_LOGIN_TOKEN_EXPIRE, null, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                CommunalUserInfoBean tokenExpireBean = GsonUtils.fromJson(result, CommunalUserInfoBean.class);
                if (tokenExpireBean != null) {
                    //?????????(????????????????????????)
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

    //????????????
    private void exitLogin(Activity mContext) {
        //???????????????????????????????????????
        long currentTime = System.currentTimeMillis();
        if (ConstantMethod.userId > 0 && currentTime - mLastTime > 3000) {
            mLastTime = System.currentTimeMillis();
            //????????????????????????
            savePersonalInfoCache(mContext, null);
            //????????????????????????
            EventBus.getDefault().post(new EventMessage(ConstantVariable.TOKEN_EXPIRE_LOG_OUT, ""));
            //??????????????????
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

                mNotificationAlertDialogHelper.setTitle("??????")
                        .setMsg("?????????????????????????????????")
                        .setCancelText("??????")
                        .setMsgTextGravity(Gravity.CENTER)
                        .setConfirmText("??????");
            }
            mNotificationAlertDialogHelper.show();
        }
    }
}
