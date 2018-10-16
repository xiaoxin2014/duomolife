package com.amkj.dmsh.mine.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.NetLoadUtils;
import com.amkj.dmsh.constant.PasswordEncrypt;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.constant.XUtil;
import com.amkj.dmsh.mine.CountDownHelper;
import com.amkj.dmsh.mine.bean.MinePassword;
import com.amkj.dmsh.mine.bean.RegisterPhoneStatus;
import com.amkj.dmsh.utils.alertdialog.AlertDialogHelper;
import com.amkj.dmsh.utils.inteface.MyCallBack;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

import static com.amkj.dmsh.constant.ConstantMethod.disposeMessageCode;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.Url.BASE_URL;
import static com.amkj.dmsh.constant.Url.CHECK_PHONE_IS_REG;
import static com.amkj.dmsh.utils.NetWorkUtils.isConnectedByState;

;

public class FoundPasswordActivity extends BaseActivity {
    @BindView(R.id.tv_blue_title)
    TextView tv_blue_title;
    @BindView(R.id.iv_blue_close)
    ImageView iv_blue_close;
    @BindView(R.id.tv_bind_mobile_click)
    TextView tv_bind_mobile_click;
    @BindView(R.id.edit_binding_mobile)
    EditText edit_binding_mobile;
    //点击获取验证码
    @BindView(R.id.tv_bind_send_code)
    TextView tv_bind_send_code;
    //   转动加载图
    @BindView(R.id.reg_bind_code_gif_view)
    ProgressBar reg_bind_code_gif_view;
    //获取输入的验证码
    @BindView(R.id.edit_get_code)
    EditText edit_get_code;
    @BindView(R.id.edit_bind_set_password_new)
    EditText edit_bind_set_password_new;
    private String phoneNumber;
    private String password;
    private CountDownHelper countDownHelper;
    private AlertDialogHelper alertDialogHelper;

    @Override
    protected int getContentView() {
        return R.layout.activity_binding_mobile;
    }

    @Override
    protected void initViews() {
        iv_blue_close.setVisibility(View.INVISIBLE);
        tv_bind_mobile_click.setText("完成");
    }

    @Override
    protected void loadData() {
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
            loadHud.dismiss();
            if (result == SMSSDK.RESULT_COMPLETE) {
                //回调完成
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) { //提交验证码成功
                    //请求服务器
                    // 上传新密码
                    ResetPassword(password);
                } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) { //获取验证码成功
                    //请求验证码成功，进入倒计时
                    showToast(FoundPasswordActivity.this, R.string.GetSmsCodeSuccess);
                    tv_bind_send_code.setVisibility(View.VISIBLE);
                    reg_bind_code_gif_view.setVisibility(View.GONE);
                    if(countDownHelper==null){
                        countDownHelper = CountDownHelper.getTimerInstance();
                    }
                    countDownHelper.setSmsCountDown(tv_bind_send_code, getResources().getString(R.string.send_sms), 60);
                } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) { //返回支持发送验证码的国家列表
                }
            } else { //回调失败
                try {
                    tv_bind_send_code.setVisibility(View.VISIBLE);
                    reg_bind_code_gif_view.setVisibility(View.GONE);
                    Throwable throwable = (Throwable) data;
                    throwable.printStackTrace();
                    JSONObject object = new JSONObject(throwable.getMessage());
                    int status = object.optInt("status");//错误代码
                    disposeMessageCode(FoundPasswordActivity.this, status);
                } catch (Exception e) {
                    showToast(FoundPasswordActivity.this, R.string.unConnectedNetwork);
                }
            }
            return false;
        }
    });

    @OnClick(R.id.tv_bind_send_code)
    void sendSmsCode(View view) {
        String phoneNumber = edit_binding_mobile.getText().toString().trim();
        if (phoneNumber.length() == 11) {
//            判断手机号是否注册
            Map<String, Object> params = new HashMap<>();
            params.put("mobile", phoneNumber);
            NetLoadUtils.getQyInstance().loadNetDataPost(this, BASE_URL + CHECK_PHONE_IS_REG,params , new NetLoadUtils.NetLoadListener() {
                @Override
                public void onSuccess(String result) {
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
                        if (status != null && status.getRegisterFlag() == 1) {
                            if (isConnectedByState(FoundPasswordActivity.this)) {
                                //            请求验证码
                                tv_bind_send_code.setVisibility(View.GONE);
                                reg_bind_code_gif_view.setVisibility(View.VISIBLE);
                                SMSSDK.getVerificationCode("86", phoneNumber);
                            } else {
                                showToast(FoundPasswordActivity.this, R.string.unConnectedNetwork);
                            }
                        } else if (status != null && status.getRegisterFlag() != 1) {
                            showToast(FoundPasswordActivity.this, status.getResult());
                        }
                    } else {
                        showToast(FoundPasswordActivity.this, R.string.unConnectedNetwork);
                    }
                }

                @Override
                public void netClose() {
                    showToast(FoundPasswordActivity.this, R.string.unConnectedNetwork);
                }

                @Override
                public void onError(Throwable throwable) {
                    showToast(FoundPasswordActivity.this, R.string.unConnectedNetwork);
                }
            });
        } else if (phoneNumber.length() < 11) {
            showToast(this, "手机号码有误，请重新输入");
        }
    }

    @OnClick(R.id.tv_bind_mobile_click)
    void skipChangePas(View view) {
        phoneNumber = edit_binding_mobile.getText().toString().trim();
        password = edit_bind_set_password_new.getText().toString().trim();
        String msgCode = edit_get_code.getText().toString().trim();
        if (phoneNumber.length() == 11 && !TextUtils.isEmpty(msgCode)) {
            //            提交验证码
            if (isConnectedByState(FoundPasswordActivity.this)) {
                if (loadHud != null) {
                    loadHud.show();
                }
                SMSSDK.submitVerificationCode("86", phoneNumber, msgCode);
            } else {
                showToast(this, R.string.unConnectedNetwork);
            }
        } else {
            if (phoneNumber.length() < 11) {
                showToast(this, R.string.MobileError);
            } else if (TextUtils.isEmpty(msgCode)) {
                showToast(this, R.string.SmsCodeNull);
            }
        }
    }

    //重置密码
    private void ResetPassword(String newPassword) {
        if (loadHud != null) {
            loadHud.show();
        }
        String passwordNewLock = PasswordEncrypt.getEncryptedPassword(newPassword);
        String url = BASE_URL + Url.MINE_RESET_PASSWORD;
        Map<String, Object> params = new HashMap<>();
        params.put("mobile", phoneNumber);
        params.put("newPassword", passwordNewLock);
        XUtil.Post(url, params, new MyCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                if (loadHud != null) {
                    loadHud.dismiss();
                }
                Gson gson = new Gson();
                MinePassword minePassword = gson.fromJson(result, MinePassword.class);
                if (minePassword != null) {
                    if (minePassword.getCode().equals("01")) {
                        SharedPreferences loginStatus = getSharedPreferences("LoginStatus", Context.MODE_PRIVATE);
                        SharedPreferences.Editor edit = loginStatus.edit();
                        edit.putBoolean("isLogin", true);
                        edit.putInt("uid", minePassword.getPasswordBackList().get(0).getId());
                        edit.apply();
                        showToast(FoundPasswordActivity.this, "密码重置成功");
                        //重置密码成功
                        finish();
                    } else {
                        if(EMPTY_CODE.equals(minePassword.getCode())){
                            showException(getResources().getString(R.string.date_exception_hint));
                        }else{
                            showException(minePassword.getMsg());
                        }
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (loadHud != null) {
                    loadHud.dismiss();
                }
                super.onError(ex, isOnCallback);
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
            alertDialogHelper = new AlertDialogHelper(FoundPasswordActivity.this)
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
    @OnClick(R.id.iv_blue_back)
    void goBack(View view) {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterAllEventHandler();
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (countDownHelper==null){
            countDownHelper = CountDownHelper.getTimerInstance();
        }
        countDownHelper.setSmsCountDown(tv_bind_send_code);
    }
}
