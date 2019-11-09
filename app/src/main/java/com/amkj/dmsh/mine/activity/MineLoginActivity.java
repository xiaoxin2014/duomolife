package com.amkj.dmsh.mine.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
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
import com.amkj.dmsh.base.TinkerBaseApplicationLike;
import com.amkj.dmsh.bean.CommunalUserInfoEntity;
import com.amkj.dmsh.bean.CommunalUserInfoEntity.CommunalUserInfoBean;
import com.amkj.dmsh.bean.RequestStatus;
import com.amkj.dmsh.constant.PasswordEncrypt;
import com.amkj.dmsh.mine.CountDownHelper;
import com.amkj.dmsh.mine.bean.LoginPhoneCodeEntity;
import com.amkj.dmsh.mine.bean.LoginPhoneCodeEntity.LoginPhoneCodeBean;
import com.amkj.dmsh.mine.bean.OtherAccountBindEntity.OtherAccountBindInfo;
import com.amkj.dmsh.mine.bean.SavePersonalInfoBean;
import com.amkj.dmsh.mine.bean.ThirdInfoEntity;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.utils.alertdialog.AlertDialogHelper;
import com.google.gson.Gson;
import com.klinker.android.link_builder.Link;
import com.klinker.android.link_builder.LinkBuilder;
import com.tencent.bugly.beta.tinker.TinkerManager;
import com.tencent.stat.StatConfig;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareConfig;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

import static com.amkj.dmsh.constant.ConstantMethod.bindJPush;
import static com.amkj.dmsh.constant.ConstantMethod.getStringChangeIntegers;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.savePersonalInfoCache;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.toMD5;
import static com.amkj.dmsh.constant.ConstantVariable.LOGIN;
import static com.amkj.dmsh.constant.ConstantVariable.OTHER_QQ;
import static com.amkj.dmsh.constant.ConstantVariable.OTHER_SINA;
import static com.amkj.dmsh.constant.ConstantVariable.OTHER_WECHAT;
import static com.amkj.dmsh.constant.ConstantVariable.R_LOGIN_BACK_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.WEB_TYPE_PRIVACY_POLICY;
import static com.amkj.dmsh.constant.ConstantVariable.WEB_TYPE_REG_AGREEMENT;
import static com.amkj.dmsh.constant.ConstantVariable.WEB_VALUE_TYPE;
import static com.amkj.dmsh.constant.Url.LOGIN_ACCOUNT;
import static com.amkj.dmsh.constant.Url.LOGIN_CHECK_SMS_CODE;
import static com.amkj.dmsh.constant.Url.MINE_OTHER_NEW_ACCOUNT;
import static com.amkj.dmsh.constant.Url.REQ_SEND_SMS_CODE;
import static com.umeng.socialize.bean.SHARE_MEDIA.QQ;
import static com.umeng.socialize.bean.SHARE_MEDIA.SINA;
import static com.umeng.socialize.bean.SHARE_MEDIA.WEIXIN;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/3/6
 * class description:登录
 */
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
    @BindView(R.id.tv_agreement_privacy)
    public TextView tv_agreement_privacy;
    private boolean isPhoneLogin = true;//默认为手机验证码登录
    private CountDownHelper countDownHelper;
    private AlertDialogHelper weChatDialogHelper;
    private AlertDialogHelper sinaDialogHelper;
    private AlertDialogHelper qqDialogHelper;
    private AlertDialogHelper alertDialogHelper;
    private LoginFinishReceiver loginFinishReceiver;
    private IntentFilter intentFilter;

    @Override
    protected int getContentView() {
        return R.layout.activity_mine_login;
    }

    @Override
    protected void initViews() {
        tv_blue_title.setVisibility(View.INVISIBLE);
        iv_blue_back.setVisibility(View.INVISIBLE);
        loadHud.setCancellable(true);
        String s1 = "注册协议";
        String s2 = "隐私政策";
        String text = "登录即表示同意多么生活用户" + s1 + "和" + s2;
        Link link1 = new Link(s1);
        Link link2 = new Link(s2);
        //        @用户昵称
        link1.setTextColor(Color.parseColor("#0a88fa"));
        link1.setHighlightAlpha(0f);
        link2.setTextColor(Color.parseColor("#0a88fa"));
        link2.setHighlightAlpha(0f);
        LinkBuilder.on(tv_agreement_privacy)
                .setText(text)
                .addLink(link1)
                .addLink(link2)
                .build();
        link1.setOnClickListener(new Link.OnClickListener() {
            @Override
            public void onClick(String clickedText) {
                Intent intent = new Intent(MineLoginActivity.this, WebRuleCommunalActivity.class);
                intent.putExtra(WEB_VALUE_TYPE, WEB_TYPE_REG_AGREEMENT);
                startActivity(intent);
            }
        });
        link2.setOnClickListener(new Link.OnClickListener() {
            @Override
            public void onClick(String clickedText) {
                Intent intent = new Intent(MineLoginActivity.this, WebRuleCommunalActivity.class);
                intent.putExtra(WEB_VALUE_TYPE, WEB_TYPE_PRIVACY_POLICY);
                startActivity(intent);
            }
        });
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
        Map<String, Object> params = new HashMap<>();
        String phoneNumber = edit_login_mobile.getText().toString().trim();
        String password = edit_login_password.getText().toString().trim();
        //手机号
        params.put("mobile", phoneNumber);
//        v3.2.0 加入参数 1  同意协议政策
        params.put("approve", 1);
        //密码
        params.put("password", PasswordEncrypt.getEncryptedPassword(password));
        NetLoadUtils.getNetInstance().loadNetDataPost(this, LOGIN_ACCOUNT, params, new NetLoadListenerHelper() {
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
            public void onNotNetOrException() {
                if (loadHud != null) {
                    loadHud.dismiss();
                }
            }

            @Override
            public void netClose() {
                showToast(MineLoginActivity.this, R.string.unConnectedNetwork);
            }

            @Override
            public void onError(Throwable throwable) {
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
        savePersonalInfo.setToken(getStrings(communalUserInfoBean.getToken()));
        savePersonalInfo.setTokenExpireSeconds(System.currentTimeMillis() + communalUserInfoBean.getTokenExpireSeconds());
        savePersonalInfoCache(MineLoginActivity.this, savePersonalInfo);
        resultForBackData(communalUserInfoEntity);
    }

    /**
     * 登录信息回调信息
     *
     * @param communalUserInfoEntity
     */
    private void resultForBackData(CommunalUserInfoEntity communalUserInfoEntity) {
        if (communalUserInfoEntity != null && communalUserInfoEntity.getCommunalUserInfoBean() != null) {
            Intent data = new Intent();
            Bundle bundle = new Bundle();
            bundle.putParcelable("AccountInf", communalUserInfoEntity);
            data.putExtras(bundle);
            setResult(RESULT_OK, data);
        }
        finish();
        overridePendingTransition(0, 0);
    }

    /**
     * 授权类型
     *
     * @param authType
     */
    public void doAuthInfo(String authType) {
        UMShareConfig config = new UMShareConfig();
        config.isNeedAuthOnGetUserInfo(true);
        UMShareAPI.get(this).setShareConfig(config);
        switch (getStrings(authType)) {
            case OTHER_WECHAT:
                // 打开微信授权
                SHARE_MEDIA platform = WEIXIN;
                UMShareAPI.get(this).getPlatformInfo(MineLoginActivity.this, platform, getDataInfoListener);
                break;
            case OTHER_QQ:
                //qq授权
                platform = QQ;
                UMShareAPI.get(this).getPlatformInfo(MineLoginActivity.this, platform, getDataInfoListener);
                break;
            case OTHER_SINA:
                //新浪授权
                platform = SINA;
                UMShareAPI.get(this).getPlatformInfo(MineLoginActivity.this, platform, getDataInfoListener);
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
                info.setAccessToken(data.get("accessToken"));
                info.setUnionId(data.get("uid"));
                //openId三端都要传，uid只有微信有值，其他的传"0"
                switch (platform) {
                    case WEIXIN:
                        info.setType(OTHER_WECHAT);
                        info.setOpenid(data.get("openid"));
                        thirdLogin(info);
                        break;
                    case QQ:
                        info.setType(OTHER_QQ);
                        info.setOpenid(data.get("uid"));
                        thirdLogin(info);
                        break;
                    case SINA:
                        info.setType(OTHER_SINA);
                        info.setOpenid(data.get("uid"));
                        thirdLogin(info);
                        break;
                }
            }else {
                if (loadHud != null) {
                    loadHud.dismiss();
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
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    private void thirdLogin(OtherAccountBindInfo otherAccountBindInfo) {
        Map<String, Object> params = new HashMap<>();
        params.put("type", otherAccountBindInfo.getType());
        params.put("unionid", otherAccountBindInfo.getUnionId());
        params.put("openid", otherAccountBindInfo.getOpenid());
        params.put("accessToken", otherAccountBindInfo.getAccessToken());
        NetLoadUtils.getNetInstance().loadNetDataPost(this, MINE_OTHER_NEW_ACCOUNT, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                ThirdInfoEntity accountInfo = gson.fromJson(result, ThirdInfoEntity.class);
                if (accountInfo != null && accountInfo.getThirdInfoBean() != null) {
                    ThirdInfoEntity.ThirdInfoBean thirdInfoBean = accountInfo.getThirdInfoBean();
                    if (accountInfo.getCode().equals(SUCCESS_CODE) && !TextUtils.isEmpty(thirdInfoBean.getToken())) {
                        showToast(MineLoginActivity.this, R.string.login_success);
                        //绑定JPush
                        bindJPush(thirdInfoBean.getUid());
                        StatConfig.setCustomUserId(MineLoginActivity.this, thirdInfoBean.getUid());
                        //友盟统计
                        MobclickAgent.onProfileSignIn(thirdInfoBean.getUid());
                        //保存个人信息
                        SavePersonalInfoBean savePersonalInfo = new SavePersonalInfoBean();
                        savePersonalInfo.setLogin(true);
                        savePersonalInfo.setUid(getStringChangeIntegers(thirdInfoBean.getUid()));
                        savePersonalInfo.setLoginType(otherAccountBindInfo.getType());
                        savePersonalInfo.setOpenId(otherAccountBindInfo.getOpenid());
                        savePersonalInfo.setUnionId(otherAccountBindInfo.getUnionId());
                        savePersonalInfo.setAccessToken(otherAccountBindInfo.getAccessToken());
                        savePersonalInfo.setToken(thirdInfoBean.getToken());
                        savePersonalInfo.setTokenExpireSeconds(System.currentTimeMillis() + thirdInfoBean.getTokenExpireSeconds());
                        savePersonalInfoCache(MineLoginActivity.this, savePersonalInfo);
                        //跳转传递信息
                        Intent data = new Intent();
                        Bundle bundle = new Bundle();
                        CommunalUserInfoEntity communalUserInfoEntity = new CommunalUserInfoEntity();
                        CommunalUserInfoBean communalUserInfoBean = new CommunalUserInfoBean();
                        communalUserInfoBean.setUid(thirdInfoBean.getUid());
                        communalUserInfoEntity.setCommunalUserInfoBean(communalUserInfoBean);
                        bundle.putParcelable("AccountInf", communalUserInfoEntity);
                        data.putExtras(bundle);
                        setResult(RESULT_OK, data);
                        finish();
                    } else if ("39".equals(accountInfo.getCode())) {
                        //跳转绑定手机页面
                        Intent intent = new Intent(MineLoginActivity.this, BindingMobileActivity.class);
                        intent.putExtra("openId", otherAccountBindInfo.getOpenid());
                        intent.putExtra("unionid", otherAccountBindInfo.getUnionId());
                        intent.putExtra("uid", thirdInfoBean.getUid());
                        intent.putExtra("accessToken", otherAccountBindInfo.getAccessToken());
                        intent.putExtra("type", otherAccountBindInfo.getType());
                        startActivity(intent);
                    } else {
                        showToast(MineLoginActivity.this, accountInfo.getMsg());
                    }
                }

                if (loadHud != null) {
                    loadHud.dismiss();
                }
            }

            @Override
            public void onNotNetOrException() {
                if (loadHud != null) {
                    loadHud.dismiss();
                }
            }
        });
    }

    private void reqSMSCode(String phoneNumber) {
        Map<String, Object> params = new HashMap<>();
        params.put("mobile", phoneNumber);
        params.put("smsType", LOGIN);
        long currentTimeMillis = System.currentTimeMillis();
        /**
         * 3.1.2加入校验码 避免发送验证码被刷库
         */
//        校验token
        params.put("verify_token", toMD5("" + currentTimeMillis + LOGIN + phoneNumber + "Domolife2018"));
//        时间戳
        params.put("unixtime", currentTimeMillis);
        NetLoadUtils.getNetInstance().loadNetDataPost(MineLoginActivity.this, REQ_SEND_SMS_CODE,
                params, new NetLoadListenerHelper() {
                    @Override
                    public void onSuccess(String result) {
                        loadHud.dismiss();
                        Gson gson = new Gson();
                        RequestStatus requestStatus = gson.fromJson(result, RequestStatus.class);
                        if (requestStatus != null) {
                            if (requestStatus.getCode().equals(SUCCESS_CODE)) {
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
                                        showException(resultData.getResultMsg());
                                    }
                                }
                            } else {
                                showException(requestStatus.getResult() != null ? requestStatus.getResult().getResultMsg() : requestStatus.getMsg());
                            }
                        }
                    }

                    @Override
                    public void onNotNetOrException() {
                        loadHud.dismiss();
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
        Map<String, Object> params = new HashMap<>();
        params.put("mobile", phoneNumber);
        params.put("checkcode", smsCode);
        params.put("smsType", LOGIN);
        //        v3.2.0 加入参数 1  同意协议政策
        params.put("approve", 1);
        NetLoadUtils.getNetInstance().loadNetDataPost(MineLoginActivity.this, LOGIN_CHECK_SMS_CODE,
                params, new NetLoadListenerHelper() {
                    @Override
                    public void onSuccess(String result) {
                        loadHud.dismiss();
                        Gson gson = new Gson();
                        LoginPhoneCodeEntity loginPhoneCodeEntity = gson.fromJson(result, LoginPhoneCodeEntity.class);
                        if (loginPhoneCodeEntity != null) {
                            if (loginPhoneCodeEntity.getCode().equals(SUCCESS_CODE)) {
                                if (loginPhoneCodeEntity.getLoginPhoneCodeBean() != null) {
                                    LoginPhoneCodeBean loginPhoneCodeBean = loginPhoneCodeEntity.getLoginPhoneCodeBean();
                                    if (loginPhoneCodeBean.getResultCode().equals(SUCCESS_CODE)) {
                                        CommunalUserInfoEntity communalUserInfoEntity = new CommunalUserInfoEntity();
                                        communalUserInfoEntity.setCommunalUserInfoBean(loginPhoneCodeBean.getCommunalUserInfoBean());
                                        loginSuccessSetData(communalUserInfoEntity);
                                    } else {
                                        showException(loginPhoneCodeBean.getResultMsg());
                                    }
                                }
                            } else {
                                showException(loginPhoneCodeEntity.getMsg());
                            }
                        }
                    }

                    @Override
                    public void onNotNetOrException() {
                        loadHud.dismiss();
                    }

                    @Override
                    public void netClose() {
                        showToast(MineLoginActivity.this, R.string.unConnectedNetwork);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        showException(getResources().getString(R.string.do_failed));
                    }
                });
    }

    //  切换登录方式
    @OnClick(R.id.tv_tv_mine_change_login_way)
    void changLoginWay(View view) {
        if (!isPhoneLogin) {
            isPhoneLogin = true;
            ll_account_pas_way.setVisibility(View.GONE);
            ll_mobile_num_way.setVisibility(View.VISIBLE);
            tv_tv_mine_change_login_way.setText("账号密码登录");
        } else {
            isPhoneLogin = false;
            ll_account_pas_way.setVisibility(View.VISIBLE);
            ll_mobile_num_way.setVisibility(View.GONE);
            tv_tv_mine_change_login_way.setText("短信验证码登录");
        }
    }

    @OnClick({R.id.rImg_login_way_weChat})
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

    @OnClick({R.id.rImg_login_way_qq})
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

    @OnClick({R.id.rImg_login_way_weiBo})
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
        try {
            return super.dispatchTouchEvent(ev);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginFinishReceiver = new LoginFinishReceiver();
        intentFilter = new IntentFilter();
        intentFilter.addAction(R_LOGIN_BACK_CODE);
        LocalBroadcastManager.getInstance(this).registerReceiver(loginFinishReceiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(loginFinishReceiver);
        if (qqDialogHelper != null && qqDialogHelper.isShowing()) {
            qqDialogHelper.dismiss();
        }
        if (weChatDialogHelper != null && weChatDialogHelper.isShowing()) {
            weChatDialogHelper.dismiss();
        }
        if (sinaDialogHelper != null && sinaDialogHelper.isShowing()) {
            sinaDialogHelper.dismiss();
        }
        if (alertDialogHelper != null && alertDialogHelper.isShowing()) {
            alertDialogHelper.dismiss();
        }
    }

    class LoginFinishReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context arg0, Intent arg1) {
            try {
                Bundle extras = arg1.getExtras();
                CommunalUserInfoEntity communalUserInfoEntity = (CommunalUserInfoEntity) extras.getParcelable("AccountInf");
                TinkerBaseApplicationLike tinkerBaseApplicationLike = (TinkerBaseApplicationLike) TinkerManager.getTinkerApplicationLike();
                tinkerBaseApplicationLike.finishToCurrentPage(MineLoginActivity.class.getName());
                resultForBackData(communalUserInfoEntity);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
