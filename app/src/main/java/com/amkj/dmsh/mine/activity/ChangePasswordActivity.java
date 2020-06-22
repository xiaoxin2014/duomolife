package com.amkj.dmsh.mine.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.constant.ConstantVariable;
import com.amkj.dmsh.constant.PasswordEncrypt;
import com.amkj.dmsh.mine.bean.MinePassword;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.utils.gson.GsonUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.Url.MINE_CHANGE_PASSWORD;

;

/**
 * 修改密码
 */
public class ChangePasswordActivity extends BaseActivity{
    @BindView(R.id.tv_header_title)
    TextView tv_header_titleAll;
    @BindView(R.id.tv_header_shared)
    TextView header_shared;
    @BindView(R.id.edit_change_password_old)
    EditText edit_change_password_old;
    @BindView(R.id.edit_change_password_new)
    EditText edit_change_password_new;
    private boolean flag;

    @Override
    protected int getContentView() {
        return R.layout.activity_change_password;
    }

    @Override
    protected void initViews() {
        getLoginStatus(this);
        tv_header_titleAll.setText("修改密码");
        header_shared.setCompoundDrawables(null, null, null, null);
        header_shared.setText("完成");
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
    @Override
    protected void loadData() {
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ConstantVariable.IS_LOGIN_CODE) {
            loadData();
        }
    }

    //修改密码
    private void updatePassword(String oldPassword, String newPassword) {
        String passwordOldLock = PasswordEncrypt.getEncryptedPassword(oldPassword);
        String passwordNewLock = PasswordEncrypt.getEncryptedPassword(newPassword);
        Map<String, Object> params = new HashMap<>();
        params.put("id", userId);
        params.put("password", passwordOldLock);
        params.put("newPassword", passwordNewLock);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, MINE_CHANGE_PASSWORD,params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                if (loadHud != null) {
                    loadHud.dismiss();
                }

                MinePassword minePassword = GsonUtils.fromJson(result, MinePassword.class);
                if (minePassword != null) {
                    if (minePassword.getCode().equals(SUCCESS_CODE)) {
                        SharedPreferences loginStatus = getSharedPreferences("loginStatus", Context.MODE_PRIVATE);
                        SharedPreferences.Editor edit = loginStatus.edit();
                        edit.putBoolean("isLogin", true);
                        edit.putInt("uid", minePassword.getPasswordBackList().get(0).getId());
                        edit.apply();
                        showToast("密码修改成功");
                        //修改密码成功
                        finish();
                    } else {
                        showToast( minePassword.getMsg());
                    }
                    header_shared.setEnabled(true);
                }
            }

            @Override
            public void onNotNetOrException() {
                if (loadHud != null) {
                    loadHud.dismiss();
                }
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
        String newPassword = edit_change_password_new.getText().toString().trim();
        //修改密码
        String oldPassword = edit_change_password_old.getText().toString().trim();
//        密码小于6位
        if (oldPassword.length() < 6 || newPassword.length() < 6) {
            showToast( R.string.PasswordLessSix);
            return;
        }
//        新旧密码一致
        if (oldPassword.equals(newPassword)) {
            showToast( R.string.PasswordSame);
            return;
        }
//        6-20 数字与字母
        if (!PasswordEncrypt.isPwEligibility(newPassword)) {
            showToast(R.string.PasswordInconformity);
            return;
        }
        if (loadHud != null) {
            loadHud.show();
        }
        header_shared.setEnabled(false);
        // 验证原密码
        // 验证通过 上传新密码
        // 验证通过返回
        updatePassword(oldPassword, newPassword);
    }

}
