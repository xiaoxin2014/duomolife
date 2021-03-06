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
import com.amkj.dmsh.mine.SmsCodeHelper;
import com.amkj.dmsh.mine.bean.RegisterPhoneStatus;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.utils.LifecycleHandler;
import com.amkj.dmsh.utils.NetWorkUtils;
import com.amkj.dmsh.utils.gson.GsonUtils;
import com.amkj.dmsh.views.alertdialog.AlertDialogHelper;
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
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.WEB_TYPE_PRIVACY_POLICY;
import static com.amkj.dmsh.constant.ConstantVariable.WEB_TYPE_REG_AGREEMENT;
import static com.amkj.dmsh.constant.ConstantVariable.WEB_VALUE_TYPE;
import static com.amkj.dmsh.constant.Url.CHECK_PHONE_IS_REG;
import static com.amkj.dmsh.constant.Url.USER_REGISTER_ACCOUNT;
import static com.amkj.dmsh.rxeasyhttp.utils.DeviceUtils.getDeviceId;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/3/6
 * class description:????????????
 */
public class RegisterAccountActivity extends BaseActivity {
    @BindView(R.id.ll_layout_reg)
    public LinearLayout ll_layout_reg;
    @BindView(R.id.edit_register_mobile)
    public EditText edit_register_mobile;
    @BindView(R.id.edit_register_password)
    public EditText edit_register_password;
    //???????????????
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
    private AlertDialogHelper alertDialogHelper;

    @Override
    protected int getContentView() {
        return R.layout.activity_register_account;
    }

    @Override
    protected void initViews() {
        //??????????????????
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

    private LifecycleHandler handler = new LifecycleHandler(this, new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            int event = msg.arg1;
            int result = msg.arg2;
            Object data = msg.obj;
            if (result == SMSSDK.RESULT_COMPLETE) {
                //????????????
//                showToast(RegisterAccountActivity.this, "????????????");
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) { //?????????????????????
                    //???????????????
//                    showToast(RegisterAccountActivity.this, "????????????");
                    getData(edit_register_mobile.getText().toString().trim(), edit_register_password.getText().toString().trim());
                } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) { //?????????????????????
                    showToast(R.string.GetSmsCodeSuccess);
                    tv_sms_code.setVisibility(View.VISIBLE);
                    reg_req_code_gif_view.setVisibility(View.GONE);
                    SmsCodeHelper.startCountDownTimer(getActivity(), tv_sms_code);
                } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) { //??????????????????????????????????????????
                }
            } else if (data != null) { //????????????
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
                    int status = object.optInt("status");//????????????
                    disposeMessageCode(status);
                } catch (Exception e) {
                    showToast(R.string.unConnectedNetwork);
                }
            } else {
                showToast(R.string.do_failed);
            }
            return false;
        }
    });

    //  ????????????
    private void getData(String phoneNumber, String password) {
        Sha1Md5 sha1Md5 = new Sha1Md5();
        Map<String, Object> params = new HashMap<>();
        params.put("mobile", phoneNumber);
        params.put("newPassword", PasswordEncrypt.getEncryptedPassword(password));
        long currentTimeMillis = System.currentTimeMillis();
        String uniqueId = getDeviceId(this);
        /**
         * 3.1.2??????????????? ?????????????????????
         */
//        ??????token
        params.put("verify_token", sha1Md5.getMD5("" + currentTimeMillis + phoneNumber + uniqueId + "Domolife2018"));
//        ?????????
        params.put("unixtime", currentTimeMillis);
//        ?????????
        params.put("device_number", uniqueId);
        //        v3.2.0 ???????????? 1  ??????????????????
        params.put("approve", 1);
        NetLoadUtils.getNetInstance().loadNetDataPost(RegisterAccountActivity.this, USER_REGISTER_ACCOUNT,
                params, new NetLoadListenerHelper() {
                    @Override
                    public void onSuccess(String result) {
                        bt_mine_reg.setEnabled(true);
                        if (loadHud != null) {
                            loadHud.dismiss();
                        }

                        LoginDataEntity loginDataEntity = GsonUtils.fromJson(result, LoginDataEntity.class);
                        if (loginDataEntity != null) {
                            String code = loginDataEntity.getCode();
                            if (SUCCESS_CODE.equals(code)) {
                                //??????????????????
                                Intent intent = new Intent(RegisterAccountActivity.this, RegisterSelSexActivity.class);
                                startActivity(intent);
                                //??????????????????
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
     * ????????????????????????
     *
     * @param exceptionMsg
     */
    private void showException(String exceptionMsg) {
        if (alertDialogHelper == null) {
            alertDialogHelper = new AlertDialogHelper(RegisterAccountActivity.this)
                    .setTitle("????????????")
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

    //???????????????
    @OnClick(R.id.tv_sms_code)
    void sendCode(View view) {
        String phoneNumber = edit_register_mobile.getText().toString().trim();
        isPhoneReg(phoneNumber);
    }

    /**
     * ?????????????????????????????????
     *
     * @param phoneNumber
     */
    private void isPhoneReg(final String phoneNumber) {
        if (phoneNumber.length() != 11) {
            showToast(R.string.MobileError);
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

                    RegisterPhoneStatus status = GsonUtils.fromJson(result, RegisterPhoneStatus.class);
                    if (status != null && status.getRegisterFlag() == 0) {
                        //            ???????????????
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
                    showToast(R.string.unConnectedNetwork);
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
            showToast(R.string.PasswordLessSix);
            return;
        }
        if (!PasswordEncrypt.isPwEligibility(password)) {
            showToast(R.string.PasswordInconformity);
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
}
