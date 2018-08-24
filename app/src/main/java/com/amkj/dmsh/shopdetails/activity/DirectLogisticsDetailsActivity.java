package com.amkj.dmsh.shopdetails.activity;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.BaseApplication;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.constant.XUtil;
import com.amkj.dmsh.shopdetails.adapter.LogisticsPagerAdapter;
import com.amkj.dmsh.shopdetails.bean.DirectLogisticsEntity;
import com.amkj.dmsh.shopdetails.bean.DirectLogisticsEntity.DirectLogisticsBean;
import com.amkj.dmsh.utils.inteface.MyCallBack;
import com.flyco.tablayout.SlidingTabLayout;
import com.google.gson.Gson;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class DirectLogisticsDetailsActivity extends BaseActivity {
    @BindView(R.id.tv_header_title)
    TextView tv_header_titleAll;
    @BindView(R.id.tv_header_shared)
    TextView header_shared;
    @BindView(R.id.stl_direct_logistics_details)
    SlidingTabLayout stl_direct_logistics_details;
    @BindView(R.id.rel_direct_logistics_layout)
    RelativeLayout rel_direct_logistics_layout;
    @BindView(R.id.vp_direct_logistics_details)
    ViewPager vp_direct_logistics_details;
    private String orderNo;
    private List<String> pageTitle = new ArrayList<>();
    private float tabWidth;
    @Override
    protected int getContentView() {
        return R.layout.activity_logistics_details;
    }
    @Override
    protected void initViews() {
        tv_header_titleAll.setText("物流详情");
        header_shared.setVisibility(View.INVISIBLE);
        Intent intent = getIntent();
        orderNo = intent.getStringExtra("orderNo");
        stl_direct_logistics_details.setTextsize(AutoUtils.getPercentWidth1px() * 28);
    }

    private void setLogisticsData(DirectLogisticsBean directLogisticsBean) {
        if (pageTitle.size() < 2) {
            rel_direct_logistics_layout.setVisibility(View.GONE);
        } else {
            rel_direct_logistics_layout.setVisibility(View.VISIBLE);
        }
        LogisticsPagerAdapter logisticsPagerAdapter = new LogisticsPagerAdapter(getSupportFragmentManager(), pageTitle, directLogisticsBean);
        vp_direct_logistics_details.setAdapter(logisticsPagerAdapter);
        if (tabWidth == 0) {
            BaseApplication app = (BaseApplication) getApplication();
            tabWidth = app.getScreenWidth() / 5f;
        }
        stl_direct_logistics_details.setTabWidth(tabWidth);
        stl_direct_logistics_details.setViewPager(vp_direct_logistics_details);
    }

    @Override
    protected void loadData() {
        String url = Url.BASE_URL + Url.Q_CONFIRM_LOGISTICS;
        Map<String, Object> params = new HashMap<>();
        params.put("no", orderNo);
        XUtil.Post(url, params, new MyCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                DirectLogisticsEntity directLogisticsEntity = gson.fromJson(result, DirectLogisticsEntity.class);
                if (directLogisticsEntity != null) {
                    if (directLogisticsEntity.getCode().equals("01")) {
                        int pageCount = directLogisticsEntity.getDirectLogisticsBean().getLogistics().size();
                        for (int i = 0; i < pageCount; i++) {
                            pageTitle.add("包裹" + (i + 1));
                        }
                        setLogisticsData(directLogisticsEntity.getDirectLogisticsBean());
                    } else {
                        showToast(DirectLogisticsDetailsActivity.this, directLogisticsEntity.getMsg());
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                showToast(DirectLogisticsDetailsActivity.this, R.string.invalidData);
            }
        });
    }

    @OnClick(R.id.tv_life_back)
    void goBack(View view) {
        finish();
    }

}
