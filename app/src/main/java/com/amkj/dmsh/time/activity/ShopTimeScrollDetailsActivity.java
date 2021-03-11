package com.amkj.dmsh.time.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amkj.dmsh.MainActivity;
import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.TinkerBaseApplicationLike;
import com.amkj.dmsh.bean.TabEntity;
import com.amkj.dmsh.constant.CommunalAdHolderView;
import com.amkj.dmsh.constant.UMShareAction;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.dominant.bean.GroupDetailEntity;
import com.amkj.dmsh.dominant.bean.GroupDetailEntity.CouponListBean;
import com.amkj.dmsh.homepage.adapter.CommunalDetailAdapter;
import com.amkj.dmsh.homepage.bean.CommunalADActivityEntity.CommunalADActivityBean;
import com.amkj.dmsh.mine.activity.ShopCarActivity;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.qyservice.QyProductIndentInfo;
import com.amkj.dmsh.qyservice.QyServiceUtils;
import com.amkj.dmsh.shopdetails.adapter.GoodsRecommendAdapter;
import com.amkj.dmsh.shopdetails.bean.CommunalDetailObjectBean;
import com.amkj.dmsh.shopdetails.bean.ShopRecommendHotTopicEntity;
import com.amkj.dmsh.shopdetails.bean.ShopRecommendHotTopicEntity.ShopRecommendHotTopicBean;
import com.amkj.dmsh.time.adapter.TimeCouponAdapter;
import com.amkj.dmsh.utils.CountDownTimer;
import com.amkj.dmsh.utils.TimeUtils;
import com.amkj.dmsh.utils.gson.GsonUtils;
import com.amkj.dmsh.utils.webformatdata.CommunalWebDetailUtils;
import com.amkj.dmsh.views.flycoTablayout.CommonTabLayout;
import com.amkj.dmsh.views.flycoTablayout.listener.CustomTabEntity;
import com.amkj.dmsh.views.flycoTablayout.listener.OnTabSelectListener;
import com.amkj.dmsh.views.convenientbanner.ConvenientBanner;
import com.amkj.dmsh.views.convenientbanner.holder.CBViewHolderCreator;
import com.amkj.dmsh.views.convenientbanner.holder.Holder;
import com.gyf.barlibrary.ImmersionBar;
import com.melnykov.fab.FloatingActionButton;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.tencent.bugly.beta.tinker.TinkerManager;
import com.tencent.stat.StatService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;
import cn.iwgang.countdownview.CountdownView;
import cn.iwgang.countdownview.DynamicConfig;
import me.jessyan.autosize.utils.AutoSizeUtils;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.getSpannableString;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.getStringsChNPrice;
import static com.amkj.dmsh.constant.ConstantMethod.getStringsFormat;
import static com.amkj.dmsh.constant.ConstantMethod.showImageActivity;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.skipProductUrl;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.Url.TIME_PRODUCT_CLICK_TOTAL;
import static com.amkj.dmsh.constant.Url.TIME_SHOW_PRO_TOP_PRODUCT;
import static com.amkj.dmsh.dao.BaiChuanDao.skipAliBC;
import static com.amkj.dmsh.find.activity.ImagePagerActivity.IMAGE_DEF;
import static com.amkj.dmsh.utils.glide.GlideImageLoaderUtil.getSquareImgUrl;

/**
 * Created by xiaoxin on 2020/9/27
 * Version:v4.8.0
 * ClassDescription :新版团购商品详情
 */
public class ShopTimeScrollDetailsActivity extends BaseActivity {
    @BindView(R.id.banner_ql_sp_pro_details)
    ConvenientBanner mBannerQlSpProDetails;
    @BindView(R.id.countdownTime)
    CountdownView mCountdownTime;
    @BindView(R.id.tv_price)
    TextView mTvPrice;
    @BindView(R.id.tv_market_price)
    TextView mTvMarketPrice;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_subtitle)
    TextView mTvSubtitle;
    @BindView(R.id.tv_like_num)
    TextView mTvLikeNum;
    @BindView(R.id.rv_coupon)
    RecyclerView mRvCoupon;
    @BindView(R.id.tv_desc)
    TextView mTvDesc;
    @BindView(R.id.rv_goods_recommend)
    RecyclerView mRvGoodsRecommend;
    @BindView(R.id.ll_product_recommend)
    LinearLayout mLlProductRecommend;
    @BindView(R.id.rv_product_details)
    RecyclerView mRvProductDetails;
    @BindView(R.id.scroll_pro)
    NestedScrollView mScrollPro;
    @BindView(R.id.smart_ql_sp_pro_details)
    SmartRefreshLayout mSmartQlSpProDetails;
    @BindView(R.id.tv_home)
    TextView mTvHome;
    @BindView(R.id.tv_qy)
    TextView mTvQy;
    @BindView(R.id.tv_buy)
    TextView mTvBuy;
    @BindView(R.id.ll_apply)
    LinearLayout mLlApply;
    @BindView(R.id.download_btn_communal)
    FloatingActionButton mDownloadBtnCommunal;
    @BindView(R.id.fl_product_details)
    FrameLayout mFlProductDetails;
    @BindView(R.id.iv_life_back2)
    ImageView mIvLifeBack2;
    @BindView(R.id.ll_back2)
    LinearLayout mLlBack2;
    @BindView(R.id.iv_img_service2)
    ImageView mIvImgService2;
    @BindView(R.id.ll_service2)
    LinearLayout mLlService2;
    @BindView(R.id.iv_img_share2)
    ImageView mIvImgShare2;
    @BindView(R.id.ll_share2)
    LinearLayout mLlShare2;
    @BindView(R.id.rl_toolbar2)
    RelativeLayout mRlToolbar2;
    @BindView(R.id.iv_life_back)
    ImageView mIvLifeBack;
    @BindView(R.id.ll_back)
    LinearLayout mLlBack;
    @BindView(R.id.ctb_qt_pro_details)
    CommonTabLayout mCtbQtProDetails;
    @BindView(R.id.iv_img_service)
    ImageView mIvImgService;
    @BindView(R.id.ll_service)
    LinearLayout mLlService;
    @BindView(R.id.iv_img_share)
    ImageView mIvImgShare;
    @BindView(R.id.ll_share)
    LinearLayout mLlShare;
    @BindView(R.id.rl_toolbar)
    RelativeLayout mRlToolbar;
    @BindView(R.id.fl_header_service)
    RelativeLayout mFlHeaderService;
    @BindView(R.id.fl_quality_bar)
    FrameLayout mFlQualityBar;
    @BindView(R.id.ll_pro_layout)
    LinearLayout mLlProLayout;
    @BindView(R.id.tv_tiem_prefix)
    TextView mTvTiemPrefix;
    @BindView(R.id.ll_recommend)
    LinearLayout mLlRecommend;
    private String mId;
    private String sharePageUrl = Url.BASE_SHARE_PAGE_TWO + "common/taoBaoGoods.html?id=";
    private CommunalDetailAdapter communalDetailAdapter;
    private List<CommunalADActivityBean> imagesVideoList = new ArrayList<>();
    private ArrayList<CustomTabEntity> tabData = new ArrayList<>();
    private List<CommunalDetailObjectBean> shopDetailBeanList = new ArrayList<>();
    private String[] detailTabData = {"商品", "详情"};
    private CBViewHolderCreator cbViewHolderCreator;
    private int measuredHeight;
    private GroupDetailEntity mGroupDetailEntity;
    private int screenWith;
    private CountDownTimer mCountDownTimer;
    private List<ShopRecommendHotTopicBean> goodsRecommendList = new ArrayList<>();
    private List<CouponListBean> couponList = new ArrayList<>();
    private GoodsRecommendAdapter mGoodsRecommendAdapter;
    private TimeCouponAdapter mTimeCouponAdapter;

    @Override
    public void setStatusBar() {
        ImmersionBar.with(this).keyboardEnable(true).navigationBarEnable(false).statusBarDarkFont(true).fullScreen(true).init();
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_time_detail;
    }

    @Override
    protected void initViews() {
        if (getIntent().getExtras() != null) {
            mId = getIntent().getStringExtra("productId");
        } else {
            showToast("数据有误，请重试");
            finish();
        }
        TinkerBaseApplicationLike app = (TinkerBaseApplicationLike) TinkerManager.getTinkerApplicationLike();
        screenWith = app.getScreenWidth();
        //动态修改标题栏padding
        int statusBarHeight = ImmersionBar.getStatusBarHeight(this);
        int paddingTop = statusBarHeight > 0 ? statusBarHeight : AutoSizeUtils.mm2px(this, 40);
        mRlToolbar.setPadding(0, paddingTop, 0, 0);
        mRlToolbar2.setPadding(0, paddingTop, 0, 0);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) mFlHeaderService.getLayoutParams();
        layoutParams.topMargin = paddingTop;
        mFlHeaderService.setLayoutParams(layoutParams);
        //设置结束时间倒计时
        DynamicConfig.Builder dynamic = new DynamicConfig.Builder();
        dynamic.setSuffixTextSize(AutoSizeUtils.mm2px(mAppContext, 24));
        dynamic.setTimeTextSize(AutoSizeUtils.mm2px(mAppContext, 24));
        dynamic.setSuffixGravity(Gravity.CENTER);
        DynamicConfig.BackgroundInfo backgroundInfo = new DynamicConfig.BackgroundInfo();
        backgroundInfo.setColor(getResources().getColor(R.color.text_pink_da))
                .setBorderRadius((float) AutoSizeUtils.mm2px(mAppContext, 5))
                .setBorderColor(getResources().getColor(R.color.text_pink_da))
                .setShowTimeBgBorder(true);
        dynamic.setBackgroundInfo(backgroundInfo);
        mCountdownTime.dynamicShow(dynamic.build());

        //初始化商品详情
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRvProductDetails.setLayoutManager(linearLayoutManager);
        mRvProductDetails.setNestedScrollingEnabled(false);
        communalDetailAdapter = new CommunalDetailAdapter(getActivity(), shopDetailBeanList);
        communalDetailAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            CommunalWebDetailUtils.getCommunalWebInstance()
                    .setWebDataClick(getActivity(), null, view, loadHud);
        });
        mRvProductDetails.setAdapter(communalDetailAdapter);
        for (String detailTabDatum : detailTabData) {
            tabData.add(new TabEntity(detailTabDatum, 0, 0));
        }
        mCtbQtProDetails.setTextSize(AutoSizeUtils.mm2px(mAppContext, 30));
        mCtbQtProDetails.setIndicatorWidth(AutoSizeUtils.mm2px(mAppContext, 28 * 2));
        mCtbQtProDetails.setTabPadding(AutoSizeUtils.mm2px(mAppContext, 20));
        mCtbQtProDetails.setTabData(tabData);
        mCtbQtProDetails.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                scrollLocation(position);
            }

            @Override
            public void onTabReselect(int position) {
                scrollLocation(position);
            }
        });
        mCtbQtProDetails.setCurrentTab(0);
        int bannerHeight = AutoSizeUtils.mm2px(this, 750);
        mScrollPro.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (measuredHeight < 1) {
                    measuredHeight = mLlProLayout.getMeasuredHeight();
                    return;
                }
                int currentTab = mCtbQtProDetails.getCurrentTab();
                if (scrollY >= 0 && scrollY < measuredHeight && currentTab != 0) {
                    mCtbQtProDetails.setCurrentTab(0);
                } else if (scrollY >= measuredHeight && currentTab != 1) {
                    mCtbQtProDetails.setCurrentTab(1);
                }

                //设置标题栏
                float alpha = scrollY * 1.0f / bannerHeight * 1.0f;
                if (alpha >= 1) {
                    mRlToolbar2.setAlpha(0);
                    mRlToolbar.setAlpha(1);
                } else if (alpha > 0) {
                    mRlToolbar2.setAlpha(alpha > 0.4f ? 0 : 1 - alpha);
                    mRlToolbar.setAlpha(alpha < 0.4f ? 0 : alpha);
                } else {
                    mRlToolbar2.setAlpha(1);
                    mRlToolbar.setAlpha(0);
                }
            }
        });
        mSmartQlSpProDetails.setOnRefreshListener(refreshLayout -> loadData());
        //初始化推荐商品列表
        mRvGoodsRecommend.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mGoodsRecommendAdapter = new GoodsRecommendAdapter(this, goodsRecommendList, true);
        mRvGoodsRecommend.setAdapter(mGoodsRecommendAdapter);
        mGoodsRecommendAdapter.setOnItemClickListener((adapter, view, position) -> {
            ShopRecommendHotTopicBean shopRecommendHotTopicBean = (ShopRecommendHotTopicBean) view.getTag();
            if (shopRecommendHotTopicBean != null) {
                skipProductUrl(getActivity(), 0, shopRecommendHotTopicBean.getId());
            }
        });

        //初始化优惠券列表
        mRvCoupon.setLayoutManager(new LinearLayoutManager(getActivity()));
        mTimeCouponAdapter = new TimeCouponAdapter(getActivity(), couponList);
        mRvCoupon.setAdapter(mTimeCouponAdapter);
    }

    private void scrollLocation(int position) {
        setChangeMeasureHeight();
        if (position == 0) {
            mScrollPro.scrollTo(0, 0);
        } else if (position == 1) {
            if (mLlProLayout.getMeasuredHeight() > 0) {
                mScrollPro.scrollTo(0, measuredHeight);
            }
        }
    }

    private void setChangeMeasureHeight() {
        measuredHeight = mLlProLayout.getMeasuredHeight();
    }

    @Override
    protected void loadData() {
        getShopDetails();
        getRecommendGoods();
    }

    private void getRecommendGoods() {
        Map<String, Object> params = new HashMap<>();
        params.put("recommendType", "top");
        if (userId != 0) {
            params.put("uid", userId);
        }
        NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(), TIME_SHOW_PRO_TOP_PRODUCT, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                goodsRecommendList.clear();
                ShopRecommendHotTopicEntity recommendEntity = GsonUtils.fromJson(result, ShopRecommendHotTopicEntity.class);
                if (recommendEntity != null) {
                    if (recommendEntity.getCode().equals(SUCCESS_CODE)) {
                        List<ShopRecommendHotTopicBean> recommendList = recommendEntity.getShopRecommendHotTopicList();
                        if (recommendList != null && recommendList.size() > 0) {
                            goodsRecommendList.addAll(recommendList);
                        }
                    }
                }
                mGoodsRecommendAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNotNetOrException() {
            }
        });
    }

    private void getShopDetails() {
        Map<String, String> map = new HashMap<>();
        map.put("id", mId);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, Url.GET_GROUP_PEODUCT_DETAILS, map, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                shopDetailBeanList.clear();
                couponList.clear();
                mSmartQlSpProDetails.finishRefresh();
                mGroupDetailEntity = GsonUtils.fromJson(result, GroupDetailEntity.class);
                if (mGroupDetailEntity != null) {
                    String code = mGroupDetailEntity.getCode();
                    if (SUCCESS_CODE.equals(code)) {
                        setTimeProductData(mGroupDetailEntity);
                    } else if (!EMPTY_CODE.equals(code)) {
                        showToast(mGroupDetailEntity.getMsg());
                    }
                }
                communalDetailAdapter.notifyDataSetChanged();
                mTimeCouponAdapter.notifyDataSetChanged();
                NetLoadUtils.getNetInstance().showLoadSir(loadService, mGroupDetailEntity);
            }

            @Override
            public void onNotNetOrException() {
                mSmartQlSpProDetails.finishRefresh();
                NetLoadUtils.getNetInstance().showLoadSir(loadService, mGroupDetailEntity);
            }
        });
    }

    public void setTimeProductData(final GroupDetailEntity groupDetailEntity) {
        Properties prop = new Properties();
        prop.setProperty("proName", getStrings(groupDetailEntity.getTitle()));
        prop.setProperty("proId", String.valueOf(groupDetailEntity.getId()));
        StatService.trackCustomKVEvent(this, "timeProLook", prop);
        //轮播位（包含图片以及视频）
        imagesVideoList.clear();
        List<String> imageList = Arrays.asList(groupDetailEntity.getImages().split(","));
        if (groupDetailEntity.haveVideo()) {
            imagesVideoList.add(new CommunalADActivityBean("", groupDetailEntity.getVideoUrl()));
        }

        for (int i = 0; i < imageList.size(); i++) {
            String imgUrl = imageList.get(i);
            imagesVideoList.add(new CommunalADActivityBean(i == 0 ? getSquareImgUrl(imgUrl, screenWith, groupDetailEntity.getWaterRemark()) : imgUrl, ""));
        }

        if (cbViewHolderCreator == null) {
            cbViewHolderCreator = new CBViewHolderCreator() {
                @Override
                public Holder createHolder(View itemView) {
                    return new CommunalAdHolderView(itemView, getActivity(), false);
                }

                @Override
                public int getLayoutId() {
                    return R.layout.layout_ad_image_video;
                }
            };
        }
        mBannerQlSpProDetails.setPages(this, cbViewHolderCreator, imagesVideoList);
        mBannerQlSpProDetails.setOnItemClickListener(position -> {
            if (imagesVideoList.size() > position) {
                CommunalADActivityBean communalADActivityBean = imagesVideoList.get(position);
                if (!TextUtils.isEmpty(communalADActivityBean.getVideoUrl())) {
                    return;
                }

                showImageActivity(getActivity(), IMAGE_DEF,
                        position - (groupDetailEntity.haveVideo() ? 1 : 0),
                        imageList);
            }
        });
        //价格以及标题
        String priceText = "团购价 ¥" + groupDetailEntity.getPrice() + (groupDetailEntity.getPrice().equals(groupDetailEntity.getMaxPrice()) ? "" : "起");
        int start = priceText.indexOf(groupDetailEntity.getPrice());
        mTvPrice.setText(getSpannableString(priceText, start, start + groupDetailEntity.getPrice().length(), 2.0f, ""));
        if (!TextUtils.isEmpty(groupDetailEntity.getMarketPrice())) {
            mTvMarketPrice.setText(getStringsChNPrice(this, groupDetailEntity.getMarketPrice()));
        }
        mTvTitle.setText(getStrings(groupDetailEntity.getTitle()));
        if (!TextUtils.isEmpty(groupDetailEntity.getSubtitle())) {
            mTvSubtitle.setText(getStrings(groupDetailEntity.getSubtitle()));
        }
        mTvDesc.setText(getStrings(groupDetailEntity.getRecommend()));
        mTvLikeNum.setText(groupDetailEntity.getFlashBuyClickCount() + "人喜欢");
        mLlRecommend.setVisibility(!TextUtils.isEmpty(groupDetailEntity.getRecommend()) ? View.VISIBLE : View.GONE);
        //限时团价倒计时
        String endTime = groupDetailEntity.getEndTime();
        String startTime = groupDetailEntity.getStartTime();
        String currentTime = groupDetailEntity.getCurrentTime();
        //活动已结束
        if (TimeUtils.isEndOrStartTime(currentTime, endTime)) {
            mTvTiemPrefix.setText("已结束");
        } else if (TimeUtils.isEndOrStartTime(currentTime, startTime)) {
            //已开始未结束
            timeStart(endTime, currentTime);
        } else {
            //未开始
            mTvTiemPrefix.setText("距开始 ");
            mTvBuy.setText("提前预览");
            long millisInFuture = TimeUtils.getTimeDifference(currentTime, startTime);
            if (mCountDownTimer == null) {
                mCountDownTimer = new CountDownTimer(this) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        mCountdownTime.updateShow(millisUntilFinished);
                    }

                    @Override
                    public void onFinish() {
                        timeStart(endTime, currentTime);
                    }
                };
            }
            mCountDownTimer.setMillisInFuture(millisInFuture);
            mCountDownTimer.start();
        }

        //优惠券列表
        List<CouponListBean> coupons = groupDetailEntity.getCouponList();
        if (coupons != null && coupons.size() > 0) {
            couponList.addAll(coupons);
        }

        //商品详情
        List<CommunalDetailObjectBean> detailsDataList = CommunalWebDetailUtils.getCommunalWebInstance().getWebDetailsFormatDataList(groupDetailEntity.getItemBody());
        shopDetailBeanList.addAll(detailsDataList);
    }

    private void timeStart(String endTime, String currentTime) {
        mTvBuy.setText("我要跟团");
        mTvTiemPrefix.setText("距结束 ");
        long millisInFuture = TimeUtils.getTimeDifference(currentTime, endTime);
        if (mCountDownTimer == null) {
            mCountDownTimer = new CountDownTimer(this) {
                @Override
                public void onTick(long millisUntilFinished) {
                    mCountdownTime.updateShow(millisUntilFinished);
                }

                @Override
                public void onFinish() {
                    mTvTiemPrefix.setText("已结束 ");
                    mCountdownTime.updateShow(0);
                }
            };
        }
        mCountDownTimer.setMillisInFuture(millisInFuture);
        mCountDownTimer.start();
    }

    @OnClick({R.id.tv_home, R.id.tv_qy, R.id.tv_buy, R.id.ll_back2, R.id.ll_share2, R.id.ll_back, R.id.ll_share, R.id.ll_service, R.id.ll_service2})
    public void onViewClicked(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.ll_back:
            case R.id.ll_back2:
                finish();
                break;
            //跳转购物车
            case R.id.ll_service:
            case R.id.ll_service2:
                intent = new Intent(this, ShopCarActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_home:
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_qy:
                skipServiceDataInfo();
                break;
            case R.id.tv_buy:
                if (userId != 0) {
                    setClickProductTotal();
                    if (mGroupDetailEntity != null) {
                        skipAliBC(this, mGroupDetailEntity.getThirdUrl(), mGroupDetailEntity.getThirdId(), false);
                        Properties prop = new Properties();
                        prop.setProperty("proName", getStrings(mGroupDetailEntity.getTitle()));
                        prop.setProperty("proId", String.valueOf(mGroupDetailEntity.getId()));
                        StatService.trackCustomKVEvent(this, "timeProAhead", prop);
                    }
                } else {
                    getLoginStatus(this);
                }
                break;
            case R.id.ll_share2:
            case R.id.ll_share:
                if (mGroupDetailEntity != null) {
                    new UMShareAction(this
                            , mGroupDetailEntity.getPicUrl()
                            , getStringsFormat(this, R.string.group_price, mGroupDetailEntity.getPrice()) + mGroupDetailEntity.getTitle()
                            , getStrings(mGroupDetailEntity.getSubtitle())
                            , sharePageUrl + mGroupDetailEntity.getId(), mGroupDetailEntity.getId());
                }
                break;
        }
    }

    /**
     * 设置限时特惠购买统计
     */
    private void setClickProductTotal() {
        Map<String, Object> params = new HashMap<>();
        params.put("productId", mId);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, TIME_PRODUCT_CLICK_TOTAL, params, null);
    }

    //    七鱼客服
    private void skipServiceDataInfo() {
        QyProductIndentInfo qyProductIndentInfo = null;
        String sourceTitle = "";
        String sourceUrl = "";
        if (mGroupDetailEntity != null) {
            qyProductIndentInfo = new QyProductIndentInfo();
            sourceUrl = sharePageUrl + mGroupDetailEntity.getId();
            sourceTitle = "限时特惠详情：" + mGroupDetailEntity.getTitle();
            qyProductIndentInfo.setUrl(sourceUrl);
            qyProductIndentInfo.setTitle(getStrings(mGroupDetailEntity.getTitle()));
            qyProductIndentInfo.setPicUrl(getStrings(mGroupDetailEntity.getPicUrl()));
            qyProductIndentInfo.setNote("¥" + getStrings(mGroupDetailEntity.getPrice()));
        }
        QyServiceUtils.getQyInstance().openQyServiceChat(this, sourceTitle, sourceUrl, qyProductIndentInfo);
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    @Override
    public View getLoadView() {
        return mFlProductDetails;
    }
}
