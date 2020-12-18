package com.amkj.dmsh;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.ali.auth.third.ui.context.CallbackContext;
import com.alibaba.fastjson.JSON;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.BaseEntity;
import com.amkj.dmsh.base.TinkerBaseApplicationLike;
import com.amkj.dmsh.bean.CommunalUserInfoEntity;
import com.amkj.dmsh.bean.CommunalUserInfoEntity.CommunalUserInfoBean;
import com.amkj.dmsh.bean.CouponEntity;
import com.amkj.dmsh.bean.MainNavEntity;
import com.amkj.dmsh.bean.MainNavEntity.MainNavBean;
import com.amkj.dmsh.bean.OSSConfigEntity;
import com.amkj.dmsh.bean.OSSConfigEntity.OSSConfigBean;
import com.amkj.dmsh.bean.PushInfoEntity;
import com.amkj.dmsh.bean.RequestStatus;
import com.amkj.dmsh.bean.SysNotificationEntity;
import com.amkj.dmsh.catergory.fragment.QualityFragment;
import com.amkj.dmsh.constant.AppUpdateUtils;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.ConstantVariable;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.dao.AddClickDao;
import com.amkj.dmsh.dominant.activity.QualityNewUserActivity;
import com.amkj.dmsh.find.fragment.FindFragment;
import com.amkj.dmsh.homepage.bean.CommunalADActivityEntity;
import com.amkj.dmsh.homepage.bean.CommunalADActivityEntity.CommunalADActivityBean;
import com.amkj.dmsh.homepage.fragment.AliBCFragment;
import com.amkj.dmsh.homepage.fragment.HomePageFragment;
import com.amkj.dmsh.mine.bean.SavePersonalInfoBean;
import com.amkj.dmsh.mine.fragment.MineDataFragment;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.qyservice.QyServiceUtils;
import com.amkj.dmsh.shopdetails.activity.DirectMyCouponActivity;
import com.amkj.dmsh.time.fragment.TimeShowNewFragment;
import com.amkj.dmsh.utils.AddressUtils;
import com.amkj.dmsh.utils.CountDownTimer;
import com.amkj.dmsh.utils.FileStreamUtils;
import com.amkj.dmsh.utils.SaveUpdateImportDateUtils;
import com.amkj.dmsh.utils.SelectorUtil;
import com.amkj.dmsh.utils.SharedPreUtils;
import com.amkj.dmsh.utils.TimeUtils;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.amkj.dmsh.utils.gson.GsonUtils;
import com.amkj.dmsh.utils.restartapputils.RestartAPPTool;
import com.amkj.dmsh.utils.webformatdata.CommunalWebDetailUtils;
import com.amkj.dmsh.views.alertdialog.AlertDialogBottomListHelper;
import com.amkj.dmsh.views.alertdialog.AlertDialogEdit;
import com.amkj.dmsh.views.alertdialog.AlertDialogGroup;
import com.amkj.dmsh.views.alertdialog.AlertDialogHelper;
import com.amkj.dmsh.views.alertdialog.AlertDialogImage;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
import com.tencent.bugly.beta.tinker.TinkerManager;
import com.umeng.socialize.UMShareAPI;

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.NavGraphNavigator;
import androidx.navigation.NavigatorProvider;
import androidx.navigation.fragment.FragmentNavigator;
import androidx.navigation.fragment.NavHostFragment;
import butterknife.BindView;
import me.jessyan.autosize.utils.AutoSizeUtils;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.CommunalSavePutValueVariable.APP_FIRST_TIMES;
import static com.amkj.dmsh.constant.CommunalSavePutValueVariable.APP_SAVE_VERSION;
import static com.amkj.dmsh.constant.ConstantMethod.getDeviceAppNotificationStatus;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.isContextExisted;
import static com.amkj.dmsh.constant.ConstantMethod.isWebLinkUrl;
import static com.amkj.dmsh.constant.ConstantMethod.setDeviceInfo;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.COUPON_POPUP;
import static com.amkj.dmsh.constant.ConstantVariable.FORCE_UPDATE;
import static com.amkj.dmsh.constant.ConstantVariable.GET_FIRST_INSTALL_INFO;
import static com.amkj.dmsh.constant.ConstantVariable.GP_REMIND;
import static com.amkj.dmsh.constant.ConstantVariable.IS_NEW_USER;
import static com.amkj.dmsh.constant.ConstantVariable.MAIN_FIND;
import static com.amkj.dmsh.constant.ConstantVariable.MAIN_HOME;
import static com.amkj.dmsh.constant.ConstantVariable.MAIN_MINE;
import static com.amkj.dmsh.constant.ConstantVariable.MAIN_QUALITY;
import static com.amkj.dmsh.constant.ConstantVariable.MAIN_TIME;
import static com.amkj.dmsh.constant.ConstantVariable.MARKING_POPUP;
import static com.amkj.dmsh.constant.ConstantVariable.NOT_FORCE_UPDATE;
import static com.amkj.dmsh.constant.ConstantVariable.OTHER_WECHAT;
import static com.amkj.dmsh.constant.ConstantVariable.PUSH_OPEN_REMIND;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.TOKEN_EXPIRE_TIME;
import static com.amkj.dmsh.constant.ConstantVariable.TOKEN_REFRESH_TIME;
import static com.amkj.dmsh.constant.ConstantVariable.isDebugTag;
import static com.amkj.dmsh.constant.ConstantVariable.isShowTint;
import static com.amkj.dmsh.constant.Url.APP_SYS_NOTIFICATION;
import static com.amkj.dmsh.constant.Url.CHECK_CLEAR_USER_DATA;
import static com.amkj.dmsh.constant.Url.GET_UNIFIED_POPUP;
import static com.amkj.dmsh.constant.Url.GROUP_GET_GP_POPUP;
import static com.amkj.dmsh.constant.Url.H_AD_DIALOG;
import static com.amkj.dmsh.dao.UserDao.getPersonalInfo;
import static com.amkj.dmsh.dao.UserDao.savePersonalInfoCache;
import static com.amkj.dmsh.utils.TimeUtils.getDateFormat;
import static com.amkj.dmsh.utils.TimeUtils.getTimeDifference;
import static com.amkj.dmsh.utils.TimeUtils.isEndOrStartTime;
import static com.amkj.dmsh.utils.TimeUtils.isSameTimeDay;


/**
 * @author Liuguipeng
 * @date 2017/10/31
 */
public class MainActivity extends BaseActivity {
    @BindView(R.id.rp_bottom_main)
    RadioGroup radio_group;
    public static final int MINE_REQ_CODE = 13;
    private String[] SERVER = {"正式库", "测试库", "招立", "泽鑫", "泽鑫9090", "预发布", "王凯2", "王凯1", "鸿星", "修改BaseUrl"};
    public static final String ImgKey = "ImgPath";
    public static final String TimeKey = "ShowSeconds";
    public static final String SkipUrlKey = "SkipUrl";
    public static final String LauncherAdIdKey = "AD_ID";
    public static final String InvokeTimeFileName = "INVOKE_TIME_FILENAME";
    public static String OriginalImgUrl = "OriginalImgUrl";

    private Map<String, String> pushMap;
    private AlertDialogImage alertDialogAdImage;
    private AlertDialogHelper alertDialogHelper;
    //    底部限制98 padding top&bottom 10*2 drawablepadding 10 textsize 20
    private float iconHeight = 32f;
    private AlertDialogGroup mAlertDialogGroup;
    private AlertDialogHelper mAlertDialogNotify;
    private AlertDialogImage mAlertDialogUserImage;
    private AlertDialogBottomListHelper mAlertDialogServer;
    private AlertDialogEdit mAlertDialogEdit;
    private CountDownTimer mCountDownTimer;
    private NavController navController;
    private String[] links = {MAIN_HOME, MAIN_QUALITY, MAIN_TIME, MAIN_FIND, MAIN_MINE};
    private int[] resIds = {R.id.home, R.id.quality, R.id.time, R.id.find, R.id.mine};
    private int[] drawables = {R.drawable.selector_bottom_home_bar, R.drawable.selector_bottom_catergory_bar,
            R.drawable.selector_bottom_time_bar, R.drawable.selector_bottom_find_bar, R.drawable.selector_bottom_mine_bar};
    private int webNum;//Web链接数量

    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViews() {
        if (isDebugTag) {
            getSelectedDialog();
        }
        //设置默认配置
        setNavDataDefault();
        //获取本地配置,如果有值覆盖默认配置
        setNavDataLocal();
        //获取页面容器NavHostFragment
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        if (fragment == null) return;
        //获取导航控制器
        navController = NavHostFragment.findNavController(fragment);
        //创建自定义的Fragment导航器
        FixFragmentNavigator fragmentNavigator = new FixFragmentNavigator(this, fragment.getChildFragmentManager(), fragment.getId());
        //获取导航器提供者
        NavigatorProvider provider = navController.getNavigatorProvider();
        //把自定义的Fragment导航器添加进去
        provider.addNavigator(fragmentNavigator);
        //手动创建导航图
        NavGraph navGraph = initNavGraph(provider, fragmentNavigator);
        //设置导航图(添加起始导航参数)
        Bundle startDestinationArgs = new Bundle();
        if (isWebLinkUrl(links[0])) {
            startDestinationArgs.putString("loadUrl", links[0]);
            startDestinationArgs.putString("paddingStatus", "true");
        }
        navController.setGraph(navGraph, startDestinationArgs);
        //底部导航设置监听
        radio_group.setOnCheckedChangeListener((group, checkedId) -> {
            //按钮切换时导航到对应的目的地
            Bundle bundle = new Bundle();
            for (int i = 0; i < radio_group.getChildCount(); i++) {
                if (radio_group.getChildAt(i).getId() == checkedId) {
                    //导航时添加参数（创建目的地时无法添加参数）
                    if (isWebLinkUrl(links[i])) {
                        bundle.putString("loadUrl", links[i]);
                        bundle.putString("paddingStatus", "true");
                    }
                    navController.navigate(resIds[i], bundle);
                    break;
                }
            }
        });
    }


    //初始化导航图
    private NavGraph initNavGraph(NavigatorProvider provider, FixFragmentNavigator fragmentNavigator) {
        NavGraph navGraph = new NavGraph(new NavGraphNavigator(provider));
        for (int i = 0; i < links.length; i++) {
            //导航器添加目的地
            FragmentNavigator.Destination destination = fragmentNavigator.createDestination();
            destination.setId(resIds[i]);
            destination.setClassName(getFragmentName(links[i]));//绑定fragment
            destination.setLabel(String.valueOf(i));//lable必须唯一
            navGraph.addDestination(destination);
        }
        navGraph.setStartDestination(resIds[0]);
        return navGraph;
    }


    @Override
    protected void loadData() {
        // 七鱼客服登录 获取用户信息 登进登出……
        getNetDataInfo();
        // 刷新token
        flushToken();
        // 启动广告
        SaveUpdateImportDateUtils.getUpdateDataUtilsInstance().getLaunchBanner(this);
        // 加载OSS配置  (备注：做统计使用，保留该接口调用)
        getOSSConfig();
        // 获取地址版本
        getAddressVersion();
        // 获取图标更新
        SaveUpdateImportDateUtils.getUpdateDataUtilsInstance().getMainIconData(this, 3);
        // 获取push信息
        getFirstPushInfo();
        // 设置分享提示
        setShareTint();
        // 统计首次安装设备信息
        getFirstInstallInfo();
        //获取要显示的弹窗
        getUnifiedPopup();
    }

    //获取未读的平台客服通知
    private void getUnreadCustomerMsg() {
        if (userId > 0) {
            NetLoadUtils.getNetInstance().loadNetDataPost(this, Url.GET_UNREAD_CUSTOMER_MSG, new NetLoadListenerHelper() {
                @Override
                public void onSuccess(String result) {
                    RequestStatus requestStatus = RequestStatus.objectFromData(result);
                    if (requestStatus != null && SUCCESS_CODE.equals(requestStatus.getCode()) && !TextUtils.isEmpty(requestStatus.getContent())) {
                        newQyMessageComming(MsgTypeEnum.text, getTimeDifference(requestStatus.getCurrentTime(),
                                requestStatus.getTime()), requestStatus.getContent(), requestStatus.getLink());
                    }
                }
            });
        }
    }

    //统一弹窗规则接口
    private void getUnifiedPopup() {
        Map<String, Object> allTime = (Map<String, Object>) getSharedPreferences(InvokeTimeFileName, Context.MODE_PRIVATE).getAll();
        if (!TextUtils.isEmpty((String) allTime.get(MARKING_POPUP))) {
            Map markingTime = GsonUtils.fromJson((String) allTime.get(MARKING_POPUP), Map.class);
            allTime.put(MARKING_POPUP, markingTime);
        }

        Map<String, Object> map = new HashMap<>();
        map.put("lastShowTimeMap", GsonUtils.toJson(allTime));
        NetLoadUtils.getNetInstance().loadNetDataPost(this, GET_UNIFIED_POPUP, map, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                RequestStatus requestStatus = GsonUtils.fromJson(result, RequestStatus.class);
                if (requestStatus != null && SUCCESS_CODE.equals(requestStatus.getCode())) {
                    switch (requestStatus.getNo()) {
                        //强制更新
                        case FORCE_UPDATE:
                            SharedPreUtils.setParam(InvokeTimeFileName, FORCE_UPDATE, TimeUtils.getCurrentTime(requestStatus));//记录调用时间
                            AppUpdateUtils.getInstance().getAppUpdate(getActivity());
                            break;
                        //新人优惠券弹窗
                        case COUPON_POPUP:
                            getNewUserCouponDialog(getActivity());
                            break;
                        //拼团未完成
                        case GP_REMIND:
                            getGpPopup();
                            break;
                        //非强制更新
                        case NOT_FORCE_UPDATE:
                            SharedPreUtils.setParam(InvokeTimeFileName, NOT_FORCE_UPDATE, TimeUtils.getCurrentTime(requestStatus));//记录调用时间
                            AppUpdateUtils.getInstance().getAppUpdate(getActivity());
                            break;
                        //推送通知打开提醒
                        case PUSH_OPEN_REMIND:
                            checkPushPermission(requestStatus);
                            break;
                        //营销弹窗
                        case MARKING_POPUP:
                            getMarkingPopup(requestStatus.getTargetId());
                            break;
                        default:
                            getUnreadCustomerMsg();
                            break;
                    }
                } else {
                    //获取未读的平台客服通知
                    getUnreadCustomerMsg();
                }
            }
        });
    }

    /**
     * 获取拼团弹窗
     */
    private void getGpPopup() {
        NetLoadUtils.getNetInstance().loadNetDataPost(this, GROUP_GET_GP_POPUP, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                RequestStatus requestStatus = GsonUtils.fromJson(result, RequestStatus.class);
                if (requestStatus != null && SUCCESS_CODE.equals(requestStatus.getCode()) && ConstantMethod.getStringChangeIntegers(requestStatus.getGpRecordId()) > 0) {
                    SharedPreUtils.setParam(InvokeTimeFileName, GP_REMIND, TimeUtils.getCurrentTime(requestStatus));//记录调用时间
                    GlideImageLoaderUtil.setLoadImgFinishListener(getActivity(), requestStatus.getCoverImage(), new GlideImageLoaderUtil.ImageLoaderFinishListener() {
                        @Override
                        public void onSuccess(Bitmap bitmap) {
                            showAlertDialogGroup(requestStatus);
                        }

                        @Override
                        public void onError() {
                            showAlertDialogGroup(requestStatus);
                        }
                    });
                }
            }
        });
    }

    public void getNewUserCouponDialog(Activity context) {
        Map<String, Object> params = new HashMap<>();
        params.put("user_id", userId);
        NetLoadUtils.getNetInstance().loadNetDataPost(context, Url.H_NEW_USER_COUPON, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                RequestStatus requestStatus = RequestStatus.objectFromData(result);
                if (requestStatus != null && requestStatus.getCode().equals(SUCCESS_CODE) && !TextUtils.isEmpty(requestStatus.getImgUrl())
                        && 0 < requestStatus.getUserType() && requestStatus.getUserType() < 4) {
                    SharedPreUtils.setParam(InvokeTimeFileName, COUPON_POPUP, TimeUtils.getCurrentTime(requestStatus));//记录调用时间
                    if (isContextExisted(context)) {
                        GlideImageLoaderUtil.setLoadImgFinishListener(context, requestStatus.getImgUrl(), new GlideImageLoaderUtil.ImageLoaderFinishListener() {
                            @Override
                            public void onSuccess(Bitmap bitmap) {
                                mAlertDialogUserImage = new AlertDialogImage(context);
                                mAlertDialogUserImage.show();
                                mAlertDialogUserImage.setAlertClickListener(() -> {
                                    Intent intent = new Intent();
                                    switch (requestStatus.getUserType()) {
                                        //新人用户
                                        case 1:
                                            intent.setClass(context, QualityNewUserActivity.class);
                                            context.startActivity(intent);
                                            break;
                                        //领取优惠券
                                        case 2:
                                        case 3:
                                            CommunalWebDetailUtils.getCommunalWebInstance().getDirectCoupon(context,
                                                    requestStatus.getCouponId(), null, new CommunalWebDetailUtils.GetCouponListener() {
                                                        @Override
                                                        public void onSuccess(CouponEntity.CouponListEntity couponListEntity) {
                                                            Intent intent = new Intent(context, DirectMyCouponActivity.class);
                                                            context.startActivity(intent);
                                                        }

                                                        @Override
                                                        public void onFailure(CouponEntity.CouponListEntity couponListEntity) {

                                                        }
                                                    });
                                            break;
                                    }
                                });
                                mAlertDialogUserImage.setImage(bitmap);
                            }

                            @Override
                            public void onError() {

                            }
                        });
                    }
                }
            }
        });
    }

    private void showAlertDialogGroup(RequestStatus requestStatus) {
        if (mAlertDialogGroup == null) {
            mAlertDialogGroup = new AlertDialogGroup(getActivity());
        }
        mAlertDialogGroup.update(requestStatus);
        mAlertDialogGroup.show();
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
                    CommunalUserInfoBean tokenExpireBean = GsonUtils.fromJson(result, CommunalUserInfoBean.class);
                    //刷新本地token过期时间
                    SharedPreUtils.setParam(TOKEN_EXPIRE_TIME, System.currentTimeMillis() + tokenExpireBean.getTokenExpireSeconds());
                }
            });
            //记录刷新时间
            SharedPreUtils.setParam(TOKEN_REFRESH_TIME, System.currentTimeMillis());
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
            String url = Url.FIRST_PUSH_INFO;
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
                                pushMap = GsonUtils.fromJson(pushInfoMap, new TypeToken<Map<String, String>>() {
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
        if (mCountDownTimer == null) {
            mCountDownTimer = new CountDownTimer(getActivity()) {
                @Override
                public void onTick(long millisUntilFinished) {
                }

                @Override
                public void onFinish() {
                    getReceivePushInfo(pushInfoEntity, sharedPreferences);
                }
            };
        }
        mCountDownTimer.setMillisInFuture(pushInfoEntity.getAppPushInfo().getPushSecond() * 1000);
        mCountDownTimer.start();
    }

    private void getReceivePushInfo(PushInfoEntity pushInfoEntity, SharedPreferences sharedPreferences) {
        String url = Url.FIRST_PUSH_INFO_RECEIVE;
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("pushId", pushInfoEntity.getAppPushInfo().getId());
        NetLoadUtils.getNetInstance().loadNetDataPost(this, url, params, null);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        if (pushMap == null) {
            pushMap = new HashMap<>();
        }
        pushMap.put(String.valueOf(userId), pushInfoEntity.getCurrentTime());
        edit.putString("pushInfoMap", GsonUtils.toJson(pushMap));
        edit.apply();
    }

    /**
     * 获取最新账号信息 避免黑名单、异常账户登录
     *
     * @v3.2.0
     */
    private void getNetDataInfo() {
        final SavePersonalInfoBean personalInfo = getPersonalInfo(this);
        if (personalInfo.isLogin()) {
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
            NetLoadUtils.getNetInstance().loadNetDataPost(this, Url.MINE_PAGE, params, new NetLoadListenerHelper() {
                @Override
                public void onSuccess(String result) {
                    CommunalUserInfoEntity minePageData = GsonUtils.fromJson(result, CommunalUserInfoEntity.class);
                    if (minePageData != null) {
                        CommunalUserInfoBean communalUserInfoBean = minePageData.getCommunalUserInfoBean();
                        if (minePageData.getCode().equals(SUCCESS_CODE)) {
                            getLoginStatusTime();
                            //上传设备信息
                            setDeviceInfo(MainActivity.this, communalUserInfoBean.getApp_version_no()
                                    , communalUserInfoBean.getDevice_model()
                                    , communalUserInfoBean.getDevice_sys_version(), communalUserInfoBean.getSysNotice());
                            //更新最新个人信息
                            SavePersonalInfoBean savePersonalInfo = new SavePersonalInfoBean();
                            savePersonalInfo.setAvatar(getStrings(communalUserInfoBean.getAvatar()));
                            savePersonalInfo.setNickName(getStrings(communalUserInfoBean.getNickname()));
                            savePersonalInfo.setPhoneNum(getStrings(communalUserInfoBean.getMobile()));
                            savePersonalInfo.setUid(communalUserInfoBean.getUid());
                            savePersonalInfo.setVip(communalUserInfoBean.isVip());
                            savePersonalInfo.setVipLevel(communalUserInfoBean.getVipLevel());
                            savePersonalInfo.setIsWhiteUser(communalUserInfoBean.isWhiteUser());
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
        String url = Url.H_LOGIN_LAST_TIME;
        Map<String, Object> params = new HashMap<>();
        params.put("uid", userId);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, url, params, null);
    }

    private void getAddressVersion() {
        //地址初始化
        AddressUtils.getQyInstance().initAddress();
        //获取最新的地址库
        NetLoadUtils.getNetInstance().loadNetDataPost(this, Url.H_ADDRESS_VERSION, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                RequestStatus requestStatus = GsonUtils.fromJson(result, RequestStatus.class);
                //如果有新版本，覆盖当前版本
                if (requestStatus != null && SUCCESS_CODE.equals(requestStatus.getCode())) {
                    SharedPreferences preferences = getSharedPreferences("addressConfig", MODE_PRIVATE);
                    String versionName = preferences.getString("version", "");
                    if (!requestStatus.getVersion().equals(versionName)) {
                        SharedPreferences.Editor edit = preferences.edit();
                        edit.putString("version", requestStatus.getVersion());
                        edit.apply();
                        getAddressData();
                    }
                }
            }
        });
    }


    private void getAddressData() {
        NetLoadUtils.getNetInstance().loadNetDataPost(this, Url.H_ADDRESS_DATA, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                BaseEntity baseEntity = GsonUtils.fromJson(result, BaseEntity.class);
                if (baseEntity != null && SUCCESS_CODE.equals(baseEntity.getCode())) {
                    //地址初始化
                    AddressUtils.getQyInstance().initAddress(result);
                    //地址信息写入本地保存
                    FileStreamUtils.writeFileFromString(getFilesDir().getAbsolutePath() + "/adr_s/asr_s.txt", result, false);
                }
            }
        });
    }

    private void getOSSConfig() {
        String url = Url.H_OSS_CONFIG;
        NetLoadUtils.getNetInstance().loadNetDataPost(this, url, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                OSSConfigEntity ossConfigEntity = GsonUtils.fromJson(result, OSSConfigEntity.class);
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


    //BaseUrl选择框
    private void getSelectedDialog() {
        if (mAlertDialogServer == null) {
            mAlertDialogServer = new AlertDialogBottomListHelper(getActivity());
            mAlertDialogServer.setMsgVisible(View.GONE)
                    .setTitle(Url.BASE_URL)
                    .setItemData(SERVER).itemNotifyDataChange()
                    .setAlertListener((text, position) -> {
                        if ("修改BaseUrl".equals(text)) {
                            if (mAlertDialogEdit == null) {
                                mAlertDialogEdit = new AlertDialogEdit(getActivity());
                                mAlertDialogEdit.setCancleVisible(View.VISIBLE)
                                        .setEditHint(text)
                                        .setEditText("http://192.168.2.98:8080/")
                                        .setOnAlertListener(this::changeBaseUrl);
                            }

                            mAlertDialogEdit.show();
                        } else {
                            changeBaseUrl(Url.getUrl(position));
                        }
                    });
        }
        mAlertDialogServer.show();
    }

    //切换BaseUrl
    private void changeBaseUrl(String baseUrl) {
        SharedPreUtils.setParam("selectedServer", "selectServerUrl", baseUrl);
        SharedPreUtils.setParam("isLogin", false);
        RestartAPPTool.restartAPP(MainActivity.this);
    }

    //获取营销弹窗
    private void getMarkingPopup(int targetId) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", targetId);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, H_AD_DIALOG, map, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                CommunalADActivityEntity communalADActivityEntity = GsonUtils.fromJson(result, CommunalADActivityEntity.class);
                if (communalADActivityEntity != null && communalADActivityEntity.getCode().equals(SUCCESS_CODE)) {
                    List<CommunalADActivityBean> communalADActivityBeanList = communalADActivityEntity.getCommunalADActivityBeanList();
                    if (communalADActivityBeanList != null && communalADActivityBeanList.size() > 0) {
                        CommunalADActivityBean communalADActivityBean = communalADActivityBeanList.get(communalADActivityBeanList.size() - 1);
                        if (communalADActivityBean != null) {
                            try {
                                String json = (String) SharedPreUtils.getParam(InvokeTimeFileName, MARKING_POPUP, "");
                                Map map = !TextUtils.isEmpty(json) ? GsonUtils.fromJson(json, Map.class) : new HashMap();
                                if (map.size() > 20) {//防止缓存数据过多，每20条清一次
                                    map.clear();
                                }
                                map.put(String.valueOf(communalADActivityBean.getId()), TimeUtils.getCurrentTime(communalADActivityEntity));
                                SharedPreUtils.setParam(InvokeTimeFileName, MARKING_POPUP, GsonUtils.toJson(map));
                            } catch (JsonSyntaxException e) {
                                e.printStackTrace();
                            }
                            showAdDialog(communalADActivityBean);
                        }
                    }
                }
            }
        });
    }

    private void showAdDialog(CommunalADActivityBean communalADActivityBean) {
        GlideImageLoaderUtil.setLoadImgFinishListener(MainActivity.this, communalADActivityBean.getPicUrl(), new GlideImageLoaderUtil.ImageLoaderFinishListener() {
            @Override
            public void onSuccess(Bitmap bitmap) {
                if (alertDialogAdImage == null) {
                    alertDialogAdImage = new AlertDialogImage(MainActivity.this);
                }
                alertDialogAdImage.show();
                alertDialogAdImage.setAlertClickListener(() -> {
                    AddClickDao.addMarketClick(getActivity(), communalADActivityBean.getAndroidLink(), communalADActivityBean.getId());
                });
                alertDialogAdImage.setImage(bitmap);
            }

            @Override
            public void onError() {

            }
        });
    }

    //检查推送权限
    public void checkPushPermission(RequestStatus requestStatus) {
        SharedPreUtils.setParam(InvokeTimeFileName, PUSH_OPEN_REMIND, TimeUtils.getCurrentTime(requestStatus));//记录调用时间
        if (isContextExisted(getActivity()) && !getDeviceAppNotificationStatus()) {
            NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(), APP_SYS_NOTIFICATION, new NetLoadListenerHelper() {
                @Override
                public void onSuccess(String result) {
                    SysNotificationEntity sysNotificationEntity = GsonUtils.fromJson(result, SysNotificationEntity.class);
                    if (sysNotificationEntity != null && sysNotificationEntity.getSysNotificationBean() != null &&
                            SUCCESS_CODE.equals(sysNotificationEntity.getCode())) {
                        SysNotificationEntity.SysNotificationBean sysNotificationBean = sysNotificationEntity.getSysNotificationBean();
                        mAlertDialogNotify = new AlertDialogHelper(getActivity());
                        mAlertDialogNotify.setAlertListener(new AlertDialogHelper.AlertConfirmCancelListener() {
                            @Override
                            public void confirm() {
                                // 根据isOpened结果，判断是否需要提醒用户跳转AppInfo页面，去打开App通知权限
                                Intent intent = new Intent();
                                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                                intent.setData(uri);
                                getActivity().startActivity(intent);
                            }

                            @Override
                            public void cancel() {
                            }
                        });
                        mAlertDialogNotify.setTitle("通知提示")
                                .setMsg(TextUtils.isEmpty(sysNotificationBean.getContent()) ? "“多么生活”想给您发送通知,方便我们更好的为您服务，限时秒杀不再错过。" :
                                        sysNotificationBean.getContent())
                                .setSingleButton(true)
                                .setConfirmText("去设置");
                        mAlertDialogNotify.show();
                    }
                }
            });
        }
    }

    private void navigate() {
        Intent intent = getIntent();
        String type = intent.getStringExtra("type");
        if (!TextUtils.isEmpty(type) && Arrays.asList(links).contains(type)) {
            //兼容多个web地址的情况
            for (int i = 0; i < links.length; i++) {
                if (type.equals(links[i])) {
                    radio_group.check(radio_group.getChildAt(i).getId());
                    break;
                }
            }
        }
    }


    private int getResId(String link) {
        if (isWebLinkUrl(link)) {
            //兼容多个web地址的情况,避免指向同一个目的地
            webNum++;
            switch (webNum) {
                case 2:
                    return R.id.web2;
                case 3:
                    return R.id.web3;
                case 4:
                    return R.id.web4;
                case 5:
                    return R.id.web5;
                default:
                    return R.id.web;
            }
        } else if (MAIN_QUALITY.equals(link)) {
            return R.id.quality;
        } else if (MAIN_TIME.equals(link)) {
            return R.id.time;
        } else if (MAIN_FIND.equals(link)) {
            return R.id.find;
        } else if (MAIN_MINE.equals(link)) {
            return R.id.mine;
        } else {
            return R.id.home;
        }
    }

    private String getFragmentName(String link) {
        if (isWebLinkUrl(link)) {
            return AliBCFragment.class.getName();
        } else if (MAIN_QUALITY.equals(link)) {
            return QualityFragment.class.getName();
        } else if (MAIN_TIME.equals(link)) {
            return TimeShowNewFragment.class.getName();
        } else if (MAIN_FIND.equals(link)) {
            return FindFragment.class.getName();
        } else if (MAIN_MINE.equals(link)) {
            return MineDataFragment.class.getName();
        } else {
            return HomePageFragment.class.getName();
        }
    }


    private void setNavDataDefault() {
        //适配图标大小
        for (int i = 0; i < radio_group.getChildCount(); i++) {
            RadioButton radioButton = ((RadioButton) radio_group.getChildAt(i));
            Drawable drawable = getResources().getDrawable(drawables[i]);
            drawable.setBounds(0, 0, AutoSizeUtils.mm2px(mAppContext, iconHeight / drawable.getMinimumHeight() * drawable.getMinimumWidth()), AutoSizeUtils.mm2px(mAppContext, iconHeight));//设置边界
            radioButton.setCompoundDrawables(null, drawable, null, null);
        }
    }

    private void setNavDataLocal() {
        try {
            String result = (String) SharedPreUtils.getParam("MainNav", "NavDate", "");
            if (!TextUtils.isEmpty(result)) {
                MainNavEntity mainNavEntity = GsonUtils.fromJson(result, MainNavEntity.class);
                if (mainNavEntity != null && !isEndOrStartTime(mainNavEntity.getCurrentTime(), mainNavEntity.getExpireTime())) {
                    List<MainNavBean> mainNavBeanList = mainNavEntity.getMainNavBeanList();
                    if (mainNavBeanList != null && mainNavBeanList.size() == 5) {
                        webNum = 0;
                        for (int i = 0; i < mainNavBeanList.size(); i++) {
                            MainNavBean mainNavBean = mainNavBeanList.get(i);
                            RadioButton radioButton = (RadioButton) radio_group.getChildAt(i);
                            if (!isWebLinkUrl(mainNavBean.getPicUrl()) && !isWebLinkUrl(mainNavBean.getSecondColor())) {
                                //设置底部导航链接
                                links[i] = mainNavBean.getAndroidLink();
                                resIds[i] = getResId(mainNavBean.getAndroidLink());
                                //设置底部title
                                radioButton.setText(getStrings(mainNavBean.getTitle()));
                                //设置底部title颜色
                                radioButton.setTextColor(SelectorUtil.getColorStateList(mainNavBean.getMainColor(), mainNavBean.getSecondColor()));
                                //设置底部icon
                                StateListDrawable drawable = SelectorUtil.getDrawableStateList(mainNavBean.getPicUrlSecond(), mainNavBean.getPicUrl());
                                radioButton.setCompoundDrawables(null, drawable, null, null);
                            }
                        }
                        //设置底部背景颜色
                        if (!TextUtils.isEmpty(mainNavEntity.getBgColor())) {
                            radio_group.setBackgroundColor(Color.parseColor(mainNavEntity.getBgColor()));
                        }
                    }
                } else {
                    SharedPreUtils.clear("MainNav", "NavDate");
                }
            }
        } catch (Exception e) {
            SharedPreUtils.clear("MainNav", "NavDate");
        }
    }

    //登陆成功返回消息
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == MINE_REQ_CODE) {
            if (Arrays.asList(links).contains("mine")) {
                navController.navigate(R.id.mine);
            }
        }
        CallbackContext.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(getActivity()).onActivityResult(requestCode, resultCode, data);
    }

    private Intent onHomeIntent; // home键退出后通过intent启动程序

    @Override
    protected void onNewIntent(Intent intent) {
        // 拦截Intent，保存Intent，在onResume中进行处理
        super.onNewIntent(intent);
        onHomeIntent = intent;
        setIntent(intent);
        navigate();
    }

    @Override
    public void onResume() {
        if (onHomeIntent != null) { // home键退出后通过intent启动程序
            // dosomething···
            onHomeIntent = null;
        }
        super.onResume();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (getFragment() instanceof AliBCFragment) {
                boolean isGoBack = ((AliBCFragment) getFragment()).goBack();
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
        SaveUpdateImportDateUtils.getUpdateDataUtilsInstance().getMainIconData(this, 3);
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

        try {
            return super.dispatchTouchEvent(ev);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // Return whether touch the view.
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v instanceof EditText) {
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

    //获取当前选中的Fragment是HomePageFragment,返回选中的fragment类名
    public String getCheckedFragmentName() {
        if (getFragment() instanceof HomePageFragment) {
            return ((HomePageFragment) getFragment()).getFragmentName();
        }
        return "";
    }


    //获取当前被选中的fragment
    public Fragment getFragment() {
        Fragment navFragment = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        if (navFragment != null) {
            return navFragment.getChildFragmentManager().getPrimaryNavigationFragment();
        }
        return null;
    }
}
