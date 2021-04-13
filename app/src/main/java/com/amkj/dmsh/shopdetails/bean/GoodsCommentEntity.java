package com.amkj.dmsh.shopdetails.bean;

import com.amkj.dmsh.base.BaseEntity;
import com.amkj.dmsh.find.bean.RelatedGoodsBean;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by atd48 on 2016/10/5.
 */
public class GoodsCommentEntity extends BaseEntity{

    private int evaluateCount;
    @SerializedName(value = "postList", alternate = {"list", "result"})
    private List<GoodsCommentBean> goodsComments;
    private RelatedGoodsBean productInfo;

    public RelatedGoodsBean getProductInfo() {
        return productInfo;
    }

    public void setProductInfo(RelatedGoodsBean productInfo) {
        this.productInfo = productInfo;
    }

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
        @SerializedName(value = "content", alternate = "replyContent")
        private String content;
        private String createTime;
        private String nickname;
        private int status;
        private int userId;
        private int star;
        @SerializedName(value = "images", alternate = "imgsPath")
        private String images;
        private String avatar;
        private int backCode;
        private int is_reply;
        private String reply_content;
        private int itemType;
        @SerializedName(value = "likeNum", alternate = "favorNum")
        private int likeNum;
        private int id;
        private boolean isFavor;
        private String skuText;

        public String getSkuText() {
            return skuText;
        }

        public void setSkuText(String skuText) {
            this.skuText = skuText;
        }

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
