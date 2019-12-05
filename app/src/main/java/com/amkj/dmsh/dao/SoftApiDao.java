package com.amkj.dmsh.dao;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.provider.Settings;
import android.text.TextUtils;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.bean.RequestStatus;
import com.amkj.dmsh.bean.SysNotificationEntity;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.ConstantVariable;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.dominant.bean.PostCommentEntity;
import com.amkj.dmsh.find.bean.BaseFavorBean;
import com.amkj.dmsh.find.bean.PostEntity.PostBean;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.shopdetails.bean.GoodsCommentEntity;
import com.amkj.dmsh.utils.alertdialog.AlertDialogHelper;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;
import static com.amkj.dmsh.constant.ConstantMethod.getDeviceAppNotificationStatus;
import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.getNumber;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.getStringsFormat;
import static com.amkj.dmsh.constant.ConstantMethod.isContextExisted;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.showToastRequestMsg;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.NEW_FANS;
import static com.amkj.dmsh.constant.ConstantVariable.PUSH_CHECK;
import static com.amkj.dmsh.constant.ConstantVariable.PUSH_CHECK_TIME;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.UPDATE_FOLLOW_STATUS;
import static com.amkj.dmsh.constant.Url.APP_SYS_NOTIFICATION;
import static com.amkj.dmsh.constant.Url.F_ARTICLE_COLLECT;
import static com.amkj.dmsh.constant.Url.F_ARTICLE_DETAILS_FAVOR;
import static com.amkj.dmsh.constant.Url.F_TOPIC_COLLECT;
import static com.amkj.dmsh.constant.Url.SHOP_EVA_LIKE;
import static com.amkj.dmsh.constant.Url.UPDATE_ATTENTION;
import static com.amkj.dmsh.utils.TimeUtils.isTimeDayEligibility;

/**
 * Created by xiaoxin on 2019/7/13
 * Version:v4.1.0
 * ClassDescription :轻量级接口Dao类
 */
public class SoftApiDao {

    //关注用户
    public static void followUser(BaseActivity activity, int uid, TextView tvFollow, BaseFavorBean item) {
        if (userId > 0) {
            activity.loadHud.show();
            boolean isFollow = tvFollow.isSelected();
            Map<String, Object> params = new HashMap<>();
            params.put("fuid", userId);
            params.put("buid", uid);
            params.put("ftype", isFollow ? "cancel" : "add");
            NetLoadUtils.getNetInstance().loadNetDataPost(activity, UPDATE_ATTENTION, params, new NetLoadListenerHelper() {
                @Override
                public void onSuccess(String result) {
                    activity.loadHud.dismiss();
                    RequestStatus requestStatus = new Gson().fromJson(result, RequestStatus.class);
                    if (requestStatus != null) {
                        if (requestStatus.getCode().equals(SUCCESS_CODE)) {
                            EventBus.getDefault().post(new EventMessage(UPDATE_FOLLOW_STATUS, !isFollow));
                            if (item != null) {
                                tvFollow.setSelected(!isFollow);
                                tvFollow.setText(!isFollow ? "已关注" : (item.getItemType() == NEW_FANS ? "回粉" : "关注"));
                            }
                            showToast(!isFollow ? "已关注" : "已取消关注");
                        } else {
                            showToastRequestMsg(activity, requestStatus);
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
                    Gson gson = new Gson();
                    RequestStatus requestStatus = gson.fromJson(result, RequestStatus.class);
                    if (requestStatus != null) {
                        if (requestStatus.getCode().equals(SUCCESS_CODE)) {
                            tvFollow.setSelected(!tvFollow.isSelected());
                            ConstantMethod.showToast(tvFollow.isSelected() ? "已关注" : "已取消关注");
                        } else {
                            showToastRequestMsg(activity, requestStatus);
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

    //帖子收藏
    public static void CollectPost(BaseActivity activity, int postId, TextView tvCollect) {
        if (userId > 0) {
            activity.loadHud.show();
            Map<String, Object> params = new HashMap<>();
            params.put("uid", userId);
            params.put("object_id", postId);        //帖子id
            params.put("type", ConstantVariable.TYPE_C_ARTICLE);
            NetLoadUtils.getNetInstance().loadNetDataPost(activity, Url.F_ARTICLE_COLLECT, params, new NetLoadListenerHelper() {
                @Override
                public void onSuccess(String result) {
                    activity.loadHud.dismiss();
                    Gson gson = new Gson();
                    RequestStatus requestStatus = gson.fromJson(result, RequestStatus.class);
                    if (requestStatus != null) {
                        if (requestStatus.getCode().equals(SUCCESS_CODE)) {
                            tvCollect.setSelected(!tvCollect.isSelected());
                        } else {
                            showToast(activity, String.format(activity.getResources().getString(R.string.collect_failed), "文章"));
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

    //文章点赞
    public static void favorArtical(BaseActivity activity, String artId, TextView tvLike) {
        if (userId > 0) {
            Map<String, Object> params = new HashMap<>();
            params.put("tuid", userId);
            params.put("id", artId);
            params.put("favortype", "doc");
            tvLike.setSelected(!tvLike.isSelected());
            String likeCount = getNumber(tvLike.getText().toString().trim());
            int likeNum = Integer.parseInt(likeCount);
            tvLike.setText(String.valueOf(tvLike.isSelected()
                    ? likeNum + 1 : likeNum - 1 > 0 ? likeNum - 1 : "赞"));
            NetLoadUtils.getNetInstance().loadNetDataPost(activity, F_ARTICLE_DETAILS_FAVOR, params, null);
        } else {
            getLoginStatus(activity);
        }
    }

    //    文章收藏
    public static void collectArtical(BaseActivity activity, String artId, TextView tvCollect) {
        if (userId > 0) {
            activity.loadHud.show();
            Map<String, Object> params = new HashMap<>();
            //用户id
            params.put("uid", userId);
            //文章id
            params.put("object_id", artId);
            params.put("type", ConstantVariable.TYPE_C_ARTICLE);
            NetLoadUtils.getNetInstance().loadNetDataPost(activity, F_ARTICLE_COLLECT, params, new NetLoadListenerHelper() {
                @Override
                public void onSuccess(String result) {
                    activity.loadHud.dismiss();
                    Gson gson = new Gson();
                    RequestStatus requestStatus = gson.fromJson(result, RequestStatus.class);
                    if (requestStatus != null) {
                        if (requestStatus.getCode().equals(SUCCESS_CODE)) {
                            tvCollect.setSelected(!tvCollect.isSelected());
                        } else {
                            showToast(activity, getStringsFormat(activity, R.string.collect_failed, "文章"));
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

    //帖子详情点赞
    public static void favorPostDetail(Activity activity, int postId, TextView tvFavor) {
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
    public static void favorPost(Activity activity, PostBean item, TextView tvFavor) {
        if (userId > 0) {
            item.setIsFavor(!item.isFavor());
            item.setFavorNum(item.isFavor() ? item.getFavorNum() + 1 :
                    item.getFavorNum() - 1);
            tvFavor.setSelected(!tvFavor.isSelected());
            tvFavor.setText(getStrings(String.valueOf(item.getFavorNum() > 0 ? item.getFavorNum() : "赞")));
            Map<String, Object> params = new HashMap<>();
            params.put("tuid", userId);
            params.put("id", item.getId());            //帖子id
            params.put("favortype", "doc");
            NetLoadUtils.getNetInstance().loadNetDataPost(activity, Url.F_ARTICLE_DETAILS_FAVOR, params, null);
        } else {
            getLoginStatus(activity);
        }
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

    //文章评论点赞
    public static void favorGoodsComment(Activity activity, GoodsCommentEntity.GoodsCommentBean goodsCommentBean, TextView tvLike) {
        if (userId > 0) {
            Map<String, Object> params = new HashMap<>();
            params.put("id", goodsCommentBean.getId());
            params.put("uid", userId);
            NetLoadUtils.getNetInstance().loadNetDataPost(activity, SHOP_EVA_LIKE, params, null);
            goodsCommentBean.setFavor(!goodsCommentBean.isFavor());
            tvLike.setSelected(!tvLike.isSelected());
            tvLike.setText(ConstantMethod.getNumCount(tvLike.isSelected(), goodsCommentBean.isFavor(), goodsCommentBean.getLikeNum(), "赞"));
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
                RequestStatus requestStatus = new Gson().fromJson(result, RequestStatus.class);
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


    //检查推送权限
    public static void checkPushPermission(Activity activity) {
        if (isContextExisted(activity) && !getDeviceAppNotificationStatus(activity)) {
            NetLoadUtils.getNetInstance().loadNetDataPost(activity, APP_SYS_NOTIFICATION, new NetLoadListenerHelper() {
                @Override
                public void onSuccess(String result) {
                    Gson gson = new Gson();
                    SysNotificationEntity sysNotificationEntity = gson.fromJson(result, SysNotificationEntity.class);
                    SharedPreferences preferences = activity.getSharedPreferences(PUSH_CHECK, MODE_PRIVATE);
                    String pushCheckTime = preferences.getString(PUSH_CHECK_TIME, "");
                    SharedPreferences.Editor edit = preferences.edit();
                    if (sysNotificationEntity != null && sysNotificationEntity.getSysNotificationBean() != null &&
                            SUCCESS_CODE.equals(sysNotificationEntity.getCode())) {
                        SysNotificationEntity.SysNotificationBean sysNotificationBean = sysNotificationEntity.getSysNotificationBean();
                        if (TextUtils.isEmpty(pushCheckTime) ||
                                isTimeDayEligibility(pushCheckTime, sysNotificationEntity.getCurrentTime(), sysNotificationBean.getIntervalDay())) {
                            edit.putString(PUSH_CHECK_TIME, sysNotificationEntity.getCurrentTime());
                            edit.apply();
                            AlertDialogHelper alertDialogHelper = new AlertDialogHelper(activity);
                            alertDialogHelper.setAlertListener(new AlertDialogHelper.AlertConfirmCancelListener() {
                                @Override
                                public void confirm() {
                                    // 根据isOpened结果，判断是否需要提醒用户跳转AppInfo页面，去打开App通知权限
                                    Intent intent = new Intent();
                                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
                                    intent.setData(uri);
                                    activity.startActivity(intent);
                                    alertDialogHelper.dismiss();
                                }

                                @Override
                                public void cancel() {
                                    alertDialogHelper.dismiss();
                                }
                            });
                            alertDialogHelper.setTitle("通知提示")
                                    .setMsg(TextUtils.isEmpty(sysNotificationBean.getContent()) ? "“多么生活”想给您发送通知,方便我们更好的为您服务，限时秒杀不再错过。" :
                                            sysNotificationBean.getContent())
                                    .setSingleButton(true)
                                    .setConfirmText("去设置");
                            alertDialogHelper.show();
                        }
                    } else {
                        edit.clear().apply();
                    }
                }
            });
        }
    }
}
