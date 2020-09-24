package com.amkj.dmsh.mine.activity;

import android.content.Intent;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.Gravity;
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
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.UMShareAction;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.dominant.activity.QualityTypeHotSaleProActivity;
import com.amkj.dmsh.dominant.adapter.GoodProductAdapter;
import com.amkj.dmsh.mine.adapter.MonthCouponAdapter;
import com.amkj.dmsh.mine.adapter.PowerBottomAdapter;
import com.amkj.dmsh.mine.adapter.PowerTopAdapter;
import com.amkj.dmsh.mine.adapter.VipExclusivePagerAdapter;
import com.amkj.dmsh.mine.adapter.VipFavoriteAdapter;
import com.amkj.dmsh.mine.adapter.WeekProductAdapter;
import com.amkj.dmsh.mine.bean.CalculatorEntity;
import com.amkj.dmsh.mine.bean.CalculatorEntity.CalculatorBean;
import com.amkj.dmsh.mine.bean.PowerEntity;
import com.amkj.dmsh.mine.bean.PowerEntity.PowerBean;
import com.amkj.dmsh.mine.bean.VipCouponEntity;
import com.amkj.dmsh.mine.bean.VipCouponEntity.VipCouponBean;
import com.amkj.dmsh.mine.bean.VipCouponEntity.VipCouponBean.CouponListBean;
import com.amkj.dmsh.mine.bean.VipExclusiveInfoEntity;
import com.amkj.dmsh.mine.bean.VipExclusiveInfoEntity.VipExclusiveInfoBean;
import com.amkj.dmsh.mine.bean.VipPriceEntity;
import com.amkj.dmsh.mine.bean.WeekProductEntity;
import com.amkj.dmsh.mine.bean.WeekProductEntity.WeekProductBean;
import com.amkj.dmsh.mine.bean.ZeroInfoBean;
import com.amkj.dmsh.mine.bean.ZeroInfoEntity;
import com.amkj.dmsh.mine.bean.ZeroInfoEntity.ResultBean;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.user.bean.LikedProductBean;
import com.amkj.dmsh.utils.SharedPreUtils;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.amkj.dmsh.utils.gson.GsonUtils;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;
import com.amkj.dmsh.utils.itemdecoration.NewGridItemDecoration;
import com.amkj.dmsh.views.alertdialog.AlertDialogCalculator;
import com.amkj.dmsh.views.alertdialog.AlertDialogPower;
import com.amkj.dmsh.views.alertdialog.VipHomeMenuPw;
import com.amkj.dmsh.views.flycoTablayout.SlidingTabLayout;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
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
import static com.amkj.dmsh.constant.ConstantMethod.getSpannableString;
import static com.amkj.dmsh.constant.ConstantMethod.getStringChangeDouble;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.getStringsChNPrice;
import static com.amkj.dmsh.constant.ConstantMethod.getStringsFormat;
import static com.amkj.dmsh.constant.ConstantMethod.isVip;
import static com.amkj.dmsh.constant.ConstantMethod.showLoadhud;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.skipProductUrl;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.LOGIN_SUCCESS;
import static com.amkj.dmsh.constant.ConstantVariable.OPEN_VIP_SUCCESS;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;

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
    @BindView(R.id.tv_zero_product_subtitle)
    TextView mTvZeroProductSubtitle;
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
    @BindView(R.id.ll_open_vip)
    LinearLayout mLlOpenVip;
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
    @BindView(R.id.rl_novip_info)
    RelativeLayout mRlNovipInfo;
    @BindView(R.id.ll_domolife_vip)
    LinearLayout mLlDomolifeVip;
    @BindView(R.id.smart_communal_refresh)
    SmartRefreshLayout mSmartCommunalRefresh;
    @BindView(R.id.tv_vip_price)
    TextView mTvVipPrice;
    @BindView(R.id.tv_vip_market_price)
    TextView mTvVipMarketPrice;
    @BindView(R.id.ll_novip_info)
    LinearLayout mLlNovipInfo;
    @BindView(R.id.ll_power_bottom)
    LinearLayout mLlPowerBottom;
    @BindView(R.id.iv_vip_level)
    ImageView mIvVipLevel;
    @BindView(R.id.tv_vip_price_title)
    TextView mTvVipPriceTitle;
    @BindView(R.id.tv_vip_price_subtitle)
    TextView mTvVipPriceSubtitle;
    @BindView(R.id.rv_vip_price)
    RecyclerView mRvVipPrice;
    @BindView(R.id.rl_vip_price)
    RelativeLayout mRlVipPrice;
    @BindView(R.id.tv_vip_favorite)
    TextView mTvVipFavorite;
    @BindView(R.id.rv_vip_favorite)
    RecyclerView mRvVipFavorite;
    @BindView(R.id.rl_vip_exclusive)
    RelativeLayout mRlVipExclusive;
    @BindView(R.id.iv_menu)
    ImageView mIvMenu;


    private PowerEntity mPowerEntity;
    private VipCouponEntity mVipCouponEntity;
    private List<PowerBean> mPowerTopList = new ArrayList<>();
    private List<PowerBean> mPowerBottomList = new ArrayList<>();
    private List<CouponListBean> mCouponList = new ArrayList<>();
    private List<LikedProductBean> mWeekGoodsList = new ArrayList<>();
    private List<LikedProductBean> mVipGoodsList = new ArrayList<>();
    private List<LikedProductBean> mVipFavoriteList = new ArrayList<>();
    private PowerTopAdapter mPowerTopAdapter;
    private PowerBottomAdapter mPowerBottomAdapter;
    private MonthCouponAdapter mVipCouponAdapter;
    private RequestStatus mVipInfoEntity;
    private RequestStatus.Result mVipInfoBean;
    private CalculatorEntity mCalculatorEntity;
    private AlertDialogCalculator mAlertDialogCalculator;
    private WeekProductAdapter mWeekProductAdapter;
    private GoodProductAdapter mVipPriceAdapter;
    private VipFavoriteAdapter mVipFavoriteAdapter;
    private ZeroInfoBean mZeroInfoBean;
    private String cardGiftCover;
    private String vipDayCover;
    public final int[] VipLevel = new int[]{R.drawable.vip1, R.drawable.vip2, R.drawable.vip3, R.drawable.vip4, R.drawable.vip5, R.drawable.vip6, R.drawable.vip7, R.drawable.vip8, R.drawable.vip9, R.drawable.vip10};
    private String mVipZoneId;
    private WeekProductBean mWeekProductBean;
    private AlertDialogPower mAlertDialogPower;
    private String mCouponZoneId;
    private VipHomeMenuPw mVipHomeMenuPw;
    private List<VipExclusiveInfoBean> mInfos = new ArrayList<>();
    private VipExclusivePagerAdapter mVipExclusivePagerAdapter;

    @Override
    protected int getContentView() {
        return R.layout.activity_domolife_vip;
    }

    @Override
    protected void initViews() {
        mTvHeaderTitle.setText("多么会员");
        mTvZeroMarketPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        mTvZeroMarketPrice.getPaint().setAntiAlias(true);
        mTvVipMarketPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        mTvVipMarketPrice.getPaint().setAntiAlias(true);
        //初始化权益表格列表
        GridLayoutManager powerLayoutManager = new GridLayoutManager(this, 5);
        mRvPowerTop.setLayoutManager(powerLayoutManager);
        mPowerTopAdapter = new PowerTopAdapter(this, mPowerTopList);
        mRvPowerTop.setAdapter(mPowerTopAdapter);
        //初始化优惠券列表
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        mRvCoupon.setLayoutManager(linearLayoutManager);
        mVipCouponAdapter = new MonthCouponAdapter(mCouponList);
        mRvCoupon.setAdapter(mVipCouponAdapter);
        //初始化权益线性列表
        mRvPowerBottom.setLayoutManager(new LinearLayoutManager(this));
        mRvPowerBottom.addItemDecoration(new ItemDecoration.Builder()
                .setDividerId(R.drawable.item_divider_29_transparent)
                .setLastDraw(false)
                .create());
        mPowerBottomAdapter = new PowerBottomAdapter(this, mPowerBottomList);
        mRvPowerBottom.setAdapter(mPowerBottomAdapter);
        //初始化每周会员特价商品列表
        GridLayoutManager weekLayoutManager = new GridLayoutManager(this, 3);
        mRvWeekGoods.setLayoutManager(weekLayoutManager);
        mRvWeekGoods.addItemDecoration(new ItemDecoration.Builder()
                .setDividerId(R.drawable.item_divider_five_white)
                .create());
        mWeekProductAdapter = new WeekProductAdapter(this, mWeekGoodsList);
        mWeekProductAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                LikedProductBean likedProductBean = (LikedProductBean) view.getTag();
                if (likedProductBean != null) {
                    if (view.getId() == R.id.tv_buy_now)
                        if (isVip()) {
                            skipProductUrl(getActivity(), 1, likedProductBean.getId());
                        } else {
                            showAlertDialogPower("开通会员即可享受每周更新的多款商品超低价，最低3折哦~");
                        }
                }
            }
        });
        mRvWeekGoods.setAdapter(mWeekProductAdapter);
        //初始化多么会员价商品列表
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        mRvVipPrice.setLayoutManager(gridLayoutManager);
        mRvVipPrice.addItemDecoration(new ItemDecoration.Builder()
                .setDividerId(R.drawable.item_divider_ten_white)
                .create());
        mVipPriceAdapter = new GoodProductAdapter(this, mVipGoodsList, 2);
        mRvVipPrice.setAdapter(mVipPriceAdapter);
        //初始化会员最爱买商品列表
        GridLayoutManager favoriteLayoutManager = new GridLayoutManager(this, 3);
        mRvVipFavorite.setLayoutManager(favoriteLayoutManager);
        mRvVipFavorite.addItemDecoration(new NewGridItemDecoration.Builder()
                .setDividerId(R.drawable.item_divider_five_white)
                .create());
        mVipFavoriteAdapter = new VipFavoriteAdapter(this, mVipFavoriteList);
        mRvVipFavorite.setAdapter(mVipFavoriteAdapter);
        mSmartCommunalRefresh.setOnRefreshListener(refreshLayout -> {
            loadData();
        });

        mVipExclusivePagerAdapter = new VipExclusivePagerAdapter(getSupportFragmentManager(), mInfos, "1");
        mVpVip.setAdapter(mVipExclusivePagerAdapter);
        mVpVip.setOffscreenPageLimit(mInfos.size() - 1);
        mTablayoutVip.setViewPager(mVpVip);
    }

    @Override
    protected void loadData() {
        getVipInfo();
        getPowerList();
        getMonthCouponList();
        getWeekGoods();
        getZeroActivityInfo();
    }

    //获取会员信息
    private void getVipInfo() {
        NetLoadUtils.getNetInstance().loadNetDataPost(this, Url.GET_VIP_USER_INFO, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                mSmartCommunalRefresh.finishRefresh();
                mVipInfoEntity = GsonUtils.fromJson(result, RequestStatus.class);
                if (mVipInfoEntity != null) {
                    if (SUCCESS_CODE.equals(mVipInfoEntity.getCode())) {
                        setVipInfo();
                    } else {
                        showToast(mVipInfoEntity.getMsg());
                    }
                }
                NetLoadUtils.getNetInstance().showLoadSir(loadService, mVipInfoBean, mVipInfoEntity);
            }

            @Override
            public void onNotNetOrException() {
                mSmartCommunalRefresh.finishRefresh();
                NetLoadUtils.getNetInstance().showLoadSir(loadService, mVipInfoBean, mVipInfoEntity);
            }
        });
    }

    private void setVipInfo() {
        mVipInfoBean = mVipInfoEntity.getResult();
        if (mVipInfoBean != null) {
            //更新会员状态
            SharedPreUtils.setParam("isVip", mVipInfoBean.isVip());
            ConstantMethod.setIsVip(mVipInfoBean.isVip());
            //会员信息
            if (isVip()) {
                getVipDayCover();
                getVipExclusiveInfo();
                mRlVipInfo.setVisibility(View.VISIBLE);
                mRlNovipInfo.setVisibility(View.GONE);
                mLlOpenVip.setVisibility(View.GONE);
                mTvVipPower.setVisibility(View.GONE);
                mRlFreeCondition.setVisibility(View.GONE);
                mIvVipGift.setVisibility(View.GONE);
                mRlVipPrice.setVisibility(View.GONE);
                mRlVipExclusive.setVisibility(View.VISIBLE);
                mVpVip.setVisibility(View.VISIBLE);
                mRvVipFavorite.setVisibility(View.GONE);
                mTvVipFavorite.setVisibility(View.GONE);
                mLlNovipInfo.setBackgroundResource(R.drawable.shap_domolife_info_bg);
                GlideImageLoaderUtil.loadRoundImg(getActivity(), mIvHeadIcon, mVipInfoBean.getAvatar(), AutoSizeUtils.mm2px(mAppContext, 90));
                mIvVipLevel.setImageResource(VipLevel[mVipInfoBean.getVipLevel() - 1]);
                mTvUserName.setText(mVipInfoBean.getNickName());
                mTvVipLevel.setText(mVipInfoBean.getCardName());
                mTvExpireTime.setText(getStringsFormat(getActivity(), R.string.vip_expire, mVipInfoBean.getEndTime()));
                if (!TextUtils.isEmpty(mVipInfoBean.getBeEconomical())) {
                    String beEconomical = getStringsFormat(getActivity(), R.string.vip_already_save, mVipInfoBean.getBeEconomical());
                    int start = beEconomical.indexOf(mVipInfoBean.getBeEconomical());
                    mTvAlreadySave.setText(getSpannableString(beEconomical, start, start + mVipInfoBean.getBeEconomical().length(), 1.33f, "", true));
                }
            } else {
                getGiftCover();
                getFreeCondition();
                getVipPriceGoods();
                getVipFavorite();
                mRlVipInfo.setVisibility(View.GONE);
                mRlNovipInfo.setVisibility(View.VISIBLE);
                mLlOpenVip.setVisibility(View.VISIBLE);
                mTvVipPower.setVisibility(View.VISIBLE);
                mRlVipPrice.setVisibility(View.VISIBLE);
                mRlVipExclusive.setVisibility(View.GONE);
                mVpVip.setVisibility(View.GONE);
                mRvVipFavorite.setVisibility(View.VISIBLE);
                mTvVipFavorite.setVisibility(View.VISIBLE);
                mLlNovipInfo.setBackgroundResource(R.drawable.border_rect_10dp_bg_white);
                mTvExprectSave.setText(mVipInfoBean.getPredictBeEconomical());//预期可省
                mTvVipPrice.setText(getStringsFormat(getActivity(), R.string.open_vip_price, mVipInfoBean.getPriceText()));
                mTvVipMarketPrice.setText(getStringsChNPrice(getActivity(), mVipInfoBean.getMarketPrice()));
            }
        }
    }

    //获取会员专享价专区id和标题
    private void getVipExclusiveInfo() {
        Map<String, Object> map = new HashMap<>();
        map.put("isHomePage", 1);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, Url.GET_VIP_EXCLUSIVE_TITLE, map, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                mInfos.clear();
                VipExclusiveInfoEntity vipExclusiveInfoEntity = GsonUtils.fromJson(result, VipExclusiveInfoEntity.class);
                if (vipExclusiveInfoEntity != null) {
                    List<VipExclusiveInfoBean> infos = vipExclusiveInfoEntity.getResult();
                    if (infos != null && infos.size() > 0) {
                        mInfos.addAll(infos);
                    }
                }
//                //初始化自定义专区
//                ConstantMethod.clearFragmentCache(getSupportFragmentManager());
                mVipExclusivePagerAdapter.notifyDataSetChanged();
                mTablayoutVip.notifyDataSetChanged();
            }

            @Override
            public void onNotNetOrException() {
                super.onNotNetOrException();
            }
        });
    }

    //会员最爱买
    private void getVipFavorite() {
        Map<String, Object> map = new HashMap<>();
        NetLoadUtils.getNetInstance().loadNetDataPost(this, Url.GET_VIP_LIKE_GOODS, map, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                mVipFavoriteList.clear();
                VipPriceEntity vipPriceEntity = GsonUtils.fromJson(result, VipPriceEntity.class);
                if (vipPriceEntity != null && SUCCESS_CODE.equals(vipPriceEntity.getCode())) {
                    List<LikedProductBean> goodsList = vipPriceEntity.getResult();
                    if (goodsList != null && goodsList.size() > 0) {
                        mVipFavoriteList.addAll(goodsList.subList(0, Math.min(goodsList.size(), 36)));
                    }
                }
                mVipFavoriteAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNotNetOrException() {
            }
        });
    }

    //多么会员价商品
    private void getVipPriceGoods() {
        Map<String, Object> map = new HashMap<>();
        NetLoadUtils.getNetInstance().loadNetDataPost(this, Url.GET_VIP_PRICE_GOODS, map, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                mVipGoodsList.clear();
                VipPriceEntity vipPriceEntity = GsonUtils.fromJson(result, VipPriceEntity.class);
                if (vipPriceEntity != null && SUCCESS_CODE.equals(vipPriceEntity.getCode())) {
                    List<LikedProductBean> goodsList = vipPriceEntity.getResult();
                    if (goodsList != null && goodsList.size() > 0) {
                        mVipGoodsList.addAll(goodsList.subList(0, Math.min(goodsList.size(), 2)));
                    }
                }
                mVipPriceAdapter.notifyDataSetChanged();
                mRlVipPrice.setVisibility(mVipGoodsList.size() > 0 ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onNotNetOrException() {
                mRlVipPrice.setVisibility(mVipGoodsList.size() > 0 ? View.VISIBLE : View.GONE);
            }
        });
    }

    //获取权益列表
    private void getPowerList() {
        NetLoadUtils.getNetInstance().loadNetDataPost(this, Url.GET_VIP_POWER, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                mPowerTopList.clear();
                mPowerBottomList.clear();
                mPowerEntity = GsonUtils.fromJson(result, PowerEntity.class);
                if (mPowerEntity != null) {
                    List<PowerBean> powerList = mPowerEntity.getPowerList();
                    if (powerList != null && powerList.size() > 0) {
                        for (int i = 0; i < powerList.size(); i++) {
                            PowerBean powerBean = powerList.get(i);
                            if (powerBean.isShow()) {
                                powerBean.setPosition(String.valueOf(i));
                                mPowerBottomList.add(powerBean);
                            }
                        }
                        mPowerTopList.addAll(powerList);
                        String powerNum = ConstantMethod.getIntegralFormat(getActivity(), R.string.vip_power_num, mPowerTopList.size());
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
        mRvPowerTop.setVisibility(mPowerTopList.size() > 0 ? View.VISIBLE : View.GONE);
        mLlPowerBottom.setVisibility(mPowerBottomList.size() > 0 ? View.VISIBLE : View.GONE);
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
        if (userId > 0) {
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
                            mSeekbarFreeVip.setProgress((int) (getStringChangeDouble(statusResult.getPercent())));
                        } else {
                            mRlFreeCondition.setVisibility(View.GONE);
                        }
                    }
                }
            });
        }
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
                        mCouponZoneId = couponBean.getZoneId();
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
                mWeekGoodsList.clear();
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
                            mWeekGoodsList.addAll(goodsList.subList(0, Math.min(goodsList.size(), 3)));
                        }
                    }
                }
                mWeekProductAdapter.notifyDataSetChanged();
                mRlWeek.setVisibility(mWeekGoodsList.size() > 0 ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onNotNetOrException() {
                mRlWeek.setVisibility(mWeekGoodsList.size() > 0 ? View.VISIBLE : View.GONE);
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
                        mTvZeroProductSubtitle.setText(getStrings(mZeroInfoBean.getSubtitle()));
                        mTvZeroPrice.setText(ConstantMethod.getRmbFormat(getActivity(), "0"));
                        mTvZeroMarketPrice.setText(getStringsChNPrice(getActivity(), mZeroInfoBean.getMarketPrice()));
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

    @OnClick({R.id.tv_life_back, R.id.iv_vip_day, R.id.tv_domolife_price_subtitle, R.id.tv_week_subtitle, R.id.tv_coupon_subtitle, R.id.tv_renewal,
            R.id.tv_zero_subtitle, R.id.iv_calculator, R.id.tv_expect_save, R.id.tv_header_shared, R.id.tv_shopping, R.id.iv_vip_gift, R.id.tv_get_coupon, R.id.rl_zero_item,
            R.id.tv_vip_power, R.id.ll_open_vip, R.id.tv_already_save, R.id.tv_buy_record, R.id.tv_vip_price_subtitle, R.id.tv_apply, R.id.iv_menu})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.tv_life_back:
                finish();
                break;
            case R.id.tv_header_shared:
                new UMShareAction(this, "http://domolifes.oss-cn-beijing.aliyuncs.com/wechatIcon/vip_home_share_cover.jpg",
                        "在多么生活买东西竟然省这么多钱！", "原来好友平时是这样省钱的，我也不能再浪费！",
                        Url.BASE_SHARE_PAGE_TWO + "vip/index.html", -1);
                break;
            case R.id.iv_menu:
                if (mVipHomeMenuPw == null) {
                    mVipHomeMenuPw = new VipHomeMenuPw(this);
                    mVipHomeMenuPw.setPopupGravity(Gravity.BOTTOM);
                    mVipHomeMenuPw.bindLifecycleOwner(this);
                    mVipHomeMenuPw.setBackgroundColor(getResources().getColor(R.color.transparent));
                }
                mVipHomeMenuPw.showPopupWindow(mIvMenu);
                break;
            //购买记录
            case R.id.tv_buy_record:
                intent = new Intent(this, VipRecordActivity.class);
                startActivity(intent);
                break;
            //非会员省钱计算器
            case R.id.iv_calculator:
            case R.id.tv_expect_save:
                getCalculator();
                break;
            //会员省钱账单
            case R.id.tv_already_save:
                intent = new Intent(this, SaveMoneyDetailActivity.class);
                startActivity(intent);
                break;
            //去购物
            case R.id.tv_shopping:
                intent = new Intent(this, QualityTypeHotSaleProActivity.class);
                startActivity(intent);
                break;
            //立即开卡
            case R.id.ll_open_vip:
            case R.id.iv_vip_gift:
            case R.id.tv_renewal:
                intent = new Intent(this, OpenVipActivity.class);
                startActivity(intent);
                break;
            //每月专享券列表
            case R.id.tv_coupon_subtitle:
                if (isVip()) {
                    if (!TextUtils.isEmpty(mCouponZoneId)) {
                        intent = new Intent(this, MonthCouponListActivity.class);
                        intent.putExtra("zoneId", mCouponZoneId);
                        startActivity(intent);
                    }
                } else {
                    intent = new Intent(this, VipPowerDetailActivity.class);
                    startActivity(intent);
                }
                break;
            //领取优惠券
            case R.id.tv_get_coupon:
                if (isVip()) {
                    if (!TextUtils.isEmpty(mCouponZoneId)) {
                        intent = new Intent(this, MonthCouponListActivity.class);
                        intent.putExtra("zoneId", mCouponZoneId);
                        startActivity(intent);
                    }
                } else {
                    showAlertDialogPower("开通立享每月价值100元的优惠券，还有神秘好券不定时发放");
                }
                break;
            //0元试用活动列表
            case R.id.tv_zero_subtitle:
                intent = new Intent(this, isVip() ? ZeroActivityListActivity.class : VipPowerDetailActivity.class);
                startActivity(intent);
                break;
            //点击0元试用商品
            case R.id.rl_zero_item:
                skipZeroActivityDetail();
                break;
            //申请0元试用
            case R.id.tv_apply:
                if (isVip()) {
                    skipZeroActivityDetail();
                } else {
                    showAlertDialogPower("开通即可享受0元参与试用，商品不定时更新，快来参与吧~");
                }
                break;
            //每周会员专区
            case R.id.tv_week_subtitle:
                if (mWeekProductBean != null) {
                    intent = new Intent(this, isVip() ? VipZoneDetailActivity.class : VipPowerDetailActivity.class);
                    intent.putExtra("zoneId", mWeekProductBean.getZoneId());
                    startActivity(intent);
                }
                break;
            case R.id.tv_vip_price_subtitle:  //多么会员价
            case R.id.tv_vip_power:  //会员专属特权
                intent = new Intent(this, VipPowerDetailActivity.class);
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

    private void skipZeroActivityDetail() {
        Intent intent;
        if (mZeroInfoBean != null && !TextUtils.isEmpty(mZeroInfoBean.getActivityId())) {
            intent = new Intent(this, ZeroActivityDetailActivity.class);
            intent.putExtra("activityId", mZeroInfoBean.getActivityId());
            startActivity(intent);
        }
    }

    private void showAlertDialogPower(String desc) {
        if (mAlertDialogPower == null) {
            mAlertDialogPower = new AlertDialogPower(this);
        }

        mAlertDialogPower.setDesc(desc);
        mAlertDialogPower.show();
    }

    @Override
    protected void postEventResult(@NonNull EventMessage message) {
        if (OPEN_VIP_SUCCESS.equals(message.type) || LOGIN_SUCCESS.equals(message.type)) {
            //登录成功和会员开通成功时更新会员首页
            loadData();
        }
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    @Override
    public View getLoadView() {
        return mLlDomolifeVip;
    }
}
