package com.amkj.dmsh.shopdetails.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.base.NetLoadUtils;
import com.amkj.dmsh.base.TinkerBaseApplicationLike;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.shopdetails.activity.DoMoRefundDetailActivity;
import com.amkj.dmsh.shopdetails.adapter.DirectSalesReturnRecordAdapter;
import com.amkj.dmsh.shopdetails.bean.DirectReturnRecordEntity;
import com.amkj.dmsh.shopdetails.bean.DirectReturnRecordEntity.DirectReturnRecordBean.OrderListBean;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;
import com.google.gson.Gson;
import com.melnykov.fab.FloatingActionButton;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.tencent.bugly.beta.tinker.TinkerManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.DEFAULT_TOTAL_COUNT;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.INDENT_PRO_STATUS;
import static com.amkj.dmsh.constant.ConstantVariable.REFUND_REPAIR;
import static com.amkj.dmsh.constant.ConstantVariable.REFUND_TYPE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;

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

        smart_communal_refresh.setOnRefreshListener((refreshLayout) -> {
            //                滚动距离置0
            scrollY = 0;
            loadData();
        });
        directSalesReturnListAdapter.setOnLoadMoreListener(() -> {
            if (page * DEFAULT_TOTAL_COUNT <= directSalesReturnListAdapter.getItemCount()) {
                page++;
                getReturnReplyData();
            } else {
                directSalesReturnListAdapter.loadMoreEnd();
            }
        }, communal_recycler);
        download_btn_communal.attachToRecyclerView(communal_recycler, null, new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (!recyclerView.canScrollVertically(-1)) {
                    scrollY = 0;
                }
                scrollY += dy;
                if (screenHeight == 0) {
                    TinkerBaseApplicationLike app = (TinkerBaseApplicationLike) TinkerManager.getTinkerApplicationLike();
                    screenHeight = app.getScreenHeight();
                }
                if (scrollY >= screenHeight) {
                    download_btn_communal.setVisibility(View.VISIBLE);
                } else {
                    download_btn_communal.setVisibility(View.GONE);
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
        String url = Url.BASE_URL + Url.Q_APPLY_AFTER_SALE_REPLY_RECORD;
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("showCount", DEFAULT_TOTAL_COUNT);
        params.put("currentPage", page);
        NetLoadUtils.getQyInstance().loadNetDataPost(mAppContext, url, params, new NetLoadUtils.NetLoadListener() {
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
                }
                directSalesReturnListAdapter.notifyDataSetChanged();
                NetLoadUtils.getQyInstance().showLoadSir(loadService,code);
            }

            @Override
            public void netClose() {
                smart_communal_refresh.finishRefresh();
                directSalesReturnListAdapter.loadMoreComplete();
                NetLoadUtils.getQyInstance().showLoadSir(loadService,inquiryOrderEntry);
            }

            @Override
            public void onError(Throwable throwable) {
                smart_communal_refresh.finishRefresh();
                directSalesReturnListAdapter.loadMoreComplete();
                NetLoadUtils.getQyInstance().showLoadSir(loadService,inquiryOrderEntry);
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
