package com.amkj.dmsh.mine.activity;

import android.content.Intent;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.bean.CommunalUserInfoEntity;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;

import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.Url.MINE_CHANGE_DATA;


/**
 * Created by atd48 on 2016/8/18.
 * 限时特惠商品 设置消息提醒
 */
public class MessageWarmActivity extends BaseActivity {
    @BindView(R.id.tv_header_title)
    TextView tv_header_titleAll;
    @BindView(R.id.tv_header_shared)
    TextView header_shared;
    @BindView(R.id.rp_message_warm)
    RadioGroup rp_message_warm;
    private final String TIME_WARM = "timeWarm";
    private String timeWarm;

    @Override
    protected int getContentView() {
        return R.layout.activity_message_warm;
    }

    @Override
    protected void initViews() {
        getLoginStatus(MessageWarmActivity.this);
        tv_header_titleAll.setText("提醒设定");
        header_shared.setCompoundDrawables(null, null, null, null);
        header_shared.setText("保存");
        Intent intent = getIntent();
        timeWarm = intent.getStringExtra("warmTime");
        for (int i = 0; i < rp_message_warm.getChildCount(); i++) {
            RadioButton button = (RadioButton) rp_message_warm.getChildAt(i);
            if (getNumber(button.getText().toString().trim()).equals(timeWarm)) {
                button.setChecked(true);
                i = rp_message_warm.getChildCount();
            } else {
                button.setChecked(false);
            }
        }
        rp_message_warm.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int childCount = group.getChildCount();
                for (int i = 0; i < childCount; i++) {
                    RadioButton radioButton = (RadioButton) group.getChildAt(i);
                    if (checkedId == radioButton.getId()) {
                        radioButton.setChecked(true);
                        i = rp_message_warm.getChildCount();
                    }
                }
            }
        });
    }

    private String getNumber(String str) {
        String regex = "\\d*";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(str);
        while (m.find()) {
            if (!"".equals(m.group()))
                return m.group();
        }
        return "5";
    }

    @Override
    protected void loadData() {
    }

    private void getData(String warmData) {
        if (userId < 1) {
            NetLoadUtils.getNetInstance().showLoadSirEmpty(loadService);
            return;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("uid", userId);
        params.put("remindtime", getNumber(warmData));
        NetLoadUtils.getNetInstance().loadNetDataPost(this,MINE_CHANGE_DATA,params,new NetLoadListenerHelper(){
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                CommunalUserInfoEntity communalUserInfoEntity = gson.fromJson(result, CommunalUserInfoEntity.class);
                if (communalUserInfoEntity != null) {
                    if (communalUserInfoEntity.getCode().equals(SUCCESS_CODE)) {
                        showToast(R.string.saveSuccess);
                        Intent intent = new Intent();
                        intent.putExtra(TIME_WARM, getNumber(communalUserInfoEntity.getCommunalUserInfoBean().getRemindtime() + ""));
                        setResult(RESULT_OK, intent);
                        finish();
                    } else {
                        showToast(communalUserInfoEntity.getMsg());
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            finish();
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @OnClick(R.id.tv_life_back)
    void goBack(View view) {
        finish();
    }

    @OnClick(R.id.tv_header_shared)
    void saveWarm(View view) {
        RadioButton button = (RadioButton) findViewById(rp_message_warm.getCheckedRadioButtonId());
        String warmData = button.getText().toString().trim();
        getData(warmData);
    }


}
