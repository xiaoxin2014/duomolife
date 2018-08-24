package com.amkj.dmsh.dominant.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/5/12
 * class description:请输入类描述
 */

public class DMLTimeDetailEntity {

    /**
     * result : [{"picUrl":"http://img.domolife.cn/platform/20170511/20170511152753403.jpg","marketPrice":"39.00","quantity":100,"price":"32.00","subtitle":null,"isRemind":0,"name":"hello kitty 宠物店","startTime":"2017-05-12 10:00:00","id":7132,"endTime":"2017-05-17 23:59:59"},{"picUrl":"http://img.domolife.cn/platform/20170511/20170511152134366.jpg","marketPrice":"32.00","quantity":100,"price":"22.00","subtitle":null,"isRemind":0,"name":"hello kitty 过家家系列","startTime":"2017-05-12 10:00:00","id":7131,"endTime":"2017-05-17 23:59:59"},{"picUrl":"http://img.domolife.cn/platform/20170511/20170511151921619.jpg","marketPrice":"85.00","quantity":100,"price":"68.00","subtitle":null,"isRemind":0,"name":"hello kitty 生日蛋糕房子","startTime":"2017-05-12 10:00:00","id":7130,"endTime":"2017-05-17 23:59:59"},{"picUrl":"http://img.domolife.cn/platform/20170511/20170511151734602.jpg","marketPrice":"128.00","quantity":100,"price":"95.00","subtitle":null,"isRemind":0,"name":"hello kitty 露营车","startTime":"2017-05-12 10:00:00","id":7129,"endTime":"2017-05-17 23:59:59"},{"picUrl":"http://img.domolife.cn/platform/20170511/20170511151520929.jpg","marketPrice":"180.00","quantity":100,"price":"120.00","subtitle":null,"isRemind":0,"name":"hello kitty购物发声超市","startTime":"2017-05-12 10:00:00","id":7128,"endTime":"2017-05-17 23:59:59"},{"picUrl":"http://img.domolife.cn/platform/20170511/20170511151247103.jpg","marketPrice":"208.00","quantity":100,"price":"150.00","subtitle":null,"isRemind":0,"name":"jada-hello kitty飞机模型","startTime":"2017-05-12 10:00:00","id":5891,"endTime":"2017-05-17 23:59:59"}]
     * currentTime : 2017-05-12 10:30:38
     * msg : 请求成功
     * code : 01
     */

    private String currentTime;
    private String msg;
    private String code;
    @SerializedName("result")
    private List<DMLTimeDetailBean> dmlTimeDetailBeanList;

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

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

    public List<DMLTimeDetailBean> getDmlTimeDetailBeanList() {
        return dmlTimeDetailBeanList;
    }

    public void setDmlTimeDetailBeanList(List<DMLTimeDetailBean> dmlTimeDetailBeanList) {
        this.dmlTimeDetailBeanList = dmlTimeDetailBeanList;
    }

    public static class DMLTimeDetailBean {
        /**
         * picUrl : http://img.domolife.cn/platform/20170511/20170511152753403.jpg
         * marketPrice : 39.00
         * quantity : 100
         * price : 32.00
         * subtitle : null
         * isRemind : 0
         * name : hello kitty 宠物店
         * startTime : 2017-05-12 10:00:00
         * id : 7132
         * endTime : 2017-05-17 23:59:59
         */

        private String picUrl;
        private String marketPrice;
        private int quantity;
        private String price;
        private Object subtitle;
        private int isRemind;
        private String name;
        private String startTime;
        private int id;
        private String endTime;
        private String currentTime;

        public String getPicUrl() {
            return picUrl;
        }

        public void setPicUrl(String picUrl) {
            this.picUrl = picUrl;
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

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public Object getSubtitle() {
            return subtitle;
        }

        public void setSubtitle(Object subtitle) {
            this.subtitle = subtitle;
        }

        public int getIsRemind() {
            return isRemind;
        }

        public void setIsRemind(int isRemind) {
            this.isRemind = isRemind;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getCurrentTime() {
            return currentTime;
        }

        public void setCurrentTime(String currentTime) {
            this.currentTime = currentTime;
        }
    }
}
