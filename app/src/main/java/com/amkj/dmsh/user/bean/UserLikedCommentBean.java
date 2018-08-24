package com.amkj.dmsh.user.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by atd48 on 2016/9/28.
 */
public class UserLikedCommentBean {

    /**
     * result : [{"content":"挺不错的","uid":6,"atList":[{"uid":"23292","nickname":"13456981485"}],"title":"芒果糯米糍","status":1,"nickname":"小斌斌","is_at":0,"path":"/Uploads/goods_img/2016-03-31/56fc880e98ce9.jpg","like_num":1,"obj":"doc","obj_id":253,"ctime":"2016-05-04 15:22:47","is_reply":0},{"content":"挺不错的","uid":6,"atList":[],"title":"芒果糯米糍","status":1,"nickname":"小斌斌","is_at":0,"path":"/Uploads/goods_img/2016-03-31/56fc880e98ce9.jpg","like_num":1,"obj":"doc","obj_id":253,"ctime":"2016-05-04 15:22:47","is_reply":0}]
     * code : 01
     * msg : 请求成功
     */

    private String code;
    private String msg;
    /**
     * content : 挺不错的
     * uid : 6
     * atList : [{"uid":"23292","nickname":"13456981485"}]
     * title : 芒果糯米糍
     * status : 1
     * nickname : 小斌斌
     * is_at : 0
     * path : /Uploads/goods_img/2016-03-31/56fc880e98ce9.jpg
     * like_num : 1
     * obj : doc
     * obj_id : 253
     * ctime : 2016-05-04 15:22:47
     * is_reply : 0
     */

    @SerializedName("result")
    private List<ULikedComment> userlikeComments;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<ULikedComment> getUserlikeComments() {
        return userlikeComments;
    }

    public void setUserlikeComments(List<ULikedComment> userlikeComments) {
        this.userlikeComments = userlikeComments;
    }

    public static class ULikedComment {
        private String content;
        private int uid;
        private String title;
        private int status;
        private String nickname;
        private String nickname1;
        private int is_at;
        private String path;
        private int like_num;
        private String obj;
        private int obj_id;
        private String ctime;
        private int is_reply;
        private int backCode;
        private String avatar;

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public int getBackCode() {
            return backCode;
        }

        public void setBackCode(int backCode) {
            this.backCode = backCode;
        }

        /**
         * uid : 23292
         * nickname : 13456981485
         */

        private List<AtListBean> atList;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
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

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public int getIs_at() {
            return is_at;
        }

        public void setIs_at(int is_at) {
            this.is_at = is_at;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public int getLike_num() {
            return like_num;
        }

        public void setLike_num(int like_num) {
            this.like_num = like_num;
        }

        public String getObj() {
            return obj;
        }

        public void setObj(String obj) {
            this.obj = obj;
        }

        public int getObj_id() {
            return obj_id;
        }

        public void setObj_id(int obj_id) {
            this.obj_id = obj_id;
        }

        public String getCtime() {
            return ctime;
        }

        public void setCtime(String ctime) {
            this.ctime = ctime;
        }

        public String getNickname1() {
            return nickname1;
        }

        public void setNickname1(String nickname1) {
            this.nickname1 = nickname1;
        }

        public int getIs_reply() {
            return is_reply;
        }

        public void setIs_reply(int is_reply) {
            this.is_reply = is_reply;
        }

        public List<AtListBean> getAtList() {
            return atList;
        }

        public void setAtList(List<AtListBean> atList) {
            this.atList = atList;
        }

        public static class AtListBean {
            private String uid;
            private String nickname;

            public String getUid() {
                return uid;
            }

            public void setUid(String uid) {
                this.uid = uid;
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }
        }
    }
}
