package com.amkj.dmsh.shopdetails.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.address.activity.SelectedAddressActivity;
import com.amkj.dmsh.address.bean.AddressInfoEntity.AddressInfoBean;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.bean.RequestStatus;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.mine.bean.DepositWriteEntity;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.shopdetails.adapter.IndentDiscountAdapter;
import com.amkj.dmsh.shopdetails.bean.IndentProDiscountBean;
import com.amkj.dmsh.shopdetails.bean.PriceInfoBean;
import com.amkj.dmsh.shopdetails.bean.QualityCreateAliPayIndentBean;
import com.amkj.dmsh.shopdetails.bean.QualityCreateUnionPayIndentEntity;
import com.amkj.dmsh.shopdetails.bean.QualityCreateWeChatPayIndentBean;
import com.amkj.dmsh.shopdetails.integration.bean.AddressListEntity;
import com.amkj.dmsh.shopdetails.payutils.AliPay;
import com.amkj.dmsh.shopdetails.payutils.UnionPay;
import com.amkj.dmsh.shopdetails.payutils.WXPay;
import com.amkj.dmsh.utils.KeyboardUtils;
import com.amkj.dmsh.utils.LifecycleHandler;
import com.amkj.dmsh.utils.TextWatchListener;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.amkj.dmsh.utils.gson.GsonUtils;
import com.amkj.dmsh.views.RectAddAndSubWriteView;
import com.amkj.dmsh.views.alertdialog.AlertDialogHelper;
import com.amkj.dmsh.views.alertdialog.AlertDialogRealName;
import com.google.gson.reflect.TypeToken;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfigC;
import com.luck.picture.lib.entity.LocalMediaC;
import com.tencent.bugly.crashreport.CrashReport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;
import me.jessyan.autosize.utils.AutoSizeUtils;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.dismissLoadhud;
import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.getStringChangeIntegers;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.getStringsChNPrice;
import static com.amkj.dmsh.constant.ConstantMethod.getStringsFormat;
import static com.amkj.dmsh.constant.ConstantMethod.showLoadhud;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.INDENT_PRODUCT_TYPE;
import static com.amkj.dmsh.constant.ConstantVariable.INDENT_PROPRIETOR_PRODUCT;
import static com.amkj.dmsh.constant.ConstantVariable.IS_LOGIN_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.NEW_CRE_ADDRESS_REQ;
import static com.amkj.dmsh.constant.ConstantVariable.PAY_ALI_PAY;
import static com.amkj.dmsh.constant.ConstantVariable.PAY_UNION_PAY;
import static com.amkj.dmsh.constant.ConstantVariable.PAY_WX_PAY;
import static com.amkj.dmsh.constant.ConstantVariable.SEL_ADDRESS_REQ;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.Url.ADDRESS_LIST;
import static com.amkj.dmsh.constant.Url.PAY_CANCEL;

/**
 * Created by xiaoxin on 2020/10/23
 * Version:v4.8.0
 * ClassDescription :预售-定金结算
 */
public class DirectDepositWriteActivity extends BaseActivity {

    @BindView(R.id.tv_life_back)
    TextView mTvLifeBack;
    @BindView(R.id.tv_header_title)
    TextView mTvHeaderTitle;
    @BindView(R.id.tv_header_shared)
    TextView mTvHeaderShared;
    @BindView(R.id.tl_normal_bar)
    Toolbar mTlNormalBar;
    @BindView(R.id.tv_lv_top)
    TextView mTvLvTop;
    @BindView(R.id.ll_indent_address_null)
    LinearLayout mLlIndentAddressNull;
    @BindView(R.id.tv_consignee_name)
    TextView mTvConsigneeName;
    @BindView(R.id.tv_consignee_mobile_number)
    TextView mTvConsigneeMobileNumber;
    @BindView(R.id.tv_indent_details_address)
    TextView mTvIndentDetailsAddress;
    @BindView(R.id.img_skip_address)
    ImageView mImgSkipAddress;
    @BindView(R.id.ll_indent_address_default)
    LinearLayout mLlIndentAddressDefault;
    @BindView(R.id.iv_direct_indent_pro)
    ImageView mIvDirectIndentPro;
    @BindView(R.id.tv_move)
    TextView mTvMove;
    @BindView(R.id.rl_cover)
    RelativeLayout mRlCover;
    @BindView(R.id.tv_direct_indent_pro_name)
    TextView mTvDirectIndentProName;
    @BindView(R.id.tv_direct_indent_pro_sku)
    TextView mTvDirectIndentProSku;
    @BindView(R.id.tv_direct_pro_count)
    TextView mTvDirectProCount;
    @BindView(R.id.ll_sku)
    LinearLayout mLlSku;
    @BindView(R.id.tv_direct_indent_pro_price)
    TextView mTvDirectIndentProPrice;
    @BindView(R.id.tv_dir_indent_pro_status)
    TextView mTvDirIndentProStatus;
    @BindView(R.id.fl_dir_indent_pro_status)
    FrameLayout mFlDirIndentProStatus;
    @BindView(R.id.tv_indent_pro_refund_price)
    TextView mTvIndentProRefundPrice;
    @BindView(R.id.iv_indent_pro_use_cp)
    ImageView mIvIndentProUseCp;
    @BindView(R.id.tv_indent_write_reason)
    TextView mTvIndentWriteReason;
    @BindView(R.id.ll_product)
    LinearLayout mLlProduct;
    @BindView(R.id.edt_direct_product_note)
    EditText mEdtDirectProductNote;
    @BindView(R.id.ll_indent_product_note)
    LinearLayout mLlIndentProductNote;
    @BindView(R.id.rv_indent_write_info)
    RecyclerView mRvIndentWriteInfo;
    @BindView(R.id.rb_checked_alipay)
    RadioButton mRbCheckedAlipay;
    @BindView(R.id.rb_checked_wechat_pay)
    RadioButton mRbCheckedWechatPay;
    @BindView(R.id.rb_checked_union_pay)
    RadioButton mRbCheckedUnionPay;
    @BindView(R.id.radio_group)
    RadioGroup mRadioGroup;
    @BindView(R.id.img_aliPay)
    ImageView mImgAliPay;
    @BindView(R.id.tv_item_alipay)
    TextView mTvItemAlipay;
    @BindView(R.id.ll_aliPay)
    LinearLayout mLlAliPay;
    @BindView(R.id.img_weChat_way)
    ImageView mImgWeChatWay;
    @BindView(R.id.tv_item_wechat_pay)
    TextView mTvItemWechatPay;
    @BindView(R.id.ll_Layout_weChat)
    LinearLayout mLlLayoutWeChat;
    @BindView(R.id.img_union_way)
    ImageView mImgUnionWay;
    @BindView(R.id.tv_item_union_pay)
    TextView mTvItemUnionPay;
    @BindView(R.id.ll_Layout_union_pay)
    LinearLayout mLlLayoutUnionPay;
    @BindView(R.id.ll_pay_way)
    LinearLayout mLlPayWay;
    @BindView(R.id.tv_indent_total_price)
    TextView mTvIndentTotalPrice;
    @BindView(R.id.tv_indent_write_commit)
    TextView mTvIndentWriteCommit;
    @BindView(R.id.ll_write_commit)
    LinearLayout mLlWriteCommit;
    @BindView(R.id.ll_indent_details)
    LinearLayout mLlIndentDetails;
    @BindView(R.id.rect_indent_number)
    RectAddAndSubWriteView mRectIndentNumber;
    @BindView(R.id.ll_layout_coupon)
    LinearLayout mLlLayoutCoupon;
    @BindView(R.id.tv_oversea_buy_tint)
    TextView mTvOverseaBuyTint;
    private AlertDialogHelper payCancelDialogHelper;
    private String payWay = PAY_ALI_PAY;//默认支付宝付款
    private int addressId;
    private String orderId = "";//创建订单 未结算
    private IndentDiscountAdapter indentDiscountAdapter;
    private List<PriceInfoBean> mPriceInfoList = new ArrayList<>();
    //    订单优惠信息必传参数
    private List<IndentProDiscountBean> discountBeanList = new ArrayList<>();
    private QualityCreateWeChatPayIndentBean qualityWeChatIndent;
    private QualityCreateAliPayIndentBean qualityAliPayIndent;
    private QualityCreateUnionPayIndentEntity qualityUnionIndent;
    private String mGoodsJson;
    private DepositWriteEntity mDeoisitWriteEntity;
    private boolean first = true;
    private int mNum = 1;
    private boolean isReal;
    private AlertDialogRealName mAlertDialogRealName;
    private String orderCreateNo;
    private String productOrder;
    private AlertDialogHelper mAlertDialogRealNameDiffer;
    private AlertDialogHelper mAlertDialogRealNameError;

    @Override
    protected int getContentView() {
        return R.layout.activity_deposit_write;
    }

    @Override
    protected void initViews() {
        getLoginStatus(this);
        if (loadHud != null) {
            loadHud.setCancellable(false);
        }

        mGoodsJson = getIntent().getStringExtra("goods");
        if (TextUtils.isEmpty(mGoodsJson)) {
            showToast("数据有误，请重试");
            finish();
        } else {
            discountBeanList = GsonUtils.fromJson(mGoodsJson, new TypeToken<List<IndentProDiscountBean>>() {
            }.getType());
        }

        mTvHeaderTitle.setText("确认订单");
        mTvIndentWriteCommit.setText("支付订金");
        mTvHeaderShared.setVisibility(GONE);
        mLlLayoutCoupon.setVisibility(GONE);
        //初始化金额明细
        mRvIndentWriteInfo.setLayoutManager(new LinearLayoutManager(getActivity()));
        indentDiscountAdapter = new IndentDiscountAdapter(this, mPriceInfoList);
        mRvIndentWriteInfo.setAdapter(indentDiscountAdapter);
        //支付方式监听
        mRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rb_checked_alipay) {
                payWay = PAY_ALI_PAY;
            } else if (checkedId == R.id.rb_checked_wechat_pay) {
                payWay = PAY_WX_PAY;
            } else if (checkedId == R.id.rb_checked_union_pay) {
                payWay = PAY_UNION_PAY;
            }
        });

        //监听数量修改
        ((EditText) mRectIndentNumber.findViewById(R.id.tv_integration_details_credits_count)).addTextChangedListener(new TextWatchListener() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mNum = getStringChangeIntegers(s.toString());
            }
        });
        mRectIndentNumber.setOnNumChangeListener(new RectAddAndSubWriteView.OnNumChangeListener() {
            @Override
            public void onNumChange(View view, int stype, int num) {
                if (discountBeanList.size() > 0 && discountBeanList.get(0).getCount() != num) {
                    if (loadHud != null) {
                        loadHud.show();
                    }
                    discountBeanList.get(0).setCount(num);
                    getIndentDiscounts();
                }
            }

            @Override
            public void onMaxQuantity(View view, int num) {
                showToast(R.string.product_sell_out);
            }
        });

        //监听软键盘
        KeyboardUtils.registerSoftInputChangedListener(this, new KeyboardUtils.OnSoftInputChangedListener() {
            @Override
            public void onSoftInputChanged(int height) {
                try {
                    if (!first) {//默认会调用一次
                        if (height == 0) {
                            //软键盘隐藏
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, AutoSizeUtils.mm2px(mAppContext, 98));
                            mLlWriteCommit.setLayoutParams(params);
                            if (mNum <= 0) {//最小购买数量为1件
                                mNum = 1;
                                mRectIndentNumber.setNum(mNum);
                            } else if (mNum > mRectIndentNumber.getMaxNum()) {
                                mNum = mRectIndentNumber.getMaxNum();
                                mRectIndentNumber.setNum(mNum);
                                showToast(R.string.product_sell_out);
                            }
                            if (discountBeanList.size() > 0 && discountBeanList.get(0).getCount() != mNum) {
                                if (loadHud != null) {
                                    loadHud.show();
                                }
                                discountBeanList.get(0).setCount(mNum);
                                getIndentDiscounts();
                            }
                        } else {
                            //软键盘显示
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);
                            mLlWriteCommit.setLayoutParams(params);
                        }
                    }
                    first = false;
                } catch (Exception e) {
                    CrashReport.postCatchedException(new Exception("订单填写手动修改数量异常：" + e.getMessage()));
                }
            }
        });
    }

    @Override
    protected void loadData() {
        if (userId > 0) {
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
                AddressListEntity addressListEntity = GsonUtils.fromJson(result, AddressListEntity.class);
                if (addressListEntity != null) {
                    if (addressListEntity.getCode().equals(SUCCESS_CODE)) {
                        List<AddressInfoBean> addressAllBeanList = addressListEntity.getAddressAllBeanList();
                        if (addressAllBeanList != null && addressAllBeanList.size() > 0) {
                            setAddressData(addressAllBeanList.get(0));
                        }
                    } else if (addressListEntity.getCode().equals(EMPTY_CODE)) {
                        setAddressData(null);
                    } else {
                        showToast(addressListEntity.getMsg());
                    }
                }
            }
        });
    }

    //更新地址信息
    private void setAddressData(AddressInfoBean addressInfoBean) {
        if (addressInfoBean != null) {
            addressId = addressInfoBean.getId();
            mLlIndentAddressDefault.setVisibility(VISIBLE);
            mLlIndentAddressNull.setVisibility(GONE);
            mTvConsigneeName.setText(addressInfoBean.getConsignee());
            mTvConsigneeMobileNumber.setText(addressInfoBean.getMobile());
            mTvIndentDetailsAddress.setText((addressInfoBean.getAddress_com() + addressInfoBean.getAddress() + " "));
        } else {
            mLlIndentAddressDefault.setVisibility(GONE);
            mLlIndentAddressNull.setVisibility(VISIBLE);
        }

        getIndentDiscounts();
    }

    /**
     * 获取订单结算信息
     */
    private void getIndentDiscounts() {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("count", mNum);
        params.put("addressId", addressId);
        params.put("goods", GsonUtils.toJson(discountBeanList));
        NetLoadUtils.getNetInstance().loadNetDataPost(this, Url.GET_DEPOSIT_SETTLE_INFO, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                dismissLoadhud(getActivity());
                mPriceInfoList.clear();
                mDeoisitWriteEntity = GsonUtils.fromJson(result, DepositWriteEntity.class);
                if (mDeoisitWriteEntity != null) {
                    if (mDeoisitWriteEntity.getCode().equals(SUCCESS_CODE)) {
                        //实名制相关
                        isReal = mDeoisitWriteEntity.isReal();
                        mRectIndentNumber.setMaxNum(mDeoisitWriteEntity.getQuantity());
                        mTvIndentTotalPrice.setText(getStringsChNPrice(getActivity(), mDeoisitWriteEntity.getDepositTotal()));
                        setOverseaData();
                        showProductInfo();
                        showPriceInfo();
                        showPayType(mDeoisitWriteEntity.getShowPayTypeList());
                    } else {
                        showToast(mDeoisitWriteEntity.getMsg());
                    }
                    indentDiscountAdapter.notifyDataSetChanged();
                }

                NetLoadUtils.getNetInstance().showLoadSir(loadService, mDeoisitWriteEntity);
            }

            @Override
            public void onNotNetOrException() {
                dismissLoadhud(getActivity());
                NetLoadUtils.getNetInstance().showLoadSir(loadService, mDeoisitWriteEntity);
            }
        });
    }

    //顶部通知
    private void setOverseaData() {
        if (!TextUtils.isEmpty(mDeoisitWriteEntity.getPrompt())) {
            mTvOverseaBuyTint.setVisibility(VISIBLE);
            mTvOverseaBuyTint.setText(getStrings(mDeoisitWriteEntity.getPrompt()));
        } else {
            mTvOverseaBuyTint.setVisibility(GONE);
        }
    }

    //展示价格明细
    private void showPriceInfo() {
        PriceInfoBean deposit = new PriceInfoBean("定金", "", getStringsChNPrice(getActivity(), mDeoisitWriteEntity.getDeposit()));
        PriceInfoBean orderPriceTotal = new PriceInfoBean("尾款", getStringsFormat(getActivity(), R.string.payment_start_time, mDeoisitWriteEntity.getOrderPayStartTime()), getStringsChNPrice(getActivity(), mDeoisitWriteEntity.getOrderPriceTotal()));
        PriceInfoBean depositTotal = new PriceInfoBean("小计", "", getStringsChNPrice(getActivity(), mDeoisitWriteEntity.getDepositTotal()));
        mPriceInfoList.add(deposit);
        mPriceInfoList.add(orderPriceTotal);
        mPriceInfoList.add(depositTotal);
    }

    //展示商品信息
    private void showProductInfo() {
        GlideImageLoaderUtil.loadCenterCrop(getActivity(), mIvDirectIndentPro, mDeoisitWriteEntity.getPicUrl());
        mTvDirectIndentProName.setText(getStrings(mDeoisitWriteEntity.getTitle()));
        mTvDirectIndentProPrice.setText(getStringsChNPrice(this, mDeoisitWriteEntity.getPrice()));
        mTvDirectProCount.setText("x" + mDeoisitWriteEntity.getCount());
    }

    //支付方式
    private void showPayType(List<String> showPayTypeList) {
        if (showPayTypeList != null && showPayTypeList.size() > 0) {
            //微信
            if (!showPayTypeList.contains("1")) {
                mRadioGroup.getChildAt(1).setVisibility(GONE);
                mLlPayWay.getChildAt(1).setVisibility(GONE);
            }
            //支付宝
            if (!showPayTypeList.contains("2")) {
                mRadioGroup.getChildAt(0).setVisibility(GONE);
                mLlPayWay.getChildAt(0).setVisibility(GONE);
            }
            //银联
            if (!showPayTypeList.contains("3")) {
                mRadioGroup.getChildAt(2).setVisibility(GONE);
                mLlPayWay.getChildAt(2).setVisibility(GONE);
            }

            String payType = showPayTypeList.get(0);
            if ("1".equals(payType)) {
                payWay = PAY_WX_PAY;
                ((RadioButton) mRadioGroup.getChildAt(1)).setChecked(true);
            } else if ("2".equals(payType)) {
                payWay = PAY_ALI_PAY;
                ((RadioButton) mRadioGroup.getChildAt(0)).setChecked(true);
            } else if ("3".equals(payType)) {
                payWay = PAY_UNION_PAY;
                ((RadioButton) mRadioGroup.getChildAt(2)).setChecked(true);
            }
        }
    }

    @OnClick({R.id.tv_life_back, R.id.tv_indent_write_commit, R.id.tv_lv_top, R.id.ll_indent_address_default})
    public void onViewClicked(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.tv_life_back:
                payCancel();
                break;
            case R.id.tv_indent_write_commit:
                goExchange();
                break;
            case R.id.tv_lv_top:
                intent = new Intent(getActivity(), SelectedAddressActivity.class);
                intent.putExtra("hasDefaultAddress", false);
                startActivityForResult(intent, NEW_CRE_ADDRESS_REQ);
                break;
            case R.id.ll_indent_address_default:
                if (TextUtils.isEmpty(orderId)) {
                    intent = new Intent(getActivity(), SelectedAddressActivity.class);
                    intent.putExtra("addressId", String.valueOf(addressId));
                    startActivityForResult(intent, SEL_ADDRESS_REQ);
                }
                break;
        }
    }

    private void goExchange() {
        if (userId > 0) {
            //第一次提交订单
            if (addressId == 0) {
                showToast("收货地址为空");
            } else if (TextUtils.isEmpty(payWay)) {
                showToast("请选择支付方式");
            } else if (isReal && (TextUtils.isEmpty(mDeoisitWriteEntity.getIdcardImg1()) || TextUtils.isEmpty(mDeoisitWriteEntity.getIdcardImg2()))) {
                //如果需要实名并且用户还没有完善实名信息
                showAlertDialogRealName();
            } else if (!TextUtils.isEmpty(mGoodsJson)) {
                createIndent();
            } else {
                showToast("商品数据错误");
            }
        } else {
            getLoginStatus(this);
        }
    }

    private void showAlertDialogRealName() {
        if (mAlertDialogRealName == null) {
            mAlertDialogRealName = new AlertDialogRealName(this, mDeoisitWriteEntity);
        }

        //直接提交订单
        mAlertDialogRealName.setOnCommitListener((name, idcard, idcardImg1, idcardImg2) -> {
            if (mDeoisitWriteEntity != null) {
                mDeoisitWriteEntity.setRealName(name);
                mDeoisitWriteEntity.setIdCard(idcard);
                mDeoisitWriteEntity.setIdcardImg1(idcardImg1);
                mDeoisitWriteEntity.setIdcardImg2(idcardImg2);
                goExchange();
            }
        });
        mAlertDialogRealName.show(Gravity.BOTTOM);
    }

    /**
     * 创建普通订单
     */
    private void createIndent() {
        showLoadhud(this);
        String message = mEdtDirectProductNote.getText().toString().trim();
        Map<String, Object> params = new HashMap<>();
        params.put("isDeposit", 1);
        //用户ID
        params.put("userId", userId);
        //用户地址
        params.put("userAddressId", addressId);
        if (discountBeanList != null && discountBeanList.size() > 0) {
            params.put("goods", GsonUtils.toJson(discountBeanList));
        }
        //用户留言
        if (!TextUtils.isEmpty(message)) {
            params.put("remark", message);
        }

        //付款方式 2019.1.16 新增银联支付
        params.put("buyType", payWay);
        if (payWay.equals(PAY_UNION_PAY)) {
            params.put("paymentLinkType", 2);
            params.put("isApp", true);
        }

        //实名信息
        if (isReal) {
            params.put("realName", mDeoisitWriteEntity.getRealName());
            params.put("idcard", mDeoisitWriteEntity.getIdCard());
            params.put("idcardImg1", mDeoisitWriteEntity.getIdcardImg1());
            params.put("idcardImg2", mDeoisitWriteEntity.getIdcardImg2());
        }
        //订单来源
        params.put("isWeb", false);
        params.put("source", 0);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, Url.Q_CREATE_INDENT, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                dealingIndentPayResult(result);
            }

            @Override
            public void onNotNetOrException() {
                dismissLoadhud(getActivity());
                showToast(R.string.do_failed);
            }
        });
    }

    /**
     * 处理订单支付数据
     *
     * @param result 处理订单返回数据
     */
    private void dealingIndentPayResult(String result) {
        dismissLoadhud(this);
        if (mAlertDialogRealName != null) {
            mAlertDialogRealName.dismiss();
        }
        if (payWay.equals(PAY_WX_PAY)) {
            qualityWeChatIndent = GsonUtils.fromJson(result, QualityCreateWeChatPayIndentBean.class);
            if (qualityWeChatIndent != null) {
                String msg = qualityWeChatIndent.getResult() != null &&
                        !TextUtils.isEmpty(qualityWeChatIndent.getResult().getMsg()) ?
                        getStrings(qualityWeChatIndent.getResult().getMsg()) :
                        getStrings(qualityWeChatIndent.getMsg());
                if (qualityWeChatIndent.getCode().equals(SUCCESS_CODE)) {
                    //返回成功，调起微信支付接口
                    orderCreateNo = qualityWeChatIndent.getResult().getNo();
                    productOrder = qualityWeChatIndent.getResult().getProductOrder();
                    doWXPay(qualityWeChatIndent.getResult());
                } else if ("75".equals(qualityWeChatIndent.getCode())) {//实名信息与收件人不一致
                    showRealNameDiffer(msg);
                } else if ("72".equals(qualityWeChatIndent.getCode())) {//实名信息有误
                    showRealNameError(msg);
                } else {
                    showToast(msg);
//                            赠品送完刷新数据
                    if (qualityWeChatIndent.getResult() != null) {
                        presentStatusUpdate(qualityWeChatIndent.getResult().getCode());
                    }
                }
            }
        } else if (payWay.equals(PAY_ALI_PAY)) {
            qualityAliPayIndent = GsonUtils.fromJson(result, QualityCreateAliPayIndentBean.class);
            if (qualityAliPayIndent != null) {
                String msg = qualityAliPayIndent.getResult() != null &&
                        !TextUtils.isEmpty(qualityAliPayIndent.getResult().getMsg()) ?
                        getStrings(qualityAliPayIndent.getResult().getMsg()) :
                        getStrings(qualityAliPayIndent.getMsg());
                if (qualityAliPayIndent.getCode().equals(SUCCESS_CODE)) {
                    //返回成功，调起支付宝支付接口
                    orderCreateNo = qualityAliPayIndent.getResult().getNo();
                    productOrder = qualityAliPayIndent.getResult().getProductOrder();
                    doAliPay(qualityAliPayIndent.getResult());
                } else if ("75".equals(qualityAliPayIndent.getCode())) {
                    showRealNameDiffer(msg);
                } else if ("72".equals(qualityAliPayIndent.getCode())) {
                    showRealNameError(msg);
                } else {
                    //                            赠品送完刷新数据
                    if (qualityAliPayIndent.getResult() != null) {
                        presentStatusUpdate(qualityAliPayIndent.getResult().getCode());
                    }
                    showToast(msg);
                }
            }
        } else if (payWay.equals(PAY_UNION_PAY)) {
            qualityUnionIndent = GsonUtils.fromJson(result, QualityCreateUnionPayIndentEntity.class);
            if (qualityUnionIndent != null) {
                String msg = qualityUnionIndent.getQualityCreateUnionPayIndent() != null &&
                        !TextUtils.isEmpty(qualityUnionIndent.getQualityCreateUnionPayIndent().getMsg()) ?
                        getStrings(qualityUnionIndent.getQualityCreateUnionPayIndent().getMsg()) :
                        getStrings(qualityUnionIndent.getMsg());
                if (SUCCESS_CODE.equals(qualityUnionIndent.getCode())) {
                    //返回成功，调起银联支付接口
                    orderCreateNo = qualityUnionIndent.getQualityCreateUnionPayIndent().getNo();
                    productOrder = qualityUnionIndent.getQualityCreateUnionPayIndent().getProductOrder();
                    unionPay(qualityUnionIndent);
                } else if ("75".equals(qualityUnionIndent.getCode())) {
                    showRealNameDiffer(msg);
                } else if ("72".equals(qualityUnionIndent.getCode())) {
                    showRealNameError(msg);
                } else {
                    showToast(msg);
//                            赠品送完刷新数据
                    if (qualityUnionIndent.getQualityCreateUnionPayIndent() != null) {
                        presentStatusUpdate(qualityUnionIndent.getQualityCreateUnionPayIndent().getCode());
                    }
                }
            }
        }
    }

    //收件人与实名信息不一致
    private void showRealNameDiffer(String msg) {
        if (mAlertDialogRealNameDiffer == null) {
            mAlertDialogRealNameDiffer = new AlertDialogHelper(this);
            mAlertDialogRealNameDiffer.setCancelText("重新实名认证")
                    .setConfirmText("同意")
                    .setAlertListener(new AlertDialogHelper.AlertConfirmCancelListener() {
                        @Override
                        public void confirm() {
                            Intent intent = new Intent(getActivity(), SelectedAddressActivity.class);
                            intent.putExtra("addressId", String.valueOf(addressId));
                            startActivityForResult(intent, SEL_ADDRESS_REQ);
                        }

                        @Override
                        public void cancel() {
                            showAlertDialogRealName();
                        }
                    });
        }
        mAlertDialogRealNameDiffer.setMsg(msg);
        mAlertDialogRealNameDiffer.show();
    }

    //实名信息有误
    private void showRealNameError(String msg) {
        if (mAlertDialogRealNameError == null) {
            mAlertDialogRealNameError = new AlertDialogHelper(this);
            mAlertDialogRealNameError.setSingleButton(true)
                    .setConfirmText("重新实名认证")
                    .setAlertListener(new AlertDialogHelper.AlertConfirmCancelListener() {
                        @Override
                        public void confirm() {
                            showAlertDialogRealName();
                        }

                        @Override
                        public void cancel() {

                        }
                    });
        }
        mAlertDialogRealNameError.setMsg(msg);
        mAlertDialogRealNameError.show();
    }


    /**
     * 创建订单返回状态
     */
    private void presentStatusUpdate(String codeStatus) {
        try {
//            赠品赠完
            if ("10023".equals(codeStatus)) {
                getIndentDiscounts();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void doAliPay(QualityCreateAliPayIndentBean.ResultBean resultBean) {
        new AliPay(this, resultBean.getNo(), resultBean.getPayKey(), new AliPay.AliPayResultCallBack() {
            @Override
            public void onSuccess() {
                showToast("支付成功");
                skipDirectIndent();
            }

            @Override
            public void onDealing() {
                showToast("支付处理中...");
            }

            @Override
            public void onError(int error_code) {
                switch (error_code) {
                    case AliPay.ERROR_RESULT:
                        showToast("支付失败:支付结果解析错误");
                        break;
                    case AliPay.ERROR_NETWORK:
                        showToast("支付失败:网络连接错误");
                        break;
                    case AliPay.ERROR_PAY:
                        showToast("支付错误:支付码支付失败");
                        break;
                    default:
                        showToast("支付错误");
                        break;
                }
                skipIndentDetail();
            }

            @Override
            public void onCancel() {
                skipIndentDetail();
                showToast("支付取消");
            }
        }).doPay();
    }

    private void doWXPay(QualityCreateWeChatPayIndentBean.ResultBean resultBean) {
        WXPay.init(this);//要在支付前调用
        WXPay.getInstance().doPayDateObject(resultBean.getNo(), resultBean.getPayKey(), new WXPay.WXPayResultCallBack() {
            @Override
            public void onSuccess() {
                showToast("支付成功");
                skipDirectIndent();
            }

            @Override
            public void onError(int error_code) {
                switch (error_code) {
                    case WXPay.NO_OR_LOW_WX:
                        showToast("未安装微信或微信版本过低");
                        break;
                    case WXPay.ERROR_PAY_PARAM:
                        showToast("参数错误");
                        break;
                    case WXPay.ERROR_PAY:
                        showToast("支付失败");
                        break;
                }
//                payError();
                skipIndentDetail();
            }

            @Override
            public void onCancel() {
                skipIndentDetail();
                showToast("支付取消");
            }
        });
    }

    private void unionPay(@NonNull QualityCreateUnionPayIndentEntity qualityUnionIndent) {
        if (qualityUnionIndent.getQualityCreateUnionPayIndent().getPayKeyBean() != null &&
                !TextUtils.isEmpty(qualityUnionIndent.getQualityCreateUnionPayIndent().getPayKeyBean().getPaymentUrl())) {
            if (loadHud != null) {
                loadHud.show();
            }
            //                跳转订单完成页
            UnionPay unionPay = new UnionPay(getActivity(), qualityUnionIndent.getQualityCreateUnionPayIndent().getNo(),
                    qualityUnionIndent.getQualityCreateUnionPayIndent().getPayKeyBean().getPaymentUrl(),
                    new UnionPay.UnionPayResultCallBack() {
                        @Override
                        public void onUnionPaySuccess(String webResultValue) {
                            if (loadHud != null) {
                                loadHud.dismiss();
                            }
                            if (!TextUtils.isEmpty(webResultValue) && "1".equals(webResultValue)) {
                                finish();
                            } else {
                                //跳转订单完成页
                                skipDirectIndent();
                            }
                        }

                        @Override
                        public void onUnionPayError(String errorMes) {
                            if (loadHud != null) {
                                loadHud.dismiss();
                            }
                            showToast(errorMes);
                            skipIndentDetail();
                        }
                    });
        } else {
            showToast("缺少重要参数，请选择其它支付渠道！");
        }
    }

    //支付成功跳转
    private void skipDirectIndent() {
        Intent intent = new Intent(getActivity(), DirectPaySuccessActivity.class);
        intent.putExtra(INDENT_PRODUCT_TYPE, INDENT_PROPRIETOR_PRODUCT);
        intent.putExtra("indentNo", productOrder);
        //延时跳转到支付成功页面（因为线程问题，立即跳转可能会失效）
        new LifecycleHandler(this).postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(intent);
                finish();
            }
        }, 1000);
    }

    //已创建订单，取消支付，跳转到订单详情
    private void skipIndentDetail() {
        if (!TextUtils.isEmpty(productOrder)) {
            Intent intent = new Intent(this, DirectExchangeDetailsActivity.class);
            intent.putExtra("orderNo", productOrder);
            //延时跳转到订单详情页面（因为线程问题，立即跳转可能会失效）
            new LifecycleHandler(this).postDelayed(() -> {
                startActivity(intent);
                finish();
            }, 500);
        }
    }


    /**
     * 订单支付取消弹窗
     */
    private void payCancel() {
        if (payCancelDialogHelper == null) {
            NetLoadUtils.getNetInstance().loadNetDataPost(this, PAY_CANCEL, new NetLoadListenerHelper() {
                @Override
                public void onSuccess(String result) {
                    RequestStatus requestStatus = RequestStatus.objectFromData(result);
                    if (requestStatus != null && SUCCESS_CODE.equals(requestStatus.getCode())) {
                        payCancelDialogHelper = new AlertDialogHelper(getActivity());
                        payCancelDialogHelper.setMsgTextGravity(Gravity.CENTER).setTitleVisibility(GONE)
                                .setMsg(!TextUtils.isEmpty(requestStatus.getDescription()) ?
                                        requestStatus.getDescription() : "好货不等人哦，喜欢就入了吧")
                                .setCancelText("去意已决").setConfirmText("继续支付")
                                .setCancelTextColor(getResources().getColor(R.color.text_login_gray_s))
                                .setCancelable(false);
                        payCancelDialogHelper.setAlertListener(new AlertDialogHelper.AlertConfirmCancelListener() {
                            @Override
                            public void confirm() {
                            }

                            @Override
                            public void cancel() {
                                finish();
                            }
                        });
                        payCancelDialogHelper.show();
                    } else {
                        finish();
                    }
                }

                @Override
                public void onNotNetOrException() {
                    finish();
                }
            });
        } else {
            finish();
        }
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
        switch (requestCode) {
            case IS_LOGIN_CODE:
                loadData();
                break;
            case NEW_CRE_ADDRESS_REQ:
            case SEL_ADDRESS_REQ:
                //获取地址成功
                AddressInfoBean addressInfoBean = data.getParcelableExtra("addressInfoBean");
                setAddressData(addressInfoBean);
                break;
            case PictureConfigC.CHOOSE_REQUEST:
                List<LocalMediaC> localMediaList = PictureSelector.obtainMultipleResult(data);
                if (localMediaList != null && localMediaList.size() > 0) {
                    mAlertDialogRealName.update(localMediaList.get(0).getPath());
                }
                break;
        }
    }


    @Override
    protected boolean isAddLoad() {
        return true;
    }

    @Override
    public View getLoadView() {
        return mLlIndentDetails;
    }
}
