package com.amkj.dmsh.homepage.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.bean.RequestStatus;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.constant.XUtil;
import com.amkj.dmsh.utils.inteface.MyCallBack;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;

;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/7/28
 * class description:搜索留言
 */
public class SearchLeaveMessageActivity extends BaseActivity {
    @BindView(R.id.tv_header_title)
    TextView tv_header_titleAll;
    @BindView(R.id.tv_header_shared)
    TextView header_shared;
    @BindView(R.id.et_search_leave_mes)
    EditText et_search_leave_mes;
    private int uid;
    @Override
    protected int getContentView() {
        return R.layout.activity_search_leave_mes;
    }
    @Override
    protected void initViews() {
        getLoginStatus(SearchLeaveMessageActivity.this);
        tv_header_titleAll.setVisibility(View.GONE);
        header_shared.setCompoundDrawables(null, null, null, null);
        header_shared.setText("提交");
    }

    @Override
    protected void loadData() {
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
    void finish(View view) {
        finish();
    }

    @OnClick(R.id.tv_header_shared)
    void subMes(View view) {
        String leaveMes = et_search_leave_mes.getText().toString();
        if (!TextUtils.isEmpty(leaveMes)) {
            subMes(leaveMes);
        } else {
            showToast(this, "输入为空，请填写内容");
        }
    }

    private void subMes(String leaveMes) {
        String url = Url.BASE_URL + Url.SEARCH_LEAVE_MES;
        Map<String, Object> params = new HashMap<>();
        params.put("uid", uid);
        params.put("content", leaveMes);
        XUtil.Post(url, params, new MyCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                RequestStatus requestStatus = gson.fromJson(result, RequestStatus.class);
                if (requestStatus != null) {
                    if (requestStatus.getCode().equals("01")) {
                        showToast(SearchLeaveMessageActivity.this, "提交完成");
                        finish();
                    } else {
                        showToast(SearchLeaveMessageActivity.this, requestStatus.getMsg() + ",请重新提交");
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                showToast(SearchLeaveMessageActivity.this, "提交失败,请重新提交");
                super.onError(ex, isOnCallback);
            }
        });
    }


}
