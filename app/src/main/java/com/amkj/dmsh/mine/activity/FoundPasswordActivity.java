package com.amkj.dmsh.mine.activity;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.constant.PasswordEncrypt;
import com.amkj.dmsh.mine.CountDownHelper;
import com.amkj.dmsh.mine.bean.MinePassword;
import com.amkj.dmsh.mine.bean.RegisterPhoneStatus;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.utils.alertdialog.AlertDialogHelper;
import com.amkj.dmsh.utils.gson.GsonUtils;

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
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.Url.CHECK_PHONE_IS_REG;
import static com.amkj.dmsh.constant.Url.MINE_RESET_PASSWORD;
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
        return R.layout.activity_forget_pwd;
    }

    @Override
    protected void initViews() {
        iv_blue_close.setVisibility(View.INVISIBLE);
        tv_bind_mobile_click.setText("完成");
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

    @Override
    protected void loadData() {

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
                    resetPassword(password);
                } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) { //获取验证码成功
                    //请求验证码成功，进入倒计时
                    showToast( R.string.GetSmsCodeSuccess);
                    tv_bind_send_code.setVisibility(View.VISIBLE);
                    reg_bind_code_gif_view.setVisibility(View.GONE);
                    if (countDownHelper == null) {
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
                    disposeMessageCode( status);
                } catch (Exception e) {
                    showToast( R.string.unConnectedNetwork);
                }
            }
            return false;
        }
    });

    @OnClick(R.id.tv_bind_send_code)
    void sendSmsCode(View view) {
        String phoneNumber = edit_binding_mobile.getText().toString().trim();
        if (phoneNumber.length() != 11) {
            showToast(R.string.MobileError);
            return;
        }
//            判断手机号是否注册
        Map<String, Object> params = new HashMap<>();
        params.put("mobile", phoneNumber);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, CHECK_PHONE_IS_REG,
                params, new NetLoadListenerHelper() {
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

                            RegisterPhoneStatus status = GsonUtils.fromJson(result, RegisterPhoneStatus.class);
                            if (status != null && status.getRegisterFlag() == 1) {
                                if (isConnectedByState(FoundPasswordActivity.this)) {
                                    //            请求验证码
                                    tv_bind_send_code.setVisibility(View.GONE);
                                    reg_bind_code_gif_view.setVisibility(View.VISIBLE);
                                    SMSSDK.getVerificationCode("86", phoneNumber);
                                } else {
                                    showToast(R.string.unConnectedNetwork);
                                }
                            } else if (status != null && status.getRegisterFlag() != 1) {
                                showToast(status.getResult());
                            }
                        } else {
                            showToast(R.string.unConnectedNetwork);
                        }
                    }


                    @Override
                    public void onError(Throwable throwable) {
                        showToast( R.string.do_failed);
                    }
                });
    }

    @OnClick(R.id.tv_bind_mobile_click)
    void skipChangePas(View view) {
        phoneNumber = edit_binding_mobile.getText().toString().trim();
        password = edit_bind_set_password_new.getText().toString().trim();
        String msgCode = edit_get_code.getText().toString().trim();
        if (phoneNumber.length() < 11) {
            showToast(R.string.MobileError);
            return;
        }
        if (TextUtils.isEmpty(msgCode)) {
            showToast( R.string.SmsCodeNull);
            return;
        }
        if (!isConnectedByState(FoundPasswordActivity.this)) {
            showToast(R.string.unConnectedNetwork);
            return;
        }

        if (setPasswordRule(password)) return;

        if (loadHud != null) {
            loadHud.show();
        }
        SMSSDK.submitVerificationCode("86", phoneNumber, msgCode);
    }

    //重置密码
    private void resetPassword(String newPassword) {
        if (setPasswordRule(newPassword)) return;
        if (loadHud != null) {
            loadHud.show();
        }
        String passwordNewLock = PasswordEncrypt.getEncryptedPassword(newPassword);
        Map<String, Object> params = new HashMap<>();
        params.put("mobile", phoneNumber);
        params.put("newPassword", passwordNewLock);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, MINE_RESET_PASSWORD, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                if (loadHud != null) {
                    loadHud.dismiss();
                }

                MinePassword minePassword = GsonUtils.fromJson(result, MinePassword.class);
                if (minePassword != null) {
                    if (minePassword.getCode().equals(SUCCESS_CODE)) {
                        showToast("密码重置成功");
                        finish();
                    } else {
                        showException(minePassword.getMsg());
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
        });
    }

    private boolean setPasswordRule(String newPassword) {
        if (TextUtils.isEmpty(newPassword)) {
            showToast("请输入新密码");
            return true;
        }
        if (newPassword.length() < 5) {
            showToast( R.string.PasswordLessSix);
            return true;
        }
        if (!PasswordEncrypt.isPwEligibility(newPassword)) {
            showToast( R.string.PasswordInconformity);
            return true;
        }
        return false;
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
        if (countDownHelper == null) {
            countDownHelper = CountDownHelper.getTimerInstance();
        }
        countDownHelper.setSmsCountDown(tv_bind_send_code);
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
