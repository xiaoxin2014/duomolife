package com.amkj.dmsh.mine.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.bean.LoginDataEntity;
import com.amkj.dmsh.constant.PasswordEncrypt;
import com.amkj.dmsh.constant.Sha1Md5;
import com.amkj.dmsh.dao.UserDao;
import com.amkj.dmsh.mine.CountDownHelper;
import com.amkj.dmsh.mine.bean.RegisterPhoneStatus;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.utils.NetWorkUtils;
import com.amkj.dmsh.utils.alertdialog.AlertDialogHelper;
import com.google.gson.Gson;
import com.klinker.android.link_builder.Link;
import com.klinker.android.link_builder.LinkBuilder;

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
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.WEB_TYPE_PRIVACY_POLICY;
import static com.amkj.dmsh.constant.ConstantVariable.WEB_TYPE_REG_AGREEMENT;
import static com.amkj.dmsh.constant.ConstantVariable.WEB_VALUE_TYPE;
import static com.amkj.dmsh.constant.Url.CHECK_PHONE_IS_REG;
import static com.amkj.dmsh.constant.Url.USER_REGISTER_ACCOUNT;


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
    public TextView bt_mine_reg;
    @BindView(R.id.tv_register_agreement_privacy)
    public TextView tv_register_agreement_privacy;
    @BindView(R.id.reg_req_code_gif_view)
    public ProgressBar reg_req_code_gif_view;
    private boolean flag;
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
        String s1 = "注册协议";
        String s2 = "隐私政策";
        String text = "注册即表示同意多么生活用户" + s1 + "和" + s2;
        Link link1 = new Link(s1);
        Link link2 = new Link(s2);
        //        @用户昵称
        link1.setTextColor(Color.parseColor("#0a88fa"));
        link1.setHighlightAlpha(0f);
        link2.setTextColor(Color.parseColor("#0a88fa"));
        link2.setHighlightAlpha(0f);
        LinkBuilder.on(tv_register_agreement_privacy)
                .setText(text)
                .addLink(link1)
                .addLink(link2)
                .build();
        link1.setOnClickListener(new Link.OnClickListener() {
            @Override
            public void onClick(String clickedText) {
                Intent intent = new Intent(RegisterAccountActivity.this, WebRuleCommunalActivity.class);
                intent.putExtra(WEB_VALUE_TYPE, WEB_TYPE_REG_AGREEMENT);
                startActivity(intent);
            }
        });
        link2.setOnClickListener(new Link.OnClickListener() {
            @Override
            public void onClick(String clickedText) {
                Intent intent = new Intent(RegisterAccountActivity.this, WebRuleCommunalActivity.class);
                intent.putExtra(WEB_VALUE_TYPE, WEB_TYPE_PRIVACY_POLICY);
                startActivity(intent);
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
                    showToast( R.string.GetSmsCodeSuccess);
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
                    disposeMessageCode(status);
                } catch (Exception e) {
                    showToast(R.string.unConnectedNetwork);
                }
            } else {
                showToast( R.string.do_failed);
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
        //        v3.2.0 加入参数 1  同意协议政策
        params.put("approve", 1);
        NetLoadUtils.getNetInstance().loadNetDataPost(RegisterAccountActivity.this, USER_REGISTER_ACCOUNT,
                params, new NetLoadListenerHelper() {
                    @Override
                    public void onSuccess(String result) {
                        bt_mine_reg.setEnabled(true);
                        if (loadHud != null) {
                            loadHud.dismiss();
                        }
                        Gson gson = new Gson();
                        LoginDataEntity loginDataEntity = gson.fromJson(result, LoginDataEntity.class);
                        if (loginDataEntity != null) {
                            String code = loginDataEntity.getCode();
                            if (SUCCESS_CODE.equals(code)) {
                                //跳转选择性别
                                Intent intent = new Intent(RegisterAccountActivity.this, RegisterSelSexActivity.class);
                                startActivity(intent);
                                //保存个人信息
                                UserDao.loginSuccessSetData(getActivity(), loginDataEntity, null);
                            } else {
                                showException(loginDataEntity.getMsg());
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
            showToast( R.string.MobileError);
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
                            showToast(R.string.unConnectedNetwork);
                        }
                    } else if (status != null && status.getRegisterFlag() == 1) {
                        showToast(status.getResult());
                    }
                } else {
                    showToast( R.string.unConnectedNetwork);
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
                showToast(R.string.do_failed);
            }
        });
    }

    @OnClick(R.id.bt_mine_reg)
    void addAccount(View view) {
        final String phone = edit_register_mobile.getText().toString().trim();
        final String smsCode = edit_register_sms_code.getText().toString().trim();
        final String password = edit_register_password.getText().toString().trim();
        if (phone.length() != 11) {
            showToast(R.string.MobileError);
            return;
        }
        if (TextUtils.isEmpty(smsCode)) {
            showToast(R.string.SmsCodeNull);
            return;
        }
        if (TextUtils.isEmpty(password) || password.length() < 6) {
            showToast( R.string.PasswordLessSix);
            return;
        }
        if (!PasswordEncrypt.isPwEligibility(password)) {
            showToast( R.string.PasswordInconformity);
            return;
        }
        if (!NetWorkUtils.isConnectedByState(RegisterAccountActivity.this)) {
            showToast(R.string.unConnectedNetwork);
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
