package com.amkj.dmsh.shopdetails.integration;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.base.TinkerBaseApplicationLike;
import com.amkj.dmsh.bean.RequestStatus;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.mine.adapter.IntegralIndentListAdapter;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.shopdetails.activity.DirectLogisticsDetailsActivity;
import com.amkj.dmsh.shopdetails.activity.DirectMyCouponActivity;
import com.amkj.dmsh.shopdetails.activity.DirectPublishAppraiseActivity;
import com.amkj.dmsh.shopdetails.bean.DirectAppraisePassBean;
import com.amkj.dmsh.shopdetails.integration.bean.IntegralIndentOrderEntity;
import com.amkj.dmsh.shopdetails.integration.bean.IntegralIndentOrderEntity.IntegralIndentOrderBean.OrderListBean;
import com.amkj.dmsh.shopdetails.integration.bean.IntegralIndentOrderEntity.IntegralIndentOrderBean.OrderListBean.GoodsBean;
import com.amkj.dmsh.utils.alertdialog.AlertDialogHelper;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;
import com.chad.library.adapter.base.BaseQuickAdapter;
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

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.CANCEL_ORDER;
import static com.amkj.dmsh.constant.ConstantVariable.CANCEL_PAY_ORDER;
import static com.amkj.dmsh.constant.ConstantVariable.CHECK_LOG;
import static com.amkj.dmsh.constant.ConstantVariable.CONFIRM_ORDER;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.INDENT_PRO_STATUS;
import static com.amkj.dmsh.constant.ConstantVariable.LITTER_CONSIGN;
import static com.amkj.dmsh.constant.ConstantVariable.PAY;
import static com.amkj.dmsh.constant.ConstantVariable.PRO_APPRAISE;
import static com.amkj.dmsh.constant.ConstantVariable.REFUND_APPLY;
import static com.amkj.dmsh.constant.ConstantVariable.REFUND_FEEDBACK;
import static com.amkj.dmsh.constant.ConstantVariable.REFUND_INTEGRAL_REPAIR;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.TOTAL_COUNT_TEN;
import static com.amkj.dmsh.constant.ConstantVariable.VIRTUAL_COUPON;

;


/**
 * Created by atd48 on 2016/8/25.
 */
public class IntegralIndentFragment extends BaseFragment {
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
    private OrderListBean orderListBean;
    private AlertDialogHelper cancelOrderDialogHelper;
    private AlertDialogHelper confirmOrderDialogHelper;
    private IntegralIndentOrderEntity indentOrderEntity;

    @Override
    protected int getContentView() {
        return R.layout.layout_communal_smart_refresh_recycler_float_loading;
    }

    @Override
    protected void initViews() {
        communal_recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        communal_recycler.addItemDecoration(new ItemDecoration.Builder()
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_ten_dp)


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
        smart_communal_refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                scrollY = 0;
                loadData();
            }
        });
        integralIndentListAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                page++;
                getIntegralIndentList();
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
                    switch (indentStatus) {
                        case CANCEL_ORDER:
//                        取消订单
                            if (cancelOrderDialogHelper == null) {
                                cancelOrderDialogHelper = new AlertDialogHelper(getActivity());
                                cancelOrderDialogHelper.setTitleVisibility(View.GONE).setMsgTextGravity(Gravity.CENTER)
                                        .setMsg("确定要取消当前订单？").setCancelText("取消").setConfirmText("确定")
                                        .setCancelTextColor(getResources().getColor(R.color.text_login_gray_s));
                                cancelOrderDialogHelper.setAlertListener(new AlertDialogHelper.AlertConfirmCancelListener() {
                                    @Override
                                    public void confirm() {
                                        cancelIntegralIndent(orderListBean);
                                    }

                                    @Override
                                    public void cancel() {
                                    }
                                });
                            }
                            cancelOrderDialogHelper.show();
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
                            if (confirmOrderDialogHelper == null) {
                                confirmOrderDialogHelper = new AlertDialogHelper(getActivity());
                                confirmOrderDialogHelper.setTitleVisibility(View.GONE).setMsgTextGravity(Gravity.CENTER)
                                        .setMsg("确定已收到货物?").setCancelText("取消").setConfirmText("确定")
                                        .setCancelTextColor(getResources().getColor(R.color.text_login_gray_s));
                                confirmOrderDialogHelper.setAlertListener(new AlertDialogHelper.AlertConfirmCancelListener() {
                                    @Override
                                    public void confirm() {
                                        confirmIntegralOrder();
                                    }

                                    @Override
                                    public void cancel() {
                                    }
                                });
                            }
                            confirmOrderDialogHelper.show();
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
        NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(),url,params,new NetLoadListenerHelper(){
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                RequestStatus requestStatus = gson.fromJson(result, RequestStatus.class);
                if (requestStatus != null) {
                    if (requestStatus.getCode().equals(SUCCESS_CODE)) {
                        showToast(getActivity(), "取消订单成功");
                        loadData();
                    } else {
                        showToast(getActivity(), requestStatus.getMsg());
                    }
                }
            }

            @Override
            public void onError(Throwable throwable) {
                showToast(getActivity(), R.string.do_failed);
            }

            @Override
            public void netClose() {
                showToast(getActivity(), R.string.unConnectedNetwork);
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
            params.put("showCount", TOTAL_COUNT_TEN);
            params.put("currentPage", page);
            if (!TextUtils.isEmpty(typeStatus)) {
                params.put("statusCategory", typeStatus);
            }
            NetLoadUtils.getNetInstance().loadNetDataPost(mAppContext, url, params, new NetLoadListenerHelper() {
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
                        indentOrderEntity = gson.fromJson(result, IntegralIndentOrderEntity.class);
                        INDENT_PRO_STATUS = indentOrderEntity.getIntegralIndentOrderBean().getStatus();
                        orderListBeanList.addAll(indentOrderEntity.getIntegralIndentOrderBean().getOrderList());
                    } else if (!code.equals(EMPTY_CODE)) {
                        showToast(getActivity(), msg);
                    }else{
                        integralIndentListAdapter.loadMoreEnd();
                    }
                    integralIndentListAdapter.notifyDataSetChanged();
                    NetLoadUtils.getNetInstance().showLoadSirString(loadService,orderListBeanList,code);;
                }

                @Override
                public void onNotNetOrException() {
                    smart_communal_refresh.finishRefresh();
                    integralIndentListAdapter.loadMoreEnd(true);
                    NetLoadUtils.getNetInstance().showLoadSir(loadService,orderListBeanList,indentOrderEntity);
                }

                @Override
                public void netClose() {
                    showToast(getActivity(), R.string.unConnectedNetwork);
                }

                @Override
                public void onError(Throwable throwable) {
                    showToast(getActivity(), R.string.invalidData);
                }
            });
        } else {
            NetLoadUtils.getNetInstance().showLoadSirEmpty(loadService);
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

    /**
     * 取消积分订单
     */
    private void confirmIntegralOrder() {
        String url = Url.BASE_URL + Url.Q_INDENT_CONFIRM;
        Map<String, Object> params = new HashMap<>();
        params.put("no", orderListBean.getNo());
        params.put("userId", userId);
        params.put("orderProductId",/*orderBean.getId()*/0);
        NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(),url,params,new NetLoadListenerHelper(){
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                RequestStatus requestStatus = gson.fromJson(result, RequestStatus.class);
                if (requestStatus != null) {
                    if (requestStatus.getCode().equals(SUCCESS_CODE)) {
                        loadData();
                        showToast(getActivity(), requestStatus.getMsg());
                    } else {
                        showToast(getActivity(), requestStatus.getResult() != null ?
                                requestStatus.getResult().getMsg() : requestStatus.getMsg());
                    }
                }
            }

            @Override
            public void netClose() {
                showToast(getActivity(),R.string.unConnectedNetwork);
            }

            @Override
            public void onError(Throwable throwable) {
                showToast(getActivity(),R.string.do_failed);
            }
        });
    }
}
