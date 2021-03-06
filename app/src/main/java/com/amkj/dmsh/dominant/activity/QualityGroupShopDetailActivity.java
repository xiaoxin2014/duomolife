package com.amkj.dmsh.dominant.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.amkj.dmsh.bean.CommunalDetailBean;
import com.amkj.dmsh.bean.RequestStatus;
import com.amkj.dmsh.bean.TabEntity;
import com.amkj.dmsh.constant.CommunalAdHolderView;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.ConstantVariable;
import com.amkj.dmsh.constant.UMShareAction;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.dao.GroupDao;
import com.amkj.dmsh.dominant.adapter.JoinGroupAdapter;
import com.amkj.dmsh.dominant.bean.GroupShopDetailsEntity;
import com.amkj.dmsh.dominant.bean.GroupShopDetailsEntity.GroupShopDetailsBean;
import com.amkj.dmsh.dominant.bean.GroupShopDetailsEntity.GroupShopDetailsBean.ParticipantInfoBean.GroupShopJoinBean;
import com.amkj.dmsh.dominant.bean.QualityGroupEntity;
import com.amkj.dmsh.dominant.bean.QualityGroupEntity.QualityGroupBean;
import com.amkj.dmsh.homepage.activity.ArticleOfficialActivity;
import com.amkj.dmsh.homepage.adapter.CommunalDetailAdapter;
import com.amkj.dmsh.homepage.bean.CommunalADActivityEntity.CommunalADActivityBean;
import com.amkj.dmsh.mine.activity.ShopCarActivity;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.shopdetails.activity.ShopScrollDetailsActivity;
import com.amkj.dmsh.shopdetails.adapter.DirectEvaluationAdapter;
import com.amkj.dmsh.shopdetails.adapter.GoodsRecommendAdapter;
import com.amkj.dmsh.shopdetails.adapter.GroupRecommendAdapter;
import com.amkj.dmsh.shopdetails.bean.CommunalDetailObjectBean;
import com.amkj.dmsh.shopdetails.bean.EditGoodsSkuEntity;
import com.amkj.dmsh.shopdetails.bean.GoodsCommentEntity;
import com.amkj.dmsh.shopdetails.bean.GoodsCommentEntity.GoodsCommentBean;
import com.amkj.dmsh.shopdetails.bean.ShopRecommendHotTopicEntity;
import com.amkj.dmsh.shopdetails.bean.ShopRecommendHotTopicEntity.ShopRecommendHotTopicBean;
import com.amkj.dmsh.shopdetails.bean.SkuSaleBean;
import com.amkj.dmsh.utils.CountDownTimer;
import com.amkj.dmsh.utils.ProductLabelCreateUtils;
import com.amkj.dmsh.utils.TimeUtils;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.amkj.dmsh.utils.gson.GsonUtils;
import com.amkj.dmsh.utils.webformatdata.CommunalWebDetailUtils;
import com.amkj.dmsh.views.bottomdialog.SkuDialog;
import com.amkj.dmsh.views.convenientbanner.ConvenientBanner;
import com.amkj.dmsh.views.convenientbanner.holder.CBViewHolderCreator;
import com.amkj.dmsh.views.convenientbanner.holder.Holder;
import com.amkj.dmsh.views.flycoTablayout.CommonTabLayout;
import com.amkj.dmsh.views.flycoTablayout.listener.CustomTabEntity;
import com.amkj.dmsh.views.flycoTablayout.listener.OnTabSelectListener;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.flexbox.JustifyContent;
import com.gyf.barlibrary.ImmersionBar;
import com.melnykov.fab.FloatingActionButton;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.tencent.bugly.beta.tinker.TinkerManager;
import com.umeng.socialize.UMShareAPI;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;
import me.jessyan.autosize.utils.AutoSizeUtils;
import q.rorbin.badgeview.Badge;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.getBadge;
import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.getSpannableString;
import static com.amkj.dmsh.constant.ConstantMethod.getStringChangeFloat;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.getStringsChNPrice;
import static com.amkj.dmsh.constant.ConstantMethod.getStringsFormat;
import static com.amkj.dmsh.constant.ConstantMethod.showImageActivity;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.showToastRequestMsg;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.INDENT_GROUP_SHOP;
import static com.amkj.dmsh.constant.ConstantVariable.JOIN_GROUP;
import static com.amkj.dmsh.constant.ConstantVariable.OPEN_GROUP;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.TOTAL_COUNT_TWENTY;
import static com.amkj.dmsh.constant.ConstantVariable.TYPE_2;
import static com.amkj.dmsh.constant.Url.GROUP_SHOP_JOIN_NEW_INDEX;
import static com.amkj.dmsh.constant.Url.GROUP_SHOP_JOIN_NRE_USER;
import static com.amkj.dmsh.constant.Url.GROUP_SHOP_NEW_DETAILS;
import static com.amkj.dmsh.constant.Url.SHOP_EVA_LIKE;
import static com.amkj.dmsh.dao.OrderDao.getCarCount;
import static com.amkj.dmsh.find.activity.ImagePagerActivity.IMAGE_DEF;
import static com.amkj.dmsh.shopdetails.bean.CommunalDetailObjectBean.TYPE_PRODUCT_TITLE;
import static com.amkj.dmsh.utils.TimeUtils.getTimeDifference;
import static com.amkj.dmsh.utils.TimeUtils.getTimeDifferenceText;
import static com.amkj.dmsh.utils.TimeUtils.isEndOrStartTime;
import static com.amkj.dmsh.utils.glide.GlideImageLoaderUtil.getSquareImgUrl;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/6/6
 * class description:????????????
 */
public class QualityGroupShopDetailActivity extends BaseActivity {
    @BindView(R.id.smart_refresh_ql_sp_details)
    SmartRefreshLayout smart_refresh_ql_sp_details;
    @BindView(R.id.banner_ql_gp_sp_details)
    ConvenientBanner banner_ql_sp_pro_details;
    @BindView(R.id.tv_ql_gp_sp_new_detail)
    TextView tv_ql_gp_sp_new_detail;
    //            ????????????
    @BindView(R.id.tv_ql_sp_pro_name)
    TextView tv_ql_sp_pro_name;
    //            ????????????
    @BindView(R.id.tv_gp_sp_per_price)
    TextView tv_gp_sp_per_price;
    //    ?????????
    @BindView(R.id.tv_gp_sp_nor_price)
    TextView tv_gp_sp_nor_price;
    //    ????????????
    @BindView(R.id.ll_group_buy)
    LinearLayout ll_group_buy;
    //    ?????????
    @BindView(R.id.tv_sp_details_join_buy_price)
    TextView tv_sp_details_join_buy_price;
    //    ????????????
    @BindView(R.id.tv_sp_details_join_count)
    TextView tv_sp_details_join_count;
    //    ????????????
    @BindView(R.id.ll_alone_buy)
    LinearLayout ll_alone_buy;
    //    ??????????????????
    @BindView(R.id.tv_sp_details_ol_buy_price)
    TextView tv_sp_details_ol_buy_price;
    //    ??????????????????
    @BindView(R.id.tv_sp_details_ol_buy)
    TextView tv_sp_details_ol_buy;
    @BindView(R.id.fl_group_product)
    FrameLayout fl_group_product;
    @BindView(R.id.download_btn_communal)
    FloatingActionButton download_btn_communal;
    @BindView(R.id.nsv_gp_detail)
    NestedScrollView scroll_pro;
    @BindView(R.id.ll_gp_detail_bottom)
    LinearLayout ll_gp_detail_bottom;
    @BindView(R.id.rv_group_join)
    RecyclerView rv_group_join;
    @BindView(R.id.tv_partner_join)
    TextView tv_partner_join;
    @BindView(R.id.ll_group_join)
    LinearLayout ll_group_join;
    @BindView(R.id.rv_comment)
    RecyclerView rv_comment;
    @BindView(R.id.tv_shop_comment_count)
    TextView tv_shop_comment_count;
    @BindView(R.id.rel_pro_comment)
    RelativeLayout rel_pro_comment;
    @BindView(R.id.ll_comment)
    LinearLayout ll_comment;
    @BindView(R.id.rv_group_rule)
    RecyclerView rv_group_rule;
    @BindView(R.id.iv_artical_cover)
    ImageView mIvArticalCover;
    @BindView(R.id.tv_artical_title)
    TextView mTvArticalTitle;
    @BindView(R.id.tv_artical_desc)
    TextView mTvArticalDesc;
    @BindView(R.id.ll_artical)
    LinearLayout mLlArtical;
    @BindView(R.id.flex_product_tag)
    FlexboxLayout flex_product_tag;
    @BindView(R.id.iv_more_tag)
    ImageView mIvMoreTag;
    @BindView(R.id.ll_layout_pro_sc_tag)
    LinearLayout ll_layout_pro_sc_tag;
    @BindView(R.id.rv_product_details)
    RecyclerView rv_product_details;
    @BindView(R.id.rl_toolbar)
    RelativeLayout mRlToolbar;
    @BindView(R.id.rl_toolbar2)
    RelativeLayout mRlToolbar2;
    @BindView(R.id.ll_group_detail_bottom)
    LinearLayout mLlGroupDetailBottom;
    @BindView(R.id.ll_ll_group_detail_bottom2)
    LinearLayout mLlGroupDetailBottom2;
    @BindView(R.id.ll_sp_pro_sku_value)
    LinearLayout ll_sp_pro_sku_value;
    @BindView(R.id.tv_ql_sp_pro_sku)
    TextView tv_ql_sp_pro_sku;
    @BindView(R.id.ctb_qt_pro_details)
    CommonTabLayout ctb_qt_pro_details;
    @BindView(R.id.ll_pro_layout)
    LinearLayout ll_pro_layout;
    @BindView(R.id.tv_group_home)
    TextView mTvGroupHome;
    @BindView(R.id.tv_invate)
    TextView mTvInvate;
    @BindView(R.id.rv_goods_recommend)
    RecyclerView mRvGoodsRecommend;
    @BindView(R.id.rv_hot_sale)
    RecyclerView mRvHotSale;
    @BindView(R.id.ll_product_recommend)
    LinearLayout mllProductRecommend;
    @BindView(R.id.flex_lottery_list)
    FlexboxLayout mFlexLottery;
    @BindView(R.id.tv_all_lottery)
    TextView mTvAllLottery;
    @BindView(R.id.ll_lottery_list)
    LinearLayout mLlLotteryList;
    @BindView(R.id.ll_custom_zone)
    LinearLayout mLlCustomZone;
    @BindView(R.id.ll_open_group_info)
    LinearLayout mLlOpenGroupInfo;
    @BindView(R.id.iv_lottery_zone)
    ImageView mIvLotteryZone;
    @BindView(R.id.view_divider_zone)
    View mViewDividerZone;
    @BindView(R.id.fl_header_service)
    RelativeLayout fl_header_service;
    @BindView(R.id.tv_free_postage)
    TextView mTvFreePostage;
    @BindView(R.id.ll_top_start_time)
    LinearLayout mLlStartTime;
    @BindView(R.id.tv_start_time)
    TextView mTvStartTime;
    @BindView(R.id.flex_bug_before)
    FlexboxLayout flex_buy_before;
    @BindView(R.id.ll_pro_buy_before)
    LinearLayout ll_pro_buy_before;


    //????????????
    private List<GroupShopJoinBean> groupShopJoinList = new ArrayList<>();
    //????????????
    private List<CommunalDetailObjectBean> gpRuleList = new ArrayList<>();
    //??????????????????
    private List<CommunalADActivityBean> imagesVideoList = new ArrayList<>();
    //????????????
    private List<GoodsCommentBean> goodsComments = new ArrayList<>();
    //??????????????????
    private List<ShopRecommendHotTopicBean> articalRecommendList = new ArrayList<>();
    //??????????????????
    private List<QualityGroupBean> goodsRecommendList = new ArrayList<>();
    //???????????? ???????????? ??????
    private List<CommunalDetailObjectBean> shopDetailBeanList = new ArrayList<>();
    //?????????????????????
    private List<ShopRecommendHotTopicBean> hotsaleList = new ArrayList<>();
    private JoinGroupAdapter joinGroupAdapter;
    private DirectEvaluationAdapter directEvaluationAdapter;
    private CommunalDetailAdapter gpRuleDetailsAdapter;
    private CommunalDetailAdapter communalDetailAdapter;
    private GroupRecommendAdapter mGroupRecommendAdapter;
    private GroupShopDetailsEntity mGroupShopDetailsEntity;
    private CBViewHolderCreator cbViewHolderCreator;
    private String gpInfoId;
    private String productId;
    private String gpRecordId;
    private int screenHeight;
    private int screenWith;
    private int statusBarHeight;
    private int measuredHeight;
    private SkuDialog skuDialog;
    private AlertDialog alertDialog;
    private GroupShopDetailsBean mGroupShopDetailsBean;
    private String[] detailTabData = {"??????", "??????"};
    private ArrayList<CustomTabEntity> tabData = new ArrayList<>();
    private GoodsRecommendAdapter mGoodsRecommendAdapter;
    private Badge badge;
    private CountDownTimer mCountDownEndTimer;


    @Override
    protected int getContentView() {
        return R.layout.activity_ql_gp_sp_details;
    }

    @Override
    public void setStatusBar() {
        ImmersionBar.with(this).keyboardEnable(true).navigationBarEnable(false).statusBarDarkFont(true).fullScreen(true).init();
    }

    @Override
    protected void initViews() {
        //?????????????????????padding
        statusBarHeight = ImmersionBar.getStatusBarHeight(this);
        int paddingTop = statusBarHeight > 0 ? statusBarHeight : AutoSizeUtils.mm2px(this, 40);
        mRlToolbar.setPadding(0, paddingTop, 0, 0);
        mRlToolbar2.setPadding(0, paddingTop, 0, 0);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) fl_header_service.getLayoutParams();
        layoutParams.topMargin = paddingTop;
        fl_header_service.setLayoutParams(layoutParams);
//        --------------------------------
        badge = getBadge(getActivity(), fl_header_service);
        tv_gp_sp_nor_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        tv_gp_sp_nor_price.getPaint().setAntiAlias(true);
        Intent intent = getIntent();
        gpInfoId = intent.getStringExtra("gpInfoId");
        gpRecordId = intent.getStringExtra("gpRecordId");
        TinkerBaseApplicationLike app = (TinkerBaseApplicationLike) TinkerManager.getTinkerApplicationLike();
        screenHeight = app.getScreenHeight();
        screenWith = app.getScreenWidth();
        ll_gp_detail_bottom.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                ll_gp_detail_bottom.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int measuredHeight = ll_gp_detail_bottom.getMeasuredHeight();
                ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) download_btn_communal.getLayoutParams();
                int bottomMargin = marginLayoutParams.bottomMargin;
                int topMargin = marginLayoutParams.topMargin;
                int leftMargin = marginLayoutParams.leftMargin;
                int rightMargin = marginLayoutParams.rightMargin;
                marginLayoutParams.setMargins(leftMargin, topMargin, rightMargin, bottomMargin + measuredHeight);
                download_btn_communal.setLayoutParams(marginLayoutParams);
            }
        });
        smart_refresh_ql_sp_details.setOnRefreshListener(refreshLayout -> loadData());
        //?????????????????????
        rv_group_join.setLayoutManager(new LinearLayoutManager(QualityGroupShopDetailActivity.this));
        joinGroupAdapter = new JoinGroupAdapter(QualityGroupShopDetailActivity.this, groupShopJoinList);
        rv_group_join.setAdapter(joinGroupAdapter);
        joinGroupAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            if (view.getId() == R.id.tv_ql_gp_sp_join) {
                GroupShopJoinBean groupShopJoinBean = (GroupShopJoinBean) view.getTag();
                if (groupShopJoinBean != null && mGroupShopDetailsBean != null) {
                    if (mGroupShopDetailsBean.getGpQuantity() > 0) {
                        isCanJoinGroup(groupShopJoinBean);
                    } else {
                        showToast("????????????,?????????????????????");
                    }
                }
            }
        });

        //?????????????????????
        mRvHotSale.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mGoodsRecommendAdapter = new GoodsRecommendAdapter(this, hotsaleList, false);
        mRvHotSale.setAdapter(mGoodsRecommendAdapter);
        mGoodsRecommendAdapter.setOnItemClickListener((adapter, view, position) -> {
            ShopRecommendHotTopicBean shopRecommendHotTopicBean = (ShopRecommendHotTopicBean) view.getTag();
            if (shopRecommendHotTopicBean != null) {
                ConstantMethod.skipProductUrl(getActivity(), shopRecommendHotTopicBean.getType_id(), shopRecommendHotTopicBean.getId());
            }
        });

        //?????????????????????
        rv_group_rule.setNestedScrollingEnabled(false);
        gpRuleDetailsAdapter = new CommunalDetailAdapter(this, gpRuleList);
        rv_group_rule.setLayoutManager(new LinearLayoutManager(this));
        rv_group_rule.setAdapter(gpRuleDetailsAdapter);

        //?????????????????????
        rv_comment.setLayoutManager(new LinearLayoutManager(this));
        directEvaluationAdapter = new DirectEvaluationAdapter(this, goodsComments);
        rv_comment.setNestedScrollingEnabled(false);
        rv_comment.setAdapter(directEvaluationAdapter);
        directEvaluationAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.tv_eva_count:
                        GoodsCommentBean goodsCommentBean = (GoodsCommentBean) view.getTag();
                        if (goodsCommentBean != null && !goodsCommentBean.isFavor()) {
                            if (userId > 0) {
                                setProductEvaLike(view);
                            } else {
                                getLoginStatus(QualityGroupShopDetailActivity.this);
                            }
                        }
                        break;
                }
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

        //???????????????????????????
        mRvGoodsRecommend.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mGroupRecommendAdapter = new GroupRecommendAdapter(this, goodsRecommendList);
        mRvGoodsRecommend.setAdapter(mGroupRecommendAdapter);
        mGroupRecommendAdapter.setOnItemClickListener((adapter, view, position) -> {
            QualityGroupBean qualityGroupBean = (QualityGroupBean) view.getTag();
            if (qualityGroupBean != null) {
                Intent intent2 = new Intent(getActivity(), QualityGroupShopDetailActivity.class);
                intent2.putExtra("gpInfoId", String.valueOf(qualityGroupBean.getGpInfoId()));
                intent2.putExtra("productId", qualityGroupBean.getProductId());
                startActivity(intent2);
                finish();
            }
        });

        //?????????????????????
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rv_product_details.setLayoutManager(linearLayoutManager);
        rv_product_details.setNestedScrollingEnabled(false);
        communalDetailAdapter = new CommunalDetailAdapter(getActivity(), shopDetailBeanList);
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
        scroll_pro.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView nestedScrollView, int i, int scrollY, int i2, int oldY) {
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
                float alpha = scrollY * 1.0f / screenHeight * 1.0f;
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
        download_btn_communal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scroll_pro.scrollTo(0, 0);
                scroll_pro.fling(0);
                download_btn_communal.hide(false);
            }
        });
    }

    private void setChangeMeasureHeight() {
        //???????????????????????????????????????????????????????????????
        int height = ll_pro_layout.getMeasuredHeight() - statusBarHeight;
        measuredHeight = height > 0 ? height : ll_pro_layout.getMeasuredHeight();
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
        //??????????????????
        getGroupShopDetails();
        getCarCount(getActivity());
    }

    @Override
    public View getLoadView() {
        return fl_group_product;
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    private void getGroupShopDetails() {
        Map<String, Object> params = new HashMap<>();
        params.put("gpInfoId", gpInfoId);
        if (!TextUtils.isEmpty(gpRecordId)) {
            params.put("gpRecordId", gpRecordId);
        }
        NetLoadUtils.getNetInstance().loadNetDataPost(QualityGroupShopDetailActivity.this, GROUP_SHOP_NEW_DETAILS,
                params, new NetLoadListenerHelper() {
                    @Override
                    public void onSuccess(String result) {
                        smart_refresh_ql_sp_details.finishRefresh();
                        mGroupShopDetailsEntity = GsonUtils.fromJson(result, GroupShopDetailsEntity.class);
                        if (mGroupShopDetailsEntity != null) {
                            mGroupShopDetailsBean = mGroupShopDetailsEntity.getGroupShopDetailsBean();
                            if (mGroupShopDetailsEntity.getCode().equals(SUCCESS_CODE)) {
                                if (mGroupShopDetailsBean != null) {
                                    setGroupShopDetailsData(mGroupShopDetailsBean);
                                    setProductData(mGroupShopDetailsBean);
                                    //??????????????????
                                    getComment();
                                    //??????????????????
                                    getArticalRecommend();
                                    //????????????????????????
                                    getGoodsRecommend();
                                }
                            } else {
                                showToast(mGroupShopDetailsEntity.getMsg());
                            }
                        }
                        NetLoadUtils.getNetInstance().showLoadSir(loadService, mGroupShopDetailsBean, mGroupShopDetailsEntity);
                    }

                    @Override
                    public void onNotNetOrException() {
                        smart_refresh_ql_sp_details.finishRefresh();
                        NetLoadUtils.getNetInstance().showLoadSir(loadService, mGroupShopDetailsBean, mGroupShopDetailsEntity);
                    }
                });
    }

    //  ??????????????????
    private void getComment() {
        Map<String, Object> params = new HashMap<>();
        params.put("currentPage", 1);
        params.put("showCount", 1);
        params.put("id", productId);
        if (userId > 0) {
            params.put("uid", userId);
        }
        NetLoadUtils.getNetInstance().loadNetDataPost(this, Url.Q_SHOP_DETAILS_COMMENT, params, new NetLoadListenerHelper() {
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
                ll_comment.setVisibility(goodsComments.size() > 0 ? VISIBLE : GONE);
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
        Map<String, Object> params = new HashMap<>();
        params.put("id", goodsCommentBean.getId());
        params.put("uid", userId);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, SHOP_EVA_LIKE, params, null);
        goodsCommentBean.setFavor(!goodsCommentBean.isFavor());
        tv_eva_like.setSelected(!tv_eva_like.isSelected());
        tv_eva_like.setText(ConstantMethod.getNumCount(tv_eva_like.isSelected(), goodsCommentBean.isFavor(), goodsCommentBean.getLikeNum(), "???"));
    }

    private void setProductData(final GroupShopDetailsBean groupShopDetailsBean) {
        //???????????????????????????????????????
        imagesVideoList.clear();
        List<String> imageList = groupShopDetailsBean.getPicUrlList();
        if (!TextUtils.isEmpty(groupShopDetailsBean.getVideoUrl())) {
            imagesVideoList.add(new CommunalADActivityBean("", groupShopDetailsBean.getVideoUrl()));
        }
        for (int i = 0; i < imageList.size(); i++) {
            imagesVideoList.add(new CommunalADActivityBean(getSquareImgUrl(imageList.get(i), screenWith, ""), ""));
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
                CommunalADActivityBean communalADActivityBean = imagesVideoList.get(position);
                if (!TextUtils.isEmpty(communalADActivityBean.getVideoUrl())) {
                    return;
                }

                showImageActivity(getActivity(), IMAGE_DEF,
                        position - (!TextUtils.isEmpty(groupShopDetailsBean.getVideoUrl()) ? 1 : 0),
                        imageList);
            }
        });

        //??????????????????
        setSeviceTag(groupShopDetailsBean.getTagText(), ll_layout_pro_sc_tag, flex_product_tag);

        //????????????
        List<Map<String, String>> newPreSaleInfo = groupShopDetailsBean.getPreSaleInfo();
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
        shopDetailBeanList.clear();
        List<CommunalDetailBean> itemBody = groupShopDetailsBean.getItemBody();
        if (itemBody != null && itemBody.size() > 0) {
            CommunalDetailObjectBean communalDetailObjectBean = new CommunalDetailObjectBean();
            communalDetailObjectBean.setItemType(TYPE_PRODUCT_TITLE);
            communalDetailObjectBean.setContent("????????????");
            shopDetailBeanList.add(communalDetailObjectBean);
            shopDetailBeanList.addAll(CommunalWebDetailUtils.getCommunalWebInstance().getWebDetailsFormatDataList(groupShopDetailsBean.getItemBody()));
        }

        //????????????
        List<CommunalDetailBean> servicePromise = groupShopDetailsBean.getServicePromise();
        if (servicePromise != null && servicePromise.size() > 0) {
            CommunalDetailObjectBean detailObjectBean = new CommunalDetailObjectBean();
            detailObjectBean.setItemType(TYPE_PRODUCT_TITLE);
            detailObjectBean.setContent("????????????");
            detailObjectBean.setFirstLinePadding(true);
            shopDetailBeanList.add(detailObjectBean);
            shopDetailBeanList.addAll(CommunalWebDetailUtils.getCommunalWebInstance().getWebDetailsFormatDataList(servicePromise));
        }
        communalDetailAdapter.notifyDataSetChanged();

        //??????sku
        mGroupShopDetailsBean.setGpSkuId(0);
        setSkuProp(groupShopDetailsBean);
    }

    private void setSkuProp(GroupShopDetailsBean shopProperty) {
        List<SkuSaleBean> skuSaleList = shopProperty.getSkuSale();
        if (skuSaleList != null && skuSaleList.size() > 0) {
            for (SkuSaleBean skuSaleBean : skuSaleList) {
                skuSaleBean.setNewUserTag("?????????");
            }
            //????????????
            tv_ql_sp_pro_sku.setTextColor(getResources().getColor(R.color.color_blue_reply_com));
            tv_ql_sp_pro_sku.setText(getString(R.string.sel_pro_sku, getStrings(shopProperty.getProps().get(0).getPropName())));
            //?????????SKU
            if (skuSaleList.size() > 1) {
                ll_sp_pro_sku_value.setVisibility(VISIBLE);
                EditGoodsSkuEntity.EditGoodsSkuBean editGoodsSkuBean = new EditGoodsSkuEntity.EditGoodsSkuBean();
                editGoodsSkuBean.setQuantity(shopProperty.getGpQuantity());
                editGoodsSkuBean.setId(shopProperty.getProductId());
                editGoodsSkuBean.setPicUrl(shopProperty.getCoverImage());
                editGoodsSkuBean.setProps(shopProperty.getProps());
                editGoodsSkuBean.setPropvalues(shopProperty.getPropvalues());
                editGoodsSkuBean.setProductName(shopProperty.getProductName());
                editGoodsSkuBean.setSkuSale(shopProperty.getSkuSale());
                editGoodsSkuBean.setCombine(true);
                skuDialog = new SkuDialog(getActivity());
                skuDialog.setDismissListener(shopCarGoodsSku -> {
                    if (shopCarGoodsSku != null) {
                        mGroupShopDetailsBean.setGpSkuId(shopCarGoodsSku.getSaleSkuId());
                        tv_ql_sp_pro_sku.setTextColor(getResources().getColor(R.color.text_black_t));
                        tv_ql_sp_pro_sku.setText(("?????????" + shopCarGoodsSku.getValuesName()));
                    }
                });
                skuDialog.refreshView(editGoodsSkuBean);
            } else {
                //????????????SKU
                ll_sp_pro_sku_value.setVisibility(GONE);
                mGroupShopDetailsBean.setGpSkuId(shopProperty.getSkuSale().get(0).getId());
            }
        }

        //????????????????????????????????????
        mViewDividerZone.setVisibility((skuSaleList == null || skuSaleList.size() <= 1) && groupShopJoinList.size() == 0 ? GONE : VISIBLE);
    }

    private void setGroupShopDetailsData(GroupShopDetailsBean groupShopDetailsBean) {
        gpRecordId = groupShopDetailsBean.getGpRecordId();
        productId = String.valueOf(groupShopDetailsBean.getProductId());
        //??????????????????
        String gpName = groupShopDetailsBean.getGpName();
        String productName = groupShopDetailsBean.getProductName();
        String subTitle = groupShopDetailsBean.getSubTitle();
        tv_ql_sp_pro_name.setText(getStrings(!TextUtils.isEmpty(gpName) ? gpName : (TextUtils.isEmpty(subTitle) ? productName : (subTitle + "???" + productName))));
        //?????????
        String count = groupShopDetailsBean.getRequireCount() + "???" + (groupShopDetailsBean.isLotteryGroup() ? "????????? ??" : "????????? ??");
        String price = getStrings(groupShopDetailsBean.getGpPrice());
        String priceText = count + price + getSuffix();
        tv_gp_sp_per_price.setText(getSpannableString(priceText, priceText.indexOf(price), priceText.indexOf(price) + price.length(), 1.6f, ""));
        //??????
        tv_gp_sp_nor_price.setText("?????? ??" + groupShopDetailsBean.getPrice());
        //???????????????
        tv_sp_details_ol_buy_price.setText(getStringsChNPrice(this, groupShopDetailsBean.getPrice()));

        //????????????
        tv_partner_join.setText(getStrings(groupShopDetailsBean.getTipText()));
        tv_partner_join.setVisibility(!TextUtils.isEmpty(this.gpRecordId) || groupShopDetailsBean.isLotteryGroup() ? GONE : VISIBLE);

        //??????????????????
        groupShopJoinList.clear();
        if (TextUtils.isEmpty(gpRecordId)) {
            mLlGroupDetailBottom.setVisibility(VISIBLE);
            mLlGroupDetailBottom2.setVisibility(GONE);
            ll_alone_buy.setVisibility(mGroupShopDetailsBean.isLotteryGroup() ? GONE : VISIBLE);//????????????????????????????????????
            List<GroupShopJoinBean> joinBeanList = groupShopDetailsBean.getRecordList();
            if (joinBeanList != null && joinBeanList.size() > 0) {
                for (int i = 0; i < joinBeanList.size(); i++) {
                    GroupShopJoinBean groupShopJoinBean = joinBeanList.get(i);
                    groupShopJoinBean.setCurrentTime(mGroupShopDetailsEntity.getCurrentTime());
                    groupShopJoinList.add(groupShopJoinBean);
                }
            }

            mLlStartTime.setVisibility(GONE);
            if (isEndOrStartTime(TimeUtils.getCurrentTime(mGroupShopDetailsEntity), mGroupShopDetailsBean.getGpEndTime())) {
                ll_group_buy.setEnabled(false);
                tv_sp_details_join_buy_price.setText("?????????");
                tv_sp_details_join_count.setText("????????????");
            } else if (!isEndOrStartTime(TimeUtils.getCurrentTime(mGroupShopDetailsEntity), mGroupShopDetailsBean.getGpStartTime())) {
                ll_group_buy.setEnabled(false);
                tv_sp_details_join_buy_price.setText("?????????");
                tv_sp_details_join_count.setText("????????????");
                mLlStartTime.setVisibility(VISIBLE);
                if (mCountDownEndTimer == null) {
                    mCountDownEndTimer = new CountDownTimer(this) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            mTvStartTime.setText(String.format("?????????%s", getTimeDifferenceText(millisUntilFinished)));
                        }

                        @Override
                        public void onFinish() {
                            //???????????????
                            mLlStartTime.setVisibility(GONE);
                            groupEnable();
                        }
                    };
                }
                mCountDownEndTimer.setMillisInFuture(getTimeDifference(mGroupShopDetailsBean.getGpStartTime(), mGroupShopDetailsEntity.getCurrentTime()));
                mCountDownEndTimer.start();
            } else if (groupShopDetailsBean.getGpQuantity() <= 0 || mGroupShopDetailsBean.getGpCreateCount() >= mGroupShopDetailsBean.getGpMaxCreateCount()) {
                ll_group_buy.setEnabled(false);
                tv_sp_details_join_buy_price.setText("????????????");
                tv_sp_details_join_count.setText("????????????");
            } else {
                groupEnable();
            }
        } else {
            mLlGroupDetailBottom.setVisibility(GONE);
            mLlGroupDetailBottom2.setVisibility(VISIBLE);
        }
        mTvInvate.setEnabled(mGroupShopDetailsBean.isBtUsable());

        //?????????????????????
        GroupShopDetailsBean.ParticipantInfoBean participantInfo = groupShopDetailsBean.getParticipantInfo();
        if (participantInfo != null) {
            List<GroupShopJoinBean> userInfoList = participantInfo.getUserInfoList();
            String endTime = participantInfo.getEndTime();
            String statusText = participantInfo.getStatusText();
            if (userInfoList != null && userInfoList.size() > 0) {
                GroupShopJoinBean groupShopJoinBean = new GroupShopJoinBean();
                List<GroupShopJoinBean> memberListBeans = new ArrayList<>(userInfoList);
                int leftParticipant = groupShopDetailsBean.getRequireCount() - userInfoList.size();
                for (int i = 0; i < leftParticipant; i++) {
                    GroupShopJoinBean memberListBean = new GroupShopJoinBean();
                    memberListBean.setAvatar("android.resource://com.amkj.dmsh/drawable/" + R.drawable.who);
                    memberListBeans.add(memberListBean);
                }
                groupShopJoinBean.setItemType(TYPE_2);
                groupShopJoinBean.setMemberListBeans(memberListBeans);
                groupShopJoinBean.setEndTime(endTime);
                groupShopJoinBean.setCurrentTime(mGroupShopDetailsEntity.getCurrentTime());
                //????????????????????????
                if (TextUtils.isEmpty(statusText)) {
                    groupShopJoinBean.setDownTime(true);
                } else {
                    groupShopJoinBean.setGroupStatus(statusText);
                }
                groupShopJoinList.add(groupShopJoinBean);
            }
        }
        joinGroupAdapter.setNewData(groupShopJoinList);
        ll_group_join.setVisibility(groupShopJoinList.size() > 0 ? VISIBLE : GONE);

        //??????????????????
        List<GroupShopJoinBean> luckUserList = groupShopDetailsBean.getLuckUserList();
        if (luckUserList != null && luckUserList.size() > 0) {
            mTvAllLottery.setVisibility(luckUserList.size() > 10 ? VISIBLE : GONE);
            mFlexLottery.setJustifyContent(JustifyContent.CENTER);
            mFlexLottery.removeAllViews();
            for (int i = 0; i < (luckUserList.size() > 10 ? 10 : luckUserList.size()); i++) {
                mFlexLottery.addView(ProductLabelCreateUtils
                        .createOpenGroupUserInfo(this, luckUserList.get(i)));
            }
            mLlLotteryList.setVisibility(VISIBLE);
        } else {
            mLlLotteryList.setVisibility(GONE);
        }

        //?????????-????????????
        if (groupShopDetailsBean.isLotteryGroup()) {
            mLlCustomZone.setVisibility(VISIBLE);
            GlideImageLoaderUtil.loadRoundImg(this, mIvLotteryZone, "http://domolifes.oss-cn-beijing.aliyuncs.com/wechatIcon/lottery-zone-cover.jpg", AutoSizeUtils.mm2px(this, 20));
            getHotSale();
        } else {
            mLlCustomZone.setVisibility(GONE);
        }

        //????????????
        gpRuleList.clear();
        List<CommunalDetailBean> gpRule = groupShopDetailsBean.getGpRule();
        if (gpRule != null && gpRule.size() > 0) {
            gpRuleList.addAll(CommunalWebDetailUtils.getCommunalWebInstance().getWebDetailsFormatDataList(gpRule));
        }
        gpRuleDetailsAdapter.setNewData(gpRuleList);
        mLlOpenGroupInfo.setVisibility(groupShopJoinList.size() > 0 || mFlexLottery.getChildCount() > 0 ? VISIBLE : GONE);
    }

    //???????????????????????????
    private String getSuffix() {
        List<SkuSaleBean> skuSale = mGroupShopDetailsBean.getSkuSale();
        if (skuSale != null && skuSale.size() > 0) {
            List<SkuSaleBean> skuSaleList = new ArrayList<>(skuSale);
            Collections.sort(skuSaleList, (lhs, rhs) -> Float.compare(getStringChangeFloat(lhs.getPrice()), getStringChangeFloat(rhs.getPrice())));
            return skuSaleList.get(0).getPrice().equals(skuSaleList.get(skuSaleList.size() - 1).getPrice()) ? "" : "???";
        } else {
            return "";
        }
    }

    //?????????????????????
    private void groupEnable() {
        ll_group_buy.setEnabled(true);
        tv_sp_details_join_count.setText("????????????");
        if (mGroupShopDetailsBean.isLotteryGroup()) {
            tv_sp_details_join_buy_price.setText(getStringsFormat(this, R.string.lottery_price, mGroupShopDetailsBean.getGpPrice()));
            tv_sp_details_join_count.setVisibility(GONE);
        } else {
            tv_sp_details_join_buy_price.setText(getStringsChNPrice(this, mGroupShopDetailsBean.getGpPrice()));
            tv_sp_details_join_count.setVisibility(VISIBLE);
        }
    }

    //??????????????????
    private void getHotSale() {
        String url = Url.QUALITY_HOT_SALE_LIST_NEW;
        Map<String, Object> params = new HashMap<>();
        params.put("day", 1);
        params.put("showCount", TOTAL_COUNT_TWENTY);
        params.put("currentPage", 1);
        if (userId > 0) {
            params.put("uid", userId);
        }
        NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(), url
                , params, new NetLoadListenerHelper() {
                    @Override
                    public void onSuccess(String result) {
                        hotsaleList.clear();
                        ShopRecommendHotTopicEntity recommendHotTopicEntity = ShopRecommendHotTopicEntity.objectFromData(result);
                        if (recommendHotTopicEntity != null) {
                            List<ShopRecommendHotTopicBean> hotTopicList = recommendHotTopicEntity.getShopRecommendHotTopicList();
                            if (recommendHotTopicEntity.getCode().equals(SUCCESS_CODE)) {
                                if (hotTopicList != null && hotTopicList.size() > 0) {
                                    hotsaleList.addAll(hotTopicList.subList(0, hotTopicList.size() > 20 ? 20 : hotTopicList.size()));
                                }
                                mllProductRecommend.setVisibility(goodsRecommendList.size() > 0 ? VISIBLE : GONE);
                            }
                        }
                        mGoodsRecommendAdapter.notifyDataSetChanged();
                    }
                });
    }

    //??????????????????
    private void getArticalRecommend() {
        String url = Url.Q_SP_DETAIL_TOPIC_RECOMMEND;
        Map<String, Object> params = new HashMap<>();
        params.put("id", productId);
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

    //??????????????????
    private void getGoodsRecommend() {
        Map<String, Object> params = new HashMap<>();
        params.put("currentPage", 1);
        params.put("showCount", TOTAL_COUNT_TWENTY);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, GROUP_SHOP_JOIN_NEW_INDEX, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                QualityGroupEntity qualityGroupEntity = GsonUtils.fromJson(result, QualityGroupEntity.class);
                if (qualityGroupEntity != null) {
                    List<QualityGroupBean> hotTopicList = qualityGroupEntity.getQualityGroupBeanList();
                    if (qualityGroupEntity.getCode().equals(SUCCESS_CODE)) {
                        if (hotTopicList != null && hotTopicList.size() > 0) {
                            goodsRecommendList.clear();
                            goodsRecommendList.addAll(hotTopicList.subList(0, hotTopicList.size() > 20 ? 20 : hotTopicList.size()));
                            mGroupRecommendAdapter.notifyDataSetChanged();
                        }

                        mllProductRecommend.setVisibility(goodsRecommendList.size() > 0 ? VISIBLE : GONE);
                    }
                }
            }
        });
    }

    //??????????????????
    private void setSeviceTag(List<String> tags, ViewGroup viewGroup, FlexboxLayout flexboxLayout) {
        try {
            if (tags != null && tags.size() > 0) {
                flexboxLayout.removeAllViews();
                int tagLength = 0;
                for (String tagName : tags) {
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
    private boolean isStart() {
        if (mGroupShopDetailsBean != null) {
            List<SkuSaleBean> skuSale = mGroupShopDetailsBean.getSkuSale();
            if (skuSale != null && skuSale.size() > 0) {
                return !skuSale.get(0).getPrice().equals(skuSale.get(skuSale.size() - 1).getPrice());
            }
        }

        return false;
    }

    @OnClick({R.id.ll_back, R.id.ll_back2, R.id.ll_service, R.id.ll_service2, R.id.ll_share, R.id.ll_share2,
            R.id.ll_group_buy, R.id.ll_alone_buy, R.id.tv_all_lottery, R.id.iv_lottery_zone, R.id.rel_pro_comment,
            R.id.tv_group_home, R.id.tv_ql_sp_pro_sku, R.id.ll_layout_pro_sc_tag, R.id.tv_quality_all_gp_sp, R.id.tv_invate})
    public void onViewClicked(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.ll_back:
            case R.id.ll_back2:
                finish();
                break;
            case R.id.ll_service:
            case R.id.ll_service2:
                intent = new Intent(getActivity(), ShopCarActivity.class);
                startActivity(intent);
//                skipServiceDataInfo(mGroupShopDetailsBean);
                break;
            //??????????????????
            case R.id.ll_share:
            case R.id.ll_share2:
                if (mGroupShopDetailsBean != null) {
                    new UMShareAction(QualityGroupShopDetailActivity.this
                            , mGroupShopDetailsBean.getCoverImage()
                            , (mGroupShopDetailsBean.isProductGroup() ? getStringsFormat(this, isStart() ? R.string.group_price_end : R.string.group_price, mGroupShopDetailsBean.getGpPrice()) : "") + (!TextUtils.isEmpty(mGroupShopDetailsBean.getGpName()) ? mGroupShopDetailsBean.getGpName() : mGroupShopDetailsBean.getProductName())
                            , ""
                            , ""
                            , (mGroupShopDetailsBean.isProductGroup() ? "pages/groupDetails/groupDetails?id=" : "pages/LotteryGroup/lotteryGroup?gpInfoId=") + mGroupShopDetailsBean.getGpInfoId(),
                            mGroupShopDetailsBean.getGpInfoId(), -1, "1");
                }
                break;
            //????????????
            case R.id.tv_invate:
                if (mGroupShopDetailsBean != null && !TextUtils.isEmpty(gpRecordId)) {
                    GroupDao.invitePartnerGroup(getActivity(), mGroupShopDetailsBean);
                }
                break;
            //????????????
            case R.id.tv_group_home:
            case R.id.tv_quality_all_gp_sp:
                intent = new Intent(getActivity(), QualityGroupShopActivity.class);
                startActivity(intent);
                break;
            //????????????
            case R.id.ll_alone_buy:
                intent = new Intent(getActivity(), ShopScrollDetailsActivity.class);
                intent.putExtra("productId", productId);
                startActivity(intent);
                break;
            //??????
            case R.id.ll_group_buy:
                if (mGroupShopDetailsBean != null) {
                    mGroupShopDetailsBean.setGpStatus(OPEN_GROUP);
                    buyGoItCheckStatus();
                }
                break;
            //????????????
            case R.id.rel_pro_comment:
                skipMoreEvaluate();
                break;
            //sku????????????
            case R.id.tv_ql_sp_pro_sku:
                if (skuDialog != null) {
                    skuDialog.show(true, "??????");
                    skuDialog.getGoodsSKu(shopCarGoodsSku -> {
                        mGroupShopDetailsBean.setGpSkuId(shopCarGoodsSku == null ? 0 : shopCarGoodsSku.getSaleSkuId());
                        if (shopCarGoodsSku == null) {
                            tv_ql_sp_pro_sku.setTextColor(getResources().getColor(R.color.color_blue_reply_com));
                            tv_ql_sp_pro_sku.setText(String.format(getResources().getString(R.string.sel_pro_sku)
                                    , getStrings(mGroupShopDetailsBean.getProps().get(0).getPropName())));
                        }
                    });
                }
                break;
            //??????????????????
            case R.id.ll_layout_pro_sc_tag:
                if (mGroupShopDetailsBean != null && mIvMoreTag.getVisibility() == VISIBLE) {
                    if (alertDialog == null) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomTransDialog);
                        View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_sevicetag_dialog, null, false);
                        FlexboxLayout flex_communal_tag = dialogView.findViewById(R.id.flex_communal_tag);
                        setSeviceTag(mGroupShopDetailsBean.getTagText(), flex_communal_tag, flex_communal_tag);
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
            //??????????????????
            case R.id.tv_all_lottery:
                if (mGroupShopDetailsBean != null) {
                    List<GroupShopJoinBean> luckUserList = mGroupShopDetailsBean.getLuckUserList();
                    if (luckUserList != null) {
                        intent = new Intent(this, AllLotteryActivity.class);
                        intent.putExtra("allLotteryJson", GsonUtils.toJson(luckUserList));
                        startActivity(intent);
                    }
                }
                break;
            //????????????????????????
            case R.id.iv_lottery_zone:
                intent = new Intent(this, QualityCustomTopicActivity.class);
                intent.putExtra("productType", "206");
                startActivity(intent);
                break;
        }
    }

    //??????????????????
    private void skipMoreEvaluate() {
        Intent intent = new Intent(getActivity(), DirectProductEvaluationActivity.class);
        intent.putExtra("productId", productId);
        intent.putExtra("productName", mGroupShopDetailsBean.getProductName());
        intent.putExtra("cover", mGroupShopDetailsBean.getCoverImage());
        startActivity(intent);
    }

    //????????????
    private void buyGoItCheckStatus() {
        //?????????sku
        if (mGroupShopDetailsBean != null && mGroupShopDetailsBean.getGpSkuId() > 0) {
            skipIndentWrite();
        } else {
            //??????sku
            if (skuDialog != null) {
                skuDialog.show(true, "??????");
                skuDialog.getGoodsSKu(shopCarGoodsSku -> {
                    mGroupShopDetailsBean.setGpSkuId(shopCarGoodsSku == null ? 0 : shopCarGoodsSku.getSaleSkuId());
                    if (shopCarGoodsSku != null) {
                        tv_ql_sp_pro_sku.setTextColor(getResources().getColor(R.color.text_black_t));
                        tv_ql_sp_pro_sku.setText(("?????????" + shopCarGoodsSku.getValuesName()));
                        skipIndentWrite();
                    }
                });
            }
        }
    }

    //??????????????????
    private void skipIndentWrite() {
        if (userId > 0) {
            Bundle bundle = new Bundle();
            bundle.putString("gpShopInfo", GsonUtils.toJson(mGroupShopDetailsBean));
            ConstantMethod.skipIndentWrite(getActivity(), INDENT_GROUP_SHOP, bundle);
        } else {
            getLoginStatus(getActivity());
        }
    }

    //??????
    public void isCanJoinGroup(final GroupShopJoinBean groupShopjoinBean) {
        if (userId != 0) {
            Map<String, Object> params = new HashMap<>();
            params.put("uid", userId);
            params.put("gpInfoId", mGroupShopDetailsBean.getGpInfoId());
            NetLoadUtils.getNetInstance().loadNetDataPost(this, GROUP_SHOP_JOIN_NRE_USER, params, new NetLoadListenerHelper() {
                @Override
                public void onSuccess(String result) {

                    RequestStatus requestStatus = GsonUtils.fromJson(result, RequestStatus.class);
                    if (requestStatus.getCode().equals(SUCCESS_CODE)) {
                        mGroupShopDetailsBean.setGpStatus(JOIN_GROUP);
                        mGroupShopDetailsBean.setGpRecordId(groupShopjoinBean.getGpRecordId());
                        buyGoItCheckStatus();
                    } else {
                        showToastRequestMsg(requestStatus);
                    }
                }

                @Override
                public void onError(Throwable throwable) {
                    showToast(R.string.invalidData);
                }
            });
        } else {
            getLoginStatus(QualityGroupShopDetailActivity.this);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
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
