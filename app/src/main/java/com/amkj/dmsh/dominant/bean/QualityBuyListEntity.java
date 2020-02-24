package com.amkj.dmsh.dominant.bean;

import com.amkj.dmsh.base.BaseEntity;
import com.amkj.dmsh.user.bean.UserLikedProductEntity.LikedProductBean.MarketLabelBean;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/6/26
 * class description:请输入类描述
 */

public class QualityBuyListEntity extends BaseEntity{

    /**
     * result : [{"picUrl":"http://img.domolife.cn/platform/kKrBXN28hY.jpg","quantity":1971,"price":26,"recommendReason":"实用","name":"FaSoLa冰箱冷冻饺子收纳盒","id":4283,"inCart":false},{"picUrl":"http://img.domolife.cn/platform/pXSire6TMD.jpg","quantity":3888,"price":14,"recommendReason":"纯棉贴肤","name":"日本FaSoLa文胸洗衣袋网袋洗内衣","id":4316,"inCart":false},{"picUrl":"http://img.domolife.cn/platform/20170225/20170225203234095.jpg","quantity":200,"price":366,"recommendReason":"特别推荐","name":"韩国Ifam森林儿童书架简易小书柜构成2","id":4290,"inCart":false}]
     * msg : 请求成功
     * code : 01
     */
    @SerializedName("result")
    private List<QualityBuyListBean> qualityBuyListBeanList;

    public List<QualityBuyListBean> getQualityBuyListBeanList() {
        return qualityBuyListBeanList;
    }

    public void setQualityBuyListBeanList(List<QualityBuyListBean> qualityBuyListBeanList) {
        this.qualityBuyListBeanList = qualityBuyListBeanList;
    }

    public static class QualityBuyListBean {
        /**
         * picUrl : http://img.domolife.cn/platform/kKrBXN28hY.jpg
         * quantity : 1971
         * price : 26
         * recommendReason : 实用
         * name : FaSoLa冰箱冷冻饺子收纳盒
         * id : 4283
         * inCart : false
         */

        private String picUrl;
        private int quantity = 1;
        private String price;
        private String recommendReason;
        private String name;
        private int id;
        private boolean inCart;
        private String activityCode;
        private List<MarketLabelBean> marketLabelList;
        //拼团商品专用字段
        private String gpInfoId;

        public String getGpInfoId() {
            return gpInfoId;
        }

        public void setGpInfoId(String gpInfoId) {
            this.gpInfoId = gpInfoId;
        }

        public String getActivityCode() {
            return activityCode;
        }

        public void setActivityCode(String activityCode) {
            this.activityCode = activityCode;
        }

        public String getPicUrl() {
            return picUrl;
        }

        public void setPicUrl(String picUrl) {
            this.picUrl = picUrl;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public List<MarketLabelBean> getMarketLabelList() {
            return marketLabelList;
        }

        public void setMarketLabelList(List<MarketLabelBean> marketLabelList) {
            this.marketLabelList = marketLabelList;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getRecommendReason() {
            return recommendReason;
        }

        public void setRecommendReason(String recommendReason) {
            this.recommendReason = recommendReason;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public boolean isInCart() {
            return inCart;
        }

        public void setInCart(boolean inCart) {
            this.inCart = inCart;
        }
    }
}
