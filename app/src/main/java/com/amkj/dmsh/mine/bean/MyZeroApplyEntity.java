package com.amkj.dmsh.mine.bean;

import com.amkj.dmsh.base.BaseTimeEntity;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by xiaoxin on 2020/8/17
 * Version:v4.7.0
 */
public class MyZeroApplyEntity extends BaseTimeEntity {

    /**
     * sysTime : 2020-08-17 20:13:43
     * showCount : 10
     * totalPage : 1
     * totalResult : 1
     * currentPage : 1
     * result : [{"productId":"17486","productName":"蜡笔小新手机卡扣式导航车载支架","subtitle":"","productImg":"http://image.domolife.cn/platform/20200806/20200806153914217.jpeg","activityId":"1","marketPrice":"45","price":"0","endTime":"2020-08-14 00:00:00","applyTime":"2020-08-11 17:20:57","status":"","orderId":"","msg":""}]
     */

    private List<MyZeroApplyBean> result;

    public List<MyZeroApplyBean> getResult() {
        return result;
    }

    public void setResult(List<MyZeroApplyBean> result) {
        this.result = result;
    }

    public static class MyZeroApplyBean {
        /**
         * productId : 17486
         * productName : 蜡笔小新手机卡扣式导航车载支架
         * subtitle :
         * productImg : http://image.domolife.cn/platform/20200806/20200806153914217.jpeg
         * activityId : 1
         * marketPrice : 45
         * price : 0
         * endTime : 2020-08-14 00:00:00
         * applyTime : 2020-08-11 17:20:57
         * status :
         * orderId :
         * msg :
         */

        private String productId;
        private String productName;
        private String subtitle;
        private String productImg;
        private String activityId;
        private String marketPrice;
        private String price;
        private String endTime;
        private String applyTime;
        private String status;
        private String orderId;
        @SerializedName("msg")
        private String msgX;

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public String getSubtitle() {
            return subtitle;
        }

        public void setSubtitle(String subtitle) {
            this.subtitle = subtitle;
        }

        public String getProductImg() {
            return productImg;
        }

        public void setProductImg(String productImg) {
            this.productImg = productImg;
        }

        public String getActivityId() {
            return activityId;
        }

        public void setActivityId(String activityId) {
            this.activityId = activityId;
        }

        public String getMarketPrice() {
            return marketPrice;
        }

        public void setMarketPrice(String marketPrice) {
            this.marketPrice = marketPrice;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getApplyTime() {
            return applyTime;
        }

        public void setApplyTime(String applyTime) {
            this.applyTime = applyTime;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getMsgX() {
            return msgX;
        }

        public void setMsgX(String msgX) {
            this.msgX = msgX;
        }
    }
}
