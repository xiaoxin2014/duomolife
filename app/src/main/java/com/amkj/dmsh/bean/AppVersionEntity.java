package com.amkj.dmsh.bean;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2016/11/28
 * version 3.1.2
 * class description:版本检测
 */

public class AppVersionEntity {

    /**
     * result : {"showPop":0,"device_type_id":2,"interval_seconds":604800,"context":1,"link":"www.baidu.com","updateTime":"2016-10-25 15:11:11","description":"多么生活更新啦！快去体验吧！","id":32,"version":"3.0.8","status":1}
     * msg : 请求成功
     * code : 01
     */

    @SerializedName("result")
    private AppVersionBean appVersionBean;
    private String msg;
    private String code;
    private String currentTime;

    public static AppVersionEntity objectFromData(String str) {

        return new Gson().fromJson(str, AppVersionEntity.class);
    }

    public AppVersionBean getAppVersionBean() {
        return appVersionBean;
    }

    public void setAppVersionBean(AppVersionBean appVersionBean) {
        this.appVersionBean = appVersionBean;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    public static class AppVersionBean {
        /**
         * showPop : 0
         * device_type_id : 2
         * interval_seconds : 604800
         * context : 1
         * link : www.baidu.com
         * updateTime : 2016-10-25 15:11:11
         * description : 多么生活更新啦！快去体验吧！
         * id : 32
         * version : 3.0.8
         * status : 1
         */
        @SerializedName("is_popup")
        private int showPop;
        private int device_type_id;
        private int interval_seconds;
        private int context;
        private String link;
        private String updateTime;
        private String description;
        private int id;
        private String version;
        private int status;
        private String lowestVersion;
        private String compel_version;
        private String compel_up_desc;

        public static AppVersionBean objectFromData(String str) {

            return new Gson().fromJson(str, AppVersionBean.class);
        }

        public String getCompel_version() {
            return compel_version;
        }

        public void setCompel_version(String compel_version) {
            this.compel_version = compel_version;
        }

        public String getCompel_up_desc() {
            return compel_up_desc;
        }

        public void setCompel_up_desc(String compel_up_desc) {
            this.compel_up_desc = compel_up_desc;
        }

        public int getShowPop() {
            return showPop;
        }

        public void setShowPop(int showPop) {
            this.showPop = showPop;
        }

        public int getDevice_type_id() {
            return device_type_id;
        }

        public void setDevice_type_id(int device_type_id) {
            this.device_type_id = device_type_id;
        }

        public int getInterval_seconds() {
            return interval_seconds;
        }

        public void setInterval_seconds(int interval_seconds) {
            this.interval_seconds = interval_seconds;
        }

        public String getLowestVersion() {
            return lowestVersion;
        }

        public void setLowestVersion(String lowestVersion) {
            this.lowestVersion = lowestVersion;
        }

        public int getContext() {
            return context;
        }

        public void setContext(int context) {
            this.context = context;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }
}
