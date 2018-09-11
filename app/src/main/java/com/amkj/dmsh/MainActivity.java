package com.amkj.dmsh;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.ali.auth.third.ui.context.CallbackContext;
import com.amkj.dmsh.base.BaseApplication;
import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.base.BaseFragmentActivity;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.base.NetLoadUtils;
import com.amkj.dmsh.bean.CommunalUserInfoEntity;
import com.amkj.dmsh.bean.CommunalUserInfoEntity.CommunalUserInfoBean;
import com.amkj.dmsh.bean.MainNavEntity;
import com.amkj.dmsh.bean.MainNavEntity.MainNavBean;
import com.amkj.dmsh.bean.OSSConfigEntity;
import com.amkj.dmsh.bean.OSSConfigEntity.OSSConfigBean;
import com.amkj.dmsh.bean.PushInfoEntity;
import com.amkj.dmsh.bean.RequestStatus;
import com.amkj.dmsh.constant.AppUpdateUtils;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.ConstantVariable;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.constant.XUtil;
import com.amkj.dmsh.dominant.fragment.QualityFragment;
import com.amkj.dmsh.find.fragment.FindFragment;
import com.amkj.dmsh.homepage.bean.CommunalADActivityEntity;
import com.amkj.dmsh.homepage.bean.CommunalADActivityEntity.CommunalADActivityBean;
import com.amkj.dmsh.homepage.fragment.AliBCFragment;
import com.amkj.dmsh.homepage.fragment.HomePageFragment;
import com.amkj.dmsh.homepage.fragment.TimeShowNewFragment;
import com.amkj.dmsh.message.bean.MessageTotalEntity;
import com.amkj.dmsh.message.bean.MessageTotalEntity.MessageTotalBean;
import com.amkj.dmsh.mine.bean.SavePersonalInfoBean;
import com.amkj.dmsh.mine.fragment.MineDataFragment;
import com.amkj.dmsh.qyservice.QyServiceUtils;
import com.amkj.dmsh.release.dialogutils.AlertSettingBean;
import com.amkj.dmsh.release.dialogutils.AlertView;
import com.amkj.dmsh.release.dialogutils.OnAlertItemClickListener;
import com.amkj.dmsh.utils.FileStreamUtils;
import com.amkj.dmsh.utils.NetWorkUtils;
import com.amkj.dmsh.utils.SelectorUtil;
import com.amkj.dmsh.utils.alertdialog.AlertDialogHelper;
import com.amkj.dmsh.utils.alertdialog.AlertDialogImage;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.amkj.dmsh.utils.inteface.MyCallBack;
import com.amkj.dmsh.utils.restartapputils.RestartAPPTool;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
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
import me.leolin.shortcutbadger.ShortcutBadger;

import static com.amkj.dmsh.base.BaseApplication.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.adClickTotal;
import static com.amkj.dmsh.constant.ConstantMethod.getCurrentTime;
import static com.amkj.dmsh.constant.ConstantMethod.getDateFormat;
import static com.amkj.dmsh.constant.ConstantMethod.getDateMilliSecond;
import static com.amkj.dmsh.constant.ConstantMethod.getPersonalInfo;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.isEndOrEndTimeAddSeconds;
import static com.amkj.dmsh.constant.ConstantMethod.isSameTimeDay;
import static com.amkj.dmsh.constant.ConstantMethod.savePersonalInfoCache;
import static com.amkj.dmsh.constant.ConstantMethod.setDeviceInfo;
import static com.amkj.dmsh.constant.ConstantMethod.setSkipPath;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.MAIN_FIND;
import static com.amkj.dmsh.constant.ConstantVariable.MAIN_HOME;
import static com.amkj.dmsh.constant.ConstantVariable.MAIN_MINE;
import static com.amkj.dmsh.constant.ConstantVariable.MAIN_QUALITY;
import static com.amkj.dmsh.constant.ConstantVariable.MAIN_TIME;
import static com.amkj.dmsh.constant.ConstantVariable.MAIN_WEB;
import static com.amkj.dmsh.constant.ConstantVariable.OTHER_WECHAT;
import static com.amkj.dmsh.constant.ConstantVariable.PUSH_CHECK;
import static com.amkj.dmsh.constant.ConstantVariable.PUSH_CHECK_TIME;
import static com.amkj.dmsh.constant.ConstantVariable.REFRESH_MESSAGE_TOTAL;
import static com.amkj.dmsh.constant.ConstantVariable.REGEX_TEXT;
import static com.amkj.dmsh.constant.ConstantVariable.UP_TOTAL_SIZE;
import static com.amkj.dmsh.constant.ConstantVariable.isDebugTag;
import static com.amkj.dmsh.constant.ConstantVariable.isShowTint;
import static com.amkj.dmsh.utils.ServiceDownUtils.INSTALL_APP_PROGRESS;
import static com.amkj.dmsh.utils.glide.GlideImageLoaderUtil.createFilePath;
import static com.amkj.dmsh.utils.glide.GlideImageLoaderUtil.fileIsExist;

/**
 * @author Liuguipeng
 * @date 2017/10/31
 */
public class MainActivity extends BaseFragmentActivity implements OnAlertItemClickListener, View.OnClickListener {
    @BindView(R.id.rp_bottom_main)
    RadioGroup rp_bottom_main;
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    public static final int MINE_REQ_CODE = 13;
    private Map<String, String> params = new HashMap<>();
    private boolean isFirstTime;
    private AlertView exitApp;
    private AlertView selectServer;
    private String[] SERVER = {"正式库", "测试库", "泽钦", "泽鑫", "Mr.W", "修改UID", "预发布", "王凯2"};
    private AlertView mAlertViewExt;
    private EditText etName;
    private static Map<String, Integer> iconMap = new HashMap<>();
    private List<CommunalADActivityBean> adActivityList = new ArrayList<>();
    public static final String ImgKey = "ImgPath";
    public static final String TimeKey = "ShowSeconds";
    public static final String SkipUrlKey = "SkipUrl";
    public static final String LauncherAdIdKey = "AD_ID";
    public static String OriginalImgUrl = "OriginalImgUrl";
    private SharedPreferences sharedPreferences;
    //    地址存储路径
    private String adsPath = "";
    private int screenHeight;
    private Fragment fragment;
    private boolean isChecked;
    private ConstantMethod constantMethod;
    private Map<String, String> pushMap;
    private AlertDialogImage alertDialogAdImage;
    private AlertDialog alertImageAdDialog;

    @Override
    protected void postEventResult(@NonNull EventMessage message) {
        if ("skipMinePage".equals(message.type)) {
            changePage(ConstantVariable.MAIN_MINE, null);
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
        Intent intent = getIntent();
        String type = intent.getStringExtra("type");
        isFirstTime = intent.getBooleanExtra("isFirstTime", true);
        sharedPreferences = getSharedPreferences("launchAD", Context.MODE_PRIVATE);
        String skipPage = intent.getStringExtra(ConstantVariable.SKIP_PAGE);
        setNavData();
        if (type != null && type.equals("BindOtherAccount")) {
            if (userId > 0) {
                //登陆成功，加载信息
                changePage(ConstantVariable.MAIN_MINE, null);
            }
        } else if (skipPage != null && skipPage.equals("1")) {
            params.clear();
            String webUrl = intent.getStringExtra("webUrl");
            if (!TextUtils.isEmpty(webUrl)) {
                params.put("webUrl", webUrl);
            }
            String indexShowLight = intent.getStringExtra("indexShowLight");
//            导航栏显示高亮
            if (!TextUtils.isEmpty(indexShowLight)) {
                int index = Integer.parseInt(indexShowLight);
                RadioButton rb = (RadioButton) rp_bottom_main.getChildAt(index > 4 ? 0 : index);
                rb.setChecked(true);
            }
            changePage(intent.getStringExtra("type"), params);
        } else if (!TextUtils.isEmpty(type)) {
            changePage(intent.getStringExtra("type"), null);
        } else {
            initMainPage();
        }
        if (isFirstTime) {
//            弹窗广告
            getADDialog();
//            启动广告
            getLaunchBanner();
            if (isDebugTag) {
                getSelectedDialog();
            }
//            打开app时间统计
//            加载OSS配置
            getOSSConfig();
            adsPath = getFilesDir().getAbsolutePath() + "/adr_s/asr_s.txt";
//            获取地址版本
            getAddressVersion();
//            获取图标更新
            getMainIconData();
//            获取push信息
            getFirstPushInfo();
//            获取更新
            AppUpdateUtils.getInstance().getAppUpdate(MainActivity.this);

            getAppUpdateJson();
            //            统计数据上传大小
            getUpTotalSize();
//            设置分享提示
            setShareTint();
//            检查推送权限
            checkPushPermission();
        }
        rp_bottom_main.setOnCheckedChangeListener((group, checkedId) -> {
            isChecked = true;
            for (int i = 0; i < group.getChildCount(); i++) {
                RadioButton rb = (RadioButton) group.getChildAt(i);
                String mainPage = (String) rb.getTag(R.id.main_page);
                rb.setOnClickListener(MainActivity.this);
                if (rb.getId() == checkedId) {
                    rb.setChecked(true);
                    if (!TextUtils.isEmpty(mainPage)) {
                        try {
                            params.clear();
                            Matcher matcher = Pattern.compile(REGEX_TEXT).matcher(mainPage);
                            boolean isUrl = false;
                            while (matcher.find()) {
                                isUrl = true;
                            }
                            if (isUrl) {
                                params.put("loadUrl", mainPage);
                                changePage(MAIN_WEB, params);
                            } else {
                                changePage(mainPage, null);
                            }
                        } catch (Exception e) {
                            skipDefaultNum(i);
                            e.printStackTrace();
                        }
                    } else {
                        skipDefaultNum(i);
                    }
                }
            }
        });
        //        七鱼客服登录 获取用户信息 登进登出……
        getNetDataInfo();
    }

    /**
     * 检查推送权限
     */
    private void checkPushPermission() {
        SharedPreferences preferences = getSharedPreferences(PUSH_CHECK, MODE_PRIVATE);
        String pushCheckTime = preferences.getString(PUSH_CHECK_TIME, "");
        String currentTime = getCurrentTime();
        if (TextUtils.isEmpty(pushCheckTime) || isEndOrEndTimeAddSeconds(currentTime, pushCheckTime, 10 * 24 * 60 * 60)) {
            SharedPreferences.Editor edit = preferences.edit();
            edit.putString(PUSH_CHECK_TIME, currentTime);
            edit.apply();
            NotificationManagerCompat manager = NotificationManagerCompat.from(getApplicationContext());
            boolean isOpened = manager.areNotificationsEnabled();
            if (!isOpened) {
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
                        .setMsg("“多么生活”想给您发送通知,方便我们更好的为您服务，限时秒杀不再错过。")
                        .setSingleButton(true)
                        .setConfirmText("去设置");
                alertDialogHelper.show();
            }
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
            XUtil.Post(url, params, new MyCallBack<String>() {
                @Override
                public void onSuccess(String result) {
                    PushInfoEntity pushInfoEntity = PushInfoEntity.objectFromData(result);
                    if (pushInfoEntity != null
                            && pushInfoEntity.getCode().equals("01")
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
        XUtil.Post(url, params, new MyCallBack<String>() {
        });
        SharedPreferences.Editor edit = sharedPreferences.edit();
        if (pushMap == null) {
            pushMap = new HashMap<>();
        }
        pushMap.put(String.valueOf(userId), pushInfoEntity.getCurrentTime());
        edit.putString("pushInfoMap", new Gson().toJson(pushMap));
        edit.apply();
    }

    private void loginClassQYService(CommunalUserInfoBean communalUserInfoBean) {
        QyServiceUtils.getQyInstance().loginQyUserInfo(MainActivity.this
                , communalUserInfoBean.getUid()
                , getStrings(communalUserInfoBean.getNickname())
                , getStrings(communalUserInfoBean.getMobile())
                , communalUserInfoBean.getAvatar());
    }

    private void skipDefaultNum(int i) {
        switch (i) {
            case 0:
                changePage(MAIN_HOME, null);
                break;
            case 1:
                changePage(MAIN_QUALITY, null);
                break;
            case 2:
                changePage(MAIN_TIME, null);
                break;
            case 3:
                changePage(MAIN_FIND, null);
                break;
            case 4:
                changePage(MAIN_MINE, null);
                break;
        }
    }

    private void setNavData() {
        iconMap.clear();
        BaseApplication app = (BaseApplication) getApplication();
        screenHeight = app.getScreenHeight();
        SharedPreferences sharedPreferences = getSharedPreferences("MainNav", MODE_PRIVATE);
        String modifyTime = sharedPreferences.getString("modifyTime", "");
        if (!TextUtils.isEmpty(modifyTime)) {
            String result = sharedPreferences.getString("NavDate", "");
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
        } catch (ParseException e) {
            e.printStackTrace();
            return true;
        }
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
     * @param position
     */
    private void initDownNavData(RadioButton rb, MainNavBean mainNavBean, int position) {
        String picPath = getPicPath(mainNavBean.getPicUrl());
        String picSecondPath = getPicPath(mainNavBean.getPicUrlSecond());
        if (fileIsExist(picPath) && fileIsExist(picSecondPath)) {
            setDownBitmap(rb, picPath, picSecondPath);
            rb.setTag(R.id.main_page, getStrings(getNovType(mainNavBean.getAndroidLink())));
            iconMap.put(getStrings(getIcMapType(mainNavBean.getAndroidLink())), position);
            rb.setText(getStrings(mainNavBean.getTitle()));
        } else if (fileIsExist(picPath) || fileIsExist(picSecondPath)) {
//            设置相同Icon
            if (fileIsExist(picPath)) {
                setDownBitmap(rb, picPath, picPath);
            } else {
                setDownBitmap(rb, picSecondPath, picSecondPath);
            }
            rb.setTag(R.id.main_page, getStrings(getNovType(mainNavBean.getAndroidLink())));
            iconMap.put(getStrings(getIcMapType(mainNavBean.getAndroidLink())), position);
            rb.setText(getStrings(mainNavBean.getTitle()));
        } else {
            setNormalIcon(position, rb);
        }
    }

    /**
     * 判断是否是网页底栏
     *
     * @param androidLink
     * @return
     */
    private String getNovType(String androidLink) {
        Matcher matcher = Pattern.compile(REGEX_TEXT).matcher(androidLink);
        while (matcher.find()) {
            androidLink = matcher.group();
        }
        return androidLink;
    }

    /**
     * map 存储网页类型
     *
     * @param linkType
     * @return
     */
    private String getIcMapType(String linkType) {
        Matcher matcher = Pattern.compile(REGEX_TEXT).matcher(linkType);
        while (matcher.find()) {
            linkType = MAIN_WEB;
        }
        return linkType;
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
        drawable.addState(new int[]{android.R.attr.state_checked}, new BitmapDrawable(secBitmap));
        drawable.addState(new int[]{-android.R.attr.state_checked}, new BitmapDrawable(bitmap));
        float cofValue = (48f / 1334) * screenHeight / drawable.getMinimumHeight();
        drawable.setBounds(0, 0, (int) (drawable.getMinimumWidth() * cofValue), (int) (cofValue * drawable.getMinimumHeight())); //设置边界
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
                rb.setTag(R.id.main_page, MAIN_HOME);
                iconMap.put(MAIN_HOME, i);
                Drawable drawable = getResources().getDrawable(R.drawable.selector_bottom_home_bar);
                float cofValue = (48f / 1334) * screenHeight / drawable.getMinimumHeight();
                drawable.setBounds(0, 0, (int) (drawable.getMinimumWidth() * cofValue), (int) (cofValue * drawable.getMinimumHeight()));//设置边界
                rb.setCompoundDrawables(null, drawable, null, null);
                break;
            case 1:
                rb.setTag(R.id.main_page, MAIN_QUALITY);
                iconMap.put(MAIN_QUALITY, i);
                drawable = getResources().getDrawable(R.drawable.selector_bottom_quality_bar);
                cofValue = (48f / 1334) * screenHeight / drawable.getMinimumHeight();
                drawable.setBounds(0, 0, (int) (drawable.getMinimumWidth() * cofValue), (int) (cofValue * drawable.getMinimumHeight()));//设置边界
                rb.setCompoundDrawables(null, drawable, null, null);
                break;
            case 2:
                rb.setTag(R.id.main_page, MAIN_TIME);
                iconMap.put(MAIN_TIME, i);
                drawable = getResources().getDrawable(R.drawable.selector_bottom_time_bar);
                cofValue = (48f / 1334) * screenHeight / drawable.getMinimumHeight();
                drawable.setBounds(0, 0, (int) (drawable.getMinimumWidth() * cofValue), (int) (cofValue * drawable.getMinimumHeight()));//设置边界
                rb.setCompoundDrawables(null, drawable, null, null);
                break;
            case 3:
                rb.setTag(R.id.main_page, MAIN_FIND);
                iconMap.put(MAIN_FIND, i);
                drawable = getResources().getDrawable(R.drawable.selector_bottom_find_bar);
                cofValue = (48f / 1334) * screenHeight / drawable.getMinimumHeight();
                drawable.setBounds(0, 0, (int) (drawable.getMinimumWidth() * cofValue), (int) (cofValue * drawable.getMinimumHeight()));//设置边界
                rb.setCompoundDrawables(null, drawable, null, null);
                break;
            case 4:
                rb.setTag(R.id.main_page, MAIN_MINE);
                iconMap.put(MAIN_MINE, i);
                drawable = getResources().getDrawable(R.drawable.selector_bottom_mine_bar);
                cofValue = (48f / 1334) * screenHeight / drawable.getMinimumHeight();
                drawable.setBounds(0, 0, (int) (drawable.getMinimumWidth() * cofValue), (int) (cofValue * drawable.getMinimumHeight()));//设置边界
                rb.setCompoundDrawables(null, drawable, null, null);
                break;
        }
    }

    /**
     * 获取底部导航栏数据
     */
    private void getMainIconData() {
        String url = Url.BASE_URL + Url.H_BOTTOM_ICON;
        XUtil.Get(url, null, new MyCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                MainNavEntity mainNavEntity = gson.fromJson(result, MainNavEntity.class);
                if (mainNavEntity != null) {
                    if (mainNavEntity.getCode().equals("01")) {
                        if (mainNavEntity.getMainNavBeanList().size() == 5) {
                            SharedPreferences sharedPreferences = getSharedPreferences("MainNav", MODE_PRIVATE);
                            String modifyTime = sharedPreferences.getString("modifyTime", "");
                            if (!modifyTime.equals(mainNavEntity.getModifyTime())) {
                                SharedPreferences.Editor edit = sharedPreferences.edit();
                                edit.putString("modifyTime", getStrings(mainNavEntity.getModifyTime()));
                                edit.putString("NavDate", result);
                                edit.apply();
                                for (MainNavBean mainNavBean : mainNavEntity.getMainNavBeanList()) {
                                    saveNavIcon(mainNavBean.getPicUrl());
                                    saveNavIcon(mainNavBean.getPicUrlSecond());
                                }
                            }
                        }
                    }
                }
            }
        });
    }

    //    保存底部导航图标
    private void saveNavIcon(String picUrl) {
        final String Nav = getPicPath(picUrl);
        if (!TextUtils.isEmpty(picUrl)) {
            if (!fileIsExist(Nav)) {
                Glide.with(getApplicationContext()).downloadOnly().load(picUrl).into(new SimpleTarget<File>() {
                    @Override
                    public void onResourceReady(File file, Transition<? super File> transition) {
                        try {
                            FileStreamUtils.forChannel(file, new File(Nav));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }
    }

    /**
     * 获取图标保存路径
     *
     * @param picUrl
     * @return
     */
    @NonNull
    private String getPicPath(String picUrl) {
        String Nav = getCacheDir().getAbsolutePath() + "/DownNavIcon";
        createFilePath(Nav);
        Nav = Nav + "/" + picUrl.substring(picUrl.lastIndexOf("/"));
        return Nav;
    }

    /**
     * 获取消息信息
     * 配置桌面图标角标
     */
    private void getDesktopMesCount() {
        if (userId > 0) {
            String url = Url.BASE_URL + Url.H_MES_STATISTICS;
            Map<String, Object> params = new HashMap<>();
            params.put("uid", userId);
            NetLoadUtils.getQyInstance().loadNetDataPost(mAppContext, url
                    , params, new NetLoadUtils.NetLoadListener() {
                        @Override
                        public void onSuccess(String result) {
                            Gson gson = new Gson();
                            MessageTotalEntity messageTotalEntity = gson.fromJson(result, MessageTotalEntity.class);
                            if (messageTotalEntity != null) {
                                if (messageTotalEntity.getCode().equals("01")) {
                                    MessageTotalBean messageTotalBean = messageTotalEntity.getMessageTotalBean();
                                    int totalCount = messageTotalBean.getSmTotal() + messageTotalBean.getLikeTotal()
                                            + messageTotalBean.getCommentTotal() + messageTotalBean.getOrderTotal()
                                            + messageTotalBean.getCommOffifialTotal();
                                    if (!Build.MANUFACTURER.equalsIgnoreCase("Xiaomi")) {
                                        ShortcutBadger.applyCount(mAppContext, totalCount);
                                    }
                                }
                            }
                        }

                        @Override
                        public void netClose() {
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            ShortcutBadger.removeCount(mAppContext);
                        }
                    });
        } else {
            ShortcutBadger.removeCount(getApplicationContext());
        }
    }

    /**
     * 获取最新账号信息 避免黑名单、异常账户登录
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
            XUtil.Post(url, params, new MyCallBack<String>() {
                @Override
                public void onSuccess(String result) {
                    Gson gson = new Gson();
                    CommunalUserInfoEntity minePageData = gson.fromJson(result, CommunalUserInfoEntity.class);
                    if (minePageData != null) {
                        CommunalUserInfoBean communalUserInfoBean = minePageData.getCommunalUserInfoBean();
                        if (minePageData.getCode().equals("01")) {
                            loginClassQYService(communalUserInfoBean);
                            getLoginStatusTime();
//                            上传设备信息
                            setDeviceInfo(MainActivity.this, communalUserInfoBean.getApp_version_no(), communalUserInfoBean.getDevice_model(), communalUserInfoBean.getDevice_sys_version());
                        } else {
                            personalInfo.setLogin(false);
                            savePersonalInfoCache(MainActivity.this, personalInfo);
                        }
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    super.onError(ex, isOnCallback);
                    personalInfo.setLogin(false);
                    savePersonalInfoCache(MainActivity.this, personalInfo);
                }
            });
        } else {
            userId = 0;
        }
    }

    /**
     * 统计登陆时间
     */
    private void getLoginStatusTime() {
        String url = Url.BASE_URL + Url.H_LOGIN_LAST_TIME;
        Map<String, Object> params = new HashMap<>();
        params.put("uid", userId);
        XUtil.Post(url, params, new MyCallBack<String>() {
        });
    }

    /**
     * 获取上传统计大小
     */
    private void getUpTotalSize() {
        String url = Url.BASE_URL + Url.TOTAL_UP_SIZE;
        XUtil.Get(url, null, new MyCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                RequestStatus requestStatus = gson.fromJson(result, RequestStatus.class);
                if (requestStatus != null) {
                    if (requestStatus.getCode().equals("01") && requestStatus.getResult() != null) {
                        UP_TOTAL_SIZE = requestStatus.getResult().getCollectSize() > 0 ? requestStatus.getResult().getCollectSize() : 50;
                    }
                }
            }
        });
    }

    private void getAddressVersion() {
        String url = Url.BASE_URL + Url.H_ADDRESS_VERSION;
        XUtil.Get(url, null, new MyCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                RequestStatus requestStatus = gson.fromJson(result, RequestStatus.class);
                if (requestStatus != null) {
                    if (requestStatus.getCode().equals("01")) {
                        SharedPreferences preferences = getSharedPreferences("addressConfig", MODE_PRIVATE);
                        String versionName = preferences.getString("version", "");
                        if (!versionName.equals(requestStatus.getVersion())) {
                            SharedPreferences.Editor edit = preferences.edit();
                            edit.putString("version", requestStatus.getVersion());
                            edit.apply();
                            getAddressData();
                        }
                    }
                }
            }
        });
    }


    private void getAddressData() {
        String url = Url.BASE_URL + Url.H_ADDRESS_DATA;
        XUtil.Get(url, null, new MyCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                String code = "";
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    code = (String) jsonObject.get("code");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (code.equals("01")) {
                    FileStreamUtils.writeFileFromString(adsPath, result, false);
                }
            }
        });
    }

    private void getOSSConfig() {
        if (NetWorkUtils.checkNet(MainActivity.this)) {
            String url = Url.BASE_URL + Url.H_OSS_CONFIG;
            XUtil.Get(url, null, new MyCallBack<String>() {
                @Override
                public void onSuccess(String result) {
                    Gson gson = new Gson();
                    OSSConfigEntity ossConfigEntity = gson.fromJson(result, OSSConfigEntity.class);
                    if (ossConfigEntity != null) {
                        if (ossConfigEntity.getCode().equals("01")) {
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
    }

    private void getLaunchBanner() {
        String url = Url.BASE_URL + Url.H_LAUNCH_AD_DIALOG;
        adActivityList.clear();
        if (NetWorkUtils.checkNet(MainActivity.this)) {
            XUtil.Get(url, null, new MyCallBack<String>() {
                @Override
                public void onSuccess(String result) {
                    Gson gson = new Gson();
                    CommunalADActivityEntity categoryAD = gson.fromJson(result, CommunalADActivityEntity.class);
                    if (categoryAD != null) {
                        if (categoryAD.getCode().equals("01")) {
                            adActivityList.addAll(categoryAD.getCommunalADActivityBeanList());
                            if (adActivityList.size() > 0) {
                                final SharedPreferences.Editor edit = sharedPreferences.edit();
                                CommunalADActivityBean communalADActivityBean = adActivityList.get(adActivityList.size() - 1);
                                if (getDateMilliSecond(communalADActivityBean.getEndTime()) > Calendar.getInstance().getTime().getTime()) {
                                    setLaunchAdData(adActivityList, edit);
                                } else {
                                    edit.clear().apply();
                                }
                            }
                        }
                    }
                }
            });
        }
    }

    private void setLaunchAdData(List<CommunalADActivityBean> adActivityList, SharedPreferences.Editor edit) {
        if (adActivityList.size() > 0) {
            final CommunalADActivityBean communalADActivityBean = adActivityList.get(adActivityList.size() - 1);
            final String pic_url = communalADActivityBean.getPicUrl();
            Glide.with(MainActivity.this).asBitmap().load(pic_url).into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(@NonNull Bitmap resource, Transition<? super Bitmap> transition) {
                    saveLaunchImg(bitmapByte(resource), communalADActivityBean, edit);
                }
            });
        } else {
            edit.clear().apply();
        }
    }

    private byte[] bitmapByte(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private void saveLaunchImg(byte[] resource, CommunalADActivityBean
            communalADActivityBean, SharedPreferences.Editor edit) {
        String pic_url = communalADActivityBean.getPicUrl();
        String imgSaveName = pic_url.substring(pic_url.lastIndexOf("/"));
        String IMG_FILE_NAME = getFilesDir().getAbsolutePath() + "/launch";
        String imgAbsPath = IMG_FILE_NAME + "/" + imgSaveName;
        File fileDir = new File(IMG_FILE_NAME);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        if (!fileIsExist(imgAbsPath) || TextUtils.isEmpty(sharedPreferences.getString(ImgKey, ""))) {
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(imgAbsPath);
                fos.write(resource);
                fos.flush();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fos != null) {
                        fos.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            edit.putString(ImgKey, imgAbsPath);
            edit.putString(OriginalImgUrl, communalADActivityBean.getPicUrl());
            edit.putInt(LauncherAdIdKey, communalADActivityBean.getObjectId());
            edit.putString(TimeKey, !TextUtils.isEmpty(communalADActivityBean.getShowTime()) ? communalADActivityBean.getShowTime() : "3");
            edit.putString(SkipUrlKey, !TextUtils.isEmpty(communalADActivityBean.getAndroidLink())
                    ? communalADActivityBean.getAndroidLink() : "app://");
            edit.apply();
        }
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
                                    SharedPreferences loginStatus = getSharedPreferences("LoginStatus", Context.MODE_PRIVATE);
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
                    edit.apply();
                    SharedPreferences loginStatus = getSharedPreferences("LoginStatus", MODE_PRIVATE);
                    SharedPreferences.Editor loginEdit = loginStatus.edit();
                    loginEdit.putBoolean("isLogin", false);
                    loginEdit.apply();
                    finish();
                }
            });
            selectServer.setCancelable(true);
        }
        selectServer.show();
    }

    @Override
    public void setStatusColor() {
        super.setStatusColor();
    }

    private void closeKeyboard() {
        //关闭软键盘
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etName.getWindowToken(), 0);
    }

    private void getADDialog() {
        String url = Url.BASE_URL + Url.H_AD_DIALOG;
        XUtil.Get(url, null, new MyCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                CommunalADActivityEntity communalADActivityEntity = gson.fromJson(result, CommunalADActivityEntity.class);
                if (communalADActivityEntity != null) {
                    if (communalADActivityEntity.getCode().equals("01")) {
                        List<CommunalADActivityBean> communalADActivityBeanList = communalADActivityEntity.getCommunalADActivityBeanList();
                        if (communalADActivityBeanList != null && communalADActivityBeanList.size() > 0) {
                            CommunalADActivityBean communalADActivityBean = communalADActivityBeanList.get(communalADActivityBeanList.size() - 1);
                            SharedPreferences sp = getSharedPreferences("ADDialog", MODE_PRIVATE);
                            String createTime = sp.getString("createTime", "0");
                            String create_time = communalADActivityBean.getCtime();
                            if (!createTime.equals(create_time)) {
                                SharedPreferences.Editor edit = sp.edit();
                                edit.putString("createTime", create_time);
                                edit.apply();
                                GlideImageLoaderUtil.loadFinishImgDrawable(MainActivity.this, communalADActivityBean.getPicUrl(), new GlideImageLoaderUtil.ImageLoaderFinishListener() {
                                    @Override
                                    public void onSuccess(Bitmap bitmap) {
                                        if (alertImageAdDialog == null) {
                                            alertDialogAdImage = new AlertDialogImage();
                                            alertImageAdDialog = alertDialogAdImage.createAlertDialog(MainActivity.this);
                                        }
                                        alertImageAdDialog.show();
                                        alertDialogAdImage.setAlertClickListener(new AlertDialogImage.AlertImageClickListener() {
                                            @Override
                                            public void imageClick() {
                                                adClickTotal(communalADActivityBean.getObjectId());
                                                setSkipPath(MainActivity.this, communalADActivityBean.getAndroidLink(), false);
                                                alertImageAdDialog.dismiss();
                                            }
                                        });
                                        alertDialogAdImage.setImage(bitmap);
                                    }

                                    @Override
                                    public void onStart() {

                                    }

                                    @Override
                                    public void onError(Drawable errorDrawable) {

                                    }
                                });
                            }
                        }
                    }
                }
            }
        });
    }

    //登陆成功返回消息
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case MINE_REQ_CODE:
                changePage(ConstantVariable.MAIN_MINE, null);
                break;
        }
        CallbackContext.onActivityResult(requestCode, resultCode, data);
    }

    private void initMainPage() {
        changePage(MAIN_HOME, null);
    }

    private Fragment lastFragment = null;

    private void changePage(String tag, Map<String, String> params) {
        try {
            int lightIndex = iconMap.get(tag) != null ? iconMap.get(tag) : 0;
            if (lightIndex < rp_bottom_main.getChildCount()) {
                RadioButton radioButton = (RadioButton) rp_bottom_main.getChildAt(lightIndex);
                radioButton.setChecked(true);
            }
            fragment = fragmentManager.findFragmentByTag(tag);
            transaction = fragmentManager.beginTransaction();
            if (fragment != null && fragment.isAdded()) {
                if (lastFragment != null) {
                    transaction.hide(lastFragment).commitAllowingStateLoss();
                }
                transaction.show(fragment);
                lastFragment = fragment;
                fragment.onResume();
            } else {
                switch (tag) {
                    case MAIN_HOME:
                        fragment = BaseFragment.newInstance(HomePageFragment.class, null, null);
                        break;
                    case ConstantVariable.MAIN_QUALITY:
                        fragment = BaseFragment.newInstance(QualityFragment.class, null, null);
                        break;
                    case ConstantVariable.MAIN_TIME:
                        fragment = BaseFragment.newInstance(TimeShowNewFragment.class, null, null);
                        break;
                    case ConstantVariable.MAIN_FIND:
                        fragment = BaseFragment.newInstance(FindFragment.class, null, null);
                        break;
                    case ConstantVariable.MAIN_MINE:
                        fragment = BaseFragment.newInstance(MineDataFragment.class, null, null);
                        break;
                    case MAIN_WEB:
                        params.put("paddingStatus", "true");
                        fragment = BaseFragment.newInstance(AliBCFragment.class, params, null);
                        break;
                    default:
                        fragment = BaseFragment.newInstance(HomePageFragment.class, null, null);
                        break;
                }
                if (lastFragment != null) {
                    if (fragment.isAdded()) {
                        transaction.hide(lastFragment).show(fragment).commitAllowingStateLoss();

                    } else {
                        transaction.hide(lastFragment).add(R.id.main_container, fragment, tag).commitAllowingStateLoss();

                    }
                } else {
                    transaction.add(R.id.main_container, fragment, tag).commitAllowingStateLoss();
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

    @Override
    public void onAlertItemClick(Object o, int position) {
        if (o == exitApp && position != AlertView.CANCELPOSITION) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
            //            获取桌面图标角标更新
            getDesktopMesCount();
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
        String mainPage = (String) v.getTag(R.id.main_page);
        if (!TextUtils.isEmpty(mainPage) && !isChecked) {
            changePage(mainPage, params);
        }
        isChecked = false;
    }

    public void getAppUpdateJson() {
        String url = Url.BASE_URL + Url.APP_TOTAL_ACTION;
        XUtil.Get(url, null, new MyCallBack<String>() {
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
        RestartAPPTool.restartAPP(MainActivity.this);
        super.onDestroy();
        if (constantMethod != null) {
            constantMethod.releaseHandlers();
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
        if (exitApp == null) {
            AlertSettingBean alertSettingBean = new AlertSettingBean();
            AlertSettingBean.AlertData alertData = new AlertSettingBean.AlertData();
            alertData.setCancelStr("取消");
            alertData.setDetermineStr("确定");
            alertData.setFirstDet(true);
            alertData.setMsg("确定要退出当前程序");
            alertSettingBean.setStyle(AlertView.Style.Alert);
            alertSettingBean.setAlertData(alertData);
            exitApp = new AlertView(alertSettingBean, this, this);
            exitApp.setCancelable(true);
        }
        exitApp.show();
    }

    @Override
    public void onBackPressed() {
        if (!isFinishing()) {
            super.onBackPressed();
        }
    }
}
