package com.amkj.dmsh.bean;

import com.amkj.dmsh.base.BaseTimeEntity;

import java.util.List;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;

/**
 * Created by xiaoxin on 2019/3/16 0016
 */
public class EditorCommentEntity extends BaseTimeEntity {

    /**
     * sysTime : 2019-03-20 20:31:02
     * result : [{"id":4,"nickname":"哎呦哈嘿","avatar":"http://image.domolife.cn/platform/hjXPrGpMaJ1534473782022.jpg","redactorpickedId":4,"uid":87906007,"content":"I love it","likeNum":2,"createTime":"2019-03-19 17:57:48.0","examineTime":"2019-03-20 10:07:39.0","isFavor":1},{"id":5,"nickname":"哎呦哈嘿","avatar":"http://image.domolife.cn/platform/hjXPrGpMaJ1534473782022.jpg","redactorpickedId":4,"uid":87906007,"content":"i love it so much","likeNum":1,"createTime":"2019-03-20 09:36:06.0","examineTime":"2019-03-20 10:07:42.0","isFavor":1}]
     */


    private List<EditorCommentBean> result;
    /**
     * sysTime : 2019-03-22 15:04:00
     * commentCount : 6
     * isFavor : 0
     * favorCount : 1
     */

    private int commentCount;
    private int isFavor;
    private int favorCount;

    public List<EditorCommentBean> getResult() {
        return result;
    }

    public void setResult(List<EditorCommentBean> result) {
        this.result = result;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public boolean getIsFavor() {
        return isFavor == 1;
    }

    public int getFavorStatus() {
        return isFavor;
    }

    public void setIsFavor(boolean isFavor) {
        this.isFavor = isFavor ? 1 : 0;
    }

    public int getFavorCount() {
        return favorCount;
    }

    public String getFavorString() {
        return getStrings(getFavorCount() == 0 ? "赞" : String.valueOf(getFavorCount()));
    }


    public void setFavorCount(int favorCount) {
        this.favorCount = favorCount;
    }

    public static class EditorCommentBean {
        /**
         * id : 4
         * nickname : 哎呦哈嘿
         * avatar : http://image.domolife.cn/platform/hjXPrGpMaJ1534473782022.jpg
         * redactorpickedId : 4
         * uid : 87906007
         * content : I love it
         * likeNum : 2
         * createTime : 2019-03-19 17:57:48.0
         * examineTime : 2019-03-20 10:07:39.0
         * isFavor : 1
         */

        private int id;
        private String nickname;
        private String avatar;
        private int redactorpickedId;
        private int uid;
        private String content;
        private int likeNum;
        private String createTime;
        private String examineTime;
        private int isFavor;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public int getRedactorpickedId() {
            return redactorpickedId;
        }

        public void setRedactorpickedId(int redactorpickedId) {
            this.redactorpickedId = redactorpickedId;
        }

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getLikeNum() {
            return likeNum < 0 ? 0 : likeNum;
        }

        public String getLikeString() {
            return getStrings(getLikeNum() == 0 ? "赞" : String.valueOf(getLikeNum()));
        }

        public void setLikeNum(int likeNum) {
            this.likeNum = likeNum;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getExamineTime() {
            return examineTime;
        }

        public void setExamineTime(String examineTime) {
            this.examineTime = examineTime;
        }

        public boolean getIsFavor() {
            return isFavor == 1;
        }

        public int getFavorStatus() {
            return isFavor;
        }

        public void setIsFavor(boolean isFavor) {
            this.isFavor = isFavor ? 1 : 0;
        }

    }
}
