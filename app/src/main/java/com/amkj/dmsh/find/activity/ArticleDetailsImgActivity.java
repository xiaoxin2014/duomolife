package com.amkj.dmsh.find.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.text.emoji.widget.EmojiEditText;
import android.support.text.emoji.widget.EmojiTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.base.TinkerBaseApplicationLike;
import com.amkj.dmsh.bean.RequestStatus;
import com.amkj.dmsh.constant.CommunalComment;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.ConstantVariable;
import com.amkj.dmsh.constant.UMShareAction;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.dominant.adapter.ArticleCommentAdapter;
import com.amkj.dmsh.dominant.bean.DmlSearchCommentEntity;
import com.amkj.dmsh.dominant.bean.DmlSearchCommentEntity.DmlSearchCommentBean;
import com.amkj.dmsh.dominant.bean.DmlSearchCommentEntity.DmlSearchCommentBean.ReplyCommListBean;
import com.amkj.dmsh.find.adapter.FindImageListAdapter;
import com.amkj.dmsh.find.bean.InvitationImgDetailEntity;
import com.amkj.dmsh.find.bean.InvitationImgDetailEntity.InvitationImgDetailBean;
import com.amkj.dmsh.homepage.bean.InvitationDetailEntity.InvitationDetailBean.PictureBean;
import com.amkj.dmsh.homepage.bean.InvitationDetailEntity.InvitationDetailBean.RelevanceProBean;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.shopdetails.activity.ShopScrollDetailsActivity;
import com.amkj.dmsh.user.activity.UserPagerActivity;
import com.amkj.dmsh.user.adapter.InvitationProAdapter;
import com.amkj.dmsh.utils.CommonUtils;
import com.amkj.dmsh.utils.CommunalCopyTextUtils;
import com.amkj.dmsh.utils.KeyboardUtils;
import com.amkj.dmsh.utils.OffsetLinearLayoutManager;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.flexbox.FlexboxLayout;
import com.google.gson.Gson;
import com.klinker.android.link_builder.Link;
import com.klinker.android.link_builder.LinkBuilder;
import com.melnykov.fab.FloatingActionButton;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
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

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.amkj.dmsh.constant.ConstantMethod.addArticleShareCount;
import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.getStringChangeBoolean;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.insertNewTotalData;
import static com.amkj.dmsh.constant.ConstantMethod.setSkipPath;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.showToastRequestMsg;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.COMMENT_TYPE;
import static com.amkj.dmsh.constant.ConstantVariable.DEFAULT_COMMENT_TOTAL_COUNT;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.REGEX_URL;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.TOTAL_COUNT_TEN;
import static com.amkj.dmsh.constant.ConstantVariable.TYPE_1;
import static com.amkj.dmsh.constant.Url.F_INVITATION_DETAIL;
import static com.amkj.dmsh.constant.Url.Q_DML_SEARCH_COMMENT;
import static com.amkj.dmsh.constant.Url.UPDATE_ATTENTION;
import static com.amkj.dmsh.find.activity.FindTopicDetailsActivity.TOPIC_TYPE;
import static com.amkj.dmsh.utils.ProductLabelCreateUtils.getLabelInstance;

;

/**
 * Created by atd48 on 2016/6/30.
 * 帖子详情
 */
public class ArticleDetailsImgActivity extends BaseActivity {
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
    EmojiEditText emoji_edit_comment;
    @BindView(R.id.tv_send_comment)
    TextView tv_send_comment;
    //    点赞底栏
    @BindView(R.id.ll_article_comment)
    LinearLayout ll_article_comment;
    @BindView(R.id.tv_article_bottom_like)
    TextView tv_article_bottom_like;
    @BindView(R.id.tv_article_bottom_collect)
    TextView tv_article_bottom_collect;
    @BindView(R.id.tv_publish_comment)
    TextView tv_publish_comment;
    private ArticleCommentAdapter adapterArticleComment;
    //    评论数
    private List<DmlSearchCommentBean> articleCommentList = new ArrayList<>();
    //    关联商品
    private List<RelevanceProBean> relevanceProList = new ArrayList<>();
    private List<PictureBean> pathList = new ArrayList<>();
    private String artId;
    private int page = 1;
    private ImgDetailsHeaderView imgHeaderView;
    private InvitationImgDetailEntity invitationDetailEntity;
    private String type = "articleDetails";
    private float screenHeight;
    //    按下点击位置
    private float locationY;
    private InvitationProAdapter invitationProAdapter;
    private InvitationImgDetailBean invitationDetailBean;
    private FindImageListAdapter findImageListAdapter;
    private boolean isScrollToComment;

    @Override
    protected int getContentView() {
        return R.layout.activity_find_article_details;
    }

    @Override
    protected void initViews() {
        tv_header_titleAll.setText("帖子详情");
        Intent intent = getIntent();
        artId = intent.getStringExtra("ArtId");
        isScrollToComment = getStringChangeBoolean(intent.getStringExtra("scrollToComment"));
        TinkerBaseApplicationLike app = (TinkerBaseApplicationLike) TinkerManager.getTinkerApplicationLike();
        screenHeight = app.getScreenHeight();
        View headerView = LayoutInflater.from(ArticleDetailsImgActivity.this).inflate(R.layout.layout_find_img_details, (ViewGroup) communal_recycler.getParent(), false);
        imgHeaderView = new ImgDetailsHeaderView();
        ButterKnife.bind(imgHeaderView, headerView);
        imgHeaderView.initView();
        adapterArticleComment = new ArticleCommentAdapter(ArticleDetailsImgActivity.this, articleCommentList, COMMENT_TYPE);
        adapterArticleComment.addHeaderView(headerView);
        communal_recycler.setLayoutManager(new OffsetLinearLayoutManager(this));
        communal_recycler.addItemDecoration(new ItemDecoration.Builder()
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_five_dp).create());
        communal_recycler.setAdapter(adapterArticleComment);

        smart_communal_refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                //                滚动距离置0
                page = 1;
                getDetailData();
            }
        });
        adapterArticleComment.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                page++;
                getArticleImgComment();
            }
        }, communal_recycler);

        communal_recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                OffsetLinearLayoutManager layoutManager = (OffsetLinearLayoutManager) recyclerView.getLayoutManager();
                int scrollY = layoutManager.computeVerticalScrollOffset();
                if (scrollY > screenHeight * 1.5) {
                    if (download_btn_communal.getVisibility() == GONE) {
                        download_btn_communal.setVisibility(VISIBLE);
                        download_btn_communal.hide(false);
                    }
                    if(!download_btn_communal.isVisible()){
                        download_btn_communal.show(true);
                    }
                } else {
                    if (download_btn_communal.isVisible()) {
                        download_btn_communal.hide();
                    }
                }
            }
        });
        download_btn_communal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) communal_recycler.getLayoutManager();
                download_btn_communal.hide();
                linearLayoutManager.scrollToPositionWithOffset(0, 0);
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
                                getLoginStatus(ArticleDetailsImgActivity.this);
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
                                getLoginStatus(ArticleDetailsImgActivity.this);
                            }
                            break;
                        case R.id.civ_comm_comment_avatar:
                            Intent intent = new Intent();
                            intent.setClass(ArticleDetailsImgActivity.this, UserPagerActivity.class);
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
                    CommunalCopyTextUtils.showPopWindow(ArticleDetailsImgActivity.this, (TextView) view, dmlSearchCommentBean.getContent());
                }
                return false;
            }
        });
        tv_publish_comment.setText(R.string.comment_hint_invitation);
        totalPersonalTrajectory = insertNewTotalData(this, artId);
        rel_article_img_bottom.setVisibility(GONE);
        KeyboardUtils.registerSoftInputChangedListener(this, new KeyboardUtils.OnSoftInputChangedListener() {
            @Override
            public void onSoftInputChanged(int height) {
                if (height == 0) {
                    ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0).requestFocus();
                }
            }
        });
    }

    /**
     * 点击编辑器外区域隐藏键盘 避免点击搜索完没有隐藏键盘
     *
     * @param ev
     * @return
     */
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
        return super.dispatchTouchEvent(ev);
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
        params.put("id", invitationDetailBean.getId());
        params.put("favortype", "doc");
        NetLoadUtils.getNetInstance().loadNetDataPost(this,url,params,null);
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

    /**
     * 设置评论点赞
     * @param dmlSearchCommentBean
     */
    private void setCommentLike(DmlSearchCommentBean dmlSearchCommentBean) {
        String url = Url.BASE_URL + Url.FIND_AND_COMMENT_FAV;
        Map<String, Object> params = new HashMap<>();
        //用户id
        params.put("tuid", userId);
        //评论id
        params.put("id", dmlSearchCommentBean.getId());
        NetLoadUtils.getNetInstance().loadNetDataPost(this,url,params,null);
    }

    //    文章收藏
    private void setArticleCollect() {
        String url = Url.BASE_URL + Url.F_ARTICLE_COLLECT;
        Map<String, Object> params = new HashMap<>();
        //用户id
        params.put("uid", userId);
        //文章id
        params.put("object_id", invitationDetailBean.getId());
        params.put("type", ConstantVariable.TYPE_C_ARTICLE);
        NetLoadUtils.getNetInstance().loadNetDataPost(this,url,params,new NetLoadListenerHelper(){
            @Override
            public void onSuccess(String result) {
                loadHud.dismiss();
                Gson gson = new Gson();
                RequestStatus requestStatus = gson.fromJson(result, RequestStatus.class);
                if (requestStatus != null) {
                    if (requestStatus.getCode().equals(SUCCESS_CODE)) {
                        tv_article_bottom_collect.setSelected(!tv_article_bottom_collect.isSelected());
                    }
                }
            }

            @Override
            public void onNotNetOrException() {
                loadHud.dismiss();
            }

            @Override
            public void onError(Throwable throwable) {
                showToast(ArticleDetailsImgActivity.this, String.format(getResources().getString(R.string.collect_failed), "文章"));
            }

            @Override
            public void netClose() {
                showToast(ArticleDetailsImgActivity.this, R.string.unConnectedNetwork);
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
                showToast(ArticleDetailsImgActivity.this, R.string.comment_send_success);
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
        constantMethod.setSendComment(ArticleDetailsImgActivity.this, communalComment);
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
                        communalComment.setObjId(invitationDetailBean.getId());
                        communalComment.setUserId(userId);
                        communalComment.setToUid(invitationDetailBean.getUid());
                        sendComment(communalComment);
                    } else {
                        showToast(ArticleDetailsImgActivity.this, "请正确输入内容");
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
        Map<String, Object> params = new HashMap<>();
        params.put("id", artId);
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
                smart_communal_refresh.finishRefresh();
                adapterArticleComment.loadMoreComplete();
                if (page == 1) {
                    articleCommentList.clear();
                }
                Gson gson = new Gson();
                DmlSearchCommentEntity dmlSearchCommentEntity = gson.fromJson(result, DmlSearchCommentEntity.class);
                if (dmlSearchCommentEntity != null) {
                    if (dmlSearchCommentEntity.getCode().equals(SUCCESS_CODE)) {
                        articleCommentList.addAll(dmlSearchCommentEntity.getDmlSearchCommentList());
                    } else if (dmlSearchCommentEntity.getCode().equals(EMPTY_CODE)) {
                        adapterArticleComment.loadMoreEnd();
                    } else {
                        showToast(ArticleDetailsImgActivity.this, dmlSearchCommentEntity.getMsg());
                    }
                    if (articleCommentList.size() > 0) {
                        imgHeaderView.rel_img_art_com.setVisibility(View.VISIBLE);
                        imgHeaderView.tv_comm_comment_count.setText(String.format(getString(R.string.comment_count), dmlSearchCommentEntity.getCommentSize()));
                    } else {
                        imgHeaderView.rel_img_art_com.setVisibility(GONE);
                    }
                    adapterArticleComment.notifyDataSetChanged();
                }
                scrollToComment();
            }

            @Override
            public void onNotNetOrException() {
                adapterArticleComment.loadMoreEnd(true);
            }

            @Override
            public void onError(Throwable throwable) {
                showToast(ArticleDetailsImgActivity.this, R.string.invalidData);
            }
        });
    }

    class ImgDetailsHeaderView {
        //        详情内容
        @BindView(R.id.tv_find_img_content)
        EmojiTextView tv_find_img_content;
        @BindView(R.id.tv_inv_user_name)
        TextView tv_proName;
        @BindView(R.id.tv_recommend_invitation_time)
        TextView tv_recommend_invitation_time;
        @BindView(R.id.tv_inv_live_att)
        TextView tv_live_attention;
        //头像
        @BindView(R.id.iv_inv_user_avatar)
        ImageView img_live_avatar;
        //标签
        @BindView(R.id.flex_communal_tag)
        FlexboxLayout flex_communal_tag;
        @BindView(R.id.rel_tag_layout_img)
        RelativeLayout rel_tag_layout_img;
        //是否显示多张图片，判断
        @BindView(R.id.rv_img_find)
        RecyclerView rv_img_find;
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
//            图片
            rv_img_find.setNestedScrollingEnabled(false);
            rv_img_find.setLayoutManager(new LinearLayoutManager(ArticleDetailsImgActivity.this));
            rv_img_find.addItemDecoration(new ItemDecoration.Builder()
                    // 设置分隔线资源ID
                    .setDividerId(R.drawable.item_divider_ten_white)


                    .create());
            findImageListAdapter = new FindImageListAdapter(ArticleDetailsImgActivity.this, pathList);
            rv_img_find.setAdapter(findImageListAdapter);
            communal_recycler_wrap.setNestedScrollingEnabled(false);
            communal_recycler_wrap.setLayoutManager(new LinearLayoutManager(ArticleDetailsImgActivity.this));
            invitationProAdapter = new InvitationProAdapter(ArticleDetailsImgActivity.this, relevanceProList);
            communal_recycler_wrap.setAdapter(invitationProAdapter);
            invitationProAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    RelevanceProBean relevanceProBean = (RelevanceProBean) view.getTag();
                    if (relevanceProBean != null) {
                        Intent intent = new Intent(ArticleDetailsImgActivity.this, ShopScrollDetailsActivity.class);
                        intent.putExtra("productId", String.valueOf(relevanceProBean.getProductId()));
                        startActivity(intent);
                    }
                }
            });
            tv_live_attention.setVisibility(View.VISIBLE);
        }

        private void setData(final InvitationImgDetailBean detailsBean) {
            //图片展示、浏览
            if (detailsBean.getPictureList().size() < 1) {
                rv_img_find.setVisibility(View.GONE);
            } else {
                rv_img_find.setVisibility(View.VISIBLE);
                setImagePath(detailsBean.getPictureList());
            }
            GlideImageLoaderUtil.loadHeaderImg(ArticleDetailsImgActivity.this, img_live_avatar, detailsBean.getAvatar());
            img_live_avatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ArticleDetailsImgActivity.this, UserPagerActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("userId", String.valueOf(detailsBean.getUid()));
                    startActivity(intent);
                }
            });
            tv_proName.setText(detailsBean.getNickname());
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
            if (detailsBean.getTagsList() != null && detailsBean.getTagsList().size() > 0) {
                rel_tag_layout_img.setVisibility(View.VISIBLE);
                flex_communal_tag.setShowDivider(FlexboxLayout.SHOW_DIVIDER_MIDDLE);
                flex_communal_tag.setDividerDrawable(getResources().getDrawable(R.drawable.item_divider_nine_dp_white));
                flex_communal_tag.removeAllViews();
                for (int i = 0; i < detailsBean.getTagsList().size(); i++) {
                    if (i == 0) {
                        flex_communal_tag.addView(getLabelInstance().createArticleIcon(ArticleDetailsImgActivity.this));
                    }
                    flex_communal_tag.addView(getLabelInstance().createArticleClickTag(ArticleDetailsImgActivity.this, detailsBean.getTagsList().get(i)));
                }
            } else {
                rel_tag_layout_img.setVisibility(View.GONE);
            }
            tv_article_bottom_like.setSelected(detailsBean.isIsFavor());
            articleFavor = detailsBean.isIsFavor();
            tv_article_bottom_like.setText(String.valueOf(detailsBean.getFavor()));
            tv_article_bottom_collect.setSelected(detailsBean.isIsCollect());
            final String description = detailsBean.getDescription();
            List<Link> links = new ArrayList<>();
            if (!TextUtils.isEmpty(description)) {
                tv_find_img_content.setText(getStrings(detailsBean.getDescription()));
                if (!TextUtils.isEmpty(detailsBean.getTopic_title())) {
                    Link link = new Link(String.format(getResources().getString(R.string.topic_format), getStrings(invitationDetailBean.getTopic_title())));
                    link.setTextColor(0xff0a88fa);
                    link.setUnderlined(false);
                    link.setHighlightAlpha(0f);
                    links.add(link);
                    link.setOnClickListener(new Link.OnClickListener() {
                        @Override
                        public void onClick(String clickedText) {
                            if (!TOPIC_TYPE.equals(type) && !TextUtils.isEmpty(detailsBean.getTopic_title()) && invitationDetailBean.getTopic_id() > 0) {
                                Intent intent = new Intent(ArticleDetailsImgActivity.this, FindTopicDetailsActivity.class);
                                intent.putExtra("topicId", String.valueOf(detailsBean.getTopic_id()));
                                startActivity(intent);
                            }
                        }
                    });
                }
                /**
                 * 网址识别
                 */

                Link discernUrl = new Link(Pattern.compile(REGEX_URL));
                discernUrl.setTextColor(0xff0a88fa);
                discernUrl.setUnderlined(false);
                discernUrl.setHighlightAlpha(0f);
                discernUrl.setUrlReplace(true);
                discernUrl.setOnClickListener(new Link.OnClickListener() {
                    @Override
                    public void onClick(String clickedText) {
                        if (!TextUtils.isEmpty(clickedText)) {
                            setSkipPath(ArticleDetailsImgActivity.this, clickedText, false);
                        } else {
                            showToast(ArticleDetailsImgActivity.this, "网址有误，请重试");
                        }
                    }
                });
                links.add(discernUrl);
                LinkBuilder.on(tv_find_img_content)
                        .setText(getStrings(invitationDetailBean.getDescription()))
                        .addLinks(links)
                        .build();
            } else {
//                没有数据内容
                tv_find_img_content.setVisibility(GONE);
            }
            tv_find_img_content.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    CommunalCopyTextUtils.showPopWindow(ArticleDetailsImgActivity.this, (TextView) v, description);
                    return true;
                }
            });
            if (detailsBean.getRelevanceProList() != null && detailsBean.getRelevanceProList().size() > 0) {
                relevanceProList.clear();
                relevanceProList.addAll(detailsBean.getRelevanceProList());
                invitationProAdapter.setNewData(relevanceProList);
            }
        }

        private void setImagePath(List<PictureBean> pictureBeanList) {
            for (int i = 0; i < pictureBeanList.size(); i++) {
                PictureBean pictureBean = pictureBeanList.get(i);
                pictureBean.setItemType(TYPE_1);
                pictureBean.setIndex(i);
            }
            pathList.clear();
            pathList.addAll(pictureBeanList);
            findImageListAdapter.setNewData(pathList);
        }

        @OnClick(R.id.tv_inv_live_att)
        void setAttentionTag(View view) {
            if (invitationDetailBean != null) {
                if (userId > 0) {
                    setAttentionFlag(invitationDetailBean, view);
                } else {
                    getLoginStatus(ArticleDetailsImgActivity.this);
                }
            }
        }

        public void setAttentionFlag(final InvitationImgDetailBean imgDetailBean, final View v) {
            final TextView textView = (TextView) v;
            isAttention = textView.isSelected();
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
            NetLoadUtils.getNetInstance().loadNetDataPost(ArticleDetailsImgActivity.this,UPDATE_ATTENTION,params,new NetLoadListenerHelper(){
                @Override
                public void onSuccess(String result) {
                    Gson gson = new Gson();
                    RequestStatus requestStatus = gson.fromJson(result, RequestStatus.class);
                    if (requestStatus != null) {
                        if (requestStatus.getCode().equals(SUCCESS_CODE)) {
                            if (!isAttention) {
                                textView.setSelected(true);
                                textView.setText("已关注");
                                showToast(ArticleDetailsImgActivity.this, "已关注");
                                isAttention = true;
                            } else {
                                textView.setSelected(false);
                                textView.setText("+ 关注");
                                showToast(ArticleDetailsImgActivity.this, "已取消关注");
                                isAttention = false;
                            }
                            getDetailsData();
                        } else {
                            showToastRequestMsg(ArticleDetailsImgActivity.this, requestStatus);
                        }
                        textView.setEnabled(true);
                    }
                }

                @Override
                public void onNotNetOrException() {
                    textView.setEnabled(true);
                }
            });
        }

        public void getDetailsData() {
            Map<String, String> params = new HashMap<>();
            params.put("id", artId);
//            帖子改版 3.0.9
            params.put("isV2", "true");
            if (userId > 0) {
                params.put("fuid", String.valueOf(userId));
            }
            NetLoadUtils.getNetInstance().loadNetDataPost(ArticleDetailsImgActivity.this,F_INVITATION_DETAIL,params,new NetLoadListenerHelper(){
                @Override
                public void onSuccess(String result) {
                    smart_communal_refresh.finishRefresh();
                    adapterArticleComment.loadMoreComplete();
                    rel_article_img_bottom.setVisibility(View.VISIBLE);
                    Gson gson = new Gson();
                    invitationDetailEntity = gson.fromJson(result, InvitationImgDetailEntity.class);
                    if (invitationDetailEntity != null) {
                        if (invitationDetailEntity.getCode().equals(SUCCESS_CODE)) {
                            invitationDetailBean = invitationDetailEntity.getInvitationImgDetailBean();
                            setData(invitationDetailBean);
                        } else {
                            if (!invitationDetailEntity.getCode().equals(EMPTY_CODE)) {
                                showToast(ArticleDetailsImgActivity.this, invitationDetailEntity.getMsg());
                            }
                        }
                    }
                    NetLoadUtils.getNetInstance().showLoadSir(loadService, invitationDetailBean, invitationDetailEntity);
                }

                @Override
                public void onNotNetOrException() {
                    smart_communal_refresh.finishRefresh();
                    adapterArticleComment.loadMoreEnd(true);
                    rel_article_img_bottom.setVisibility(GONE);
                    NetLoadUtils.getNetInstance().showLoadSir(loadService, invitationDetailBean, invitationDetailEntity);
                }
            });
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
    void publishComment() {
        if (invitationDetailBean != null) {
            if (userId > 0) {
                setPublishComment();
            } else {
                getLoginStatus(ArticleDetailsImgActivity.this);
            }
        }
    }

    //文章点赞
    @OnClick(R.id.tv_article_bottom_like)
    void likeArticle() {
        if (invitationDetailBean != null) {
            if (userId > 0) {
                setArticleLike();
            } else {
                getLoginStatus(ArticleDetailsImgActivity.this);
            }
        }
    }

    //文章收藏
    @OnClick(R.id.tv_article_bottom_collect)
    void collectArticle() {
        if (invitationDetailBean != null) {
            if (userId > 0) {
                loadHud.show();
                setArticleCollect();
            } else {
                getLoginStatus(ArticleDetailsImgActivity.this);
            }
        }
    }

    @OnClick(R.id.tv_life_back)
    void goBack() {
        finish();
    }

    //设置分享
    @OnClick({R.id.tv_header_shared})
    void setShare() {
        if (invitationDetailBean != null) {
            UMShareAction umShareAction = new UMShareAction(ArticleDetailsImgActivity.this
                    , invitationDetailBean.getPath()
                    , "分享" + invitationDetailBean.getNickname() + "帖子"
                    , invitationDetailBean.getDescription()
                    , Url.BASE_SHARE_PAGE_TWO + "m/template/find_template/find_detail.html?id=" + invitationDetailBean.getId());
            umShareAction.setOnShareSuccessListener(new UMShareAction.OnShareSuccessListener() {
                @Override
                public void onShareSuccess() {
                    addArticleShareCount(invitationDetailBean.getId());
                }
            });
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (totalPersonalTrajectory != null) {
            Map<String, String> totalMap = new HashMap<>();
            totalMap.put("relate_id", artId);
            totalPersonalTrajectory.stopTotal(totalMap);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (totalPersonalTrajectory != null) {
            Map<String, String> totalMap = new HashMap<>();
            totalMap.put("relate_id", artId);
            totalPersonalTrajectory.stopTotal(totalMap);
        }
    }

    /**
     * 滚动到评论区
     */
    private void scrollToComment() {
        if (isScrollToComment) {
//                        必须确保详情数据下来
            int headerLayoutCount = adapterArticleComment.getHeaderLayoutCount();
            LinearLayout headerLayout = adapterArticleComment.getHeaderLayout();
            TinkerBaseApplicationLike tinkerBaseApplicationLike = (TinkerBaseApplicationLike) TinkerManager.getTinkerApplicationLike();
            int screenHeight = tinkerBaseApplicationLike.getScreenHeight();
            if (headerLayout != null) {
                final boolean[] isFirstSame = {true};
                final int[] measureHeight = {0};
                headerLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        int measuredNewHeight = headerLayout.getMeasuredHeight();
                        if (measuredNewHeight > screenHeight || (measureHeight[0] == measuredNewHeight && !isFirstSame[0])) {
                            headerLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                            if (measuredNewHeight > screenHeight) {
                                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) communal_recycler.getLayoutManager();
                                linearLayoutManager.scrollToPositionWithOffset(headerLayoutCount, 300);
                                linearLayoutManager.setStackFromEnd(true);
                            }
                        } else if (measureHeight[0] == measuredNewHeight && isFirstSame[0]) {
                            /**
                             * 因会出现两次测量结果一致，故避免这情况
                             */
                            isFirstSame[0] = false;
                        } else {
                            measureHeight[0] = measuredNewHeight;
                        }
                    }
                });
            }
        }
        isScrollToComment = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        KeyboardUtils.unregisterSoftInputChangedListener(this);
    }
}
