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
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.shopdetails.adapter.DirectLogisticsAdapter;
import com.amkj.dmsh.shopdetails.bean.DirectLogisticsEntity.DirectLogisticsBean.LogisticsProductPacketBean.LogisticsDetailsBean.LogisticsBean.LogisticTextBean;
import com.amkj.dmsh.shopdetails.bean.DirectRepairLogisticsEntity;
import com.amkj.dmsh.shopdetails.bean.DirectRepairLogisticsEntity.DirectRepairLogisticsBean;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;

;


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
    SmartRefreshLayout smart_communal_refresh;
    @BindView(R.id.communal_recycler)
    RecyclerView communal_recycler;
    private DirectLogisticsHeadView dirLogisticHeadView;
    private DirectLogisticsAdapter directLogisticsAdapter;
    private List<LogisticTextBean> logisticsBeanList = new ArrayList();
    private String orderProductId;
    private String orderRefundProductId;
    private DirectRepairLogisticsEntity directLogisticsEntity;

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
        smart_communal_refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                loadData();
            }
        });
        View headerView = LayoutInflater.from(DirectRepairLogisticDetailsActivity.this).inflate(R.layout.layout_direct_logistics_header, (ViewGroup) communal_recycler.getParent(), false);
        dirLogisticHeadView = new DirectLogisticsHeadView();
        ButterKnife.bind(dirLogisticHeadView, headerView);
        dirLogisticHeadView.initViews();
        directLogisticsAdapter = new DirectLogisticsAdapter(logisticsBeanList);
        directLogisticsAdapter.addHeaderView(headerView);
        communal_recycler.setLayoutManager(new LinearLayoutManager(DirectRepairLogisticDetailsActivity.this));
        communal_recycler.setAdapter(directLogisticsAdapter);
    }

    @Override
    protected void loadData() {
        String url = Url.BASE_URL + Url.Q_INDENT_REPAIR_LOGISTIC;
        Map<String, Object> params = new HashMap<>();
        params.put("orderRefundProductId", orderRefundProductId);
        params.put("orderProductId", orderProductId);
        NetLoadUtils.getQyInstance().loadNetDataPost(mAppContext, url, params, new NetLoadUtils.NetLoadListener() {
            @Override
            public void onSuccess(String result) {
                smart_communal_refresh.finishRefresh();
                logisticsBeanList.clear();
                directLogisticsEntity = DirectRepairLogisticsEntity.objectFromData(result);
                if (directLogisticsEntity != null) {
                    if (directLogisticsEntity.getCode().equals(SUCCESS_CODE)) {
                        setRepairLogisticsData(directLogisticsEntity.getDirectRepairLogisticsBean());
                    } else if (!directLogisticsEntity.getCode().equals(EMPTY_CODE)) {
                        showToast(DirectRepairLogisticDetailsActivity.this, directLogisticsEntity.getMsg());
                    }
                }
                NetLoadUtils.getQyInstance().showLoadSir(loadService, directLogisticsEntity);
            }

            @Override
            public void netClose() {
                smart_communal_refresh.finishRefresh();
                showToast(DirectRepairLogisticDetailsActivity.this, R.string.unConnectedNetwork);
                NetLoadUtils.getQyInstance().showLoadSir(loadService, directLogisticsEntity);
            }

            @Override
            public void onError(Throwable throwable) {
                smart_communal_refresh.finishRefresh();
                showToast(DirectRepairLogisticDetailsActivity.this, R.string.invalidData);
                NetLoadUtils.getQyInstance().showLoadSir(loadService, directLogisticsEntity);
            }
        });
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    @Override
    protected View getLoadView() {
        return smart_communal_refresh;
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

    @OnClick(R.id.tv_indent_back)
    void goBack() {
        finish();
    }
}
