package com.amkj.dmsh.mine.activity;

import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.bean.RequestStatus;
import com.amkj.dmsh.constant.PasswordEncrypt;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

import static com.amkj.dmsh.constant.ConstantMethod.dismissLoadhud;
import static com.amkj.dmsh.constant.ConstantMethod.showLoadhud;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.Url.SETTING_NEW_PWD;

/**
 * Created by xiaoxin on 2019/9/19
 * Version:v4.2.2
 * ClassDescription :绑定未注册的手机时设置密码
 */
public class SettingPasswordActivity extends BaseActivity {
    @BindView(R.id.tv_life_back)
    TextView mTvLifeBack;
    @BindView(R.id.tv_header_title)
    TextView mTvHeaderTitle;
    @BindView(R.id.tl_normal_bar)
    Toolbar mTlNormalBar;
    @BindView(R.id.et_password)
    EditText mEtPassword;
    private String phoneNum;
    private String openid;
    private String type;
    private String password;

    @Override
    protected int getContentView() {
        return R.layout.activity_setting_password;
    }

    @Override
    protected void initViews() {
        mTvHeaderTitle.setText("设置新密码");
        mTlNormalBar.setSelected(true);
        if (getIntent() != null) {
            phoneNum = getIntent().getStringExtra("phoneNum");
            openid = getIntent().getStringExtra("openid");
            type = getIntent().getStringExtra("type");
        } else {
            finish();
        }
    }

    @Override
    protected void loadData() {

    }

    @OnClick({R.id.tv_life_back, R.id.tv_confirm, R.id.tv_skip})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_skip:
            case R.id.tv_life_back:
                finish();
                break;
            case R.id.tv_confirm:
                password = mEtPassword.getText().toString().trim();
                if (TextUtils.isEmpty(password)) {
                    showToast(this, "请输入密码");
                    return;
                }
                if (password.length() < 6) {
                    showToast(this, R.string.PasswordLessSix);
                    return;
                }

                if (!PasswordEncrypt.isPwEligibility(password)) {
                    showToast(this, R.string.PasswordInconformity);
                    return;
                }
                settingPwd();
                break;
        }
    }

    private void settingPwd() {
        showLoadhud(this);
        Map<String, String> map = new HashMap<>();
        map.put("mobile", phoneNum);
        map.put("openid", openid);
        map.put("type", type);
        map.put("password", PasswordEncrypt.getEncryptedPassword(password));
        NetLoadUtils.getNetInstance().loadNetDataPost(this, SETTING_NEW_PWD, map, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                dismissLoadhud(getActivity());
                RequestStatus requestStatus = new Gson().fromJson(result, RequestStatus.class);
                if (requestStatus != null) {
                    String code = requestStatus.getCode();
                    String msg = requestStatus.getMsg();
                    if (SUCCESS_CODE.equals(code)) {
                        showToast("密码设置成功");
                        finish();
                    } else {
                        showToast(getActivity(), msg);
                    }
                }
            }

            @Override
            public void netClose() {
                dismissLoadhud(getActivity());
            }
        });
    }
}
