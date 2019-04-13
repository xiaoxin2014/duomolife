package com.amkj.dmsh.homepage.bean;

import com.amkj.dmsh.base.BaseEntity;

import java.util.List;

/**
 * Created by xiaoxin on 2019/4/12 0012
 * Version:v4.0.0
 * ClassDescription :新版首页Tab实体
 */
public class HomeNavbarEntity extends BaseEntity {


    /**
     * goodsNavbarList : [{"color":"string","icon":"string","link":"string","name":"string","productInfoList":[{"categoryId":0,"count":0,"id":0,"integralPrice":0,"moneyPrice":"string","name":"string","picUrl":"string","saleSkuId":0,"skuName":"string"}],"showType":"string"}]
     * sysTime : string
     */

    private String sysTime;
    private List<HomeNavbarBean> guidanceInfoList;

    public String getSysTime() {
        return sysTime;
    }

    public void setSysTime(String sysTime) {
        this.sysTime = sysTime;
    }

    public List<HomeNavbarBean> getGoodsNavbarList() {
        return guidanceInfoList;
    }

    public void setGoodsNavbarList(List<HomeNavbarBean> goodsNavbarList) {
        this.guidanceInfoList = goodsNavbarList;
    }

    public static class HomeNavbarBean {
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
        private String name;
        private String showType;

        public HomeNavbarBean(String showType, String icon, String name, String color, String link) {
            this.color = color;
            this.icon = icon;
            this.link = link;
            this.name = name;
            this.showType = showType;
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

    }
}
