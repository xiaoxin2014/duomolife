package com.amkj.dmsh.qyservice;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.amkj.dmsh.mine.bean.SavePersonalInfoBean;
import com.qiyukf.unicorn.api.ConsultSource;
import com.qiyukf.unicorn.api.OnMessageItemClickListener;
import com.qiyukf.unicorn.api.ProductDetail;
import com.qiyukf.unicorn.api.StatusBarNotificationConfig;
import com.qiyukf.unicorn.api.Unicorn;
import com.qiyukf.unicorn.api.UnreadCountChangeListener;
import com.qiyukf.unicorn.api.YSFOptions;
import com.qiyukf.unicorn.api.YSFUserInfo;
import com.qiyukf.unicorn.api.lifecycle.SessionLifeCycleOptions;

import static com.amkj.dmsh.constant.ConstantMethod.getPersonalInfo;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.getVersionName;
import static com.amkj.dmsh.constant.ConstantMethod.setSkipPath;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/8/27
 * version 3.1.6
 * class description:七鱼客服
 */
public class QyServiceUtils {

    private static QyServiceUtils qyServiceUtils;
    private boolean isQyInit;
    private UnreadCountChangeListener listener;

    private QyServiceUtils() {
    }

    public static QyServiceUtils getQyInstance() {
        if (qyServiceUtils == null) {
            synchronized (QyServiceUtils.class) {
                if (qyServiceUtils == null) {
                    qyServiceUtils = new QyServiceUtils();
                }
            }
        }
        return qyServiceUtils;
    }

    /**
     * 初始化网易七鱼
     */
    public void initQyService(Context context) {
        String appKey = "ef251a87b903f9fd6938caafbdf0a9de";
        isQyInit = Unicorn.init(context, appKey, QyOptions(), new QYGlideImageLoader(context.getApplicationContext()));
    }

    private YSFOptions QyOptions() {
        YSFOptions options = new YSFOptions();
        StatusBarNotificationConfig statusBarNotificationConfig = new StatusBarNotificationConfig();
//        配置七鱼点击通知栏
        statusBarNotificationConfig.notificationEntrance = QyServiceNotifyReceiver.class;
        options.statusBarNotificationConfig = statusBarNotificationConfig;
        //        链接点击设置
        options.onMessageItemClickListener = new OnMessageItemClickListener() {
            @Override
            public void onURLClicked(Context context, String url) {
                setSkipPath(context,url, false);
            }
        };
        return options;
    }

    /**
     * 打开客服页面 默认
     */
    public void openQyServiceChat(Context context) {
        openQyServiceChat(context, null);
    }

    public void openQyServiceChat(Context context, String sourceTitle) {
        openQyServiceChat(context, sourceTitle, null);
    }

    /**
     * @param context     上下文
     * @param sourceTitle 聊天窗口的标题
     * @param sourceUrl   咨询的发起来源，包括发起咨询的url，title，描述信息等
     */
    public void openQyServiceChat(Context context, String sourceTitle, String sourceUrl) {
        openQyServiceChat(context, sourceTitle, sourceUrl, null);
    }

    public void openQyServiceChat(Context context, String sourceTitle, String sourceUrl, QyProductIndentInfo qyProductIndentInfo) {
        ConsultSource pageSource;
        if (!TextUtils.isEmpty(sourceTitle) || !TextUtils.isEmpty(sourceUrl)) {
            /**
             * 设置访客来源，标识访客是从哪个页面发起咨询的，用于客服了解用户是从什么页面进入。
             * 三个参数分别为：来源页面的url，来源页面标题，来源页面额外信息（保留字段，暂时无用）。
             * 设置来源后，在客服会话界面的"用户资料"栏的页面项，可以看到这里设置的值。
             */
            pageSource = new ConsultSource(sourceUrl, sourceTitle, "");
        } else {
            pageSource = new ConsultSource(null, null, null);
        }

        if (qyProductIndentInfo != null) {
            pageSource.productDetail = new ProductDetail.Builder()
                    .setPicture(qyProductIndentInfo.getPicUrl())
                    .setTitle(getStrings(qyProductIndentInfo.getTitle()))
                    .setUrl(qyProductIndentInfo.getUrl())
                    .setDesc(qyProductIndentInfo.getDesc())
                    .setNote(qyProductIndentInfo.getNote())
                    .setAlwaysSend(true)
                    .setUrl(getStrings(qyProductIndentInfo.getUrl()))
                    .setShow(1)
                    .setSendByUser(false)
                    .build();
        }

//        排队设置
        SessionLifeCycleOptions lifeCycleOptions = new SessionLifeCycleOptions();
        lifeCycleOptions.setCanCloseSession(true)
                .setCanQuitQueue(true)
                .setQuitQueuePrompt("是否确定退出排队？");
        pageSource.sessionLifeCycleOptions = lifeCycleOptions;
        /**
         * 请注意： 调用该接口前，应先检查Unicorn.isServiceAvailable()，
         * 如果返回为false，该接口不会有任何动作
         *
         * @param context 上下文
         * @param title   聊天窗口的标题
         * @param source  咨询的发起来源，包括发起咨询的url，title，描述信息等
         */
        if (isQyInit) {
            Unicorn.openServiceActivity(context, getStrings(sourceTitle), pageSource);
        } else {
            showToast(context, "客服信息错误");
        }
    }

    /**
     * 监听客服消息条数
     *
     * @param listener 未读消息监听
     */
    public void getServiceCount(UnreadCountChangeListener listener) {
        this.listener = listener;
        Unicorn.addUnreadCountChangeListener(listener, true);
    }

    /**
     * 获取客服未读消息
     *
     * @return
     */
    public int getServiceTotalCount() {
        return Unicorn.getUnreadCount();
    }

    /**
     * 取消客服消息通知
     */
    public void cancelServiceCount() {
        if (listener != null) {
            Unicorn.addUnreadCountChangeListener(listener, false);
        }
    }

    /**
     * 清除文件缓存，将删除SDK接收过的所有文件。<br>
     * 建议在工作线程中执行该操作。
     */
    public void clearQyCache() {
        Unicorn.clearCache();
    }

    /**
     * 设置用户信息
     */
    public void loginQyUserInfo(Context context) {
        if (userId > 0) {
            SavePersonalInfoBean personalInfo = getPersonalInfo(context);
            loginQyUserInfo(context, userId, personalInfo.getNickName(), personalInfo.getPhoneNum(), personalInfo.getAvatar());
        } else {
            Unicorn.logout();
        }
    }

    public void loginQyUserInfo(Context context, int userId, String nickName, String mobile, String avatar) {
        if (userId > 0) {
            String osVersion = Build.VERSION.RELEASE;
//        手机型号
            String mobileModel = Build.MODEL;
            YSFUserInfo userInfo = new YSFUserInfo();
            userInfo.userId = String.valueOf(userId);
            JSONArray array = new JSONArray();
            array.add(userInfoDataItem("real_name", getStrings(nickName), -1, null, null));
            array.add(userInfoDataItem("mobile_phone", getStrings(mobile), -1, null, null));
//            array.add(userInfoDataItem("email", email, -1,null, null)); // email
            array.add(userInfoDataItem("avatar", getStrings(avatar), -1, null, null));
            array.add(userInfoDataItem("system_version", osVersion, 0, "系统版本", null));
            array.add(userInfoDataItem("app_version", getVersionName(context), 1, "app版本", null));
            array.add(userInfoDataItem("mobile_model", mobileModel, 2, "手机型号", null));
            userInfo.data = array.toJSONString();
            Unicorn.setUserInfo(userInfo);
        } else {
            Unicorn.logout();
        }
    }

    private JSONObject userInfoDataItem(String key, Object value, int index, String label, String href) {
        JSONObject userInfo = new JSONObject();
        userInfo.put("key", key);
        userInfo.put("value", value);
        if (index >= 0) {
            userInfo.put("index", index);
        }
        if (!TextUtils.isEmpty(label)) {
            userInfo.put("label", label);
        }
        if (!TextUtils.isEmpty(href)) {
            userInfo.put("href", href);
        }
        return userInfo;
    }

    /**
     * 注销用户
     */
    public void logoutQyUser() {
        Unicorn.logout();
    }


}
