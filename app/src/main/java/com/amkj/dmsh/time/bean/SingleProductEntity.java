package com.amkj.dmsh.time.bean;

import com.amkj.dmsh.base.BaseTimeEntity;
import com.amkj.dmsh.user.bean.MarketLabelBean;

import java.util.List;

/**
 * Created by xiaoxin on 2020/9/23
 * Version:v4.8.0
 */
public class SingleProductEntity extends BaseTimeEntity {

    /**
     * currentPage : 0
     * result : [{"endTime":"string","flashBuyClickCount":"string","id":0,"isRemind":0,"marketLabelList":[{"id":0,"labelType":0,"title":"string"}],"marketPrice":"string","maxPrice":"string","picUrl":"string","previsionFlag":0,"price":"string","productId":"string","quantity":0,"startTime":"string","subtitle":"string","thirdUrl":"string","title":"string"}]
     * showCount : 0
     * sysTime : string
     * totalPage : 0
     * totalResult : 0
     */


    private List<SingleProductBean> result;

    public List<SingleProductBean> getResult() {
        return result;
    }

    public void setResult(List<SingleProductBean> result) {
        this.result = result;
    }

    public static class SingleProductBean {
        /**
         * endTime : string
         * flashBuyClickCount : string
         * id : 0
         * isRemind : 0
         * marketLabelList : [{"id":0,"labelType":0,"title":"string"}]
         * marketPrice : string
         * maxPrice : string
         * picUrl : string
         * previsionFlag : 0
         * price : string
         * productId : string
         * quantity : 0
         * startTime : string
         * subtitle : string
         * thirdUrl : string
         * title : string
         */

        private String endTime;
        private String flashBuyClickCount;
        private int id;
        private int isRemind;
        private String marketPrice;
        private String maxPrice;
        private String picUrl;
        private int previsionFlag;
        private String price;
        private String productId;
        private int quantity;
        private String startTime;
        private String subtitle;
        private String thirdUrl;
        private String title;
        private String waterRemark;
        private List<MarketLabelBean> marketLabelList;


        public String getWaterRemark() {
            return waterRemark;
        }

        public void setWaterRemark(String waterRemark) {
            this.waterRemark = waterRemark;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getFlashBuyClickCount() {
            return flashBuyClickCount;
        }

        public void setFlashBuyClickCount(String flashBuyClickCount) {
            this.flashBuyClickCount = flashBuyClickCount;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getIsRemind() {
            return isRemind;
        }

        public void setIsRemind(int isRemind) {
            this.isRemind = isRemind;
        }

        public String getMarketPrice() {
            return marketPrice;
        }

        public void setMarketPrice(String marketPrice) {
            this.marketPrice = marketPrice;
        }

        public String getMaxPrice() {
            return maxPrice;
        }

        public void setMaxPrice(String maxPrice) {
            this.maxPrice = maxPrice;
        }

        public String getPicUrl() {
            return picUrl;
        }

        public void setPicUrl(String picUrl) {
            this.picUrl = picUrl;
        }

        public int getPrevisionFlag() {
            return previsionFlag;
        }

        public void setPrevisionFlag(int previsionFlag) {
            this.previsionFlag = previsionFlag;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getSubtitle() {
            return subtitle;
        }

        public void setSubtitle(String subtitle) {
            this.subtitle = subtitle;
        }

        public String getThirdUrl() {
            return thirdUrl;
        }

        public void setThirdUrl(String thirdUrl) {
            this.thirdUrl = thirdUrl;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public List<MarketLabelBean> getMarketLabelList() {
            return marketLabelList;
        }

        public void setMarketLabelList(List<MarketLabelBean> marketLabelList) {
            this.marketLabelList = marketLabelList;
        }
    }
}
