package com.amkj.dmsh.shopdetails.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.bean.RequestStatus;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.UMShareAction;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.dominant.activity.QualityProductActActivity;
import com.amkj.dmsh.dominant.bean.QualityGroupShareEntity;
import com.amkj.dmsh.dominant.bean.QualityGroupShareEntity.QualityGroupShareBean;
import com.amkj.dmsh.mine.bean.CartProductInfoBean;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.qyservice.QyProductIndentInfo;
import com.amkj.dmsh.qyservice.QyServiceUtils;
import com.amkj.dmsh.shopdetails.adapter.DirectProductListAdapter;
import com.amkj.dmsh.shopdetails.adapter.IndentDiscountAdapter;
import com.amkj.dmsh.shopdetails.bean.ApplyRefundCheckEntity;
import com.amkj.dmsh.shopdetails.bean.ApplyRefundCheckEntity.ApplyRefundCheckBean;
import com.amkj.dmsh.shopdetails.bean.DirectApplyRefundBean;
import com.amkj.dmsh.shopdetails.bean.DirectApplyRefundBean.DirectRefundProBean;
import com.amkj.dmsh.shopdetails.bean.DirectAppraisePassBean;
import com.amkj.dmsh.shopdetails.bean.IndentInfoDetailEntity;
import com.amkj.dmsh.shopdetails.bean.IndentInfoDetailEntity.IndentInfoDetailBean;
import com.amkj.dmsh.shopdetails.bean.IndentInfoDetailEntity.IndentInfoDetailBean.OrderDetailBean;
import com.amkj.dmsh.shopdetails.bean.IndentInfoDetailEntity.IndentInfoDetailBean.OrderDetailBean.GoodsDetailBean;
import com.amkj.dmsh.shopdetails.bean.IndentInfoDetailEntity.IndentInfoDetailBean.OrderDetailBean.GoodsDetailBean.ActivityInfoDetailBean;
import com.amkj.dmsh.shopdetails.bean.IndentInfoDetailEntity.IndentInfoDetailBean.OrderDetailBean.GoodsDetailBean.OrderProductInfoBean;
import com.amkj.dmsh.shopdetails.bean.PriceInfoBean;
import com.amkj.dmsh.shopdetails.bean.QualityCreateAliPayIndentBean;
import com.amkj.dmsh.shopdetails.bean.QualityCreateUnionPayIndentEntity;
import com.amkj.dmsh.shopdetails.bean.QualityCreateWeChatPayIndentBean;
import com.amkj.dmsh.shopdetails.bean.RefundDetailEntity;
import com.amkj.dmsh.shopdetails.bean.RefundDetailEntity.RefundDetailBean;
import com.amkj.dmsh.shopdetails.bean.RefundDetailEntity.RefundDetailBean.RefundPayInfoBean;
import com.amkj.dmsh.shopdetails.payutils.AliPay;
import com.amkj.dmsh.shopdetails.payutils.UnionPay;
import com.amkj.dmsh.shopdetails.payutils.WXPay;
import com.amkj.dmsh.utils.CommunalCopyTextUtils;
import com.amkj.dmsh.utils.alertdialog.AlertDialogHelper;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;
import com.amkj.dmsh.views.CustomPopWindow;
import com.google.gson.Gson;
import com.klinker.android.link_builder.Link;
import com.klinker.android.link_builder.LinkBuilder;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import cn.iwgang.countdownview.CountdownView;
import cn.iwgang.countdownview.DynamicConfig;
import me.jessyan.autosize.utils.AutoSizeUtils;

import static android.view.View.GONE;
import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.isEndOrStartTimeAddSeconds;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.showToastRequestMsg;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.BUY_AGAIN;
import static com.amkj.dmsh.constant.ConstantVariable.CANCEL_ORDER;
import static com.amkj.dmsh.constant.ConstantVariable.CANCEL_PAY_ORDER;
import static com.amkj.dmsh.constant.ConstantVariable.CHECK_LOG;
import static com.amkj.dmsh.constant.ConstantVariable.CONFIRM_ORDER;
import static com.amkj.dmsh.constant.ConstantVariable.DEL;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.INDENT_PRODUCT_TYPE;
import static com.amkj.dmsh.constant.ConstantVariable.INDENT_PROPRIETOR_PRODUCT;
import static com.amkj.dmsh.constant.ConstantVariable.INDENT_PRO_STATUS;
import static com.amkj.dmsh.constant.ConstantVariable.INVITE_GROUP;
import static com.amkj.dmsh.constant.ConstantVariable.LITTER_CONSIGN;
import static com.amkj.dmsh.constant.ConstantVariable.PAY;
import static com.amkj.dmsh.constant.ConstantVariable.PAY_ALI_PAY;
import static com.amkj.dmsh.constant.ConstantVariable.PAY_UNION_PAY;
import static com.amkj.dmsh.constant.ConstantVariable.PAY_WX_PAY;
import static com.amkj.dmsh.constant.ConstantVariable.PRO_APPRAISE;
import static com.amkj.dmsh.constant.ConstantVariable.PRO_INVOICE;
import static com.amkj.dmsh.constant.ConstantVariable.REFUND_REPAIR;
import static com.amkj.dmsh.constant.ConstantVariable.REFUND_TYPE;
import static com.amkj.dmsh.constant.ConstantVariable.REGEX_NUM;
import static com.amkj.dmsh.constant.ConstantVariable.REMIND_DELIVERY;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.UNION_RESULT_CODE;
import static com.amkj.dmsh.constant.Url.GROUP_MINE_SHARE;
import static com.amkj.dmsh.constant.Url.Q_INDENT_APPLY_REFUND_CHECK;
import static com.amkj.dmsh.constant.Url.Q_INDENT_CANCEL;
import static com.amkj.dmsh.constant.Url.Q_INDENT_CONFIRM;
import static com.amkj.dmsh.constant.Url.Q_INDENT_DEL;
import static com.amkj.dmsh.constant.Url.Q_INDENT_DETAILS;
import static com.amkj.dmsh.constant.Url.Q_INDENT_DETAIL_REFUND;
import static com.amkj.dmsh.constant.Url.Q_INQUIRY_WAIT_SEND_EXPEDITING;
import static com.amkj.dmsh.constant.Url.Q_PAYMENT_INDENT;

;

/**
 * Created by atd48 on 2016/7/18.
 * 订单详情
 */
public class DirectExchangeDetailsActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.tv_indent_title)
    TextView tv_indent_title;
    @BindView(R.id.tb_indent_bar)
    Toolbar tb_indent_bar;
    @BindView(R.id.iv_indent_search)
    ImageView iv_indent_search;
    @BindView(R.id.smart_communal_refresh)
    SmartRefreshLayout smart_communal_refresh;
    @BindView(R.id.communal_recycler)
    RecyclerView communal_recycler;
    @BindView(R.id.ll_indent_detail_bottom)
    LinearLayout ll_indent_detail_bottom;
    //    底栏灰色
    @BindView(R.id.tv_indent_border_first_gray)
    TextView tv_indent_border_first_gray;
    //    底栏蓝色
    @BindView(R.id.tv_indent_border_second_blue)
    TextView tv_indent_border_second_blue;

    private LvHeaderView lvHeaderView;
    private LvFootView lvFootView;
    //    订单号
    private String orderNo;
    private OrderDetailBean orderDetailBean;
    private List<OrderProductInfoBean> goodsBeanList = new ArrayList<>();
    private List<DirectAppraisePassBean> directAppraisePassBeanList = new ArrayList<>();
    //    订单价格优惠列表
    private List<PriceInfoBean> priceInfoList = new ArrayList<>();
    private DirectProductListAdapter directProductListAdapter;
    public static final String INDENT_DETAILS_TYPE = "directDetailsGoods";
    private int statusCode;
    private String payWay = "";
    private CustomPopWindow mCustomPopWindow;
    private IndentInfoDetailEntity infoDetailEntity;
    private IndentDiscountAdapter indentDiscountAdapter;
    private boolean isOnPause;
    private ConstantMethod constantMethod;
    private AlertDialogHelper cancelOrderDialogHelper;
    private AlertDialogHelper confirmOrderDialogHelper;
    private AlertDialogHelper delOrderDialogHelper;
    private AlertDialogHelper refundOrderDialogHelper;
    private UnionPay unionPay;

    @Override
    protected int getContentView() {
        return R.layout.activity_direct_indent_detail;
    }

    @Override
    protected void initViews() {
        getLoginStatus(this);
        if (loadHud != null) {
            loadHud.setCancellable(false);
        }
        iv_indent_search.setVisibility(GONE);
        tb_indent_bar.setSelected(true);
        tv_indent_title.setText("订单详情");
        Intent intent = getIntent();
//        订单号
        orderNo = intent.getStringExtra("orderNo");
        communal_recycler.setLayoutManager(new LinearLayoutManager(DirectExchangeDetailsActivity.this));
        communal_recycler.addItemDecoration(new ItemDecoration.Builder()
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_gray_f_two_px).create());
        directProductListAdapter = new DirectProductListAdapter(DirectExchangeDetailsActivity.this, goodsBeanList, INDENT_DETAILS_TYPE);
        View headerView = LayoutInflater.from(DirectExchangeDetailsActivity.this).inflate(R.layout.layout_direct_details_top_default, (ViewGroup) communal_recycler.getParent(), false);
        lvHeaderView = new LvHeaderView();
        ButterKnife.bind(lvHeaderView, headerView);
        DynamicConfig.Builder dynamic = new DynamicConfig.Builder();
        dynamic.setSuffixTextSize(AutoSizeUtils.mm2px(mAppContext, 28));
        dynamic.setTimeTextSize(AutoSizeUtils.mm2px(mAppContext, 28));
        lvHeaderView.cv_countdownTime_direct.dynamicShow(dynamic.build());
        View footView = LayoutInflater.from(DirectExchangeDetailsActivity.this).inflate(R.layout.layout_direct_details_bottom_default, (ViewGroup) communal_recycler.getParent(), false);
        lvFootView = new LvFootView();
        ButterKnife.bind(lvFootView, footView);
        directProductListAdapter.addHeaderView(headerView);
        directProductListAdapter.addFooterView(footView);
        communal_recycler.setAdapter(directProductListAdapter);
        smart_communal_refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                //                滚动距离置0
                loadData();
                directProductListAdapter.setEnableLoadMore(false);

            }
        });
        directProductListAdapter.setOnItemClickListener((adapter, view, position) -> {
            OrderProductInfoBean orderProductInfoBean = (OrderProductInfoBean) view.getTag();
            if (orderProductInfoBean != null) {
                Intent intent1 = new Intent(DirectExchangeDetailsActivity.this, ShopScrollDetailsActivity.class);
                intent1.putExtra("productId", String.valueOf(orderProductInfoBean.getId()));
                startActivity(intent1);
            }
        });
        directProductListAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            OrderProductInfoBean orderProductInfoBean = (OrderProductInfoBean) view.getTag();
            if (orderProductInfoBean != null) {
                switch (view.getId()) {
                    case R.id.fl_dir_indent_pro_status:
                        Intent dataIndent = new Intent();
                        if (orderProductInfoBean.getStatus() == 10) { //申请退款
                            requestRefundData(orderProductInfoBean);
                        } else if (orderProductInfoBean.getStatus() == -40 ||
                                (orderProductInfoBean.getStatus() <= -30 && -32 <= orderProductInfoBean.getStatus())
                                || orderProductInfoBean.getStatus() == -35) {
//                                退款处理中 被驳回 退货申请通过
                            if (orderProductInfoBean.getOrderRefundProductId() > 0) {
                                dataIndent.setClass(DirectExchangeDetailsActivity.this, DoMoRefundDetailActivity.class);
                                dataIndent.putExtra("no", orderNo);
                                dataIndent.putExtra("orderProductId", String.valueOf(orderProductInfoBean.getOrderProductId()));
                                dataIndent.putExtra("orderRefundProductId", String.valueOf(orderProductInfoBean.getOrderRefundProductId()));
                                dataIndent.putExtra(REFUND_TYPE, REFUND_TYPE);
                                startActivity(dataIndent);
                            }
                        } else if (orderProductInfoBean.getStatus() <= 40 && 30 <= orderProductInfoBean.getStatus()) {
//                                申请售后
                            DirectApplyRefundBean refundBean = new DirectApplyRefundBean();
                            refundBean.setType(2);
                            refundBean.setOrderNo(orderNo);
                            DirectRefundProBean directRefundProBean = new DirectRefundProBean();
                            directRefundProBean.setId(orderProductInfoBean.getId());
                            directRefundProBean.setOrderProductId(orderProductInfoBean.getOrderProductId());
                            directRefundProBean.setCount(orderProductInfoBean.getCount());
                            directRefundProBean.setName(orderProductInfoBean.getName());
                            directRefundProBean.setPicUrl(orderProductInfoBean.getPicUrl());
                            directRefundProBean.setSaleSkuValue(orderProductInfoBean.getSaleSkuValue());
                            directRefundProBean.setPrice(orderProductInfoBean.getPrice());
                            if (orderProductInfoBean.getPresentProductInfoList() != null
                                    && orderProductInfoBean.getPresentProductInfoList().size() > 0) {
                                directRefundProBean.setCartProductInfoList(orderProductInfoBean.getPresentProductInfoList());
                            }
                            List<DirectRefundProBean> directProList = new ArrayList<>();
                            directProList.add(directRefundProBean);
                            refundBean.setDirectRefundProList(directProList);
                            dataIndent.setClass(DirectExchangeDetailsActivity.this, DirectApplyRefundActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putParcelable("refundPro", refundBean);
                            dataIndent.putExtras(bundle);
                            dataIndent.putExtra(REFUND_TYPE, REFUND_TYPE);
                            startActivity(dataIndent);
                        } else if (50 <= orderProductInfoBean.getStatus() && orderProductInfoBean.getStatus() <= 58) {
                            dataIndent.setClass(DirectExchangeDetailsActivity.this, DoMoRefundDetailActivity.class);
                            dataIndent.putExtra("orderProductId", String.valueOf(orderProductInfoBean.getOrderProductId()));
                            dataIndent.putExtra("no", orderNo);
                            dataIndent.putExtra("orderRefundProductId", String.valueOf(orderProductInfoBean.getOrderRefundProductId()));
                            dataIndent.putExtra(REFUND_TYPE, REFUND_REPAIR);
                            startActivity(dataIndent);
                        }
                        break;
                    case R.id.ll_communal_activity_topic_tag:
                        ActivityInfoDetailBean activityInfoBean = orderProductInfoBean.getActivityInfoDetailBean();
                        if (activityInfoBean != null && !TextUtils.isEmpty(activityInfoBean.getActivityCode())) {
                            Intent activityIntent = new Intent(DirectExchangeDetailsActivity.this, QualityProductActActivity.class);
                            activityIntent.putExtra("activityCode", getStrings(activityInfoBean.getActivityCode()));
                            startActivity(activityIntent);
                        }
                        break;
                }
            }
        });
        lvFootView.rv_indent_details.setLayoutManager(new LinearLayoutManager(DirectExchangeDetailsActivity.this));
        indentDiscountAdapter = new IndentDiscountAdapter(priceInfoList);
        lvFootView.rv_indent_details.setAdapter(indentDiscountAdapter);
    }

    private void requestRefundData(final OrderProductInfoBean orderProductInfoBean) {
        Map<String, Object> params = new HashMap<>();
        params.put("no", orderDetailBean.getNo());
        params.put("userId", orderDetailBean.getUserId());
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", orderProductInfoBean.getId());
            jsonObject.put("orderProductId", orderProductInfoBean.getOrderProductId());
            jsonObject.put("count", orderProductInfoBean.getCount());
            JSONArray jsonArray = new JSONArray();
            jsonArray.put(jsonObject);
            params.put("goods", jsonArray.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        NetLoadUtils.getNetInstance().loadNetDataPost(this, Q_INDENT_APPLY_REFUND_CHECK, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                ApplyRefundCheckEntity refundCheckEntity = gson.fromJson(result, ApplyRefundCheckEntity.class);
                if (refundCheckEntity != null) {
                    if (refundCheckEntity.getCode().equals(SUCCESS_CODE)) {
                        ApplyRefundCheckBean applyRefundCheckBean = refundCheckEntity.getApplyRefundCheckBean();
                        final DirectApplyRefundBean refundBean = new DirectApplyRefundBean();
                        refundBean.setType(1);
                        refundBean.setOrderNo(orderNo);
                        DirectRefundProBean directRefundProBean = new DirectRefundProBean();
                        directRefundProBean.setId(orderProductInfoBean.getId());
                        directRefundProBean.setOrderProductId(orderProductInfoBean.getOrderProductId());
                        directRefundProBean.setCount(orderProductInfoBean.getCount());
                        directRefundProBean.setName(orderProductInfoBean.getName());
                        directRefundProBean.setPicUrl(orderProductInfoBean.getPicUrl());
                        directRefundProBean.setSaleSkuValue(orderProductInfoBean.getSaleSkuValue());
                        directRefundProBean.setPrice(orderProductInfoBean.getPrice());
                        if (orderProductInfoBean.getCombineProductInfoList() != null
                                && orderProductInfoBean.getCombineProductInfoList().size() > 0) {
                            directRefundProBean.setCartProductInfoList(orderProductInfoBean.getCombineProductInfoList());
                        }
                        if (orderProductInfoBean.getPresentProductInfoList() != null
                                && orderProductInfoBean.getPresentProductInfoList().size() > 0) {
                            directRefundProBean.setCartProductInfoList(orderProductInfoBean.getPresentProductInfoList());
                        }
                        List<DirectRefundProBean> directProList = new ArrayList<>();
                        directProList.add(directRefundProBean);
                        refundBean.setDirectRefundProList(directProList);
                        refundBean.setRefundPrice(applyRefundCheckBean.getRefundPrice());
                        final Intent intent = new Intent();
                        switch (applyRefundCheckBean.getNoticeFlagType()) {
                            case 0:
                                intent.setClass(DirectExchangeDetailsActivity.this, DirectApplyRefundActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putParcelable("refundPro", refundBean);
                                intent.putExtras(bundle);
                                startActivity(intent);
                                break;
                            case 1:
                                showToast(DirectExchangeDetailsActivity.this, refundCheckEntity.getApplyRefundCheckBean() == null
                                        ? refundCheckEntity.getMsg() : refundCheckEntity.getApplyRefundCheckBean().getMsg()
                                );
                                break;
                            case 2:
                                if (refundOrderDialogHelper == null) {
                                    refundOrderDialogHelper = new AlertDialogHelper(DirectExchangeDetailsActivity.this);
                                    refundOrderDialogHelper.setTitleVisibility(View.GONE).setMsgTextGravity(Gravity.CENTER)
                                            .setMsg(getStrings(applyRefundCheckBean.getMsg())).setCancelText("取消").setConfirmText("确定")
                                            .setCancelTextColor(getResources().getColor(R.color.text_login_gray_s));
                                    refundOrderDialogHelper.setAlertListener(new AlertDialogHelper.AlertConfirmCancelListener() {
                                        @Override
                                        public void confirm() {
                                            intent.setClass(DirectExchangeDetailsActivity.this, DirectApplyRefundActivity.class);
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
                        showToast(DirectExchangeDetailsActivity.this, refundCheckEntity.getApplyRefundCheckBean() == null
                                ? refundCheckEntity.getMsg() : refundCheckEntity.getApplyRefundCheckBean().getMsg()
                        );
                    }
                }
            }

            @Override
            public void onError(Throwable throwable) {
                showToast(DirectExchangeDetailsActivity.this, R.string.do_failed);
            }

            @Override
            public void netClose() {
                showToast(DirectExchangeDetailsActivity.this, R.string.unConnectedNetwork);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            if (requestCode == UNION_RESULT_CODE) {
                return;
            } else {
                finish();
                return;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UNION_RESULT_CODE) {
            if (unionPay != null) {
                String webManualFinish = data.getStringExtra("webManualFinish");
                unionPay.unionPayResult(this, orderNo, webManualFinish);
            } else {
                showToast("支付取消！");
            }
        }

    }

    @Override
    protected void loadData() {
        ll_indent_detail_bottom.setVisibility(GONE);
        Map<String, Object> params = new HashMap<>();
        params.put("no", orderNo);
        //        版本号控制 3 组合商品赠品
        params.put("version", 3);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, Q_INDENT_DETAILS
                , params, new NetLoadListenerHelper() {
                    @Override
                    public void onSuccess(String result) {
                        smart_communal_refresh.finishRefresh();
                        directProductListAdapter.loadMoreComplete();
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
                            infoDetailEntity = gson.fromJson(result, IndentInfoDetailEntity.class);
                            if (infoDetailEntity != null) {
                                INDENT_PRO_STATUS = infoDetailEntity.getIndentInfoDetailBean().getStatus();
                                setIndentData(infoDetailEntity);
                            }
                        } else if (code.equals(EMPTY_CODE)) {
                            ll_indent_detail_bottom.setVisibility(GONE);
                            showToast(DirectExchangeDetailsActivity.this, R.string.invalidData);
                        } else {
                            ll_indent_detail_bottom.setVisibility(GONE);
                            showToast(DirectExchangeDetailsActivity.this, msg);
                        }
                        communal_recycler.smoothScrollToPosition(0);
                        NetLoadUtils.getNetInstance().showLoadSir(loadService, code);
                    }

                    @Override
                    public void onNotNetOrException() {
                        smart_communal_refresh.finishRefresh();
                        directProductListAdapter.loadMoreComplete();
                        ll_indent_detail_bottom.setVisibility(GONE);
                        NetLoadUtils.getNetInstance().showLoadSir(loadService, infoDetailEntity);
                    }

                    @Override
                    public void netClose() {
                        showToast(DirectExchangeDetailsActivity.this, R.string.unConnectedNetwork);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        showToast(DirectExchangeDetailsActivity.this, R.string.invalidData);
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

    private void setIndentData(IndentInfoDetailEntity infoDetailEntity) {
//        订单信息
        IndentInfoDetailBean indentInfoDetailBean = infoDetailEntity.getIndentInfoDetailBean();
        orderDetailBean = indentInfoDetailBean.getOrderDetailBean();
        statusCode = orderDetailBean.getStatus();
        if (statusCode == -24) {
            getRefundPrice();
        }
        goodsBeanList.clear();
        for (int i = 0; i < orderDetailBean.getGoodDetails().size(); i++) {
            GoodsDetailBean goodsDetailBean = orderDetailBean.getGoodDetails().get(i);
            for (int j = 0; j < goodsDetailBean.getOrderProductInfoList().size(); j++) {
                OrderProductInfoBean orderProductInfoBean = goodsDetailBean.getOrderProductInfoList().get(j);
                if (j == 0 && goodsDetailBean.getActivityInfo() != null) {
                    orderProductInfoBean.setActivityInfoDetailBean(goodsDetailBean.getActivityInfo());
                }
                if (orderDetailBean.getStatus() == 15 || orderDetailBean.getStatus() == 14) {
                    orderProductInfoBean.setStatus(15);
                }
                /**
                 *强制把商品状态改为拼团失败
                 */
                if (orderDetailBean.getStatus() == -26) {
                    orderProductInfoBean.setStatus(-26);
                }
                goodsBeanList.add(orderProductInfoBean);
            }
            if (goodsDetailBean.getActivityInfo() != null && goodsBeanList.size() > 0
                    && orderDetailBean.getGoodDetails().size() > i + 1) {
                OrderProductInfoBean orderProductInfoBean = goodsBeanList.get(goodsBeanList.size() - 1);
                orderProductInfoBean.setShowLine(1);
                goodsBeanList.set(goodsBeanList.size() - 1, orderProductInfoBean);
            }
        }
        getConstant();
        setIntentStatus(indentInfoDetailBean);
        if (!TextUtils.isEmpty(infoDetailEntity.getCurrentTime()) && goodsBeanList.size() > 0) {
            final OrderProductInfoBean orderProductInfoBean = goodsBeanList.get(0);
            orderProductInfoBean.setCurrentTime(infoDetailEntity.getCurrentTime());
            orderProductInfoBean.setSecond(infoDetailEntity.getSecond());
            orderProductInfoBean.setOrderCreateTime(infoDetailEntity.getIndentInfoDetailBean().getOrderDetailBean().getCreateTime());
            goodsBeanList.set(0, orderProductInfoBean);
            setTimeDown(orderProductInfoBean);
//            创建时间加倒计时间 大于等于当前时间 展示倒计时
            if (isEndOrStartTimeAddSeconds(orderProductInfoBean.getOrderCreateTime()
                    , orderProductInfoBean.getCurrentTime()
                    , orderProductInfoBean.getSecond())) {
                constantMethod.createSchedule();
                constantMethod.setRefreshTimeListener(() ->
                        setTimeDown(orderProductInfoBean)
                );
            } else {
                if (constantMethod != null) {
                    constantMethod.stopSchedule();
                }
                lvHeaderView.ll_direct_count_time.setVisibility(View.GONE);
            }
        } else {
            if (constantMethod != null) {
                constantMethod.stopSchedule();
            }
            lvHeaderView.ll_direct_count_time.setVisibility(View.GONE);
        }
/**
 * 头部
 */
        lvHeaderView.img_skip_address.setVisibility(GONE);
        lvHeaderView.tv_indent_detail_status.setText(getStrings(INDENT_PRO_STATUS.get(String.valueOf(statusCode))));
//        收件人名字
        lvHeaderView.tv_consignee_name.setText(orderDetailBean.getConsignee());
//        收件人手机号码
        lvHeaderView.tv_address_mobile_number.setText(orderDetailBean.getMobile());
//        订单地址
        lvHeaderView.tv_indent_details_address.setText(orderDetailBean.getAddress());
        if (!TextUtils.isEmpty(orderDetailBean.getRemark())) {
            lvHeaderView.rel_indent_lea_mes.setVisibility(View.VISIBLE);
            lvHeaderView.tv_indent_user_lea_mes.setText(getStrings(orderDetailBean.getRemark()));
        } else {
            lvHeaderView.rel_indent_lea_mes.setVisibility(GONE);
        }
/**
 * 尾部
 */
//        订单编号
        lvFootView.tv_indent_order_no.setTag(orderDetailBean.getNo());
        lvFootView.tv_indent_order_no.setText("订单编号:" + orderDetailBean.getNo());
//        订单时间
        lvFootView.tv_indent_order_time.setText("创建时间:" + orderDetailBean.getCreateTime());
//        支付方式
        if (orderDetailBean.getPayType() == null) {
            lvFootView.tv_indent_pay_way.setVisibility(GONE);
        } else {
            lvFootView.tv_indent_pay_way.setVisibility(View.VISIBLE);
            lvFootView.tv_indent_pay_way.setText("支付方式:" + orderDetailBean.getPayType());
        }
//        优惠力度
        if (orderDetailBean.getPriceInfoList() != null && orderDetailBean.getPriceInfoList().size() > 0) {
            priceInfoList.clear();
            priceInfoList.addAll(orderDetailBean.getPriceInfoList());
            indentDiscountAdapter.setNewData(priceInfoList);
        }
        directProductListAdapter.notifyDataSetChanged();
    }

    /**
     * 整单退款金额去向
     */
    private void getRefundPrice() {
        Map<String, Object> params = new HashMap<>();
        params.put("no", orderNo);
        params.put("userId", userId);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, Q_INDENT_DETAIL_REFUND, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
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
                    RefundDetailEntity refundDetailEntity = gson.fromJson(result, RefundDetailEntity.class);
                    if (refundDetailEntity != null) {
                        RefundDetailBean refundDetailBean = refundDetailEntity.getRefundDetailBean();
                        if (refundDetailBean.getRefundPayInfo() != null
                                && !TextUtils.isEmpty(refundDetailBean.getRefundPayInfo().getRefundAccount())) {
                            setRefundToPrice(View.VISIBLE);
                            RefundPayInfoBean refundPayInfo = refundDetailBean.getRefundPayInfo();
                            lvHeaderView.tv_indent_detail_to_refund.setText(getResources().getString(R.string.refund_account_time_price,
                                    getStrings(refundPayInfo.getRefundAccount()),
                                    getStrings(refundPayInfo.getReceiveRefundTime())));
                        } else {
                            setRefundToPrice(View.GONE);
                        }
                    } else {
                        setRefundToPrice(View.GONE);
                    }
                } else {
                    setRefundToPrice(View.GONE);
                }
            }

            @Override
            public void onNotNetOrException() {
                setRefundToPrice(View.GONE);
            }
        });
    }

    private void setRefundToPrice(int viewType) {
        lvHeaderView.tv_indent_detail_to_refund.setVisibility(viewType);
    }

    /**
     * 根据主订单状态展示布局
     *
     * @param indentInfoDetailBean
     */
    private void setIntentStatus(IndentInfoDetailBean indentInfoDetailBean) {
        OrderDetailBean orderDetailBean = indentInfoDetailBean.getOrderDetailBean();
        int statusCode = orderDetailBean.getStatus();
        List<GoodsDetailBean> goodDetails = indentInfoDetailBean.getOrderDetailBean().getGoodDetails();
        if (-20 <= statusCode && statusCode < -10) {
//            待支付
            ll_indent_detail_bottom.setVisibility(View.VISIBLE);
            tv_indent_border_first_gray.setVisibility(View.VISIBLE);
            tv_indent_border_second_blue.setVisibility(View.VISIBLE);
            tv_indent_border_first_gray.setText("删除订单");
            tv_indent_border_first_gray.setTag(R.id.tag_first, DEL);
            tv_indent_border_first_gray.setTag(R.id.tag_second, indentInfoDetailBean);
            tv_indent_border_first_gray.setOnClickListener(this);
            //            再次购买
            tv_indent_border_second_blue.setText("再次购买");
            tv_indent_border_second_blue.setTag(R.id.tag_first, BUY_AGAIN);
            tv_indent_border_second_blue.setTag(R.id.tag_second, indentInfoDetailBean);
            tv_indent_border_second_blue.setOnClickListener(this);
        } else if (statusCode == 14) {
            ll_indent_detail_bottom.setVisibility(View.VISIBLE);
            tv_indent_border_second_blue.setVisibility(View.VISIBLE);
            tv_indent_border_first_gray.setVisibility(View.GONE);
            tv_indent_border_second_blue.setText("邀请参团");
            tv_indent_border_second_blue.setTag(R.id.tag_first, INVITE_GROUP);
            tv_indent_border_second_blue.setTag(R.id.tag_second, indentInfoDetailBean);
            tv_indent_border_second_blue.setOnClickListener(this);
        } else if (10 <= statusCode && statusCode < 20) {
            if (10 == statusCode) {
                //            取消订单 待发货
                boolean isRefund = true;
                for (int i = 0; i < goodDetails.size(); i++) {
                    List<OrderProductInfoBean> orderProductInfoList = goodDetails.get(i).getOrderProductInfoList();
                    for (int j = 0; j < orderProductInfoList.size(); j++) {
                        int status = orderProductInfoList.get(j).getStatus();
                        if (status != 10) {
                            isRefund = false;
                            break;
                        }
                    }
                }
                if (isRefund) {
//            取消订单
                    ll_indent_detail_bottom.setVisibility(View.VISIBLE);
                    tv_indent_border_second_blue.setVisibility(GONE);
                    tv_indent_border_first_gray.setVisibility(View.VISIBLE);
                    tv_indent_border_first_gray.setText("取消订单");
                    tv_indent_border_first_gray.setTag(R.id.tag_first, CANCEL_PAY_ORDER);
                    tv_indent_border_first_gray.setTag(R.id.tag_second, indentInfoDetailBean);
                    tv_indent_border_first_gray.setOnClickListener(this);
                } else {
                    //            不可取消订单
                    ll_indent_detail_bottom.setVisibility(View.GONE);
                    tv_indent_border_second_blue.setVisibility(GONE);
                    tv_indent_border_first_gray.setVisibility(View.GONE);
                }

            } else if (12 == statusCode) {
                ll_indent_detail_bottom.setVisibility(View.VISIBLE);
                tv_indent_border_second_blue.setVisibility(View.GONE);
                tv_indent_border_first_gray.setVisibility(View.VISIBLE);
                tv_indent_border_first_gray.setText("查看物流");
                tv_indent_border_first_gray.setTag(R.id.tag_first, LITTER_CONSIGN);
                tv_indent_border_first_gray.setTag(R.id.tag_second, indentInfoDetailBean);
                tv_indent_border_first_gray.setOnClickListener(this);
            } else {
//            不可取消订单
                ll_indent_detail_bottom.setVisibility(View.GONE);
                tv_indent_border_second_blue.setVisibility(View.GONE);
                tv_indent_border_first_gray.setVisibility(View.GONE);
            }
            if (orderDetailBean.isWaitDeliveryFlag()) {
                ll_indent_detail_bottom.setVisibility(View.VISIBLE);
                tv_indent_border_second_blue.setVisibility(View.VISIBLE);
                tv_indent_border_second_blue.setText("提醒发货");
                tv_indent_border_second_blue.setTag(R.id.tag_first, REMIND_DELIVERY);
                tv_indent_border_second_blue.setTag(R.id.tag_second, indentInfoDetailBean);
                tv_indent_border_second_blue.setOnClickListener(this);
            }
        } else if (statusCode == -25 || statusCode == -26 || statusCode == -24) {//交易关闭 拼团失败 -》删除订单
            ll_indent_detail_bottom.setVisibility(View.VISIBLE);
            tv_indent_border_second_blue.setVisibility(GONE);
            tv_indent_border_first_gray.setVisibility(View.VISIBLE);
            tv_indent_border_first_gray.setText("删除订单");
            tv_indent_border_first_gray.setTag(R.id.tag_first, DEL);
            tv_indent_border_first_gray.setTag(R.id.tag_second, indentInfoDetailBean);
            tv_indent_border_first_gray.setOnClickListener(this);
        } else if (0 <= statusCode && statusCode < 10) {
//            展示倒计时
            if (!TextUtils.isEmpty(infoDetailEntity.getCurrentTime())) {
                lvHeaderView.rel_indent_time_count.setVisibility(View.VISIBLE);
                lvHeaderView.ll_direct_count_time.setVisibility(View.VISIBLE);
            } else {
                lvHeaderView.rel_indent_time_count.setVisibility(GONE);
            }
//          头部状态栏
            lvFootView.tv_indent_pay_way.setVisibility(GONE);
//          底栏 件数
            ll_indent_detail_bottom.setVisibility(View.VISIBLE);
            tv_indent_border_first_gray.setVisibility(View.VISIBLE);
            tv_indent_border_second_blue.setVisibility(View.VISIBLE);
            tv_indent_border_second_blue.setText("立即付款");
            tv_indent_border_first_gray.setText("取消订单");
            tv_indent_border_first_gray.setTag(R.id.tag_first, CANCEL_ORDER);
            tv_indent_border_first_gray.setTag(R.id.tag_second, indentInfoDetailBean);
            tv_indent_border_first_gray.setOnClickListener(this);
            tv_indent_border_second_blue.setTag(R.id.tag_first, PAY);
            tv_indent_border_second_blue.setTag(R.id.tag_second, indentInfoDetailBean);
            tv_indent_border_second_blue.setOnClickListener(this);
        } else if (20 <= statusCode && statusCode < 30) {
            ll_indent_detail_bottom.setVisibility(View.VISIBLE);
            tv_indent_border_first_gray.setVisibility(View.VISIBLE);
            tv_indent_border_first_gray.setText("查看物流");
            tv_indent_border_first_gray.setTag(R.id.tag_first, CHECK_LOG);
            tv_indent_border_first_gray.setTag(R.id.tag_second, indentInfoDetailBean);
            tv_indent_border_first_gray.setOnClickListener(this);
//          确认订单
            tv_indent_border_second_blue.setVisibility(View.VISIBLE);
            tv_indent_border_second_blue.setText("确认收货");
            tv_indent_border_second_blue.setTag(R.id.tag_first, CONFIRM_ORDER);
            tv_indent_border_second_blue.setTag(R.id.tag_second, indentInfoDetailBean);
            tv_indent_border_second_blue.setOnClickListener(this);
        } else if (30 <= statusCode && statusCode <= 40) {
            ll_indent_detail_bottom.setVisibility(View.VISIBLE);
            if (statusCode == 40) { //已评价状态
                tv_indent_border_first_gray.setVisibility(View.VISIBLE);
                tv_indent_border_first_gray.setText("发票详情");
//            发票详情
                tv_indent_border_first_gray.setTag(R.id.tag_first, PRO_INVOICE);
                tv_indent_border_first_gray.setTag(R.id.tag_second, indentInfoDetailBean);
                tv_indent_border_first_gray.setOnClickListener(this);
                tv_indent_border_second_blue.setVisibility(View.VISIBLE);
                tv_indent_border_second_blue.setText("删除订单");
                tv_indent_border_second_blue.setTag(R.id.tag_first, DEL);
                tv_indent_border_second_blue.setTag(R.id.tag_second, indentInfoDetailBean);
                tv_indent_border_second_blue.setOnClickListener(this);
            } else {
                boolean isShowEvaluate = false;
                reach:
                for (GoodsDetailBean goodsDetailBean : goodDetails) {
                    for (OrderProductInfoBean orderProductInfoBean : goodsDetailBean.getOrderProductInfoList()) {
                        if (orderProductInfoBean.getStatus() == 30) {
                            isShowEvaluate = true;
                            break reach;
                        }
                    }
                }
                if (isShowEvaluate) {
                    if (!TextUtils.isEmpty(orderDetailBean.getTotalScore())) {
                        String scoreTotal = String.format(getResources().getString(R.string.appraise_integral_score)
                                , orderDetailBean.getTotalScore());
                        tv_indent_border_second_blue.setText(scoreTotal);
                        Pattern p = Pattern.compile(REGEX_NUM);
                        Link redNum = new Link(p);
                        //        @用户昵称
                        redNum.setTextColor(Color.parseColor("#ff5e5e"));
                        redNum.setUnderlined(false);
                        redNum.setHighlightAlpha(0f);
                        LinkBuilder.on(tv_indent_border_second_blue)
                                .setText(scoreTotal)
                                .addLink(redNum)
                                .build();
                    } else {
                        tv_indent_border_second_blue.setText(R.string.appraise_integral);
                    }
                    tv_indent_border_second_blue.setVisibility(View.VISIBLE);
                    tv_indent_border_second_blue.setTag(R.id.tag_first, PRO_APPRAISE);
                    tv_indent_border_second_blue.setTag(R.id.tag_second, indentInfoDetailBean);
                    tv_indent_border_second_blue.setOnClickListener(this);
                } else {
                    tv_indent_border_second_blue.setVisibility(View.GONE);
                }
                tv_indent_border_first_gray.setVisibility(View.VISIBLE);
                tv_indent_border_first_gray.setText("发票详情");
//            发票详情
                tv_indent_border_first_gray.setTag(R.id.tag_first, PRO_INVOICE);
                tv_indent_border_first_gray.setTag(R.id.tag_second, indentInfoDetailBean);
                tv_indent_border_first_gray.setOnClickListener(this);
            }
        } else {
            ll_indent_detail_bottom.setVisibility(GONE);
        }
    }

    //  订单底栏点击时间
    @Override
    public void onClick(View v) {
        String type = (String) v.getTag(R.id.tag_first);
        IndentInfoDetailBean indentInfoDetailBean = (IndentInfoDetailBean) v.getTag(R.id.tag_second);
        OrderDetailBean orderDetailBean = indentInfoDetailBean.getOrderDetailBean();
        Intent intent = new Intent();
        Bundle bundle;
        switch (type) {
            case BUY_AGAIN:
//                        再次购买
                intent.setClass(DirectExchangeDetailsActivity.this, DirectIndentWriteActivity.class);
                intent.putExtra("orderNo", orderDetailBean.getNo());
                startActivity(intent);
                break;
            case CANCEL_ORDER:
//                        取消订单
                if (cancelOrderDialogHelper == null) {
                    cancelOrderDialogHelper = new AlertDialogHelper(DirectExchangeDetailsActivity.this);
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
                setRemindDelivery(orderDetailBean);
                break;
            //    取消订单 待发货
            case CANCEL_PAY_ORDER:
                DirectApplyRefundBean refundBean = new DirectApplyRefundBean();
                refundBean.setType(3);
                refundBean.setOrderNo(orderNo);
                List<DirectRefundProBean> directProList = new ArrayList<>();
                List<CartProductInfoBean> cartProductInfoList;
                DirectRefundProBean directRefundProBean;
                for (int i = 0; i < orderDetailBean.getGoodDetails().size(); i++) {
                    GoodsDetailBean goodsDetailBean = orderDetailBean.getGoodDetails().get(i);
                    cartProductInfoList = new ArrayList<>();
                    for (int j = 0; j < goodsDetailBean.getOrderProductInfoList().size(); j++) {
                        OrderProductInfoBean orderProductInfoBean = goodsDetailBean.getOrderProductInfoList().get(j);
                        directRefundProBean = new DirectRefundProBean();
                        directRefundProBean.setId(orderProductInfoBean.getId());
                        directRefundProBean.setOrderProductId(orderProductInfoBean.getOrderProductId());
                        directRefundProBean.setCount(orderProductInfoBean.getCount());
                        directRefundProBean.setName(orderProductInfoBean.getName());
                        directRefundProBean.setPicUrl(orderProductInfoBean.getPicUrl());
                        directRefundProBean.setSaleSkuValue(orderProductInfoBean.getSaleSkuValue());
                        directRefundProBean.setPrice(orderProductInfoBean.getPrice());
                        if (orderProductInfoBean.getPresentProductInfoList() != null && orderProductInfoBean.getPresentProductInfoList().size() > 0) {
                            cartProductInfoList.addAll(orderProductInfoBean.getPresentProductInfoList());
                        }
                        if (orderProductInfoBean.getCombineProductInfoList() != null && orderProductInfoBean.getCombineProductInfoList().size() > 0) {
                            cartProductInfoList.addAll(orderProductInfoBean.getCombineProductInfoList());
                        }
                        if (cartProductInfoList.size() > 0) {
                            directRefundProBean.setCartProductInfoList(cartProductInfoList);
                        }
                        directProList.add(directRefundProBean);
                    }
                }
                refundBean.setDirectRefundProList(directProList);
                intent.setClass(DirectExchangeDetailsActivity.this, DirectApplyRefundActivity.class);
                intent.putExtra(REFUND_TYPE, REFUND_TYPE);
                bundle = new Bundle();
                bundle.putParcelable("refundPro", refundBean);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case PAY://支付
                View indentPopWindow = getLayoutInflater().inflate(R.layout.layout_indent_pay_pop, null, false);
                PopupWindowView popupWindowView = new PopupWindowView();
                ButterKnife.bind(popupWindowView, indentPopWindow);
                mCustomPopWindow = new CustomPopWindow.PopupWindowBuilder(DirectExchangeDetailsActivity.this)
                        .setView(indentPopWindow)
                        .enableBackgroundDark(true) //弹出popWindow时，背景是否变暗
                        .setBgDarkAlpha(0.7f) // 控制亮度
                        .create().showAtLocation((View) communal_recycler.getParent(), Gravity.CENTER, 0, 0);
//                        .showAsDropDown(tv_indent_red_second, 0, 20);
                break;
            case LITTER_CONSIGN:
            case CHECK_LOG:
//                        查看物流
                intent.setClass(DirectExchangeDetailsActivity.this, DirectLogisticsDetailsActivity.class);
                intent.putExtra("orderNo", orderDetailBean.getNo());
                startActivity(intent);
                break;
            case CONFIRM_ORDER:
//                        确认收货
                if (confirmOrderDialogHelper == null) {
                    confirmOrderDialogHelper = new AlertDialogHelper(DirectExchangeDetailsActivity.this);
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
                //评价
                DirectAppraisePassBean directAppraisePassBean;
                directAppraisePassBeanList.clear();
                for (int i = 0; i < orderDetailBean.getGoodDetails().size(); i++) {
                    GoodsDetailBean goodsDetailBean = orderDetailBean.getGoodDetails().get(i);
                    for (int j = 0; j < goodsDetailBean.getOrderProductInfoList().size(); j++) {
                        OrderProductInfoBean orderProductInfoBean = goodsDetailBean.getOrderProductInfoList().get(j);
                        if (orderProductInfoBean.getCombineProductInfoList() != null
                                && orderProductInfoBean.getCombineProductInfoList().size() > 0) {
                            for (int k = 0; k < orderProductInfoBean.getCombineProductInfoList().size(); k++) {
                                CartProductInfoBean cartProductInfoBean = orderProductInfoBean.getCombineProductInfoList().get(k);
                                if (cartProductInfoBean.getStatus() == 30) {
                                    directAppraisePassBean = new DirectAppraisePassBean();
                                    directAppraisePassBean.setProductId(cartProductInfoBean.getId());
                                    directAppraisePassBean.setOrderProductId(cartProductInfoBean.getOrderProductId());
                                    directAppraisePassBean.setPath(cartProductInfoBean.getPicUrl());
                                    directAppraisePassBean.setStar(5);
                                    directAppraisePassBeanList.add(directAppraisePassBean);
                                }
                            }
                        } else if (orderProductInfoBean.getStatus() == 30) {
                            directAppraisePassBean = new DirectAppraisePassBean();
                            directAppraisePassBean.setProductId(orderProductInfoBean.getId());
                            directAppraisePassBean.setOrderProductId(orderProductInfoBean.getOrderProductId());
                            directAppraisePassBean.setPath(orderProductInfoBean.getPicUrl());
                            directAppraisePassBean.setStar(5);
                            directAppraisePassBeanList.add(directAppraisePassBean);
                        }
                    }
                }
//                待评价
                if (directAppraisePassBeanList.size() > 0) {
                    intent.setClass(DirectExchangeDetailsActivity.this, DirectPublishAppraiseActivity.class);
                    bundle = new Bundle();
                    bundle.putParcelableArrayList("appraiseData", (ArrayList<? extends Parcelable>) directAppraisePassBeanList);
                    intent.putExtras(bundle);
                    intent.putExtra("uid", this.orderDetailBean.getUserId());
                    intent.putExtra("orderNo", this.orderDetailBean.getNo());
                    startActivity(intent);
                }
                break;
            case DEL:
//                        删除订单
                if (delOrderDialogHelper == null) {
                    delOrderDialogHelper = new AlertDialogHelper(DirectExchangeDetailsActivity.this);
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
            case PRO_INVOICE:
//                        发票详情
                intent.setClass(DirectExchangeDetailsActivity.this, DirectIndentInvoiceActivity.class);
                bundle = new Bundle();
                bundle.putParcelable("indentInfo", orderDetailBean);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case INVITE_GROUP:
                getInviteGroupInfo(orderDetailBean.getNo());
                break;
        }
    }

    private void getInviteGroupInfo(String no) {
        if (loadHud != null) {
            loadHud.show();
        }
        Map<String, Object> params = new HashMap<>();
        params.put("orderNo", no);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, GROUP_MINE_SHARE, params, new NetLoadListenerHelper() {
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
                        invitePartnerGroup(qualityGroupShareBean);
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
                showToast(DirectExchangeDetailsActivity.this, R.string.do_failed);
            }

            @Override
            public void netClose() {
                showToast(DirectExchangeDetailsActivity.this, R.string.unConnectedNetwork);
            }
        });
    }

    /**
     * 邀请参团
     *
     * @param qualityGroupShareBean 参团信息
     */
    private void invitePartnerGroup(@NonNull QualityGroupShareBean qualityGroupShareBean) {
        new UMShareAction(DirectExchangeDetailsActivity.this
                , qualityGroupShareBean.getGpPicUrl()
                , qualityGroupShareBean.getName()
                , getStrings(qualityGroupShareBean.getSubtitle())
                , Url.BASE_SHARE_PAGE_TWO + "m/template/share_template/groupShare.html?id=" + qualityGroupShareBean.getGpInfoId()
                + "&record=" + qualityGroupShareBean.getGpRecordId(), "pages/groupshare/groupshare?id=" + qualityGroupShareBean.getGpInfoId()
                + (TextUtils.isEmpty(orderNo) ? "&gpRecordId=" + qualityGroupShareBean.getGpRecordId() : "&order=" + orderNo), qualityGroupShareBean.getGpInfoId());
    }

    @Override
    protected void postEventResult(@NonNull EventMessage message) {
        if (message.type.equals("indentDetailsRefresh")) {
            lvHeaderView.rel_indent_time_count.setVisibility(GONE);
            loadData();
        }
    }

    class LvHeaderView {
        //        订单状态
        @BindView(R.id.tv_indent_detail_status)
        TextView tv_indent_detail_status;
        //        整单退款去向
        @BindView(R.id.tv_indent_detail_to_refund)
        TextView tv_indent_detail_to_refund;
        //    收件人名字
        @BindView(R.id.tv_consignee_name)
        TextView tv_consignee_name;
        //    收件人手机号码
        @BindView(R.id.tv_consignee_mobile_number)
        TextView tv_address_mobile_number;
        //    订单地址
        @BindView(R.id.tv_indent_details_address)
        TextView tv_indent_details_address;
        //        选择按钮
        @BindView(R.id.img_skip_address)
        ImageView img_skip_address;
        //        倒计时布局
        @BindView(R.id.rel_indent_time_count)
        RelativeLayout rel_indent_time_count;
        @BindView(R.id.ll_direct_count_time)
        LinearLayout ll_direct_count_time;
        //        留言布局
        @BindView(R.id.rel_indent_lea_mes)
        RelativeLayout rel_indent_lea_mes;
        //        具体留言
        @BindView(R.id.tv_indent_user_lea_mes)
        TextView tv_indent_user_lea_mes;
        @BindView(R.id.cv_countdownTime_direct)
        CountdownView cv_countdownTime_direct;
    }

    class LvFootView {
        //        订单件数
        @BindView(R.id.rv_indent_details)
        RecyclerView rv_indent_details;
        //        订单编号
        @BindView(R.id.tv_indent_order_no)
        TextView tv_indent_order_no;
        //        订单时间
        @BindView(R.id.tv_indent_order_time)
        TextView tv_indent_order_time;
        //        支付方式
        @BindView(R.id.tv_indent_pay_way)
        TextView tv_indent_pay_way;

        @OnLongClick(R.id.tv_indent_order_no)
        public boolean copyIndentNo(View view) {
            CommunalCopyTextUtils.showPopWindow(DirectExchangeDetailsActivity.this, (TextView) view, (String) view.getTag());
            return true;
        }

        @OnClick(R.id.tv_copy_text)
        void copyNo(View view) {
            String content = (String) tv_indent_order_no.getTag();
            ClipboardManager cmb = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData mClipData = ClipData.newPlainText("Label", getStrings(content));
            cmb.setPrimaryClip(mClipData);
            showToast(DirectExchangeDetailsActivity.this, "已复制");
        }
    }

    private void paymentIndent() {
        Map<String, Object> params = new HashMap<>();
        params.put("no", orderDetailBean.getNo());
        params.put("userId", orderDetailBean.getUserId());
        //        2019.1.16 新增银联支付
        params.put("buyType", payWay);
        if (payWay.equals(PAY_UNION_PAY)) {
            params.put("paymentLinkType", 2);
            params.put("isApp", true);
        }
        NetLoadUtils.getNetInstance().loadNetDataPost(this, Q_PAYMENT_INDENT, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                if (payWay.equals(PAY_WX_PAY)) {
                    QualityCreateWeChatPayIndentBean qualityWeChatIndent = gson.fromJson(result, QualityCreateWeChatPayIndentBean.class);
                    if (qualityWeChatIndent != null) {
                        if (qualityWeChatIndent.getCode().equals(SUCCESS_CODE)) {
                            //返回成功，调起微信支付接口
                            doWXPay(qualityWeChatIndent.getResult().getPayKey());
                        } else {
                            showToast(DirectExchangeDetailsActivity.this, qualityWeChatIndent.getResult() == null
                                    ? qualityWeChatIndent.getMsg() : qualityWeChatIndent.getResult().getMsg());
                        }
                    }
                } else if (payWay.equals(PAY_ALI_PAY)) {
                    QualityCreateAliPayIndentBean qualityAliPayIndent = gson.fromJson(result, QualityCreateAliPayIndentBean.class);
                    if (qualityAliPayIndent != null) {
                        if (qualityAliPayIndent.getCode().equals(SUCCESS_CODE)) {
                            //返回成功，调起支付宝支付接口
                            doAliPay(qualityAliPayIndent.getResult().getPayKey());
                        } else {
                            showToast(DirectExchangeDetailsActivity.this, qualityAliPayIndent.getResult() == null
                                    ? qualityAliPayIndent.getMsg() : qualityAliPayIndent.getResult().getMsg());
                        }
                    }
                } else if (payWay.equals(PAY_UNION_PAY)) {
                    QualityCreateUnionPayIndentEntity qualityUnionIndent = gson.fromJson(result, QualityCreateUnionPayIndentEntity.class);
                    if (qualityUnionIndent != null) {
                        if (qualityUnionIndent.getCode().equals(SUCCESS_CODE)) {
                            //返回成功，调起银联支付接口
                            unionPay(qualityUnionIndent);
                        } else {
                            showToast(DirectExchangeDetailsActivity.this, qualityUnionIndent.getQualityCreateUnionPayIndent() != null &&
                                    qualityUnionIndent.getQualityCreateUnionPayIndent() != null &&
                                    !TextUtils.isEmpty(qualityUnionIndent.getQualityCreateUnionPayIndent().getMsg()) ?
                                    getStrings(qualityUnionIndent.getQualityCreateUnionPayIndent().getMsg()) :
                                    getStrings(qualityUnionIndent.getMsg()));
                        }
                    }
                }
            }

            @Override
            public void onError(Throwable throwable) {
                showToast(DirectExchangeDetailsActivity.this, R.string.do_failed);
            }

            @Override
            public void netClose() {
                showToast(DirectExchangeDetailsActivity.this, R.string.unConnectedNetwork);
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
                skipDirectIndent();
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

    private void skipDirectIndent() {
        new Handler().postDelayed(() -> {
            if (totalPersonalTrajectory != null) {
                totalPersonalTrajectory.getFileTotalTrajectory();
            }
            Intent intent = new Intent(DirectExchangeDetailsActivity.this, DirectPaySuccessActivity.class);
            intent.putExtra("indentNo", orderNo);
            intent.putExtra(INDENT_PRODUCT_TYPE, INDENT_PROPRIETOR_PRODUCT);
            startActivity(intent);
        }, 1000);
    }

    private void doAliPay(String pay_param) {
        new AliPay(this, pay_param, new AliPay.AliPayResultCallBack() {
            @Override
            public void onSuccess() {
                showToast(getApplication(), "支付成功");
//                recordPaySuc();
                skipDirectIndent();
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

    /**
     * 银联支付
     *
     * @param qualityUnionIndent
     */
    private void unionPay(@NonNull QualityCreateUnionPayIndentEntity qualityUnionIndent) {
        if (qualityUnionIndent.getQualityCreateUnionPayIndent().getPayKeyBean() != null &&
                !TextUtils.isEmpty(qualityUnionIndent.getQualityCreateUnionPayIndent().getPayKeyBean().getPaymentUrl())) {
            if (loadHud != null) {
                loadHud.show();
            }
            unionPay = new UnionPay(DirectExchangeDetailsActivity.this,
                    qualityUnionIndent.getQualityCreateUnionPayIndent().getPayKeyBean().getPaymentUrl(),
                    new UnionPay.UnionPayResultCallBack() {
                        @Override
                        public void onUnionPaySuccess(String webResultValue) {
                            if (loadHud != null) {
                                loadHud.dismiss();
                            }
                            showToast(DirectExchangeDetailsActivity.this, "支付成功");
                            if (!TextUtils.isEmpty(webResultValue) && "1".equals(webResultValue)) {
                                if (totalPersonalTrajectory != null) {
                                    totalPersonalTrajectory.getFileTotalTrajectory();
                                }
                                loadData();
                            } else {
                                skipDirectIndent();
                            }
                        }

                        @Override
                        public void onUnionPayError(String errorMes) {
                            if (loadHud != null) {
                                loadHud.dismiss();
                            }
                            showToast(getApplication(), "支付取消");
                        }
                    });
        } else {
            showToast(DirectExchangeDetailsActivity.this, "缺少重要参数，请选择其它支付渠道！");
        }
    }

    private void confirmOrder() {
        Map<String, Object> params = new HashMap<>();
        params.put("no", orderNo);
        params.put("userId", userId);
        params.put("orderProductId", 0);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, Q_INDENT_CONFIRM, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                RequestStatus requestStatus = gson.fromJson(result, RequestStatus.class);
                if (requestStatus != null) {
                    if (requestStatus.getCode().equals(SUCCESS_CODE)) {
                        showToastRequestMsg(DirectExchangeDetailsActivity.this, requestStatus);
                        finish();
                    } else {
                        showToastRequestMsg(DirectExchangeDetailsActivity.this, requestStatus);
                    }
                }
            }
        });
    }

    //  取消订单
    private void cancelOrder() {
        Map<String, Object> params = new HashMap<>();
        params.put("no", orderNo);
        params.put("userId", userId);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, Q_INDENT_CANCEL, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                RequestStatus requestStatus = gson.fromJson(result, RequestStatus.class);
                if (requestStatus != null) {
                    if (requestStatus.getCode().equals(SUCCESS_CODE)) {
                        showToastRequestMsg(DirectExchangeDetailsActivity.this, requestStatus);
                        finish();
                    } else {
                        showToastRequestMsg(DirectExchangeDetailsActivity.this, requestStatus);
                    }
                }
            }
        });
    }

    //  订单删除
    private void delOrder() {
        Map<String, Object> params = new HashMap<>();
        params.put("no", orderNo);
        params.put("userId", userId);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, Q_INDENT_DEL, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                RequestStatus requestStatus = gson.fromJson(result, RequestStatus.class);
                if (requestStatus != null) {
                    if (requestStatus.getCode().equals(SUCCESS_CODE)) {
                        showToast(DirectExchangeDetailsActivity.this, String.format(getResources().getString(R.string.doSuccess), "删除订单"));
                        finish();
                    } else {
                        showToastRequestMsg(DirectExchangeDetailsActivity.this, requestStatus);
                    }
                }
            }
        });
    }


    @OnClick(R.id.tv_indent_back)
    void goBack(View view) {
        finish();
    }

    //  跳转客服
    @OnClick(R.id.iv_indent_service)
    void openService(View view) {
        setVisitorOpenService();
    }

    private void setVisitorOpenService() {
        if (infoDetailEntity != null) {
            IndentInfoDetailBean indentInfoDetailBean = infoDetailEntity.getIndentInfoDetailBean();
            OrderDetailBean orderDetailBean = indentInfoDetailBean.getOrderDetailBean();
            QyProductIndentInfo qyProductIndentInfo = null;
            if (orderDetailBean != null) {
                qyProductIndentInfo = new QyProductIndentInfo();
                if (goodsBeanList.size() > 0) {
                    qyProductIndentInfo.setTitle(getStrings(goodsBeanList.get(0).getName()));
                    qyProductIndentInfo.setPicUrl(getStrings(goodsBeanList.get(0).getPicUrl()));
                }
                qyProductIndentInfo.setDesc(INDENT_PRO_STATUS.get(String.valueOf(orderDetailBean.getStatus())));
                qyProductIndentInfo.setNote(String.format(getResources().getString(R.string.money_price_chn), orderDetailBean.getAmount()));
                qyProductIndentInfo.setUrl(Url.BASE_SHARE_PAGE_TWO + "m/template/order_template/order.html?noid=" + orderNo);
            }
            QyServiceUtils.getQyInstance().openQyServiceChat(this, "订单详情", Url.BASE_SHARE_PAGE_TWO + "m/template/order_template/order.html?noid=" + orderNo, qyProductIndentInfo);
        } else {
            QyServiceUtils.getQyInstance().openQyServiceChat(this, "订单详情", Url.BASE_SHARE_PAGE_TWO + "m/template/order_template/order.html?noid=" + orderNo, null);
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
//                            支付宝
                case R.id.ll_pay_ali:
                    //                调起支付宝支付
                    payWay = PAY_ALI_PAY;
                    paymentIndent();
                    break;
//                            微信
                case R.id.ll_pay_we_chat:
                    payWay = PAY_WX_PAY;
                    paymentIndent();
                    break;
                case R.id.ll_pay_union:
//                    银联支付
                    payWay = PAY_UNION_PAY;
                    paymentIndent();
                    break;
//                            取消
                case R.id.tv_pay_cancel:
                    break;
            }
        }
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

    @Override
    protected void onDestroy() {
        getConstant();
        constantMethod.stopSchedule();
        constantMethod.releaseHandlers();
        super.onDestroy();
        if (cancelOrderDialogHelper != null
                && cancelOrderDialogHelper.getAlertDialog() != null && cancelOrderDialogHelper.getAlertDialog().isShowing()) {
            cancelOrderDialogHelper.dismiss();
        }
        if (confirmOrderDialogHelper != null
                && confirmOrderDialogHelper.getAlertDialog() != null && confirmOrderDialogHelper.getAlertDialog().isShowing()) {
            confirmOrderDialogHelper.dismiss();
        }
        if (delOrderDialogHelper != null
                && delOrderDialogHelper.getAlertDialog() != null && delOrderDialogHelper.getAlertDialog().isShowing()) {
            delOrderDialogHelper.dismiss();
        }
        if (refundOrderDialogHelper != null
                && refundOrderDialogHelper.getAlertDialog() != null && refundOrderDialogHelper.getAlertDialog().isShowing()) {
            refundOrderDialogHelper.dismiss();
        }
    }


    private void setTimeDown(OrderProductInfoBean orderProductInfoBean) {
        int indentStatus = orderProductInfoBean.getStatus();
        if (0 <= indentStatus && indentStatus < 10) {
            lvHeaderView.ll_direct_count_time.setVisibility(View.VISIBLE);
            try {
                orderProductInfoBean.setSecond(orderProductInfoBean.getSecond() - 1);
                //格式化结束时间
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
                Date dateCreate = formatter.parse(orderDetailBean.getCreateTime());
                long overTime = orderProductInfoBean.getSecond() * 1000;
                Date dateCurrent;
                if (!TextUtils.isEmpty(infoDetailEntity.getCurrentTime())) {
                    dateCurrent = formatter.parse(infoDetailEntity.getCurrentTime());
                } else {
                    dateCurrent = new Date();
                }
                lvHeaderView.cv_countdownTime_direct.updateShow(dateCreate.getTime() + overTime
                        - dateCurrent.getTime());
                if (isEndOrStartTimeAddSeconds(orderProductInfoBean.getOrderCreateTime()
                        , orderProductInfoBean.getCurrentTime()
                        , orderProductInfoBean.getSecond())) {
                    lvHeaderView.cv_countdownTime_direct.setOnCountdownEndListener(new CountdownView.OnCountdownEndListener() {
                        @Override
                        public void onEnd(CountdownView cv) {
                            cv.setOnCountdownEndListener(null);
                            loadData();
                        }
                    });
                } else {
                    lvHeaderView.cv_countdownTime_direct.setOnCountdownEndListener(null);
                    lvHeaderView.ll_direct_count_time.setVisibility(GONE);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            lvHeaderView.ll_direct_count_time.setVisibility(GONE);
        }
    }

    private void getConstant() {
        if (constantMethod == null) {
            constantMethod = new ConstantMethod();
        }
    }

    /**
     * 设置催发货
     *
     * @param orderDetailBean
     */
    private void setRemindDelivery(OrderDetailBean orderDetailBean) {
        Map<String, Object> params = new HashMap<>();
        params.put("uid", userId);
        params.put("orderNo", orderDetailBean.getNo());
        NetLoadUtils.getNetInstance().loadNetDataPost(this, Q_INQUIRY_WAIT_SEND_EXPEDITING, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                if (loadHud != null) {
                    loadHud.dismiss();
                }
                Gson gson = new Gson();
                RequestStatus requestStatus = gson.fromJson(result, RequestStatus.class);
                if (requestStatus != null) {
                    if (requestStatus.getCode().equals(SUCCESS_CODE)) {
                        orderDetailBean.setWaitDeliveryFlag(false);
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
}
