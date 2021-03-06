package com.amkj.dmsh.mine.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import com.amkj.dmsh.bean.LoginDataEntity;
import com.amkj.dmsh.bean.RequestStatus;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.PasswordEncrypt;
import com.amkj.dmsh.mine.SmsCodeHelper;
import com.amkj.dmsh.mine.bean.LoginPhoneCodeEntity;
import com.amkj.dmsh.mine.bean.LoginPhoneCodeEntity.LoginPhoneCodeBean;
import com.amkj.dmsh.mine.bean.OtherAccountBindEntity.OtherAccountBindInfo;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.utils.gson.GsonUtils;
import com.amkj.dmsh.views.alertdialog.AlertDialogHelper;
import com.klinker.android.link_builder.Link;
import com.klinker.android.link_builder.LinkBuilder;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareConfig;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import butterknife.BindView;
import butterknife.OnClick;

import static com.amkj.dmsh.constant.ConstantMethod.dismissLoadhud;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.showLoadhud;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.toMD5;
import static com.amkj.dmsh.constant.ConstantVariable.LOGIN;
import static com.amkj.dmsh.constant.ConstantVariable.LOGIN_SUCCESS;
import static com.amkj.dmsh.constant.ConstantVariable.OTHER_QQ;
import static com.amkj.dmsh.constant.ConstantVariable.OTHER_SINA;
import static com.amkj.dmsh.constant.ConstantVariable.OTHER_WECHAT;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.WEB_TYPE_PRIVACY_POLICY;
import static com.amkj.dmsh.constant.ConstantVariable.WEB_TYPE_REG_AGREEMENT;
import static com.amkj.dmsh.constant.ConstantVariable.WEB_VALUE_TYPE;
import static com.amkj.dmsh.constant.Url.LOGIN_ACCOUNT;
import static com.amkj.dmsh.constant.Url.LOGIN_CHECK_SMS_CODE;
import static com.amkj.dmsh.constant.Url.MINE_OTHER_NEW_ACCOUNT;
import static com.amkj.dmsh.constant.Url.REQ_SEND_SMS_CODE;
import static com.amkj.dmsh.dao.UserDao.loginSuccessSetData;
import static com.umeng.socialize.bean.SHARE_MEDIA.QQ;
import static com.umeng.socialize.bean.SHARE_MEDIA.SINA;
import static com.umeng.socialize.bean.SHARE_MEDIA.WEIXIN;


/**
 * @author LGuiPeng
 */
public class MineLoginActivity extends BaseActivity {
    @BindView(R.id.tv_blue_title)
    TextView tv_blue_title;
    @BindView(R.id.iv_blue_back)
    ImageView iv_blue_back;
    @BindView(R.id.header_title_login)
    public LinearLayout Margin_title;
    //    ??????????????????
    @BindView(R.id.tv_ming_login_forget_password)
    public TextView tv_ming_login_forget_password;
    //    ??????????????????
    @BindView(R.id.tv_tv_mine_change_login_way)
    public TextView tv_tv_mine_change_login_way;
    //    ????????????????????????
    @BindView(R.id.ll_account_pas_way)
    public LinearLayout ll_account_pas_way;
    //?????????
    @BindView(R.id.edit_login_mobile)
    public EditText edit_login_mobile;
    //??????
    @BindView(R.id.edit_login_password)
    public EditText edit_login_password;
    //    ???????????????
    @BindView(R.id.ll_mobile_num_way)
    public LinearLayout ll_mobile_num_way;
    //?????????
    @BindView(R.id.et_login_get_code)
    public EditText et_login_get_code;
    //    ???????????????
    @BindView(R.id.tv_login_send_code)
    public TextView tv_login_send_code;
    @BindView(R.id.reg_login_code_gif_view)
    public ProgressBar reg_login_code_gif_view;
    @BindView(R.id.tv_agreement_privacy)
    public TextView tv_agreement_privacy;
    private boolean isPhoneLogin = true;//??????????????????????????????
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
        String s1 = "????????????";
        String s2 = "????????????";
        String text = "???????????????????????????????????????" + s1 + "???" + s2;
        Link link1 = new Link(s1);
        Link link2 = new Link(s2);
        //        @????????????
        link1.setTextColor(Color.parseColor("#0a88fa"));
        link1.setHighlightAlpha(0f);
        link2.setTextColor(Color.parseColor("#0a88fa"));
        link2.setHighlightAlpha(0f);
        LinkBuilder.on(tv_agreement_privacy)
                .setText(text)
                .addLink(link1)
                .addLink(link2)
                .build();
        link1.setOnClickListener(clickedText -> {
            Intent intent = new Intent(MineLoginActivity.this, WebRuleCommunalActivity.class);
            intent.putExtra(WEB_VALUE_TYPE, WEB_TYPE_REG_AGREEMENT);
            startActivity(intent);
        });
        link2.setOnClickListener(clickedText -> {
            Intent intent = new Intent(MineLoginActivity.this, WebRuleCommunalActivity.class);
            intent.putExtra(WEB_VALUE_TYPE, WEB_TYPE_PRIVACY_POLICY);
            startActivity(intent);
        });
    }

    @Override
    protected void loadData() {
    }

    //??????????????????
    public void getData() {
        Map<String, Object> params = new HashMap<>();
        String phoneNumber = edit_login_mobile.getText().toString().trim();
        String password = edit_login_password.getText().toString().trim();
        //?????????
        params.put("mobile", phoneNumber);
//        v3.2.0 ???????????? 1  ??????????????????
        params.put("approve", 1);
        //??????
        params.put("password", PasswordEncrypt.getEncryptedPassword(password));
        NetLoadUtils.getNetInstance().loadNetDataPost(this, LOGIN_ACCOUNT, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                dismissLoadhud(getActivity());
                LoginDataEntity loginDataEntity = GsonUtils.fromJson(result, LoginDataEntity.class);
                if (loginDataEntity != null) {
                    String code = loginDataEntity.getCode();
                    if (SUCCESS_CODE.equals(code)) {
                        loginSuccessSetData(getActivity(), loginDataEntity, null);
                    } else {
                        showToast(loginDataEntity.getMsg());
                    }
                }
            }

            @Override
            public void onNotNetOrException() {
                dismissLoadhud(getActivity());
            }

            @Override
            public void onError(Throwable throwable) {
                showToast(R.string.login_failed);
            }
        });
    }


    //???????????????
    private void reqSMSCode(String phoneNumber) {
        Map<String, Object> params = new HashMap<>();
        params.put("mobile", phoneNumber);
        params.put("smsType", LOGIN);
        long currentTimeMillis = System.currentTimeMillis();
        /**
         * 3.1.2??????????????? ??????????????????????????????
         */
//        ??????token
        params.put("verify_token", toMD5("" + currentTimeMillis + LOGIN + phoneNumber + "Domolife2018"));
//        ?????????
        params.put("unixtime", currentTimeMillis);
        NetLoadUtils.getNetInstance().loadNetDataPost(MineLoginActivity.this, REQ_SEND_SMS_CODE,
                params, new NetLoadListenerHelper() {
                    @Override
                    public void onSuccess(String result) {
                        dismissLoadhud(getActivity());

                        RequestStatus requestStatus = GsonUtils.fromJson(result, RequestStatus.class);
                        if (requestStatus != null) {
                            if (requestStatus.getCode().equals(SUCCESS_CODE)) {
                                RequestStatus.Result resultData = requestStatus.getResult();
                                if (resultData != null) {
                                    if (resultData.getResultCode().equals(SUCCESS_CODE)) {
                                        showToast(R.string.GetSmsCodeSuccess);
                                        tv_login_send_code.setVisibility(View.VISIBLE);
                                        reg_login_code_gif_view.setVisibility(View.GONE);
                                        SmsCodeHelper.startCountDownTimer(getActivity(), tv_login_send_code);
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
                        dismissLoadhud(getActivity());
                    }
                });
    }

    //?????????????????????
    private void checkPhoneCode(String phoneNumber, String smsCode) {
        Map<String, Object> params = new HashMap<>();
        params.put("mobile", phoneNumber);
        params.put("checkcode", smsCode);
        params.put("smsType", LOGIN);
        //        v3.2.0 ???????????? 1  ??????????????????
        params.put("approve", 1);
        NetLoadUtils.getNetInstance().loadNetDataPost(MineLoginActivity.this, LOGIN_CHECK_SMS_CODE,
                params, new NetLoadListenerHelper() {
                    @Override
                    public void onSuccess(String result) {
                        dismissLoadhud(getActivity());

                        LoginPhoneCodeEntity loginPhoneCodeEntity = GsonUtils.fromJson(result, LoginPhoneCodeEntity.class);
                        if (loginPhoneCodeEntity != null) {
                            if (loginPhoneCodeEntity.getCode().equals(SUCCESS_CODE)) {
                                if (loginPhoneCodeEntity.getLoginPhoneCodeBean() != null) {
                                    LoginPhoneCodeBean loginPhoneCodeBean = loginPhoneCodeEntity.getLoginPhoneCodeBean();
                                    if (loginPhoneCodeBean.getResultCode().equals(SUCCESS_CODE)) {
                                        LoginDataEntity loginDataEntity = new LoginDataEntity();
                                        loginDataEntity.setLoginDataBean(loginPhoneCodeBean.getCommunalUserInfoBean());
                                        loginSuccessSetData(getActivity(), loginDataEntity, null);
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
                        dismissLoadhud(getActivity());
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        showException(getResources().getString(R.string.do_failed));
                    }
                });
    }


    //??????????????????
    public void doAuthInfo(String authType) {
        UMShareConfig config = new UMShareConfig();
        config.isNeedAuthOnGetUserInfo(true);
        UMShareAPI.get(this).setShareConfig(config);
        switch (getStrings(authType)) {
            case OTHER_WECHAT:
                // ??????????????????
                SHARE_MEDIA platform = WEIXIN;
                UMShareAPI.get(this).getPlatformInfo(MineLoginActivity.this, platform, getDataInfoListener);
                break;
            case OTHER_QQ:
                //qq??????
                platform = QQ;
                UMShareAPI.get(this).getPlatformInfo(MineLoginActivity.this, platform, getDataInfoListener);
                break;
            case OTHER_SINA:
                //????????????
                platform = SINA;
                UMShareAPI.get(this).getPlatformInfo(MineLoginActivity.this, platform, getDataInfoListener);
                break;
            default:
                showToast("????????????????????????");
                break;
        }
    }

    //????????????
    UMAuthListener getDataInfoListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {
            showLoadhud(getActivity());
        }

        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            if (data != null) {
                final OtherAccountBindInfo info = new OtherAccountBindInfo();
                info.setAccessToken(data.get("accessToken"));
                info.setUnionId(data.get("uid"));
                //openId??????????????????uid?????????????????????????????????"0"
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
            } else {
                dismissLoadhud(getActivity());
            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            showToast("????????????");
            dismissLoadhud(getActivity());
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            showToast("????????????");
            dismissLoadhud(getActivity());
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        dismissLoadhud(getActivity());
        if (resultCode != RESULT_OK) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }


    //????????????????????????
    private void thirdLogin(OtherAccountBindInfo otherAccountBindInfo) {
        Map<String, Object> params = new HashMap<>();
        params.put("type", otherAccountBindInfo.getType());
        params.put("unionid", otherAccountBindInfo.getUnionId());
        params.put("openid", otherAccountBindInfo.getOpenid());
        params.put("accessToken", otherAccountBindInfo.getAccessToken());
        NetLoadUtils.getNetInstance().loadNetDataPost(this, MINE_OTHER_NEW_ACCOUNT, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                LoginDataEntity loginDataEntity = GsonUtils.fromJson(result, LoginDataEntity.class);
                if (loginDataEntity != null && loginDataEntity.getLoginDataBean() != null) {
                    LoginDataEntity.LoginDataBean loginDataBean = loginDataEntity.getLoginDataBean();
                    String code = loginDataEntity.getCode();
                    if (SUCCESS_CODE.equals(code) && !TextUtils.isEmpty(loginDataBean.getToken())) {
                        loginSuccessSetData(getActivity(), loginDataEntity, otherAccountBindInfo);
                    } else if ("39".equals(code)) {
                        //????????????????????????
                        Intent intent = new Intent(MineLoginActivity.this, BindingMobileActivity.class);
                        intent.putExtra("openId", otherAccountBindInfo.getOpenid());
                        intent.putExtra("unionid", otherAccountBindInfo.getUnionId());
                        intent.putExtra("uid", String.valueOf(loginDataBean.getUid()));
                        intent.putExtra("accessToken", otherAccountBindInfo.getAccessToken());
                        intent.putExtra("type", otherAccountBindInfo.getType());
                        startActivity(intent);
                        finish();
                    } else {
                        showToast(loginDataEntity.getMsg());
                    }
                }

                dismissLoadhud(getActivity());
            }

            @Override
            public void onNotNetOrException() {
                dismissLoadhud(getActivity());
            }
        });
    }


    //????????????????????????
    private void showException(String exceptionMsg) {
        if (alertDialogHelper == null) {
            alertDialogHelper = new AlertDialogHelper(MineLoginActivity.this)
                    .setTitle("????????????")
                    .setSingleButton(true)
                    .setTitleGravity(Gravity.CENTER)
                    .setMsg(getStrings(exceptionMsg))
                    .setMsgTextGravity(Gravity.CENTER);
        } else {
            alertDialogHelper.setMsg(getStrings(exceptionMsg));
        }
        alertDialogHelper.show();
    }


    @OnClick({R.id.tv_tv_mine_change_login_way, R.id.rImg_login_way_weChat, R.id.rImg_login_way_qq,
            R.id.rImg_login_way_weiBo, R.id.iv_blue_close, R.id.tv_mine_Login, R.id.tv_ming_login_register, R.id.tv_ming_login_forget_password, R.id.tv_login_send_code})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            //?????????????????????????????????
            case R.id.tv_mine_Login:
                if (isPhoneLogin) {
                    String phoneNumber = edit_login_mobile.getText().toString().trim();
                    String smsCode = et_login_get_code.getText().toString().trim();
                    // ????????????
                    if (phoneNumber.length() == 11 && smsCode.length() > 0) {
                        showLoadhud(getActivity());
                        checkPhoneCode(phoneNumber, smsCode);
                    } else if (phoneNumber.length() < 11) {
                        showToast(R.string.MobileError);
                    } else if (smsCode.length() < 1) {
                        showToast(R.string.SmsCodeNull);
                    }
                } else {
                    String phoneNumber = edit_login_mobile.getText().toString().trim();
                    String password = edit_login_password.getText().toString().trim();
                    // ????????????
                    if (phoneNumber.length() == 11 && password.length() > 5) {
                        showLoadhud(getActivity());
                        getData();
                    } else if (phoneNumber.length() < 11) {
                        showToast(R.string.MobileError);
                    } else if (password.length() < 6) {
                        showToast(R.string.PasswordLessSix);
                    }
                }
                break;
            //????????????
            case R.id.rImg_login_way_weChat:
                if (weChatDialogHelper == null) {
                    weChatDialogHelper = new AlertDialogHelper(MineLoginActivity.this);
                    weChatDialogHelper.setCancelTextColor(getResources().getColor(R.color.text_login_gray_s))
                            .setTitleVisibility(View.GONE).setMsgTextGravity(Gravity.CENTER)
                            .setMsg("??????????????????????????????????????????").setCancelText("??????").setConfirmText("??????")
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
                break;
            //QQ??????
            case R.id.rImg_login_way_qq:
                if (qqDialogHelper == null) {
                    qqDialogHelper = new AlertDialogHelper(MineLoginActivity.this);
                    qqDialogHelper.setCancelTextColor(getResources().getColor(R.color.text_login_gray_s))
                            .setTitleVisibility(View.GONE).setMsgTextGravity(Gravity.CENTER)
                            .setMsg("?????????????????????????????????QQ???").setCancelText("??????").setConfirmText("??????")
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
                break;
            //????????????
            case R.id.rImg_login_way_weiBo:
                if (sinaDialogHelper == null) {
                    sinaDialogHelper = new AlertDialogHelper(this);
                    sinaDialogHelper.setCancelTextColor(getResources().getColor(R.color.text_login_gray_s))
                            .setTitleVisibility(View.GONE).setMsgTextGravity(Gravity.CENTER)
                            .setMsg("??????????????????????????????????????????").setCancelText("??????").setConfirmText("??????")
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
                break;
            //??????????????????
            case R.id.tv_tv_mine_change_login_way:
                if (!isPhoneLogin) {
                    isPhoneLogin = true;
                    ll_account_pas_way.setVisibility(View.GONE);
                    ll_mobile_num_way.setVisibility(View.VISIBLE);
                    tv_tv_mine_change_login_way.setText("??????????????????");
                } else {
                    isPhoneLogin = false;
                    ll_account_pas_way.setVisibility(View.VISIBLE);
                    ll_mobile_num_way.setVisibility(View.GONE);
                    tv_tv_mine_change_login_way.setText("?????????????????????");
                }
                break;
            //????????????
            case R.id.tv_ming_login_register:
                intent = new Intent(MineLoginActivity.this, RegisterAccountActivity.class);
                startActivity(intent);
                break;
            //????????????
            case R.id.tv_ming_login_forget_password:
                intent = new Intent(MineLoginActivity.this, FoundPasswordActivity.class);
                startActivity(intent);
                break;
            //???????????????
            case R.id.tv_login_send_code:
                String phoneNumber = edit_login_mobile.getText().toString().trim();
                if (phoneNumber.length() == 11) {
                    showLoadhud(getActivity());
                    reqSMSCode(phoneNumber);
                } else if (phoneNumber.length() < 11) {
                    showToast("????????????????????????????????????");
                }
                break;
            case R.id.iv_blue_close:
                finish();
                break;
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

    @Override
    protected void onResume() {
        super.onResume();
        dismissLoadhud(getActivity());
    }


    @Override
    protected void postEventResult(@NonNull EventMessage message) {
        //???????????????
        if (LOGIN_SUCCESS.equals(message.type)) {
            if (ConstantMethod.isContextExisted(this)) {
                finish();
            }
        }
    }
}
