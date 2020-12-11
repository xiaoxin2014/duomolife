package com.amkj.dmsh.time.bean;

import android.text.TextUtils;

import com.amkj.dmsh.base.BaseEntity;
import com.amkj.dmsh.user.bean.MarketLabelBean;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by xiaoxin on 2020/9/25
 * Version:v4.8.0
 */
public class BrandEntity extends BaseEntity {

    /**
     * sysTime : 2020-09-25 09:51:23
     * result : [{"id":1120,"picUrl":"http://image.domolife.cn/platform/20200915/20200915144104419.jpeg","title":"3123","subtitle":"11312","logo":"http://image.domolife.cn/platform/20200918/20200918111755772.png","backgroundUrl":"http://image.domolife.cn/platform/20200918/20200918112730560.png","productList":[{"id":18327,"marketPrice":"10","quantity":100,"picUrl":"https://img.alicdn.com/i4/2207283231975/O1CN01cY7s4i1QSaqgyiluI_!!2207283231975.jpg","previsionFlag":0,"price":"1","title":"紫山(Zishan)自加热米饭","subtitle":"","startTime":"2020-09-25 00:00:00","maxPrice":"2","endTime":"2020-10-31 00:00:00","flashBuyClickCount":"0"}]}]
     */

    private String sysTime;
    private List<BrandBean> result;

    public String getSysTime() {
        return sysTime;
    }

    public void setSysTime(String sysTime) {
        this.sysTime = sysTime;
    }

    public List<BrandBean> getResult() {
        return result;
    }

    public void setResult(List<BrandBean> result) {
        this.result = result;
    }

    public static class BrandBean extends BaseEntity {
        /**
         * id : 1120
         * picUrl : http://image.domolife.cn/platform/20200915/20200915144104419.jpeg
         * title : 3123
         * subtitle : 11312
         * logo : http://image.domolife.cn/platform/20200918/20200918111755772.png
         * backgroundUrl : http://image.domolife.cn/platform/20200918/20200918112730560.png
         * productList : [{"id":18327,"marketPrice":"10","quantity":100,"picUrl":"https://img.alicdn.com/i4/2207283231975/O1CN01cY7s4i1QSaqgyiluI_!!2207283231975.jpg","previsionFlag":0,"price":"1","title":"紫山(Zishan)自加热米饭","subtitle":"","startTime":"2020-09-25 00:00:00","maxPrice":"2","endTime":"2020-10-31 00:00:00","flashBuyClickCount":"0"}]
         */

        private String id;
        private String picUrl;
        private String title;
        private String subtitle;
        private String discription;
        private String logo;
        private String backgroundUrl;
        @SerializedName(value = "productList", alternate = "result")
        private List<BrandProductBean> productList;


        public String getDiscription() {
            return discription;
        }

        public void setDiscription(String discription) {
            this.discription = discription;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPicUrl() {
            return picUrl;
        }

        public void setPicUrl(String picUrl) {
            this.picUrl = picUrl;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getSubtitle() {
            return !TextUtils.isEmpty(discription) ? discription : subtitle;
        }

        public void setSubtitle(String subtitle) {
            this.subtitle = subtitle;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public String getBackgroundUrl() {
            return backgroundUrl;
        }

        public void setBackgroundUrl(String backgroundUrl) {
            this.backgroundUrl = backgroundUrl;
        }

        public List<BrandProductBean> getProductList() {
            return productList;
        }

        public void setProductList(List<BrandProductBean> productList) {
            this.productList = productList;
        }
    }

    public static class BrandProductBean {
        /**
         * id : 18327
         * marketPrice : 10
         * quantity : 100
         * picUrl : https://img.alicdn.com/i4/2207283231975/O1CN01cY7s4i1QSaqgyiluI_!!2207283231975.jpg
         * previsionFlag : 0
         * price : 1
         * title : 紫山(Zishan)自加热米饭
         * subtitle :
         * startTime : 2020-09-25 00:00:00
         * maxPrice : 2
         * endTime : 2020-10-31 00:00:00
         * flashBuyClickCount : 0
         */

        private int id;
        private String marketPrice;
        private int quantity;
        private String picUrl;
        private int previsionFlag;
        private String price;
        private String title;
        private String subtitle;
        private String startTime;
        private String maxPrice;
        private String endTime;
        private String flashBuyClickCount;
        private String waterRemark;
        private List<MarketLabelBean> marketLabelList;


        public String getWaterRemark() {
            return waterRemark;
        }

        public void setWaterRemark(String waterRemark) {
            this.waterRemark = waterRemark;
        }

        public List<MarketLabelBean> getMarketLabelList() {
            return marketLabelList;
        }

        public void setMarketLabelList(List<MarketLabelBean> marketLabelList) {
            this.marketLabelList = marketLabelList;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getMarketPrice() {
            return marketPrice;
        }

        public void setMarketPrice(String marketPrice) {
            this.marketPrice = marketPrice;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
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

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getSubtitle() {
            return subtitle;
        }

        public void setSubtitle(String subtitle) {
            this.subtitle = subtitle;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getMaxPrice() {
            return maxPrice;
        }

        public void setMaxPrice(String maxPrice) {
            this.maxPrice = maxPrice;
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
    }
}
