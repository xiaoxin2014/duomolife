package com.amkj.dmsh.homepage.bean;

import com.amkj.dmsh.base.BaseEntity;

import java.util.List;

/**
 * Created by xiaoxin on 2019/4/12 0012
 * Version:v4.0.0
 * ClassDescription :新版首页Tab实体
 */
public class HomeCommonEntity extends BaseEntity {


    /**
     * goodsNavbarList : [{"color":"string","icon":"string","link":"string","name":"string","productInfoList":[{"categoryId":0,"count":0,"id":0,"integralPrice":0,"moneyPrice":"string","name":"string","picUrl":"string","saleSkuId":0,"skuName":"string"}],"showType":"string"}]
     * sysTime : string
     */

    private String sysTime;
    private List<HomeCommonBean> guidanceInfoList;

    public String getSysTime() {
        return sysTime;
    }

    public void setSysTime(String sysTime) {
        this.sysTime = sysTime;
    }

    public List<HomeCommonBean> getResult() {
        return guidanceInfoList;
    }

    public void setResult(List<HomeCommonBean> goodsNavbarList) {
        this.guidanceInfoList = goodsNavbarList;
    }

    public static class HomeCommonBean {
        /**
         * color : string
         * icon : string
         * link : string
         * name : string
         * productInfoList : [{"categoryId":0,"count":0,"id":0,"integralPrice":0,"moneyPrice":"string","name":"string","picUrl":"string","saleSkuId":0,"skuName":"string"}]
         * showType : string
         */

        private String color;
        private String icon;
        private String link;
        private String cover;
        private String isDisplay;
        private String description;
        private String name;
        private String showType;
        private List<ProductInfoListBean> productInfoList;

        public HomeCommonBean(String showType, String icon, String name, String color, String link) {
            this.color = color;
            this.icon = icon;
            this.link = link;
            this.name = name;
            this.showType = showType;
        }


        public List<ProductInfoListBean> getProductInfoList() {
            return productInfoList;
        }

        public void setProductInfoList(List<ProductInfoListBean> productInfoList) {
            this.productInfoList = productInfoList;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getShowType() {
            return showType;
        }

        public void setShowType(String showType) {
            this.showType = showType;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public String getIsDisplay() {
            return isDisplay;
        }

        public void setIsDisplay(String isDisplay) {
            this.isDisplay = isDisplay;
        }
    }

    public static class ProductInfoListBean {
        /**
         * img : http://image.domolife.cn/platform/20171218/20171218161441555.jpg
         * price : 59.00
         * marketPrice : 59.00
         */

        private String img;
        private String price;
        private String marketPrice;

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getMarketPrice() {
            return marketPrice;
        }

        public void setMarketPrice(String marketPrice) {
            this.marketPrice = marketPrice;
        }
    }
}
