package com.amkj.dmsh.shopdetails.integration;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.amkj.dmsh.base.NetLoadUtils;
import com.amkj.dmsh.bean.RequestStatus;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.constant.XUtil;
import com.amkj.dmsh.release.dialogutils.AlertSettingBean;
import com.amkj.dmsh.release.dialogutils.AlertView;
import com.amkj.dmsh.release.dialogutils.OnAlertItemClickListener;
import com.amkj.dmsh.shopdetails.activity.DirectApplyRefundActivity;
import com.amkj.dmsh.shopdetails.activity.DirectLogisticsDetailsActivity;
import com.amkj.dmsh.shopdetails.activity.DirectMyCouponActivity;
import com.amkj.dmsh.shopdetails.activity.DirectPublishAppraiseActivity;
import com.amkj.dmsh.shopdetails.activity.DoMoRefundDetailActivity;
import com.amkj.dmsh.shopdetails.adapter.IndentDiscountAdapter;
import com.amkj.dmsh.shopdetails.alipay.AliPay;
import com.amkj.dmsh.shopdetails.bean.ApplyRefundCheckEntity;
import com.amkj.dmsh.shopdetails.bean.ApplyRefundCheckEntity.ApplyRefundCheckBean;
import com.amkj.dmsh.shopdetails.bean.DirectApplyRefundBean;
import com.amkj.dmsh.shopdetails.bean.DirectApplyRefundBean.DirectRefundProBean;
import com.amkj.dmsh.shopdetails.bean.DirectAppraisePassBean;
import com.amkj.dmsh.shopdetails.bean.IndentDiscountsEntity.IndentDiscountsBean.PriceInfoBean;
import com.amkj.dmsh.shopdetails.bean.QualityCreateAliPayIndentBean;
import com.amkj.dmsh.shopdetails.bean.QualityCreateWeChatPayIndentBean;
import com.amkj.dmsh.shopdetails.integration.bean.IntegralIndentOrderEntity.IntegralIndentOrderBean.OrderListBean;
import com.amkj.dmsh.shopdetails.integration.bean.IntegralIndentOrderEntity.IntegralIndentOrderBean.OrderListBean.GoodsBean;
import com.amkj.dmsh.shopdetails.integration.bean.IntegralOrderDetailEntity;
import com.amkj.dmsh.shopdetails.integration.bean.IntegralOrderDetailEntity.IntegralOrderDetailBean;
import com.amkj.dmsh.shopdetails.weixin.WXPay;
import com.amkj.dmsh.utils.CommunalCopyTextUtils;
import com.amkj.dmsh.utils.inteface.MyCallBack;
import com.amkj.dmsh.views.CustomPopWindow;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.oushangfeng.pinnedsectionitemdecoration.PinnedHeaderItemDecoration;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
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
import static com.amkj.dmsh.constant.ConstantVariable.PAY_ALI_PAY;
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
 * 积分订单详情
 */
public class IntegExchangeDetailActivity extends BaseActivity implements OnAlertItemClickListener, View.OnClickListener {
    @BindView(R.id.tv_header_shared)
    TextView header_shared;
    @BindView(R.id.tv_header_title)
    TextView tv_header_titleAll;
    @BindView(R.id.smart_communal_refresh)
    SmartRefreshLayout smart_communal_refresh;
    @BindView(R.id.communal_recycler)
    RecyclerView communal_recycler;
    //    积分订单
    @BindView(R.id.ll_indent_bottom)
    LinearLayout ll_indent_bottom;
    @BindView(R.id.tv_indent_border_first_gray)
    TextView tv_indent_border_first_gray;
    @BindView(R.id.tv_indent_border_second_blue)
    TextView tv_indent_border_second_blue;
    private String indentNum;
    private IntegralProductIndentAdapter productIndentAdapter;
    private List<GoodsBean> goodsBeanList = new ArrayList();
    //    订单价格优惠列表
    private List<PriceInfoBean> priceInfoList = new ArrayList<>();
    private List<DirectAppraisePassBean> directAppraisePassList = new ArrayList<>();
    private RvHeaderView rvHeaderView;
    private RvFootView rvFootView;
    private AlertView cancelOrderDialog;
    private AlertView confirmOrderDialog;
    private OrderListBean orderListBean;
    private IndentDiscountAdapter indentDiscountAdapter;
    private CustomPopWindow mCustomPopWindow;
    private String payWay;
    private boolean isOnPause;
    private IntegralOrderDetailEntity integralOrderDetailEntity;

    @Override
    protected int getContentView() {
        return R.layout.activity_integ_indent_details_new;
    }

    @Override
    protected void initViews() {
        header_shared.setVisibility(View.INVISIBLE);
        tv_header_titleAll.setText("积分兑换详情");
        Intent intent = getIntent();
        indentNum = intent.getStringExtra("orderNo");
        communal_recycler.setLayoutManager(new LinearLayoutManager(IntegExchangeDetailActivity.this));
        communal_recycler.addItemDecoration(new PinnedHeaderItemDecoration.Builder(-1)
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_img_white)
                // 开启绘制分隔线，默认关闭
                .enableDivider(true)
                // 是否关闭标签点击事件，默认开启
                .disableHeaderClick(false)
                // 设置标签和其内部的子控件的监听，若设置点击监听不为null，但是disableHeaderClick(true)的话，还是不会响应点击事件
                .setHeaderClickListener(null)
                .create());
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
        smart_communal_refresh.setOnRefreshListener((refreshLayout) -> {
            loadData();
            productIndentAdapter.setEnableLoadMore(false);
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
        indentDiscountAdapter = new IndentDiscountAdapter(priceInfoList);
        rvFootView.rv_integral_indent_price.setAdapter(indentDiscountAdapter);
        ll_indent_bottom.setVisibility(View.GONE);
    }

    @Override
    protected void loadData() {
        String url = Url.BASE_URL + Url.INDENT_INTEGRAL_DETAILS;
        Map<String, Object> params = new HashMap<>();
        params.put("no", indentNum);
        params.put("userId", userId);
        NetLoadUtils.getQyInstance().loadNetDataPost(mAppContext, url, params, new NetLoadUtils.NetLoadListener() {
            @Override
            public void onSuccess(String result) {
                smart_communal_refresh.finishRefresh();
                String code = "";
                String msg = "";
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    code = (String) jsonObject.get("code");
                    msg = (String) jsonObject.get("msg");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (code.equals(SUCCESS_CODE)) {
                    Gson gson = new Gson();
                    integralOrderDetailEntity = gson.fromJson(result, IntegralOrderDetailEntity.class);
                    if (integralOrderDetailEntity != null) {
                        IntegralOrderDetailBean integralOrderDetailBean = integralOrderDetailEntity.getIntegralOrderDetailBean();
                        if (integralOrderDetailBean != null) {
                            INDENT_PRO_STATUS = integralOrderDetailBean.getStatus();
                            setIntegralIndentData(integralOrderDetailBean);
                        }
                    }
                } else if (code.equals(EMPTY_CODE)) {
                    showToast(IntegExchangeDetailActivity.this, R.string.shopOverdue);
                } else {
                    showToast(IntegExchangeDetailActivity.this, msg);
                }
                NetLoadUtils.getQyInstance().showLoadSir(loadService,code);
            }

            @Override
            public void netClose() {
                smart_communal_refresh.finishRefresh();
                showToast(IntegExchangeDetailActivity.this, R.string.connectedFaile);
                NetLoadUtils.getQyInstance().showLoadSir(loadService,integralOrderDetailEntity);
            }

            @Override
            public void onError(Throwable throwable) {
                smart_communal_refresh.finishRefresh();
                showToast(IntegExchangeDetailActivity.this, R.string.connectedFaile);
                NetLoadUtils.getQyInstance().showLoadSir(loadService,integralOrderDetailEntity);
            }
        });
    }

    @Override
    protected View getLoadView() {
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
//        手机号
            rvHeaderView.tv_consignee_mobile_number.setText(orderListBean.getMobile());
//        收货地址
            rvHeaderView.tv_indent_details_address.setText(orderListBean.getAddress());
        }
//        订 单 号
        rvFootView.tv_integral_indent_no.setTag(orderListBean.getNo());
        rvFootView.tv_integral_indent_no.setText(("订单编号：" + orderListBean.getNo()));
//        订单创建时间
        rvFootView.tv_integral_indent_create_time.setText(("付款时间：" + orderListBean.getCreateTime()));
        if (orderListBean.getGoods() != null && orderListBean.getGoods().size() > 0) {
            GoodsBean goodsBean = orderListBean.getGoods().get(0);
            //        积分订单状态
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
//          底栏 件数
                tv_indent_border_first_gray.setVisibility(View.VISIBLE);
                tv_indent_border_second_blue.setVisibility(View.VISIBLE);
                tv_indent_border_second_blue.setText("立即付款");
                tv_indent_border_first_gray.setText("取消订单");
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
                tv_indent_border_first_gray.setText("查看物流");
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
//            取消订单
                        tv_indent_border_second_blue.setVisibility(View.GONE);
                        tv_indent_border_first_gray.setVisibility(View.VISIBLE);
                        tv_indent_border_first_gray.setText("取消订单");
                        tv_indent_border_first_gray.setTag(R.id.tag_first, CANCEL_PAY_ORDER);
                        tv_indent_border_first_gray.setTag(R.id.tag_second, orderListBean);
                        tv_indent_border_first_gray.setOnClickListener(this);
                    } else {
                        //            不可取消订单
                        ll_indent_bottom.setVisibility(View.GONE);
                    }
                } else {
//            不可取消订单
                    ll_indent_bottom.setVisibility(View.GONE);
                }
            } else if (20 <= statusCode && statusCode < 30) {
                tv_indent_border_first_gray.setVisibility(View.VISIBLE);
                tv_indent_border_second_blue.setVisibility(View.VISIBLE);
                tv_indent_border_first_gray.setText("查看物流");
                tv_indent_border_second_blue.setText("确认收货");
                tv_indent_border_first_gray.setTag(R.id.tag_first, CHECK_LOG);
                tv_indent_border_first_gray.setTag(R.id.tag_second, orderListBean);
                tv_indent_border_first_gray.setOnClickListener(this);
                tv_indent_border_second_blue.setTag(R.id.tag_first, CONFIRM_ORDER);
                tv_indent_border_second_blue.setTag(R.id.tag_second, orderListBean);
                tv_indent_border_second_blue.setOnClickListener(this);

//          确认订单
                tv_indent_border_second_blue.setTag(R.id.tag_first, CONFIRM_ORDER);
                tv_indent_border_second_blue.setTag(R.id.tag_second, orderListBean);
                tv_indent_border_first_gray.setOnClickListener(this);
            } else if (30 <= statusCode && statusCode <= 40) {
                if(statusCode == 40){
                    tv_indent_border_first_gray.setVisibility(View.GONE);
                }else{
                    tv_indent_border_first_gray.setVisibility(View.VISIBLE);
                    tv_indent_border_first_gray.setText("评价");
                    tv_indent_border_first_gray.setTag(R.id.tag_first, PRO_APPRAISE);
                    tv_indent_border_first_gray.setTag(R.id.tag_second, orderListBean);
                    tv_indent_border_first_gray.setOnClickListener(this);
                }
                tv_indent_border_second_blue.setText("申请售后");
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
            tv_indent_border_second_blue.setText("查看优惠券");
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
        String url = Url.BASE_URL + Url.Q_INDENT_CANCEL;
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("no", no);
        XUtil.Post(url, params, new MyCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                RequestStatus requestStatus = gson.fromJson(result, RequestStatus.class);
                if (requestStatus != null) {
                    if (requestStatus.getCode().equals("01")) {
                        showToast(IntegExchangeDetailActivity.this, "取消订单成功");
                        finish();
                    } else {
                        showToast(IntegExchangeDetailActivity.this, requestStatus.getMsg());
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                showToast(IntegExchangeDetailActivity.this, R.string.do_failed);
                super.onError(ex, isOnCallback);
            }
        });
    }

    private void confirmOrder(OrderListBean orderListBean) {
        String url = Url.BASE_URL + Url.Q_INDENT_CONFIRM;
        Map<String, Object> params = new HashMap<>();
        params.put("no", orderListBean.getNo());
        params.put("userId", orderListBean.getUserId());
        params.put("orderProductId", 0);
        XUtil.Post(url, params, new MyCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                RequestStatus requestStatus = gson.fromJson(result, RequestStatus.class);
                if (requestStatus != null) {
                    if (requestStatus.getCode().equals("01")) {
                        showToast(IntegExchangeDetailActivity.this, requestStatus.getMsg());
                        finish();
                    } else {
                        showToast(IntegExchangeDetailActivity.this, requestStatus.getResult() != null ?
                                requestStatus.getResult().getMsg() : requestStatus.getMsg());
                    }
                }
            }
        });
    }

    private void requestRefundData(OrderListBean indentInfoBean) {
        String url = Url.BASE_URL + Url.Q_INDENT_APPLY_REFUND_CHECK;
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
            showToast(IntegExchangeDetailActivity.this, "数据异常，请刷新重试");
            return;
        }

        XUtil.Post(url, params, new MyCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                ApplyRefundCheckEntity refundCheckEntity = gson.fromJson(result, ApplyRefundCheckEntity.class);
                if (refundCheckEntity != null) {
                    if (refundCheckEntity.getCode().equals("01")) {
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
                                intent.setClass(IntegExchangeDetailActivity.this, DirectApplyRefundActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putParcelable("refundPro", refundBean);
                                intent.putExtras(bundle);
                                startActivity(intent);
                                break;
                            case 1:
                                showToast(IntegExchangeDetailActivity.this, refundCheckEntity.getApplyRefundCheckBean() == null
                                        ? refundCheckEntity.getMsg() : refundCheckEntity.getApplyRefundCheckBean().getMsg()
                                );
                                break;
                            case 2:
                                AlertSettingBean alertSettingBean = new AlertSettingBean();
                                AlertSettingBean.AlertData alertData = new AlertSettingBean.AlertData();
                                alertData.setCancelStr("取消");
                                alertData.setDetermineStr("确定");
                                alertData.setFirstDet(true);
                                alertData.setTitle(getStrings(applyRefundCheckBean.getMsg()));
                                alertSettingBean.setStyle(AlertView.Style.Alert);
                                alertSettingBean.setAlertData(alertData);
                                AlertView refundDialog = new AlertView(alertSettingBean, IntegExchangeDetailActivity.this, new OnAlertItemClickListener() {
                                    @Override
                                    public void onAlertItemClick(Object o, int position) {
                                        if (position != AlertView.CANCELPOSITION) {
                                            intent.setClass(IntegExchangeDetailActivity.this, DirectApplyRefundActivity.class);
                                            Bundle bundle = new Bundle();
                                            bundle.putParcelable("refundPro", refundBean);
                                            intent.putExtra(REFUND_TYPE, REFUND_TYPE);
                                            intent.putExtras(bundle);
                                            startActivity(intent);
                                        }
                                    }
                                });
                                refundDialog.setCancelable(true);
                                refundDialog.show();
                                break;
                        }
                    } else {
                        showToast(IntegExchangeDetailActivity.this, refundCheckEntity.getApplyRefundCheckBean() == null
                                ? refundCheckEntity.getMsg() : refundCheckEntity.getApplyRefundCheckBean().getMsg()
                        );
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                showToast(IntegExchangeDetailActivity.this, R.string.unConnectedNetwork);
                super.onError(ex, isOnCallback);
            }
        });
    }

    @Override
    public void onAlertItemClick(Object o, int position) {
        if (o == cancelOrderDialog && position != AlertView.CANCELPOSITION && orderListBean != null) {
            cancelIntegralIndent(orderListBean.getNo());
        } else if (o == confirmOrderDialog && position != AlertView.CANCELPOSITION && orderListBean != null) {
            confirmOrder(orderListBean);
        }
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
            showToast(this, "商品数据异常，请刷新重试");
            return;
        }
        Intent intent = new Intent();
        AlertSettingBean alertSettingBean;
        AlertSettingBean.AlertData alertData;
        switch (type) {
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
                cancelOrderDialog = new AlertView(alertSettingBean, this, this);
                cancelOrderDialog.setCancelable(true);
                cancelOrderDialog.show();
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
                        .enableBackgroundDark(true) //弹出popWindow时，背景是否变暗
                        .setBgDarkAlpha(0.7f) // 控制亮度
                        .create().showAtLocation((View) communal_recycler.getParent(), Gravity.CENTER, 0, 0);
                break;
            case CHECK_LOG:
            case LITTER_CONSIGN:
//                        查看物流
                intent.setClass(this, DirectLogisticsDetailsActivity.class);
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
                confirmOrderDialog = new AlertView(alertSettingBean, this, this);
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
                //              申请售后
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
                intent.setClass(IntegExchangeDetailActivity.this, DirectApplyRefundActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("refundPro", refundBean);
                intent.putExtras(bundle);
                intent.putExtra(REFUND_TYPE, REFUND_TYPE);
                startActivity(intent);
                break;
            case REFUND_FEEDBACK:
                //                 退款处理中 被驳回 退货申请通过
                if (goodsBean != null && goodsBean.getOrderRefundProductId() > 0) {
                    intent.setClass(IntegExchangeDetailActivity.this, DoMoRefundDetailActivity.class);
                    intent.putExtra("no", orderListBean.getNo());
                    intent.putExtra("orderProductId", String.valueOf(goodsBean.getOrderProductId()));
                    intent.putExtra("orderRefundProductId", String.valueOf(goodsBean.getOrderRefundProductId()));
                    intent.putExtra(REFUND_TYPE, REFUND_TYPE);
                    startActivity(intent);
                }
                break;
            case REFUND_REPAIR:
                intent.setClass(IntegExchangeDetailActivity.this, DoMoRefundDetailActivity.class);
                intent.putExtra("orderProductId", String.valueOf(goodsBean.getOrderProductId()));
                intent.putExtra("no", orderListBean.getNo());
                intent.putExtra("orderRefundProductId", String.valueOf(goodsBean.getOrderRefundProductId()));
                intent.putExtra(REFUND_TYPE, REFUND_REPAIR);
                startActivity(intent);
                break;
        }
    }

    class RvHeaderView {
        //    收件人名字
        @BindView(R.id.tv_consignee_name)
        TextView tv_consignee_name;
        @BindView(R.id.ll_indent_address_default)
        LinearLayout ll_indent_address_default;
        //    收件人手机号码
        @BindView(R.id.tv_consignee_mobile_number)
        TextView tv_consignee_mobile_number;
        //    订单地址
        @BindView(R.id.tv_indent_details_address)
        TextView tv_indent_details_address;
        @BindView(R.id.img_skip_address)
        ImageView img_skip_address;
        //        积分订单状态
        @BindView(R.id.tv_indent_border_first_gray)
        TextView tv_integral_indent_status;
    }

    class RvFootView {
        //        积分价格
        @BindView(R.id.rv_integral_indent_price)
        RecyclerView rv_integral_indent_price;
        //    订单号
        @BindView(R.id.tv_integ_indent_no)
        TextView tv_integral_indent_no;
        //    订单时间
        @BindView(R.id.tv_integ_indent_create_time)
        TextView tv_integral_indent_create_time;

        @OnLongClick(R.id.tv_integ_indent_no)
        boolean copyIndentNumber(View view) {
            CommunalCopyTextUtils.showPopWindow(IntegExchangeDetailActivity.this, (TextView) view, (String) view.getTag());
            return true;
        }

        @OnClick(R.id.tv_copy_text)
        void copyNo(View view){
            String content = (String) tv_integral_indent_no.getTag();
            ClipboardManager cmb = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData mClipData = ClipData.newPlainText("Label", getStrings(content));
            cmb.setPrimaryClip(mClipData);
            showToast(IntegExchangeDetailActivity.this, "已复制");
        }
    }

    class PopupWindowView {
        @OnClick({R.id.ll_pay_ali, R.id.iv_pay_icon_ali, R.id.tv_pay_text_ali
                , R.id.ll_pay_we_chat, R.id.iv_pay_icon_we_chat, R.id.tv_pay_text_we_chat
                , R.id.tv_pay_cancel})
        void clickPayView(View view) {
            mCustomPopWindow.dissmiss();
            switch (view.getId()) {
//                            支付宝
                case R.id.ll_pay_ali:
                case R.id.iv_pay_icon_ali:
                case R.id.tv_pay_text_ali:
                    //                调起支付宝支付
                    payWay = PAY_ALI_PAY;
                    paymentIndent();
                    break;
//                            微信
                case R.id.ll_pay_we_chat:
                case R.id.iv_pay_icon_we_chat:
                case R.id.tv_pay_text_we_chat:
                    payWay = PAY_WX_PAY;
//                调起微信支付
                    paymentIndent();
                    break;
//                            取消
                case R.id.tv_pay_cancel:
                    break;
            }
        }
    }

    private void paymentIndent() {
        String url = Url.BASE_URL + Url.Q_PAYMENT_INDENT;
        Map<String, Object> params = new HashMap<>();
        params.put("no", orderListBean.getNo());
        params.put("userId", orderListBean.getUserId());
        params.put("buyType", payWay);
        XUtil.Post(url, params, new MyCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                if (payWay.equals(PAY_WX_PAY)) {
                    QualityCreateWeChatPayIndentBean qualityWeChatIndent = gson.fromJson(result, QualityCreateWeChatPayIndentBean.class);
                    if (qualityWeChatIndent != null) {
                        if (qualityWeChatIndent.getCode().equals("01")) {
                            //返回成功，调起微信支付接口
                            doWXPay(qualityWeChatIndent.getResult().getPayKey());
                        } else {
                            showToast(IntegExchangeDetailActivity.this, qualityWeChatIndent.getResult() == null
                                    ? qualityWeChatIndent.getMsg() : qualityWeChatIndent.getResult().getMsg());
                        }
                    }
                } else if (payWay.equals(PAY_ALI_PAY)) {
                    QualityCreateAliPayIndentBean qualityAliPayIndent = gson.fromJson(result, QualityCreateAliPayIndentBean.class);
                    if (qualityAliPayIndent != null) {
                        if (qualityAliPayIndent.getCode().equals("01")) {
                            //返回成功，调起支付宝支付接口
                            doAliPay(qualityAliPayIndent.getResult().getPayKey());
                        } else {
                            showToast(IntegExchangeDetailActivity.this, qualityAliPayIndent.getResult() == null
                                    ? qualityAliPayIndent.getMsg() : qualityAliPayIndent.getResult().getMsg());
                        }
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                showToast(IntegExchangeDetailActivity.this, R.string.unConnectedNetwork);
                super.onError(ex, isOnCallback);
            }
        });
    }

    private void doWXPay(QualityCreateWeChatPayIndentBean.ResultBean.PayKeyBean pay_param) {
        WXPay.init(getApplicationContext());//要在支付前调用
        WXPay.getInstance().doPayDateObject(pay_param, new WXPay.WXPayResultCallBack() {
            @Override
            public void onSuccess() {
//                recordPaySuc();
                showToast(getApplication(), "支付成功");
                loadData();
            }

            @Override
            public void onError(int error_code) {
                switch (error_code) {
                    case WXPay.NO_OR_LOW_WX:
                        showToast(getApplication(), "未安装微信或微信版本过低");
                        break;
                    case WXPay.ERROR_PAY_PARAM:
                        showToast(getApplication(), "参数错误");
                        break;
                    case WXPay.ERROR_PAY:
                        showToast(getApplication(), "支付失败");
                        break;
                }
            }

            @Override
            public void onCancel() {
                showToast(getApplication(), "支付取消");
            }
        });
    }

    private void doAliPay(String pay_param) {
        new AliPay(this, pay_param, new AliPay.AliPayResultCallBack() {
            @Override
            public void onSuccess() {
                showToast(getApplication(), "支付成功");
//                recordPaySuc();
                loadData();
            }

            @Override
            public void onDealing() {
                showToast(getApplication(), "支付处理中...");
            }

            @Override
            public void onError(int error_code) {
                switch (error_code) {
                    case AliPay.ERROR_RESULT:
                        showToast(getApplication(), "支付失败:支付结果解析错误");
                        break;

                    case AliPay.ERROR_NETWORK:
                        showToast(getApplication(), "支付失败:网络连接错误");
                        break;

                    case AliPay.ERROR_PAY:
                        showToast(getApplication(), "支付错误:支付码支付失败");
                        break;

                    default:
                        showToast(getApplication(), "支付错误");
                        break;
                }

            }

            @Override
            public void onCancel() {
                showToast(getApplication(), "支付取消");
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
