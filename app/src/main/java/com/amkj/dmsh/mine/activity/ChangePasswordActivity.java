package com.amkj.dmsh.mine.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.ConstantVariable;
import com.amkj.dmsh.constant.PasswordEncrypt;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.constant.XUtil;
import com.amkj.dmsh.mine.bean.MinePassword;
import com.amkj.dmsh.mine.bean.SavePersonalInfoBean;
import com.amkj.dmsh.utils.inteface.MyCallBack;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;

;

/**
 * 修改密码
 */
public class ChangePasswordActivity extends BaseActivity implements TextWatcher {
    @BindView(R.id.tv_header_title)
    TextView tv_header_titleAll;
    @BindView(R.id.tv_header_shared)
    TextView header_shared;
    @BindView(R.id.edit_change_password_old)
    EditText edit_change_password_old;
    @BindView(R.id.edit_change_password_new)
    EditText edit_change_password_new;
    public static final String FOUND_PASSWORD = "foundPassword";
    private final String CHANGE_PASSWORD = "changePassword";
    private String type;
    private int uid;
    private boolean flag;
    @Override
    protected int getContentView() {
        return R.layout.activity_change_password;
    }
    @Override
    protected void initViews() {
        getLoginStatus();
        tv_header_titleAll.setText("修改密码");
        header_shared.setCompoundDrawables(null, null, null, null);
        header_shared.setText("完成");
        Intent intent = getIntent();
        type = intent.getStringExtra("type");
        //手机号
        if (type.equals(FOUND_PASSWORD)) {
            edit_change_password_old.setVisibility(View.GONE);
        } else {
            edit_change_password_old.setVisibility(View.VISIBLE);
        }
        edit_change_password_old.addTextChangedListener(this);
        edit_change_password_new.addTextChangedListener(this);
    }

    @Override
    protected void loadData() {
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.toString().trim().length() > 5) {
            header_shared.setEnabled(true);
        } else {
            header_shared.setEnabled(false);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
    }

    private void getLoginStatus() {
        SavePersonalInfoBean personalInfo = ConstantMethod.getPersonalInfo(this);
        if (personalInfo.isLogin()) {
            uid = personalInfo.getUid();
        } else {
            //未登录跳转登录页
            Intent intent = new Intent(this, MineLoginActivity.class);
            startActivityForResult(intent, ConstantVariable.IS_LOGIN_CODE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == ConstantVariable.IS_LOGIN_CODE) {
                getLoginStatus();
                loadData();
            }
        }
    }

    //修改密码
    private void updatePassword(String oldPassword, String newPassword) {
        String passwordOldLock = PasswordEncrypt.getEncryptedPassword(oldPassword);
        String passwordNewLock = PasswordEncrypt.getEncryptedPassword(newPassword);
        String url = Url.BASE_URL + Url.MINE_CHANGE_PASSWORD;
        Map<String, Object> params = new HashMap<>();
        params.put("id", uid);
        params.put("password", passwordOldLock);
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
                    if (minePassword.getCode().equals(SUCCESS_CODE)) {
                        SharedPreferences loginStatus = getSharedPreferences("LoginStatus", Context.MODE_PRIVATE);
                        SharedPreferences.Editor edit = loginStatus.edit();
                        edit.putBoolean("isLogin", true);
                        edit.putInt("uid", minePassword.getPasswordBackList().get(0).getId());
                        edit.apply();
                        showToast(ChangePasswordActivity.this, "密码修改成功");
                        //修改密码成功
                        finish();
                    } else {
                        showToast(ChangePasswordActivity.this, minePassword.getMsg());
                    }
                    header_shared.setEnabled(true);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (loadHud != null) {
                    loadHud.dismiss();
                }
                super.onError(ex, isOnCallback);
            }
        });
    }

    @OnClick(R.id.tv_life_back)
     void goBack(View view) {
        finish();
    }

    //    显示密码
    @OnClick(R.id.iv_show_pas)
    void showPas(View view) {
        String newPassword = edit_change_password_new.getText().toString().trim();
        if (!TextUtils.isEmpty(newPassword)) {
            if (flag) {
                edit_change_password_new.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                Editable etAble = edit_change_password_new.getText();
                Selection.setSelection(etAble, etAble.length());
                flag = false;
            } else {
                edit_change_password_new.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                Editable etAble = edit_change_password_new.getText();
                Selection.setSelection(etAble, etAble.length());
                flag = true;
            }
        }
    }

    @OnClick(R.id.tv_header_shared)
    void saveData(View view) {
        // 点击提交 设置不能点击，成功传递或者失败，再次打开
        String newPassword1 = edit_change_password_new.getText().toString().trim();
        //修改密码
        if (type.equals(CHANGE_PASSWORD)) {
            //1.判断是否符合密码规则 2.判断新密码是否一致 3.最后判断旧密码正确与否
            String oldPassword = edit_change_password_old.getText().toString().trim();
            if (oldPassword.length() < 6 || newPassword1.length() < 6) {
                showToast(this, R.string.PasswordLessSix);
            } else if (newPassword1.length() > 5) {
                if (loadHud != null) {
                    loadHud.show();
                }
                header_shared.setEnabled(false);
                // 验证原密码
                // 验证通过 上传新密码
                // 验证通过返回
                updatePassword(oldPassword, newPassword1);
            }
        }
    }

}
