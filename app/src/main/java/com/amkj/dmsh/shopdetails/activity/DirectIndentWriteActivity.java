package com.amkj.dmsh.shopdetails.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.address.activity.SelectedAddressActivity;
import com.amkj.dmsh.address.bean.AddressInfoEntity;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.NetLoadUtils;
import com.amkj.dmsh.base.TinkerBaseApplicationLike;
import com.amkj.dmsh.bean.RequestStatus;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.constant.XUtil;
import com.amkj.dmsh.dominant.activity.DoMoGroupJoinShareActivity;
import com.amkj.dmsh.dominant.activity.QualityGroupShopMineActivity;
import com.amkj.dmsh.dominant.activity.QualityProductActActivity;
import com.amkj.dmsh.dominant.bean.GroupShopDetailsEntity.GroupShopDetailsBean;
import com.amkj.dmsh.homepage.adapter.CommunalDetailAdapter;
import com.amkj.dmsh.mine.bean.ShopCarNewInfoEntity.ShopCarNewInfoBean.CartInfoBean;
import com.amkj.dmsh.mine.bean.ShopCarNewInfoEntity.ShopCarNewInfoBean.CartInfoBean.CartProductInfoBean;
import com.amkj.dmsh.mine.bean.ShopCarNewInfoEntity.ShopCarNewInfoBean.CartInfoBean.SaleSkuBean;
import com.amkj.dmsh.release.dialogutils.AlertSettingBean;
import com.amkj.dmsh.release.dialogutils.AlertView;
import com.amkj.dmsh.release.dialogutils.OnAlertItemClickListener;
import com.amkj.dmsh.shopdetails.adapter.DirectProductListAdapter;
import com.amkj.dmsh.shopdetails.adapter.IndentDiscountAdapter;
import com.amkj.dmsh.shopdetails.alipay.AliPay;
import com.amkj.dmsh.shopdetails.bean.CommunalDetailObjectBean;
import com.amkj.dmsh.shopdetails.bean.DirectCouponEntity.DirectCouponBean;
import com.amkj.dmsh.shopdetails.bean.DirectReBuyGoods;
import com.amkj.dmsh.shopdetails.bean.DirectReBuyGoods.ReBuyGoodsBean;
import com.amkj.dmsh.shopdetails.bean.IndentDiscountsEntity;
import com.amkj.dmsh.shopdetails.bean.IndentDiscountsEntity.IndentDiscountsBean;
import com.amkj.dmsh.shopdetails.bean.IndentDiscountsEntity.IndentDiscountsBean.PriceInfoBean;
import com.amkj.dmsh.shopdetails.bean.IndentDiscountsEntity.IndentDiscountsBean.ProductInfoBean;
import com.amkj.dmsh.shopdetails.bean.IndentDiscountsEntity.IndentDiscountsBean.ProductInfoBean.ActivityInfoBean;
import com.amkj.dmsh.shopdetails.bean.IndentDiscountsEntity.IndentDiscountsBean.ProductInfoBean.ActivityProductInfoBean;
import com.amkj.dmsh.shopdetails.bean.IndentProDiscountBean;
import com.amkj.dmsh.shopdetails.bean.QualityCreateAliPayIndentBean;
import com.amkj.dmsh.shopdetails.bean.QualityCreateWeChatPayIndentBean;
import com.amkj.dmsh.shopdetails.bean.QualityCreateWeChatPayIndentBean.ResultBean.PayKeyBean;
import com.amkj.dmsh.shopdetails.bean.ShopCarGoodsSkuTransmit;
import com.amkj.dmsh.shopdetails.weixin.WXPay;
import com.amkj.dmsh.utils.inteface.MyCallBack;
import com.amkj.dmsh.views.RectAddAndSubViewDirect;
import com.google.gson.Gson;
import com.klinker.android.link_builder.Link;
import com.klinker.android.link_builder.LinkBuilder;
import com.oushangfeng.pinnedsectionitemdecoration.PinnedHeaderItemDecoration;
import com.tencent.bugly.beta.tinker.TinkerManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.view.View.VISIBLE;
import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.createExecutor;
import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.getStringFilter;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.isEndOrStartTimeAddSeconds;
import static com.amkj.dmsh.constant.ConstantMethod.setEtFilter;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.INDENT_PRODUCT_TYPE;
import static com.amkj.dmsh.constant.ConstantVariable.INDENT_PROPRIETOR_PRODUCT;
import static com.amkj.dmsh.constant.ConstantVariable.PAY_ALI_PAY;
import static com.amkj.dmsh.constant.ConstantVariable.PAY_WX_PAY;
import static com.amkj.dmsh.constant.ConstantVariable.REGEX_NUM;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.isUpTotalFile;
import static com.amkj.dmsh.shopdetails.activity.DirectExchangeDetailsActivity.INDENT_DETAILS_TYPE;

;
;

/**
 * Created by atd48 on 2016/8/17.
 * 订单填写
 */
public class DirectIndentWriteActivity extends BaseActivity implements OnAlertItemClickListener {
    @BindView(R.id.tv_header_title)
    TextView tv_header_titleAll;
    @BindView(R.id.tv_header_shared)
    TextView header_shared;
    @BindView(R.id.communal_recycler)
    RecyclerView communal_recycler;
    @BindView(R.id.ll_indent_details)
    LinearLayout ll_indent_details;
    //结算
    @BindView(R.id.tv_indent_write_commit)
    TextView tv_indent_write_commit;
    //    商品结算金额
    @BindView(R.id.tv_indent_total_price)
    TextView tv_indent_total_price;
    private boolean isFirst = true;
    private int addressId;
    private String type = "";
    private PullHeaderView pullHeaderView;
    private PullFootView pullFootView;
    private final int NEW_CRE_ADDRESS_REQ = 101;
    private final int SEL_ADDRESS_REQ = 102;
    //    订单数据
    private List<CartInfoBean> passGoods = new ArrayList<>();
    private final int DIRECT_COUPON_REQ = 105;
    private QualityCreateWeChatPayIndentBean qualityWeChatIndent;
    private QualityCreateAliPayIndentBean qualityAliPayIndent;
    private String payWay = "";
    //    创建订单 未结算
    private String orderCreateNo = "";
    //     完成支付订单编号
    private String orderNo = "";

    private DirectProductListAdapter directProductAdapter;
    private DirectCouponBean directSelfCouponBean;
    //    优惠券id
    private int couponId;
    private final int REQ_INVOICE = 130;
    private String invoiceTitle;
    private GroupShopDetailsBean groupShopDetailsBean;
    private List<GroupShopDetailsBean> groupShopDetailsBeanList = new ArrayList<>();
    public final static String INDENT_GROUP_SHOP = "group_shop";
    public final static String INDENT_W_TYPE = "indent";
    //    订单优惠信息必传参数
    private List<IndentProDiscountBean> discountBeanList = new ArrayList<>();
    //    订单价格优惠列表
    private List<PriceInfoBean> priceInfoList = new ArrayList<>();
    private String invoiceNum;
    private IndentDiscountAdapter indentDiscountAdapter;
    private IndentDiscountsEntity indentDiscountsEntity;
    private List<ActivityProductInfoBean> productInfoList = new ArrayList<>();
    private AlertDialog alertDialog;
    private RuleDialogView ruleDialog;
    private boolean isOversea = false;
    private AlertView payErrorAlert;
    private AlertView payCancelAlert;
    private Date createIndentTime;
    private ConstantMethod constantMethod;
    private Date current;
    private TextView tvAlertMsg;
    private CharSequence payErrorMsg;
    private IndentDiscountsBean indentDiscountsBean;

    @Override
    protected int getContentView() {
        return R.layout.activity_direct_indent_write;
    }

    @Override
    protected void initViews() {
        getLoginStatus(this);
        isOversea = false;
        constantMethod = new ConstantMethod();
        tv_header_titleAll.setText("订单填写");
        header_shared.setVisibility(View.INVISIBLE);
        Intent intent = getIntent();
        passGoods = intent.getParcelableArrayListExtra("productDate");
        groupShopDetailsBean = intent.getParcelableExtra("gpShopInfo");
        orderNo = intent.getStringExtra("orderNo");
//        地址栏
        View headerView = LayoutInflater.from(this).inflate(R.layout.layout_direct_indent_write_header_address, (ViewGroup) communal_recycler.getParent(), false);
        pullHeaderView = new PullHeaderView();
        ButterKnife.bind(pullHeaderView, headerView);
        setEtFilter(pullHeaderView.et_oversea_name);
        setEtFilter(pullHeaderView.et_oversea_card);
//        订单详情
        View footView = LayoutInflater.from(this).inflate(R.layout.layout_direct_indent_write_foot, (ViewGroup) communal_recycler.getParent(), false);
        pullFootView = new PullFootView();
        ButterKnife.bind(pullFootView, footView);
        if (passGoods != null || orderNo != null) {
            if (passGoods != null) {
                discountBeanList.clear();
                for (int i = 0; i < passGoods.size(); i++) {
                    CartInfoBean cartInfoBean = passGoods.get(i);
//                    优惠信息参数
                    IndentProDiscountBean indentProBean = new IndentProDiscountBean();
                    indentProBean.setId(cartInfoBean.getProductId());
                    indentProBean.setSaleSkuId(cartInfoBean.getSaleSku().getId());
                    indentProBean.setCount(cartInfoBean.getCount());
                    indentProBean.setCartId(cartInfoBean.getCartId());
                    discountBeanList.add(indentProBean);
                }
            }
            type = INDENT_W_TYPE;
            directProductAdapter = new DirectProductListAdapter(DirectIndentWriteActivity.this, productInfoList, type);
        } else if (groupShopDetailsBean != null) {
            type = INDENT_GROUP_SHOP;
            groupShopDetailsBeanList.clear();
            groupShopDetailsBeanList.add(groupShopDetailsBean);
            discountBeanList.clear();
            for (int i = 0; i < groupShopDetailsBeanList.size(); i++) {
                IndentProDiscountBean indentProBean = new IndentProDiscountBean();
                indentProBean.setId(groupShopDetailsBean.getProductId());
                indentProBean.setSaleSkuId(groupShopDetailsBean.getGpSkuId());
                indentProBean.setCount(1);
                discountBeanList.add(indentProBean);
            }
            directProductAdapter = new DirectProductListAdapter(DirectIndentWriteActivity.this, groupShopDetailsBeanList, type);
        }
        pullFootView.rect_indent_number.setFontColor(Color.parseColor("#333333"));
        communal_recycler.setLayoutManager(new LinearLayoutManager(DirectIndentWriteActivity.this));
        communal_recycler.addItemDecoration(new PinnedHeaderItemDecoration.Builder(-1)
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_gray_f_two_px)
                // 开启绘制分隔线，默认关闭
                .enableDivider(true)
                // 是否关闭标签点击事件，默认开启
                .disableHeaderClick(false)
                // 设置标签和其内部的子控件的监听，若设置点击监听不为null，但是disableHeaderClick(true)的话，还是不会响应点击事件
                .setHeaderClickListener(null)
                .create());
        directProductAdapter.addHeaderView(headerView);
        directProductAdapter.addFooterView(footView);
        communal_recycler.setAdapter(directProductAdapter);
        directProductAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            switch (view.getId()) {
                case R.id.ll_communal_activity_topic_tag:
                    ActivityProductInfoBean productInfoBean = (ActivityProductInfoBean) view.getTag();
                    setDialogRule(productInfoBean);
                    break;
                case R.id.iv_indent_product_del:
                    productInfoBean = (ActivityProductInfoBean) view.getTag();
                    for (IndentProDiscountBean discountBean : discountBeanList) {
                        if (productInfoBean.getId() == discountBean.getId()) {
                            discountBeanList.remove(discountBean);
                            if (discountBeanList.size() > 0) {
                                getIndentDiscounts(false);
                            } else {
                                finish();
                            }
                            break;
                        }
                    }
                    break;
            }
        });

        pullFootView.rect_indent_number.setOnNumChangeListener(new RectAddAndSubViewDirect.OnNumChangeListener() {
            @Override
            public void onNumChange(View view, int stype, int num) {
                if (discountBeanList.size() > 0 && discountBeanList.get(0).getCount() != num) {
                    discountBeanList.get(0).setCount(num);
                    getIndentDiscounts(false);
                }
            }

            @Override
            public void onMaxQuantity(View view, int num) {
                constantMethod.showImportantToast(DirectIndentWriteActivity.this, R.string.product_sell_out);
            }
        });
        pullFootView.rv_indent_write_info.setLayoutManager(new LinearLayoutManager(DirectIndentWriteActivity.this));
        indentDiscountAdapter = new IndentDiscountAdapter(priceInfoList);
        pullFootView.rv_indent_write_info.setAdapter(indentDiscountAdapter);
    }

    //  再次购买，获取商品信息
    private void getOrderData() {
        passGoods = new ArrayList<>();
        String url = Url.BASE_URL + Url.Q_RE_BUY_INDENT;
        Map<String, Object> params = new HashMap<>();
        params.put("no", orderNo);
        XUtil.Post(url, params, new MyCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                if (productInfoList != null) {
                    productInfoList.clear();
                }
                discountBeanList.clear();
                Gson gson = new Gson();
                DirectReBuyGoods directReBuyGoods = gson.fromJson(result, DirectReBuyGoods.class);
                if (directReBuyGoods != null) {
                    if (directReBuyGoods.getCode().equals(SUCCESS_CODE)) {
                        List<ReBuyGoodsBean> reBuyGoodsBeanList = directReBuyGoods.getReBuyGoodsBean();
                        for (int i = 0; i < reBuyGoodsBeanList.size(); i++) {
                            ReBuyGoodsBean reBuyGoodsBean = reBuyGoodsBeanList.get(i);
                            IndentProDiscountBean indentProBean = new IndentProDiscountBean();
                            indentProBean.setId(reBuyGoodsBean.getId());
                            indentProBean.setCount(reBuyGoodsBean.getCount());
                            indentProBean.setSaleSkuId(reBuyGoodsBean.getSaleSkuId());
                            discountBeanList.add(indentProBean);
                        }
                        loadHud.show();
                        getIndentDiscounts(false);
                    } else if (directReBuyGoods.getCode().equals(EMPTY_CODE)) {
                        constantMethod.showImportantToast(DirectIndentWriteActivity.this, R.string.invalidData);
                    } else {
                        constantMethod.showImportantToast(DirectIndentWriteActivity.this, directReBuyGoods.getMsg());
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                constantMethod.showImportantToast(DirectIndentWriteActivity.this, R.string.invalidData);
                super.onError(ex, isOnCallback);
            }
        });
    }

    @OnClick(R.id.tv_indent_write_commit)
    void goExchange(View view) {
        if (type.equals(INDENT_DETAILS_TYPE)) {
            tv_indent_write_commit.setEnabled(false);
            if (pullFootView.rb_checked_weChat_pay.isChecked() && !pullFootView.rb_checked_aliPay.isChecked()) {
                payWay = PAY_WX_PAY;
//                调起微信支付
                paymentIndent();
            } else if (pullFootView.rb_checked_aliPay.isChecked() && !pullFootView.rb_checked_weChat_pay.isChecked()) {
//                调起支付宝支付
                payWay = PAY_ALI_PAY;
                paymentIndent();
            }
        } else if (type.equals(INDENT_W_TYPE)) {
            if (addressId != 0 && productInfoList.size() > 0) {
                tv_indent_write_commit.setEnabled(false);
                if (pullFootView.rb_checked_weChat_pay.isChecked() && !pullFootView.rb_checked_aliPay.isChecked()) {
                    //创建订单
                    payWay = PAY_WX_PAY;
//                调起微信支付
                    if (!TextUtils.isEmpty(orderCreateNo)) {
                        paymentIndent();
                    } else {
                        setCreateIndent();
                    }
                } else if (pullFootView.rb_checked_aliPay.isChecked() && !pullFootView.rb_checked_weChat_pay.isChecked()) {
//                调起支付宝支付
                    payWay = PAY_ALI_PAY;
                    if (!TextUtils.isEmpty(orderCreateNo)) {
                        paymentIndent();
                    } else {
                        setCreateIndent();
                    }
                }
            } else if (addressId == 0) {
                constantMethod.showImportantToast(this, "收货地址为空");
            } else {
                constantMethod.showImportantToast(this, "商品选择错误");
            }
        } else if (type.equals(INDENT_GROUP_SHOP) && groupShopDetailsBean != null) {
            if (userId > 0) {
                if (addressId != 0) {
                    tv_indent_write_commit.setEnabled(false);
                    if (pullFootView.rb_checked_weChat_pay.isChecked() && !pullFootView.rb_checked_aliPay.isChecked()) {
                        //创建订单
                        payWay = PAY_WX_PAY;
//                调起微信支付
                        if (!TextUtils.isEmpty(orderCreateNo)) {
                            paymentIndent();
                        } else {
                            createGroupIndent(payWay, groupShopDetailsBean);
                        }
                    } else if (pullFootView.rb_checked_aliPay.isChecked() && !pullFootView.rb_checked_weChat_pay.isChecked()) {
//                调起支付宝支付
                        payWay = PAY_ALI_PAY;
                        if (!TextUtils.isEmpty(orderCreateNo)) {
                            paymentIndent();
                        } else {
                            createGroupIndent(payWay, groupShopDetailsBean);
                        }
                    }
                } else {
                    constantMethod.showImportantToast(this, "收货地址为空");
                }
            } else {
                getLoginStatus(this);
            }
        }
    }

    private void setCreateIndent() {
        if (isOversea) {
            if (pullHeaderView.et_oversea_name.getText().toString().length() > 0
                    && pullHeaderView.et_oversea_card.getText().toString().length() > 0) {
                createIndent(payWay, productInfoList);
            } else if (TextUtils.isEmpty(pullHeaderView.et_oversea_name.getText().toString())) {
                constantMethod.showImportantToast(this, R.string.input_name);
            } else if (TextUtils.isEmpty(pullHeaderView.et_oversea_card.getText().toString())) {
                constantMethod.showImportantToast(this, R.string.input_card);
            }
        } else {
            createIndent(payWay, productInfoList);
        }
    }

    private void createGroupIndent(final String payWay, GroupShopDetailsBean groupShopDetailsBean) {
        String message = pullFootView.edt_direct_product_note.getText().toString().trim();
        String url = Url.BASE_URL + Url.Q_CREATE_GROUP_INDENT;
        Map<String, Object> params = new HashMap<>();
        //用户ID
        params.put("userId", userId);
        //用户地址
        params.put("userAddressId", addressId);
//        拼团订单状态 开团 1 拼团 2
        params.put("gpStatus", groupShopDetailsBean.getGpStatus());
        params.put("gpProductId", groupShopDetailsBean.getGpProductId());
        params.put("gpInfoId", groupShopDetailsBean.getGpInfoId());
        if (groupShopDetailsBean.getGpStatus() == 2) {
            params.put("gpRecordId", groupShopDetailsBean.getGpRecordId());
        }
        //订单信息
        List<ShopCarGoodsSkuTransmit> settlementGoods = new ArrayList<>();
        ShopCarGoodsSkuTransmit shopCarGoodsSkuTransmit = new ShopCarGoodsSkuTransmit();
        shopCarGoodsSkuTransmit.setId(groupShopDetailsBean.getProductId());
        shopCarGoodsSkuTransmit.setSaleSkuId(groupShopDetailsBean.getGpSkuId());
        shopCarGoodsSkuTransmit.setCount(1);
        settlementGoods.add(shopCarGoodsSkuTransmit);
        params.put("goods", new Gson().toJson(settlementGoods));
        //用户留言
        if (!TextUtils.isEmpty(message)) {
            params.put("remark", message);
        }
//        是否开具发票
        if (!TextUtils.isEmpty(invoiceTitle)) {
            params.put("invoice", invoiceTitle);
            if (!TextUtils.isEmpty(invoiceNum)) {
                params.put("taxpayer_on", invoiceNum);
            }
        }
//        付款方式
//        微信 wechatPay 支付宝 aliPay
        params.put("buyType", payWay);
        params.put("source", 0);
        XUtil.Post(url, params, new MyCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                if (payWay.equals(PAY_WX_PAY)) {
                    qualityWeChatIndent = gson.fromJson(result, QualityCreateWeChatPayIndentBean.class);
                    if (qualityWeChatIndent != null) {
                        if (qualityWeChatIndent.getCode().equals("01")) {
                            //返回成功，调起微信支付接口
                            doWXPay(qualityWeChatIndent.getResult().getPayKey());
                            orderCreateNo = qualityWeChatIndent.getResult().getNo();
                            recordIndentTrack(orderCreateNo);
                        } else {
                            constantMethod.showImportantToast(DirectIndentWriteActivity.this, qualityWeChatIndent.getResult() == null ? qualityWeChatIndent.getMsg() : qualityWeChatIndent.getResult().getMsg());
                        }
                    }
                } else {
                    qualityAliPayIndent = gson.fromJson(result, QualityCreateAliPayIndentBean.class);
                    if (qualityAliPayIndent != null) {
                        if (qualityAliPayIndent.getCode().equals("01")) {
                            //返回成功，调起支付宝支付接口
                            doAliPay(qualityAliPayIndent.getResult().getPayKey());
                            orderCreateNo = qualityAliPayIndent.getResult().getNo();
                            recordIndentTrack(orderCreateNo);
                        } else {
                            constantMethod.showImportantToast(DirectIndentWriteActivity.this, qualityAliPayIndent.getResult() == null ? qualityAliPayIndent.getMsg() : qualityAliPayIndent.getResult().getMsg());
                        }
                    }
                }
                tv_indent_write_commit.setEnabled(true);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                constantMethod.showImportantToast(DirectIndentWriteActivity.this, R.string.unConnectedNetwork);
                tv_indent_write_commit.setEnabled(true);
                super.onError(ex, isOnCallback);
            }
        });
    }

    private void paymentIndent() {
        String url = Url.BASE_URL + Url.Q_PAYMENT_INDENT;
        Map<String, Object> params = new HashMap<>();
        params.put("no", !TextUtils.isEmpty(orderCreateNo) ? orderCreateNo : orderNo);
        params.put("userId", userId);
        params.put("buyType", payWay);
        XUtil.Post(url, params, new MyCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                if (payWay.equals(PAY_WX_PAY)) {
                    qualityWeChatIndent = gson.fromJson(result, QualityCreateWeChatPayIndentBean.class);
                    if (qualityWeChatIndent != null) {
                        if (qualityWeChatIndent.getCode().equals("01")) {
                            //返回成功，调起微信支付接口
                            doWXPay(qualityWeChatIndent.getResult().getPayKey());
                        } else {
                            constantMethod.showImportantToast(DirectIndentWriteActivity.this, qualityWeChatIndent.getResult() == null
                                    ? qualityWeChatIndent.getMsg() : qualityWeChatIndent.getResult().getMsg());
                        }
                    }
                } else if (payWay.equals(PAY_ALI_PAY)) {
                    qualityAliPayIndent = gson.fromJson(result, QualityCreateAliPayIndentBean.class);
                    if (qualityAliPayIndent != null) {
                        if (qualityAliPayIndent.getCode().equals("01")) {
                            //返回成功，调起支付宝支付接口
                            doAliPay(qualityAliPayIndent.getResult().getPayKey());
                        } else {
                            constantMethod.showImportantToast(DirectIndentWriteActivity.this, qualityWeChatIndent.getResult() == null
                                    ? qualityWeChatIndent.getMsg() : qualityWeChatIndent.getResult().getMsg());
                        }
                    }
                }
                tv_indent_write_commit.setEnabled(true);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                constantMethod.showImportantToast(DirectIndentWriteActivity.this, R.string.unConnectedNetwork);
                tv_indent_write_commit.setEnabled(true);
                super.onError(ex, isOnCallback);
            }
        });
    }

    private void createIndent(final String payType, final List<ActivityProductInfoBean> productInfoList) {
        if (loadHud != null) {
            loadHud.show();
        }
        String message = pullFootView.edt_direct_product_note.getText().toString().trim();
        String url = Url.BASE_URL + Url.Q_CREATE_INDENT;
        Map<String, Object> params = new HashMap<>();
        //用户ID
        params.put("userId", userId);
        //用户地址
        params.put("userAddressId", addressId);
        //订单信息
//        Json对象
        JSONArray jsonArray = new JSONArray();
        try {
            for (int i = 0; i < productInfoList.size(); i++) {
                ActivityProductInfoBean activityProductInfoBean = productInfoList.get(i);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("saleSkuId", activityProductInfoBean.getSaleSkuId());
                jsonObject.put("id", activityProductInfoBean.getId());
                jsonObject.put("count", activityProductInfoBean.getCount());
                if (activityProductInfoBean.getCartId() > 0) {
                    jsonObject.put("cartId", activityProductInfoBean.getCartId());
                }
                if (activityProductInfoBean.getPresentProductInfoList() != null
                        && activityProductInfoBean.getPresentProductInfoList().size() > 0) {
                    StringBuilder stringBuilder = new StringBuilder();
                    for (CartProductInfoBean cartProductInfoBean : activityProductInfoBean.getPresentProductInfoList()) {
                        stringBuilder.append(!TextUtils.isEmpty(stringBuilder) ?
                                ("," + getStrings(cartProductInfoBean.getSaleSkuId()))
                                : getStrings(cartProductInfoBean.getSaleSkuId()));
                    }
                    jsonObject.put("presentSkuIds", stringBuilder.toString());
                } else {
                    jsonObject.put("presentSkuIds", "");
                }
                if (!TextUtils.isEmpty(activityProductInfoBean.getActivityCode())) {
                    jsonObject.put("activityCode", activityProductInfoBean.getActivityCode());
                }
                jsonArray.put(jsonObject);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        params.put("goods", jsonArray.toString().trim());
        //用户留言
        if (!TextUtils.isEmpty(message)) {
            params.put("remark", message);
        }
//        是否开具发票
        if (!TextUtils.isEmpty(invoiceTitle)) {
            params.put("invoice", invoiceTitle);
        }
        //是否使用优惠券
        if (couponId > 0) {
            params.put("userCouponId", couponId);
        }
//        付款方式
//        微信 wechatPay 支付宝 aliPay
        params.put("buyType", payType);
        params.put("isWeb", false);
        if (isOversea) {
            params.put("isOverseasGo", true);
            params.put("realName", pullHeaderView.et_oversea_name.getText().toString().trim());
            String idCard = pullHeaderView.et_oversea_card.getText().toString().trim();
            String showIdCard = (String) pullHeaderView.et_oversea_card.getTag();
            String reallyIdCard = (String) pullHeaderView.et_oversea_card.getTag(R.id.id_tag);
            if (idCard.equals(getStrings(showIdCard))) {
                params.put("idcard", reallyIdCard);
            } else {
                params.put("idcard", idCard);
            }
        } else {
            params.put("isOverseasGo", false);
        }
//        订单来源
        params.put("source", 0);
        XUtil.Post(url, params, new MyCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                if (loadHud != null) {
                    loadHud.dismiss();
                }
                Gson gson = new Gson();
                if (payType.equals(PAY_WX_PAY)) {
                    qualityWeChatIndent = gson.fromJson(result, QualityCreateWeChatPayIndentBean.class);
                    if (qualityWeChatIndent != null) {
                        if (qualityWeChatIndent.getCode().equals("01")) {
                            //返回成功，调起微信支付接口
                            doWXPay(qualityWeChatIndent.getResult().getPayKey());
                            orderCreateNo = qualityWeChatIndent.getResult().getNo();
                            recordIndentTrack(orderCreateNo);
                        } else {
                            constantMethod.showImportantToast(DirectIndentWriteActivity.this, qualityWeChatIndent.getResult() == null ? qualityWeChatIndent.getMsg() : qualityWeChatIndent.getResult().getMsg());
//                            赠品送完刷新数据
                            if (qualityWeChatIndent.getResult() != null) {
                                presentStatusUpdate(qualityWeChatIndent.getResult().getCode());
                            }
                        }
                    }
                } else {
                    qualityAliPayIndent = gson.fromJson(result, QualityCreateAliPayIndentBean.class);
                    if (qualityAliPayIndent != null) {
                        if (qualityAliPayIndent.getCode().equals("01")) {
                            //返回成功，调起支付宝支付接口
                            doAliPay(qualityAliPayIndent.getResult().getPayKey());
                            orderCreateNo = qualityAliPayIndent.getResult().getNo();
                            recordIndentTrack(orderCreateNo);
                        } else {
                            //                            赠品送完刷新数据
                            if (qualityAliPayIndent.getResult() != null) {
                                presentStatusUpdate(qualityAliPayIndent.getResult().getCode());
                            }
                            constantMethod.showImportantToast(DirectIndentWriteActivity.this, qualityAliPayIndent.getResult() == null ? qualityAliPayIndent.getMsg() : qualityAliPayIndent.getResult().getMsg());
                        }
                    }
                }
                tv_indent_write_commit.setEnabled(true);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (loadHud != null) {
                    loadHud.dismiss();
                }
                constantMethod.showImportantToast(DirectIndentWriteActivity.this, R.string.unConnectedNetwork);
                tv_indent_write_commit.setEnabled(true);
                super.onError(ex, isOnCallback);
            }
        });
    }

    //    赠品送完
    private final String PresentCode = "10023";

    /**
     * 创建订单返回状态
     *
     * @param codeStatus
     */
    private void presentStatusUpdate(String codeStatus) {
        try {
            if (PresentCode.equals(codeStatus)) {
                getIndentDiscounts(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void recordIndentTrack(String orderNo) {
        if (totalPersonalTrajectory != null) {
            Map<String, String> totalMap = new HashMap<>();
            totalMap.put("order_no", orderNo);
            totalPersonalTrajectory.stopTotal(totalMap);
        }
    }

    private void doWXPay(PayKeyBean pay_param) {
        WXPay.init(getApplicationContext());//要在支付前调用
        WXPay.getInstance().doPayDateObject(pay_param, new WXPay.WXPayResultCallBack() {
            @Override
            public void onSuccess() {
                constantMethod.showImportantToast(DirectIndentWriteActivity.this, "支付成功");
//                跳转订单完成页
                if (type.equals(INDENT_GROUP_SHOP)) {
                    switch (groupShopDetailsBean.getGpStatus()) {
                        case 1:
                            skipGpShareIndent();
                            break;
                        case 2:
                            skipMineGroupIndent();
                            break;
                    }
                } else {
                    skipDirectIndent();
                }
                if (totalPersonalTrajectory != null) {
                    isUpTotalFile = true;
                    createExecutor().execute(() -> {
                        totalPersonalTrajectory.getFileTotalTrajectory();
                    });
                    isUpTotalFile = false;
                }
            }

            @Override
            public void onError(int error_code) {
                switch (error_code) {
                    case WXPay.NO_OR_LOW_WX:
                        constantMethod.showImportantToast(DirectIndentWriteActivity.this, "未安装微信或微信版本过低");
                        break;
                    case WXPay.ERROR_PAY_PARAM:
                        constantMethod.showImportantToast(DirectIndentWriteActivity.this, "参数错误");
                        break;
                    case WXPay.ERROR_PAY:
                        constantMethod.showImportantToast(DirectIndentWriteActivity.this, "支付失败");
                        break;
                }
                payError();
            }

            @Override
            public void onCancel() {
                payError();
                constantMethod.showImportantToast(DirectIndentWriteActivity.this, "支付取消");
            }
        });
    }

    /**
     * 支付失败弹窗
     */
    private void payError() {
        Calendar calendar = Calendar.getInstance();
        if (createIndentTime == null) {
            createIndentTime = calendar.getTime();
        }
        if (current == null) {
            current = calendar.getTime();
        }
        if (payErrorAlert == null) {
            String url = Url.BASE_URL + Url.PAY_ERROR;
            XUtil.Get(url, null, new MyCallBack<String>() {
                @Override
                public void onSuccess(String result) {
                    RequestStatus requestStatus = RequestStatus.objectFromData(result);
                    if (requestStatus != null && "01".equals(requestStatus.getCode())) {
                        setRefundTime(requestStatus);
                        if (!TextUtils.isEmpty(payErrorMsg)) {
                            AlertSettingBean alertSettingBean = new AlertSettingBean();
                            AlertSettingBean.AlertData alertData = new AlertSettingBean.AlertData();
                            AlertSettingBean.AlertInitView alertInitView = new AlertSettingBean.AlertInitView();
                            alertData.setCancelStr("确认离开");
                            alertData.setDetermineStr("继续支付");
                            alertData.setFirstDet(false);
                            alertData.setTitle("支付失败");
                            alertData.setMsg(payErrorMsg.toString());
                            alertInitView.setCancelColorValue("#999999");
                            alertInitView.setCancelSize(30);
                            alertInitView.setDetColorValue("#0a88fa");
                            alertInitView.setCancelSize(30);
                            alertSettingBean.setStyle(AlertView.Style.Alert);
                            alertSettingBean.setAlertData(alertData);
                            alertSettingBean.setAlertInitView(alertInitView);
                            payErrorAlert = new AlertView(alertSettingBean, DirectIndentWriteActivity.this, DirectIndentWriteActivity.this);
                            tvAlertMsg = payErrorAlert.getTvAlertMsg();
                            tvAlertMsg.setText(payErrorMsg);
                            payErrorAlert.setCancelable(false);
                            payErrorAlert.show();
                        }
                    }
                }
            });
        } else {
            payErrorAlert.show();
        }
    }

    /**
     * 设置退款倒计时
     *
     * @param requestStatus
     */
    private void setRefundTime(final RequestStatus requestStatus) {
        //            创建时间加倒计时间 大于等于当前时间 展示倒计时
        if (isEndOrStartTimeAddSeconds(createIndentTime
                , current
                , requestStatus.getSecond())) {
            setReFundTimeDown(requestStatus);
            if (constantMethod == null) {
                constantMethod = new ConstantMethod();
            }
            constantMethod.createSchedule();
            constantMethod.setRefreshTimeListener(() -> setReFundTimeDown(requestStatus));
        } else {
            if (constantMethod != null) {
                constantMethod.stopSchedule();
            }
            if (tvAlertMsg != null) {
                tvAlertMsg.setText("订单已失效");
            }
        }
    }

    /**
     * 更新退款倒计时
     *
     * @param requestStatus
     */
    private void setReFundTimeDown(RequestStatus requestStatus) {
        try {
            requestStatus.setSecond(requestStatus.getSecond() - 1);
            long overTime = requestStatus.getSecond() * 1000;
            long ms = createIndentTime.getTime() + overTime - current.getTime();
            if (ms >= 0) {
                int hour = (int) ((ms % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
                int minute = (int) ((ms % (1000 * 60 * 60)) / (1000 * 60));
                String timeMsg = (String.format(getString(R.string.pay_failed), hour, minute));
                Link link = new Link(Pattern.compile(REGEX_NUM));
                link.setTextColor(0xffff5e6b);
                link.setUnderlined(false);
                link.setHighlightAlpha(0f);
                payErrorMsg = LinkBuilder.from(DirectIndentWriteActivity.this, timeMsg)
                        .addLink(link)
                        .build();
            } else {
                if (payErrorAlert != null) {
                    payErrorAlert.dismiss();
                }
            }
        } catch (Exception e) {
            if (payErrorAlert != null) {
                payErrorAlert.dismiss();
            }
        }
    }

    /**
     * 订单支付取消弹窗
     */
    private void payCancel() {
        if (payCancelAlert == null) {
            String url = Url.BASE_URL + Url.PAY_CANCEL;
            XUtil.Get(url, null, new MyCallBack<String>() {
                @Override
                public void onSuccess(String result) {
                    RequestStatus requestStatus = RequestStatus.objectFromData(result);
                    if (requestStatus != null && "01".equals(requestStatus.getCode())) {
                        AlertSettingBean alertSettingBean = new AlertSettingBean();
                        AlertSettingBean.AlertData alertData = new AlertSettingBean.AlertData();
                        AlertSettingBean.AlertInitView alertInitView = new AlertSettingBean.AlertInitView();
                        alertData.setCancelStr("我再想想");
                        alertData.setDetermineStr("去意已决");
                        alertData.setFirstDet(false);
                        alertData.setMsg(!TextUtils.isEmpty(requestStatus.getDescription()) ?
                                requestStatus.getDescription() : "好货不等人哦，喜欢就入了吧");
                        alertInitView.setCancelColorValue("#999999");
                        alertInitView.setCancelSize(30);
                        alertInitView.setDetColorValue("#0a88fa");
                        alertInitView.setCancelSize(30);
                        alertSettingBean.setStyle(AlertView.Style.Alert);
                        alertSettingBean.setAlertData(alertData);
                        alertSettingBean.setAlertInitView(alertInitView);
                        payCancelAlert = new AlertView(alertSettingBean, DirectIndentWriteActivity.this, DirectIndentWriteActivity.this);
                        payCancelAlert.setCancelable(false);
                        payCancelAlert.show();
                    }
                }
            });
        } else {
            finish();
        }
    }

    @Override
    public void onAlertItemClick(Object o, int position) {
        if (payErrorAlert == o) {
            if (payErrorAlert != null) {
                payErrorAlert.dismiss();
            }
            if (position == AlertView.CANCELPOSITION) {
                finish();
            }
        } else if (payCancelAlert == o) {
            if (payCancelAlert != null) {
                payCancelAlert.dismiss();
            }
            if (position != AlertView.CANCELPOSITION) {
                finish();
            }
        }
    }

    private void skipMineGroupIndent() {
        Intent intent = new Intent();
        intent.setClass(DirectIndentWriteActivity.this, QualityGroupShopMineActivity.class);
        startActivity(intent);
    }

    private void skipGpShareIndent() {
        String orderNo;
        Intent intent = new Intent(DirectIndentWriteActivity.this, DoMoGroupJoinShareActivity.class);
        if (payWay.equals(PAY_WX_PAY)) {
            orderNo = qualityWeChatIndent.getResult().getNo();
            intent.putExtra("orderNo", orderNo);
        } else {
            orderNo = qualityAliPayIndent.getResult().getNo();
            intent.putExtra("orderNo", orderNo);
        }
        startActivity(intent);
        finish();
    }

    private void doAliPay(String pay_param) {
        new AliPay(this, pay_param, new AliPay.AliPayResultCallBack() {
            @Override
            public void onSuccess() {
                constantMethod.showImportantToast(DirectIndentWriteActivity.this, "支付成功");
//                跳转订单完成页
                if (type.equals(INDENT_GROUP_SHOP)) {
                    switch (groupShopDetailsBean.getGpStatus()) {
                        case 1:
//                            开团
                            skipGpShareIndent();
                            break;
                        case 2:
//                            拼团
                            skipMineGroupIndent();
                            break;
                    }
                } else {
                    skipDirectIndent();
                }
                if (totalPersonalTrajectory != null) {
                    isUpTotalFile = true;
                    createExecutor().execute(() -> {
                        totalPersonalTrajectory.getFileTotalTrajectory();
                    });
                    isUpTotalFile = false;
                }
            }

            @Override
            public void onDealing() {
                constantMethod.showImportantToast(DirectIndentWriteActivity.this, "支付处理中...");
            }

            @Override
            public void onError(int error_code) {
                switch (error_code) {
                    case AliPay.ERROR_RESULT:
                        constantMethod.showImportantToast(DirectIndentWriteActivity.this, "支付失败:支付结果解析错误");
                        break;

                    case AliPay.ERROR_NETWORK:
                        constantMethod.showImportantToast(DirectIndentWriteActivity.this, "支付失败:网络连接错误");
                        break;

                    case AliPay.ERROR_PAY:
                        constantMethod.showImportantToast(DirectIndentWriteActivity.this, "支付错误:支付码支付失败");
                        break;

                    default:
                        constantMethod.showImportantToast(DirectIndentWriteActivity.this, "支付错误");
                        break;
                }
                payError();
            }

            @Override
            public void onCancel() {
                payError();
                constantMethod.showImportantToast(DirectIndentWriteActivity.this, "支付取消");
            }
        }).doPay();
    }

    private void skipDirectIndent() {
        Intent intent = new Intent(DirectIndentWriteActivity.this, DirectPaySuccessActivity.class);
        intent.putExtra(INDENT_PRODUCT_TYPE, INDENT_PROPRIETOR_PRODUCT);
        if (payWay.equals(PAY_WX_PAY)) {
            if (qualityWeChatIndent.getResult() != null && !TextUtils.isEmpty(qualityWeChatIndent.getResult().getNo())) {
                intent.putExtra("indentNo", qualityWeChatIndent.getResult().getNo());
                startActivity(intent);
            }
        } else if (payWay.equals(PAY_ALI_PAY)) {
            if (qualityAliPayIndent.getResult() != null && !TextUtils.isEmpty(qualityAliPayIndent.getResult().getNo())) {
                intent.putExtra("indentNo", qualityAliPayIndent.getResult().getNo());
                startActivity(intent);
            }
        }
        finish();
    }

    @Override
    protected void loadData() {
        if (type.equals(INDENT_W_TYPE)) {
            if (isFirst) {
                getDefaultAddress();
            } else {
                getAddressDetails();
            }
        } else if (type.equals(INDENT_GROUP_SHOP)) {
            pullFootView.ll_layout_coupon.setVisibility(View.GONE);
            if (isFirst) {
                getDefaultAddress();
            } else {
                getAddressDetails();
            }
        }
    }

    @Override
    protected View getLoadView() {
        return ll_indent_details;
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    /**
     * 结算金额 不带优惠券
     */
    private void getIndentDiscounts(boolean disuseCoupon) {
        if (discountBeanList.size() > 0) {
            //优惠详情信息
            String url = Url.BASE_URL + Url.INDENT_DISCOUNTS_INFO;
            Map<String, Object> params = new HashMap<>();
            params.put("userId", userId);
            if (addressId > 0) {
                params.put("addressId", addressId);
            }
            if (couponId > 0 || disuseCoupon) {
                params.put("userCouponId", couponId);
            }
            if (type.equals(INDENT_GROUP_SHOP)) {
                params.put("gpInfoId", groupShopDetailsBean.getGpInfoId());
            }
            JSONArray jsonArray = new JSONArray();
            for (IndentProDiscountBean indentProDiscountBean : discountBeanList) {
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("id", indentProDiscountBean.getId());
                    jsonObject.put("saleSkuId", indentProDiscountBean.getSaleSkuId());
                    jsonObject.put("count", indentProDiscountBean.getCount());
                    if (indentProDiscountBean.getCartId() > 0) {
                        jsonObject.put("cartId", indentProDiscountBean.getCartId());
                    }
                    jsonArray.put(jsonObject);
                    params.put("goods", jsonArray.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            params.put("version", "v3.1.5");
            NetLoadUtils.getQyInstance().loadNetDataPost(mAppContext, url, params, new NetLoadUtils.NetLoadListener() {
                @Override
                public void onSuccess(String result) {
                    loadHud.dismiss();
                    Gson gson = new Gson();
                    indentDiscountsEntity = gson.fromJson(result, IndentDiscountsEntity.class);
                    if (indentDiscountsEntity != null) {
                        if (indentDiscountsEntity.getCode().equals(SUCCESS_CODE)) {
                            indentDiscountsBean = indentDiscountsEntity.getIndentDiscountsBean();
                            setDiscountsInfo(indentDiscountsBean);
                        }
                    }
                    NetLoadUtils.getQyInstance().showLoadSir(loadService,indentDiscountsEntity);
                }

                @Override
                public void netClose() {
                    loadHud.dismiss();
                    NetLoadUtils.getQyInstance().showLoadSir(loadService,indentDiscountsEntity);
                }

                @Override
                public void onError(Throwable throwable) {
                    loadHud.dismiss();
                    NetLoadUtils.getQyInstance().showLoadSir(loadService,indentDiscountsEntity);
                }
            });
        }else{
            NetLoadUtils.getQyInstance().showLoadSir(loadService,indentDiscountsEntity);
        }
    }

    /**
     * 结算金额 换取优惠券调用
     */
//    private void getIndentSettle() {
//        if (indentDiscountsEntity != null) {
//            String url = Url.BASE_URL + Url.INDENT_SETTLE_PRICE;
//            Map<String, Object> params = new HashMap<>();
//            params.put("newUserCouponId", couponId);
//            params.put("userId", uid);
//            if (addressId > 0) {
//                params.put("addressId", addressId);
//            }
//            try {
//                JSONArray jsonArray = new JSONArray();
//                List<ProductInfoBean> productInfoList = indentDiscountsEntity.getIndentDiscountsBean().getProductInfoList();
//                for (int i = 0; i < productInfoList.size(); i++) {
//                    ProductInfoBean productInfoBean = productInfoList.get(i);
//                    for (int j = 0; j < productInfoBean.getActivityProductInfo().size(); j++) {
//                        ActivityProductInfoBean activityProductInfoBean = productInfoBean.getActivityProductInfo().get(j);
//                        JSONObject jsonObject = new JSONObject();
//                        jsonObject.put("id", activityProductInfoBean.getId());
//                        jsonObject.put("price", activityProductInfoBean.getPrice());
//                        jsonObject.put("count", activityProductInfoBean.getCount());
//                        jsonObject.put("saleSkuId", activityProductInfoBean.getSaleSkuId());
//                        if (!TextUtils.isEmpty(activityProductInfoBean.getActivityCode())) {
//                            jsonObject.put("activityCode", activityProductInfoBean.getActivityCode());
//                        }
//                        jsonArray.put(jsonObject);
//                    }
//                }
//                params.put("orderList", jsonArray.toString());
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            XUtil.Post(url, params, new MyCallBack<String>() {
//                @Override
//                public void onSuccess(String result) {
//                    Gson gson = new Gson();
//                    CouponSettleEntity couponSettleEntity = gson.fromJson(result, CouponSettleEntity.class);
//                    if (couponSettleEntity != null) {
//                        if (couponSettleEntity.getCode().equals("01")) {
//                            setDiscounts(couponSettleEntity.getCouponSettleList());
//                        }
//                    }
//                }
//
//                @Override
//                public void onError(Throwable ex, boolean isOnCallback) {
//                    super.onError(ex, isOnCallback);
//                    constantMethod.showImportantToast(DirectIndentWriteActivity.this, R.string.do_failed);
//                }
//            });
//        }
//    }
    private void setDiscountsInfo(IndentDiscountsBean indentDiscountsBean) {
        isOversea = indentDiscountsBean.isOverseasGo();
        tv_indent_write_commit.setEnabled(indentDiscountsBean.getHideCreate() == 0);
        setOverseaData(indentDiscountsBean);
        setDiscounts(indentDiscountsBean.getPriceInfoList());
        if (indentDiscountsBean.getUserCouponInfo() != null) {
            IndentDiscountsBean.UserCouponInfoBean userCouponInfo = indentDiscountsBean.getUserCouponInfo();
            pullFootView.tv_direct_product_favorable.setText(("-￥" + userCouponInfo.getAmount()));
            couponId = userCouponInfo.getId();
            pullFootView.tv_direct_product_favorable.setSelected(true);
        } else {
            pullFootView.tv_direct_product_favorable.setSelected(false);
//                暂无可用优惠券
            if (!TextUtils.isEmpty(indentDiscountsBean.getUserCouponMsg())) {
                pullFootView.tv_direct_product_favorable.setText(indentDiscountsBean.getUserCouponMsg());
            } else {
                pullFootView.tv_direct_product_favorable.setText(R.string.unavailable_ticket);
            }
        }
        productInfoList.clear();
        for (int i = 0; i < indentDiscountsBean.getProductInfoList().size(); i++) {
            ProductInfoBean productInfoBean = indentDiscountsBean.getProductInfoList().get(i);
            for (int j = 0; j < productInfoBean.getActivityProductInfo().size(); j++) {
                ActivityProductInfoBean activityProductInfoBean = productInfoBean.getActivityProductInfo().get(j);
                if (productInfoBean.getActivityInfo() != null && j == 0) {
                    activityProductInfoBean.setShowActInfo(1);
                }
                activityProductInfoBean.setActivityInfoBean(productInfoBean.getActivityInfo());
                activityProductInfoBean.setShowDel(indentDiscountsBean.getShowDel() == 1);
                productInfoList.add(activityProductInfoBean);
            }
            if (productInfoBean.getActivityInfo() != null && productInfoList.size() > 0
                    && indentDiscountsBean.getProductInfoList().size() > i + 1) {
                ActivityProductInfoBean activityProductInfoBean = productInfoList.get(productInfoList.size() - 1);
                activityProductInfoBean.setShowLine(1);
                productInfoList.set(productInfoList.size() - 1, activityProductInfoBean);
            }
        }
        if (productInfoList.size() > 0 && productInfoList.size() < 2) {
            ActivityProductInfoBean activityProductInfoBean = productInfoList.get(0);
            pullFootView.rect_indent_number.setVisibility(VISIBLE);
            pullFootView.rect_indent_number.setNum(activityProductInfoBean.getCount());
            if (passGoods != null) {
                for (CartInfoBean cartInfoBean : passGoods) {
                    SaleSkuBean saleSku = cartInfoBean.getSaleSku();
                    if (saleSku != null && saleSku.getId() == activityProductInfoBean.getSaleSkuId()) {
                        pullFootView.rect_indent_number.setMaxNum(saleSku.getQuantity());
                    }
                }
            }
        } else {
            pullFootView.rect_indent_number.setVisibility(View.GONE);
        }
        directProductAdapter.notifyDataSetChanged();
    }

    private void setOverseaData(IndentDiscountsBean indentDiscountsBean) {
        if (indentDiscountsBean.isOverseasGo()) {
            pullHeaderView.ll_oversea_info.setVisibility(VISIBLE);
            pullHeaderView.et_oversea_name.setText(getStringFilter(indentDiscountsBean.getRealName()));
            pullHeaderView.et_oversea_name.setSelection(getStrings(indentDiscountsBean.getRealName()).length());
            pullHeaderView.et_oversea_card.setText(getStringFilter(indentDiscountsBean.getShowIdcard()));
            pullHeaderView.et_oversea_card.setSelection(getStrings(indentDiscountsBean.getShowIdcard()).length());
            pullHeaderView.et_oversea_card.setTag(R.id.id_tag, getStrings(indentDiscountsBean.getIdcard()));
            pullHeaderView.et_oversea_card.setTag(getStrings(indentDiscountsBean.getShowIdcard()));
            if (!TextUtils.isEmpty(indentDiscountsBean.getPrompt())) {
                pullHeaderView.tv_oversea_buy_tint.setVisibility(VISIBLE);
                pullHeaderView.tv_oversea_buy_tint.setText(getStrings(indentDiscountsBean.getPrompt()));
            } else {
                pullHeaderView.tv_oversea_buy_tint.setVisibility(View.GONE);
            }
        } else {
            pullHeaderView.tv_oversea_buy_tint.setVisibility(View.GONE);
            pullHeaderView.ll_oversea_info.setVisibility(View.GONE);
        }
    }

    private void setDiscounts(List<PriceInfoBean> indentDiscountsList) {
        if (indentDiscountsList != null) {
            priceInfoList.clear();
            priceInfoList.addAll(indentDiscountsList);
            PriceInfoBean priceInfoBean = priceInfoList.get(priceInfoList.size() - 1);
            tv_indent_total_price.setText(getStrings(priceInfoBean.getTotalPriceName()));
            priceInfoList.remove(priceInfoList.get(priceInfoList.size() - 1));
            indentDiscountAdapter.setNewData(priceInfoList);
        }
    }

    private void getDefaultAddress() {
        String url = Url.BASE_URL + Url.DELIVERY_ADDRESS + userId;
        XUtil.Get(url, null, new MyCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                AddressInfoEntity addressInfoEntity = gson.fromJson(result, AddressInfoEntity.class);
                if (addressInfoEntity != null) {
                    if (addressInfoEntity.getCode().equals("01")) {
                        setAddressData(addressInfoEntity.getAddressInfoBean());
                    } else if (addressInfoEntity.getCode().equals("02")) {
                        setAddressData(null);
                    } else {
                        constantMethod.showImportantToast(DirectIndentWriteActivity.this, addressInfoEntity.getMsg());
                    }
                }
            }
        });
    }

    private void getAddressDetails() {
        //地址详情内容
        String url = Url.BASE_URL + Url.ADDRESS_DETAILS + addressId;
        XUtil.Get(url, null, new MyCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                AddressInfoEntity addressInfoEntity = gson.fromJson(result, AddressInfoEntity.class);
                if (addressInfoEntity != null) {
                    if (addressInfoEntity.getCode().equals("01")) {
                        setAddressData(addressInfoEntity.getAddressInfoBean());
                    } else if (addressInfoEntity.getCode().equals("02")) {
                        setAddressData(null);
                    } else {
                        constantMethod.showImportantToast(DirectIndentWriteActivity.this, addressInfoEntity.getMsg());
                    }
                }
            }
        });
    }

    private void setAddressData(AddressInfoEntity.AddressInfoBean addressInfoBean) {
        if (addressInfoBean != null) {
            addressId = addressInfoBean.getId();
            pullHeaderView.ll_indent_address_default.setVisibility(VISIBLE);
            pullHeaderView.ll_indent_address_null.setVisibility(View.GONE);
            pullHeaderView.tv_consignee_name.setText(addressInfoBean.getConsignee());
            pullHeaderView.tv_address_mobile_number.setText(addressInfoBean.getMobile());
            pullHeaderView.tv_indent_details_address.setText((addressInfoBean.getAddress_com() + addressInfoBean.getAddress() + " "));
        } else {
            pullHeaderView.ll_indent_address_default.setVisibility(View.GONE);
            pullHeaderView.ll_indent_address_null.setVisibility(VISIBLE);
        }
        //            再次购买
        if (orderNo != null) {
            getOrderData();
        } else {
            getIndentDiscounts(false);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            if (requestCode == REQ_INVOICE) {
                if (TextUtils.isEmpty(invoiceTitle)) {
                    invoiceTitle = "";
                    pullFootView.tv_direct_product_invoice.setText("不开发票");
                }
            } else {
                return;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case NEW_CRE_ADDRESS_REQ:
                case SEL_ADDRESS_REQ:
                    //            获取地址成功
                    addressId = data.getIntExtra("addressId", 0);
                    isFirst = false;
                    getAddressDetails();
                    break;
                case DIRECT_COUPON_REQ:
                    //            获取优惠券
                    couponId = data.getIntExtra("couponId", 0);
                    int couponAmount = data.getIntExtra("couponAmount", 0);
                    if (couponId < 1) {
                        pullFootView.tv_direct_product_favorable.setEnabled(true);
                        pullFootView.tv_direct_product_favorable.setSelected(false);
                        pullFootView.tv_direct_product_favorable.setText("不使用优惠券");
                        getIndentDiscounts(true);
                    } else {
                        pullFootView.tv_direct_product_favorable.setEnabled(true);
                        pullFootView.tv_direct_product_favorable.setSelected(true);
                        pullFootView.tv_direct_product_favorable.setText(("-￥" + couponAmount));
                        getIndentDiscounts(false);
                    }
                    break;
                case REQ_INVOICE:
                    invoiceTitle = data.getStringExtra("invoiceTitle");
                    if (!TextUtils.isEmpty(invoiceTitle)) {
                        invoiceNum = data.getStringExtra("invoiceNum");
                    }
                    if (!TextUtils.isEmpty(invoiceTitle)) {
                        pullFootView.tv_direct_product_invoice.setText("开发票");
                    } else {
                        pullFootView.tv_direct_product_invoice.setText("不开发票");
                    }
                    break;
            }
        }
    }

    /**
     * 活动规则弹框
     *
     * @param productInfoBean
     */
    private void setDialogRule(ActivityProductInfoBean productInfoBean) {
        if (productInfoBean.getActivityInfoBean() != null) {
            if (alertDialog == null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DirectIndentWriteActivity.this, R.style.CustomTransDialog);
                View dialogView = LayoutInflater.from(DirectIndentWriteActivity.this).inflate(R.layout.layout_act_topic_rule, null, false);
                ruleDialog = new RuleDialogView();
                ButterKnife.bind(ruleDialog, dialogView);
                ruleDialog.initViews();
                ruleDialog.setData(productInfoBean.getActivityInfoBean());
                alertDialog = builder.create();
                alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                alertDialog.setCanceledOnTouchOutside(true);
                alertDialog.show();
                TinkerBaseApplicationLike app = (TinkerBaseApplicationLike) TinkerManager.getTinkerApplicationLike();
                int dialogHeight = (int) (app.getScreenHeight() * 0.4 + 1);
                Window window = alertDialog.getWindow();
                window.getDecorView().setPadding(0, 0, 0, 0);
                window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, dialogHeight);
                window.setGravity(Gravity.BOTTOM);//底部出现
                window.setContentView(dialogView);
            } else {
                alertDialog.show();
            }
            ruleDialog.tv_act_topic_rule_skip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ActivityInfoBean activityInfoBean = (ActivityInfoBean) view.getTag();
                    if (activityInfoBean != null && !TextUtils.isEmpty(activityInfoBean.getActivityCode())) {
                        alertDialog.dismiss();
                        Intent intent = new Intent(DirectIndentWriteActivity.this, QualityProductActActivity.class);
                        intent.putExtra("activityCode", getStrings(activityInfoBean.getActivityCode()));
                        startActivity(intent);
                        finish();
                        overridePendingTransition(0, 0);
                    }
                }
            });
        }
    }

    class RuleDialogView {
        @BindView(R.id.tv_act_topic_rule_title)
        TextView tv_act_topic_rule_title;
        @BindView(R.id.tv_act_topic_rule_skip)
        TextView tv_act_topic_rule_skip;
        @BindView(R.id.communal_recycler)
        RecyclerView communal_recycler;
        CommunalDetailAdapter actRuleDetailsAdapter;
        List<CommunalDetailObjectBean> communalDetailBeanList = new ArrayList<>();

        public void initViews() {
            tv_act_topic_rule_skip.setVisibility(VISIBLE);
            communal_recycler.setNestedScrollingEnabled(false);
            communal_recycler.setLayoutManager(new LinearLayoutManager(DirectIndentWriteActivity.this));
            communal_recycler.setNestedScrollingEnabled(false);
            actRuleDetailsAdapter = new CommunalDetailAdapter(DirectIndentWriteActivity.this, communalDetailBeanList);
            communal_recycler.setLayoutManager(new LinearLayoutManager(DirectIndentWriteActivity.this));
            communal_recycler.setAdapter(actRuleDetailsAdapter);
        }

        public void setData(ActivityInfoBean activityInfoBean) {
            tv_act_topic_rule_title.setText("活动规则");
            tv_act_topic_rule_skip.setText(String.format(getResources().getString(R.string.skip_topic)
                    , getStrings(activityInfoBean.getActivityTag())));
            tv_act_topic_rule_skip.setTag(activityInfoBean);
            if (activityInfoBean.getActivityRuleDetailList() != null && activityInfoBean.getActivityRuleDetailList().size() > 0) {
                communalDetailBeanList.clear();
                communalDetailBeanList.addAll(ConstantMethod.getDetailsDataList(activityInfoBean.getActivityRuleDetailList()));
                actRuleDetailsAdapter.notifyDataSetChanged();
            }
        }
    }

    class PullHeaderView {
        //    地址为空
        @BindView(R.id.ll_indent_address_null)
        LinearLayout ll_indent_address_null;
        //   有默认地址
        @BindView(R.id.ll_indent_address_default)
        LinearLayout ll_indent_address_default;
        //    收件人名字
        @BindView(R.id.tv_consignee_name)
        TextView tv_consignee_name;
        //    收件人手机号码
        @BindView(R.id.tv_consignee_mobile_number)
        TextView tv_address_mobile_number;
        //    订单地址
        @BindView(R.id.tv_indent_details_address)
        TextView tv_indent_details_address;
        //    订单地址
        @BindView(R.id.img_skip_address)
        ImageView img_skip_address;
        //        海外购实名提示
        @BindView(R.id.tv_oversea_buy_tint)
        TextView tv_oversea_buy_tint;
        //        实名填写布局
        @BindView(R.id.ll_oversea_info)
        LinearLayout ll_oversea_info;
        //        实名用户姓名
        @BindView(R.id.et_oversea_name)
        EditText et_oversea_name;
        //        实名用户身份证号码
        @BindView(R.id.et_oversea_card)
        EditText et_oversea_card;

        //    地址列表为空 跳转新建地址
        @OnClick(R.id.tv_lv_top)
        void skipNewAddress(View view) {
            Intent intent = new Intent(DirectIndentWriteActivity.this, SelectedAddressActivity.class);
            intent.putExtra("hasDefaultAddress", false);
            startActivityForResult(intent, NEW_CRE_ADDRESS_REQ);
        }

        //  更换地址  跳转地址列表
        @OnClick(R.id.ll_indent_address_default)
        void skipAddressList(View view) {
            if (TextUtils.isEmpty(orderCreateNo)) {
                if (type.equals(INDENT_W_TYPE) || type.equals(INDENT_GROUP_SHOP)) {
                    Intent intent = new Intent(DirectIndentWriteActivity.this, SelectedAddressActivity.class);
                    intent.putExtra("addressId", addressId);
                    intent.putExtra("hasDefaultAddress", true);
                    startActivityForResult(intent, SEL_ADDRESS_REQ);
                } else {
                    img_skip_address.setVisibility(View.GONE);
                    return;
                }
            }
        }
    }

    class PullFootView {
        //优惠券说明
        @BindView(R.id.tv_direct_product_favorable)
        TextView tv_direct_product_favorable;
        //留言编辑
        @BindView(R.id.edt_direct_product_note)
        EditText edt_direct_product_note;
        //支付宝支付
        @BindView(R.id.rb_checked_alipay)
        RadioButton rb_checked_aliPay;
        //微信支付
        @BindView(R.id.rb_checked_wechat_pay)
        RadioButton rb_checked_weChat_pay;
        //        发票展示
        @BindView(R.id.tv_direct_product_invoice)
        TextView tv_direct_product_invoice;
        //        优惠券布局
        @BindView(R.id.ll_layout_coupon)
        LinearLayout ll_layout_coupon;
        //        总优惠
        @BindView(R.id.rv_indent_write_info)
        RecyclerView rv_indent_write_info;
        @BindView(R.id.rect_indent_number)
        RectAddAndSubViewDirect rect_indent_number;

        //支付宝方式
        @OnClick(R.id.ll_aliPay)
        void aliPay(View view) {
            rb_checked_aliPay.setChecked(true);
            rb_checked_weChat_pay.setChecked(false);
        }

        //微信支付方式
        @OnClick(R.id.ll_Layout_weChat)
        void weChat(View view) {
            rb_checked_aliPay.setChecked(false);
            rb_checked_weChat_pay.setChecked(true);
        }

        //        优惠券选择
        @OnClick(R.id.ll_layout_coupon)
        void selectFavorable() {
            if(indentDiscountsBean!=null){
                if(indentDiscountsBean.getProductIsUsable()==0){
                    constantMethod.showImportantToast(DirectIndentWriteActivity.this,"该商品不支持使用优惠券！");
                }else if(indentDiscountsBean.getProductIsUsable()==1){
                    if (TextUtils.isEmpty(orderCreateNo)) {
                        if (!type.equals(INDENT_DETAILS_TYPE)) {
                            Intent intent = new Intent(DirectIndentWriteActivity.this, DirectCouponGetActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putParcelableArrayList("couponGoods", (ArrayList<? extends Parcelable>) productInfoList);
                            intent.putExtras(bundle);
                            startActivityForResult(intent, DIRECT_COUPON_REQ);
                        } else {
                            tv_direct_product_favorable.setCompoundDrawables(null, null, null, null);
                            return;
                        }
                    }
                }
            }
        }

        //        开发票
        @OnClick(R.id.ll_indent_open_invoice)
        void selInvoice(View view) {
            if (TextUtils.isEmpty(orderCreateNo)) {
//            选择发票
                Intent intent = new Intent(DirectIndentWriteActivity.this, IndentDrawUpInvoiceActivity.class);
                intent.putExtra("type", "indentWrite");
                if (!TextUtils.isEmpty(invoiceTitle)) {
                    intent.putExtra("invoiceTitle", invoiceTitle);
                    if (!TextUtils.isEmpty(invoiceNum)) {
                        intent.putExtra("invoiceNum", invoiceNum);
                    }
                }
                startActivityForResult(intent, REQ_INVOICE);
            }
        }

    }

    @OnClick(R.id.tv_life_back)
    void goBack(View view) {
        payCancel();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            payCancel();
            return true;//不执行父类点击事件
        }
        return super.onKeyDown(keyCode, event);//继续执行父类其他点击事件
    }
}
