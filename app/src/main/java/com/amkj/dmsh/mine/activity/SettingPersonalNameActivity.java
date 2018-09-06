package com.amkj.dmsh.mine.activity;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.bean.CommunalUserInfoEntity;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.constant.XUtil;
import com.amkj.dmsh.utils.ByteLimitWatcher;
import com.amkj.dmsh.utils.TextWatchListener;
import com.amkj.dmsh.utils.inteface.MyCallBack;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import emojicon.EmojiconEditTextClean;

import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.setEtFilter;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.IS_LOGIN_CODE;

;

/**
 * Created by atd48 on 2016/8/18.
 */
public class SettingPersonalNameActivity extends BaseActivity {
    @BindView(R.id.edit_personal_name)
    EmojiconEditTextClean edit_personal_name;
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
        String url = Url.BASE_URL + Url.MINE_CHANGE_DATA;
        Map<String, Object> params = new HashMap<>();
        params.put("uid", userId);
        params.put("nickname", name);
        XUtil.Post(url, params, new MyCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                if (loadHud != null) {
                    loadHud.dismiss();
                }
                Gson gson = new Gson();
                CommunalUserInfoEntity communalUserInfoEntity = gson.fromJson(result, CommunalUserInfoEntity.class);
                if (communalUserInfoEntity != null) {
                    if (communalUserInfoEntity.getCode().equals("01")) {
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


}
