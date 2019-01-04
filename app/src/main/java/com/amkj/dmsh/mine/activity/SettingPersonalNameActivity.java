package com.amkj.dmsh.mine.activity;

import android.content.Context;
import android.content.Intent;
import android.support.text.emoji.widget.EmojiEditText;
import android.text.Editable;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.bean.CommunalUserInfoEntity;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.utils.ByteLimitWatcher;
import com.amkj.dmsh.utils.KeyboardUtils;
import com.amkj.dmsh.utils.TextWatchListener;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.setEtFilter;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.IS_LOGIN_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.Url.MINE_CHANGE_DATA;

;

/**
 * Created by atd48 on 2016/8/18.
 */
public class SettingPersonalNameActivity extends BaseActivity {
    @BindView(R.id.edit_personal_name)
    EmojiEditText edit_personal_name;
    @BindView(R.id.tv_personal_name_length)
    TextView tv_personal_name_length;
    @BindView(R.id.tv_header_title)
    TextView tv_header_titleAll;
    @BindView(R.id.tv_header_shared)
    TextView header_shared;
    private String name;
    private final String NAME = "name";
    private final int maxLength = 20;
    private final int maxByteLength = 60;
    private int textRemainLength;

    @Override
    protected int getContentView() {
        return R.layout.activity_setting_personal_name;
    }

    @Override
    protected void initViews() {
        getLoginStatus(this);
        tv_header_titleAll.setText("修改昵称");
        header_shared.setText("保存");
        header_shared.setCompoundDrawables(null, null, null, null);
        Intent intent = getIntent();
        name = intent.getStringExtra(NAME);
        if (!TextUtils.isEmpty(name)) {
            edit_personal_name.setText(name);
            edit_personal_name.setSelection(name.length());
            textRemainLength = maxLength - edit_personal_name.getText().toString().getBytes().length / 3;
            tv_personal_name_length.setText(String.valueOf(textRemainLength < 0 ? 0 : textRemainLength));
        }
        setEtFilter(edit_personal_name);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            if (requestCode == IS_LOGIN_CODE) {
                finish();
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    protected void loadData() {
        ByteLimitWatcher byteLimitWatcher = new ByteLimitWatcher(edit_personal_name, new TextWatchListener() {
            @Override
            public void afterTextChanged(Editable s) {
                textRemainLength = maxLength - s.toString().getBytes().length / 3;
                tv_personal_name_length.setText(String.valueOf(textRemainLength < 0 ? 0 : textRemainLength));
            }
        }, maxByteLength);
        edit_personal_name.addTextChangedListener(byteLimitWatcher);
    }


    @OnClick(R.id.tv_header_shared)
    void saveName(View view) {
        String name = edit_personal_name.getText().toString().trim();
        if (!TextUtils.isEmpty(name)) {
            if (name.getBytes().length / 3 < 2) {
                showToast(this, R.string.personal_name_hint);
            } else {
                if (name.getBytes().length / 3 > maxLength) {
                    showToast(this, R.string.personal_name_more_hint);
                } else {
                    if (loadHud != null) {
                        loadHud.show();
                    }
                    getData(name);
                }
            }
        } else {
            showToast(this, R.string.text_fonts_empty);
        }
    }

    private void getData(String name) {
        Map<String, Object> params = new HashMap<>();
        params.put("uid", userId);
        params.put("nickname", name);
        NetLoadUtils.getNetInstance().loadNetDataPost(this,MINE_CHANGE_DATA,params,new NetLoadListenerHelper(){
            @Override
            public void onSuccess(String result) {
                if (loadHud != null) {
                    loadHud.dismiss();
                }
                Gson gson = new Gson();
                CommunalUserInfoEntity communalUserInfoEntity = gson.fromJson(result, CommunalUserInfoEntity.class);
                if (communalUserInfoEntity != null) {
                    if (communalUserInfoEntity.getCode().equals(SUCCESS_CODE)) {
                        Intent intent = new Intent();
                        intent.putExtra(NAME, communalUserInfoEntity.getCommunalUserInfoBean().getNickname());
                        setResult(RESULT_OK, intent);
                        finish();
                    } else {
                        showToast(SettingPersonalNameActivity.this, communalUserInfoEntity.getMsg());
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
                showToast(SettingPersonalNameActivity.this,R.string.do_failed);
            }

            @Override
            public void netClose() {
                showToast(SettingPersonalNameActivity.this,R.string.unConnectedNetwork);
            }
        });
    }

    @OnClick(R.id.tv_life_back)
    void goBack(View view) {
        finish();
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                InputMethodManager imm =
                        (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if(imm!=null){
                    imm.hideSoftInputFromWindow(v.getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        }
        return super.dispatchTouchEvent(ev);
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
