package com.amkj.dmsh.shopdetails.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.shopdetails.adapter.DirectLogisticsAdapter;
import com.amkj.dmsh.shopdetails.adapter.DirectLogisticsHeaderAdapter;
import com.amkj.dmsh.shopdetails.bean.DirectLogisticPacketBean;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/1/23
 * class description:订单物流查询
 */
public class DirectLogisticsFragment extends BaseFragment {
    @BindView(R.id.communal_recycler)
    RecyclerView communal_recycler;
    private DirectLogisticsHeadView directLogisticsHeadView;
    private DirectLogisticsAdapter directLogisticsAdapter;
    private DirectLogisticPacketBean directLogisticPacketBean;

    @Override
    protected int getContentView() {
        return R.layout.layout_communal_recycler;
    }

    @Override
    protected void initViews() {
        if(directLogisticPacketBean ==null){
            return;
        }
        View headerView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_direct_logistics_header, null, false);
        directLogisticsHeadView = new DirectLogisticsHeadView();
        ButterKnife.bind(directLogisticsHeadView, headerView);
        //头部商品信息
        DirectLogisticsHeaderAdapter directLogisticsHeaderAdapter = new DirectLogisticsHeaderAdapter(getActivity(), directLogisticPacketBean.getDirectGoods());
        directLogisticsHeadView.communal_recycler_wrap.setLayoutManager(new LinearLayoutManager(getActivity()));
        directLogisticsHeadView.communal_recycler_wrap.setAdapter(directLogisticsHeaderAdapter);
        directLogisticsHeadView.communal_recycler_wrap.setNestedScrollingEnabled(false);
        directLogisticsAdapter = new DirectLogisticsAdapter(directLogisticPacketBean.getLogisticPacketList());
        directLogisticsAdapter.addHeaderView(headerView);
        communal_recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        communal_recycler.setAdapter(directLogisticsAdapter);
        setLogisticData();
    }

    private void setLogisticData() {
        directLogisticsHeadView.tv_logis_indent_dispatch_way.setText(("物流公司：" + directLogisticPacketBean.getExpressCompany()));
        directLogisticsHeadView.tv_logis_indent_exp_number.setText(("物流单号：" + directLogisticPacketBean.getExpressNo()));
    }

    @Override
    protected void loadData() {
    }

    @Override
    protected boolean isAddLoad() {
        return false;
    }

    @Override
    protected void getReqParams(Bundle bundle) {
//        数据信息
        directLogisticPacketBean = bundle.getParcelable("DirectLogisticPacketBean");
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
}
