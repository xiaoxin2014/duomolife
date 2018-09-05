package com.amkj.dmsh.dominant.bean;

import com.amkj.dmsh.base.BaseEntity;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/8/3
 * class description:请输入类描述
 */

public class CommentDetailEntity extends BaseEntity{

    /**
     * result : {"like_num":1,"obj_id":9769,"isFavor":false,"avatar":"http://q.qlogo.cn/qqapp/1105138467/10A168AD00FC6BF32AD94988B337C848/100","replyCommList":[{"like_num":1,"obj_id":9769,"isFavor":false,"avatar":"http://img.domolife.cn/test/20161223092818.jpg","content":"在我的里面，点击私人助手联系","is_reply":1,"reply_uid":29436,"uid":113,"is_at":0,"to_uid":0,"nickname":"多么生活","ctime":"02-21","isReplyMain":true,"id":3138,"nickname1":"1376326****","atList":[]},{"like_num":0,"obj_id":9769,"isFavor":false,"avatar":"http://img.domolife.cn/test/20161223092818.jpg","content":"可以联系一下app的私人秘书给你处理下哈","is_reply":1,"reply_uid":29436,"uid":113,"is_at":0,"to_uid":0,"nickname":"多么生活","ctime":"02-23","isReplyMain":true,"id":3153,"nickname1":"1376326****","atList":[]},{"like_num":0,"obj_id":9769,"isFavor":false,"avatar":"http://img.domolife.cn/test/20161223092818.jpg","content":"好的，不好意思","is_reply":1,"reply_uid":29436,"uid":113,"is_at":0,"to_uid":0,"nickname":"多么生活","ctime":"02-24","isReplyMain":true,"id":3165,"nickname1":"1376326****","atList":[]},{"like_num":0,"obj_id":9769,"isFavor":false,"avatar":"http://q.qlogo.cn/qqapp/1105138467/605725B499344A11B43786FE9156EC32/100","content":"直接联系app的私人秘书给你处理哈","is_reply":1,"reply_uid":29436,"uid":24599,"is_at":1,"to_uid":1,"nickname":"｀繼續___:。","ctime":"02-25","isReplyMain":true,"id":3174,"nickname1":"1376326****","atList":[]}],"content":"想问一下买了东西想换货找哪位，在哪找","is_reply":0,"uid":29436,"is_at":1,"to_uid":1,"nickname":"1376326****","ctime":"02-18","id":3125,"atList":[]}
     * msg : 请求成功
     * code : 01
     */

    @SerializedName("result")
    private CommentDetailBean commentDetailBean;

    public CommentDetailBean getCommentDetailBean() {
        return commentDetailBean;
    }

    public void setCommentDetailBean(CommentDetailBean commentDetailBean) {
        this.commentDetailBean = commentDetailBean;
    }

    public static class CommentDetailBean {
        /**
         * like_num : 1
         * obj_id : 9769
         * isFavor : false
         * avatar : http://q.qlogo.cn/qqapp/1105138467/10A168AD00FC6BF32AD94988B337C848/100
         * replyCommList : [{"like_num":1,"obj_id":9769,"isFavor":false,"avatar":"http://img.domolife.cn/test/20161223092818.jpg","content":"在我的里面，点击私人助手联系","is_reply":1,"reply_uid":29436,"uid":113,"is_at":0,"to_uid":0,"nickname":"多么生活","ctime":"02-21","isReplyMain":true,"id":3138,"nickname1":"1376326****","atList":[]},{"like_num":0,"obj_id":9769,"isFavor":false,"avatar":"http://img.domolife.cn/test/20161223092818.jpg","content":"可以联系一下app的私人秘书给你处理下哈","is_reply":1,"reply_uid":29436,"uid":113,"is_at":0,"to_uid":0,"nickname":"多么生活","ctime":"02-23","isReplyMain":true,"id":3153,"nickname1":"1376326****","atList":[]},{"like_num":0,"obj_id":9769,"isFavor":false,"avatar":"http://img.domolife.cn/test/20161223092818.jpg","content":"好的，不好意思","is_reply":1,"reply_uid":29436,"uid":113,"is_at":0,"to_uid":0,"nickname":"多么生活","ctime":"02-24","isReplyMain":true,"id":3165,"nickname1":"1376326****","atList":[]},{"like_num":0,"obj_id":9769,"isFavor":false,"avatar":"http://q.qlogo.cn/qqapp/1105138467/605725B499344A11B43786FE9156EC32/100","content":"直接联系app的私人秘书给你处理哈","is_reply":1,"reply_uid":29436,"uid":24599,"is_at":1,"to_uid":1,"nickname":"｀繼續___:。","ctime":"02-25","isReplyMain":true,"id":3174,"nickname1":"1376326****","atList":[]}]
         * content : 想问一下买了东西想换货找哪位，在哪找
         * is_reply : 0
         * uid : 29436
         * is_at : 1
         * to_uid : 1
         * nickname : 1376326****
         * ctime : 02-18
         * id : 3125
         * atList : []
         */

        private int like_num;
        private int obj_id;
        private boolean isFavor;
        private String avatar;
        private String content;
        private int is_reply;
        private int uid;
        private int is_at;
        private int to_uid;
        private String nickname;
        private String ctime;
        private int id;
        private List<ReplyCommBean> replyCommList;

        public int getLike_num() {
            return like_num;
        }

        public void setLike_num(int like_num) {
            this.like_num = like_num;
        }

        public int getObj_id() {
            return obj_id;
        }

        public void setObj_id(int obj_id) {
            this.obj_id = obj_id;
        }

        public boolean isIsFavor() {
            return isFavor;
        }

        public void setIsFavor(boolean isFavor) {
            this.isFavor = isFavor;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
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

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public int getIs_at() {
            return is_at;
        }

        public void setIs_at(int is_at) {
            this.is_at = is_at;
        }

        public int getTo_uid() {
            return to_uid;
        }

        public void setTo_uid(int to_uid) {
            this.to_uid = to_uid;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getCtime() {
            return ctime;
        }

        public void setCtime(String ctime) {
            this.ctime = ctime;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public List<ReplyCommBean> getReplyCommList() {
            return replyCommList;
        }

        public void setReplyCommList(List<ReplyCommBean> replyCommList) {
            this.replyCommList = replyCommList;
        }

        public static class ReplyCommBean {
            /**
             * like_num : 1
             * obj_id : 9769
             * isFavor : false
             * avatar : http://img.domolife.cn/test/20161223092818.jpg
             * content : 在我的里面，点击私人助手联系
             * is_reply : 1
             * reply_uid : 29436
             * uid : 113
             * is_at : 0
             * to_uid : 0
             * nickname : 多么生活
             * ctime : 02-21
             * isReplyMain : true
             * id : 3138
             * nickname1 : 1376326****
             * atList : []
             */

            private int like_num;
            private int obj_id;
            @SerializedName("isFavor")
            private boolean favor;
            private String avatar;
            private String content;
            private int is_reply;
            private int reply_uid;
            private int uid;
            private int is_at;
            private int to_uid;
            private String nickname;
            private String ctime;
            private boolean replyMain;
            private int id;
            private String nickname1;

            public int getLike_num() {
                return like_num;
            }

            public void setLike_num(int like_num) {
                this.like_num = like_num;
            }

            public int getObj_id() {
                return obj_id;
            }

            public void setObj_id(int obj_id) {
                this.obj_id = obj_id;
            }

            public boolean isFavor() {
                return favor;
            }

            public void setFavor(boolean favor) {
                this.favor = favor;
            }

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
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

            public int getIs_at() {
                return is_at;
            }

            public void setIs_at(int is_at) {
                this.is_at = is_at;
            }

            public int getTo_uid() {
                return to_uid;
            }

            public void setTo_uid(int to_uid) {
                this.to_uid = to_uid;
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public String getCtime() {
                return ctime;
            }

            public void setCtime(String ctime) {
                this.ctime = ctime;
            }

            public boolean isReplyMain() {
                return replyMain;
            }

            public void setReplyMain(boolean replyMain) {
                this.replyMain = replyMain;
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
        }
    }
}
