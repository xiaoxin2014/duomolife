package com.amkj.dmsh.dominant.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.text.emoji.widget.EmojiEditText;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.constant.CommunalComment;
import com.amkj.dmsh.constant.CommunalDetailBean;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.ConstantVariable;
import com.amkj.dmsh.constant.UMShareAction;
import com.amkj.dmsh.constant.Url;
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
import com.amkj.dmsh.mine.activity.ShopCarActivity;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.shopdetails.bean.CommunalDetailObjectBean;
import com.amkj.dmsh.user.activity.UserPagerActivity;
import com.amkj.dmsh.utils.CommonUtils;
import com.amkj.dmsh.utils.KeyboardUtils;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;
import com.amkj.dmsh.utils.webformatdata.CommunalWebDetailUtils;
import com.amkj.dmsh.utils.webformatdata.ShareDataBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.umeng.socialize.UMShareAPI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;
import me.jessyan.autosize.utils.AutoSizeUtils;
import q.rorbin.badgeview.Badge;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.amkj.dmsh.R.id.ll_communal_pro_list;
import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.getBadge;
import static com.amkj.dmsh.constant.ConstantMethod.getCarCount;
import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;

import static com.amkj.dmsh.constant.ConstantMethod.saveSourceId;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.skipProductUrl;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.COMMENT_TOPIC_TYPE;
import static com.amkj.dmsh.constant.ConstantVariable.DEFAULT_COMMENT_TOTAL_COUNT;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.IS_LOGIN_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.TOTAL_COUNT_TEN;
import static com.amkj.dmsh.constant.Url.FIND_AND_COMMENT_FAV;
import static com.amkj.dmsh.constant.Url.H_DML_THEME_DETAIL;
import static com.amkj.dmsh.constant.Url.Q_DML_SEARCH_COMMENT;
import static com.amkj.dmsh.constant.Url.SHARE_COMMUNAL_ARTICLE;
import static com.amkj.dmsh.dao.AddClickDao.totalWelfareProNum;
import static com.amkj.dmsh.utils.CommunalCopyTextUtils.showPopWindow;

public class DoMoLifeWelfareDetailsActivity extends BaseActivity {
    @BindView(R.id.smart_communal_refresh)
    SmartRefreshLayout smart_communal_refresh;
    @BindView(R.id.communal_recycler)
    RecyclerView communal_recycler;
    @BindView(R.id.tv_header_title)
    TextView tv_header_titleAll;
    @BindView(R.id.iv_img_service)
    ImageView iv_img_service;
    @BindView(R.id.fl_header_service)
    FrameLayout fl_header_service;
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
    @BindView(R.id.scrollview)
    ScrollView mScrollview;


    private List<CommunalDetailObjectBean> itemDescriptionList = new ArrayList();
    //侧滑商品列表
    private List<ProductListBean> welfareProductList = new ArrayList();
    //    评论列表
    private List<DmlSearchCommentBean> articleCommentList = new ArrayList<>();
    private String welfareId;
    private int page = 1;
    private WelfareHeaderView welfareHeaderView;
    private CommunalDetailAdapter communalWelfareDetailAdapter;
    private Badge badge;
    private WelfareSlideProAdapter welfareSlideProAdapter;
    private QualityWefBean qualityWefBean;
    private ArticleCommentAdapter adapterTopicComment;
    private View commentHeaderView;
    private float locationY;
    private CommentCountView commentCountView;
    private DmlSearchCommentEntity dmlSearchCommentEntity;
    private QualityWefEntity qualityWefEntity;

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
        //记录埋点参数sourceId(福利社专题对应的ID)
        ConstantMethod.saveSourceId(getClass().getSimpleName(), String.valueOf(welfareId));
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
        communal_recycler.addItemDecoration(new ItemDecoration.Builder()
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_gray_f_two_px)
                .create());
        adapterTopicComment.setOnItemChildClickListener((adapter, view, position) -> {
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
                            adapterTopicComment.notifyItemChanged(position + adapterTopicComment.getHeaderLayoutCount());
                            setCommentLike(dmlSearchCommentBean);
                        } else {
                            getLoginStatus(DoMoLifeWelfareDetailsActivity.this);
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
                            getLoginStatus(DoMoLifeWelfareDetailsActivity.this);
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
            page++;
            getTopicComment();
        }, communal_recycler);
//        侧滑菜单
        rv_wel_details_pro.setLayoutManager(new LinearLayoutManager(DoMoLifeWelfareDetailsActivity.this));
        rv_wel_details_pro.addItemDecoration(new ItemDecoration.Builder()
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_gray_f_two_px)


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
                    saveSourceId(getClass().getSimpleName(), welfareId);             //记录埋点参数sourceId
                    skipProductUrl(DoMoLifeWelfareDetailsActivity.this, productListBean.getItemTypeId(), productListBean.getId());
                    //                    统计商品点击
                    totalWelfareProNum(getActivity(), productListBean.getId(), Integer.parseInt(welfareId));
                }
            }
        });
        smart_communal_refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                loadData();
            }
        });
        //          关闭手势滑动
        dr_welfare_detail_pro.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.END);
        badge = getBadge(DoMoLifeWelfareDetailsActivity.this, fl_header_service);
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
    protected void loadData() {
        //头部信息
        page = 1;
        getThemeDetailsData();
        getCarCount(getActivity());
        getTopicComment();
    }

    @Override
    public View getLoadView() {
        return smart_communal_refresh;
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    private void getThemeDetailsData() {
        Map<String, Object> params = new HashMap<>();
        params.put("id", welfareId);
        params.put("is_up", 1);
        /**
         * 3.1.7.5 加入 区分3,2排并列商品
         */
        params.put("version", 1);
        if (userId > 0) {
            params.put("uid", userId);
        }
        NetLoadUtils.getNetInstance().loadNetDataPost(DoMoLifeWelfareDetailsActivity.this, H_DML_THEME_DETAIL
                , params, new NetLoadListenerHelper() {
                    @Override
                    public void onSuccess(String result) {
                        smart_communal_refresh.finishRefresh();
                        Gson gson = new Gson();
                        qualityWefEntity = gson.fromJson(result, QualityWefEntity.class);
                        if (qualityWefEntity != null) {
                            if (qualityWefEntity.getCode().equals(SUCCESS_CODE)) {
                                qualityWefBean = qualityWefEntity.getQualityWefBean();
                                setData(qualityWefBean);
                            } else if (!qualityWefEntity.getCode().equals(EMPTY_CODE)) {
                                showToast(DoMoLifeWelfareDetailsActivity.this, qualityWefEntity.getMsg());
                            }
                        }
                        rel_topic_bottom.setVisibility(VISIBLE);
                        NetLoadUtils.getNetInstance().showLoadSir(loadService, qualityWefBean, qualityWefEntity);
                    }

                    @Override
                    public void onNotNetOrException() {
                        rel_topic_bottom.setVisibility(VISIBLE);
                        smart_communal_refresh.finishRefresh();
                        NetLoadUtils.getNetInstance().showLoadSir(loadService, qualityWefBean, qualityWefEntity);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        showToast(DoMoLifeWelfareDetailsActivity.this, R.string.invalidData);
                    }
                });
    }


    private void setData(QualityWefBean qualityWefBean) {
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
            itemDescriptionList.addAll(CommunalWebDetailUtils.getCommunalWebInstance().getWebDetailsFormatDataList(descriptionList));
            getShareData();
            communalWelfareDetailAdapter.setNewData(itemDescriptionList);
        }
    }

    private void getShareData() {
        NetLoadUtils.getNetInstance().loadNetDataPost(this, SHARE_COMMUNAL_ARTICLE, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                CommunalOnlyDescription communalOnlyDescription = gson.fromJson(result, CommunalOnlyDescription.class);
                if (communalOnlyDescription != null) {
                    if (communalOnlyDescription.getCode().equals(SUCCESS_CODE)
                            && communalOnlyDescription.getComOnlyDesBean() != null) {
                        ComOnlyDesBean comOnlyDesBean = communalOnlyDescription.getComOnlyDesBean();
                        if (comOnlyDesBean.getDescriptionList() != null && comOnlyDesBean.getDescriptionList().size() > 0) {
                            CommunalDetailObjectBean communalDetailObjectBean = new CommunalDetailObjectBean();
                            communalDetailObjectBean.setItemType(CommunalDetailObjectBean.TYPE_SHARE);
                            itemDescriptionList.add(communalDetailObjectBean);
                            itemDescriptionList.addAll(CommunalWebDetailUtils.getCommunalWebInstance().getWebDetailsFormatDataList(comOnlyDesBean.getDescriptionList()));
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
        int radius = AutoSizeUtils.mm2px(mAppContext, 50);
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

    private void getTopicComment() {
        Map<String, Object> params = new HashMap<>();
        params.put("id", welfareId);
        params.put("currentPage", page);
        if (userId > 0) {
            params.put("uid", userId);
        }
        params.put("showCount", TOTAL_COUNT_TEN);
        params.put("replyCurrentPage", 1);
        params.put("replyShowCount", DEFAULT_COMMENT_TOTAL_COUNT);
        params.put("comtype", "topic");
        NetLoadUtils.getNetInstance().loadNetDataPost(this, Q_DML_SEARCH_COMMENT, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                adapterTopicComment.loadMoreComplete();
                if (page == 1) {
                    articleCommentList.clear();
                }
                Gson gson = new Gson();
                dmlSearchCommentEntity = gson.fromJson(result, DmlSearchCommentEntity.class);
                if (dmlSearchCommentEntity != null) {
                    if (dmlSearchCommentEntity.getCode().equals(SUCCESS_CODE)) {
                        articleCommentList.addAll(dmlSearchCommentEntity.getDmlSearchCommentList());
                    } else if (dmlSearchCommentEntity.getCode().equals(EMPTY_CODE)) {
                        adapterTopicComment.loadMoreEnd();
                    } else {
                        showToast(DoMoLifeWelfareDetailsActivity.this, dmlSearchCommentEntity.getMsg());
                    }
                    adapterTopicComment.removeHeaderView(commentHeaderView);
                    if (articleCommentList.size() > 0) {
                        adapterTopicComment.addHeaderView(commentHeaderView);
                        commentCountView.tv_comm_comment_count.setText(String.format(getString(R.string.comment_handpick_count), dmlSearchCommentEntity.getCommentSize()));
                    }
                    adapterTopicComment.notifyDataSetChanged();
                }
            }

            @Override
            public void onNotNetOrException() {
                adapterTopicComment.loadMoreEnd(true);
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

    //    页面分享
    @OnClick(R.id.iv_img_share)
    void sendShare() {
        setShareData();
    }

    private void setShareData() {
        if (qualityWefBean != null) {
            UMShareAction umShareAction = new UMShareAction(DoMoLifeWelfareDetailsActivity.this
                    , ""
                    , qualityWefBean.getTitle()
                    , ""
                    , Url.BASE_SHARE_PAGE_TWO + ("m/template/common/topic.html" + "?id="
                    + qualityWefBean.getId() + (userId > 0 ? "&sid=" + userId : "")), "pages/topic/topic?id=" + qualityWefBean.getId(), qualityWefBean.getId());
        }
    }

    @OnClick(R.id.tv_life_back)
    void goBack() {
        finish();
    }

    @OnClick(R.id.iv_img_service)
    void skipShopCar() {
        Intent intent = new Intent(DoMoLifeWelfareDetailsActivity.this, ShopCarActivity.class);
        startActivity(intent);
    }

    private void setCommentLike(DmlSearchCommentBean dmlSearchCommentBean) {
        Map<String, Object> params = new HashMap<>();
        //用户id
        params.put("tuid", userId);
        //评论id
        params.put("id", dmlSearchCommentBean.getId());
        NetLoadUtils.getNetInstance().loadNetDataPost(this, FIND_AND_COMMENT_FAV, params, null);
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
                    communalComment.setUserId(userId);
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
        } else if (message.type.equals(ConstantVariable.UPDATE_CAR_NUM)) {
            if (badge!=null){
                badge.setBadgeNumber((int) message.result);
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
            communalWelfareDetailAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                @Override
                public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                    ShareDataBean shareDataBean = null;
                    if (view.getId() == R.id.tv_communal_share && qualityWefBean != null) {
                        shareDataBean = new ShareDataBean(qualityWefBean.getPicUrl()
                                , qualityWefBean.getTitle()
                                , ""
                                , Url.BASE_SHARE_PAGE_TWO + ("m/template/common/topic.html" + "?id="
                                + qualityWefBean.getId() + (userId > 0 ? "&sid=" + userId : "")), qualityWefBean.getId());

                    }
                    CommunalWebDetailUtils.getCommunalWebInstance()
                            .setWebDataClick(DoMoLifeWelfareDetailsActivity.this, shareDataBean, view, loadHud);
                }
            });
            communalWelfareDetailAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    CommunalDetailObjectBean communalDetailBean = (CommunalDetailObjectBean) view.getTag();
                    if (communalDetailBean != null) {
                        skipProductUrl(DoMoLifeWelfareDetailsActivity.this, communalDetailBean.getItemTypeId(), communalDetailBean.getId());
                        //                    统计商品点击
                        totalWelfareProNum(getActivity(), communalDetailBean.getId(), Integer.parseInt(welfareId));
                    }
                }
            });
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
        if (qualityWefBean != null) {
            if (userId > 0) {
                setPublishComment();
            } else {
                getLoginStatus(DoMoLifeWelfareDetailsActivity.this);
            }
        }
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
        if (v != null && (v instanceof EditText)) {
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

    @Override
    public View getTopView() {
        return mScrollview;
    }

}
