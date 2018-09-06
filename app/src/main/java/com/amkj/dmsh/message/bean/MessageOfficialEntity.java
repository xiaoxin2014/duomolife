package com.amkj.dmsh.message.bean;

import com.amkj.dmsh.base.BaseEntity;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by atd48 on 2016/11/2.
 */
public class MessageOfficialEntity extends BaseEntity{

    /**
     * result : [{"cover_url":"http://img.domolife.cn/platform/iDyriNGHwh1497691483627.png","create_time":"2017-06-17 17:25:09","isRead":false,"description":"112123","id":13,"abstract":"MTEyMTIz","title":"sdasdf","status":2},{"cover_url":"http://img.domolife.cn/platform/wZmYNz3As71496241754671.png","create_time":"2017-05-31 16:29:50","isRead":false,"id":12,"title":"测试000","status":2},{"cover_url":"http://img.domolife.cn/platform/cBfNttp4sa1495183765314.png","create_time":"2017-05-19 16:56:02","isRead":false,"id":11,"title":"浪里个浪，520约吗？加班狗","status":2},{"cover_url":"http://img.domolife.cn/platform/3RnHDMc4bM1494574885140.png","create_time":"2017-05-12 15:41:31","isRead":false,"id":10,"title":"cs1","status":2},{"cover_url":"http://img.domolife.cn/platform/XsZQdzKmaC.jpg","create_time":"2017-04-21 11:07:53","isRead":false,"id":9,"title":"测试","status":2},{"cover_url":"http://img.domolife.cn/platform/cNJPQ2sREP.jpg","create_time":"2017-04-10 10:22:06","isRead":false,"id":8,"title":"测试","status":2},{"cover_url":"http://img.domolife.cn/platform/6cz52mZkcj.jpg","create_time":"2017-02-29 00:00:00","isRead":false,"id":7,"title":"测试消息","status":2},{"cover_url":"http://img.domolife.cn/platform/GAeFytm57R.jpg","create_time":"2017-02-28 15:48:39","isRead":false,"id":6,"title":"旧版APP用户积分补偿政策","status":2},{"cover_url":"http://img.domolife.cn/platform/BsaRdWTWXY.jpg","create_time":"2017-01-17 10:14:33","isRead":false,"id":5,"title":"停止发货通知","status":2},{"cover_url":"http://img.domolife.cn/platform/Z7NnHFQc3w.jpg","create_time":"2016-12-30 15:38:14","isRead":false,"id":4,"title":"你好2017！元旦放假通知","status":2}]
     * msg : 请求成功
     * code : 01
     */
    @SerializedName("result")
    private List<MessageOfficialBean> messageOfficialList;

    public List<MessageOfficialBean> getMessageOfficialList() {
        return messageOfficialList;
    }

    public void setMessageOfficialList(List<MessageOfficialBean> messageOfficialList) {
        this.messageOfficialList = messageOfficialList;
    }

    public static class MessageOfficialBean {
        /**
         * cover_url : http://img.domolife.cn/platform/iDyriNGHwh1497691483627.png
         * create_time : 2017-06-17 17:25:09
         * isRead : false
         * description : 112123
         * id : 13
         * abstract : MTEyMTIz
         * title : sdasdf
         * status : 2
         */

        private String cover_url;
        private String create_time;
        @SerializedName("isRead")
        private boolean read;
        private String description;
        private int id;
        @SerializedName("abstract")
        private String abstractX;
        private String title;
        private int object_type;
        private String ios_link;
        private String android_link;
        private int status;

        public String getIos_link() {
            return ios_link;
        }

        public void setIos_link(String ios_link) {
            this.ios_link = ios_link;
        }

        public int getObject_type() {
            return object_type;
        }

        public void setObject_type(int object_type) {
            this.object_type = object_type;
        }

        public String getAndroid_link() {
            return android_link;
        }

        public void setAndroid_link(String android_link) {
            this.android_link = android_link;
        }

        public String getCover_url() {
            return cover_url;
        }

        public void setCover_url(String cover_url) {
            this.cover_url = cover_url;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public boolean isRead() {
            return read;
        }

        public void setRead(boolean read) {
            this.read = read;
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

        public String getAbstractX() {
            return abstractX;
        }

        public void setAbstractX(String abstractX) {
            this.abstractX = abstractX;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }
}
