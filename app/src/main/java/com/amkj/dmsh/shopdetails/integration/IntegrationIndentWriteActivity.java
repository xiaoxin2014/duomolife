package com.amkj.dmsh.shopdetails.integration;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.address.activity.SelectedAddressActivity;
import com.amkj.dmsh.address.bean.AddressInfoEntity;
import com.amkj.dmsh.address.bean.AddressInfoEntity.AddressInfoBean;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.shopdetails.activity.DirectPaySuccessActivity;
import com.amkj.dmsh.shopdetails.adapter.IndentDiscountAdapter;
import com.amkj.dmsh.shopdetails.bean.IntegrationIndentEntity;
import com.amkj.dmsh.shopdetails.bean.IntegrationIndentEntity.IntegrationIndentBean;
import com.amkj.dmsh.shopdetails.bean.PriceInfoBean;
import com.amkj.dmsh.shopdetails.bean.QualityCreateWeChatPayIndentBean.ResultBean.PayKeyBean;
import com.amkj.dmsh.shopdetails.bean.ShopCarGoodsSku;
import com.amkj.dmsh.shopdetails.integration.bean.AddressListEntity;
import com.amkj.dmsh.shopdetails.integration.bean.IntegralSettlementEntity;
import com.amkj.dmsh.shopdetails.integration.bean.IntegralSettlementEntity.IntegralSettlementBean;
import com.amkj.dmsh.shopdetails.integration.bean.IntegralSettlementEntity.IntegralSettlementBean.ProductInfoBean;
import com.amkj.dmsh.shopdetails.payutils.AliPay;
import com.amkj.dmsh.shopdetails.payutils.UnionPay;
import com.amkj.dmsh.shopdetails.payutils.WXPay;
import com.amkj.dmsh.utils.KeyboardUtils;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.google.gson.Gson;
import com.klinker.android.link_builder.Link;
import com.klinker.android.link_builder.LinkBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;
import me.jessyan.autosize.utils.AutoSizeUtils;

import static android.view.View.VISIBLE;
import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.getStringFilter;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.showImportantToast;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.INDENT_INTEGRAL_PRODUCT;
import static com.amkj.dmsh.constant.ConstantVariable.INDENT_PRODUCT_TYPE;
import static com.amkj.dmsh.constant.ConstantVariable.IS_LOGIN_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.PAY_ALI_PAY;
import static com.amkj.dmsh.constant.ConstantVariable.PAY_UNION_PAY;
import static com.amkj.dmsh.constant.ConstantVariable.PAY_WX_PAY;
import static com.amkj.dmsh.constant.ConstantVariable.REGEX_NUM;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.UNION_RESULT_CODE;
import static com.amkj.dmsh.constant.Url.ADDRESS_LIST;

/**
 * @author Liuguipeng
 * @des 积分订单填写
 */
public class IntegrationIndentWriteActivity extends BaseActivity {
    @BindView(R.id.tv_header_title)
    TextView tvHeaderTitle;
    @BindView(R.id.tv_header_shared)
    TextView tvHeaderShared;
    @BindView(R.id.tl_normal_bar)
    Toolbar tlNormalBar;
    @BindView(R.id.ll_indent_address_empty_default)
    LinearLayout ll_indent_address_empty_default;
    //    进关信息提示
    @BindView(R.id.tv_oversea_buy_tint)
    TextView tvOverseaBuyTint;
    @BindView(R.id.ll_indent_address_null)
    LinearLayout llIndentAddressNull;

    @BindView(R.id.tv_consignee_name)
    TextView tvConsigneeName;
    @BindView(R.id.tv_consignee_mobile_number)
    TextView tvConsigneeMobileNumber;
    @BindView(R.id.tv_indent_details_address)
    TextView tvIndentDetailsAddress;
    @BindView(R.id.ll_indent_address_default)
    LinearLayout llIndentAddressDefault;
    //进关信息
    @BindView(R.id.ll_oversea_info)
    LinearLayout llOverseaInfo;

    @BindView(R.id.iv_integral_product_image)
    ImageView ivIntegralProductImage;
    @BindView(R.id.tv_integral_product_name)
    TextView tvIntegralProductName;
    @BindView(R.id.tv_integral_sku_value)
    TextView tvIntegralSkuValue;
    @BindView(R.id.tv_integral_product_count)
    TextView tv_integral_product_count;
    @BindView(R.id.tv_integral_product_price)
    TextView tvIntegralProductPrice;
    @BindView(R.id.edt_integral_product_note)
    EditText edtIntegralProductNote;
    //    订单结算信息
    @BindView(R.id.rv_integral_write_info)
    RecyclerView rvIntegralWriteInfo;

    @BindView(R.id.ll_integral_pay_way)
    LinearLayout ll_integral_pay_way;
    @BindView(R.id.rb_checked_alipay)
    RadioButton rbCheckedAlipay;
    @BindView(R.id.rb_checked_wechat_pay)
    RadioButton rbCheckedWechatPay;
    @BindView(R.id.rb_checked_union_pay)
    RadioButton rbCheckedUnionPay;

    @BindView(R.id.tv_integral_details_price)
    TextView tvIntegralDetailsPrice;
    @BindView(R.id.tv_integral_details_create_int)
    TextView tvIntegralDetailsCreateInt;
    private final int NEW_CREATE_ADDRESS_REQ = 101;
    private final int SEL_ADDRESS_REQ = 102;
    private int addressId;
    private boolean isFirst = true;
    private ShopCarGoodsSku shopCarGoodsSku;
    //    订单价格优惠列表
    private List<PriceInfoBean> priceInfoList = new ArrayList<>();
    private IndentDiscountAdapter indentDiscountAdapter;
    private IntegralSettlementBean integralSettlementBean;
    private String payType;
    private String orderCreateNo;
    private UnionPay unionPay;

    @Override
    protected int getContentView() {
        return R.layout.activity_indent_write_integration;
    }

    @Override
    protected void initViews() {
        getLoginStatus(this);
        tlNormalBar.setSelected(true);
        tvHeaderTitle.setText("确认订单");
        tvHeaderShared.setVisibility(View.GONE);
        tvOverseaBuyTint.setVisibility(View.GONE);
        llOverseaInfo.setVisibility(View.GONE);
        rvIntegralWriteInfo.setVisibility(View.GONE);
        ll_integral_pay_way.setVisibility(View.GONE);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            shopCarGoodsSku = bundle.getParcelable("integralProduct");
        }
        if (shopCarGoodsSku == null) {
            showToast(this, "数据缺失，请刷新重试");
            finish();
            return;
        }
        setIntegralData();
        rvIntegralWriteInfo.setLayoutManager(new LinearLayoutManager(this));
        indentDiscountAdapter = new IndentDiscountAdapter(priceInfoList);
        rvIntegralWriteInfo.setAdapter(indentDiscountAdapter);
        KeyboardUtils.registerSoftInputChangedListener(this, new KeyboardUtils.OnSoftInputChangedListener() {
            @Override
            public void onSoftInputChanged(int height) {
                if (height == 0) {
                    ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0).requestFocus();
                }
            }
        });
    }

    @Override
    protected void loadData() {
        if (shopCarGoodsSku.getIntegralProductType() == 1) {
            ll_indent_address_empty_default.setVisibility(View.GONE);
            getProductSettlementInfo();
        } else {
            getAddress();
        }
    }

    //获取默认地址（取地址列表的第一个）
    private void getAddress() {
        Map<String, Object> params = new HashMap<>();
        params.put("uid", userId);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, ADDRESS_LIST, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                AddressListEntity addressListEntity = gson.fromJson(result, AddressListEntity.class);
                if (addressListEntity != null) {
                    if (addressListEntity.getCode().equals(SUCCESS_CODE)) {
                        List<AddressInfoBean> addressAllBeanList = addressListEntity.getAddressAllBeanList();
                        if (addressAllBeanList != null && addressAllBeanList.size() > 0) {
                            setAddressData(addressAllBeanList.get(0));
                        }
                    } else if (addressListEntity.getCode().equals(EMPTY_CODE)) {
                        setAddressData(null);
                    } else {
                        showImportantToast(getActivity(), addressListEntity.getMsg());
                    }
                }
            }
        });
    }

    private void setAddressData(AddressInfoEntity.AddressInfoBean addressInfoBean) {
        if (addressInfoBean != null) {
            addressId = addressInfoBean.getId();
            llIndentAddressDefault.setVisibility(VISIBLE);
            llIndentAddressNull.setVisibility(View.GONE);
            tvConsigneeName.setText(addressInfoBean.getConsignee());
            tvConsigneeMobileNumber.setText(addressInfoBean.getMobile());
            tvIndentDetailsAddress.setText((addressInfoBean.getAddress_com() + addressInfoBean.getAddress() + " "));
            getProductSettlementInfo();
        } else {
            llIndentAddressDefault.setVisibility(View.GONE);
            llIndentAddressNull.setVisibility(VISIBLE);
        }
    }

    /**
     * 获取结算信息
     */
    private void getProductSettlementInfo() {
        String url = Url.INTEGRAL_DIRECT_SETTLEMENT;
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        if (addressId > 0) {
            params.put("addressId", addressId);
        }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("saleSkuId", shopCarGoodsSku.getSaleSkuId());
            jsonObject.put("id", shopCarGoodsSku.getProductId());
            jsonObject.put("count", shopCarGoodsSku.getCount());
            JSONArray jsonArray = new JSONArray();
            jsonArray.put(jsonObject);
            params.put("goods", jsonArray.toString().trim());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        NetLoadUtils.getNetInstance().loadNetDataPost(this, url, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                IntegralSettlementEntity settlementEntity = gson.fromJson(result, IntegralSettlementEntity.class);
                if (settlementEntity != null) {
                    if (settlementEntity.getCode().equals(SUCCESS_CODE)) {
                        integralSettlementBean = settlementEntity.getIntegralSettlementBean();
                        setIntegralSettlementInfo(integralSettlementBean);
                    }
                }
            }

            @Override
            public void onError(Throwable throwable) {
                showToast(IntegrationIndentWriteActivity.this, R.string.connectedFaile);
            }

            @Override
            public void netClose() {
                showToast(IntegrationIndentWriteActivity.this, R.string.unConnectedNetwork);
            }
        });
    }

    /**
     * 设置结算信息
     *
     * @param integralSettlementBean 结算数据
     */
    private void setIntegralSettlementInfo(IntegralSettlementBean integralSettlementBean) {
        List<PriceInfoBean> indentDiscountsList = integralSettlementBean.getPriceInfo();
        if (indentDiscountsList != null) {
            rvIntegralWriteInfo.setVisibility(VISIBLE);
            priceInfoList.clear();
            priceInfoList.addAll(indentDiscountsList);
            PriceInfoBean priceInfoBean = priceInfoList.get(priceInfoList.size() - 1);
            tvIntegralDetailsPrice.setText(getStrings(priceInfoBean.getTotalPriceName()));
            priceInfoList.remove(priceInfoList.get(priceInfoList.size() - 1));
            indentDiscountAdapter.setNewData(priceInfoList);
        } else {
            rvIntegralWriteInfo.setVisibility(View.GONE);
        }
        if (integralSettlementBean.getTotalPrice() > 0) {
            ll_integral_pay_way.setVisibility(VISIBLE);
        } else {
            ll_integral_pay_way.setVisibility(View.GONE);
        }
    }

    /**
     * 设置积分商品信息
     */
    private void setIntegralData() {
        if (shopCarGoodsSku != null) {
            GlideImageLoaderUtil.loadCenterCrop(this, ivIntegralProductImage, shopCarGoodsSku.getPicUrl());
            tvIntegralProductName.setText(getStringFilter(shopCarGoodsSku.getIntegralName()));
            tvIntegralSkuValue.setText(getStringFilter(shopCarGoodsSku.getValuesName()));
            tv_integral_product_count.setText(String.format(getResources().getString(R.string.integral_lottery_award_count), shopCarGoodsSku.getCount()));
            String priceName;
            if (shopCarGoodsSku.getIntegralType() == 0) {
                priceName = String.format(getResources().getString(R.string.integral_indent_product_price), (int) shopCarGoodsSku.getPrice());
            } else {
                priceName = String.format(getResources().getString(R.string.integral_product_and_price)
                        , (int) shopCarGoodsSku.getPrice(), getStrings(shopCarGoodsSku.getMoneyPrice()));
            }
            tvIntegralProductPrice.setText(priceName);
            Pattern p = Pattern.compile(REGEX_NUM);
            Link redNum = new Link(p);
            //        @用户昵称
            redNum.setTextColor(getResources().getColor(R.color.text_normal_red));
            redNum.setUnderlined(false);
            redNum.setTextSize(AutoSizeUtils.mm2px(mAppContext, 32));
            redNum.setHighlightAlpha(0f);
            LinkBuilder.on(tvIntegralProductPrice)
                    .addLink(redNum)
                    .build();
        }
    }

    @OnClick(R.id.tv_life_back)
    void goBack() {
        finish();
    }


    //  确定兑换
    @OnClick({R.id.tv_integral_details_create_int})
    void confirmExchange() {
        if (integralSettlementBean != null) {
            if (shopCarGoodsSku.getIntegralProductType() == 1) {
                createSettlementIndent();
            } else {
                if (addressId != 0) {
                    createSettlementIndent();
                } else {
                    showToast(this, "地址未填写,请重新填写地址");
                }
            }
        }
    }

    //创建订单
    private void createSettlementIndent() {
        List<ProductInfoBean> productInfoBeans = integralSettlementBean.getProductInfo();
        if (productInfoBeans == null || productInfoBeans.size() < 1) {
            return;
        }
        ProductInfoBean productInfoBean = productInfoBeans.get(0);
        tvIntegralDetailsCreateInt.setEnabled(false);
        String message = edtIntegralProductNote.getText().toString().trim();
        //  创建订单
        String url = Url.INTEGRAL_CREATE_INDENT;
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        if (addressId > 0) {
            params.put("userAddressId", addressId);
        }
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", productInfoBean.getId());
            jsonObject.put("saleSkuId", productInfoBean.getSaleSkuId());
            jsonObject.put("count", productInfoBean.getCount());
            JSONArray jsonArray = new JSONArray();
            jsonArray.put(jsonObject);
            params.put("goods", jsonArray.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (!TextUtils.isEmpty(message)) {
            params.put("remark", message);
        }
//                结算金额 1 积分+金额 0 纯积分
        params.put("integralType", integralSettlementBean.getTotalPrice() > 0 ? 1 : 0);
        if (integralSettlementBean.getTotalPrice() > 0) {
            if (rbCheckedAlipay.isChecked() ||
                    rbCheckedWechatPay.isChecked() ||
                    rbCheckedUnionPay.isChecked()) {
                if (rbCheckedAlipay.isChecked()) {
                    //  调起支付宝支付
                    payType = PAY_ALI_PAY;
                } else if (rbCheckedWechatPay.isChecked()) {
                    //  调起微信支付
                    payType = PAY_WX_PAY;
                } else {
                    //  调起银联支付
                    payType = PAY_UNION_PAY;
                }
                params.put("buyType", payType);
            } else {
                showToast(this, "请选择支付方式");
                return;
            }
        }
//                默认实物
        params.put("integralProductType", shopCarGoodsSku.getIntegralProductType());
        params.put("source", 0);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, url, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                tvIntegralDetailsCreateInt.setEnabled(true);
                Gson gson = new Gson();
                IntegrationIndentEntity indentEntity = gson.fromJson(result, IntegrationIndentEntity.class);
                if (indentEntity != null && indentEntity.getResult() != null) {
                    IntegrationIndentBean indentBean = indentEntity.getResult();
                    if (SUCCESS_CODE.equals(indentEntity.getCode())) {
                        orderCreateNo = indentBean.getNo();
                        String payKey = indentBean.getPayKey();
                        //纯积分兑换
                        if (TextUtils.isEmpty(payType)){
                            if (!TextUtils.isEmpty(orderCreateNo)){
                                skipDirectIndent();
                            }
                        }else {
                            //返回成功，调起微信支付接口
                            switch (payType) {
                                case PAY_WX_PAY:
                                    doWXPay(gson.fromJson(payKey, PayKeyBean.class));
                                    break;
                                case PAY_ALI_PAY:
                                    doAliPay(payKey);
                                    break;
                                case PAY_UNION_PAY:
                                    PayKeyBean payKeyBean = gson.fromJson(payKey, PayKeyBean.class);
                                    unionPay(payKeyBean.getPaymentUrl());
                                    break;
                            }
                        }
                    } else {
                        showToast(IntegrationIndentWriteActivity.this, indentBean == null ?
                                indentEntity.getMsg() : indentBean.getMsg());
                    }
                } else {
                    showToast(IntegrationIndentWriteActivity.this, "创建订单失败，请重新提交订单");
                }
            }

            @Override
            public void onNotNetOrException() {
                tvIntegralDetailsCreateInt.setEnabled(true);
            }

            @Override
            public void onError(Throwable throwable) {
                showToast(IntegrationIndentWriteActivity.this, R.string.do_failed);
            }

            @Override
            public void netClose() {
                showToast(IntegrationIndentWriteActivity.this, R.string.unConnectedNetwork);
            }
        });
    }

    private void doAliPay(String pay_param) {
        new AliPay(this, pay_param, new AliPay.AliPayResultCallBack() {
            @Override
            public void onSuccess() {
                showToast(getApplication(), "支付成功");
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
                cancelIntegralIndent();
            }

            @Override
            public void onCancel() {
                cancelIntegralIndent();
                showToast(getApplication(), "支付取消");
            }
        }).doPay();
    }

    private void doWXPay(PayKeyBean pay_param) {
        WXPay.init(getApplicationContext());//要在支付前调用
        WXPay.getInstance().doPayDateObject(pay_param, new WXPay.WXPayResultCallBack() {
            @Override
            public void onSuccess() {
                showToast(getApplication(), "支付成功");
                skipDirectIndent();
            }

            @Override
            public void onError(int error_code) {
                cancelIntegralIndent();
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
                cancelIntegralIndent();
                showToast(getApplication(), "支付取消");
            }
        });
    }

    /**
     * 银联支付
     */
    private void unionPay(@NonNull String paymentUrl) {
        if (!TextUtils.isEmpty(paymentUrl)) {
            if (loadHud != null) {
                loadHud.show();
            }
            unionPay = new UnionPay(IntegrationIndentWriteActivity.this,
                    paymentUrl, new UnionPay.UnionPayResultCallBack() {
                @Override
                public void onUnionPaySuccess(String webResultValue) {
                    if (loadHud != null) {
                        loadHud.dismiss();
                    }
                    showToast(getApplication(), "支付成功");
                    skipDirectIndent();
                }

                @Override
                public void onUnionPayError(String errorMes) {
                    if (loadHud != null) {
                        loadHud.dismiss();
                    }
                    cancelIntegralIndent();
                }
            });
        } else {
            showToast(IntegrationIndentWriteActivity.this, "缺少重要参数，请选择其它支付渠道！");
        }
    }

    /**
     * 订单取消
     */
    private void cancelIntegralIndent() {
        String url = Url.Q_INDENT_CANCEL;
        Map<String, Object> params = new HashMap<>();
        params.put("no", orderCreateNo);
        params.put("userId", userId);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, url, params, null);
    }

    /**
     * 跳转支付完成页面
     */
    private void skipDirectIndent() {
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(getActivity(), DirectPaySuccessActivity.class);
            intent.putExtra("indentNo", orderCreateNo);
            intent.putExtra(INDENT_PRODUCT_TYPE, INDENT_INTEGRAL_PRODUCT);
            startActivity(intent);
            finish();
        }, 1000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NEW_CREATE_ADDRESS_REQ || requestCode == SEL_ADDRESS_REQ) {
//            获取地址成功
            AddressInfoBean addressInfoBean = data.getParcelableExtra("addressInfoBean");
            isFirst = false;
            setAddressData(addressInfoBean);
        } else if (requestCode == IS_LOGIN_CODE) {
            loadData();
        } else if (requestCode == UNION_RESULT_CODE) {
            if (unionPay != null) {
                String webManualFinish = data.getStringExtra("webManualFinish");
                unionPay.unionPayResult(this, orderCreateNo, webManualFinish);
            } else {
                cancelIntegralIndent();
            }
        }
    }

    //支付宝方式
    @OnClick(value = {R.id.ll_aliPay, R.id.rb_checked_alipay})
    void aliPay() {
        rbCheckedAlipay.setChecked(true);
        rbCheckedWechatPay.setChecked(false);
        rbCheckedUnionPay.setChecked(false);
    }

    //微信支付方式
    @OnClick(value = {R.id.ll_Layout_weChat, R.id.rb_checked_wechat_pay})
    void weChat() {
        rbCheckedWechatPay.setChecked(true);
        rbCheckedAlipay.setChecked(false);
        rbCheckedUnionPay.setChecked(false);
    }

    //        银联支付
    @OnClick(value = {R.id.ll_Layout_union_pay, R.id.rb_checked_union_pay})
    void unionPay() {
        rbCheckedUnionPay.setChecked(true);
        rbCheckedAlipay.setChecked(false);
        rbCheckedWechatPay.setChecked(false);
    }

    //    地址列表为空 跳转新建地址
    @OnClick(R.id.tv_lv_top)
    void skipNewAddress(View view) {
        Intent intent = new Intent(IntegrationIndentWriteActivity.this, SelectedAddressActivity.class);
        startActivityForResult(intent, NEW_CREATE_ADDRESS_REQ);
    }

    //  更换地址  跳转地址列表
    @OnClick(R.id.ll_indent_address_default)
    void skipAddressList() {
        Intent intent = new Intent(IntegrationIndentWriteActivity.this, SelectedAddressActivity.class);
        intent.putExtra("addressId", String.valueOf(addressId));
        startActivityForResult(intent, SEL_ADDRESS_REQ);
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
        return super.dispatchTouchEvent(ev);
    }

    // Return whether touch the view.
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        KeyboardUtils.unregisterSoftInputChangedListener(this);
    }
}
