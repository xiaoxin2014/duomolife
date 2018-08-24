package com.amkj.dmsh.dominant.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/7/5
 * class description:请输入类描述
 */

public class QualityNewUserShopEntity {

    /**
     * result : [{"picUrl":"http://img.domolife.cn/platform/20170225/20170225203712448.jpg","quantity":150,"decreasePrice":"0","price":10,"name":"韩国Ifam宝宝森林玩具整理架边角架构成5","id":4292,"tag":""},{"picUrl":"http://img.domolife.cn/platform/3bn4KF6Wx4.jpg","quantity":0,"decreasePrice":"6","price":2549,"name":"戴森吸尘器V6Motorhead","id":4278,"tag":"新人立减6元"}]
     * msg : 请求成功
     * code : 01
     */

    private String msg;
    private String code;
    @SerializedName("result")
    private List<QualityNewUserShopBean> qualityNewUserShopList;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<QualityNewUserShopBean> getQualityNewUserShopList() {
        return qualityNewUserShopList;
    }

    public void setQualityNewUserShopList(List<QualityNewUserShopBean> qualityNewUserShopList) {
        this.qualityNewUserShopList = qualityNewUserShopList;
    }

    public static class QualityNewUserShopBean implements MultiItemEntity {
        /**
         * picUrl : http://img.domolife.cn/platform/20170225/20170225203712448.jpg
         * quantity : 150
         * decreasePrice : 0
         * price : 10
         * name : 韩国Ifam宝宝森林玩具整理架边角架构成5
         * id : 4292
         * tag :
         */

        private String picUrl;
        private int quantity = 1;
        private String decreasePrice;
        @SerializedName(value = "price", alternate = "preferentialPrice")
        private String price;
        private String name;
        private int id;
        private String tag;
        private String activityCode;
        private int cItemType;
        private String cTitle;

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

        public String getDecreasePrice() {
            return decreasePrice;
        }

        public void setDecreasePrice(String decreasePrice) {
            this.decreasePrice = decreasePrice;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
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

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        @Override
        public int getItemType() {
            return cItemType;
        }

        public void setcItemType(int cItemType) {
            this.cItemType = cItemType;
        }

        public String getcTitle() {
            return cTitle;
        }

        public void setcTitle(String cTitle) {
            this.cTitle = cTitle;
        }
    }
}
