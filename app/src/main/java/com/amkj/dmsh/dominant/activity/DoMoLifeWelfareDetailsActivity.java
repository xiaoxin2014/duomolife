package com.amkj.dmsh.dominant.activity;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.baichuan.android.trade.AlibcTrade;
import com.alibaba.baichuan.android.trade.callback.AlibcTradeCallback;
import com.alibaba.baichuan.android.trade.model.AlibcShowParams;
import com.alibaba.baichuan.android.trade.model.OpenType;
import com.alibaba.baichuan.android.trade.page.AlibcBasePage;
import com.alibaba.baichuan.android.trade.page.AlibcPage;
import com.alibaba.baichuan.trade.biz.AlibcConstants;
import com.alibaba.baichuan.trade.biz.context.AlibcTradeResult;
import com.alibaba.baichuan.trade.biz.login.AlibcLogin;
import com.alibaba.baichuan.trade.biz.login.AlibcLoginCallback;
import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.bean.RequestStatus;
import com.amkj.dmsh.constant.BaseAddCarProInfoBean;
import com.amkj.dmsh.constant.CommunalComment;
import com.amkj.dmsh.constant.CommunalDetailBean;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.ConstantVariable;
import com.amkj.dmsh.constant.UMShareAction;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.constant.XUtil;
import com.amkj.dmsh.dominant.adapter.ArticleCommentAdapter;
import com.amkj.dmsh.dominant.adapter.WelfareSlideProAdapter;
import com.amkj.dmsh.dominant.bean.DmlSearchCommentEntity;
import com.amkj.dmsh.dominant.bean.DmlSearchCommentEntity.DmlSearchCommentBean;
import com.amkj.dmsh.dominant.bean.DmlSearchCommentEntity.DmlSearchCommentBean.ReplyCommListBean;
import com.amkj.dmsh.dominant.bean.DmlSearchDetailEntity.DmlSearchDetailBean.ProductListBean;
import com.amkj.dmsh.dominant.bean.QualityWefEntity;
import com.amkj.dmsh.dominant.bean.QualityWefEntity.QualityWefBean;
import com.amkj.dmsh.homepage.adapter.CommunalDetailAdapter;
import com.amkj.dmsh.homepage.bean.CommunalOnlyDescription;
import com.amkj.dmsh.homepage.bean.CommunalOnlyDescription.ComOnlyDesBean;
import com.amkj.dmsh.mine.activity.MineLoginActivity;
import com.amkj.dmsh.mine.activity.ShopCarActivity;
import com.amkj.dmsh.mine.bean.SavePersonalInfoBean;
import com.amkj.dmsh.shopdetails.activity.ShopScrollDetailsActivity;
import com.amkj.dmsh.shopdetails.bean.CommunalDetailObjectBean;
import com.amkj.dmsh.user.activity.UserPagerActivity;
import com.amkj.dmsh.utils.CommonUtils;
import com.amkj.dmsh.utils.Log;
import com.amkj.dmsh.utils.NetWorkUtils;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.amkj.dmsh.utils.inteface.MyCallBack;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.oushangfeng.pinnedsectionitemdecoration.PinnedHeaderItemDecoration;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.umeng.socialize.UMShareAPI;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;
import emojicon.EmojiconEditText;
import q.rorbin.badgeview.Badge;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.amkj.dmsh.R.id.ll_communal_pro_list;
import static com.amkj.dmsh.constant.ConstantMethod.getBadge;
import static com.amkj.dmsh.constant.ConstantMethod.getDetailsDataList;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.insertNewTotalData;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.skipProductUrl;
import static com.amkj.dmsh.constant.ConstantMethod.totalWelfareProNum;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.COMMENT_TOPIC_TYPE;
import static com.amkj.dmsh.constant.ConstantVariable.DEFAULT_COMMENT_TOTAL_COUNT;
import static com.amkj.dmsh.constant.ConstantVariable.DEFAULT_TOTAL_COUNT;
import static com.amkj.dmsh.shopdetails.bean.CommunalDetailObjectBean.TYPE_COUPON;
import static com.amkj.dmsh.shopdetails.bean.CommunalDetailObjectBean.TYPE_COUPON_PACKAGE;
import static com.amkj.dmsh.utils.CommunalCopyTextUtils.showPopWindow;

;

public class DoMoLifeWelfareDetailsActivity extends BaseActivity {
    @BindView(R.id.smart_communal_refresh)
    RefreshLayout smart_communal_refresh;
    @BindView(R.id.communal_recycler)
    RecyclerView communal_recycler;
    @BindView(R.id.tv_header_title)
    TextView tv_header_titleAll;
    @BindView(R.id.iv_img_service)
    ImageView iv_img_service;
    @BindView(R.id.fl_header_service)
    FrameLayout fl_header_service;
    @BindView(R.id.communal_load)
    View communal_load;
    @BindView(R.id.communal_error)
    View communal_error;
    @BindView(R.id.communal_empty)
    View communal_empty;
    //    滑动布局
    @BindView(R.id.dr_welfare_detail_pro)
    DrawerLayout dr_welfare_detail_pro;
    @BindView(ll_communal_pro_list)
    LinearLayout ll_wel_pro_list;
    @BindView(R.id.tv_communal_pro_title)
    TextView tv_slid_pro_title;
    @BindView(R.id.rv_communal_pro)
    RecyclerView rv_wel_details_pro;

    @BindView(R.id.tv_communal_pro_tag)
    TextView tv_wel_pro_tag;

    //    底栏
    @BindView(R.id.rel_topic_bottom)
    RelativeLayout rel_topic_bottom;
    //    评论输入
    @BindView(R.id.ll_input_comment)
    LinearLayout ll_input_comment;
    @BindView(R.id.emoji_edit_comment)
    EmojiconEditText emoji_edit_comment;
    @BindView(R.id.tv_send_comment)
    TextView tv_send_comment;
    //    点赞底栏
    @BindView(R.id.ll_article_comment)
    LinearLayout ll_article_comment;
    @BindView(R.id.tv_publish_comment)
    TextView tv_publish_comment;
    @BindView(R.id.tv_article_bottom_like)
    TextView tv_article_bottom_like;
    @BindView(R.id.tv_article_bottom_collect)
    TextView tv_article_bottom_collect;


    private List<CommunalDetailObjectBean> itemDescriptionList = new ArrayList();
    //侧滑商品列表
    private List<ProductListBean> welfareProductList = new ArrayList();
    //    评论列表
    private List<DmlSearchCommentBean> articleCommentList = new ArrayList<>();
    private String welfareId;
    private int page = 1;
    private int uid;
    private WelfareHeaderView welfareHeaderView;
    private CommunalDetailAdapter communalWelfareDetailAdapter;
    private Badge badge;
    private WelfareSlideProAdapter welfareSlideProAdapter;
    private QualityWefBean qualityWefBean;
    private ArticleCommentAdapter adapterTopicComment;
    private View commentHeaderView;
    private float locationY;
    private CommentCountView commentCountView;

    @Override
    protected int getContentView() {
        return R.layout.activity_ql_welfare_details;
    }

    @Override
    protected void initViews() {
        tv_header_titleAll.setText("");
        tv_article_bottom_like.setVisibility(GONE);
        tv_article_bottom_collect.setVisibility(GONE);
        iv_img_service.setImageResource(R.drawable.shop_car_gray_icon);
        Intent intent = getIntent();
        welfareId = intent.getStringExtra("welfareId");
        View headerView = LayoutInflater.from(DoMoLifeWelfareDetailsActivity.this)
                .inflate(R.layout.layout_welfare_header, (ViewGroup) communal_recycler.getParent(), false);
        welfareHeaderView = new WelfareHeaderView();
        ButterKnife.bind(welfareHeaderView, headerView);
        welfareHeaderView.initView();
        communal_recycler.setLayoutManager(new LinearLayoutManager(DoMoLifeWelfareDetailsActivity.this));
        communal_recycler.setNestedScrollingEnabled(false);
        commentHeaderView = LayoutInflater.from(DoMoLifeWelfareDetailsActivity.this)
                .inflate(R.layout.layout_comm_comment_header, (ViewGroup) communal_recycler.getParent(), false);
        commentCountView = new CommentCountView();
        ButterKnife.bind(commentCountView, commentHeaderView);
        adapterTopicComment = new ArticleCommentAdapter(DoMoLifeWelfareDetailsActivity.this, articleCommentList, COMMENT_TOPIC_TYPE);
        adapterTopicComment.setHeaderAndEmpty(true);
        adapterTopicComment.addHeaderView(headerView);
        communal_recycler.setAdapter(adapterTopicComment);
        communal_recycler.addItemDecoration(new PinnedHeaderItemDecoration.Builder(-1)
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_gray_f_two_px)
                // 开启绘制分隔线，默认关闭
                .enableDivider(true)
                // 是否关闭标签点击事件，默认开启
                .disableHeaderClick(false)
                // 设置标签和其内部的子控件的监听，若设置点击监听不为null，但是disableHeaderClick(true)的话，还是不会响应点击事件
                .setHeaderClickListener(null)
                .create());
        adapterTopicComment.setOnItemChildClickListener((adapter, view, position) -> {
            DmlSearchCommentBean dmlSearchCommentBean = (DmlSearchCommentBean) view.getTag(R.id.iv_tag);
            if (dmlSearchCommentBean == null) {
                dmlSearchCommentBean = (DmlSearchCommentBean) view.getTag();
            }
            if (dmlSearchCommentBean != null) {
                switch (view.getId()) {
                    case R.id.tv_comm_comment_like:
                        if (uid > 0) {
                            dmlSearchCommentBean.setFavor(!dmlSearchCommentBean.isFavor());
                            int likeNum = dmlSearchCommentBean.getLike_num();
                            dmlSearchCommentBean.setLike_num(dmlSearchCommentBean.isFavor()
                                    ? likeNum + 1 : likeNum - 1 > 0 ? likeNum - 1 : 0);
                            articleCommentList.set(position, dmlSearchCommentBean);
                            adapterTopicComment.notifyItemChanged(position + adapterTopicComment.getHeaderLayoutCount());
                            setCommentLike(dmlSearchCommentBean);
                        } else {
                            getLoginStatus();
                        }
                        break;
                    case R.id.tv_comm_comment_receive:
//                            打开评论
                        if (uid > 0) {
                            if (VISIBLE == ll_input_comment.getVisibility()) {
                                commentViewVisible(GONE, dmlSearchCommentBean);
                            } else {
                                commentViewVisible(VISIBLE, dmlSearchCommentBean);
                            }
                        } else {
                            getLoginStatus();
                        }
                        break;
                    case R.id.civ_comm_comment_avatar:
                        Intent intent1 = new Intent();
                        intent1.setClass(DoMoLifeWelfareDetailsActivity.this, UserPagerActivity.class);
                        intent1.putExtra("userId", String.valueOf(dmlSearchCommentBean.getUid()));
                        startActivity(intent1);
                        break;
                    default:
                        break;
                }
            }
        });
        adapterTopicComment.setOnItemChildLongClickListener((adapter, view, position) -> {
            DmlSearchCommentBean dmlSearchCommentBean = (DmlSearchCommentBean) view.getTag();
            if (dmlSearchCommentBean != null && !TextUtils.isEmpty(dmlSearchCommentBean.getContent())) {
                showPopWindow(DoMoLifeWelfareDetailsActivity.this, (TextView) view, dmlSearchCommentBean.getContent());
            }
            return false;
        });
        adapterTopicComment.setOnLoadMoreListener(() -> {
            if (page * DEFAULT_TOTAL_COUNT <= articleCommentList.size()) {
                page++;
                getTopicComment();
            } else {
                adapterTopicComment.loadMoreEnd();
            }
        }, communal_recycler);
//        侧滑菜单
        rv_wel_details_pro.setLayoutManager(new LinearLayoutManager(DoMoLifeWelfareDetailsActivity.this));
        rv_wel_details_pro.addItemDecoration(new PinnedHeaderItemDecoration.Builder(-1)
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_gray_f_two_px)
                // 开启绘制分隔线，默认关闭
                .enableDivider(true)
                // 是否关闭标签点击事件，默认开启
                .disableHeaderClick(false)
                // 设置标签和其内部的子控件的监听，若设置点击监听不为null，但是disableHeaderClick(true)的话，还是不会响应点击事件
                .setHeaderClickListener(null)
                .create());
        welfareSlideProAdapter = new WelfareSlideProAdapter(DoMoLifeWelfareDetailsActivity.this, welfareProductList);
        welfareSlideProAdapter.setEnableLoadMore(false);
        rv_wel_details_pro.setAdapter(welfareSlideProAdapter);
        welfareSlideProAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ProductListBean productListBean = (ProductListBean) view.getTag();
                if (productListBean != null) {
                    dr_welfare_detail_pro.closeDrawers();
                    skipProductUrl(DoMoLifeWelfareDetailsActivity.this, productListBean.getItemTypeId(), productListBean.getId());
                    //                    统计商品点击
                    totalWelfareProNum(productListBean.getId(), Integer.parseInt(welfareId));
                }
            }
        });
        smart_communal_refresh.setOnRefreshListener((refreshLayout) -> {
            page = 1;
            loadData();
        });
        communalWelfareDetailAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (loadHud != null) {
                    loadHud.show();
                }
                switch (view.getId()) {
                    case R.id.img_product_coupon_pic:
                        int couponId = (int) view.getTag(R.id.iv_avatar_tag);
                        int type = (int) view.getTag(R.id.iv_type_tag);
                        if (couponId > 0) {
                            if (uid != 0) {
                                if (type == TYPE_COUPON) {
                                    getDirectCoupon(couponId);
                                } else if (type == TYPE_COUPON_PACKAGE) {
                                    getDirectCouponPackage(couponId);
                                }
                            } else {
                                if (loadHud != null) {
                                    loadHud.dismiss();
                                }
                                getLoginStatus();
                            }
                        }
                        break;
                    case R.id.iv_communal_cover_wrap:
                        CommunalDetailObjectBean detailObjectBean = (CommunalDetailObjectBean) view.getTag(R.id.iv_tag);
                        if (detailObjectBean != null) {
                            if (detailObjectBean.getItemType() == CommunalDetailObjectBean.TYPE_GOODS_IMG) {
                                Intent intent = new Intent(DoMoLifeWelfareDetailsActivity.this, ShopScrollDetailsActivity.class);
                                intent.putExtra("productId", String.valueOf(detailObjectBean.getId()));
                                startActivity(intent);
                                //                    统计商品点击
                                totalWelfareProNum(detailObjectBean.getId(), Integer.parseInt(welfareId));
                            }
                        }
                        loadHud.dismiss();
                        break;
                    case R.id.tv_click_get_lucky_money:
                    case R.id.ll_get_lucky_money:
                        CommunalDetailObjectBean couponBean = (CommunalDetailObjectBean) view.getTag();
                        if (couponBean != null) {
                            if (loadHud != null) {
                                loadHud.dismiss();
                            }
                            if (uid != 0) {
                                skipAliBCWebView(couponBean.getCouponUrl());
                            } else {
                                if (loadHud != null) {
                                    loadHud.dismiss();
                                }
                                getLoginStatus();
                            }
                        }
                        break;
                    case R.id.iv_ql_bl_add_car:
                        CommunalDetailObjectBean qualityWelPro = (CommunalDetailObjectBean) view.getTag();
                        loadHud.show();
                        if (qualityWelPro != null) {
                            if (userId > 0) {
                                BaseAddCarProInfoBean baseAddCarProInfoBean = new BaseAddCarProInfoBean();
                                baseAddCarProInfoBean.setProductId(qualityWelPro.getId());
                                baseAddCarProInfoBean.setProName(getStrings(qualityWelPro.getName()));
                                baseAddCarProInfoBean.setProPic(getStrings(qualityWelPro.getPicUrl()));
                                ConstantMethod constantMethod = new ConstantMethod();
                                constantMethod.addShopCarGetSku(DoMoLifeWelfareDetailsActivity.this, baseAddCarProInfoBean, loadHud);
                                constantMethod.setAddOnCarListener(new ConstantMethod.OnAddCarListener() {
                                    @Override
                                    public void onAddCarSuccess() {
                                        getCarCount();
                                    }
                                });
                            } else {
                                loadHud.dismiss();
                                getLoginStatus();
                            }
                        }
                        break;
                    case R.id.tv_communal_share:
                        loadHud.dismiss();
                        setShareData();
                        break;
                }
            }
        });
        communalWelfareDetailAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                CommunalDetailObjectBean communalDetailBean = (CommunalDetailObjectBean) view.getTag();
                if (communalDetailBean != null) {
                    skipProductUrl(DoMoLifeWelfareDetailsActivity.this, communalDetailBean.getItemTypeId(), communalDetailBean.getId());
                    //                    统计商品点击
                    totalWelfareProNum(communalDetailBean.getId(), Integer.parseInt(welfareId));
                }
            }
        });
        //          关闭手势滑动
        dr_welfare_detail_pro.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.END);
        badge = getBadge(DoMoLifeWelfareDetailsActivity.this, fl_header_service);
        communal_load.setVisibility(View.VISIBLE);
        totalPersonalTrajectory = insertNewTotalData(DoMoLifeWelfareDetailsActivity.this, welfareId);
        tv_publish_comment.setText(R.string.comment_article_hint);
    }

    @Override
    protected void loadData() {
        //头部信息
        getThemeDetailsData();
        getCarCount();
        getTopicComment();
    }

    private void getThemeDetailsData() {
        String url = Url.BASE_URL + Url.H_DML_THEME_DETAIL;
        if (NetWorkUtils.checkNet(DoMoLifeWelfareDetailsActivity.this) && !TextUtils.isEmpty(welfareId)) {
            Map<String, Object> params = new HashMap<>();
            params.put("id", welfareId);
            params.put("is_up", 1);
            if (userId > 0) {
                params.put("uid", userId);
            }
            XUtil.Post(url, params, new MyCallBack<String>() {
                @Override
                public void onSuccess(String result) {
                    smart_communal_refresh.finishRefresh();
                    communal_load.setVisibility(GONE);
                    communal_empty.setVisibility(GONE);
                    communal_error.setVisibility(GONE);
                    Gson gson = new Gson();
                    QualityWefEntity qualityWefEntity = gson.fromJson(result, QualityWefEntity.class);
                    if (qualityWefEntity != null) {
                        if (qualityWefEntity.getCode().equals("01")) {
                            qualityWefBean = qualityWefEntity.getQualityWefBean();
                            setData(qualityWefBean);
                        } else if (qualityWefEntity.getCode().equals("02")) {
                            if (page == 1) {
                                showToast(DoMoLifeWelfareDetailsActivity.this, R.string.invalidData);
                                communal_empty.setVisibility(View.VISIBLE);
                            }
                        } else {
                            if (page == 1) {
                                communal_error.setVisibility(View.VISIBLE);
                            }
                            showToast(DoMoLifeWelfareDetailsActivity.this, qualityWefEntity.getMsg());
                        }
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    smart_communal_refresh.finishRefresh();
                    communal_empty.setVisibility(GONE);
                    communal_load.setVisibility(GONE);
                    communal_error.setVisibility(GONE);
                    if (page == 1) {
                        communal_load.setVisibility(GONE);
                        communal_error.setVisibility(View.VISIBLE);
                    }
                    showToast(DoMoLifeWelfareDetailsActivity.this, R.string.invalidData);
                    super.onError(ex, isOnCallback);
                }
            });
        } else {
            smart_communal_refresh.finishRefresh();
            communal_load.setVisibility(View.GONE);
            if (page == 1) {
                communal_load.setVisibility(GONE);
                communal_empty.setVisibility(GONE);
                communal_error.setVisibility(View.VISIBLE);
            } else {
                showToast(this, R.string.unConnectedNetwork);
            }
        }
    }


    private void setData(QualityWefBean qualityWefBean) {
        GlideImageLoaderUtil.loadCenterCrop(DoMoLifeWelfareDetailsActivity.this, welfareHeaderView.iv_welfare_detail_bg, qualityWefBean.getPicUrl());
        tv_header_titleAll.setText(getStrings(qualityWefBean.getTitle()));
        if (!TextUtils.isEmpty(qualityWefBean.getSubtitle())) {
            welfareHeaderView.tv_welfare_details_content.setText(qualityWefBean.getSubtitle());
        } else {
            welfareHeaderView.tv_welfare_details_content.setVisibility(GONE);
        }
        welfareProductList.clear();
        if (qualityWefBean.getProductList() != null
                && qualityWefBean.getProductList().size() > 0) {
            welfareProductList.addAll(qualityWefBean.getProductList());
            setPrepareData(qualityWefBean);
            welfareSlideProAdapter.setNewData(welfareProductList);
        } else {
            tv_wel_pro_tag.setVisibility(View.GONE);
            ll_wel_pro_list.setVisibility(View.GONE);
        }

        List<CommunalDetailBean> descriptionList = qualityWefBean.getDescriptionList();
        if (descriptionList != null) {
            itemDescriptionList.clear();
            itemDescriptionList.addAll(getDetailsDataList(descriptionList));
            getShareData();
            communalWelfareDetailAdapter.setNewData(itemDescriptionList);
        }
    }

    private void getShareData() {
        XUtil.Get(Url.BASE_URL + Url.SHARE_COMMUNAL_ARTICLE, null, new MyCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                CommunalOnlyDescription communalOnlyDescription = gson.fromJson(result, CommunalOnlyDescription.class);
                if (communalOnlyDescription != null) {
                    if (communalOnlyDescription.getCode().equals("01")
                            && communalOnlyDescription.getComOnlyDesBean() != null) {
                        ComOnlyDesBean comOnlyDesBean = communalOnlyDescription.getComOnlyDesBean();
                        if (comOnlyDesBean.getDescriptionList() != null && comOnlyDesBean.getDescriptionList().size() > 0) {
                            CommunalDetailObjectBean communalDetailObjectBean = new CommunalDetailObjectBean();
                            communalDetailObjectBean.setItemType(CommunalDetailObjectBean.TYPE_SHARE);
                            itemDescriptionList.add(communalDetailObjectBean);
                            itemDescriptionList.addAll(getDetailsDataList(comOnlyDesBean.getDescriptionList()));
                            communalWelfareDetailAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }
        });
    }

    private void setPrepareData(QualityWefBean qualityWefBean) {
        tv_wel_pro_tag.setVisibility(View.VISIBLE);
        ll_wel_pro_list.setVisibility(View.VISIBLE);
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        int radius = (int) AutoUtils.getPercentWidth1px() * 50;
        drawable.setCornerRadii(new float[]{radius, radius, 0, 0, 0, 0, radius, radius});
        try {
            drawable.setColor(0xffffffff);
            drawable.setStroke(1, 0xffcccccc);
        } catch (Exception e) {
            e.printStackTrace();
        }
        tv_wel_pro_tag.setBackground(drawable);
        tv_wel_pro_tag.setText(welfareProductList.size() + "商品");
        tv_slid_pro_title.setText(getStrings(qualityWefBean.getTitle()));
    }

    private void getDirectCoupon(int id) {
        String url = Url.BASE_URL + Url.FIND_ARTICLE_COUPON;
        Map<String, Object> params = new HashMap<>();
        params.put("userId", uid);
        params.put("couponId", id);
        XUtil.Post(url, params, new MyCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                if (loadHud != null) {
                    loadHud.dismiss();
                }
                Gson gson = new Gson();
                RequestStatus requestStatus = gson.fromJson(result, RequestStatus.class);
                if (requestStatus != null) {
                    if (requestStatus.getCode().equals("01")) {
                        showToast(DoMoLifeWelfareDetailsActivity.this, requestStatus.getResult() != null ? requestStatus.getResult().getMsg() : requestStatus.getMsg());
                    } else {
                        showToast(DoMoLifeWelfareDetailsActivity.this, requestStatus.getResult() != null ? requestStatus.getResult().getMsg() : requestStatus.getMsg());
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                showToast(DoMoLifeWelfareDetailsActivity.this, R.string.Get_Coupon_Fail);
                if (loadHud != null) {
                    loadHud.dismiss();
                }
            }
        });
    }

    private void getDirectCouponPackage(int couponId) {
        String url = Url.BASE_URL + Url.COUPON_PACKAGE;
        Map<String, Object> params = new HashMap<>();
        params.put("uId", uid);
        params.put("cpId", couponId);
        XUtil.Post(url, params, new MyCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                if (loadHud != null) {
                    loadHud.dismiss();
                }
                Gson gson = new Gson();
                RequestStatus requestStatus = gson.fromJson(result, RequestStatus.class);
                if (requestStatus != null) {
                    if (requestStatus.getCode().equals("01")) {
                        showToast(DoMoLifeWelfareDetailsActivity.this, requestStatus.getResult() != null ? requestStatus.getResult().getMsg() : requestStatus.getMsg());
                    } else {
                        showToast(DoMoLifeWelfareDetailsActivity.this, requestStatus.getResult() != null ? requestStatus.getResult().getMsg() : requestStatus.getMsg());
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                showToast(DoMoLifeWelfareDetailsActivity.this, R.string.Get_Coupon_Fail);
                if (loadHud != null) {
                    loadHud.dismiss();
                }
            }
        });
    }

    public void skipAliBCWebView(final String url) {
        if (!TextUtils.isEmpty(url)) {
            if (uid != 0) {
                skipNewTaoBao(url);
            } else {
                if (loadHud != null) {
                    loadHud.dismiss();
                }
                getLoginStatus();
            }
        } else {
            showToast(DoMoLifeWelfareDetailsActivity.this, "地址缺失");
            if (loadHud != null) {
                loadHud.dismiss();
            }
        }
    }

    private void skipNewTaoBao(final String url) {
        AlibcLogin alibcLogin = AlibcLogin.getInstance();
        alibcLogin.showLogin(new AlibcLoginCallback() {
            @Override
            public void onSuccess(int i) {
                if (loadHud != null) {
                    loadHud.dismiss();
                }
                skipNewShopDetails(url);
            }

            @Override
            public void onFailure(int code, String msg) {
                if (loadHud != null) {
                    loadHud.dismiss();
                }
                showToast(DoMoLifeWelfareDetailsActivity.this, "登录失败 ");
            }
        });
    }

    private void skipNewShopDetails(String url) {
        //提供给三方传递配置参数
        Map<String, String> exParams = new HashMap<>();
        exParams.put(AlibcConstants.ISV_CODE, "appisvcode");
        //设置页面打开方式
        AlibcShowParams showParams = new AlibcShowParams(OpenType.Native, false);
        //实例化商品详情 itemID打开page
        AlibcBasePage ordersPage = new AlibcPage(url);
        AlibcTrade.show(DoMoLifeWelfareDetailsActivity.this, ordersPage, showParams, null, exParams, new AlibcTradeCallback() {
            @Override
            public void onTradeSuccess(AlibcTradeResult alibcTradeResult) {
                //打开电商组件，用户操作中成功信息回调。tradeResult：成功信息（结果类型：加购，支付；支付结果）
//                showToast(context, "获取详情成功");
                Log.d("商品详情", "onTradeSuccess: ");
            }

            @Override
            public void onFailure(int code, String msg) {
                Log.d("商品详情", "onFailure: " + code + msg);
                //打开电商组件，用户操作中错误信息回调。code：错误码；msg：错误信息
//                showToast(ShopTimeScrollDetailsActivity.this, msg);
            }
        });
    }

    private void getTopicComment() {
        if (NetWorkUtils.checkNet(DoMoLifeWelfareDetailsActivity.this)) {
            String url = Url.BASE_URL + Url.Q_DML_SEARCH_COMMENT;
            Map<String, Object> params = new HashMap<>();
            params.put("id", welfareId);
            params.put("currentPage", page);
            if (uid > 0) {
                params.put("uid", uid);
            }
            params.put("showCount", DEFAULT_TOTAL_COUNT);
            params.put("replyCurrentPage", 1);
            params.put("replyShowCount", DEFAULT_COMMENT_TOTAL_COUNT);
            params.put("comtype", "topic");
            XUtil.Post(url, params, new MyCallBack<String>() {
                @Override
                public void onSuccess(String result) {
                    adapterTopicComment.loadMoreComplete();
                    if (page == 1) {
                        articleCommentList.clear();
                    }
                    Gson gson = new Gson();
                    DmlSearchCommentEntity dmlSearchCommentEntity = gson.fromJson(result, DmlSearchCommentEntity.class);
                    if (dmlSearchCommentEntity != null) {
                        if (dmlSearchCommentEntity.getCode().equals("01")) {
                            articleCommentList.addAll(dmlSearchCommentEntity.getDmlSearchCommentList());
                        } else if (!dmlSearchCommentEntity.getCode().equals("02")) {
                            showToast(DoMoLifeWelfareDetailsActivity.this, dmlSearchCommentEntity.getMsg());
                        }
                        adapterTopicComment.removeHeaderView(commentHeaderView);
                        if (articleCommentList.size() > 0) {
                            adapterTopicComment.addHeaderView(commentHeaderView);
                            commentCountView.tv_comm_comment_count.setText(String.format(getString(R.string.comment_handpick_count), dmlSearchCommentEntity.getCommentSize()));
                        }
                        if (page == 1) {
                            adapterTopicComment.setNewData(articleCommentList);
                        } else {
                            adapterTopicComment.notifyDataSetChanged();
                        }
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    adapterTopicComment.loadMoreComplete();
                    showToast(DoMoLifeWelfareDetailsActivity.this, R.string.invalidData);
                    super.onError(ex, isOnCallback);
                }
            });
        }
    }

    private void getCarCount() {
        if (uid < 1) {
            isLoginStatus();
        }
        if (uid > 0) {
            //购物车数量展示
            String url = Url.BASE_URL + Url.Q_QUERY_CAR_COUNT;
            Map<String, Object> params = new HashMap<>();
            params.put("userId", uid);
            XUtil.Post(url, params, new MyCallBack<String>() {
                @Override
                public void onSuccess(String result) {
                    Gson gson = new Gson();
                    RequestStatus requestStatus = gson.fromJson(result, RequestStatus.class);
                    if (requestStatus != null) {
                        if (requestStatus.getCode().equals("01")) {
                            int cartNumber = requestStatus.getResult().getCartNumber();
                            badge.setBadgeNumber(cartNumber);
                        } else if (!requestStatus.getCode().equals("02")) {
                            showToast(DoMoLifeWelfareDetailsActivity.this, requestStatus.getMsg());
                        }
                    }
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getCarCount();
    }

    private void isLoginStatus() {
        SavePersonalInfoBean personalInfo = ConstantMethod.getPersonalInfo(this);
        if (personalInfo.isLogin()) {
            uid = personalInfo.getUid();
        } else {
            uid = 0;
        }
    }

    private void getLoginStatus() {
        SavePersonalInfoBean personalInfo = ConstantMethod.getPersonalInfo(this);
        if (personalInfo.isLogin()) {
            uid = personalInfo.getUid();
        } else {
            //未登录跳转登录页
            Intent intent = new Intent(this, MineLoginActivity.class);
            startActivityForResult(intent, ConstantVariable.IS_LOGIN_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ConstantVariable.IS_LOGIN_CODE) {
            getLoginStatus();
            loadData();
        }
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    //    页面分享
    @OnClick(R.id.iv_img_share)
    void sendShare(View view) {
        setShareData();
    }

    private void setShareData() {
        if (qualityWefBean != null) {
            UMShareAction umShareAction = new UMShareAction(DoMoLifeWelfareDetailsActivity.this
                    , qualityWefBean.getPicUrl()
                    , qualityWefBean.getTitle()
                    , ""
                    , Url.BASE_SHARE_PAGE_TWO + ("m/template/common/topic.html" + "?id="
                    + qualityWefBean.getId() + (uid > 0 ? "&sid=" + uid : "")));
            umShareAction.setOnShareSuccessListener(new UMShareAction.OnShareSuccessListener() {
                @Override
                public void onShareSuccess() {
                    ConstantMethod.addArticleShareCount(qualityWefBean.getId());
                }
            });
        }
    }

    @OnClick(R.id.tv_life_back)
    void goBack(View view) {
        finish();
    }

    @OnClick(R.id.iv_img_service)
    void skipShopCar(View view) {
        Intent intent = new Intent(DoMoLifeWelfareDetailsActivity.this, ShopCarActivity.class);
        startActivity(intent);
    }

    @OnClick({R.id.rel_communal_error, R.id.communal_empty})
    void refreshData(View view) {
        communal_load.setVisibility(View.VISIBLE);
        communal_empty.setVisibility(GONE);
        communal_error.setVisibility(GONE);
        page = 1;
        loadData();
    }

    private void setCommentLike(DmlSearchCommentBean dmlSearchCommentBean) {
        String url = Url.BASE_URL + Url.FIND_AND_COMMENT_FAV;
        Map<String, Object> params = new HashMap<>();
        //用户id
        params.put("tuid", uid);
        //评论id
        params.put("id", dmlSearchCommentBean.getId());
        XUtil.Post(url, params, new MyCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
            }
        });
    }

    private String getNumber(String content) {
        String regex = "[0-9]\\d*\\.?\\d*";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(content);
        while (m.find()) {
            return m.group();
        }
        return "0";
    }

    //发送评论
    private void sendComment(CommunalComment communalComment) {
        loadHud.setCancellable(true);
        loadHud.show();
        tv_send_comment.setText("发送中…");
        tv_send_comment.setEnabled(false);
        ConstantMethod constantMethod = new ConstantMethod();
        constantMethod.setOnSendCommentFinish(new ConstantMethod.OnSendCommentFinish() {
            @Override
            public void onSuccess() {
                loadHud.dismiss();
                tv_send_comment.setText("发送");
                tv_send_comment.setEnabled(true);
                showToast(DoMoLifeWelfareDetailsActivity.this, R.string.comment_article_send_success);
                commentViewVisible(GONE, null);
                page = 1;
                getTopicComment();
                emoji_edit_comment.setText("");
            }

            @Override
            public void onError() {
                loadHud.dismiss();
                tv_send_comment.setText("发送");
                tv_send_comment.setEnabled(true);
            }
        });
        constantMethod.setSendComment(DoMoLifeWelfareDetailsActivity.this, communalComment);
    }

    private void setPublishComment() {
        if (VISIBLE == ll_input_comment.getVisibility()) {
            commentViewVisible(GONE, null);
        } else {
            commentViewVisible(VISIBLE, null);
        }
    }

    private void commentViewVisible(int visibility, final DmlSearchCommentBean dmlSearchCommentBean) {
        ll_input_comment.setVisibility(visibility);
        ll_article_comment.setVisibility(visibility == VISIBLE ? GONE : VISIBLE);
        if (VISIBLE == visibility) {
            emoji_edit_comment.requestFocus();
            //弹出键盘
            if (dmlSearchCommentBean != null) {
                emoji_edit_comment.setHint("回复" + dmlSearchCommentBean.getNickname() + ":");
            } else {
                emoji_edit_comment.setHint(R.string.comment_article_hint);
            }
            CommonUtils.showSoftInput(emoji_edit_comment.getContext(), emoji_edit_comment);
            tv_send_comment.setOnClickListener((v) -> {
                //判断有内容调用接口
                String comment = emoji_edit_comment.getText().toString();
                if (!TextUtils.isEmpty(comment)) {
                    comment = emoji_edit_comment.getText().toString();
                    CommunalComment communalComment = new CommunalComment();
                    communalComment.setCommType("topic");
                    communalComment.setContent(comment);
                    if (dmlSearchCommentBean != null) {
                        communalComment.setIsReply(1);
                        communalComment.setReplyUserId(dmlSearchCommentBean.getUid());
                        communalComment.setPid(dmlSearchCommentBean.getId());
                        communalComment.setMainCommentId(dmlSearchCommentBean.getMainContentId() > 0
                                ? dmlSearchCommentBean.getMainContentId() : dmlSearchCommentBean.getId());
                    }
                    communalComment.setObjId(qualityWefBean.getId());
                    communalComment.setUserId(uid);
                    sendComment(communalComment);
                } else {
                    showToast(DoMoLifeWelfareDetailsActivity.this, "请正确输入内容");
                }
            });
        } else if (GONE == visibility) {
            //隐藏键盘
            CommonUtils.hideSoftInput(emoji_edit_comment.getContext(), emoji_edit_comment);
        }
    }

    @OnTouch(R.id.communal_recycler)
    boolean onTouch(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                locationY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float moveY = event.getY() - locationY;//y轴距离
                if (Math.abs(moveY) > 250) {
                    commentViewVisible(GONE, null);
                }
                break;
            case MotionEvent.ACTION_UP:
                locationY = 0;
                break;
        }
        return false;
    }

    @Override
    protected void postEventResult(@NonNull EventMessage message) {
        if (message.type.equals("showEditView")) {
            ReplyCommListBean replyCommListBean = (ReplyCommListBean) message.result;
            DmlSearchCommentBean dmlSearchCommentBean = new DmlSearchCommentBean();
            dmlSearchCommentBean.setNickname(replyCommListBean.getNickname());
            dmlSearchCommentBean.setUid(replyCommListBean.getUid());
            dmlSearchCommentBean.setId(replyCommListBean.getId());
            dmlSearchCommentBean.setMainContentId(replyCommListBean.getMainContentId());
            dmlSearchCommentBean.setObj_id(replyCommListBean.getObj_id());
            if (VISIBLE == ll_input_comment.getVisibility()) {
                commentViewVisible(GONE, dmlSearchCommentBean);
            } else {
                commentViewVisible(VISIBLE, dmlSearchCommentBean);
            }
        } else if (message.type.equals("replyComm")) {
            List<ReplyCommListBean> replyCommList = (List<ReplyCommListBean>) message.result;
            ReplyCommListBean replyCommListBean = replyCommList.get(replyCommList.size() - 1);
            for (int i = 0; i < articleCommentList.size(); i++) {
                DmlSearchCommentBean dmlSearchCommentBean = articleCommentList.get(i);
                if (dmlSearchCommentBean.getId() == replyCommListBean.getMainContentId()) {
                    dmlSearchCommentBean.setReplyCommList(replyCommList);
                    articleCommentList.set(i, dmlSearchCommentBean);
                }
            }
        }
    }

    class WelfareHeaderView {
        //品牌团描述
        @BindView(R.id.tv_welfare_details_content)
        TextView tv_welfare_details_content;
        //头部背景
        @BindView(R.id.iv_cover_detail_bg)
        ImageView iv_welfare_detail_bg;
        //        文章内容
        @BindView(R.id.communal_recycler_wrap)
        RecyclerView communal_recycler_wrap;

        public void initView() {
            communal_recycler_wrap.setLayoutManager(new LinearLayoutManager(DoMoLifeWelfareDetailsActivity.this));
            communal_recycler_wrap.setNestedScrollingEnabled(false);
            communalWelfareDetailAdapter = new CommunalDetailAdapter(DoMoLifeWelfareDetailsActivity.this, itemDescriptionList);
            communal_recycler_wrap.setAdapter(communalWelfareDetailAdapter);
        }
    }

    public class CommentCountView {
        @BindView(R.id.tv_comm_comment_count)
        TextView tv_comm_comment_count;
    }

    @OnClick(R.id.tv_communal_pro_tag)
    void openSlideProList(View view) {
//            商品列表
        if (dr_welfare_detail_pro.isDrawerOpen(ll_wel_pro_list)) {
            dr_welfare_detail_pro.closeDrawers();
        } else {
            dr_welfare_detail_pro.openDrawer(ll_wel_pro_list);
        }
    }

    @OnClick(R.id.tv_publish_comment)
    void publishComment(View view) {
        if (uid > 0) {
            setPublishComment();
        } else {
            getLoginStatus();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (totalPersonalTrajectory != null) {
            Map<String, String> totalMap = new HashMap<>();
            totalMap.put("relate_id", welfareId);
            totalPersonalTrajectory.stopTotal(totalMap);
        }
    }
}
