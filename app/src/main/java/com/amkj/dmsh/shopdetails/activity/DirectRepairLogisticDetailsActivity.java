package com.amkj.dmsh.shopdetails.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.constant.XUtil;
import com.amkj.dmsh.shopdetails.adapter.DirectLogisticsAdapter;
import com.amkj.dmsh.shopdetails.bean.DirectLogisticsPacketEntity.DirectLogisticsPacketBean.LogisticsEntity.LogisticsBean.ListBean;
import com.amkj.dmsh.shopdetails.bean.DirectRepairLogisticsEntity;
import com.amkj.dmsh.shopdetails.bean.DirectRepairLogisticsEntity.DirectRepairLogisticsBean;
import com.amkj.dmsh.utils.NetWorkUtils;
import com.amkj.dmsh.utils.inteface.MyCallBack;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.amkj.dmsh.constant.ConstantMethod.showToast;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/3/28
 * version 3.1.1
 * class description:售后维修物流信息查询
 */

public class DirectRepairLogisticDetailsActivity extends BaseActivity {
    @BindView(R.id.tv_header_shared)
    TextView header_shared;
    @BindView(R.id.tv_header_title)
    TextView tv_header_titleAll;
    @BindView(R.id.tl_normal_bar)
    Toolbar tl_normal_bar;
    @BindView(R.id.smart_communal_refresh)
    RefreshLayout smart_communal_refresh;
    @BindView(R.id.communal_recycler)
    RecyclerView communal_recycler;
    @BindView(R.id.communal_load)
    View communal_load;
    @BindView(R.id.communal_error)
    View communal_error;
    @BindView(R.id.communal_empty)
    View communal_empty;
    private DirectLogisticsHeadView dirLogisticHeadView;
    private DirectLogisticsAdapter directLogisticsAdapter;
    private List<ListBean> logisticsBeanList = new ArrayList<>();
    private String orderProductId;
    private String orderRefundProductId;

    @Override
    protected int getContentView() {
        return R.layout.layout_communal_refresh_recycle_header;
    }

    @Override
    protected void initViews() {
        tv_header_titleAll.setText("物流信息");
        Intent intent = getIntent();
        orderProductId = intent.getStringExtra("orderProductId");
        orderRefundProductId = intent.getStringExtra("orderRefundProductId");
        if (TextUtils.isEmpty(orderProductId) || TextUtils.isEmpty(orderRefundProductId)) {
            showToast(DirectRepairLogisticDetailsActivity.this, R.string.variable_exception);
            finish();
        }
        tl_normal_bar.setSelected(true);
        header_shared.setVisibility(View.INVISIBLE);
        smart_communal_refresh.setOnRefreshListener((refreshLayout) -> {
            loadData();
        });
        View headerView = LayoutInflater.from(DirectRepairLogisticDetailsActivity.this).inflate(R.layout.layout_direct_logistics_head, (ViewGroup) communal_recycler.getParent(), false);
        dirLogisticHeadView = new DirectLogisticsHeadView();
        ButterKnife.bind(dirLogisticHeadView, headerView);
        dirLogisticHeadView.initViews();
        directLogisticsAdapter = new DirectLogisticsAdapter(logisticsBeanList);
        directLogisticsAdapter.addHeaderView(headerView);
        communal_recycler.setLayoutManager(new LinearLayoutManager(DirectRepairLogisticDetailsActivity.this));
        communal_recycler.setAdapter(directLogisticsAdapter);
        communal_load.setVisibility(View.VISIBLE);
    }

    @Override
    protected void loadData() {
        if (NetWorkUtils.checkNet(DirectRepairLogisticDetailsActivity.this)) {
            String url = Url.BASE_URL + Url.Q_INDENT_REPAIR_LOGISTIC;
            Map<String, Object> params = new HashMap<>();
            params.put("orderRefundProductId", orderRefundProductId);
            params.put("orderProductId", orderProductId);
            XUtil.Post(url, params, new MyCallBack<String>() {
                @Override
                public void onSuccess(String result) {
                    smart_communal_refresh.finishRefresh();
                    communal_load.setVisibility(View.GONE);
                    logisticsBeanList.clear();
                    DirectRepairLogisticsEntity directLogisticsEntity = DirectRepairLogisticsEntity.objectFromData(result);
                    if (directLogisticsEntity != null) {
                        if (directLogisticsEntity.getCode().equals("01")) {
                            setRepairLogisticsData(directLogisticsEntity.getDirectRepairLogisticsBean());
                        } else {
                            showToast(DirectRepairLogisticDetailsActivity.this, directLogisticsEntity.getMsg());
                        }
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    smart_communal_refresh.finishRefresh();
                    communal_load.setVisibility(View.GONE);
                    showToast(DirectRepairLogisticDetailsActivity.this, R.string.invalidData);
                }
            });
        } else {
            smart_communal_refresh.finishRefresh();
            communal_load.setVisibility(View.GONE);
        }
    }

    private void setRepairLogisticsData(DirectRepairLogisticsBean directRepairLogisticsBean) {
        dirLogisticHeadView.tv_logistic_indent_dispatch_way.setText(("物流公司：" + directRepairLogisticsBean.getRepairReturnExpressCompany()));
        dirLogisticHeadView.tv_logistic_indent_exp_number.setText(("物流单号：" + directRepairLogisticsBean.getRepairReturnExpressNo()));
        if (directRepairLogisticsBean.getLogistics() != null
                && directRepairLogisticsBean.getLogistics().size() > 0) {
            logisticsBeanList.addAll(directRepairLogisticsBean.getLogistics());
            directLogisticsAdapter.notifyDataSetChanged();
        }
    }

    class DirectLogisticsHeadView {
        @BindView(R.id.communal_recycler_wrap)
        RecyclerView communal_recycler_wrap;
        //        配送方式
        @BindView(R.id.tv_logistics_indent_dispatch_way)
        TextView tv_logistic_indent_dispatch_way;
        //        订单号
        @BindView(R.id.tv_logistics_indent_express_number)
        TextView tv_logistic_indent_exp_number;

        public void initViews() {
            communal_recycler_wrap.setVisibility(View.GONE);
        }
    }

    @OnClick({R.id.rel_communal_error, R.id.communal_empty})
    void refreshData(View view) {
        communal_load.setVisibility(View.VISIBLE);
        communal_empty.setVisibility(View.GONE);
        communal_error.setVisibility(View.GONE);
        loadData();
    }

    @OnClick(R.id.tv_indent_back)
    void goBack() {
        finish();
    }
}
