package com.amkj.dmsh.dominant.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/7/12
 * class description:请输入类描述
 */

public class QualityHomeTypeEntity {

    /**
     * result : [{"picUrl":"http://img.domolife.cn/platform/EariC5wsfj1499423080904.jpg","androidLink":"app://ShopScrollDetailsActivity?productId=451","subtitle":"这是必买清单","iosLink":"app://DMLGoodsProductsInfoViewController?goodsId=451","rank":1,"id":17,"position":1,"title":"必买清单"},{"picUrl":"http://img.domolife.cn/platform/XtHsJzXzAX1499423142046.jpg","androidLink":"","subtitle":"这是多么拼团","iosLink":"","rank":2,"id":18,"position":1,"title":"多么拼团"},{"picUrl":"http://img.domolife.cn/platform/dt7behwDQN1499423168900.jpg","androidLink":"","subtitle":"这是新人","iosLink":"","rank":2,"id":19,"position":1,"title":"新人专区"}]
     * msg : 请求成功
     * code : 01
     */

    private String msg;
    private String code;
    @SerializedName("result")
    private List<QualityHomeTypeBean> qualityHomeTypeList;

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

    public List<QualityHomeTypeBean> getQualityHomeTypeList() {
        return qualityHomeTypeList;
    }

    public void setQualityHomeTypeList(List<QualityHomeTypeBean> QualityHomeTypeList) {
        this.qualityHomeTypeList = QualityHomeTypeList;
    }

    public static class QualityHomeTypeBean implements MultiItemEntity {
        /**
         * picUrl : http://img.domolife.cn/platform/EariC5wsfj1499423080904.jpg
         * androidLink : app://ShopScrollDetailsActivity?productId=451
         * subtitle : 这是必买清单
         * iosLink : app://DMLGoodsProductsInfoViewController?goodsId=451
         * rank : 1
         * id : 17
         * position : 1
         * title : 必买清单
         */

        private String picUrl;
        private String androidLink;
        private String subtitle;
        private String iosLink;
        private int rank;
        private int id;
        private int position;
        private String title;
        private String color;
        private int itemType;

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public String getPicUrl() {
            return picUrl;
        }

        public void setPicUrl(String picUrl) {
            this.picUrl = picUrl;
        }

        public String getAndroidLink() {
            return androidLink;
        }

        public void setAndroidLink(String androidLink) {
            this.androidLink = androidLink;
        }

        public String getSubtitle() {
            return subtitle;
        }

        public void setSubtitle(String subtitle) {
            this.subtitle = subtitle;
        }

        public String getIosLink() {
            return iosLink;
        }

        public void setIosLink(String iosLink) {
            this.iosLink = iosLink;
        }

        public int getRank() {
            return rank;
        }

        public void setRank(int rank) {
            this.rank = rank;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        @Override
        public int getItemType() {
            return itemType;
        }

        public void setItemType(int itemType) {
            this.itemType = itemType;
        }
    }
}
