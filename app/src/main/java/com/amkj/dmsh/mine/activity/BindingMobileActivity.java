package com.amkj.dmsh.mine.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.bean.CommunalUserInfoEntity;
import com.amkj.dmsh.bean.CommunalUserInfoEntity.CommunalUserInfoBean;
import com.amkj.dmsh.bean.RequestStatus;
import com.amkj.dmsh.constant.PasswordEncrypt;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.mine.CountDownHelper;
import com.amkj.dmsh.mine.bean.OtherAccountBindEntity.OtherAccountBindInfo;
import com.amkj.dmsh.mine.bean.SavePersonalInfoBean;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.utils.NetWorkUtils;
import com.amkj.dmsh.utils.alertdialog.AlertDialogHelper;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

import static com.amkj.dmsh.constant.ConstantMethod.disposeMessageCode;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.savePersonalInfoCache;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantVariable.OTHER_WECHAT;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.Url.MINE_BIND_ACCOUNT_MOBILE;

;

/**
 * 绑定/更换手机
 */
public class BindingMobileActivity extends BaseActivity {
    @BindView(R.id.tv_blue_title)
    TextView tv_blue_title;
    @BindView(R.id.iv_blue_close)
    ImageView iv_blue_close;
    //未绑定手机
    @BindView(R.id.ll_phone_unBind)
    LinearLayout ll_phone_unBind;
    //是否可点击
    @BindView(R.id.tv_bind_mobile_click)
    TextView tv_bind_mobile_click;
    //手机号码
    @BindView(R.id.edit_binding_mobile)
    EditText edit_binding_mobile;
    //点击获取验证码
    @BindView(R.id.tv_bind_send_code)
    TextView tv_bind_send_code;
    //   转动加载图
    @BindView(R.id.reg_bind_code_gif_view)
    ProgressBar reg_bind_code_gif_view;
    //    填写验证码
    @BindView(R.id.edit_get_code)
    EditText edit_get_code;
    //更换手机
    @BindView(R.id.ll_phone_change)
    LinearLayout ll_phone_change;
    //更换手机
    //显示手机号
    @BindView(R.id.tv_bind_mobile_num)
    TextView tv_bind_mobile_num;
    //更换手机号码
    @BindView(R.id.tv_change_mobile_phone_number)
    TextView tv_change_mobile_phone_number;
    //    更换手机 输入新的手机号码
    //    绑定手机设置密码
    @BindView(R.id.ll_bind_mobile_set_password)
    LinearLayout ll_bind_mobile_set_password;
    //    绑定手机设置新密码
    @BindView(R.id.edit_bind_set_password_new)
    EditText edit_bind_set_password_new;

    private String phoneNumber;
    private OtherAccountBindInfo otherAccountBindInfo;
    private boolean isBindMobile;
    private int uid;
    private String newPassword;
    private CountDownHelper countDownHelper;
    private AlertDialogHelper alertDialogHelper;

    @Override
    protected int getContentView() {
        return R.layout.activity_binding_mobile;
    }

    @Override
    protected void initViews() {
        tv_blue_title.setText("绑定手机号");
        iv_blue_close.setVisibility(View.INVISIBLE);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle == null) {
            return;
        }
        otherAccountBindInfo = bundle.getParcelable("info");
        if (otherAccountBindInfo != null) {
            isBindMobile = otherAccountBindInfo.isMobile_verification();
            uid = otherAccountBindInfo.getUid();
        }
        //是否绑定手机
        if (isBindMobile) {
            ll_phone_change.setVisibility(View.VISIBLE);
            tv_bind_mobile_num.setVisibility(View.VISIBLE);
            tv_bind_mobile_num.setText(otherAccountBindInfo.getMobileNum());
            ll_phone_unBind.setVisibility(View.GONE);
            ll_bind_mobile_set_password.setVisibility(View.GONE);
        } else {
            ll_bind_mobile_set_password.setVisibility(View.VISIBLE);
            ll_phone_change.setVisibility(View.GONE);
            ll_phone_unBind.setVisibility(View.VISIBLE);
        }

        //注册短信回调
        SMSSDK.registerEventHandler(new EventHandler() {
            @Override
            public void afterEvent(int event, int result, Object data) {
                Message msg = Message.obtain();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                handler.sendMessage(msg);
            }
        });
    }

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            int event = msg.arg1;
            int result = msg.arg2;
            Object data = msg.obj;
            if (loadHud != null) {
                loadHud.dismiss();
            }
            if (result == SMSSDK.RESULT_COMPLETE) {
                //回调完成
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) { //提交验证码成功
                    //请求服务器
                    if (!isBindMobile) {
                        if (otherAccountBindInfo != null) {
                            setBindMobile();
                        }
                    } else {
                        changeMobileNumber();
                    }
                } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) { //获取验证码成功
                    //请求验证码成功，进入倒计时
                    tv_bind_send_code.setVisibility(View.VISIBLE);
                    reg_bind_code_gif_view.setVisibility(View.GONE);
                    showToast(BindingMobileActivity.this, R.string.GetSmsCodeSuccess);
                    if (countDownHelper == null) {
                        countDownHelper = CountDownHelper.getTimerInstance();
                    }
                    countDownHelper.setSmsCountDown(tv_bind_send_code, getResources().getString(R.string.send_sms), 60);
                } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) { //返回支持发送验证码的国家列表
                }
            } else if (data != null) { //回调失败
                if (loadHud != null) {
                    loadHud.dismiss();
                }
                try {
                    tv_bind_send_code.setVisibility(View.VISIBLE);
                    reg_bind_code_gif_view.setVisibility(View.GONE);
                    Throwable throwable = (Throwable) data;
                    throwable.printStackTrace();
                    JSONObject object = new JSONObject(throwable.getMessage());
                    int status = object.optInt("status");//错误代码
                    disposeMessageCode(BindingMobileActivity.this, status);
                } catch (Exception e) {
                    showToast(BindingMobileActivity.this, R.string.unConnectedNetwork);
                }
            } else {
                showToast(BindingMobileActivity.this, R.string.do_failed);
            }
            return false;
        }
    });


    //  绑定手机
    private void setBindMobile() {
        if (!TextUtils.isEmpty(newPassword)) {
            String encryptedPassword = PasswordEncrypt.getEncryptedPassword(newPassword);
            Map<String, Object> params = new HashMap<>();
            if (uid != 0) {
                params.put("uid", uid);
            }
            params.put("openid", otherAccountBindInfo.getOpenid());
            if (OTHER_WECHAT.equals(otherAccountBindInfo.getType())) {
                params.put("unionid", otherAccountBindInfo.getUnionId());
            }
            params.put("type", otherAccountBindInfo.getType());
            params.put("nickname", otherAccountBindInfo.getNickname());
            params.put("avatar", otherAccountBindInfo.getAvatar());
            params.put("sex", otherAccountBindInfo.getSex());
            params.put("mobile", phoneNumber);
            params.put("password", encryptedPassword);
            NetLoadUtils.getNetInstance().loadNetDataPost(this, MINE_BIND_ACCOUNT_MOBILE, params, new NetLoadListenerHelper() {
                @Override
                public void onSuccess(String result) {
                    if (loadHud != null) {
                        loadHud.dismiss();
                    }
                    Gson gson = new Gson();
                    CommunalUserInfoEntity communalUserInfoEntity = gson.fromJson(result, CommunalUserInfoEntity.class);
                    if (communalUserInfoEntity != null) {
                        String backCode = communalUserInfoEntity.getCode();
                        CommunalUserInfoBean communalUserInfo = communalUserInfoEntity.getCommunalUserInfoBean();
                        if (backCode.equals(SUCCESS_CODE)) {
//                            保存个人信息
                            SavePersonalInfoBean savePersonalInfoBean = new SavePersonalInfoBean();
                            savePersonalInfoBean.setAvatar(getStrings(communalUserInfo.getAvatar()));
                            savePersonalInfoBean.setNickName(getStrings(communalUserInfo.getNickname()));
                            savePersonalInfoBean.setPhoneNum(getStrings(communalUserInfo.getMobile()));
                            savePersonalInfoBean.setUid(communalUserInfo.getUid());
                            savePersonalInfoBean.setLogin(true);
                            if (otherAccountBindInfo != null) {
                                savePersonalInfoBean.setOpenId(getStrings(otherAccountBindInfo.getOpenid()));
                                savePersonalInfoBean.setLoginType(getStrings(otherAccountBindInfo.getType()));
                                if (OTHER_WECHAT.equals(getStrings(otherAccountBindInfo.getType()))) {
                                    savePersonalInfoBean.setUnionId(getStrings(otherAccountBindInfo.getUnionId()));
                                }
                            }
                            EventBus.getDefault().post(new EventMessage("loginShowDialog", ""));
                            savePersonalInfoCache(BindingMobileActivity.this, savePersonalInfoBean);
                            showToast(BindingMobileActivity.this, "绑定成功");
                            Intent intent = new Intent(BindingMobileActivity.this, RegisterSelSexActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            showException(communalUserInfoEntity.getMsg());
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
                public void onError(Throwable throwable) {
                    showException(getResources().getString(R.string.do_failed));
                }

                @Override
                public void netClose() {
                    showToast(BindingMobileActivity.this, R.string.unConnectedNetwork);
                }
            });
        } else {
            showException("请输入新密码！");
        }
    }

    /**
     * 展示后台数据异常
     *
     * @param exceptionMsg
     */
    private void showException(String exceptionMsg) {
        if (alertDialogHelper == null) {
            alertDialogHelper = new AlertDialogHelper(BindingMobileActivity.this)
                    .setTitle("重要提示")
                    .setSingleButton(true)
                    .setTitleGravity(Gravity.CENTER)
                    .setMsg(getStrings(exceptionMsg))
                    .setMsgTextGravity(Gravity.CENTER);
        } else {
            alertDialogHelper.setMsg(getStrings(exceptionMsg));
        }
        edit_get_code.getText().clear();
        alertDialogHelper.show();
    }

    @Override
    protected void loadData() {
    }

    @OnClick({R.id.tv_bind_mobile_click})
    void addCode(View view) {
        if (!isBindMobile) {
            String newPassword = edit_bind_set_password_new.getText().toString().trim();
            if (TextUtils.isEmpty(newPassword)) {
                showToast(this, "请输入新密码");
                return;
            }
            if (newPassword.length() < 5) {
                showToast(this, R.string.PasswordLessSix);
                return;
            }
            if (!PasswordEncrypt.isPwEligibility(newPassword)) {
                showToast(this, R.string.PasswordInconformity);
                return;
            }
            this.newPassword = newPassword;
        }
        final String smsCode = edit_get_code.getText().toString().trim();
        phoneNumber = edit_binding_mobile.getText().toString().trim();
        if (TextUtils.isEmpty(phoneNumber) || phoneNumber.length() != 11) {
            showToast(this, R.string.MobileError);
            return;
        }
        if (TextUtils.isEmpty(smsCode)) {
            showToast(this, R.string.SmsCodeNull);
            return;
        }
        if (NetWorkUtils.isConnectedByState(BindingMobileActivity.this)) {
            if (loadHud != null) {
                loadHud.show();
            }
            SMSSDK.submitVerificationCode("86", phoneNumber, smsCode);
        } else {
            showToast(this, R.string.unConnectedNetwork);
        }

    }

    @OnClick(R.id.tv_bind_send_code)
    void sendSmsCode(View view) {
        phoneNumber = edit_binding_mobile.getText().toString().trim();
        if (phoneNumber.length() == 11) {
            //            请求验证码
            tv_bind_send_code.setVisibility(View.GONE);
            reg_bind_code_gif_view.setVisibility(View.VISIBLE);
            if (NetWorkUtils.isConnectedByState(BindingMobileActivity.this)) {
                SMSSDK.getVerificationCode("86", phoneNumber);
            } else {
                tv_bind_send_code.setVisibility(View.VISIBLE);
                reg_bind_code_gif_view.setVisibility(View.GONE);
                showToast(this, R.string.unConnectedNetwork);
            }
        } else {
            showToast(this, R.string.MobileError);
        }
    }

    //点击更换手机号码
    @OnClick(R.id.tv_change_mobile_phone_number)
    void changePhone(View view) {
        ll_phone_change.setVisibility(View.GONE);
        ll_phone_unBind.setVisibility(View.VISIBLE);
        ll_bind_mobile_set_password.setVisibility(View.GONE);
        tv_bind_mobile_click.setText("确 认");
        tv_blue_title.setText("更换手机");
    }

    private void changeMobileNumber() {
        String newPhoneNumber = edit_binding_mobile.getText().toString().trim();
        String url = Url.BASE_URL + Url.MINE_CHANGE_MOBILE;
        Map<String, Object> params = new HashMap<>();
        params.put("id", uid);
        params.put("oldMobile", otherAccountBindInfo.getMobileNum());
        params.put("newMobile", newPhoneNumber);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, url, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                if (loadHud != null) {
                    loadHud.dismiss();
                }
                Gson gson = new Gson();
                RequestStatus requestStatus = gson.fromJson(result, RequestStatus.class);
                if (requestStatus != null) {
                    if (requestStatus.getCode().equals(SUCCESS_CODE)) {
                        showToast(BindingMobileActivity.this, "更换手机成功");
                        finish();
                    } else {
                        showException(requestStatus.getResult()!=null?requestStatus.getResult().getMsg():requestStatus.getMsg());
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
            public void onError(Throwable throwable) {
                showException(getResources().getString(R.string.do_failed));
            }

            @Override
            public void netClose() {
                showException(getResources().getString(R.string.unConnectedNetwork));
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }

    @OnClick(R.id.iv_blue_back)
    void goBack(View view) {
        finish();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (countDownHelper == null) {
            countDownHelper = CountDownHelper.getTimerInstance();
        }
        countDownHelper.setSmsCountDown(tv_bind_send_code);
    }
}
