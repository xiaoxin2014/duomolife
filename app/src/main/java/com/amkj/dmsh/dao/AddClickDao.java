package com.amkj.dmsh.dao;

import android.app.Activity;
import android.text.TextUtils;

import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.ConstantVariable;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.message.bean.MessageNotifyEntity.MessageNotifyBean;
import com.amkj.dmsh.network.NetLoadUtils;

import java.util.HashMap;
import java.util.Map;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.setSkipPath;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.Url.ADD_CLICK_VIDEO;
import static com.amkj.dmsh.constant.Url.SAVE_DOCUMENT_DATA;
import static com.amkj.dmsh.constant.Url.TOTAL_ACTIVITY_MSG_COUNT;
import static com.amkj.dmsh.constant.Url.TOTAL_AD_COUNT;
import static com.amkj.dmsh.constant.Url.TOTAL_DYNAMIC_COUNT;
import static com.amkj.dmsh.constant.Url.TOTAL_MYDEFINEDICON_COUNT;
import static com.amkj.dmsh.constant.Url.TOTAL_NOTIFY_MSG_COUNT;

/**
 * Created by xiaoxin on 2019/11/22
 * Version:v4.3.7
 * ClassDescription :点击统计相关接口Dao类
 */
public class AddClickDao {

    //    文章分享统计
    public static void addArticleShareCount(Activity activity, int articleId) {
        String url = Url.ARTICLE_SHARE_COUNT;
        Map<String, Object> params = new HashMap<>();
        //回复文章或帖子
        params.put("id", articleId);
        NetLoadUtils.getNetInstance().loadNetDataPost(activity, url, params, null);
    }

    //      统计文章点击商品
    public static void totalProNum(Activity activity, int productId, String artId) {
        String url = Url.TOTAL_PRO_NUM;
        Map<String, Object> params = new HashMap<>();
        //回复文章或帖子
        params.put("product_id", productId);
        params.put("doc_id", artId);
        NetLoadUtils.getNetInstance().loadNetDataPost(activity, url, params, null);
    }

    //      统计福利社点击商品
    public static void totalWelfareProNum(Activity activity, int productId, String topId) {
        String url = Url.TOTAL_WELFARE_PRO_NUM;
        Map<String, Object> params = new HashMap<>();
        //回复文章或帖子
        params.put("productId", productId);
        params.put("topId", topId);
        NetLoadUtils.getNetInstance().loadNetDataPost(activity, url, params, null);
    }

    //      统计官方通知点击商品
    public static void totalOfficialProNum(Activity activity, int productId, String officialId) {
        String url = Url.TOTAL_OFFICIAL_PRO_NUM;
        Map<String, Object> params = new HashMap<>();
        //回复文章或帖子
        params.put("productId", productId);
        params.put("cId", officialId);
        NetLoadUtils.getNetInstance().loadNetDataPost(activity, url, params, null);
    }

    //    统计广告点击
    public static void adClickTotal(Activity activity, String androidLink, int adId, boolean addSource) {
        if (!TextUtils.isEmpty(androidLink)) {
            setSkipPath(activity, androidLink + (addSource ? ((androidLink.contains("?") ? "&" : "?") +
                    "sourceType=" + ConstantVariable.AD + "&sourceId=" + adId) : ""), false);
        }

        if (adId > 0) {
            Map<String, Object> params = new HashMap<>();
            //回复文章或帖子
            params.put("id", adId);
            NetLoadUtils.getNetInstance().loadNetDataPost(activity, TOTAL_AD_COUNT, params, null);
        }
    }

    //    统计营销弹窗点击点击
    public static void addMarketClick(Activity activity, String androidLink, int adId) {
        if (!TextUtils.isEmpty(androidLink)) {
            setSkipPath(activity, androidLink + (androidLink.contains("?") ? "&" : "?") +
                    "sourceType=" + ConstantVariable.POPUP + "&sourceId=" + adId, false);
        }

        if (adId > 0) {
            Map<String, Object> params = new HashMap<>();
            //回复文章或帖子
            params.put("id", adId);
            params.put("type", 1);
            NetLoadUtils.getNetInstance().loadNetDataPost(activity, TOTAL_AD_COUNT, params, null);
        }
    }

    //统计首页动态专区点击
    public static void addDynamicClick(Activity activity, String androidLink, int adId) {
        if (!TextUtils.isEmpty(androidLink)) {
            setSkipPath(activity, androidLink + (androidLink.contains("?") ? "&" : "?") +
                    "sourceType=" + ConstantVariable.DYNAMIC_AREA + "&sourceId=" + adId, false);
        }
        if (adId > 0) {
            Map<String, Object> params = new HashMap<>();
            params.put("id", adId);
            NetLoadUtils.getNetInstance().loadNetDataPost(activity, TOTAL_DYNAMIC_COUNT, params, null);
        }
    }


    //统计JPush打开数目
    public static void clickTotalPush(String pushType, String id) {
        String url = Url.TOTAL_JPUSH_COUNT;
        Map<String, Object> params = new HashMap<>();
        params.put("type", pushType);
        if (!TextUtils.isEmpty(id)) {
            params.put("id", id);
        }
        NetLoadUtils.getNetInstance().loadNetDataPost(mAppContext, url, params, null);
    }


    //统计通知消息点击
    public static void addNotifyMsgClick(Activity activity, MessageNotifyBean messageNotifyBean) {
        int m_id = messageNotifyBean.getM_id();
        int noticeId = messageNotifyBean.getNotice_id();
        boolean isRead = messageNotifyBean.isRead();

        if (noticeId > 0 && !isRead) {
            Map<String, Object> params = new HashMap<>();
            params.put("id", m_id);
            params.put("noticeId", noticeId);
            NetLoadUtils.getNetInstance().loadNetDataPost(activity, TOTAL_NOTIFY_MSG_COUNT, params, null);
        }
    }

    //统计活动消息点击
    public static void addActivityMsgClick(Activity activity, int m_id) {
        if (m_id > 0) {
            Map<String, Object> params = new HashMap<>();
            params.put("id", m_id);
            if (userId > 0) {
                params.put("uid", userId);
            }
            NetLoadUtils.getNetInstance().loadNetDataPost(activity, TOTAL_ACTIVITY_MSG_COUNT, params, null);
        }
    }


    //统计我的十二宫格点击
    public static void addMyDefinedIconClick(Activity activity, int id) {
        if (id > 0) {
            Map<String, Object> params = new HashMap<>();
            params.put("id", id);
            NetLoadUtils.getNetInstance().loadNetDataPost(activity, TOTAL_MYDEFINEDICON_COUNT, params, null);
        }
    }

    /**
     * 统计淘好货帖子/帖子内商品点击量
     *
     * @param type 1帖子 2帖子内商品
     */
    public static void addTimePostClick(Activity activity, int id, int type) {
        if (id > 0) {
            Map<String, Object> params = new HashMap<>();
            if (type == 1) {
                params.put("docId", id);
            } else {
                params.put("productId", id);
            }
            NetLoadUtils.getNetInstance().loadNetDataPost(activity, SAVE_DOCUMENT_DATA, params, null);
        }
    }


    public static void addVideoClick(Activity activity, String id) {
        if (ConstantMethod.getStringChangeIntegers(id) > 0) {
            Map<String, Object> params = new HashMap<>();
            params.put("id", id);
            NetLoadUtils.getNetInstance().loadNetDataPost(activity, ADD_CLICK_VIDEO, params, null);
        }
    }

}
