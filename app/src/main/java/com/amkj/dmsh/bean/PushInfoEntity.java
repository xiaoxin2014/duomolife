package com.amkj.dmsh.bean;

import com.amkj.dmsh.utils.gson.GsonUtils;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/4/14
 * version 3.1.1
 * class description:首次启动push信息
 */
public class PushInfoEntity {

    /**
     * currentTime : 2018-04-14 10:34:56
     * msg : 请求成功
     * appPushInfo : {"subTitle":"不错哦123","mainTitle":"不错哦123121","pushSecond":15,"id":1,"userId":"44951"}
     * code : 01
     */

    private String currentTime;
    private String msg;
    private AppPushInfoBean appPushInfo;
    private String code;

    public static PushInfoEntity objectFromData(String str) {

        return GsonUtils.fromJson(str, PushInfoEntity.class);
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public AppPushInfoBean getAppPushInfo() {
        return appPushInfo;
    }

    public void setAppPushInfo(AppPushInfoBean appPushInfo) {
        this.appPushInfo = appPushInfo;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public static class AppPushInfoBean {
        /**
         * subTitle : 不错哦123
         * mainTitle : 不错哦123121
         * pushSecond : 15
         * id : 1
         * userId : 44951
         */

        private String subTitle;
        private String mainTitle;
        private int pushSecond;
        private int id;
        private String userId;

        public static AppPushInfoBean objectFromData(String str) {

            return GsonUtils.fromJson(str, AppPushInfoBean.class);
        }

        public String getSubTitle() {
            return subTitle;
        }

        public void setSubTitle(String subTitle) {
            this.subTitle = subTitle;
        }

        public String getMainTitle() {
            return mainTitle;
        }

        public void setMainTitle(String mainTitle) {
            this.mainTitle = mainTitle;
        }

        public int getPushSecond() {
            return pushSecond;
        }

        public void setPushSecond(int pushSecond) {
            this.pushSecond = pushSecond;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }
    }
}
