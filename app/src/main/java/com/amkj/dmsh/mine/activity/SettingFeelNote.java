package com.amkj.dmsh.mine.activity;

import android.content.Intent;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.bean.CommunalUserInfoEntity;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.constant.XUtil;
import com.amkj.dmsh.mine.bean.SavePersonalInfoBean;
import com.amkj.dmsh.utils.ByteLimitWatcher;
import com.amkj.dmsh.utils.TextWatchListener;
import com.amkj.dmsh.utils.inteface.MyCallBack;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import emojicon.EmojiconEditText;

import static com.amkj.dmsh.constant.ConstantMethod.getPersonalInfo;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;

;

/**
 * Created by atd48 on 2016/8/18.
 */
public class SettingFeelNote extends BaseActivity {
    @BindView(R.id.edit_feel)
    EmojiconEditText edit_feel;
    @BindView(R.id.tv_header_title)
    TextView tv_header_titleAll;
    @BindView(R.id.tv_header_shared)
    TextView header_shared;
    @BindView(R.id.tv_feel_count)
    TextView tv_feel_count;
    private final String FEELNOTE = "feelNote";
    private String feelNote;
    private int uid;
    private final int maxFontsCount = 30;
    private final int maxFontsCountBytes = 90;
    private int textRemainLength;
    @Override
    protected int getContentView() {
        return R.layout.activity_setting_feell_note;
    }
    @Override
    protected void initViews() {
        isLoginStatus();
        tv_header_titleAll.setText("个性签名");
        header_shared.setText("保存");
        header_shared.setTextColor(Color.parseColor("#408ed6"));
        header_shared.setCompoundDrawables(null, null, null, null);
        Intent intent = getIntent();
        feelNote = intent.getStringExtra(FEELNOTE);
        if (!TextUtils.isEmpty(feelNote)) {
            edit_feel.setText(feelNote);
            edit_feel.setSelection(feelNote.length());
            textRemainLength = maxFontsCount - edit_feel.getText().toString().getBytes().length / 3;
            tv_feel_count.setText((textRemainLength < 0 ? 0 : textRemainLength) + "");
        }
//        限制字符30个字
        ByteLimitWatcher byteLimitWatcher = new ByteLimitWatcher(edit_feel, new TextWatchListener() {
            @Override
            public void afterTextChanged(Editable s) {
                textRemainLength = maxFontsCount - s.toString().getBytes().length / 3;
                tv_feel_count.setText((textRemainLength < 0 ? 0 : textRemainLength) + "");
            }
        }, maxFontsCountBytes);
        edit_feel.addTextChangedListener(byteLimitWatcher);
    }

    private void getData(String name) {
        String url = Url.BASE_URL + Url.MINE_CHANGE_DATA;
        Map<String, Object> params = new HashMap<>();
        params.put("uid", uid);
        params.put("signature", name);
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
                        intent.putExtra(FEELNOTE, communalUserInfoEntity.getCommunalUserInfoBean().getSignature());
                        setResult(RESULT_OK, intent);
                        finish();
                    } else {
                        showToast(SettingFeelNote.this, communalUserInfoEntity.getMsg());
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
    private void isLoginStatus() {
        SavePersonalInfoBean personalInfo = getPersonalInfo(this);
        if (personalInfo.isLogin()) {
            uid = personalInfo.getUid();
        } else {
            uid = 0;
        }
    }
    @Override
    protected void loadData() {

    }

    @OnClick(R.id.tv_header_shared)
    void saveName(View view) {
        String feelNote = edit_feel.getText().toString();
        if (!TextUtils.isEmpty(feelNote)) {
            if (feelNote.getBytes().length / 3 < 2) {
                showToast(this, R.string.personal_name_hint);
            } else {
                if (feelNote.getBytes().length / 3 > maxFontsCount) {
                    showToast(this, R.string.personal_name_more_hint);
                } else {
                    if (loadHud != null) {
                        loadHud.show();
                    }
                    getData(feelNote);
                }
            }
        } else {
            showToast(this, R.string.text_fonts_empty);
        }
    }

    @OnClick(R.id.tv_life_back)
    void goBack(View view) {
        finish();
    }


}
