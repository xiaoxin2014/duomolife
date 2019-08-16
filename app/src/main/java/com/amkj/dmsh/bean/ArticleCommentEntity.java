package com.amkj.dmsh.bean;

import com.amkj.dmsh.base.BaseEntity;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by atd48 on 2016/8/30.
 */
public class ArticleCommentEntity extends BaseEntity{


    /**
     * result : [{"content":"快来看啊","uid":23291,"atList":[{"uid":"23292","nickname":"13456981485"}],"to_uid":23288,"nickname":"18123961075","is_at":1,"at_uid":23292,"like_num":0,"obj_id":4050,"ctime":"2016-09-01 16:32:59","is_reply":0},{"nickname1":"18123961075","uid":23293,"reply_uid":23291,"atList":[{"uid":"23292","nickname":"13456981485"}],"to_uid":23288,"is_at":1,"nickname":"13825698412","at_uid":23292,"pid":2487,"obj_id":4050,"ctime":"2016-09-01 16:34:59","content":"快来看啊啊啊啊啊啊啊a","like_num":0,"is_reply":1},{"nickname1":"18123961075","content":"好吧，我来看了。","reply_uid":23291,"uid":23292,"atList":[],"to_uid":23288,"nickname":"13456981485","is_at":0,"like_num":0,"pid":2491,"obj_id":4050,"ctime":"2016-09-01 16:42:40","is_reply":1},{"content":"这是个好文章","uid":23291,"atList":[],"to_uid":23288,"nickname":"18123961075","is_at":0,"like_num":0,"obj_id":4050,"ctime":"2016-09-05 20:15:26","is_reply":0}]
     * code : 01
     * msg : 请求成功
     */
    /**
     * content : 快来看啊
     * uid : 23291
     * atList : [{"uid":"23292","nickname":"13456981485"}]
     * to_uid : 23288
     * nickname : 18123961075
     * is_at : 1
     * at_uid : 23292
     * like_num : 0
     * obj_id : 4050
     * ctime : 2016-09-01 16:32:59
     * is_reply : 0
     */

    @SerializedName("result")
    private List<ArticleCommentBean> articleCommentList;

    public List<ArticleCommentBean> getArticleCommentList() {
        return articleCommentList;
    }

    public void setArticleCommentList(List<ArticleCommentBean> articleCommentList) {
        this.articleCommentList = articleCommentList;
    }

    public static class ArticleCommentBean implements MultiItemEntity {
        public final static int NORMAL_CODE = 0;
        //    没有数据
        public final static int FOOT_EMPTY_CODE = 1;

        private String content;
        private int uid;
        private int uid2;
        private int id;
        private int pid;
        private int reply_uid;
        private String nickname2;
        private String type;
        private String nickname1;
        private String nickname;
        private String avatar;
        private int is_at;
        private String at_uid;
        private int like_num;
        private int obj_id;
        private int object_id;
        private boolean isFavor;
        private String ctime;
        private String create_time;
        private int is_reply;
        private String path;
        private int status = 1;
        private String obj;
        private String description;
        private int comment_id;
        private Object to_uid;
        private int itemType;
        private int backCode;
        private String commentUserName;
        private String postAuthorName;

        public String getCommentUserName() {
            return commentUserName;
        }

        public void setCommentUserName(String commentUserName) {
            this.commentUserName = commentUserName;
        }

        public String getPostAuthorName() {
            return postAuthorName;
        }

        public void setPostAuthorName(String postAuthorName) {
            this.postAuthorName = postAuthorName;
        }

        public int getBackCode() {
            return backCode;
        }

        public void setBackCode(int backCode) {
            this.backCode = backCode;
        }

        public Object getTo_uid() {
            return to_uid;
        }

        public void setTo_uid(Object to_uid) {
            this.to_uid = to_uid;
        }

        public int getObject_id() {
            return object_id;
        }

        public void setObject_id(int object_id) {
            this.object_id = object_id;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public int getUid2() {
            return uid2;
        }

        public void setUid2(int uid2) {
            this.uid2 = uid2;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getComment_id() {
            return comment_id;
        }

        public void setComment_id(int comment_id) {
            this.comment_id = comment_id;
        }

        public int getPid() {
            return pid;
        }

        public void setPid(int pid) {
            this.pid = pid;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getNickname2() {
            return nickname2;
        }

        public void setNickname2(String nickname2) {
            this.nickname2 = nickname2;
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

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getObj() {
            return obj;
        }

        public void setObj(String obj) {
            this.obj = obj;
        }

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public int getReply_uid() {
            return reply_uid;
        }

        public void setReply_uid(int reply_uid) {
            this.reply_uid = reply_uid;
        }


        public String getNickname1() {
            return nickname1;
        }

        public void setNickname1(String nickname1) {
            this.nickname1 = nickname1;
        }

        public int getIs_at() {
            return is_at;
        }

        public void setIs_at(int is_at) {
            this.is_at = is_at;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
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

        public boolean isFavor() {
            return isFavor;
        }

        public void setFavor(boolean favor) {
            isFavor = favor;
        }

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

        public String getCtime() {
            return ctime;
        }

        public void setCtime(String ctime) {
            this.ctime = ctime;
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

        public void setItemType(int itemType) {
            this.itemType = itemType;
        }

        @Override
        public int getItemType() {
            return itemType;
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
