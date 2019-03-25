package com.amkj.dmsh.shopdetails.fragment;

import android.content.Intent;
import android.support.annotation.NonNull;
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
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.shopdetails.activity.DirectExchangeDetailsActivity;
import com.amkj.dmsh.shopdetails.adapter.DoMoIndentListAdapter;
import com.amkj.dmsh.shopdetails.bean.InquiryOrderEntry;
import com.amkj.dmsh.shopdetails.bean.InquiryOrderEntry.OrderInquiryDateEntry.OrderListBean;
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

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.showToastRequestMsg;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.CANCEL_ORDER;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.INDENT_PRO_STATUS;
import static com.amkj.dmsh.constant.ConstantVariable.PAY;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.TOTAL_COUNT_TEN;

;
;

/**
 * Created by atd48 on 2016/8/23.
 * 待支付
 */
public class DoMoIndentWaitPayFragment extends BaseFragment {
    @BindView(R.id.smart_communal_refresh)
    SmartRefreshLayout smart_communal_refresh;
    @BindView(R.id.communal_recycler)
    RecyclerView communal_recycler;
    //    滚动至顶部
    @BindView(R.id.download_btn_communal)
    public FloatingActionButton download_btn_communal;
    List<OrderListBean> orderListBeanList = new ArrayList();
    //根据type类型分类DuomoIndentPayFragment
    private int page = 1;
    private OrderListBean orderBean;
    private boolean isOnPause;
    private DoMoIndentListAdapter doMoIndentListAdapter;
    private int scrollY = 0;
    private float screenHeight;
    private InquiryOrderEntry inquiryOrderEntry;
    private AlertDialogHelper alertDialogHelper;

    @Override
    protected int getContentView() {
        return R.layout.layout_communal_smart_refresh_recycler_float_loading;
    }

    @Override
    protected void initViews() {
        getLoginStatus(this);
        communal_recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        communal_recycler.addItemDecoration(new ItemDecoration.Builder()
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_five_dp)


                .create());
        doMoIndentListAdapter = new DoMoIndentListAdapter(getActivity(), orderListBeanList);
        communal_recycler.setAdapter(doMoIndentListAdapter);

        smart_communal_refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                //                滚动距离置0
                scrollY = 0;
                loadData();
            }
        });
        doMoIndentListAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                page++;
                getWaitPayData();
            }
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
        doMoIndentListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                InquiryOrderEntry.OrderInquiryDateEntry.OrderListBean orderListBean = (InquiryOrderEntry.OrderInquiryDateEntry.OrderListBean) view.getTag();
                if (orderListBean != null) {
                    Intent intent = new Intent(getActivity(), DirectExchangeDetailsActivity.class);
                    intent.putExtra("orderNo", orderListBean.getNo());
                    intent.putExtra("uid", orderListBean.getUserId());
                    startActivity(intent);
                }
            }
        });
        doMoIndentListAdapter.setOnClickViewListener(new DoMoIndentListAdapter.OnClickViewListener() {
            @Override
            public void click(String type, InquiryOrderEntry.OrderInquiryDateEntry.OrderListBean orderListBean) {
                orderBean = orderListBean;
                Intent intent = new Intent();
                switch (type) {
                    case CANCEL_ORDER:
//                        取消订单
                        if (alertDialogHelper == null) {
                            alertDialogHelper = new AlertDialogHelper(getActivity());
                            alertDialogHelper.setTitleVisibility(View.GONE).setMsgTextGravity(Gravity.CENTER)
                                    .setMsg("确定要取消当前订单？").setCancelText("取消").setConfirmText("确定")
                                    .setCancelTextColor(getResources().getColor(R.color.text_login_gray_s));
                            alertDialogHelper.setAlertListener(new AlertDialogHelper.AlertConfirmCancelListener() {
                                @Override
                                public void confirm() {
                                    cancelOrder();
                                }

                                @Override
                                public void cancel() {
                                }
                            });
                        }
                        alertDialogHelper.show();
                        break;
                    case PAY:
                        intent.setClass(getActivity(), DirectExchangeDetailsActivity.class);
                        intent.putExtra("orderNo", orderListBean.getNo());
                        intent.putExtra("uid", orderListBean.getUserId());
                        startActivity(intent);
                        break;
                }
            }
        });
    }


    @Override
    protected void loadData() {
        page = 1;
        getWaitPayData();
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    private void getWaitPayData() {
        String url = Url.BASE_URL + Url.Q_INQUIRY_WAIT_PAY;
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("showCount", TOTAL_COUNT_TEN);
        params.put("currentPage", page);
        //        版本号控制 3 组合商品赠品
        params.put("version", 3);
        NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(), url, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                smart_communal_refresh.finishRefresh();
                doMoIndentListAdapter.loadMoreComplete();
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
                    inquiryOrderEntry = gson.fromJson(result, InquiryOrderEntry.class);
                    InquiryOrderEntry.OrderInquiryDateEntry orderInquiryDateEntry = inquiryOrderEntry.getOrderInquiryDateEntry();
                    if (!TextUtils.isEmpty(orderInquiryDateEntry.getCurrentTime())) {
                        for (int i = 0; i < orderInquiryDateEntry.getOrderList().size(); i++) {
                            OrderListBean orderListBean = inquiryOrderEntry.getOrderInquiryDateEntry().getOrderList().get(i);
                            orderListBean.setCurrentTime(orderInquiryDateEntry.getCurrentTime());
                        }
                    }
                    INDENT_PRO_STATUS = inquiryOrderEntry.getOrderInquiryDateEntry().getStatus();
                    orderListBeanList.addAll(inquiryOrderEntry.getOrderInquiryDateEntry().getOrderList());
                } else if (!code.equals(EMPTY_CODE)) {
                    showToast(getActivity(), msg);
                }else{
                    doMoIndentListAdapter.loadMoreEnd();
                }
                doMoIndentListAdapter.notifyDataSetChanged();
                NetLoadUtils.getNetInstance().showLoadSirString(loadService, orderListBeanList, code);
            }

            @Override
            public void onNotNetOrException() {
                smart_communal_refresh.finishRefresh();
                doMoIndentListAdapter.loadMoreEnd(true);
                NetLoadUtils.getNetInstance().showLoadSir(loadService, orderListBeanList, inquiryOrderEntry);
            }
        });
    }

    //  取消订单
    private void cancelOrder() {
        String url = Url.BASE_URL + Url.Q_INDENT_CANCEL;
        Map<String, Object> params = new HashMap<>();
        params.put("no", orderBean.getNo());
        params.put("userId", userId);
        NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(),url,params,new NetLoadListenerHelper(){
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                RequestStatus requestStatus = gson.fromJson(result, RequestStatus.class);
                if (requestStatus != null) {
                    if (requestStatus.getCode().equals(SUCCESS_CODE)) {
                        showToastRequestMsg(getActivity(), requestStatus);
                        loadData();
                    } else {
                        showToastRequestMsg(getActivity(), requestStatus);
                    }
                }
            }
        });
    }

    public void onPause() {
        isOnPause = true;
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isOnPause) {
            loadData();
        }
    }
}
