package com.amkj.dmsh.dominant.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.BaseApplication;
import com.amkj.dmsh.constant.CommunalComment;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.ConstantVariable;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.constant.XUtil;
import com.amkj.dmsh.dominant.adapter.ArticleCommentDetailAdapter;
import com.amkj.dmsh.dominant.bean.CommentDetailEntity;
import com.amkj.dmsh.dominant.bean.CommentDetailEntity.CommentDetailBean;
import com.amkj.dmsh.dominant.bean.CommentDetailEntity.CommentDetailBean.ReplyCommBean;
import com.amkj.dmsh.mine.activity.MineLoginActivity;
import com.amkj.dmsh.mine.bean.SavePersonalInfoBean;
import com.amkj.dmsh.user.activity.UserPagerActivity;
import com.amkj.dmsh.utils.CommonUtils;
import com.amkj.dmsh.utils.CommunalCopyTextUtils;
import com.amkj.dmsh.utils.NetWorkUtils;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.amkj.dmsh.utils.inteface.MyCallBack;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.melnykov.fab.FloatingActionButton;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import butterknife.OnTouch;
import emojicon.EmojiconEditText;

import static android.view.View.GONE;
import static com.amkj.dmsh.R.id.tv_comm_comment_like;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantVariable.TOTAL_COUNT_TWENTY;

;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/7/21
 * class description:评论详情
 */
public class CommentDetailsActivity extends BaseActivity {
    @BindView(R.id.smart_communal_refresh)
    RefreshLayout smart_communal_refresh;
    @BindView(R.id.communal_recycler)
    RecyclerView communal_recycler;
    //    滚动至顶部
    @BindView(R.id.download_btn_communal)
    public FloatingActionButton download_btn_communal;

    @BindView(R.id.ll_input_comment)
    public LinearLayout ll_input_comment;
    @BindView(R.id.emoji_edit_comment)
    EmojiconEditText emoji_edit_comment;
    @BindView(R.id.tv_send_comment)
    TextView tv_send_comment;

    @BindView(R.id.tv_header_title)
    TextView tv_header_titleAll;
    @BindView(R.id.tv_header_shared)
    TextView tv_header_shared;
    @BindView(R.id.communal_load)
    View communal_load;
    @BindView(R.id.communal_error)
    View communal_error;
    @BindView(R.id.communal_empty)
    View communal_empty;
    private int page = 1;
    private int scrollY;
    //    按下点击位置
    private float locationY;
    private float screenHeight;
    private int uid;
    private ArticleCommentDetailAdapter articleCommentAdapter;
    private List<ReplyCommBean> commentDetailList = new ArrayList();
    private CommentHeaderView commentHeaderView;
    private CommentDetailBean commentDetailBean;
    private String commentId;
    private String objId;
    private String commentType;

    @Override
    protected int getContentView() {
        return R.layout.activity_comment_details;
    }
    @Override
    protected void initViews() {
        isLoginStatus();
        tv_header_titleAll.setText("评论");
        tv_header_shared.setVisibility(GONE);
        Intent intent = getIntent();
        commentId = intent.getStringExtra("commentId");
        objId = intent.getStringExtra("objId");
        commentType = intent.getStringExtra("commentType");
        communal_recycler.setLayoutManager(new LinearLayoutManager(CommentDetailsActivity.this));
        articleCommentAdapter = new ArticleCommentDetailAdapter(CommentDetailsActivity.this, commentDetailList);
        View commentView = LayoutInflater.from(CommentDetailsActivity.this).inflate(R.layout.adapter_article_comment, (ViewGroup) communal_recycler.getParent(), false);
        commentHeaderView = new CommentHeaderView();
        ButterKnife.bind(commentHeaderView, commentView);
        commentHeaderView.initView();
        articleCommentAdapter.addHeaderView(commentView);
        communal_recycler.setAdapter(articleCommentAdapter);

        smart_communal_refresh.setOnRefreshListener((refreshLayout) -> {
            page = 1;
                loadData();
        });
        articleCommentAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (page * TOTAL_COUNT_TWENTY <= commentDetailList.size()) {
                    page++;
                    loadData();
                } else {
                    articleCommentAdapter.loadMoreEnd();
                }
            }
        }, communal_recycler);
        articleCommentAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                ReplyCommBean replyCommBean = (ReplyCommBean) view.getTag(R.id.iv_tag);
                if (replyCommBean == null) {
                    replyCommBean = (ReplyCommBean) view.getTag();
                }
                if (replyCommBean != null) {
                    switch (view.getId()) {
                        case tv_comm_comment_like:
                            if (uid > 0) {
                                replyCommBean.setFavor(!replyCommBean.isFavor());
                                int likeNum = replyCommBean.getLike_num();
                                replyCommBean.setLike_num(replyCommBean.isFavor()
                                        ? likeNum + 1 : likeNum - 1 > 0 ? likeNum - 1 : 0);
                                commentDetailList.set(position, replyCommBean);
                                articleCommentAdapter.notifyItemChanged(position + adapter.getHeaderLayoutCount());
                                setCommentLike(replyCommBean.getId());
                            } else {
                                getLoginStatus();
                            }
                            break;
                        case R.id.tv_comm_comment_receive:
//                            打开评论
                            if (uid > 0) {
                                if (View.VISIBLE == ll_input_comment.getVisibility()) {
                                    commentViewVisible(View.GONE, null);
                                } else {
                                    commentViewVisible(View.VISIBLE, replyCommBean);
                                }
                            } else {
                                getLoginStatus();
                            }
                            break;
                        case R.id.civ_comm_comment_avatar:
                            skipUserUI(replyCommBean.getUid());
                            break;
                    }
                }
            }
        });
        articleCommentAdapter.setOnItemChildLongClickListener(new BaseQuickAdapter.OnItemChildLongClickListener() {
            @Override
            public boolean onItemChildLongClick(BaseQuickAdapter adapter, View view, int position) {
                ReplyCommBean replyCommBean = (ReplyCommBean) view.getTag();
                if (replyCommBean != null && !TextUtils.isEmpty(replyCommBean.getContent())) {
                    CommunalCopyTextUtils.showPopWindow(CommentDetailsActivity.this, (TextView) view, replyCommBean.getContent());
                }
                return false;
            }
        });

        smart_communal_refresh.setOnRefreshListener((refreshLayout) -> {
            page = 1;
                loadData();
        });
        download_btn_communal.attachToRecyclerView(communal_recycler, null, new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                scrollY += dy;
                if (!recyclerView.canScrollVertically(-1)) {
                    scrollY = 0;
                }
                if (screenHeight == 0) {
                    BaseApplication app = (BaseApplication) getApplication();
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
        communal_load.setVisibility(View.VISIBLE);
    }

    @Override
    protected void loadData() {
        getCommentData();
    }

    private void getCommentData() {
        if (NetWorkUtils.checkNet(CommentDetailsActivity.this)) {
            String url = Url.BASE_URL + Url.Q_COMMENT_DETAILS;
            Map<String, Object> params = new HashMap<>();
            params.put("comment_id", commentId);
            params.put("currentPage", page);
            params.put("showCount", TOTAL_COUNT_TWENTY);
            params.put("comtype", commentType);
            params.put("obj_id", objId);
            if (uid > 0) {
                params.put("uid", uid);
            }
            XUtil.Post(url, params, new MyCallBack<String>() {
                @Override
                public void onSuccess(String result) {
                    smart_communal_refresh.finishRefresh();
                    communal_load.setVisibility(GONE);
                    communal_error.setVisibility(GONE);
                    articleCommentAdapter.loadMoreComplete();
                    if (page == 1) {
                        commentDetailList.clear();
                    }
                    Gson gson = new Gson();
                    CommentDetailEntity commentDetailEntity = gson.fromJson(result, CommentDetailEntity.class);
                    if (commentDetailEntity != null) {
                        if (commentDetailEntity.getCode().equals("01")) {
                            commentDetailBean = commentDetailEntity.getCommentDetailBean();
                            setCommentData(commentDetailBean);
                        } else if (!commentDetailEntity.getCode().equals("02")) {
                            if (page == 1) {
                                communal_error.setVisibility(GONE);
                            }
                            showToast(CommentDetailsActivity.this, commentDetailEntity.getMsg());
                        }
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    smart_communal_refresh.finishRefresh();
                    communal_load.setVisibility(GONE);
                    articleCommentAdapter.loadMoreComplete();
                    if (commentDetailList.size() < 1) {
                        communal_error.setVisibility(View.VISIBLE);
                    } else {
                        showToast(CommentDetailsActivity.this, R.string.invalidData);
                    }
                    super.onError(ex, isOnCallback);
                }
            });
        } else {
            articleCommentAdapter.loadMoreComplete();
            smart_communal_refresh.finishRefresh();
            communal_load.setVisibility(GONE);
            articleCommentAdapter.loadMoreComplete();
            showToast(CommentDetailsActivity.this, R.string.unConnectedNetwork);
        }
    }

    private void setCommentData(CommentDetailBean commentDetailBean) {
        GlideImageLoaderUtil.loadHeaderImg(CommentDetailsActivity.this, commentHeaderView.civ_comm_comment_avatar, commentDetailBean.getAvatar());
        commentHeaderView.tv_comm_comment_nick_name.setText(getStrings(commentDetailBean.getNickname()));
        commentHeaderView.tv_comm_comment_content.setText(getStrings(commentDetailBean.getContent()));
        commentHeaderView.tv_comm_comment_time.setText(getStrings(commentDetailBean.getCtime()));
        commentHeaderView.tv_comm_comment_like.setSelected(commentDetailBean.isIsFavor());
        commentHeaderView.tv_comm_comment_like.setText(commentDetailBean.getLike_num() > 0 ? String.valueOf(commentDetailBean.getLike_num()) : "赞");
        commentDetailList.addAll(commentDetailBean.getReplyCommList());
        if (page == 1) {
            articleCommentAdapter.setNewData(commentDetailList);
        } else {
            articleCommentAdapter.notifyDataSetChanged();
        }
    }

    private void setCommentLike(int id) {
        String url = Url.BASE_URL + Url.FIND_AND_COMMENT_FAV;
        Map<String, Object> params = new HashMap<>();
        //用户id
        params.put("tuid", uid);
        //评论id
        params.put("id", id);
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

    private void commentViewVisible(int visibility, final ReplyCommBean replyCommBean) {
        ll_input_comment.setVisibility(visibility);
        if (View.VISIBLE == visibility) {
            emoji_edit_comment.requestFocus();
            //弹出键盘
            if (replyCommBean != null) {
                emoji_edit_comment.setHint("回复" + replyCommBean.getNickname() + ":");
            } else {
                emoji_edit_comment.setHint(getString(R.string.comment_article_hint));
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
                        if (replyCommBean != null) {
                            communalComment.setIsReply(1);
                            communalComment.setReplyUserId(replyCommBean.getReply_uid());
                            communalComment.setPid(replyCommBean.getId());
                            communalComment.setMainCommentId(commentDetailBean.getId());
                        }
                        communalComment.setObjId(replyCommBean.getObj_id());
                        communalComment.setUserId(uid);
                        communalComment.setToUid(replyCommBean.getTo_uid());
                        sendComment(communalComment);
                    } else {
                        showToast(CommentDetailsActivity.this, "请正确输入内容");
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
                commentViewVisible(View.GONE, null);
                showToast(CommentDetailsActivity.this, R.string.comment_article_send_success);
                page = 1;
                getCommentData();
                emoji_edit_comment.setText("");
            }

            @Override
            public void onError() {
                loadHud.dismiss();
                tv_send_comment.setText("发送");
                tv_send_comment.setEnabled(true);
            }
        });
        constantMethod.setSendComment(CommentDetailsActivity.this, communalComment);
    }

    private void isLoginStatus() {
        SavePersonalInfoBean personalInfo = ConstantMethod.getPersonalInfo(this);
        if(personalInfo.isLogin()){
            uid = personalInfo.getUid();
        }else{
            uid = 0;
        }
    }

    private void getLoginStatus() {
        SavePersonalInfoBean personalInfo = ConstantMethod.getPersonalInfo(this);
        if(personalInfo.isLogin()){
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
        if (resultCode == RESULT_OK) {
            if (requestCode == ConstantVariable.IS_LOGIN_CODE) {
                getLoginStatus();
                loadData();
            }
        }
    }

    @OnClick({R.id.rel_communal_error, R.id.communal_empty})
    void refreshData(View view) {
        communal_load.setVisibility(View.VISIBLE);
        communal_error.setVisibility(GONE);
        page = 1;
        loadData();
    }

    @OnClick(R.id.tv_life_back)
    void goBack(View view) {
        finish();
    }

    class CommentHeaderView {
        @BindView(R.id.civ_comm_comment_avatar)
        ImageView civ_comm_comment_avatar;
        @BindView(R.id.tv_comm_comment_nick_name)
        TextView tv_comm_comment_nick_name;
        @BindView(R.id.tv_comm_comment_content)
        TextView tv_comm_comment_content;
        @BindView(R.id.tv_comm_comment_time)
        TextView tv_comm_comment_time;
        @BindView(R.id.tv_comm_comment_like)
        TextView tv_comm_comment_like;
        @BindView(R.id.rel_art_comment_inner)
        RelativeLayout rel_art_comment_inner;

        public void initView() {
            rel_art_comment_inner.setVisibility(GONE);
        }

        @OnClick(R.id.tv_comm_comment_like)
        void likeComment() {
            if (uid > 0) {
                tv_comm_comment_like.setSelected(!tv_comm_comment_like.isSelected());
                String likeCount = getNumber(tv_comm_comment_like.getText().toString().trim());
                int likeNum = Integer.parseInt(likeCount);
                tv_comm_comment_like.setText(String.valueOf(tv_comm_comment_like.isSelected()
                        ? likeNum + 1 : likeNum - 1 > 0 ? likeNum - 1 : "赞"));
                setCommentLike(commentDetailBean.getId());
            } else {
                getLoginStatus();
            }
        }

        @OnClick(R.id.tv_comm_comment_receive)
        void receiverComment() {
            if (uid > 0) {
                setReceiverComment();
            } else {
                getLoginStatus();
            }
        }

        @OnClick(R.id.civ_comm_comment_avatar)
        void clickAvatar() {
            if (commentDetailBean != null) {
                skipUserUI(commentDetailBean.getUid());
            }
        }

        //        长安复制
        @OnLongClick(R.id.tv_comm_comment_content)
        boolean copyContent(View view) {
            if (commentDetailBean != null && !TextUtils.isEmpty(commentDetailBean.getContent())) {
                CommunalCopyTextUtils.showPopWindow(CommentDetailsActivity.this, (TextView) view, commentDetailBean.getContent());
            }
            return false;
        }
    }

    private void skipUserUI(int uid) {
        Intent intent = new Intent();
        intent.setClass(CommentDetailsActivity.this, UserPagerActivity.class);
        intent.putExtra("userId", String.valueOf(uid));
        startActivity(intent);
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

    private void setReceiverComment() {
        if (uid > 0) {
            if (View.VISIBLE == ll_input_comment.getVisibility()) {
                commentViewVisible(View.GONE, null);
            } else {
                ReplyCommBean replyCommBean = new ReplyCommBean();
                replyCommBean.setReply_uid(commentDetailBean.getUid());
                replyCommBean.setNickname(commentDetailBean.getNickname());
                replyCommBean.setId(commentDetailBean.getId());
                replyCommBean.setObj_id(commentDetailBean.getObj_id());
                replyCommBean.setFavor(commentDetailBean.isIsFavor());
                replyCommBean.setTo_uid(commentDetailBean.getTo_uid());
                commentViewVisible(View.VISIBLE, replyCommBean);
            }
        } else {
            getLoginStatus();
        }
    }

}
