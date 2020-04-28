package com.amkj.dmsh.shopdetails.activity;

import android.view.View;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.qyservice.QyServiceUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by xiaoxin on 2020/3/26
 * Version:v4.5.0
 * ClassDescription :订单搜索帮助
 */
public class OrderSearchHelpActivity extends BaseActivity {
    @BindView(R.id.tv_header_shared)
    TextView mTvHeaderShared;

    @Override
    protected int getContentView() {
        return R.layout.activity_order_search_help;
    }

    @Override
    protected void initViews() {
        mTvHeaderShared.setVisibility(View.GONE);
    }

    @Override
    protected void loadData() {

    }

    @OnClick({R.id.tv_life_back, R.id.ll_service})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_life_back:
                finish();
                break;
            case R.id.ll_service:
                QyServiceUtils.getQyInstance().openQyServiceChat(getActivity(),"找不到订单");
                break;
        }
    }
}
