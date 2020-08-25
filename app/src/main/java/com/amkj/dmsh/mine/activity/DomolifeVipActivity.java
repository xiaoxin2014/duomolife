package com.amkj.dmsh.mine.activity;

import android.content.Intent;
import android.graphics.Paint;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;
import android.text.style.StrikethroughSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.bean.RequestStatus;
import com.amkj.dmsh.bean.TabNameBean;
import com.amkj.dmsh.bean.WeekProductEntity;
import com.amkj.dmsh.bean.WeekProductEntity.WeekProductBean;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.ConstantVariable;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.dominant.adapter.QualityCustomAdapter;
import com.amkj.dmsh.mine.adapter.PowerBottomAdapter;
import com.amkj.dmsh.mine.adapter.PowerTopAdapter;
import com.amkj.dmsh.mine.adapter.VipCouponAdapter;
import com.amkj.dmsh.mine.adapter.WeekProductAdapter;
import com.amkj.dmsh.mine.bean.CalculatorEntity;
import com.amkj.dmsh.mine.bean.CalculatorEntity.CalculatorBean;
import com.amkj.dmsh.mine.bean.PowerEntity;
import com.amkj.dmsh.mine.bean.PowerEntity.PowerBean;
import com.amkj.dmsh.mine.bean.VipCouponEntity;
import com.amkj.dmsh.mine.bean.VipCouponEntity.VipCouponBean;
import com.amkj.dmsh.mine.bean.VipCouponEntity.VipCouponBean.CouponListBean;
import com.amkj.dmsh.mine.bean.ZeroInfoBean;
import com.amkj.dmsh.mine.bean.ZeroInfoEntity;
import com.amkj.dmsh.mine.bean.ZeroInfoEntity.ResultBean;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.user.bean.LikedProductBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.amkj.dmsh.utils.gson.GsonUtils;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;
import com.amkj.dmsh.views.alertdialog.AlertDialogCalculator;
import com.amkj.dmsh.views.flycoTablayout.SlidingTabLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import me.jessyan.autosize.utils.AutoSizeUtils;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.dismissLoadhud;
import static com.amkj.dmsh.constant.ConstantMethod.getStringChangeDouble;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.getStringsFormat;
import static com.amkj.dmsh.constant.ConstantMethod.showLoadhud;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.skipProductUrl;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.mine.adapter.WeekProductAdapter.WEEK_VIP_GOODS;

/**
 * Created by xiaoxin on 2020/7/21
 * Version:v4.7.0
 * ClassDescription :多么会员
 */
public class DomolifeVipActivity extends BaseActivity {
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
    @BindView(R.id.rv_power_top)
    RecyclerView mRvPowerTop;
    @BindView(R.id.rv_power_bottom)
    RecyclerView mRvPowerBottom;
    @BindView(R.id.tv_free_condition)
    TextView mTvFreeCondition;
    @BindView(R.id.seekbar_free_vip)
    ProgressBar mSeekbarFreeVip;
    @BindView(R.id.tv_current_cost)
    TextView mTvCurrentCost;
    @BindView(R.id.tv_shopping)
    TextView mTvShopping;
    @BindView(R.id.iv_vip_gift)
    ImageView mIvVipGift;
    @BindView(R.id.tv_coupon_subtitle)
    TextView mTvCouponSubtitle;
    @BindView(R.id.tv_coupon_title)
    TextView mTvCouponTitle;
    @BindView(R.id.rl_coupon)
    RelativeLayout mRlCoupon;
    @BindView(R.id.rv_coupon)
    RecyclerView mRvCoupon;
    @BindView(R.id.tv_get_coupon)
    TextView mTvGetCoupon;
    @BindView(R.id.iv_zero_cover)
    ImageView mIvZeroCover;
    @BindView(R.id.tv_zero_name)
    TextView mTvZeroName;
    @BindView(R.id.tv_zero_title)
    TextView mTvZeroTitle;
    @BindView(R.id.tv_zero_subtitle)
    TextView mTvZeroSubtitle;
    @BindView(R.id.tv_zero_price)
    TextView mTvZeroPrice;
    @BindView(R.id.tv_zero_market_price)
    TextView mTvZeroMarketPrice;
    @BindView(R.id.tv_zero_quantity)
    TextView mTvZeroQuantity;
    @BindView(R.id.rl_zero_info)
    LinearLayout mRlZeroInfo;
    @BindView(R.id.tv_apply)
    TextView mTvApply;
    @BindView(R.id.tv_vip_power)
    TextView mTvVipPower;
    @BindView(R.id.tv_power_num)
    TextView mTvPowerNum;
    @BindView(R.id.rv_vip_like)
    RecyclerView mRvVipLike;
    @BindView(R.id.tv_open_vip)
    TextView mTvOpenVip;
    @BindView(R.id.rl_free_condition)
    RelativeLayout mRlFreeCondition;
    @BindView(R.id.rl_vip_info)
    RelativeLayout mRlVipInfo;
    @BindView(R.id.iv_head_icon)
    CircleImageView mIvHeadIcon;
    @BindView(R.id.tv_user_name)
    TextView mTvUserName;
    @BindView(R.id.tv_vip_level)
    TextView mTvVipLevel;
    @BindView(R.id.tv_expire_time)
    TextView mTvExpireTime;
    @BindView(R.id.tv_vip_power_num)
    TextView mTvVipPowerNum;
    @BindView(R.id.tv_already_save)
    TextView mTvAlreadySave;
    @BindView(R.id.tv_renewal)
    TextView mTvRenewal;
    @BindView(R.id.tv_expect_save)
    TextView mTvExprectSave;
    @BindView(R.id.iv_vip_day)
    ImageView mIvVipDay;
    @BindView(R.id.rv_week_goods)
    RecyclerView mRvWeekGoods;
    @BindView(R.id.tv_week_title)
    TextView mTvWeekTitle;
    @BindView(R.id.tv_week_subtitle)
    TextView mTvWeekSubtitle;
    @BindView(R.id.rl_vip_week)
    RelativeLayout mRlWeek;
    @BindView(R.id.sliding_tablayout)
    SlidingTabLayout mTablayoutVip;
    @BindView(R.id.vp_vip_exclusive)
    ViewPager mVpVip;

    private PowerEntity mPowerEntity;
    private VipCouponEntity mVipCouponEntity;
    private List<PowerBean> mPowerList = new ArrayList<>();
    private List<CouponListBean> mCouponList = new ArrayList<>();
    private List<LikedProductBean> mGoodsList = new ArrayList<>();
    private PowerTopAdapter mPowerTopAdapter;
    private PowerBottomAdapter mPowerBottomAdapter;
    private VipCouponAdapter mVipCouponAdapter;
    private RequestStatus mVipInfoEntity;
    private RequestStatus.Result mVipInfoBean;
    private CalculatorEntity mCalculatorEntity;
    private AlertDialogCalculator mAlertDialogCalculator;
    private WeekProductAdapter mWeekProductAdapter;
    private ZeroInfoBean mZeroInfoBean;
    private String cardGiftCover;
    private String vipDayCover;
    private String[] titles = {"专区1", "专区2", "专区3", "专区4"};
    public final String[] CUSTOM_IDS = new String[]{"405", "406", "407", "408"};
    private String mVipZoneId;
    private WeekProductBean mWeekProductBean;

    @Override
    protected int getContentView() {
        return R.layout.activity_domolife_vip;
    }

    @Override
    protected void initViews() {
        mTvHeaderTitle.setText("多么会员");
        mTvZeroMarketPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        mTvZeroMarketPrice.getPaint().setAntiAlias(true);
        SpannableString spannableString = new SpannableString(getResources().getString(R.string.vip_year_price));
        RelativeSizeSpan sizeSpan = new RelativeSizeSpan(1.3f);
        spannableString.setSpan(sizeSpan, 0, 13, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        StrikethroughSpan strikethroughSpan = new StrikethroughSpan();
        spannableString.setSpan(strikethroughSpan, 14, 18, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        mTvOpenVip.setText(spannableString);
        //初始化权益表格列表
        GridLayoutManager powerLayoutManager = new GridLayoutManager(this, 5);
        mRvPowerTop.setLayoutManager(powerLayoutManager);
        mPowerTopAdapter = new PowerTopAdapter(this, mPowerList);
        mRvPowerTop.setAdapter(mPowerTopAdapter);
        //初始化优惠券列表
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        mRvCoupon.setLayoutManager(linearLayoutManager);
        mVipCouponAdapter = new VipCouponAdapter(mCouponList);
        mRvCoupon.setAdapter(mVipCouponAdapter);
        //初始化权益线性列表
        mRvPowerBottom.setLayoutManager(new LinearLayoutManager(this));
        mPowerBottomAdapter = new PowerBottomAdapter(this, mPowerList);
        mRvPowerBottom.setAdapter(mPowerBottomAdapter);
        //初始化每周会员特价商品列表
        GridLayoutManager weekLayoutManager = new GridLayoutManager(this, 3);
        mRvWeekGoods.setLayoutManager(weekLayoutManager);
        mRvWeekGoods.addItemDecoration(new ItemDecoration.Builder()
                .setDividerId(R.drawable.item_divider_five_white)
                .create());
        mWeekProductAdapter = new WeekProductAdapter(this, mGoodsList, WEEK_VIP_GOODS);
        mRvWeekGoods.setAdapter(mWeekProductAdapter);
        mWeekProductAdapter.setOnItemClickListener((adapter, view, position) -> {
            String productId = (String) view.getTag();
            if (!TextUtils.isEmpty(productId)) {
                skipProductUrl(this, 1, ConstantMethod.getStringChangeIntegers(productId));
            }
        });
        //初始化自定义专区
        QualityCustomAdapter qualityCustomAdapter = new QualityCustomAdapter(getSupportFragmentManager(), Arrays.asList(CUSTOM_IDS), getSimpleName(), 1);
        mVpVip.setAdapter(qualityCustomAdapter);
        mVpVip.setOffscreenPageLimit(titles.length - 1);
        mTablayoutVip.setViewPager(mVpVip, titles);
    }

    @Override
    protected void loadData() {
        getVipInfo();
        getPowerList();
        getGiftCover();
        getMonthCouponList();
        getVipDayCover();
        getWeekGoods();
        getZeroActivityInfo();
        if (userId > 0) {
            getFreeCondition();
            mRlFreeCondition.setVisibility(View.VISIBLE);
        } else {
            mRlFreeCondition.setVisibility(View.GONE);
        }
    }

    //获取会员信息
    private void getVipInfo() {
        NetLoadUtils.getNetInstance().loadNetDataPost(this, Url.GET_VIP_USER_INFO, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                mVipInfoEntity = GsonUtils.fromJson(result, RequestStatus.class);
                if (mVipInfoEntity != null) {
                    String code = mVipInfoEntity.getCode();
                    String msg = mVipInfoEntity.getMsg();
                    if (SUCCESS_CODE.equals(code)) {
                        mVipInfoBean = mVipInfoEntity.getResult();
                        if (mVipInfoBean != null) {
                            //预期可省
                            mTvExprectSave.setText(mVipInfoBean.getPredictBeEconomical());
                            //会员信息
                            if (userId > 0) {
                                mRlVipInfo.setVisibility(View.VISIBLE);
                                GlideImageLoaderUtil.loadRoundImg(getActivity(), mIvHeadIcon, mVipInfoBean.getAvatar(), AutoSizeUtils.mm2px(mAppContext, 90));
                                mTvUserName.setText(mVipInfoBean.getNickName());
                                mTvVipLevel.setText(mVipInfoBean.getVipLevel());
                                mTvExpireTime.setText(mVipInfoBean.getEndTime());
                                mTvAlreadySave.setText(mVipInfoBean.getBeEconomical());
                            } else {
                                mRlVipInfo.setVisibility(View.GONE);
                            }
                        }
                    } else {
                        showToast(msg);
                    }
                }
                NetLoadUtils.getNetInstance().showLoadSir(loadService, mVipInfoBean, mVipInfoEntity);
            }

            @Override
            public void onNotNetOrException() {
                NetLoadUtils.getNetInstance().showLoadSir(loadService, mVipInfoBean, mVipInfoEntity);
            }
        });
    }

    //获取权益列表
    private void getPowerList() {
        NetLoadUtils.getNetInstance().loadNetDataPost(this, Url.GET_VIP_POWER, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                mPowerList.clear();
                mPowerEntity = GsonUtils.fromJson(result, PowerEntity.class);
                if (mPowerEntity != null) {
                    List<PowerBean> powerList = mPowerEntity.getPowerList();
                    if (powerList != null && powerList.size() > 0) {
                        mPowerList.addAll(powerList);
                        String powerNum = ConstantMethod.getIntegralFormat(getActivity(), R.string.vip_power_num, mPowerList.size());
                        mTvPowerNum.setText(powerNum);
                        mTvVipPowerNum.setText(powerNum);
                    }
                }
                mPowerTopAdapter.notifyDataSetChanged();
                mPowerBottomAdapter.notifyDataSetChanged();
                showPowerVisible();
            }

            @Override
            public void onNotNetOrException() {
                showPowerVisible();
            }
        });
    }

    private void showPowerVisible() {
        mRvPowerTop.setVisibility(mPowerList.size() > 0 ? View.VISIBLE : View.GONE);
        mRvPowerBottom.setVisibility(mPowerList.size() > 0 ? View.VISIBLE : View.GONE);
    }

    //获取省钱计算器
    private void getCalculator() {
        if (mAlertDialogCalculator == null) {
            showLoadhud(this);
            NetLoadUtils.getNetInstance().loadNetDataPost(this, Url.GET_BEECONOMICAL, new NetLoadListenerHelper() {
                @Override
                public void onSuccess(String result) {
                    dismissLoadhud(getActivity());
                    mCalculatorEntity = GsonUtils.fromJson(result, CalculatorEntity.class);
                    if (mCalculatorEntity != null) {
                        if (SUCCESS_CODE.equals(mCalculatorEntity.getCode())) {
                            CalculatorBean calculatorBean = mCalculatorEntity.getResult();
                            if (calculatorBean != null) {
                                mAlertDialogCalculator = new AlertDialogCalculator(getActivity(), calculatorBean);
                                mAlertDialogCalculator.show();
                            }
                        } else {
                            showToast(mCalculatorEntity.getMsg());
                        }
                    }
                }

                @Override
                public void onNotNetOrException() {
                    dismissLoadhud(getActivity());
                }
            });
        } else {
            mAlertDialogCalculator.show();
        }
    }

    //获取是否满足免费7天会员条件
    private void getFreeCondition() {
        NetLoadUtils.getNetInstance().loadNetDataPost(this, Url.GET_CONSUME_LARGESS_INFO, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                RequestStatus requestStatus = GsonUtils.fromJson(result, RequestStatus.class);
                if (requestStatus != null) {
                    if (SUCCESS_CODE.equals(requestStatus.getCode())) {
                        mRlFreeCondition.setVisibility(View.VISIBLE);
                        RequestStatus.Result statusResult = requestStatus.getResult();
                        mTvFreeCondition.setText(getStrings(statusResult.getContentTop()));
                        mTvCurrentCost.setText(getStrings(statusResult.getContentBelow()));
                        mSeekbarFreeVip.setProgress((int) (getStringChangeDouble(statusResult.getPercent()) * 100));
                    } else {
                        mRlFreeCondition.setVisibility(View.GONE);
                    }
                }
            }
        });
    }

    //获取开卡礼封面
    private void getGiftCover() {
        NetLoadUtils.getNetInstance().loadNetDataPost(this, Url.GET_CARD_GIFT_INFO, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                RequestStatus requestStatus = GsonUtils.fromJson(result, RequestStatus.class);
                if (requestStatus != null && SUCCESS_CODE.equals(requestStatus.getCode())) {
                    cardGiftCover = requestStatus.getResult().getPicUrl();
                    GlideImageLoaderUtil.loadImage(getActivity(), mIvVipGift, cardGiftCover);
                }
                mIvVipGift.setVisibility(!TextUtils.isEmpty(cardGiftCover) ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onNotNetOrException() {
                mIvVipGift.setVisibility(!TextUtils.isEmpty(cardGiftCover) ? View.VISIBLE : View.GONE);
            }
        });
    }

    //获取会员日封面
    private void getVipDayCover() {
        NetLoadUtils.getNetInstance().loadNetDataPost(this, Url.GET_VIPDAYS_INFO, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                RequestStatus requestStatus = GsonUtils.fromJson(result, RequestStatus.class);
                if (requestStatus != null && SUCCESS_CODE.equals(requestStatus.getCode())) {
                    vipDayCover = requestStatus.getResult().getCoverImg();
                    mVipZoneId = requestStatus.getResult().getZoneId();
                    GlideImageLoaderUtil.loadImage(getActivity(), mIvVipDay, vipDayCover);
                }
                mIvVipDay.setVisibility(!TextUtils.isEmpty(vipDayCover) ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onNotNetOrException() {
                mIvVipDay.setVisibility(!TextUtils.isEmpty(vipDayCover) ? View.VISIBLE : View.GONE);
            }
        });
    }

    //获取优惠券列表
    private void getMonthCouponList() {
        Map<String, Object> map = new HashMap<>();
        map.put("showCount", 3);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, Url.GET_VIP_COUPON_LIST, map, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                mCouponList.clear();
                mVipCouponEntity = GsonUtils.fromJson(result, VipCouponEntity.class);
                if (mVipCouponEntity != null) {
                    VipCouponBean couponBean = mVipCouponEntity.getResult();
                    if (couponBean != null) {
                        if (!TextUtils.isEmpty(couponBean.getTitle())) {
                            mTvCouponTitle.setText(getStrings(couponBean.getTitle()));
                        }
                        if (!TextUtils.isEmpty(couponBean.getSubtitle())) {
                            mTvCouponSubtitle.setText(getStrings(couponBean.getSubtitle()) + ">>");
                        }
                        List<CouponListBean> couponList = couponBean.getCouponList();
                        if (couponList != null && couponList.size() > 0) {
                            mCouponList.addAll(couponList);
                        }
                    }
                }
                mVipCouponAdapter.notifyDataSetChanged();
                mRlCoupon.setVisibility(mCouponList.size() > 0 ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onNotNetOrException() {
                mRlCoupon.setVisibility(mCouponList.size() > 0 ? View.VISIBLE : View.GONE);
            }
        });
    }

    //获取每周会员特价商品
    private void getWeekGoods() {
        Map<String, Object> map = new HashMap<>();
        NetLoadUtils.getNetInstance().loadNetDataPost(this, Url.GET_WEEK_GOODS, map, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                mGoodsList.clear();
                WeekProductEntity vipProductEntity = GsonUtils.fromJson(result, WeekProductEntity.class);
                if (vipProductEntity != null && SUCCESS_CODE.equals(vipProductEntity.getCode())) {
                    mWeekProductBean = vipProductEntity.getResult();
                    if (mWeekProductBean != null) {
                        if (!TextUtils.isEmpty(mWeekProductBean.getTitle())) {
                            mTvWeekTitle.setText(getStrings(mWeekProductBean.getTitle()));
                        }
                        if (!TextUtils.isEmpty(mWeekProductBean.getSubtitle())) {
                            mTvWeekSubtitle.setText(getStrings(mWeekProductBean.getSubtitle()) + ">>");
                        }
                        List<LikedProductBean> goodsList = mWeekProductBean.getGoodsList();
                        if (goodsList != null && goodsList.size() > 0) {
                            mGoodsList.addAll(goodsList.subList(0, Math.min(goodsList.size(), 3)));
                        }
                    }
                }
                mWeekProductAdapter.notifyDataSetChanged();
                mRlWeek.setVisibility(mGoodsList.size() > 0 ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onNotNetOrException() {
                mRlWeek.setVisibility(mGoodsList.size() > 0 ? View.VISIBLE : View.GONE);
            }
        });
    }

    //获取0元试用活动
    private void getZeroActivityInfo() {
        NetLoadUtils.getNetInstance().loadNetDataPost(this, Url.GET_ZERO_ACTIVITY_INFO, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                ZeroInfoEntity zeroInfoEntity = GsonUtils.fromJson(result, ZeroInfoEntity.class);
                if (zeroInfoEntity != null && SUCCESS_CODE.equals(zeroInfoEntity.getCode())) {
                    ResultBean resultBean = zeroInfoEntity.getResult();
                    if (!TextUtils.isEmpty(resultBean.getTitle())) {
                        mTvZeroTitle.setText(getStrings(resultBean.getTitle()));
                    }
                    if (!TextUtils.isEmpty(resultBean.getSubtitle())) {
                        mTvZeroSubtitle.setText(getStrings(resultBean.getSubtitle()) + ">>");
                    }
                    mZeroInfoBean = resultBean.getZeroInfo();
                    if (mZeroInfoBean != null) {
                        GlideImageLoaderUtil.loadCenterCrop(getActivity(), mIvZeroCover, mZeroInfoBean.getProductImg());
                        mTvZeroName.setText(getStrings(mZeroInfoBean.getProductName()));
                        mTvZeroPrice.setText(ConstantMethod.getRmbFormat(getActivity(), "0"));
                        mTvZeroMarketPrice.setText(getStrings(mZeroInfoBean.getMarketPrice()));
                        mTvZeroQuantity.setText(getStringsFormat(getActivity(), R.string.limit_quantity, mZeroInfoBean.getCount()));
                    }
                }
                mRlZeroInfo.setVisibility(mZeroInfoBean != null ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onNotNetOrException() {
                mRlZeroInfo.setVisibility(mZeroInfoBean != null ? View.VISIBLE : View.GONE);
            }
        });
    }

    @OnClick({R.id.tv_life_back, R.id.iv_vip_day, R.id.tv_domolife_price_subtitle, R.id.tv_week_subtitle, R.id.tv_coupon_subtitle, R.id.tv_zero_subtitle, R.id.rl_novip_info, R.id.tv_header_shared, R.id.tv_shopping, R.id.iv_vip_gift, R.id.tv_get_coupon, R.id.rl_zero_item, R.id.tv_vip_power, R.id.tv_open_vip})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.tv_life_back:
                finish();
                break;
            case R.id.tv_header_shared:
                break;
            //省钱计算器
            case R.id.rl_novip_info:
                getCalculator();
                break;
            //去购物
            case R.id.tv_shopping:
                break;
            //立即开卡
            case R.id.tv_open_vip:
            case R.id.iv_vip_gift:
                intent = new Intent(this, OpenVipActivity.class);
                startActivity(intent);
                break;
            //领取优惠券
            case R.id.tv_get_coupon:
            case R.id.tv_coupon_subtitle:
                intent = new Intent(this, MonthCouponListActivity.class);
                startActivity(intent);
                break;
            //0元试用活动列表
            case R.id.tv_zero_subtitle:
                intent = new Intent(this, ZeroActivityListActivity.class);
                startActivity(intent);
                break;
            //每周会员专区
            case R.id.tv_week_subtitle:
                if (mWeekProductBean != null) {
                    intent = new Intent(this, VipZoneDetailActivity.class);
                    intent.putExtra("zoneId", mWeekProductBean.getZoneId());
                    startActivity(intent);
                }
                break;
            //申请0元试用
            case R.id.rl_zero_item:
                if (mZeroInfoBean != null && !TextUtils.isEmpty(mZeroInfoBean.getActivityId())) {
                    intent = new Intent(this, ZeroDetailActivity.class);
                    intent.putExtra("activityId", mZeroInfoBean.getActivityId());
                    startActivity(intent);
                }
                break;
            //会员专属特权
            case R.id.tv_vip_power:
                intent = new Intent(this, VipPowerActivity.class);
                startActivity(intent);
                break;
            //会员专享价
            case R.id.tv_domolife_price_subtitle:
                intent = new Intent(this, VipExclusiveActivity.class);
                startActivity(intent);
                break;
            //会员日
            case R.id.iv_vip_day:
                intent = new Intent(this, VipZoneDetailActivity.class);
                intent.putExtra("zoneId", mVipZoneId);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void postEventResult(@NonNull EventMessage message) {
        if (message.type.equals(ConstantVariable.UPDATE_CUSTOM_NAME)) {
            try {
                if (mTablayoutVip != null) {
                    TabNameBean tabNameBean = (TabNameBean) message.result;
                    if (getSimpleName().equals(tabNameBean.getSimpleName())) {
                        TextView titleView = mTablayoutVip.getTitleView(tabNameBean.getPosition());
                        titleView.setText(tabNameBean.getTabName());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
