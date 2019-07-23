package com.amkj.dmsh.shopdetails.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.bean.RequestStatus;
import com.amkj.dmsh.constant.UMShareAction;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.dominant.bean.QualityGroupShareEntity;
import com.amkj.dmsh.dominant.bean.QualityGroupShareEntity.QualityGroupShareBean;
import com.amkj.dmsh.mine.bean.CartProductInfoBean;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.shopdetails.activity.DirectApplyRefundActivity;
import com.amkj.dmsh.shopdetails.activity.DirectLogisticsDetailsActivity;
import com.amkj.dmsh.shopdetails.adapter.DoMoIndentListAdapter;
import com.amkj.dmsh.shopdetails.bean.DirectApplyRefundBean;
import com.amkj.dmsh.shopdetails.bean.DirectApplyRefundBean.DirectRefundProBean;
import com.amkj.dmsh.shopdetails.bean.InquiryOrderEntry;
import com.amkj.dmsh.shopdetails.bean.InquiryOrderEntry.OrderInquiryDateEntry.OrderListBean;
import com.amkj.dmsh.shopdetails.bean.InquiryOrderEntry.OrderInquiryDateEntry.OrderListBean.GoodsBean;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.melnykov.fab.FloatingActionButton;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

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
import static com.amkj.dmsh.constant.ConstantMethod.showToastRequestMsg;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.CANCEL_PAY_ORDER;
import static com.amkj.dmsh.constant.ConstantVariable.CHECK_LOG;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.INDENT_PRO_STATUS;
import static com.amkj.dmsh.constant.ConstantVariable.INVITE_GROUP;
import static com.amkj.dmsh.constant.ConstantVariable.LITTER_CONSIGN;
import static com.amkj.dmsh.constant.ConstantVariable.REFUND_TYPE;
import static com.amkj.dmsh.constant.ConstantVariable.REMIND_DELIVERY;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.TOTAL_COUNT_TEN;
import static com.amkj.dmsh.constant.Url.BASE_URL;
import static com.amkj.dmsh.constant.Url.Q_INQUIRY_WAIT_SEND_EXPEDITING;


/**
 * Created by atd48 on 2016/8/23.
 * 待发货
 */
public class DoMoIndentWaitSendFragment extends BaseFragment {
    @BindView(R.id.smart_communal_refresh)
    SmartRefreshLayout smart_communal_refresh;
    @BindView(R.id.communal_recycler)
    RecyclerView communal_recycler;
    //    滚动至顶部
    @BindView(R.id.download_btn_communal)
    public FloatingActionButton download_btn_communal;
    List<OrderListBean> orderListBeanList = new ArrayList<>();
    //根据type类型分类DuomoIndentPayFragment
    private int page = 1;
    private DoMoIndentListAdapter doMoIndentListAdapter;
    private boolean isOnPause;
    private InquiryOrderEntry inquiryOrderEntry;

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

        setFloatingButton(download_btn_communal, communal_recycler);
        smart_communal_refresh.setOnRefreshListener(refreshLayout -> loadData());
        doMoIndentListAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                page++;
                getWaitSendData();
            }
        }, communal_recycler);

        doMoIndentListAdapter.setOnClickViewListener(new DoMoIndentListAdapter.OnClickViewListener() {
            @Override
            public void click(String type, OrderListBean orderListBean) {
                Intent intent = new Intent();
                switch (type) {
                    case LITTER_CONSIGN:
                    case CHECK_LOG:
//                        部分发货，查看物流
                        intent.setClass(getActivity(), DirectLogisticsDetailsActivity.class);
                        intent.putExtra("orderNo", orderListBean.getNo());
                        startActivity(intent);
                        break;
                    case REMIND_DELIVERY:
                        if (loadHud != null) {
                            loadHud.show();
                        }
                        setRemindDelivery(orderListBean);
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
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("refundPro", refundBean);
                        intent.putExtras(bundle);
                        startActivity(intent);
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
        getWaitSendData();
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    private void getWaitSendData() {
        String url = BASE_URL + Url.Q_INQUIRY_WAIT_SEND;
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("showCount", TOTAL_COUNT_TEN);
        params.put("currentPage", page);
        params.put("orderType", "currency");
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
                doMoIndentListAdapter.loadMoreComplete();
                NetLoadUtils.getNetInstance().showLoadSir(loadService, orderListBeanList, inquiryOrderEntry);
            }
        });
    }

    /**
     * 邀请参团获取信息
     *
     * @param no
     */
    private void getInviteGroupInfo(String no) {
        String url = BASE_URL + Url.GROUP_MINE_SHARE;
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

    /**
     * 邀请参团
     *
     * @param qualityGroupShareBean 参团信息
     */
    private void invitePartnerGroup(@NonNull QualityGroupShareBean qualityGroupShareBean, String orderNo) {
        new UMShareAction((BaseActivity) getActivity()
                , qualityGroupShareBean.getGpPicUrl()
                , qualityGroupShareBean.getName()
                , getStrings(qualityGroupShareBean.getSubtitle())
                , Url.BASE_SHARE_PAGE_TWO + "m/template/share_template/groupShare.html?id=" + qualityGroupShareBean.getGpInfoId()
                + "&record=" + qualityGroupShareBean.getGpRecordId(), "pages/groupshare/groupshare?id=" + qualityGroupShareBean.getGpInfoId()
                + (TextUtils.isEmpty(orderNo) ? "&gpRecordId=" + qualityGroupShareBean.getGpRecordId() : "&order=" + orderNo),qualityGroupShareBean.getGpInfoId());
    }


    @Override
    protected boolean isDataInitiated() {
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isOnPause) {
            loadData();
        }
        isOnPause = true;
    }
}
