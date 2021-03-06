package com.amkj.dmsh.shopdetails.integration;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.bean.RequestStatus;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.shopdetails.activity.DirectLogisticsDetailsActivity;
import com.amkj.dmsh.shopdetails.activity.DirectMyCouponActivity;
import com.amkj.dmsh.shopdetails.activity.DirectPublishAppraiseActivity;
import com.amkj.dmsh.shopdetails.adapter.IndentDiscountAdapter;
import com.amkj.dmsh.shopdetails.bean.ApplyRefundCheckEntity;
import com.amkj.dmsh.shopdetails.bean.ApplyRefundCheckEntity.ApplyRefundCheckBean;
import com.amkj.dmsh.shopdetails.bean.DirectApplyRefundBean;
import com.amkj.dmsh.shopdetails.bean.DirectApplyRefundBean.DirectRefundProBean;
import com.amkj.dmsh.shopdetails.bean.DirectAppraisePassBean;
import com.amkj.dmsh.shopdetails.bean.PriceInfoBean;
import com.amkj.dmsh.shopdetails.bean.QualityCreateAliPayIndentBean;
import com.amkj.dmsh.shopdetails.bean.QualityCreateWeChatPayIndentBean;
import com.amkj.dmsh.shopdetails.integration.bean.IntegralIndentOrderEntity.IntegralIndentOrderBean.OrderListBean;
import com.amkj.dmsh.shopdetails.integration.bean.IntegralIndentOrderEntity.IntegralIndentOrderBean.OrderListBean.GoodsBean;
import com.amkj.dmsh.shopdetails.integration.bean.IntegralOrderDetailEntity;
import com.amkj.dmsh.shopdetails.integration.bean.IntegralOrderDetailEntity.IntegralOrderDetailBean;
import com.amkj.dmsh.shopdetails.payutils.AliPay;
import com.amkj.dmsh.shopdetails.payutils.WXPay;
import com.amkj.dmsh.utils.CommunalCopyTextUtils;
import com.amkj.dmsh.views.alertdialog.AlertDialogHelper;
import com.amkj.dmsh.utils.gson.GsonUtils;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;
import com.amkj.dmsh.views.CustomPopWindow;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.showToastRequestMsg;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.CANCEL_ORDER;
import static com.amkj.dmsh.constant.ConstantVariable.CANCEL_PAY_ORDER;
import static com.amkj.dmsh.constant.ConstantVariable.CHECK_LOG;
import static com.amkj.dmsh.constant.ConstantVariable.CONFIRM_ORDER;
import static com.amkj.dmsh.constant.ConstantVariable.INDENT_PRO_STATUS;
import static com.amkj.dmsh.constant.ConstantVariable.LITTER_CONSIGN;
import static com.amkj.dmsh.constant.ConstantVariable.PAY;
import static com.amkj.dmsh.constant.ConstantVariable.PAY_ALI_PAY;
import static com.amkj.dmsh.constant.ConstantVariable.PAY_UNION_PAY;
import static com.amkj.dmsh.constant.ConstantVariable.PAY_WX_PAY;
import static com.amkj.dmsh.constant.ConstantVariable.PRO_APPRAISE;
import static com.amkj.dmsh.constant.ConstantVariable.REFUND_APPLY;
import static com.amkj.dmsh.constant.ConstantVariable.REFUND_FEEDBACK;
import static com.amkj.dmsh.constant.ConstantVariable.REFUND_REPAIR;
import static com.amkj.dmsh.constant.ConstantVariable.REFUND_TYPE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.VIRTUAL_COUPON;

;

/**
 * Created by atd48 on 2016/7/18.
 * ??????????????????
 */
public class IntegExchangeDetailActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.tv_header_shared)
    TextView header_shared;
    @BindView(R.id.tv_header_title)
    TextView tv_header_titleAll;
    @BindView(R.id.smart_communal_refresh)
    SmartRefreshLayout smart_communal_refresh;
    @BindView(R.id.communal_recycler)
    RecyclerView communal_recycler;
    //    ????????????
    @BindView(R.id.ll_indent_bottom)
    LinearLayout ll_indent_bottom;
    @BindView(R.id.tv_indent_border_first_gray)
    TextView tv_indent_border_first_gray;
    @BindView(R.id.tv_indent_border_second_blue)
    TextView tv_indent_border_second_blue;
    private String indentNum;
    private IntegralProductIndentAdapter productIndentAdapter;
    private List<GoodsBean> goodsBeanList = new ArrayList();
    //    ????????????????????????
    private List<PriceInfoBean> priceInfoList = new ArrayList<>();
    private List<DirectAppraisePassBean> directAppraisePassList = new ArrayList<>();
    private RvHeaderView rvHeaderView;
    private RvFootView rvFootView;
    private OrderListBean orderListBean;
    private IndentDiscountAdapter indentDiscountAdapter;
    private CustomPopWindow mCustomPopWindow;
    private String payWay;
    private boolean isOnPause;
    private IntegralOrderDetailEntity integralOrderDetailEntity;
    private AlertDialogHelper confirmOrderDialogHelper;
    private AlertDialogHelper cancelOrderDialogHelper;
    private AlertDialogHelper refundOrderDialogHelper;

    @Override
    protected int getContentView() {
        return R.layout.activity_integ_indent_details_new;
    }

    @Override
    protected void initViews() {
        header_shared.setVisibility(View.INVISIBLE);
        tv_header_titleAll.setText("??????????????????");
        Intent intent = getIntent();
        indentNum = intent.getStringExtra("orderNo");
        communal_recycler.setLayoutManager(new LinearLayoutManager(IntegExchangeDetailActivity.this));
        communal_recycler.addItemDecoration(new ItemDecoration.Builder()
                // ?????????????????????ID
                .setDividerId(R.drawable.item_divider_img_white).create());
        productIndentAdapter = new IntegralProductIndentAdapter(IntegExchangeDetailActivity.this, goodsBeanList);
        View headerView = LayoutInflater.from(IntegExchangeDetailActivity.this)
                .inflate(R.layout.layout_integ_direct_detail_head, (ViewGroup) communal_recycler.getParent(), false);
        rvHeaderView = new RvHeaderView();
        ButterKnife.bind(rvHeaderView, headerView);
        View footView = LayoutInflater.from(IntegExchangeDetailActivity.this).inflate(R.layout.layout_integ_direct_detail_foot, (ViewGroup) communal_recycler.getParent(), false);
        rvFootView = new RvFootView();
        ButterKnife.bind(rvFootView, footView);
        productIndentAdapter.addHeaderView(headerView);
        productIndentAdapter.addFooterView(footView);
        communal_recycler.setAdapter(productIndentAdapter);
        smart_communal_refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                loadData();
                productIndentAdapter.setEnableLoadMore(false);
            }
        });
        productIndentAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                GoodsBean goodsBean = (GoodsBean) view.getTag();
                if (goodsBean != null) {
                    Intent intent = new Intent(IntegExchangeDetailActivity.this, IntegralScrollDetailsActivity.class);
                    intent.putExtra("productId", String.valueOf(goodsBean.getId()));
                    startActivity(intent);
                }
            }
        });
        rvFootView.rv_integral_indent_price.setNestedScrollingEnabled(false);
        rvFootView.rv_integral_indent_price.setLayoutManager(new LinearLayoutManager(this));
        indentDiscountAdapter = new IndentDiscountAdapter(this,priceInfoList);
        rvFootView.rv_integral_indent_price.setAdapter(indentDiscountAdapter);
        ll_indent_bottom.setVisibility(View.GONE);
    }

    @Override
    protected void loadData() {
        String url = Url.INDENT_INTEGRAL_DETAILS;
        Map<String, Object> params = new HashMap<>();
        params.put("no", indentNum);
        params.put("userId", userId);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, url, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                smart_communal_refresh.finishRefresh();
                integralOrderDetailEntity = GsonUtils.fromJson(result, IntegralOrderDetailEntity.class);
                if (integralOrderDetailEntity != null) {
                    if (SUCCESS_CODE.equals(integralOrderDetailEntity.getCode())) {
                        IntegralOrderDetailBean integralOrderDetailBean = integralOrderDetailEntity.getIntegralOrderDetailBean();
                        if (integralOrderDetailBean != null) {
                            INDENT_PRO_STATUS = integralOrderDetailBean.getStatus();
                            setIntegralIndentData(integralOrderDetailBean);
                        }
                    } else {
                        showToast(integralOrderDetailEntity.getMsg());
                    }
                }
                NetLoadUtils.getNetInstance().showLoadSir(loadService, integralOrderDetailEntity);
            }

            @Override
            public void onNotNetOrException() {
                smart_communal_refresh.finishRefresh();
                NetLoadUtils.getNetInstance().showLoadSir(loadService, integralOrderDetailEntity);
            }
        });
    }

    @Override
    public View getLoadView() {
        return smart_communal_refresh;
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    private void setIntegralIndentData(IntegralOrderDetailBean indentInfoBean) {
        orderListBean = indentInfoBean.getOrderListBean();
        goodsBeanList.clear();
        goodsBeanList.addAll(orderListBean.getGoods());
        productIndentAdapter.setNewData(goodsBeanList);
        if (TextUtils.isEmpty(orderListBean.getAddress())) {
            rvHeaderView.ll_indent_address_default.setVisibility(View.GONE);
        } else {
            rvHeaderView.ll_indent_address_default.setVisibility(View.VISIBLE);
            rvHeaderView.img_skip_address.setVisibility(View.GONE);
            rvHeaderView.tv_consignee_name.setText(orderListBean.getConsignee());
//        ?????????
            rvHeaderView.tv_consignee_mobile_number.setText(orderListBean.getMobile());
//        ????????????
            rvHeaderView.tv_indent_details_address.setText(orderListBean.getAddress());
        }
//        ??? ??? ???
        rvFootView.tv_integral_indent_no.setTag(orderListBean.getNo());
        rvFootView.tv_integral_indent_no.setText(("???????????????" + orderListBean.getNo()));
//        ??????????????????
        rvFootView.tv_integral_indent_create_time.setText(("???????????????" + orderListBean.getCreateTime()));
        if (orderListBean.getGoods() != null && orderListBean.getGoods().size() > 0) {
            GoodsBean goodsBean = orderListBean.getGoods().get(0);
            //        ??????????????????
            rvHeaderView.tv_integral_indent_status.setText(getStrings(INDENT_PRO_STATUS.get(String.valueOf(goodsBean.getStatus()))));
            setIntegralIntentStatus(orderListBean, goodsBean);
        }
        if (orderListBean.getPriceInfoBeans() != null && orderListBean.getPriceInfoBeans().size() > 0) {
            priceInfoList.clear();
            priceInfoList.addAll(orderListBean.getPriceInfoBeans());
            indentDiscountAdapter.setNewData(priceInfoList);
        }
    }

    private void setIntegralIntentStatus(OrderListBean orderListBean, GoodsBean goodBean) {
        int statusCode = goodBean.getStatus();
        ll_indent_bottom.setVisibility(View.VISIBLE);
        if (goodBean.getIntegralProductType() == 0) {
            if (0 <= statusCode && statusCode < 10) {
//          ?????? ??????
                tv_indent_border_first_gray.setVisibility(View.VISIBLE);
                tv_indent_border_second_blue.setVisibility(View.VISIBLE);
                tv_indent_border_second_blue.setText("????????????");
                tv_indent_border_first_gray.setText("????????????");
                tv_indent_border_first_gray.setTag(R.id.tag_first, CANCEL_ORDER);
                tv_indent_border_first_gray.setTag(R.id.tag_second, orderListBean);
                tv_indent_border_first_gray.setOnClickListener(this);
                tv_indent_border_second_blue.setTag(R.id.tag_first, PAY);
                tv_indent_border_second_blue.setTag(R.id.tag_second, orderListBean);
                tv_indent_border_second_blue.setOnClickListener(this);
            } else if (12 == statusCode) {
                ll_indent_bottom.setVisibility(View.VISIBLE);
                tv_indent_border_second_blue.setVisibility(View.GONE);
                tv_indent_border_first_gray.setVisibility(View.VISIBLE);
                tv_indent_border_first_gray.setText("????????????");
                tv_indent_border_first_gray.setTag(R.id.tag_first, LITTER_CONSIGN);
                tv_indent_border_first_gray.setTag(R.id.tag_second, orderListBean);
                tv_indent_border_first_gray.setOnClickListener(this);
            } else if (10 <= statusCode && statusCode < 20) {
                if (10 == statusCode) {
                    boolean isRefund = true;
                    for (int i = 0; i < orderListBean.getGoods().size(); i++) {
                        GoodsBean goodsBean = orderListBean.getGoods().get(i);
                        if (goodsBean.getStatus() == 10) {
                            continue;
                        } else {
                            isRefund = false;
                            break;
                        }
                    }
                    if (isRefund) {
//            ????????????
                        tv_indent_border_second_blue.setVisibility(View.GONE);
                        tv_indent_border_first_gray.setVisibility(View.VISIBLE);
                        tv_indent_border_first_gray.setText("????????????");
                        tv_indent_border_first_gray.setTag(R.id.tag_first, CANCEL_PAY_ORDER);
                        tv_indent_border_first_gray.setTag(R.id.tag_second, orderListBean);
                        tv_indent_border_first_gray.setOnClickListener(this);
                    } else {
                        //            ??????????????????
                        ll_indent_bottom.setVisibility(View.GONE);
                    }
                } else {
//            ??????????????????
                    ll_indent_bottom.setVisibility(View.GONE);
                }
            } else if (20 <= statusCode && statusCode < 30) {
                tv_indent_border_first_gray.setVisibility(View.VISIBLE);
                tv_indent_border_second_blue.setVisibility(View.VISIBLE);
                tv_indent_border_first_gray.setText("????????????");
                tv_indent_border_second_blue.setText("????????????");
                tv_indent_border_first_gray.setTag(R.id.tag_first, CHECK_LOG);
                tv_indent_border_first_gray.setTag(R.id.tag_second, orderListBean);
                tv_indent_border_first_gray.setOnClickListener(this);
                tv_indent_border_second_blue.setTag(R.id.tag_first, CONFIRM_ORDER);
                tv_indent_border_second_blue.setTag(R.id.tag_second, orderListBean);
                tv_indent_border_second_blue.setOnClickListener(this);

//          ????????????
                tv_indent_border_second_blue.setTag(R.id.tag_first, CONFIRM_ORDER);
                tv_indent_border_second_blue.setTag(R.id.tag_second, orderListBean);
                tv_indent_border_first_gray.setOnClickListener(this);
            } else if (30 <= statusCode && statusCode <= 40) {
//                if (statusCode == 40) {
                tv_indent_border_first_gray.setVisibility(View.GONE);
//                } else {
//                    tv_indent_border_first_gray.setVisibility(View.VISIBLE);
//                    tv_indent_border_first_gray.setText("??????");
//                    tv_indent_border_first_gray.setTag(R.id.tag_first, PRO_APPRAISE);
//                    tv_indent_border_first_gray.setTag(R.id.tag_second, orderListBean);
//                    tv_indent_border_first_gray.setOnClickListener(this);
//                }
                tv_indent_border_second_blue.setText("????????????");
                tv_indent_border_second_blue.setTag(R.id.tag_first, REFUND_APPLY);
                tv_indent_border_second_blue.setTag(R.id.tag_second, orderListBean);
                tv_indent_border_second_blue.setOnClickListener(this);
            } else if (statusCode == -40 ||
                    (statusCode <= -30 && -32 <= statusCode)
                    || statusCode == -35) {
                tv_indent_border_first_gray.setVisibility(View.GONE);
                tv_indent_border_second_blue.setVisibility(View.VISIBLE);
                tv_indent_border_second_blue.setText(getStrings(INDENT_PRO_STATUS.get(String.valueOf(statusCode))));
                tv_indent_border_second_blue.setTag(R.id.tag_first, REFUND_FEEDBACK);
                tv_indent_border_second_blue.setTag(R.id.tag_second, orderListBean);
                tv_indent_border_second_blue.setOnClickListener(this);
            } else if (50 <= statusCode && statusCode <= 58) {
                tv_indent_border_first_gray.setVisibility(View.GONE);
                tv_indent_border_second_blue.setVisibility(View.VISIBLE);
                tv_indent_border_second_blue.setText(getStrings(INDENT_PRO_STATUS.get(String.valueOf(statusCode))));
                tv_indent_border_second_blue.setTag(R.id.tag_first, REFUND_REPAIR);
                tv_indent_border_second_blue.setTag(R.id.tag_second, orderListBean);
                tv_indent_border_second_blue.setOnClickListener(this);
            } else {
                ll_indent_bottom.setVisibility(View.GONE);
            }
        } else {
            ll_indent_bottom.setVisibility(View.VISIBLE);
            tv_indent_border_second_blue.setText("???????????????");
            tv_indent_border_second_blue.setTag(R.id.tag_first, VIRTUAL_COUPON);
            tv_indent_border_second_blue.setTag(R.id.tag_second, orderListBean);
            tv_indent_border_second_blue.setOnClickListener(this);
            tv_indent_border_first_gray.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.tv_life_back)
    void goBack(View view) {
        finish();
    }

    private void cancelIntegralIndent(String no) {
        String url = Url.Q_INDENT_CANCEL;
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("no", no);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, url, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {

                RequestStatus requestStatus = GsonUtils.fromJson(result, RequestStatus.class);
                if (requestStatus != null) {
                    if (requestStatus.getCode().equals(SUCCESS_CODE)) {
                        showToast("??????????????????");
                        finish();
                    } else {
                        showToastRequestMsg(requestStatus);
                    }
                }
            }

            @Override
            public void onError(Throwable throwable) {
                showToast(R.string.do_failed);
            }
        });
    }

    private void confirmOrder(OrderListBean orderListBean) {
        String url = Url.Q_INDENT_CONFIRM;
        Map<String, Object> params = new HashMap<>();
        params.put("no", orderListBean.getNo());
        params.put("userId", orderListBean.getUserId());
        params.put("orderProductId", 0);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, url, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {

                RequestStatus requestStatus = GsonUtils.fromJson(result, RequestStatus.class);
                if (requestStatus != null) {
                    if (requestStatus.getCode().equals(SUCCESS_CODE)) {
                        showToastRequestMsg(requestStatus);
                        finish();
                    } else {
                        showToastRequestMsg(requestStatus);
                    }
                }
            }
        });
    }

    private void requestRefundData(OrderListBean indentInfoBean) {
        String url = Url.Q_INDENT_APPLY_REFUND_CHECK;
        Map<String, Object> params = new HashMap<>();
        params.put("no", indentInfoBean.getNo());
        params.put("userId", indentInfoBean.getUserId());
        List<GoodsBean> goods = indentInfoBean.getGoods();
        GoodsBean goodsBean;
        if (goods != null && goods.size() > 0) {
            goodsBean = goods.get(0);
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id", goodsBean.getId());
                jsonObject.put("orderProductId", goodsBean.getOrderProductId());
                jsonObject.put("count", goodsBean.getCount());
                JSONArray jsonArray = new JSONArray();
                jsonArray.put(jsonObject);
                params.put("goods", jsonArray.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            showToast("??????????????????????????????");
            return;
        }
        NetLoadUtils.getNetInstance().loadNetDataPost(this, url, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {

                ApplyRefundCheckEntity refundCheckEntity = GsonUtils.fromJson(result, ApplyRefundCheckEntity.class);
                if (refundCheckEntity != null) {
                    if (refundCheckEntity.getCode().equals(SUCCESS_CODE)) {
                        ApplyRefundCheckBean applyRefundCheckBean = refundCheckEntity.getApplyRefundCheckBean();
                        final DirectApplyRefundBean refundBean = new DirectApplyRefundBean();
                        refundBean.setType(1);
                        refundBean.setOrderNo(indentInfoBean.getNo());
                        DirectRefundProBean directRefundProBean = new DirectRefundProBean();
                        directRefundProBean.setId(indentInfoBean.getId());
                        directRefundProBean.setOrderProductId(goodsBean.getOrderProductId());
                        directRefundProBean.setCount(goodsBean.getCount());
                        directRefundProBean.setName(goodsBean.getName());
                        directRefundProBean.setPicUrl(goodsBean.getPicUrl());
                        directRefundProBean.setSaleSkuValue(goodsBean.getSaleSkuValue());
                        directRefundProBean.setIntegralPrice(goodsBean.getIntegralPrice());
                        directRefundProBean.setPrice(goodsBean.getPrice());
                        List<DirectRefundProBean> directProList = new ArrayList<>();
                        directProList.add(directRefundProBean);
                        refundBean.setDirectRefundProList(directProList);
                        refundBean.setRefundPrice(applyRefundCheckBean.getRefundPrice());
                        final Intent intent = new Intent();
                        switch (applyRefundCheckBean.getNoticeFlagType()) {
                            case 0:
                                intent.setClass(IntegExchangeDetailActivity.this, IntegralApplyRefundActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putParcelable("refundPro", refundBean);
                                intent.putExtras(bundle);
                                startActivity(intent);
                                break;
                            case 1:
                                showToast(refundCheckEntity.getApplyRefundCheckBean() == null
                                        ? refundCheckEntity.getMsg() : refundCheckEntity.getApplyRefundCheckBean().getMsg()
                                );
                                break;
                            case 2:
                                if (refundOrderDialogHelper == null) {
                                    refundOrderDialogHelper = new AlertDialogHelper(IntegExchangeDetailActivity.this);
                                    refundOrderDialogHelper.setTitleVisibility(View.GONE).setMsgTextGravity(Gravity.CENTER)
                                            .setMsg(getStrings(applyRefundCheckBean.getMsg())).setCancelText("??????")
                                            .setConfirmText("??????")
                                            .setCancelTextColor(getResources().getColor(R.color.text_login_gray_s));
                                    refundOrderDialogHelper.setAlertListener(new AlertDialogHelper.AlertConfirmCancelListener() {
                                        @Override
                                        public void confirm() {
                                            intent.setClass(IntegExchangeDetailActivity.this, IntegralApplyRefundActivity.class);
                                            Bundle bundle = new Bundle();
                                            bundle.putParcelable("refundPro", refundBean);
                                            intent.putExtra(REFUND_TYPE, REFUND_TYPE);
                                            intent.putExtras(bundle);
                                            startActivity(intent);
                                        }

                                        @Override
                                        public void cancel() {
                                        }
                                    });
                                } else {
                                    refundOrderDialogHelper.setMsg(getStrings(applyRefundCheckBean.getMsg()));
                                }
                                refundOrderDialogHelper.show();
                                break;
                        }
                    } else {
                        showToast(refundCheckEntity.getApplyRefundCheckBean() == null
                                ? refundCheckEntity.getMsg() : refundCheckEntity.getApplyRefundCheckBean().getMsg()
                        );
                    }
                }
            }

            @Override
            public void onError(Throwable throwable) {
                showToast(R.string.invalidData);
            }
        });
    }

    @Override
    public void onClick(View v) {
        String type = (String) v.getTag(R.id.tag_first);
        OrderListBean indentInfoBean = (OrderListBean) v.getTag(R.id.tag_second);
        List<GoodsBean> goods = orderListBean.getGoods();
        GoodsBean goodsBean = null;
        if (goods != null && goods.size() > 0) {
            goodsBean = goods.get(0);
        } else {
            showToast("????????????????????????????????????");
            return;
        }
        Intent intent = new Intent();
        switch (type) {
            case CANCEL_ORDER:
//                        ????????????
                if (cancelOrderDialogHelper == null) {
                    cancelOrderDialogHelper = new AlertDialogHelper(this);
                    cancelOrderDialogHelper.setTitleVisibility(View.GONE).setMsgTextGravity(Gravity.CENTER)
                            .setMsg("??????????????????????????????").setCancelText("??????").setConfirmText("??????")
                            .setCancelTextColor(getResources().getColor(R.color.text_login_gray_s));
                    cancelOrderDialogHelper.setAlertListener(new AlertDialogHelper.AlertConfirmCancelListener() {
                        @Override
                        public void confirm() {
                            cancelIntegralIndent(orderListBean.getNo());
                        }

                        @Override
                        public void cancel() {
                        }
                    });
                }
                cancelOrderDialogHelper.show();
                cancelIntegralIndent(orderListBean.getNo());
                break;
            case CANCEL_PAY_ORDER:
                requestRefundData(indentInfoBean);
                break;
            case PAY:
                View indentPopWindow = getLayoutInflater().inflate(R.layout.layout_indent_pay_pop, null);
                PopupWindowView popupWindowView = new PopupWindowView();
                ButterKnife.bind(popupWindowView, indentPopWindow);
                mCustomPopWindow = new CustomPopWindow.PopupWindowBuilder(this)
                        .setView(indentPopWindow)
                        .enableBackgroundDark(true) //??????popWindow????????????????????????
                        .setBgDarkAlpha(0.7f) // ????????????
                        .create().showAtLocation((View) communal_recycler.getParent(), Gravity.CENTER, 0, 0);
                break;
            case CHECK_LOG:
            case LITTER_CONSIGN:
//                        ????????????
                intent.setClass(this, DirectLogisticsDetailsActivity.class);
                intent.putExtra("orderNo", orderListBean.getNo());
                startActivity(intent);
                break;
            case CONFIRM_ORDER:
//                        ????????????
                if (confirmOrderDialogHelper == null) {
                    confirmOrderDialogHelper = new AlertDialogHelper(this);
                    confirmOrderDialogHelper.setTitleVisibility(View.GONE).setMsgTextGravity(Gravity.CENTER)
                            .setMsg("??????????????????????").setCancelText("??????").setConfirmText("??????")
                            .setCancelTextColor(getResources().getColor(R.color.text_login_gray_s));
                    confirmOrderDialogHelper.setAlertListener(new AlertDialogHelper.AlertConfirmCancelListener() {
                        @Override
                        public void confirm() {
                            confirmOrder(orderListBean);
                        }

                        @Override
                        public void cancel() {
                        }
                    });
                }
                confirmOrderDialogHelper.show();
                break;
            case PRO_APPRAISE:
//                        ??????
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
                    intent.setClass(this, DirectPublishAppraiseActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("appraiseData", (ArrayList<? extends Parcelable>) directAppraisePassList);
                    intent.putExtras(bundle);
                    intent.putExtra("orderNo", orderListBean.getNo());
                    startActivity(intent);
                }
                break;
            case VIRTUAL_COUPON:
                intent.setClass(this, DirectMyCouponActivity.class);
                startActivity(intent);
                break;
            case REFUND_APPLY:
                //              ????????????
                DirectApplyRefundBean refundBean = new DirectApplyRefundBean();
                refundBean.setType(2);
                refundBean.setOrderNo(orderListBean.getNo());
                DirectRefundProBean directRefundProBean = new DirectRefundProBean();
                directRefundProBean.setId(orderListBean.getId());
                if (goodsBean != null) {
                    directRefundProBean.setOrderProductId(goodsBean.getOrderProductId());
                    directRefundProBean.setCount(goodsBean.getCount());
                    directRefundProBean.setName(goodsBean.getName());
                    directRefundProBean.setPicUrl(goodsBean.getPicUrl());
                    directRefundProBean.setSaleSkuValue(goodsBean.getSaleSkuValue());
                    directRefundProBean.setPrice(goodsBean.getPrice());
                    directRefundProBean.setIntegralPrice(goodsBean.getIntegralPrice());
                }
                List<DirectRefundProBean> directProList = new ArrayList<>();
                directProList.add(directRefundProBean);
                refundBean.setDirectRefundProList(directProList);
                intent.setClass(IntegExchangeDetailActivity.this, IntegralApplyRefundActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("refundPro", refundBean);
                intent.putExtras(bundle);
                intent.putExtra(REFUND_TYPE, REFUND_TYPE);
                startActivity(intent);
                break;
            case REFUND_FEEDBACK:
                //                 ??????????????? ????????? ??????????????????
                if (goodsBean != null && goodsBean.getOrderRefundProductId() > 0) {
                    intent.setClass(IntegExchangeDetailActivity.this, IntegralRefundDetailActivity.class);
                    intent.putExtra("orderNo", orderListBean.getNo());
                    intent.putExtra("orderProductId", String.valueOf(goodsBean.getOrderProductId()));
                    intent.putExtra("orderRefundProductId", String.valueOf(goodsBean.getOrderRefundProductId()));
                    intent.putExtra(REFUND_TYPE, REFUND_TYPE);
                    startActivity(intent);
                }
                break;
        }
    }

    class RvHeaderView {
        //    ???????????????
        @BindView(R.id.tv_consignee_name)
        TextView tv_consignee_name;
        @BindView(R.id.ll_indent_address_default)
        LinearLayout ll_indent_address_default;
        //    ?????????????????????
        @BindView(R.id.tv_consignee_mobile_number)
        TextView tv_consignee_mobile_number;
        //    ????????????
        @BindView(R.id.tv_indent_details_address)
        TextView tv_indent_details_address;
        @BindView(R.id.img_skip_address)
        ImageView img_skip_address;
        //        ??????????????????
        @BindView(R.id.tv_indent_border_first_gray)
        TextView tv_integral_indent_status;
    }

    class RvFootView {
        //        ????????????
        @BindView(R.id.rv_integral_indent_price)
        RecyclerView rv_integral_indent_price;
        //    ?????????
        @BindView(R.id.tv_integ_indent_no)
        TextView tv_integral_indent_no;
        //    ????????????
        @BindView(R.id.tv_integ_indent_create_time)
        TextView tv_integral_indent_create_time;

        @OnLongClick(R.id.tv_integ_indent_no)
        boolean copyIndentNumber(View view) {
            CommunalCopyTextUtils.showPopWindow(IntegExchangeDetailActivity.this, (TextView) view, (String) view.getTag());
            return true;
        }

        @OnClick(R.id.tv_copy_text)
        void copyNo(View view) {
            String content = (String) tv_integral_indent_no.getTag();
            ClipboardManager cmb = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData mClipData = ClipData.newPlainText("Label", getStrings(content));
            cmb.setPrimaryClip(mClipData);
            showToast("?????????");
        }
    }

    class PopupWindowView {
        @OnClick({R.id.ll_pay_ali
                , R.id.ll_pay_we_chat
                , R.id.ll_pay_union
                , R.id.tv_pay_cancel})
        void clickPayView(View view) {
            mCustomPopWindow.dissmiss();
            switch (view.getId()) {
//                            ?????????
                case R.id.ll_pay_ali:
                    //                ?????????????????????
                    payWay = PAY_ALI_PAY;
                    paymentIndent();
                    break;
//                            ??????
                case R.id.ll_pay_we_chat:
                    payWay = PAY_WX_PAY;
//                ??????????????????
                    paymentIndent();
                    break;
                case R.id.ll_pay_union:
                    payWay = PAY_UNION_PAY;
//                ??????????????????
                    paymentIndent();
                    break;
//                            ??????
                case R.id.tv_pay_cancel:
                    break;
            }
        }
    }

    private void paymentIndent() {
        String url = Url.Q_PAYMENT_INDENT;
        Map<String, Object> params = new HashMap<>();
        params.put("no", orderListBean.getNo());
        params.put("userId", orderListBean.getUserId());
        params.put("buyType", payWay);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, url, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {

                if (payWay.equals(PAY_WX_PAY)) {
                    QualityCreateWeChatPayIndentBean qualityWeChatIndent = GsonUtils.fromJson(result, QualityCreateWeChatPayIndentBean.class);
                    if (qualityWeChatIndent != null) {
                        if (qualityWeChatIndent.getCode().equals(SUCCESS_CODE)) {
                            //???????????????????????????????????????
                            doWXPay(qualityWeChatIndent.getResult());
                        } else {
                            showToast(qualityWeChatIndent.getResult() == null
                                    ? qualityWeChatIndent.getMsg() : qualityWeChatIndent.getResult().getMsg());
                        }
                    }
                } else if (payWay.equals(PAY_ALI_PAY)) {
                    QualityCreateAliPayIndentBean qualityAliPayIndent = GsonUtils.fromJson(result, QualityCreateAliPayIndentBean.class);
                    if (qualityAliPayIndent != null) {
                        if (qualityAliPayIndent.getCode().equals(SUCCESS_CODE)) {
                            //??????????????????????????????????????????
                            doAliPay(qualityAliPayIndent.getResult());
                        } else {
                            showToast(qualityAliPayIndent.getResult() == null
                                    ? qualityAliPayIndent.getMsg() : qualityAliPayIndent.getResult().getMsg());
                        }
                    }
                }
            }

            @Override
            public void onError(Throwable throwable) {
                showToast(R.string.do_failed);
            }
        });
    }

    private void doWXPay(QualityCreateWeChatPayIndentBean.ResultBean resultBean) {
        WXPay.init(this);//?????????????????????
        WXPay.getInstance().doPayDateObject(resultBean.getNo(), resultBean.getPayKey(), new WXPay.WXPayResultCallBack() {
            @Override
            public void onSuccess() {
//                recordPaySuc();
                showToast("????????????");
                loadData();
            }

            @Override
            public void onError(int error_code) {
                switch (error_code) {
                    case WXPay.NO_OR_LOW_WX:
                        showToast("????????????????????????????????????");
                        break;
                    case WXPay.ERROR_PAY_PARAM:
                        showToast("????????????");
                        break;
                    case WXPay.ERROR_PAY:
                        showToast("????????????");
                        break;
                }
            }

            @Override
            public void onCancel() {
                showToast("????????????");
            }
        });
    }

    private void doAliPay(QualityCreateAliPayIndentBean.ResultBean resultBean) {
        new AliPay(this, resultBean.getNo(), resultBean.getPayKey(), new AliPay.AliPayResultCallBack() {
            @Override
            public void onSuccess() {
                showToast("????????????");
//                recordPaySuc();
                loadData();
            }

            @Override
            public void onDealing() {
                showToast("???????????????...");
            }

            @Override
            public void onError(int error_code) {
                switch (error_code) {
                    case AliPay.ERROR_RESULT:
                        showToast("????????????:????????????????????????");
                        break;

                    case AliPay.ERROR_NETWORK:
                        showToast("????????????:??????????????????");
                        break;

                    case AliPay.ERROR_PAY:
                        showToast("????????????:?????????????????????");
                        break;

                    default:
                        showToast("????????????");
                        break;
                }

            }

            @Override
            public void onCancel() {
                showToast("????????????");
            }
        }).doPay();
    }

    @Override
    protected void onPause() {
        isOnPause = true;
        super.onPause();
    }

    @Override
    protected void onResume() {
        if (isOnPause) {
            loadData();
        }
        super.onResume();
        isOnPause = false;
    }
}
