package com.amkj.dmsh.homepage.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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
import com.alibaba.baichuan.trade.biz.core.taoke.AlibcTaokeParams;
import com.alibaba.baichuan.trade.biz.login.AlibcLogin;
import com.alibaba.baichuan.trade.biz.login.AlibcLoginCallback;
import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.base.NetLoadUtils;
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
import com.amkj.dmsh.dominant.bean.DmlSearchDetailEntity;
import com.amkj.dmsh.dominant.bean.DmlSearchDetailEntity.DmlSearchDetailBean;
import com.amkj.dmsh.dominant.bean.DmlSearchDetailEntity.DmlSearchDetailBean.ProductListBean;
import com.amkj.dmsh.homepage.adapter.CommunalDetailAdapter;
import com.amkj.dmsh.homepage.bean.CommunalOnlyDescription;
import com.amkj.dmsh.homepage.bean.CommunalOnlyDescription.ComOnlyDesBean;
import com.amkj.dmsh.shopdetails.activity.ShopScrollDetailsActivity;
import com.amkj.dmsh.shopdetails.bean.CommunalDetailObjectBean;
import com.amkj.dmsh.user.activity.UserPagerActivity;
import com.amkj.dmsh.utils.CommonUtils;
import com.amkj.dmsh.utils.Log;
import com.amkj.dmsh.utils.NetWorkUtils;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.amkj.dmsh.utils.inteface.MyCallBack;
import com.amkj.dmsh.utils.pinnedsectionitemdecoration.PinnedHeaderItemDecoration;
import com.google.gson.Gson;
import com.klinker.android.link_builder.Link;
import com.klinker.android.link_builder.LinkBuilder;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.umeng.socialize.UMShareAPI;

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
import me.jessyan.autosize.utils.AutoSizeUtils;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.addArticleShareCount;
import static com.amkj.dmsh.constant.ConstantMethod.getDetailsDataList;
import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.insertNewTotalData;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.skipProductUrl;
import static com.amkj.dmsh.constant.ConstantMethod.totalProNum;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.COMMENT_TYPE;
import static com.amkj.dmsh.constant.ConstantVariable.DEFAULT_COMMENT_TOTAL_COUNT;
import static com.amkj.dmsh.constant.ConstantVariable.DEFAULT_TOTAL_COUNT;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.IS_LOGIN_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.TAOBAO_APPKEY;
import static com.amkj.dmsh.shopdetails.bean.CommunalDetailObjectBean.TYPE_COUPON;
import static com.amkj.dmsh.shopdetails.bean.CommunalDetailObjectBean.TYPE_COUPON_PACKAGE;
import static com.amkj.dmsh.shopdetails.bean.CommunalDetailObjectBean.TYPE_PRODUCT_RECOMMEND;
import static com.amkj.dmsh.shopdetails.bean.CommunalDetailObjectBean.TYPE_PRODUCT_TAG;
import static com.amkj.dmsh.utils.CommunalCopyTextUtils.showPopWindow;

;

/**
 * Created by atd48 on 2016/6/30.
 */
public class ArticleOfficialActivity extends BaseActivity {
    @BindView(R.id.smart_communal_refresh)
    SmartRefreshLayout smart_communal_refresh;
    @BindView(R.id.communal_recycler)
    RecyclerView communal_recycler;
    @BindView(R.id.tl_normal_bar)
    Toolbar toolbar;
    @BindView(R.id.tv_header_title)
    TextView tv_header_titleAll;
    @BindView(R.id.tv_header_shared)
    TextView tv_header_shared;
    //  滑动界面
    @BindView(R.id.dl_art_detail_pro)
    DrawerLayout dl_art_detail_pro;
    @BindView(R.id.ll_communal_pro_list)
    LinearLayout ll_communal_pro_list;
    @BindView(R.id.tv_communal_pro_title)
    TextView tv_communal_pro_title;
    @BindView(R.id.rv_communal_pro)
    RecyclerView rv_communal_pro;

    @BindView(R.id.tv_communal_pro_tag)
    TextView tv_communal_pro_tag;
    //    底栏
    @BindView(R.id.rel_article_bottom)
    RelativeLayout rel_article_bottom;
    //    评论输入
    @BindView(R.id.ll_input_comment)
    LinearLayout ll_input_comment;
    @BindView(R.id.emoji_edit_comment)
    EmojiconEditText emoji_edit_comment;
    @BindView(R.id.tv_publish_comment)
    TextView tv_publish_comment;
    @BindView(R.id.tv_send_comment)
    TextView tv_send_comment;
    //    点赞底栏
    @BindView(R.id.ll_article_comment)
    LinearLayout ll_article_comment;
    @BindView(R.id.tv_article_bottom_like)
    TextView tv_article_bottom_like;
    @BindView(R.id.tv_article_bottom_collect)
    TextView tv_article_bottom_collect;

    //    评论列表
    private List<DmlSearchCommentBean> articleCommentList = new ArrayList<>();
    //    详细描述
    private List<CommunalDetailObjectBean> descripDetailList = new ArrayList<>();
    //侧滑商品列表
    private List<ProductListBean> searchProductList = new ArrayList();

    private ArticleCommentAdapter adapterArticleComment;
    private String artId;
    private CoverTitleView coverTitleView;
    private CommunalDetailAdapter communalDescripAdapter;
    private WelfareSlideProAdapter searchSlideProAdapter;
    private int page = 1;
    private DmlSearchDetailBean dmlSearchDetailBean;
    //    按下点击位置
    private float locationY;
    private CommentCountView commentCountView;
    private View commentHeaderView;
    private DmlSearchDetailEntity dmlSearchDetailEntity;

    @Override
    protected int getContentView() {
        return R.layout.activity_article_details;
    }

    @Override
    protected void initViews() {
        toolbar.setSelected(true);
        tv_header_titleAll.setText("");
        Intent intent = getIntent();
        artId = intent.getStringExtra("ArtId");
        rel_article_bottom.setVisibility(GONE);
        communal_recycler.setLayoutManager(new LinearLayoutManager(ArticleOfficialActivity.this));
        adapterArticleComment = new ArticleCommentAdapter(ArticleOfficialActivity.this, articleCommentList, COMMENT_TYPE);
        adapterArticleComment.setHeaderAndEmpty(true);
        View coverView = LayoutInflater.from(ArticleOfficialActivity.this)
                .inflate(R.layout.layout_article_detailes_header, (ViewGroup) communal_recycler.getParent(), false);
        commentHeaderView = LayoutInflater.from(ArticleOfficialActivity.this)
                .inflate(R.layout.layout_comm_comment_header, (ViewGroup) communal_recycler.getParent(), false);
        coverTitleView = new CoverTitleView();
        ButterKnife.bind(coverTitleView, coverView);
        commentCountView = new CommentCountView();
        ButterKnife.bind(commentCountView, commentHeaderView);
        coverTitleView.initView();
        adapterArticleComment.addHeaderView(coverView);
        communal_recycler.setAdapter(adapterArticleComment);
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
        adapterArticleComment.setOnItemChildClickListener((adapter, view, position) -> {
            DmlSearchCommentBean dmlSearchCommentBean = (DmlSearchCommentBean) view.getTag(R.id.iv_tag);
            if (dmlSearchCommentBean == null) {
                dmlSearchCommentBean = (DmlSearchCommentBean) view.getTag();
            }
            if (dmlSearchCommentBean != null) {
                switch (view.getId()) {
                    case R.id.tv_comm_comment_like:
                        if (userId > 0) {
                            dmlSearchCommentBean.setFavor(!dmlSearchCommentBean.isFavor());
                            int likeNum = dmlSearchCommentBean.getLike_num();
                            dmlSearchCommentBean.setLike_num(dmlSearchCommentBean.isFavor()
                                    ? likeNum + 1 : likeNum - 1 > 0 ? likeNum - 1 : 0);
                            articleCommentList.set(position, dmlSearchCommentBean);
                            adapterArticleComment.notifyItemChanged(position + adapterArticleComment.getHeaderLayoutCount());
                            setCommentLike(dmlSearchCommentBean);
                        } else {
                            getLoginStatus(ArticleOfficialActivity.this);
                        }
                        break;
                    case R.id.tv_comm_comment_receive:
//                            打开评论
                        if (userId > 0) {
                            if (VISIBLE == ll_input_comment.getVisibility()) {
                                commentViewVisible(GONE, dmlSearchCommentBean);
                            } else {
                                commentViewVisible(VISIBLE, dmlSearchCommentBean);
                            }
                        } else {
                            getLoginStatus(ArticleOfficialActivity.this);
                        }
                        break;
                    case R.id.civ_comm_comment_avatar:
                        Intent intent1 = new Intent();
                        intent1.setClass(ArticleOfficialActivity.this, UserPagerActivity.class);
                        intent1.putExtra("userId", String.valueOf(dmlSearchCommentBean.getUid()));
                        startActivity(intent1);
                        break;
                    default:
                        break;
                }
            }
        });
        adapterArticleComment.setOnItemChildLongClickListener((adapter, view, position) -> {
            DmlSearchCommentBean dmlSearchCommentBean = (DmlSearchCommentBean) view.getTag();
            if (dmlSearchCommentBean != null && !TextUtils.isEmpty(dmlSearchCommentBean.getContent())) {
                showPopWindow(ArticleOfficialActivity.this, (TextView) view, dmlSearchCommentBean.getContent());
            }
            return false;
        });
        adapterArticleComment.setOnLoadMoreListener(() -> {
            if (page * DEFAULT_TOTAL_COUNT <= articleCommentList.size()) {
                page++;
                getArticleComment();
            } else {
                adapterArticleComment.loadMoreEnd();
            }
        }, communal_recycler);
//侧滑布局
        rv_communal_pro.setLayoutManager(new LinearLayoutManager(ArticleOfficialActivity.this));
        rv_communal_pro.addItemDecoration(new PinnedHeaderItemDecoration.Builder(-1)
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_gray_f_two_px)
                // 开启绘制分隔线，默认关闭
                .enableDivider(true)
                // 是否关闭标签点击事件，默认开启
                .disableHeaderClick(false)
                // 设置标签和其内部的子控件的监听，若设置点击监听不为null，但是disableHeaderClick(true)的话，还是不会响应点击事件
                .setHeaderClickListener(null)
                .create());
        searchSlideProAdapter = new WelfareSlideProAdapter(ArticleOfficialActivity.this, searchProductList);
        searchSlideProAdapter.setEnableLoadMore(false);
        rv_communal_pro.setAdapter(searchSlideProAdapter);
        searchSlideProAdapter.setOnItemClickListener((adapter, view, position) -> {
            ProductListBean productListBean = (ProductListBean) view.getTag();
            if (productListBean != null) {
                dl_art_detail_pro.closeDrawers();
                skipProductUrl(ArticleOfficialActivity.this, productListBean.getItemTypeId(), productListBean.getId());
//                    统计商品点击
                totalProNum(productListBean.getId(), dmlSearchDetailBean.getId());
            }
        });
        communalDescripAdapter.setOnItemClickListener((adapter, view, position) -> {
            CommunalDetailObjectBean communalDetailBean = (CommunalDetailObjectBean) view.getTag();
            if (communalDetailBean != null) {
                skipProductUrl(ArticleOfficialActivity.this, communalDetailBean.getItemTypeId(), communalDetailBean.getId());
//                    统计商品点击
                totalProNum(communalDetailBean.getId(), dmlSearchDetailBean.getId());
            }
        });
        communalDescripAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            if (loadHud != null) {
                loadHud.show();
            }
            switch (view.getId()) {
                case R.id.img_product_coupon_pic:
                    int couponId = (int) view.getTag(R.id.iv_avatar_tag);
                    int type = (int) view.getTag(R.id.iv_type_tag);
                    if (couponId > 0) {
                        if (userId != 0) {
                            if (type == TYPE_COUPON) {
                                getDirectCoupon(couponId);
                            } else if (type == TYPE_COUPON_PACKAGE) {
                                getDirectCouponPackage(couponId);
                            }
                        } else {
                            if (loadHud != null) {
                                loadHud.dismiss();
                            }
                            getLoginStatus(ArticleOfficialActivity.this);
                        }
                    }
                    break;
                case R.id.iv_communal_cover_wrap:
                    CommunalDetailObjectBean detailObjectBean = (CommunalDetailObjectBean) view.getTag(R.id.iv_tag);
                    if (detailObjectBean != null) {
                        if (detailObjectBean.getItemType() == CommunalDetailObjectBean.TYPE_GOODS_IMG) {
                            Intent newIntent = new Intent(ArticleOfficialActivity.this, ShopScrollDetailsActivity.class);
                            newIntent.putExtra("productId", String.valueOf(detailObjectBean.getId()));
                            startActivity(newIntent);
                        }
                    }
                    loadHud.dismiss();
                    break;

                case R.id.ll_layout_tb_coupon:
                    CommunalDetailObjectBean couponBean = (CommunalDetailObjectBean) view.getTag();
                    if (couponBean != null) {
                        if (loadHud != null) {
                            loadHud.dismiss();
                        }
                        if (userId != 0) {
                            skipAliBCWebView(couponBean.getCouponUrl());
                        } else {
                            if (loadHud != null) {
                                loadHud.dismiss();
                            }
                            getLoginStatus(ArticleOfficialActivity.this);
                        }
                    }
                    break;
                case R.id.iv_ql_bl_add_car:
                    CommunalDetailObjectBean qualityWelPro = (CommunalDetailObjectBean) view.getTag();
                    if (qualityWelPro != null) {
                        if (userId > 0) {
                            BaseAddCarProInfoBean baseAddCarProInfoBean = new BaseAddCarProInfoBean();
                            baseAddCarProInfoBean.setProductId(qualityWelPro.getId());
                            baseAddCarProInfoBean.setProName(getStrings(qualityWelPro.getName()));
                            baseAddCarProInfoBean.setProPic(getStrings(qualityWelPro.getPicUrl()));
                            ConstantMethod constantMethod = new ConstantMethod();
                            constantMethod.addShopCarGetSku(ArticleOfficialActivity.this, baseAddCarProInfoBean, loadHud);
                        } else {
                            loadHud.dismiss();
                            getLoginStatus(ArticleOfficialActivity.this);
                        }
                    }
                    break;
                case R.id.tv_communal_share:
                    loadHud.dismiss();
                    setShareData();
                    break;
                case R.id.tv_communal_tb_link:
                case R.id.iv_communal_tb_cover:
                    if (loadHud != null) {
                        loadHud.show();
                    }
                    CommunalDetailObjectBean tbLink = (CommunalDetailObjectBean) view.getTag(R.id.iv_tag);
                    if (tbLink == null) {
                        tbLink = (CommunalDetailObjectBean) view.getTag();
                    }
                    if (tbLink != null) {
                        skipAliBCWebView(tbLink.getUrl());
                    }
                    break;
                default:
                    loadHud.dismiss();
                    break;
            }
        });

        smart_communal_refresh.setOnRefreshListener((refreshLayout) -> {
            loadData();
        });
        //          关闭手势滑动
        dl_art_detail_pro.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.END);
        totalPersonalTrajectory = insertNewTotalData(ArticleOfficialActivity.this, artId);
        tv_publish_comment.setText(R.string.comment_article_hint);
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

    @Override
    protected View getLoadView() {
        return smart_communal_refresh;
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    @Override
    protected void loadData() {
        page = 1;
        getArticleData();
        getArticleComment();
    }

    private void getArticleComment() {
        if (NetWorkUtils.checkNet(ArticleOfficialActivity.this)) {
            String url = Url.BASE_URL + Url.Q_DML_SEARCH_COMMENT;
            Map<String, Object> params = new HashMap<>();
            params.put("id", artId);
            params.put("currentPage", page);
            if (userId > 0) {
                params.put("uid", userId);
            }
            params.put("showCount", DEFAULT_TOTAL_COUNT);
            params.put("replyCurrentPage", 1);
            params.put("replyShowCount", DEFAULT_COMMENT_TOTAL_COUNT);
            params.put("comtype", "doc");
            XUtil.Post(url, params, new MyCallBack<String>() {
                @Override
                public void onSuccess(String result) {
                    adapterArticleComment.loadMoreComplete();
                    if (page == 1) {
                        articleCommentList.clear();
                    }
                    Gson gson = new Gson();
                    DmlSearchCommentEntity dmlSearchCommentEntity = gson.fromJson(result, DmlSearchCommentEntity.class);
                    if (dmlSearchCommentEntity != null) {
                        if (dmlSearchCommentEntity.getCode().equals("01")) {
                            articleCommentList.addAll(dmlSearchCommentEntity.getDmlSearchCommentList());
                        } else if (!dmlSearchCommentEntity.getCode().equals("02")) {
                            showToast(ArticleOfficialActivity.this, dmlSearchCommentEntity.getMsg());
                        }
                        adapterArticleComment.removeHeaderView(commentHeaderView);
                        if (articleCommentList.size() > 0) {
                            adapterArticleComment.addHeaderView(commentHeaderView);
                            commentCountView.tv_comm_comment_count.setText(String.format(getString(R.string.comment_handpick_count), dmlSearchCommentEntity.getCommentSize()));
                        }
                        adapterArticleComment.notifyDataSetChanged();
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    adapterArticleComment.loadMoreComplete();
                    showToast(ArticleOfficialActivity.this, R.string.invalidData);
                    super.onError(ex, isOnCallback);
                }
            });
        }
    }

    private void getArticleData() {
        String url = Url.BASE_URL + Url.F_INVITATION_DETAIL;
        Map<String, Object> params = new HashMap<>();
        params.put("id", artId);
        if (userId > 0) {
            params.put("fuid", userId);
        }
        NetLoadUtils.getQyInstance().loadNetDataPost(ArticleOfficialActivity.this, url, params, new NetLoadUtils.NetLoadListener() {
            @Override
            public void onSuccess(String result) {
                smart_communal_refresh.finishRefresh();
                descripDetailList.clear();
                Gson gson = new Gson();
                dmlSearchDetailEntity = gson.fromJson(result, DmlSearchDetailEntity.class);
                if (dmlSearchDetailEntity != null) {
                    if (dmlSearchDetailEntity.getCode().equals(SUCCESS_CODE)) {
                        dmlSearchDetailBean = dmlSearchDetailEntity.getDmlSearchDetailBean();
                        setSearchData(dmlSearchDetailBean);
                    } else if (!dmlSearchDetailEntity.getCode().equals(EMPTY_CODE)) {
                        showToast(ArticleOfficialActivity.this, dmlSearchDetailEntity.getMsg());
                    }
                }
                NetLoadUtils.getQyInstance().showLoadSir(loadService,dmlSearchDetailBean, dmlSearchDetailEntity);
            }

            @Override
            public void netClose() {
                smart_communal_refresh.finishRefresh();
                showToast(ArticleOfficialActivity.this, R.string.unConnectedNetwork);
                NetLoadUtils.getQyInstance().showLoadSir(loadService,dmlSearchDetailBean,dmlSearchDetailEntity);
            }

            @Override
            public void onError(Throwable throwable) {
                smart_communal_refresh.finishRefresh();
                showToast(ArticleOfficialActivity.this, R.string.invalidData);
                NetLoadUtils.getQyInstance().showLoadSir(loadService,dmlSearchDetailBean,dmlSearchDetailEntity);
            }
        });
    }

    private void setSearchData(DmlSearchDetailBean dmlSearchDetailBean) {
//        是否有分享数据
        tv_header_titleAll.setText(getStrings(dmlSearchDetailBean.getTitle()));
        GlideImageLoaderUtil.loadCenterCrop(ArticleOfficialActivity.this, coverTitleView.img_article_details_bg, dmlSearchDetailBean.getPath());
        coverTitleView.tv_article_details_title.setText(getStrings(dmlSearchDetailBean.getTitle()));
        coverTitleView.tv_article_details_time.setText(getStrings(dmlSearchDetailBean.getCtime()));
        if (!TextUtils.isEmpty(dmlSearchDetailBean.getCategory_title())) {
            String tagName = dmlSearchDetailBean.getCategory_title();
            String content = "来自于 " + tagName;

            Link replyNameLink = new Link(tagName);
//                    回复颜色
            replyNameLink.setTextColor(Color.parseColor("#0a88fa"));
            replyNameLink.setUnderlined(false);
            replyNameLink.setHighlightAlpha(0f);
            coverTitleView.tv_article_label.setText(content);
            LinkBuilder.on(coverTitleView.tv_article_label)
                    .setText(content)
                    .addLink(replyNameLink)
                    .build();
        } else {
            coverTitleView.tv_article_label.setVisibility(GONE);
        }
        searchProductList.clear();
        if (dmlSearchDetailBean.getProductList() != null
                && dmlSearchDetailBean.getProductList().size() > 0) {
            searchProductList.addAll(dmlSearchDetailBean.getProductList());
            searchSlideProAdapter.setNewData(searchProductList);
            setPrepareData(dmlSearchDetailBean);
        } else {
            tv_communal_pro_tag.setVisibility(GONE);
            ll_communal_pro_list.setVisibility(GONE);
        }
        rel_article_bottom.setVisibility(VISIBLE);
        tv_article_bottom_like.setSelected(dmlSearchDetailBean.isIsFavor());
        tv_article_bottom_like.setText(dmlSearchDetailBean.getFavor() > 0 ? String.valueOf(dmlSearchDetailBean.getFavor()) : "赞");
        tv_article_bottom_collect.setSelected(dmlSearchDetailBean.isIsCollect());
        tv_article_bottom_collect.setText("收藏");
        List<CommunalDetailBean> descriptionList = dmlSearchDetailBean.getDescription();
        if (descriptionList != null) {
            descripDetailList.clear();
            descripDetailList.addAll(getDetailsDataList(descriptionList));
            if (dmlSearchDetailBean.getDocumentProductList() != null
                    && dmlSearchDetailBean.getDocumentProductList().size() > 0) {
                CommunalDetailObjectBean communalDetailBean = new CommunalDetailObjectBean();
                communalDetailBean.setItemType(TYPE_PRODUCT_RECOMMEND);
                communalDetailBean.setProductList(dmlSearchDetailBean.getDocumentProductList());
                descripDetailList.add(communalDetailBean);
            }
            if (dmlSearchDetailBean.getTags() != null
                    && dmlSearchDetailBean.getTags().size() > 0) {
                CommunalDetailObjectBean communalDetailBean = new CommunalDetailObjectBean();
                communalDetailBean.setItemType(TYPE_PRODUCT_TAG);
                communalDetailBean.setTagsBeans(dmlSearchDetailBean.getTags());
                descripDetailList.add(communalDetailBean);
            }
            getShareData();
            communalDescripAdapter.setNewData(descripDetailList);
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
                            descripDetailList.add(communalDetailObjectBean);
                            descripDetailList.addAll(getDetailsDataList(comOnlyDesBean.getDescriptionList()));
                            communalDescripAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }
        });
    }

    private void setPrepareData(DmlSearchDetailBean dmlSearchDetailBean) {
        tv_communal_pro_tag.setVisibility(VISIBLE);
        ll_communal_pro_list.setVisibility(VISIBLE);
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        int radius = AutoSizeUtils.mm2px(mAppContext,50);
        drawable.setCornerRadii(new float[]{radius, radius, 0, 0, 0, 0, radius, radius});
        try {
            drawable.setColor(0xffffffff);
            drawable.setStroke(1, 0xffcccccc);
        } catch (Exception e) {
            e.printStackTrace();
        }
        tv_communal_pro_tag.setBackground(drawable);
        tv_communal_pro_tag.setText((searchProductList.size() + "商品"));
        tv_communal_pro_title.setText(getStrings(dmlSearchDetailBean.getTitle()));
    }

    private void getDirectCoupon(int id) {
        String url = Url.BASE_URL + Url.FIND_ARTICLE_COUPON;
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
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
                        showToast(ArticleOfficialActivity.this, requestStatus.getResult() != null ? requestStatus.getResult().getMsg() : requestStatus.getMsg());
                    } else {
                        showToast(ArticleOfficialActivity.this, requestStatus.getResult() != null ? requestStatus.getResult().getMsg() : requestStatus.getMsg());
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                showToast(ArticleOfficialActivity.this, R.string.Get_Coupon_Fail);
                if (loadHud != null) {
                    loadHud.dismiss();
                }
            }
        });
    }

    private void getDirectCouponPackage(int couponId) {
        String url = Url.BASE_URL + Url.COUPON_PACKAGE;
        Map<String, Object> params = new HashMap<>();
        params.put("uId", userId);
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
                        showToast(ArticleOfficialActivity.this, requestStatus.getResult() != null ? requestStatus.getResult().getMsg() : requestStatus.getMsg());
                    } else {
                        showToast(ArticleOfficialActivity.this, requestStatus.getResult() != null ? requestStatus.getResult().getMsg() : requestStatus.getMsg());
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                showToast(ArticleOfficialActivity.this, R.string.Get_Coupon_Fail);
                if (loadHud != null) {
                    loadHud.dismiss();
                }
            }
        });
    }

    public void skipAliBCWebView(final String url) {
        if (!TextUtils.isEmpty(url)) {
            if (userId != 0) {
                skipNewTaoBao(url);
            } else {
                if (loadHud != null) {
                    loadHud.dismiss();
                }
                getLoginStatus(ArticleOfficialActivity.this);
            }
        } else {
            showToast(ArticleOfficialActivity.this, "地址缺失");
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
                showToast(ArticleOfficialActivity.this, "登录失败 ");
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
        AlibcTaokeParams alibcTaokeParams = new AlibcTaokeParams();
//        alibcTaokeParams.setPid(TAOBAO_PID);
//        alibcTaokeParams.setAdzoneid(TAOBAO_ADZONEID);
        alibcTaokeParams.extraParams = new HashMap<>();
        alibcTaokeParams.extraParams.put("taokeAppkey", TAOBAO_APPKEY);
        AlibcTrade.show(ArticleOfficialActivity.this, ordersPage, showParams, alibcTaokeParams, exParams, new AlibcTradeCallback() {
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

    private void setCommentLike(DmlSearchCommentBean dmlSearchCommentBean) {
        String url = Url.BASE_URL + Url.FIND_AND_COMMENT_FAV;
        Map<String, Object> params = new HashMap<>();
        //用户id
        params.put("tuid", userId);
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

    private void setArticleLike() {
        String url = Url.BASE_URL + Url.F_ARTICLE_DETAILS_FAVOR;
        Map<String, Object> params = new HashMap<>();
        //用户id
        params.put("tuid", userId);
        //关注id
        params.put("id", dmlSearchDetailBean.getId());
        params.put("favortype", "doc");
        XUtil.Post(url, params, new MyCallBack<String>() {
            @Override
            public void onSuccess(String result) {
            }
        });
        tv_article_bottom_like.setSelected(!tv_article_bottom_like.isSelected());
        String likeCount = getNumber(tv_article_bottom_like.getText().toString().trim());
        int likeNum = Integer.parseInt(likeCount);
        tv_article_bottom_like.setText(String.valueOf(tv_article_bottom_like.isSelected()
                ? likeNum + 1 : likeNum - 1 > 0 ? likeNum - 1 : "赞"));
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

    //    文章收藏
    private void setArticleCollect() {
        String url = Url.BASE_URL + Url.F_ARTICLE_COLLECT;
        Map<String, Object> params = new HashMap<>();
        //用户id
        params.put("uid", userId);
        //文章id
        params.put("object_id", dmlSearchDetailBean.getId());
        params.put("type", ConstantVariable.TYPE_C_ARTICLE);
        XUtil.Post(url, params, new MyCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                loadHud.dismiss();
                Gson gson = new Gson();
                RequestStatus requestStatus = gson.fromJson(result, RequestStatus.class);
                if (requestStatus != null) {
                    if (requestStatus.getCode().equals("01")) {
                        tv_article_bottom_collect.setSelected(!tv_article_bottom_collect.isSelected());
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                loadHud.dismiss();
                showToast(ArticleOfficialActivity.this, String.format(getResources().getString(R.string.collect_failed), "文章"));
                super.onError(ex, isOnCallback);
            }
        });
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
                showToast(ArticleOfficialActivity.this, R.string.comment_article_send_success);
                commentViewVisible(GONE, null);
                page = 1;
                getArticleComment();
                emoji_edit_comment.setText("");
            }

            @Override
            public void onError() {
                loadHud.dismiss();
                tv_send_comment.setText("发送");
                tv_send_comment.setEnabled(true);
            }
        });
        constantMethod.setSendComment(ArticleOfficialActivity.this, communalComment);
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
                emoji_edit_comment.setHint(getString(R.string.comment_article_hint));
            }
            CommonUtils.showSoftInput(emoji_edit_comment.getContext(), emoji_edit_comment);
            tv_send_comment.setOnClickListener((v) -> {
                //判断有内容调用接口
                String comment = emoji_edit_comment.getText().toString();
                if (!TextUtils.isEmpty(comment)) {
                    comment = emoji_edit_comment.getText().toString();
                    CommunalComment communalComment = new CommunalComment();
                    communalComment.setCommType(COMMENT_TYPE);
                    communalComment.setContent(comment);
                    if (dmlSearchCommentBean != null) {
                        communalComment.setIsReply(1);
                        communalComment.setReplyUserId(dmlSearchCommentBean.getUid());
                        communalComment.setPid(dmlSearchCommentBean.getId());
                        communalComment.setMainCommentId(dmlSearchCommentBean.getMainContentId() > 0
                                ? dmlSearchCommentBean.getMainContentId() : dmlSearchCommentBean.getId());
                    }
                    communalComment.setObjId(dmlSearchDetailBean.getId());
                    communalComment.setUserId(userId);
                    communalComment.setToUid(dmlSearchDetailBean.getUid());
                    sendComment(communalComment);
                } else {
                    showToast(ArticleOfficialActivity.this, "请正确输入内容");
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

    //    页面分享
    @OnClick(R.id.tv_header_shared)
    void sendShare() {
        setShareData();
    }

    private void setShareData() {
        if (dmlSearchDetailBean != null) {
            UMShareAction umShareAction = new UMShareAction(ArticleOfficialActivity.this
                    , dmlSearchDetailBean.getPath()
                    , getStrings(dmlSearchDetailBean.getTitle())
                    , getStrings(dmlSearchDetailBean.getDigest())
                    , Url.BASE_SHARE_PAGE_TWO + ("m/template/goods/study_detail.html" + "?id="
                    + dmlSearchDetailBean.getId() + (userId > 0 ? "&sid=" + userId : "")));
            umShareAction.setOnShareSuccessListener(() ->
                    addArticleShareCount(dmlSearchDetailBean.getId())
            );
        }
    }

    @OnClick(R.id.tv_life_back)
    void goBack(View view) {
        finish();
    }

    @OnClick(R.id.tv_publish_comment)
    void publishComment(View view) {
        if (userId > 0) {
            if (VISIBLE == ll_input_comment.getVisibility()) {
                commentViewVisible(GONE, null);
            } else {
                commentViewVisible(VISIBLE, null);
            }
        } else {
            getLoginStatus(ArticleOfficialActivity.this);
        }
    }

    //文章点赞
    @OnClick(R.id.tv_article_bottom_like)
    void likeArticle(View view) {
        if (dmlSearchDetailBean != null) {
            if (userId > 0) {
                setArticleLike();
            } else {
                getLoginStatus(ArticleOfficialActivity.this);
            }
        }
    }

    //文章收藏
    @OnClick(R.id.tv_article_bottom_collect)
    void collectArticle(View view) {
        if (dmlSearchDetailBean != null) {
            if (userId > 0) {
                loadHud.show();
                setArticleCollect();
            } else {
                getLoginStatus(ArticleOfficialActivity.this);
            }
        }
    }

    @OnClick(R.id.tv_communal_pro_tag)
    void openSlideProList() {
//            商品列表
        if (dl_art_detail_pro.isDrawerOpen(ll_communal_pro_list)) {
            dl_art_detail_pro.closeDrawers();
        } else {
            dl_art_detail_pro.openDrawer(ll_communal_pro_list);
        }
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

    class CoverTitleView {
        //        标题
        @BindView(R.id.tv_article_details_title)
        TextView tv_article_details_title;
        //        封面图
        @BindView(R.id.img_article_details_bg)
        ImageView img_article_details_bg;
        //        发布时间
        @BindView(R.id.tv_article_details_time)
        TextView tv_article_details_time;
        //        文章类别
        @BindView(R.id.tv_article_label)
        TextView tv_article_label;
        //        文章内容
        @BindView(R.id.communal_recycler_wrap)
        RecyclerView communal_recycler_wrap;
        //        评论数

        public void initView() {
            communal_recycler_wrap.setLayoutManager(new LinearLayoutManager(ArticleOfficialActivity.this));
            communal_recycler_wrap.setNestedScrollingEnabled(false);
            communalDescripAdapter = new CommunalDetailAdapter(ArticleOfficialActivity.this, descripDetailList);
            communalDescripAdapter.setEnableLoadMore(false);
            communal_recycler_wrap.setAdapter(communalDescripAdapter);
        }

        @OnClick(R.id.tv_article_label)
        void skipArticleLabel() {
            if (dmlSearchDetailBean.getCategory_id() > 0
                    && !TextUtils.isEmpty(dmlSearchDetailBean.getCategory_title())) {
                Intent intent = new Intent(ArticleOfficialActivity.this, ArticleTypeActivity.class);
                intent.putExtra("categoryId", String.valueOf(dmlSearchDetailBean.getCategory_id()));
                intent.putExtra("categoryTitle", getStrings(dmlSearchDetailBean.getCategory_title()));
                startActivity(intent);
            }
        }
    }

    public class CommentCountView {
        @BindView(R.id.tv_comm_comment_count)
        TextView tv_comm_comment_count;
    }

    @Override
    protected void onPause() {
        super.onPause();
        insertTotalData();
    }

    @Override
    protected void onStop() {
        super.onStop();
        insertTotalData();
    }

    private void insertTotalData() {
        if (totalPersonalTrajectory != null) {
            Map<String, String> totalMap = new HashMap<>();
            totalMap.put("relate_id", artId);
            totalPersonalTrajectory.stopTotal(totalMap);
        }
    }
}
