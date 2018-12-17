package com.amkj.dmsh.shopdetails.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
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
import com.amkj.dmsh.constant.UMShareAction;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.dominant.bean.QualityGroupShareEntity;
import com.amkj.dmsh.dominant.bean.QualityGroupShareEntity.QualityGroupShareBean;
import com.amkj.dmsh.mine.bean.ShopCarNewInfoEntity.ShopCarNewInfoBean.CartInfoBean.CartProductInfoBean;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.release.activity.ReleaseImgArticleActivity;
import com.amkj.dmsh.release.dialogutils.AlertSettingBean;
import com.amkj.dmsh.shopdetails.activity.DirectApplyRefundActivity;
import com.amkj.dmsh.shopdetails.activity.DirectExchangeDetailsActivity;
import com.amkj.dmsh.shopdetails.activity.DirectIndentWriteActivity;
import com.amkj.dmsh.shopdetails.activity.DirectLogisticsDetailsActivity;
import com.amkj.dmsh.shopdetails.activity.DirectPublishAppraiseActivity;
import com.amkj.dmsh.shopdetails.adapter.DoMoIndentListAdapter;
import com.amkj.dmsh.shopdetails.bean.DirectApplyRefundBean;
import com.amkj.dmsh.shopdetails.bean.DirectApplyRefundBean.DirectRefundProBean;
import com.amkj.dmsh.shopdetails.bean.DirectAppraisePassBean;
import com.amkj.dmsh.shopdetails.bean.InquiryOrderEntry;
import com.amkj.dmsh.shopdetails.bean.InquiryOrderEntry.OrderInquiryDateEntry.OrderListBean;
import com.amkj.dmsh.shopdetails.bean.InquiryOrderEntry.OrderInquiryDateEntry.OrderListBean.GoodsBean;
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
import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.BASK_READER;
import static com.amkj.dmsh.constant.ConstantVariable.BUY_AGAIN;
import static com.amkj.dmsh.constant.ConstantVariable.CANCEL_ORDER;
import static com.amkj.dmsh.constant.ConstantVariable.CANCEL_PAY_ORDER;
import static com.amkj.dmsh.constant.ConstantVariable.CHECK_LOG;
import static com.amkj.dmsh.constant.ConstantVariable.CONFIRM_ORDER;
import static com.amkj.dmsh.constant.ConstantVariable.DEL;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.INDENT_PRO_STATUS;
import static com.amkj.dmsh.constant.ConstantVariable.INVITE_GROUP;
import static com.amkj.dmsh.constant.ConstantVariable.LITTER_CONSIGN;
import static com.amkj.dmsh.constant.ConstantVariable.PAY;
import static com.amkj.dmsh.constant.ConstantVariable.PRO_APPRAISE;
import static com.amkj.dmsh.constant.ConstantVariable.REFUND_TYPE;
import static com.amkj.dmsh.constant.ConstantVariable.REMIND_DELIVERY;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.TOTAL_COUNT_TEN;
import static com.amkj.dmsh.constant.Url.Q_INQUIRY_WAIT_SEND_EXPEDITING;

;
;


/**
 * Created by atd48 on 2016/8/23.
 */
public class DuMoIndentAllFragment extends BaseFragment {
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
    private DoMoIndentListAdapter doMoIndentListAdapter;
    private List<DirectAppraisePassBean> directAppraisePassList = new ArrayList<>();
    private OrderListBean orderBean;
    private DirectAppraisePassBean directAppraisePassBean;
    private boolean isOnPause;
    private int scrollY = 0;
    private float screenHeight;
    private InquiryOrderEntry inquiryOrderEntry;
    private AlertDialogHelper delOrderDialogHelper;
    private AlertDialogHelper cancelOrderDialogHelper;
    private AlertDialogHelper confirmOrderDialogHelper;

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
                .setDividerId(R.drawable.divider_ll_10dp_gray_bg).create());
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
                getAllIndent();
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
        doMoIndentListAdapter.setOnClickViewListener(new DoMoIndentListAdapter.OnClickViewListener() {
            @Override
            public void click(String type, OrderListBean orderListBean) {
                orderBean = orderListBean;
                Intent intent = new Intent();
                Bundle bundle;
                AlertSettingBean alertSettingBean;
                AlertSettingBean.AlertData alertData;
                switch (type) {
                    case BUY_AGAIN:
//                        再次购买
                        intent.setClass(getActivity(), DirectIndentWriteActivity.class);
                        intent.putExtra("orderNo", orderListBean.getNo());
                        startActivity(intent);
                        break;
                    case REMIND_DELIVERY:
                        if (loadHud != null) {
                            loadHud.show();
                        }
                        setRemindDelivery(orderBean);
                        break;
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
                                    cancelOrder();
                                }

                                @Override
                                public void cancel() {
                                }
                            });
                        }
                        cancelOrderDialogHelper.show();
                        break;
                    case CANCEL_PAY_ORDER:
                        DirectApplyRefundBean refundBean = new DirectApplyRefundBean();
                        refundBean.setType(3);
                        refundBean.setOrderNo(orderListBean.getNo());
                        List<DirectRefundProBean> directProList = new ArrayList<>();
                        List<CartProductInfoBean> cartProductInfoList;
                        DirectRefundProBean directRefundProBean;
                        for (int i = 0; i < orderListBean.getGoods().size(); i++) {
                            GoodsBean goodsBean = orderListBean.getGoods().get(i);
                            cartProductInfoList = new ArrayList<>();
                            directRefundProBean = new DirectRefundProBean();
                            directRefundProBean.setId(goodsBean.getId());
                            directRefundProBean.setOrderProductId(goodsBean.getOrderProductId());
                            directRefundProBean.setCount(goodsBean.getCount());
                            directRefundProBean.setName(goodsBean.getName());
                            directRefundProBean.setPicUrl(goodsBean.getPicUrl());
                            directRefundProBean.setSaleSkuValue(goodsBean.getSaleSkuValue());
                            directRefundProBean.setPrice(goodsBean.getPrice());
                            if (goodsBean.getPresentProductInfoList() != null && goodsBean.getPresentProductInfoList().size() > 0) {
                                cartProductInfoList.addAll(goodsBean.getPresentProductInfoList());
                            }
                            if (goodsBean.getCombineProductInfoList() != null && goodsBean.getCombineProductInfoList().size() > 0) {
                                cartProductInfoList.addAll(goodsBean.getCombineProductInfoList());
                            }
                            if (cartProductInfoList.size() > 0) {
                                directRefundProBean.setCartProductInfoList(cartProductInfoList);
                            }
                            directProList.add(directRefundProBean);
                        }
                        refundBean.setDirectRefundProList(directProList);
                        intent.setClass(getActivity(), DirectApplyRefundActivity.class);
                        intent.putExtra(REFUND_TYPE, REFUND_TYPE);
                        bundle = new Bundle();
                        bundle.putParcelable("refundPro", refundBean);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        break;
                    case PAY:
                        intent.setClass(getActivity(), DirectExchangeDetailsActivity.class);
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
                            GoodsBean goodBean = orderListBean.getGoods().get(i);
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
                        if (directAppraisePassList.size() > 0) {
                            intent.setClass(getActivity(), DirectPublishAppraiseActivity.class);
                            bundle = new Bundle();
                            bundle.putParcelableArrayList("appraiseData", (ArrayList<? extends Parcelable>) directAppraisePassList);
                            intent.putExtras(bundle);
                            intent.putExtra("uid", orderListBean.getUserId());
                            intent.putExtra("orderNo", orderListBean.getNo());
                            startActivity(intent);
                        }
                        break;
//                        晒单赢积分
                    case BASK_READER:
                        intent.setClass(getActivity(), ReleaseImgArticleActivity.class);
                        intent.putExtra("orderNo", orderListBean.getNo());
                        startActivity(intent);
                        break;
                    case DEL:
//                        删除订单
                        if (delOrderDialogHelper == null) {
                            delOrderDialogHelper = new AlertDialogHelper(getActivity());
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
                    case INVITE_GROUP:
                        getInviteGroupInfo(orderListBean.getNo());
                        break;
                }
            }
        });
    }

    @Override
    protected void loadData() {
        page = 1;
        getAllIndent();
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    private void getAllIndent() {
        String url = Url.BASE_URL + Url.Q_INQUIRY_ALL_ORDER;
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("showCount", TOTAL_COUNT_TEN);
        params.put("currentPage", page);
        params.put("orderType", "currency");
//        版本号控制 3 组合商品赠品
        params.put("version", 3);
        NetLoadUtils.getNetInstance().loadNetDataPost(mAppContext, url, params, new NetLoadListenerHelper() {
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

    //  订单删除
    private void delOrder() {
        String url = Url.BASE_URL + Url.Q_INDENT_DEL;
        Map<String, Object> params = new HashMap<>();
        params.put("no", orderBean.getNo());
        params.put("userId", userId);
        NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(),url,params,new NetLoadListenerHelper(){
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                RequestStatus indentInfo = gson.fromJson(result, RequestStatus.class);
                if (indentInfo != null) {
                    if (indentInfo.getCode().equals(SUCCESS_CODE)) {
                        loadData();
                        showToast(getActivity(), String.format(getResources().getString(R.string.doSuccess), "删除订单"));
                    } else {
                        showToast(getActivity(), indentInfo.getResult() != null ?
                                indentInfo.getResult().getMsg() : indentInfo.getMsg());
                    }
                }
            }
        });
    }

    private void confirmOrder() {
        String url = Url.BASE_URL + Url.Q_INDENT_CONFIRM;
        Map<String, Object> params = new HashMap<>();
        params.put("no", orderBean.getNo());
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
                        showToast(getActivity(), requestStatus.getMsg());
                        loadData();
                    } else {
                        showToast(getActivity(), requestStatus.getResult() != null ?
                                requestStatus.getResult().getMsg() : requestStatus.getMsg());
                    }
                }
            }
        });
    }

    private void getInviteGroupInfo(String no) {
        String url = Url.BASE_URL + Url.GROUP_MINE_SHARE;
        if (loadHud != null) {
            loadHud.show();
        }
        Map<String, Object> params = new HashMap<>();
        params.put("orderNo", no);
        NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(),url,params,new NetLoadListenerHelper(){
            @Override
            public void onSuccess(String result) {
                if (loadHud != null) {
                    loadHud.dismiss();
                }
                Gson gson = new Gson();
                QualityGroupShareEntity qualityGroupShareEntity = gson.fromJson(result, QualityGroupShareEntity.class);
                if (qualityGroupShareEntity != null) {
                    if (qualityGroupShareEntity.getCode().equals(SUCCESS_CODE)) {
                        QualityGroupShareBean qualityGroupShareBean = qualityGroupShareEntity.getQualityGroupShareBean();
                        invitePartnerGroup(qualityGroupShareBean, no);
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
            public void onError(Throwable throwable) {
                showToast(getActivity(), R.string.do_failed);
            }

            @Override
            public void netClose() {
                showToast(getActivity(), R.string.unConnectedNetwork);
            }
        });
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
        NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(), Q_INQUIRY_WAIT_SEND_EXPEDITING, params, new NetLoadListenerHelper() {
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
                        showToast(mAppContext, requestStatus.getMsg());
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

    /**
     * 邀请参团
     *
     * @param qualityGroupShareBean 参团信息
     * @param orderNo
     */
    private void invitePartnerGroup(@NonNull QualityGroupShareBean qualityGroupShareBean, String orderNo) {
        new UMShareAction(getActivity()
                , qualityGroupShareBean.getGpPicUrl()
                , qualityGroupShareBean.getName()
                , getStrings(qualityGroupShareBean.getSubtitle())
                , Url.BASE_SHARE_PAGE_TWO + "m/template/share_template/groupShare.html?id=" + qualityGroupShareBean.getGpInfoId()
                + "&record=" + qualityGroupShareBean.getGpRecordId(), "pages/groupshare/groupshare?id=" + qualityGroupShareBean.getGpInfoId()
                + (TextUtils.isEmpty(orderNo) ? "&gpRecordId=" + qualityGroupShareBean.getGpRecordId() : "&order=" + orderNo));
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
