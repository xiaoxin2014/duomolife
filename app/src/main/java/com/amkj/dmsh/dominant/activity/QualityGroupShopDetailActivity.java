package com.amkj.dmsh.dominant.activity;

import android.content.Intent;
import android.graphics.Paint;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.amkj.dmsh.base.TinkerBaseApplicationLike;
import com.amkj.dmsh.bean.RequestStatus;
import com.amkj.dmsh.constant.CommunalAdHolderView;
import com.amkj.dmsh.constant.CommunalDetailBean;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.TabEntity;
import com.amkj.dmsh.constant.UMShareAction;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.dominant.adapter.JoinGroupAdapter;
import com.amkj.dmsh.dominant.bean.GroupShopCommunalInfoEntity;
import com.amkj.dmsh.dominant.bean.GroupShopCommunalInfoEntity.GroupShopCommunalInfoBean;
import com.amkj.dmsh.dominant.bean.GroupShopDetailsEntity;
import com.amkj.dmsh.dominant.bean.GroupShopDetailsEntity.GroupShopDetailsBean;
import com.amkj.dmsh.dominant.bean.GroupShopDetailsEntity.GroupShopDetailsBean.QuantityStatusBean;
import com.amkj.dmsh.dominant.bean.GroupShopJoinEntity;
import com.amkj.dmsh.dominant.bean.GroupShopJoinEntity.GroupShopJoinBean;
import com.amkj.dmsh.dominant.bean.QualityGroupShareEntity;
import com.amkj.dmsh.dominant.bean.QualityGroupShareEntity.QualityGroupShareBean;
import com.amkj.dmsh.dominant.bean.QualityGroupShareEntity.QualityGroupShareBean.MemberListBean;
import com.amkj.dmsh.homepage.adapter.CommunalDetailAdapter;
import com.amkj.dmsh.homepage.bean.CommunalADActivityEntity.CommunalADActivityBean;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.qyservice.QyProductIndentInfo;
import com.amkj.dmsh.qyservice.QyServiceUtils;
import com.amkj.dmsh.shopdetails.activity.DirectIndentWriteActivity;
import com.amkj.dmsh.shopdetails.activity.ShopScrollDetailsActivity;
import com.amkj.dmsh.shopdetails.adapter.DirectEvaluationAdapter;
import com.amkj.dmsh.shopdetails.bean.CommunalDetailObjectBean;
import com.amkj.dmsh.shopdetails.bean.EditGoodsSkuEntity;
import com.amkj.dmsh.shopdetails.bean.GoodsCommentEntity;
import com.amkj.dmsh.shopdetails.bean.GoodsCommentEntity.GoodsCommentBean;
import com.amkj.dmsh.shopdetails.bean.ShopCarGoodsSku;
import com.amkj.dmsh.shopdetails.bean.ShopDetailsEntity;
import com.amkj.dmsh.shopdetails.bean.ShopDetailsEntity.ShopPropertyBean;
import com.amkj.dmsh.shopdetails.bean.ShopRecommendHotTopicEntity;
import com.amkj.dmsh.shopdetails.bean.ShopRecommendHotTopicEntity.ShopRecommendHotTopicBean;
import com.amkj.dmsh.shopdetails.bean.SkuSaleBean;
import com.amkj.dmsh.utils.TimeUtils;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.amkj.dmsh.utils.webformatdata.CommunalWebDetailUtils;
import com.amkj.dmsh.views.StatusBarUtil;
import com.amkj.dmsh.views.bottomdialog.SkuDialog;
import com.amkj.dmsh.views.flycoTablayout.CommonTabLayout;
import com.amkj.dmsh.views.flycoTablayout.listener.CustomTabEntity;
import com.amkj.dmsh.views.flycoTablayout.listener.OnTabSelectListener;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.google.android.flexbox.FlexboxLayout;
import com.google.gson.Gson;
import com.melnykov.fab.FloatingActionButton;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.tencent.bugly.beta.tinker.TinkerManager;
import com.umeng.socialize.UMShareAPI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import me.jessyan.autosize.utils.AutoSizeUtils;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.getStringChangeBoolean;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.getStringsChNPrice;
import static com.amkj.dmsh.constant.ConstantMethod.getStringsFormat;
import static com.amkj.dmsh.constant.ConstantMethod.showImageActivity;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.showToastRequestMsg;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.TYPE_2;
import static com.amkj.dmsh.constant.Url.BASE_SHARE_PAGE_TWO;
import static com.amkj.dmsh.constant.Url.GROUP_MINE_SHARE;
import static com.amkj.dmsh.constant.Url.GROUP_SHOP_COMMUNAL;
import static com.amkj.dmsh.constant.Url.GROUP_SHOP_DETAILS;
import static com.amkj.dmsh.constant.Url.GROUP_SHOP_JOIN_NRE_USER;
import static com.amkj.dmsh.constant.Url.GROUP_SHOP_OPEN_PERSON;
import static com.amkj.dmsh.constant.Url.Q_NEW_SHOP_DETAILS;
import static com.amkj.dmsh.find.activity.ImagePagerActivity.IMAGE_DEF;
import static com.amkj.dmsh.shopdetails.bean.CommunalDetailObjectBean.TYPE_PRODUCT_TITLE;
import static com.amkj.dmsh.utils.TimeUtils.isEndOrStartTime;
import static com.amkj.dmsh.utils.glide.GlideImageLoaderUtil.getSquareImgUrl;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/6/6
 * class description:拼团详情
 */
public class QualityGroupShopDetailActivity extends BaseActivity {
    @BindView(R.id.smart_refresh_ql_sp_details)
    SmartRefreshLayout smart_refresh_ql_sp_details;
    @BindView(R.id.banner_ql_gp_sp_details)
    ConvenientBanner banner_ql_sp_pro_details;
    @BindView(R.id.tv_ql_gp_sp_new_detail)
    TextView tv_ql_gp_sp_new_detail;
    //            商品名字
    @BindView(R.id.tv_ql_sp_pro_name)
    TextView tv_ql_sp_pro_name;
    //            拼团人数
    @BindView(R.id.tv_gp_sp_per_count)
    TextView tv_gp_sp_per_count;
    //            拼团价格
    @BindView(R.id.tv_gp_sp_per_price)
    TextView tv_gp_sp_per_price;
    //    单买价
    @BindView(R.id.tv_gp_sp_nor_price)
    TextView tv_gp_sp_nor_price;
    //    团购布局
    @BindView(R.id.ll_group_buy)
    LinearLayout ll_group_buy;
    //    团购价
    @BindView(R.id.tv_sp_details_join_buy_price)
    TextView tv_sp_details_join_buy_price;
    //    团购信息
    @BindView(R.id.tv_sp_details_join_count)
    TextView tv_sp_details_join_count;
    //    单独购买
    @BindView(R.id.ll_alone_buy)
    LinearLayout ll_alone_buy;
    //    单独购买价格
    @BindView(R.id.tv_sp_details_ol_buy_price)
    TextView tv_sp_details_ol_buy_price;
    //    单独购买描述
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
    LinearLayout mRlToolbar;
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

    //拼团列表
    private List<GroupShopJoinBean> groupShopJoinList = new ArrayList<>();
    //拼团规则
    private List<CommunalDetailObjectBean> gpRuleList = new ArrayList<>();
    //轮播图片视频
    private List<CommunalADActivityBean> imagesVideoList = new ArrayList<>();
    //商品评论
    private List<GoodsCommentBean> goodsComments = new ArrayList<>();
    //文章主题推荐
    private List<ShopRecommendHotTopicBean> articalRecommendList = new ArrayList<>();
    //商品详情 服务承诺 合集
    private List<CommunalDetailObjectBean> shopDetailBeanList = new ArrayList<>();
    //    服务承诺
    private List<CommunalDetailObjectBean> serviceDataList = new ArrayList<>();
    private JoinGroupAdapter joinGroupAdapter;
    private DirectEvaluationAdapter directEvaluationAdapter;
    private CommunalDetailAdapter gpRuleDetailsAdapter;
    private CommunalDetailAdapter communalDetailAdapter;
    private GroupShopDetailsEntity mGroupShopDetailsEntity;
    private GroupShopCommunalInfoEntity groupShopCommunalInfoEntity;
    private QualityGroupShareEntity qualityGroupShareEntity;
    private ShopDetailsEntity mShopDetailsEntity;
    private EditGoodsSkuEntity.EditGoodsSkuBean editGoodsSkuBean;
    private boolean isPause;
    private String sharePageUrl = BASE_SHARE_PAGE_TWO + "m/template/share_template/groupDetail.html?id=";
    private CBViewHolderCreator cbViewHolderCreator;
    private boolean invitePartnerJoin;
    private String orderNo;
    private String gpInfoId;
    private String productId;
    private String gpRecordId;
    private int screenHeight;
    private int screenWith;
    private int statusBarHeight;
    private int measuredHeight;
    private SkuDialog skuDialog;
    private AlertDialog alertDialog;
    private ShopCarGoodsSku shopCarGoodsSkuDif;
    private GroupShopDetailsBean mGroupShopDetailsBean;
    private String[] detailTabData = {"商品", "详情"};
    private ArrayList<CustomTabEntity> tabData = new ArrayList<>();


    @Override
    protected int getContentView() {
        return R.layout.activity_ql_gp_sp_details;
    }

    @Override
    protected void initViews() {
        tv_gp_sp_nor_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        tv_gp_sp_nor_price.getPaint().setAntiAlias(true);
        Intent intent = getIntent();
        gpInfoId = intent.getStringExtra("gpInfoId");
        productId = intent.getStringExtra("productId");
        gpRecordId = intent.getStringExtra("gpRecordId");
        orderNo = intent.getStringExtra("orderNo");
        TinkerBaseApplicationLike app = (TinkerBaseApplicationLike) TinkerManager.getTinkerApplicationLike();
        screenHeight = app.getScreenHeight();
        screenWith = app.getScreenWidth();
        statusBarHeight = StatusBarUtil.getStatusBarHeight(this);
        invitePartnerJoin = getStringChangeBoolean(intent.getStringExtra("invitePartnerJoinCode"));
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

        //初始化拼团列表
        rv_group_join.setLayoutManager(new LinearLayoutManager(QualityGroupShopDetailActivity.this));
        joinGroupAdapter = new JoinGroupAdapter(QualityGroupShopDetailActivity.this, groupShopJoinList);
        rv_group_join.setAdapter(joinGroupAdapter);
        joinGroupAdapter.setOnItemClickListener((adapter, view, position) -> {
            GroupShopJoinBean groupShopJoinBean = (GroupShopJoinBean) view.getTag();
            if (groupShopJoinBean != null && mGroupShopDetailsBean != null) {
                if (userId != 0) {
                    isCanJoinGroup(groupShopJoinBean);
                } else {
                    getLoginStatus(QualityGroupShopDetailActivity.this);
                }
            }
        });

        //初始化活动规则
        rv_group_rule.setNestedScrollingEnabled(false);
        gpRuleDetailsAdapter = new CommunalDetailAdapter(this, gpRuleList);
        rv_group_rule.setLayoutManager(new LinearLayoutManager(this));
        rv_group_rule.setAdapter(gpRuleDetailsAdapter);

        //初始化评论列表
        rv_comment.setLayoutManager(new LinearLayoutManager(this));
        directEvaluationAdapter = new DirectEvaluationAdapter(this, goodsComments);
        rv_comment.setNestedScrollingEnabled(false);
        rv_comment.setAdapter(directEvaluationAdapter);

        //初始化图文详情
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

                //设置标题栏
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
        //获取的测量高度包含了状态栏高度，所以要减去
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
        //获取团购信息
        getGroupShopDetails();
        //拼团规则 服务承诺
        getGroupCommunalInfo();
        //获取拼团列表
        getGroupShopPerson();
        //获取商品评论
        getComment();
        //获取推荐文章
        getArticalRecommend();
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
        NetLoadUtils.getNetInstance().loadNetDataPost(QualityGroupShopDetailActivity.this, GROUP_SHOP_DETAILS,
                params, new NetLoadListenerHelper() {
                    @Override
                    public void onSuccess(String result) {
                        Gson gson = new Gson();
                        mGroupShopDetailsEntity = gson.fromJson(result, GroupShopDetailsEntity.class);
                        if (mGroupShopDetailsEntity != null && mGroupShopDetailsEntity.getGroupShopDetailsBean() != null) {
                            mGroupShopDetailsBean = mGroupShopDetailsEntity.getGroupShopDetailsBean();
                            if (mGroupShopDetailsEntity.getCode().equals(SUCCESS_CODE)) {
                                setGroupShopDetailsData(mGroupShopDetailsBean);
                            } else if (!mGroupShopDetailsEntity.getCode().equals(EMPTY_CODE)) {
                                showToast(QualityGroupShopDetailActivity.this, mGroupShopDetailsEntity.getMsg());
                            }
                        }

                        getShopDetails();
                    }

                    @Override
                    public void onNotNetOrException() {
                        getShopDetails();
                    }
                });
    }

    private void getShopDetails() {
        Map<String, Object> params = new HashMap<>();
        params.put("id", productId);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, Q_NEW_SHOP_DETAILS, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                smart_refresh_ql_sp_details.finishRefresh();
                shopDetailBeanList.clear();
                mShopDetailsEntity = new Gson().fromJson(result, ShopDetailsEntity.class);
                if (mShopDetailsEntity != null && mShopDetailsEntity.getShopPropertyBean() != null) {
                    if (mShopDetailsEntity.getCode().equals(SUCCESS_CODE)) {
                        setProductData(mShopDetailsEntity.getShopPropertyBean());
                    }
                }

                communalDetailAdapter.notifyDataSetChanged();
                NetLoadUtils.getNetInstance().showLoadSir(loadService, mGroupShopDetailsEntity);
            }

            @Override
            public void onNotNetOrException() {
                smart_refresh_ql_sp_details.finishRefresh();
                NetLoadUtils.getNetInstance().showLoadSir(loadService, mGroupShopDetailsEntity);
            }
        });
    }

    //  获取商品评论
    private void getComment() {
        Map<String, Object> params = new HashMap<>();
        params.put("showCount", 1);
        params.put("currentPage", 1);
        params.put("id", productId);
        if (userId > 0) {
            params.put("uid", userId);
        }
        NetLoadUtils.getNetInstance().loadNetDataPost(this, Url.Q_SHOP_DETAILS_COMMENT, params, new NetLoadListenerHelper() {
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
                    tv_shop_comment_count.setText(("Ta们在说(" + goodsCommentEntity.getEvaluateCount() + ")"));
                    directEvaluationAdapter.setNewData(goodsComments);

                    ll_comment.setVisibility(goodsComments.size() > 0 ? VISIBLE : GONE);
                }
            }
        });
    }

    private void setProductData(final ShopPropertyBean shopProperty) {
        //轮播位（包含图片以及视频）
        imagesVideoList.clear();
        List<String> imageList = Arrays.asList(shopProperty.getImages().split(","));
        if (shopProperty.haveVideo()) {
            imagesVideoList.add(new CommunalADActivityBean("", shopProperty.getVideoUrl()));
        }
        for (int i = 0; i < imageList.size(); i++) {
            imagesVideoList.add(new CommunalADActivityBean(getSquareImgUrl(imageList.get(i), screenWith, shopProperty.getWaterRemark()), ""));
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
        banner_ql_sp_pro_details.setPages(this, cbViewHolderCreator, imagesVideoList).setCanLoop(false)
                .setPageIndicator(new int[]{R.drawable.unselected_radius, R.drawable.selected_radius});
        banner_ql_sp_pro_details.setOnItemClickListener(position -> {
            if (imagesVideoList.size() > position) {
                CommunalADActivityBean communalADActivityBean = imagesVideoList.get(position);
                if (!TextUtils.isEmpty(communalADActivityBean.getVideoUrl())) {
                    return;
                }

                showImageActivity(getActivity(), IMAGE_DEF,
                        position - (shopProperty.haveVideo() ? 1 : 0),
                        imageList);
            }
        });

        //商品服务标签
        setSeviceTag(shopProperty, ll_layout_pro_sc_tag, flex_product_tag);

        //图文详情
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

        //设置sku
        setSkuProp(shopProperty);
    }

    private void setSkuProp(ShopPropertyBean shopProperty) {
        List<SkuSaleBean> skuSaleList = shopProperty.getSkuSale();
        if (skuSaleList != null && skuSaleList.size() > 0) {
            //选择规格
            tv_ql_sp_pro_sku.setTextColor(getResources().getColor(R.color.color_blue_reply_com));
            tv_ql_sp_pro_sku.setText(getString(R.string.sel_pro_sku
                    , getStrings(shopProperty.getProps().get(0).getPropName())));
            //有多个SKU
            if (skuSaleList.size() > 1) {
                ll_sp_pro_sku_value.setVisibility(VISIBLE);
                editGoodsSkuBean = new EditGoodsSkuEntity.EditGoodsSkuBean();
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
                if ("待售".equals(getStrings(shopProperty.getSellStatus()))) {
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
                        tv_ql_sp_pro_sku.setText(("已选：" + shopCarGoodsSku.getValuesName()));
                    }
                });
                skuDialog.refreshView(editGoodsSkuBean);
            } else {
                //仅有一个SKU
                ll_sp_pro_sku_value.setVisibility(View.GONE);
                ll_sp_pro_sku_value.setVisibility(View.GONE);
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
        } else {
            showToast(this, "商品数据错误");
        }
    }

    private void setGroupShopDetailsData(GroupShopDetailsBean groupShopDetailsBean) {
        if (groupShopDetailsBean.getRange() == 1) {
            tv_ql_gp_sp_new_detail.setVisibility(View.VISIBLE);
            tv_partner_join.setText(R.string.ql_new_gp_tint);
        } else if (groupShopDetailsBean.getRange() == 0) {
            tv_ql_gp_sp_new_detail.setVisibility(View.GONE);
            tv_partner_join.setText(R.string.ql_normal_gp_tint);
        }

        //设置商品标题
        String productName = TextUtils.isEmpty(groupShopDetailsBean.getSubtitle()) ? groupShopDetailsBean.getName() : (groupShopDetailsBean.getSubtitle() + "•" + groupShopDetailsBean.getName());
        tv_ql_sp_pro_name.setText(getStrings(productName));

        tv_gp_sp_per_count.setText(getStrings(groupShopDetailsBean.getMemberCount() + "人拼团价¥"));
        tv_gp_sp_per_price.setText(getStrings(groupShopDetailsBean.getGpPrice()));
        tv_gp_sp_nor_price.setText("原价 ¥" + groupShopDetailsBean.getPrice());
        tv_sp_details_ol_buy.setText("单独购买");
        tv_sp_details_ol_buy_price.setText(getStringsChNPrice(this, groupShopDetailsBean.getPrice()));
        if (groupShopDetailsBean.getQuantityStatus() == null) {
            return;
        }
        if (!invitePartnerJoin) {
            QuantityStatusBean quantityStatus = groupShopDetailsBean.getQuantityStatus();
            switch (quantityStatus.getQuantityStatusId()) {
                case 1002:
                    ll_group_buy.setEnabled(true);
                    tv_sp_details_join_buy_price.setText(getStringsChNPrice(this, groupShopDetailsBean.getGpPrice()));
                    tv_sp_details_join_count.setText(getStrings(groupShopDetailsBean.getGpType()));
                    break;
                case 1001:
                    ll_group_buy.setEnabled(false);
                    tv_sp_details_join_buy_price.setText(getStrings(quantityStatus.getQuantityStatusMsg()));
                    tv_sp_details_join_count.setText("逛逛其它");
                    break;
                case 1003:
                    ll_group_buy.setEnabled(false);
                    tv_sp_details_join_buy_price.setText(getStrings(quantityStatus.getQuantityStatusMsg()));
                    tv_sp_details_join_count.setText("逛逛其它");
                    break;
            }
        }
    }


    //获取拼团列表
    private void getGroupShopPerson() {
        if (!TextUtils.isEmpty(gpRecordId)) {
            mLlGroupDetailBottom.setVisibility(GONE);
            mLlGroupDetailBottom2.setVisibility(VISIBLE);
            Map<String, Object> params = new HashMap<>();
            params.put("gpRecordId", gpRecordId);
            NetLoadUtils.getNetInstance().loadNetDataPost(this, GROUP_MINE_SHARE, params, new NetLoadListenerHelper() {
                @Override
                public void onSuccess(String result) {
                    groupShopJoinList.clear();
                    Gson gson = new Gson();
                    qualityGroupShareEntity = gson.fromJson(result, QualityGroupShareEntity.class);
                    if (qualityGroupShareEntity != null) {
                        if (qualityGroupShareEntity.getCode().equals(SUCCESS_CODE)) {
                            QualityGroupShareBean qualityGroupShareBean = qualityGroupShareEntity.getQualityGroupShareBean();
                            if (qualityGroupShareBean != null) {
                                List<MemberListBean> memberList = qualityGroupShareBean.getMemberList();
                                if (memberList != null && memberList.size() > 0) {
                                    GroupShopJoinBean groupShopJoinBean = new GroupShopJoinBean();
                                    List<MemberListBean> memberListBeans = new ArrayList<>(qualityGroupShareBean.getMemberList());
                                    int leftParticipant = qualityGroupShareBean.getMemberCount() - qualityGroupShareBean.getMemberList().size();
                                    for (int i = 0; i < leftParticipant; i++) {
                                        MemberListBean memberListBean = new MemberListBean();
                                        memberListBean.setAvatar("android.resource://com.amkj.dmsh/drawable/" + R.drawable.who);
                                        memberListBeans.add(memberListBean);
                                    }
                                    groupShopJoinBean.setItemType(TYPE_2);
                                    groupShopJoinBean.setMemberListBeans(memberListBeans);

                                    //还差几人成团
                                    if (leftParticipant > 0) {
                                        //未结束
                                        if (isEndOrStartTime(qualityGroupShareBean.getGpEndTime(), TimeUtils.getCurrentTime(qualityGroupShareEntity))) {
                                            mTvInvate.setEnabled(true);
                                            groupShopJoinBean.setGroupStatus(ConstantMethod.getIntegralFormat(getActivity(), R.string.leftParticipant, leftParticipant));
                                        } else {
                                            mTvInvate.setEnabled(false);
                                            groupShopJoinBean.setGroupStatus("已结束");
                                        }
                                    } else {
                                        mTvInvate.setEnabled(false);
                                        groupShopJoinBean.setGroupStatus("已成团");
                                    }

                                    groupShopJoinList.add(groupShopJoinBean);
                                }else {
                                    mTvInvate.setEnabled(false);
                                }
                            }
                        }
                    }

                    joinGroupAdapter.setNewData(groupShopJoinList);
                    ll_group_join.setVisibility(groupShopJoinList.size() > 0 ? VISIBLE : GONE);
                }

                @Override
                public void onNotNetOrException() {
                    ll_group_join.setVisibility(groupShopJoinList.size() > 0 ? VISIBLE : GONE);
                }
            });
        } else {
            mLlGroupDetailBottom.setVisibility(VISIBLE);
            mLlGroupDetailBottom2.setVisibility(GONE);
            Map<String, Object> params = new HashMap<>();
            params.put("gpInfoId", gpInfoId);
            if (userId > 0) {
                params.put("uid", userId);
            }
            NetLoadUtils.getNetInstance().loadNetDataPost(this, GROUP_SHOP_OPEN_PERSON, params, new NetLoadListenerHelper() {
                @Override
                public void onSuccess(String result) {
                    groupShopJoinList.clear();
                    Gson gson = new Gson();
                    GroupShopJoinEntity groupShopJoinEntity = gson.fromJson(result, GroupShopJoinEntity.class);
                    if (groupShopJoinEntity != null) {
                        if (groupShopJoinEntity.getCode().equals(SUCCESS_CODE)) {
                            List<GroupShopJoinBean> joinBeanList = groupShopJoinEntity.getGroupShopJoinBeanList();
                            if (joinBeanList != null && joinBeanList.size() > 0) {
                                for (int i = 0; i < joinBeanList.size(); i++) {
                                    GroupShopJoinBean groupShopJoinBean = joinBeanList.get(i);
                                    groupShopJoinBean.setCurrentTime(groupShopJoinEntity.getCurrentTime());
                                    groupShopJoinList.add(groupShopJoinBean);
                                }
                            }
                        }
                    }
                    ll_group_join.setVisibility(groupShopJoinList.size() > 0 ? VISIBLE : GONE);
                    joinGroupAdapter.setNewData(groupShopJoinList);
                }

                @Override
                public void onNotNetOrException() {
                    ll_group_join.setVisibility(groupShopJoinList.size() > 0 ? VISIBLE : GONE);
                }
            });
        }
    }

    //拼团规则
    private void getGroupCommunalInfo() {
        NetLoadUtils.getNetInstance().loadNetDataPost(this, GROUP_SHOP_COMMUNAL, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                gpRuleList.clear();
                serviceDataList.clear();
                groupShopCommunalInfoEntity = new Gson().fromJson(result, GroupShopCommunalInfoEntity.class);
                if (groupShopCommunalInfoEntity != null) {
                    GroupShopCommunalInfoBean infoBean = groupShopCommunalInfoEntity.getGroupShopCommunalInfoBean();
                    if (infoBean != null) {
                        List<CommunalDetailBean> gpRuleBeanList = infoBean.getGpRule();
                        List<CommunalDetailBean> servicePromiseList = infoBean.getServicePromise();
                        if (gpRuleBeanList != null && gpRuleBeanList.size() > 0) {
                            gpRuleList.addAll(CommunalWebDetailUtils.getCommunalWebInstance().getWebDetailsFormatDataList(gpRuleBeanList));
                        }

                        if (servicePromiseList != null && servicePromiseList.size() > 0) {
                            CommunalDetailObjectBean communalDetailObjectBean = new CommunalDetailObjectBean();
                            communalDetailObjectBean.setItemType(TYPE_PRODUCT_TITLE);
                            communalDetailObjectBean.setContent("服务承诺");
                            communalDetailObjectBean.setFirstLinePadding(true);
                            serviceDataList.add(communalDetailObjectBean);
                            serviceDataList.addAll(CommunalWebDetailUtils.getCommunalWebInstance().getWebDetailsFormatDataList(servicePromiseList));
                        }

                        //保证图文详情在服务承诺上面
                        if (shopDetailBeanList.size() > 0) {
                            shopDetailBeanList.addAll(serviceDataList);
                            communalDetailAdapter.notifyDataSetChanged();
                        }
                    }
                }

                gpRuleDetailsAdapter.notifyDataSetChanged();
                communalDetailAdapter.notifyDataSetChanged();
            }
        });
    }

    //获取推荐文章
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

    //商品服务标签
    private void setSeviceTag(ShopPropertyBean shopProperty, ViewGroup viewGroup, FlexboxLayout flexboxLayout) {
        try {
            List<ShopPropertyBean.TagsBean> tags = shopProperty.getTags();
            String tagIds = shopProperty.getTagIds();
            shopProperty.setTagIds(tagIds);
            if (tags != null && tags.size() > 0 && !TextUtils.isEmpty(tagIds) && tagIds.split(",").length > 0) {
                final Map<Integer, String> tagMap = new HashMap<>();
                for (ShopPropertyBean.TagsBean tagsBean : shopProperty.getTags()) {
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

    @OnClick({R.id.iv_life_back, R.id.iv_img_service, R.id.iv_img_share, R.id.ll_group_buy, R.id.ll_alone_buy,
            R.id.rel_pro_comment, R.id.tv_group_home, R.id.tv_ql_sp_pro_sku, R.id.ll_layout_pro_sc_tag, R.id.tv_quality_all_gp_sp, R.id.tv_invate})
    public void onViewClicked(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.iv_life_back:
                finish();
                break;
            case R.id.iv_img_service:
                if (mGroupShopDetailsBean != null) {
                    skipServiceDataInfo(mGroupShopDetailsBean);
                } else {
                    skipServiceDataInfo(null);
                }
                break;
            //分享拼团详情
            case R.id.iv_img_share:
                if (mGroupShopDetailsBean != null) {
                    new UMShareAction(QualityGroupShopDetailActivity.this
                            , !TextUtils.isEmpty(mGroupShopDetailsBean.getGpPicUrl()) ? mGroupShopDetailsBean.getGpPicUrl() : mGroupShopDetailsBean.getCoverImage()
                            , getStringsFormat(this, R.string.group_price, mGroupShopDetailsBean.getGpPrice()) + mGroupShopDetailsBean.getName()
                            , "超值两人团，好货又便宜。"
                            , sharePageUrl + mGroupShopDetailsBean.getGpInfoId(),
                            "pages/groupDetails/groupDetails?id=" + mGroupShopDetailsBean.getGpInfoId(), mGroupShopDetailsBean.getGpInfoId());
                }

                break;
            //邀请参团
            case R.id.tv_invate:
                if (!TextUtils.isEmpty(gpRecordId) && qualityGroupShareEntity != null
                        && qualityGroupShareEntity.getQualityGroupShareBean() != null) {
                    QualityGroupShareBean qualityGroupShareBean = qualityGroupShareEntity.getQualityGroupShareBean();
                    new UMShareAction(QualityGroupShopDetailActivity.this
                            , qualityGroupShareBean.getGpPicUrl()
                            , qualityGroupShareBean.getName()
                            , getStrings(qualityGroupShareBean.getSubtitle())
                            , BASE_SHARE_PAGE_TWO + "m/template/share_template/groupShare.html?id=" + qualityGroupShareBean.getGpInfoId()
                            + "&record=" + qualityGroupShareBean.getGpRecordId(), "pages/groupshare/groupshare?id=" + qualityGroupShareBean.getGpInfoId()
                            + (TextUtils.isEmpty(orderNo) ? "&gpRecordId=" + qualityGroupShareBean.getGpRecordId() : "&order=" + orderNo), qualityGroupShareBean.getGpInfoId(), -1, "1");
                }
                break;
            //拼团首页
            case R.id.tv_group_home:
            case R.id.tv_quality_all_gp_sp:
                intent = new Intent(getActivity(), QualityGroupShopActivity.class);
                startActivity(intent);
                break;
            //单独购买
            case R.id.ll_alone_buy:
                intent = new Intent(getActivity(), ShopScrollDetailsActivity.class);
                intent.putExtra("productId", productId);
                startActivity(intent);
                break;
            //开团
            case R.id.ll_group_buy:
                buyGoItCheckStatus();
                break;
            //更多评论
            case R.id.rel_pro_comment:
                intent = new Intent(getActivity(), DirectProductEvaluationActivity.class);
                intent.putExtra("productId", productId);
                startActivity(intent);
                break;
            //sku属性选择
            case R.id.tv_ql_sp_pro_sku:
                if (skuDialog != null) {
                    skuDialog.show(true, "确定");
                    skuDialog.getGoodsSKu(shopCarGoodsSku -> {
                        shopCarGoodsSkuDif = shopCarGoodsSku;
                        if (shopCarGoodsSku != null) {
                            buyGoItCheckStatus();
                        } else {
                            tv_ql_sp_pro_sku.setTextColor(getResources().getColor(R.color.color_blue_reply_com));
                            tv_ql_sp_pro_sku.setText(String.format(getResources().getString(R.string.sel_pro_sku)
                                    , getStrings(mShopDetailsEntity.getShopPropertyBean().getProps().get(0).getPropName())));
                        }
                    });
                }
                break;
            //点击服务标签
            case R.id.ll_layout_pro_sc_tag:
                if (mShopDetailsEntity != null && mIvMoreTag.getVisibility() == VISIBLE) {
                    if (alertDialog == null) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomTransDialog);
                        View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_sevicetag_dialog, null, false);
                        FlexboxLayout flex_communal_tag = dialogView.findViewById(R.id.flex_communal_tag);
                        setSeviceTag(mShopDetailsEntity.getShopPropertyBean(), flex_communal_tag, flex_communal_tag);
                        alertDialog = builder.create();
                        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        alertDialog.setCanceledOnTouchOutside(true);
                        alertDialog.show();
                        Window window = alertDialog.getWindow();
                        window.getDecorView().setPadding(0, 0, 0, 0);
                        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        window.setGravity(Gravity.BOTTOM);//底部出现
                        window.setContentView(dialogView);
                    } else {
                        alertDialog.show();
                    }
                }
                break;
        }
    }

    private void buyGoItCheckStatus() {
        if (userId > 0) {
            buyGoIt();
        } else {
            getLoginStatus(getActivity());
        }
    }

    //立即购买
    private void buyGoIt() {
        if (shopCarGoodsSkuDif != null) {
            if (mGroupShopDetailsBean != null) {
                Intent intent = new Intent(QualityGroupShopDetailActivity.this, DirectIndentWriteActivity.class);
                mGroupShopDetailsBean.setGpSkuId(shopCarGoodsSkuDif.getSaleSkuId());
                mGroupShopDetailsBean.setGpPicUrl(shopCarGoodsSkuDif.getPicUrl());
                mGroupShopDetailsBean.setProductSkuValue(shopCarGoodsSkuDif.getValuesName());
                mGroupShopDetailsBean.setGpPrice(shopCarGoodsSkuDif.getPrice() + "");
                intent.putExtra("gpShopInfo", new Gson().toJson(mGroupShopDetailsEntity.getGroupShopDetailsBean()));
                startActivity(intent);
            }
        } else {
            if (skuDialog != null) {
                skuDialog.show(true, "确定");
                skuDialog.getGoodsSKu(shopCarGoodsSku -> {
                    shopCarGoodsSkuDif = shopCarGoodsSku;
                    if (shopCarGoodsSku != null) {
                        tv_ql_sp_pro_sku.setTextColor(getResources().getColor(R.color.text_black_t));
                        tv_ql_sp_pro_sku.setText(("已选：" + shopCarGoodsSku.getValuesName()));
                        buyGoItCheckStatus();
                    }
                });
            }
        }
    }

    //    七鱼客服
    private void skipServiceDataInfo(GroupShopDetailsBean groupShopDetailsBean) {
        QyProductIndentInfo qyProductIndentInfo = null;
        String sourceTitle = "";
        String sourceUrl = "";
        if (groupShopDetailsBean != null) {
            qyProductIndentInfo = new QyProductIndentInfo();
            sourceUrl = sharePageUrl + groupShopDetailsBean.getGpInfoId();
            sourceTitle = "我的拼团：" + groupShopDetailsBean.getName();
            qyProductIndentInfo.setUrl(sourceUrl);
            qyProductIndentInfo.setTitle(getStrings(groupShopDetailsBean.getSubtitle()));
            qyProductIndentInfo.setPicUrl(groupShopDetailsBean.getGpPicUrl());
            qyProductIndentInfo.setDesc(getStrings(groupShopDetailsBean.getName()));
            qyProductIndentInfo.setNote("¥" + groupShopDetailsBean.getGpPrice());
        }
        QyServiceUtils.getQyInstance().openQyServiceChat(this, sourceTitle, sourceUrl, qyProductIndentInfo);
    }

    //参团
    public void isCanJoinGroup(final GroupShopJoinBean groupShopjoinBean) {
        Map<String, Object> params = new HashMap<>();
        params.put("uid", userId);
        params.put("gpInfoId", groupShopjoinBean.getGpInfoId());
        NetLoadUtils.getNetInstance().loadNetDataPost(this, GROUP_SHOP_JOIN_NRE_USER, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                RequestStatus requestStatus = gson.fromJson(result, RequestStatus.class);
                if (requestStatus != null) {
                    if (requestStatus.getCode().equals(SUCCESS_CODE)) {
                        mGroupShopDetailsBean.setGpStatus(2);
                        mGroupShopDetailsBean.setGpRecordId(groupShopjoinBean.getGpRecordId());
                        Intent intent = new Intent(QualityGroupShopDetailActivity.this, DirectIndentWriteActivity.class);
                        intent.putExtra("gpShopInfo", new Gson().toJson(mGroupShopDetailsBean));
                        startActivity(intent);
                    } else {
                        showToastRequestMsg(QualityGroupShopDetailActivity.this, requestStatus);
                    }
                } else {
                    showToast(QualityGroupShopDetailActivity.this, R.string.invalidData);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        isPause = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isPause) {
            getGroupShopPerson();
            isPause = false;
        }
    }
}
