package com.amkj.dmsh.mine.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.TinkerBaseApplicationLike;
import com.amkj.dmsh.bean.LoginDataEntity;
import com.amkj.dmsh.dao.UserDao;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.utils.ByteLimitWatcher;
import com.amkj.dmsh.utils.KeyboardUtils;
import com.amkj.dmsh.utils.TextWatchListener;
import com.google.gson.Gson;
import com.tencent.bugly.beta.tinker.TinkerManager;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.setEtFilter;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.Url.MINE_CHANGE_DATA;

;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/1/11
 * version 3.7
 * class description 注册 选择性别
 */

public class RegisterSelSexActivity extends BaseActivity {
    @BindView(R.id.rg_sex_sel)
    RadioGroup rg_sex_sel;
    @BindView(R.id.rb_register_sex_male)
    RadioButton rb_register_sex_male;
    @BindView(R.id.rb_register_sex_female)
    RadioButton rb_register_sex_female;

    @BindView(R.id.et_register_name)
    EditText et_register_name;
    @BindView(R.id.tv_register_data_confirm)
    TextView tv_register_data_confirm;

    @Override
    protected int getContentView() {
        return R.layout.activity_register_set_sex_name;
    }

    @Override
    protected void initViews() {
        getLoginStatus(this);
        TinkerBaseApplicationLike app = (TinkerBaseApplicationLike) TinkerManager.getTinkerApplicationLike();
        int screenHeight = app.getScreenHeight();
        Drawable[] compoundMaleDrawables = rb_register_sex_male.getCompoundDrawables();
        Drawable[] compoundFemaleDrawables = rb_register_sex_female.getCompoundDrawables();
        if (compoundMaleDrawables.length > 0) {
            Drawable drawableMale = compoundMaleDrawables[1];
            float cofValue = (97f / 1334) * screenHeight / drawableMale.getMinimumHeight();
            drawableMale.setBounds(0, 0, (int) (drawableMale.getMinimumWidth() * cofValue), (int) (cofValue * drawableMale.getMinimumHeight()));//设置边界
            rb_register_sex_male.setCompoundDrawables(null, drawableMale, null, null);
            Drawable drawableFemale = compoundFemaleDrawables[1];
            drawableFemale.setBounds(0, 0, (int) (drawableFemale.getMinimumWidth() * cofValue), (int) (cofValue * drawableFemale.getMinimumHeight()));
            rb_register_sex_female.setCompoundDrawables(null, drawableFemale, null, null);
        }
        rg_sex_sel.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                for (int i = 0; i < group.getChildCount(); i++) {
                    RadioButton rb = (RadioButton) group.getChildAt(i);
                    if (rb.getId() == checkedId) {
                        rb.setChecked(true);
                    }
                }
            }
        });
        setEtFilter(et_register_name);
        ByteLimitWatcher byteLimitWatcher = new ByteLimitWatcher(et_register_name, new TextWatchListener() {
            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString().trim();
                if (!TextUtils.isEmpty(text) && text.length() > 0) {
                    tv_register_data_confirm.setEnabled(true);
                } else {
                    tv_register_data_confirm.setEnabled(false);
                }
            }
        }, 60);
        et_register_name.addTextChangedListener(byteLimitWatcher);
        KeyboardUtils.registerSoftInputChangedListener(this, new KeyboardUtils.OnSoftInputChangedListener() {
            @Override
            public void onSoftInputChanged(int height) {
                if (height == 0) {
                    ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0).requestFocus();
                }
            }
        });
    }

    @Override
    protected void loadData() {
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            finish();
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @OnClick(R.id.tv_register_data_confirm)
    public void registerDataConfirm() {
        String nickName = et_register_name.getText().toString().trim();
        if (!TextUtils.isEmpty(nickName)) {
            int sexSelector = 0;
            int checkedRadioButtonId = rg_sex_sel.getCheckedRadioButtonId();
            switch (checkedRadioButtonId) {
                case R.id.rb_register_sex_female:
                    sexSelector = 1;
                    break;
                case R.id.rb_register_sex_male:
                    sexSelector = 0;
                    break;
            }
            Map<String, Object> params = new HashMap<>();
            params.put("uid", userId);
            params.put("sex", sexSelector);
            params.put("nickname", nickName);
            if (loadHud != null) {
                loadHud.show();
            }
            NetLoadUtils.getNetInstance().loadNetDataPost(this, MINE_CHANGE_DATA, params, new NetLoadListenerHelper() {
                @Override
                public void onSuccess(String result) {
                    if (loadHud != null) {
                        loadHud.dismiss();
                    }
                    Gson gson = new Gson();
                    LoginDataEntity loginDataEntity = gson.fromJson(result, LoginDataEntity.class);
                    if (loginDataEntity != null) {
                        if (loginDataEntity.getCode().equals(SUCCESS_CODE)) {
                            showToast( R.string.saveSuccess);
                            if (userId < 1) {
                                UserDao.loginSuccessSetData(getActivity(), loginDataEntity, null);
                            } else {
                                finish();
                            }
                        } else {
                            showToast( loginDataEntity.getMsg());
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
                    showToast(R.string.do_failed);
                }
            });
        } else {
            showToast( R.string.personal_nick_name);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            禁用返回
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        KeyboardUtils.unregisterSoftInputChangedListener(this);
    }
}
