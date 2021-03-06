package com.amkj.dmsh.shopdetails.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
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
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.ConstantVariable;
import com.amkj.dmsh.constant.UMShareAction;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.dao.SoftApiDao;
import com.amkj.dmsh.dominant.activity.DirectProductEvaluationActivity;
import com.amkj.dmsh.dominant.activity.QualityGroupShopDetailActivity;
import com.amkj.dmsh.dominant.activity.QualityProductActActivity;
import com.amkj.dmsh.homepage.activity.ArticleOfficialActivity;
import com.amkj.dmsh.homepage.activity.JzVideoFullScreenActivity;
import com.amkj.dmsh.homepage.adapter.CommunalDetailAdapter;
import com.amkj.dmsh.homepage.bean.CommunalADActivityEntity.CommunalADActivityBean;
import com.amkj.dmsh.mine.activity.OpenVipActivity;
import com.amkj.dmsh.mine.activity.ShopCarActivity;
import com.amkj.dmsh.mine.bean.ShopCarEntity.ShopCartBean.CartBean.CartInfoBean;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.qyservice.QyProductIndentInfo;
import com.amkj.dmsh.qyservice.QyServiceUtils;
import com.amkj.dmsh.shopdetails.activity.QuestionsEntity.ResultBean;
import com.amkj.dmsh.shopdetails.activity.QuestionsEntity.ResultBean.QuestionInfoBean;
import com.amkj.dmsh.shopdetails.adapter.DirectEvaluationAdapter;
import com.amkj.dmsh.shopdetails.adapter.GoodsGroupAdapter;
import com.amkj.dmsh.shopdetails.adapter.GoodsRecommendAdapter;
import com.amkj.dmsh.shopdetails.adapter.ProductTextAdapter;
import com.amkj.dmsh.shopdetails.adapter.QuestionContentAdapter;
import com.amkj.dmsh.shopdetails.bean.CommunalDetailObjectBean;
import com.amkj.dmsh.shopdetails.bean.DirectGoodsServerEntity;
import com.amkj.dmsh.shopdetails.bean.DirectGoodsServerEntity.DirectGoodsServerBean;
import com.amkj.dmsh.shopdetails.bean.EditGoodsSkuEntity.EditGoodsSkuBean;
import com.amkj.dmsh.shopdetails.bean.GoodsCommentEntity;
import com.amkj.dmsh.shopdetails.bean.GoodsCommentEntity.GoodsCommentBean;
import com.amkj.dmsh.shopdetails.bean.GroupGoodsEntity;
import com.amkj.dmsh.shopdetails.bean.GroupGoodsEntity.GroupGoodsBean.CombineCommonBean;
import com.amkj.dmsh.shopdetails.bean.IndentProDiscountBean;
import com.amkj.dmsh.shopdetails.bean.ShopCarGoodsSku;
import com.amkj.dmsh.shopdetails.bean.ShopDetailsEntity;
import com.amkj.dmsh.shopdetails.bean.ShopDetailsEntity.ShopPropertyBean;
import com.amkj.dmsh.shopdetails.bean.ShopDetailsEntity.ShopPropertyBean.CouponJsonBean;
import com.amkj.dmsh.shopdetails.bean.ShopDetailsEntity.ShopPropertyBean.PresentProductInfoBean;
import com.amkj.dmsh.shopdetails.bean.ShopDetailsEntity.ShopPropertyBean.TagsBean;
import com.amkj.dmsh.shopdetails.bean.ShopRecommendHotTopicEntity;
import com.amkj.dmsh.shopdetails.bean.ShopRecommendHotTopicEntity.ShopRecommendHotTopicBean;
import com.amkj.dmsh.shopdetails.bean.SkuSaleBean;
import com.amkj.dmsh.user.bean.MarketLabelBean;
import com.amkj.dmsh.utils.CountDownTimer;
import com.amkj.dmsh.utils.DoubleUtil;
import com.amkj.dmsh.utils.ProductLabelCreateUtils;
import com.amkj.dmsh.utils.SharedPreUtils;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.amkj.dmsh.utils.gson.GsonUtils;
import com.amkj.dmsh.utils.webformatdata.CommunalWebDetailUtils;
import com.amkj.dmsh.utils.webformatdata.ShareDataBean;
import com.amkj.dmsh.views.alertdialog.AlertDialogTax;
import com.amkj.dmsh.views.bottomdialog.SkuDialog;
import com.amkj.dmsh.views.convenientbanner.ConvenientBanner;
import com.amkj.dmsh.views.convenientbanner.holder.CBViewHolderCreator;
import com.amkj.dmsh.views.convenientbanner.holder.Holder;
import com.amkj.dmsh.views.flycoTablayout.CommonTabLayout;
import com.amkj.dmsh.views.flycoTablayout.listener.CustomTabEntity;
import com.amkj.dmsh.views.flycoTablayout.listener.OnTabSelectListener;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.flexbox.FlexboxLayout;
import com.gyf.barlibrary.ImmersionBar;
import com.klinker.android.link_builder.Link;
import com.klinker.android.link_builder.LinkBuilder;
import com.melnykov.fab.FloatingActionButton;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.tencent.bugly.beta.tinker.TinkerManager;
import com.tencent.stat.StatService;
import com.umeng.socialize.UMShareAPI;

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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;
import cn.iwgang.countdownview.CountdownView;
import cn.iwgang.countdownview.DynamicConfig;
import me.jessyan.autosize.utils.AutoSizeUtils;
import q.rorbin.badgeview.Badge;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.getBadge;
import static com.amkj.dmsh.constant.ConstantMethod.getIntegralFormat;
import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.getRmbFormat;
import static com.amkj.dmsh.constant.ConstantMethod.getSpannableString;
import static com.amkj.dmsh.constant.ConstantMethod.getStringChangeDouble;
import static com.amkj.dmsh.constant.ConstantMethod.getStringChangeFloat;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.getStringsChNPrice;
import static com.amkj.dmsh.constant.ConstantMethod.getStringsFormat;
import static com.amkj.dmsh.constant.ConstantMethod.isVip;
import static com.amkj.dmsh.constant.ConstantMethod.showImageActivity;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.showToastRequestMsg;
import static com.amkj.dmsh.constant.ConstantMethod.skipProductUrl;
import static com.amkj.dmsh.constant.ConstantMethod.stripTrailingZeros;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.INDENT_W_TYPE;
import static com.amkj.dmsh.constant.ConstantVariable.IS_LOGIN_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.REGEX_NUM;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.TYPE_2;
import static com.amkj.dmsh.constant.ConstantVariable.isShowTint;
import static com.amkj.dmsh.dao.OrderDao.getCarCount;
import static com.amkj.dmsh.find.activity.ImagePagerActivity.IMAGE_DEF;
import static com.amkj.dmsh.shopdetails.bean.CommunalDetailObjectBean.TYPE_PRODUCT_TITLE;
import static com.amkj.dmsh.utils.TimeUtils.getTimeDifferenceText;
import static com.amkj.dmsh.utils.TimeUtils.isEndOrStartTime;
import static com.amkj.dmsh.utils.glide.GlideImageLoaderUtil.getSquareImgUrl;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/7/15
 * class description: ??????????????????
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
    //            ????????????
    @BindView(R.id.tv_ql_sp_pro_sc_name)
    TextView tv_ql_sp_pro_sc_name;
    //            ????????????
    @BindView(R.id.tv_ql_sp_pro_sc_price)
    TextView tv_ql_sp_pro_sc_price;
    //            ????????????
    @BindView(R.id.tv_ql_sp_pro_sc_market_price)
    TextView tv_ql_sp_pro_sc_market_price;
    //    ????????????
    @BindView(R.id.rel_shop_pro_time)
    RelativeLayout rel_shop_pro_time;
    //    ???????????????
    @BindView(R.id.tv_pro_time_detail_status)
    TextView tv_pro_time_detail_status;
    //    ???????????????
    @BindView(R.id.ct_pro_show_time_detail)
    CountdownView ct_pro_show_time_detail;
    @BindView(R.id.fbl_details_market_label)
    FlexboxLayout fbl_details_market_label;
    //    ????????????
    @BindView(R.id.ll_layout_pro_sc_tag)
    LinearLayout ll_layout_pro_sc_tag;
    @BindView(R.id.flex_product_tag)
    FlexboxLayout flex_product_tag;
    @BindView(R.id.flex_bug_before)
    FlexboxLayout flex_buy_before;
    //    ????????????
    @BindView(R.id.ll_pro_buy_before)
    LinearLayout ll_pro_buy_before;

    @BindView(R.id.communal_recycler_wrap)
    RecyclerView communal_recycler_wrap;
    @BindView(R.id.rel_shop_details_comment)
    LinearLayout rel_shop_details_comment;
    //  ????????????????????????
    @BindView(R.id.iv_ql_shop_pro_cp_tag)
    ImageView iv_ql_shop_pro_cp_tag;
    //    ???????????? ????????????
    @BindView(R.id.rv_product_details)
    RecyclerView rv_product_details;
    //    Sku??????
    @BindView(R.id.ll_sp_pro_sku_value)
    LinearLayout ll_sp_pro_sku_value;
    //    sku
    @BindView(R.id.tv_ql_sp_pro_sku)
    TextView tv_ql_sp_pro_sku;
    //    ???????????????
    @BindView(R.id.tv_sp_details_add_car)
    TextView tv_sp_details_add_car;
    //    ?????? ???????????????????????????
    @BindView(R.id.ll_pro_layout)
    LinearLayout ll_pro_layout;
    //    ????????????
    @BindView(R.id.tv_sp_details_buy_it)
    TextView tv_sp_details_buy_it;
    // ??????????????????
    @BindView(R.id.tv_product_share_tint)
    TextView tv_product_share_tint;
    @BindView(R.id.fl_header_service)
    RelativeLayout fl_header_service;
    //    ????????????
    @BindView(R.id.tv_sp_details_collect)
    TextView tv_sp_details_collect;
    //  ??????????????????
    @BindView(R.id.ll_product_activity_detail)
    LinearLayout ll_product_activity_detail;
    @BindView(R.id.tv_product_activity_description)
    TextView tv_product_activity_description;
    // ????????????
    @BindView(R.id.ll_top_end_time)
    LinearLayout ll_top_end_time;
    //????????????
    @BindView(R.id.ll_top_start_time)
    LinearLayout ll_top_start_time;
    @BindView(R.id.tv_start_time)
    TextView tv_start_time;
    @BindView(R.id.cv_countdownTime_white_hours)
    CountdownView cv_countdownTime_white_hours;
    //    ???????????????
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
    RelativeLayout mRlToolbar;
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
    @BindView(R.id.ll_product_recommend)
    LinearLayout mllProductRecommend;
    @BindView(R.id.ll_product_group)
    LinearLayout mllProductGroup;
    @BindView(R.id.ll_scrool_detail_price)
    LinearLayout mLlScroolDetailPrice;
    @BindView(R.id.rl_activity_price)
    RelativeLayout mRlActivityPrice;
    @BindView(R.id.ll_deposit_info)
    LinearLayout mLlDepositInfo;
    @BindView(R.id.ll_countdown_time_bottom)
    LinearLayout mLlTimeHoursBottom;
    @BindView(R.id.tv_top_activity_price)
    TextView mTvTopActivityPrice;
    @BindView(R.id.tv_product_martket_price)
    TextView mTvProductMartketPrice;
    @BindView(R.id.tv_tips)
    TextView mTvTipsBottom;
    @BindView(R.id.cv_countdownTime_bottom)
    CountdownView mCtCountDownBottom;
    @BindView(R.id.iv_next_icon)
    ImageView mIvNextIcon;
    @BindView(R.id.rl_product_activity_description)
    LinearLayout rl_product_activity_description;
    //        ????????????
    @BindView(R.id.tv_shop_comment_count)
    TextView tv_shop_comment_count;
    @BindView(R.id.tv_shippingProvince)
    TextView mTvShippingProvince;
    @BindView(R.id.tv_shipping)
    TextView mTvShipping;
    @BindView(R.id.ll_shippingProvince)
    LinearLayout mLlShippingProvince;
    @BindView(R.id.tv_preSaleDeliveryTime)
    TextView mTvPreSaleDeliveryTime;
    @BindView(R.id.tv_tax_text)
    TextView mTvTaxText;
    @BindView(R.id.tv_vip_price)
    TextView mTvVipPrice;
    @BindView(R.id.ll_tax_txt)
    LinearLayout mLlTaxText;
    @BindView(R.id.ll_open_vip)
    LinearLayout mLlOpenVip;
    @BindView(R.id.tv_vip_save)
    TextView mTvVipSave;
    @BindView(R.id.ll_vip_price)
    LinearLayout mLlVipPrice;
    @BindView(R.id.iv_top_vip_logo)
    ImageView mIvTopVipLogo;
    @BindView(R.id.iv_bottom_vip_logo)
    ImageView mIvBottomVipLogo;
    @BindView(R.id.tv_bottom_activity_price)
    TextView mTvBottomActivityPrice;
    @BindView(R.id.ll_bottom_activity_price)
    LinearLayout mLlBottomActivityPrice;
    @BindView(R.id.iv_vip_logo)
    ImageView mIvVipLogo;
    @BindView(R.id.rl_price_and_province)
    RelativeLayout mRlPriceAndProvince;
    @BindView(R.id.tv_deposit_discount_price)
    TextView mTvDepositDiscountPrice;
    @BindView(R.id.tv_deposit_price)
    TextView mTvDepositPrice;
    @BindView(R.id.tv_deposit_allowance)
    TextView mTvDepositAllowance;
    @BindView(R.id.ll_deposit_pay)
    LinearLayout mLlDepositPay;
    @BindView(R.id.tv_deposit_discount_detail)
    TextView mTvDepositDiscountDetail;
    @BindView(R.id.tv_deposit)
    TextView mTvDeposit;
    @BindView(R.id.tv_deposit_discount)
    TextView mTvDepositDiscount;
    @BindView(R.id.tv_deposit_order_time)
    TextView mTvDepositOrderTime;
    @BindView(R.id.rl_deposit_discount_info)
    LinearLayout mLlDepositDiscountInfo;
    @BindView(R.id.iv_video)
    ImageView mIvVideo;
    @BindView(R.id.iv_close_float)
    ImageView mIvCloseFloat;
    @BindView(R.id.cv_video_float)
    CardView mLlVideoFloat;
    @BindView(R.id.tv_question_num)
    TextView mTvQuestionNum;
    @BindView(R.id.rv_question)
    RecyclerView mRvQuestion;
    @BindView(R.id.ll_question)
    LinearLayout mLlQuestion;
    @BindView(R.id.ll_ask)
    LinearLayout mLlAsk;


    //    ????????????
    private List<PresentProductInfoBean> presentProductInfoBeans = new ArrayList<>();
    //    ??????????????????
    private List<ShopRecommendHotTopicBean> articalRecommendList = new ArrayList<>();
    //    ????????????
    private List<ShopRecommendHotTopicBean> goodsRecommendList = new ArrayList<>();
    //    ????????????
    private List<CombineCommonBean> goodsGroupList = new ArrayList<>();
    //    ??????????????????
    private List<CommunalADActivityBean> imagesVideoList = new ArrayList<>();
    //    ????????????
    private List<GoodsCommentBean> goodsComments = new ArrayList<>();
    //    ????????????
    private List<QuestionInfoBean> questions = new ArrayList<>();
    //???????????? ???????????? ??????
    private List<CommunalDetailObjectBean> shopDetailBeanList = new ArrayList<>();
    //    ????????????
    private List<CommunalDetailObjectBean> serviceDataList = new ArrayList<>();

    private DirectEvaluationAdapter directEvaluationAdapter;
    private String productId;
    private ShopDetailsEntity shopDetailsEntity;
    private ShopPropertyBean shopPropertyBean;
    private EditGoodsSkuBean editGoodsSkuBean;
    private SkuDialog skuDialog;
    private ShopCarGoodsSku shopCarGoodsSkuDif;
    private Badge badge;
    //    private View proTitleView;
    private String sharePageUrl = Url.BASE_SHARE_PAGE_TWO + "common/proprietary.html?id=";
    private boolean isWaitSellStatus;
    private ProductTextAdapter presentProAdapter;
    private CommunalDetailAdapter communalDetailAdapter;
    private String[] detailTabData = {"??????", "??????"};
    private ArrayList<CustomTabEntity> tabData = new ArrayList<>();
    private int measuredHeight;
    private CBViewHolderCreator cbViewHolderCreator;
    private int skuSaleBeanId;
    private int screenHeight;
    private int screenWith;
    private GoodsRecommendAdapter mGoodsRecommendAdapter;
    private GoodsGroupAdapter mGoodsGroupAdapter;
    private AlertDialog alertDialog;
    private DirectGoodsServerEntity mDirectGoodsServerEntity;
    private GroupGoodsEntity mGroupGoodsEntity;
    private CountDownTimer mCountDownEndTimer;
    private CountDownTimer mCountDownStartTimer;
    private CountDownTimer mCountDownForSale;
    private AlertDialogTax mAlertDialogTax;
    private QuestionContentAdapter mQuestionListAdapter;

    @Override
    protected int getContentView() {
        return R.layout.activity_ql_shop_pro_details;
    }

    @Override
    public void setStatusBar() {
        ImmersionBar.with(this).keyboardEnable(true).navigationBarEnable(false).statusBarDarkFont(true).fullScreen(true).init();
    }

    @Override
    protected void initViews() {
        tv_product_activity_description.setSelected(true);
        //?????????????????????paddingw
        int statusBarHeight = ImmersionBar.getStatusBarHeight(this);
        int paddingTop = statusBarHeight > 0 ? statusBarHeight : AutoSizeUtils.mm2px(this, 40);
        mRlToolbar.setPadding(0, paddingTop, 0, 0);
        mRlToolbar2.setPadding(0, paddingTop, 0, 0);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) fl_header_service.getLayoutParams();
        layoutParams.topMargin = paddingTop;
        fl_header_service.setLayoutParams(layoutParams);
        DynamicConfig.Builder dynamicDetails = new DynamicConfig.Builder();
        dynamicDetails.setSuffixTextSize(AutoSizeUtils.mm2px(mAppContext, 22));
        dynamicDetails.setTimeTextSize(AutoSizeUtils.mm2px(mAppContext, 22));
        ct_pro_show_time_detail.dynamicShow(dynamicDetails.build());
        //?????????????????????
        DynamicConfig.Builder buyDynamic = new DynamicConfig.Builder();
        buyDynamic.setTimeTextColor(getResources().getColor(R.color.text_normal_red));
        buyDynamic.setSuffixTextColor(getResources().getColor(R.color.text_black_t));
        ct_pro_show_time_detail.dynamicShow(buyDynamic.build());
        //???????????????????????????
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
        //???????????????????????????
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
        TinkerBaseApplicationLike app = (TinkerBaseApplicationLike) TinkerManager.getTinkerApplicationLike();
        screenHeight = app.getScreenHeight();
        screenWith = app.getScreenWidth();
        if (TextUtils.isEmpty(productId)) {
            showToast("??????????????????????????????");
            finish();
        }
        smart_ql_sp_pro_details.setOnRefreshListener(refreshLayout -> loadData());
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
        communalDetailAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            ShareDataBean shareDataBean = null;
            if (view.getId() == R.id.tv_communal_share && shopPropertyBean != null) {
                shareDataBean = new ShareDataBean(shopPropertyBean.getPicUrl()
                        , "???????????????????????????" + shopPropertyBean.getName()
                        , getStrings(shopPropertyBean.getSubtitle())
                        , sharePageUrl + shopPropertyBean.getId());
            }
            CommunalWebDetailUtils.getCommunalWebInstance()
                    .setWebDataClick(getActivity(), shareDataBean, view, loadHud);
        });
        rv_product_details.setAdapter(communalDetailAdapter);

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


                //???????????????
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

        //????????????????????????
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
        download_btn_communal.setOnClickListener(v -> {
            scroll_pro.fling(0);
            scroll_pro.scrollTo(0, 0);
            download_btn_communal.hide(false);
        });
        badge = getBadge(getActivity(), fl_header_service);
        //?????????????????????
        communal_recycler_wrap.setLayoutManager(new LinearLayoutManager(getActivity()));
        directEvaluationAdapter = new DirectEvaluationAdapter(getActivity(), goodsComments);
        communal_recycler_wrap.setNestedScrollingEnabled(false);
        communal_recycler_wrap.setAdapter(directEvaluationAdapter);
        directEvaluationAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            switch (view.getId()) {
                case R.id.tv_eva_count:
                    GoodsCommentBean goodsCommentBean = (GoodsCommentBean) view.getTag();
                    if (goodsCommentBean != null) {
                        if (userId > 0) {
                            setProductEvaLike(view);
                        } else {
                            getLoginStatus(getActivity());
                        }
                    }
                    break;
            }
        });

        //?????????????????????
        mRvQuestion.setLayoutManager(new LinearLayoutManager(this));
        mQuestionListAdapter = new QuestionContentAdapter(this, questions, productId);
        mRvQuestion.setAdapter(mQuestionListAdapter);

        //???????????????????????????
        LinearLayoutManager groupManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRvGoodsGroup.setLayoutManager(groupManager);
        mGoodsGroupAdapter = new GoodsGroupAdapter(this, goodsGroupList);
        mRvGoodsGroup.setAdapter(mGoodsGroupAdapter);
        mGoodsGroupAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (mGroupGoodsEntity != null && mGroupGoodsEntity.getResult() != null && mGroupGoodsEntity.getResult().getCombineMainProduct() != null) {
                Intent intent1 = new Intent(getActivity(), GroupMatchActivity.class);
                intent1.putExtra("productId", String.valueOf(mGroupGoodsEntity.getResult().getCombineMainProduct().getProductId()));
                getActivity().startActivity(intent1);
            }
        });

        //???????????????????????????
        mRvGoodsRecommend.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mGoodsRecommendAdapter = new GoodsRecommendAdapter(this, goodsRecommendList, true);
        mRvGoodsRecommend.setAdapter(mGoodsRecommendAdapter);
        mGoodsRecommendAdapter.setOnItemClickListener((adapter, view, position) -> {
            ShopRecommendHotTopicBean shopRecommendHotTopicBean = (ShopRecommendHotTopicBean) view.getTag();
            if (shopRecommendHotTopicBean != null) {
                skipProductUrl(getActivity(), shopRecommendHotTopicBean.getType_id(), shopRecommendHotTopicBean.getId());
            }
        });

        //????????????
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
            communalDetailAdapter.notifyDataSetChanged();
            getShopProDetails();
            getServiceData(productId);
            getCarCount(getActivity());
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

    //??????????????????
    private void getShopProDetails() {
        String url = Url.Q_NEW_SHOP_DETAILS;
        Map<String, Object> params = new HashMap<>();
        params.put("id", productId);
        if (userId > 0) {
            params.put("uid", userId);
        }
        NetLoadUtils.getNetInstance().loadNetDataPost(this, url, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                smart_ql_sp_pro_details.finishRefresh();
                shopDetailsEntity = GsonUtils.fromJson(result, ShopDetailsEntity.class);
                if (shopDetailsEntity != null) {
                    if (shopDetailsEntity.getCode().equals(SUCCESS_CODE)) {
                        shopPropertyBean = shopDetailsEntity.getShopPropertyBean();
                        setProductData(shopPropertyBean);
                        getCombineGoods(shopPropertyBean.getId());
                        getShopProComment(shopPropertyBean);
                        getArticalRecommend(shopPropertyBean.getId());
                        getGoodsRecommend(shopPropertyBean.getId());
                        getQuestions();
                    } else if (!shopDetailsEntity.getCode().equals(EMPTY_CODE)) {
                        showToast(shopDetailsEntity.getMsg());
                        if ("32".equals(shopDetailsEntity.getCode())) {
                            finish();
                        }
                    }
                }
                NetLoadUtils.getNetInstance().showLoadSir(loadService, shopDetailsEntity);
            }

            @Override
            public void onNotNetOrException() {
                smart_ql_sp_pro_details.finishRefresh();
                NetLoadUtils.getNetInstance().showLoadSir(loadService, shopDetailsEntity);
            }
        });
    }

    private void getQuestions() {
        Map<String, Object> map = new HashMap<>();
        map.put("productId", productId);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, Url.GET_QUESTION_LIST, map, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                questions.clear();
                QuestionsEntity questionsEntity = GsonUtils.fromJson(result, QuestionsEntity.class);
                if (questionsEntity != null) {
                    ResultBean questionBean = questionsEntity.getResult();
                    if (questionBean != null) {
                        List<QuestionInfoBean> questionInfoList = questionBean.getQuestionInfoList();
                        if (questionInfoList != null && questionInfoList.size() > 0) {
                            mTvQuestionNum.setText(getIntegralFormat(getActivity(), R.string.question_num, questionInfoList.size()));
                            questions.addAll(questionInfoList.subList(0, Math.min(questionInfoList.size(), 2)));
                        } else {
                            mTvQuestionNum.setText("????????????");
                        }
                    }
                }
                mQuestionListAdapter.notifyDataSetChanged();
                mRvQuestion.setVisibility(questions.size() > 0 ? VISIBLE : GONE);
                mLlAsk.setVisibility(questions.size() == 0 ? VISIBLE : GONE);
            }

            @Override
            public void onNotNetOrException() {
                mRvQuestion.setVisibility(questions.size() > 0 ? VISIBLE : GONE);
                mLlAsk.setVisibility(questions.size() == 0 ? VISIBLE : GONE);
            }
        });
    }

    //??????????????????
    private void getGoodsRecommend(final int id) {
        String url = Url.Q_SP_DETAIL_RECOMMEND;
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

    //??????????????????
    private void getArticalRecommend(final int id) {
        String url = Url.Q_SP_DETAIL_TOPIC_RECOMMEND;
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
                                GlideImageLoaderUtil.loadRoundImg(getActivity(), mIvArticalCover, hotTopicBean.getPicUrl(), 0);
                                mTvArticalTitle.setText(getStrings(hotTopicBean.getTitle()));
                                mTvArticalDesc.setText(getStrings(hotTopicBean.getDigest()));
                            }
                        }
                    }
                }
            }
        });
    }

    //??????????????????????????????
    private void getCombineGoods(int id) {
        String url = Url.Q_GROUP_GOODS_BASIC;
        Map<String, Object> params = new HashMap<>();
        params.put("productId", id);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, url, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                mGroupGoodsEntity = GsonUtils.fromJson(result, GroupGoodsEntity.class);
                if (mGroupGoodsEntity != null && mGroupGoodsEntity.getResult() != null) {
                    if (mGroupGoodsEntity.getCode().equals(SUCCESS_CODE)) {
                        GroupGoodsEntity.GroupGoodsBean groupGoodsBean = mGroupGoodsEntity.getResult();
                        List<CombineCommonBean> combineProductList = groupGoodsBean.getCombineMatchProductList();
                        if (combineProductList != null && combineProductList.size() > 0) {
                            goodsGroupList.clear();
                            goodsGroupList.addAll(combineProductList.subList(0, combineProductList.size() > 20 ? 20 : combineProductList.size()));
                            mGoodsGroupAdapter.notifyDataSetChanged();
                        }

                        mllProductGroup.setVisibility(goodsGroupList.size() > 0 ? VISIBLE : GONE);
                    }
                }
            }
        });
    }

    //  ??????????????????
    private void getShopProComment(ShopPropertyBean shopPropertyBean) {
        String url = Url.Q_SHOP_DETAILS_COMMENT;
        Map<String, Object> params = new HashMap<>();
        params.put("currentPage", 1);
        params.put("showCount", 2);
        params.put("id", shopPropertyBean.getId());
        if (userId > 0) {
            params.put("uid", userId);
        }
        NetLoadUtils.getNetInstance().loadNetDataPost(this, url, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                goodsComments.clear();
                GoodsCommentEntity goodsCommentEntity = GsonUtils.fromJson(result, GoodsCommentEntity.class);
                if (goodsCommentEntity != null) {
                    if (goodsCommentEntity.getCode().equals(SUCCESS_CODE)) {
                        goodsComments.addAll(goodsCommentEntity.getGoodsComments());
                    } else if (!goodsCommentEntity.getCode().equals(EMPTY_CODE)) {
                        showToast(goodsCommentEntity.getMsg());
                    }
                    tv_shop_comment_count.setText(("Ta?????????(" + goodsCommentEntity.getEvaluateCount() + ")"));
                }
                rel_shop_details_comment.setVisibility(goodsComments.size() > 0 ? VISIBLE : GONE);
                directEvaluationAdapter.notifyDataSetChanged();
//                if (goodsComments.size() < TOTAL_COUNT_TEN) {
//                    directEvaluationAdapter.removeAllFooterView();
//                }
            }
        });
    }

    private void setProductEvaLike(View view) {
        GoodsCommentBean goodsCommentBean = (GoodsCommentBean) view.getTag();
        TextView tv_eva_like = (TextView) view;
        String url = Url.SHOP_EVA_LIKE;
        Map<String, Object> params = new HashMap<>();
        params.put("id", goodsCommentBean.getId());
        params.put("uid", userId);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, url, params, null);
        goodsCommentBean.setFavor(!goodsCommentBean.isFavor());
        goodsCommentBean.setLikeNum(goodsCommentBean.isFavor() ? goodsCommentBean.getLikeNum() + 1 : goodsCommentBean.getLikeNum() - 1);
        tv_eva_like.setSelected(!tv_eva_like.isSelected());
        tv_eva_like.setText(ConstantMethod.getNumCount(tv_eva_like.isSelected(), goodsCommentBean.isFavor(), goodsCommentBean.getLikeNum(), "???"));
    }


    private void setProductData(final ShopPropertyBean shopProperty) {
        if (shopProperty == null) return;
        //??????????????????????????????
        mLlDepositPay.setVisibility(shopProperty.isDepositFlag() ? VISIBLE : GONE);
        mTvDepositDiscountDetail.setText(getStringsFormat(this, R.string.deposit_discount_detail, shopProperty.getDeposit(), shopProperty.getDepositDiscount()));
        //???????????????????????????????????????
        mLlVideoFloat.setVisibility(shopProperty.haveVideo() ? VISIBLE : GONE);
        GlideImageLoaderUtil.loadCenterCrop(this, mIvVideo, shopProperty.getPicUrl());
        imagesVideoList.clear();
        List<String> imageList = Arrays.asList(shopProperty.getImages().split(","));
        for (int i = 0; i < imageList.size(); i++) {
            String imgUrl = imageList.get(i);
            imagesVideoList.add(new CommunalADActivityBean(i == 0 ? getSquareImgUrl(imgUrl, screenWith, shopProperty.getWaterRemark()) : imgUrl, ""));
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
        banner_ql_sp_pro_details.setPages(this, cbViewHolderCreator, imagesVideoList);
        banner_ql_sp_pro_details.setOnItemClickListener(position -> {
            if (imagesVideoList.size() > position) {
                showImageActivity(getActivity(), IMAGE_DEF, position, imageList);
            }
        });

        //??????????????????
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

        if (isEndOrStartTime(shopPropertyBean.getStartTime(), shopDetailsEntity.getCurrentTime())
                && TextUtils.isEmpty(shopPropertyBean.getActivityTag())) {
            ll_sp_pro_sku_value.setVisibility(VISIBLE);
            rel_shop_pro_time.setVisibility(VISIBLE);
            //    ??????
            tv_sp_details_buy_it.setText(R.string.buy_go_wait);
            tv_sp_details_buy_it.setEnabled(false);
            tv_sp_details_add_car.setEnabled(true);
            isWaitSellStatus = true;
            startProductDownTime();
        } else {  //        ????????????
            rel_shop_pro_time.setVisibility(GONE);
            if (isEndOrStartTime(shopPropertyBean.getStartTime(), shopDetailsEntity.getCurrentTime())) {
                //    ??????
                tv_sp_details_buy_it.setText(R.string.buy_go_wait);
                tv_sp_details_buy_it.setEnabled(false);
                tv_sp_details_add_car.setEnabled(true);
                isWaitSellStatus = true;
            } else {
                isWaitSellStatus = false;
                if (shopProperty.getQuantity() > 0) {
                    tv_sp_details_buy_it.setText(R.string.buy_go_it);
                    tv_sp_details_add_car.setEnabled(true);
                    //    ????????????
                    tv_sp_details_buy_it.setEnabled(true);
                    ll_sp_pro_sku_value.setVisibility(VISIBLE);
                } else {
                    setReplenishmentNotice();
                    //    ?????????
                    tv_sp_details_buy_it.setText(R.string.buy_go_null);
                    tv_sp_details_buy_it.setEnabled(false);
                    ll_sp_pro_sku_value.setVisibility(GONE);
                }
            }
        }


        //????????????
        List<SkuSaleBean> skuSaleList = shopProperty.getSkuSale();
        Collections.sort(skuSaleList, (lhs, rhs) -> Float.compare(getStringChangeFloat(lhs.getPrice()), getStringChangeFloat(rhs.getPrice())));

        //??????????????????????????????
        if (!TextUtils.isEmpty(shopProperty.getNewUserTag())) {
            ll_product_activity_detail.setVisibility(VISIBLE);
            mLlScroolDetailPrice.setVisibility(GONE);
            mLlVipPrice.setVisibility(GONE);
            ll_top_end_time.setVisibility(GONE);//??????????????????
            rl_product_activity_description.setVisibility(GONE);
            mRlActivityPrice.setVisibility(VISIBLE);
            mLlTimeHoursBottom.setVisibility(GONE); //??????????????????
            String newUserTag = "";
            String end = "";
            if (skuSaleList.size() > 0) {
                newUserTag = skuSaleList.get(0).getNewUserTag();
                end = skuSaleList.get(0).getPrice().equals(skuSaleList.get(skuSaleList.size() - 1).getPrice()) ? "" : "???";
            }
            String price = newUserTag + getRmbFormat(this, shopProperty.getPrice()) + end;
            mTvTopActivityPrice.setText(getSpannableString(price, 1 + newUserTag.length(), price.length() - end.length(), 1.6f, null));
            mTvProductMartketPrice.setText(getStringsFormat(this, R.string.money_market_price_chn, shopProperty.getMarketPrice()));
            mTvProductMartketPrice.setVisibility(getStringChangeDouble(shopProperty.getMarketPrice()) > 0 ? VISIBLE : GONE);//??????0?????????
        } else {
            //????????????????????????????????????
            String activityCode = shopProperty.getActivityCode();
            //??????????????????????????????????????????????????????????????????????????????
            if (!TextUtils.isEmpty(activityCode) && !TextUtils.isEmpty(shopProperty.getActivityTag()) && !isEndOrStartTime(shopDetailsEntity.getCurrentTime()
                    , shopPropertyBean.getActivityEndTime())) {
                ll_product_activity_detail.setVisibility(VISIBLE);
                if (activityCode.contains("XSG")) {
                    setTopTimeVisible(true);
                    mRlActivityPrice.setVisibility(VISIBLE);       //???????????????
                    mLlDepositInfo.setVisibility(GONE);
                    rl_product_activity_description.setVisibility(GONE);
                    mLlScroolDetailPrice.setVisibility(GONE); //????????????????????????
                    mLlVipPrice.setVisibility(GONE);
                    mLlTimeHoursBottom.setVisibility(GONE); //??????????????????
                    startActivityDownTime(cv_countdownTime_white_hours, false);//???????????????
                    //????????????
                    List<String> oldPrices = new ArrayList<>();
                    List<String> prices = new ArrayList<>();
                    List<String> vipPrices = new ArrayList<>();
                    List<String> activityPrices = new ArrayList<>();
                    List<String> vipActivityPrices = new ArrayList<>();
                    for (SkuSaleBean skuSaleBean : shopProperty.getSkuSale()) {
                        if (getStringChangeFloat(skuSaleBean.getOldPrice()) > 0) {
                            oldPrices.add(skuSaleBean.getOldPrice());
                        }
                        if (getStringChangeFloat(skuSaleBean.getPrice()) > 0) {
                            prices.add(skuSaleBean.getPrice());
                        }
                        if (getStringChangeFloat(skuSaleBean.getVipPrice()) > 0) {
                            vipPrices.add(skuSaleBean.getVipPrice());
                        }
                        if (getStringChangeFloat(skuSaleBean.getActivityPrice()) > 0) {
                            activityPrices.add(skuSaleBean.getActivityPrice());
                        }
                        if (getStringChangeFloat(skuSaleBean.getVipActivityPrice()) > 0) {
                            vipActivityPrices.add(skuSaleBean.getVipActivityPrice());
                        }
                    }
                    Collections.sort(oldPrices, (lhs, rhs) -> Float.compare(getStringChangeFloat(lhs), getStringChangeFloat(rhs)));
                    Collections.sort(prices, (lhs, rhs) -> Float.compare(getStringChangeFloat(lhs), getStringChangeFloat(rhs)));
                    Collections.sort(vipPrices, (lhs, rhs) -> Float.compare(getStringChangeFloat(lhs), getStringChangeFloat(rhs)));
                    Collections.sort(activityPrices, (lhs, rhs) -> Float.compare(getStringChangeFloat(lhs), getStringChangeFloat(rhs)));
                    Collections.sort(vipActivityPrices, (lhs, rhs) -> Float.compare(getStringChangeFloat(lhs), getStringChangeFloat(rhs)));

                    //?????????????????????
                    boolean isStart = isEndOrStartTime(shopDetailsEntity.getCurrentTime(), shopDetailsEntity.getShopPropertyBean().getActivityStartTime());
                    //???????????????
                    String vipActivityPrice = isStart ? getMinPrice(vipPrices) + appendEnd(vipPrices) : getMinPrice(vipActivityPrices) + appendEnd(vipActivityPrices);
                    //??????????????????
                    String noVipActivityPrice = "??" + (isStart ? getMinPrice(prices) + appendEnd(prices) : getMinPrice(activityPrices) + appendEnd(activityPrices));
                    //??????????????????  ????????????????????????????????? ?????????????????????????????????
                    String topActivityPrice = isVip() && !TextUtils.isEmpty(vipActivityPrice) ? "???????????" + vipActivityPrice : noVipActivityPrice;
                    mTvTopActivityPrice.setText(getSpannableString(topActivityPrice, topActivityPrice.indexOf("??") + 1, topActivityPrice.contains("???") ? topActivityPrice.length() - 1 : topActivityPrice.length(),
                            1.6f, null));
                    //??????????????????  ???????????????????????????????????? ??????????????????????????????
                    String bottomActivityPrice = isVip() ? "????????????" + noVipActivityPrice : "???????????" + vipActivityPrice;
                    mTvBottomActivityPrice.setText(bottomActivityPrice);
                    mIvVipLogo.setVisibility(!isVip() ? VISIBLE : GONE);
                    mLlBottomActivityPrice.setVisibility(!TextUtils.isEmpty(vipActivityPrice) ? VISIBLE : GONE);
                    //??????(???????????? ????????????????????????????????????oldPrice,????????????????????? ?????????????????????????????????vipPrice,??????????????????price)
                    String oldPrice = isStart ? (getMinPrice(oldPrices)) : (isVip() ? getMinPrice(vipPrices) : getMinPrice(prices));
                    mTvProductMartketPrice.setText("???????????" + oldPrice);
                    mTvProductMartketPrice.setVisibility(getStringChangeFloat(oldPrice) > 0 ? VISIBLE : GONE);
                } else if (activityCode.contains("MJ") || activityCode.contains("MM") || activityCode.contains("MZ")) {
                    setTopTimeVisible(false);
                    mLlDepositInfo.setVisibility(GONE);
                    mRlActivityPrice.setVisibility(GONE);
                    mIvNextIcon.setVisibility(VISIBLE);
                    tv_product_activity_description.setVisibility(VISIBLE);
                    mLlScroolDetailPrice.setVisibility(VISIBLE);
                    mLlTimeHoursBottom.setVisibility(VISIBLE);
                    mLlVipPrice.setVisibility(VISIBLE);
                    startActivityDownTime(mCtCountDownBottom, true);
                } else if (activityCode.contains("YS")) {
                    rl_product_activity_description.setVisibility(GONE);
                    //???????????????????????????????????????????????????????????????
                    if (!isEndOrStartTime(shopDetailsEntity.getCurrentTime(), shopPropertyBean.getDepositStartTime()) || shopProperty.isDepositFlag()) {
                        ll_product_activity_detail.setVisibility(VISIBLE);
                        mLlDepositDiscountInfo.setVisibility(VISIBLE);
                        mTvDeposit.setText(getStringsFormat(this, R.string.deposit, shopProperty.getDeposit()));
                        mTvDepositDiscount.setText(getStringsFormat(this, R.string.deposit_discount, shopProperty.getDepositDiscount()));
                        mTvDepositOrderTime.setText(getStringsFormat(this, R.string.deposit_order_time, shopProperty.getDepositOrderStartTime(), shopProperty.getDepositOrderEndTime()));
                        setTopTimeVisible(true);
                        mLlDepositInfo.setVisibility(VISIBLE);
                        mRlActivityPrice.setVisibility(GONE);
                        mIvNextIcon.setVisibility(GONE);
                        tv_product_activity_description.setVisibility(VISIBLE);
                        mLlScroolDetailPrice.setVisibility(VISIBLE);
                        mLlTimeHoursBottom.setVisibility(GONE);
                        startActivityDownTime(cv_countdownTime_white_hours, false);//???????????????
                        //??????????????????
                        mTvDepositPrice.setText(getStringsChNPrice(this, shopProperty.getPrice()));
                        String depositAllowance = Double.toString(DoubleUtil.sub(shopProperty.getDepositDiscount(), shopProperty.getDeposit()));
                        mTvDepositAllowance.setText(getStringsChNPrice(this, depositAllowance));
                        mTvDepositDiscountPrice.setText(getStringsChNPrice(this, stripTrailingZeros(Double.toString(DoubleUtil.sub(shopProperty.getPrice(), depositAllowance)))));
                    } else {
                        ll_product_activity_detail.setVisibility(GONE);
                        mLlDepositDiscountInfo.setVisibility(GONE);
                    }
                } else {
                    //activityCode.contains("LJ") || activityCode.contains("ZK") || activityCode.contains("TH")|| activityCode.contains("YK")
                    setTopTimeVisible(true);
                    mLlDepositInfo.setVisibility(GONE);
                    mRlActivityPrice.setVisibility(GONE);
                    mIvNextIcon.setVisibility(GONE);
                    tv_product_activity_description.setVisibility(VISIBLE);
                    mLlScroolDetailPrice.setVisibility(VISIBLE);
                    mLlTimeHoursBottom.setVisibility(GONE);
                    mLlVipPrice.setVisibility(VISIBLE);
                    startActivityDownTime(cv_countdownTime_white_hours, false);
                }

                //????????????
                String rule = getStrings(shopProperty.getActivityRule());
                int index = rule.indexOf("'");
                int lastIndex = rule.lastIndexOf("'");
                if (index != -1 && index != lastIndex) {
                    tv_product_activity_description.setText(getSpannableString(rule.replaceAll("'", ""), index, lastIndex - 1, 1.6f, ""));
                } else {
                    tv_product_activity_description.setText(getStrings(shopProperty.getActivityRule()));
                }
            } else {
                ll_product_activity_detail.setVisibility(GONE);
                mLlDepositDiscountInfo.setVisibility(GONE);
                mLlScroolDetailPrice.setVisibility(VISIBLE);
                mLlVipPrice.setVisibility(VISIBLE);
                mLlTimeHoursBottom.setVisibility(GONE);
            }
        }

        //?????????????????????
        presentProductInfoBeans.clear();
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
                rv_shop_details_text_communal.setVisibility(GONE);
            }
        }

        //????????????????????????
        if ((!TextUtils.isEmpty(shopProperty.getActivityTag()) && !TextUtils.isEmpty(shopProperty.getActivityCode())) ||
                (shopPropertyBean.getMarketLabelList() != null && shopProperty.getMarketLabelList().size() > 0) ||
                !TextUtils.isEmpty(shopProperty.getNewUserTag()) || shopProperty.isHasPresent() || shopProperty.isVipActivity()) {
            fbl_details_market_label.setVisibility(VISIBLE);
            fbl_details_market_label.removeAllViews();
            //??????????????????
            if (shopProperty.isVipActivity()) {
                fbl_details_market_label.addView(ProductLabelCreateUtils.createLabelText(this, "", 2));
            }

            //????????????????????????
            if (!TextUtils.isEmpty(shopProperty.getNewUserTag())) {
                MarketLabelBean marketLabelBean = new MarketLabelBean();
                marketLabelBean.setTitle(shopProperty.getNewUserTag());
                marketLabelBean.setLabelCode(1);
                marketLabelBean.setNewUserTag(true);
                fbl_details_market_label.addView(ProductLabelCreateUtils.createLabelClickText(getActivity(), marketLabelBean));
            }

            //??????????????????
            if (!TextUtils.isEmpty(shopProperty.getActivityTag())) {
                MarketLabelBean marketLabelBean = new MarketLabelBean();
                marketLabelBean.setTitle(shopProperty.getActivityTag());
                marketLabelBean.setActivityCode(getStrings(shopPropertyBean.getActivityCode()));
                marketLabelBean.setLabelCode(1);
                fbl_details_market_label.addView(ProductLabelCreateUtils.createLabelClickText(getActivity(), marketLabelBean));
            }

            //??????????????????
            ShopPropertyBean.MZPresentInfoBean mzPresentInfo = shopProperty.getMzPresentInfo();
            if (mzPresentInfo != null && !shopProperty.getActivityCode().contains("MZ")) {
                MarketLabelBean marketLabelBean = new MarketLabelBean();
                marketLabelBean.setTitle(mzPresentInfo.getActivityTag());
                marketLabelBean.setActivityCode(getStrings(mzPresentInfo.getActivityCode()));
                marketLabelBean.setLabelCode(1);
                fbl_details_market_label.addView(ProductLabelCreateUtils.createLabelClickText(getActivity(), marketLabelBean));
            }

            //??????????????????
            if (shopProperty.isHasPresent()) {
                MarketLabelBean marketLabelBean = new MarketLabelBean();
                marketLabelBean.setTitle("??????");
                marketLabelBean.setLabelCode(1);
                fbl_details_market_label.addView(ProductLabelCreateUtils.createLabelClickText(getActivity(), marketLabelBean));
            }

            //??????????????????
            if (shopPropertyBean.getMarketLabelList() != null && shopProperty.getMarketLabelList().size() > 0) {
                for (MarketLabelBean marketLabelBean : shopPropertyBean.getMarketLabelList()) {
                    marketLabelBean.setLabelCode(0);
                    fbl_details_market_label.addView(ProductLabelCreateUtils.createLabelClickText(getActivity(), marketLabelBean));
                }
            }
        } else {
            fbl_details_market_label.setVisibility(GONE);
        }

        //?????????
        mTvTaxText.setText(getStringsFormat(this, R.string.tax_text, shopProperty.getTaxText()));
        mLlTaxText.setVisibility(!TextUtils.isEmpty(shopProperty.getTaxText()) ? VISIBLE : GONE);

        //???????????????????????????
        mLlShippingProvince.setVisibility(!TextUtils.isEmpty(shopProperty.getShippingProvince()) ? VISIBLE : GONE);
        mTvShippingProvince.setText(getStrings(shopProperty.getShippingProvince()));
        mTvShipping.setText("1".equals(shopProperty.getEcmFlag()) ? "??????????????????" : "?????????");
        mTvPreSaleDeliveryTime.setVisibility(!TextUtils.isEmpty(shopProperty.getPreSaleDeliveryTime()) ? VISIBLE : GONE);
        mTvPreSaleDeliveryTime.setText(getStringsFormat(this, R.string.preSaleDeliveryTime, shopProperty.getPreSaleDeliveryTime()));
        mRlPriceAndProvince.setVisibility(fbl_details_market_label.getVisibility() == GONE && mLlShippingProvince.getVisibility() == GONE ? GONE : VISIBLE);

        //??????????????????SKU
        setSkuProp(shopProperty);

        //???????????????
        if (!TextUtils.isEmpty(shopPropertyBean.getActivityPrice())
                && shopDetailsEntity.getShopPropertyBean() != null
                && shopDetailsEntity.getShopPropertyBean().getActivityStartTime() != null
                && !isEndOrStartTime(shopDetailsEntity.getCurrentTime(), shopDetailsEntity.getShopPropertyBean().getActivityStartTime())) {
            String priceCHN = getStringsChNPrice(this, shopPropertyBean.getActivityPrice());
            String priceDes = (shopProperty.getActivityCode().contains("YS") ? "?????????" : "?????????") + getStringsFormat(this, R.string.text_act_not_start, shopProperty.getActivityPrice());
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
            String marketPrice = shopProperty.getMarketPrice();
            tv_ql_sp_pro_sc_market_price.setText(String.format(getString(R.string.money_market_price_chn), marketPrice));
            tv_ql_sp_pro_sc_market_price.setVisibility(getStringChangeDouble(marketPrice) > 0 ? VISIBLE : View.INVISIBLE);
        }

        //??????????????????
        String productName = TextUtils.isEmpty(shopProperty.getSubtitle()) ? shopProperty.getName() : (shopProperty.getSubtitle() + "???" + shopProperty.getName());
        String ecmTag = shopProperty.getEcmTag();
        //????????????
        if (!TextUtils.isEmpty(ecmTag)) {
            Link link = new Link("\t" + ecmTag + "\t");
            link.setTextColor(Color.parseColor("#ffffff"));
            link.setTextSize(AutoSizeUtils.mm2px(mAppContext, 24));
            link.setBgColor(Color.parseColor("#ffb20b"));
            link.setBgRadius(AutoSizeUtils.mm2px(this, 5));
            link.setUnderlined(false);
            link.setHighlightAlpha(0f);
            CharSequence charSequence = LinkBuilder.from(this, link.getText() + "\t" + productName)
                    .addLink(link)
                    .build();
            tv_ql_sp_pro_sc_name.setText(charSequence);
        } else {
            tv_ql_sp_pro_sc_name.setText(productName);
        }

        // ????????????
        List<String> buyReasonList = shopProperty.getBuyReason();
        if (buyReasonList != null && buyReasonList.size() > 0) {
            mFlexProductPoint.removeAllViews();
            for (int i = 0; i < buyReasonList.size(); i++) {
                String tagName = buyReasonList.get(i);
                if (!TextUtils.isEmpty(tagName)) {
                    TextView textView = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.layout_buy_before_tag, null, false);
                    textView.setText(getStrings(tagName));
                    mFlexProductPoint.addView(textView);
                }
            }
        }
        mFlexProductPoint.setVisibility(mFlexProductPoint.getChildCount() > 0 ? VISIBLE : GONE);

        //??????????????????
        setSeviceTag(shopProperty, ll_layout_pro_sc_tag, flex_product_tag);

        //??????????????????
        List<Map<String, String>> newPreSaleInfo = shopProperty.getNewPreSaleInfo();
        if (newPreSaleInfo != null && newPreSaleInfo.size() > 0) {
            flex_buy_before.removeAllViews();
            for (int i = 0; i < newPreSaleInfo.size(); i++) {
                Map<String, String> map = newPreSaleInfo.get(i);
                String color = map.get("color");
                String tagName = map.get("text");
                if (!TextUtils.isEmpty(tagName)) {
                    TextView textView = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.layout_buy_before_tag, null, false);
                    textView.setText(getStrings(tagName));
                    if (!TextUtils.isEmpty(color)) {
                        textView.setTextColor(Color.parseColor(color));
                    }
                    flex_buy_before.addView(textView);
                }
            }
        }
        ll_pro_buy_before.setVisibility(flex_buy_before.getChildCount() > 0 ? VISIBLE : GONE);

        //????????????
        Properties prop = new Properties();
        prop.setProperty("proName", getStrings(shopProperty.getName()));
        prop.setProperty("proId", shopDetailsEntity.getShopPropertyBean().getId() + "");
        StatService.trackCustomKVEvent(getActivity(), "qlProLook", prop);
        tv_sp_details_collect.setSelected(shopProperty.isCollect());
        List<CommunalDetailObjectBean> detailsDataList = CommunalWebDetailUtils.getCommunalWebInstance().getWebDetailsFormatDataList(shopProperty.getItemBody());
        CommunalDetailObjectBean communalDetailObjectBean = new CommunalDetailObjectBean();
        communalDetailObjectBean.setItemType(TYPE_PRODUCT_TITLE);
        communalDetailObjectBean.setContent("????????????");
        shopDetailBeanList.add(communalDetailObjectBean);
        shopDetailBeanList.addAll(detailsDataList);
        if (serviceDataList.size() > 0) {
            shopDetailBeanList.addAll(serviceDataList);
        }
        communalDetailAdapter.notifyDataSetChanged();
        ctb_qt_pro_details.setCurrentTab(0);
        scroll_pro.scrollTo(0, 0);
    }

    //???????????????????????????????????????????????????
    private String appendEnd(List<String> prices) {
        return prices != null && prices.size() > 1 && !prices.get(0).equals(prices.get(prices.size() - 1)) ? "???" : "";
    }

    private String getMinPrice(List<String> prices) {
        return prices != null && prices.size() > 0 ? prices.get(0) : "";
    }

    private void setTopTimeVisible(boolean topTimeVisible) {
        if (!topTimeVisible) {
            ll_top_end_time.setVisibility(GONE);//????????????????????????
            ll_top_start_time.setVisibility(GONE);//????????????????????????
        } else {
            //?????????????????????????????????
            if (isEndOrStartTime(shopDetailsEntity.getCurrentTime(), shopDetailsEntity.getShopPropertyBean().getActivityStartTime())) {
                ll_top_end_time.setVisibility(VISIBLE);
                ll_top_start_time.setVisibility(GONE);
            } else {
                //?????????????????????????????????
                ll_top_end_time.setVisibility(GONE);
                ll_top_start_time.setVisibility(VISIBLE);
            }
        }

    }

    //??????????????????
    private void setSeviceTag(ShopPropertyBean shopProperty, ViewGroup viewGroup, FlexboxLayout flexboxLayout) {
        try {
            List<TagsBean> tags = shopProperty.getTags();
            String tagIds = shopProperty.getTagIds();
            shopProperty.setTagIds(tagIds);
            if (tags != null && tags.size() > 0 && !TextUtils.isEmpty(tagIds) && tagIds.split(",").length > 0) {
                final Map<Integer, String> tagMap = new HashMap<>();
                for (TagsBean tagsBean : shopProperty.getTags()) {
                    tagMap.put(tagsBean.getId(), getStrings(tagsBean.getName()));
                }
                final String[] tagSelected = shopProperty.getTagIds().split(",");
                flexboxLayout.removeAllViews();
                int tagLength = 0;
                for (String aTagSelected : tagSelected) {
                    String tagName = tagMap.get(Integer.parseInt(aTagSelected));
                    if (!TextUtils.isEmpty(tagName)) {
                        tagLength = tagLength + tagName.length();
                        TextView textView = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.layout_product_tag, null, false);
                        textView.setLines(1);
                        textView.setText(getStrings(tagName));
                        flexboxLayout.addView(textView);
                    }
                }
                mIvMoreTag.setVisibility(tagLength > 20 ? VISIBLE : GONE);
            }

            viewGroup.setVisibility(flexboxLayout.getChildCount() > 0 ? VISIBLE : GONE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //?????????????????????
    private void startActivityDownTime(CountdownView countDownTimeView, boolean isMJ) {
        try {
            String activityStartTime = shopPropertyBean.getActivityCode().contains("YS") ? shopPropertyBean.getDepositStartTime() : shopPropertyBean.getActivityStartTime();
            String activityEndTime = shopPropertyBean.getActivityCode().contains("YS") ? shopPropertyBean.getDepositEndTime() : shopPropertyBean.getActivityEndTime();
            String currentTime = shopDetailsEntity.getCurrentTime();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
            long dateStart = formatter.parse(activityStartTime).getTime();
            long dateEnd = formatter.parse(activityEndTime).getTime();
            long dateCurret = !TextUtils.isEmpty(currentTime) ? formatter.parse(currentTime).getTime() : System.currentTimeMillis();
            //???????????????
            if (!isEndOrStartTime(shopDetailsEntity.getCurrentTime(), shopPropertyBean.getActivityStartTime())) {
                mTvTipsBottom.setText("?????????");
                if (mCountDownEndTimer != null) {
                    mCountDownEndTimer.cancel();
                }
                if (mCountDownStartTimer == null) {
                    mCountDownStartTimer = new CountDownTimer(this) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            if (isMJ) {
                                countDownTimeView.updateShow(millisUntilFinished);
                            } else {
                                tv_start_time.setText(String.format("?????????%s", getTimeDifferenceText(millisUntilFinished)));
                            }
                        }

                        @Override
                        public void onFinish() {
                            //???????????????
                            countDownTimeView.updateShow(0);
                            loadData();
                        }
                    };
                }

                mCountDownStartTimer.setMillisInFuture(dateStart - dateCurret);
                mCountDownStartTimer.start();
            } else if (!isEndOrStartTime(currentTime, activityEndTime)) {
                //????????????????????????
                mTvTipsBottom.setText("?????????");
                if (mCountDownStartTimer != null) {
                    mCountDownStartTimer.cancel();
                }
                if (mCountDownEndTimer == null) {
                    mCountDownEndTimer = new CountDownTimer(this) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            countDownTimeView.updateShow(millisUntilFinished);
                        }

                        @Override
                        public void onFinish() {
                            //???????????????
                            countDownTimeView.updateShow(0);
                            loadData();
                        }
                    };
                }
                mCountDownEndTimer.setMillisInFuture(dateEnd - dateCurret);
                mCountDownEndTimer.start();
            } else {
                //???????????????
                ll_product_activity_detail.setVisibility(GONE);
                mLlTimeHoursBottom.setVisibility(GONE);
            }
        } catch (Exception e) {
            ll_product_activity_detail.setVisibility(GONE);
            mLlTimeHoursBottom.setVisibility(GONE);
        }
    }

    //???????????????????????????
    private void startProductDownTime() {
        try {
            tv_pro_time_detail_status.setText("?????????");
            String startTime = shopPropertyBean.getStartTime();
            String currentTime = shopDetailsEntity.getCurrentTime();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
            long dateStart = formatter.parse(startTime).getTime();
            long dateCurret = !TextUtils.isEmpty(currentTime) ? formatter.parse(currentTime).getTime() : System.currentTimeMillis();
            if (mCountDownForSale == null) {
                mCountDownForSale = new CountDownTimer(this) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        ct_pro_show_time_detail.updateShow(millisUntilFinished);
                    }

                    @Override
                    public void onFinish() {
                        //???????????????
                        loadData();
                    }
                };
            }
            mCountDownForSale.setMillisInFuture(dateStart - dateCurret);
            mCountDownForSale.start();
        } catch (ParseException e) {
            rel_shop_pro_time.setVisibility(GONE);
        }
    }

    private void setReplenishmentNotice() {
        SkuSaleBean skuSaleBean = shopPropertyBean.getSkuSale().get(0);
        if (shopPropertyBean.getSkuSale().size() == 1 && (skuSaleBean.getIsNotice() == 1 || skuSaleBean.getIsNotice() == 2)) {
            tv_sp_details_add_car.setEnabled(true);
            tv_sp_details_add_car.setSelected(true);
            skuSaleBeanId = skuSaleBean.getId();
            tv_sp_details_add_car.setText(skuSaleBean.getIsNotice() == 1 ? "????????????" : "????????????");
        } else {
            tv_sp_details_add_car.setEnabled(false);
        }
    }


    //????????????
    private void getServiceData(String productId) {
        String url = Url.Q_SP_DETAIL_SERVICE_COMMITMENT;
        Map<String, Object> params = new HashMap<>();
        params.put("productId", productId);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, url, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                mDirectGoodsServerEntity = GsonUtils.fromJson(result, DirectGoodsServerEntity.class);
                if (mDirectGoodsServerEntity != null) {
                    if (mDirectGoodsServerEntity.getCode().equals(SUCCESS_CODE)) {
                        serviceDataList.clear();
                        DirectGoodsServerBean directGoodsServerBean = mDirectGoodsServerEntity.getDirectGoodsServerBean();
                        if (directGoodsServerBean != null) {
                            List<DirectGoodsServerBean.ServicePromiseBean> servicePromiseList = directGoodsServerBean.getServicePromiseList();
                            if (servicePromiseList.size() > 0) {
                                CommunalDetailObjectBean communalDetailObjectBean = new CommunalDetailObjectBean();
                                communalDetailObjectBean.setItemType(TYPE_PRODUCT_TITLE);
                                communalDetailObjectBean.setContent("????????????");
                                serviceDataList.add(communalDetailObjectBean);
                                for (int i = 0; i < servicePromiseList.size(); i++) {
                                    communalDetailObjectBean = new CommunalDetailObjectBean();
                                    if (i == 0) {
                                        communalDetailObjectBean.setFirstLinePadding(true);
                                    }
                                    DirectGoodsServerBean.ServicePromiseBean servicePromiseBean = servicePromiseList.get(i);
                                    communalDetailObjectBean.setContent(servicePromiseBean.getContent());
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


    private void setSkuProp(ShopPropertyBean shopProperty) {
        if (shopProperty.getSkuSale() != null && shopProperty.getSkuSale().size() > 0) {
            //????????????
            tv_ql_sp_pro_sku.setTextColor(getResources().getColor(R.color.color_blue_reply_com));
            tv_ql_sp_pro_sku.setText(getString(R.string.sel_pro_sku, getStrings(shopProperty.getProps().get(0).getPropName())));
            List<SkuSaleBean> skuSaleList = shopProperty.getSkuSale();
            if (skuSaleList.size() == 1) {
                //????????????SKU
                ll_sp_pro_sku_value.setVisibility(GONE);
                shopCarGoodsSkuDif = new ShopCarGoodsSku();
                shopCarGoodsSkuDif.setCount(1);
                shopCarGoodsSkuDif.setSaleSkuId(shopProperty.getSkuSale().get(0).getId());
                shopCarGoodsSkuDif.setProductId(shopProperty.getId());
                shopCarGoodsSkuDif.setValuesName(
                        !TextUtils.isEmpty(shopProperty.getPropvalues().get(0).getPropValueName())
                                ? shopProperty.getPropvalues().get(0).getPropValueName() : "??????");
            } else if (skuSaleList.size() > 1) {//??????sku
                ll_sp_pro_sku_value.setVisibility(VISIBLE);
                String skuText = shopProperty.getShowSkuText();
                if (!TextUtils.isEmpty(skuText)) {
                    tv_ql_sp_pro_sku.setTextColor(getResources().getColor(R.color.text_black_t));
                    tv_ql_sp_pro_sku.setText(skuText);
                }

                int selectSkuId = shopProperty.getSelectSkuId();
                if (selectSkuId > 0) {
                    tv_ql_sp_pro_sku.setText("?????????" + skuText);
                    shopCarGoodsSkuDif = new ShopCarGoodsSku();
                    shopCarGoodsSkuDif.setCount(1);
                    shopCarGoodsSkuDif.setSaleSkuId(selectSkuId);
                    shopCarGoodsSkuDif.setProductId(shopProperty.getId());
                    shopCarGoodsSkuDif.setValuesName(!TextUtils.isEmpty(skuText) ? skuText : "??????");
                } else {
                    shopCarGoodsSkuDif = null;
                }

                //?????????sku????????????
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
                if ("??????".equals(getStrings(shopProperty.getSellStatus()))) {
                    editGoodsSkuBean.setSellStatus(true);
                } else {
                    editGoodsSkuBean.setSellStatus(false);
                }
                editGoodsSkuBean.setMaxDiscounts(getStrings(shopProperty.getMaxDiscounts()));
                skuDialog = new SkuDialog(getActivity());
                skuDialog.setDismissListener(shopCarGoodsSku -> {
                    if (shopCarGoodsSku != null) {
                        shopCarGoodsSkuDif = shopCarGoodsSku;
                        tv_ql_sp_pro_sku.setTextColor(getResources().getColor(R.color.text_black_t));
                        tv_ql_sp_pro_sku.setText(("?????????" + shopCarGoodsSkuDif.getValuesName()));
                        //???????????????sku
                        userSelectProductSku(shopCarGoodsSkuDif.getSaleSkuId());
                    }
                });
                skuDialog.refreshView(editGoodsSkuBean);
                //??????????????????sku
                if (selectSkuId > 0) {
                    for (int i = 0; i < skuSaleList.size(); i++) {
                        SkuSaleBean skuSaleBean = skuSaleList.get(i);
                        if (selectSkuId == skuSaleBean.getId()) {
                            skuDialog.selectedHistorySku(skuSaleBean.getPropValues().split(","));
                        }
                    }
                }
            }

            //??????????????????
            mLlOpenVip.setVisibility(showVipEnter() ? VISIBLE : GONE);
            String openVipTips = !TextUtils.isEmpty(shopProperty.getVipReduce()) ?
                    getStringsFormat(this, R.string.open_vip_save_money, shopProperty.getVipReduce()) : "????????????????????????????????????";
            mTvVipSave.setText(openVipTips.contains("???") ? getSpannableString(openVipTips, openVipTips.indexOf("???") + 1, openVipTips.length(), -1, "#ff0015") : openVipTips);
            String vipPrice = getVipPrice(skuSaleList);
            String normalPrice = getNormalPrice(skuSaleList);
            //????????????????????????????????????????????????????????????????????????????????????
            mIvTopVipLogo.setVisibility(isVip() && !TextUtils.isEmpty(vipPrice) ? VISIBLE : GONE);
            String depositPrefix = !TextUtils.isEmpty(shopProperty.getActivityCode()) && shopProperty.getActivityCode().contains("YS")
                    && isEndOrStartTime(shopDetailsEntity.getCurrentTime(), shopPropertyBean.getDepositStartTime()) ? "?????????" : "";//???????????????
            String topPrice = depositPrefix + (isVip() && !TextUtils.isEmpty(vipPrice) ? vipPrice : normalPrice);
            SpannableString spannableString = new SpannableString(topPrice);
            RelativeSizeSpan minSizeSpan = new RelativeSizeSpan(0.64f);
            spannableString.setSpan(minSizeSpan, topPrice.indexOf("??"), topPrice.indexOf("??") + 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            if (topPrice.contains("~")) {//?????????????????????
                RelativeSizeSpan maxSizeSpan = new RelativeSizeSpan(0.64f);
                spannableString.setSpan(maxSizeSpan, topPrice.lastIndexOf("??"), topPrice.lastIndexOf("??") + 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            }
            tv_ql_sp_pro_sc_price.setText(spannableString);
            //????????????????????????????????????????????????????????????,?????????????????????
            mIvBottomVipLogo.setVisibility(!isVip() && !TextUtils.isEmpty(vipPrice) ? VISIBLE : GONE);
            String bottomPrice = isVip() && !TextUtils.isEmpty(vipPrice) ? "????????? " + normalPrice : vipPrice;
            mTvVipPrice.setText(bottomPrice);
            mLlVipPrice.setVisibility(!TextUtils.isEmpty(bottomPrice) && mLlVipPrice.getVisibility() == VISIBLE ? VISIBLE : GONE);
        } else {
            showToast("??????????????????");
        }
    }

    //???????????????sku
    private void userSelectProductSku(int selectSkuId) {
        Map<String, Object> map = new HashMap<>();
        map.put("skuId", selectSkuId);
        map.put("productId", productId);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, Url.SELECT_PRODUCT_SKU, map, null);
    }

    //???????????????
    private String getNormalPrice(List<SkuSaleBean> skuSaleList) {
        String minPrice = "??" + skuSaleList.get(0).getPrice();
        String maxPrice = "??" + skuSaleList.get(skuSaleList.size() - 1).getPrice();
        return minPrice + (minPrice.equals(maxPrice) ? "" : "~" + maxPrice);
    }

    //???????????????
    private String getVipPrice(List<SkuSaleBean> skuSaleList) {
        //???????????????
        List<SkuSaleBean> newSkuSaleList = new ArrayList<>();
        for (SkuSaleBean skuSaleBean : skuSaleList) {
            if (!TextUtils.isEmpty(skuSaleBean.getVipPrice())) {
                newSkuSaleList.add(skuSaleBean);
            }
        }

        //???????????????
        Collections.sort(newSkuSaleList, (lhs, rhs) -> {
            if (!TextUtils.isEmpty(lhs.getPrice()) && !TextUtils.isEmpty(rhs.getPrice())) {
                float p1 = getStringChangeFloat(lhs.getVipPrice());
                float p2 = getStringChangeFloat(rhs.getVipPrice());
                return Float.compare(p1, p2);
            } else {
                return 0;
            }
        });

        if (newSkuSaleList.size() > 0) {
            String minVipPrice = newSkuSaleList.get(0).getVipPrice();
            String maxVipPrice = newSkuSaleList.get(newSkuSaleList.size() - 1).getVipPrice();
            return "????????? " + getRmbFormat(this, minVipPrice) + (!minVipPrice.equals(maxVipPrice) ? ("~??" + maxVipPrice) : "");
        } else {
            return "";
        }
    }

    //    ????????????
    private void skipServiceDataInfo() {
        QyProductIndentInfo qyProductIndentInfo = null;
        if (shopPropertyBean != null) {
            qyProductIndentInfo = new QyProductIndentInfo();
            qyProductIndentInfo.setUrl(sharePageUrl + productId);
            qyProductIndentInfo.setTitle(getStrings(shopPropertyBean.getSubtitle()));
            qyProductIndentInfo.setPicUrl(shopPropertyBean.getPicUrl());
            qyProductIndentInfo.setDesc(getStrings(shopPropertyBean.getName()));
            qyProductIndentInfo.setNote("??" + shopPropertyBean.getPrice());
        }
        QyServiceUtils.getQyInstance().openQyServiceChat(this, "??????????????????", sharePageUrl + productId, qyProductIndentInfo);
    }

    //???????????????
    private void addGoodsToCar() {
        if (shopCarGoodsSkuDif != null) {
//          ???????????????
            tv_sp_details_add_car.setEnabled(false);
            //?????????????????????
            String url = Url.Q_SHOP_DETAILS_ADD_CAR;
            Map<String, Object> params = new HashMap<>();
            params.put("userId", userId);
            params.put("productId", shopDetailsEntity.getShopPropertyBean().getId());
            params.put("saleSkuId", shopCarGoodsSkuDif.getSaleSkuId());
            params.put("count", shopCarGoodsSkuDif.getCount());
            NetLoadUtils.getNetInstance().loadNetDataPost(this, url, params, new NetLoadListenerHelper() {
                @Override
                public void onSuccess(String result) {
                    Properties prop = new Properties();
                    prop.setProperty("proName", getStrings(shopPropertyBean.getName()));
                    prop.setProperty("proId", shopPropertyBean.getId() + "");
                    prop.setProperty("proCount", shopCarGoodsSkuDif.getCount() + "");
                    prop.setProperty("proSalSku", getStrings(shopCarGoodsSkuDif.getValuesName()));
                    StatService.trackCustomKVEvent(getActivity(), "addProToCar", prop);

                    RequestStatus status = GsonUtils.fromJson(result, RequestStatus.class);
                    if (status != null) {
                        if (status.getCode().equals(SUCCESS_CODE)) {
                            showToast(getString(R.string.AddCarSuccess));
                            //???????????????????????????
                            getCarCount(getActivity());
                            if (skuDialog != null) {
                                shopCarGoodsSkuDif = null;
                            }
                        } else {
                            showToastRequestMsg(status);
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
                skuDialog.show(true, "??????");
                skuDialog.getGoodsSKu(new SkuDialog.DataListener() {
                    @Override
                    public void getSkuProperty(ShopCarGoodsSku shopCarGoodsSku) {
                        shopCarGoodsSkuDif = shopCarGoodsSku;
                        if (shopCarGoodsSkuDif != null) {
                            tv_ql_sp_pro_sku.setTextColor(getResources().getColor(R.color.text_black_t));
                            tv_ql_sp_pro_sku.setText(("?????????" + shopCarGoodsSkuDif.getValuesName()));
                            addCarCheckLoginStatus();
                        }
                    }
                });
            }
        }
    }

    //????????????
    private void buyGoIt() {
        //?????????SKU
        if (shopCarGoodsSkuDif != null) {
            //???????????????????????????????????????????????????????????????
            if (shopPropertyBean.isDepositFlag()) {
                List<IndentProDiscountBean> discountBeanList = new ArrayList<>();
                IndentProDiscountBean indentProBean = new IndentProDiscountBean();
                indentProBean.setId(shopPropertyBean.getId());
                indentProBean.setSaleSkuId(shopCarGoodsSkuDif.getSaleSkuId());
                indentProBean.setCount(1);
                discountBeanList.add(indentProBean);
                Intent intent = new Intent(getActivity(), DirectDepositWriteActivity.class);
                intent.putExtra("goods", GsonUtils.toJson(discountBeanList));
                startActivity(intent);
            } else {
                List<CartInfoBean> cartInfoBeans = new ArrayList<>();
                CartInfoBean cartInfoBean = new CartInfoBean();
                cartInfoBean.setProductId(shopPropertyBean.getId());
                cartInfoBean.setCount(shopCarGoodsSkuDif.getCount());
                cartInfoBean.setId(0);//????????????????????????????????????id
                cartInfoBean.setSaleSku(new SkuSaleBean(shopPropertyBean.getQuantity(), shopCarGoodsSkuDif.getPrice() + "", shopCarGoodsSkuDif.getSaleSkuId()));
                cartInfoBeans.add(cartInfoBean);
                //????????????
                Properties prop = new Properties();
                prop.setProperty("proName", getStrings(shopPropertyBean.getName()));
                prop.setProperty("proId", shopPropertyBean.getId() + "");
                prop.setProperty("proCount", shopCarGoodsSkuDif.getCount() + "");
                prop.setProperty("proSalSku", getStrings(shopCarGoodsSkuDif.getValuesName()));
                StatService.trackCustomKVEvent(getActivity(), "qlProBuy", prop);
                //???????????? ??????????????????
                Bundle bundle = new Bundle();
                bundle.putString("goods", GsonUtils.toJson(cartInfoBeans));
                ConstantMethod.skipIndentWrite(getActivity(), INDENT_W_TYPE, bundle);
            }
        } else {
            if (skuDialog != null) {
                skuDialog.show(true, "??????");
                skuDialog.getGoodsSKu(new SkuDialog.DataListener() {
                    @Override
                    public void getSkuProperty(ShopCarGoodsSku shopCarGoodsSku) {
                        shopCarGoodsSkuDif = shopCarGoodsSku;
                        if (shopCarGoodsSkuDif != null) {
                            tv_ql_sp_pro_sku.setTextColor(getResources().getColor(R.color.text_black_t));
                            tv_ql_sp_pro_sku.setText(("?????????" + shopCarGoodsSkuDif.getValuesName()));
                            buyGoItCheckStatus();
                        }
                    }
                });
            }
        }
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

    @OnClick({R.id.ll_back, R.id.ll_back2, R.id.ll_service, R.id.ll_service2, R.id.ll_share, R.id.ll_share2, R.id.ll_product_activity_detail, R.id.tv_sp_details_service,
            R.id.tv_sp_details_add_car, R.id.tv_sp_details_buy_it, R.id.tv_sp_details_collect, R.id.tv_group_product, R.id.iv_ql_shop_pro_cp_tag, R.id.tv_ql_sp_pro_sku,
            R.id.ll_layout_pro_sc_tag, R.id.tv_shop_comment_more, R.id.ll_tax_txt, R.id.ll_open_vip, R.id.ll_deposit_pay, R.id.iv_close_float, R.id.cv_video_float, R.id.ll_ask_question, R.id.ll_ask})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.ll_back:
            case R.id.ll_back2:
                finish();
                break;
            case R.id.ll_open_vip:
                intent = new Intent(this, OpenVipActivity.class);
                startActivity(intent);
                break;
            //??????????????????
            case R.id.ll_product_activity_detail:
                if (shopPropertyBean != null && !TextUtils.isEmpty(shopPropertyBean.getActivityCode())) {
                    intent = new Intent(this, QualityProductActActivity.class);
                    intent.putExtra("activityCode", getStrings(shopPropertyBean.getActivityCode()));
                    startActivity(intent);
                }
                break;
            //??????
            case R.id.tv_sp_details_service:
                skipServiceDataInfo();
                break;
            //???????????????
            case R.id.tv_sp_details_add_car:
                addCarCheckLoginStatus();
                break;
            //????????????
            case R.id.tv_sp_details_buy_it:
            case R.id.ll_deposit_pay://????????????
                buyGoItCheckStatus();
                break;
            //????????????
            case R.id.tv_sp_details_collect:
                if (userId > 0) {
                    if (shopPropertyBean != null) {
                        SoftApiDao.collectGoods(this, shopPropertyBean.getId(), tv_sp_details_collect);
                    }
                } else {
                    getLoginStatus(getActivity());
                }
                break;
            //???????????????
            case R.id.ll_service:
            case R.id.ll_service2:
                intent = new Intent(getActivity(), ShopCarActivity.class);
                startActivity(intent);
                break;
            //??????????????????
            case R.id.tv_shop_comment_more:
                if (shopPropertyBean != null && shopPropertyBean.getId() > 0) {
                    skipMoreEvaluate();
                }
                break;
            //??????
            case R.id.ll_share:
            case R.id.ll_share2:
                if (shopPropertyBean != null) {
                    String title = shopPropertyBean.getName();
                    String activityCode = shopPropertyBean.getActivityCode();
                    if (!TextUtils.isEmpty(activityCode)) {
                        if (activityCode.contains("XSG")) {
                            //???????????????
                            List<SkuSaleBean> skuSale = shopPropertyBean.getSkuSale();
                            if (skuSale != null && skuSale.size() > 0) {
                                String price = skuSale.get(0).getPrice();
                                title = getStringsFormat(getActivity(), R.string.limited_time_preference, price) + shopPropertyBean.getName();
                            }
                        } else if (activityCode.contains("LJ")) {
                            //????????????
                            title = TextUtils.isEmpty(shopPropertyBean.getSubtitle()) ? "???????????????????????????" + shopPropertyBean.getName()
                                    : getStringsFormat(getActivity(), R.string.bracket, shopPropertyBean.getActivityRule()) + shopPropertyBean.getName();
                        }
                    } else {
                        //????????????
                        title = TextUtils.isEmpty(shopPropertyBean.getSubtitle()) ? "???????????????????????????" + shopPropertyBean.getName()
                                : shopPropertyBean.getSubtitle() + "???" + shopPropertyBean.getName();
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
            //????????????
            case R.id.tv_group_product:
                if (shopPropertyBean != null
                        && shopPropertyBean.getGpInfoId() > 0) {
                    intent = new Intent(getActivity(), QualityGroupShopDetailActivity.class);
                    intent.putExtra("gpInfoId", String.valueOf(shopPropertyBean.getGpInfoId()));
                    intent.putExtra("productId", productId);
                    startActivity(intent);
                }
                break;
            case R.id.iv_ql_shop_pro_cp_tag:
                showToast("????????????????????????????????????");
                break;
            //sku????????????
            case R.id.tv_ql_sp_pro_sku:
                if (skuDialog != null) {
                    skuDialog.show(false, isWaitSellStatus, "???????????????");
                    skuDialog.getGoodsSKu(shopCarGoodsSku -> {
                        shopCarGoodsSkuDif = shopCarGoodsSku;
                        if (shopCarGoodsSkuDif != null) {
                            tv_ql_sp_pro_sku.setTextColor(getResources().getColor(R.color.text_black_t));
                            tv_ql_sp_pro_sku.setText(("?????????" + shopCarGoodsSkuDif.getValuesName()));
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
                            tv_ql_sp_pro_sku.setTextColor(getResources().getColor(R.color.color_blue_reply_com));
                            tv_ql_sp_pro_sku.setText(String.format(getResources().getString(R.string.sel_pro_sku)
                                    , getStrings(shopPropertyBean.getProps().get(0).getPropName())));
                        }
                    });
                }
                break;
            //??????????????????
            case R.id.ll_layout_pro_sc_tag:
                if (mDirectGoodsServerEntity != null && mIvMoreTag.getVisibility() == VISIBLE) {
                    if (alertDialog == null) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomTransDialog);
                        View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_sevicetag_dialog, communal_recycler_wrap, false);
                        FlexboxLayout flex_communal_tag = dialogView.findViewById(R.id.flex_communal_tag);
                        setSeviceTag(shopPropertyBean, flex_communal_tag, flex_communal_tag);
                        alertDialog = builder.create();
                        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        alertDialog.setCanceledOnTouchOutside(true);
                        alertDialog.show();
                        Window window = alertDialog.getWindow();
                        window.getDecorView().setPadding(0, 0, 0, 0);
                        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        window.setGravity(Gravity.BOTTOM);//????????????
                        window.setContentView(dialogView);
                    } else {
                        alertDialog.show();
                    }
                }
                break;
            //????????????
            case R.id.ll_tax_txt:
                if (mAlertDialogTax == null) {
                    mAlertDialogTax = new AlertDialogTax(this, shopPropertyBean);
                }
                mAlertDialogTax.show(Gravity.BOTTOM);
                break;
            //??????????????????
            case R.id.iv_close_float:
                mLlVideoFloat.setVisibility(GONE);
                break;
            //??????????????????
            case R.id.cv_video_float:
                if (shopPropertyBean != null) {
                    Intent intent1 = new Intent(this, JzVideoFullScreenActivity.class);
                    intent1.putExtra("url", shopPropertyBean.getVideoUrl());
                    intent1.putExtra("cover", shopPropertyBean.getPicUrl());
                    startActivity(intent1);
                }
                break;
            //????????????
            case R.id.ll_ask_question:
            case R.id.ll_ask:
                Intent intent2 = new Intent(this, QuestionListActivity.class);
                intent2.putExtra("productId", productId);
                startActivity(intent2);
                break;
        }
    }

    //??????????????????
    private void skipMoreEvaluate() {
        Intent intent = new Intent(getActivity(), DirectProductEvaluationActivity.class);
        intent.putExtra("productId", productId);
        intent.putExtra("productName", shopPropertyBean.getName());
        intent.putExtra("cover", shopPropertyBean.getPicUrl());
        startActivity(intent);
    }


    private void getDirectCoupon(int id) {
        if (userId > 0) {
            CommunalWebDetailUtils.getCommunalWebInstance().getDirectCoupon(this, id, loadHud);
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


    //??????????????????
    private void addCancelNotice() {
        String url = Url.Q_REPLENISHMENT_NOTICE;
        Map<String, Object> params = new HashMap<>();
        params.put("uid", userId);
        params.put("skuId", skuSaleBeanId);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, url, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                RequestStatus requestStatus = GsonUtils.fromJson(result, RequestStatus.class);
                if (requestStatus != null) {
                    if (requestStatus.getCode().equals(SUCCESS_CODE)) {
                        tv_sp_details_add_car.setText(requestStatus.getIsNotice() == 1 ? "????????????" : "????????????");
                        showToast(requestStatus.getIsNotice() == 1 ? "???????????????" : "???????????????");
                    } else {
                        showToastRequestMsg(requestStatus);
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

    //??????????????????????????????
    private boolean showVipEnter() {
        boolean isWhiteUser = (boolean) SharedPreUtils.getParam("isWhiteUser", false);
        return !isVip() && userId > 0 && isWhiteUser;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mLlOpenVip.setVisibility(showVipEnter() ? VISIBLE : GONE);
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
    protected void postEventResult(@NonNull EventMessage message) {
        if (message.type.equals(ConstantVariable.UPDATE_CAR_NUM)) {
            if (badge != null) {
                badge.setBadgeNumber((int) message.result);
            }
        }
    }
}
