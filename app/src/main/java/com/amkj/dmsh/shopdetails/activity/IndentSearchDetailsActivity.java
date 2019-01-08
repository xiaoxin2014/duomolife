package com.amkj.dmsh.shopdetails.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.TinkerBaseApplicationLike;
import com.amkj.dmsh.bean.RequestStatus;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.mine.bean.ShopCarNewInfoEntity.ShopCarNewInfoBean.CartInfoBean.CartProductInfoBean;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.qyservice.QyServiceUtils;
import com.amkj.dmsh.release.activity.ReleaseImgArticleActivity;
import com.amkj.dmsh.shopdetails.adapter.DoMoIndentListAdapter;
import com.amkj.dmsh.shopdetails.bean.DirectAppraisePassBean;
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
import butterknife.OnClick;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.showToastRequestMsg;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.BASK_READER;
import static com.amkj.dmsh.constant.ConstantVariable.BUY_AGAIN;
import static com.amkj.dmsh.constant.ConstantVariable.CANCEL_ORDER;
import static com.amkj.dmsh.constant.ConstantVariable.CHECK_LOG;
import static com.amkj.dmsh.constant.ConstantVariable.CONFIRM_ORDER;
import static com.amkj.dmsh.constant.ConstantVariable.DEL;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.INDENT_PRO_STATUS;
import static com.amkj.dmsh.constant.ConstantVariable.IS_LOGIN_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.LITTER_CONSIGN;
import static com.amkj.dmsh.constant.ConstantVariable.PAY;
import static com.amkj.dmsh.constant.ConstantVariable.PRO_APPRAISE;
import static com.amkj.dmsh.constant.ConstantVariable.REMIND_DELIVERY;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.TOTAL_COUNT_TEN;
import static com.amkj.dmsh.constant.Url.INDENT_SEARCH;
import static com.amkj.dmsh.constant.Url.Q_INDENT_CANCEL;
import static com.amkj.dmsh.constant.Url.Q_INDENT_CONFIRM;
import static com.amkj.dmsh.constant.Url.Q_INDENT_DEL;
import static com.amkj.dmsh.homepage.activity.HomePageSearchActivity.SEARCH_DATA;

;
;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/4/24
 * class description:请输入类描述
 */
public class IndentSearchDetailsActivity extends BaseActivity {
    @BindView(R.id.iv_indent_search)
    ImageView iv_indent_search;
    @BindView(R.id.iv_indent_service)
    ImageView iv_indent_more;
    @BindView(R.id.tv_indent_title)
    TextView tv_indent_title;
    @BindView(R.id.smart_communal_refresh)
    SmartRefreshLayout smart_communal_refresh;
    @BindView(R.id.communal_recycler)
    RecyclerView communal_recycler;
    //    滚动至顶部
    @BindView(R.id.download_btn_communal)
    public FloatingActionButton download_btn_communal;
    List<OrderListBean> orderListBeanList = new ArrayList();
    private int page = 1;
    private DoMoIndentListAdapter doMoIndentListAdapter;
    private List<DirectAppraisePassBean> directAppraisePassList = new ArrayList<>();
    private OrderListBean orderBean;
    private DirectAppraisePassBean directAppraisePassBean;
    private boolean isOnPause;
    private int scrollY = 0;
    private float screenHeight;
    private String searchKey;
    private InquiryOrderEntry inquiryOrderEntry;
    private AlertDialogHelper delOrderDialogHelper;
    private AlertDialogHelper confirmOrderDialogHelper;
    private AlertDialogHelper cancelOrderDialogHelper;

    @Override
    protected int getContentView() {
        return R.layout.activity_indent_search_details;
    }

    @Override
    protected void initViews() {
        getLoginStatus(this);
        iv_indent_more.setSelected(true);
        iv_indent_search.setVisibility(View.GONE);
        tv_indent_title.setText("订单");
        Intent intent = getIntent();
        searchKey = intent.getStringExtra(SEARCH_DATA);
        communal_recycler.setLayoutManager(new LinearLayoutManager(IndentSearchDetailsActivity.this));
        communal_recycler.addItemDecoration(new ItemDecoration.Builder()
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_five_dp).create());
        doMoIndentListAdapter = new DoMoIndentListAdapter(IndentSearchDetailsActivity.this, orderListBeanList);
        communal_recycler.setAdapter(doMoIndentListAdapter);
        smart_communal_refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                scrollY = 0;
                loadData();
                doMoIndentListAdapter.loadMoreEnd(true);
            }
        });
        doMoIndentListAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                page++;
                getData();
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
        doMoIndentListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                OrderListBean orderListBean = (OrderListBean) view.getTag();
                if (orderListBean != null) {
                    Intent intent = new Intent(IndentSearchDetailsActivity.this, DirectExchangeDetailsActivity.class);
                    intent.putExtra("orderNo", orderListBean.getNo());
                    startActivity(intent);
                }
            }
        });
        doMoIndentListAdapter.setOnClickViewListener(new DoMoIndentListAdapter.OnClickViewListener() {
            @Override
            public void click(String type, OrderListBean orderListBean) {
                orderBean = orderListBean;
                Intent intent = new Intent();
                Bundle bundle;
                switch (type) {
                    case BUY_AGAIN:
//                        再次购买
                        intent.setClass(IndentSearchDetailsActivity.this, DirectIndentWriteActivity.class);
                        intent.putExtra("orderNo", orderListBean.getNo());
                        startActivity(intent);
                        break;
                    case CANCEL_ORDER:
//                        取消订单
                        if (cancelOrderDialogHelper == null) {
                            cancelOrderDialogHelper = new AlertDialogHelper(IndentSearchDetailsActivity.this);
                            cancelOrderDialogHelper.setTitleVisibility(View.GONE).setMsgTextGravity(Gravity.CENTER)
                                    .setMsg("确定要取消当前订单？").setCancelText("取消").setConfirmText("确定")
                                    .setCancelTextColor(getResources().getColor(R.color.text_login_gray_s));
                            cancelOrderDialogHelper.setAlertListener(new AlertDialogHelper.AlertConfirmCancelListener() {
                                @Override
                                public void confirm() {
                                    cancelOrder();
                                }

                                @Override
                                public void cancel() {
                                }
                            });
                        }
                        cancelOrderDialogHelper.show();
                        break;
                    case REMIND_DELIVERY:
                        if (loadHud != null) {
                            loadHud.show();
                        }
                        setRemindDelivery(orderListBean);
                        break;
                    case PAY:
                        intent.setClass(IndentSearchDetailsActivity.this, DirectExchangeDetailsActivity.class);
                        intent.putExtra("orderNo", orderListBean.getNo());
                        intent.putExtra("uid", orderListBean.getUserId());
                        startActivity(intent);
                        break;
                    case LITTER_CONSIGN:
                    case CHECK_LOG:
//                        查看物流
                        intent.setClass(IndentSearchDetailsActivity.this, DirectLogisticsDetailsActivity.class);
                        intent.putExtra("orderNo", orderListBean.getNo());
                        startActivity(intent);
                        break;
                    case CONFIRM_ORDER:
//                        确认收货
                        if (confirmOrderDialogHelper == null) {
                            confirmOrderDialogHelper = new AlertDialogHelper(IndentSearchDetailsActivity.this);
                            confirmOrderDialogHelper.setTitleVisibility(View.GONE).setMsgTextGravity(Gravity.CENTER)
                                    .setMsg("确定已收到货物?").setCancelText("取消").setConfirmText("确定")
                                    .setCancelTextColor(getResources().getColor(R.color.text_login_gray_s));
                            confirmOrderDialogHelper.setAlertListener(new AlertDialogHelper.AlertConfirmCancelListener() {
                                @Override
                                public void confirm() {
                                    confirmOrder();
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
                        directAppraisePassList.clear();
                        for (int i = 0; i < orderListBean.getGoods().size(); i++) {
                            OrderListBean.GoodsBean goodBean = orderListBean.getGoods().get(i);
                            if (goodBean.getCombineProductInfoList() != null
                                    && goodBean.getCombineProductInfoList().size() > 0) {
                                for (int k = 0; k < goodBean.getCombineProductInfoList().size(); k++) {
                                    CartProductInfoBean cartProductInfoBean = goodBean.getCombineProductInfoList().get(k);
                                    if (cartProductInfoBean.getStatus() == 30) {
                                        directAppraisePassBean = new DirectAppraisePassBean();
                                        directAppraisePassBean.setProductId(cartProductInfoBean.getId());
                                        directAppraisePassBean.setOrderProductId(cartProductInfoBean.getOrderProductId());
                                        directAppraisePassBean.setPath(cartProductInfoBean.getPicUrl());
                                        directAppraisePassBean.setStar(5);
                                        directAppraisePassList.add(directAppraisePassBean);
                                    }
                                }
                            } else if (goodBean.getStatus() == 30) {
                                directAppraisePassBean = new DirectAppraisePassBean();
                                directAppraisePassBean.setPath(goodBean.getPicUrl());
                                directAppraisePassBean.setProductId(goodBean.getId());
                                directAppraisePassBean.setStar(5);
                                directAppraisePassBean.setOrderProductId(goodBean.getOrderProductId());
                                directAppraisePassList.add(directAppraisePassBean);
                            }
                        }
                        intent.setClass(IndentSearchDetailsActivity.this, DirectPublishAppraiseActivity.class);
                        bundle = new Bundle();
                        bundle.putParcelableArrayList("appraiseData", (ArrayList<? extends Parcelable>) directAppraisePassList);
                        intent.putExtras(bundle);
                        intent.putExtra("uid", orderListBean.getUserId());
                        intent.putExtra("orderNo", orderListBean.getNo());
                        startActivity(intent);
                        break;
                    //                        晒单赢积分
                    case BASK_READER:
                        intent.setClass(IndentSearchDetailsActivity.this, ReleaseImgArticleActivity.class);
                        intent.putExtra("orderNo", orderListBean.getNo());
                        startActivity(intent);
                        break;
                    case DEL:
//                        删除订单
                        if (delOrderDialogHelper == null) {
                            delOrderDialogHelper = new AlertDialogHelper(IndentSearchDetailsActivity.this);
                            delOrderDialogHelper.setTitleVisibility(View.GONE).setMsgTextGravity(Gravity.CENTER)
                                    .setMsg("确定要删除该订单？").setCancelText("取消").setConfirmText("确定")
                                    .setCancelTextColor(getResources().getColor(R.color.text_login_gray_s));
                            delOrderDialogHelper.setAlertListener(new AlertDialogHelper.AlertConfirmCancelListener() {
                                @Override
                                public void confirm() {
                                    delOrder();
                                }

                                @Override
                                public void cancel() {
                                }
                            });
                        }
                        delOrderDialogHelper.show();
                        break;
                }
            }
        });
    }

    @Override
    protected void loadData() {
        page = 1;
        getData();
    }

    @Override
    protected void getData() {
        if (userId < 1) {
            NetLoadUtils.getNetInstance().showLoadSirEmpty(loadService);
            return;
        }
        //        订单搜索
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("showCount", TOTAL_COUNT_TEN);
        params.put("currentPage", page);
//        3 组合赠品兼容
        params.put("version", 3);
        params.put("productName", getStrings(searchKey));
        NetLoadUtils.getNetInstance().loadNetDataPost(mAppContext, INDENT_SEARCH, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                smart_communal_refresh.finishRefresh();
                doMoIndentListAdapter.loadMoreComplete();
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
                    showToast(IndentSearchDetailsActivity.this, msg);
                }else{
                    doMoIndentListAdapter.loadMoreEnd();
                }
                doMoIndentListAdapter.notifyDataSetChanged();
                NetLoadUtils.getNetInstance().showLoadSirString(loadService,orderListBeanList,code);
            }

            @Override
            public void onNotNetOrException() {
                smart_communal_refresh.finishRefresh();
                doMoIndentListAdapter.loadMoreEnd(true);
                NetLoadUtils.getNetInstance().showLoadSir(loadService,orderListBeanList, inquiryOrderEntry);
            }

            @Override
            public void netClose() {
                showToast(mAppContext, R.string.unConnectedNetwork);
            }

            @Override
            public void onError(Throwable throwable) {
                showToast(mAppContext, R.string.invalidData);
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

    //  订单删除
    private void delOrder() {
        String url = Url.BASE_URL + Q_INDENT_DEL;
        Map<String, Object> params = new HashMap<>();
        params.put("no", orderBean.getNo());
        params.put("userId", userId);
        NetLoadUtils.getNetInstance().loadNetDataPost(this,Q_INDENT_DEL,params,new NetLoadListenerHelper(){
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                RequestStatus requestStatus = gson.fromJson(result, RequestStatus.class);
                if (requestStatus != null) {
                    if (requestStatus.getCode().equals(SUCCESS_CODE)) {
                        loadData();
                        showToast(IndentSearchDetailsActivity.this, String.format(getResources().getString(R.string.doSuccess), "删除订单"));
                    } else {
                        showToastRequestMsg(IndentSearchDetailsActivity.this, requestStatus);
                    }
                }
            }
        });
    }

    private void confirmOrder() {
        String url = Url.BASE_URL + Q_INDENT_CONFIRM;
        Map<String, Object> params = new HashMap<>();
        params.put("no", orderBean.getNo());
        params.put("userId", userId);
        params.put("orderProductId", 0);
        NetLoadUtils.getNetInstance().loadNetDataPost(this,Q_INDENT_CONFIRM,params,new NetLoadListenerHelper(){
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                RequestStatus requestStatus = gson.fromJson(result, RequestStatus.class);
                if (requestStatus != null) {
                    if (requestStatus.getCode().equals(SUCCESS_CODE)) {
                        loadData();
                        showToastRequestMsg(IndentSearchDetailsActivity.this, requestStatus);
                    } else {
                        showToastRequestMsg(IndentSearchDetailsActivity.this, requestStatus);
                    }
                }
            }
        });
    }

    //  取消订单
    private void cancelOrder() {
        String url = Url.BASE_URL + Q_INDENT_CANCEL;
        Map<String, Object> params = new HashMap<>();
        params.put("no", orderBean.getNo());
        params.put("userId", userId);
        NetLoadUtils.getNetInstance().loadNetDataPost(this,Q_INDENT_CANCEL,params,new NetLoadListenerHelper(){
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                RequestStatus requestStatus = gson.fromJson(result, RequestStatus.class);
                if (requestStatus != null) {
                    if (requestStatus.getCode().equals(SUCCESS_CODE)) {
                        showToast(IndentSearchDetailsActivity.this, requestStatus.getMsg());
                        loadData();
                    } else {
                        showToastRequestMsg(IndentSearchDetailsActivity.this, requestStatus);
                    }
                }
            }

            @Override
            public void onError(Throwable throwable) {
                showToast(IndentSearchDetailsActivity.this,R.string.do_failed);
            }

            @Override
            public void netClose() {
                showToast(IndentSearchDetailsActivity.this,R.string.unConnectedNetwork);
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

    @OnClick(R.id.tv_indent_back)
    void goBack(View view) {
        finish();
    }

    @OnClick(R.id.iv_indent_service)
    void openPopWindows(View view) {
        setVisitorOpenService();
    }

    private void setVisitorOpenService() {
        QyServiceUtils.getQyInstance()
                .openQyServiceChat(IndentSearchDetailsActivity.this
                        , "订单搜索", "");
    }

    /**
     * 设置催发货
     *
     * @param orderBean
     */
    private void setRemindDelivery(OrderListBean orderBean) {
        Map<String, Object> params = new HashMap<>();
        params.put("uid", userId);
        params.put("orderNo", orderBean.getNo());
        NetLoadUtils.getNetInstance().loadNetDataPost(this, Url.Q_INQUIRY_WAIT_SEND_EXPEDITING, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                if (loadHud != null) {
                    loadHud.dismiss();
                }
                Gson gson = new Gson();
                RequestStatus requestStatus = gson.fromJson(result, RequestStatus.class);
                if (requestStatus != null) {
                    if (requestStatus.getCode().equals(SUCCESS_CODE)) {
                        orderBean.setWaitDeliveryFlag(false);
                        showToast(mAppContext, "已提醒商家尽快发货，请耐心等候~");
                    } else {
                        showToastRequestMsg(mAppContext, requestStatus);
                    }
                }
            }

            @Override
            public void onNotNetOrException() {
                if (loadHud != null) {
                    loadHud.dismiss();
                }
            }

            @Override
            public void netClose() {
                showToast(mAppContext, R.string.unConnectedNetwork);
            }

            @Override
            public void onError(Throwable throwable) {
                showToast(mAppContext, R.string.do_failed);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            if (requestCode == IS_LOGIN_CODE) {
                finish();
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IS_LOGIN_CODE) {
            loadData();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cancelOrderDialogHelper != null && cancelOrderDialogHelper.isShowing()) {
            cancelOrderDialogHelper.dismiss();
        }
        if (confirmOrderDialogHelper != null
                && confirmOrderDialogHelper.isShowing()) {
            confirmOrderDialogHelper.dismiss();
        }
        if (delOrderDialogHelper != null
                && delOrderDialogHelper.isShowing()) {
            delOrderDialogHelper.dismiss();
        }
    }
}
