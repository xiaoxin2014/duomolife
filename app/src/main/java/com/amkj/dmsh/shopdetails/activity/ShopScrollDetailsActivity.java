package com.amkj.dmsh.shopdetails.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
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
import com.amkj.dmsh.constant.CommunalAdHolderView;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.ConstantVariable;
import com.amkj.dmsh.constant.TabEntity;
import com.amkj.dmsh.constant.TotalPersonalTrajectory;
import com.amkj.dmsh.constant.UMShareAction;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.dominant.activity.DirectProductEvaluationActivity;
import com.amkj.dmsh.dominant.activity.QualityGroupShopDetailActivity;
import com.amkj.dmsh.dominant.activity.QualityProductActActivity;
import com.amkj.dmsh.dominant.activity.ShopTimeScrollDetailsActivity;
import com.amkj.dmsh.homepage.activity.ArticleOfficialActivity;
import com.amkj.dmsh.homepage.adapter.CommunalDetailAdapter;
import com.amkj.dmsh.homepage.bean.CommunalADActivityEntity.CommunalADActivityBean;
import com.amkj.dmsh.mine.activity.ShopCarActivity;
import com.amkj.dmsh.mine.bean.ShopCarNewInfoEntity.ShopCarNewInfoBean.ActivityInfoBean;
import com.amkj.dmsh.mine.bean.ShopCarNewInfoEntity.ShopCarNewInfoBean.CartInfoBean;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.qyservice.QyProductIndentInfo;
import com.amkj.dmsh.qyservice.QyServiceUtils;
import com.amkj.dmsh.shopdetails.adapter.DirectEvaluationAdapter;
import com.amkj.dmsh.shopdetails.adapter.GoodsGroupAdapter;
import com.amkj.dmsh.shopdetails.adapter.GoodsRecommendAdapter;
import com.amkj.dmsh.shopdetails.adapter.ProductTextAdapter;
import com.amkj.dmsh.shopdetails.bean.CommunalDetailObjectBean;
import com.amkj.dmsh.shopdetails.bean.DirectGoodsServerEntity;
import com.amkj.dmsh.shopdetails.bean.DirectGoodsServerEntity.DirectGoodsServerBean;
import com.amkj.dmsh.shopdetails.bean.EditGoodsSkuEntity.EditGoodsSkuBean;
import com.amkj.dmsh.shopdetails.bean.GoodsCommentEntity;
import com.amkj.dmsh.shopdetails.bean.GoodsCommentEntity.GoodsCommentBean;
import com.amkj.dmsh.shopdetails.bean.ShopCarGoodsSku;
import com.amkj.dmsh.shopdetails.bean.ShopDetailsEntity;
import com.amkj.dmsh.shopdetails.bean.ShopDetailsEntity.ShopPropertyBean;
import com.amkj.dmsh.shopdetails.bean.ShopDetailsEntity.ShopPropertyBean.CouponJsonBean;
import com.amkj.dmsh.shopdetails.bean.ShopDetailsEntity.ShopPropertyBean.PresentProductInfoBean;
import com.amkj.dmsh.shopdetails.bean.ShopDetailsEntity.ShopPropertyBean.TagsBean;
import com.amkj.dmsh.shopdetails.bean.ShopRecommendHotTopicEntity;
import com.amkj.dmsh.shopdetails.bean.ShopRecommendHotTopicEntity.ShopRecommendHotTopicBean;
import com.amkj.dmsh.shopdetails.integration.IntegralScrollDetailsActivity;
import com.amkj.dmsh.user.activity.UserPagerActivity;
import com.amkj.dmsh.user.bean.UserLikedProductEntity.LikedProductBean.MarketLabelBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.amkj.dmsh.utils.webformatdata.CommunalWebDetailUtils;
import com.amkj.dmsh.utils.webformatdata.ShareDataBean;
import com.amkj.dmsh.views.bottomdialog.SkuDialog;
import com.amkj.dmsh.views.flycoTablayout.CommonTabLayout;
import com.amkj.dmsh.views.flycoTablayout.listener.CustomTabEntity;
import com.amkj.dmsh.views.flycoTablayout.listener.OnTabSelectListener;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.flexbox.FlexboxLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.klinker.android.link_builder.Link;
import com.klinker.android.link_builder.LinkBuilder;
import com.melnykov.fab.FloatingActionButton;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.tencent.bugly.beta.tinker.TinkerManager;
import com.tencent.stat.StatService;
import com.umeng.socialize.UMShareAPI;

import org.greenrobot.eventbus.EventBus;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.iwgang.countdownview.CountdownView;
import cn.iwgang.countdownview.DynamicConfig;
import me.jessyan.autosize.utils.AutoSizeUtils;
import q.rorbin.badgeview.Badge;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.getBadge;
import static com.amkj.dmsh.constant.ConstantMethod.getCarCount;
import static com.amkj.dmsh.constant.ConstantMethod.getDistinctString;
import static com.amkj.dmsh.constant.ConstantMethod.getFloatNumber;
import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.getRmbFormat;
import static com.amkj.dmsh.constant.ConstantMethod.getSpannableString;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.getStringsFormat;
import static com.amkj.dmsh.constant.ConstantMethod.isEndOrStartTime;
import static com.amkj.dmsh.constant.ConstantMethod.isEndOrStartTimeAddSeconds;
import static com.amkj.dmsh.constant.ConstantMethod.showImageActivity;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.showToastRequestMsg;
import static com.amkj.dmsh.constant.ConstantMethod.stripTrailingZeros;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.IS_LOGIN_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.RECOMMEND_PRODUCT;
import static com.amkj.dmsh.constant.ConstantVariable.RECOMMEND_TYPE;
import static com.amkj.dmsh.constant.ConstantVariable.REGEX_NUM;
import static com.amkj.dmsh.constant.ConstantVariable.START_AUTO_PAGE_TURN;
import static com.amkj.dmsh.constant.ConstantVariable.STOP_AUTO_PAGE_TURN;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.TOTAL_NAME_TYPE;
import static com.amkj.dmsh.constant.ConstantVariable.TYPE_2;
import static com.amkj.dmsh.constant.ConstantVariable.isShowTint;
import static com.amkj.dmsh.find.activity.ImagePagerActivity.IMAGE_DEF;
import static com.amkj.dmsh.shopdetails.bean.CommunalDetailObjectBean.TYPE_PRODUCT_MORE;
import static com.amkj.dmsh.shopdetails.bean.CommunalDetailObjectBean.TYPE_PRODUCT_TITLE;
import static com.amkj.dmsh.utils.ProductLabelCreateUtils.getLabelInstance;
import static com.amkj.dmsh.utils.glide.GlideImageLoaderUtil.getWaterMarkImgUrl;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/7/15
 * class description: 良品商品详情
 */
public class ShopScrollDetailsActivity extends BaseActivity {
    @BindView(R.id.smart_ql_sp_pro_details)
    SmartRefreshLayout smart_ql_sp_pro_details;
    @BindView(R.id.banner_ql_sp_pro_details)
    ConvenientBanner banner_ql_sp_pro_details;
    @BindView(R.id.tv_group_product)
    TextView tv_group_product;
    @BindView(R.id.ctb_qt_pro_details)
    CommonTabLayout ctb_qt_pro_details;
    //            商品名字
    @BindView(R.id.tv_ql_sp_pro_sc_name)
    TextView tv_ql_sp_pro_sc_name;
    //            商品价格
    @BindView(R.id.tv_ql_sp_pro_sc_price)
    TextView tv_ql_sp_pro_sc_price;
    //            市场价格
    @BindView(R.id.tv_ql_sp_pro_sc_market_price)
    TextView tv_ql_sp_pro_sc_market_price;
    //    开售时间
    @BindView(R.id.rel_shop_pro_time)
    RelativeLayout rel_shop_pro_time;
    //    计时器状态
    @BindView(R.id.tv_pro_time_detail_status)
    TextView tv_pro_time_detail_status;
    //    计时器状态
    @BindView(R.id.ct_pro_show_time_detail)
    CountdownView ct_pro_show_time_detail;
    @BindView(R.id.fbl_details_market_label)
    FlexboxLayout fbl_details_market_label;
    //    标签布局
    @BindView(R.id.ll_layout_pro_sc_tag)
    RelativeLayout ll_layout_pro_sc_tag;
    @BindView(R.id.flex_product_tag)
    FlexboxLayout flex_product_tag;
    @BindView(R.id.flex_bug_before)
    FlexboxLayout flex_buy_before;
    //    售前内容
    @BindView(R.id.ll_pro_buy_before)
    LinearLayout ll_pro_buy_before;

    @BindView(R.id.communal_recycler_wrap)
    RecyclerView communal_recycler_wrap;
    @BindView(R.id.rel_shop_details_comment)
    RelativeLayout rel_shop_details_comment;
    //  是否可使用优惠券
    @BindView(R.id.iv_ql_shop_pro_cp_tag)
    ImageView iv_ql_shop_pro_cp_tag;
    //    商品详情 服务承诺
    @BindView(R.id.rv_product_details)
    RecyclerView rv_product_details;
    //    Sku展示
    @BindView(R.id.ll_sp_pro_sku_value)
    LinearLayout ll_sp_pro_sku_value;
    //    sku
    @BindView(R.id.tv_ql_sp_pro_sku)
    TextView tv_ql_sp_pro_sku;
    //    加入购物车
    @BindView(R.id.tv_sp_details_add_car)
    TextView tv_sp_details_add_car;
    //    目的 仅作为测量商品高度
    @BindView(R.id.ll_pro_layout)
    LinearLayout ll_pro_layout;

    //    立即购买
    @BindView(R.id.tv_sp_details_buy_it)
    TextView tv_sp_details_buy_it;
    // 首次分享提示
    @BindView(R.id.tv_product_share_tint)
    TextView tv_product_share_tint;

    @BindView(R.id.fl_header_service)
    RelativeLayout fl_header_service;
    //    商品收藏
    @BindView(R.id.tv_sp_details_collect)
    TextView tv_sp_details_collect;
    //  营销活动规则
    @BindView(R.id.ll_product_activity_detail)
    LinearLayout ll_product_activity_detail;
    @BindView(R.id.tv_product_activity_description)
    TextView tv_product_activity_description;
    //        结束时间
    @BindView(R.id.ll_communal_time_hours)
    LinearLayout ll_communal_time_hours;
    @BindView(R.id.tv_count_time_before_white)
    TextView tv_count_time_before_white;
    @BindView(R.id.cv_countdownTime_white_hours)
    CountdownView cv_countdownTime_white_hours;
    //    赠品信息
    @BindView(R.id.rv_shop_details_text_communal)
    RecyclerView rv_shop_details_text_communal;

    @BindView(R.id.scroll_pro)
    NestedScrollView scroll_pro;
    @BindView(R.id.fl_product_details)
    FrameLayout fl_product_details;
    @BindView(R.id.ll_details_bottom)
    LinearLayout ll_details_bottom;
    @BindView(R.id.download_btn_communal)
    FloatingActionButton download_btn_communal;
    @BindView(R.id.fl_quality_bar)
    FrameLayout mTlQualityBar;
    @BindView(R.id.iv_life_back)
    ImageView mIvLifeBack;
    @BindView(R.id.iv_img_service)
    ImageView mIvImgService;
    @BindView(R.id.iv_img_share)
    ImageView mIvImgShare;
    @BindView(R.id.iv_life_back2)
    ImageView mIvLifeBack2;
    @BindView(R.id.iv_img_service2)
    ImageView mIvImgService2;
    @BindView(R.id.iv_img_share2)
    ImageView mIvImgShare2;
    @BindView(R.id.rl_toolbar)
    LinearLayout mRlToolbar;
    @BindView(R.id.rl_toolbar2)
    RelativeLayout mRlToolbar2;
    @BindView(R.id.rv_goods_recommend)
    RecyclerView mRvGoodsRecommend;
    @BindView(R.id.rv_goods_group)
    RecyclerView mRvGoodsGroup;
    @BindView(R.id.iv_artical_cover)
    ImageView mIvArticalCover;
    @BindView(R.id.tv_artical_title)
    TextView mTvArticalTitle;
    @BindView(R.id.tv_artical_desc)
    TextView mTvArticalDesc;
    @BindView(R.id.ll_artical)
    LinearLayout mLlArtical;
    @BindView(R.id.iv_more_tag)
    ImageView mIvMoreTag;
    @BindView(R.id.flex_product_point)
    FlexboxLayout mFlexProductPoint;
    @BindView(R.id.tv_max_price)
    TextView mTvMaxPrice;
    @BindView(R.id.tv_activity_price)
    TextView mTvActivityPrice;
    @BindView(R.id.ll_product_recommend)
    LinearLayout mllProductRecommend;
    @BindView(R.id.ll_product_group)
    LinearLayout mllProductGroup;
    @BindView(R.id.ll_scrool_detail_price)
    RelativeLayout mLlScroolDetailPrice;
    @BindView(R.id.rl_activity_price)
    RelativeLayout mRlActivityPrice;
    @BindView(R.id.ll_countdown_time_bottom)
    LinearLayout mLlTimeHoursBottom;
    @BindView(R.id.tv_product_min_price)
    TextView mTvProductMinPrice;
    @BindView(R.id.tv_product_martket_price)
    TextView mTvProductMartketPrice;
    @BindView(R.id.tv_tips)
    TextView mTvTipsBottom;
    @BindView(R.id.cv_countdownTime_bottom)
    CountdownView mCtCountDownBottom;
    @BindView(R.id.iv_next_icon)
    ImageView mIvNextIcon;
    @BindView(R.id.rl_product_activity_detail)
    LinearLayout rl_product_activity_detail;


    //    赠品信息
    private List<PresentProductInfoBean> presentProductInfoBeans = new ArrayList<>();
    //    文章主题推荐
    private List<ShopRecommendHotTopicBean> articalRecommendList = new ArrayList<>();
    //    商品推荐
    private List<ShopRecommendHotTopicBean> goodsRecommendList = new ArrayList<>();
    //    组合商品
    private List<ShopRecommendHotTopicBean> goodsGroupList = new ArrayList<>();
    //    轮播图片视频
    private List<CommunalADActivityBean> imagesVideoList = new ArrayList<>();
    //    商品评论
    private List<GoodsCommentBean> goodsComments = new ArrayList<>();
    //商品详情 服务承诺 合集
    private List<CommunalDetailObjectBean> shopDetailBeanList = new ArrayList<>();
    //    服务承诺
    private List<CommunalDetailObjectBean> serviceDataList = new ArrayList<>();
    //    服务承诺总条数
    private List<CommunalDetailObjectBean> serviceDataTotalList = new ArrayList<>();

    private DirectEvaluationAdapter directEvaluationAdapter;
    private ShopCommentHeaderView shopCommentHeaderView;
    private String productId;
    private ShopDetailsEntity shopDetailsEntity;
    private ShopPropertyBean shopPropertyBean;
    private EditGoodsSkuBean editGoodsSkuBean;
    private SkuDialog skuDialog;
    private ShopCarGoodsSku shopCarGoodsSkuDif;
    private Badge badge;
    //    private View proTitleView;
    private String sharePageUrl = Url.BASE_SHARE_PAGE_TWO + "m/template/common/proprietary.html?id=";
    private ConstantMethod constantMethod;
    private boolean isWaitSellStatus;
    private ProductTextAdapter presentProAdapter;
    private CommunalDetailAdapter communalDetailAdapter;
    private String[] detailTabData = {"商品", "详情"};
    private ArrayList<CustomTabEntity> tabData = new ArrayList<>();
    private int measuredHeight;
    private CBViewHolderCreator cbViewHolderCreator;
    private String recommendFlag;
    private String recommendType;
    private int skuSaleBeanId;
    private int screenHeight;
    private GoodsRecommendAdapter mGoodsRecommendAdapter;
    private GoodsGroupAdapter mGoodsGroupAdapter;

    @Override
    protected int getContentView() {
        return R.layout.activity_ql_shop_pro_details;
    }

    @Override
    protected void initViews() {
        tv_product_activity_description.setSelected(true);
        DynamicConfig.Builder dynamicDetails = new DynamicConfig.Builder();
        dynamicDetails.setSuffixTextSize(AutoSizeUtils.mm2px(mAppContext, 22));
        dynamicDetails.setTimeTextSize(AutoSizeUtils.mm2px(mAppContext, 22));
        ct_pro_show_time_detail.dynamicShow(dynamicDetails.build());
        //设置上面活动倒计时
        DynamicConfig.Builder dynamic = new DynamicConfig.Builder();
        dynamic.setSuffixTextSize(AutoSizeUtils.mm2px(mAppContext, 26));
        dynamic.setTimeTextSize(AutoSizeUtils.mm2px(mAppContext, 26));
        dynamic.setSuffixGravity(Gravity.CENTER);
        DynamicConfig.BackgroundInfo backgroundInfo = new DynamicConfig.BackgroundInfo();
        backgroundInfo.setColor(getResources().getColor(R.color.text_normal_red))
                .setBorderRadius((float) AutoSizeUtils.mm2px(mAppContext, 5))
                .setBorderColor(getResources().getColor(R.color.text_normal_red))
                .setShowTimeBgBorder(true);
        dynamic.setBackgroundInfo(backgroundInfo);
        cv_countdownTime_white_hours.dynamicShow(dynamic.build());
        //设置下面活动倒计时
        DynamicConfig.Builder dynamicBottom = new DynamicConfig.Builder();
        dynamicBottom.setSuffixTextSize(AutoSizeUtils.mm2px(mAppContext, 26));
        dynamicBottom.setTimeTextSize(AutoSizeUtils.mm2px(mAppContext, 26));
        dynamicBottom.setSuffixGravity(Gravity.CENTER);
        DynamicConfig.BackgroundInfo backgroundInfoBottom = new DynamicConfig.BackgroundInfo();
        backgroundInfoBottom.setColor(getResources().getColor(R.color.white))
                .setBorderRadius((float) AutoSizeUtils.mm2px(mAppContext, 3))
                .setBorderColor(getResources().getColor(R.color.text_normal_red))
                .setBorderSize((float) AutoSizeUtils.mm2px(mAppContext, 1))
                .setShowTimeBgBorder(true);
        dynamicBottom.setBackgroundInfo(backgroundInfoBottom);
        mCtCountDownBottom.dynamicShow(dynamicBottom.build());
        Intent intent = getIntent();
        productId = intent.getStringExtra("productId");
        recommendFlag = intent.getStringExtra("recommendFlag");
        TinkerBaseApplicationLike app = (TinkerBaseApplicationLike) TinkerManager.getTinkerApplicationLike();
        screenHeight = app.getScreenHeight();
        recommendType = intent.getStringExtra(RECOMMEND_TYPE);
        if (TextUtils.isEmpty(productId)) {
            showToast(getActivity(), "商品信息有误，请重试");
            finish();
        }
        smart_ql_sp_pro_details.setOnRefreshListener(refreshLayout -> loadData());
        communal_recycler_wrap.setLayoutManager(new LinearLayoutManager(getActivity()));
        directEvaluationAdapter = new DirectEvaluationAdapter(getActivity(), goodsComments);
        directEvaluationAdapter.setHeaderAndEmpty(true);
//        评论头部
        View commentHeaderView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_shop_comment_header, null);
        shopCommentHeaderView = new ShopCommentHeaderView();
        ButterKnife.bind(shopCommentHeaderView, commentHeaderView);
        shopCommentHeaderView.initView();
        shopCommentHeaderView.scroll_details.setVisibility(GONE);
        directEvaluationAdapter.addHeaderView(commentHeaderView);
        communal_recycler_wrap.setNestedScrollingEnabled(false);
        communal_recycler_wrap.setAdapter(directEvaluationAdapter);
        directEvaluationAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            switch (view.getId()) {
                case R.id.img_direct_avatar:
                    GoodsCommentBean goodsCommentBean = (GoodsCommentBean) view.getTag(R.id.iv_avatar_tag);
                    if (goodsCommentBean != null) {
                        Intent intent1 = new Intent(getActivity(), UserPagerActivity.class);
                        intent1.putExtra("userId", String.valueOf(goodsCommentBean.getUserId()));
                        startActivity(intent1);
                    }
                    break;
                case R.id.tv_eva_count:
                    goodsCommentBean = (GoodsCommentBean) view.getTag();
                    if (goodsCommentBean != null && !goodsCommentBean.isFavor()) {
                        if (userId > 0) {
                            setProductEvaLike(view);
                        } else {
                            getLoginStatus(getActivity());
                        }
                    }
                    break;
            }
        });
        ll_details_bottom.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                ll_details_bottom.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int measuredHeight = ll_details_bottom.getMeasuredHeight();
                ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) download_btn_communal.getLayoutParams();
                int bottomMargin = marginLayoutParams.bottomMargin;
                int topMargin = marginLayoutParams.topMargin;
                int leftMargin = marginLayoutParams.leftMargin;
                int rightMargin = marginLayoutParams.rightMargin;
                marginLayoutParams.setMargins(leftMargin, topMargin, rightMargin, bottomMargin + measuredHeight);
                download_btn_communal.setLayoutParams(marginLayoutParams);
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rv_product_details.setLayoutManager(linearLayoutManager);
        rv_product_details.setNestedScrollingEnabled(false);
        communalDetailAdapter = new CommunalDetailAdapter(getActivity(), shopDetailBeanList);
        rv_product_details.setAdapter(communalDetailAdapter);
        communalDetailAdapter.setOnItemClickListener((adapter, view, position) -> {
            CommunalDetailObjectBean communalDetailObjectBean = (CommunalDetailObjectBean) view.getTag();
            if (communalDetailObjectBean != null
                    && TYPE_PRODUCT_MORE == communalDetailObjectBean.getItemType()
                    && communalDetailObjectBean.getMoreDataList() != null) {
                int serviceMorePosition = 0;
                if (shopDetailBeanList.size() > 0) {
                    serviceMorePosition = shopDetailBeanList.size() - 1;
                }
                shopDetailBeanList.removeAll(serviceDataList);
                shopDetailBeanList.addAll(communalDetailObjectBean.getMoreDataList());
                if (serviceMorePosition < shopDetailBeanList.size()) {
                    communalDetailAdapter.notifyItemRangeChanged(serviceMorePosition,
                            shopDetailBeanList.size());
                } else {
                    communalDetailAdapter.notifyDataSetChanged();
                }
            }
        });
        communalDetailAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            ShareDataBean shareDataBean = null;
            if (view.getId() == R.id.tv_communal_share && shopPropertyBean != null) {
                shareDataBean = new ShareDataBean(shopPropertyBean.getPicUrl()
                        , "我在多么生活看中了" + shopPropertyBean.getName()
                        , getStrings(shopPropertyBean.getSubtitle())
                        , sharePageUrl + shopPropertyBean.getId());
            }
            CommunalWebDetailUtils.getCommunalWebInstance()
                    .setWebDataClick(getActivity(), shareDataBean, view, loadHud);
        });

        for (int i = 0; i < detailTabData.length; i++) {
            tabData.add(new TabEntity(detailTabData[i], 0, 0));
        }
        ctb_qt_pro_details.setTextSize(AutoSizeUtils.mm2px(mAppContext, 30));
        ctb_qt_pro_details.setIndicatorWidth(AutoSizeUtils.mm2px(mAppContext, 28 * 2));
        ctb_qt_pro_details.setTabPadding(AutoSizeUtils.mm2px(mAppContext, 20));
        ctb_qt_pro_details.setTabData(tabData);
        ctb_qt_pro_details.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                scrollLocation(position);
            }

            @Override
            public void onTabReselect(int position) {
                scrollLocation(position);
            }
        });
        ctb_qt_pro_details.setCurrentTab(0);
        int bannerHeight = AutoSizeUtils.mm2px(this, 750);
        scroll_pro.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (measuredHeight < 1) {
                    measuredHeight = ll_pro_layout.getMeasuredHeight();
                }

                if (measuredHeight < 1) {
                    return;
                }
                int currentTab = ctb_qt_pro_details.getCurrentTab();
                if (scrollY >= 0 && scrollY < measuredHeight && currentTab != 0) {
                    ctb_qt_pro_details.setCurrentTab(0);
                } else if (scrollY >= measuredHeight && currentTab != 1) {
                    ctb_qt_pro_details.setCurrentTab(1);
                }
                if (scrollY > screenHeight * 1.5) {
                    if (download_btn_communal.getVisibility() == GONE) {
                        download_btn_communal.setVisibility(VISIBLE);
                        download_btn_communal.show(false);
                    }
                    if (!download_btn_communal.isVisible()) {
                        download_btn_communal.show(false);
                    }
                } else {
                    if (download_btn_communal.isVisible()) {
                        download_btn_communal.hide(false);
                    }
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

        //初始化优惠券列表
        rv_shop_details_text_communal.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_shop_details_text_communal.setNestedScrollingEnabled(false);
        presentProAdapter = new ProductTextAdapter(presentProductInfoBeans);
        rv_shop_details_text_communal.setAdapter(presentProAdapter);
        presentProAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                PresentProductInfoBean presentProductInfoBean = (PresentProductInfoBean) view.getTag();
                if (presentProductInfoBean != null && presentProductInfoBean.getItemType() == TYPE_2) {
                    getDirectCoupon(presentProductInfoBean.getCouponId());
                }
            }
        });

        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setCornerRadius(AutoSizeUtils.mm2px(mAppContext, 55));
        gradientDrawable.setColor(getResources().getColor(R.color.yellow_bg_ffae));
        tv_group_product.setBackground(gradientDrawable);
        if (isShowTint) {
            SharedPreferences showShareTint = getSharedPreferences("showShareTint", Context.MODE_PRIVATE);
            SharedPreferences.Editor edit = showShareTint.edit();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
            String format = simpleDateFormat.format(new Date());
            edit.putString("shareDate", format);
            edit.apply();
            tv_product_share_tint.setVisibility(VISIBLE);
            isShowTint = false;
        } else {
            tv_product_share_tint.setVisibility(GONE);
        }
        download_btn_communal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scroll_pro.fling(0);
                scroll_pro.scrollTo(0, 0);
                download_btn_communal.hide(false);
            }
        });
        badge = getBadge(getActivity(), fl_header_service).setBadgeGravity(Gravity.END | Gravity.TOP);
        insertNewTotalData();

        //初始化组团商品列表
        mRvGoodsGroup.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mGoodsGroupAdapter = new GoodsGroupAdapter(this, goodsGroupList);
        mRvGoodsGroup.setAdapter(mGoodsGroupAdapter);
        mGoodsGroupAdapter.setOnItemClickListener((adapter, view, position) -> {
            ShopRecommendHotTopicBean shopRecommendHotTopicBean = (ShopRecommendHotTopicBean) view.getTag();
            if (shopRecommendHotTopicBean != null) {
                Intent intent1 = new Intent(getActivity(), GroupCollocationActivity.class);
                intent1.putParcelableArrayListExtra("groupList", (ArrayList<? extends Parcelable>) goodsGroupList);
                intent1.putExtra("productId", productId);
                startActivity(intent1);
            }
        });

        //初始化推荐商品列表
        mRvGoodsRecommend.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mGoodsRecommendAdapter = new GoodsRecommendAdapter(this, goodsRecommendList);
        mRvGoodsRecommend.setAdapter(mGoodsRecommendAdapter);
        mGoodsRecommendAdapter.setOnItemClickListener((adapter, view, position) -> {
            ShopRecommendHotTopicBean shopRecommendHotTopicBean = (ShopRecommendHotTopicBean) view.getTag();
            if (shopRecommendHotTopicBean != null) {
                skipProDetail(shopRecommendHotTopicBean);
            }
        });

        mLlArtical.setOnClickListener(v -> {
            if (articalRecommendList != null && articalRecommendList.size() > 0) {
                ShopRecommendHotTopicBean hotTopicBean = articalRecommendList.get(0);
                if (hotTopicBean != null) {
                    Intent intent2 = new Intent(getActivity(), ArticleOfficialActivity.class);
                    intent2.putExtra("ArtId", String.valueOf(hotTopicBean.getId()));
                    startActivity(intent2);
                }
            }
        });
    }

    private void setChangeMeasureHeight() {
        measuredHeight = ll_pro_layout.getMeasuredHeight();
    }

    private void scrollLocation(int position) {
        setChangeMeasureHeight();
        if (position == 0) {
            scroll_pro.scrollTo(0, 0);
        } else if (position == 1) {
            if (ll_pro_layout.getMeasuredHeight() > 0) {
                scroll_pro.scrollTo(0, measuredHeight);
            }
        }
    }

    @Override
    protected void loadData() {
        if (smart_ql_sp_pro_details != null) {
            serviceDataList.clear();
            shopDetailBeanList.clear();
            serviceDataTotalList.clear();
            communalDetailAdapter.notifyDataSetChanged();
            getShopProDetails();
            getServiceData(productId);
            getCarCount(getActivity(), badge);
        }
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    @Override
    public View getLoadView() {
        return fl_product_details;
    }

    /**
     * 获取推荐商品
     *
     * @param id
     */
    private void getGoodsRecommend(final int id) {
        String url = Url.BASE_URL + Url.Q_SP_DETAIL_RECOMMEND;
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, url, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                ShopRecommendHotTopicEntity recommendHotTopicEntity = ShopRecommendHotTopicEntity.objectFromData(result);
                if (recommendHotTopicEntity != null) {
                    List<ShopRecommendHotTopicBean> hotTopicList = recommendHotTopicEntity.getShopRecommendHotTopicList();
                    if (recommendHotTopicEntity.getCode().equals(SUCCESS_CODE)) {
                        if (hotTopicList != null && hotTopicList.size() > 0) {
                            goodsRecommendList.clear();
                            goodsRecommendList.addAll(hotTopicList.subList(0, hotTopicList.size() > 20 ? 20 : hotTopicList.size()));
                            mGoodsRecommendAdapter.notifyDataSetChanged();
                        }

                        mllProductRecommend.setVisibility(goodsRecommendList.size() > 0 ? VISIBLE : GONE);
                    }
                }
            }
        });
    }

    /**
     * 获取推荐文章
     *
     * @param id
     */
    private void getArticalRecommend(final int id) {
        String url = Url.BASE_URL + Url.Q_SP_DETAIL_TOPIC_RECOMMEND;
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, url, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                ShopRecommendHotTopicEntity recommendHotTopicEntity = ShopRecommendHotTopicEntity.objectFromData(result);
                if (recommendHotTopicEntity != null) {
                    List<ShopRecommendHotTopicBean> articalList = recommendHotTopicEntity.getShopRecommendHotTopicList();
                    if (recommendHotTopicEntity.getCode().equals(SUCCESS_CODE)) {
                        if (recommendHotTopicEntity.getShopRecommendHotTopicList().size() > 0) {
                            articalRecommendList.clear();
                            articalRecommendList.addAll(articalList);
                            ShopRecommendHotTopicBean hotTopicBean = articalRecommendList.get(0);
                            if (hotTopicBean != null) {
                                mLlArtical.setVisibility(VISIBLE);
                                GlideImageLoaderUtil.loadImage(getActivity(), mIvArticalCover, hotTopicBean.getPicUrl());
                                mTvArticalTitle.setText(getStrings(hotTopicBean.getTitle()));
                                mTvArticalDesc.setText(getStrings(hotTopicBean.getDescription()));
                            }
                        }
                    }
                }
            }
        });
    }


    //获取组合商品
    private void getGroupGoods(int id) {
        String url = Url.BASE_URL + Url.Q_SP_DETAIL_RECOMMEND;
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, url, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                ShopRecommendHotTopicEntity recommendHotTopicEntity = ShopRecommendHotTopicEntity.objectFromData(result);
                if (recommendHotTopicEntity != null) {
                    List<ShopRecommendHotTopicBean> hotTopicList = recommendHotTopicEntity.getShopRecommendHotTopicList();
                    if (recommendHotTopicEntity.getCode().equals(SUCCESS_CODE)) {
                        if (hotTopicList != null && hotTopicList.size() > 0) {
                            goodsGroupList.clear();
                            goodsGroupList.addAll(hotTopicList.subList(0, hotTopicList.size() > 20 ? 20 : hotTopicList.size()));
                            mGoodsGroupAdapter.notifyDataSetChanged();
                        }

                        mllProductGroup.setVisibility(goodsGroupList.size() > 0 ? VISIBLE : GONE);
                    }
                }
            }
        });
    }

    private void setProductEvaLike(View view) {
        GoodsCommentBean goodsCommentBean = (GoodsCommentBean) view.getTag();
        TextView tv_eva_like = (TextView) view;
        String url = Url.BASE_URL + Url.SHOP_EVA_LIKE;
        Map<String, Object> params = new HashMap<>();
        params.put("id", goodsCommentBean.getId());
        params.put("uid", userId);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, url, params, null);
        goodsCommentBean.setFavor(!goodsCommentBean.isFavor());
        tv_eva_like.setSelected(!tv_eva_like.isSelected());
        tv_eva_like.setText(ConstantMethod.getNumCount(tv_eva_like.isSelected(), goodsCommentBean.isFavor(), goodsCommentBean.getLikeNum(), "赞"));
    }

    private void getShopProDetails() {
        String url = Url.BASE_URL + Url.Q_SHOP_DETAILS;
        Map<String, Object> params = new HashMap<>();
        params.put("id", productId);
        if (!TextUtils.isEmpty(recommendFlag)) {
            params.put("recommendFlag", recommendFlag);
        }
        if (userId > 0) {
            params.put("uid", userId);
        }
        NetLoadUtils.getNetInstance().loadNetDataPost(this, url, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                smart_ql_sp_pro_details.finishRefresh();
                Gson gson = new GsonBuilder().create();
                shopDetailsEntity = gson.fromJson(result, ShopDetailsEntity.class);
                if (shopDetailsEntity != null) {
                    if (shopDetailsEntity.getCode().equals(SUCCESS_CODE)) {
                        shopPropertyBean = shopDetailsEntity.getShopPropertyBean();
                        getShopProComment(shopPropertyBean);
                        getRecommendData();
                        setProductData(shopPropertyBean);
                    } else if (!shopDetailsEntity.getCode().equals(EMPTY_CODE)) {
                        showToast(getActivity(), shopDetailsEntity.getMsg());
                    }
                }
                NetLoadUtils.getNetInstance().showLoadSir(loadService, shopDetailsEntity);
            }

            @Override
            public void onNotNetOrException() {
                smart_ql_sp_pro_details.finishRefresh();
                NetLoadUtils.getNetInstance().showLoadSir(loadService, shopDetailsEntity);
            }

            @Override
            public void netClose() {
                showToast(getActivity(), R.string.unConnectedNetwork);
            }

            @Override
            public void onError(Throwable throwable) {
                showToast(getActivity(), R.string.connectedFaile);
            }
        });
    }

    private void getRecommendData() {
        getGoodsRecommend(shopPropertyBean.getId());
        getArticalRecommend(shopPropertyBean.getId());
        getGroupGoods(shopPropertyBean.getId());
    }


    //  获取商品评论
    private void getShopProComment(ShopPropertyBean shopPropertyBean) {
        String url = Url.BASE_URL + Url.Q_SHOP_DETAILS_COMMENT;
        Map<String, Object> params = new HashMap<>();
        params.put("showCount", 1);
        params.put("currentPage", 1);
        params.put("id", shopPropertyBean.getId());
        if (userId > 0) {
            params.put("uid", userId);
        }
        NetLoadUtils.getNetInstance().loadNetDataPost(this, url, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                goodsComments.clear();
                Gson gson = new Gson();
                GoodsCommentEntity goodsCommentEntity = gson.fromJson(result, GoodsCommentEntity.class);
                if (goodsCommentEntity != null) {
                    if (goodsCommentEntity.getCode().equals(SUCCESS_CODE)) {
                        goodsComments.addAll(goodsCommentEntity.getGoodsComments());
                    } else if (!goodsCommentEntity.getCode().equals(EMPTY_CODE)) {
                        showToast(getActivity(), goodsCommentEntity.getMsg());
                    }
                    setCommentCount(goodsCommentEntity);
                    directEvaluationAdapter.setNewData(goodsComments);
                }
            }
        });
    }

    private void setCommentCount(GoodsCommentEntity goodsCommentEntity) {
        if (goodsCommentEntity.getEvaluateCount() < 1) {
            shopCommentHeaderView.rel_pro_comment.setVisibility(GONE);
            rel_shop_details_comment.setVisibility(View.GONE);
        } else {
            shopCommentHeaderView.rel_pro_comment.setVisibility(VISIBLE);
            rel_shop_details_comment.setVisibility(VISIBLE);
            shopCommentHeaderView.tv_shop_comment_count.setText(("Ta们在说(" + goodsCommentEntity.getEvaluateCount() + ")"));
        }
    }

    private void setProductData(final ShopPropertyBean shopProperty) {
        if (shopProperty == null) return;
        String[] images = shopProperty.getImages().split(",");
//        视频video
        imagesVideoList.clear();
//        赠品优惠券
        presentProductInfoBeans.clear();
        CommunalADActivityBean communalADActivityBean;
        List<String> imageList = new ArrayList<>();
        int videoCount = 0;
        if (images.length != 0) {
            imageList.addAll(Arrays.asList(images));
            for (int i = 0; i < imageList.size(); i++) {
                communalADActivityBean = new CommunalADActivityBean();
                if (i == 0) {
                    communalADActivityBean.setPicUrl(getWaterMarkImgUrl(imageList.get(i), shopProperty.getWaterRemark()));
                    if (!TextUtils.isEmpty(shopProperty.getVideoUrl())) {
                        communalADActivityBean.setVideoUrl(shopProperty.getVideoUrl());
                        videoCount++;
                    }
                } else {
                    communalADActivityBean.setPicUrl(imageList.get(i));
                }
                imagesVideoList.add(communalADActivityBean);
            }
        } else {
            communalADActivityBean = new CommunalADActivityBean();
            communalADActivityBean.setPicUrl(getStrings(shopProperty.getImages()));
            if (!TextUtils.isEmpty(shopProperty.getVideoUrl())) {
                communalADActivityBean.setVideoUrl(shopProperty.getVideoUrl());
            }
            imageList.add(getStrings(shopProperty.getImages()));
            imagesVideoList.add(communalADActivityBean);
        }
//         轮播图
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
        banner_ql_sp_pro_details.setPages(getActivity(), cbViewHolderCreator, imagesVideoList).setCanScroll(true).setCanLoop(false)
                .setPageIndicator(new int[]{R.drawable.unselected_radius, R.drawable.selected_radius});
        int finalVideoCount = videoCount;
        banner_ql_sp_pro_details.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (imagesVideoList.size() > position) {
                    CommunalADActivityBean communalADActivityBean = imagesVideoList.get(position);
                    if (!TextUtils.isEmpty(communalADActivityBean.getVideoUrl())) {
                        return;
                    }
                    if (position - finalVideoCount < 0) {
                        return;
                    }
                    if (imageList.size() > 0 && position < imageList.size()) {
                        showImageActivity(getActivity(), IMAGE_DEF,
                                position - finalVideoCount,
                                imageList);
                    }
                }
            }
        });
        if (!TextUtils.isEmpty(shopProperty.getGpDiscounts())) {
            tv_group_product.setVisibility(VISIBLE);
            tv_group_product.setText(getString(R.string.group_discount, getStrings(shopProperty.getGpDiscounts())));
            Link numberLink = new Link(Pattern.compile(REGEX_NUM));
            numberLink.setTextColor(Color.parseColor("#ffffff"));
            numberLink.setUnderlined(false);
            numberLink.setTextSize(AutoSizeUtils.mm2px(mAppContext, 26));
            numberLink.setHighlightAlpha(0f);
            LinkBuilder.on(tv_group_product)
                    .addLink(numberLink)
                    .build();
        } else {
            tv_group_product.setVisibility(GONE);
        }
        //选择规格
        tv_ql_sp_pro_sku.setText(getString(R.string.sel_pro_sku
                , getStrings(shopProperty.getProps().get(0).getPropName())));
        if (isEndOrStartTime(shopPropertyBean.getStartTime(), shopDetailsEntity.getCurrentTime())
                && TextUtils.isEmpty(shopPropertyBean.getActivityTag())) {
            ll_sp_pro_sku_value.setVisibility(VISIBLE);
            rel_shop_pro_time.setVisibility(VISIBLE);
            //    待售
            tv_sp_details_buy_it.setText(R.string.buy_go_wait);
            tv_sp_details_buy_it.setEnabled(false);
            tv_sp_details_add_car.setEnabled(true);
            isWaitSellStatus = true;
            setCountTime();
            if (!isEndOrStartTime(shopDetailsEntity.getCurrentTime()
                    , shopPropertyBean.getStartTime())) {
                getConstant();
                constantMethod.createSchedule();
                constantMethod.setRefreshTimeListener(() -> {
                    shopPropertyBean.setAddSecond(shopPropertyBean.getAddSecond() + 1);
                    setCountTime();
                });
            } else {
                if (constantMethod != null) {
                    constantMethod.stopSchedule();
                }
            }
        } else {  //        判断库存
            rel_shop_pro_time.setVisibility(View.GONE);
            if (isEndOrStartTime(shopPropertyBean.getStartTime(), shopDetailsEntity.getCurrentTime())) {
                //    待售
                tv_sp_details_buy_it.setText(R.string.buy_go_wait);
                tv_sp_details_buy_it.setEnabled(false);
                tv_sp_details_add_car.setEnabled(true);
                isWaitSellStatus = true;
            } else {
                isWaitSellStatus = false;
                if (shopProperty.getQuantity() > 0) {
                    tv_sp_details_buy_it.setText(R.string.buy_go_it);
                    tv_sp_details_add_car.setEnabled(true);
                    //    立即购买
                    tv_sp_details_buy_it.setEnabled(true);
                    ll_sp_pro_sku_value.setVisibility(VISIBLE);
                } else {
                    setReplenishmentNotice();
                    //    已抢光
                    tv_sp_details_buy_it.setText(R.string.buy_go_null);
                    tv_sp_details_buy_it.setEnabled(false);
                    ll_sp_pro_sku_value.setVisibility(View.GONE);
                }
            }
        }

        //设置活动标签信息以及状态
        String activityCode = shopProperty.getActivityCode();
        if (!TextUtils.isEmpty(activityCode) && !TextUtils.isEmpty(shopProperty.getActivityTag()) && !isEndOrStartTime(shopDetailsEntity.getCurrentTime()
                , shopPropertyBean.getActivityEndTime())) {
            //活动标签不为空并且活动未结束才显示活动信息以及倒计时
            ll_product_activity_detail.setVisibility(VISIBLE);

            if (activityCode.contains("XSG")) {
                mRlActivityPrice.setVisibility(VISIBLE);       //限时购价格
                rl_product_activity_detail.setVisibility(GONE);
                mLlScroolDetailPrice.setVisibility(GONE); //非限时购价格
                startCountDownTime(tv_count_time_before_white, cv_countdownTime_white_hours);//开启倒计时
                mLlTimeHoursBottom.setVisibility(GONE); //下面的倒计时
                ll_communal_time_hours.setVisibility(VISIBLE);//上面的倒计时
            } else if (activityCode.contains("LJ") || activityCode.contains("ZK") || activityCode.contains("BJ")) {
                mRlActivityPrice.setVisibility(GONE);
                mIvNextIcon.setVisibility(GONE);
                tv_product_activity_description.setVisibility(VISIBLE);
                mLlScroolDetailPrice.setVisibility(VISIBLE);
                startCountDownTime(tv_count_time_before_white, cv_countdownTime_white_hours);
                mLlTimeHoursBottom.setVisibility(GONE);
                ll_communal_time_hours.setVisibility(VISIBLE);
            } else if (activityCode.contains("MJ") || activityCode.contains("MM") || activityCode.contains("MZ")) {
                ll_communal_time_hours.setVisibility(GONE);
                mRlActivityPrice.setVisibility(GONE);
                mIvNextIcon.setVisibility(VISIBLE);
                tv_product_activity_description.setVisibility(VISIBLE);
                mLlScroolDetailPrice.setVisibility(VISIBLE);
                startCountDownTime(mTvTipsBottom, mCtCountDownBottom);
                mLlTimeHoursBottom.setVisibility(VISIBLE);
            }

            tv_product_activity_description.setText(getStrings(shopProperty.getActivityRule()));
        } else {
            ll_product_activity_detail.setVisibility(GONE);
            mLlScroolDetailPrice.setVisibility(VISIBLE);
            if (constantMethod != null) {
                constantMethod.stopSchedule();
            }
        }

        if (!TextUtils.isEmpty(shopPropertyBean.getActivityPrice())
                && shopDetailsEntity.getShopPropertyBean() != null
                && shopDetailsEntity.getShopPropertyBean().getActivityStartTime() != null
                && !isTimeStart(shopDetailsEntity)) {
            String priceCHN = String.format(getResources().getString(R.string.money_price_chn)
                    , getStrings(shopPropertyBean.getActivityPrice()));
            String priceDes = String.format(getResources().getString(R.string.text_act_not_start)
                    , getStrings(shopPropertyBean.getActivityPrice()));
            tv_ql_sp_pro_sc_market_price.setText(priceDes);
            Link link = new Link(priceCHN);
            link.setTextColor(Color.parseColor("#ff5a6b"));
            link.setUnderlined(false);
            link.setHighlightAlpha(0f);
            LinkBuilder.on(tv_ql_sp_pro_sc_market_price)
                    .setText(priceDes)
                    .addLink(link)
                    .build();
        } else {
            tv_ql_sp_pro_sc_market_price.setText(String.format(getString(R.string.money_market_price_chn), shopProperty.getMarketPrice()));
        }
        if (!TextUtils.isEmpty(shopProperty.getPresentIds())) {
            setPresentProData(shopProperty.getPresentIds());
        } else if (shopProperty.getSkuSale() != null && shopProperty.getSkuSale().size() > 0) {
            setPresentProData(getStrings(shopProperty.getSkuSale().get(0).getPresentSkuIds()));
        }

        //优惠券
        if (shopProperty.getCouponJsonList() != null) {
            PresentProductInfoBean presentProductInfoBean;
            for (int i = 0; i < shopProperty.getCouponJsonList().size(); i++) {
                CouponJsonBean couponJsonBean = shopProperty.getCouponJsonList().get(i);
                presentProductInfoBean = new PresentProductInfoBean();
                presentProductInfoBean.setItemType(TYPE_2);
                if (i == 0) {
                    presentProductInfoBean.setFirstTag(true);
                    if (presentProductInfoBeans.size() > 0) {
                        presentProductInfoBean.setSelect(true);
                    }
                }
                presentProductInfoBean.setName(getStrings(couponJsonBean.getCoupon_name()));
                presentProductInfoBean.setCouponId(couponJsonBean.getId());
                presentProductInfoBeans.add(presentProductInfoBean);
            }
            presentProAdapter.notifyDataSetChanged();
            if (presentProductInfoBeans.size() > 0) {
                rv_shop_details_text_communal.setVisibility(VISIBLE);
            } else {
                rv_shop_details_text_communal.setVisibility(View.GONE);
            }
        }

        //营销以及活动标签
        if (!TextUtils.isEmpty(shopProperty.getActivityTag()) ||
                (shopPropertyBean.getMarketLabelList() != null && shopProperty.getMarketLabelList().size() > 0)) {
            fbl_details_market_label.setVisibility(VISIBLE);
            fbl_details_market_label.removeAllViews();
            if (!TextUtils.isEmpty(shopProperty.getActivityTag())) {
                MarketLabelBean marketLabelBean = new MarketLabelBean();
                marketLabelBean.setTitle(shopProperty.getActivityTag());
                marketLabelBean.setActivityCode(getStrings(shopPropertyBean.getActivityCode()));
                marketLabelBean.setLabelCode(1);
                fbl_details_market_label.addView(getLabelInstance().createLabelClickText(getActivity(), marketLabelBean));
            }
            if (shopPropertyBean.getMarketLabelList() != null && shopProperty.getMarketLabelList().size() > 0) {
                for (MarketLabelBean marketLabelBean : shopPropertyBean.getMarketLabelList()) {
                    marketLabelBean.setLabelCode(0);
                    fbl_details_market_label.addView(getLabelInstance().createLabelClickText(getActivity(), marketLabelBean));
                }
            }
        } else {
            fbl_details_market_label.setVisibility(GONE);
        }

        //设置价格以及SKU
        setSkuProp(shopProperty);

        //设置商品标题
        String productName = TextUtils.isEmpty(shopProperty.getSubtitle()) ? shopProperty.getName() : (shopProperty.getSubtitle() + "•" + shopProperty.getName());
        tv_ql_sp_pro_sc_name.setText(getStrings(productName));

        // 商品卖点
        List<String> strList = new ArrayList<>();
        strList.add("卖点1111111111111");
        strList.add("卖点222222222222");
        strList.add("卖点3333333333333");
        strList.add("卖点44444444444444");
        shopProperty.setProductPointInfo(strList);
        List<String> productPointInfo = shopProperty.getProductPointInfo();
        if (productPointInfo != null && productPointInfo.size() > 0) {
            mFlexProductPoint.setVisibility(VISIBLE);
            mFlexProductPoint.removeAllViews();
            for (int i = 0; i < productPointInfo.size(); i++) {
                String tagName = productPointInfo.get(i);
                if (!TextUtils.isEmpty(tagName)) {
                    TextView textView = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.layout_buy_before_tag, null, false);
                    textView.setText(getStrings(tagName));
                    mFlexProductPoint.addView(textView);
                }
            }
        } else {
            mFlexProductPoint.setVisibility(GONE);
        }

        //商品服务标签
        List<TagsBean> tags = shopProperty.getTags();
        tags.add(new TagsBean(21, "tag1111111111111111111111111111"));
        tags.add(new TagsBean(22, "tag22222222222222222222222222222222222222"));
        tags.add(new TagsBean(23, "tag333333333333333333333333333"));
        tags.add(new TagsBean(24, "tag4444444444444444"));
        String tagIds = shopProperty.getTagIds();
        tagIds = tagIds + ",21,22,23,24";
        shopProperty.setTagIds(tagIds);
        if (tags != null && tags.size() > 0 && !TextUtils.isEmpty(tagIds) && tagIds.split(",").length > 0) {
            ll_layout_pro_sc_tag.setVisibility(VISIBLE);
            final Map<Integer, String> tagMap = new HashMap<>();
            for (TagsBean tagsBean : shopProperty.getTags()) {
                tagMap.put(tagsBean.getId(), getStrings(tagsBean.getName()));
            }
            final String[] tagSelected = shopProperty.getTagIds().split(",");
            flex_product_tag.removeAllViews();
            for (int j = 0; j < tagSelected.length; j++) {
                try {
                    String tagName = tagMap.get(Integer.parseInt(tagSelected[j]));
                    if (!TextUtils.isEmpty(tagName)) {
                        TextView textView = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.layout_product_tag, null, false);
                        textView.setLines(1);
                        textView.setText(getStrings(tagName));
                        flex_product_tag.addView(textView);
                        int width = flex_product_tag.getMeasuredWidth();
                        Log.d("", width + "");
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }

            ViewTreeObserver observer = flex_product_tag.getViewTreeObserver();
            observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    int width = flex_product_tag.getMeasuredWidth();
                    int screenWidth = ((TinkerBaseApplicationLike) TinkerManager.getTinkerApplicationLike()).getScreenWidth();
                    int max = screenWidth - AutoSizeUtils.mm2px(mAppContext, 60) - mIvMoreTag.getWidth();
                    if (width >= max) {
                        flex_product_tag.removeViewAt(flex_product_tag.getChildCount() - 1);
                    } else {
                        flex_product_tag.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                }
            });
        } else {
            ll_layout_pro_sc_tag.setVisibility(GONE);
        }

        //购前须知标签
        List<String> strList2 = new ArrayList<>();
        strList2.add("1111111111111");
        strList2.add("222222222222");
        strList2.add("3333333333333");
        strList2.add("44444444444444");
        shopProperty.setPreSaleInfo(strList2);
        List<String> preSaleInfo = shopProperty.getPreSaleInfo();
        if (preSaleInfo != null && preSaleInfo.size() > 0) {
            ll_pro_buy_before.setVisibility(VISIBLE);
            flex_buy_before.removeAllViews();
            for (int i = 0; i < preSaleInfo.size(); i++) {
                String tagName = preSaleInfo.get(i);
                if (!TextUtils.isEmpty(tagName)) {
                    TextView textView = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.layout_buy_before_tag, null, false);
                    textView.setText(getStrings(tagName));
                    flex_buy_before.addView(textView);
                }
            }
        } else {
            ll_pro_buy_before.setVisibility(GONE);
        }


        //            商品浏览
        Properties prop = new Properties();
        prop.setProperty("proName", getStrings(shopProperty.getName()));
        prop.setProperty("proId", shopDetailsEntity.getShopPropertyBean().getId() + "");
        StatService.trackCustomKVEvent(getActivity(), "qlProLook", prop);

//        iv_ql_shop_pro_cp_tag.setVisibility(shopProperty.getAllowCoupon() == 0 ? View.GONE : VISIBLE);
        tv_sp_details_collect.setSelected(shopProperty.isCollect());

        List<CommunalDetailObjectBean> detailsDataList = CommunalWebDetailUtils.getCommunalWebInstance().getWebDetailsFormatDataList(shopProperty.getItemBody());
        CommunalDetailObjectBean communalDetailObjectBean = new CommunalDetailObjectBean();
        communalDetailObjectBean.setItemType(TYPE_PRODUCT_TITLE);
        communalDetailObjectBean.setContent("商品详情");
        shopDetailBeanList.add(communalDetailObjectBean);
        shopDetailBeanList.addAll(detailsDataList);
        if (serviceDataList.size() > 0) {
            shopDetailBeanList.addAll(serviceDataList);
        }
        communalDetailAdapter.notifyDataSetChanged();
        ctb_qt_pro_details.setCurrentTab(0);
        scroll_pro.scrollTo(0, 0);
    }

    private void startCountDownTime(TextView tipView, CountdownView countDownTimeView) {
        setActCountTime(tipView, countDownTimeView);
        getConstant();
        constantMethod.createSchedule();
        constantMethod.setRefreshTimeListener(() -> {
            shopPropertyBean.setAddSecond(shopPropertyBean.getAddSecond() + 1);
            setActCountTime(tipView, countDownTimeView);
        });
    }

    private void setReplenishmentNotice() {
        ShopPropertyBean.SkuSaleBean skuSaleBean = shopPropertyBean.getSkuSale().get(0);
        if (shopPropertyBean.getSkuSale().size() > 0 && shopPropertyBean.getSkuSale().size() < 2
                && (skuSaleBean.getIsNotice() == 1 || skuSaleBean.getIsNotice() == 2)) {
            tv_sp_details_add_car.setEnabled(true);
            tv_sp_details_add_car.setSelected(true);
            skuSaleBeanId = skuSaleBean.getId();
            tv_sp_details_add_car.setText(skuSaleBean.getIsNotice() == 1 ? "到货提醒" : "取消提醒");
        } else {
            tv_sp_details_add_car.setEnabled(false);
        }
    }

    /**
     * 服务承诺
     *
     * @param productId
     */
    private void getServiceData(String productId) {
        String url = Url.BASE_URL + Url.Q_SP_DETAIL_SERVICE_COMMITMENT;
        Map<String, Object> params = new HashMap<>();
        params.put("productId", productId);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, url, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                DirectGoodsServerEntity directGoodsServerEntity = gson.fromJson(result, DirectGoodsServerEntity.class);
                if (directGoodsServerEntity != null) {
                    if (directGoodsServerEntity.getCode().equals(SUCCESS_CODE)) {
                        serviceDataList.clear();
                        serviceDataTotalList.clear();
                        DirectGoodsServerBean directGoodsServerBean = directGoodsServerEntity.getDirectGoodsServerBean();
                        if (directGoodsServerBean != null) {
                            List<DirectGoodsServerBean.ServicePromiseBean> servicePromiseList = directGoodsServerBean.getServicePromiseList();
                            if (servicePromiseList.size() > 0) {
                                CommunalDetailObjectBean communalDetailObjectBean = new CommunalDetailObjectBean();
                                communalDetailObjectBean.setItemType(TYPE_PRODUCT_TITLE);
                                communalDetailObjectBean.setContent("服务承诺");
                                serviceDataList.add(communalDetailObjectBean);
                                serviceDataTotalList.add(communalDetailObjectBean);
                                int serviceSize = servicePromiseList.size();
                                for (int i = 0; i < serviceSize; i++) {
                                    communalDetailObjectBean = new CommunalDetailObjectBean();
                                    if (i == 0) {
                                        communalDetailObjectBean.setFirstLinePadding(true);
                                    }
                                    DirectGoodsServerBean.ServicePromiseBean servicePromiseBean = servicePromiseList.get(i);
                                    communalDetailObjectBean.setContent(servicePromiseBean.getContent());
                                    if (i <= 10) {
                                        serviceDataList.add(communalDetailObjectBean);
                                    }
                                    serviceDataTotalList.add(communalDetailObjectBean);
                                }
                                if (serviceSize > 10) {
                                    communalDetailObjectBean = new CommunalDetailObjectBean();
                                    communalDetailObjectBean.setItemType(TYPE_PRODUCT_MORE);
                                    communalDetailObjectBean.setMoreDataList(serviceDataTotalList);
                                    serviceDataList.add(communalDetailObjectBean);
                                }
                            }
                        }
                        if (shopDetailBeanList.size() > 0) {
                            shopDetailBeanList.addAll(serviceDataList);
                            communalDetailAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }
        });
    }

    private void setActCountTime(TextView tipView, CountdownView countDownTimeView) {
        if (isTimeStart(shopDetailsEntity)) {
            tipView.setText(tipView.getId() == R.id.tv_count_time_before_white ? "距结束还有" : "距结束");
            try {
                //格式化结束时间
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
                Date dateEnd = formatter.parse(shopPropertyBean.getActivityEndTime());
                Date dateCurrent;
                if (!TextUtils.isEmpty(shopDetailsEntity.getCurrentTime())) {
                    dateCurrent = formatter.parse(shopDetailsEntity.getCurrentTime());
                } else {
                    dateCurrent = new Date();
                }
                if ((dateEnd.getTime() - dateCurrent.getTime() - shopPropertyBean.getAddSecond() * 1000) > 0) {
                    countDownTimeView.updateShow((dateEnd.getTime() - dateCurrent.getTime() - shopPropertyBean.getAddSecond() * 1000));
                } else {
                    //活动已结束
                    countDownTimeView.updateShow(0);
                    if (constantMethod != null) {
                        constantMethod.stopSchedule();
                    }
                    loadData();
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            tipView.setText(tipView.getId() == R.id.tv_count_time_before_white ? "距开始还有" : "距开始");
            try {
                //格式化结束时间
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
                Date dateStart = formatter.parse(shopPropertyBean.getActivityStartTime());
                Date dateCurrent;
                if (!TextUtils.isEmpty(shopDetailsEntity.getCurrentTime())) {
                    dateCurrent = formatter.parse(shopDetailsEntity.getCurrentTime());
                } else {
                    dateCurrent = new Date();
                }
                if (((dateStart.getTime() - dateCurrent.getTime() - shopPropertyBean.getAddSecond() * 1000)) > 0) {
                    countDownTimeView.updateShow((dateStart.getTime() - dateCurrent.getTime() - shopPropertyBean.getAddSecond() * 1000));
                } else {
                    //活动已开始
                    countDownTimeView.updateShow(0);
                    if (constantMethod != null) {
                        constantMethod.stopSchedule();
                    }
                    loadData();
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    //判断活动是否开始
    public boolean isTimeStart(ShopDetailsEntity shopDetailsEntity) {
        try {
            //格式化开始时间
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
            Date dateStart = formatter.parse(shopDetailsEntity.getShopPropertyBean().getActivityStartTime());
            Date dateCurrent;
            if (!TextUtils.isEmpty(shopDetailsEntity.getCurrentTime())) {
                dateCurrent = formatter.parse(shopDetailsEntity.getCurrentTime());
            } else {
                dateCurrent = new Date();
            }
            if (dateCurrent.getTime() >= dateStart.getTime()) {
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void setCountTime() {
        if (shopPropertyBean != null) {
            try {
                //格式化开始时间
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
                Date dateStart = formatter.parse(shopPropertyBean.getStartTime());
                Date dateCurrent;
                if (!TextUtils.isEmpty(shopDetailsEntity.getCurrentTime())) {
                    dateCurrent = formatter.parse(shopDetailsEntity.getCurrentTime());
                } else {
                    dateCurrent = new Date();
                }
                DynamicConfig.Builder dynamic = new DynamicConfig.Builder();
                dynamic.setTimeTextColor(getResources().getColor(R.color.text_normal_red));
                dynamic.setSuffixTextColor(getResources().getColor(R.color.text_black_t));
                ct_pro_show_time_detail.dynamicShow(dynamic.build());
                ct_pro_show_time_detail.updateShow((dateStart.getTime() - dateCurrent.getTime() - shopPropertyBean.getAddSecond() * 1000));
                tv_pro_time_detail_status.setText("距开售");
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (!isEndOrStartTimeAddSeconds(shopDetailsEntity.getCurrentTime()
                    , shopPropertyBean.getStartTime()
                    , shopPropertyBean.getAddSecond())) {
                ct_pro_show_time_detail.setOnCountdownEndListener(new CountdownView.OnCountdownEndListener() {
                    @Override
                    public void onEnd(CountdownView cv) {
                        cv.setOnCountdownEndListener(null);
                        loadData();
                    }
                });
            } else {
                ct_pro_show_time_detail.setOnCountdownEndListener(null);
            }
        }
    }

    private void setSkuProp(ShopPropertyBean shopProperty) {
        if (shopProperty.getSkuSale() != null && shopProperty.getSkuSale().size() > 0) {
            List<ShopPropertyBean.SkuSaleBean> skuSaleList = shopProperty.getSkuSale();
            //有多个SKU
            if (shopProperty.getSkuSale().size() > 1) {
                //        获取价格排序范围
                Collections.sort(skuSaleList, (lhs, rhs) -> {
                    if (!TextUtils.isEmpty(lhs.getPrice()) && !TextUtils.isEmpty(rhs.getPrice())) {
                        float p1 = getFloatNumber(lhs.getPrice());
                        float p2 = getFloatNumber(rhs.getPrice());
                        return Float.compare(p1, p2);
                    } else {
                        return 0;
                    }
                });

                ll_sp_pro_sku_value.setVisibility(VISIBLE);
                editGoodsSkuBean = new EditGoodsSkuBean();
                editGoodsSkuBean.setQuantity(shopProperty.getQuantity());
                editGoodsSkuBean.setId(shopProperty.getId());
                editGoodsSkuBean.setDelivery(shopProperty.getDelivery());
                editGoodsSkuBean.setPicUrl(shopProperty.getPicUrl());
                editGoodsSkuBean.setProps(shopProperty.getProps());
                editGoodsSkuBean.setPropvalues(shopProperty.getPropvalues());
                editGoodsSkuBean.setProductName(shopProperty.getName());
                editGoodsSkuBean.setSkuSale(shopProperty.getSkuSale());
                editGoodsSkuBean.setActivityCode(getStrings(shopProperty.getActivityCode()));
                editGoodsSkuBean.setPresentIds(getStrings(shopProperty.getPresentIds()));
                if (shopProperty.getPresentProductInfoList() != null
                        && shopProperty.getPresentProductInfoList().size() > 0) {
                    editGoodsSkuBean.setPresentProductInfoList(shopProperty.getPresentProductInfoList());
                }
                if (shopProperty.getCombineProductInfoList() != null
                        && shopProperty.getCombineProductInfoList().size() > 0) {
                    editGoodsSkuBean.setCombineProductInfoList(shopProperty.getCombineProductInfoList());
                }
                if ("待售".equals(getStrings(shopProperty.getSellStatus()))) {
                    editGoodsSkuBean.setSellStatus(true);
                } else {
                    editGoodsSkuBean.setSellStatus(false);
                }
                editGoodsSkuBean.setMaxDiscounts(getStrings(shopProperty.getMaxDiscounts()));
                shopCarGoodsSkuDif = null;
                skuDialog = new SkuDialog(getActivity());
                skuDialog.refreshView(editGoodsSkuBean);
            } else {
                //仅有一个SKU
                ll_sp_pro_sku_value.setVisibility(View.GONE);
                setReallyPrice((!TextUtils.isEmpty(shopProperty.getSkuSale().get(0).getPrice())
                        ? shopProperty.getSkuSale().get(0).getPrice() : shopProperty.getPrice()));
                shopCarGoodsSkuDif = new ShopCarGoodsSku();
                shopCarGoodsSkuDif.setCount(1);
                shopCarGoodsSkuDif.setSaleSkuId(shopProperty.getSkuSale().get(0).getId());
                shopCarGoodsSkuDif.setPrice(Double.parseDouble(shopProperty.getSkuSale().get(0).getPrice()));
                shopCarGoodsSkuDif.setProductId(shopProperty.getId());
                shopCarGoodsSkuDif.setPicUrl(shopProperty.getPicUrl());
                shopCarGoodsSkuDif.setActivityCode(getStrings(shopProperty.getActivityCode()));
                shopCarGoodsSkuDif.setValuesName(
                        !TextUtils.isEmpty(shopProperty.getPropvalues().get(0).getPropValueName())
                                ? shopProperty.getPropvalues().get(0).getPropValueName() : "默认");
                shopCarGoodsSkuDif.setPresentIds(getStrings(shopProperty.getSkuSale().get(0).getPresentSkuIds()));
            }


            String minPrice = skuSaleList.get(0).getPrice();
            String maxPrice = skuSaleList.get(skuSaleList.size() - 1).getPrice();
            mTvMaxPrice.setVisibility(!minPrice.equals(maxPrice) ? VISIBLE : GONE);
            //设置最低价
            setReallyPrice(minPrice);
            //设置市场价
            mTvProductMartketPrice.setText("￥" + getStrings(shopProperty.getMarketPrice()));
            mTvProductMartketPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            mTvProductMartketPrice.getPaint().setAntiAlias(true);
            //设置最高价
            mTvMaxPrice.setText(getRmbFormat(this, "~" + "￥" + maxPrice, false));
        } else {
            showToast(this, "商品数据错误");
        }
    }

    /**
     * 设置赠品信息
     *
     * @param presentProData
     */
    private void setPresentProData(String presentProData) {
        if (!TextUtils.isEmpty(presentProData) && shopPropertyBean != null) {
            rv_shop_details_text_communal.setVisibility(VISIBLE);
            String[] preIds = presentProData.split(",");
            if (preIds.length > 0) {
                preIds = getDistinctString(preIds);
                presentProductInfoBeans.clear();
                for (int i = 0; i < preIds.length; i++) {
                    String preId = preIds[i];
                    for (PresentProductInfoBean presentProductInfoBean : shopPropertyBean.getPresentProductInfoList()) {
                        if (preId.equals(presentProductInfoBean.getPresentSkuId())) {
                            if (presentProductInfoBean.getPresentQuantity() > 0) {
                                if (i == 0) {
                                    presentProductInfoBean.setFirstTag(true);
                                }
                                presentProductInfoBeans.add(presentProductInfoBean);
                            }
                        }
                    }
                }
            }
            presentProAdapter.notifyDataSetChanged();
        } else {
            rv_shop_details_text_communal.setVisibility(View.GONE);
        }
    }

    private void setReallyPrice(String price) {
        if (!TextUtils.isEmpty(shopPropertyBean.getActivityCode()) && shopPropertyBean.getActivityCode().contains("XSG") && isTimeStart(shopDetailsEntity)) {
            mTvActivityPrice.setText("活动价");
        } else {
            mTvActivityPrice.setText("");
        }
        tv_ql_sp_pro_sc_price.setText(getRmbFormat(this, price));
        String minPrice = getStringsFormat(this, R.string.min_price, stripTrailingZeros(price.trim()));
        mTvProductMinPrice.setText(getSpannableString(minPrice, 1, minPrice.length() - 1, 1.6f));
    }

    //    七鱼客服
    private void skipServiceDataInfo() {
        QyProductIndentInfo qyProductIndentInfo = null;
        if (shopPropertyBean != null) {
            qyProductIndentInfo = new QyProductIndentInfo();
            qyProductIndentInfo.setUrl(sharePageUrl + productId);
            qyProductIndentInfo.setTitle(getStrings(shopPropertyBean.getSubtitle()));
            qyProductIndentInfo.setPicUrl(shopPropertyBean.getPicUrl());
            qyProductIndentInfo.setDesc(getStrings(shopPropertyBean.getName()));
            qyProductIndentInfo.setNote("￥" + shopPropertyBean.getPrice());
        }
        QyServiceUtils.getQyInstance().openQyServiceChat(this, "自营商品详情", sharePageUrl + productId, qyProductIndentInfo);
    }

    private void addGoodsToCar() {
        if (shopCarGoodsSkuDif != null) {
//          加入购物车
            tv_sp_details_add_car.setEnabled(false);
            //添加商品购物车
            String url = Url.BASE_URL + Url.Q_SHOP_DETAILS_ADD_CAR;
            Map<String, Object> params = new HashMap<>();
            params.put("userId", userId);
            params.put("productId", shopDetailsEntity.getShopPropertyBean().getId());
            params.put("saleSkuId", shopCarGoodsSkuDif.getSaleSkuId());
            params.put("count", shopCarGoodsSkuDif.getCount());
            params.put("price", shopCarGoodsSkuDif.getPrice());
            if (!TextUtils.isEmpty(shopCarGoodsSkuDif.getActivityCode())) {
                params.put("activityCode", shopCarGoodsSkuDif.getActivityCode());
            }
            //添加埋点来源参数
            ConstantMethod.addSourceParameter(params);
            NetLoadUtils.getNetInstance().loadNetDataPost(this, url, params, new NetLoadListenerHelper() {
                @Override
                public void onSuccess(String result) {
                    Properties prop = new Properties();
                    prop.setProperty("proName", getStrings(shopPropertyBean.getName()));
                    prop.setProperty("proId", shopDetailsEntity.getShopPropertyBean().getId() + "");
                    prop.setProperty("proCount", shopCarGoodsSkuDif.getCount() + "");
                    prop.setProperty("proSalSku", getStrings(shopCarGoodsSkuDif.getValuesName()));
                    StatService.trackCustomKVEvent(getActivity(), "addProToCar", prop);
                    Gson gson = new Gson();
                    RequestStatus status = gson.fromJson(result, RequestStatus.class);
                    if (status != null) {
                        if (status.getCode().equals(SUCCESS_CODE)) {
                            TotalPersonalTrajectory totalPersonalTrajectory = new TotalPersonalTrajectory(getActivity());
                            Map<String, String> totalMap = new HashMap<>();
                            totalMap.put("productId", String.valueOf(shopPropertyBean.getId()));
                            totalMap.put(TOTAL_NAME_TYPE, "addCartSuccess");
                            totalPersonalTrajectory.saveTotalDataToFile(totalMap);
                            showToast(getActivity(), "添加商品成功");
                            //通知刷新购物车数量
                            EventBus.getDefault().post(new EventMessage(ConstantVariable.UPDATE_CAR_NUM, ""));
                            if (skuDialog != null) {
                                shopCarGoodsSkuDif = null;
                            }
                        } else {
                            showToastRequestMsg(getActivity(), status);
                        }
                    }
                    tv_sp_details_add_car.setEnabled(true);
                }

                @Override
                public void onNotNetOrException() {
                    tv_sp_details_add_car.setEnabled(true);
                }
            });
        } else {
            if (skuDialog != null) {
                skuDialog.show(true, "确定");
                skuDialog.getGoodsSKu(new SkuDialog.DataListener() {
                    @Override
                    public void getSkuProperty(ShopCarGoodsSku shopCarGoodsSku) {
                        shopCarGoodsSkuDif = shopCarGoodsSku;
                        if (shopCarGoodsSkuDif != null) {
                            tv_ql_sp_pro_sku.setText(("已选：" + shopCarGoodsSkuDif.getValuesName()));
//                            setReallyPrice(String.valueOf(shopCarGoodsSkuDif.getPrice()));
                            addCarCheckLoginStatus();
                            setPresentProData(shopCarGoodsSkuDif.getPresentIds());
                        }
                    }
                });
            }
        }
    }

    private void buyGoIt() {
        if (shopCarGoodsSkuDif != null) {
            List<CartInfoBean> settlementGoods = new ArrayList<>();
            CartInfoBean cartInfoBean = new CartInfoBean();
            CartInfoBean.SaleSkuBean saleSkuBean = new CartInfoBean.SaleSkuBean();
            cartInfoBean.setPicUrl(shopCarGoodsSkuDif.getPicUrl());
            cartInfoBean.setProductId(shopPropertyBean.getId());
            cartInfoBean.setCount(shopCarGoodsSkuDif.getCount());
            cartInfoBean.setId(shopCarGoodsSkuDif.getSaleSkuId());
            cartInfoBean.setName(shopPropertyBean.getName());
            cartInfoBean.setPrice(shopCarGoodsSkuDif.getPrice() + "");
            cartInfoBean.setSelected(true);
            cartInfoBean.setSaleSkuValue(shopCarGoodsSkuDif.getValuesName());
            cartInfoBean.setAllowCoupon(shopPropertyBean.getAllowCoupon());
            saleSkuBean.setPrice(shopCarGoodsSkuDif.getPrice() + "");
            saleSkuBean.setId(shopCarGoodsSkuDif.getSaleSkuId());
            saleSkuBean.setQuantity(shopPropertyBean.getQuantity());
            cartInfoBean.setSaleSku(saleSkuBean);
            if (!TextUtils.isEmpty(shopPropertyBean.getActivityTag())) {
                ActivityInfoBean activityInfoBean = new ActivityInfoBean();
                activityInfoBean.setActivityCode(getStrings(shopPropertyBean.getActivityCode()));
                activityInfoBean.setActivityTag(getStrings(shopPropertyBean.getActivityTag()));
                activityInfoBean.setActivityRule(getStrings(shopPropertyBean.getActivityRule()));
                cartInfoBean.setActivityInfoData(activityInfoBean);
            }
            cartInfoBean.setActivityCode(getStrings(shopCarGoodsSkuDif.getActivityCode()));
            settlementGoods.add(cartInfoBean);
//            商品结算
            Properties prop = new Properties();
            prop.setProperty("proName", getStrings(shopPropertyBean.getName()));
            prop.setProperty("proId", shopDetailsEntity.getShopPropertyBean().getId() + "");
            prop.setProperty("proCount", shopCarGoodsSkuDif.getCount() + "");
            prop.setProperty("proSalSku", getStrings(shopCarGoodsSkuDif.getValuesName()));
            StatService.trackCustomKVEvent(getActivity(), "qlProBuy", prop);
//            结算商品 跳转订单填写
            Intent intent = new Intent(getActivity(), DirectIndentWriteActivity.class);
            intent.putExtra("uid", userId);
            intent.putParcelableArrayListExtra("productDate", (ArrayList<? extends Parcelable>) settlementGoods);
            startActivity(intent);
        } else {
            if (skuDialog != null) {
                skuDialog.show(true, "确定");
                skuDialog.getGoodsSKu(new SkuDialog.DataListener() {
                    @Override
                    public void getSkuProperty(ShopCarGoodsSku shopCarGoodsSku) {
                        shopCarGoodsSkuDif = shopCarGoodsSku;
                        if (shopCarGoodsSkuDif != null) {
                            tv_ql_sp_pro_sku.setText(("已选：" + shopCarGoodsSkuDif.getValuesName()));
                            setReallyPrice(String.valueOf(shopCarGoodsSkuDif.getPrice()));
                            setPresentProData(shopCarGoodsSkuDif.getPresentIds());
                            buyGoItCheckStatus();
                        }
                    }
                });
            }
        }
    }

    //收藏商品
    private void goCollectPro() {
        String url = Url.BASE_URL + Url.Q_SP_DETAIL_PRO_COLLECT;
        Map<String, Object> params = new HashMap<>();
        params.put("uid", userId);
        params.put("object_id", shopPropertyBean.getId());
        params.put("type", "goods");
        //添加埋点来源参数
        ConstantMethod.addSourceParameter(params);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, url, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                loadHud.dismiss();
                Gson gson = new Gson();
                RequestStatus requestStatus = gson.fromJson(result, RequestStatus.class);
                if (requestStatus != null) {
                    if (requestStatus.getCode().equals(SUCCESS_CODE)) {
                        tv_sp_details_collect.setSelected(requestStatus.isCollect());
                        showToast(getActivity(),
                                String.format(getResources().getString(
                                        tv_sp_details_collect.isSelected() ? R.string.collect_success : R.string.cancel_done), "商品", "收藏"));
                    }
                }
            }

            @Override
            public void onNotNetOrException() {
                loadHud.dismiss();
            }

            @Override
            public void onError(Throwable throwable) {
                showToast(getActivity(), String.format(getResources().getString(R.string.collect_failed), "商品"));
            }

            @Override
            public void netClose() {
                showToast(getActivity(), R.string.unConnectedNetwork);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IS_LOGIN_CODE) {
            loadData();
        }
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    class ShopCommentHeaderView {
        //        商品评论展示
        @BindView(R.id.rel_pro_comment)
        RelativeLayout rel_pro_comment;
        @BindView(R.id.scroll_details)
        NestedScrollView scroll_details;
        //        评价数目
        @BindView(R.id.tv_shop_comment_count)
        TextView tv_shop_comment_count;
        @BindView(R.id.communal_recycler_wrap)
        RecyclerView communal_recycler_wrap;

        @OnClick(R.id.tv_shop_comment_more)
        void getMoreComment(View view) {
            if (shopPropertyBean != null
                    && shopPropertyBean.getId() > 0) {
//                跳转更多评论
                Intent intent = new Intent(getActivity(), DirectProductEvaluationActivity.class);
                intent.putExtra("productId", String.valueOf(shopPropertyBean.getId()));
                startActivity(intent);
            }
        }

        public void initView() {
            communal_recycler_wrap.setVisibility(View.GONE);
        }

    }

    /**
     * 点击推荐商品
     */
    private void skipProDetail(ShopRecommendHotTopicBean shopRecommendHotTopicBean) {
        switch (shopRecommendHotTopicBean.getType_id()) {
            case 0:
                Intent intent = new Intent();
                intent.setClass(getActivity(), ShopTimeScrollDetailsActivity.class);
                intent.putExtra("productId", String.valueOf(shopRecommendHotTopicBean.getId()));
                startActivity(intent);
                break;
            case 1:
                NetLoadUtils.getNetInstance().showLoadSirLoading(loadService);
                productId = String.valueOf(shopRecommendHotTopicBean.getId());
                recommendType = RECOMMEND_PRODUCT;
                setTotalData();
                ctb_qt_pro_details.setCurrentTab(0);
                if (constantMethod != null) {
                    constantMethod.stopSchedule();
                    constantMethod.releaseHandlers();
                    constantMethod = null;
                }
                loadData();
                break;
            case 2:
                intent = new Intent();
                intent.setClass(getActivity(), IntegralScrollDetailsActivity.class);
                intent.putExtra("productId", String.valueOf(shopRecommendHotTopicBean.getId()));
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    private void setTotalData() {
        if (totalPersonalTrajectory != null) {
            Map<String, String> totalMap = new HashMap<>();
            totalMap.put("productId", productId);
            if (!TextUtils.isEmpty(recommendType)) {
                totalMap.put(TOTAL_NAME_TYPE, recommendType);
            }
            totalPersonalTrajectory.stopTotal(totalMap);
        }
    }

    /**
     * 推荐跳转更多推荐
     *
     * @param shopRecommendHotTopicBean
     */
    private void skipMoreRecommend(ShopRecommendHotTopicBean shopRecommendHotTopicBean) {
        Intent intent = new Intent(ShopScrollDetailsActivity.this, ProRecommendActivity.class);
        intent.putExtra("recommendType", shopRecommendHotTopicBean.getType());
        intent.putExtra("id", String.valueOf(shopRecommendHotTopicBean.getId()));
        startActivity(intent);
    }

    @OnClick({R.id.iv_life_back, R.id.ll_product_activity_detail, R.id.tv_sp_details_service,
            R.id.tv_sp_details_add_car, R.id.tv_sp_details_buy_it, R.id.tv_sp_details_collect, R.id.iv_img_service, R.id.iv_img_share,
            R.id.tv_group_product, R.id.iv_ql_shop_pro_cp_tag, R.id.tv_ql_sp_pro_sku})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.iv_life_back:
                finish();
                break;
            //打开活动规则详情
            case R.id.ll_product_activity_detail:
                if (shopPropertyBean != null) {
                    intent = new Intent(getActivity(), QualityProductActActivity.class);
                    intent.putExtra("activityCode", getStrings(shopPropertyBean.getActivityCode()));
                    startActivity(intent);
                }
                break;
            //客服
            case R.id.tv_sp_details_service:
                skipServiceDataInfo();
                break;
            //加入购物车
            case R.id.tv_sp_details_add_car:
                addCarCheckLoginStatus();
                break;
            //立即购买
            case R.id.tv_sp_details_buy_it:
                buyGoItCheckStatus();
                break;
            //收藏商品
            case R.id.tv_sp_details_collect:
                if (userId > 0) {
                    if (shopPropertyBean != null) {
                        loadHud.show();
                        goCollectPro();
                    }
                } else {
                    getLoginStatus(getActivity());
                }
                break;
            //跳转购物车
            case R.id.iv_img_service:
                intent = new Intent(getActivity(), ShopCarActivity.class);
                startActivity(intent);
                break;
            //分享
            case R.id.iv_img_share:
                if (shopPropertyBean != null) {
                    String title = shopPropertyBean.getName();
                    String activityCode = shopPropertyBean.getActivityCode();
                    if (!TextUtils.isEmpty(activityCode)) {
                        if (activityCode.contains("XSG")) {
                            //限时购活动
                            List<ShopPropertyBean.SkuSaleBean> skuSale = shopPropertyBean.getSkuSale();
                            if (skuSale != null && skuSale.size() > 0) {
                                String price = skuSale.get(0).getPrice();
                                title = getStringsFormat(getActivity(), R.string.limited_time_preference, price) + shopPropertyBean.getName();
                            }
                        } else if (activityCode.contains("LJ")) {
                            //立减活动
                            title = TextUtils.isEmpty(shopPropertyBean.getSubtitle()) ? "我在多么生活看中了" + shopPropertyBean.getName()
                                    : getStringsFormat(getActivity(), R.string.bracket, shopPropertyBean.getActivityRule()) + shopPropertyBean.getName();
                        }
                    } else {
                        //普通商品
                        title = TextUtils.isEmpty(shopPropertyBean.getSubtitle()) ? "我在多么生活看中了" + shopPropertyBean.getName()
                                : shopPropertyBean.getSubtitle() + "•" + shopPropertyBean.getName();
                    }
                    new UMShareAction(getActivity()
                            , shopPropertyBean.getPicUrl()
                            , title
                            , getStrings(shopPropertyBean.getSubtitle())
                            , sharePageUrl + shopPropertyBean.getId()
                            , "pages/goodsDetails/goodsDetails?id=" + shopPropertyBean.getId()
                            , shopPropertyBean.getId(), true);
                }
                tv_product_share_tint.setVisibility(GONE);
                break;
            //拼团详情
            case R.id.tv_group_product:
                if (shopPropertyBean != null
                        && shopPropertyBean.getGpInfoId() > 0) {
                    intent = new Intent(getActivity(), QualityGroupShopDetailActivity.class);
                    intent.putExtra("gpInfoId", String.valueOf(shopPropertyBean.getGpInfoId()));
                    startActivity(intent);
                }
                break;
            case R.id.iv_ql_shop_pro_cp_tag:
                showToast(getActivity(), "该商品不支持使用优惠券！");
                break;
            //sku属性选择
            case R.id.tv_ql_sp_pro_sku:
                if (skuDialog != null) {
                    skuDialog.show(false, isWaitSellStatus, "加入购物车");
                    skuDialog.getGoodsSKu(shopCarGoodsSku -> {
                        shopCarGoodsSkuDif = shopCarGoodsSku;
                        if (shopCarGoodsSkuDif != null) {
                            tv_ql_sp_pro_sku.setText(("已选：" + shopCarGoodsSkuDif.getValuesName()));
                            setReallyPrice(String.valueOf(shopCarGoodsSkuDif.getPrice()));
                            setPresentProData(shopCarGoodsSkuDif.getPresentIds());
                            if (!TextUtils.isEmpty(shopCarGoodsSkuDif.getProType())) {
                                switch (shopCarGoodsSkuDif.getProType()) {
                                    case "addCar":
                                        addCarCheckLoginStatus();
                                        break;
                                    case "buyGoIt":
                                        buyGoItCheckStatus();
                                        break;
                                }
                            }
                        } else {
                            tv_ql_sp_pro_sku.setText(String.format(getResources().getString(R.string.sel_pro_sku)
                                    , getStrings(shopPropertyBean.getProps().get(0).getPropName())));
                        }
                    });
                }
                break;


        }
    }

    private void getDirectCoupon(int id) {
        if (userId > 0) {
            String url = Url.BASE_URL + Url.FIND_ARTICLE_COUPON;
            Map<String, Object> params = new HashMap<>();
            params.put("userId", userId);
            params.put("couponId", id);
            NetLoadUtils.getNetInstance().loadNetDataPost(this, url, params, new NetLoadListenerHelper() {
                @Override
                public void onSuccess(String result) {
                    if (loadHud != null) {
                        loadHud.dismiss();
                    }
                    Gson gson = new Gson();
                    RequestStatus requestStatus = gson.fromJson(result, RequestStatus.class);
                    if (requestStatus != null) {
                        showToastRequestMsg(getActivity(), requestStatus);
                    }
                }

                @Override
                public void onNotNetOrException() {
                    if (loadHud != null) {
                        loadHud.dismiss();
                    }
                }
            });
        } else {
            getLoginStatus(getActivity());
        }
    }


    private void addCarCheckLoginStatus() {
        if (userId > 0) {
            if (tv_sp_details_add_car.isSelected() && shopPropertyBean != null) {
                addCancelNotice();
            } else {
                addGoodsToCar();
            }
        } else {
            getLoginStatus(getActivity());
        }
    }

    /**
     * 取消添加通知
     */
    private void addCancelNotice() {
        String url = Url.BASE_URL + Url.Q_REPLENISHMENT_NOTICE;
        Map<String, Object> params = new HashMap<>();
        params.put("uid", userId);
        params.put("skuId", skuSaleBeanId);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, url, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                RequestStatus requestStatus = gson.fromJson(result, RequestStatus.class);
                if (requestStatus != null) {
                    if (requestStatus.getCode().equals(SUCCESS_CODE)) {
                        tv_sp_details_add_car.setText(requestStatus.getIsNotice() == 1 ? "到货提醒" : "取消提醒");
                        showToast(getActivity(), requestStatus.getIsNotice() == 1 ? "已取消通知" : "已设置通知");
                    } else {
                        showToastRequestMsg(getActivity(), requestStatus);
                    }
                }
            }
        });
    }

    private void buyGoItCheckStatus() {
        if (userId > 0) {
            buyGoIt();
        } else {
            getLoginStatus(getActivity());
        }
    }


    private void getConstant() {
        if (constantMethod == null) {
            constantMethod = new ConstantMethod();
        }
    }


    /**
     * 插入一条数据
     */
    private void insertNewTotalData() {
        if (totalPersonalTrajectory == null) {
            totalPersonalTrajectory = new TotalPersonalTrajectory(getActivity());
        }
        Map<String, String> totalMap = new HashMap<>();
        totalMap.put("relate_id", productId);
        totalPersonalTrajectory.saveTotalDataToFile(totalMap);
    }

    @Override
    protected void postEventResult(@NonNull EventMessage message) {
        if (START_AUTO_PAGE_TURN.equals(message.type)) {
            if (banner_ql_sp_pro_details != null && !banner_ql_sp_pro_details.isCanScroll()) {
                openScrollBanner();
            }
        } else if (STOP_AUTO_PAGE_TURN.equals(message.type)) {
            if (banner_ql_sp_pro_details != null && banner_ql_sp_pro_details.isCanScroll()) {
                stopScrollBanner();
            }
        } else if (message.type.equals(ConstantVariable.UPDATE_CAR_NUM)) {
            getCarCount(getActivity(), badge);
        }
    }

    private void stopScrollBanner() {
        banner_ql_sp_pro_details.setCanScroll(false);
        banner_ql_sp_pro_details.setPointViewVisible(false);
    }

    private void openScrollBanner() {
        banner_ql_sp_pro_details.setCanScroll(true);
        banner_ql_sp_pro_details.setPointViewVisible(true);
    }


    @Override
    protected void onResume() {
        super.onResume();
        tv_product_share_tint.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (tv_product_share_tint != null) {
                    tv_product_share_tint.setVisibility(GONE);
                }
            }
        }, 5 * 1000);
    }

    @Override
    protected void onStop() {
        super.onStop();
        setTotalData();
    }

    @Override
    protected void onPause() {
        super.onPause();
        setTotalData();
    }


    @Override
    protected void onDestroy() {
        if (constantMethod != null) {
            constantMethod.stopSchedule();
            constantMethod.releaseHandlers();
        }
        super.onDestroy();
    }
}
