package com.amkj.dmsh.mine.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.base.NetLoadUtils;
import com.amkj.dmsh.bean.CommunalUserInfoEntity;
import com.amkj.dmsh.bean.CommunalUserInfoEntity.CommunalUserInfoBean;
import com.amkj.dmsh.bean.RequestStatus;
import com.amkj.dmsh.constant.Sha1Md5;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.constant.XUtil;
import com.amkj.dmsh.mine.CountDownHelper;
import com.amkj.dmsh.mine.bean.AuthorizeSuccessOtherData;
import com.amkj.dmsh.mine.bean.AuthorizeSuccessOtherData.OtherAccountBean;
import com.amkj.dmsh.mine.bean.LoginPhoneCodeEntity;
import com.amkj.dmsh.mine.bean.LoginPhoneCodeEntity.LoginPhoneCodeBean;
import com.amkj.dmsh.mine.bean.OtherAccountBindEntity.OtherAccountBindInfo;
import com.amkj.dmsh.mine.bean.SavePersonalInfoBean;
import com.amkj.dmsh.utils.alertdialog.AlertDialogHelper;
import com.amkj.dmsh.utils.inteface.MyCallBack;
import com.amkj.dmsh.views.SystemBarHelper;
import com.google.gson.Gson;
import com.tencent.stat.StatConfig;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

import static com.amkj.dmsh.constant.ConstantMethod.bindJPush;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.savePersonalInfoCache;
import static com.amkj.dmsh.constant.ConstantMethod.setDeviceInfo;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.toMD5;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.OTHER_QQ;
import static com.amkj.dmsh.constant.ConstantVariable.OTHER_SINA;
import static com.amkj.dmsh.constant.ConstantVariable.OTHER_WECHAT;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.umeng.socialize.bean.SHARE_MEDIA.QQ;
import static com.umeng.socialize.bean.SHARE_MEDIA.SINA;
import static com.umeng.socialize.bean.SHARE_MEDIA.WEIXIN;

;

public class MineLoginActivity extends BaseActivity {
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
    private UMShareAPI mShareAPI;
    private boolean isPhoneLogin;
    private CountDownHelper countDownHelper;
    private AlertDialogHelper weChatDialogHelper;
    private AlertDialogHelper sinaDialogHelper;
    private AlertDialogHelper qqDialogHelper;
    private AlertDialogHelper alertDialogHelper;

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
        NetLoadUtils.getQyInstance().loadNetDataPost(this, url, params, new NetLoadUtils.NetLoadListener() {
            @Override
            public void onSuccess(String result) {
                if (loadHud != null) {
                    loadHud.dismiss();
                }
                Gson gson = new Gson();
                CommunalUserInfoEntity loginAccount = gson.fromJson(result, CommunalUserInfoEntity.class);
                if (loginAccount != null) {
                    String backCode = loginAccount.getCode();
                    if (backCode.equals(SUCCESS_CODE)) {
                        loginSuccessSetData(loginAccount);
                    } else {
                        showToast(MineLoginActivity.this, loginAccount.getMsg());
                    }
                }
            }

            @Override
            public void netClose() {
                if (loadHud != null) {
                    loadHud.dismiss();
                }
                showToast(MineLoginActivity.this, R.string.unConnectedNetwork);
            }

            @Override
            public void onError(Throwable throwable) {
                if (loadHud != null) {
                    loadHud.dismiss();
                }
                showToast(MineLoginActivity.this, R.string.login_failed);
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
//        保存个人信息
        SavePersonalInfoBean savePersonalInfo = new SavePersonalInfoBean();
        savePersonalInfo.setAvatar(getStrings(communalUserInfoBean.getAvatar()));
        savePersonalInfo.setNickName(getStrings(communalUserInfoBean.getNickname()));
        savePersonalInfo.setPhoneNum(getStrings(communalUserInfoBean.getMobile()));
        savePersonalInfo.setUid(communalUserInfoBean.getUid());
        savePersonalInfo.setLogin(true);
        EventBus.getDefault().post(new EventMessage("loginShowDialog", ""));
        savePersonalInfoCache(MineLoginActivity.this, savePersonalInfo);
// 上传设备信息
        setDeviceInfo(this, communalUserInfoBean.getApp_version_no()
                , communalUserInfoBean.getDevice_model()
                , communalUserInfoBean.getDevice_sys_version()
                , communalUserInfoBean.getSysNotice());
        Intent data = new Intent();
        Bundle bundle = new Bundle();
        bundle.putParcelable("AccountInf", communalUserInfoEntity);
        data.putExtras(bundle);
        setResult(RESULT_OK, data);
        finish();
        overridePendingTransition(0, 0);
    }

    /**
     * 授权类型
     *
     * @param authType
     */
    public void doAuthInfo(String authType) {
        if (mShareAPI == null) {
            mShareAPI = UMShareAPI.get(this);
        }
        switch (getStrings(authType)) {
            case OTHER_WECHAT:
                // 打开微信授权
                SHARE_MEDIA platform = WEIXIN;
                mShareAPI.getPlatformInfo(MineLoginActivity.this, platform, getDataInfoListener);
                break;
            case OTHER_QQ:
                //qq授权
                platform = QQ;
                mShareAPI.getPlatformInfo(MineLoginActivity.this, platform, getDataInfoListener);
                break;
            case OTHER_SINA:
                //新浪授权
                platform = SINA;
                mShareAPI.getPlatformInfo(MineLoginActivity.this, platform, getDataInfoListener);
                break;
            default:
                showToast(MineLoginActivity.this, "暂不支持该授权！");
                break;
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
                            EventBus.getDefault().post(new EventMessage("loginShowDialog", ""));
                            savePersonalInfo.setLogin(true);
                            savePersonalInfo.setPhoneNum(getStrings(otherAccountBindInfo.getMobile()));
                            savePersonalInfoCache(MineLoginActivity.this, savePersonalInfo);
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
        NetLoadUtils.getQyInstance().loadNetDataPost(MineLoginActivity.this, url, params, new NetLoadUtils.NetLoadListener() {
            @Override
            public void onSuccess(String result) {
                loadHud.dismiss();
                Gson gson = new Gson();
                RequestStatus requestStatus = gson.fromJson(result, RequestStatus.class);
                if (requestStatus != null) {
                    if (requestStatus.getCode().equals("01")) {
                        RequestStatus.Result resultData = requestStatus.getResult();
                        if (resultData != null) {
                            if (resultData.getResultCode().equals(SUCCESS_CODE)) {
                                showToast(MineLoginActivity.this, R.string.GetSmsCodeSuccess);
                                tv_login_send_code.setVisibility(View.VISIBLE);
                                reg_login_code_gif_view.setVisibility(View.GONE);
                                if (countDownHelper == null) {
                                    countDownHelper = CountDownHelper.getTimerInstance();
                                }
                                countDownHelper.setSmsCountDown(tv_login_send_code, getResources().getString(R.string.send_sms), 60);
                            } else {
                                if (EMPTY_CODE.equals(resultData.getCode())) {
                                    showException(getResources().getString(R.string.date_exception_hint));
                                } else {
                                    showException(resultData.getMsg());
                                }
                            }
                        }
                    } else {
                        if (EMPTY_CODE.equals(requestStatus.getCode())) {
                            showException(getResources().getString(R.string.date_exception_hint));
                        } else {
                            showException(requestStatus.getMsg());
                        }
                    }
                }
            }

            @Override
            public void netClose() {
                showToast(MineLoginActivity.this, R.string.unConnectedNetwork);
                loadHud.dismiss();
            }

            @Override
            public void onError(Throwable throwable) {
                loadHud.dismiss();
                showException(getResources().getString(R.string.date_exception_hint));
            }
        });
    }

    /**
     * 展示后台数据异常
     *
     * @param exceptionMsg
     */
    private void showException(String exceptionMsg) {
        if (alertDialogHelper == null) {
            alertDialogHelper = new AlertDialogHelper(MineLoginActivity.this)
                    .setTitle("重要提示")
                    .setSingleButton(true)
                    .setTitleGravity(Gravity.CENTER)
                    .setMsg(getStrings(exceptionMsg))
                    .setMsgTextGravity(Gravity.CENTER);
        } else {
            alertDialogHelper.setMsg(getStrings(exceptionMsg));
        }
        alertDialogHelper.show();
    }

    private void checkPhoneCode(String phoneNumber, String smsCode) {
        String url = Url.BASE_URL + Url.LOGIN_CHECK_SMS_CODE;
        Map<String, Object> params = new HashMap<>();
        params.put("mobile", phoneNumber);
        params.put("checkcode", smsCode);
        params.put("smsType", "login");
        NetLoadUtils.getQyInstance().loadNetDataPost(MineLoginActivity.this, url, params, new NetLoadUtils.NetLoadListener() {
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
                                if (EMPTY_CODE.equals(loginPhoneCodeBean.getResultCode())) {
                                    showException(getResources().getString(R.string.date_exception_hint));
                                } else {
                                    showException(loginPhoneCodeBean.getResultMsg());
                                }
                            }
                        }
                    } else {
                        if (EMPTY_CODE.equals(loginPhoneCodeEntity.getCode())) {
                            showException(getResources().getString(R.string.date_exception_hint));
                        } else {
                            showException(loginPhoneCodeEntity.getMsg());
                        }
                    }
                }
            }

            @Override
            public void netClose() {
                loadHud.dismiss();
                showToast(MineLoginActivity.this, R.string.unConnectedNetwork);
            }

            @Override
            public void onError(Throwable throwable) {
                loadHud.dismiss();
                showException(getResources().getString(R.string.date_exception_hint));
            }
        });
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
        if (weChatDialogHelper == null) {
            weChatDialogHelper = new AlertDialogHelper(MineLoginActivity.this);
            weChatDialogHelper.setCancelTextColor(getResources().getColor(R.color.text_login_gray_s))
                    .setTitleVisibility(View.GONE).setMsgTextGravity(Gravity.CENTER)
                    .setMsg("“多么生活”想要打开“微信”").setCancelText("取消").setConfirmText("打开")
                    .setAlertListener(new AlertDialogHelper.AlertConfirmCancelListener() {
                        @Override
                        public void confirm() {
                            doAuthInfo(OTHER_WECHAT);
                        }

                        @Override
                        public void cancel() {
                        }
                    });
        }
        weChatDialogHelper.show();
    }

    @OnClick({R.id.ll_layout_qq, R.id.rImg_login_way_qq, R.id.tv_login_way_qq})
    void openQQ(View view) {
        if (qqDialogHelper == null) {
            qqDialogHelper = new AlertDialogHelper(MineLoginActivity.this);
            qqDialogHelper.setCancelTextColor(getResources().getColor(R.color.text_login_gray_s))
                    .setTitleVisibility(View.GONE).setMsgTextGravity(Gravity.CENTER)
                    .setMsg("“多么生活”想要打开“QQ”").setCancelText("取消").setConfirmText("打开")
                    .setAlertListener(new AlertDialogHelper.AlertConfirmCancelListener() {
                        @Override
                        public void confirm() {
                            doAuthInfo(OTHER_QQ);
                        }

                        @Override
                        public void cancel() {
                        }
                    });
        }
        qqDialogHelper.show();
    }

    @OnClick({R.id.ll_layout_weiBo, R.id.rImg_login_way_weiBo, R.id.tv_login_way_weiBo})
    void openSina(View view) {
        if (sinaDialogHelper == null) {
            sinaDialogHelper = new AlertDialogHelper(this);
            sinaDialogHelper.setCancelTextColor(getResources().getColor(R.color.text_login_gray_s))
                    .setTitleVisibility(View.GONE).setMsgTextGravity(Gravity.CENTER)
                    .setMsg("“多么生活”想要打开“微博”").setCancelText("取消").setConfirmText("打开")
                    .setAlertListener(new AlertDialogHelper.AlertConfirmCancelListener() {
                        @Override
                        public void confirm() {
                            doAuthInfo(OTHER_SINA);
                        }

                        @Override
                        public void cancel() {
                        }
                    });
        }
        sinaDialogHelper.show();
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

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                InputMethodManager imm =
                        (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS
                );
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (qqDialogHelper != null && qqDialogHelper.isShowing()) {
            qqDialogHelper.dismiss();
        }
        if (weChatDialogHelper != null && weChatDialogHelper.isShowing()) {
            weChatDialogHelper.dismiss();
        }
        if (sinaDialogHelper != null && sinaDialogHelper.isShowing()) {
            sinaDialogHelper.dismiss();
        }
        if (alertDialogHelper != null&&alertDialogHelper.isShowing()) {
            alertDialogHelper.dismiss();
        }
    }
}
