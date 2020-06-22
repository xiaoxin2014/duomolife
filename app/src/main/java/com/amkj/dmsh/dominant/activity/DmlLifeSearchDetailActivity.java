package com.amkj.dmsh.dominant.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.bean.CommunalComment;
import com.amkj.dmsh.bean.CommunalDetailBean;
import com.amkj.dmsh.bean.RequestStatus;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.ConstantVariable;
import com.amkj.dmsh.constant.UMShareAction;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.dao.CommentDao;
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
import com.amkj.dmsh.mine.activity.ShopCarActivity;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.shopdetails.bean.CommunalDetailObjectBean;
import com.amkj.dmsh.user.activity.UserPagerActivity;
import com.amkj.dmsh.utils.CommonUtils;
import com.amkj.dmsh.utils.CommunalCopyTextUtils;
import com.amkj.dmsh.utils.KeyboardUtils;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.amkj.dmsh.utils.gson.GsonUtils;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;
import com.amkj.dmsh.utils.webformatdata.CommunalWebDetailUtils;
import com.amkj.dmsh.utils.webformatdata.ShareDataBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.melnykov.fab.FloatingActionButton;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.umeng.socialize.UMShareAPI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.emoji.widget.EmojiEditText;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;
import me.jessyan.autosize.utils.AutoSizeUtils;
import q.rorbin.badgeview.Badge;

import static android.view.View.GONE;
import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.skipProductUrl;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.COMMENT_TYPE;
import static com.amkj.dmsh.constant.ConstantVariable.DEFAULT_COMMENT_TOTAL_COUNT;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.TOTAL_COUNT_TEN;
import static com.amkj.dmsh.constant.Url.FIND_AND_COMMENT_FAV;
import static com.amkj.dmsh.constant.Url.F_ARTICLE_COLLECT;
import static com.amkj.dmsh.constant.Url.F_ARTICLE_DETAILS_FAVOR;
import static com.amkj.dmsh.constant.Url.F_INVITATION_DETAIL;
import static com.amkj.dmsh.constant.Url.Q_DML_SEARCH_COMMENT;
import static com.amkj.dmsh.constant.Url.SHARE_COMMUNAL_ARTICLE;
import static com.amkj.dmsh.dao.AddClickDao.totalWelfareProNum;
import static com.amkj.dmsh.dao.OrderDao.getCarCount;
import static com.amkj.dmsh.shopdetails.bean.CommunalDetailObjectBean.TYPE_PRODUCT_TAG;

;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/7/7
 * class description:生活研究所详情 种草营
 */
public class DmlLifeSearchDetailActivity extends BaseActivity {
    @BindView(R.id.smart_communal_refresh)
    SmartRefreshLayout smart_communal_refresh;
    @BindView(R.id.communal_recycler)
    RecyclerView communal_recycler;
    //    滚动至顶部
    @BindView(R.id.download_btn_communal)
    public FloatingActionButton download_btn_communal;
    @BindView(R.id.tv_header_title)
    TextView tv_header_titleAll;
    @BindView(R.id.iv_img_service)
    ImageView iv_img_service;
    @BindView(R.id.fl_header_service)
    FrameLayout fl_header_service;
    //  滑动界面
    @BindView(R.id.dl_ql_search)
    DrawerLayout dl_ql_search;
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
    EmojiEditText emoji_edit_comment;
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
    @BindView(R.id.tl_quality_bar)
    Toolbar tl_quality_bar;

    //    评论列表
    private List<DmlSearchCommentBean> articleCommentList = new ArrayList<>();
    //    详细描述
    private List<CommunalDetailObjectBean> descripDetailList = new ArrayList<>();
    //侧滑商品列表
    private List<ProductListBean> searchProductList = new ArrayList();

    private ArticleCommentAdapter adapterArticleComment;
    private String searchId;
    private CoverTitleView coverTitleView;
    private CommunalDetailAdapter communalDescripAdapter;
    private WelfareSlideProAdapter searchSlideProAdapter;
    private Badge badge;
    private int page = 1;
    private DmlSearchDetailBean dmlSearchDetailBean;
    //    按下点击位置
    private float locationY;
    private View commentHeaderView;
    private CommentCountView commentCountView;
    private DmlSearchDetailEntity dmlSearchDetailEntity;

    @Override
    protected int getContentView() {
        return R.layout.activity_dml_search_detail;
    }

    @Override
    protected void initViews() {
        tv_header_titleAll.setText("");
        Intent intent = getIntent();
        searchId = intent.getStringExtra("searchId");
        rel_article_bottom.setVisibility(View.GONE);
        iv_img_service.setImageResource(R.drawable.shop_car_gray_icon);
        communal_recycler.setLayoutManager(new LinearLayoutManager(DmlLifeSearchDetailActivity.this));
        adapterArticleComment = new ArticleCommentAdapter(DmlLifeSearchDetailActivity.this, articleCommentList, COMMENT_TYPE);
        adapterArticleComment.setHeaderAndEmpty(true);
        View coverView = LayoutInflater.from(DmlLifeSearchDetailActivity.this)
                .inflate(R.layout.layout_cover_title_descrip_header, (ViewGroup) communal_recycler.getParent(), false);
        commentHeaderView = LayoutInflater.from(DmlLifeSearchDetailActivity.this)
                .inflate(R.layout.layout_comm_comment_header, (ViewGroup) communal_recycler.getParent(), false);
        coverTitleView = new CoverTitleView();
        ButterKnife.bind(coverTitleView, coverView);
        coverTitleView.initView();
        commentCountView = new CommentCountView();
        ButterKnife.bind(commentCountView, commentHeaderView);
        adapterArticleComment.addHeaderView(coverView);
        communal_recycler.setAdapter(adapterArticleComment);
        communal_recycler.addItemDecoration(new ItemDecoration.Builder()
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_gray_f_one_px)
                .create());
        adapterArticleComment.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
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
                                getLoginStatus(DmlLifeSearchDetailActivity.this);
                            }
                            break;
                        case R.id.tv_comm_comment_receive:
//                            打开评论
                            if (userId > 0) {
                                if (View.VISIBLE == ll_input_comment.getVisibility()) {
                                    commentViewVisible(GONE, dmlSearchCommentBean);
                                } else {
                                    commentViewVisible(View.VISIBLE, dmlSearchCommentBean);
                                }
                            } else {
                                getLoginStatus(DmlLifeSearchDetailActivity.this);
                            }
                            break;
                        case R.id.civ_comm_comment_avatar:
                            Intent intent = new Intent();
                            intent.setClass(DmlLifeSearchDetailActivity.this, UserPagerActivity.class);
                            intent.putExtra("userId", String.valueOf(dmlSearchCommentBean.getUid()));
                            startActivity(intent);
                            break;
                    }
                }
            }
        });
        adapterArticleComment.setOnItemChildLongClickListener(new BaseQuickAdapter.OnItemChildLongClickListener() {
            @Override
            public boolean onItemChildLongClick(BaseQuickAdapter adapter, View view, int position) {
                DmlSearchCommentBean dmlSearchCommentBean = (DmlSearchCommentBean) view.getTag();
                if (dmlSearchCommentBean != null && !TextUtils.isEmpty(dmlSearchCommentBean.getContent())) {
                    CommunalCopyTextUtils.showPopWindow(DmlLifeSearchDetailActivity.this, (TextView) view, dmlSearchCommentBean.getContent());
                }
                return false;
            }
        });

        adapterArticleComment.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                page++;
                getSearchComment();
            }
        }, communal_recycler);
//      侧滑布局
        rv_communal_pro.setLayoutManager(new LinearLayoutManager(DmlLifeSearchDetailActivity.this));
        rv_communal_pro.addItemDecoration(new ItemDecoration.Builder()
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_gray_f_one_px)
                .create());

        searchSlideProAdapter = new WelfareSlideProAdapter(DmlLifeSearchDetailActivity.this, searchProductList);
        searchSlideProAdapter.setEnableLoadMore(false);
        rv_communal_pro.setAdapter(searchSlideProAdapter);
        searchSlideProAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ProductListBean productListBean = (ProductListBean) view.getTag();
                if (productListBean != null) {
                    dl_ql_search.closeDrawers();
                    skipProductUrl(DmlLifeSearchDetailActivity.this, productListBean.getItemTypeId(), productListBean.getId());
                    //                    统计商品点击
                    totalWelfareProNum(DmlLifeSearchDetailActivity.this, productListBean.getId(), searchId);
                }
            }
        });
        communalDescripAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                ShareDataBean shareDataBean = null;
                if (view.getId() == R.id.tv_communal_share && dmlSearchDetailBean != null) {
                    shareDataBean = new ShareDataBean(dmlSearchDetailBean.getPath()
                            , getStrings(dmlSearchDetailBean.getTitle())
                            , getStrings(dmlSearchDetailBean.getDigest())
                            , Url.BASE_SHARE_PAGE_TWO + "m/template/goods/study_detail.html?id="
                            + dmlSearchDetailBean.getId(), dmlSearchDetailBean.getId());

                }
                CommunalWebDetailUtils.getCommunalWebInstance()
                        .setWebDataClick(DmlLifeSearchDetailActivity.this, shareDataBean, view, loadHud);
            }
        });

        smart_communal_refresh.setOnRefreshListener(refreshLayout -> {
            page = 1;
            getData();
        });
        badge = ConstantMethod.getBadge(DmlLifeSearchDetailActivity.this, fl_header_service);
        //          关闭手势滑动
        dl_ql_search.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.END);
        tv_publish_comment.setText(R.string.comment_article_hint);
        KeyboardUtils.registerSoftInputChangedListener(this, new KeyboardUtils.OnSoftInputChangedListener() {
            @Override
            public void onSoftInputChanged(int height) {
                if (height == 0) {
                    ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0).requestFocus();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public View getLoadView() {
        return smart_communal_refresh;
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    @Override
    protected void loadData() {
        page = 1;
        getData();
    }

    @Override
    protected void getData() {
        getSearchData();
        getSearchComment();
        getCarCount(getActivity());
    }


    private void getSearchComment() {
        Map<String, Object> params = new HashMap<>();
        params.put("id", searchId);
        params.put("currentPage", page);
        if (userId > 0) {
            params.put("uid", userId);
        }
        params.put("showCount", TOTAL_COUNT_TEN);
        params.put("replyCurrentPage", 1);
        params.put("replyShowCount", DEFAULT_COMMENT_TOTAL_COUNT);
        params.put("comtype", "doc");
        NetLoadUtils.getNetInstance().loadNetDataPost(this, Q_DML_SEARCH_COMMENT, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                adapterArticleComment.loadMoreComplete();
                if (page == 1) {
                    articleCommentList.clear();
                }

                DmlSearchCommentEntity dmlSearchCommentEntity = GsonUtils.fromJson(result, DmlSearchCommentEntity.class);
                if (dmlSearchCommentEntity != null) {
                    if (dmlSearchCommentEntity.getCode().equals(SUCCESS_CODE)) {
                        articleCommentList.addAll(dmlSearchCommentEntity.getDmlSearchCommentList());
                    } else if (dmlSearchCommentEntity.getCode().equals(EMPTY_CODE)) {
                        adapterArticleComment.loadMoreEnd();
                    } else {
                        showToast(dmlSearchCommentEntity.getMsg());
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
            public void onNotNetOrException() {
                adapterArticleComment.loadMoreEnd(true);
            }
        });
    }

    private void getSearchData() {
        Map<String, Object> params = new HashMap<>();
        params.put("id", searchId);
//            params.put("isV2", "true");
        /**
         * 3.1.8 加入并列商品 两排 三排
         */
        params.put("version", 1);
        if (userId > 0) {
            params.put("fuid", userId);
        }
        NetLoadUtils.getNetInstance().loadNetDataPost(DmlLifeSearchDetailActivity.this, F_INVITATION_DETAIL,
                params,
                new NetLoadListenerHelper() {
                    @Override
                    public void onSuccess(String result) {
                        smart_communal_refresh.finishRefresh();

                        dmlSearchDetailEntity = GsonUtils.fromJson(result, DmlSearchDetailEntity.class);
                        if (dmlSearchDetailEntity != null) {
                            if (dmlSearchDetailEntity.getCode().equals(SUCCESS_CODE)) {
                                descripDetailList.clear();
                                dmlSearchDetailBean = dmlSearchDetailEntity.getDmlSearchDetailBean();
                                setSearchData(dmlSearchDetailBean);
                            } else if (!dmlSearchDetailEntity.getCode().equals(EMPTY_CODE)) {
                                showToast(dmlSearchDetailEntity.getMsg());
                            }
                        }
                        NetLoadUtils.getNetInstance().showLoadSir(loadService, descripDetailList, dmlSearchDetailEntity);
                    }

                    @Override
                    public void onNotNetOrException() {
                        smart_communal_refresh.finishRefresh();
                        NetLoadUtils.getNetInstance().showLoadSir(loadService, descripDetailList, dmlSearchDetailEntity);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        showToast(R.string.invalidData);
                    }
                });
    }

    private void setSearchData(DmlSearchDetailBean dmlSearchDetailBean) {
        tv_header_titleAll.setText(getStrings(dmlSearchDetailBean.getTitle()));
        GlideImageLoaderUtil.loadCenterCrop(DmlLifeSearchDetailActivity.this, coverTitleView.iv_cover_detail_bg, dmlSearchDetailBean.getPath());
        coverTitleView.tv_descrip_title.setText(getStrings(dmlSearchDetailBean.getTitle()));
        rel_article_bottom.setVisibility(View.VISIBLE);
        tv_article_bottom_like.setSelected(dmlSearchDetailBean.isIsFavor());
        tv_article_bottom_like.setText((dmlSearchDetailBean.getFavor() > 0 ? dmlSearchDetailBean.getFavor() : "赞") + "");
        tv_article_bottom_collect.setSelected(dmlSearchDetailBean.isIsCollect());
        tv_article_bottom_collect.setText("收藏");
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
        List<CommunalDetailBean> descriptionList = dmlSearchDetailBean.getDescription();
        if (descriptionList != null) {
            descripDetailList.clear();
            descripDetailList.addAll(CommunalWebDetailUtils.getCommunalWebInstance().getWebDetailsFormatDataList(descriptionList));
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
        NetLoadUtils.getNetInstance().loadNetDataPost(this, SHARE_COMMUNAL_ARTICLE, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {

                CommunalOnlyDescription communalOnlyDescription = GsonUtils.fromJson(result, CommunalOnlyDescription.class);
                if (communalOnlyDescription != null) {
                    if (communalOnlyDescription.getCode().equals(SUCCESS_CODE)
                            && communalOnlyDescription.getComOnlyDesBean() != null) {
                        ComOnlyDesBean comOnlyDesBean = communalOnlyDescription.getComOnlyDesBean();
                        if (comOnlyDesBean.getDescriptionList() != null && comOnlyDesBean.getDescriptionList().size() > 0) {
                            CommunalDetailObjectBean communalDetailObjectBean = new CommunalDetailObjectBean();
                            communalDetailObjectBean.setItemType(CommunalDetailObjectBean.TYPE_SHARE);
                            descripDetailList.add(communalDetailObjectBean);
                            descripDetailList.addAll(CommunalWebDetailUtils.getCommunalWebInstance().getWebDetailsFormatDataList(comOnlyDesBean.getDescriptionList()));
                            communalDescripAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }
        });
    }

    private void setPrepareData(DmlSearchDetailBean dmlSearchDetailBean) {
        tv_communal_pro_tag.setVisibility(View.VISIBLE);
        ll_communal_pro_list.setVisibility(View.VISIBLE);
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        int radius = AutoSizeUtils.mm2px(mAppContext, 50);
        drawable.setCornerRadii(new float[]{radius, radius, 0, 0, 0, 0, radius, radius});
        try {
            drawable.setColor(0xffffffff);
            drawable.setStroke(1, 0xffcccccc);
        } catch (Exception e) {
            e.printStackTrace();
        }
        tv_communal_pro_tag.setBackground(drawable);
        tv_communal_pro_tag.setText(searchProductList.size() + "商品");
        tv_communal_pro_title.setText(getStrings(dmlSearchDetailBean.getTitle()));
    }

    private void setCommentLike(DmlSearchCommentBean dmlSearchCommentBean) {
        Map<String, Object> params = new HashMap<>();
        //用户id
        params.put("tuid", userId);
        //评论id
        params.put("id", dmlSearchCommentBean.getId());
        NetLoadUtils.getNetInstance().loadNetDataPost(this, FIND_AND_COMMENT_FAV, params, null);
    }

    private void setArticleLike() {
        Map<String, Object> params = new HashMap<>();
        //用户id
        params.put("tuid", userId);
        //关注id
        params.put("id", dmlSearchDetailBean.getId());
        params.put("favortype", "doc");
        NetLoadUtils.getNetInstance().loadNetDataPost(this, F_ARTICLE_DETAILS_FAVOR, params, null);
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
        Map<String, Object> params = new HashMap<>();
        //用户id
        params.put("uid", userId);
        //文章id
        params.put("object_id", dmlSearchDetailBean.getId());
        params.put("type", ConstantVariable.TYPE_C_SEARCH);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, F_ARTICLE_COLLECT, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                loadHud.dismiss();

                RequestStatus requestStatus = GsonUtils.fromJson(result, RequestStatus.class);
                if (requestStatus != null) {
                    if (requestStatus.getCode().equals(SUCCESS_CODE)) {
                        tv_article_bottom_collect.setSelected(!tv_article_bottom_collect.isSelected());
                        tv_article_bottom_collect.setText("收藏");
                    }
                }
            }

            @Override
            public void onNotNetOrException() {
                loadHud.dismiss();
            }

            @Override
            public void onError(Throwable throwable) {
                showToast(String.format(getResources().getString(R.string.collect_failed), "文章"));
            }
        });
    }

    //发送评论
    private void sendComment(CommunalComment communalComment) {
        loadHud.setCancellable(true);
        loadHud.show();
        tv_send_comment.setText("发送中…");
        tv_send_comment.setEnabled(false);
        CommentDao.setSendComment(this, communalComment, new CommentDao.OnSendCommentFinish() {
            @Override
            public void onSuccess() {
                loadHud.dismiss();
                tv_send_comment.setText("发送");
                tv_send_comment.setEnabled(true);
                commentViewVisible(View.GONE, null);
                showToast(R.string.comment_article_send_success);
                page = 1;
                getSearchComment();
                emoji_edit_comment.setText("");
            }

            @Override
            public void onError() {
                loadHud.dismiss();
                tv_send_comment.setText("发送");
                tv_send_comment.setEnabled(true);
            }
        });
    }

    private void setPublishComment() {
        if (View.VISIBLE == ll_input_comment.getVisibility()) {
            commentViewVisible(View.GONE, null);
        } else {
            commentViewVisible(View.VISIBLE, null);
        }
    }

    private void commentViewVisible(int visibility, final DmlSearchCommentBean dmlSearchCommentBean) {
        ll_input_comment.setVisibility(visibility);
        ll_article_comment.setVisibility(visibility == View.VISIBLE ? View.GONE : View.VISIBLE);
        if (View.VISIBLE == visibility) {
            emoji_edit_comment.requestFocus();
            //弹出键盘
            if (dmlSearchCommentBean != null) {
                emoji_edit_comment.setHint("回复" + dmlSearchCommentBean.getNickname() + ":");
            } else {
                emoji_edit_comment.setHint(R.string.comment_article_hint);
            }
            CommonUtils.showSoftInput(emoji_edit_comment.getContext(), emoji_edit_comment);
            tv_send_comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //判断有内容调用接口
                    String comment = emoji_edit_comment.getText().toString();
                    if (!TextUtils.isEmpty(comment)) {
                        comment = emoji_edit_comment.getText().toString();
                        CommunalComment communalComment = new CommunalComment();
                        communalComment.setCommType("doc");
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
                        showToast("请正确输入内容");
                    }
                }
            });
        } else if (View.GONE == visibility) {
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
                    commentViewVisible(View.GONE, null);
                }
                break;
            case MotionEvent.ACTION_UP:
                locationY = 0;
                break;
        }
        return false;
    }

    //    页面分享
    @OnClick(R.id.iv_img_share)
    void sendShare() {
        setShareData();
    }

    private void setShareData() {
        if (dmlSearchDetailBean != null) {
            new UMShareAction(DmlLifeSearchDetailActivity.this
                    , dmlSearchDetailBean.getPath()
                    , getStrings(dmlSearchDetailBean.getTitle())
                    , getStrings(dmlSearchDetailBean.getDigest())
                    , Url.BASE_SHARE_PAGE_TWO + "m/template/goods/study_detail.html?id="
                    + dmlSearchDetailBean.getId(), dmlSearchDetailBean.getId());
        }
    }

    @OnClick(R.id.tv_life_back)
    void goBack() {
        finish();
    }

    @OnClick(R.id.iv_img_service)
    void skipShopCar() {
        Intent intent = new Intent(DmlLifeSearchDetailActivity.this, ShopCarActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.tv_publish_comment)
    void publishComment() {
        if (dmlSearchDetailBean != null) {
            if (userId > 0) {
                setPublishComment();
            } else {
                getLoginStatus(DmlLifeSearchDetailActivity.this);
            }
        }
    }

    //文章点赞
    @OnClick(R.id.tv_article_bottom_like)
    void likeArticle(View view) {
        if (dmlSearchDetailBean != null) {
            if (userId > 0) {
                setArticleLike();
            } else {
                getLoginStatus(DmlLifeSearchDetailActivity.this);
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
                getLoginStatus(DmlLifeSearchDetailActivity.this);
            }
        }
    }

    @OnClick(R.id.tv_communal_pro_tag)
    void openSlideProList(View view) {
//            商品列表
        if (dl_ql_search.isDrawerOpen(ll_communal_pro_list)) {
            dl_ql_search.closeDrawers();
        } else {
            dl_ql_search.openDrawer(ll_communal_pro_list);
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
            if (View.VISIBLE == ll_input_comment.getVisibility()) {
                commentViewVisible(View.GONE, dmlSearchCommentBean);
            } else {
                commentViewVisible(View.VISIBLE, dmlSearchCommentBean);
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
        } else if (message.type.equals(ConstantVariable.UPDATE_CAR_NUM)) {
            if (badge != null) {
                badge.setBadgeNumber((int) message.result);
            }
        }
    }

    class CoverTitleView {
        @BindView(R.id.tv_descrip_title)
        TextView tv_descrip_title;
        @BindView(R.id.iv_cover_detail_bg)
        ImageView iv_cover_detail_bg;
        @BindView(R.id.communal_recycler_wrap)
        RecyclerView communal_recycler_wrap;

        public void initView() {
            communal_recycler_wrap.setNestedScrollingEnabled(false);
            communalDescripAdapter = new CommunalDetailAdapter(DmlLifeSearchDetailActivity.this, descripDetailList);
            communalDescripAdapter.setEnableLoadMore(false);
            communal_recycler_wrap.setLayoutManager(new LinearLayoutManager(DmlLifeSearchDetailActivity.this));
            communal_recycler_wrap.setAdapter(communalDescripAdapter);
        }
    }

    public class CommentCountView {
        @BindView(R.id.tv_comm_comment_count)
        TextView tv_comm_comment_count;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                InputMethodManager imm =
                        (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        }
        try {
            return super.dispatchTouchEvent(ev);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // Return whether touch the view.
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v instanceof EditText) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            return !(event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom);
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        KeyboardUtils.unregisterSoftInputChangedListener(this);
    }
}
