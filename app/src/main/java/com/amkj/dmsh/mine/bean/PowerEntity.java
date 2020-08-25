package com.amkj.dmsh.mine.bean;

import com.amkj.dmsh.base.BaseEntity;

import java.util.List;

/**
 * Created by xiaoxin on 2020/7/28
 * Version:v4.7.0
 * ClassDescription :权益列表实体类
 */
public class PowerEntity extends BaseEntity {

    /**
     * code : 01
     * msg : 请求成功
     * sysTime : 2020-07-28 10:43:19
     * result : [{"id":"1","title":"会员专享价","subtitle":"9.5折起，可叠加券和红包","picUrl":"http://image.domolife.cn/platform/pR5xxaksWn1595851599096.png","icon":"http://image.domolife.cn/platform/iRck4F62sc1595849791483.png","detail":"d","btnText":"去看看","iosLink":"111","androidLink":"222","webLink":"33","wxLink":"44"},{"id":"2","title":"会员专享券","subtitle":"","picUrl":"http://image.domolife.cn/platform/pR5xxaksWn1595851599096.png","icon":"http://image.domolife.cn/platform/iRck4F62sc1595849791483.png","detail":"","btnText":"去领券","iosLink":"","androidLink":"","webLink":"","wxLink":""},{"id":"3","title":"每周特价","subtitle":"","picUrl":"http://image.domolife.cn/platform/pR5xxaksWn1595851599096.png","icon":"http://image.domolife.cn/platform/iRck4F62sc1595849791483.png","detail":"","btnText":"去购物","iosLink":"","androidLink":"","webLink":"","wxLink":""},{"id":"4","title":"0元试用","subtitle":"会员专享，0元使用","picUrl":"http://image.domolife.cn/platform/pR5xxaksWn1595851599096.png","icon":"http://image.domolife.cn/platform/iRck4F62sc1595849791483.png","detail":"","btnText":"去看看","iosLink":"","androidLink":"","webLink":"","wxLink":""},{"id":"5","title":"购卡礼","subtitle":"","picUrl":"http://image.domolife.cn/platform/pR5xxaksWn1595851599096.png","icon":"http://image.domolife.cn/platform/iRck4F62sc1595849791483.png","detail":"","btnText":"去购卡","iosLink":"","androidLink":"","webLink":"","wxLink":""},{"id":"6","title":"会员日","subtitle":"","picUrl":"http://image.domolife.cn/platform/pR5xxaksWn1595851599096.png","icon":"http://image.domolife.cn/platform/iRck4F62sc1595849791483.png","detail":"","btnText":"去看看","iosLink":"","androidLink":"","webLink":"","wxLink":""},{"id":"7","title":"双倍积分","subtitle":"积分提速，购物后可获得双倍积分","picUrl":"http://image.domolife.cn/platform/pR5xxaksWn1595851599096.png","icon":"http://image.domolife.cn/platform/iRck4F62sc1595849791483.png","detail":"","btnText":"查看我的积分","iosLink":"","androidLink":"","webLink":"","wxLink":""},{"id":"8","title":"专属客服","subtitle":"专属客服通道，vip专线接听","picUrl":"http://image.domolife.cn/platform/pR5xxaksWn1595851599096.png","icon":"http://image.domolife.cn/platform/iRck4F62sc1595849791483.png","detail":"","btnText":"去看看","iosLink":"","androidLink":"","webLink":"","wxLink":""},{"id":"9","title":"退款无忧","subtitle":"无忧退换货，平台承担运费","picUrl":"http://image.domolife.cn/platform/pR5xxaksWn1595851599096.png","icon":"http://image.domolife.cn/platform/iRck4F62sc1595849791483.png","detail":"","btnText":"去购物","iosLink":"","androidLink":"","webLink":"","wxLink":""},{"id":"10","title":"分享有礼","subtitle":"立即分享","picUrl":"http://image.domolife.cn/platform/pR5xxaksWn1595851599096.png","icon":"http://image.domolife.cn/platform/iRck4F62sc1595849791483.png","detail":"","btnText":"去分享","iosLink":"","androidLink":"","webLink":"","wxLink":""}]
     */

    private List<PowerBean> result;

    public List<PowerBean> getPowerList() {
        return result;
    }

    public void setResult(List<PowerBean> result) {
        this.result = result;
    }

    public static class PowerBean {
        /**
         * id : 1
         * title : 会员专享价
         * subtitle : 9.5折起，可叠加券和红包
         * picUrl : http://image.domolife.cn/platform/pR5xxaksWn1595851599096.png
         * icon : http://image.domolife.cn/platform/iRck4F62sc1595849791483.png
         * detail : d
         * btnText : 去看看
         * iosLink : 111
         * androidLink : 222
         * webLink : 33
         * wxLink : 44
         */

        private String id;
        private String title;
        private String subtitle;
        private String picUrl;
        private String icon;
        private String detail;
        private String btnText;
        private String iosLink;
        private String androidLink;
        private String webLink;
        private String wxLink;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
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

        public String getPicUrl() {
            return picUrl;
        }

        public void setPicUrl(String picUrl) {
            this.picUrl = picUrl;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getDetail() {
            return detail;
        }

        public void setDetail(String detail) {
            this.detail = detail;
        }

        public String getBtnText() {
            return btnText;
        }

        public void setBtnText(String btnText) {
            this.btnText = btnText;
        }

        public String getIosLink() {
            return iosLink;
        }

        public void setIosLink(String iosLink) {
            this.iosLink = iosLink;
        }

        public String getAndroidLink() {
            return androidLink;
        }

        public void setAndroidLink(String androidLink) {
            this.androidLink = androidLink;
        }

        public String getWebLink() {
            return webLink;
        }

        public void setWebLink(String webLink) {
            this.webLink = webLink;
        }

        public String getWxLink() {
            return wxLink;
        }

        public void setWxLink(String wxLink) {
            this.wxLink = wxLink;
        }
    }
}
