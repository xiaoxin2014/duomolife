package com.amkj.dmsh.shopdetails.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amkj.dmsh.R;
import com.amkj.dmsh.address.widget.WheelView;
import com.amkj.dmsh.address.widget.adapters.ArrayWheelAdapter;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.NetLoadUtils;
import com.amkj.dmsh.bean.RequestStatus;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.constant.XUtil;
import com.amkj.dmsh.mine.adapter.ShopCarComPreProAdapter;
import com.amkj.dmsh.mine.bean.ShopCarNewInfoEntity.ShopCarNewInfoBean.CartInfoBean.CartProductInfoBean;
import com.amkj.dmsh.qyservice.QyProductIndentInfo;
import com.amkj.dmsh.qyservice.QyServiceUtils;
import com.amkj.dmsh.release.dialogutils.AlertSettingBean;
import com.amkj.dmsh.release.dialogutils.AlertView;
import com.amkj.dmsh.release.dialogutils.OnAlertItemClickListener;
import com.amkj.dmsh.shopdetails.adapter.RefundTypeAdapter;
import com.amkj.dmsh.shopdetails.bean.DirectApplyRefundBean;
import com.amkj.dmsh.shopdetails.bean.DirectApplyRefundBean.DirectRefundProBean;
import com.amkj.dmsh.shopdetails.bean.RefundDetailEntity;
import com.amkj.dmsh.shopdetails.bean.RefundDetailEntity.RefundDetailBean;
import com.amkj.dmsh.shopdetails.bean.RefundDetailEntity.RefundDetailBean.RefundGoodsAddressBean;
import com.amkj.dmsh.shopdetails.bean.RefundDetailEntity.RefundDetailBean.RefundPayInfoBean;
import com.amkj.dmsh.shopdetails.bean.RefundLogisticEntity;
import com.amkj.dmsh.shopdetails.bean.RefundTypeBean;
import com.amkj.dmsh.utils.NetWorkUtils;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.amkj.dmsh.utils.inteface.MyCallBack;
import com.google.gson.Gson;
import com.klinker.android.link_builder.Link;
import com.klinker.android.link_builder.LinkBuilder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;;
import static com.amkj.dmsh.constant.ConstantMethod.getFloatNumber;
import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.getStringsChNPrice;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.INDENT_PRO_STATUS;
import static com.amkj.dmsh.constant.ConstantVariable.IS_LOGIN_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.REFUND_REPAIR;
import static com.amkj.dmsh.constant.ConstantVariable.REFUND_TYPE;
import static com.amkj.dmsh.constant.ConstantVariable.REGEX_NUM;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;

;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/8/18
 * class description:退款售后详情
 */
public class DoMoRefundDetailActivity extends BaseActivity implements OnAlertItemClickListener {
    @BindView(R.id.iv_indent_service)
    ImageView iv_indent_service;
    @BindView(R.id.tv_indent_title)
    TextView tv_indent_title;
    @BindView(R.id.iv_indent_search)
    ImageView iv_indent_search;
    @BindView(R.id.sv_layout_refund_detail)
    NestedScrollView sv_layout_refund_detail;
    //    退款状态
    @BindView(R.id.tv_refund_detail_status)
    TextView tv_refund_detail_status;
    //    退款信息
    @BindView(R.id.tv_refund_detail_msg)
    TextView tv_refund_detail_msg;
    //    物流信息填写
    @BindView(R.id.ll_refund_logistic)
    LinearLayout ll_refund_logistic;
    //    退货地址
    @BindView(R.id.tv_repair_address)
    TextView tv_refund_address;
    @BindView(R.id.tv_repair_consignee_phone)
    TextView tv_repair_consignee_phone;
    //    物流公司加*
    @BindView(R.id.tv_refund_logistic)
    TextView tv_refund_logistic;
    //    物流单号加*
    @BindView(R.id.tv_refund_logistic_no)
    TextView tv_refund_logistic_no;
    /**
     * 维修寄回运费
     */
    @BindView(R.id.rel_repair_logistic_fee)
    RelativeLayout rel_repair_logistic_fee;
    @BindView(R.id.tv_refund_logistic_fee)
    TextView tv_refund_logistic_fee;
    @BindView(R.id.et_refund_logistic_fee)
    EditText et_refund_logistic_fee;
    //    填写物流单号
    @BindView(R.id.et_refund_logistic_no)
    EditText et_refund_logistic_no;
    //    商品属性
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
    //    售后类型数据
    @BindView(R.id.rv_refund_type)
    RecyclerView rv_refund_type;
    //    底栏申请
    @BindView(R.id.ll_refund_bottom)
    LinearLayout ll_refund_bottom;
    //    第一按钮
    @BindView(R.id.tv_refund_first)
    TextView tv_refund_first;
    //    第二按钮
    @BindView(R.id.tv_refund_second)
    TextView tv_refund_second;
    //    提交按钮
    @BindView(R.id.tv_submit_apply_refund)
    TextView tv_submit_apply_refund;
    //    物流公司选择
    @BindView(R.id.ll_communal_sel_wheel)
    LinearLayout ll_communal_sel_wheel;
    @BindView(R.id.tv_refund_logistic_sel)
    TextView tv_refund_logistic_sel;
    @BindView(R.id.wv_communal_one)
    WheelView wv_communal_one;
    private String no;
    private String orderProductId;
    private String orderRefundProductId;
    private final int CANCEL_APPLY = 1;
    private final int EDIT_APPLY = 2;
    //   维修查看物流
    private final int CHECK_REPAIR_LOGISTIC = 3;
    //    维修确认收货
    private final int REPAIR_CONFIRM = 4;
    private AlertView cancelApplyDialog;
    private String express;
    private List<String> expressCompanies;
    private List<RefundTypeBean> refundTypeBeans = new ArrayList<>();
    private List<CartProductInfoBean> preComProInfoBeanList = new ArrayList();
    private final String LOGISTIC_C = "物流公司*";
    private final String LOGISTIC_N = "物流单号*";
    private final String LOGISTIC_F = "运费*";
    private final String[] refundTypes = {"服务类型：", "申诉原因：", "退款金额：", "申请时间：", "退款编号："};
    private final String[] refundRepairTypes = {"服务类型：", "问题描述：", "申请时间：", "物流公司：", "物流单号：", "运费：", "收货地址："};
    private ShopCarComPreProAdapter shopCarComPreProAdapter;
    private ConstantMethod constantMethod;
    private String refundType;
    private RefundTypeAdapter refundTypeAdapter;
    private RefundDetailBean refundDetailBean;
    private String repairAddress;
    private RefundDetailEntity refundDetailEntity;

    @Override
    protected int getContentView() {
        return R.layout.activity_domo_refund_detail;
    }

    @Override
    protected void initViews() {
        getLoginStatus(this);
        iv_indent_search.setVisibility(View.GONE);
        sv_layout_refund_detail.setVisibility(View.GONE);
        tv_refund_logistic.setText(LOGISTIC_C);
        tv_refund_logistic_no.setText(LOGISTIC_N);
        Link link = new Link("*");
        //        @用户昵称
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
        tv_indent_title.setText("退款详情");
        Intent intent = getIntent();
        refundType = intent.getStringExtra(REFUND_TYPE);
        no = intent.getStringExtra("no");
        orderProductId = intent.getStringExtra("orderProductId");
        orderRefundProductId = intent.getStringExtra("orderRefundProductId");
        rv_refund_type.setLayoutManager(new LinearLayoutManager(DoMoRefundDetailActivity.this));
        refundTypeAdapter = new RefundTypeAdapter(refundTypeBeans);
        rv_refund_type.setAdapter(refundTypeAdapter);
        rv_refund_type.setNestedScrollingEnabled(false);
        communal_recycler_wrap.setNestedScrollingEnabled(false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            finish();
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IS_LOGIN_CODE) {
            loadData();
        }
    }

    @Override
    protected void loadData() {
//        维修详情
        if (!TextUtils.isEmpty(refundType)
                && refundType.equals(REFUND_REPAIR)) {
            getRepairDetailData();
        } else {
//            退款退货售后详情
            getRefundDetailData();
        }
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    @Override
    protected View getLoadView() {
        return sv_layout_refund_detail;
    }

    private void getRepairDetailData() {
        String url = Url.BASE_URL + Url.Q_INDENT_REPAIR_DETAIL;
        Map<String, Object> params = new HashMap<>();
        params.put("orderRefundProductId", orderRefundProductId);
        params.put("orderProductId", orderProductId);
        params.put("userId", userId);
        params.put("no", no);
        NetLoadUtils.getQyInstance().loadNetDataPost(mAppContext, url, params, new NetLoadUtils.NetLoadListener() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                refundDetailEntity = gson.fromJson(result, RefundDetailEntity.class);
                if (refundDetailEntity != null) {
                    if (refundDetailEntity.getCode().equals(SUCCESS_CODE)) {
                        sv_layout_refund_detail.setVisibility(View.VISIBLE);
                        refundDetailBean = refundDetailEntity.getRefundDetailBean();
                        if (refundDetailEntity.getStatus() != null) {
                            INDENT_PRO_STATUS = refundDetailEntity.getStatus();
                        }
                        setRefundDetailData(refundDetailBean);
                    } else if (refundDetailEntity.getCode().equals(EMPTY_CODE)) {
                        showToast(DoMoRefundDetailActivity.this, R.string.invalidData);
                    } else {
                        showToast(DoMoRefundDetailActivity.this, refundDetailEntity.getMsg());
                    }
                }
                NetLoadUtils.getQyInstance().showLoadSir(loadService, refundDetailEntity);
            }

            @Override
            public void netClose() {
                showToast(DoMoRefundDetailActivity.this, R.string.unConnectedNetwork);
                NetLoadUtils.getQyInstance().showLoadSir(loadService, refundDetailEntity);
            }

            @Override
            public void onError(Throwable throwable) {
                showToast(DoMoRefundDetailActivity.this, R.string.invalidData);
                NetLoadUtils.getQyInstance().showLoadSir(loadService, refundDetailEntity);
            }
        });
    }

    private void getRefundDetailData() {
        String url = Url.BASE_URL + Url.Q_INDENT_REFUND_DETAIL;
        Map<String, Object> params = new HashMap<>();
        params.put("no", no);
        params.put("orderProductId", orderProductId);
        params.put("orderRefundProductId", orderRefundProductId);
        params.put("userId", userId);
        NetLoadUtils.getQyInstance().loadNetDataPost(mAppContext, url, params, new NetLoadUtils.NetLoadListener() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                refundDetailEntity = gson.fromJson(result, RefundDetailEntity.class);
                if (refundDetailEntity != null) {
                    if (refundDetailEntity.getCode().equals(SUCCESS_CODE)) {
                        sv_layout_refund_detail.setVisibility(View.VISIBLE);
                        refundDetailBean = refundDetailEntity.getRefundDetailBean();
                        refundDetailBean.setCurrentTime(refundDetailEntity.getCurrentTime());
                        setRefundDetailData(refundDetailBean);
                    } else if (refundDetailEntity.getCode().equals(EMPTY_CODE)) {
                        showToast(DoMoRefundDetailActivity.this, R.string.invalidData);
                    } else {
                        showToast(DoMoRefundDetailActivity.this, refundDetailEntity.getMsg());
                    }
                }
                NetLoadUtils.getQyInstance().showLoadSir(loadService, refundDetailEntity);
            }

            @Override
            public void netClose() {
                showToast(DoMoRefundDetailActivity.this, R.string.unConnectedNetwork);
                NetLoadUtils.getQyInstance().showLoadSir(loadService, refundDetailEntity);
            }

            @Override
            public void onError(Throwable throwable) {
                showToast(DoMoRefundDetailActivity.this, R.string.invalidData);
                NetLoadUtils.getQyInstance().showLoadSir(loadService, refundDetailEntity);
            }
        });
    }

    //    取消申请
    private void cancelApply() {
        if (NetWorkUtils.checkNet(DoMoRefundDetailActivity.this)) {
            String url;
            Map<String, Object> params = new HashMap<>();
            if (refundType.equals(REFUND_REPAIR)) {
//                维修撤销
                url = Url.BASE_URL + Url.Q_INDENT_REFUND_REPAIR_CANCEL;
                cancelRefund(url, params);
            } else if (REFUND_TYPE.equals(refundType)) {
//                退货退款撤销
                url = Url.BASE_URL + Url.Q_CANCEL_APPLY;
                cancelRefund(url, params);
            } else {
                Toast.makeText(this, R.string.refund_type_error, Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 取消售后操作
     *
     * @param url
     * @param params
     */
    private void cancelRefund(String url, Map<String, Object> params) {
        params.put("orderRefundProductId", refundDetailBean.getOrderRefundProductId());
        params.put("orderProductId", refundDetailBean.getOrderProductId());
        XUtil.Post(url, params, new MyCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                RequestStatus requestStatus = gson.fromJson(result, RequestStatus.class);
                if (requestStatus != null) {
                    if (requestStatus.getCode().equals("01")) {
                        showToast(DoMoRefundDetailActivity.this, String.format(getResources().getString(R.string.doSuccess), "撤销"));
                        finish();
                    } else if (requestStatus.getCode().equals("02")) {
                        showToast(DoMoRefundDetailActivity.this, R.string.invalidData);
                    } else {
                        showToast(DoMoRefundDetailActivity.this, requestStatus.getMsg());
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                showToast(DoMoRefundDetailActivity.this, R.string.invalidData);
                super.onError(ex, isOnCallback);
            }
        });
    }

    private void getLogisticCompany() {
        String url = Url.BASE_URL + Url.Q_INDENT_LOGISTIC_COM;
        if (NetWorkUtils.checkNet(DoMoRefundDetailActivity.this)) {
            XUtil.Get(url, null, new MyCallBack<String>() {
                @Override
                public void onSuccess(String result) {
                    Gson gson = new Gson();
                    RefundLogisticEntity refundLogisticEntity = gson.fromJson(result, RefundLogisticEntity.class);
                    if (refundLogisticEntity != null) {
                        if (refundLogisticEntity.getCode().equals("01")) {
                            expressCompanies = refundLogisticEntity.getExpressCompanys();
                            setRefundLogisticData(expressCompanies);
                        } else if (refundLogisticEntity.getCode().equals("02")) {
                            showToast(DoMoRefundDetailActivity.this, R.string.invalidData);
                        } else {
                            showToast(DoMoRefundDetailActivity.this, refundLogisticEntity.getMsg());
                        }
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    showToast(DoMoRefundDetailActivity.this, R.string.invalidData);
                    super.onError(ex, isOnCallback);
                }
            });
        }
    }

    private void setRefundLogisticData(final List<String> expressCompanys) {
        if (expressCompanys.size() > 0) {
            wv_communal_one.setViewAdapter(new ArrayWheelAdapter<>(DoMoRefundDetailActivity.this, expressCompanys.toArray()));
            wv_communal_one.setVisibleItems(7);
            wv_communal_one.setCurrentItem(0);
        }
    }

    /**
     * 售后信息填写
     *
     * @param refundDetailBean
     */
    private void setRefundDetailData(RefundDetailBean refundDetailBean) {
        tv_refund_detail_status.setText(getStrings(refundDetailBean.getStatusName()));
        if (refundDetailBean.getAutoUndoRefundGoodsTime() > 0) {
            setRefundTime(refundDetailBean);
        } else {
            if (constantMethod != null) {
                constantMethod.stopSchedule();
            }
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
        GlideImageLoaderUtil.loadCenterCrop(DoMoRefundDetailActivity.this, iv_direct_indent_pro, refundDetailBean.getPicUrl());
        tv_direct_indent_pro_name.setText(getStrings(refundDetailBean.getName()));
        String priceName;
        if (refundDetailBean.getIntegralPrice() > 0) {
            float moneyPrice = getFloatNumber(refundDetailBean.getPrice());
            if (moneyPrice > 0) {
                priceName = String.format(getResources().getString(R.string.integral_product_and_price)
                        , refundDetailBean.getIntegralPrice(), getStrings(refundDetailBean.getPrice()));
            } else {
                priceName = String.format(getResources().getString(R.string.integral_indent_product_price)
                        , refundDetailBean.getIntegralPrice());
            }
        } else {
            priceName = getStringsChNPrice(DoMoRefundDetailActivity.this, refundDetailBean.getPrice());
        }
        tv_direct_indent_pro_price.setText(priceName);
        tv_direct_indent_pro_sku.setText(getStrings(refundDetailBean.getSaleSkuValue()));
        tv_direct_pro_count.setText(("x" + refundDetailBean.getCount()));
        setRefundTypeData(refundDetailBean);
        tv_submit_apply_refund.setVisibility(View.GONE);
        if (REFUND_REPAIR.equals(refundType)) {
            switch (refundDetailBean.getStatus()) {
                case 50:
                    ll_refund_logistic.setVisibility(View.GONE);
                    ll_refund_bottom.setVisibility(View.VISIBLE);
                    tv_refund_first.setVisibility(View.VISIBLE);
                    tv_refund_first.setText("撤销申请");
                    tv_refund_first.setTag(R.id.tag_first, CANCEL_APPLY);
                    tv_refund_first.setTag(R.id.tag_second, refundDetailBean);
                    tv_refund_second.setVisibility(View.GONE);
                    break;
                case 51:
                    ll_refund_bottom.setVisibility(View.VISIBLE);
                    tv_refund_first.setVisibility(View.VISIBLE);
                    tv_refund_first.setText("撤销申请");
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
                    tv_refund_first.setText("查看物流");
                    tv_refund_first.setTag(R.id.tag_first, CHECK_REPAIR_LOGISTIC);
                    tv_refund_first.setTag(R.id.tag_second, refundDetailBean);
                    tv_refund_second.setText("确认收货");
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
//            退款处理中
                if (refundDetailBean.getStatus() == -10 || refundDetailBean.getStatus() == -30) {
                    if (refundDetailBean.getChildOrderStatus() == 30 || refundDetailBean.getChildOrderStatus() == 31
                            || refundDetailBean.getChildOrderStatus() == 40) {
                        tv_refund_first.setVisibility(View.VISIBLE);
                        tv_refund_second.setVisibility(View.GONE);
                        tv_refund_first.setText("撤销申请");
                        tv_refund_first.setTag(R.id.tag_first, CANCEL_APPLY);
                        tv_refund_first.setTag(R.id.tag_second, refundDetailBean);
                    } else {
                        ll_refund_bottom.setVisibility(View.GONE);
                    }
                } else if (refundDetailBean.getStatus() == -31) { // 申请被驳回
                    if (refundDetailBean.getChildOrderStatus() == 30 || refundDetailBean.getChildOrderStatus() == 31
                            || refundDetailBean.getChildOrderStatus() == 40) {
                        tv_refund_first.setVisibility(View.VISIBLE);
                        tv_refund_second.setVisibility(View.VISIBLE);
                        tv_refund_first.setText("撤销申请");
                        tv_refund_first.setTag(R.id.tag_first, CANCEL_APPLY);
                        tv_refund_first.setTag(R.id.tag_second, refundDetailBean);
                        tv_refund_second.setText("修改申请");
                        tv_refund_second.setTag(R.id.tag_first, EDIT_APPLY);
                        tv_refund_second.setTag(R.id.tag_second, refundDetailBean);
                    } else {
                        tv_refund_first.setVisibility(View.VISIBLE);
                        tv_refund_second.setVisibility(View.GONE);
                        tv_refund_first.setText("修改申请");
                        tv_refund_first.setTag(R.id.tag_first, EDIT_APPLY);
                        tv_refund_first.setTag(R.id.tag_second, refundDetailBean);
                    }
                } else {
                    ll_refund_bottom.setVisibility(View.GONE);
                }
            } else {
                ll_refund_bottom.setVisibility(View.GONE);
//            退货申请通过
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
                communal_recycler_wrap.setLayoutManager(new LinearLayoutManager(DoMoRefundDetailActivity.this));
                shopCarComPreProAdapter = new ShopCarComPreProAdapter(DoMoRefundDetailActivity.this, preComProInfoBeanList);
                communal_recycler_wrap.setAdapter(shopCarComPreProAdapter);
            }
            shopCarComPreProAdapter.notifyDataSetChanged();
        } else {
            rel_indent_com_pre_pro.setVisibility(View.GONE);
        }
    }

    /**
     * 退款退货 售后详细数据
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
                    refundTypeBeans.add(new RefundTypeBean(refundRepairTypes[5], "￥" + getStrings(refundDetailBean.getRepairExpressFee())));
                }
                refundTypeBeans.add(new RefundTypeBean(refundRepairTypes[6],
                        getStrings(refundDetailBean.getRepairReceiveReceiver()) + "\t" + getStrings(refundDetailBean.getRepairReceivePhone()) + "\n" + getStrings(refundDetailBean.getRepairReceiveAddress())));
                break;
            default:
                refundTypeBeans.add(new RefundTypeBean(refundTypes[0], getStrings(refundDetailBean.getRefundType())));
                refundTypeBeans.add(new RefundTypeBean(refundTypes[1], getStrings(refundDetailBean.getReason())));
                String priceName;
                if (refundDetailBean.getRefundIntegralPrice() > 0) {
                    float moneyPrice = getFloatNumber(refundDetailBean.getRefundPrice());
                    if (moneyPrice > 0) {
                        priceName = String.format(getResources().getString(R.string.integral_product_and_price)
                                , refundDetailBean.getRefundIntegralPrice(), getStrings(refundDetailBean.getRefundPrice()));
                    } else {
                        priceName = String.format(getResources().getString(R.string.integral_indent_product_price)
                                , refundDetailBean.getRefundIntegralPrice());
                    }
                } else {
                    priceName = getStringsChNPrice(DoMoRefundDetailActivity.this, refundDetailBean.getRefundPrice());
                }
                refundTypeBeans.add(new RefundTypeBean(refundTypes[2], priceName));
                refundTypeBeans.add(new RefundTypeBean(refundTypes[3], getStrings(refundDetailBean.getCreateTime())));
                refundTypeBeans.add(new RefundTypeBean(refundTypes[4], getStrings(refundDetailBean.getRefundNo())));
                break;
        }
        refundTypeAdapter.notifyDataSetChanged();
    }

    /**
     * 设置退款倒计时
     *
     * @param refundDetailBean
     */
    private void setRefundTime(final RefundDetailBean refundDetailBean) {
        //            创建时间加倒计时间 大于等于当前时间 展示倒计时
        if (ConstantMethod.isEndOrStartTimeAddSeconds(refundDetailBean.getUpdateTime()
                , refundDetailBean.getCurrentTime()
                , refundDetailBean.getAutoUndoRefundGoodsTime())) {
            setReFundTimeDown(refundDetailBean);
            if (constantMethod == null) {
                constantMethod = new ConstantMethod();
            }
            constantMethod.createSchedule();
            constantMethod.setRefreshTimeListener(new ConstantMethod.RefreshTimeListener() {
                @Override
                public void refreshTime() {
                    setReFundTimeDown(refundDetailBean);
                }
            });
        } else {
            if (constantMethod != null) {
                constantMethod.stopSchedule();
            }
            tv_refund_detail_msg.setText(getStrings(refundDetailBean.getNoticeMsg()));
        }
    }

    /**
     * 更新退款倒计时
     *
     * @param refundDetailBean
     */
    private void setReFundTimeDown(RefundDetailBean refundDetailBean) {
        try {
            refundDetailBean.setAutoUndoRefundGoodsTime(refundDetailBean.getAutoUndoRefundGoodsTime() - 1);
//            格式化结束时间
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
            Date dateCreate = formatter.parse(refundDetailBean.getUpdateTime());
            long overTime = refundDetailBean.getAutoUndoRefundGoodsTime() * 1000;
            Date dateCurrent;
            if (!TextUtils.isEmpty(refundDetailBean.getCurrentTime())) {
                dateCurrent = formatter.parse(refundDetailBean.getCurrentTime());
            } else {
                dateCurrent = new Date();
            }
            long ms = dateCreate.getTime() + overTime - dateCurrent.getTime();
            if (ms >= 0) {
                int day = (int) (ms / (1000 * 60 * 60 * 24));
                int hour = (int) ((ms % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
                int minute = (int) ((ms % (1000 * 60 * 60)) / (1000 * 60));
                int second = (int) ((ms % (1000 * 60)) / 1000);
                String timeMsg = (getStrings(refundDetailBean.getNoticeMsg())
                        + String.format(getString(R.string.refund_time), day, hour, minute, second));
                Link link = new Link(Pattern.compile(REGEX_NUM));
                link.setTextColor(0xffff5e6b);
                link.setUnderlined(false);
                link.setHighlightAlpha(0f);
                tv_refund_detail_msg.setText(LinkBuilder.from(DoMoRefundDetailActivity.this, timeMsg)
                        .addLink(link)
                        .build());
            } else {
                getRefundDetailData();
            }
        } catch (Exception e) {
            tv_refund_detail_msg.setText(getStrings(refundDetailBean.getNoticeMsg()));
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

    //    提交物流信息
    @OnClick(R.id.tv_submit_apply_refund)
    void submitInfo(View view) {
        String logistic = tv_refund_logistic_sel.getText().toString().trim();
        String logisticNo = et_refund_logistic_no.getText().toString().trim();
        if (REFUND_TYPE.equals(refundType)) {
            if (!TextUtils.isEmpty(logistic) && !TextUtils.isEmpty(logisticNo)) {
                submitLogisticInfo(logistic, logisticNo);
            } else if (TextUtils.isEmpty(logistic)) {
                showToast(this, "请选择物流公司");
            } else if (TextUtils.isEmpty(logisticNo)) {
                showToast(this, "请输入物流单号");
            }
        } else if (REFUND_REPAIR.equals(refundType)) {
            String logisticFee = et_refund_logistic_fee.getText().toString().trim();
            if (!TextUtils.isEmpty(logistic)
                    && !TextUtils.isEmpty(logisticNo)) {
                submitRepairLogisticInfo(logistic, logisticNo, logisticFee);
            } else if (TextUtils.isEmpty(logistic)) {
                showToast(this, "请选择物流公司");
            } else if (TextUtils.isEmpty(logisticNo)) {
                showToast(this, "请输入物流单号");
            }
        } else {
            Toast.makeText(this, "服务类型异常，请更新app最新版本", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 提交维修物流信息
     *
     * @param logistic
     * @param logisticNo
     * @param logisticFee
     */
    private void submitRepairLogisticInfo(String logistic, String logisticNo, String logisticFee) {
        String url = Url.BASE_URL + Url.Q_INDENT_REPAIR_LOGISTIC_SUB;
        if (NetWorkUtils.checkNet(DoMoRefundDetailActivity.this)) {
            Map<String, Object> params = new HashMap<>();
            params.put("no", no);
            params.put("orderProductId", orderProductId);
            params.put("orderRefundProductId", orderRefundProductId);
            params.put("expressCompany", logistic);
            params.put("refundNo", refundDetailBean.getRefundNo());
            params.put("expressNo", logisticNo);
            params.put("expressFee", TextUtils.isEmpty(logisticFee) ? "0" : logisticFee);
            XUtil.Post(url, params, new MyCallBack<String>() {
                @Override
                public void onSuccess(String result) {
                    Gson gson = new Gson();
                    RequestStatus requestStatus = gson.fromJson(result, RequestStatus.class);
                    if (requestStatus != null) {
                        if (requestStatus.getCode().equals("01")) {
                            showToast(DoMoRefundDetailActivity.this, String.format(getResources().getString(R.string.doSuccess), "提交"));
                            loadData();
                        } else if (requestStatus.getCode().equals("02")) {
                            showToast(DoMoRefundDetailActivity.this, R.string.invalidData);
                        } else {
                            showToast(DoMoRefundDetailActivity.this, requestStatus.getMsg());
                        }
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    showToast(DoMoRefundDetailActivity.this, R.string.invalidData);
                    super.onError(ex, isOnCallback);
                }
            });
        }
    }

    /**
     * 提交退货物流信息
     *
     * @param logistic
     * @param logisticNo
     */
    private void submitLogisticInfo(String logistic, String logisticNo) {
        String url = Url.BASE_URL + Url.Q_INDENT_LOGISTIC_SUB;
        if (NetWorkUtils.checkNet(DoMoRefundDetailActivity.this)) {
            Map<String, Object> params = new HashMap<>();
            params.put("no", no);
            params.put("orderProductId", orderProductId);
            params.put("orderRefundProductId", orderRefundProductId);
            params.put("userId", userId);
            params.put("expressCompany", logistic);
            params.put("expressNo", logisticNo);
            XUtil.Post(url, params, new MyCallBack<String>() {
                @Override
                public void onSuccess(String result) {
                    Gson gson = new Gson();
                    RequestStatus requestStatus = gson.fromJson(result, RequestStatus.class);
                    if (requestStatus != null) {
                        if (requestStatus.getCode().equals("01")) {
                            showToast(DoMoRefundDetailActivity.this, String.format(getResources().getString(R.string.doSuccess), "提交"));
                            loadData();
                        } else if (requestStatus.getCode().equals("02")) {
                            showToast(DoMoRefundDetailActivity.this, R.string.invalidData);
                        } else {
                            showToast(DoMoRefundDetailActivity.this, requestStatus.getMsg());
                        }
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    showToast(DoMoRefundDetailActivity.this, R.string.invalidData);
                    super.onError(ex, isOnCallback);
                }
            });
        }
    }

    /**
     * 确认维修商品已收货
     */
    private void confirmRepairReceive() {
        String url = Url.BASE_URL + Url.Q_INDENT_REPAIR_RECEIVE;
        if (NetWorkUtils.checkNet(DoMoRefundDetailActivity.this)) {
            Map<String, Object> params = new HashMap<>();
            params.put("orderNo", no);
            params.put("orderProductId", orderProductId);
            params.put("orderRefundProductId", orderRefundProductId);
            XUtil.Post(url, params, new MyCallBack<String>() {
                @Override
                public void onSuccess(String result) {
                    Gson gson = new Gson();
                    RequestStatus requestStatus = gson.fromJson(result, RequestStatus.class);
                    if (requestStatus != null) {
                        if (requestStatus.getCode().equals("01")) {
                            loadData();
                        } else if (requestStatus.getCode().equals("02")) {
                            showToast(DoMoRefundDetailActivity.this, R.string.invalidData);
                        } else {
                            showToast(DoMoRefundDetailActivity.this, requestStatus.getMsg());
                        }
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    showToast(DoMoRefundDetailActivity.this, R.string.invalidData);
                    super.onError(ex, isOnCallback);
                }
            });
        }
    }

    //    选取物流公司
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

    //    复制收货地址
    @OnClick(R.id.tv_copy_text)
    void copyNo(View view) {
        if (!TextUtils.isEmpty(repairAddress)) {
            ClipboardManager cmb = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData mClipData = ClipData.newPlainText("Label", repairAddress);
            cmb.setPrimaryClip(mClipData);
            showToast(DoMoRefundDetailActivity.this, "已复制");
        }
    }

    @OnClick({R.id.tv_refund_first, R.id.tv_refund_second})
    void setRefundDo(View view) {
        int doType = (int) view.getTag(R.id.tag_first);
        refundDetailBean = (RefundDetailBean) view.getTag(R.id.tag_second);
        switch (doType) {
            case CANCEL_APPLY:
                AlertSettingBean alertSettingBean = new AlertSettingBean();
                AlertSettingBean.AlertData alertData = new AlertSettingBean.AlertData();
                alertData.setCancelStr("取消");
                alertData.setDetermineStr("确定");
                alertData.setFirstDet(false);
                alertData.setMsg(getResources().getString(R.string.cancel_invite));
                alertSettingBean.setStyle(AlertView.Style.Alert);
                alertSettingBean.setAlertData(alertData);
                cancelApplyDialog = new AlertView(alertSettingBean, DoMoRefundDetailActivity.this, DoMoRefundDetailActivity.this);
                cancelApplyDialog.setCancelable(true);
                cancelApplyDialog.show();
                break;
            case EDIT_APPLY:
                Intent intent = new Intent(DoMoRefundDetailActivity.this, DirectApplyRefundActivity.class);
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
                intent.putExtra("cancelRefund", true);
                intent.putExtra(REFUND_TYPE, REFUND_TYPE);
                startActivity(intent);
                finish();
                break;
            case CHECK_REPAIR_LOGISTIC:
                intent = new Intent(DoMoRefundDetailActivity.this, DirectRepairLogisticDetailsActivity.class);
                intent.putExtra("orderProductId", String.valueOf(refundDetailBean.getOrderProductId()));
                intent.putExtra("orderRefundProductId", String.valueOf(refundDetailBean.getOrderRefundProductId()));
                startActivity(intent);
                break;
            case REPAIR_CONFIRM:
                confirmRepairReceive();
                break;
        }
    }


    @Override
    public void onAlertItemClick(Object o, int position) {
        if (o.equals(cancelApplyDialog) && position != AlertView.CANCELPOSITION) {
            cancelApply();
        }
    }

    @OnClick(R.id.tv_indent_back)
    void goBack(View view) {
        finish();
    }

    @Override
    protected void onDestroy() {
        if (constantMethod != null) {
            constantMethod.stopSchedule();
            constantMethod.releaseHandlers();
        }
        super.onDestroy();
    }

    @OnClick(R.id.iv_indent_service)
    void skipService() {
        QyProductIndentInfo qyProductIndentInfo = null;
        if (refundDetailBean != null) {
            qyProductIndentInfo = new QyProductIndentInfo();
            qyProductIndentInfo.setTitle(refundDetailBean.getName());
            qyProductIndentInfo.setPicUrl(getStrings(refundDetailBean.getPicUrl()));
            qyProductIndentInfo.setDesc(INDENT_PRO_STATUS.get(String.valueOf(refundDetailBean.getStatus())));
            qyProductIndentInfo.setNote(String.format(getResources().getString(R.string.money_price_chn),refundDetailBean.getPrice()));
            qyProductIndentInfo.setUrl(Url.BASE_SHARE_PAGE_TWO + "m/template/order_template/order.html?noid=" + refundDetailBean.getNo());
        }
        QyServiceUtils.getQyInstance().openQyServiceChat(this, "退款售后详情", "", qyProductIndentInfo);
    }
}
