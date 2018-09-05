package com.amkj.dmsh.dominant.bean;

import com.amkj.dmsh.base.BaseEntity;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/6/19
 * class description:请输入类描述
 */

public class QualityGroupShareEntity extends BaseEntity{

    /**
     * result : {"leftParticipant":1,"gpCount":12,"gpPicUrl":"http://img.domolife.cn/platform/ZZWGaMTANz.jpg","gpStartTime":"2017-06-21 11:52:48","gpInfoId":1,"productSkuValue":"默认:蓝色","uid":27928,"gpProductQuantity":75,"gpType":"2人团","price":"1098.0","coverImage":"http://img.domolife.cn/platform/3atQYAZQ4X1497410876254.jpeg","gpInfoStartTime":"2017-06-01 00:00:00","gpInfoEndTime":"2017-06-27 00:00:00","quantityStatus":{"quantityStatusMsg":"库存足够，可开团和参团","quantityStatusId":1002},"gpRecordId":"38","images":"http://img.domolife.cn/platform/F4bZMMKEFF1497410875842.jpeg,http://img.domolife.cn/platform/NBCDtS3nBa1497410876188.jpeg,http://img.domolife.cn/platform/3atQYAZQ4X1497410876254.jpeg","productId":4282,"gpProductId":17,"gpEndTime":"2017-06-22 11:52:48","memberCount":2,"gpSkuId":112,"avatar":["http://img.domolife.cn/201704191717321905157574.png"],"skuQuantity":1004,"propValueId":"5","goodsAreaLabel":"拼团","subtitle":null,"name":"北鼎K206钻石电热水壶礼盒装","gpPrice":"0.01"}
     * currentTime : 2017-06-22 11:59:36
     * msg : 请求成功
     * code : 01
     */

    @SerializedName("result")
    private QualityGroupShareBean qualityGroupShareBean;
    private String currentTime;
    private long second;

    public QualityGroupShareBean getQualityGroupShareBean() {
        return qualityGroupShareBean;
    }

    public void setQualityGroupShareBean(QualityGroupShareBean qualityGroupShareBean) {
        this.qualityGroupShareBean = qualityGroupShareBean;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    public long getSecond() {
        return second;
    }

    public void setSecond(long second) {
        this.second = second;
    }

    public static class QualityGroupShareBean {
        /**
         * leftParticipant : 1
         * gpCount : 12
         * gpPicUrl : http://img.domolife.cn/platform/ZZWGaMTANz.jpg
         * gpStartTime : 2017-06-21 11:52:48
         * gpInfoId : 1
         * productSkuValue : 默认:蓝色
         * uid : 27928
         * gpProductQuantity : 75
         * gpType : 2人团
         * price : 1098.0
         * coverImage : http://img.domolife.cn/platform/3atQYAZQ4X1497410876254.jpeg
         * gpInfoStartTime : 2017-06-01 00:00:00
         * gpInfoEndTime : 2017-06-27 00:00:00
         * quantityStatus : {"quantityStatusMsg":"库存足够，可开团和参团","quantityStatusId":1002}
         * gpRecordId : 38
         * images : http://img.domolife.cn/platform/F4bZMMKEFF1497410875842.jpeg,http://img.domolife.cn/platform/NBCDtS3nBa1497410876188.jpeg,http://img.domolife.cn/platform/3atQYAZQ4X1497410876254.jpeg
         * productId : 4282
         * gpProductId : 17
         * gpEndTime : 2017-06-22 11:52:48
         * memberCount : 2
         * gpSkuId : 112
         * avatar : ["http://img.domolife.cn/201704191717321905157574.png"]
         * skuQuantity : 1004
         * propValueId : 5
         * goodsAreaLabel : 拼团
         * subtitle : null
         * name : 北鼎K206钻石电热水壶礼盒装
         * gpPrice : 0.01
         */

        private int leftParticipant;
        private int gpCount;
        private String gpPicUrl;
        private String gpStartTime;
        private int gpInfoId;
        private String productSkuValue;
        private int uid;
        private int gpProductQuantity;
        private String gpType;
        private String price;
        private String coverImage;
        private String gpInfoStartTime;
        private String gpInfoEndTime;
        private QuantityStatusBean quantityStatus;
        private String gpRecordId;
        private String images;
        private int productId;
        private int gpProductId;
        private String gpEndTime;
        private int memberCount;
        private int gpSkuId;
        private int skuQuantity;
        private String propValueId;
        private String goodsAreaLabel;
        private String subtitle;
        private String name;
        private String gpPrice;
        private List<String> avatar;
        private List<MemberListBean> memberList;

        public int getLeftParticipant() {
            return leftParticipant;
        }

        public void setLeftParticipant(int leftParticipant) {
            this.leftParticipant = leftParticipant;
        }

        public int getGpCount() {
            return gpCount;
        }

        public void setGpCount(int gpCount) {
            this.gpCount = gpCount;
        }

        public String getGpPicUrl() {
            return gpPicUrl;
        }

        public void setGpPicUrl(String gpPicUrl) {
            this.gpPicUrl = gpPicUrl;
        }

        public String getGpStartTime() {
            return gpStartTime;
        }

        public void setGpStartTime(String gpStartTime) {
            this.gpStartTime = gpStartTime;
        }

        public int getGpInfoId() {
            return gpInfoId;
        }

        public void setGpInfoId(int gpInfoId) {
            this.gpInfoId = gpInfoId;
        }

        public String getProductSkuValue() {
            return productSkuValue;
        }

        public void setProductSkuValue(String productSkuValue) {
            this.productSkuValue = productSkuValue;
        }

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public int getGpProductQuantity() {
            return gpProductQuantity;
        }

        public void setGpProductQuantity(int gpProductQuantity) {
            this.gpProductQuantity = gpProductQuantity;
        }

        public String getGpType() {
            return gpType;
        }

        public void setGpType(String gpType) {
            this.gpType = gpType;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getCoverImage() {
            return coverImage;
        }

        public void setCoverImage(String coverImage) {
            this.coverImage = coverImage;
        }

        public String getGpInfoStartTime() {
            return gpInfoStartTime;
        }

        public void setGpInfoStartTime(String gpInfoStartTime) {
            this.gpInfoStartTime = gpInfoStartTime;
        }

        public String getGpInfoEndTime() {
            return gpInfoEndTime;
        }

        public void setGpInfoEndTime(String gpInfoEndTime) {
            this.gpInfoEndTime = gpInfoEndTime;
        }

        public QuantityStatusBean getQuantityStatus() {
            return quantityStatus;
        }

        public void setQuantityStatus(QuantityStatusBean quantityStatus) {
            this.quantityStatus = quantityStatus;
        }

        public String getGpRecordId() {
            return gpRecordId;
        }

        public void setGpRecordId(String gpRecordId) {
            this.gpRecordId = gpRecordId;
        }

        public String getImages() {
            return images;
        }

        public void setImages(String images) {
            this.images = images;
        }

        public int getProductId() {
            return productId;
        }

        public void setProductId(int productId) {
            this.productId = productId;
        }

        public int getGpProductId() {
            return gpProductId;
        }

        public void setGpProductId(int gpProductId) {
            this.gpProductId = gpProductId;
        }

        public String getGpEndTime() {
            return gpEndTime;
        }

        public void setGpEndTime(String gpEndTime) {
            this.gpEndTime = gpEndTime;
        }

        public int getMemberCount() {
            return memberCount;
        }

        public void setMemberCount(int memberCount) {
            this.memberCount = memberCount;
        }

        public int getGpSkuId() {
            return gpSkuId;
        }

        public void setGpSkuId(int gpSkuId) {
            this.gpSkuId = gpSkuId;
        }

        public int getSkuQuantity() {
            return skuQuantity;
        }

        public void setSkuQuantity(int skuQuantity) {
            this.skuQuantity = skuQuantity;
        }

        public String getPropValueId() {
            return propValueId;
        }

        public void setPropValueId(String propValueId) {
            this.propValueId = propValueId;
        }

        public String getGoodsAreaLabel() {
            return goodsAreaLabel;
        }

        public void setGoodsAreaLabel(String goodsAreaLabel) {
            this.goodsAreaLabel = goodsAreaLabel;
        }

        public String getSubtitle() {
            return subtitle;
        }

        public void setSubtitle(String subtitle) {
            this.subtitle = subtitle;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getGpPrice() {
            return gpPrice;
        }

        public void setGpPrice(String gpPrice) {
            this.gpPrice = gpPrice;
        }

        public List<String> getAvatar() {
            return avatar;
        }

        public void setAvatar(List<String> avatar) {
            this.avatar = avatar;
        }

        public List<MemberListBean> getMemberList() {
            return memberList;
        }

        public void setMemberList(List<MemberListBean> memberList) {
            this.memberList = memberList;
        }

        public static class QuantityStatusBean {
            /**
             * quantityStatusMsg : 库存足够，可开团和参团
             * quantityStatusId : 1002
             */

            private String quantityStatusMsg;
            private int quantityStatusId;

            public String getQuantityStatusMsg() {
                return quantityStatusMsg;
            }

            public void setQuantityStatusMsg(String quantityStatusMsg) {
                this.quantityStatusMsg = quantityStatusMsg;
            }

            public int getQuantityStatusId() {
                return quantityStatusId;
            }

            public void setQuantityStatusId(int quantityStatusId) {
                this.quantityStatusId = quantityStatusId;
            }
        }

        public static class MemberListBean {
            /**
             * uid : 27928
             * nickname : 穆茨d'god like
             * avatar : http://image.domolife.cn/201704280903427554142183.png
             */
            private int uid;
            private String nickname;
            private String avatar;

            public static MemberListBean objectFromData(String str) {

                return new Gson().fromJson(str, MemberListBean.class);
            }

            public String getNickname() {
                return nickname;
            }

            public int getUid() {
                return uid;
            }

            public void setUid(int uid) {
                this.uid = uid;
            }

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }
        }
    }
}
