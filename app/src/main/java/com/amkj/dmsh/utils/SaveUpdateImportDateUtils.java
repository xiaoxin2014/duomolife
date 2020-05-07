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

import static android.content.Context.MODE_PRIVATE;
import static com.amkj.dmsh.MainActivity.ImgKey;
import static com.amkj.dmsh.MainActivity.LauncherAdIdKey;
import static com.amkj.dmsh.MainActivity.OriginalImgUrl;
import static com.amkj.dmsh.MainActivity.SkipUrlKey;
import static com.amkj.dmsh.MainActivity.TimeKey;
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
        weakReference = new WeakReference<>(activity);
        String url = Url.H_BOTTOM_ICON;
        Map<String, Object> params = new HashMap<>();
        /**
         * 3.1.8 加入 区分以前底部导航只能加入一个web地址，首页默认为app首页 bug
         */
        params.put("version", version);
        SharedPreferences sharedPreferences = activity.getSharedPreferences("MainNav", MODE_PRIVATE);
        NetLoadUtils.getNetInstance().loadNetDataPost(weakReference.get(), url, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                if (weakReference.get() != null) {

                    MainNavEntity mainNavEntity = GsonUtils.fromJson(result, MainNavEntity.class);
                    if (mainNavEntity != null
                            && mainNavEntity.getCode().equals(SUCCESS_CODE)
                            && mainNavEntity.getMainNavBeanList().size() == 5
                            && !isTimeExpress(mainNavEntity)) {
                        /**
                         * v3.1.9 修改判断 当前时间是否大于过期时间
                         */
                        SharedPreferences.Editor edit = sharedPreferences.edit();
                        edit.putString("NavDate", result);
                        edit.apply();
                        for (MainNavBean mainNavBean : mainNavEntity.getMainNavBeanList()) {
                            saveImageToFile(activity, mainNavBean.getPicUrl());
                            saveImageToFile(activity, mainNavBean.getPicUrlSecond());
                        }
                    } else {
                        if (version == 3) {
                            getMainIconData(activity, 2);
                        } else {
                            if (sharedPreferences != null) {
                                clearData(sharedPreferences.edit());
                            }
                        }
                    }
                }
            }

            @Override
            public void onError(Throwable throwable) {
                if (sharedPreferences != null) {
                    clearData(sharedPreferences.edit());
                }
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
                    if (!imageUrl.equals(communalADActivityBean.getPicUrl())) {
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
                                ? communalADActivityBean.getAndroidLink() : "app://");
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
