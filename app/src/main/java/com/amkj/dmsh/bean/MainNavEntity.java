package com.amkj.dmsh.bean;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/10/27
 * class description:底栏导航信息
 */

public class MainNavEntity {

    /**
     * currentTime : 2017-10-27 15:10:52
     * result : [{"picUrl":"http://image.domolife.cn/platform/RfnRzp6eFX1508988227305.png","androidLink":"321","picUrlSecond":"http://image.domolife.cn/platform/H2BhjyW8sK1508988237874.png","webLink":"231","mainColor":"","iosLink":"app://DMLHomepageViewController","rank":1,"expire_time":1509120000000,"secondColor":"","title":"首页"},{"picUrl":"http://image.domolife.cn/platform/255PR5TZAT1508988354667.png","androidLink":"app://DMLGroupPurchaseListViewController","picUrlSecond":"http://image.domolife.cn/platform/8xJ6RnH8Cw1508988367343.png","webLink":"www.baidu.com","mainColor":"","iosLink":"app://DMLGoodProductsViewController","rank":2,"expire_time":1509120000000,"secondColor":"","title":"良品"},{"picUrl":"http://image.domolife.cn/platform/dj7Ran4dtF1508988402581.png","androidLink":"12","picUrlSecond":"http://image.domolife.cn/platform/exSXyisNaH1508988408994.png","webLink":"12","mainColor":"","iosLink":"app://DMLSpringSaleViewController","rank":3,"expire_time":1509120000000,"secondColor":"","title":"限时特惠"},{"picUrl":"http://image.domolife.cn/platform/7CEhciiJzE1508988518995.png","androidLink":"126","picUrlSecond":"http://image.domolife.cn/platform/WWMwp6Jc2Q1508988524542.png","webLink":"126","mainColor":"","iosLink":"app://DMLDiscoverViewController","rank":4,"expire_time":1509120000000,"secondColor":"","title":"发现"},{"picUrl":"http://image.domolife.cn/platform/MMtNMBzist1508988558564.png","androidLink":"234","picUrlSecond":"http://image.domolife.cn/platform/H2ZJBQKAKk1508988567549.png","webLink":"44","mainColor":"ffff29","iosLink":"app://DMLMeViewController","rank":5,"secondColor":"ffff1f","title":"我的"}]
     * msg : 请求成功
     * expireTime : 2017-10-28 00:00:00
     * code : 01
     */

    private String currentTime;
    private String msg;
    private String expireTime;
    private String code;
    @SerializedName("result")
    private List<MainNavBean> mainNavBeanList;
    private String bgColor;


    public String getBgColor() {
        if (TextUtils.isEmpty(bgColor)) {
            return "";
        } else {
            return bgColor.startsWith("#") ? bgColor : "#" + bgColor;
        }
    }

    public void setBgColor(String bgColor) {
        this.bgColor = bgColor;
    }

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

    public String getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(String expireTime) {
        this.expireTime = expireTime;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<MainNavBean> getMainNavBeanList() {
        return mainNavBeanList;
    }

    public void setMainNavBeanList(List<MainNavBean> mainNavBeanList) {
        this.mainNavBeanList = mainNavBeanList;
    }

    public static class MainNavBean {
        /**
         * picUrl : http://image.domolife.cn/platform/RfnRzp6eFX1508988227305.png
         * androidLink : 321
         * picUrlSecond : http://image.domolife.cn/platform/H2BhjyW8sK1508988237874.png
         * webLink : 231
         * mainColor :
         * iosLink : app://DMLHomepageViewController
         * rank : 1
         * expire_time : 1509120000000
         * secondColor :
         * title : 首页
         */

        private String picUrl;
        private String androidLink;
        private String picUrlSecond;
        private String webLink;
        private String mainColor;
        private String iosLink;
        private int rank;
        private long expire_time;
        private String secondColor;
        private String title;

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

        public String getPicUrlSecond() {
            return picUrlSecond;
        }

        public void setPicUrlSecond(String picUrlSecond) {
            this.picUrlSecond = picUrlSecond;
        }

        public String getWebLink() {
            return webLink;
        }

        public void setWebLink(String webLink) {
            this.webLink = webLink;
        }

        public String getMainColor() {
            return mainColor;
        }

        public void setMainColor(String mainColor) {
            this.mainColor = mainColor;
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

        public long getExpire_time() {
            return expire_time;
        }

        public void setExpire_time(long expire_time) {
            this.expire_time = expire_time;
        }

        public String getSecondColor() {
            return secondColor;
        }

        public void setSecondColor(String secondColor) {
            this.secondColor = secondColor;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
