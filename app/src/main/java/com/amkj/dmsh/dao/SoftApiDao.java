package com.amkj.dmsh.dao;

import android.app.Activity;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.bean.RequestStatus;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.ConstantVariable;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.dominant.bean.PostCommentEntity;
import com.amkj.dmsh.find.bean.PostEntity.PostBean;
import com.amkj.dmsh.homepage.bean.VideoProductEntity;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.utils.gson.GsonUtils;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import static com.amkj.dmsh.constant.ConstantMethod.dismissLoadhud;
import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.getNumber;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.getStringsFormat;
import static com.amkj.dmsh.constant.ConstantMethod.showLoadhud;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.showToastRequestMsg;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.NEW_FANS;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.UPDATE_FOLLOW_STATUS;
import static com.amkj.dmsh.constant.Url.F_TOPIC_COLLECT;
import static com.amkj.dmsh.constant.Url.UPDATE_ATTENTION;

/**
 * Created by xiaoxin on 2019/7/13
 * Version:v4.1.0
 * ClassDescription :轻量级接口Dao类
 */
public class SoftApiDao {
    public interface FollowCompleteListener {
        void followComplete(boolean isFollow);
    }

    //关注用户
    public static void followUser(BaseActivity activity, int buid, TextView tvFollow, MultiItemEntity item) {
        followUser(activity, buid, tvFollow, item, null);
    }

    public static void followUser(BaseActivity activity, int buid, TextView tvFollow, MultiItemEntity item, FollowCompleteListener followCompleteListener) {
        if (userId > 0) {
            activity.loadHud.show();
            boolean isFollow = tvFollow.isSelected();
            Map<String, Object> params = new HashMap<>();
            params.put("fuid", userId);
            params.put("buid", buid);
            params.put("ftype", isFollow ? "cancel" : "add");
            NetLoadUtils.getNetInstance().loadNetDataPost(activity, UPDATE_ATTENTION, params, new NetLoadListenerHelper() {
                @Override
                public void onSuccess(String result) {
                    activity.loadHud.dismiss();
                    RequestStatus requestStatus = GsonUtils.fromJson(result, RequestStatus.class);
                    if (requestStatus != null) {
                        if (requestStatus.getCode().equals(SUCCESS_CODE)) {
                            if (item != null) {
                                tvFollow.setSelected(!isFollow);
                                tvFollow.setText(!isFollow ? "已关注" : (item.getItemType() == NEW_FANS ? "回粉" : "关注"));
                            } else {
                                EventBus.getDefault().post(new EventMessage(UPDATE_FOLLOW_STATUS, !isFollow));
                            }

                            if (followCompleteListener != null) {
                                followCompleteListener.followComplete(!isFollow);
                            }

                            showToast(!isFollow ? "已关注" : "已取消关注");
                        } else {
                            showToastRequestMsg(requestStatus);
                        }
                    }
                }

                @Override
                public void onNotNetOrException() {
                    activity.loadHud.dismiss();
                }
            });
        } else {
            getLoginStatus(activity);
        }
    }


    //关注话题
    public static void followTopic(BaseActivity activity, int topicId, TextView tvFollow) {
        if (userId > 0) {
            activity.loadHud.show();
            Map<String, Object> params = new HashMap<>();
            params.put("uid", userId);
            params.put("object_id", topicId);        //话题id
            params.put("type", "findtopic");
            NetLoadUtils.getNetInstance().loadNetDataPost(activity, F_TOPIC_COLLECT, params, new NetLoadListenerHelper() {
                @Override
                public void onSuccess(String result) {
                    activity.loadHud.dismiss();

                    RequestStatus requestStatus = GsonUtils.fromJson(result, RequestStatus.class);
                    if (requestStatus != null) {
                        if (requestStatus.getCode().equals(SUCCESS_CODE)) {
                            tvFollow.setSelected(!tvFollow.isSelected());
                            ConstantMethod.showToast(tvFollow.isSelected() ? "已关注" : "已取消关注");
                        } else {
                            showToastRequestMsg(requestStatus);
                        }
                    }
                }

                @Override
                public void onNotNetOrException() {
                    activity.loadHud.dismiss();
                }
            });
        } else {
            getLoginStatus(activity);
        }
    }

    //单个帖子点赞
    public static void favorPostDetail(Activity activity, String postId, TextView tvFavor) {
        if (userId > 0) {
            tvFavor.setSelected(!tvFavor.isSelected());
            String likeCount = getNumber(tvFavor.getText().toString().trim());
            int likeNum = Integer.parseInt(likeCount);
            tvFavor.setText(String.valueOf(tvFavor.isSelected()
                    ? likeNum + 1 : likeNum - 1 > 0 ? likeNum - 1 : "赞"));
            Map<String, Object> params = new HashMap<>();
            params.put("tuid", userId);
            params.put("id", postId);//帖子id
            params.put("favortype", "doc");
            NetLoadUtils.getNetInstance().loadNetDataPost(activity, Url.F_ARTICLE_DETAILS_FAVOR, params, null);
        } else {
            getLoginStatus(activity);
        }
    }

    //帖子列表点赞
    public static void favorPost(Activity activity, PostBean item, TextView tvFavor, String type) {
        if (userId > 0) {
            item.setIsFavor(!item.isFavor());
            item.setFavorNum(item.isFavor() ? item.getFavorNum() + 1 :
                    item.getFavorNum() - 1);
            tvFavor.setSelected(!tvFavor.isSelected());
            tvFavor.setText(getStrings(String.valueOf(item.getFavorNum() > 0 ? item.getFavorNum() : "赞")));
            Map<String, Object> params = new HashMap<>();
            params.put("tuid", userId);
            params.put("id", item.getId());
            params.put("favortype", type);
            NetLoadUtils.getNetInstance().loadNetDataPost(activity, Url.F_ARTICLE_DETAILS_FAVOR, params, null);
        } else {
            getLoginStatus(activity);
        }
    }

    //帖子收藏
    public static void CollectPost(BaseActivity activity, String postId, TextView tvCollect) {
        if (userId > 0) {
            showLoadhud(activity);
            Map<String, Object> params = new HashMap<>();
            params.put("uid", userId);
            params.put("object_id", postId);        //帖子id
            params.put("type", ConstantVariable.TYPE_C_ARTICLE);
            NetLoadUtils.getNetInstance().loadNetDataPost(activity, Url.F_ARTICLE_COLLECT, params, new NetLoadListenerHelper() {
                @Override
                public void onSuccess(String result) {
                    dismissLoadhud(activity);
                    RequestStatus requestStatus = GsonUtils.fromJson(result, RequestStatus.class);
                    if (requestStatus != null) {
                        if (requestStatus.getCode().equals(SUCCESS_CODE)) {
                            tvCollect.setSelected(!tvCollect.isSelected());
                            showToast(R.string.collect_success);
                        } else {
                            showToast(R.string.collect_failed);
                        }
                    }
                }

                @Override
                public void onNotNetOrException() {
                    activity.loadHud.dismiss();
                }
            });
        } else {
            getLoginStatus(activity);
        }
    }

    //商品收藏
    public static void collectGoods(Activity activity, int id, TextView tvCollect) {
        showLoadhud(activity);
        String url = Url.Q_SP_DETAIL_PRO_COLLECT;
        Map<String, Object> params = new HashMap<>();
        params.put("uid", userId);
        params.put("object_id", id);
        params.put("type", "goods");
        NetLoadUtils.getNetInstance().loadNetDataPost(activity, url, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                dismissLoadhud(activity);
                RequestStatus requestStatus = GsonUtils.fromJson(result, RequestStatus.class);
                if (requestStatus != null) {
                    if (requestStatus.getCode().equals(SUCCESS_CODE)) {
                        tvCollect.setSelected(requestStatus.isCollect());
                        showToast(
                                String.format(activity.getResources().getString(
                                        tvCollect.isSelected() ? R.string.collect_success : R.string.cancel_done), "商品", "收藏"));
                    }
                }
            }

            @Override
            public void onNotNetOrException() {
                dismissLoadhud(activity);
            }

            @Override
            public void onError(Throwable throwable) {
                showToast(R.string.collect_failed);
            }
        });
    }

    //视频收藏
    public static void collectVideo(Activity activity, VideoProductEntity.VideoProductBean videoProductBean, TextView tvCollect) {
        showLoadhud(activity);
        String url = Url.ADD_VIDEO_COLLECT;
        Map<String, Object> params = new HashMap<>();
        params.put("id", videoProductBean.getId());
        NetLoadUtils.getNetInstance().loadNetDataPost(activity, url, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                dismissLoadhud(activity);
                RequestStatus requestStatus = GsonUtils.fromJson(result, RequestStatus.class);
                if (requestStatus != null) {
                    if (requestStatus.getCode().equals(SUCCESS_CODE)) {
                        tvCollect.setSelected(!tvCollect.isSelected());
                        videoProductBean.setIsCollect(tvCollect.isSelected() ? "1" : "0");
                        showToast(
                                String.format(activity.getResources().getString(
                                        tvCollect.isSelected() ? R.string.collect_success : R.string.cancel_done), "视频", "收藏"));
                    }
                }
            }

            @Override
            public void onNotNetOrException() {
                dismissLoadhud(activity);
            }

            @Override
            public void onError(Throwable throwable) {
                showToast(R.string.collect_failed);
            }
        });
    }

    //文章评论点赞
    public static void favorComment(Activity activity, PostCommentEntity.PostCommentBean item, TextView tvFavor) {
        if (userId > 0) {
            item.setFavor(!item.isFavor());
            item.setLike_num(item.isFavor() ? item.getLike_num() + 1 : item.getLike_num() - 1);
            tvFavor.setSelected(!tvFavor.isSelected());
            tvFavor.setText(getStrings(String.valueOf(item.getLike_num() > 0 ? item.getLike_num() : "赞")));
            Map<String, Object> params = new HashMap<>();
            params.put("tuid", userId);
            params.put("id", item.getId());
            NetLoadUtils.getNetInstance().loadNetDataPost(activity, Url.FIND_AND_COMMENT_FAV, params, null);
        } else {
            getLoginStatus(activity);
        }
    }

    //报告点赞
    public static void favorReport(Activity activity, PostBean item, TextView tvLike) {
        if (userId > 0) {
            item.setIsFavor(!tvLike.isSelected());
            item.setFavorNum(item.isFavor() ? item.getFavorNum() + 1 : item.getFavorNum() - 1);
            tvLike.setSelected(item.isFavor());
            tvLike.setText(item.getFavorNum() > 0 ? String.valueOf(item.getFavorNum()) : "赞");
            Map<String, Object> params = new HashMap<>();
            params.put("orderId", item.getOrderId());
            NetLoadUtils.getNetInstance().loadNetDataPost(activity, Url.REPORT_FAVOR, params, null);
        } else {
            getLoginStatus(activity);
        }
    }

    // 报告收藏
    public static void collectReport(Activity activity, PostBean item, TextView tvCollect) {
        if (userId > 0) {
            showLoadhud(activity);
            Map<String, Object> params = new HashMap<>();
            params.put("orderId", item.getOrderId());
            NetLoadUtils.getNetInstance().loadNetDataPost(activity, Url.REPORT_COLLECT, params, new NetLoadListenerHelper() {
                @Override
                public void onSuccess(String result) {
                    dismissLoadhud(activity);
                    RequestStatus requestStatus = GsonUtils.fromJson(result, RequestStatus.class);
                    if (requestStatus != null) {
                        if (requestStatus.getCode().equals(SUCCESS_CODE)) {
                            item.setIsCollect(!tvCollect.isSelected());
                            tvCollect.setSelected(item.isCollect());
                        } else {
                            showToast(getStringsFormat(activity, R.string.collect_failed, "报告"));
                        }
                    }
                }

                @Override
                public void onNotNetOrException() {
                    dismissLoadhud(activity);
                }
            });
        } else {
            getLoginStatus(activity);
        }
    }


    //举报帖子或者评论
    public static void reportIllegal(Activity activity, int id, int type) {
        Map<String, Object> map = new HashMap<>();
        map.put("objectId", id);
        map.put("type", type);//1 帖子 2 帖子评论
        NetLoadUtils.getNetInstance().loadNetDataPost(activity, Url.REPORT_ILLEGAL, map, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                RequestStatus requestStatus = GsonUtils.fromJson(result, RequestStatus.class);
                if (requestStatus != null) {
                    if (SUCCESS_CODE.equals(requestStatus.getCode())) {
                        ConstantMethod.showToast("举报成功！");
                        EventBus.getDefault().post(new EventMessage(ConstantVariable.UPDATE_POST_COMMENT, activity.getClass().getSimpleName()));
                    } else {
                        ConstantMethod.showToast(requestStatus.getCode());
                    }
                }
            }
        });
    }
}
