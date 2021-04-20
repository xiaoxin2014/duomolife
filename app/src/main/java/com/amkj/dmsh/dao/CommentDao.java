package com.amkj.dmsh.dao;

import android.app.Activity;

import com.amkj.dmsh.R;
import com.amkj.dmsh.bean.CommunalComment;
import com.amkj.dmsh.bean.RequestStatus;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.utils.gson.GsonUtils;

import java.util.HashMap;
import java.util.Map;

import static com.amkj.dmsh.constant.ConstantMethod.dismissLoadhud;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.showToastRequestMsg;
import static com.amkj.dmsh.constant.ConstantVariable.COMMENT_DOC_TYPE;
import static com.amkj.dmsh.constant.ConstantVariable.COMMENT_GROUP_TYPE;
import static com.amkj.dmsh.constant.ConstantVariable.COMMENT_VIDEO_TYPE;
import static com.amkj.dmsh.constant.ConstantVariable.MES_ADVISE;
import static com.amkj.dmsh.constant.ConstantVariable.MES_FEEDBACK;
import static com.amkj.dmsh.constant.ConstantVariable.PRO_COMMENT;
import static com.amkj.dmsh.constant.ConstantVariable.PRO_TOPIC;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.TYPE_C_WELFARE;

/**
 * Created by xiaoxin on 2020/6/9
 * Version:v4.6.1
 * ClassDescription :评论相关Dao类
 */
public class CommentDao {

    public static void setSendComment(final Activity context, CommunalComment communalComment, OnSendCommentFinish onSendCommentFinish) {
        switch (communalComment.getCommType()) {
            case PRO_COMMENT:
                setGoodsComment(context, communalComment, onSendCommentFinish);
                break;
            case COMMENT_DOC_TYPE:
            case COMMENT_GROUP_TYPE:
            case COMMENT_VIDEO_TYPE:
            case PRO_TOPIC:
            case TYPE_C_WELFARE:
                setDocComment(context, communalComment, onSendCommentFinish);
                break;
            case MES_ADVISE:
                setAdviceData(context, communalComment, onSendCommentFinish);
                break;
            case MES_FEEDBACK:
                setFeedbackData(context, communalComment, onSendCommentFinish);
                break;
            default:
                dismissLoadhud(context);
                if (onSendCommentFinish != null) {
                    onSendCommentFinish.onError();
                }
                showToast(R.string.comment_send_failed);
                break;
        }

    }


    private static void setGoodsComment(final Activity context, CommunalComment communalComment, OnSendCommentFinish onSendCommentFinish) {
        String url = Url.GOODS_COMMENT;
        Map<String, Object> params = new HashMap<>();
        //回复评论
        params.put("uid", communalComment.getUserId());
        params.put("obj_id", communalComment.getObjId());
        params.put("content", communalComment.getContent());
        params.put("is_at", 0);
        params.put("com_type", "goods");
        if (communalComment.getIsReply() == 1) {
            params.put("is_reply", 1);
            params.put("reply_uid", communalComment.getReplyUserId());
            //评论id
            params.put("pid", communalComment.getPid());
        } else {
            //回复文章或帖子
            params.put("is_reply", 0);
        }
        NetLoadUtils.getNetInstance().loadNetDataPost(context, url, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {

                RequestStatus requestStatus = GsonUtils.fromJson(result, RequestStatus.class);
                if (requestStatus != null) {
                    if (requestStatus.getCode().equals(SUCCESS_CODE)) {
                        if (onSendCommentFinish != null) {
                            onSendCommentFinish.onSuccess();
                        }
                        showToast(R.string.comment_send_success);
                    } else {
                        if (onSendCommentFinish != null) {
                            onSendCommentFinish.onError();
                        }
                        showToastRequestMsg(requestStatus);
                    }
                }
            }

            @Override
            public void onNotNetOrException() {
                if (onSendCommentFinish != null) {
                    onSendCommentFinish.onError();
                }
            }

            @Override
            public void onError(Throwable throwable) {
                showToast(R.string.comment_send_failed);
            }
        });
    }

    private static void setDocComment(final Activity context, CommunalComment communalComment, OnSendCommentFinish onSendCommentFinish) {
        String url = Url.FIND_COMMENT;
        Map<String, Object> params = new HashMap<>();
        //回复文章或帖子
        params.put("is_reply", communalComment.getIsReply());
        params.put("uid", communalComment.getUserId());
        params.put("to_uid", communalComment.getToUid());
        params.put("obj_id", communalComment.getObjId());
        params.put("content", communalComment.getContent());
        params.put("com_type", communalComment.getCommType());
        if (communalComment.getIsReply() == 1) {
            params.put("reply_uid", communalComment.getReplyUserId() > 0 ? String.valueOf(communalComment.getReplyUserId()) : "");
            params.put("pid", communalComment.getPid() > 0 ? String.valueOf(communalComment.getPid()) : "");
            params.put("pid_type", communalComment.getCommType());
            params.put("main_comment_id", communalComment.getMainCommentId() > 0 ? String.valueOf(communalComment.getMainCommentId()) : "");
        }
        NetLoadUtils.getNetInstance().loadNetDataPost(context, url, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {

                RequestStatus requestStatus = GsonUtils.fromJson(result, RequestStatus.class);
                if (requestStatus != null) {
                    if (requestStatus.getCode().equals(SUCCESS_CODE)) {
                        if (onSendCommentFinish != null) {
                            onSendCommentFinish.onSuccess();
                        }
                    } else {
                        if (onSendCommentFinish != null) {
                            onSendCommentFinish.onError();
                        }
                        showToastRequestMsg(requestStatus);
                    }
                }
            }

            @Override
            public void onNotNetOrException() {
                if (onSendCommentFinish != null) {
                    onSendCommentFinish.onError();
                }
            }

            @Override
            public void onError(Throwable throwable) {
                showToast(R.string.comment_send_failed);
            }
        });
    }

    /**
     * 发送留言
     *
     * @param context
     * @param communalComment
     */
    private static void setAdviceData(final Activity context, CommunalComment communalComment, OnSendCommentFinish onSendCommentFinish) {
        String url = Url.SEARCH_LEAVE_MES;
        Map<String, Object> params = new HashMap<>();
        params.put("uid", communalComment.getUserId());
        params.put("content", getStrings(communalComment.getContent()));
        NetLoadUtils.getNetInstance().loadNetDataPost(context, url, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                dismissLoadhud(context);
                RequestStatus requestStatus = GsonUtils.fromJson(result, RequestStatus.class);
                if (requestStatus != null) {
                    if (requestStatus.getCode().equals(SUCCESS_CODE)) {
                        if (onSendCommentFinish != null) {
                            onSendCommentFinish.onSuccess();
                        }
                        showToast(R.string.Submit_Success);
                    } else {
                        if (onSendCommentFinish != null) {
                            onSendCommentFinish.onError();
                        }
                        showToast(requestStatus.getMsg() + ",请重新提交");
                    }
                }
            }

            @Override
            public void onNotNetOrException() {
                dismissLoadhud(context);
                if (onSendCommentFinish != null) {
                    onSendCommentFinish.onError();
                }
            }

            @Override
            public void onError(Throwable throwable) {
                showToast(R.string.commit_failed);
            }
        });
    }

    /**
     * 发送意见反馈
     *
     * @param context
     * @param communalComment
     */
    private static void setFeedbackData(final Activity context, CommunalComment communalComment, OnSendCommentFinish onSendCommentFinish) {
        String url = Url.MINE_FEEDBACK;
        Map<String, Object> params = new HashMap<>();
        params.put("uid", communalComment.getUserId());
        params.put("remark", getStrings(communalComment.getContent()));
        NetLoadUtils.getNetInstance().loadNetDataPost(context, url, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                dismissLoadhud(context);
                RequestStatus requestInfo = GsonUtils.fromJson(result, RequestStatus.class);
                if (requestInfo != null) {
                    if (requestInfo.getCode().equals(SUCCESS_CODE)) {
                        if (onSendCommentFinish != null) {
                            onSendCommentFinish.onSuccess();
                        }
                        showToast(R.string.Submit_Success);
                    } else {
                        if (onSendCommentFinish != null) {
                            onSendCommentFinish.onError();
                        }
                        showToastRequestMsg(requestInfo);
                    }
                }
            }

            @Override
            public void onNotNetOrException() {
                dismissLoadhud(context);
                if (onSendCommentFinish != null) {
                    onSendCommentFinish.onError();
                }
            }

            @Override
            public void onError(Throwable throwable) {
                showToast(R.string.commit_failed);
            }
        });
    }

    public interface OnSendCommentFinish {
        void onSuccess();

        void onError();
    }
}
