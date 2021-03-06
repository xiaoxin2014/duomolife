package com.amkj.dmsh.dominant.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.bean.CommunalComment;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.dao.CommentDao;
import com.amkj.dmsh.dominant.adapter.ArticleCommentDetailAdapter;
import com.amkj.dmsh.dominant.bean.CommentDetailEntity;
import com.amkj.dmsh.dominant.bean.PostCommentEntity;
import com.amkj.dmsh.dominant.bean.PostCommentEntity.PostCommentBean;
import com.amkj.dmsh.dominant.bean.PostCommentEntity.PostCommentBean.ReplyCommListBean;
import com.amkj.dmsh.find.view.PostReplyPw;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.user.activity.UserPagerActivity;
import com.amkj.dmsh.utils.CommonUtils;
import com.amkj.dmsh.utils.KeyboardUtils;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.amkj.dmsh.utils.gson.GsonUtils;
import com.melnykov.fab.FloatingActionButton;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;
import androidx.emoji.widget.EmojiEditText;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import butterknife.OnTouch;

import static android.view.View.GONE;
import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.getStringChangeIntegers;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.DEFAULT_COMMENT_TOTAL_COUNT;
import static com.amkj.dmsh.constant.ConstantVariable.ERROR_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.IS_LOGIN_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.UPDATE_POST_COMMENT;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/7/21
 * class description:????????????
 */
public class CommentDetailsActivity extends BaseActivity {
    @BindView(R.id.smart_communal_refresh)
    SmartRefreshLayout smart_communal_refresh;
    @BindView(R.id.communal_recycler)
    RecyclerView communal_recycler;
    //    ???????????????
    @BindView(R.id.download_btn_communal)
    public FloatingActionButton download_btn_communal;
    @BindView(R.id.ll_input_comment)
    public LinearLayout ll_input_comment;
    @BindView(R.id.emoji_edit_comment)
    EmojiEditText emoji_edit_comment;
    @BindView(R.id.tv_send_comment)
    TextView tv_send_comment;
    @BindView(R.id.tv_header_title)
    TextView tv_header_titleAll;
    @BindView(R.id.tv_header_shared)
    TextView tv_header_shared;
    private int page = 1;
    private int scrollY;
    //    ??????????????????
    private float locationY;
    private ArticleCommentDetailAdapter articleCommentAdapter;
    private List<ReplyCommListBean> commentDetailList = new ArrayList<>();
    private CommentHeaderView commentHeaderView;
    private PostCommentEntity.PostCommentBean commentDetailBean;
    private String commentId;
    private String objId;
    private String commentType;
    private CommentDetailEntity commentDetailEntity;
    private PostReplyPw mAlphaPw;

    @Override
    protected int getContentView() {
        return R.layout.activity_comment_details;
    }

    @Override
    protected void initViews() {
        tv_header_titleAll.setText("??????");
        tv_header_shared.setVisibility(GONE);
        Intent intent = getIntent();
        commentId = intent.getStringExtra("commentId");
        objId = intent.getStringExtra("objId");
        commentType = intent.getStringExtra("commentType");
        communal_recycler.setLayoutManager(new LinearLayoutManager(CommentDetailsActivity.this));
        articleCommentAdapter = new ArticleCommentDetailAdapter(CommentDetailsActivity.this, commentDetailList);
        View commentView = LayoutInflater.from(CommentDetailsActivity.this).inflate(R.layout.layout_comment_detail_head, (ViewGroup) communal_recycler.getParent(), false);
        commentHeaderView = new CommentHeaderView();
        ButterKnife.bind(commentHeaderView, commentView);
        articleCommentAdapter.addHeaderView(commentView);
        communal_recycler.setAdapter(articleCommentAdapter);
        articleCommentAdapter.setOnLoadMoreListener(() -> {
            page++;
            getCommentData();
        }, communal_recycler);

        articleCommentAdapter.setOnItemLongClickListener((adapter, view, position) -> {
            TextView tvContent = (TextView) adapter.getViewByPosition(communal_recycler, position + adapter.getHeaderLayoutCount(), R.id.tv_comm_comment_inner_content);
            ReplyCommListBean replyCommBean = (ReplyCommListBean) view.getTag();
            if (replyCommBean != null) {
                //?????????????????????????????????
                mAlphaPw = new PostReplyPw(getActivity(), replyCommBean.getObj_id()) {
                    @Override
                    public void onCommentClick() {
                        //????????????
                        if (userId > 0) {
                            if (View.VISIBLE == ll_input_comment.getVisibility()) {
                                commentViewVisible(View.GONE, null);
                            } else {
                                commentViewVisible(View.VISIBLE, replyCommBean);
                            }
                        } else {
                            getLoginStatus(CommentDetailsActivity.this);
                        }
                    }
                };

                mAlphaPw.showAsDropDown(tvContent, 0, 0);
            }
            return true;
        });

        articleCommentAdapter.setOnItemClickListener((adapter, view, position) -> {
            ReplyCommListBean replyCommBean = (ReplyCommListBean) view.getTag();
            if (replyCommBean != null) {
                //????????????
                if (userId > 0) {
                    if (View.VISIBLE == ll_input_comment.getVisibility()) {
                        commentViewVisible(View.GONE, null);
                    } else {
                        commentViewVisible(View.VISIBLE, replyCommBean);
                    }
                } else {
                    getLoginStatus(CommentDetailsActivity.this);
                }
            }
        });

        smart_communal_refresh.setOnRefreshListener(refreshLayout -> {
            loadData();
        });

        setFloatingButton(download_btn_communal, communal_recycler);
        KeyboardUtils.registerSoftInputChangedListener(this, height -> {
            if (height == 0) {
                ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0).requestFocus();
            }
        });
    }


    @Override
    protected void loadData() {
        page = 1;
        getCommentData();
    }

    private void getCommentData() {
        String url =  Url.Q_COMMENT_DETAILS;
        Map<String, Object> params = new HashMap<>();
        params.put("comment_id", commentId);
        params.put("currentPage", page);
        params.put("showCount", DEFAULT_COMMENT_TOTAL_COUNT);
        params.put("comtype", commentType);
        params.put("obj_id", objId);
        if (userId > 0) {
            params.put("uid", userId);
        }
        NetLoadUtils.getNetInstance().loadNetDataPost(CommentDetailsActivity.this, url
                , params, new NetLoadListenerHelper() {
                    @Override
                    public void onSuccess(String result) {
                        smart_communal_refresh.finishRefresh();
                        articleCommentAdapter.loadMoreComplete();
                        if (page == 1) {
                            commentDetailList.clear();
                        }

                        commentDetailEntity = GsonUtils.fromJson(result, CommentDetailEntity.class);
                        if (commentDetailEntity != null && commentDetailEntity.getCommentDetailBean() != null) {
                            commentDetailBean = commentDetailEntity.getCommentDetailBean();
                            List<ReplyCommListBean> replyCommList = commentDetailBean.getReplyCommList();
                            if (replyCommList != null && replyCommList.size() > 0 ) {
                                setCommentData(commentDetailBean);
                            } else if (ERROR_CODE.equals(commentDetailEntity.getCode())) {
                                showToast( commentDetailEntity.getMsg());
                                articleCommentAdapter.loadMoreFail();
                            } else {
                                articleCommentAdapter.loadMoreEnd();
                            }
                        }else {
                            articleCommentAdapter.loadMoreEnd();
                        }
                        articleCommentAdapter.notifyDataSetChanged();
                        NetLoadUtils.getNetInstance().showLoadSir(loadService, commentDetailList, commentDetailEntity);
                    }

                    @Override
                    public void onNotNetOrException() {
                        articleCommentAdapter.loadMoreFail();
                        smart_communal_refresh.finishRefresh();
                        NetLoadUtils.getNetInstance().showLoadSir(loadService, commentDetailList, commentDetailEntity);
                    }
                });
    }

    private void setCommentData(PostCommentBean commentDetailBean) {
        GlideImageLoaderUtil.loadHeaderImg(CommentDetailsActivity.this, commentHeaderView.civ_comm_comment_avatar, commentDetailBean.getAvatar());
        commentHeaderView.tv_comm_comment_nick_name.setText(getStrings(commentDetailBean.getNickname()));
        commentHeaderView.tv_comm_comment_content.setText(getStrings(commentDetailBean.getContent()));
        commentHeaderView.tv_comm_comment_time.setText(getStrings(commentDetailBean.getCtime()));
        commentHeaderView.tv_comm_comment_like.setSelected(commentDetailBean.isFavor());
        commentHeaderView.tv_comm_comment_like.setText(commentDetailBean.getLike_num() > 0 ? String.valueOf(commentDetailBean.getLike_num()) : "???");
        commentDetailList.addAll(commentDetailBean.getReplyCommList());
    }

    private void setCommentLike(int id) {
        String url =  Url.FIND_AND_COMMENT_FAV;
        Map<String, Object> params = new HashMap<>();
        //??????id
        params.put("tuid", userId);
        //??????id
        params.put("id", id);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, url, params, null);
    }

    private void commentViewVisible(int visibility, final ReplyCommListBean replyCommBean) {
        ll_input_comment.setVisibility(visibility);
        if (View.VISIBLE == visibility) {
            emoji_edit_comment.requestFocus();
            //????????????
            if (replyCommBean != null) {
                emoji_edit_comment.setHint("??????" + replyCommBean.getNickname() + ":");
            } else {
                emoji_edit_comment.setHint(getString(R.string.comment_article_hint));
            }
            CommonUtils.showSoftInput(emoji_edit_comment.getContext(), emoji_edit_comment);
            tv_send_comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //???????????????????????????
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
                        communalComment.setUserId(userId);
                        communalComment.setToUid(replyCommBean.getTo_uid());
                        sendComment(communalComment);
                    } else {
                        showToast("?????????????????????");
                    }
                }
            });
        } else if (View.GONE == visibility) {
            //????????????
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
                float moveY = event.getY() - locationY;//y?????????
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

    //????????????
    private void sendComment(CommunalComment communalComment) {
        loadHud.setCancellable(true);
        loadHud.show();
        tv_send_comment.setText("????????????");
        tv_send_comment.setEnabled(false);
        CommentDao.setSendComment(CommentDetailsActivity.this, communalComment, new CommentDao.OnSendCommentFinish() {
            @Override
            public void onSuccess() {
                loadHud.dismiss();
                tv_send_comment.setText("??????");
                tv_send_comment.setEnabled(true);
                commentViewVisible(View.GONE, null);
                showToast( R.string.comment_article_send_success);
                page = 1;
                getCommentData();
                emoji_edit_comment.setText("");
            }

            @Override
            public void onError() {
                loadHud.dismiss();
                tv_send_comment.setText("??????");
                tv_send_comment.setEnabled(true);
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


        @OnClick(R.id.tv_comm_comment_like)
        void likeComment() {
            if (userId > 0) {
                tv_comm_comment_like.setSelected(!tv_comm_comment_like.isSelected());
                String likeCount = getNumber(tv_comm_comment_like.getText().toString().trim());
                int likeNum = Integer.parseInt(likeCount);
                tv_comm_comment_like.setText(String.valueOf(tv_comm_comment_like.isSelected()
                        ? likeNum + 1 : likeNum - 1 > 0 ? likeNum - 1 : "???"));
                setCommentLike(commentDetailBean.getId());
            } else {
                getLoginStatus(CommentDetailsActivity.this);
            }
        }

        @OnClick(R.id.civ_comm_comment_avatar)
        void clickAvatar() {
            if (commentDetailBean != null) {
                skipUserUI(commentDetailBean.getUid());
            }
        }

        //????????????
        @OnClick(R.id.rl_head)
        void reply(View view) {
            if (userId > 0) {
                setReceiverComment();
            } else {
                getLoginStatus(CommentDetailsActivity.this);
            }
        }

        //????????????
        @OnLongClick(R.id.rl_head)
        boolean ReplyAndReport(View view) {
            //?????????????????????????????????
            mAlphaPw = new PostReplyPw(getActivity(), getStringChangeIntegers(objId)) {
                @Override
                public void onCommentClick() {
                    if (userId > 0) {
                        setReceiverComment();
                    } else {
                        getLoginStatus(CommentDetailsActivity.this);
                    }
                }
            };

            mAlphaPw.showAsDropDown(tv_comm_comment_content, 0, 0);
            return true;
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
        if (userId > 0) {
            if (View.VISIBLE == ll_input_comment.getVisibility()) {
                commentViewVisible(View.GONE, null);
            } else {
                ReplyCommListBean replyCommBean = new ReplyCommListBean();
                replyCommBean.setReply_uid(commentDetailBean.getUid());
                replyCommBean.setNickname(commentDetailBean.getNickname());
                replyCommBean.setId(commentDetailBean.getId());
                replyCommBean.setObj_id(commentDetailBean.getObj_id());
                replyCommBean.setIsFavor(commentDetailBean.isFavor());
                replyCommBean.setTo_uid(commentDetailBean.getTo_uid());
                commentViewVisible(View.VISIBLE, replyCommBean);
            }
        } else {
            getLoginStatus(CommentDetailsActivity.this);
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

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    @Override
    public View getLoadView() {
        return smart_communal_refresh;
    }


    @Override
    protected void postEventResult(@NonNull EventMessage message) {
        super.postEventResult(message);
        if (UPDATE_POST_COMMENT.equals(message.type) && getSimpleName().equals(message.result)) {
            loadData();
        }
    }
}
