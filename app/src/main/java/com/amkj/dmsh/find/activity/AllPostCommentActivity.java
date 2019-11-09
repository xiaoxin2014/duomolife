package com.amkj.dmsh.find.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.text.emoji.widget.EmojiEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.constant.CommunalComment;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.dominant.bean.PostCommentEntity;
import com.amkj.dmsh.dominant.bean.PostCommentEntity.PostCommentBean;
import com.amkj.dmsh.find.view.PostReplyPw;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.shopdetails.adapter.PostCommentAdapter;
import com.amkj.dmsh.utils.CommonUtils;
import com.amkj.dmsh.utils.KeyboardUtils;
import com.amkj.dmsh.utils.WindowUtils;
import com.google.gson.Gson;
import com.melnykov.fab.FloatingActionButton;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTouch;

import static android.view.View.GONE;
import static com.amkj.dmsh.constant.ConstantMethod.getIntegralFormat;
import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.COMMENT_TYPE;
import static com.amkj.dmsh.constant.ConstantVariable.DEFAULT_COMMENT_TOTAL_COUNT;
import static com.amkj.dmsh.constant.ConstantVariable.ERROR_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.TOTAL_COUNT_TEN;
import static com.amkj.dmsh.constant.ConstantVariable.UPDATE_POST_COMMENT;
import static com.amkj.dmsh.constant.Url.Q_DML_SEARCH_COMMENT;

/**
 * Created by xiaoxin on 2019/7/18
 * Version:v4.1.0
 * ClassDescription :发现-帖子详情-更多评论
 */
public class AllPostCommentActivity extends BaseActivity {
    @BindView(R.id.tv_header_shared)
    TextView header_shared;
    @BindView(R.id.tv_header_title)
    TextView tv_header_titleAll;
    @BindView(R.id.smart_communal_refresh)
    SmartRefreshLayout mSmartLayout;
    @BindView(R.id.communal_recycler)
    RecyclerView mRvComment;
    @BindView(R.id.download_btn_communal)
    FloatingActionButton mDownloadBtnCommunal;
    @BindView(R.id.emoji_edit_comment)
    EmojiEditText mEmojiEditComment;
    @BindView(R.id.tv_send_comment)
    TextView mTvSendComment;
    @BindView(R.id.ll_input_comment)
    LinearLayout mLlInputComment;
    @BindView(R.id.rel_article_img_bottom)
    RelativeLayout mRelArticleImgBottom;
    private List<PostCommentBean> mCommentList = new ArrayList<>();
    private int page = 1;
    private PostCommentEntity postCommentEntity;
    private int objId;
    private int uid;
    private float locationY;
    private PostCommentAdapter postCommentAdapter;
    private PostReplyPw mAlphaPw;

    @Override
    protected int getContentView() {
        return R.layout.activity_all_post_comment;
    }

    @Override
    protected void initViews() {
        if (getIntent() != null) {
            Intent intent = getIntent();
            objId = intent.getIntExtra("objId", 0);
            uid = intent.getIntExtra("toUid", 0);
        } else {
            showToast("数据有误，请重试");
            finish();
        }
        header_shared.setVisibility(View.GONE);
        tv_header_titleAll.setText("全部评价");
        mSmartLayout.setOnRefreshListener(refreshLayout -> {
            page = 1;
            loadData();
        });

        //初始化评论列表
        postCommentAdapter = new PostCommentAdapter(this, mCommentList, COMMENT_TYPE);
        mRvComment.setNestedScrollingEnabled(false);
        mRvComment.setLayoutManager(new LinearLayoutManager(this));
        mRvComment.setAdapter(postCommentAdapter);
        postCommentAdapter.setOnItemLongClickListener((adapter, view, position) -> {
            TextView tvContent = (TextView) adapter.getViewByPosition(mRvComment, position, R.id.tv_comment_content);
            PostCommentBean postCommentBean = (PostCommentBean) view.getTag();
            if (postCommentBean != null) {
                //点击弹窗（回复和举报）
                mAlphaPw = new PostReplyPw(getActivity(), postCommentBean.getId(), 2) {
                    @Override
                    public void onCommentClick() {
                        setPublishComment(postCommentBean);
                    }
                };

                mAlphaPw.showAsDropDown(tvContent, 0, 0);
            }
            return true;
        });

        postCommentAdapter.setOnItemClickListener((adapter, view, position) -> {
            PostCommentBean postCommentBean = (PostCommentBean) view.getTag();
            if (postCommentBean != null) {
                setPublishComment(postCommentBean);
            }
        });

        postCommentAdapter.setOnLoadMoreListener(() -> {
            page++;
            loadData();
        }, mRvComment);

    }

    @Override
    protected void loadData() {
        getComment(false);
    }


    /**
     * 获取帖子评论
     *
     * @param isComment 评论完成，刷新评论
     */
    private void getComment(boolean isComment) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", objId);
        params.put("currentPage", isComment ? 1 : page);
        if (userId > 0) {
            params.put("uid", userId);
        }
        params.put("showCount", isComment ? mCommentList.size() : TOTAL_COUNT_TEN);
        params.put("replyCurrentPage", 1);
        params.put("replyShowCount", DEFAULT_COMMENT_TOTAL_COUNT);
        params.put("comtype", COMMENT_TYPE);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, Q_DML_SEARCH_COMMENT, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                mSmartLayout.finishRefresh();
                if (page == 1 || isComment) {
                    mCommentList.clear();
                }
                Gson gson = new Gson();
                postCommentEntity = gson.fromJson(result, PostCommentEntity.class);
                if (postCommentEntity != null) {
                    List<PostCommentBean> commentList = postCommentEntity.getCommentList();
                    int commentSize = postCommentEntity.getCommentSize();
                    if (commentList != null && commentList.size() > 0) {
                        tv_header_titleAll.setText(getIntegralFormat(getActivity(), R.string.comment_count, commentSize));
                        mCommentList.addAll(commentList);
                        postCommentAdapter.loadMoreComplete();
                    } else if (ERROR_CODE.equals(postCommentEntity.getCode())) {
                        ConstantMethod.showToast(postCommentEntity.getMsg());
                        postCommentAdapter.loadMoreFail();
                    } else {
                        postCommentAdapter.loadMoreEnd();
                    }
                } else {
                    postCommentAdapter.loadMoreEnd();
                }

                postCommentAdapter.notifyDataSetChanged();
                NetLoadUtils.getNetInstance().showLoadSir(loadService, mCommentList, postCommentEntity);
            }

            @Override
            public void onNotNetOrException() {
                mSmartLayout.finishRefresh();
                NetLoadUtils.getNetInstance().showLoadSir(loadService, mCommentList, postCommentEntity);
            }
        });
    }

    private void setPublishComment(PostCommentBean postCommentBean) {
        if (postCommentEntity != null) {
            if (userId > 0) {
                commentViewVisible(View.VISIBLE, postCommentBean);
            } else {
                getLoginStatus(this);
            }
        }
    }

    private void commentViewVisible(int visibility, final PostCommentBean postCommentBean) {
        mLlInputComment.setVisibility(visibility);
        if (View.VISIBLE == visibility) {
            mEmojiEditComment.requestFocus();
            //弹出键盘
            if (postCommentBean != null) {
                mEmojiEditComment.setHint("回复" + postCommentBean.getNickname() + ":");
            } else {
                mEmojiEditComment.setHint("");
            }
            CommonUtils.showSoftInput(mEmojiEditComment.getContext(), mEmojiEditComment);
            mTvSendComment.setOnClickListener(v -> {
                //判断有内容调用接口
                String comment = mEmojiEditComment.getText().toString();
                if (!TextUtils.isEmpty(comment)) {
                    comment = mEmojiEditComment.getText().toString();
                    CommunalComment communalComment = new CommunalComment();
                    communalComment.setCommType(COMMENT_TYPE);
                    communalComment.setContent(comment);
                    if (postCommentBean != null) {
                        communalComment.setIsReply(1);
                        communalComment.setReplyUserId(postCommentBean.getUid());
                        communalComment.setPid(postCommentBean.getId());
                        communalComment.setMainCommentId(postCommentBean.getMain_comment_id() > 0
                                ? postCommentBean.getMain_comment_id() : postCommentBean.getId());
                    }
                    communalComment.setObjId(objId);
                    communalComment.setUserId(userId);
                    communalComment.setToUid(uid);
                    sendComment(communalComment);
                } else {
                    showToast(getActivity(), "请正确输入内容");
                }
            });
        } else if (GONE == visibility) {
            //隐藏键盘
            CommonUtils.hideSoftInput(mEmojiEditComment.getContext(), mEmojiEditComment);
        }
    }

    //发送评论
    private void sendComment(CommunalComment communalComment) {
        loadHud.setCancellable(true);
        loadHud.show();
        mTvSendComment.setText("发送中…");
        mTvSendComment.setEnabled(false);
        ConstantMethod constantMethod = new ConstantMethod();
        constantMethod.setOnSendCommentFinish(new ConstantMethod.OnSendCommentFinish() {
            @Override
            public void onSuccess() {
                loadHud.dismiss();
                mTvSendComment.setText("发送");
                mTvSendComment.setEnabled(true);
                showToast(getActivity(), R.string.comment_send_success);
                commentViewVisible(GONE, null);
                page = 1;
                getComment(true);
                mEmojiEditComment.setText("");
            }

            @Override
            public void onError() {
                loadHud.dismiss();
                mTvSendComment.setText("发送");
                mTvSendComment.setEnabled(true);
            }
        });
        constantMethod.setSendComment(this, communalComment);
    }

    @Override
    protected void postEventResult(@NonNull EventMessage message) {
        //子评论回复
        if ("replyChildComment".equals(message.type)) {
            PostCommentBean.ReplyCommListBean replyCommListBean = (PostCommentBean.ReplyCommListBean) message.result;
            PostCommentBean postCommentBean = new PostCommentBean();
            postCommentBean.setNickname(replyCommListBean.getNickname());
            postCommentBean.setUid(replyCommListBean.getUid());
            postCommentBean.setId(replyCommListBean.getId());
            postCommentBean.setMain_comment_id(replyCommListBean.getMain_comment_id());
            postCommentBean.setObj_id(replyCommListBean.getObj_id());
            setPublishComment(postCommentBean);
        } else if (UPDATE_POST_COMMENT.equals(message.type) && getSimpleName().equals(message.result)) {
            getComment(true);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        KeyboardUtils.unregisterSoftInputChangedListener(this);
        WindowUtils.closePw(mAlphaPw);
        WindowUtils.closePw(postCommentAdapter.getChildPostCommentPw());
    }

    /**
     * 点击编辑器外区域隐藏键盘 避免点击搜索完没有隐藏键盘
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
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

    @OnTouch(R.id.communal_recycler)
    boolean onTouch(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                locationY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float moveY = event.getY() - locationY;//y轴距离
                if (Math.abs(moveY) > 250 && mLlInputComment.getVisibility() == View.VISIBLE) {
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
    protected boolean isAddLoad() {
        return true;
    }

    @Override
    public View getLoadView() {
        return mSmartLayout;
    }

    @OnClick(R.id.tv_life_back)
    public void onViewClicked() {
        finish();
    }
}
