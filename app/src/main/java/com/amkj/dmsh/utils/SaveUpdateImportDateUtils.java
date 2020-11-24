package com.amkj.dmsh.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.amkj.dmsh.bean.MainNavEntity;
import com.amkj.dmsh.bean.MainNavEntity.MainNavBean;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.homepage.bean.CommunalADActivityEntity;
import com.amkj.dmsh.homepage.bean.CommunalADActivityEntity.CommunalADActivityBean;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.amkj.dmsh.utils.gson.GsonUtils;

import java.io.File;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.amkj.dmsh.MainActivity.ImgKey;
import static com.amkj.dmsh.MainActivity.LauncherAdIdKey;
import static com.amkj.dmsh.MainActivity.OriginalImgUrl;
import static com.amkj.dmsh.MainActivity.SkipUrlKey;
import static com.amkj.dmsh.MainActivity.TimeKey;
import static com.amkj.dmsh.constant.CommunalSavePutValueVariable.FILE_IMAGE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.utils.TimeUtils.getDateMilliSecond;
import static com.amkj.dmsh.utils.glide.GlideImageLoaderUtil.saveImageToFile;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2019/2/16
 * version 3.2.0
 * class description:保存重要数据
 */
public class SaveUpdateImportDateUtils {

    private static volatile SaveUpdateImportDateUtils saveUpdateImportDateUtils;
    private WeakReference<Activity> weakReference;

    private SaveUpdateImportDateUtils() {
    }

    public static SaveUpdateImportDateUtils getUpdateDataUtilsInstance() {
        if (saveUpdateImportDateUtils == null) {
            synchronized (SaveUpdateImportDateUtils.class) {
                if (saveUpdateImportDateUtils == null) {
                    saveUpdateImportDateUtils = new SaveUpdateImportDateUtils();
                }
            }
        }
        return saveUpdateImportDateUtils;
    }

    /**
     * 获取底部导航栏数据
     * v4.3.1 版本开始支持配置底部导航栏颜色
     * 先请求version=3，请求成功判断过期时间，如果未过期保存数据，如果过期请求version=2（未过期保存数据，过期直接清除本地数据）
     */
    public void getMainIconData(Activity activity, int version) {
        String url = Url.H_BOTTOM_ICON;
        Map<String, Object> params = new HashMap<>();
        /**
         * 3.1.8 加入 区分以前底部导航只能加入一个web地址，首页默认为app首页 bug
         */
        params.put("version", version);
        NetLoadUtils.getNetInstance().loadNetDataPost(activity, url, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                MainNavEntity mainNavEntity = GsonUtils.fromJson(result, MainNavEntity.class);
                if (mainNavEntity != null && mainNavEntity.getCode().equals(SUCCESS_CODE)
                        && mainNavEntity.getMainNavBeanList().size() == 5 && !isTimeExpress(mainNavEntity)) {
                    Map<String, String> picMap = new HashMap<>();
                    for (MainNavBean mainNavBean : mainNavEntity.getMainNavBeanList()) {
                        downLoadIcon(mainNavEntity, picMap, mainNavBean.getPicUrl(), activity);
                        downLoadIcon(mainNavEntity, picMap, mainNavBean.getPicUrlSecond(), activity);
                    }
                } else {
                    if (version == 3) {
                        getMainIconData(activity, 2);
                    } else {
                        SharedPreUtils.clear("MainNav", "NavDate");
                    }
                }
            }

            @Override
            public void onError(Throwable throwable) {
                SharedPreUtils.clear("MainNav", "NavDate");
            }
        });
    }

    private void downLoadIcon(MainNavEntity mainNavEntity, Map<String, String> picMap, String picUrl, Activity activity) {
        saveImageToFile(activity, picUrl, FILE_IMAGE, new GlideImageLoaderUtil.OriginalLoaderFinishListener() {
            @Override
            public void onSuccess(File file) {
                String path = file.getPath();
                picMap.put(picUrl, path);
                //全部下载完成，缓存数据到本地
                if (picMap.size() == 10) {
                    for (MainNavBean mainNavBean : mainNavEntity.getMainNavBeanList()) {
                        mainNavBean.setPicUrl(picMap.get(mainNavBean.getPicUrl()));
                        mainNavBean.setPicUrlSecond(picMap.get(mainNavBean.getPicUrlSecond()));
                    }
                    SharedPreUtils.setParam("MainNav", "NavDate", GsonUtils.toJson(mainNavEntity));
                }
            }

            @Override
            public void onError() {

            }
        });
    }

    /**
     * 时间是否过期
     *
     * @param mainNavEntity
     * @return
     */
    private boolean isTimeExpress(MainNavEntity mainNavEntity) {
        try {
            //格式化开始时间
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
            Date dateExpress = formatter.parse(mainNavEntity.getExpireTime());
            Date dateCurrent;
            if (!TextUtils.isEmpty(mainNavEntity.getCurrentTime())) {
                dateCurrent = formatter.parse(mainNavEntity.getCurrentTime());
            } else {
                dateCurrent = new Date();
            }
            return dateCurrent.getTime() >= dateExpress.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 启动广告
     */
    public void getLaunchBanner(Activity activity) {
        weakReference = new WeakReference<>(activity);
        String url = Url.H_LAUNCH_AD_DIALOG;
        SharedPreferences sharedPreferences = weakReference.get().getSharedPreferences("launchAD", Context.MODE_PRIVATE);
        final SharedPreferences.Editor edit = sharedPreferences.edit();
        NetLoadUtils.getNetInstance().loadNetDataPost(weakReference.get(), url, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {

                CommunalADActivityEntity categoryAD = GsonUtils.fromJson(result, CommunalADActivityEntity.class);
                if (categoryAD == null ||
                        !categoryAD.getCode().equals(SUCCESS_CODE) ||
                        categoryAD.getCommunalADActivityBeanList() == null ||
                        categoryAD.getCommunalADActivityBeanList().size() < 1) {
                    clearData(edit);
                    return;
                }
                List<CommunalADActivityBean> communalADActivityBeanList = categoryAD.getCommunalADActivityBeanList();
                CommunalADActivityBean communalADActivityBean = communalADActivityBeanList.get(communalADActivityBeanList.size() - 1);
                if (getDateMilliSecond(communalADActivityBean.getEndTime()) > Calendar.getInstance().getTime().getTime()) {
                    String imageUrl = sharedPreferences.getString(OriginalImgUrl, "");
                    String SkipUrl = sharedPreferences.getString(SkipUrlKey, "");
                    if (!imageUrl.equals(communalADActivityBean.getPicUrl())||!SkipUrl.equals(communalADActivityBean.getAndroidLink())) {
                        setLaunchAdData(communalADActivityBean, edit);
                    }
                } else {
                    clearData(edit);
                }
            }

            @Override
            public void onError(Throwable throwable) {
                clearData(edit);
            }
        });
    }

    /**
     * 清除数据
     *
     * @param edit
     */
    private void clearData(SharedPreferences.Editor edit) {
        if (edit != null) {
            edit.clear().apply();
        }
    }

    private void setLaunchAdData(CommunalADActivityBean communalADActivityBean, SharedPreferences.Editor edit) {
        if (communalADActivityBean == null) {
            clearData(edit);
            return;
        }
        final String pic_url = communalADActivityBean.getPicUrl();
        if (weakReference.get() != null) {
            GlideImageLoaderUtil.saveImageToFile(weakReference.get(), pic_url, "launch_ad", new GlideImageLoaderUtil.OriginalLoaderFinishListener() {
                @Override
                public void onSuccess(File file) {
                    if (edit != null && file != null) {
                        edit.putString(ImgKey, file.getAbsolutePath());
                        edit.putString(OriginalImgUrl, communalADActivityBean.getPicUrl());
                        edit.putInt(LauncherAdIdKey, communalADActivityBean.getId());
                        edit.putString(TimeKey, !TextUtils.isEmpty(communalADActivityBean.getShowTime()) ? communalADActivityBean.getShowTime() : "3");
                        edit.putString(SkipUrlKey, !TextUtils.isEmpty(communalADActivityBean.getAndroidLink())
                                ? communalADActivityBean.getAndroidLink() : "");
                        edit.apply();
                    }
                }

                @Override
                public void onError() {
                    clearData(edit);
                }
            });
        }
    }
}
