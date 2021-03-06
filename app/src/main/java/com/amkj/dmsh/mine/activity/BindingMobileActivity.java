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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.bean.LoginDataEntity;
import com.amkj.dmsh.bean.RequestStatus;
import com.amkj.dmsh.dao.UserDao;
import com.amkj.dmsh.mine.SmsCodeHelper;
import com.amkj.dmsh.mine.bean.OtherAccountBindEntity.OtherAccountBindInfo;
import com.amkj.dmsh.mine.bean.RegisterPhoneStatus;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.utils.gson.GsonUtils;
import com.amkj.dmsh.views.alertdialog.AlertDialogHelper;
import com.klinker.android.link_builder.Link;
import com.klinker.android.link_builder.LinkBuilder;

import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.OnClick;

import static com.amkj.dmsh.constant.ConstantMethod.dismissLoadhud;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.showLoadhud;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.toMD5;
import static com.amkj.dmsh.constant.ConstantVariable.BIND_PHONE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.WEB_TYPE_PRIVACY_POLICY;
import static com.amkj.dmsh.constant.ConstantVariable.WEB_TYPE_REG_AGREEMENT;
import static com.amkj.dmsh.constant.ConstantVariable.WEB_VALUE_TYPE;
import static com.amkj.dmsh.constant.Url.CHECK_PHONE_IS_REG;
import static com.amkj.dmsh.constant.Url.MINE_BIND_WX_MOBILE;
import static com.amkj.dmsh.constant.Url.REQ_SEND_SMS_CODE;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/3/6
 * ??????/????????????
 */
public class BindingMobileActivity extends BaseActivity {
    //???????????????
    @BindView(R.id.tv_bind_mobile_click)
    TextView tv_bind_mobile_click;
    //????????????
    @BindView(R.id.edit_binding_mobile)
    EditText edit_binding_mobile;
    //?????????????????????
    @BindView(R.id.tv_bind_send_code)
    TextView tv_bind_send_code;
    //   ???????????????
    @BindView(R.id.reg_bind_code_gif_view)
    ProgressBar reg_bind_code_gif_view;
    //    ???????????????
    @BindView(R.id.edit_get_code)
    EditText edit_get_code;
    @BindView(R.id.tv_agree)
    TextView mTvAgree;
    @BindView(R.id.tv_header_title)
    TextView mTvHeaderTitle;
    @BindView(R.id.tv_header_shared)
    TextView mTvHeaderShared;
    @BindView(R.id.tl_normal_bar)
    Toolbar mTlNormalBar;

    private String phoneNumber;
    private AlertDialogHelper alertDialogHelper;
    private String uid;
    private String openId;
    private String unionid;
    private String type;
    private String accessToken;

    @Override
    protected int getContentView() {
        return R.layout.activity_binding_mobile;
    }


    @Override
    protected void initViews() {
        mTvHeaderTitle.setText("???????????????");
        mTvHeaderShared.setVisibility(View.INVISIBLE);
        mTlNormalBar.setSelected(true);
        Intent intent = getIntent();
        if (intent == null) {
            return;
        }
        uid = intent.getStringExtra("uid");
        openId = intent.getStringExtra("openId");
        unionid = intent.getStringExtra("unionid");
        type = intent.getStringExtra("type");
        accessToken = intent.getStringExtra("accessToken");

        String s1 = "????????????????????????????????????";
        String s2 = "????????????????????????????????????";
        String text = "????????????????????? " + s1 + " " + s2;
        Link link1 = new Link(s1);
        Link link2 = new Link(s2);
        //        @????????????
        link1.setTextColor(Color.parseColor("#5faeff"));
        link1.setUnderlined(false);
        link1.setHighlightAlpha(0f);
        link2.setTextColor(Color.parseColor("#5faeff"));
        link2.setUnderlined(false);
        link2.setHighlightAlpha(0f);
        LinkBuilder.on(mTvAgree)
                .setText(text)
                .addLink(link1)
                .addLink(link2)
                .build();
        link1.setOnClickListener(new Link.OnClickListener() {
            @Override
            public void onClick(String clickedText) {
                Intent intent = new Intent(getActivity(), WebRuleCommunalActivity.class);
                intent.putExtra(WEB_VALUE_TYPE, WEB_TYPE_REG_AGREEMENT);
                startActivity(intent);
            }
        });
        link2.setOnClickListener(new Link.OnClickListener() {
            @Override
            public void onClick(String clickedText) {
                Intent intent = new Intent(getActivity(), WebRuleCommunalActivity.class);
                intent.putExtra(WEB_VALUE_TYPE, WEB_TYPE_PRIVACY_POLICY);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void loadData() {
    }


    //?????????????????????
    @OnClick({R.id.tv_bind_mobile_click})
    void addCode(View view) {
        isPhoneReg();
    }

    //???????????????
    @OnClick(R.id.tv_bind_send_code)
    void sendSmsCode(View view) {
        phoneNumber = edit_binding_mobile.getText().toString().trim();
        if (phoneNumber.length() == 11) {
            //            ???????????????
            tv_bind_send_code.setVisibility(View.GONE);
            reg_bind_code_gif_view.setVisibility(View.VISIBLE);
            reqSMSCode(phoneNumber);
        } else {
            showToast(R.string.MobileError);
        }
    }

    @OnClick(R.id.tv_life_back)
    void goBack(View view) {
        finish();
    }

    /**
     * ?????????????????????????????????
     * 3.1.9.1 ????????????????????????????????????
     * ??????????????????????????????
     */
    private void isPhoneReg() {
        final String smsCode = edit_get_code.getText().toString().trim();
        phoneNumber = edit_binding_mobile.getText().toString().trim();
        if (TextUtils.isEmpty(phoneNumber) || phoneNumber.length() != 11) {
            showToast(R.string.MobileError);
            return;
        }
        if (TextUtils.isEmpty(smsCode)) {
            showToast(R.string.SmsCodeNull);
            return;
        }
        showLoadhud(this);
        Map<String, Object> params = new HashMap<>();
        params.put("mobile", phoneNumber);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, CHECK_PHONE_IS_REG, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                RegisterPhoneStatus registerPhoneStatus = GsonUtils.fromJson(result, RegisterPhoneStatus.class);
                if (registerPhoneStatus != null) {
                    String code = registerPhoneStatus.getCode();
                    String msg = registerPhoneStatus.getMsg();
                    if (SUCCESS_CODE.equals(code)) {
                        setBindWxMobile();
                    } else {
                        dismissLoadhud(getActivity());
                        showToast(msg);
                    }
                } else {
                    dismissLoadhud(getActivity());
                }
            }

            @Override
            public void onNotNetOrException() {
                dismissLoadhud(getActivity());
            }
        });
    }

    private void reqSMSCode(String phoneNumber) {
        Map<String, Object> params = new HashMap<>();
        params.put("mobile", phoneNumber);
        params.put("smsType", BIND_PHONE);
        long currentTimeMillis = System.currentTimeMillis();
        /**
         * 3.1.2??????????????? ??????????????????????????????
         */
//        ??????token
        params.put("verify_token", toMD5("" + currentTimeMillis + BIND_PHONE + phoneNumber + "Domolife2018"));
//        ?????????
        params.put("unixtime", currentTimeMillis);
        NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(), REQ_SEND_SMS_CODE,
                params, new NetLoadListenerHelper() {
                    @Override
                    public void onSuccess(String result) {
                        loadHud.dismiss();

                        RequestStatus requestStatus = GsonUtils.fromJson(result, RequestStatus.class);
                        if (requestStatus != null) {
                            if (requestStatus.getCode().equals(SUCCESS_CODE)) {
                                RequestStatus.Result resultData = requestStatus.getResult();
                                if (resultData != null) {
                                    if (resultData.getResultCode().equals(SUCCESS_CODE)) {
                                        showToast(R.string.GetSmsCodeSuccess);
                                        tv_bind_send_code.setVisibility(View.VISIBLE);
                                        reg_bind_code_gif_view.setVisibility(View.GONE);
                                        SmsCodeHelper.startCountDownTimer(getActivity(), tv_bind_send_code);
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
                        tv_bind_send_code.setVisibility(View.VISIBLE);
                        reg_bind_code_gif_view.setVisibility(View.GONE);
                    }
                });
    }

    //????????????????????????
    private void setBindWxMobile() {
        Map<String, Object> params = new HashMap<>();
        params.put("mobile", phoneNumber);
        params.put("code", edit_get_code.getText().toString().trim());
        params.put("smsType", BIND_PHONE);
        params.put("type", type);
        params.put("uid", uid);
        params.put("accessToken", accessToken);
        params.put("openid", openId);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, MINE_BIND_WX_MOBILE, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                dismissLoadhud(getActivity());

                LoginDataEntity loginDataEntity = GsonUtils.fromJson(result, LoginDataEntity.class);
                if (loginDataEntity != null) {
                    String code = loginDataEntity.getCode();
                    LoginDataEntity.LoginDataBean loginDataBean = loginDataEntity.getLoginDataBean();
                    if (SUCCESS_CODE.equals(code)) {
                        showToast("????????????");
                        //?????????????????????????????????
                        if (loginDataBean.isResetPassword()) {
                            Intent intent = new Intent(getActivity(), SettingPasswordActivity.class);
                            intent.putExtra("phoneNum", phoneNumber);
                            intent.putExtra("openid", openId);
                            intent.putExtra("type", type);
                            startActivity(intent);
                        }
                        //???????????????????????????????????????????????????
                        UserDao.loginSuccessSetData(getActivity(), loginDataEntity, new OtherAccountBindInfo(type, openId, unionid, accessToken));
                    } else {
                        showException(loginDataEntity.getMsg());
                    }
                }
            }

            @Override
            public void onNotNetOrException() {
                dismissLoadhud(getActivity());
            }
        });
    }


    /**
     * ????????????????????????
     *
     * @param exceptionMsg
     */
    private void showException(String exceptionMsg) {
        edit_get_code.getText().clear();
        tv_bind_send_code.setVisibility(View.VISIBLE);
        reg_bind_code_gif_view.setVisibility(View.GONE);
        if (alertDialogHelper == null) {
            alertDialogHelper = new AlertDialogHelper(this)
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
