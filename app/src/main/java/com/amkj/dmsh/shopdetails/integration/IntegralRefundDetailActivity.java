package com.amkj.dmsh.shopdetails.integration;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.bean.RequestStatus;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.mine.adapter.ShopCarComPreProAdapter;
import com.amkj.dmsh.mine.bean.CartProductInfoBean;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.qyservice.QyProductIndentInfo;
import com.amkj.dmsh.qyservice.QyServiceUtils;
import com.amkj.dmsh.shopdetails.adapter.RefundDetailProductAdapter;
import com.amkj.dmsh.shopdetails.adapter.RefundTypeAdapter;
import com.amkj.dmsh.shopdetails.bean.DirectApplyRefundBean;
import com.amkj.dmsh.shopdetails.bean.DirectApplyRefundBean.DirectRefundProBean;
import com.amkj.dmsh.shopdetails.bean.RefundDetailEntity;
import com.amkj.dmsh.shopdetails.bean.RefundDetailEntity.RefundDetailBean;
import com.amkj.dmsh.shopdetails.bean.RefundDetailEntity.RefundDetailBean.ExpressInfoBean;
import com.amkj.dmsh.shopdetails.bean.RefundDetailEntity.RefundDetailBean.RefundGoodsAddressBean;
import com.amkj.dmsh.shopdetails.bean.RefundDetailEntity.RefundDetailBean.RefundPayInfoBean;
import com.amkj.dmsh.shopdetails.bean.RefundDetailProductBean;
import com.amkj.dmsh.shopdetails.bean.RefundLogisticEntity;
import com.amkj.dmsh.shopdetails.bean.RefundTypeBean;
import com.amkj.dmsh.utils.CountDownTimer;
import com.amkj.dmsh.utils.KeyboardUtils;
import com.amkj.dmsh.utils.NetWorkUtils;
import com.amkj.dmsh.utils.gson.GsonUtils;
import com.amkj.dmsh.views.alertdialog.AlertDialogHelper;
import com.bigkoo.pickerview.adapter.ArrayWheelAdapter;
import com.contrarywind.view.WheelView;
import com.klinker.android.link_builder.Link;
import com.klinker.android.link_builder.LinkBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

import static android.widget.LinearLayout.SHOW_DIVIDER_END;
import static android.widget.LinearLayout.SHOW_DIVIDER_NONE;
import static com.amkj.dmsh.constant.ConstantMethod.getStringChangeFloat;
import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.getStringsChNPrice;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.showToastRequestMsg;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.INDENT_PRO_STATUS;
import static com.amkj.dmsh.constant.ConstantVariable.IS_LOGIN_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.REFUND_REPAIR;
import static com.amkj.dmsh.constant.ConstantVariable.REFUND_TYPE;
import static com.amkj.dmsh.constant.ConstantVariable.REGEX_NUM;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.Url.Q_INDENT_LOGISTIC_COM;
import static com.amkj.dmsh.constant.Url.Q_INDENT_LOGISTIC_SUB;
import static com.amkj.dmsh.constant.Url.Q_INDENT_REFUND_DETAIL;
import static com.amkj.dmsh.constant.Url.Q_INDENT_REPAIR_DETAIL;
import static com.amkj.dmsh.constant.Url.Q_INDENT_REPAIR_LOGISTIC_SUB;
import static com.amkj.dmsh.constant.Url.Q_INDENT_REPAIR_RECEIVE;
import static com.amkj.dmsh.utils.TimeUtils.getCoutDownTime;
import static com.amkj.dmsh.utils.TimeUtils.getTimeDifference;
import static com.amkj.dmsh.utils.TimeUtils.isEndOrStartTimeAddSeconds;

/**
 * Created by xiaoxin on 2020/4/8
 * Version:v4.5.0
 * ClassDescription :????????????????????????
 */
public class IntegralRefundDetailActivity extends BaseActivity {
    @BindView(R.id.iv_indent_service)
    ImageView iv_indent_service;
    @BindView(R.id.tv_indent_title)
    TextView tv_indent_title;
    @BindView(R.id.iv_indent_search)
    ImageView iv_indent_search;
    @BindView(R.id.sv_layout_refund_detail)
    NestedScrollView sv_layout_refund_detail;
    //    ????????????
    @BindView(R.id.tv_refund_detail_status)
    TextView tv_refund_detail_status;
    //    ????????????
    @BindView(R.id.tv_refund_detail_msg)
    TextView tv_refund_detail_msg;
    //    ??????????????????
    @BindView(R.id.ll_refund_logistic)
    LinearLayout ll_refund_logistic;
    //    ????????????
    @BindView(R.id.tv_repair_address)
    TextView tv_refund_address;
    @BindView(R.id.tv_repair_consignee_phone)
    TextView tv_repair_consignee_phone;
    //    ???????????????*
    @BindView(R.id.tv_refund_logistic)
    TextView tv_refund_logistic;
    //    ???????????????*
    @BindView(R.id.tv_refund_logistic_no)
    TextView tv_refund_logistic_no;
    /**
     * ??????????????????
     */
    @BindView(R.id.rel_repair_logistic_fee)
    RelativeLayout rel_repair_logistic_fee;
    @BindView(R.id.tv_refund_logistic_fee)
    TextView tv_refund_logistic_fee;
    @BindView(R.id.et_refund_logistic_fee)
    EditText et_refund_logistic_fee;
    //    ??????????????????
    @BindView(R.id.et_refund_logistic_no)
    EditText et_refund_logistic_no;
    //    ????????????
    @BindView(R.id.iv_direct_indent_pro)
    ImageView iv_direct_indent_pro;
    @BindView(R.id.tv_direct_indent_pro_name)
    TextView tv_direct_indent_pro_name;
    @BindView(R.id.tv_direct_indent_pro_sku)
    TextView tv_direct_indent_pro_sku;
    @BindView(R.id.tv_direct_pro_count)
    TextView tv_direct_pro_count;
    @BindView(R.id.tv_direct_indent_pro_price)
    TextView tv_direct_indent_pro_price;
    @BindView(R.id.rel_indent_com_pre_pro)
    RelativeLayout rel_indent_com_pre_pro;
    @BindView(R.id.communal_recycler_wrap)
    RecyclerView communal_recycler_wrap;
    //    ??????????????????
    @BindView(R.id.rv_refund_type)
    RecyclerView rv_refund_type;
    //    ?????????????????????????????????
    @BindView(R.id.rel_refund_product)
    RelativeLayout rel_refund_product;
    //    ????????????
    @BindView(R.id.ll_refund_bottom)
    LinearLayout ll_refund_bottom;
    //    ????????????
    @BindView(R.id.tv_refund_first)
    TextView tv_refund_first;
    //    ????????????
    @BindView(R.id.tv_refund_second)
    TextView tv_refund_second;
    //    ????????????
    @BindView(R.id.tv_submit_apply_refund)
    TextView tv_submit_apply_refund;
    //    ??????????????????
    @BindView(R.id.ll_communal_sel_wheel)
    LinearLayout ll_communal_sel_wheel;
    @BindView(R.id.tv_refund_logistic_sel)
    TextView tv_refund_logistic_sel;
    @BindView(R.id.wv_communal_one)
    WheelView wv_communal_one;
    @BindView(R.id.rv_product)
    RecyclerView mRvProduct;
    @BindView(R.id.ll_product)
    LinearLayout mLlProduct;
    private String no;
    private String orderProductId;
    private String orderRefundProductId;
    private final int CANCEL_APPLY = 1;
    private final int EDIT_APPLY = 2;
    //   ??????????????????
    private final int CHECK_REPAIR_LOGISTIC = 3;
    //    ??????????????????
    private final int REPAIR_CONFIRM = 4;
    private String express;
    private List<String> expressCompanies;
    private List<RefundTypeBean> refundTypeBeans = new ArrayList<>();
    private List<CartProductInfoBean> preComProInfoBeanList = new ArrayList();
    private final String LOGISTIC_C = "????????????*";
    private final String LOGISTIC_N = "????????????*";
    private final String LOGISTIC_F = "??????*";
    private final String[] refundTypes = {"???????????????", "???????????????", "???????????????", "???????????????", "???????????????"};
    private final String[] refundRepairTypes = {"???????????????", "???????????????", "???????????????", "???????????????", "???????????????", "?????????", "???????????????"};
    private ShopCarComPreProAdapter shopCarComPreProAdapter;
    private ConstantMethod constantMethod;
    private String refundType;
    private RefundTypeAdapter refundTypeAdapter;
    private RefundDetailBean refundDetailBean;
    private String repairAddress;
    private RefundDetailEntity refundDetailEntity;
    private AlertDialogHelper cancelApplyDialogHelper;
    private List<RefundDetailProductBean> products = new ArrayList<>();
    private RefundDetailProductAdapter refundDetailProductAdapter;
    private CountDownTimer mCountDownTimer;

    @Override
    protected int getContentView() {
        return R.layout.activity_integral_refund_detail;
    }

    @Override
    protected void initViews() {
        getLoginStatus(this);
        iv_indent_search.setVisibility(View.GONE);
        sv_layout_refund_detail.setVisibility(View.GONE);
        tv_refund_logistic.setText(LOGISTIC_C);
        tv_refund_logistic_no.setText(LOGISTIC_N);
        Link link = new Link("*");
        //        @????????????
        link.setTextColor(Color.parseColor("#ff5e5e"));
        link.setUnderlined(false);
        link.setHighlightAlpha(0f);
        LinkBuilder.on(tv_refund_logistic)
                .setText(LOGISTIC_C)
                .addLink(link)
                .build();
        LinkBuilder.on(tv_refund_logistic_no)
                .setText(LOGISTIC_N)
                .addLink(link)
                .build();
        LinkBuilder.on(tv_refund_logistic_fee)
                .setText(LOGISTIC_F)
                .addLink(link)
                .build();
        tv_indent_title.setText("????????????");
        Intent intent = getIntent();
        refundType = intent.getStringExtra(REFUND_TYPE);
        no = intent.getStringExtra("orderNo");
        orderProductId = intent.getStringExtra("orderProductId");
        orderRefundProductId = intent.getStringExtra("orderRefundProductId");
        rv_refund_type.setLayoutManager(new LinearLayoutManager(getActivity()));
        refundTypeAdapter = new RefundTypeAdapter(refundTypeBeans);
        rv_refund_type.setAdapter(refundTypeAdapter);
        rv_refund_type.setNestedScrollingEnabled(false);
        communal_recycler_wrap.setNestedScrollingEnabled(false);
        KeyboardUtils.registerSoftInputChangedListener(this, new KeyboardUtils.OnSoftInputChangedListener() {
            @Override
            public void onSoftInputChanged(int height) {
                if (height == 0) {
                    ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0).requestFocus();
                }
            }
        });

        //???????????????????????????
        mRvProduct.setLayoutManager(new LinearLayoutManager(this));
        mRvProduct.setNestedScrollingEnabled(false);
        refundDetailProductAdapter = new RefundDetailProductAdapter(this, products);
        mRvProduct.setAdapter(refundDetailProductAdapter);
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
    protected void loadData() {
//        ????????????
        if (!TextUtils.isEmpty(refundType)
                && refundType.equals(REFUND_REPAIR)) {
            getRepairDetailData();
        } else {
//            ????????????????????????
            getRefundDetailData();
        }
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    @Override
    public View getLoadView() {
        return sv_layout_refund_detail;
    }

    private void getRepairDetailData() {
        if (userId < 1) {
            return;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("orderRefundProductId", orderRefundProductId);
        params.put("orderProductId", orderProductId);
        params.put("userId", userId);
        params.put("no", no);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, Q_INDENT_REPAIR_DETAIL, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {

                refundDetailEntity = GsonUtils.fromJson(result, RefundDetailEntity.class);
                if (refundDetailEntity != null) {
                    if (refundDetailEntity.getCode().equals(SUCCESS_CODE)) {
                        sv_layout_refund_detail.setVisibility(View.VISIBLE);
                        refundDetailBean = refundDetailEntity.getRefundDetailBean();
                        if (refundDetailEntity.getStatus() != null) {
                            INDENT_PRO_STATUS = refundDetailEntity.getStatus();
                        }
                        setRefundDetailData(refundDetailBean);
                    } else if (refundDetailEntity.getCode().equals(EMPTY_CODE)) {
                        showToast(R.string.invalidData);
                    } else {
                        showToast(refundDetailEntity.getMsg());
                    }
                }
                NetLoadUtils.getNetInstance().showLoadSir(loadService, refundDetailEntity);
            }

            @Override
            public void onNotNetOrException() {
                NetLoadUtils.getNetInstance().showLoadSir(loadService, refundDetailEntity);
            }

            @Override
            public void onError(Throwable throwable) {
                showToast(R.string.invalidData);
            }
        });
    }

    private void getRefundDetailData() {
        if (userId < 1) {
            return;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("no", no);
        params.put("orderProductId", orderProductId);
        params.put("orderRefundProductId", orderRefundProductId);
        params.put("userId", userId);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, Q_INDENT_REFUND_DETAIL, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {

                refundDetailEntity = GsonUtils.fromJson(result, RefundDetailEntity.class);
                if (refundDetailEntity != null) {
                    if (refundDetailEntity.getCode().equals(SUCCESS_CODE)) {
                        sv_layout_refund_detail.setVisibility(View.VISIBLE);
                        refundDetailBean = refundDetailEntity.getRefundDetailBean();
                        refundDetailBean.setCurrentTime(refundDetailEntity.getCurrentTime());
                        setRefundDetailData(refundDetailBean);
                    } else if (refundDetailEntity.getCode().equals(EMPTY_CODE)) {
                        showToast(R.string.invalidData);
                    } else {
                        showToast(refundDetailEntity.getMsg());
                    }
                }
                NetLoadUtils.getNetInstance().showLoadSir(loadService, refundDetailEntity);
            }

            @Override
            public void onNotNetOrException() {
                NetLoadUtils.getNetInstance().showLoadSir(loadService, refundDetailEntity);
            }

            @Override
            public void onError(Throwable throwable) {
                showToast(R.string.invalidData);
            }
        });
    }

    //    ????????????
    private void cancelApply() {
        if (NetWorkUtils.checkNet(getActivity())) {
            String url;
            Map<String, Object> params = new HashMap<>();
            if (refundType.equals(REFUND_REPAIR)) {
//                ????????????
                url = Url.Q_INDENT_REFUND_REPAIR_CANCEL;
                cancelRefund(url, params);
            } else if (REFUND_TYPE.equals(refundType)) {
//                ??????????????????
                url = Url.Q_CANCEL_APPLY;
                cancelRefund(url, params);
            } else {
                Toast.makeText(this, R.string.refund_type_error, Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * ??????????????????
     *
     * @param url
     * @param params
     */
    private void cancelRefund(String url, Map<String, Object> params) {
        params.put("orderRefundProductId", refundDetailBean.getOrderRefundProductId());
        params.put("orderProductId", refundDetailBean.getOrderProductId());
        NetLoadUtils.getNetInstance().loadNetDataPost(this, url, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {

                RequestStatus requestStatus = GsonUtils.fromJson(result, RequestStatus.class);
                if (requestStatus != null) {
                    if (requestStatus.getCode().equals(SUCCESS_CODE)) {
                        showToast(String.format(getResources().getString(R.string.doSuccess), "??????"));
                        finish();
                    } else if (requestStatus.getCode().equals(EMPTY_CODE)) {
                        showToast(R.string.invalidData);
                    } else {
                        showToastRequestMsg(requestStatus);
                    }
                }
            }

            @Override
            public void onError(Throwable throwable) {
                showToast(R.string.invalidData);
            }
        });
    }

    private void getLogisticCompany() {
        NetLoadUtils.getNetInstance().loadNetDataPost(this, Q_INDENT_LOGISTIC_COM, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {

                RefundLogisticEntity refundLogisticEntity = GsonUtils.fromJson(result, RefundLogisticEntity.class);
                if (refundLogisticEntity != null) {
                    if (refundLogisticEntity.getCode().equals(SUCCESS_CODE)) {
                        expressCompanies = refundLogisticEntity.getExpressCompanys();
                        setRefundLogisticData(expressCompanies);
                    } else if (refundLogisticEntity.getCode().equals(EMPTY_CODE)) {
                        showToast(R.string.invalidData);
                    } else {
                        showToast(refundLogisticEntity.getMsg());
                    }
                }
            }

            @Override
            public void onError(Throwable throwable) {
                showToast(R.string.invalidData);
            }
        });
    }

    private void setRefundLogisticData(final List<String> expressCompanys) {
        if (expressCompanys.size() > 0) {
            wv_communal_one.setAdapter(new ArrayWheelAdapter<>(expressCompanys));
            wv_communal_one.setCyclic(false);
            wv_communal_one.setCurrentItem(0);
        }
    }

    /**
     * ??????????????????
     *
     * @param refundDetailBean
     */
    private void setRefundDetailData(RefundDetailBean refundDetailBean) {
        rel_refund_product.setVisibility(View.VISIBLE);
        rv_refund_type.setVisibility(View.VISIBLE);
        ll_refund_logistic.setShowDividers(SHOW_DIVIDER_END);
        tv_refund_detail_status.setText(getStrings(refundDetailBean.getStatusName()));
        if (refundDetailBean.getAutoUndoRefundGoodsTime() > 0) {
            setRefundTime(refundDetailBean);
        } else {
            if (refundDetailBean.getRefundPayInfo() != null
                    && !TextUtils.isEmpty(refundDetailBean.getRefundPayInfo().getRefundAccount())) {
                RefundPayInfoBean refundPayInfo = refundDetailBean.getRefundPayInfo();
                tv_refund_detail_msg.setVisibility(View.VISIBLE);
                tv_refund_detail_msg.setText(getResources().getString(R.string.refund_account_time_price,
                        getStrings(refundPayInfo.getRefundAccount()),
                        getStrings(refundPayInfo.getReceiveRefundTime())));
            } else {
                if (!TextUtils.isEmpty(refundDetailBean.getNoticeMsg())) {
                    tv_refund_detail_msg.setVisibility(View.VISIBLE);
                    tv_refund_detail_msg.setText(refundDetailBean.getNoticeMsg());
                } else {
                    tv_refund_detail_msg.setText("");
                    tv_refund_detail_msg.setVisibility(View.GONE);
                }
            }
        }

        //????????????????????????
        List<RefundDetailProductBean> productList = refundDetailBean.getRefundOrderProductList();
        mLlProduct.setVisibility(View.GONE);
        mRvProduct.setVisibility(View.VISIBLE);
        products.clear();
        if (productList != null && productList.size() > 0) {
            products.addAll(productList);
        } else {
            RefundDetailProductBean refundDetailProductBean = new RefundDetailProductBean();
            refundDetailProductBean.setPicUrl(refundDetailBean.getPicUrl());
            refundDetailProductBean.setProductName(refundDetailBean.getName());
            refundDetailProductBean.setCount(String.valueOf(refundDetailBean.getCount()));
            refundDetailProductBean.setSaleSkuValue(refundDetailBean.getSaleSkuValue());
            refundDetailProductBean.setPrice(refundDetailBean.getPrice());
            refundDetailProductBean.setIntegralPrice(String.valueOf(refundDetailBean.getIntegralPrice()));
            products.add(refundDetailProductBean);
        }
        refundDetailProductAdapter.notifyDataSetChanged();

        setRefundTypeData(refundDetailBean);
        tv_submit_apply_refund.setVisibility(View.GONE);
        if (REFUND_REPAIR.equals(refundType)) {
            switch (refundDetailBean.getStatus()) {
                case 50:
                    ll_refund_logistic.setVisibility(View.GONE);
                    ll_refund_bottom.setVisibility(View.VISIBLE);
                    tv_refund_first.setVisibility(View.VISIBLE);
                    tv_refund_first.setText("????????????");
                    tv_refund_first.setTag(R.id.tag_first, CANCEL_APPLY);
                    tv_refund_first.setTag(R.id.tag_second, refundDetailBean);
                    tv_refund_second.setVisibility(View.GONE);
                    break;
                case 51:
//                    ????????????????????????????????????????????????????????????
                    rel_refund_product.setVisibility(View.GONE);
                    rv_refund_type.setVisibility(View.GONE);
                    ll_refund_logistic.setShowDividers(SHOW_DIVIDER_NONE);

                    ll_refund_bottom.setVisibility(View.VISIBLE);
                    tv_refund_first.setVisibility(View.VISIBLE);
                    tv_refund_first.setText("????????????");
                    tv_refund_first.setTag(R.id.tag_first, CANCEL_APPLY);
                    tv_refund_first.setTag(R.id.tag_second, refundDetailBean);
                    tv_refund_second.setVisibility(View.GONE);
                    ll_refund_logistic.setVisibility(View.VISIBLE);
                    rel_repair_logistic_fee.setVisibility(View.VISIBLE);
                    getLogisticCompany();
                    tv_submit_apply_refund.setVisibility(View.VISIBLE);
                    RefundGoodsAddressBean refundGoodsAddress = refundDetailBean.getRefundGoodsAddress();
                    if (refundGoodsAddress != null) {
                        tv_refund_address.setText(getStrings(refundGoodsAddress.getRefundGoodsAddress()));
                        tv_repair_consignee_phone.setText((refundGoodsAddress.getRefundGoodsReceiver() + "\t" + refundGoodsAddress.getRefundGoodsPhone()));
                        repairAddress = getStrings(refundGoodsAddress.getRefundGoodsAddress()) + "\t" + refundGoodsAddress.getRefundGoodsReceiver() + "\t" + refundGoodsAddress.getRefundGoodsPhone();
                    }
                    break;
                case 55:
                    ll_refund_bottom.setVisibility(View.VISIBLE);
                    ll_refund_logistic.setVisibility(View.GONE);
                    tv_refund_first.setVisibility(View.VISIBLE);
                    tv_refund_second.setVisibility(View.VISIBLE);
                    tv_refund_first.setText("????????????");
                    tv_refund_first.setTag(R.id.tag_first, CHECK_REPAIR_LOGISTIC);
                    tv_refund_first.setTag(R.id.tag_second, refundDetailBean);
                    tv_refund_second.setText("????????????");
                    tv_refund_second.setTag(R.id.tag_first, REPAIR_CONFIRM);
                    tv_refund_second.setTag(R.id.tag_second, refundDetailBean);
                    break;
                default:
                    ll_refund_bottom.setVisibility(View.GONE);
                    ll_refund_logistic.setVisibility(View.GONE);
                    rel_repair_logistic_fee.setVisibility(View.GONE);
                    break;
            }
        } else if (REFUND_TYPE.equals(refundType)) {
            if (refundDetailBean.getStatus() == -10
                    || refundDetailBean.getStatus() == -31
                    || refundDetailBean.getStatus() == -30) {
//            ???????????????
                if (refundDetailBean.getStatus() == -10 || refundDetailBean.getStatus() == -30) {
                    if (refundDetailBean.getChildOrderStatus() == 30 || refundDetailBean.getChildOrderStatus() == 31
                            || refundDetailBean.getChildOrderStatus() == 40) {
                        tv_refund_first.setVisibility(View.VISIBLE);
                        tv_refund_second.setVisibility(View.GONE);
                        tv_refund_first.setText("????????????");
                        tv_refund_first.setTag(R.id.tag_first, CANCEL_APPLY);
                        tv_refund_first.setTag(R.id.tag_second, refundDetailBean);
                    } else {
                        ll_refund_bottom.setVisibility(View.GONE);
                    }
                } else if (refundDetailBean.getStatus() == -31) { // ???????????????
                    if (refundDetailBean.getChildOrderStatus() == 30 || refundDetailBean.getChildOrderStatus() == 31
                            || refundDetailBean.getChildOrderStatus() == 40) {
                        tv_refund_first.setVisibility(View.VISIBLE);
                        tv_refund_second.setVisibility(View.VISIBLE);
                        tv_refund_first.setText("????????????");
                        tv_refund_first.setTag(R.id.tag_first, CANCEL_APPLY);
                        tv_refund_first.setTag(R.id.tag_second, refundDetailBean);
                        tv_refund_second.setText("????????????");
                        tv_refund_second.setTag(R.id.tag_first, EDIT_APPLY);
                        tv_refund_second.setTag(R.id.tag_second, refundDetailBean);
                    } else {
                        tv_refund_first.setVisibility(View.VISIBLE);
                        tv_refund_second.setVisibility(View.GONE);
                        tv_refund_first.setText("????????????");
                        tv_refund_first.setTag(R.id.tag_first, EDIT_APPLY);
                        tv_refund_first.setTag(R.id.tag_second, refundDetailBean);
                    }
                } else {
                    ll_refund_bottom.setVisibility(View.GONE);
                }
            } else {
                ll_refund_bottom.setVisibility(View.GONE);
//            ??????????????????
                if (refundDetailBean.getStatus() == -32) {
                    getLogisticCompany();
                    tv_submit_apply_refund.setVisibility(View.VISIBLE);
                    ll_refund_logistic.setVisibility(View.VISIBLE);
                    RefundGoodsAddressBean refundGoodsAddress = refundDetailBean.getRefundGoodsAddress();
                    if (refundGoodsAddress != null) {
                        tv_refund_address.setText(getStrings(refundGoodsAddress.getRefundGoodsAddress()));
                        tv_repair_consignee_phone.setText((refundGoodsAddress.getRefundGoodsReceiver() + "\t" + refundGoodsAddress.getRefundGoodsPhone()));
                        repairAddress = getStrings(refundGoodsAddress.getRefundGoodsAddress()) + "\t" + refundGoodsAddress.getRefundGoodsReceiver() + "\t" + refundGoodsAddress.getRefundGoodsPhone();
                    }
                } else if (refundDetailBean.getStatus() == -35) {
                    ll_refund_logistic.setVisibility(View.VISIBLE);
                    tv_refund_logistic_sel.setVisibility(View.GONE);
                    et_refund_logistic_no.setVisibility(View.GONE);
                    rel_repair_logistic_fee.setVisibility(View.GONE);
                    RefundGoodsAddressBean refundGoodsAddress = refundDetailBean.getRefundGoodsAddress();
                    ExpressInfoBean expressInfo = refundDetailBean.getExpressInfo();
                    if (refundGoodsAddress != null) {
                        tv_refund_address.setText(getStrings(refundGoodsAddress.getRefundGoodsAddress()));
                        tv_repair_consignee_phone.setText((refundGoodsAddress.getRefundGoodsReceiver() + "\t" + refundGoodsAddress.getRefundGoodsPhone()));
                        repairAddress = getStrings(refundGoodsAddress.getRefundGoodsAddress()) + "\t" + refundGoodsAddress.getRefundGoodsReceiver() + "\t"
                                + refundGoodsAddress.getRefundGoodsPhone();
                    }
                    if (expressInfo != null) {
                        tv_refund_logistic.setText(getString(R.string.refund_pass_express_company, getStrings(expressInfo.getExpressCompany())));
                        tv_refund_logistic_no.setText(getString(R.string.refund_pass_express_no, getStrings(expressInfo.getExpressNo())));
                    }
                } else {
                    ll_refund_logistic.setVisibility(View.GONE);
                }
            }
        } else {
            ll_refund_bottom.setVisibility(View.GONE);
            ll_refund_logistic.setVisibility(View.GONE);
        }

        preComProInfoBeanList.clear();
        if (refundDetailBean.getCombineProductInfoList() != null && refundDetailBean.getCombineProductInfoList().size() > 0) {
            preComProInfoBeanList.addAll(refundDetailBean.getCombineProductInfoList());
        }
        if (refundDetailBean.getPresentProductInfoList() != null && refundDetailBean.getPresentProductInfoList().size() > 0) {
            preComProInfoBeanList.addAll(refundDetailBean.getPresentProductInfoList());
        }
        if (preComProInfoBeanList.size() > 0) {
            rel_indent_com_pre_pro.setVisibility(View.VISIBLE);
            if (shopCarComPreProAdapter == null) {
                communal_recycler_wrap.setLayoutManager(new LinearLayoutManager(getActivity()));
                shopCarComPreProAdapter = new ShopCarComPreProAdapter(getActivity(), preComProInfoBeanList);
                communal_recycler_wrap.setAdapter(shopCarComPreProAdapter);
            }
            shopCarComPreProAdapter.notifyDataSetChanged();
        } else {
            rel_indent_com_pre_pro.setVisibility(View.GONE);
        }
    }

    /**
     * ???????????? ??????????????????
     *
     * @param refundDetailBean
     */
    private void setRefundTypeData(RefundDetailBean refundDetailBean) {
        refundTypeBeans.clear();
        switch (refundDetailBean.getRefundTypeId()) {
            case 3:
                refundTypeBeans.add(new RefundTypeBean(refundRepairTypes[0], getStrings(refundDetailBean.getRefundType())));
                refundTypeBeans.add(new RefundTypeBean(refundRepairTypes[1], getStrings(refundDetailBean.getContent())));
                refundTypeBeans.add(new RefundTypeBean(refundRepairTypes[2], getStrings(refundDetailBean.getCreateTime())));
                if (!TextUtils.isEmpty(refundDetailBean.getRepairExpressCompany())) {
                    refundTypeBeans.add(new RefundTypeBean(refundRepairTypes[3], getStrings(refundDetailBean.getRepairExpressCompany())));
                    refundTypeBeans.add(new RefundTypeBean(refundRepairTypes[4], getStrings(refundDetailBean.getRepairExpressNo())));
                    refundTypeBeans.add(new RefundTypeBean(refundRepairTypes[5], "??" + getStrings(refundDetailBean.getRepairExpressFee())));
                }
                refundTypeBeans.add(new RefundTypeBean(refundRepairTypes[6],
                        getStrings(refundDetailBean.getRepairReceiveReceiver()) + "\t" + getStrings(refundDetailBean.getRepairReceivePhone()) + "\n" + getStrings(refundDetailBean.getRepairReceiveAddress())));
                break;
            default:
                refundTypeBeans.add(new RefundTypeBean(refundTypes[0], getStrings(refundDetailBean.getRefundType())));
                refundTypeBeans.add(new RefundTypeBean(refundTypes[1], getStrings(refundDetailBean.getReason())));
                String priceName;
                if (refundDetailBean.getRefundIntegralPrice() > 0) {
                    float moneyPrice = getStringChangeFloat(refundDetailBean.getRefundPrice());
                    if (moneyPrice > 0) {
                        priceName = String.format(getResources().getString(R.string.integral_product_and_price)
                                , refundDetailBean.getRefundIntegralPrice(), getStrings(refundDetailBean.getRefundPrice()));
                    } else {
                        priceName = String.format(getResources().getString(R.string.integral_indent_product_price)
                                , refundDetailBean.getRefundIntegralPrice());
                    }
                } else {
                    priceName = getStringsChNPrice(getActivity(), refundDetailBean.getRefundPrice());
                }
                refundTypeBeans.add(new RefundTypeBean(refundTypes[2], priceName));
                refundTypeBeans.add(new RefundTypeBean(refundTypes[3], getStrings(refundDetailBean.getCreateTime())));
                refundTypeBeans.add(new RefundTypeBean(refundTypes[4], getStrings(refundDetailBean.getRefundNo())));
                break;
        }
        refundTypeAdapter.notifyDataSetChanged();
    }

    /**
     * ?????????????????????
     *
     * @param refundDetailBean
     */
    private void setRefundTime(final RefundDetailBean refundDetailBean) {
        try {
            String updateTime = refundDetailBean.getUpdateTime();
            String currentTime = refundDetailBean.getCurrentTime();
            long autoUndoRefundGoodsTime = refundDetailBean.getAutoUndoRefundGoodsTime();
            if (isEndOrStartTimeAddSeconds(updateTime, currentTime, autoUndoRefundGoodsTime)) {
                if (mCountDownTimer == null) {
                    mCountDownTimer = new CountDownTimer(getActivity()) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            String coutDownTime = getCoutDownTime(millisUntilFinished, false);
                            String coutDownText = "????????????????????????????????????" + coutDownTime;
                            Link link = new Link(Pattern.compile(REGEX_NUM));
                            link.setTextColor(0xffff5e6b);
                            link.setUnderlined(false);
                            link.setHighlightAlpha(0f);
                            tv_refund_detail_msg.setText(LinkBuilder.from(getActivity(), coutDownText)
                                    .addLink(link)
                                    .build());
                        }

                        @Override
                        public void onFinish() {
                            getRefundDetailData();
                        }
                    };
                }
                mCountDownTimer.setMillisInFuture(autoUndoRefundGoodsTime * 1000 - getTimeDifference(updateTime, currentTime));
                mCountDownTimer.start();
            } else {
                tv_refund_detail_msg.setText(getStrings(refundDetailBean.getNoticeMsg()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @OnClick({R.id.tv_one_click_cancel, R.id.tv_one_click_confirmed})
    void selLogisticCancelConfirm(View view) {
        switch (view.getId()) {
            case R.id.tv_one_click_confirmed:
                setSelLogistic();
                ll_communal_sel_wheel.setVisibility(View.GONE);
                tv_submit_apply_refund.setVisibility(View.VISIBLE);
                tv_refund_logistic_sel.setSelected(false);
                break;
            case R.id.tv_one_click_cancel:
                ll_communal_sel_wheel.setVisibility(View.GONE);
                tv_submit_apply_refund.setVisibility(View.VISIBLE);
                tv_refund_logistic_sel.setSelected(false);
                break;
        }
    }

    //    ??????????????????
    @OnClick(R.id.tv_submit_apply_refund)
    void submitInfo(View view) {
        String logistic = tv_refund_logistic_sel.getText().toString().trim();
        String logisticNo = et_refund_logistic_no.getText().toString().trim();
        if (REFUND_TYPE.equals(refundType)) {
            if (!TextUtils.isEmpty(logistic) && !TextUtils.isEmpty(logisticNo)) {
                submitLogisticInfo(logistic, logisticNo);
            } else if (TextUtils.isEmpty(logistic)) {
                showToast("?????????????????????");
            } else if (TextUtils.isEmpty(logisticNo)) {
                showToast("?????????????????????");
            }
        } else if (REFUND_REPAIR.equals(refundType)) {
            String logisticFee = et_refund_logistic_fee.getText().toString().trim();
            if (!TextUtils.isEmpty(logistic)
                    && !TextUtils.isEmpty(logisticNo)) {
                submitRepairLogisticInfo(logistic, logisticNo, logisticFee);
            } else if (TextUtils.isEmpty(logistic)) {
                showToast("?????????????????????");
            } else if (TextUtils.isEmpty(logisticNo)) {
                showToast("?????????????????????");
            }
        } else {
            Toast.makeText(this, "??????????????????????????????app????????????", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * ????????????????????????
     *
     * @param logistic
     * @param logisticNo
     * @param logisticFee
     */
    private void submitRepairLogisticInfo(String logistic, String logisticNo, String logisticFee) {
        Map<String, Object> params = new HashMap<>();
        params.put("no", no);
        params.put("orderProductId", orderProductId);
        params.put("orderRefundProductId", orderRefundProductId);
        params.put("expressCompany", logistic);
        params.put("refundNo", refundDetailBean.getRefundNo());
        params.put("expressNo", logisticNo);
        params.put("expressFee", TextUtils.isEmpty(logisticFee) ? "0" : logisticFee);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, Q_INDENT_REPAIR_LOGISTIC_SUB, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {

                RequestStatus requestStatus = GsonUtils.fromJson(result, RequestStatus.class);
                if (requestStatus != null) {
                    if (requestStatus.getCode().equals(SUCCESS_CODE)) {
                        showToast(String.format(getResources().getString(R.string.doSuccess), "??????"));
                        loadData();
                    } else if (requestStatus.getCode().equals(EMPTY_CODE)) {
                        showToast(R.string.invalidData);
                    } else {
                        showToastRequestMsg(requestStatus);
                    }
                }
            }

            @Override
            public void onError(Throwable throwable) {
                showToast(R.string.invalidData);
            }
        });
    }

    /**
     * ????????????????????????
     *
     * @param logistic
     * @param logisticNo
     */
    private void submitLogisticInfo(String logistic, String logisticNo) {
        Map<String, Object> params = new HashMap<>();
        params.put("no", no);
        params.put("orderProductId", orderProductId);
        params.put("orderRefundProductId", orderRefundProductId);
        params.put("userId", userId);
        params.put("expressCompany", logistic);
        params.put("expressNo", logisticNo);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, Q_INDENT_LOGISTIC_SUB, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {

                RequestStatus requestStatus = GsonUtils.fromJson(result, RequestStatus.class);
                if (requestStatus != null) {
                    if (requestStatus.getCode().equals(SUCCESS_CODE)) {
                        showToast(String.format(getResources().getString(R.string.doSuccess), "??????"));
                        loadData();
                    } else if (requestStatus.getCode().equals(EMPTY_CODE)) {
                        showToast(R.string.invalidData);
                    } else {
                        showToastRequestMsg(requestStatus);
                    }
                }
            }

            @Override
            public void onError(Throwable throwable) {
                showToast(R.string.invalidData);
            }
        });
    }

    /**
     * ???????????????????????????
     */
    private void confirmRepairReceive() {
        Map<String, Object> params = new HashMap<>();
        params.put("orderNo", no);
        params.put("orderProductId", orderProductId);
        params.put("orderRefundProductId", orderRefundProductId);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, Q_INDENT_REPAIR_RECEIVE, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {

                RequestStatus requestStatus = GsonUtils.fromJson(result, RequestStatus.class);
                if (requestStatus != null) {
                    if (requestStatus.getCode().equals(SUCCESS_CODE)) {
                        loadData();
                    } else if (requestStatus.getCode().equals(EMPTY_CODE)) {
                        showToast(R.string.invalidData);
                    } else {
                        showToastRequestMsg(requestStatus);
                    }
                }
            }

            @Override
            public void onError(Throwable throwable) {
                showToast(R.string.invalidData);
            }
        });
    }

    //    ??????????????????
    @OnClick(R.id.tv_refund_logistic_sel)
    void selLogistic(View view) {
        if (expressCompanies != null && expressCompanies.size() > 0
                && ll_communal_sel_wheel.getVisibility() == View.GONE) {
            tv_refund_logistic_sel.setSelected(true);
            ll_communal_sel_wheel.setVisibility(View.VISIBLE);
            tv_submit_apply_refund.setVisibility(View.GONE);
        }
    }

    private void setSelLogistic() {
        express = expressCompanies.get(wv_communal_one.getCurrentItem());
        tv_refund_logistic_sel.setText(getStrings(express));
    }

    //    ??????????????????
    @OnClick(R.id.tv_copy_text)
    void copyNo(View view) {
        if (!TextUtils.isEmpty(repairAddress)) {
            ClipboardManager cmb = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData mClipData = ClipData.newPlainText("Label", repairAddress);
            cmb.setPrimaryClip(mClipData);
            showToast("?????????");
        }
    }

    @OnClick({R.id.tv_refund_first, R.id.tv_refund_second})
    void setRefundDo(View view) {
        int doType = (int) view.getTag(R.id.tag_first);
        refundDetailBean = (RefundDetailBean) view.getTag(R.id.tag_second);
        switch (doType) {
            case CANCEL_APPLY:
                if (cancelApplyDialogHelper == null) {
                    cancelApplyDialogHelper = new AlertDialogHelper(getActivity());
                    cancelApplyDialogHelper.setTitleVisibility(View.GONE).setMsgTextGravity(Gravity.CENTER)
                            .setMsg(getResources().getString(R.string.cancel_invite)).setCancelText("??????")
                            .setConfirmText("??????")
                            .setCancelTextColor(getResources().getColor(R.color.text_login_gray_s));
                    cancelApplyDialogHelper.setAlertListener(new AlertDialogHelper.AlertConfirmCancelListener() {
                        @Override
                        public void confirm() {
                            cancelApply();
                        }

                        @Override
                        public void cancel() {
                        }
                    });
                }
                cancelApplyDialogHelper.show();
                break;
            case EDIT_APPLY:
                Intent intent = new Intent(getActivity(), IntegralApplyRefundActivity.class);
                DirectApplyRefundBean refundBean = new DirectApplyRefundBean();
                refundBean.setOrderNo(refundDetailBean.getNo());
                DirectRefundProBean directRefundProBean = new DirectRefundProBean();
                directRefundProBean.setId(refundDetailBean.getProductId());
                directRefundProBean.setOrderProductId(refundDetailBean.getOrderProductId());
                directRefundProBean.setCount(refundDetailBean.getCount());
                directRefundProBean.setName(refundDetailBean.getName());
                directRefundProBean.setPicUrl(refundDetailBean.getPicUrl());
                directRefundProBean.setSaleSkuValue(refundDetailBean.getSaleSkuValue());
                directRefundProBean.setPrice(refundDetailBean.getPrice());
                directRefundProBean.setRefundReasonId(refundDetailBean.getRefundReasonId());
                directRefundProBean.setIntegralPrice(refundDetailBean.getRefundIntegralPrice());
                List<DirectRefundProBean> directRefundProList = new ArrayList<>();
                directRefundProList.add(directRefundProBean);
                refundBean.setDirectRefundProList(directRefundProList);
                refundBean.setType(2);
                refundBean.setOrderRefundProductId(refundDetailBean.getOrderRefundProductId());
                Bundle bundle = new Bundle();
                bundle.putParcelable("refundPro", refundBean);
                intent.putExtras(bundle);
                intent.putExtra("cancelRefund", "1");
                intent.putExtra(REFUND_TYPE, REFUND_TYPE);
                startActivity(intent);
                finish();
                break;
        }
    }

    @OnClick(R.id.tv_indent_back)
    void goBack(View view) {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        KeyboardUtils.unregisterSoftInputChangedListener(this);
    }

    @OnClick(R.id.iv_indent_service)
    void skipService() {
        QyProductIndentInfo qyProductIndentInfo = null;
        if (refundDetailBean != null) {
            qyProductIndentInfo = new QyProductIndentInfo();
            qyProductIndentInfo.setTitle(refundDetailBean.getName());
            qyProductIndentInfo.setPicUrl(getStrings(refundDetailBean.getPicUrl()));
            qyProductIndentInfo.setDesc(INDENT_PRO_STATUS.get(String.valueOf(refundDetailBean.getStatus())));
            qyProductIndentInfo.setNote(String.format(getResources().getString(R.string.money_price_chn), refundDetailBean.getPrice()));
            qyProductIndentInfo.setUrl(Url.BASE_SHARE_PAGE_TWO + "order_template/order.html?noid=" + refundDetailBean.getNo());
        }
        QyServiceUtils.getQyInstance().openQyServiceChat(this, "??????????????????", "", qyProductIndentInfo);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                InputMethodManager imm =
                        (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        }
        try {
            return super.dispatchTouchEvent(ev);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // Return whether touch the view.
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v instanceof EditText) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            return !(event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom);
        }
        return false;
    }
}
