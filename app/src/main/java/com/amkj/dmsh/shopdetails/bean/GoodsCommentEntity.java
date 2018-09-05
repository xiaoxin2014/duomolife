package com.amkj.dmsh.shopdetails.bean;

import com.amkj.dmsh.base.BaseEntity;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by atd48 on 2016/10/5.
 */
public class GoodsCommentEntity extends BaseEntity{

    /**
     * result : [{"content":"傻逼","createTime":"2016-09-30 11:26:09","nickname":"Lucci","status":0,"userId":23317,"star":1,"images":"http://p1.so.qhmsg.com/bdr/326__/t01e91f5292c9cbb821.jpg","avatar":"http://img.domolife.cn/test/20160920233958.jpg"}]
     * code : 01
     * msg : 请求成功
     */
    /**
     * content : 傻逼
     * createTime : 2016-09-30 11:26:09
     * nickname : Lucci
     * status : 0
     * userId : 23317
     * star : 1
     * images : http://p1.so.qhmsg.com/bdr/326__/t01e91f5292c9cbb821.jpg
     * avatar : http://img.domolife.cn/test/20160920233958.jpg
     */
    private int evaluateCount;
    @SerializedName("result")
    private List<GoodsCommentBean> goodsComments;

    public int getEvaluateCount() {
        return evaluateCount;
    }

    public void setEvaluateCount(int evaluateCount) {
        this.evaluateCount = evaluateCount;
    }

    public List<GoodsCommentBean> getGoodsComments() {
        return goodsComments;
    }

    public void setGoodsComments(List<GoodsCommentBean> goodsComments) {
        this.goodsComments = goodsComments;
    }

    public static class GoodsCommentBean implements MultiItemEntity {
        private String content;
        private String createTime;
        private String nickname;
        private int status;
        private int userId;
        private int star;
        private String images;
        private String avatar;
        private int backCode;
        private int is_reply;
        private String reply_content;
        private int itemType;
        private int likeNum;
        private int id;
        private boolean isFavor;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public boolean isFavor() {
            return isFavor;
        }

        public void setFavor(boolean favor) {
            isFavor = favor;
        }

        public int getLikeNum() {
            return likeNum;
        }

        public void setLikeNum(int likeNum) {
            this.likeNum = likeNum;
        }

        public int getIs_reply() {
            return is_reply;
        }

        public void setIs_reply(int is_reply) {
            this.is_reply = is_reply;
        }

        public String getReply_content() {
            return reply_content;
        }

        public void setReply_content(String reply_content) {
            this.reply_content = reply_content;
        }

        public int getBackCode() {
            return backCode;
        }

        public void setBackCode(int backCode) {
            this.backCode = backCode;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public int getStar() {
            return star;
        }

        public void setStar(int star) {
            this.star = star;
        }

        public String getImages() {
            return images;
        }

        public void setImages(String images) {
            this.images = images;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        @Override
        public int getItemType() {
            return itemType;
        }

        public void setItemType(int itemType) {
            this.itemType = itemType;
        }
    }
}
