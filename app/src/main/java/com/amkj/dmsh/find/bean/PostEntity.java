package com.amkj.dmsh.find.bean;

import com.amkj.dmsh.base.BaseEntity;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by xiaoxin on 2019/7/9
 * Version:v4.1.0
 * ClassDescription :帖子列表实体类
 */
public class PostEntity extends BaseEntity {

    @SerializedName("list")
    private List<PostBean> postList;

    public List<PostBean> getPostList() {
        return postList;
    }

    public void setPostList(List<PostBean> postList) {
        this.postList = postList;
    }

    public static class PostBean {

        /**
         * id : 19893
         * status : 0
         * topicTitle : null
         * avatar : http://thirdwx.qlogo.cn/mmopen/vi_32/q7qU6Nh1iavpJdwJAtT5ic9MwgDTiaX1QVJVXdHRamZZMFEFKV228SoPzo5B2oYibgKg95Gan7nNfDibziatibRMy4L7w/132
         * nickName : 鸿星
         * digest : 发帖
         * cover : http://image.domolife.cn/201907231645540199914842.jpg
         * createTime : 2019-07-23 16:45:54
         * favorNum : 0
         * isFavor : 0
         */

        private int id;
        private int status;
        private String topicTitle;
        private String avatar;
        private String nickName;
        private String digest;
        private String cover;
        private String createTime;
        private int favorNum;
        private int isFavor;
        private int articletype;
        private int coverWidth;
        private int coverHeight;
        private int topicId;


        public int getTopicId() {
            return topicId;
        }

        public void setTopicId(int topicId) {
            this.topicId = topicId;
        }

        public int getCoverWidth() {
            return coverWidth;
        }

        public void setCoverWidth(int coverWidth) {
            this.coverWidth = coverWidth;
        }

        public int getCoverHeight() {
            return coverHeight;
        }

        public void setCoverHeight(int coverHeight) {
            this.coverHeight = coverHeight;
        }

        public int getArticletype() {
            return articletype;
        }

        public void setArticletype(int articletype) {
            this.articletype = articletype;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getTopicTitle() {
            return topicTitle;
        }

        public void setTopicTitle(String topicTitle) {
            this.topicTitle = topicTitle;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getDigest() {
            return digest;
        }

        public void setDigest(String digest) {
            this.digest = digest;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public int getFavorNum() {
            return favorNum;
        }

        public void setFavorNum(int favorNum) {
            this.favorNum = favorNum;
        }

        public boolean isFavor() {
            return isFavor == 1;
        }

        public void setIsFavor(boolean isFavor) {
            this.isFavor = isFavor ? 1 : 0;
        }
    }

}
