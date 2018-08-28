package com.amkj.dmsh.mine.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.bean.CommunalUserInfoEntity;
import com.amkj.dmsh.bean.CommunalUserInfoEntity.CommunalUserInfoBean;
import com.amkj.dmsh.bean.RequestStatus;
import com.amkj.dmsh.constant.Sha1Md5;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.constant.XUtil;
import com.amkj.dmsh.mine.CountDownHelper;
import com.amkj.dmsh.mine.bean.AuthorizeSuccessOtherData;
import com.amkj.dmsh.mine.bean.AuthorizeSuccessOtherData.OtherAccountBean;
import com.amkj.dmsh.mine.bean.FirstLoginEntity;
import com.amkj.dmsh.mine.bean.LoginPhoneCodeEntity;
import com.amkj.dmsh.mine.bean.LoginPhoneCodeEntity.LoginPhoneCodeBean;
import com.amkj.dmsh.mine.bean.OtherAccountBindEntity.OtherAccountBindInfo;
import com.amkj.dmsh.mine.bean.SavePersonalInfoBean;
import com.amkj.dmsh.qyservice.QyServiceUtils;
import com.amkj.dmsh.release.dialogutils.AlertSettingBean;
import com.amkj.dmsh.release.dialogutils.AlertView;
import com.amkj.dmsh.release.dialogutils.OnAlertItemClickListener;
import com.amkj.dmsh.shopdetails.activity.DirectMyCouponActivity;
import com.amkj.dmsh.utils.NetWorkUtils;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.amkj.dmsh.utils.inteface.MyCallBack;
import com.amkj.dmsh.views.SystemBarHelper;
import com.google.gson.Gson;
import com.tencent.stat.StatConfig;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

import static com.amkj.dmsh.base.BaseApplication.OSS_URL;
import static com.amkj.dmsh.constant.ConstantMethod.bindJPush;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.loginXNService;
import static com.amkj.dmsh.constant.ConstantMethod.savePersonalInfoCache;
import static com.amkj.dmsh.constant.ConstantMethod.setDeviceInfo;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.toMD5;
import static com.amkj.dmsh.constant.ConstantVariable.OTHER_QQ;
import static com.amkj.dmsh.constant.ConstantVariable.OTHER_SINA;
import static com.amkj.dmsh.constant.ConstantVariable.OTHER_WECHAT;
import static com.umeng.socialize.bean.SHARE_MEDIA.QQ;
import static com.umeng.socialize.bean.SHARE_MEDIA.SINA;
import static com.umeng.socialize.bean.SHARE_MEDIA.WEIXIN;

;

public class MineLoginActivity extends BaseActivity implements OnAlertItemClickListener {
    @BindView(R.id.tv_blue_title)
    TextView tv_blue_title;
    @BindView(R.id.iv_blue_back)
    ImageView iv_blue_back;
    @BindView(R.id.header_title_login)
    public LinearLayout Margin_title;
    //    忘记密码入口
    @BindView(R.id.tv_ming_login_forget_password)
    public TextView tv_ming_login_forget_password;
    //    登录入口显示
    @BindView(R.id.tv_tv_mine_change_login_way)
    public TextView tv_tv_mine_change_login_way;
    //    账号密码登录方式
    @BindView(R.id.ll_account_pas_way)
    public LinearLayout ll_account_pas_way;
    //手机号
    @BindView(R.id.edit_login_mobile)
    public EditText edit_login_mobile;
    //密码
    @BindView(R.id.edit_login_password)
    public EditText edit_login_password;
    //    手机号登录
    @BindView(R.id.ll_mobile_num_way)
    public LinearLayout ll_mobile_num_way;
    //手机号
    @BindView(R.id.et_login_code_mobile)
    public EditText et_login_code_mobile;
    //验证码
    @BindView(R.id.et_login_get_code)
    public EditText et_login_get_code;
    //    发送验证码
    @BindView(R.id.tv_login_send_code)
    public TextView tv_login_send_code;
    @BindView(R.id.reg_login_code_gif_view)
    public ProgressBar reg_login_code_gif_view;
    //注册请求码
    private AlertView weChatDialog;
    private AlertView qqDialog;
    private AlertView sinaDialog;
    private UMShareAPI mShareAPI;
    private int REQUEST_PERM = 100;
    private boolean isPhoneLogin;
    private CountDownHelper countDownHelper;

    @Override
    protected int getContentView() {
        return R.layout.activity_mine_login;
    }

    @Override
    protected void initViews() {
        tv_blue_title.setVisibility(View.INVISIBLE);
        iv_blue_back.setVisibility(View.INVISIBLE);
        loadHud.setCancellable(true);
    }

    @Override
    public void setStatusColor() {
        super.setStatusColor();
        SystemBarHelper.setPadding(MineLoginActivity.this, Margin_title);
        SystemBarHelper.immersiveStatusBar(MineLoginActivity.this);
    }

    @Override
    protected void loadData() {
    }

    private String toLowerCase(byte[] data) {
        int dist = 'a' - 'A';
        for (int i = 0; i < data.length; i++) {
            if (data[i] >= 'A' && data[i] <= 'Z') {
                data[i] += dist;
            }
        }
        return new String(data);
    }

    public void getData() {
        Sha1Md5 sha1Md5 = new Sha1Md5();
        final String t = "g{faJ&)H<34rz(q*\"C0S=Xy`TW~ZGD.wt6_1j@dU";
        String passwordLock;
        String url = Url.BASE_URL + Url.LOGIN_ACCOUNT;
        Map<String, Object> params = new HashMap<>();
        String phoneNumber = edit_login_mobile.getText().toString().trim();
        String password = edit_login_password.getText().toString().trim();
        passwordLock = sha1Md5.getMD5(toLowerCase(sha1Md5.getDigestOfString(password.getBytes()).getBytes()) + t);
        //手机号
        params.put("mobile", phoneNumber);
        //密码
        params.put("password", passwordLock);
        XUtil.Post(url, params, new MyCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                if (loadHud != null) {
                    loadHud.dismiss();
                }
                Gson gson = new Gson();
                CommunalUserInfoEntity loginAccount = gson.fromJson(result, CommunalUserInfoEntity.class);
                if (loginAccount != null) {
                    String backCode = loginAccount.getCode();
                    if (backCode.equals("01")) {
                        loginSuccessSetData(loginAccount);
                    } else {
                        showToast(MineLoginActivity.this, loginAccount.getMsg());
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (loadHud != null) {
                    loadHud.dismiss();
                }
                super.onError(ex, isOnCallback);
            }
        });
    }

    private void loginSuccessSetData(CommunalUserInfoEntity communalUserInfoEntity) {
        CommunalUserInfoBean communalUserInfoBean = communalUserInfoEntity.getCommunalUserInfoBean();
        //统计账号登录
        StatConfig.setCustomUserId(this, String.valueOf(communalUserInfoBean.getUid()));
//        友盟统计
        MobclickAgent.onProfileSignIn(String.valueOf(communalUserInfoBean.getUid()));

        showToast(MineLoginActivity.this, R.string.login_success);
        if (communalUserInfoBean.getFirstAppLogin() == 1) {
            isFirstLogin();
        }
//        保存个人信息
        SavePersonalInfoBean savePersonalInfo = new SavePersonalInfoBean();
        savePersonalInfo.setAvatar(getStrings(communalUserInfoBean.getAvatar()));
        savePersonalInfo.setNickName(getStrings(communalUserInfoBean.getNickname()));
        savePersonalInfo.setPhoneNum(getStrings(communalUserInfoBean.getMobile()));
        savePersonalInfo.setUid(communalUserInfoBean.getUid());
        savePersonalInfo.setLogin(true);
        savePersonalInfoCache(MineLoginActivity.this, savePersonalInfo);
//                            绑定JPush
        bindJPush(communalUserInfoBean.getUid());
        //小能客服登录
        loginXNService(this, communalUserInfoBean.getUid()
                , getStrings(communalUserInfoBean.getNickname())
                , getStrings(communalUserInfoBean.getMobile()));
        QyServiceUtils.getQyInstance().loginQyUserInfo(this,communalUserInfoBean.getUid(),communalUserInfoBean.getNickname(),communalUserInfoBean.getMobile(),communalUserInfoBean.getAvatar());
// 上传设备信息
        setDeviceInfo(this, communalUserInfoBean.getApp_version_no(), communalUserInfoBean.getDevice_model(), communalUserInfoBean.getDevice_sys_version());
        Intent data = new Intent();
        Bundle bundle = new Bundle();
        bundle.putParcelable("AccountInf", communalUserInfoEntity);
        data.putExtras(bundle);
        setResult(RESULT_OK, data);
        finish();
        overridePendingTransition(0, 0);
    }

    private void isFirstLogin() {
        String url = Url.BASE_URL + Url.FIRST_LOGIN_APP;
        XUtil.Get(url, null, new MyCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                FirstLoginEntity firstLoginEntity = gson.fromJson(result, FirstLoginEntity.class);
                if (firstLoginEntity.getCode().equals("01")
                        && firstLoginEntity.getFirstLoginBean() != null
                        && !TextUtils.isEmpty(firstLoginEntity.getFirstLoginBean().getImages())) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(getApplicationContext(), R.style.CustomTransDialog);
                    final View dialogView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.layout_dialog_image, null, false);
                    final ImageView iv_dialog_login = (ImageView) dialogView.findViewById(R.id.iv_dialog_login);
                    alertDialog.setView(dialogView);
                    final AlertDialog dialog = alertDialog.create();
                    dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                    dialog.setCanceledOnTouchOutside(true);//点击屏幕不消失
                    String imageUrl = firstLoginEntity.getFirstLoginBean().getImages();
                    if (!TextUtils.isEmpty(imageUrl) && imageUrl.contains(OSS_URL)) {
                        imageUrl = imageUrl + Url.OSS_IMG_FORMAT;
                    }
                    GlideImageLoaderUtil.loadFinishImgDrawable(getApplicationContext(), imageUrl, new GlideImageLoaderUtil.ImageLoaderFinishListener() {
                        @Override
                        public void onStart() {
                        }

                        @Override
                        public void onError(Drawable errorDrawable) {
                        }

                        @Override
                        public void onSuccess(Bitmap bitmap) {
                            iv_dialog_login.setImageBitmap(bitmap);
                            dialog.show();
                        }
                    });
                    iv_dialog_login.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            Intent intent = new Intent(getApplicationContext(), DirectMyCouponActivity.class);
                            startActivity(intent);
                        }
                    });
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
            }
        });

    }

    // 授权登录
    @Override
    public void onAlertItemClick(Object o, int position) {
        mShareAPI = UMShareAPI.get(this);
        if (o == weChatDialog && position != AlertView.CANCELPOSITION) {
            // 打开微信授权
            SHARE_MEDIA platform = WEIXIN;
            mShareAPI.getPlatformInfo(MineLoginActivity.this, platform, getDataInfoListener);
        } else if (o == qqDialog && position != AlertView.CANCELPOSITION) {
            //qq授权
            SHARE_MEDIA platform = QQ;
            mShareAPI.getPlatformInfo(MineLoginActivity.this, platform, getDataInfoListener);
        } else if (o == sinaDialog && position != AlertView.CANCELPOSITION) {
            //新浪授权
            SHARE_MEDIA platform = SINA;
            mShareAPI.getPlatformInfo(MineLoginActivity.this, platform, getDataInfoListener);
        }
    }

    UMAuthListener getDataInfoListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {
            if (loadHud != null) {
                loadHud.show();
            }
        }

        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            if (data != null) {
                final OtherAccountBindInfo info = new OtherAccountBindInfo();
                switch (platform) {
                    case WEIXIN:
                        info.setOpenid(data.get("openid"));
                        info.setUnionId(getStrings(data.get("uid")));
                        info.setType(OTHER_WECHAT);
                        info.setNickname(data.get("name"));
                        info.setAvatar(!TextUtils.isEmpty(data.get("iconurl")) ? data.get("iconurl") : "");
                        bindOtherAccount(info);
                        break;
                    case QQ:
                        info.setOpenid(data.get("uid"));
                        info.setType(OTHER_QQ);
                        info.setNickname(data.get("name"));
                        info.setAvatar(!TextUtils.isEmpty(data.get("iconurl")) ? data.get("iconurl") : "");
                        bindOtherAccount(info);
                        break;
                    case SINA:
                        info.setOpenid(data.get("uid"));
                        info.setType(OTHER_SINA);
                        info.setNickname(data.get("name"));
                        info.setAvatar(!TextUtils.isEmpty(data.get("iconurl ")) ? data.get("iconurl ") : "");
                        bindOtherAccount(info);
                        break;
                }
            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            showToast(getApplicationContext(), "授权失败");
            if (loadHud != null) {
                loadHud.dismiss();
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            showToast(getApplicationContext(), "授权取消");
            if (loadHud != null) {
                loadHud.dismiss();
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (loadHud != null) {
            loadHud.dismiss();
        }
        if (resultCode != RESULT_OK) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PERM) {
            //新浪授权
            SHARE_MEDIA platform = SHARE_MEDIA.SINA;
            mShareAPI.doOauthVerify(this, platform, getDataInfoListener);
        }
        try {
            mShareAPI.onActivityResult(requestCode, resultCode, data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void bindOtherAccount(final OtherAccountBindInfo otherAccountBindInfo) {
        String url = Url.BASE_URL + Url.MINE_OTHER_ACCOUNT;
        Map<String, Object> params = new HashMap<>();
        params.put("openid", otherAccountBindInfo.getOpenid());
        params.put("type", otherAccountBindInfo.getType());
        params.put("nickname", otherAccountBindInfo.getNickname());
        params.put("avatar", otherAccountBindInfo.getAvatar());
        if (OTHER_WECHAT.equals(otherAccountBindInfo.getType())) {
            params.put("unionid", otherAccountBindInfo.getUnionId());
        }
        XUtil.Post(url, params, new MyCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                if (loadHud != null) {
                    loadHud.dismiss();
                }
                Gson gson = new Gson();
                AuthorizeSuccessOtherData accountInfo = gson.fromJson(result, AuthorizeSuccessOtherData.class);
                if (accountInfo != null) {
                    OtherAccountBean otherAccountBean = accountInfo.getOtherAccountBean();
                    if (accountInfo.getCode().equals("01") && otherAccountBean != null) {
                        if (otherAccountBean.isMobile_verification()) {
                            showToast(MineLoginActivity.this, R.string.login_success);
//                            绑定JPush
                            bindJPush(otherAccountBean.getUid());
//                           保存个人信息
                            StatConfig.setCustomUserId(MineLoginActivity.this, String.valueOf(otherAccountBean.getUid()));
                            //        友盟统计
                            MobclickAgent.onProfileSignIn(String.valueOf(otherAccountBean.getUid()));

                            SavePersonalInfoBean savePersonalInfo = new SavePersonalInfoBean();
                            savePersonalInfo.setAvatar(getStrings(otherAccountBean.getAvatar()));
                            savePersonalInfo.setNickName(getStrings(otherAccountBean.getNickname()));
                            savePersonalInfo.setUid(otherAccountBean.getUid());
                            savePersonalInfo.setOpenId(getStrings(otherAccountBindInfo.getOpenid()));
                            savePersonalInfo.setLoginType(getStrings(otherAccountBindInfo.getType()));
                            if (OTHER_WECHAT.equals(getStrings(otherAccountBindInfo.getType()))) {
                                savePersonalInfo.setUnionId(getStrings(otherAccountBindInfo.getUnionId()));
                            }
                            savePersonalInfo.setLogin(true);
                            savePersonalInfoCache(MineLoginActivity.this, savePersonalInfo);
//                        小能客服登录
                            loginXNService(MineLoginActivity.this, otherAccountBean.getUid()
                                    , getStrings(otherAccountBean.getNickname())
                                    , "");

//                            跳转传递信息
                            Intent data = new Intent();
                            Bundle bundle = new Bundle();
                            CommunalUserInfoEntity communalUserInfoEntity = new CommunalUserInfoEntity();
                            CommunalUserInfoBean communalUserInfoBean = new CommunalUserInfoBean();
                            communalUserInfoBean.setAvatar(otherAccountBean.getAvatar());
                            communalUserInfoBean.setUid(otherAccountBean.getUid());
                            communalUserInfoBean.setNickname(otherAccountBean.getNickname());
                            communalUserInfoEntity.setCommunalUserInfoBean(communalUserInfoBean);
                            bundle.putParcelable("AccountInf", communalUserInfoEntity);
                            data.putExtras(bundle);
                            setResult(RESULT_OK, data);
                            finish();
                        } else {
//                            跳转绑定手机页面
                            OtherAccountBindInfo accountBind = new OtherAccountBindInfo();
                            accountBind.setAvatar(getStrings(otherAccountBean.getAvatar()));
                            accountBind.setSex(otherAccountBean.getSex());
                            accountBind.setNickname(getStrings(otherAccountBean.getNickname()));
                            accountBind.setType(getStrings(otherAccountBean.getType()));
                            accountBind.setOpenid(getStrings(otherAccountBean.getOpenid()));
                            if (OTHER_WECHAT.equals(getStrings(otherAccountBean.getType()))) {
                                accountBind.setUnionId(getStrings(otherAccountBindInfo.getUnionId()));
                            }
                            accountBind.setMobile_verification(false);
                            Intent intent = new Intent(MineLoginActivity.this, BindingMobileActivity.class);
                            intent.putExtra("info", accountBind);
                            startActivity(intent);
                        }
                    } else {
                        showToast(MineLoginActivity.this, accountInfo.getMsg());
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (loadHud != null) {
                    loadHud.dismiss();
                }
            }
        });
    }

    private void reqSMSCode(String phoneNumber) {
        if (NetWorkUtils.checkNet(MineLoginActivity.this)) {
            String url = Url.BASE_URL + Url.REQ_SEND_SMS_CODE;
            Map<String, Object> params = new HashMap<>();
            String smsType = "login";
            params.put("mobile", phoneNumber);
            params.put("smsType", smsType);
            long currentTimeMillis = System.currentTimeMillis();
            /**
             * 3.1.2加入校验码 避免发送验证码被刷库
             */
//        校验token
            params.put("verify_token", toMD5("" + currentTimeMillis + smsType + phoneNumber + "Domolife2018"));
//        时间戳
            params.put("unixtime", currentTimeMillis);
            XUtil.Post(url, params, new MyCallBack<String>() {
                @Override
                public void onSuccess(String result) {
                    loadHud.dismiss();
                    Gson gson = new Gson();
                    RequestStatus requestStatus = gson.fromJson(result, RequestStatus.class);
                    if (requestStatus != null) {
                        if (requestStatus.getCode().equals("01")) {
                            if (requestStatus.getResult() != null) {
                                if (requestStatus.getResult().getResultCode().equals("01")) {
                                    showToast(MineLoginActivity.this, R.string.GetSmsCodeSuccess);
                                    tv_login_send_code.setVisibility(View.VISIBLE);
                                    reg_login_code_gif_view.setVisibility(View.GONE);
                                    if (countDownHelper == null) {
                                        countDownHelper = CountDownHelper.getTimerInstance();
                                    }
                                    countDownHelper.setSmsCountDown(tv_login_send_code, getResources().getString(R.string.send_sms), 60);
                                } else {
                                    showToast(MineLoginActivity.this, requestStatus.getResult().getResultMsg());
                                }
                            }
                        } else {
                            showToast(MineLoginActivity.this, requestStatus.getMsg());
                        }
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    loadHud.dismiss();
                    showToast(MineLoginActivity.this, R.string.unConnectedNetwork);
                }
            });
        } else {
            showToast(this, R.string.unConnectedNetwork);
            loadHud.dismiss();
        }
    }

    private void checkPhoneCode(String phoneNumber, String smsCode) {
        if (NetWorkUtils.checkNet(MineLoginActivity.this)) {
            String url = Url.BASE_URL + Url.LOGIN_CHECK_SMS_CODE;
            Map<String, Object> params = new HashMap<>();
            params.put("mobile", phoneNumber);
            params.put("checkcode", smsCode);
            params.put("smsType", "login");
            XUtil.Post(url, params, new MyCallBack<String>() {
                @Override
                public void onSuccess(String result) {
                    loadHud.dismiss();
                    Gson gson = new Gson();
                    LoginPhoneCodeEntity loginPhoneCodeEntity = gson.fromJson(result, LoginPhoneCodeEntity.class);
                    if (loginPhoneCodeEntity != null) {
                        if (loginPhoneCodeEntity.getCode().equals("01")) {
                            if (loginPhoneCodeEntity.getLoginPhoneCodeBean() != null) {
                                LoginPhoneCodeBean loginPhoneCodeBean = loginPhoneCodeEntity.getLoginPhoneCodeBean();
                                if (loginPhoneCodeBean.getResultCode().equals("01")) {
                                    CommunalUserInfoEntity communalUserInfoEntity = new CommunalUserInfoEntity();
                                    communalUserInfoEntity.setCommunalUserInfoBean(loginPhoneCodeBean.getCommunalUserInfoBean());
                                    loginSuccessSetData(communalUserInfoEntity);
                                } else {
                                    showToast(MineLoginActivity.this, loginPhoneCodeBean.getResultMsg());
                                }
                            }
                        } else {
                            showToast(MineLoginActivity.this, R.string.unConnectedNetwork);
                        }
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    loadHud.dismiss();
                    showToast(MineLoginActivity.this, R.string.unConnectedNetwork);
                }
            });
        } else {
            showToast(this, R.string.unConnectedNetwork);
            loadHud.dismiss();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (loadHud != null) {
            loadHud.dismiss();
        }
        if (countDownHelper == null) {
            countDownHelper = CountDownHelper.getTimerInstance();
        }
        countDownHelper.setSmsCountDown(tv_login_send_code);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    //  切换登录方式
    @OnClick(R.id.tv_tv_mine_change_login_way)
    void changLoginWay(View view) {
        if (!isPhoneLogin) {
            isPhoneLogin = true;
            ll_account_pas_way.setVisibility(View.GONE);
            ll_mobile_num_way.setVisibility(View.VISIBLE);
            tv_ming_login_forget_password.setVisibility(View.GONE);
            tv_tv_mine_change_login_way.setText("密码登录");
        } else {
            isPhoneLogin = false;
            ll_account_pas_way.setVisibility(View.VISIBLE);
            ll_mobile_num_way.setVisibility(View.GONE);
            tv_ming_login_forget_password.setVisibility(View.VISIBLE);
            tv_tv_mine_change_login_way.setText("验证码登录");
        }
    }

    @OnClick({R.id.ll_layout_weChat, R.id.rImg_login_way_weChat, R.id.tv_login_way_weChat})
    void openWeChat(View view) {
        //弹窗 打开微信
        AlertSettingBean alertSettingBean = new AlertSettingBean();
        AlertSettingBean.AlertData alertData = new AlertSettingBean.AlertData();
        alertData.setCancelStr("取消");
        alertData.setDetermineStr("打开");
        alertData.setFirstDet(true);
        alertData.setMsg("“多么生活”想要打开“微信”");
        alertSettingBean.setStyle(AlertView.Style.Alert);
        alertSettingBean.setAlertData(alertData);
        weChatDialog = new AlertView(alertSettingBean, this, this);
        weChatDialog.setCancelable(true);
        weChatDialog.show();
    }

    @OnClick({R.id.ll_layout_qq, R.id.rImg_login_way_qq, R.id.tv_login_way_qq})
    void openQQ(View view) {
        //弹窗 打开QQ
        AlertSettingBean alertSettingBean = new AlertSettingBean();
        AlertSettingBean.AlertData alertData = new AlertSettingBean.AlertData();
        alertData.setCancelStr("取消");
        alertData.setDetermineStr("打开");
        alertData.setFirstDet(true);
        alertData.setMsg("“多么生活”想要打开“QQ”");
        alertSettingBean.setStyle(AlertView.Style.Alert);
        alertSettingBean.setAlertData(alertData);
        qqDialog = new AlertView(alertSettingBean, this, this);
        qqDialog.setCancelable(true);
        qqDialog.show();
    }

    @OnClick({R.id.ll_layout_weiBo, R.id.rImg_login_way_weiBo, R.id.tv_login_way_weiBo})
    void openSina(View view) {
        //弹窗 打开新浪
        AlertSettingBean alertSettingBean = new AlertSettingBean();
        AlertSettingBean.AlertData alertData = new AlertSettingBean.AlertData();
        alertData.setCancelStr("取消");
        alertData.setDetermineStr("打开");
        alertData.setFirstDet(true);
        alertData.setMsg("“多么生活”想要打开“微博”");
        alertSettingBean.setStyle(AlertView.Style.Alert);
        alertSettingBean.setAlertData(alertData);
        sinaDialog = new AlertView(alertSettingBean, this, this);
        sinaDialog.setCancelable(true);
        sinaDialog.show();
    }

    @OnClick(R.id.iv_blue_close)
    void goBack(View view) {
        finish();
    }

    //    登录
    @OnClick(R.id.tv_mine_Login)
    void loginAccount(View view) {
        if (isPhoneLogin) {
            String phoneNumber = et_login_code_mobile.getText().toString().trim();
            String smsCode = et_login_get_code.getText().toString().trim();
            // 账号登录
            if (phoneNumber.length() == 11 && smsCode.length() > 0) {
                if (loadHud != null) {
                    loadHud.show();
                }
                checkPhoneCode(phoneNumber, smsCode);
            } else if (phoneNumber.length() < 11) {
                showToast(this, R.string.MobileError);
            } else if (smsCode.length() < 1) {
                showToast(this, R.string.SmsCodeNull);
            }
        } else {
            String phoneNumber = edit_login_mobile.getText().toString().trim();
            String password = edit_login_password.getText().toString().trim();
            // 账号登录
            if (phoneNumber.length() == 11 && password.length() > 5) {
                if (loadHud != null) {
                    loadHud.show();
                }
                getData();
            } else if (phoneNumber.length() < 11) {
                showToast(this, R.string.MobileError);
            } else if (password.length() < 6) {
                showToast(this, R.string.PasswordLessSix);
            }
        }
    }

    @OnClick(R.id.tv_ming_login_register)
    void registerAccount(View view) {
        //注册
        Intent intent = new Intent(MineLoginActivity.this, RegisterAccountActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.tv_ming_login_forget_password)
    void forgetPassword(View view) {
        Intent intent = new Intent(MineLoginActivity.this, FoundPasswordActivity.class);
        startActivity(intent);
    }

    //    发送验证码
    @OnClick(R.id.tv_login_send_code)
    void sendLoginCode(View view) {
        String phoneNumber = et_login_code_mobile.getText().toString().trim();
        if (phoneNumber.length() == 11) {
            if (loadHud != null) {
                loadHud.show();
            }
            reqSMSCode(phoneNumber);
        } else if (phoneNumber.length() < 11) {
            showToast(this, "手机号码有错，请重新输入");
        }
    }
}
