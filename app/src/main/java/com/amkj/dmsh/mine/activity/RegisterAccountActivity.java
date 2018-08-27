package com.amkj.dmsh.mine.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.bean.RegisterUserInfoEntity;
import com.amkj.dmsh.bean.RegisterUserInfoEntity.RegisterUserInfoBean;
import com.amkj.dmsh.constant.Sha1Md5;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.constant.XUtil;
import com.amkj.dmsh.mine.CountDownHelper;
import com.amkj.dmsh.mine.bean.RegisterPhoneStatus;
import com.amkj.dmsh.mine.bean.SavePersonalInfoBean;
import com.amkj.dmsh.utils.NetWorkUtils;
import com.amkj.dmsh.utils.inteface.MyCallBack;
import com.amkj.dmsh.views.SystemBarHelper;
import com.google.gson.Gson;
import com.tencent.stat.StatConfig;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

import static com.amkj.dmsh.constant.ConstantMethod.bindJPush;
import static com.amkj.dmsh.constant.ConstantMethod.disposeMessageCode;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.getUniqueId;
import static com.amkj.dmsh.constant.ConstantMethod.loginXNService;
import static com.amkj.dmsh.constant.ConstantMethod.savePersonalInfoCache;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;

;

public class RegisterAccountActivity extends BaseActivity {
    @BindView(R.id.ll_layout_reg)
    public LinearLayout ll_layout_reg;
    @BindView(R.id.edit_register_mobile)
    public EditText edit_register_mobile;
    @BindView(R.id.edit_register_password)
    public EditText edit_register_password;
    //短信验证码
    @BindView(R.id.edit_register_sms_code)
    public EditText edit_register_sms_code;
    @BindView(R.id.tv_sms_code)
    public TextView tv_sms_code;
    @BindView(R.id.bt_mine_reg)
    public Button bt_mine_reg;
    @BindView(R.id.reg_req_code_gif_view)
    public ProgressBar reg_req_code_gif_view;
    private boolean flag;
    private RegisterUserInfoBean registerUserInfoBean;
    private CountDownHelper countDownHelper;

    @Override
    protected int getContentView() {
        return R.layout.activity_register_account;
    }

    @Override
    protected void initViews() {
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
            if (result == SMSSDK.RESULT_COMPLETE) {
                //回调完成
//                showToast(RegisterAccountActivity.this, "回调完成");
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) { //提交验证码成功
                    //请求服务器
//                    showToast(RegisterAccountActivity.this, "提交成功");
                    getData(edit_register_mobile.getText().toString().trim(), edit_register_password.getText().toString().trim());
                } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) { //获取验证码成功
                    showToast(RegisterAccountActivity.this, R.string.GetSmsCodeSuccess);
                    tv_sms_code.setVisibility(View.VISIBLE);
                    reg_req_code_gif_view.setVisibility(View.GONE);
                    if (countDownHelper == null) {
                        countDownHelper = CountDownHelper.getTimerInstance();
                    }
                    countDownHelper.setSmsCountDown(tv_sms_code, getResources().getString(R.string.send_sms), 60);
                } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) { //返回支持发送验证码的国家列表
                }
            } else if (data != null) { //回调失败
                bt_mine_reg.setEnabled(true);
                reg_req_code_gif_view.setVisibility(View.GONE);
                if (loadHud != null) {
                    loadHud.dismiss();
                }
                try {
                    tv_sms_code.setVisibility(View.VISIBLE);
                    reg_req_code_gif_view.setVisibility(View.GONE);
                    Throwable throwable = (Throwable) data;
                    throwable.printStackTrace();
                    JSONObject object = new JSONObject(throwable.getMessage());
                    int status = object.optInt("status");//错误代码
                    disposeMessageCode(RegisterAccountActivity.this, status);
                } catch (Exception e) {
                    showToast(RegisterAccountActivity.this, R.string.unConnectedNetwork);
                }
            } else {
                showToast(RegisterAccountActivity.this, R.string.do_failed);
            }
            return false;
        }
    });

    @Override
    public void setStatusColor() {
        super.setStatusColor();
        SystemBarHelper.setPadding(RegisterAccountActivity.this, ll_layout_reg);
        SystemBarHelper.immersiveStatusBar(RegisterAccountActivity.this);
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

    //  提交信息
    private void getData(String phoneNumber, String password) {
        Sha1Md5 sha1Md5 = new Sha1Md5();
        final String t = "g{faJ&)H<34rz(q*\"C0S=Xy`TW~ZGD.wt6_1j@dU";
        String passwordLock = sha1Md5.getMD5(toLowerCase(sha1Md5.getDigestOfString(password.getBytes()).getBytes()) + t);
        String url = Url.BASE_URL + Url.USER_REGISTER_ACCOUNT;
        Map<String, Object> params = new HashMap<>();
        params.put("mobile", phoneNumber);
        params.put("newPassword", passwordLock);
        long currentTimeMillis = System.currentTimeMillis();
        String uniqueId = getUniqueId(this);
        /**
         * 3.1.2加入校验码 避免注册被刷库
         */
//        校验token
        params.put("verify_token", sha1Md5.getMD5("" + currentTimeMillis + phoneNumber + uniqueId + "Domolife2018"));
//        时间戳
        params.put("unixtime", currentTimeMillis);
//        设备号
        params.put("device_number", uniqueId);
        XUtil.Post(url, params, new MyCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                bt_mine_reg.setEnabled(true);
                if (loadHud != null) {
                    loadHud.dismiss();
                }
                Gson gson = new Gson();
                RegisterUserInfoEntity registerUserInfoEntity = gson.fromJson(result, RegisterUserInfoEntity.class);
                if (registerUserInfoEntity != null) {
                    String backCode = registerUserInfoEntity.getCode();
                    registerUserInfoBean = registerUserInfoEntity.getRegisterUserInfoBean();
                    if (backCode.equals("01")) {
//                        保存个人信息
                        SavePersonalInfoBean savePersonalInfoBean = new SavePersonalInfoBean();
                        savePersonalInfoBean.setAvatar(getStrings(registerUserInfoBean.getAvatar()));
                        savePersonalInfoBean.setNickName(getStrings(registerUserInfoBean.getNickname()));
                        savePersonalInfoBean.setPhoneNum(getStrings(registerUserInfoBean.getMobile()));
                        savePersonalInfoBean.setUid(registerUserInfoBean.getUid());
                        savePersonalInfoBean.setLogin(true);
                        savePersonalInfoCache(RegisterAccountActivity.this, savePersonalInfoBean);
//                        移动分析账号统计
                        StatConfig.setCustomUserId(RegisterAccountActivity.this, String.valueOf(registerUserInfoBean.getUid()));
//        友盟统计
                        MobclickAgent.onProfileSignIn(String.valueOf(registerUserInfoBean.getUid()));
                        //                            绑定JPush
                        bindJPush(registerUserInfoBean.getUid());
//                        小能客服登录
                        loginXNService(RegisterAccountActivity.this, registerUserInfoBean.getUid()
                                , getStrings(registerUserInfoBean.getNickname())
                                , getStrings(registerUserInfoBean.getMobile()));
//                        QyServiceUtils.getQyInstance().loginQyUserInfo(RegisterAccountActivity.this,registerUserInfoBean.getUid(),registerUserInfoBean.getNickname(),registerUserInfoBean.getMobile(),registerUserInfoBean.getAvatar());
                        Intent intent = new Intent(RegisterAccountActivity.this, RegisterSelSexActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        showToast(RegisterAccountActivity.this, registerUserInfoEntity.getMsg());
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                bt_mine_reg.setEnabled(true);
                if (loadHud != null) {
                    loadHud.dismiss();
                }
                showToast(RegisterAccountActivity.this, R.string.unConnectedNetwork);
            }
        });
    }

    @Override
    protected void loadData() {
    }

    //请求验证码
    @OnClick(R.id.tv_sms_code)
    void sendCode(View view) {
        String phoneNumber = edit_register_mobile.getText().toString().trim();
        if (phoneNumber.length() == 11) {
//            判断该号码是否已注册
            isPhoneReg(phoneNumber);
        } else {
            showToast(this, R.string.MobileError);
        }
    }

    private void isPhoneReg(final String phoneNumber) {
        if (NetWorkUtils.isConnectedByState(RegisterAccountActivity.this)) {
            if (loadHud != null) {
                loadHud.show();
            }
            String url = Url.BASE_URL + Url.CHECK_PHONE_IS_REG;
            Map<String, Object> params = new HashMap<>();
            params.put("mobile", phoneNumber);
            XUtil.Post(url, params, new MyCallBack<String>() {
                @Override
                public void onSuccess(String result) {
                    if (loadHud != null) {
                        loadHud.dismiss();
                    }
                    String code = "";
                    String msg = "";
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        code = (String) jsonObject.get("code");
                        msg = (String) jsonObject.get("msg");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (code.equals("01")) {
                        Gson gson = new Gson();
                        RegisterPhoneStatus status = gson.fromJson(result, RegisterPhoneStatus.class);
                        if (status != null && status.getRegisterFlag() == 0) {
                            //            请求验证码
                            tv_sms_code.setVisibility(View.GONE);
                            reg_req_code_gif_view.setVisibility(View.VISIBLE);
                            if (NetWorkUtils.isConnectedByState(RegisterAccountActivity.this)) {
                                SMSSDK.getVerificationCode("86", phoneNumber);
                            } else {
                                tv_sms_code.setVisibility(View.VISIBLE);
                                reg_req_code_gif_view.setVisibility(View.GONE);
                                showToast(RegisterAccountActivity.this, R.string.unConnectedNetwork);
                            }
                        } else if (status != null && status.getRegisterFlag() == 1) {
                            showToast(RegisterAccountActivity.this, status.getResult());
                        }
                    } else {
                        showToast(RegisterAccountActivity.this, R.string.unConnectedNetwork);
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    if (loadHud != null) {
                        loadHud.dismiss();
                    }
                    showToast(RegisterAccountActivity.this, R.string.unConnectedNetwork);
                }
            });
        } else {
            showToast(this, R.string.unConnectedNetwork);
        }
    }

    @OnClick(R.id.bt_mine_reg)
    void addAccount(View view) {
        bt_mine_reg.setEnabled(false);
        final String smsCode = edit_register_sms_code.getText().toString().trim();
        final String password = edit_register_password.getText().toString().trim();
        final String phone = edit_register_mobile.getText().toString().trim();
        if (password.length() > 5 && !TextUtils.isEmpty(smsCode)) {
//            提交验证码
            if (NetWorkUtils.isConnectedByState(RegisterAccountActivity.this)) {
                if (loadHud != null) {
                    loadHud.show();
                }
                SMSSDK.submitVerificationCode("86", phone, smsCode);
            } else {
                showToast(this, R.string.unConnectedNetwork);
                bt_mine_reg.setEnabled(true);
            }
        } else {
            bt_mine_reg.setEnabled(true);
            if (phone.length() < 11) {
                showToast(this, R.string.MobileError);
            } else if (TextUtils.isEmpty(smsCode)) {
                showToast(this, R.string.SmsCodeNull);
            } else if (TextUtils.isEmpty(password)) {
                showToast(this, R.string.PasswordLessSix);
            }
        }
    }

    @OnClick(R.id.iv_password_code_visibility)
    void isVisibility(View view) {
        if (flag) {
            edit_register_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            Editable etAble = edit_register_password.getText();
            Selection.setSelection(etAble, etAble.length());
            flag = false;
        } else {
            edit_register_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            Editable etAble = edit_register_password.getText();
            Selection.setSelection(etAble, etAble.length());
            flag = true;
        }
    }

    @OnClick(R.id.tv_reg_login)
    void goBack(View view) {
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (countDownHelper == null) {
            countDownHelper = CountDownHelper.getTimerInstance();
        }
        countDownHelper.setSmsCountDown(tv_sms_code);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterAllEventHandler();
    }
}
