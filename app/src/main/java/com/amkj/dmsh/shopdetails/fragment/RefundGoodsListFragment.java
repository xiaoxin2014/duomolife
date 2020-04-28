package com.amkj.dmsh.shopdetails.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.shopdetails.adapter.DirectSalesReturnRecordAdapter;
import com.amkj.dmsh.shopdetails.bean.MainOrderListEntity;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;
import com.google.gson.Gson;
import com.melnykov.fab.FloatingActionButton;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.TOTAL_COUNT_TEN;


/**
 * Created by atd48 on 2016/8/25.
 * 售后 --->>申诉记录
 */
public class RefundGoodsListFragment extends BaseFragment {
    @BindView(R.id.smart_communal_refresh)
    SmartRefreshLayout smart_communal_refresh;
    @BindView(R.id.communal_recycler)
    RecyclerView communal_recycler;
    //    滚动至顶部
    @BindView(R.id.download_btn_communal)
    public FloatingActionButton download_btn_communal;
    private List<MainOrderListEntity.MainOrderBean> orderList = new ArrayList<>();
    private DirectSalesReturnRecordAdapter directSalesReturnListAdapter;
    private MainOrderListEntity returnRecordEntity;
    private int page = 1;
    private int position;

    @Override
    protected int getContentView() {
        return R.layout.layout_communal_smart_refresh_recycler_float_loading;
    }

    @Override
    protected void initViews() {
        communal_recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        communal_recycler.addItemDecoration(new ItemDecoration.Builder()
                .setDividerId(R.drawable.item_divider_five_dp)
                .create());
        directSalesReturnListAdapter = new DirectSalesReturnRecordAdapter(getActivity(), orderList);
        communal_recycler.setAdapter(directSalesReturnListAdapter);
        smart_communal_refresh.setOnRefreshListener(refreshLayout -> {
            //                滚动距离置0
            scrollY = 0;
            loadData();
        });
        directSalesReturnListAdapter.setOnLoadMoreListener(() -> {
            page++;
            getSaleReturnRecordData();
        }, communal_recycler);
        setFloatingButton(download_btn_communal, communal_recycler);
    }

    @Override
    protected void loadData() {
        page = 1;
        getSaleReturnRecordData();
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    private void getSaleReturnRecordData() {
        Map<String, Object> params = new HashMap<>();
        params.put("showCount", TOTAL_COUNT_TEN);
        params.put("currentPage", page);
        params.put("afterSaleType", position == 1 ? "success" : "");
        NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(), Url.Q_APPLY_AFTER_SALE_REPLY_RECORD, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                smart_communal_refresh.finishRefresh();
                if (page == 1) {
                    orderList.clear();
                }

                returnRecordEntity = new Gson().fromJson(result, MainOrderListEntity.class);
                if (returnRecordEntity != null) {
                    String code = returnRecordEntity.getCode();
                    List<MainOrderListEntity.MainOrderBean> mainOrderBeans = returnRecordEntity.getResult();
                    if (mainOrderBeans != null && mainOrderBeans.size() > 0) {
                        orderList.addAll(returnRecordEntity.getResult());
                        directSalesReturnListAdapter.loadMoreComplete();
                    } else if (!SUCCESS_CODE.equals(code) && !EMPTY_CODE.equals(code)) {
                        showToast( returnRecordEntity.getMsg());
                        directSalesReturnListAdapter.loadMoreFail();
                    } else {
                        directSalesReturnListAdapter.loadMoreEnd();
                    }
                }

                directSalesReturnListAdapter.notifyDataSetChanged();
                NetLoadUtils.getNetInstance().showLoadSir(loadService, orderList, returnRecordEntity);
            }

            @Override
            public void onNotNetOrException() {
                smart_communal_refresh.finishRefresh();
                directSalesReturnListAdapter.loadMoreFail();
                NetLoadUtils.getNetInstance().showLoadSir(loadService, orderList, returnRecordEntity);
            }
        });
    }

    @Override
    protected void getReqParams(Bundle bundle) {
        if (bundle != null) {
            position = (int) bundle.get("position");
        }
    }
}
