package com.amkj.dmsh.bean;

import com.amkj.dmsh.base.BaseTimeEntity;
import com.google.gson.annotations.SerializedName;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/12/10
 * version 3.1.9
 * class description:系统通知
 */
public class SysNotificationEntity extends BaseTimeEntity {

    /**
     * result : {"id":2,"content":"请到设置里打开","intervalDay":1}
     */

    @SerializedName("result")
    private SysNotificationBean sysNotificationBean;

    public SysNotificationBean getSysNotificationBean() {
        return sysNotificationBean;
    }

    public void setSysNotificationBean(SysNotificationBean sysNotificationBean) {
        this.sysNotificationBean = sysNotificationBean;
    }

    public static class SysNotificationBean {
        /**
         * id : 2
         * content : 请到设置里打开
         * intervalDay : 1
         */

        private String content;
        private int intervalDay;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getIntervalDay() {
            return intervalDay;
        }

        public void setIntervalDay(int intervalDay) {
            this.intervalDay = intervalDay;
        }
    }
}
