package com.amkj.dmsh.mine.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.bean.RequestStatus;
import com.amkj.dmsh.mine.CountDownHelper;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.utils.KeyboardUtils;
import com.amkj.dmsh.utils.NetWorkUtils;
import com.amkj.dmsh.utils.alertdialog.AlertDialogHelper;
import com.amkj.dmsh.utils.gson.GsonUtils;

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
import static com.amkj.dmsh.constant.Url.MINE_CHANGE_MOBILE;

;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/8/9
 * class description:更改手机号
 */
public class ChangeMobileActivity extends BaseActivity {
    @BindView(R.id.tv_header_shared)
    TextView tv_header_shared;
    @BindView(R.id.tv_header_title)
    TextView tv_header_titleAll;
    //    获取验证码
    @BindView(R.id.tv_change_send_code)
    TextView tv_change_send_code;
    //    当前绑定手机
    @BindView(R.id.tv_mine_per_cm)
    TextView tv_mine_per_cm;
    //    更换手机
    @BindView(R.id.et_change_mobile)
    EditText et_change_mobile;
    //    验证码
    @BindView(R.id.edit_cm_get_code)
    EditText edit_cm_get_code;
    //    等待窗口
    @BindView(R.id.reg_change_code_gif_view)
    ProgressBar reg_change_code_gif_view;
    private String uid;
    private String oldMobile;
    private String phoneNumber;
    private CountDownHelper countDownHelper;
    private AlertDialogHelper alertDialogHelper;

    @Override
    protected int getContentView() {
        return R.layout.activity_change_mobile;
    }

    @Override
    protected void initViews() {
        tv_header_titleAll.setVisibility(View.GONE);
        tv_header_shared.setCompoundDrawables(null, null, null, null);
        tv_header_shared.setText("完成");
        Intent intent = getIntent();
        if (getIntent() != null) {
            uid= intent.getStringExtra("uid");
            oldMobile= intent.getStringExtra("mobile");
        } else {
            finish();
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
        tv_mine_per_cm.setText("当前绑定 " + oldMobile);
        KeyboardUtils.registerSoftInputChangedListener(this, new KeyboardUtils.OnSoftInputChangedListener() {
            @Override
            public void onSoftInputChanged(int height) {
                if (height == 0) {
                    ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0).requestFocus();
                }
            }
        });
    }

    @OnClick({R.id.tv_header_shared})
    void addCode(View view) {
        final String smsCode = edit_cm_get_code.getText().toString().trim();
        phoneNumber = et_change_mobile.getText().toString().trim();
        if (!TextUtils.isEmpty(smsCode) && !TextUtils.isEmpty(phoneNumber)) {
            if (phoneNumber.length() == 11) {
                //            提交验证码
                if (loadHud != null) {
                    loadHud.show();
                }
                if (NetWorkUtils.isConnectedByState(ChangeMobileActivity.this)) {
                    SMSSDK.submitVerificationCode("86", phoneNumber, smsCode);
                } else {
                    showToast( R.string.unConnectedNetwork);
                }
            } else {
                showToast( R.string.MobileError);
            }
        } else if (TextUtils.isEmpty(phoneNumber)) {
            showToast( R.string.MobileError);
        } else {
            showToast(R.string.SmsCodeNull);
        }

    }

    //    发送验证码
    @OnClick(R.id.tv_change_send_code)
    void sendSmsCode(View view) {
        phoneNumber = et_change_mobile.getText().toString().trim();
        if (phoneNumber.length() == 11) {
            //            请求验证码
            tv_change_send_code.setVisibility(View.GONE);
            reg_change_code_gif_view.setVisibility(View.VISIBLE);
            if (NetWorkUtils.isConnectedByState(ChangeMobileActivity.this)) {
                SMSSDK.getVerificationCode("86", phoneNumber);
            } else {
                tv_change_send_code.setVisibility(View.VISIBLE);
                reg_change_code_gif_view.setVisibility(View.GONE);
                showToast( R.string.unConnectedNetwork);
            }
        } else {
            showToast( R.string.MobileError);
        }
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
                    changeMobileNumber();
                } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) { //获取验证码成功
                    //请求验证码成功，进入倒计时
                    tv_change_send_code.setVisibility(View.VISIBLE);
                    reg_change_code_gif_view.setVisibility(View.GONE);
                    showToast( R.string.GetSmsCodeSuccess);
                    if (countDownHelper == null) {
                        countDownHelper = CountDownHelper.getTimerInstance();
                    }
                    countDownHelper.setSmsCountDown(tv_change_send_code, getResources().getString(R.string.send_sms), 60);
                } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) { //返回支持发送验证码的国家列表
                }
            } else if (data != null) { //回调失败
                if (loadHud != null) {
                    loadHud.dismiss();
                }
                try {
                    tv_change_send_code.setVisibility(View.VISIBLE);
                    reg_change_code_gif_view.setVisibility(View.GONE);
                    Throwable throwable = (Throwable) data;
                    throwable.printStackTrace();
                    JSONObject object = new JSONObject(throwable.getMessage());
                    int status = object.optInt("status");//错误代码
                    disposeMessageCode( status);
                } catch (Exception e) {
                    showToast(R.string.unConnectedNetwork);
                }
            } else {
                showToast(R.string.do_failed);
            }
            return false;
        }
    });

    private void changeMobileNumber() {
        String newPhoneNumber = et_change_mobile.getText().toString().trim();
        Map<String, Object> params = new HashMap<>();
        params.put("id", uid);
        params.put("oldMobile", oldMobile);
        params.put("newMobile", newPhoneNumber);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, MINE_CHANGE_MOBILE, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                if (loadHud != null) {
                    loadHud.dismiss();
                }

                RequestStatus requestStatus = GsonUtils.fromJson(result, RequestStatus.class);
                if (requestStatus != null) {
                    if (requestStatus.getCode().equals(SUCCESS_CODE)) {
                        showToast("更换手机成功");
                        finish();
                    } else {
                        showException(requestStatus.getResult() != null ? requestStatus.getResult().getResultMsg() : requestStatus.getMsg());
                    }
                }
            }

            @Override
            public void onError(Throwable throwable) {
                showException(getResources().getString(R.string.do_failed));
            }

            @Override
            public void onNotNetOrException() {
                if (loadHud != null) {
                    loadHud.dismiss();
                }
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
            alertDialogHelper = new AlertDialogHelper(ChangeMobileActivity.this)
                    .setTitle("重要提示")
                    .setSingleButton(true)
                    .setTitleGravity(Gravity.CENTER)
                    .setMsg(getStrings(exceptionMsg))
                    .setMsgTextGravity(Gravity.CENTER);
        } else {
            alertDialogHelper.setMsg(getStrings(exceptionMsg));
        }
        edit_cm_get_code.getText().clear();
        alertDialogHelper.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
        KeyboardUtils.unregisterSoftInputChangedListener(this);
    }

    @Override
    protected void loadData() {
    }

    @OnClick(R.id.tv_life_back)
    void goBack(View view) {
        finish();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (countDownHelper == null) {
            countDownHelper = CountDownHelper.getTimerInstance();
        }
        countDownHelper.setSmsCountDown(tv_change_send_code);
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
