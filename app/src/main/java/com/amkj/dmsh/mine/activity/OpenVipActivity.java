package com.amkj.dmsh.mine.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.mine.adapter.PowerTopAdapter;
import com.amkj.dmsh.mine.adapter.VipGiftAdapter;
import com.amkj.dmsh.mine.bean.PowerEntity;
import com.amkj.dmsh.mine.bean.PowerEntity.PowerBean;
import com.amkj.dmsh.mine.bean.VipSettleInfoEntity;
import com.amkj.dmsh.mine.bean.VipSettleInfoEntity.VipSettleInfoBean.CardListBean;
import com.amkj.dmsh.mine.bean.VipSettleInfoEntity.VipSettleInfoBean.CardListBean.GiftListBean;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.shopdetails.bean.QualityCreateAliPayIndentBean;
import com.amkj.dmsh.shopdetails.bean.QualityCreateUnionPayIndentEntity;
import com.amkj.dmsh.shopdetails.bean.QualityCreateWeChatPayIndentBean;
import com.amkj.dmsh.shopdetails.payutils.AliPay;
import com.amkj.dmsh.shopdetails.payutils.UnionPay;
import com.amkj.dmsh.shopdetails.payutils.WXPay;
import com.amkj.dmsh.utils.LifecycleHandler;
import com.amkj.dmsh.utils.gson.GsonUtils;
import com.google.android.flexbox.FlexboxLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

import static android.view.View.GONE;
import static com.amkj.dmsh.constant.ConstantMethod.dismissLoadhud;
import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.getStringChangeIntegers;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.showLoadhud;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.IS_LOGIN_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.PAY_ALI_PAY;
import static com.amkj.dmsh.constant.ConstantVariable.PAY_UNION_PAY;
import static com.amkj.dmsh.constant.ConstantVariable.PAY_WX_PAY;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;

/**
 * Created by xiaoxin on 2020/7/24
 * Version:v4.7.0
 * ClassDescription :开通会员
 */
public class OpenVipActivity extends BaseActivity {
    @BindView(R.id.tv_life_back)
    TextView mTvLifeBack;
    @BindView(R.id.tv_header_title)
    TextView mTvHeaderTitle;
    @BindView(R.id.tv_header_shared)
    TextView mTvHeaderShared;
    @BindView(R.id.tl_normal_bar)
    Toolbar mTlNormalBar;
    @BindView(R.id.iv_vip_logo)
    ImageView mIvVipLogo;
    @BindView(R.id.rv_power)
    RecyclerView mRvPower;
    @BindView(R.id.rv_gift)
    RecyclerView mRvGift;
    @BindView(R.id.tv_market_price)
    TextView mTvMarketPrice;
    @BindView(R.id.tv_discount_amount)
    TextView mTvDiscountAmount;
    @BindView(R.id.tv_price)
    TextView mTvPrice;
    @BindView(R.id.tv_quarterly_price)
    TextView mTvQuarterlyPrice;
    @BindView(R.id.checkbox_agree)
    TextView mCheckboxAgree;
    @BindView(R.id.tv_open_vip)
    TextView mTvOpenVip;
    @BindView(R.id.ll_open)
    LinearLayout mLlOpen;
    @BindView(R.id.tv_tips)
    TextView mTvTips;
    @BindView(R.id.radio_group)
    RadioGroup mRadioGroupPay;
    @BindView(R.id.ll_aliPay)
    LinearLayout mLlAliPay;
    @BindView(R.id.ll_Layout_weChat)
    LinearLayout mLlLayoutWeChat;
    @BindView(R.id.ll_Layout_union_pay)
    LinearLayout mLlLayoutUnionPay;
    @BindView(R.id.ll_pay_way)
    LinearLayout ll_pay_way;
    @BindView(R.id.ll_card_name)
    FlexboxLayout mLlCardName;
    @BindView(R.id.ll_gift)
    LinearLayout mLlGift;

    private String orderCreateNo = "";
    private PowerEntity mPowerEntity;
    private VipSettleInfoEntity mVipSettleInfoEntity;
    private List<PowerBean> mPowerList = new ArrayList<>();
    private List<GiftListBean> mGiftList = new ArrayList<>();
    private List<CardListBean> mCardList = new ArrayList<>();
    private PowerTopAdapter mPowerTopAdapter;
    private VipGiftAdapter mVipPresentAdapter;
    private int maxCountGift;//最多可选礼品数量
    private int checkNum;//当前选中的礼品数量
    private String payWay = PAY_ALI_PAY;
    private List<String> giftProductIds = new ArrayList<>();//选中的礼品id
    private String cardId;//选中的会员卡id


    @Override
    protected int getContentView() {
        return R.layout.activity_open_vip;
    }

    @Override
    protected void initViews() {
        getLoginStatus(this);
        mTvHeaderTitle.setText("购卡有礼");
        mTvHeaderShared.setVisibility(GONE);
        //初始化权益表格列表
        GridLayoutManager powerLayoutManager = new GridLayoutManager(this, 5);
        mRvPower.setLayoutManager(powerLayoutManager);
        mPowerTopAdapter = new PowerTopAdapter(this, mPowerList);
        mRvPower.setAdapter(mPowerTopAdapter);
        //初始化赠品列表
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        mRvGift.setLayoutManager(linearLayoutManager);
        mVipPresentAdapter = new VipGiftAdapter(this, mGiftList);
        mRvGift.setAdapter(mVipPresentAdapter);
        mVipPresentAdapter.setOnItemClickListener((adapter, view, position) -> {
            GiftListBean giftListBean = (GiftListBean) view.getTag();
            ImageView checkBox = (ImageView) adapter.getViewByPosition(mRvGift, position, R.id.checkbox);
            if (checkBox != null && giftListBean != null) {
                boolean checked = !checkBox.isSelected();
                //点击选中
                if (checked) {
                    if (checkNum < maxCountGift) {
                        checkBox.setSelected(true);
                        giftListBean.setSelected(true);
                        checkNum++;
                        giftProductIds.add(giftListBean.getId());
                    } else {
                        showToast("最多只能勾选" + maxCountGift + "个礼品");
                    }
                } else {//取消选中
                    checkNum--;
                    checkBox.setSelected(false);
                    giftListBean.setSelected(false);
                    giftProductIds.remove(giftListBean.getId());
                }
            }
        });

        mRadioGroupPay.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rb_checked_alipay) {
                payWay = PAY_ALI_PAY;
            } else if (checkedId == R.id.rb_checked_wechat_pay) {
                payWay = PAY_WX_PAY;
            } else if (checkedId == R.id.rb_checked_union_pay) {
                payWay = PAY_UNION_PAY;
            }
        });
    }

    @Override
    protected void loadData() {
        if (userId > 0) {
            getPayWay();
            getPowerList();
            getVipSettleInfo();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            finish();
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IS_LOGIN_CODE) {
            NetLoadUtils.getNetInstance().showLoadSirLoading(loadService);
            loadData();
        }
    }

    private void getPayWay() {
        NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(), Url.GET_PAYTYPE_LIST, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                Map map = GsonUtils.fromJson(result, Map.class);
                if (map != null) {
                    Object payways = map.get("result");
                    if (payways instanceof List && ((List) payways).size() > 0) {
                        //微信
                        if (!((List) payways).contains("1")) {
                            mRadioGroupPay.getChildAt(1).setVisibility(GONE);
                            ll_pay_way.getChildAt(1).setVisibility(GONE);
                        }
                        //支付宝
                        if (!((List) payways).contains("2")) {
                            mRadioGroupPay.getChildAt(0).setVisibility(GONE);
                            ll_pay_way.getChildAt(0).setVisibility(GONE);
                        }
                        //银联
                        if (!((List) payways).contains("3")) {
                            mRadioGroupPay.getChildAt(2).setVisibility(GONE);
                            ll_pay_way.getChildAt(2).setVisibility(GONE);
                        }

                        String payType = (String) ((List) payways).get(0);
                        if ("1".equals(payType)) {
                            payWay = PAY_WX_PAY;
                            ((RadioButton) mRadioGroupPay.getChildAt(1)).setChecked(true);
                        } else if ("2".equals(payType)) {
                            payWay = PAY_ALI_PAY;
                            ((RadioButton) mRadioGroupPay.getChildAt(0)).setChecked(true);
                        } else if ("3".equals(payType)) {
                            payWay = PAY_UNION_PAY;
                            ((RadioButton) mRadioGroupPay.getChildAt(2)).setChecked(true);
                        }
                    }
                }
            }

            @Override
            public void onNotNetOrException() {
            }
        });
    }


    //获取权益列表
    private void getPowerList() {
        NetLoadUtils.getNetInstance().loadNetDataPost(this, Url.GET_VIP_POWER, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                mPowerEntity = GsonUtils.fromJson(result, PowerEntity.class);
                if (mPowerEntity != null) {
                    List<PowerBean> powerList = mPowerEntity.getPowerList();
                    mPowerList.clear();
                    if (powerList != null && powerList.size() > 0) {
                        mPowerList.addAll(powerList);
                    }
                    mPowerTopAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onNotNetOrException() {

            }
        });
    }


    //获取结算信息
    private void getVipSettleInfo() {
        NetLoadUtils.getNetInstance().loadNetDataPost(this, Url.GET_VIP_SETTLEINFO, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                mCardList.clear();
                mLlCardName.removeAllViews();
                mVipSettleInfoEntity = GsonUtils.fromJson(result, VipSettleInfoEntity.class);
                if (mVipSettleInfoEntity != null) {
                    if (SUCCESS_CODE.equals(mVipSettleInfoEntity.getCode())) {
                        VipSettleInfoEntity.VipSettleInfoBean vipSettleInfoBean = mVipSettleInfoEntity.getResult();
                        if (vipSettleInfoBean != null) {
                            mTvTips.setText(getStrings(vipSettleInfoBean.getRemark()));
                            mCardList = vipSettleInfoBean.getCardList();
                            if (mCardList != null && mCardList.size() > 0) {
                                //只有一种卡时不用显示
                                mLlCardName.setVisibility(mCardList.size() == 1 ? GONE : View.VISIBLE);
                                //动态添加会员卡选项
                                for (int i = 0; i < mCardList.size(); i++) {
                                    RadioButton view = ((RadioButton) View.inflate(getActivity(), R.layout.item_card_name, null));
                                    view.setText(mCardList.get(i).getCardName());
                                    view.setChecked(i == 0);
                                    view.setTag(mCardList.get(i).getCardId());
                                    view.setOnClickListener(v -> {
                                        for (int j = 0; j < mLlCardName.getChildCount(); j++) {
                                            RadioButton radioButton = ((RadioButton) mLlCardName.getChildAt(j));
                                            if (v.getTag() == mCardList.get(j).getCardId()) {
                                                updateCardInfo(mCardList.get(j));
                                                radioButton.setChecked(true);
                                            } else {
                                                radioButton.setChecked(false);//保证单选原则
                                            }
                                        }
                                    });
                                    mLlCardName.addView(view);
                                }

                                //默认展示第一张会员卡信息
                                updateCardInfo(mCardList.get(0));
                            }
                        }
                    } else {
                        showToast(mVipSettleInfoEntity.getMsg());
                    }
                }
                NetLoadUtils.getNetInstance().showLoadSir(loadService, mVipSettleInfoEntity);
            }

            @Override
            public void onNotNetOrException() {
                NetLoadUtils.getNetInstance().showLoadSir(loadService, mVipSettleInfoEntity);
            }
        });
    }


    private void updateCardInfo(CardListBean cardListBean) {
        cardId = cardListBean.getCardId();
        //会员卡价格明细
        mTvMarketPrice.setText(cardListBean.getMarketPrice());
        mTvDiscountAmount.setText(cardListBean.getMarketPriceMsg());
        mTvQuarterlyPrice.setText(cardListBean.getPriceMsg());
        mTvPrice.setText(cardListBean.getPrice());
        //开卡礼商品
        List<GiftListBean> giftList = cardListBean.getGiftList();
        maxCountGift = getStringChangeIntegers(cardListBean.getMaxCountGift());
        mGiftList.clear();
        giftProductIds.clear();
        if (giftList != null && giftList.size() > 0) {
            mLlGift.setVisibility(View.VISIBLE);
            mGiftList.addAll(giftList);
            mGiftList.addAll(giftList);
            mGiftList.addAll(giftList);
            mGiftList.addAll(giftList);
        } else {
            mLlGift.setVisibility(GONE);
        }
        mVipPresentAdapter.notifyDataSetChanged();
    }

    //创建订单
    private void creatIndent() {
        showLoadhud(this);
        Map<String, String> map = new HashMap<>();
        StringBuilder ids = new StringBuilder();
        for (int i = 0; i < giftProductIds.size(); i++) {
            String id = giftProductIds.get(i);
            ids.append(i == 0 ? id : "," + id);
        }
        map.put("buyType", payWay);
        map.put("cardId", cardId);
        if (!TextUtils.isEmpty(ids)) {
            map.put("giftProductIds", ids.toString());
        }
        NetLoadUtils.getNetInstance().loadNetDataPost(this, Url.SUBMIT_VIP_USER, map, new NetLoadListenerHelper() {
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
     */
    private void dealingIndentPayResult(String result) {
        dismissLoadhud(this);
        if (payWay.equals(PAY_WX_PAY)) {
            QualityCreateWeChatPayIndentBean qualityWeChatIndent = GsonUtils.fromJson(result, QualityCreateWeChatPayIndentBean.class);
            if (qualityWeChatIndent != null) {
                String msg = qualityWeChatIndent.getResult() != null &&
                        !TextUtils.isEmpty(qualityWeChatIndent.getResult().getMsg()) ?
                        getStrings(qualityWeChatIndent.getResult().getMsg()) :
                        getStrings(qualityWeChatIndent.getMsg());
                if (qualityWeChatIndent.getCode().equals(SUCCESS_CODE)) {
                    //返回成功，调起微信支付接口
                    orderCreateNo = qualityWeChatIndent.getResult().getNo();
                    doWXPay(qualityWeChatIndent.getResult());
                } else {
                    showToast(msg);
                }
            }
        } else if (payWay.equals(PAY_ALI_PAY)) {
            QualityCreateAliPayIndentBean qualityAliPayIndent = GsonUtils.fromJson(result, QualityCreateAliPayIndentBean.class);
            if (qualityAliPayIndent != null) {
                String msg = qualityAliPayIndent.getResult() != null &&
                        !TextUtils.isEmpty(qualityAliPayIndent.getResult().getMsg()) ?
                        getStrings(qualityAliPayIndent.getResult().getMsg()) :
                        getStrings(qualityAliPayIndent.getMsg());
                if (qualityAliPayIndent.getCode().equals(SUCCESS_CODE)) {
                    //返回成功，调起支付宝支付接口
                    orderCreateNo = qualityAliPayIndent.getResult().getNo();
                    doAliPay(qualityAliPayIndent.getResult());
                } else {
                    showToast(msg);
                }
            }
        } else if (payWay.equals(PAY_UNION_PAY)) {
            QualityCreateUnionPayIndentEntity qualityUnionIndent = GsonUtils.fromJson(result, QualityCreateUnionPayIndentEntity.class);
            if (qualityUnionIndent != null) {
                String msg = qualityUnionIndent.getQualityCreateUnionPayIndent() != null &&
                        !TextUtils.isEmpty(qualityUnionIndent.getQualityCreateUnionPayIndent().getMsg()) ?
                        getStrings(qualityUnionIndent.getQualityCreateUnionPayIndent().getMsg()) :
                        getStrings(qualityUnionIndent.getMsg());
                if (SUCCESS_CODE.equals(qualityUnionIndent.getCode())) {
                    //返回成功，调起银联支付接口
                    orderCreateNo = qualityUnionIndent.getQualityCreateUnionPayIndent().getNo();
                    unionPay(qualityUnionIndent);
                } else {
                    showToast(msg);
                }
            }
        }
    }

    //阿里支付
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
            }

            @Override
            public void onCancel() {
                showToast("支付取消");
            }
        }).doPay();
    }

    //微信支付
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
            }

            @Override
            public void onCancel() {
                showToast("支付取消");
            }
        });
    }

    //银联支付
    private void unionPay(@NonNull QualityCreateUnionPayIndentEntity qualityUnionIndent) {
        if (qualityUnionIndent.getQualityCreateUnionPayIndent().getPayKeyBean() != null &&
                !TextUtils.isEmpty(qualityUnionIndent.getQualityCreateUnionPayIndent().getPayKeyBean().getPaymentUrl())) {
            if (loadHud != null) {
                loadHud.show();
            }
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
                                skipDirectIndent();
                            }
                        }

                        @Override
                        public void onUnionPayError(String errorMes) {
                            if (loadHud != null) {
                                loadHud.dismiss();
                            }
                            showToast(errorMes);
                        }
                    });
        } else {
            showToast("缺少重要参数，请选择其它支付渠道！");
        }
    }


    //支付成功跳转会员首页
    private void skipDirectIndent() {
        Intent intent = new Intent(getActivity(), DomolifeVipActivity.class);
        //延时跳转到支付成功页面（因为线程问题，立即跳转可能会失效）
        new LifecycleHandler(this).postDelayed(() -> {
            startActivity(intent);
            finish();
        }, 1000);
    }


    @OnClick({R.id.tv_life_back, R.id.ll_open_vip})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_life_back:
                finish();
                break;
            case R.id.ll_open_vip:
                if (TextUtils.isEmpty(payWay)) {
                    showToast("请选择支付方式");
                } else if (TextUtils.isEmpty(cardId)) {
                    showToast("会员卡信息有误");
                } else {
                    creatIndent();
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
        return mLlOpen;
    }
}
