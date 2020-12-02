package com.amkj.dmsh.mine.activity;

import android.content.Intent;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.base.TinkerBaseApplicationLike;
import com.amkj.dmsh.bean.RequestStatus;
import com.amkj.dmsh.bean.TabEntity;
import com.amkj.dmsh.constant.CommunalAdHolderView;
import com.amkj.dmsh.constant.ConstantVariable;
import com.amkj.dmsh.constant.UMShareAction;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.homepage.adapter.CommunalDetailAdapter;
import com.amkj.dmsh.homepage.bean.CommunalADActivityEntity.CommunalADActivityBean;
import com.amkj.dmsh.mine.adapter.ZeroLotteryAdapter;
import com.amkj.dmsh.mine.bean.ZeroDetailEntity;
import com.amkj.dmsh.mine.bean.ZeroDetailEntity.ZeroDetailBean;
import com.amkj.dmsh.mine.bean.ZeroLotteryEntity.ZeroLotteryBean;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.shopdetails.bean.CommunalDetailObjectBean;
import com.amkj.dmsh.utils.CountDownTimer;
import com.amkj.dmsh.utils.TimeUtils;
import com.amkj.dmsh.utils.gson.GsonUtils;
import com.amkj.dmsh.utils.webformatdata.CommunalWebDetailUtils;
import com.amkj.dmsh.views.alertdialog.AlertDialogZeroLottery;
import com.amkj.dmsh.views.flycoTablayout.CommonTabLayout;
import com.amkj.dmsh.views.flycoTablayout.listener.CustomTabEntity;
import com.amkj.dmsh.views.flycoTablayout.listener.OnTabSelectListener;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.gyf.barlibrary.ImmersionBar;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.tencent.bugly.beta.tinker.TinkerManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;
import cn.iwgang.countdownview.CountdownView;
import cn.iwgang.countdownview.DynamicConfig;
import me.jessyan.autosize.utils.AutoSizeUtils;
import q.rorbin.badgeview.Badge;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.dismissLoadhud;
import static com.amkj.dmsh.constant.ConstantMethod.getBadge;
import static com.amkj.dmsh.constant.ConstantMethod.getIntegralFormat;
import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.getRmbFormat;
import static com.amkj.dmsh.constant.ConstantMethod.getStringChangeIntegers;
import static com.amkj.dmsh.constant.ConstantMethod.getStringsChNPrice;
import static com.amkj.dmsh.constant.ConstantMethod.getStringsFormat;
import static com.amkj.dmsh.constant.ConstantMethod.showImageActivity;
import static com.amkj.dmsh.constant.ConstantMethod.showLoadhud;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.skipProductUrl;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.dao.OrderDao.getCarCount;
import static com.amkj.dmsh.find.activity.ImagePagerActivity.IMAGE_DEF;
import static com.amkj.dmsh.shopdetails.bean.CommunalDetailObjectBean.TYPE_PRODUCT_TITLE;
import static com.amkj.dmsh.utils.glide.GlideImageLoaderUtil.getSquareImgUrl;

/**
 * Created by xiaoxin on 2020/7/25
 * Version:v4.7.0
 * ClassDescription :0元试用商品详情
 */
public class ZeroActivityDetailActivity extends BaseActivity {
    @BindView(R.id.banner_ql_sp_pro_details)
    ConvenientBanner mBannerQlSpProDetails;
    @BindView(R.id.tv_ql_sp_pro_sc_name)
    TextView mTvQlSpProScName;
    @BindView(R.id.tv_price)
    TextView mTvPrice;
    @BindView(R.id.tv_market_price)
    TextView mTvMarketPrice;
    @BindView(R.id.tv_quantity)
    TextView mTvQuantity;
    @BindView(R.id.tv_apply_num)
    TextView mTvApplyNum;
    @BindView(R.id.ll_pro_layout)
    LinearLayout mLlProLayout;
    @BindView(R.id.rv_product_details)
    RecyclerView mRvProductDetails;
    @BindView(R.id.scroll_pro)
    NestedScrollView mScrollPro;
    @BindView(R.id.smart_ql_sp_pro_details)
    SmartRefreshLayout mSmartQlSpProDetails;
    @BindView(R.id.cv_countdownTime)
    CountdownView mCvCountdownTime;
    @BindView(R.id.tv_apply)
    TextView mTvApply;
    @BindView(R.id.fl_product_details)
    FrameLayout mFlProductDetails;
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
    @BindView(R.id.rl_toolbar2)
    RelativeLayout mRlToolbar2;
    @BindView(R.id.fl_quality_bar)
    FrameLayout mFlQualityBar;
    @BindView(R.id.tv_end)
    TextView mTvEnd;
    @BindView(R.id.ll_lottery)
    LinearLayout mLlLottery;
    @BindView(R.id.ll_step)
    LinearLayout mLlStep;
    @BindView(R.id.ll_apply)
    LinearLayout mLlApply;
    @BindView(R.id.tv_buy)
    TextView mTvBuy;
    @BindView(R.id.tv_total)
    TextView mTvTotal;
    @BindView(R.id.fl_header_service)
    RelativeLayout mFlHeaderService;
    @BindView(R.id.rv_lottery)
    RecyclerView mRvLottery;
    @BindView(R.id.tv_look_product)
    TextView mTvLookProduct;


    private String[] detailTabData = {"商品", "详情"};
    private ArrayList<CustomTabEntity> tabData = new ArrayList<>();
    private int measuredHeight;
    private String mActivityId;
    private ZeroDetailEntity mZeroDetailEntity;
    private ZeroDetailBean mZeroDetailBean;
    private List<CommunalDetailObjectBean> shopDetailBeanList = new ArrayList<>();
    private List<CommunalADActivityBean> imagesVideoList = new ArrayList<>();
    List<ZeroLotteryBean> mLotteryBeans = new ArrayList<>();
    private CommunalDetailAdapter communalDetailAdapter;
    private String sharePageUrl = Url.BASE_SHARE_PAGE_TWO + "vip/tryout_detail.html?id=";
    private CBViewHolderCreator cbViewHolderCreator;
    private CountDownTimer mCountDownTimer;
    private DynamicConfig.Builder mDynamic;
    private Badge badge;
    private ZeroLotteryAdapter mZeroLotteryAdapter;
    private AlertDialogZeroLottery mAlertDialogZeroLottery;

    @Override
    protected int getContentView() {
        return R.layout.activity_zero_detail;
    }

    @Override
    public void setStatusBar() {
        ImmersionBar.with(this).keyboardEnable(true).navigationBarEnable(false).statusBarDarkFont(true).fullScreen(true).init();
    }

    @Override
    protected void initViews() {
        if (getIntent() != null) {
            mActivityId = getIntent().getStringExtra("activityId");
        } else {
            showToast("数据有误，请重试");
            finish();
        }
        badge = getBadge(getActivity(), mFlHeaderService);
        mTvMarketPrice.setPaintFlags(mTvMarketPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        //动态修改标题栏padding
        int statusBarHeight = ImmersionBar.getStatusBarHeight(this);
        int paddingTop = statusBarHeight > 0 ? statusBarHeight : AutoSizeUtils.mm2px(this, 40);
        mRlToolbar.setPadding(0, paddingTop, 0, 0);
        mRlToolbar2.setPadding(0, paddingTop, 0, 0);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) mFlHeaderService.getLayoutParams();
        layoutParams.topMargin = paddingTop;
        mFlHeaderService.setLayoutParams(layoutParams);
        //设置结束时间倒计时
        mDynamic = new DynamicConfig.Builder();
        mDynamic.setSuffixTextSize(AutoSizeUtils.mm2px(mAppContext, 30));
        mDynamic.setTimeTextSize(AutoSizeUtils.mm2px(mAppContext, 34));
        mDynamic.setSuffixGravity(Gravity.CENTER);
        DynamicConfig.BackgroundInfo backgroundInfo = new DynamicConfig.BackgroundInfo();
        backgroundInfo.setColor(getResources().getColor(R.color.vip_bg_color))
                .setBorderRadius((float) AutoSizeUtils.mm2px(mAppContext, 5))
                .setBorderColor(getResources().getColor(R.color.vip_bg_color))
                .setShowTimeBgBorder(true);
        mDynamic.setBackgroundInfo(backgroundInfo);
        mCvCountdownTime.dynamicShow(mDynamic.build());
        //初始化中奖名单
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        mRvLottery.setLayoutManager(gridLayoutManager);
        mZeroLotteryAdapter = new ZeroLotteryAdapter(this, mLotteryBeans, false);
        mRvLottery.setAdapter(mZeroLotteryAdapter);

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
    }

    @Override
    protected void loadData() {
        getCarCount(getActivity());
        getZeroDetail();
    }

    private void getZeroDetail() {
        Map<String, String> map = new HashMap<>();
        map.put("activityId", mActivityId);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, Url.GET_ZERO_PRODUCT_DETAIL, map, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                mSmartQlSpProDetails.finishRefresh();
                mZeroDetailEntity = GsonUtils.fromJson(result, ZeroDetailEntity.class);
                if (mZeroDetailEntity != null) {
                    if (SUCCESS_CODE.equals(mZeroDetailEntity.getCode())) {
                        mZeroDetailBean = mZeroDetailEntity.getResult();
                        setProductData(mZeroDetailBean);
                    } else {
                        showToast(mZeroDetailEntity.getMsg());
                    }
                }
                NetLoadUtils.getNetInstance().showLoadSir(loadService, mZeroDetailBean, mZeroDetailEntity);
            }

            @Override
            public void onNotNetOrException() {
                mSmartQlSpProDetails.finishRefresh();
                NetLoadUtils.getNetInstance().showLoadSir(loadService, mZeroDetailBean, mZeroDetailEntity);
            }
        });
    }

    private void setProductData(ZeroDetailBean zeroDetailBean) {
        if (zeroDetailBean == null) return;
        String name = TextUtils.isEmpty(zeroDetailBean.getSubtitle()) ? zeroDetailBean.getProductName() : (zeroDetailBean.getSubtitle() + "•" + zeroDetailBean.getProductName());
        mTvQlSpProScName.setText(name);
        mTvPrice.setText(getRmbFormat(this, zeroDetailBean.getPrice()));
        mTvMarketPrice.setText(getStringsChNPrice(this, zeroDetailBean.getMarketPrice()));
        mTvQuantity.setText(getStringsFormat(this, R.string.limit_quantity, zeroDetailBean.getCount()));
        mTvApplyNum.setText(getStringsFormat(this, R.string.zero_apply_num, zeroDetailBean.getPartakeCount()));
        mTvApply.setText(zeroDetailBean.isPartake() ? "已申请" : "申请试用");
        mTvApply.setEnabled(!zeroDetailBean.isPartake());
        mTvLookProduct.setVisibility(zeroDetailBean.getProductStatus() == 1 ? View.VISIBLE : View.GONE);
        //报名结束倒计时
        if (TimeUtils.isEndOrStartTime(mZeroDetailEntity.getCurrentTime(), zeroDetailBean.getEndTime())) {
            zeroActivityEnd(zeroDetailBean);
        } else {
            mLlStep.setVisibility(View.VISIBLE);
            mLlLottery.setVisibility(View.GONE);
            mTvBuy.setVisibility(View.GONE);
            mLlApply.setVisibility(View.VISIBLE);
            mCvCountdownTime.setVisibility(View.VISIBLE);
            long millisInFuture = TimeUtils.getTimeDifference(mZeroDetailEntity.getCurrentTime(), zeroDetailBean.getEndTime());
            if (mCountDownTimer == null) {
                mCountDownTimer = new CountDownTimer(this) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        mCvCountdownTime.updateShow(millisUntilFinished);
                    }

                    @Override
                    public void onFinish() {
                        mTvEnd.setText("报名已结束");
                        mCvCountdownTime.updateShow(0);
                        mCvCountdownTime.setVisibility(View.GONE);
                        loadData();
                    }
                };
            }
            mCountDownTimer.setMillisInFuture(millisInFuture);
            mCountDownTimer.start();
        }
        //轮播位（包含图片以及视频）
        imagesVideoList.clear();
        List<String> imageList = Arrays.asList(zeroDetailBean.getProductImg().split(","));
        if (zeroDetailBean.haveVideo()) {
            imagesVideoList.add(new CommunalADActivityBean("", zeroDetailBean.getVideoUrl()));
        }
        for (int i = 0; i < imageList.size(); i++) {
            String imgUrl = imageList.get(i);
            imagesVideoList.add(new CommunalADActivityBean(i == 0 ? getSquareImgUrl(imgUrl, ((TinkerBaseApplicationLike) TinkerManager.getTinkerApplicationLike()).getScreenWidth(), "") : imgUrl, ""));
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
                        position - (mZeroDetailBean.haveVideo() ? 1 : 0), imageList);
            }
        });
        //商品详情
        shopDetailBeanList.clear();
        List<CommunalDetailObjectBean> detailsDataList = CommunalWebDetailUtils.getCommunalWebInstance().getWebDetailsFormatDataList(zeroDetailBean.getItemBody());
        CommunalDetailObjectBean communalDetailObjectBean = new CommunalDetailObjectBean();
        communalDetailObjectBean.setItemType(TYPE_PRODUCT_TITLE);
        communalDetailObjectBean.setContent("商品详情");
        shopDetailBeanList.add(communalDetailObjectBean);
        shopDetailBeanList.addAll(detailsDataList);
        communalDetailAdapter.notifyDataSetChanged();
    }

    private void zeroActivityEnd(ZeroDetailBean zeroDetailBean) {
        //活动已结束
        mTvEnd.setText("报名已结束");
        mCvCountdownTime.updateShow(0);
        mCvCountdownTime.setVisibility(View.GONE);
        mLlStep.setVisibility(View.GONE);
        mTvBuy.setVisibility(zeroDetailBean.getProductStatus() == 1 ? View.VISIBLE : View.GONE);
        mLlApply.setVisibility(View.GONE);

        //显示中奖名单
        List<ZeroLotteryBean> winnersInfoList = mZeroDetailBean.getWinnersInfoList();
        if (winnersInfoList != null && winnersInfoList.size() > 0) {
            mLlLottery.setVisibility(View.VISIBLE);
            mTvTotal.setText(getIntegralFormat(this, R.string.total_lottery, winnersInfoList.size()));
            mLotteryBeans.clear();
            mLotteryBeans.addAll(winnersInfoList);
            mZeroLotteryAdapter.notifyDataSetChanged();
        } else {
            mLlLottery.setVisibility(View.GONE);
        }
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

    @OnClick({R.id.tv_apply, R.id.ll_back2, R.id.ll_share2, R.id.ll_back, R.id.ll_share, R.id.ll_service, R.id.ll_service2, R.id.tv_look_product, R.id.iv_free_idea, R.id.tv_buy, R.id.ll_total_lottery})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
            case R.id.ll_back2:
                finish();
                break;
            case R.id.ll_share:
            case R.id.ll_share2:
                if (mZeroDetailBean != null && !TextUtils.isEmpty(mZeroDetailBean.getProductImg())) {
                    new UMShareAction(this, mZeroDetailBean.getProductImg().split(",")[0],
                            getStringsFormat(this, R.string.zero_detail_share_title, mZeroDetailBean.getProductName()),
                            getStringsFormat(this, R.string.zero_detail_share_title, mZeroDetailBean.getProductName()),
                            sharePageUrl + mActivityId, getStringChangeIntegers(mActivityId));
                }
                break;
            case R.id.tv_look_product:
                skipProductUrl(this, 1, getStringChangeIntegers(mZeroDetailBean.getProductId()));
                break;
            //全部中奖名单
            case R.id.ll_total_lottery:
                if (mAlertDialogZeroLottery == null) {
                    mAlertDialogZeroLottery = new AlertDialogZeroLottery(getActivity());
                }
                mAlertDialogZeroLottery.update(mLotteryBeans);
                mAlertDialogZeroLottery.show(Gravity.BOTTOM);
                break;
            //去购买
            case R.id.tv_buy:
                skipProductUrl(this, 1, getStringChangeIntegers(mZeroDetailBean.getProductId()));
                break;
            //试用心得
            case R.id.iv_free_idea:
                Intent intent = new Intent(this, ZeroReportListActivity.class);
                startActivity(intent);
                break;
            //申请使用
            case R.id.tv_apply:
                if (userId > 0) {
                    applyZero();
                } else {
                    getLoginStatus(this);
                }
                break;
            //跳转购物车
            case R.id.ll_service:
            case R.id.ll_service2:
                intent = new Intent(getActivity(), ShopCarActivity.class);
                startActivity(intent);
                break;
        }
    }

    //申请试用
    private void applyZero() {
        showLoadhud(this);
        Map<String, String> map = new HashMap<>();
        map.put("activityId", mActivityId);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, Url.GET_APPLY_ZERO, map, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                dismissLoadhud(getActivity());
                RequestStatus requestStatus = GsonUtils.fromJson(result, RequestStatus.class);
                if (requestStatus != null) {
                    if (SUCCESS_CODE.equals(requestStatus.getCode())) {
                        showToast("申请成功");
                        mTvApply.setText("已申请");
                        mTvApply.setEnabled(false);
                    } else {
                        showToast(requestStatus.getMsg());
                    }
                }
            }

            @Override
            public void onNotNetOrException() {
                dismissLoadhud(getActivity());
            }
        });
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    @Override
    public View getLoadView() {
        return mFlProductDetails;
    }

    @Override
    protected void postEventResult(@NonNull EventMessage message) {
        if (message.type.equals(ConstantVariable.UPDATE_CAR_NUM)) {
            if (badge != null) {
                badge.setBadgeNumber((int) message.result);
            }
        }
    }
}
