package com.amkj.dmsh.mine.bean;

import com.amkj.dmsh.base.BaseEntity;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/8/10
 * class description:请输入类描述
 */

public class CollectProEntity extends BaseEntity{

    /**
     * result : [{"productId":6256,"price":59,"priceTag":"","id":902,"name":"三顿半  自家拼配意式黑熊 8包装"},{"productId":4407,"price":109,"priceTag":"","id":391,"name":"英国JOSEPH  JOSEPH C型单手皂液器"},{"productId":5699,"price":358,"priceTag":"","id":643,"name":"商务男女拉杠行李箱"},{"productId":4316,"price":22,"priceTag":"","id":190,"name":"日本FaSoLa文胸洗衣袋网袋洗内衣"},{"productId":5991,"price":9.5,"priceTag":"降了¥24.5元","id":773,"name":"可爱卡通可妮兔防滑鼠标垫创意个性办公垫子"}]
     * msg : 请求成功
     * code : 01
     */
    @SerializedName("result")
    private List<CollectProBean> collectProList;
    private int count;

    public List<CollectProBean> getCollectProList() {
        return collectProList;
    }

    public void setCollectProList(List<CollectProBean> collectProList) {
        this.collectProList = collectProList;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public static class CollectProBean {
        /**
         * productId : 6256
         * price : 59
         * priceTag :
         * id : 902
         * name : 三顿半  自家拼配意式黑熊 8包装
         * collectId : 1
         */
        private String price;
        private String priceTag;
        private int id;
        private String name;
        private String picUrl;
        private int collectId;
        private boolean valid;

        /**
         * 自定义属性
         * @return
         */
        private boolean editStatus;
        private boolean checkStatus;

        public boolean isValid() {
            return valid;
        }

        public void setValid(boolean valid) {
            this.valid = valid;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getPriceTag() {
            return priceTag;
        }

        public void setPriceTag(String priceTag) {
            this.priceTag = priceTag;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPicUrl() {
            return picUrl;
        }

        public void setPicUrl(String picUrl) {
            this.picUrl = picUrl;
        }

        public boolean isEditStatus() {
            return editStatus;
        }

        public void setEditStatus(boolean editStatus) {
            this.editStatus = editStatus;
        }

        public boolean isCheckStatus() {
            return checkStatus;
        }

        public void setCheckStatus(boolean checkStatus) {
            this.checkStatus = checkStatus;
        }

        public int getCollectId() {
            return collectId;
        }

        public void setCollectId(int collectId) {
            this.collectId = collectId;
        }
    }
}
