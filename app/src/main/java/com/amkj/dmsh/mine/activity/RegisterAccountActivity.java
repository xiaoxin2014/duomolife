package com.amkj.dmsh.mine.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.bean.RegisterUserInfoEntity;
import com.amkj.dmsh.bean.RegisterUserInfoEntity.RegisterUserInfoBean;
import com.amkj.dmsh.constant.PasswordEncrypt;
import com.amkj.dmsh.constant.Sha1Md5;
import com.amkj.dmsh.mine.CountDownHelper;
import com.amkj.dmsh.mine.bean.RegisterPhoneStatus;
import com.amkj.dmsh.mine.bean.SavePersonalInfoBean;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.utils.NetWorkUtils;
import com.amkj.dmsh.utils.alertdialog.AlertDialogHelper;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

import static com.amkj.dmsh.constant.ConstantMethod.disposeMessageCode;
import static com.amkj.dmsh.constant.ConstantMethod.getDeviceId;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.savePersonalInfoCache;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.Url.CHECK_PHONE_IS_REG;
import static com.amkj.dmsh.constant.Url.USER_REGISTER_ACCOUNT;

;
/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/3/6
 * class description:注册账号
 */
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
    private AlertDialogHelper alertDialogHelper;

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

    //  提交信息
    private void getData(String phoneNumber, String password) {
        Sha1Md5 sha1Md5 = new Sha1Md5();
        Map<String, Object> params = new HashMap<>();
        params.put("mobile", phoneNumber);
        params.put("newPassword", PasswordEncrypt.getEncryptedPassword(password));
        long currentTimeMillis = System.currentTimeMillis();
        String uniqueId = getDeviceId(this);
        /**
         * 3.1.2加入校验码 避免注册被刷库
         */
//        校验token
        params.put("verify_token", sha1Md5.getMD5("" + currentTimeMillis + phoneNumber + uniqueId + "Domolife2018"));
//        时间戳
        params.put("unixtime", currentTimeMillis);
//        设备号
        params.put("device_number", uniqueId);
        NetLoadUtils.getNetInstance().loadNetDataPost(RegisterAccountActivity.this, USER_REGISTER_ACCOUNT,
                params, new NetLoadListenerHelper() {
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
                            if (backCode.equals(SUCCESS_CODE)) {
//                        保存个人信息
                                SavePersonalInfoBean savePersonalInfoBean = new SavePersonalInfoBean();
                                savePersonalInfoBean.setAvatar(getStrings(registerUserInfoBean.getAvatar()));
                                savePersonalInfoBean.setNickName(getStrings(registerUserInfoBean.getNickname()));
                                savePersonalInfoBean.setPhoneNum(getStrings(registerUserInfoBean.getMobile()));
                                savePersonalInfoBean.setUid(registerUserInfoBean.getUid());
                                savePersonalInfoBean.setLogin(true);
                                EventBus.getDefault().post(new EventMessage("loginShowDialog", ""));
                                savePersonalInfoCache(RegisterAccountActivity.this, savePersonalInfoBean);
                                Intent intent = new Intent(RegisterAccountActivity.this, RegisterSelSexActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                showException(registerUserInfoEntity.getMsg());
                            }
                        }
                    }

                    @Override
                    public void onNotNetOrException() {
                        bt_mine_reg.setEnabled(true);
                        if (loadHud != null) {
                            loadHud.dismiss();
                        }
                    }

                    @Override
                    public void netClose() {
                        showToast(RegisterAccountActivity.this, R.string.unConnectedNetwork);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        showException(getResources().getString(R.string.do_failed));
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
            alertDialogHelper = new AlertDialogHelper(RegisterAccountActivity.this)
                    .setTitle("重要提示")
                    .setSingleButton(true)
                    .setTitleGravity(Gravity.CENTER)
                    .setMsg(getStrings(exceptionMsg))
                    .setMsgTextGravity(Gravity.CENTER);
        } else {
            alertDialogHelper.setMsg(getStrings(exceptionMsg));
        }
        edit_register_sms_code.getText().clear();
        alertDialogHelper.show();
    }

    @Override
    protected void loadData() {
    }

    //请求验证码
    @OnClick(R.id.tv_sms_code)
    void sendCode(View view) {
        String phoneNumber = edit_register_mobile.getText().toString().trim();
        isPhoneReg(phoneNumber);
    }

    /**
     * 判断手机号码是否已注册
     *
     * @param phoneNumber
     */
    private void isPhoneReg(final String phoneNumber) {
        if (phoneNumber.length() != 11) {
            showToast(this, R.string.MobileError);
            return;
        }
        if (loadHud != null) {
            loadHud.show();
        }
        Map<String, Object> params = new HashMap<>();
        params.put("mobile", phoneNumber);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, CHECK_PHONE_IS_REG, params, new NetLoadListenerHelper() {
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
                if (code.equals(SUCCESS_CODE)) {
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
            public void onNotNetOrException() {
                if (loadHud != null) {
                    loadHud.dismiss();
                }
            }

            @Override
            public void onError(Throwable throwable) {
                showToast(RegisterAccountActivity.this, R.string.do_failed);
            }

            @Override
            public void netClose() {
                showToast(RegisterAccountActivity.this, R.string.unConnectedNetwork);
            }
        });
    }

    @OnClick(R.id.bt_mine_reg)
    void addAccount(View view) {
        final String phone = edit_register_mobile.getText().toString().trim();
        final String smsCode = edit_register_sms_code.getText().toString().trim();
        final String password = edit_register_password.getText().toString().trim();
        if (phone.length() != 11) {
            showToast(this, R.string.MobileError);
            return;
        }
        if (TextUtils.isEmpty(smsCode)) {
            showToast(this, R.string.SmsCodeNull);
            return;
        }
        if (TextUtils.isEmpty(password) || password.length() < 6) {
            showToast(this, R.string.PasswordLessSix);
            return;
        }
        if(!PasswordEncrypt.isPwEligibility(password)){
            showToast(this, R.string.PasswordInconformity);
            return;
        }
        if (!NetWorkUtils.isConnectedByState(RegisterAccountActivity.this)) {
            showToast(this, R.string.unConnectedNetwork);
            return;
        }
        if (loadHud != null) {
            loadHud.show();
        }
        bt_mine_reg.setEnabled(false);
        SMSSDK.submitVerificationCode("86", phone, smsCode);
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
}
