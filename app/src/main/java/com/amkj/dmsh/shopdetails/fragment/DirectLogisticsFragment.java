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
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.shopdetails.adapter.DirectLogisticsAdapter;
import com.amkj.dmsh.shopdetails.adapter.DirectLogisticsHeaderAdapter;
import com.amkj.dmsh.shopdetails.bean.DirectLogisticsEntity.DirectLogisticsBean;
import com.amkj.dmsh.shopdetails.bean.DirectLogisticsEntity.DirectLogisticsBean.LogisticsBean;
import com.amkj.dmsh.shopdetails.bean.DirectLogisticsPacketEntity;
import com.amkj.dmsh.shopdetails.bean.DirectLogisticsPacketEntity.DirectLogisticsPacketBean.LogisticsEntity.LogisticsBean.ListBean;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;

;
;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/1/23
 * class description:订单物流查询
 */
public class DirectLogisticsFragment extends BaseFragment {
    @BindView(R.id.communal_recycler)
    RecyclerView communal_recycler;
    @BindView(R.id.smart_communal_refresh)
    RefreshLayout smart_communal_refresh;
    private DirectLogisticsHeadView directLogisticsHeadView;
    private DirectLogisticsAdapter directLogisticsAdapter;
    private int packet;
    private DirectLogisticsBean directLogisticsBean;
    private List<ListBean> logisticsBeanList = new ArrayList<>();
    private LogisticsBean logisticsBean;
    private DirectLogisticsPacketEntity directLogisticsPacketEntity;

    @Override
    protected int getContentView() {
        return R.layout.layout_communal_smart_refresh_recycler_float_loading;
    }

    @Override
    protected void initViews() {
        View headerView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_direct_logistics_head, (ViewGroup) communal_recycler.getParent(), false);
        directLogisticsHeadView = new DirectLogisticsHeadView();
        ButterKnife.bind(directLogisticsHeadView, headerView);
//        头部商品信息
        DirectLogisticsHeaderAdapter headAdapter = new DirectLogisticsHeaderAdapter(getActivity(), directLogisticsBean.getLogistics().get(packet));
        directLogisticsHeadView.communal_recycler_wrap.setLayoutManager(new LinearLayoutManager(getActivity()));
        directLogisticsHeadView.communal_recycler_wrap.setAdapter(headAdapter);
        directLogisticsAdapter = new DirectLogisticsAdapter(logisticsBeanList);
        directLogisticsAdapter.addHeaderView(headerView);
        communal_recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        communal_recycler.setAdapter(directLogisticsAdapter);
        smart_communal_refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                loadData();
            }
        });
        if (directLogisticsBean != null) {
            logisticsBean = directLogisticsBean.getLogistics().get(packet).get(0);
        }
        setLogisticData();
    }

    private void setLogisticData() {
        directLogisticsHeadView.tv_logis_indent_dispatch_way.setText(("物流公司：" + logisticsBean.getExpressCompany()));
        directLogisticsHeadView.tv_logis_indent_exp_number.setText(("物流单号：" + logisticsBean.getExpressNo()));
    }

    @Override
    protected void loadData() {
        String url = Url.BASE_URL + Url.Q_CONFIRM_PACKET;
        Map<String, Object> params = new HashMap<>();
        params.put("orderProductId", logisticsBean.getOrderProductId());
        NetLoadUtils.getQyInstance().loadNetDataPost(mAppContext, url, params, new NetLoadUtils.NetLoadListener() {
            @Override
            public void onSuccess(String result) {
                smart_communal_refresh.finishRefresh();
                logisticsBeanList.clear();
                Gson gson = new Gson();
                directLogisticsPacketEntity = gson.fromJson(result, DirectLogisticsPacketEntity.class);
                if (directLogisticsPacketEntity != null) {
                    if (directLogisticsPacketEntity.getCode().equals(SUCCESS_CODE)) {
                        logisticsBeanList.addAll(directLogisticsPacketEntity.getDirectLogisticsPacketBean().getLogisticsEntity().getLogisticsBean().getList());
                        directLogisticsAdapter.setNewData(logisticsBeanList);
                    }
                }
                NetLoadUtils.getQyInstance().showLoadSir(loadService, directLogisticsPacketEntity);
            }

            @Override
            public void netClose() {
                smart_communal_refresh.finishRefresh();
                showToast(getActivity(), R.string.unConnectedNetwork);
                NetLoadUtils.getQyInstance().showLoadSir(loadService, directLogisticsPacketEntity);
            }

            @Override
            public void onError(Throwable throwable) {
                smart_communal_refresh.finishRefresh();
                showToast(getActivity(), R.string.invalidData);
                NetLoadUtils.getQyInstance().showLoadSir(loadService, directLogisticsPacketEntity);
            }
        });
    }

    @Override
    protected boolean isAddLoad() {
        return true;
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
}
