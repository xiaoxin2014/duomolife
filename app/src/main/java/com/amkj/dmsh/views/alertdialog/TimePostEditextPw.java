package com.amkj.dmsh.views.alertdialog;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.bean.CommunalComment;
import com.amkj.dmsh.dao.CommentDao;
import com.amkj.dmsh.dominant.bean.PostCommentEntity.PostCommentBean;
import com.lxj.xpopup.core.BottomPopupView;

import org.greenrobot.eventbus.EventBus;

import androidx.annotation.NonNull;

import static com.amkj.dmsh.constant.ConstantMethod.dismissLoadhud;
import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.showLoadhud;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.COMMENT_GROUP_TYPE;
import static com.amkj.dmsh.constant.ConstantVariable.UPDATE_POST_COMMENT;

/**
 * Created by xiaoxin on 2020/10/10
 * Version:v4.8.0
 * ClassDescription :淘好货-种草帖子-全部评论-输入框弹窗
 */
public class TimePostEditextPw extends BottomPopupView {
    private BaseActivity mContext;
    private EditText mEmojiEditComment;
    private TextView mTvSendComment;
    private int mPostId;
    private int mAuthoUid;
    private final PostCommentBean mPostCommentBean;
    private CommentDao.OnSendCommentFinish mOnSendCommentFinish;

    public TimePostEditextPw(@NonNull BaseActivity context, int postId, int authoUid, PostCommentBean postCommentBean) {
        super(context);
        mContext = context;
        mPostId = postId;
        mAuthoUid = authoUid;
        mPostCommentBean = postCommentBean;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.layout_comment_edit;
    }

    @Override
    protected void onCreate() {
        findViewById(R.id.ll_input_comment).setVisibility(VISIBLE);
        mEmojiEditComment = findViewById(R.id.emoji_edit_comment);
        mTvSendComment = findViewById(R.id.tv_send_comment);
        if (mPostCommentBean != null) {
            mEmojiEditComment.setHint("回复" + mPostCommentBean.getNickname() + ":");
        } else {
            mEmojiEditComment.setHint("");
        }
        mTvSendComment.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断有内容调用接口
                String comment = mEmojiEditComment.getText().toString();
                if (!TextUtils.isEmpty(comment)) {
                    comment = mEmojiEditComment.getText().toString();
                    CommunalComment communalComment = new CommunalComment();
                    communalComment.setCommType(COMMENT_GROUP_TYPE);
                    communalComment.setContent(comment);
                    if (mPostCommentBean != null) {
                        communalComment.setIsReply(1);
                        communalComment.setReplyUserId(mPostCommentBean.getUid());
                        communalComment.setPid(mPostCommentBean.getId());
                        communalComment.setMainCommentId(mPostCommentBean.getMain_comment_id() > 0
                                ? mPostCommentBean.getMain_comment_id() : mPostCommentBean.getId());
                    }
                    communalComment.setObjId(mPostId);
                    communalComment.setUserId(userId);
                    communalComment.setToUid(mAuthoUid);
                    sendComment(communalComment);
                } else {
                    showToast("请正确输入内容");
                }
            }
        });
    }

    //发送评论
    private void sendComment(CommunalComment communalComment) {
        if (userId > 0) {
            mContext.loadHud.setCancellable(true);
            showLoadhud(mContext);
            mTvSendComment.setText("发送中…");
            mTvSendComment.setEnabled(false);
            CommentDao.setSendComment(mContext, communalComment, new CommentDao.OnSendCommentFinish() {
                @Override
                public void onSuccess() {
                    dismissLoadhud(mContext);
                    showToast(R.string.comment_send_success);
                    EventBus.getDefault().post(new EventMessage(UPDATE_POST_COMMENT, mContext.getSimpleName()));
                    dismiss();
                }

                @Override
                public void onError() {
                    dismissLoadhud(mContext);
                    mTvSendComment.setText("发送");
                    mTvSendComment.setEnabled(true);
                }
            });
        } else {
            getLoginStatus(mContext);
        }
    }


    public String getComment() {
        return mEmojiEditComment.getText().toString();
    }

    public void setOnCommentListener(CommentDao.OnSendCommentFinish onSendCommentFinish) {
        mOnSendCommentFinish = onSendCommentFinish;

    }
}
