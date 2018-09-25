package com.amkj.dmsh.find.activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
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
import com.amkj.dmsh.base.TinkerBaseApplicationLike;
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
import com.amkj.dmsh.dominant.bean.DmlSearchCommentEntity;
import com.amkj.dmsh.dominant.bean.DmlSearchCommentEntity.DmlSearchCommentBean;
import com.amkj.dmsh.dominant.bean.DmlSearchCommentEntity.DmlSearchCommentBean.ReplyCommListBean;
import com.amkj.dmsh.dominant.bean.DmlSearchDetailEntity;
import com.amkj.dmsh.dominant.bean.DmlSearchDetailEntity.DmlSearchDetailBean;
import com.amkj.dmsh.homepage.adapter.CommunalDetailAdapter;
import com.amkj.dmsh.shopdetails.activity.ShopScrollDetailsActivity;
import com.amkj.dmsh.shopdetails.bean.CommunalDetailObjectBean;
import com.amkj.dmsh.user.activity.UserPagerActivity;
import com.amkj.dmsh.utils.CommonUtils;
import com.amkj.dmsh.utils.CommunalCopyTextUtils;
import com.amkj.dmsh.utils.Log;
import com.amkj.dmsh.utils.NetWorkUtils;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.amkj.dmsh.utils.inteface.MyCallBack;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.melnykov.fab.FloatingActionButton;
import com.oushangfeng.pinnedsectionitemdecoration.PinnedHeaderItemDecoration;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.tencent.bugly.beta.tinker.TinkerManager;
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

import static android.view.View.GONE;
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
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.TAOBAO_APPKEY;
import static com.amkj.dmsh.shopdetails.bean.CommunalDetailObjectBean.TYPE_COUPON;
import static com.amkj.dmsh.shopdetails.bean.CommunalDetailObjectBean.TYPE_COUPON_PACKAGE;
import static com.amkj.dmsh.shopdetails.bean.CommunalDetailObjectBean.TYPE_PRODUCT_RECOMMEND;
import static com.amkj.dmsh.shopdetails.bean.CommunalDetailObjectBean.TYPE_PRODUCT_TAG;
import static com.amkj.dmsh.shopdetails.bean.CommunalDetailObjectBean.TYPE_RELEVANCE_PRODUCT;

;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/05/23
 * class description:发现-文章帖子
 */
public class ArticleInvitationDetailsActivity extends BaseActivity {
    @BindView(R.id.smart_communal_refresh)
    SmartRefreshLayout smart_communal_refresh;
    @BindView(R.id.communal_recycler)
    RecyclerView communal_recycler;
    //    滚动至顶部
    @BindView(R.id.download_btn_communal)
    public FloatingActionButton download_btn_communal;

    @BindView(R.id.tv_header_title)
    TextView tv_header_titleAll;
    //    底栏
    @BindView(R.id.rel_article_img_bottom)
    RelativeLayout rel_article_img_bottom;
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
    private ArticleCommentAdapter adapterArticleComment;
    //    评论数
    private List<DmlSearchCommentBean> articleCommentList = new ArrayList<>();
//    详情描述
    private List<CommunalDetailObjectBean> descriptionDetailList = new ArrayList<>();
    private String artId;
    private int page = 1;
    private ImgDetailsHeaderView imgHeaderView;
    private DmlSearchDetailEntity dmlSearchDetailEntity;
    private int scrollY = 0;
    private float screenHeight;
    //    按下点击位置
    private float locationY;
    private DmlSearchDetailBean dmlSearchDetailBean;
    private CommunalDetailAdapter communalDetailAdapter;

    @Override
    protected int getContentView() {
        return R.layout.activity_find_article_details;
    }

    @Override
    protected void initViews() {
        Intent intent = getIntent();
        artId = intent.getStringExtra("ArtId");
        View headerView = LayoutInflater.from(ArticleInvitationDetailsActivity.this).inflate(R.layout.layout_find_article_invitation_details, (ViewGroup) communal_recycler.getParent(), false);
        imgHeaderView = new ImgDetailsHeaderView();
        ButterKnife.bind(imgHeaderView, headerView);
        imgHeaderView.initView();
        adapterArticleComment = new ArticleCommentAdapter(ArticleInvitationDetailsActivity.this, articleCommentList,COMMENT_TYPE);
        adapterArticleComment.addHeaderView(headerView);
        communal_recycler.setLayoutManager(new LinearLayoutManager(this));
        communal_recycler.addItemDecoration(new PinnedHeaderItemDecoration.Builder(-1)
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_five_dp)
                // 开启绘制分隔线，默认关闭
                .enableDivider(true)
                // 是否关闭标签点击事件，默认开启
                .disableHeaderClick(false)
                // 设置标签和其内部的子控件的监听，若设置点击监听不为null，但是disableHeaderClick(true)的话，还是不会响应点击事件
                .setHeaderClickListener(null)
                .create());
        communal_recycler.setAdapter(adapterArticleComment);

        smart_communal_refresh.setOnRefreshListener((refreshLayout) -> {
                //                滚动距离置0
                scrollY = 0;
                loadData();
        });
        adapterArticleComment.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (page * DEFAULT_TOTAL_COUNT <= adapterArticleComment.getItemCount()) {
                    page++;
                    getArticleImgComment();
                } else {
                    adapterArticleComment.loadMoreEnd();
                }
            }
        }, communal_recycler);

        download_btn_communal.attachToRecyclerView(communal_recycler, null, new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (!recyclerView.canScrollVertically(-1)) {
                    scrollY = 0;
                }
                scrollY += dy;
                if (screenHeight == 0) {
                    TinkerBaseApplicationLike app = (TinkerBaseApplicationLike) TinkerManager.getTinkerApplicationLike();
                    screenHeight = app.getScreenHeight();
                }
                if (scrollY >= screenHeight) {
                    download_btn_communal.setVisibility(View.VISIBLE);
                } else {
                    download_btn_communal.setVisibility(GONE);
                }
            }
        });
        download_btn_communal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) communal_recycler.getLayoutManager();
                int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
                int mVisibleCount = linearLayoutManager.findLastVisibleItemPosition()
                        - linearLayoutManager.findFirstVisibleItemPosition() + 1;
                if (firstVisibleItemPosition > mVisibleCount) {
                    communal_recycler.scrollToPosition(mVisibleCount);
                }
                communal_recycler.smoothScrollToPosition(0);
            }
        });
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
                                getLoginStatus(ArticleInvitationDetailsActivity.this);
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
                                getLoginStatus(ArticleInvitationDetailsActivity.this);
                            }
                            break;
                        case R.id.civ_comm_comment_avatar:
                            Intent intent = new Intent();
                            intent.setClass(ArticleInvitationDetailsActivity.this, UserPagerActivity.class);
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
                    CommunalCopyTextUtils.showPopWindow(ArticleInvitationDetailsActivity.this, (TextView) view, dmlSearchCommentBean.getContent());
                }
                return false;
            }
        });
        tv_publish_comment.setText(R.string.comment_hint_invitation);
        totalPersonalTrajectory = insertNewTotalData(this,artId);
    }

    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getRawX() > left && event.getRawX() < right && event.getRawY() > top && event.getRawY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
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
            if (View.VISIBLE == ll_input_comment.getVisibility()) {
                commentViewVisible(GONE, dmlSearchCommentBean);
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
        }
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
                showToast(ArticleInvitationDetailsActivity.this, String.format(getResources().getString(R.string.collect_failed), "文章"));
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
                showToast(ArticleInvitationDetailsActivity.this,R.string.comment_send_success);
                commentViewVisible(GONE, null);
                page = 1;
                getArticleImgComment();
                emoji_edit_comment.setText("");
            }

            @Override
            public void onError() {
                loadHud.dismiss();
                tv_send_comment.setText("发送");
                tv_send_comment.setEnabled(true);
            }
        });
        constantMethod.setSendComment(ArticleInvitationDetailsActivity.this, communalComment);
    }

    private void setPublishComment() {
            if (View.VISIBLE == ll_input_comment.getVisibility()) {
                commentViewVisible(GONE, null);
            } else {
                commentViewVisible(View.VISIBLE, null);
        }
    }

    private void commentViewVisible(int visibility, final DmlSearchCommentBean dmlSearchCommentBean) {
        ll_input_comment.setVisibility(visibility);
        ll_article_comment.setVisibility(visibility == View.VISIBLE ? GONE : View.VISIBLE);
        if (View.VISIBLE == visibility) {
            emoji_edit_comment.requestFocus();
            //弹出键盘
            if (dmlSearchCommentBean != null) {
                emoji_edit_comment.setHint("回复" + dmlSearchCommentBean.getNickname() + ":");
            } else {
                emoji_edit_comment.setHint("");
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
                        showToast(ArticleInvitationDetailsActivity.this, "请正确输入内容");
                    }
                }
            });
        } else if (GONE == visibility) {
            //隐藏键盘
            CommonUtils.hideSoftInput(emoji_edit_comment.getContext(), emoji_edit_comment);
        }
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
        getDetailData();
    }

    private void getDetailData() {
        getArticleImgComment();
        imgHeaderView.getDetailsData();
    }

    private void getArticleImgComment() {
        if (NetWorkUtils.checkNet(ArticleInvitationDetailsActivity.this)) {
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
                    smart_communal_refresh.finishRefresh();
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
                            showToast(ArticleInvitationDetailsActivity.this, dmlSearchCommentEntity.getMsg());
                        }
                        if (articleCommentList.size() > 0) {
                            imgHeaderView.rel_img_art_com.setVisibility(View.VISIBLE);
                            imgHeaderView.tv_comm_comment_count.setText(String.format(getString(R.string.comment_count),dmlSearchCommentEntity.getCommentSize()));
                        } else {
                            imgHeaderView.rel_img_art_com.setVisibility(GONE);
                        }
                        if (page == 1) {
                            adapterArticleComment.setNewData(articleCommentList);
                        } else {
                            adapterArticleComment.notifyDataSetChanged();
                        }
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    adapterArticleComment.loadMoreComplete();
                    showToast(ArticleInvitationDetailsActivity.this, R.string.invalidData);
                    super.onError(ex, isOnCallback);
                }
            });
        }
    }

    class ImgDetailsHeaderView {
        @BindView(R.id.tv_inv_user_name)
        TextView tv_inv_user_name;
        @BindView(R.id.tv_recommend_invitation_time)
        TextView tv_recommend_invitation_time;
        @BindView(R.id.tv_inv_live_att)
        TextView tv_live_attention;
        //头像
        @BindView(R.id.iv_inv_user_avatar)
        ImageView img_live_avatar;
        //        评论数
        @BindView(R.id.rel_img_art_com)
        RelativeLayout rel_img_art_com;
        @BindView(R.id.tv_comm_comment_count)
        TextView tv_comm_comment_count;

        @BindView(R.id.communal_recycler_wrap)
        RecyclerView communal_recycler_wrap;
        //是否关注
        boolean isAttention = false;
        boolean articleFavor = false;

        public void initView() {
            communal_recycler_wrap.setNestedScrollingEnabled(false);
            communal_recycler_wrap.setLayoutManager(new LinearLayoutManager(ArticleInvitationDetailsActivity.this));
            communalDetailAdapter = new CommunalDetailAdapter(ArticleInvitationDetailsActivity.this,descriptionDetailList);
            communal_recycler_wrap.setAdapter(communalDetailAdapter);
            communal_recycler_wrap.setNestedScrollingEnabled(false);
            communalDetailAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    CommunalDetailObjectBean communalDetailBean = (CommunalDetailObjectBean) view.getTag();
                    if (communalDetailBean != null) {
                        skipProductUrl(ArticleInvitationDetailsActivity.this, communalDetailBean.getItemTypeId(), communalDetailBean.getId());
//                    统计商品点击
                        totalProNum(communalDetailBean.getId(), dmlSearchDetailBean.getId());
                    }
                }
            });
            communalDetailAdapter.setOnItemChildClickListener((adapter, view, position) -> {
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
                                getLoginStatus(ArticleInvitationDetailsActivity.this);
                            }
                        }
                        break;
                    case R.id.iv_communal_cover_wrap:
                        CommunalDetailObjectBean detailObjectBean = (CommunalDetailObjectBean) view.getTag(R.id.iv_tag);
                        if (detailObjectBean != null) {
                            if (detailObjectBean.getItemType() == CommunalDetailObjectBean.TYPE_GOODS_IMG) {
                                Intent newIntent = new Intent(ArticleInvitationDetailsActivity.this, ShopScrollDetailsActivity.class);
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
                                getLoginStatus(ArticleInvitationDetailsActivity.this);
                            }
                        }
                        break;
                    case R.id.iv_ql_bl_add_car:
                        CommunalDetailObjectBean qualityWelPro = (CommunalDetailObjectBean) view.getTag();
                        if (qualityWelPro != null) {
                            if (ConstantMethod.userId > 0) {
                                BaseAddCarProInfoBean baseAddCarProInfoBean = new BaseAddCarProInfoBean();
                                baseAddCarProInfoBean.setProductId(qualityWelPro.getId());
                                baseAddCarProInfoBean.setProName(getStrings(qualityWelPro.getName()));
                                baseAddCarProInfoBean.setProPic(getStrings(qualityWelPro.getPicUrl()));
                                ConstantMethod constantMethod = new ConstantMethod();
                                constantMethod.addShopCarGetSku(ArticleInvitationDetailsActivity.this, baseAddCarProInfoBean, loadHud);
                            } else {
                                loadHud.dismiss();
                                getLoginStatus(ArticleInvitationDetailsActivity.this);
                            }
                        }
                        break;
                    case R.id.tv_communal_tb_link:
                    case R.id.iv_communal_tb_cover:
                        if (loadHud != null) {
                            loadHud.show();
                        }
                        CommunalDetailObjectBean tbLink = (CommunalDetailObjectBean) view.getTag(R.id.iv_tag);
                        if(tbLink == null){
                            tbLink = (CommunalDetailObjectBean) view.getTag();
                        }
                        if(tbLink!=null){
                            skipAliBCWebView(tbLink.getUrl());
                        }
                        break;
                    default:
                        loadHud.dismiss();
                        break;
                }
            });
            tv_live_attention.setVisibility(View.VISIBLE);
        }

        private void setDetailData(final DmlSearchDetailBean detailsBean) {
            GlideImageLoaderUtil.loadHeaderImg(ArticleInvitationDetailsActivity.this, img_live_avatar, detailsBean.getAvatar());
            img_live_avatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ArticleInvitationDetailsActivity.this, UserPagerActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("userId", String.valueOf(detailsBean.getUid()));
                    startActivity(intent);
                }
            });
            tv_inv_user_name.setText(detailsBean.getNickname());
            tv_recommend_invitation_time.setText(detailsBean.getCtime());
            if (detailsBean.isFlag()) {
                tv_live_attention.setSelected(true);
                tv_live_attention.setText("已关注");
                isAttention = true;
            } else {
                tv_live_attention.setSelected(false);
                tv_live_attention.setText("关 注");
                isAttention = false;
            }
            tv_header_titleAll.setText(getStrings(detailsBean.getDigest()));
            tv_article_bottom_like.setSelected(detailsBean.isIsFavor());
            articleFavor = detailsBean.isIsFavor();
            tv_article_bottom_like.setText(String.valueOf(detailsBean.getFavor()));
            tv_article_bottom_collect.setSelected(detailsBean.isIsCollect());
            List<CommunalDetailBean> descriptionList = detailsBean.getDescription();
            if (descriptionList != null) {
                descriptionDetailList.clear();
                if (detailsBean.getTags() != null
                        && detailsBean.getTags().size() > 0) {
                    CommunalDetailObjectBean communalDetailBean = new CommunalDetailObjectBean();
                    communalDetailBean.setItemType(TYPE_PRODUCT_TAG);
                    communalDetailBean.setTagsBeans(detailsBean.getTags());
                    descriptionDetailList.add(communalDetailBean);
                }
                descriptionDetailList.addAll(getDetailsDataList(descriptionList));
                if (detailsBean.getRelevanceProList() != null
                        && detailsBean.getRelevanceProList().size() > 0) {
                    CommunalDetailObjectBean communalDetailBean = new CommunalDetailObjectBean();
                    communalDetailBean.setItemType(TYPE_RELEVANCE_PRODUCT);
                    communalDetailBean.setRelevanceProBeanList(detailsBean.getRelevanceProList());
                    descriptionDetailList.add(communalDetailBean);
                }
                if(detailsBean.getDocumentProductList()!=null
                        &&detailsBean.getDocumentProductList().size()>0){
                    CommunalDetailObjectBean communalDetailBean = new CommunalDetailObjectBean();
                    communalDetailBean.setItemType(TYPE_PRODUCT_RECOMMEND);
                    communalDetailBean.setProductList(detailsBean.getDocumentProductList());
                    descriptionDetailList.add(communalDetailBean);
                }
                communalDetailAdapter.notifyDataSetChanged();
            }
        }

        @OnClick(R.id.tv_inv_live_att)
        void setAttentionTag(View view) {
            if (dmlSearchDetailBean != null) {
                if (userId > 0) {
                    setAttentionFlag(dmlSearchDetailBean, view);
                } else {
                    getLoginStatus(ArticleInvitationDetailsActivity.this);
                }
            }
        }

        public void setAttentionFlag(final DmlSearchDetailBean imgDetailBean, final View v) {
            final TextView textView = (TextView) v;
            isAttention = textView.isSelected();
            String url = Url.BASE_URL + Url.UPDATE_ATTENTION;
            Map<String, Object> params = new HashMap<>();
            //用户id
            params.put("fuid", userId);
            //关注id
            params.put("buid", imgDetailBean.getUid());
            String flag;
            if (isAttention) {
                flag = "cancel";
            } else {
                flag = "add";
            }
            params.put("ftype", flag);
            XUtil.Post(url, params, new MyCallBack<String>() {
                @Override
                public void onSuccess(String result) {
                    Gson gson = new Gson();
                    RequestStatus requestStatus = gson.fromJson(result, RequestStatus.class);
                    if (requestStatus != null) {
                        if (requestStatus.getCode().equals("01")) {
                            if (!isAttention) {
                                textView.setSelected(true);
                                textView.setText("已关注");
                                showToast(ArticleInvitationDetailsActivity.this, "已关注");
                                isAttention = true;
                            } else {
                                textView.setSelected(false);
                                textView.setText("+ 关注");
                                showToast(ArticleInvitationDetailsActivity.this, "已取消关注");
                                isAttention = false;
                            }
                            getDetailsData();
                        } else {
                            showToast(ArticleInvitationDetailsActivity.this, requestStatus.getMsg());
                        }
                        textView.setEnabled(true);
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    textView.setEnabled(true);
                    super.onError(ex, isOnCallback);
                }
            });
        }

        public void getDetailsData() {
            String url = Url.BASE_URL + Url.F_INVITATION_DETAIL;
            Map<String, String> params = new HashMap<>();
            params.put("id", artId);
//            帖子改版 3.0.9
            params.put("isV2", "true");
            if (userId > 0) {
                params.put("fuid", String.valueOf(userId));
            }
            NetLoadUtils.getQyInstance().loadNetDataGetCache(ArticleInvitationDetailsActivity.this, url
                    , params
                    , new NetLoadUtils.NetLoadListener() {
                        @Override
                        public void onSuccess(String result) {
                            smart_communal_refresh.finishRefresh();
                            adapterArticleComment.loadMoreComplete();
                            getDetailsDataJson(result);
                        }

                        @Override
                        public void netClose() {
                            smart_communal_refresh.finishRefresh();
                            adapterArticleComment.loadMoreComplete();
                            rel_article_img_bottom.setVisibility(GONE);
                            NetLoadUtils.getQyInstance().showLoadSir(loadService,dmlSearchDetailBean,dmlSearchDetailEntity);
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            smart_communal_refresh.finishRefresh();
                            adapterArticleComment.loadMoreComplete();
                            rel_article_img_bottom.setVisibility(GONE);
                            NetLoadUtils.getQyInstance().showLoadSir(loadService,dmlSearchDetailBean,dmlSearchDetailEntity);
                        }
                    });
        }

        private void getDetailsDataJson(String result) {
            rel_article_img_bottom.setVisibility(View.VISIBLE);
            Gson gson = new Gson();
            dmlSearchDetailEntity = gson.fromJson(result, DmlSearchDetailEntity.class);
            if (dmlSearchDetailEntity != null) {
                if (dmlSearchDetailEntity.getCode().equals(SUCCESS_CODE)) {
                    dmlSearchDetailBean = dmlSearchDetailEntity.getDmlSearchDetailBean();
                    setDetailData(dmlSearchDetailBean);
                } else if (dmlSearchDetailEntity.getCode().equals(EMPTY_CODE)) {
                    showToast(ArticleInvitationDetailsActivity.this, R.string.invalidData);
                } else {
                    showToast(ArticleInvitationDetailsActivity.this, dmlSearchDetailEntity.getMsg());
                }
            }
            NetLoadUtils.getQyInstance().showLoadSir(loadService,dmlSearchDetailBean,dmlSearchDetailEntity);
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

    @OnClick(R.id.tv_publish_comment)
    void publishComment(View view) {
        if (userId > 0) {
            setPublishComment();
        } else {
            getLoginStatus(ArticleInvitationDetailsActivity.this);
        }
    }

    //文章点赞
    @OnClick(R.id.tv_article_bottom_like)
    void likeArticle(View view) {
        if (dmlSearchDetailBean != null) {
            if (userId > 0) {
                setArticleLike();
            } else {
                getLoginStatus(ArticleInvitationDetailsActivity.this);
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
                getLoginStatus(ArticleInvitationDetailsActivity.this);
            }
        }
    }

    @OnClick(R.id.tv_life_back)
    void goBack(View view) {
        finish();
    }

    //设置分享
    @OnClick({R.id.tv_header_shared})
    void setShare(View view) {
        if (dmlSearchDetailBean != null) {
            UMShareAction umShareAction = new UMShareAction(ArticleInvitationDetailsActivity.this
                    , dmlSearchDetailBean.getPath()
                    , "分享" + dmlSearchDetailBean.getNickname() + "帖子"
                    , dmlSearchDetailBean.getDigest()
                    , Url.BASE_SHARE_PAGE_TWO + "m/template/find_template/find_detail.html?id=" + dmlSearchDetailBean.getId());
            umShareAction.setOnShareSuccessListener(new UMShareAction.OnShareSuccessListener() {
                @Override
                public void onShareSuccess() {
                    addArticleShareCount(dmlSearchDetailBean.getId());
                }
            });
        }
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
                        showToast(ArticleInvitationDetailsActivity.this, requestStatus.getResult() != null ? requestStatus.getResult().getMsg() : requestStatus.getMsg());
                    } else {
                        showToast(ArticleInvitationDetailsActivity.this, requestStatus.getResult() != null ? requestStatus.getResult().getMsg() : requestStatus.getMsg());
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                showToast(ArticleInvitationDetailsActivity.this, R.string.Get_Coupon_Fail);
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
                        showToast(ArticleInvitationDetailsActivity.this, requestStatus.getResult() != null ? requestStatus.getResult().getMsg() : requestStatus.getMsg());
                    } else {
                        showToast(ArticleInvitationDetailsActivity.this, requestStatus.getResult() != null ? requestStatus.getResult().getMsg() : requestStatus.getMsg());
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                showToast(ArticleInvitationDetailsActivity.this, R.string.Get_Coupon_Fail);
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
                getLoginStatus(ArticleInvitationDetailsActivity.this);
            }
        } else {
            showToast(ArticleInvitationDetailsActivity.this, "地址缺失");
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
                showToast(ArticleInvitationDetailsActivity.this, "登录失败 ");
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
        AlibcTrade.show(ArticleInvitationDetailsActivity.this, ordersPage, showParams, alibcTaokeParams, exParams, new AlibcTradeCallback() {
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
    @Override
    protected void onPause() {
        super.onPause();
        if(totalPersonalTrajectory!=null){
            Map<String,String> totalMap = new HashMap<>();
            totalMap.put("relate_id",artId);
            totalPersonalTrajectory.stopTotal(totalMap);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(totalPersonalTrajectory!=null){
            Map<String,String> totalMap = new HashMap<>();
            totalMap.put("relate_id",artId);
            totalPersonalTrajectory.stopTotal(totalMap);
        }
    }
}
