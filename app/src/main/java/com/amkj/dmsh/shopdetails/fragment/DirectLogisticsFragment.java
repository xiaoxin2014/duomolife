package com.amkj.dmsh.shopdetails.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.constant.XUtil;
import com.amkj.dmsh.shopdetails.adapter.DirectLogisticsAdapter;
import com.amkj.dmsh.shopdetails.adapter.DirectLogisticsHeaderAdapter;
import com.amkj.dmsh.shopdetails.bean.DirectLogisticsEntity.DirectLogisticsBean;
import com.amkj.dmsh.shopdetails.bean.DirectLogisticsEntity.DirectLogisticsBean.LogisticsBean;
import com.amkj.dmsh.shopdetails.bean.DirectLogisticsPacketEntity;
import com.amkj.dmsh.shopdetails.bean.DirectLogisticsPacketEntity.DirectLogisticsPacketBean.LogisticsEntity.LogisticsBean.ListBean;
import com.amkj.dmsh.utils.inteface.MyCallBack;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.amkj.dmsh.constant.ConstantMethod.showToast;

;



/**
 * Created by atd48 on 2016/10/31.
 */
public class DirectLogisticsFragment extends BaseFragment {
    @BindView(R.id.communal_recycler)
    RecyclerView communal_recycler;
    @BindView(R.id.smart_communal_refresh)
    RefreshLayout smart_communal_refresh;
    @BindView(R.id.communal_load)
    View communal_load;
    @BindView(R.id.communal_error)
    View communal_error;
    @BindView(R.id.communal_empty)
    View communal_empty;
    private DirectLogisticsHeadView dirLogistHeadView;
    private DirectLogisticsAdapter directLogisticsAdapter;
    private int packet;
    private DirectLogisticsBean directLogisticsBean;
    private List<ListBean> logisticsBeanList = new ArrayList<>();
    private LogisticsBean logisticsBean;

    @Override
    protected int getContentView() {
        return R.layout.layout_communal_smart_refresh_recycler_float_loading;
    }
    @Override
    protected void initViews() {
        View headerView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_direct_logistics_head, (ViewGroup) communal_recycler.getParent(), false);
        dirLogistHeadView = new DirectLogisticsHeadView();
        ButterKnife.bind(dirLogistHeadView, headerView);
//        头部商品信息
        DirectLogisticsHeaderAdapter headAdapter = new DirectLogisticsHeaderAdapter(getActivity(), directLogisticsBean.getLogistics().get(packet));
        dirLogistHeadView.communal_recycler_wrap.setLayoutManager(new LinearLayoutManager(getActivity()));
        dirLogistHeadView.communal_recycler_wrap.setAdapter(headAdapter);
        directLogisticsAdapter = new DirectLogisticsAdapter(logisticsBeanList);
        directLogisticsAdapter.addHeaderView(headerView);
        communal_recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        communal_recycler.setAdapter(directLogisticsAdapter);
        smart_communal_refresh.setOnRefreshListener((refreshLayout) -> {
            loadData();
        });
        communal_load.setVisibility(View.VISIBLE);
        if(directLogisticsBean!=null){
            logisticsBean = directLogisticsBean.getLogistics().get(packet).get(0);
        }
        setLogisticData();
    }

    private void setLogisticData() {
        dirLogistHeadView.tv_logis_indent_dispatch_way.setText(("物流公司：" + logisticsBean.getExpressCompany()));
        dirLogistHeadView.tv_logis_indent_exp_number.setText(("物流单号：" + logisticsBean.getExpressNo()));
    }

    @Override
    protected void loadData() {
        String url = Url.BASE_URL + Url.Q_CONFIRM_PACKET;
        Map<String, Object> params = new HashMap<>();
        params.put("orderProductId", logisticsBean.getOrderProductId());
        XUtil.Post(url, params, new MyCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                smart_communal_refresh.finishRefresh();
                communal_load.setVisibility(View.GONE);
                communal_error.setVisibility(View.GONE);
                logisticsBeanList.clear();
                Gson gson = new Gson();
                DirectLogisticsPacketEntity directLogisticsPacketEntity = gson.fromJson(result, DirectLogisticsPacketEntity.class);
                if (directLogisticsPacketEntity != null) {
                    if (directLogisticsPacketEntity.getCode().equals("01")) {
                        logisticsBeanList.addAll(directLogisticsPacketEntity.getDirectLogisticsPacketBean().getLogisticsEntity().getLogisticsBean().getList());
                        directLogisticsAdapter.setNewData(logisticsBeanList);
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                smart_communal_refresh.finishRefresh();
                communal_load.setVisibility(View.GONE);
                communal_error.setVisibility(View.VISIBLE);
                showToast(getActivity(), R.string.unConnectedNetwork);
                super.onError(ex, isOnCallback);
            }
        });
    }

    @Override
    protected void getReqParams(Bundle bundle) {
//        数据信息
//        Map<String,Object> objectParams = new HashMap<>();
        directLogisticsBean = (DirectLogisticsBean) bundle.get("directLogisticsBean");
        packet = Integer.parseInt((String) bundle.get("packet"));
    }

    class DirectLogisticsHeadView {
        @BindView(R.id.communal_recycler_wrap)
        RecyclerView communal_recycler_wrap;
        //        配送方式
        @BindView(R.id.tv_logistics_indent_dispatch_way)
        TextView tv_logis_indent_dispatch_way;
        //        订单号
        @BindView(R.id.tv_logistics_indent_express_number)
        TextView tv_logis_indent_exp_number;

    }

    @OnClick({R.id.rel_communal_error, R.id.communal_empty})
    void refreshData(View view) {
        communal_load.setVisibility(View.VISIBLE);
        communal_empty.setVisibility(View.GONE);
        communal_error.setVisibility(View.GONE);
        loadData();
    }
}
