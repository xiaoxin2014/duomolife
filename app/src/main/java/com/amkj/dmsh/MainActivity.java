package com.amkj.dmsh;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.ali.auth.third.ui.context.CallbackContext;
import com.alibaba.fastjson.JSON;
import com.amkj.dmsh.address.AddressUtils;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.base.TinkerBaseApplicationLike;
import com.amkj.dmsh.bean.CommunalUserInfoEntity;
import com.amkj.dmsh.bean.CommunalUserInfoEntity.CommunalUserInfoBean;
import com.amkj.dmsh.bean.MainIconBean;
import com.amkj.dmsh.bean.MainNavEntity;
import com.amkj.dmsh.bean.MainNavEntity.MainNavBean;
import com.amkj.dmsh.bean.OSSConfigEntity;
import com.amkj.dmsh.bean.OSSConfigEntity.OSSConfigBean;
import com.amkj.dmsh.bean.PushInfoEntity;
import com.amkj.dmsh.bean.RequestStatus;
import com.amkj.dmsh.bean.SysNotificationEntity;
import com.amkj.dmsh.constant.AppUpdateUtils;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.ConstantVariable;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.dominant.fragment.QualityFragment;
import com.amkj.dmsh.homepage.activity.MainPageTabBarActivity;
import com.amkj.dmsh.homepage.bean.CommunalADActivityEntity;
import com.amkj.dmsh.homepage.bean.CommunalADActivityEntity.CommunalADActivityBean;
import com.amkj.dmsh.homepage.fragment.AliBCFragment;
import com.amkj.dmsh.homepage.fragment.HomePageFragment;
import com.amkj.dmsh.homepage.fragment.HomePageNewFragment;
import com.amkj.dmsh.homepage.fragment.TimeShowNewFragment;
import com.amkj.dmsh.mine.bean.SavePersonalInfoBean;
import com.amkj.dmsh.mine.fragment.MineDataFragment;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.qyservice.QyServiceUtils;
import com.amkj.dmsh.release.dialogutils.AlertSettingBean;
import com.amkj.dmsh.release.dialogutils.AlertView;
import com.amkj.dmsh.utils.FileStreamUtils;
import com.amkj.dmsh.utils.SaveUpdateImportDateUtils;
import com.amkj.dmsh.utils.SelectorUtil;
import com.amkj.dmsh.utils.SharedPreUtils;
import com.amkj.dmsh.utils.alertdialog.AlertDialogHelper;
import com.amkj.dmsh.utils.alertdialog.AlertDialogImage;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.amkj.dmsh.utils.restartapputils.RestartAPPTool;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tencent.bugly.beta.tinker.TinkerManager;
import com.umeng.socialize.UMShareAPI;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import me.jessyan.autosize.utils.AutoSizeUtils;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.CommunalSavePutValueVariable.APP_FIRST_TIMES;
import static com.amkj.dmsh.constant.CommunalSavePutValueVariable.APP_SAVE_VERSION;
import static com.amkj.dmsh.constant.ConstantMethod.adDialogClickTotal;
import static com.amkj.dmsh.constant.ConstantMethod.getDateFormat;
import static com.amkj.dmsh.constant.ConstantMethod.getDeviceAppNotificationStatus;
import static com.amkj.dmsh.constant.ConstantMethod.getPersonalInfo;
import static com.amkj.dmsh.constant.ConstantMethod.getStringChangeBoolean;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.isSameTimeDay;
import static com.amkj.dmsh.constant.ConstantMethod.isTimeDayEligibility;
import static com.amkj.dmsh.constant.ConstantMethod.savePersonalInfoCache;
import static com.amkj.dmsh.constant.ConstantMethod.setDeviceInfo;
import static com.amkj.dmsh.constant.ConstantMethod.setSkipPath;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.GET_FIRST_INSTALL_INFO;
import static com.amkj.dmsh.constant.ConstantVariable.IS_NEW_USER;
import static com.amkj.dmsh.constant.ConstantVariable.MAIN_FIND;
import static com.amkj.dmsh.constant.ConstantVariable.MAIN_HOME;
import static com.amkj.dmsh.constant.ConstantVariable.MAIN_MINE;
import static com.amkj.dmsh.constant.ConstantVariable.MAIN_QUALITY;
import static com.amkj.dmsh.constant.ConstantVariable.MAIN_TIME;
import static com.amkj.dmsh.constant.ConstantVariable.OTHER_WECHAT;
import static com.amkj.dmsh.constant.ConstantVariable.PUSH_CHECK;
import static com.amkj.dmsh.constant.ConstantVariable.PUSH_CHECK_TIME;
import static com.amkj.dmsh.constant.ConstantVariable.REFRESH_MESSAGE_TOTAL;
import static com.amkj.dmsh.constant.ConstantVariable.REGEX_TEXT;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.TOKEN_EXPIRE_TIME;
import static com.amkj.dmsh.constant.ConstantVariable.TOKEN_REFRESH_TIME;
import static com.amkj.dmsh.constant.ConstantVariable.UP_TOTAL_SIZE;
import static com.amkj.dmsh.constant.ConstantVariable.isDebugTag;
import static com.amkj.dmsh.constant.ConstantVariable.isShowTint;
import static com.amkj.dmsh.constant.Url.APP_SYS_NOTIFICATION;
import static com.amkj.dmsh.constant.Url.CHECK_CLEAR_USER_DATA;
import static com.amkj.dmsh.constant.Url.H_AD_DIALOG;
import static com.amkj.dmsh.utils.ServiceDownUtils.INSTALL_APP_PROGRESS;
import static com.amkj.dmsh.utils.glide.GlideImageLoaderUtil.fileIsExist;
import static com.amkj.dmsh.utils.glide.GlideImageLoaderUtil.getImageFilePath;
import static com.amkj.dmsh.utils.glide.GlideImageLoaderUtil.saveImageToFile;

;

/**
 * @author Liuguipeng
 * @date 2017/10/31
 */
public class MainActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.rp_bottom_main)
    RadioGroup rp_bottom_main;
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    public static final int MINE_REQ_CODE = 13;
    private Map<String, String> params = new HashMap<>();
    private AlertView selectServer;
    private String[] SERVER = {"正式库", "测试库", "招立", "泽鑫", "Mr.W", "修改UID", "预发布", "王凯2", "鸿星"};
    private AlertView mAlertViewExt;
    private EditText etName;
    private List<MainIconBean> iconDataList = new ArrayList<>();
    public static final String ImgKey = "ImgPath";
    public static final String TimeKey = "ShowSeconds";
    public static final String SkipUrlKey = "SkipUrl";
    public static final String LauncherAdIdKey = "AD_ID";
    public static String OriginalImgUrl = "OriginalImgUrl";
    //    地址存储路径
    private Fragment fragment;
    private boolean isChecked;
    private ConstantMethod constantMethod;
    private Map<String, String> pushMap;
    private AlertDialogImage alertDialogAdImage;
    private AlertDialogHelper alertDialogHelper;
    //    底部限制98 padding top&bottom 10*2 drawablepadding 10 textsize 20
    private float iconHeight = 32f;

    @Override
    protected void postEventResult(@NonNull EventMessage message) {

        if ("skipMinePage".equals(message.type)) {
            changeAdaptivePage(MAIN_MINE);
        } else if (message.type.equals("loginShowDialog")) {
            if (constantMethod == null) {
                constantMethod = new ConstantMethod();
            }
            constantMethod.getNewUserCouponDialog(MainActivity.this);
        }
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViews() {
        fragmentManager = getSupportFragmentManager();
        boolean isFirstTime = setIntentBottomIconData();
        if (isFirstTime) {
//        七鱼客服登录 获取用户信息 登进登出……
            getNetDataInfo();
            //刷新token
            flushToken();
//            弹窗广告
            getADDialog();
//            启动广告
            SaveUpdateImportDateUtils.getUpdateDataUtilsInstance().getLaunchBanner(this);

            if (isDebugTag) {
                getSelectedDialog();
            }
//            打开app时间统计
//            加载OSS配置  (备注：做统计使用，保留该接口调用)
            getOSSConfig();
//            获取地址版本
            getAddressVersion();
//            获取图标更新
            SaveUpdateImportDateUtils.getUpdateDataUtilsInstance().getMainIconData(this);
//            获取push信息
            getFirstPushInfo();
//            获取更新
            AppUpdateUtils.getInstance().getAppUpdate(MainActivity.this);
//            统计字段数据更新
            getAppUpdateJson();
//            统计数据上传大小
            getUpTotalSize();
//            设置分享提示
            setShareTint();
//            检查推送权限
            checkPushPermission();
//            统计首次安装设备信息
            getFirstInstallInfo();
        }


        rp_bottom_main.setOnCheckedChangeListener((group, checkedId) -> {
            isChecked = true;
            for (int i = 0; i < group.getChildCount(); i++) {
                RadioButton rb = (RadioButton) group.getChildAt(i);
                MainIconBean mainIconBean = (MainIconBean) rb.getTag(R.id.main_page);
                rb.setOnClickListener(MainActivity.this);
                if (rb.getId() == checkedId) {
                    rb.setChecked(true);
                    if (mainIconBean != null) {
                        changePage(mainIconBean);
                    } else {
                        skipDefaultNum(i);
                    }
                }
            }
        });
    }

    /**
     * 统计首次安装设备信息
     */
    private void getFirstInstallInfo() {
        boolean isNewUser = (boolean) SharedPreUtils.getParam(ConstantVariable.DEMO_LIFE_FILE, IS_NEW_USER, false);
        boolean GetInfo = (boolean) SharedPreUtils.getParam(ConstantVariable.DEMO_LIFE_FILE, GET_FIRST_INSTALL_INFO, false);
        //如果是新用户并且没有成功调用统计接口
        if (isNewUser && !GetInfo) {
            NetLoadUtils.getNetInstance().loadNetDataPost(this, Url.FIRST_INSTALL_DEVICE_INFO, new NetLoadListenerHelper() {
                @Override
                public void onSuccess(String result) {
                    SharedPreUtils.setParam(ConstantVariable.DEMO_LIFE_FILE, IS_NEW_USER, false);
                    SharedPreUtils.setParam(ConstantVariable.DEMO_LIFE_FILE, GET_FIRST_INSTALL_INFO, true);
                }
                @Override
                public void onNotNetOrException() {
                    super.onNotNetOrException();
                }
            });
        }
    }

    /**
     * 刷新Token
     */
    private void flushToken() {
        long tokenExpireTime = ((long) SharedPreUtils.getParam(TOKEN_EXPIRE_TIME, 0L));
        long tokenRefreshTime = ((long) SharedPreUtils.getParam(TOKEN_REFRESH_TIME, 0L));
        //1.登录情况下 2.token有效 3.距离上次登录超过一天
        if (tokenExpireTime != 0 && System.currentTimeMillis() < tokenExpireTime && System.currentTimeMillis() - tokenRefreshTime > 86400000) {
            NetLoadUtils.getNetInstance().loadNetDataPost(this, Url.FLUSH_LOGIN_TOKEN, new NetLoadListenerHelper() {
                @Override
                public void onSuccess(String result) {
                    Gson gson = new Gson();
                    CommunalUserInfoBean tokenExpireBean = gson.fromJson(result, CommunalUserInfoBean.class);
                    //刷新本地token过期时间
                    SharedPreUtils.setParam(ConstantVariable.TOKEN_EXPIRE_TIME, System.currentTimeMillis() + tokenExpireBean.getTokenExpireSeconds());
                }
            });
            //记录刷新时间
            SharedPreUtils.setParam(TOKEN_REFRESH_TIME, System.currentTimeMillis());
        }
    }

    /**
     * 设置intent底部数据
     *
     * @return
     */
    private boolean setIntentBottomIconData() {
        Intent intent = getIntent();
        String type = intent.getStringExtra("type");
        String firstTimeCode = intent.getStringExtra("isFirstTime");
        boolean isFirstTime = TextUtils.isEmpty(firstTimeCode) || getStringChangeBoolean(firstTimeCode);
        setNavData();
        if (!TextUtils.isEmpty(type)) {
            changeAdaptivePage(type);
        } else {
            initMainPage();
        }
        return isFirstTime;
    }

    /**
     * 检查推送权限
     */
    private void checkPushPermission() {
        if (!getDeviceAppNotificationStatus(this)) {
            NetLoadUtils.getNetInstance().loadNetDataPost(this, APP_SYS_NOTIFICATION, new NetLoadListenerHelper() {
                @Override
                public void onSuccess(String result) {
                    Gson gson = new Gson();
                    SysNotificationEntity sysNotificationEntity = gson.fromJson(result, SysNotificationEntity.class);
                    SharedPreferences preferences = getSharedPreferences(PUSH_CHECK, MODE_PRIVATE);
                    String pushCheckTime = preferences.getString(PUSH_CHECK_TIME, "");
                    SharedPreferences.Editor edit = preferences.edit();
                    if (sysNotificationEntity != null && sysNotificationEntity.getSysNotificationBean() != null &&
                            SUCCESS_CODE.equals(sysNotificationEntity.getCode())) {
                        SysNotificationEntity.SysNotificationBean sysNotificationBean = sysNotificationEntity.getSysNotificationBean();
                        if (TextUtils.isEmpty(pushCheckTime) ||
                                isTimeDayEligibility(pushCheckTime, sysNotificationEntity.getSystemTime(), sysNotificationBean.getIntervalDay())) {
                            edit.putString(PUSH_CHECK_TIME, sysNotificationEntity.getSystemTime());
                            edit.apply();
                            AlertDialogHelper alertDialogHelper = new AlertDialogHelper(MainActivity.this);
                            alertDialogHelper.setAlertListener(new AlertDialogHelper.AlertConfirmCancelListener() {
                                @Override
                                public void confirm() {
                                    // 根据isOpened结果，判断是否需要提醒用户跳转AppInfo页面，去打开App通知权限
                                    Intent intent = new Intent();
                                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                                    intent.setData(uri);
                                    startActivity(intent);
                                    alertDialogHelper.dismiss();
                                }

                                @Override
                                public void cancel() {
                                    alertDialogHelper.dismiss();
                                }
                            });
                            alertDialogHelper.setTitle("通知提示")
                                    .setMsg(TextUtils.isEmpty(sysNotificationBean.getContent()) ? "“多么生活”想给您发送通知,方便我们更好的为您服务，限时秒杀不再错过。" :
                                            sysNotificationBean.getContent())
                                    .setSingleButton(true)
                                    .setConfirmText("去设置");
                            alertDialogHelper.show();
                        }
                    } else {
                        edit.clear().apply();
                    }
                }
            });
        }
    }


    /**
     * 设置分享提示显示
     */
    private void setShareTint() {
        SharedPreferences showShareTint = getSharedPreferences("showShareTint", Context.MODE_PRIVATE);
        String shareDate = showShareTint.getString("shareDate", "");
        if (!TextUtils.isEmpty(shareDate)) {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            if (getDateFormat(shareDate, "yyyy-MM-dd", "yyyy-MM-dd")
                    .equals(getDateFormat(year + "-" + month + "-" + day, "yyyy-MM-dd", "yyyy-MM-dd"))) {
                isShowTint = false;
            }
        }
    }

    /**
     * 获取首次启动push信息
     */
    private void getFirstPushInfo() {
        if (userId > 0) {
            String url = Url.BASE_URL + Url.FIRST_PUSH_INFO;
            Map<String, Object> params = new HashMap<>();
            params.put("userId", userId);
            NetLoadUtils.getNetInstance().loadNetDataPost(this, url, params, new NetLoadListenerHelper() {
                @Override
                public void onSuccess(String result) {
                    PushInfoEntity pushInfoEntity = PushInfoEntity.objectFromData(result);
                    if (pushInfoEntity != null
                            && pushInfoEntity.getCode().equals(SUCCESS_CODE)
                            && pushInfoEntity.getAppPushInfo() != null && pushInfoEntity.getAppPushInfo().getId() > 0) {
                        SharedPreferences sharedPreferences = getSharedPreferences("pushInfo", MODE_PRIVATE);
                        String pushInfoMap = sharedPreferences.getString("pushInfoMap", "");
                        if (!TextUtils.isEmpty(pushInfoMap)) {
                            try {
                                Gson gson = new Gson();
                                pushMap = gson.fromJson(pushInfoMap, new TypeToken<Map<String, String>>() {
                                }.getType());
                                if (pushMap != null && !TextUtils.isEmpty(pushMap.get(String.valueOf(userId)))) {
                                    String pushTime = pushMap.get(String.valueOf(userId));
                                    if (!isSameTimeDay(pushTime, pushInfoEntity.getCurrentTime())) {
                                        setPushInfoReceive(sharedPreferences, pushInfoEntity);
                                    }
                                } else {
                                    setPushInfoReceive(sharedPreferences, pushInfoEntity);
                                }
                            } catch (Exception e) {
                                setPushInfoReceive(sharedPreferences, pushInfoEntity);
                            }
                        } else {
                            setPushInfoReceive(sharedPreferences, pushInfoEntity);
                        }
                    }
                }
            });
        }
    }

    private void setPushInfoReceive(SharedPreferences sharedPreferences, PushInfoEntity pushInfoEntity) {
        if (constantMethod == null) {
            constantMethod = new ConstantMethod();
        }
        final int[] currentSecond = {0};
        constantMethod.createSchedule();
        constantMethod.setRefreshTimeListener(() -> {
            currentSecond[0]++;
            if (currentSecond[0] == pushInfoEntity.getAppPushInfo().getPushSecond()) {
                constantMethod.stopSchedule();
                getReceivePushInfo(pushInfoEntity, sharedPreferences);
            }
        });
    }

    private void getReceivePushInfo(PushInfoEntity pushInfoEntity, SharedPreferences sharedPreferences) {
        String url = Url.BASE_URL + Url.FIRST_PUSH_INFO_RECEIVE;
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("pushId", pushInfoEntity.getAppPushInfo().getId());
        NetLoadUtils.getNetInstance().loadNetDataPost(this, url, params, null);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        if (pushMap == null) {
            pushMap = new HashMap<>();
        }
        pushMap.put(String.valueOf(userId), pushInfoEntity.getCurrentTime());
        edit.putString("pushInfoMap", new Gson().toJson(pushMap));
        edit.apply();
    }

    private void skipDefaultNum(int i) {
        String iconUrl;
        switch (i) {
            case 0:
                iconUrl = MAIN_HOME;
                break;
            case 1:
                iconUrl = MAIN_QUALITY;
                break;
            case 2:
                iconUrl = MAIN_TIME;
                break;
            case 3:
                iconUrl = MAIN_FIND;
                break;
            case 4:
                iconUrl = MAIN_MINE;
                break;
            default:
                iconUrl = "";
                break;
        }
        changePage(new MainIconBean(i, iconUrl));
    }

    private void setNavData() {
        iconDataList.clear();
        SharedPreferences sharedPreferences = getSharedPreferences("MainNav", MODE_PRIVATE);
        String result = sharedPreferences.getString("NavDate", "");
        if (!TextUtils.isEmpty(result)) {
            Gson gson = new Gson();
            MainNavEntity mainNavEntity = gson.fromJson(result, MainNavEntity.class);
            if (mainNavEntity != null && mainNavEntity.getMainNavBeanList().size() == 5
                    && !isTimeExpress(mainNavEntity)) {
                setDynamicMal(mainNavEntity.getMainNavBeanList());
            } else {
                setNorMal();
            }
        } else {
            setNorMal();
        }
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
     * 设置底栏Icon 设置字体颜色 icon
     *
     * @param mainNavBeanList
     */
    private void setDynamicMal(List<MainNavBean> mainNavBeanList) {
        for (int i = 0; i < mainNavBeanList.size(); i++) {
            RadioButton rb = (RadioButton) rp_bottom_main.getChildAt(i);
            MainNavBean mainNavBean = mainNavBeanList.get(i);
            if (!TextUtils.isEmpty(mainNavBean.getSecondColor()) && !TextUtils.isEmpty(mainNavBean.getMainColor())) {
                SelectorUtil.selectorTextColor("#" + getStrings(mainNavBean.getSecondColor()), "#" + getStrings(mainNavBean.getMainColor()), rb);
            } else {
                rb.setTextColor(getResources().getColorStateList(R.color.sel_text_gray_light_blue));
            }
            initDownNavData(rb, mainNavBean, i);
        }
    }

    /**
     * 初始化底栏Icon
     *
     * @param rb
     * @param mainNavBean
     */
    private void initDownNavData(RadioButton rb, MainNavBean mainNavBean, int position) {
        String picPath = getImageFilePath(this, mainNavBean.getPicUrl());
        String picSecondPath = getImageFilePath(this, mainNavBean.getPicUrlSecond());
        if (fileIsExist(picPath) && fileIsExist(picSecondPath)) {
            setDownBitmap(rb, picPath, picSecondPath);
            setDynamicButtonData(rb, mainNavBean, position);
            rb.setText(getStrings(mainNavBean.getTitle()));
        } else if (fileIsExist(picPath) || fileIsExist(picSecondPath)) {
//            设置相同Icon
            if (fileIsExist(picPath)) {
                setDownBitmap(rb, picPath, picPath);
            } else {
                setDownBitmap(rb, picSecondPath, picSecondPath);
            }
            setDynamicButtonData(rb, mainNavBean, position);
            rb.setText(getStrings(mainNavBean.getTitle()));
        } else {
//            如果数据已存在，文件不存在，重新保存图片 下次生效
            setNormalIcon(position, rb);
            saveImageToFile(MainActivity.this, mainNavBean.getPicUrl());
            saveImageToFile(MainActivity.this, mainNavBean.getPicUrlSecond());
        }
    }

    /**
     * 设置button动态数据
     *
     * @param button
     * @param mainNavBean
     * @param position
     */
    private void setDynamicButtonData(RadioButton button, MainNavBean mainNavBean, int position) {
        MainIconBean mainIconBean = new MainIconBean(position
                , getStrings(getNovIconUrl(mainNavBean.getAndroidLink()))
        );
        button.setTag(R.id.main_page, mainIconBean);
        iconDataList.add(mainIconBean);
    }

    /**
     * 判断是否是网页底栏
     *
     * @param androidLink
     * @return
     */
    private String getNovIconUrl(String androidLink) {
        Matcher matcher = Pattern.compile(REGEX_TEXT).matcher(androidLink);
        while (matcher.find()) {
            androidLink = matcher.group();
        }
        return androidLink;
    }

    /**
     * 是否网页地址
     *
     * @param androidLink
     * @return
     */
    private boolean isWebUrl(String androidLink) {
        if (TextUtils.isEmpty(androidLink)) {
            return false;
        }
        Matcher matcher = Pattern.compile(REGEX_TEXT).matcher(androidLink);
        while (matcher.find()) {
            return true;
        }
        return false;
    }

    /**
     * 设置下载底栏Icon
     *
     * @param rb
     * @param picPath
     * @param picSecondPath
     */
    private void setDownBitmap(RadioButton rb, String picPath, String picSecondPath) {
        Bitmap bitmap = BitmapFactory.decodeFile(picPath);
        Bitmap secBitmap = BitmapFactory.decodeFile(picSecondPath);
        StateListDrawable drawable = new StateListDrawable();
        drawable.addState(new int[]{android.R.attr.state_checked}, new BitmapDrawable(null, secBitmap));
        drawable.addState(new int[]{-android.R.attr.state_checked}, new BitmapDrawable(null, bitmap));
        drawable.setBounds(0, 0, AutoSizeUtils.mm2px(mAppContext, iconHeight / drawable.getMinimumHeight() * drawable.getMinimumWidth()), AutoSizeUtils.mm2px(mAppContext, iconHeight)); //设置边界
        rb.setCompoundDrawables(null, drawable, null, null);
    }

    /**
     * 初始化默认Icon
     */
    private void setNorMal() {
        for (int i = 0; i < rp_bottom_main.getChildCount(); i++) {
            RadioButton rb = (RadioButton) rp_bottom_main.getChildAt(i);
            rb.setTextColor(getResources().getColorStateList(R.color.sel_text_gray_light_blue));
            setNormalIcon(i, rb);
        }
    }

    /**
     * 设置正常Icon
     *
     * @param i
     * @param rb
     */
    private void setNormalIcon(int i, RadioButton rb) {
        switch (i) {
            case 0:
                setDefaultButtonData(i, rb, MAIN_HOME);
                Drawable drawable = getResources().getDrawable(R.drawable.selector_bottom_home_bar);
                drawable.setBounds(0, 0, AutoSizeUtils.mm2px(mAppContext, iconHeight / drawable.getMinimumHeight() * drawable.getMinimumWidth()), AutoSizeUtils.mm2px(mAppContext, iconHeight));//设置边界
                rb.setCompoundDrawables(null, drawable, null, null);
                break;
            case 1:
                setDefaultButtonData(i, rb, MAIN_QUALITY);
                drawable = getResources().getDrawable(R.drawable.selector_bottom_quality_bar);
                drawable.setBounds(0, 0, AutoSizeUtils.mm2px(mAppContext, iconHeight / drawable.getMinimumHeight() * drawable.getMinimumWidth()), AutoSizeUtils.mm2px(mAppContext, iconHeight));//设置边界
                rb.setCompoundDrawables(null, drawable, null, null);
                break;
            case 2:
                setDefaultButtonData(i, rb, MAIN_TIME);
                drawable = getResources().getDrawable(R.drawable.selector_bottom_time_bar);
                drawable.setBounds(0, 0, AutoSizeUtils.mm2px(mAppContext, iconHeight / drawable.getMinimumHeight() * drawable.getMinimumWidth()), AutoSizeUtils.mm2px(mAppContext, iconHeight));//设置边界
                rb.setCompoundDrawables(null, drawable, null, null);
                break;
            case 3:
                setDefaultButtonData(i, rb, MAIN_FIND);
                drawable = getResources().getDrawable(R.drawable.selector_bottom_find_bar);
                drawable.setBounds(0, 0, AutoSizeUtils.mm2px(mAppContext, iconHeight / drawable.getMinimumHeight() * drawable.getMinimumWidth()), AutoSizeUtils.mm2px(mAppContext, iconHeight));//设置边界
                rb.setCompoundDrawables(null, drawable, null, null);
                break;
            case 4:
                setDefaultButtonData(i, rb, MAIN_MINE);
                drawable = getResources().getDrawable(R.drawable.selector_bottom_mine_bar);
                drawable.setBounds(0, 0, AutoSizeUtils.mm2px(mAppContext, iconHeight / drawable.getMinimumHeight() * drawable.getMinimumWidth()), AutoSizeUtils.mm2px(mAppContext, iconHeight));//设置边界
                rb.setCompoundDrawables(null, drawable, null, null);
                break;
        }
    }

    private void setDefaultButtonData(int position, RadioButton rb, String mainHome) {
        MainIconBean mainIconBean = new MainIconBean(position, mainHome);
        rb.setTag(R.id.main_page, mainIconBean);
        iconDataList.add(mainIconBean);
    }

    /**
     * 获取最新账号信息 避免黑名单、异常账户登录
     *
     * @v3.2.0
     */
    private void getNetDataInfo() {
        final SavePersonalInfoBean personalInfo = getPersonalInfo(this);
        if (personalInfo.isLogin()) {
            userId = personalInfo.getUid();
            String url = Url.BASE_URL + Url.MINE_PAGE_POST;
            Map<String, Object> params = new HashMap<>();
            params.put("userid", personalInfo.getUid());
            if (!TextUtils.isEmpty(personalInfo.getOpenId())) {
                params.put("openid", getStrings(personalInfo.getOpenId()));
            }
            if (!TextUtils.isEmpty(personalInfo.getLoginType())) {
                params.put("type", getStrings(personalInfo.getLoginType()));
            }
            if (OTHER_WECHAT.equals(getStrings(personalInfo.getLoginType())) &&
                    !TextUtils.isEmpty(personalInfo.getUnionId())) {
                params.put("unionid", getStrings(personalInfo.getUnionId()));
            }
            NetLoadUtils.getNetInstance().loadNetDataPost(this, url, params, new NetLoadListenerHelper() {
                @Override
                public void onSuccess(String result) {
                    Gson gson = new Gson();
                    CommunalUserInfoEntity minePageData = gson.fromJson(result, CommunalUserInfoEntity.class);
                    if (minePageData != null) {
                        CommunalUserInfoBean communalUserInfoBean = minePageData.getCommunalUserInfoBean();
                        if (minePageData.getCode().equals(SUCCESS_CODE)) {
                            getLoginStatusTime();
//                            上传设备信息
                            setDeviceInfo(MainActivity.this, communalUserInfoBean.getApp_version_no()
                                    , communalUserInfoBean.getDevice_model()
                                    , communalUserInfoBean.getDevice_sys_version(), communalUserInfoBean.getSysNotice());
                            //        更新最新个人信息
                            SavePersonalInfoBean savePersonalInfo = new SavePersonalInfoBean();
                            savePersonalInfo.setAvatar(getStrings(communalUserInfoBean.getAvatar()));
                            savePersonalInfo.setNickName(getStrings(communalUserInfoBean.getNickname()));
                            savePersonalInfo.setPhoneNum(getStrings(communalUserInfoBean.getMobile()));
                            savePersonalInfo.setUid(communalUserInfoBean.getUid());
                            savePersonalInfo.setLogin(true);
                            savePersonalInfoCache(MainActivity.this, savePersonalInfo);
                            doExitAccount(communalUserInfoBean);
                        } else {
                            personalInfo.setLogin(false);
                            savePersonalInfoCache(MainActivity.this, personalInfo);
                        }
                    }
                }

                @Override
                public void onError(Throwable throwable) {
                    personalInfo.setLogin(false);
                    savePersonalInfoCache(MainActivity.this, personalInfo);
                }
            });
        } else {
            userId = 0;
            QyServiceUtils.getQyInstance().logoutQyUser(MainActivity.this);
        }
    }

    /**
     * 退出账号
     *
     * @param communalUserInfoBean
     */
    private void doExitAccount(CommunalUserInfoBean communalUserInfoBean) {
        if (communalUserInfoBean.getApprove() == 1) {
            SharedPreferences sharedPreferences = getSharedPreferences(APP_FIRST_TIMES, MODE_PRIVATE);
            int versionCode = sharedPreferences.getInt(APP_SAVE_VERSION, 0);
            int currentVersionCode = getCurrentVersionCode();
            if (versionCode < currentVersionCode) {
                Map<String, Object> params = new HashMap<>();
                params.put("uid", userId);
                NetLoadUtils.getNetInstance().loadNetDataPost(MainActivity.this, CHECK_CLEAR_USER_DATA, params, new NetLoadListenerHelper() {
                    @Override
                    public void onSuccess(String result) {
                        sharedPreferences.edit().putInt(APP_SAVE_VERSION, currentVersionCode).apply();
                        if (TextUtils.isEmpty(result)) {
                            return;
                        }
                        com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(result);
                        Boolean clean = jsonObject.getBoolean("clean");
                        if (clean) {
                            savePersonalInfoCache(MainActivity.this, null);
                        }
                    }
                });
            }
        } else {
            savePersonalInfoCache(MainActivity.this, null);
        }
    }

    /**
     * 获取当前版本code
     *
     * @return
     */
    private int getCurrentVersionCode() {
        try {
            // 获取packagemanager的实例
            PackageManager packageManager = getPackageManager();
            // getPackageName()是你当前类的包名，0代表是获取版本信息
            PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(), 0);
            return packInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 统计登陆时间
     */
    private void getLoginStatusTime() {
        String url = Url.BASE_URL + Url.H_LOGIN_LAST_TIME;
        Map<String, Object> params = new HashMap<>();
        params.put("uid", userId);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, url, params, null);
    }

    /**
     * 获取上传统计大小
     */
    private void getUpTotalSize() {
        String url = Url.BASE_URL + Url.TOTAL_UP_SIZE;
        NetLoadUtils.getNetInstance().loadNetDataPost(this, url, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                RequestStatus requestStatus = gson.fromJson(result, RequestStatus.class);
                if (requestStatus != null) {
                    if (requestStatus.getCode().equals(SUCCESS_CODE) && requestStatus.getResult() != null) {
                        UP_TOTAL_SIZE = requestStatus.getResult().getCollectSize() > 0 ? requestStatus.getResult().getCollectSize() : 50;
                    }
                }
            }
        });
    }

    private void getAddressVersion() {
        String url = Url.BASE_URL + Url.H_ADDRESS_VERSION;
        NetLoadUtils.getNetInstance().loadNetDataPost(this, url, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                RequestStatus requestStatus = gson.fromJson(result, RequestStatus.class);
                if (requestStatus != null) {
                    if (requestStatus.getCode().equals(SUCCESS_CODE)) {
                        SharedPreferences preferences = getSharedPreferences("addressConfig", MODE_PRIVATE);
                        String versionName = preferences.getString("version", "");
                        if (!versionName.equals(requestStatus.getVersion())) {
                            SharedPreferences.Editor edit = preferences.edit();
                            edit.putString("version", requestStatus.getVersion());
                            edit.apply();
                            getAddressData();
                        } else {
                            //        地址初始化
                            AddressUtils.getQyInstance().initAddress();
                        }
                    } else {
                        //        地址初始化
                        AddressUtils.getQyInstance().initAddress();
                    }
                } else {
                    //        地址初始化
                    AddressUtils.getQyInstance().initAddress();
                }
            }

            @Override
            public void onNotNetOrException() {
                //        地址初始化
                AddressUtils.getQyInstance().initAddress();
            }
        });
    }


    private void getAddressData() {
        String url = Url.BASE_URL + Url.H_ADDRESS_DATA;
        NetLoadUtils.getNetInstance().loadNetDataPost(this, url, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                String code = "";
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    code = (String) jsonObject.get("code");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (code.equals(SUCCESS_CODE)) {
                    //        地址初始化
                    AddressUtils.getQyInstance().initAddress(result);
                    FileStreamUtils.writeFileFromString(getFilesDir().getAbsolutePath() + "/adr_s/asr_s.txt", result, false);
                } else {
                    //        地址初始化
                    AddressUtils.getQyInstance().initAddress();
                }
            }

            @Override
            public void onNotNetOrException() {
                //        地址初始化
                AddressUtils.getQyInstance().initAddress();
            }
        });
    }

    private void getOSSConfig() {
        String url = Url.BASE_URL + Url.H_OSS_CONFIG;
        NetLoadUtils.getNetInstance().loadNetDataPost(this, url, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                OSSConfigEntity ossConfigEntity = gson.fromJson(result, OSSConfigEntity.class);
                if (ossConfigEntity != null) {
                    if (ossConfigEntity.getCode().equals(SUCCESS_CODE)) {
                        OSSConfigBean ossConfigBean = ossConfigEntity.getOssConfigBean();
                        SharedPreferences preferences = getSharedPreferences("ossConfig", MODE_PRIVATE);
                        SharedPreferences.Editor edit = preferences.edit();
                        edit.putString("endpoint", ossConfigBean.getEndpoint());
                        edit.putString("bucketName", ossConfigBean.getBucketname());
                        edit.putString("accessKeyId", ossConfigBean.getAccesskeyid());
                        edit.putString("accessKeySecret", ossConfigBean.getAccesskeysecret());
                        edit.putString("url", ossConfigBean.getUrl());
                        edit.apply();
                    }
                }
            }
        });
    }


    private void getSelectedDialog() {
        if (selectServer == null) {
            AlertSettingBean alertSettingBean = new AlertSettingBean();
            AlertSettingBean.AlertData alertData = new AlertSettingBean.AlertData();
            alertData.setCancelStr("取消");
            alertData.setNormalData(SERVER);
            alertData.setFirstDet(true);
            alertData.setTitle("选择服务器，设置成功后重启生效");
            alertSettingBean.setStyle(AlertView.Style.ActionSheet);
            alertSettingBean.setAlertData(alertData);
            selectServer = new AlertView(alertSettingBean, this, (o, position) -> {
                if (position != AlertView.CANCELPOSITION && SERVER[position].contains("UID")) {
                    //                    编辑框
                    selectServer.dismissImmediately();
                    if (mAlertViewExt == null) {
                        AlertSettingBean alertSettingBean1 = new AlertSettingBean();
                        AlertSettingBean.AlertData alertData1 = new AlertSettingBean.AlertData();
                        alertData1.setCancelStr("取消");
                        alertData1.setDetermineStr("完成");
                        alertData1.setFirstDet(true);
                        alertSettingBean1.setStyle(AlertView.Style.Alert);
                        alertSettingBean1.setAlertData(alertData1);
                        mAlertViewExt = new AlertView(alertSettingBean1, MainActivity.this, (o1, position1) -> {
                            if (o1 == mAlertViewExt) {
                                closeKeyboard();
                                if (position1 != AlertView.CANCELPOSITION) {
                                    String inputUid = etName.getText().toString().trim();
                                    SharedPreferences loginStatus = getSharedPreferences("loginStatus", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor edit = loginStatus.edit();
                                    edit.putBoolean("isLogin", true);
                                    edit.putInt("uid", Integer.parseInt(inputUid));
                                    edit.apply();
                                }
                            }
                        });
                        ViewGroup extView = (ViewGroup) LayoutInflater.from(MainActivity.this).inflate(R.layout.alertext_form, null);
                        etName = (EditText) extView.findViewById(R.id.etName);
                        etName.setHint("请输入用户Id");
                        mAlertViewExt.addExtView(extView);
                    }
                    mAlertViewExt.show();
                } else if (o.equals(selectServer) && position != AlertView.CANCELPOSITION) {
                    SharedPreferences sharedPreferences = getSharedPreferences("selectedServer", MODE_PRIVATE);
                    SharedPreferences.Editor edit = sharedPreferences.edit();
                    edit.putInt("selectServer", position);
                    edit.commit();
                    SharedPreferences loginStatus = getSharedPreferences("loginStatus", MODE_PRIVATE);
                    SharedPreferences.Editor loginEdit = loginStatus.edit();
                    loginEdit.putBoolean("isLogin", false);
                    loginEdit.commit();
                    RestartAPPTool.restartAPP(MainActivity.this);
                }
            });
            selectServer.setCancelable(true);
        }
        selectServer.show();
    }

    private void closeKeyboard() {
        //关闭软键盘
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etName.getWindowToken(), 0);
    }

    private void getADDialog() {
        NetLoadUtils.getNetInstance().loadNetDataPost(this, H_AD_DIALOG, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                CommunalADActivityEntity communalADActivityEntity = gson.fromJson(result, CommunalADActivityEntity.class);
                SharedPreferences sp = getSharedPreferences("ADDialog", MODE_PRIVATE);
                SharedPreferences.Editor edit = sp.edit();
                if (communalADActivityEntity != null && communalADActivityEntity.getCode().equals(SUCCESS_CODE)) {
                    List<CommunalADActivityBean> communalADActivityBeanList = communalADActivityEntity.getCommunalADActivityBeanList();
                    if (communalADActivityBeanList != null && communalADActivityBeanList.size() > 0) {
                        CommunalADActivityBean communalADActivityBean = communalADActivityBeanList.get(communalADActivityBeanList.size() - 1);
                        String saveTime = sp.getString("createTime", "0");
                        String lastShowTime = sp.getString("showTime", "0");
                        String newTime = communalADActivityBean.getCtime();
                        if (!saveTime.equals(newTime) || (communalADActivityBean.getFrequency_type() == 1 &&
                                isTimeDayEligibility(lastShowTime, communalADActivityEntity.getSystemTime(),
                                        communalADActivityBean.getInterval_day()))) {
                            edit.putString("showTime", communalADActivityEntity.getSystemTime());
                            showAdDialog(communalADActivityBean, saveTime, newTime);
                        }
                        if (!saveTime.equals(newTime)) {
                            edit.putString("createTime", newTime);
                        }
                        edit.apply();
                    } else {
                        edit.clear().apply();
                    }
                } else {
                    edit.clear().apply();
                }
            }
        });
    }

    private void showAdDialog(CommunalADActivityBean communalADActivityBean, String saveTime, String newTime) {
        GlideImageLoaderUtil.loadFinishImgDrawable(MainActivity.this, communalADActivityBean.getPicUrl(), new GlideImageLoaderUtil.ImageLoaderFinishListener() {
            @Override
            public void onSuccess(Bitmap bitmap) {
                if (alertDialogAdImage == null) {
                    alertDialogAdImage = new AlertDialogImage(MainActivity.this);
                }
                alertDialogAdImage.show();
                alertDialogAdImage.setAlertClickListener(new AlertDialogImage.AlertImageClickListener() {
                    @Override
                    public void imageClick() {
                        alertDialogAdImage.dismiss();
                        adDialogClickTotal(getActivity(), communalADActivityBean.getId());
                        setSkipPath(MainActivity.this, communalADActivityBean.getAndroidLink(), false);
                    }
                });
                alertDialogAdImage.setImage(bitmap);
            }

            @Override
            public void onError() {

            }
        });
    }

    //登陆成功返回消息
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case MINE_REQ_CODE:
                changeAdaptivePage(MAIN_MINE);
                break;
        }
        CallbackContext.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(getActivity()).onActivityResult(requestCode, resultCode, data);
    }

    private void initMainPage() {
        RadioButton radioButton = (RadioButton) rp_bottom_main.getChildAt(0);
        radioButton.setChecked(true);
        MainIconBean mainIconBean = (MainIconBean) radioButton.getTag(R.id.main_page);
        if (mainIconBean != null) {
            changePage(mainIconBean);
        } else {
            if (iconDataList.size() > 0) {
                changePage(iconDataList.get(0));
            } else {
                changePage(new MainIconBean(0, MAIN_HOME));
            }
        }
    }

    private Fragment lastFragment = null;

    /**
     * 适配 底部导航栏是否有此tag 如没有默认展示第一个
     *
     * @param tag
     */
    private void changeAdaptivePage(String tag) {
        RadioButton defaultButton = (RadioButton) rp_bottom_main.getChildAt(0);
        MainIconBean defaultIconBean = (MainIconBean) defaultButton.getTag(R.id.main_page);
        if (TextUtils.isEmpty(tag)) {
            changePage(defaultIconBean);
        }
        for (int i = 0; i < rp_bottom_main.getChildCount(); i++) {
            RadioButton radioButton = (RadioButton) rp_bottom_main.getChildAt(i);
            MainIconBean mainIconBean = (MainIconBean) radioButton.getTag(R.id.main_page);
            if (getStrings(tag).equals(getStrings(mainIconBean.getIconUrl()))) {
                changePage(mainIconBean);
                break;
            } else if (rp_bottom_main.getChildCount() - 1 == i) {
                changePage(defaultIconBean);
                skipMainPage(tag);
            }
        }
    }

    /**
     * 设置页面tag
     *
     * @param pageTag
     */
    private void skipMainPage(String pageTag) {
        if (!TextUtils.isEmpty(pageTag)) {
            Intent intent = new Intent(this, MainPageTabBarActivity.class);
            intent.putExtra("tabType", pageTag);
            startActivity(intent);
        }
    }

    private void changePage(@NonNull MainIconBean mainIconBean) {
        try {
            String tag = mainIconBean.getIconUrl();
            RadioButton radioButton = (RadioButton) rp_bottom_main.getChildAt(mainIconBean.getPosition() > rp_bottom_main.getChildCount() - 1 ?
                    rp_bottom_main.getChildCount() - 1 : mainIconBean.getPosition());
            radioButton.setChecked(true);
            fragment = fragmentManager.findFragmentByTag(tag + mainIconBean.getPosition());
            transaction = fragmentManager.beginTransaction();
            if (fragment != null && fragment.isAdded()) {
                if (lastFragment != null) {
                    transaction.hide(lastFragment).commitAllowingStateLoss();
                }
                transaction.show(fragment);
                lastFragment = fragment;
                fragment.onResume();
            } else {
                if (isWebUrl(tag)) {
                    params.put("paddingStatus", "true");
                    params.put("loadUrl", getStrings(mainIconBean.getIconUrl()));
                    fragment = BaseFragment.newInstance(AliBCFragment.class, params, null);
                } else {
                    switch (getStrings(tag)) {
                        case MAIN_HOME:
                            fragment = BaseFragment.newInstance(HomePageNewFragment.class, null, null);
                            break;
                        case MAIN_QUALITY:
                            fragment = BaseFragment.newInstance(QualityFragment.class, null, null);
                            break;
                        case MAIN_TIME:
                            fragment = BaseFragment.newInstance(TimeShowNewFragment.class, null, null);
                            break;
                        case MAIN_FIND:
                            fragment = BaseFragment.newInstance(HomePageFragment.class, null, null);
                            break;
                        case MAIN_MINE:
                            fragment = BaseFragment.newInstance(MineDataFragment.class, null, null);
                            break;
                        default:
                            fragment = BaseFragment.newInstance(HomePageNewFragment.class, null, null);
                            break;
                    }
                }
                if (lastFragment != null) {
                    if (fragment.isAdded()) {
                        transaction.hide(lastFragment).show(fragment).commitAllowingStateLoss();
                    } else {
                        transaction.hide(lastFragment).add(R.id.main_container, fragment, tag + mainIconBean.getPosition()).commitAllowingStateLoss();
                    }
                } else {
                    transaction.add(R.id.main_container, fragment, tag + mainIconBean.getPosition()).commitAllowingStateLoss();
                }
                lastFragment = fragment;
            }
        } catch (Exception e) {
            e.printStackTrace();
            RestartAPPTool.restartAPP(MainActivity.this);
        }
    }

    @Override
    protected void loadData() {
    }

    private Intent onHomeIntent; // home键退出后通过intent启动程序

    @Override
    protected void onNewIntent(Intent intent) {
// 拦截Intent，保存Intent，在onResume中进行处理
        onHomeIntent = intent;
        setIntent(intent);
        setIntentBottomIconData();
    }

    @Override
    public void onResume() {
        if (onHomeIntent != null) { // home键退出后通过intent启动程序
// dosomething···
            onHomeIntent = null;
        }
        super.onResume();
        registerMessageReceiver();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mMessageReceiver != null) {
            unregisterReceiver(mMessageReceiver);
        }
    }

    private MessageReceiver mMessageReceiver;
    public static final String MESSAGE_RECEIVED_ACTION = "JPUSH_MESSAGE_RECEIVED_ACTION";

    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        filter.addAction(INSTALL_APP_PROGRESS);
        registerReceiver(mMessageReceiver, filter);
    }

    @Override
    public void onClick(View v) {
        MainIconBean mainIconBean = (MainIconBean) v.getTag(R.id.main_page);
        if (mainIconBean != null && !isChecked) {
            changePage(mainIconBean);
        }
        isChecked = false;
    }

    /**
     * 获取统计字段 上传大小
     */
    public void getAppUpdateJson() {
        String url = Url.BASE_URL + Url.APP_TOTAL_ACTION;
        NetLoadUtils.getNetInstance().loadNetDataPost(this, url, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                TotalActionEntity totalActionEntity = TotalActionEntity.objectFromData(result);
                List<TotalActionEntity.TotalActionBean> totalActionList = totalActionEntity.getTotalActionList();
                File trajectoryFile = createFiles(new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "trajectory"));
                File file = new File(trajectoryFile.getAbsolutePath() + "/" + "total" + ".txt");
                for (TotalActionEntity.TotalActionBean totalActionBean : totalActionList) {
                    FileStreamUtils.writeFileFromString(file.getAbsolutePath(), "," + totalActionBean.getName() + "," + totalActionBean.getId() + "\n", true);
                }
            }
        });
    }

    private File createFiles(File file) {
        if (!file.exists()) {
            file.mkdir();
        }
        return file;
    }

    public class MessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
//                收到消息
                EventBus.getDefault().post(new EventMessage(REFRESH_MESSAGE_TOTAL, REFRESH_MESSAGE_TOTAL));
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (constantMethod != null) {
            constantMethod.releaseHandlers();
        }
        if (alertDialogHelper != null && alertDialogHelper.isShowing()) {
            alertDialogHelper.dismiss();
        }
        if (alertDialogAdImage != null && alertDialogAdImage.isShowing()) {
            alertDialogAdImage.dismiss();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (fragment instanceof AliBCFragment) {
                boolean isGoBack = ((AliBCFragment) fragment).goBack();
                if (!isGoBack) {
                    goBack();
                }
            } else {
                goBack();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void goBack() {
        SaveUpdateImportDateUtils.getUpdateDataUtilsInstance().getMainIconData(this);
        SaveUpdateImportDateUtils.getUpdateDataUtilsInstance().getLaunchBanner(this);
        if (alertDialogHelper == null) {
            alertDialogHelper = new AlertDialogHelper(MainActivity.this);
            alertDialogHelper.setTitleVisibility(View.GONE).setMsgTextGravity(Gravity.CENTER)
                    .setMsg("确定要退出当前程序").setCancelText("取消").setConfirmText("确定")
                    .setCancelTextColor(getResources().getColor(R.color.text_login_gray_s));
            alertDialogHelper.setAlertListener(new AlertDialogHelper.AlertConfirmCancelListener() {
                @Override
                public void confirm() {
                    TinkerBaseApplicationLike applicationLike = (TinkerBaseApplicationLike) TinkerManager.getTinkerApplicationLike();
                    applicationLike.exitApp();
                    System.exit(0);
                }

                @Override
                public void cancel() {
                }
            });
        }
        alertDialogHelper.show();
    }

    @Override
    public void onBackPressed() {
        if (!isFinishing()) {
            super.onBackPressed();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                InputMethodManager imm =
                        (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    // Return whether touch the view.
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            return !(event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom);
        }
        return false;
    }
}
