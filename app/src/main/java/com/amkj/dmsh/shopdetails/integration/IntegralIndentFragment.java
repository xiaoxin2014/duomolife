package com.amkj.dmsh.shopdetails.integration;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.base.NetLoadUtils;
import com.amkj.dmsh.base.TinkerBaseApplicationLike;
import com.amkj.dmsh.bean.RequestStatus;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.constant.XUtil;
import com.amkj.dmsh.mine.adapter.IntegralIndentListAdapter;
import com.amkj.dmsh.release.dialogutils.AlertSettingBean;
import com.amkj.dmsh.release.dialogutils.AlertView;
import com.amkj.dmsh.release.dialogutils.OnAlertItemClickListener;
import com.amkj.dmsh.shopdetails.activity.DirectLogisticsDetailsActivity;
import com.amkj.dmsh.shopdetails.activity.DirectMyCouponActivity;
import com.amkj.dmsh.shopdetails.activity.DirectPublishAppraiseActivity;
import com.amkj.dmsh.shopdetails.bean.DirectAppraisePassBean;
import com.amkj.dmsh.shopdetails.integration.bean.IntegralIndentOrderEntity;
import com.amkj.dmsh.shopdetails.integration.bean.IntegralIndentOrderEntity.IntegralIndentOrderBean.OrderListBean;
import com.amkj.dmsh.shopdetails.integration.bean.IntegralIndentOrderEntity.IntegralIndentOrderBean.OrderListBean.GoodsBean;
import com.amkj.dmsh.utils.inteface.MyCallBack;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.melnykov.fab.FloatingActionButton;
import com.amkj.dmsh.utils.pinnedsectionitemdecoration.PinnedHeaderItemDecoration;
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
import static com.amkj.dmsh.constant.ConstantMethod.getEmptyView;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.CANCEL_ORDER;
import static com.amkj.dmsh.constant.ConstantVariable.CANCEL_PAY_ORDER;
import static com.amkj.dmsh.constant.ConstantVariable.CHECK_LOG;
import static com.amkj.dmsh.constant.ConstantVariable.CONFIRM_ORDER;
import static com.amkj.dmsh.constant.ConstantVariable.DEFAULT_TOTAL_COUNT;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.INDENT_PRO_STATUS;
import static com.amkj.dmsh.constant.ConstantVariable.LITTER_CONSIGN;
import static com.amkj.dmsh.constant.ConstantVariable.PAY;
import static com.amkj.dmsh.constant.ConstantVariable.PRO_APPRAISE;
import static com.amkj.dmsh.constant.ConstantVariable.REFUND_APPLY;
import static com.amkj.dmsh.constant.ConstantVariable.REFUND_FEEDBACK;
import static com.amkj.dmsh.constant.ConstantVariable.REFUND_INTEGRAL_REPAIR;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.VIRTUAL_COUPON;

;


/**
 * Created by atd48 on 2016/8/25.
 */
public class IntegralIndentFragment extends BaseFragment implements OnAlertItemClickListener {
    @BindView(R.id.smart_communal_refresh)
    SmartRefreshLayout smart_communal_refresh;
    @BindView(R.id.communal_recycler)
    RecyclerView communal_recycler;
    //    滚动至顶部
    @BindView(R.id.download_btn_communal)
    public FloatingActionButton download_btn_communal;
    private List<OrderListBean> orderListBeanList = new ArrayList();
    private List<DirectAppraisePassBean> directAppraisePassList = new ArrayList<>();
    private String typeStatus;
    private IntegralIndentListAdapter integralIndentListAdapter;
    private int page = 1;
    private boolean isOnPause;
    private int scrollY = 0;
    private float screenHeight;
    private AlertView cancelOrderDialog;
    private AlertView confirmOrderDialog;
    private OrderListBean orderListBean;

    @Override
    protected int getContentView() {
        return R.layout.layout_communal_smart_refresh_recycler_float_loading;
    }

    @Override
    protected void initViews() {
        communal_recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        communal_recycler.addItemDecoration(new PinnedHeaderItemDecoration.Builder(-1)
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_ten_dp)
                // 开启绘制分隔线，默认关闭
                .enableDivider(true)
                // 是否关闭标签点击事件，默认开启
                .disableHeaderClick(false)
                // 设置标签和其内部的子控件的监听，若设置点击监听不为null，但是disableHeaderClick(true)的话，还是不会响应点击事件
                .setHeaderClickListener(null)
                .create());
        integralIndentListAdapter = new IntegralIndentListAdapter(getActivity(), orderListBeanList);
        communal_recycler.setAdapter(integralIndentListAdapter);
        integralIndentListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                OrderListBean orderListBean = (OrderListBean) view.getTag();
                if (orderListBean != null) {
                    Intent intent = new Intent(getActivity(), IntegExchangeDetailActivity.class);
                    intent.putExtra("orderNo", orderListBean.getNo());
                    startActivity(intent);
                }
            }
        });
        smart_communal_refresh.setOnRefreshListener((refreshLayout) -> {
            scrollY = 0;
            loadData();
        });
        integralIndentListAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (page * DEFAULT_TOTAL_COUNT <= orderListBeanList.size()) {
                    page++;
                    getIntegralIndentList();
                } else {
                    integralIndentListAdapter.loadMoreEnd();
                }
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
        integralIndentListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                OrderListBean orderListBean = (OrderListBean) view.getTag();
                if (orderListBean != null) {
                    // 2016/8/24  跳转订单详情
                    Intent intent = new Intent(getActivity(), IntegExchangeDetailActivity.class);
                    intent.putExtra("orderNo", orderListBean.getNo());
                    startActivity(intent);
                }
            }
        });
        integralIndentListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                orderListBean = (OrderListBean) view.getTag(R.id.tag_second);
                String indentStatus = (String) view.getTag(R.id.tag_first);
                if (orderListBean != null) {
                    Intent intent = new Intent();
                    Bundle bundle;
                    AlertSettingBean alertSettingBean;
                    AlertSettingBean.AlertData alertData;
                    switch (indentStatus) {
                        case CANCEL_ORDER:
//                        取消订单
                            alertSettingBean = new AlertSettingBean();
                            alertData = new AlertSettingBean.AlertData();
                            alertData.setCancelStr("取消");
                            alertData.setDetermineStr("确定");
                            alertData.setFirstDet(true);
                            alertData.setMsg("确定要取消当前订单");
                            alertSettingBean.setStyle(AlertView.Style.Alert);
                            alertSettingBean.setAlertData(alertData);
                            cancelOrderDialog = new AlertView(alertSettingBean, getActivity(), IntegralIndentFragment.this);
                            cancelOrderDialog.setCancelable(true);
                            cancelOrderDialog.show();
                            break;
                        case CANCEL_PAY_ORDER:
                            intent.setClass(getActivity(), IntegExchangeDetailActivity.class);
                            intent.putExtra("orderNo", orderListBean.getNo());
                            startActivity(intent);
                            break;
                        case PAY:
                            intent.setClass(getActivity(), IntegExchangeDetailActivity.class);
                            intent.putExtra("orderNo", orderListBean.getNo());
                            startActivity(intent);
                            break;
                        case CHECK_LOG:
                        case LITTER_CONSIGN:
//                        查看物流
                            intent.setClass(getActivity(), DirectLogisticsDetailsActivity.class);
                            intent.putExtra("orderNo", orderListBean.getNo());
                            startActivity(intent);
                            break;
                        case CONFIRM_ORDER:
//                        确认收货
                            alertSettingBean = new AlertSettingBean();
                            alertData = new AlertSettingBean.AlertData();
                            alertData.setCancelStr("取消");
                            alertData.setDetermineStr("确定");
                            alertData.setFirstDet(true);
                            alertData.setMsg("确定已收到货物");
                            alertSettingBean.setStyle(AlertView.Style.Alert);
                            alertSettingBean.setAlertData(alertData);
                            confirmOrderDialog = new AlertView(alertSettingBean, getActivity(), IntegralIndentFragment.this);
                            confirmOrderDialog.setCancelable(true);
                            confirmOrderDialog.show();
                            break;
                        case PRO_APPRAISE:
//                        评价
                            DirectAppraisePassBean directAppraisePassBean;
                            directAppraisePassList.clear();
                            for (int i = 0; i < orderListBean.getGoods().size(); i++) {
                                GoodsBean goodBean = orderListBean.getGoods().get(i);
                                if (goodBean.getStatus() == 30) {
                                    directAppraisePassBean = new DirectAppraisePassBean();
                                    directAppraisePassBean.setPath(goodBean.getPicUrl());
                                    directAppraisePassBean.setProductId(goodBean.getId());
                                    directAppraisePassBean.setStar(5);
                                    directAppraisePassBean.setOrderProductId(goodBean.getOrderProductId());
                                    directAppraisePassList.add(directAppraisePassBean);
                                }
                            }
                            if (directAppraisePassList.size() > 0) {
                                intent.setClass(getActivity(), DirectPublishAppraiseActivity.class);
                                bundle = new Bundle();
                                bundle.putParcelableArrayList("appraiseData", (ArrayList<? extends Parcelable>) directAppraisePassList);
                                intent.putExtras(bundle);
                                intent.putExtra("orderNo", orderListBean.getNo());
                                startActivity(intent);
                            }
                            break;
                        case VIRTUAL_COUPON:
                            intent.setClass(getActivity(), DirectMyCouponActivity.class);
                            startActivity(intent);
                            break;
                        case REFUND_APPLY:
                        case REFUND_FEEDBACK:
                        case REFUND_INTEGRAL_REPAIR:
                            intent.setClass(getActivity(), IntegExchangeDetailActivity.class);
                            intent.putExtra("orderNo", orderListBean.getNo());
                            startActivity(intent);
                            break;
                    }
                }
            }
        });
        integralIndentListAdapter.setEmptyView(getEmptyView(getActivity(), "订单"));
    }

    /**
     * 取消积分订单
     *
     * @param orderListBean
     */
    private void cancelIntegralIndent(OrderListBean orderListBean) {
        String url = Url.BASE_URL + Url.Q_INDENT_CANCEL;
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("no", orderListBean.getNo());
        XUtil.Post(url, params, new MyCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                RequestStatus requestStatus = gson.fromJson(result, RequestStatus.class);
                if (requestStatus != null) {
                    if (requestStatus.getCode().equals("01")) {
                        showToast(getActivity(), "取消订单成功");
                        loadData();
                    } else {
                        showToast(getActivity(), requestStatus.getMsg());
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                showToast(getActivity(), R.string.do_failed);
                super.onError(ex, isOnCallback);
            }
        });
    }

    @Override
    protected void getReqParams(Bundle bundle) {
        try {
            typeStatus = (String) bundle.get("statusCategory");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void loadData() {
        page = 1;
        getIntegralIndentList();
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    private void getIntegralIndentList() {
        if (userId > 0) {
            String url = Url.BASE_URL + Url.INTEGRAL_INDENT_LIST;
            Map<String, Object> params = new HashMap<>();
            params.put("userId", userId);
            params.put("showCount", DEFAULT_TOTAL_COUNT);
            params.put("currentPage", page);
            if (!TextUtils.isEmpty(typeStatus)) {
                params.put("statusCategory", typeStatus);
            }
            NetLoadUtils.getQyInstance().loadNetDataPost(mAppContext, url, params, new NetLoadUtils.NetLoadListener() {
                @Override
                public void onSuccess(String result) {
                    smart_communal_refresh.finishRefresh();
                    integralIndentListAdapter.loadMoreComplete();
                    Gson gson = new Gson();
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
                        IntegralIndentOrderEntity indentOrderEntity = gson.fromJson(result, IntegralIndentOrderEntity.class);
                        INDENT_PRO_STATUS = indentOrderEntity.getIntegralIndentOrderBean().getStatus();
                        orderListBeanList.addAll(indentOrderEntity.getIntegralIndentOrderBean().getOrderList());
                    } else if (!code.equals(EMPTY_CODE)) {
                        showToast(getActivity(), msg);
                    }
                    integralIndentListAdapter.notifyDataSetChanged();
                    NetLoadUtils.getQyInstance().showLoadSirSuccess(loadService);
                }

                @Override
                public void netClose() {
                    smart_communal_refresh.finishRefresh();
                    integralIndentListAdapter.loadMoreComplete();
                    showToast(getActivity(), R.string.unConnectedNetwork);
                    NetLoadUtils.getQyInstance().showLoadSirSuccess(loadService);
                }

                @Override
                public void onError(Throwable throwable) {
                    smart_communal_refresh.finishRefresh();
                    integralIndentListAdapter.loadMoreComplete();
                    NetLoadUtils.getQyInstance().showLoadSirSuccess(loadService);
                }
            });
        } else {
            NetLoadUtils.getQyInstance().showLoadSirSuccess(loadService);
        }
    }

    @Override
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

    @Override
    public void onAlertItemClick(Object o, int position) {
        if (o == cancelOrderDialog && position != AlertView.CANCELPOSITION) {
            cancelIntegralIndent(orderListBean);
        } else if (o == confirmOrderDialog && position != AlertView.CANCELPOSITION) {
            confirmIntegralOrder();
        }
    }

    /**
     * 取消积分订单
     */
    private void confirmIntegralOrder() {
        String url = Url.BASE_URL + Url.Q_INDENT_CONFIRM;
        Map<String, Object> params = new HashMap<>();
        params.put("no", orderListBean.getNo());
        params.put("userId", userId);
        params.put("orderProductId",/*orderBean.getId()*/0);
        XUtil.Post(url, params, new MyCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                RequestStatus requestStatus = gson.fromJson(result, RequestStatus.class);
                if (requestStatus != null) {
                    if (requestStatus.getCode().equals("01")) {
                        loadData();
                        showToast(getActivity(), requestStatus.getMsg());
                    } else {
                        showToast(getActivity(), requestStatus.getResult() != null ?
                                requestStatus.getResult().getMsg() : requestStatus.getMsg());
                    }
                }
            }
        });
    }
}
