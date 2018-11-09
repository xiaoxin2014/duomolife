package com.amkj.dmsh.shopdetails.activity;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.NetLoadUtils;
import com.amkj.dmsh.base.TinkerBaseApplicationLike;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.shopdetails.adapter.LogisticsPagerAdapter;
import com.amkj.dmsh.shopdetails.bean.DirectLogisticsEntity;
import com.amkj.dmsh.shopdetails.bean.DirectLogisticsEntity.DirectLogisticsBean;
import com.flyco.tablayout.SlidingTabLayout;
import com.google.gson.Gson;
import com.tencent.bugly.beta.tinker.TinkerManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import me.jessyan.autosize.utils.AutoSizeUtils;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.IS_LOGIN_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;

;

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
    private DirectLogisticsEntity directLogisticsEntity;

    @Override
    protected int getContentView() {
        return R.layout.activity_logistics_details;
    }

    @Override
    protected void initViews() {
        getLoginStatus(this);
        tv_header_titleAll.setText("物流详情");
        header_shared.setVisibility(View.INVISIBLE);
        Intent intent = getIntent();
        orderNo = intent.getStringExtra("orderNo");
        stl_direct_logistics_details.setTextsize(AutoSizeUtils.mm2px(mAppContext, 28));
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
            TinkerBaseApplicationLike app = (TinkerBaseApplicationLike) TinkerManager.getTinkerApplicationLike();
            tabWidth = app.getScreenWidth() / 5f;
        }
        stl_direct_logistics_details.setTabWidth(tabWidth);
        stl_direct_logistics_details.setViewPager(vp_direct_logistics_details);
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }


    @Override
    protected void loadData() {
        if (userId < 1) {
            NetLoadUtils.getQyInstance().showLoadSirSuccess(loadService);
            return;
        }
        String url = Url.BASE_URL + Url.Q_CONFIRM_LOGISTICS;
        Map<String, Object> params = new HashMap<>();
        params.put("no", orderNo);
        NetLoadUtils.getQyInstance().loadNetDataPost(DirectLogisticsDetailsActivity.this, url, params, new NetLoadUtils.NetLoadListener() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                directLogisticsEntity = gson.fromJson(result, DirectLogisticsEntity.class);
                if (directLogisticsEntity != null) {
                    if (directLogisticsEntity.getCode().equals(SUCCESS_CODE)) {
                        int pageCount = directLogisticsEntity.getDirectLogisticsBean().getLogistics().size();
                        for (int i = 0; i < pageCount; i++) {
                            pageTitle.add("包裹" + (i + 1));
                        }
                        setLogisticsData(directLogisticsEntity.getDirectLogisticsBean());
                    } else {
                        showToast(DirectLogisticsDetailsActivity.this, directLogisticsEntity.getMsg());
                    }
                }
                NetLoadUtils.getQyInstance().showLoadSir(loadService,directLogisticsEntity);
            }

            @Override
            public void netClose() {
                showToast(DirectLogisticsDetailsActivity.this,R.string.unConnectedNetwork);
                NetLoadUtils.getQyInstance().showLoadSir(loadService,directLogisticsEntity);
            }

            @Override
            public void onError(Throwable throwable) {
                showToast(DirectLogisticsDetailsActivity.this, R.string.invalidData);
                NetLoadUtils.getQyInstance().showLoadSir(loadService,directLogisticsEntity);
            }
        });
    }

    @OnClick(R.id.tv_life_back)
    void goBack(View view) {
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            if (requestCode == IS_LOGIN_CODE) {
                finish();
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IS_LOGIN_CODE) {
            NetLoadUtils.getQyInstance().showLoadSirLoading(loadService);
            loadData();
        }
    }

}
