package com.amkj.dmsh.message.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/8/1
 * class description:请输入类描述
 */

public class MessageCommentEntity {

    /**
     * result : [{"obj_id":4066,"sex":0,"description":"大麦若叶大采购","content":"我从自行车自行车走","is_reply":1,"reply_uid":23327,"uid":23317,"path":"http://o81ak3dv2.bkt.clouddn.com/2016-07-18_578c85c10e133.jpg","is_at":0,"obj":"doc","to_uid":12327,"ctime":"2016-10-24 09:56:37","nickname2":"糖糖妞儿","id":2620,"nickname1":"13815430077","atList":[],"status":1},{"obj_id":4066,"sex":0,"description":"大麦若叶大采购","content":"我从自行车自行车走","is_reply":1,"reply_uid":23327,"uid":23317,"path":"http://o81ak3dv2.bkt.clouddn.com/2016-07-18_578c85c10e133.jpg","is_at":0,"obj":"doc","to_uid":12327,"ctime":"2016-10-24 09:54:55","nickname2":"糖糖妞儿","id":2619,"nickname1":"13815430077","atList":[],"status":1},{"obj_id":4066,"sex":0,"description":"大麦若叶大采购","content":"男子女子呢","is_reply":1,"reply_uid":23327,"uid":23317,"path":"http://o81ak3dv2.bkt.clouddn.com/2016-07-18_578c85c10e133.jpg","is_at":0,"obj":"doc","to_uid":12327,"ctime":"2016-10-24 09:50:40","nickname2":"糖糖妞儿","id":2617,"nickname1":"13815430077","atList":[],"status":1},{"obj_id":4055,"sex":2,"description":"皇后卸妆水","avatar":"http://wx.qlogo.cn/mmopen/ajNVdqHZLLDOJUQiaOur6JVpC2aGrMkxJ98HvGDwOSpxIQDRdM1dzVCdwEGcDy7zt09Qib82J7OvUfoBwfSxoyCg/0","content":"哇，我发现新大陆了。","is_reply":1,"reply_uid":23327,"uid":23328,"at_uid":"23328,111","path":"http://o81ak3dv2.bkt.clouddn.com/2016-07-18_578c762e49090.JPG","is_at":1,"obj":"doc","to_uid":13017,"ctime":"2016-09-14 15:16:27","nickname2":"糖糖妞儿","id":2507,"nickname1":"coco","atList":[{"uid":"23328","nickname":"coco"},{"uid":"111","nickname":"Ben"}],"status":1},{"obj_id":4055,"sex":2,"description":"皇后卸妆水","avatar":"http://wx.qlogo.cn/mmopen/Q3auHgzwzM6MiaI9898MpRnickZUtjQgHYiaQdJ2sfCHtbCTRIzoJrPIbicRzdeztQmF1YfXRNz4NtUzJtqyobjSDq2pgXRPj91kWEK58HDXia6o/0","content":"谢谢你的评论","is_reply":1,"reply_uid":23327,"uid":23326,"path":"http://o81ak3dv2.bkt.clouddn.com/2016-07-18_578c762e49090.JPG","is_at":0,"obj":"doc","to_uid":13017,"ctime":"2016-09-14 15:13:38","nickname2":"糖糖妞儿","id":2506,"nickname1":"薇伊","atList":[],"status":1}]
     * msg : 请求成功
     * code : 01
     */

    private String msg;
    private String code;
    @SerializedName("result")
    private List<MessageCommentBean> messageCommentList;

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

    public List<MessageCommentBean> getMessageCommentList() {
        return messageCommentList;
    }

    public void setMessageCommentList(List<MessageCommentBean> messageCommentList) {
        this.messageCommentList = messageCommentList;
    }

    public static class MessageCommentBean {
        /**
         * obj_id : 4066
         * sex : 0
         * description : 大麦若叶大采购
         * content : 我从自行车自行车走
         * is_reply : 1
         * reply_uid : 23327
         * uid : 23317
         * path : http://o81ak3dv2.bkt.clouddn.com/2016-07-18_578c85c10e133.jpg
         * is_at : 0
         * obj : doc
         * to_uid : 12327
         * ctime : 2016-10-24 09:56:37
         * nickname2 : 糖糖妞儿
         * id : 2620
         * nickname1 : 13815430077
         * atList : []
         * status : 1
         * avatar : http://wx.qlogo.cn/mmopen/ajNVdqHZLLDOJUQiaOur6JVpC2aGrMkxJ98HvGDwOSpxIQDRdM1dzVCdwEGcDy7zt09Qib82J7OvUfoBwfSxoyCg/0
         * at_uid : 23328,111
         */

        private int obj_id;
        private int sex;
        private String description;
        private String content;
        private int is_reply;
        private int reply_uid;
        private int uid;
        private String path;
        private int is_at;
        private String obj;
        private int to_uid;
        private String ctime;
        private String nickname2;
        private int id;
        private String nickname1;
        private int status;
        private String avatar;
        private String at_uid;
        private List<?> atList;
        private int mainCommentId;

        public int getMainCommentId() {
            return mainCommentId;
        }

        public void setMainCommentId(int mainCommentId) {
            this.mainCommentId = mainCommentId;
        }

        public int getObj_id() {
            return obj_id;
        }

        public void setObj_id(int obj_id) {
            this.obj_id = obj_id;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getIs_reply() {
            return is_reply;
        }

        public void setIs_reply(int is_reply) {
            this.is_reply = is_reply;
        }

        public int getReply_uid() {
            return reply_uid;
        }

        public void setReply_uid(int reply_uid) {
            this.reply_uid = reply_uid;
        }

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public int getIs_at() {
            return is_at;
        }

        public void setIs_at(int is_at) {
            this.is_at = is_at;
        }

        public String getObj() {
            return obj;
        }

        public void setObj(String obj) {
            this.obj = obj;
        }

        public int getTo_uid() {
            return to_uid;
        }

        public void setTo_uid(int to_uid) {
            this.to_uid = to_uid;
        }

        public String getCtime() {
            return ctime;
        }

        public void setCtime(String ctime) {
            this.ctime = ctime;
        }

        public String getNickname2() {
            return nickname2;
        }

        public void setNickname2(String nickname2) {
            this.nickname2 = nickname2;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getNickname1() {
            return nickname1;
        }

        public void setNickname1(String nickname1) {
            this.nickname1 = nickname1;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getAt_uid() {
            return at_uid;
        }

        public void setAt_uid(String at_uid) {
            this.at_uid = at_uid;
        }

        public List<?> getAtList() {
            return atList;
        }

        public void setAtList(List<?> atList) {
            this.atList = atList;
        }
    }
}
