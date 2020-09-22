package com.amkj.dmsh.mine.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.homepage.activity.DoMoLifeCommunalActivity;
import com.amkj.dmsh.mine.adapter.VipGiftAdapter;
import com.amkj.dmsh.mine.bean.VipSettleInfoEntity;
import com.amkj.dmsh.mine.bean.VipSettleInfoEntity.VipSettleInfoBean.CardListBean;
import com.amkj.dmsh.mine.bean.VipSettleInfoEntity.VipSettleInfoBean.CardListBean.GiftListBean;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.utils.gson.GsonUtils;
import com.google.android.flexbox.FlexboxLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

import static android.view.View.GONE;
import static com.amkj.dmsh.constant.ConstantMethod.getIntegralFormat;
import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.getStringChangeIntegers;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.getStringsFormat;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.IS_LOGIN_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.OPEN_VIP_SUCCESS;
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
    @BindView(R.id.ll_card_name)
    FlexboxLayout mLlCardName;
    @BindView(R.id.ll_gift)
    LinearLayout mLlGift;
    @BindView(R.id.tv_select_num)
    TextView mTvSelectNum;
    @BindView(R.id.tv_coupon)
    TextView mTvCoupon;
    @BindView(R.id.tv_coupon_amount)
    TextView mTvCouponAmount;
    @BindView(R.id.ll_coupon)
    LinearLayout mLlCoupon;

    private VipSettleInfoEntity mVipSettleInfoEntity;
    private List<GiftListBean> mGiftList = new ArrayList<>();
    private List<CardListBean> mCardList = new ArrayList<>();
    private VipGiftAdapter mVipPresentAdapter;
    private int maxCountGift;//最多可选礼品数量
    private int checkNum;//当前选中的礼品数量
    private List<String> giftProductIds = new ArrayList<>();//选中的礼品id
    private String cardId;//选中的会员卡id
    private String payPrice;//选中的会员卡需要支付的金额
    private CardListBean.CouponInfoBean mCouponInfo;


    @Override
    protected int getContentView() {
        return R.layout.activity_open_vip;
    }

    @Override
    protected void initViews() {
        getLoginStatus(this);
        mTvHeaderTitle.setText("购卡有礼");
        mTvHeaderShared.setVisibility(GONE);
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
                        showToast("最多只能勾选" + maxCountGift + "件礼品");
                    }
                } else {//取消选中
                    checkNum--;
                    checkBox.setSelected(false);
                    giftListBean.setSelected(false);
                    giftProductIds.remove(giftListBean.getId());
                }
            }
        });
    }

    @Override
    protected void loadData() {
        if (userId > 0) {
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
                                mLlCardName.removeAllViews();
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
        payPrice = cardListBean.getPayPrice();
        //会员卡价格明细
        mTvMarketPrice.setText(getStrings(cardListBean.getMarketPrice()));
        mTvDiscountAmount.setText(getStrings(cardListBean.getMarketPriceMsg()));
        mTvQuarterlyPrice.setText(getStrings(cardListBean.getPriceMsg()));
        mTvPrice.setText(getStrings(cardListBean.getPrice()));
        mTvOpenVip.setText(getStringsFormat(this, R.string.vip_price_year, cardListBean.getPrice()));
        //优惠券信息
        mCouponInfo = cardListBean.getCouponInfo();
        if (mCouponInfo != null) {
            mLlCoupon.setVisibility(View.VISIBLE);
            mTvCoupon.setText(mCouponInfo.getAmount() + "元");
            mTvCouponAmount.setText(getStringsFormat(this, R.string.discount_more, mCouponInfo.getAmount()));
        } else {
            mLlCoupon.setVisibility(GONE);
        }
        //开卡礼商品
        mGiftList.clear();
        giftProductIds.clear();
        checkNum = 0;
        List<GiftListBean> giftList = cardListBean.getGiftList();
        maxCountGift = getStringChangeIntegers(cardListBean.getMaxCountGift());
        if (giftList != null && giftList.size() > 0) {
            mTvSelectNum.setVisibility(View.VISIBLE);
            mTvSelectNum.setText(getIntegralFormat(this, R.string.gift_select_num, maxCountGift));
            mLlGift.setVisibility(View.VISIBLE);
            for (GiftListBean giftListBean : giftList) {
                giftListBean.setSelected(false);//恢复未选中状态
            }
            mGiftList.addAll(giftList);
        } else {
            mTvSelectNum.setVisibility(GONE);
            mLlGift.setVisibility(GONE);
        }
        mVipPresentAdapter.notifyDataSetChanged();
    }


    @OnClick({R.id.tv_life_back, R.id.ll_open_vip, R.id.checkbox_agree})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_life_back:
                finish();
                break;
            //会员用户协议
            case R.id.checkbox_agree:
                Intent intent1 = new Intent(this, DoMoLifeCommunalActivity.class);
                intent1.putExtra("loadUrl", "https://www.domolife.cn/protocol/vip_policy.html");
                startActivity(intent1);
                break;
            //开通会员
            case R.id.ll_open_vip:
                if (TextUtils.isEmpty(cardId)) {
                    showToast("会员卡信息有误");
                } else if (checkNum < maxCountGift) {
                    showToast("您还可以再选" + (maxCountGift - checkNum) + "件礼品");
                } else {
                    Intent intent = new Intent(getActivity(), OpenVipWriteActivity.class);
                    if (giftProductIds.size() > 0) {
                        StringBuilder ids = new StringBuilder();
                        for (int i = 0; i < giftProductIds.size(); i++) {
                            String id = giftProductIds.get(i);
                            ids.append(i == 0 ? id : "," + id);
                        }
                        intent.putExtra("giftProductIds", ids.toString());
                    }
                    intent.putExtra("cardId", cardId);
                    intent.putExtra("payPrice", payPrice);
                    if (mCouponInfo != null) {
                        intent.putExtra("userCouponId", mCouponInfo.getUserCouponId());
                    }
                    startActivity(intent);
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

    @Override
    protected void postEventResult(@NonNull EventMessage message) {
        super.postEventResult(message);
        if (OPEN_VIP_SUCCESS.equals(message.type)) {
            finish();
        }
    }
}
