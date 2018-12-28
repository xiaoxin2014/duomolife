package com.amkj.dmsh.dominant.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.base.TinkerBaseApplicationLike;
import com.amkj.dmsh.bean.RequestStatus;
import com.amkj.dmsh.constant.CommunalAdHolderView;
import com.amkj.dmsh.constant.CommunalDetailBean;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.ConstantVariable;
import com.amkj.dmsh.constant.TabEntity;
import com.amkj.dmsh.constant.UMShareAction;
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
import com.amkj.dmsh.dominant.fragment.GroupCustomerServiceFragment;
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
import com.amkj.dmsh.shopdetails.bean.GoodsCommentEntity;
import com.amkj.dmsh.shopdetails.bean.GoodsCommentEntity.GoodsCommentBean;
import com.amkj.dmsh.shopdetails.bean.ShopDetailsEntity;
import com.amkj.dmsh.shopdetails.fragment.DirectImgArticleFragment;
import com.amkj.dmsh.user.activity.UserPagerActivity;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;
import com.amkj.dmsh.utils.webformatdata.CommunalWebDetailUtils;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.google.android.flexbox.FlexboxLayout;
import com.google.gson.Gson;
import com.melnykov.fab.FloatingActionButton;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tencent.bugly.beta.tinker.TinkerManager;
import com.umeng.socialize.UMShareAPI;

import org.greenrobot.eventbus.EventBus;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.iwgang.countdownview.CountdownView;
import cn.iwgang.countdownview.DynamicConfig;
import me.jessyan.autosize.utils.AutoSizeUtils;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.getStringChangeBoolean;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.getStringsChNPrice;
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
import static com.amkj.dmsh.constant.Url.Q_SHOP_DETAILS;
import static com.amkj.dmsh.constant.Url.Q_SHOP_DETAILS_COMMENT;
import static com.amkj.dmsh.constant.Url.SHOP_EVA_LIKE;
import static com.amkj.dmsh.find.activity.ImagePagerActivity.IMAGE_DEF;
import static com.amkj.dmsh.utils.ProductLabelCreateUtils.getLabelInstance;

;

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
    ConvenientBanner banner_ql_gp_sp_details;
    @BindView(R.id.tv_ql_gp_sp_new_detail)
    TextView tv_ql_gp_sp_new_detail;
    //    商品标签
    @BindView(R.id.tv_open_pro_label)
    TextView tv_open_pro_label;
    @BindView(R.id.rel_ql_gp_sp_time)
    RelativeLayout rel_ql_gp_sp_time;
    //    计时器状态
    @BindView(R.id.tv_pro_time_detail_status)
    TextView tv_pro_time_detail_status;
    //    计时器状态
    @BindView(R.id.ct_pro_show_time_detail)
    CountdownView ct_pro_show_time_detail;
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
    //    标签布局
    @BindView(R.id.ll_layout_pro_tag)
    LinearLayout ll_layout_pro_tag;
    @BindView(R.id.flex_communal_tag)
    FlexboxLayout flex_communal_tag;
    @BindView(R.id.tv_header_title)
    TextView tv_header_titleAll;
    @BindView(R.id.communal_recycler_wrap)
    RecyclerView communal_recycler_wrap;
    @BindView(R.id.ctb_ql_gp_sp_tab)
    CommonTabLayout ctb_ql_gp_sp_tab;
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
    NestedScrollView nsv_gp_detail;
    @BindView(R.id.ll_gp_detail_bottom)
    LinearLayout ll_gp_detail_bottom;

    private List<GroupShopJoinBean> groupShopJoinList = new ArrayList<>();
    //    拼团规则
    private List<CommunalDetailObjectBean> gpRuleList = new ArrayList<>();
    //    弹框
    private List<CommunalDetailObjectBean> diaRuleList = new ArrayList<>();
    //    轮播图片视频
    private List<CommunalADActivityBean> imagesVideoList = new ArrayList<>();
    //    商品评论
    private List<GoodsCommentBean> goodsComments = new ArrayList();
    private DirectEvaluationAdapter directEvaluationAdapter;
    private ShopJoinGroupView shopJoinGroupView;
    private ShopCommentHeaderView shopCommentHeaderView;
    private String gpInfoId;
    private FragmentManager fragmentManager;
    private String[] title = {"图文详情", "服务承诺"};
    //tab集合
    private ArrayList<CustomTabEntity> tabs = new ArrayList<>();
    private FragmentTransaction transaction;
    private Fragment lastFragment;
    private GroupShopDetailsEntity shopDetailsEntity;
    private GroupShopCommunalInfoEntity groupShopCommunalInfoEntity;
    private AlertDialog alertDialog;
    private String gpRecordId;
    private QualityGroupShareEntity qualityGroupShareEntity;
    private String shareJoinGroup = "shareJoinGroup";
    private String normalJoinGroup = "normalJoinGroup";
    private boolean isPause;
    private String sharePageUrl = BASE_SHARE_PAGE_TWO + "m/template/share_template/groupDetail.html?id=";
    private ConstantMethod constantMethod;
    private CBViewHolderCreator cbViewHolderCreator;
    private boolean invitePartnerJoin;
    private GroupShopDetailsBean groupShopDetailsBean;
    private String orderNo;
    private int screenHeight;

    @Override
    protected int getContentView() {
        return R.layout.activity_ql_gp_sp_details;
    }

    @Override
    protected void initViews() {
        tv_header_titleAll.setVisibility(GONE);
        Intent intent = getIntent();
        gpInfoId = intent.getStringExtra("gpInfoId");
        gpRecordId = intent.getStringExtra("gpRecordId");
        orderNo = intent.getStringExtra("orderNo");
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
        smart_refresh_ql_sp_details.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                loadData();
            }
        });
        communal_recycler_wrap.setLayoutManager(new LinearLayoutManager(QualityGroupShopDetailActivity.this));
        communal_recycler_wrap.addItemDecoration(new ItemDecoration.Builder()
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_gray_f_two_px).create());
        directEvaluationAdapter = new DirectEvaluationAdapter(QualityGroupShopDetailActivity.this, goodsComments);
        directEvaluationAdapter.setHeaderAndEmpty(true);
        View joinGroupView = LayoutInflater.from(QualityGroupShopDetailActivity.this).inflate(R.layout.layout_communal_recycler_wrap, null);
        shopJoinGroupView = new ShopJoinGroupView();
        ButterKnife.bind(shopJoinGroupView, joinGroupView);
        shopJoinGroupView.initView();
//        评论头部
        View commentHeaderView = LayoutInflater.from(QualityGroupShopDetailActivity.this).inflate(R.layout.layout_shop_comment_header, null);
        shopCommentHeaderView = new ShopCommentHeaderView();
        ButterKnife.bind(shopCommentHeaderView, commentHeaderView);
        shopCommentHeaderView.initView();
        directEvaluationAdapter.addHeaderView(joinGroupView);
        directEvaluationAdapter.addHeaderView(commentHeaderView);
        directEvaluationAdapter.getHeaderLayout();
        directEvaluationAdapter.getFooterLayout();
        communal_recycler_wrap.setNestedScrollingEnabled(false);
        communal_recycler_wrap.setAdapter(directEvaluationAdapter);

        directEvaluationAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.img_direct_avatar:
                        GoodsCommentBean goodsCommentBean = (GoodsCommentBean) view.getTag(R.id.iv_avatar_tag);
                        if (goodsCommentBean != null) {
                            Intent intent = new Intent(QualityGroupShopDetailActivity.this, UserPagerActivity.class);
                            intent.putExtra("userId", String.valueOf(goodsCommentBean.getUserId()));
                            startActivity(intent);
                        }
                        break;
                    case R.id.tv_eva_count:
                        goodsCommentBean = (GoodsCommentBean) view.getTag();
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
        fragmentManager = getSupportFragmentManager();
        for (int i = 0; i < title.length; i++) {
            tabs.add(new TabEntity(title[i], 0, 0));
        }
        changePage("ImgArticleShop");
        ctb_ql_gp_sp_tab.setTabData(tabs);
        ctb_ql_gp_sp_tab.setTextSize(AutoSizeUtils.mm2px(mAppContext, 30));
        ctb_ql_gp_sp_tab.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                switch (position) {
                    case 0:
                        changePage("ImgArticleShop");
                        break;
                    case 1:
                        changePage("ClientService");
                        break;
                }
            }

            @Override
            public void onTabReselect(int position) {
            }
        });
        Drawable drawable = getResources().getDrawable(R.drawable.clock_time_icon_b);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); //设置边界
        tv_pro_time_detail_status.setCompoundDrawables(drawable, null, null, null);

        DynamicConfig.Builder dynamicDetails = new DynamicConfig.Builder();
        dynamicDetails.setSuffixTextSize(AutoSizeUtils.mm2px(mAppContext, 22));
        dynamicDetails.setTimeTextSize(AutoSizeUtils.mm2px(mAppContext, 22));
        ct_pro_show_time_detail.dynamicShow(dynamicDetails.build());
        flex_communal_tag.setDividerDrawable(getResources().getDrawable(R.drawable.item_divider_product_tag));
        flex_communal_tag.setShowDivider(FlexboxLayout.SHOW_DIVIDER_MIDDLE);
        TinkerBaseApplicationLike app = (TinkerBaseApplicationLike) TinkerManager.getTinkerApplicationLike();
        screenHeight = app.getScreenHeight();
        nsv_gp_detail.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView nestedScrollView, int i, int newY, int i2, int oldY) {
                if (newY > screenHeight * 1.5) {
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
            }
        });
        download_btn_communal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nsv_gp_detail.scrollTo(0, 0);
                download_btn_communal.hide(false);
            }
        });
    }

    private void changePage(String tag) {
        Fragment fragmentByTag = fragmentManager.findFragmentByTag(tag);
        transaction = fragmentManager.beginTransaction();
        if (fragmentByTag != null && fragmentByTag.isAdded()) {
            if (lastFragment != null) {
                transaction.hide(lastFragment).commit();
            }
            transaction.show(fragmentByTag);
            lastFragment = fragmentByTag;
            fragmentByTag.onResume();
        } else {
            Fragment fragment = null;
            switch (tag) {
                case "ImgArticleShop":
                    fragment = BaseFragment.newInstance(DirectImgArticleFragment.class, null, null);
                    break;
                case "ClientService":
                    fragment = BaseFragment.newInstance(GroupCustomerServiceFragment.class, null, null);
                    break;
            }
            if (lastFragment != null) {
                if (fragment.isAdded()) {
                    transaction.hide(lastFragment).show(fragment).commit(); // 隐藏当前的fragment，显示下一个
                } else {
                    transaction.hide(lastFragment).add(R.id.fl_ql_sp_container, fragment, tag).commit();
                }
            } else {
                transaction.add(R.id.fl_ql_sp_container, fragment, tag).commit();
            }
            lastFragment = fragment;
        }
    }

    @Override
    protected void loadData() {
//        拼团列表
        getGroupShopPerson();
        getGroupShopDetails();
//        标签 拼团规则 服务承诺
        getGroupCommunalInfo();
    }

    @Override
    protected View getLoadView() {
        return fl_group_product;
    }

    @Override
    protected boolean isAddLoad() {
        return true;
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
        tv_eva_like.setText(ConstantMethod.getNumCount(tv_eva_like.isSelected(), goodsCommentBean.isFavor(), goodsCommentBean.getLikeNum(), "赞"));
    }

    private void getShopDetails(GroupShopDetailsBean groupShopDetailsBean) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", groupShopDetailsBean.getProductId());
        NetLoadUtils.getNetInstance().loadNetDataPost(this, Q_SHOP_DETAILS, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                ShopDetailsEntity shopDetailsEntity = gson.fromJson(result, ShopDetailsEntity.class);
                if (shopDetailsEntity != null) {
                    if (shopDetailsEntity.getCode().equals(SUCCESS_CODE)) {
                        EventBus.getDefault().post(new EventMessage("ImgArticleShop", shopDetailsEntity.getShopPropertyBean().getItemBody()));
                    }
                }
            }
        });
    }

    //  获取商品评论
    private void getGroupShopComment(GroupShopDetailsBean groupShopDetailsEntity) {
        Map<String, Object> params = new HashMap<>();
        params.put("showCount", 2);
        params.put("currentPage", 1);
        params.put("id", groupShopDetailsEntity.getProductId());
        if (userId > 0) {
            params.put("uid", userId);
        }
        NetLoadUtils.getNetInstance().loadNetDataPost(this, Q_SHOP_DETAILS_COMMENT, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                goodsComments.clear();
                Gson gson = new Gson();
                GoodsCommentEntity goodsCommentEntity = gson.fromJson(result, GoodsCommentEntity.class);
                if (goodsCommentEntity != null) {
                    if (goodsCommentEntity.getCode().equals(SUCCESS_CODE)) {
                        communal_recycler_wrap.setVisibility(View.VISIBLE);
                        goodsComments.addAll(goodsCommentEntity.getGoodsComments());
                    } else if (!goodsCommentEntity.getCode().equals(EMPTY_CODE)) {
                        communal_recycler_wrap.setVisibility(GONE);
                        showToast(QualityGroupShopDetailActivity.this, goodsCommentEntity.getMsg());
                    }
                    setCommentCount(goodsCommentEntity);
                    directEvaluationAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void setCommentCount(GoodsCommentEntity goodsCommentEntity) {
        if (goodsCommentEntity.getEvaluateCount() < 1) {
            shopCommentHeaderView.rel_pro_comment.setVisibility(GONE);
        } else {
            shopCommentHeaderView.rel_pro_comment.setVisibility(View.VISIBLE);
            shopCommentHeaderView.tv_shop_comment_count.setText(String.format(getResources().getString(R.string.product_comment_count), goodsCommentEntity.getEvaluateCount()));
        }
    }

    private void getGroupShopPerson() {
        if (!TextUtils.isEmpty(gpRecordId)) {
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
                            GroupShopJoinBean groupShopJoinBean = new GroupShopJoinBean();
                            groupShopJoinBean.setCurrentTime(qualityGroupShareEntity.getCurrentTime());
                            if (invitePartnerJoin) {
                                groupShopJoinBean.setGpCreateTime(qualityGroupShareEntity.getCurrentTime());
                                List<MemberListBean> memberListBeans = new ArrayList<>(qualityGroupShareBean.getMemberList());
                                int leftParticipant = qualityGroupShareBean.getMemberCount() - qualityGroupShareBean.getMemberList().size();
                                for (int i = 0; i < leftParticipant; i++) {
                                    MemberListBean memberListBean = new MemberListBean();
                                    memberListBean.setAvatar("android.resource://com.amkj.dmsh/drawable/" + R.drawable.dm_gp_join);
                                    memberListBeans.add(memberListBean);
                                }
                                groupShopJoinBean.setItemType(TYPE_2);
                                groupShopJoinBean.setMemberListBeans(memberListBeans);
                                tv_sp_details_join_buy_price.setVisibility(GONE);
                                if (leftParticipant < 1) {
                                    ll_group_buy.setEnabled(false);
                                    groupShopJoinBean.setGpEndTime(qualityGroupShareEntity.getCurrentTime());
                                } else {
                                    groupShopJoinBean.setGpEndTime(qualityGroupShareBean.getGpEndTime());
                                    ll_group_buy.setEnabled(true);
                                }
                                tv_sp_details_join_count.setText("邀请好友");
                                tv_sp_details_ol_buy_price.setVisibility(GONE);
                                tv_sp_details_ol_buy.setText("全部拼团");
                            } else {
                                tv_sp_details_join_buy_price.setVisibility(View.VISIBLE);
                                groupShopJoinBean.setItemType(ConstantVariable.TYPE_1);
                                setGpDataInfo(qualityGroupShareBean);
                            }
                            groupShopJoinBean.setGpInfoId(qualityGroupShareBean.getGpInfoId());
                            groupShopJoinBean.setGpRecordId(Integer.parseInt(qualityGroupShareBean.getGpRecordId()));
                            groupShopJoinList.add(groupShopJoinBean);
                            shopJoinGroupView.joinGroupAdapter.removeAllHeaderView();
                            if (groupShopJoinList.size() > 0) {
                                shopJoinGroupView.joinGroupAdapter.addHeaderView(shopJoinGroupView.headerView);
                            }
                            shopJoinGroupView.joinGroupAdapter.setNewData(groupShopJoinList);
                        } else if (qualityGroupShareEntity.getCode().equals(EMPTY_CODE)) {
                            showToast(QualityGroupShopDetailActivity.this, R.string.unConnectedNetwork);
                            ll_group_buy.setEnabled(false);
                            tv_sp_details_join_count.setText("拼团失败");
                        } else {
                            showToast(QualityGroupShopDetailActivity.this, qualityGroupShareEntity.getMsg());
                            ll_group_buy.setEnabled(false);
                            tv_sp_details_join_count.setText("拼团失败");
                        }
                    }
                }

                @Override
                public void onError(Throwable throwable) {
                    showToast(QualityGroupShopDetailActivity.this, R.string.connectedFaile);
                }

                @Override
                public void netClose() {
                    showToast(QualityGroupShopDetailActivity.this, R.string.unConnectedNetwork);
                }
            });
        } else {
            Map<String, Object> params = new HashMap<>();
            params.put("gpInfoId", gpInfoId);
            if (userId > 0) {
                params.put("uid", userId);
            }
            NetLoadUtils.getNetInstance().loadNetDataPost(this, GROUP_SHOP_OPEN_PERSON, params, new NetLoadListenerHelper() {
                @Override
                public void onSuccess(String result) {
                    smart_refresh_ql_sp_details.finishRefresh();
                    groupShopJoinList.clear();
                    Gson gson = new Gson();
                    GroupShopJoinEntity groupShopJoinEntity = gson.fromJson(result, GroupShopJoinEntity.class);
                    if (groupShopJoinEntity != null) {
                        if (groupShopJoinEntity.getCode().equals(SUCCESS_CODE)) {
                            for (int i = 0; i < groupShopJoinEntity.getGroupShopJoinBeanList().size(); i++) {
                                GroupShopJoinBean groupShopJoinBean = groupShopJoinEntity.getGroupShopJoinBeanList().get(i);
                                groupShopJoinBean.setCurrentTime(groupShopJoinEntity.getCurrentTime());
                                groupShopJoinBean.setNumOrder(i);
                                groupShopJoinList.add(groupShopJoinBean);
                            }
                        }
                        shopJoinGroupView.joinGroupAdapter.removeAllHeaderView();
                        if (groupShopJoinList.size() > 0) {
                            shopJoinGroupView.joinGroupAdapter.addHeaderView(shopJoinGroupView.headerView);
                        }
                        shopJoinGroupView.joinGroupAdapter.setNewData(groupShopJoinList);
                    }
                }

                @Override
                public void onNotNetOrException() {
                    smart_refresh_ql_sp_details.finishRefresh();
                }
            });
        }
    }

    private void setGpDataInfo(QualityGroupShareBean qualityGroupShareEntity) {
        tv_sp_details_join_buy_price.setText(getStrings(qualityGroupShareEntity.getGpPrice()));
        tv_sp_details_join_count.setText("立即参团");
    }

    private void getGroupCommunalInfo() {
        NetLoadUtils.getNetInstance().loadNetDataPost(this, GROUP_SHOP_COMMUNAL, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                smart_refresh_ql_sp_details.finishRefresh();
                Gson gson = new Gson();
                groupShopCommunalInfoEntity = gson.fromJson(result, GroupShopCommunalInfoEntity.class);
                if (groupShopCommunalInfoEntity != null) {
                    if (groupShopCommunalInfoEntity.getCode().equals(SUCCESS_CODE)) {
                        setCommunalInfo(groupShopCommunalInfoEntity.getGroupShopCommunalInfoBean());
                    }
                }
            }

            @Override
            public void onNotNetOrException() {
                smart_refresh_ql_sp_details.finishRefresh();
            }
        });
    }

    private void setCommunalInfo(GroupShopCommunalInfoBean groupShopCommunalInfoBean) {
        List<CommunalDetailBean> gpRuleBeanList = groupShopCommunalInfoBean.getGpRule();
//        拼团规则
        if (gpRuleBeanList != null && gpRuleBeanList.size() > 0) {
            gpRuleList.clear();
            gpRuleList.addAll(CommunalWebDetailUtils.getCommunalWebInstance().getWebDetailsFormatDataList(groupShopCommunalInfoBean.getGpRule()));
            shopCommentHeaderView.gpRuleDetailsAdapter.notifyDataSetChanged();
        }
        final String[] tagArray = groupShopCommunalInfoBean.getServicePromiseTitle().split(",");
        if (tagArray.length > 0) {
            ll_layout_pro_tag.setVisibility(View.VISIBLE);
            flex_communal_tag.removeAllViews();
            for (int i = 0; i < (tagArray.length > 3 ? 3 : tagArray.length); i++) {
                flex_communal_tag.addView(getLabelInstance().createProductTag(QualityGroupShopDetailActivity.this, tagArray[i]));
            }
        } else {
            ll_layout_pro_tag.setVisibility(GONE);
        }
    }

    private void getGroupShopDetails() {
        Map<String, Object> params = new HashMap<>();
        params.put("gpInfoId", gpInfoId);
        NetLoadUtils.getNetInstance().loadNetDataPost(QualityGroupShopDetailActivity.this, GROUP_SHOP_DETAILS,
                params, new NetLoadListenerHelper() {
                    @Override
                    public void onSuccess(String result) {
                        smart_refresh_ql_sp_details.finishRefresh();
                        Gson gson = new Gson();
                        shopDetailsEntity = gson.fromJson(result, GroupShopDetailsEntity.class);
                        if (shopDetailsEntity != null) {
                            if (shopDetailsEntity.getCode().equals(SUCCESS_CODE)) {
                                setGroupShopDetailsData(shopDetailsEntity);
                                //         获取商品评论列表
                                groupShopDetailsBean = shopDetailsEntity.getGroupShopDetailsBean();
                                getGroupShopComment(groupShopDetailsBean);
                                //        获取商品详情
                                getShopDetails(groupShopDetailsBean);
                            } else if (!shopDetailsEntity.getCode().equals(EMPTY_CODE)) {
                                showToast(QualityGroupShopDetailActivity.this, shopDetailsEntity.getMsg());
                            }
                        }
                        NetLoadUtils.getNetInstance().showLoadSir(loadService, groupShopDetailsBean, shopDetailsEntity);
                    }

                    @Override
                    public void onNotNetOrException() {
                        smart_refresh_ql_sp_details.finishRefresh();
                        NetLoadUtils.getNetInstance().showLoadSir(loadService, groupShopDetailsBean, shopDetailsEntity);
                    }

                    @Override
                    public void netClose() {
                        showToast(QualityGroupShopDetailActivity.this, R.string.unConnectedNetwork);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        showToast(QualityGroupShopDetailActivity.this, R.string.invalidData);
                    }
                });
    }

    private void setGroupShopDetailsData(GroupShopDetailsEntity groupShopDetailsEntity) {
        GroupShopDetailsBean groupShopDetailsBean = groupShopDetailsEntity.getGroupShopDetailsBean();
        String[] images = groupShopDetailsBean.getImages().split(",");
        imagesVideoList.clear();
        CommunalADActivityBean communalADActivityBean;
        List<String> imageList = new ArrayList<>();
        if (images.length != 0) {
            imageList.addAll(Arrays.asList(images));
            for (String imagePath : imageList) {
                communalADActivityBean = new CommunalADActivityBean();
                communalADActivityBean.setPicUrl(imagePath);
                imagesVideoList.add(communalADActivityBean);
            }
        } else {
            communalADActivityBean = new CommunalADActivityBean();
            communalADActivityBean.setPicUrl(getStrings(groupShopDetailsBean.getCoverImage()));
            imageList.add(getStrings(groupShopDetailsBean.getCoverImage()));
            imagesVideoList.add(communalADActivityBean);
        }
        if (groupShopDetailsBean.getRange() == 1) {
            tv_ql_gp_sp_new_detail.setVisibility(View.VISIBLE);
            shopJoinGroupView.tv_partner_join.setText(R.string.ql_new_gp_tint);
        } else if (groupShopDetailsBean.getRange() == 0) {
            tv_ql_gp_sp_new_detail.setVisibility(View.GONE);
            shopJoinGroupView.tv_partner_join.setText(R.string.ql_normal_gp_tint);
        }

        if (cbViewHolderCreator == null) {
            cbViewHolderCreator = new CBViewHolderCreator() {
                @Override
                public Holder createHolder(View itemView) {
                    return new CommunalAdHolderView(itemView, QualityGroupShopDetailActivity.this, false);
                }

                @Override
                public int getLayoutId() {
                    return R.layout.layout_ad_image_video;
                }
            };
        }
        banner_ql_gp_sp_details.setPages(this, cbViewHolderCreator, imagesVideoList).setCanLoop(true)
                .setPageIndicator(new int[]{R.drawable.unselected_radius, R.drawable.selected_radius});
        banner_ql_gp_sp_details.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (imagesVideoList.size() > position) {
                    if (imageList.size() > 0 && position < imageList.size()) {
                        showImageActivity(QualityGroupShopDetailActivity.this, IMAGE_DEF,
                                position,
                                imageList);
                    }
                }
            }
        });
        tv_open_pro_label.setText(getStrings(groupShopDetailsBean.getGoodsAreaLabel()));
        setCountTime(groupShopDetailsEntity);
        if (!ConstantMethod.isEndOrStartTime(groupShopDetailsEntity.getCurrentTime()
                , groupShopDetailsBean.getGpEndTime())) {
            getConstant();
            constantMethod.createSchedule();
            constantMethod.setRefreshTimeListener(new ConstantMethod.RefreshTimeListener() {
                @Override
                public void refreshTime() {
                    shopDetailsEntity.setSecond(shopDetailsEntity.getSecond() + 1);
                    setCountTime(shopDetailsEntity);
                }
            });
        } else {
            if (constantMethod != null) {
                constantMethod.stopSchedule();
            }
        }
        tv_ql_sp_pro_name.setText(getStrings(groupShopDetailsBean.getName()));
        tv_gp_sp_per_count.setText(getStrings(groupShopDetailsBean.getGpType()));
        tv_gp_sp_per_price.setText(getStringsChNPrice(this, groupShopDetailsBean.getGpPrice()));
        tv_gp_sp_nor_price.setText("单买价 ￥" + groupShopDetailsBean.getPrice());
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

    private void setCountTime(GroupShopDetailsEntity groupShopDetailsEntity) {
        GroupShopDetailsBean groupShopDetailsBean = groupShopDetailsEntity.getGroupShopDetailsBean();
        if (isTimeStart(groupShopDetailsEntity, groupShopDetailsBean)) {
            try {
                //格式化结束时间
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
                Date dateEnd = formatter.parse(groupShopDetailsBean.getGpEndTime());
                Date dateCurrent;
                if (!TextUtils.isEmpty(groupShopDetailsEntity.getCurrentTime())) {
                    dateCurrent = formatter.parse(groupShopDetailsEntity.getCurrentTime());
                } else {
                    dateCurrent = new Date();
                }
                DynamicConfig.Builder dynamic = new DynamicConfig.Builder();
                dynamic.setTimeTextColor(getResources().getColor(R.color.text_normal_red));
                dynamic.setSuffixTextColor(getResources().getColor(R.color.text_login_gray_s));
                ct_pro_show_time_detail.dynamicShow(dynamic.build());
                ct_pro_show_time_detail.updateShow(dateEnd.getTime() - dateCurrent.getTime() - groupShopDetailsEntity.getSecond() * 1000);
                tv_pro_time_detail_status.setText("距结束");
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            try {
                //格式化开始时间
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
                Date dateStart = formatter.parse(groupShopDetailsBean.getGpStartTime());
                Date dateCurrent;
                if (!TextUtils.isEmpty(groupShopDetailsEntity.getCurrentTime())) {
                    dateCurrent = formatter.parse(groupShopDetailsEntity.getCurrentTime());
                } else {
                    dateCurrent = new Date();
                }
                DynamicConfig.Builder dynamic = new DynamicConfig.Builder();
                dynamic.setTimeTextColor(getResources().getColor(R.color.text_login_gray_s));
                dynamic.setSuffixTextColor(getResources().getColor(R.color.text_login_gray_s));
                ct_pro_show_time_detail.dynamicShow(dynamic.build());
                ct_pro_show_time_detail.updateShow(dateStart.getTime() - dateCurrent.getTime() - groupShopDetailsEntity.getSecond() * 1000);
                tv_pro_time_detail_status.setText("距开始");
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if (!ConstantMethod.isEndOrStartTimeAddSeconds(groupShopDetailsEntity.getCurrentTime()
                , groupShopDetailsBean.getGpEndTime()
                , groupShopDetailsEntity.getSecond())) {
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

    private boolean isTimeStart(GroupShopDetailsEntity groupShopDetailsEntity, GroupShopDetailsBean groupShopDetailsBean) {
        try {
            //格式化开始时间
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
            Date dateStart = formatter.parse(groupShopDetailsBean.getGpStartTime());
            Date dateCurrent;
            if (!TextUtils.isEmpty(groupShopDetailsEntity.getCurrentTime())) {
                dateCurrent = formatter.parse(groupShopDetailsEntity.getCurrentTime());
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

    @Override
    protected void postEventResult(@NonNull EventMessage message) {
        if (message.type.equals("refreshGroupShopPerson") || message.type.equals("refreshGroupShop")) {
            getGroupShopPerson();
        }
    }

    @OnClick(R.id.ll_layout_pro_tag)
    void openDialog(View view) {
        if (groupShopCommunalInfoEntity != null) {
            if (alertDialog == null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(QualityGroupShopDetailActivity.this, R.style.CustomTransDialog);
                View dialogView = LayoutInflater.from(QualityGroupShopDetailActivity.this).inflate(R.layout.layout_ql_gp_dialog, communal_recycler_wrap, false);
                initDialogView(dialogView);
                alertDialog = builder.create();
                alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                alertDialog.setCanceledOnTouchOutside(true);
                alertDialog.show();
                TinkerBaseApplicationLike app = (TinkerBaseApplicationLike) TinkerManager.getTinkerApplicationLike();
                int dialogHeight = (int) (app.getScreenHeight() * 0.618 + 1);
                Window window = alertDialog.getWindow();
                window.getDecorView().setPadding(0, 0, 0, 0);
                window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, dialogHeight);
                window.setGravity(Gravity.BOTTOM);//底部出现
                window.setContentView(dialogView);
            } else {
                alertDialog.show();
            }
        }
    }

    private void initDialogView(View dialogView) {
        FlexboxLayout flex_communal_tag = dialogView.findViewById(R.id.flex_communal_tag);
        RecyclerView communal_recycler_wrap = (RecyclerView) dialogView.findViewById(R.id.communal_recycler_wrap);
        TextView tv_pro_buy_detail_text = (TextView) dialogView.findViewById(R.id.tv_pro_buy_detail_text);
        TextView tv_title_text = (TextView) dialogView.findViewById(R.id.tv_title_text);
        tv_title_text.setText("服务承诺");
        tv_pro_buy_detail_text.setVisibility(GONE);
        communal_recycler_wrap.setLayoutManager(new LinearLayoutManager(QualityGroupShopDetailActivity.this));
        communal_recycler_wrap.setNestedScrollingEnabled(false);
        CommunalDetailAdapter gpRuleDetailsAdapter = new CommunalDetailAdapter(QualityGroupShopDetailActivity.this, diaRuleList);
        communal_recycler_wrap.setLayoutManager(new LinearLayoutManager(QualityGroupShopDetailActivity.this));
        communal_recycler_wrap.setAdapter(gpRuleDetailsAdapter);
        GroupShopCommunalInfoBean groupShopCommunalInfoBean = groupShopCommunalInfoEntity.getGroupShopCommunalInfoBean();
        //        标签
        final String[] tagArray = groupShopCommunalInfoBean.getServicePromiseTitle().split(",");
        if (tagArray.length > 0) {
            flex_communal_tag.setVisibility(View.VISIBLE);
            flex_communal_tag.setDividerDrawable(getResources().getDrawable(R.drawable.item_divider_product_tag));
            flex_communal_tag.setShowDivider(FlexboxLayout.SHOW_DIVIDER_MIDDLE);
            flex_communal_tag.removeAllViews();
            for (int i = 0; i < tagArray.length; i++) {
                flex_communal_tag.addView(getLabelInstance().createProductTag(QualityGroupShopDetailActivity.this, tagArray[i]));
            }
        } else {
            flex_communal_tag.setVisibility(GONE);
        }

        List<GroupShopCommunalInfoBean.ServicePromiseBean> gpRuleBeanList = groupShopCommunalInfoBean.getServicePromise();
//        拼团规则
        if (gpRuleBeanList.size() > 0) {
            CommunalDetailObjectBean communalDetailObjectBean;
            diaRuleList.clear();
            for (int i = 0; i < gpRuleBeanList.size(); i++) {
                communalDetailObjectBean = new CommunalDetailObjectBean();
                GroupShopCommunalInfoBean.ServicePromiseBean directGoodsServerBean = gpRuleBeanList.get(i);
                if (directGoodsServerBean.getType().equals("text")) {
                    communalDetailObjectBean.setContent(directGoodsServerBean.getContent());
                    diaRuleList.add(communalDetailObjectBean);
                }
            }
            gpRuleDetailsAdapter.setNewData(diaRuleList);
        }
    }

    @OnClick(R.id.tv_life_back)
    void goBack(View view) {
        finish();
    }

    @OnClick(R.id.iv_img_service)
    void skipService(View view) {
        if (shopDetailsEntity != null && shopDetailsEntity.getGroupShopDetailsBean() != null) {
            skipServiceDataInfo(shopDetailsEntity.getGroupShopDetailsBean());
        } else {
            skipServiceDataInfo(null);
        }
    }

    @OnClick(R.id.iv_img_share)
    void setShare(View view) {
        if (shopDetailsEntity != null && shopDetailsEntity.getGroupShopDetailsBean() != null) {
            GroupShopDetailsBean groupShopDetailsBean = shopDetailsEntity.getGroupShopDetailsBean();
            new UMShareAction(QualityGroupShopDetailActivity.this
                    , !TextUtils.isEmpty(groupShopDetailsBean.getGpPicUrl()) ? groupShopDetailsBean.getGpPicUrl() : groupShopDetailsBean.getCoverImage()
                    , groupShopDetailsBean.getName()
                    , "超值两人团，好货又便宜。"
                    , sharePageUrl + groupShopDetailsBean.getGpInfoId(), "pages/groupDetails/groupDetails?id=" + groupShopDetailsBean.getGpInfoId());
        }
    }

    //  开团
    @OnClick(R.id.ll_group_buy)
    void skipBuy(View view) {
        if (userId > 0) {
//            去参团
            if (!TextUtils.isEmpty(gpRecordId) && qualityGroupShareEntity != null
                    && qualityGroupShareEntity.getQualityGroupShareBean() != null) {
                QualityGroupShareBean qualityGroupShareBean = qualityGroupShareEntity.getQualityGroupShareBean();
                if (invitePartnerJoin) {
                    new UMShareAction(QualityGroupShopDetailActivity.this
                            , qualityGroupShareBean.getGpPicUrl()
                            , qualityGroupShareBean.getName()
                            , getStrings(qualityGroupShareBean.getSubtitle())
                            , BASE_SHARE_PAGE_TWO + "m/template/share_template/groupShare.html?id=" + qualityGroupShareBean.getGpInfoId()
                            + "&record=" + qualityGroupShareBean.getGpRecordId(), "pages/groupshare/groupshare?id=" + qualityGroupShareBean.getGpInfoId()
                            + (TextUtils.isEmpty(orderNo) ? "&gpRecordId=" + qualityGroupShareBean.getGpRecordId() : "&order=" + orderNo));
                } else {
                    isCanJoinGroup(null, qualityGroupShareEntity, shareJoinGroup);
                }
            } else if (shopDetailsEntity != null && shopDetailsEntity.getGroupShopDetailsBean() != null) {
                Intent intent = new Intent(QualityGroupShopDetailActivity.this, DirectIndentWriteActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("gpShopInfo", shopDetailsEntity.getGroupShopDetailsBean());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        } else {
            getLoginStatus(QualityGroupShopDetailActivity.this);
        }
    }

    @OnClick(R.id.ll_alone_buy)
    void skipAloneBuy(View view) {
        if (shopDetailsEntity != null && shopDetailsEntity.getGroupShopDetailsBean() != null) {
            if (invitePartnerJoin) {
                Intent intent = new Intent(QualityGroupShopDetailActivity.this, QualityGroupShopAllActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            } else {
                Intent intent = new Intent(QualityGroupShopDetailActivity.this, ShopScrollDetailsActivity.class);
                GroupShopDetailsBean groupShopDetailsBean = shopDetailsEntity.getGroupShopDetailsBean();
                intent.putExtra("productId", String.valueOf(groupShopDetailsBean.getProductId()));
                startActivity(intent);
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
            qyProductIndentInfo.setTitle(getStrings(groupShopDetailsBean.getName()));
            qyProductIndentInfo.setPicUrl(groupShopDetailsBean.getGpPicUrl());
            qyProductIndentInfo.setDesc(getStrings(groupShopDetailsBean.getGpType()));
            qyProductIndentInfo.setNote("￥" + groupShopDetailsBean.getGpPrice());
        }
        QyServiceUtils.getQyInstance().openQyServiceChat(this, sourceTitle, sourceUrl, qyProductIndentInfo);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    public void isCanJoinGroup(final GroupShopJoinBean groupShopjoinBean, final QualityGroupShareEntity qualityGroupShareEntity, final String joinType) {
        Map<String, Object> params = new HashMap<>();
        params.put("uid", userId);
        params.put("gpInfoId", groupShopjoinBean != null ?
                groupShopjoinBean.getGpInfoId() : qualityGroupShareEntity.getQualityGroupShareBean().getGpInfoId());
        NetLoadUtils.getNetInstance().loadNetDataPost(this,GROUP_SHOP_JOIN_NRE_USER,params,new NetLoadListenerHelper(){
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                RequestStatus requestStatus = gson.fromJson(result, RequestStatus.class);
                if (requestStatus != null) {
                    if (requestStatus.getCode().equals(SUCCESS_CODE)) {
                        if (joinType.equals(normalJoinGroup) && groupShopjoinBean != null) {
                            final GroupShopDetailsBean groupShopDetailsBean = shopDetailsEntity.getGroupShopDetailsBean();
                            groupShopDetailsBean.setGpStatus(2);
                            groupShopDetailsBean.setGpRecordId(groupShopjoinBean.getGpRecordId());
                            Intent intent = new Intent(QualityGroupShopDetailActivity.this, DirectIndentWriteActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putParcelable("gpShopInfo", groupShopDetailsBean);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        } else if (joinType.equals(shareJoinGroup) && qualityGroupShareEntity != null) {
                            QualityGroupShareBean qualityGroupShareBean = qualityGroupShareEntity.getQualityGroupShareBean();
                            GroupShopDetailsBean groupShopDetailsBean = new GroupShopDetailsBean();
                            groupShopDetailsBean.setGpStatus(2);
                            groupShopDetailsBean.setGpRecordId(Integer.parseInt(qualityGroupShareBean.getGpRecordId()));
                            groupShopDetailsBean.setProductId(qualityGroupShareBean.getProductId());
                            groupShopDetailsBean.setGpProductId(qualityGroupShareBean.getGpProductId());
                            groupShopDetailsBean.setGpInfoId(qualityGroupShareBean.getGpInfoId());
                            groupShopDetailsBean.setProductSkuValue(qualityGroupShareBean.getProductSkuValue());
                            groupShopDetailsBean.setGpSkuId(qualityGroupShareBean.getGpSkuId());
                            groupShopDetailsBean.setCoverImage(qualityGroupShareBean.getCoverImage());
                            groupShopDetailsBean.setGpPicUrl(qualityGroupShareBean.getGpPicUrl());
                            groupShopDetailsBean.setName(qualityGroupShareBean.getName());
                            groupShopDetailsBean.setGpPrice(qualityGroupShareBean.getGpPrice());
                            Intent intent = new Intent(QualityGroupShopDetailActivity.this, DirectIndentWriteActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putParcelable("gpShopInfo", groupShopDetailsBean);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    } else {
                        showToastRequestMsg(QualityGroupShopDetailActivity.this, requestStatus);
                    }
                } else {
                    showToast(QualityGroupShopDetailActivity.this, R.string.invalidData);
                }
            }
        });
    }

    //    参团列表
    class ShopJoinGroupView {
        @BindView(R.id.communal_recycler_wrap)
        RecyclerView communal_recycler_wrap;
        private JoinGroupAdapter joinGroupAdapter;
        private View headerView;
        private TextView tv_partner_join;

        public void initView() {
            communal_recycler_wrap.setLayoutManager(new LinearLayoutManager(QualityGroupShopDetailActivity.this));
            joinGroupAdapter = new JoinGroupAdapter(QualityGroupShopDetailActivity.this, groupShopJoinList);
            headerView = LayoutInflater.from(QualityGroupShopDetailActivity.this).inflate(R.layout.layout_ql_gp_text, null);
            tv_partner_join = (TextView) headerView.findViewById(R.id.tv_partner_join);
            communal_recycler_wrap.setAdapter(joinGroupAdapter);
            joinGroupAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    GroupShopJoinBean groupShopJoinBean = (GroupShopJoinBean) view.getTag();
                    if (groupShopJoinBean != null && shopDetailsEntity != null) {
                        if (userId != 0) {
                            isCanJoinGroup(groupShopJoinBean, null, normalJoinGroup);
                        } else {
                            getLoginStatus(QualityGroupShopDetailActivity.this);
                        }
                    }
                }
            });
        }
    }

    class ShopCommentHeaderView {
        //        商品评论展示
        @BindView(R.id.rel_pro_comment)
        RelativeLayout rel_pro_comment;
        //        评价数目
        @BindView(R.id.tv_shop_comment_count)
        TextView tv_shop_comment_count;
        @BindView(R.id.communal_recycler_wrap)
        RecyclerView communal_recycler_wrap;
        private CommunalDetailAdapter gpRuleDetailsAdapter;

        @OnClick(R.id.tv_shop_comment_more)
        void getMoreComment(View view) {
            if (shopDetailsEntity != null
                    && shopDetailsEntity.getGroupShopDetailsBean() != null
                    && shopDetailsEntity.getGroupShopDetailsBean().getProductId() > 0) {
//                跳转更多评论
                Intent intent = new Intent(QualityGroupShopDetailActivity.this, DirectProductEvaluationActivity.class);
                intent.putExtra("productId", String.valueOf(shopDetailsEntity.getGroupShopDetailsBean().getProductId()));
                startActivity(intent);
            }
        }

        public void initView() {
            communal_recycler_wrap.setNestedScrollingEnabled(false);
            gpRuleDetailsAdapter = new CommunalDetailAdapter(QualityGroupShopDetailActivity.this, gpRuleList);
            communal_recycler_wrap.setLayoutManager(new LinearLayoutManager(QualityGroupShopDetailActivity.this));
            communal_recycler_wrap.setAdapter(gpRuleDetailsAdapter);
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

    private void getConstant() {
        if (constantMethod == null) {
            constantMethod = new ConstantMethod();
        }
    }

    @Override
    protected void onDestroy() {
        getConstant();
        constantMethod.stopSchedule();
        constantMethod.releaseHandlers();
        super.onDestroy();
    }
}
