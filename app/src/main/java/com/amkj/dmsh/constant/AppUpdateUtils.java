package com.amkj.dmsh.constant;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.amkj.dmsh.AppUpdateDialogActivity;
import com.amkj.dmsh.bean.AppVersionEntity;
import com.amkj.dmsh.bean.AppVersionEntity.AppVersionBean;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.beta.UpgradeInfo;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;
import static com.amkj.dmsh.constant.CommunalSavePutValueVariable.APP_CURRENT_UPDATE_VERSION;
import static com.amkj.dmsh.constant.CommunalSavePutValueVariable.APP_MANDATORY_UPDATE_VERSION;
import static com.amkj.dmsh.constant.CommunalSavePutValueVariable.APP_VERSION_INFO;
import static com.amkj.dmsh.constant.CommunalSavePutValueVariable.INTERVAL_TIME;
import static com.amkj.dmsh.constant.CommunalSavePutValueVariable.LAST_UPDATE_TIME;
import static com.amkj.dmsh.constant.CommunalSavePutValueVariable.MANDATORY_UPDATE_DESCRIPTION;
import static com.amkj.dmsh.constant.CommunalSavePutValueVariable.MANDATORY_UPDATE_LAST_VERSION;
import static com.amkj.dmsh.constant.CommunalSavePutValueVariable.UPDATE_TIME;
import static com.amkj.dmsh.constant.CommunalSavePutValueVariable.VERSION_DOWN_LINK;
import static com.amkj.dmsh.constant.CommunalSavePutValueVariable.VERSION_UPDATE_DESCRIPTION;
import static com.amkj.dmsh.constant.CommunalSavePutValueVariable.VERSION_UPDATE_LOW;
import static com.amkj.dmsh.constant.ConstantMethod.getAppendNumber;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.getVersionName;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.utils.MarketUtils.getMarketApp;
import static com.amkj.dmsh.utils.TimeUtils.isEndOrStartTimeAddSeconds;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/4/26
 * version 3.1.2
 * class description:app???????????? ?????? ??????
 */
public class AppUpdateUtils {
    private static volatile AppUpdateUtils appUpdateUtils;
    private Context context;

    private AppUpdateUtils() {
    }

    public static AppUpdateUtils getInstance() {
        if (appUpdateUtils == null) {
            synchronized (AppUpdateUtils.class) {
                if (appUpdateUtils == null) {
                    appUpdateUtils = new AppUpdateUtils();
                }
            }
        }
        return appUpdateUtils;
    }

    /**
     * ??????app????????????
     */
    public void getAppUpdate(Activity context) {
        getAppUpdate(context, false);
    }

    /**
     * ????????????
     *
     * @param mContext
     * @param isManual ????????????
     */
    public void getAppUpdate(Context mContext, boolean isManual) {
        WeakReference<Context> contextWeakReference = new WeakReference<>(mContext);
        context = contextWeakReference.get();
        String url =  Url.APP_VERSION_INFO;
        Map<String, Object> params = new HashMap<>();
        params.put("device_type_id", 2);
        NetLoadUtils.getNetInstance().loadNetDataPost(context, url, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                AppVersionEntity appVersionEntity = AppVersionEntity.objectFromData(result);
                if (appVersionEntity != null) {
                    if (appVersionEntity.getCode().equals(SUCCESS_CODE)
                            && appVersionEntity.getAppVersionBean() != null) {
                        if (context == null) {
                            return;
                        }
                        AppVersionBean appVersionBean = appVersionEntity.getAppVersionBean();
                        SharedPreferences sharedPreferences = context.getSharedPreferences(APP_VERSION_INFO, MODE_PRIVATE);
                        String updateTime = sharedPreferences.getString(UPDATE_TIME, "");
                        String lastUpdateTime = sharedPreferences.getString(LAST_UPDATE_TIME, "");
                        long intervalTime = sharedPreferences.getLong(INTERVAL_TIME, 0);
                        /**
                         * ????????????  ????????????
                         */
                        if (isMandatoryUpdateVersion(appVersionBean.getCompel_version())) { // ?????????????????????
                            openDialog(appVersionBean, true);
                        } else if (isManual) { //??????????????????
                            setAppUpdateData(appVersionEntity, sharedPreferences);
                            if (isHaveHeightUpdate(getStrings(appVersionBean.getVersion()))
//                                    ???????????? ????????????
                                    || isHaveHeightUpdate(getStrings(appVersionBean.getLatestVersion()))) {
                                openDialog(appVersionBean, false);
                            } else {
                                /**
                                 * ??????????????????
                                 */
                                UpgradeInfo upgradeInfo = Beta.getUpgradeInfo();
                                if (upgradeInfo == null) {
                                    getMarketApp(context, "??????????????????????????????????????????~");
                                } else {
                                    Beta.checkUpgrade();
                                }
                            }
                        } else if (isHaveHeightUpdate(getStrings(appVersionBean.getVersion()))) {
                            if (!appVersionBean.getUpdateTime().equals(updateTime)) {
                                setAppUpdateData(appVersionEntity, sharedPreferences);
                                if (appVersionBean.getShowPop() == 1) {
                                    openDialog(appVersionBean, false);
                                }
                            } else if (!isEndOrStartTimeAddSeconds(lastUpdateTime, appVersionEntity.getCurrentTime(), intervalTime)) {
                                setAppUpdateData(appVersionEntity, sharedPreferences);
                                openDialog(appVersionBean, false);
                            }
                        }
                    }
                }
            }
        });
    }

    /**
     * ????????????????????????
     *
     * @param compel_version
     * @return
     */
    private boolean isMandatoryUpdateVersion(String compel_version) {
        if (!TextUtils.isEmpty(compel_version)) {
            String[] split = compel_version.split(",");
            for (int i = 0; i < split.length; i++) {
                String versionName = split[i];
                String constraintVersion = getAppendNumber(versionName);
                String currentVersion = getAppendNumber(getVersionName(context));
                int constraintLength = constraintVersion.length();
                int currentLength = currentVersion.length();
                int absNumber = Math.abs(constraintLength - currentLength);
                if (absNumber > 0) {
                    if (constraintLength > currentLength) {
                        currentVersion += String.format("%1$0" + absNumber + "d", 0);
                    } else {
                        constraintVersion += String.format("%1$0" + absNumber + "d", 0);
                    }
                }
                if (currentVersion.equals(constraintVersion)) {
                    return true;
                }
            }
        }
        return false;
    }

    private void setAppUpdateData(AppVersionEntity appVersionEntity, SharedPreferences sharedPreferences) {
        AppVersionBean appVersionBean = appVersionEntity.getAppVersionBean();
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(UPDATE_TIME, getStrings(appVersionBean.getUpdateTime()));
        edit.putLong(INTERVAL_TIME, appVersionBean.getInterval_seconds());
        edit.putString(LAST_UPDATE_TIME, appVersionEntity.getCurrentTime());
        edit.putString(APP_CURRENT_UPDATE_VERSION, appVersionBean.getVersion());
        edit.apply();
    }

    /**
     * @param appVersionBean
     */
    private void openDialog(AppVersionBean appVersionBean, boolean isMandatoryUpdate) {
        if (context == null) {
            return;
        }
        if (!TextUtils.isEmpty(appVersionBean.getLink())) {
            Intent intent = new Intent(context, AppUpdateDialogActivity.class);
            intent.putExtra(VERSION_DOWN_LINK, getStrings(appVersionBean.getLink()));
            intent.putExtra(VERSION_UPDATE_DESCRIPTION, getStrings(appVersionBean.getDescription()));
            intent.putExtra(VERSION_UPDATE_LOW, getStrings(appVersionBean.getLowestVersion()));
            intent.putExtra(APP_CURRENT_UPDATE_VERSION, getStrings(appVersionBean.getVersion()));
            intent.putExtra(MANDATORY_UPDATE_LAST_VERSION, getStrings(appVersionBean.getLatestVersion()));
            if (isMandatoryUpdate) {
                intent.putExtra(APP_MANDATORY_UPDATE_VERSION, "1");
                intent.putExtra(MANDATORY_UPDATE_DESCRIPTION, getStrings(appVersionBean.getCompel_up_desc()));
            }
            context.startActivity(intent);
            ((Activity) context).overridePendingTransition(0, 0);
        }
    }

    /**
     * ????????????????????????????????????
     *
     * @param targetVersion
     * @return
     */
    private boolean isHaveHeightUpdate(String targetVersion) {
        if (context == null) {
            return false;
        }
//        ??????????????????
        String constraintVersion = getAppendNumber(targetVersion);
        String currentVersion = getAppendNumber(getVersionName(context));
        int constraintLength = constraintVersion.length();
        int currentLength = currentVersion.length();
        int absNumber = Math.abs(constraintLength - currentLength);
        if (absNumber > 0) {
            if (constraintLength > currentLength) {
                currentVersion += String.format("%1$0" + absNumber + "d", 0);
            } else {
                constraintVersion += String.format("%1$0" + absNumber + "d", 0);
            }
        }
        return Long.parseLong(constraintVersion) > Long.parseLong(currentVersion);
    }
}
