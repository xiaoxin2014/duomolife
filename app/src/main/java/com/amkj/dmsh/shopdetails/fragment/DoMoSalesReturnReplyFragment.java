package com.amkj.dmsh.shopdetails.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.base.TinkerBaseApplicationLike;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.shopdetails.activity.DoMoRefundDetailActivity;
import com.amkj.dmsh.shopdetails.adapter.DirectSalesReturnRecordAdapter;
import com.amkj.dmsh.shopdetails.bean.DirectReturnRecordEntity;
import com.amkj.dmsh.shopdetails.bean.DirectReturnRecordEntity.DirectReturnRecordBean.OrderListBean;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;
import com.google.gson.Gson;
import com.melnykov.fab.FloatingActionButton;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tencent.bugly.beta.tinker.TinkerManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.INDENT_PRO_STATUS;
import static com.amkj.dmsh.constant.ConstantVariable.REFUND_REPAIR;
import static com.amkj.dmsh.constant.ConstantVariable.REFUND_TYPE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.TOTAL_COUNT_TEN;

;
;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/2/23
 * class description:退货申诉
 */

public class DoMoSalesReturnReplyFragment extends BaseFragment {
    @BindView(R.id.smart_communal_refresh)
    SmartRefreshLayout smart_communal_refresh;
    @BindView(R.id.communal_recycler)
    RecyclerView communal_recycler;
    //    滚动至顶部
    @BindView(R.id.download_btn_communal)
    public FloatingActionButton download_btn_communal;
    private List<OrderListBean> orderListBeanList = new ArrayList();
    private int page = 1;
    private DirectSalesReturnRecordAdapter directSalesReturnListAdapter;
    //    reply 退货申诉 record 申诉记录
    private boolean isOnPause;
    private int scrollY = 0;
    private float screenHeight;
    private DirectReturnRecordEntity inquiryOrderEntry;

    @Override
    protected int getContentView() {
        return R.layout.layout_communal_smart_refresh_recycler_float_loading;
    }

    @Override
    protected void initViews() {
        communal_recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        communal_recycler.addItemDecoration(new ItemDecoration.Builder()
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_five_dp)
                .create());
        directSalesReturnListAdapter = new DirectSalesReturnRecordAdapter(getActivity(), orderListBeanList);
        communal_recycler.setAdapter(directSalesReturnListAdapter);

        smart_communal_refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                //                滚动距离置0
                scrollY = 0;
                loadData();
            }
        });
        directSalesReturnListAdapter.setOnLoadMoreListener(() -> {
            page++;
            getReturnReplyData();
        }, communal_recycler);
        TinkerBaseApplicationLike app = (TinkerBaseApplicationLike) TinkerManager.getTinkerApplicationLike();
        screenHeight = app.getScreenHeight();
        communal_recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                scrollY += dy;
                if (!recyclerView.canScrollVertically(-1)) {
                    scrollY = 0;
                }
                if (scrollY > screenHeight * 1.5 && dy < 0) {
                    if (download_btn_communal.getVisibility() == GONE) {
                        download_btn_communal.setVisibility(VISIBLE);
                        download_btn_communal.hide(false);
                    }
                    if (!download_btn_communal.isVisible()) {
                        download_btn_communal.show();
                    }
                } else {
                    if (download_btn_communal.isVisible()) {
                        download_btn_communal.hide();
                    }
                }
            }
        });
        download_btn_communal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) communal_recycler.getLayoutManager();
                int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
                int mVisibleCount = linearLayoutManager.findLastVisibleItemPosition()
                        - linearLayoutManager.findFirstVisibleItemPosition() + 1;
                if (firstVisibleItemPosition > mVisibleCount) {
                    communal_recycler.scrollToPosition(mVisibleCount);
                }
                communal_recycler.smoothScrollToPosition(0);
            }
        });

        directSalesReturnListAdapter.setOnItemClickListener((adapter, view, position) -> {
            OrderListBean orderListBean = (OrderListBean) view.getTag();
            if (orderListBean != null) {
                Intent intent = new Intent(getActivity(), DoMoRefundDetailActivity.class);
                if (50 <= orderListBean.getStatus() && orderListBean.getStatus() <= 58) {
                    intent.putExtra(REFUND_TYPE, REFUND_REPAIR);
                } else {
                    intent.putExtra(REFUND_TYPE, REFUND_TYPE);
                }
                intent.putExtra("no", orderListBean.getNo());
                intent.putExtra("orderProductId", String.valueOf(orderListBean.getOrderProductId()));
                intent.putExtra("orderRefundProductId", String.valueOf(orderListBean.getOrderRefundProductId()));
                startActivity(intent);
            }
        });
        directSalesReturnListAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            OrderListBean orderListBean = (OrderListBean) view.getTag();
            if (orderListBean != null) {
                Intent intent = new Intent(getActivity(), DoMoRefundDetailActivity.class);
                if (50 <= orderListBean.getStatus() && orderListBean.getStatus() <= 58) {
                    intent.putExtra(REFUND_TYPE, REFUND_REPAIR);
                } else {
                    intent.putExtra(REFUND_TYPE, REFUND_TYPE);
                }
                intent.putExtra("no", orderListBean.getNo());
                intent.putExtra("orderProductId", String.valueOf(orderListBean.getOrderProductId()));
                intent.putExtra("orderRefundProductId", String.valueOf(orderListBean.getOrderRefundProductId()));
                startActivity(intent);
            }
        });
    }

    //设置显示
    //第一个设置
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        setUserVisibleHint(true);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    protected void loadData() {
        page = 1;
        getReturnReplyData();
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    private void getReturnReplyData() {
        String url =  Url.Q_APPLY_AFTER_SALE_REPLY_RECORD;
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("showCount", TOTAL_COUNT_TEN);
        params.put("currentPage", page);
        NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(), url, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                smart_communal_refresh.finishRefresh();
                directSalesReturnListAdapter.loadMoreComplete();
                String code = "";
                String msg = "";
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    code = (String) jsonObject.get("code");
                    msg = (String) jsonObject.get("msg");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (page == 1) {
                    orderListBeanList.clear();
                }
                if (code.equals(SUCCESS_CODE)) {
                    Gson gson = new Gson();
                    inquiryOrderEntry = gson.fromJson(result, DirectReturnRecordEntity.class);
                    INDENT_PRO_STATUS = inquiryOrderEntry.getDirectReturnRecordBean().getStatus();
                    orderListBeanList.addAll(inquiryOrderEntry.getDirectReturnRecordBean().getOrderList());
                } else if (!code.equals(EMPTY_CODE)) {
                    showToast(getActivity(), msg);
                }else{
                    directSalesReturnListAdapter.loadMoreEnd();
                }
                directSalesReturnListAdapter.notifyDataSetChanged();
                NetLoadUtils.getNetInstance().showLoadSirString(loadService, orderListBeanList, code);
            }

            @Override
            public void onNotNetOrException() {
                smart_communal_refresh.finishRefresh();
                directSalesReturnListAdapter.loadMoreEnd(true);
                NetLoadUtils.getNetInstance().showLoadSir(loadService, orderListBeanList, inquiryOrderEntry);
            }
        });
    }

    public void onPause() {
        super.onPause();
        isOnPause = true;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isOnPause) {
            loadData();
        }
    }
}
