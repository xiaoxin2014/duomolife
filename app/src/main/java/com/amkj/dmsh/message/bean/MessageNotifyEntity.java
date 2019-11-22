package com.amkj.dmsh.message.bean;

import com.amkj.dmsh.base.BaseEntity;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/8/1
 * class description:请输入类描述
 */

public class MessageNotifyEntity extends BaseEntity {

    /**
     * result : [{"image":"http://image.domolife.cn/platform/20170726/20170726172119466.jpg","m_obj":"8619","m_content":"您购物车里面的商品降价啦，快去查看吧","m_title":"您购物车里面的商品降价啦，快去查看吧","m_uid":29756,"androidLink":"ShopCarActivity","obj":"goods","iosLink":"DMLShoppingCartViewController","flagName":"宝贝降价提醒","ctime":"2017-08-01 09:56:58.905","linkType":"1","m_type":14,"m_id":578067},{"m_title":"优惠券通知:天降优惠券[268-30]","m_uid":29756,"androidLink":"DirectMyCouponActivity","obj":"coupon","iosLink":"DMLCouponViewController","flagName":"优惠券","ctime":"2017-06-26 12:16:02","linkType":"3","m_type":9,"m_content":"优惠券[268-30]已放入口袋","m_id":573083},{"m_title":"优惠券通知:天降优惠券[阿斯顿]","m_uid":29756,"androidLink":"DirectMyCouponActivity","obj":"coupon","iosLink":"DMLCouponViewController","flagName":"优惠券","ctime":"2017-03-28 22:27:52","linkType":"3","m_type":9,"m_content":"优惠券[阿斯顿]已放入口袋","m_id":552440},{"m_title":"优惠券通知:天降优惠券[40优惠券]","m_uid":29756,"androidLink":"DirectMyCouponActivity","obj":"coupon","iosLink":"DMLCouponViewController","flagName":"优惠券","ctime":"2017-03-28 21:44:28","linkType":"3","m_type":9,"m_content":"优惠券[40优惠券]已放入口袋","m_id":528740},{"m_title":"优惠券通知:天降优惠券[40优惠券]","m_uid":29756,"androidLink":"DirectMyCouponActivity","obj":"coupon","iosLink":"DMLCouponViewController","flagName":"优惠券","ctime":"2017-03-28 21:44:21","linkType":"3","m_type":9,"m_content":"优惠券[40优惠券]已放入口袋","m_id":524615},{"m_title":"优惠券通知:天降优惠券[新人礼包99-10元券]","m_uid":29756,"androidLink":"DirectMyCouponActivity","obj":"coupon","iosLink":"DMLCouponViewController","flagName":"优惠券","ctime":"2017-02-25 22:28:07","linkType":"3","m_type":9,"m_content":"优惠券[新人礼包99-10元券]已放入口袋","m_id":193282},{"m_title":"优惠券通知:天降优惠券[新人礼包268-30]","m_uid":29756,"androidLink":"DirectMyCouponActivity","obj":"coupon","iosLink":"DMLCouponViewController","flagName":"优惠券","ctime":"2017-02-25 22:26:13","linkType":"3","m_type":9,"m_content":"优惠券[新人礼包268-30]已放入口袋","m_id":181934},{"m_title":"优惠券通知:天降优惠券[新人礼包99-10]","m_uid":29756,"androidLink":"DirectMyCouponActivity","obj":"coupon","iosLink":"DMLCouponViewController","flagName":"优惠券","ctime":"2017-02-25 22:24:35","linkType":"3","m_type":9,"m_content":"优惠券[新人礼包99-10]已放入口袋","m_id":170591},{"m_title":"优惠券通知:天降优惠券[新人礼包499-50]","m_uid":29756,"androidLink":"DirectMyCouponActivity","obj":"coupon","iosLink":"DMLCouponViewController","flagName":"优惠券","ctime":"2017-02-25 22:22:45","linkType":"3","m_type":9,"m_content":"优惠券[新人礼包499-50]已放入口袋","m_id":159223},{"m_title":"优惠券通知:天降优惠券[新人礼包99-10元券]","m_uid":29756,"androidLink":"DirectMyCouponActivity","obj":"coupon","iosLink":"DMLCouponViewController","flagName":"优惠券","ctime":"2017-02-25 19:03:37","linkType":"3","m_type":9,"m_content":"优惠券[新人礼包99-10元券]已放入口袋","m_id":147930}]
     * msg : 请求成功
     * code : 01
     */
    @SerializedName("result")
    private List<MessageNotifyBean> messageNotifyList;

    public List<MessageNotifyBean> getMessageNotifyList() {
        return messageNotifyList;
    }

    public void setMessageNotifyList(List<MessageNotifyBean> messageNotifyList) {
        this.messageNotifyList = messageNotifyList;
    }

    public static class MessageNotifyBean {
        /**
         * image : http://image.domolife.cn/platform/20170726/20170726172119466.jpg
         * m_obj : 8619
         * m_content : 您购物车里面的商品降价啦，快去查看吧
         * m_title : 您购物车里面的商品降价啦，快去查看吧
         * m_uid : 29756
         * androidLink : ShopCarActivity
         * obj : goods
         * iosLink : DMLShoppingCartViewController
         * flagName : 宝贝降价提醒
         * ctime : 2017-08-01 09:56:58.905
         * linkType : 1
         * m_type : 14
         * m_id : 578067
         */
        private int notice_id;
        private int is_read;
        private String image;
        private String m_obj;
        private String m_content;
        private String m_title;
        private int m_uid;
        private String androidLink;
        private String obj;
        private String iosLink;
        private String flagName;
        private String ctime;
        private String linkType;
        private int m_type;
        private int m_id;
        @SerializedName("json")
        private ProductPriceBean productPriceBean;
        private String title;
        private String buttonName;


        public int getNotice_id() {
            return notice_id;
        }

        public void setNotice_id(int notice_id) {
            this.notice_id = notice_id;
        }

        public boolean isRead() {
            return is_read == 1;
        }

        public String getButtonName() {
            return buttonName;
        }

        public void setButtonName(String buttonName) {
            this.buttonName = buttonName;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getM_obj() {
            return m_obj;
        }

        public void setM_obj(String m_obj) {
            this.m_obj = m_obj;
        }

        public String getM_content() {
            return m_content;
        }

        public void setM_content(String m_content) {
            this.m_content = m_content;
        }

        public String getM_title() {
            return m_title;
        }

        public void setM_title(String m_title) {
            this.m_title = m_title;
        }

        public int getM_uid() {
            return m_uid;
        }

        public void setM_uid(int m_uid) {
            this.m_uid = m_uid;
        }

        public String getAndroidLink() {
            return androidLink;
        }

        public void setAndroidLink(String androidLink) {
            this.androidLink = androidLink;
        }

        public String getObj() {
            return obj;
        }

        public void setObj(String obj) {
            this.obj = obj;
        }

        public String getIosLink() {
            return iosLink;
        }

        public void setIosLink(String iosLink) {
            this.iosLink = iosLink;
        }

        public String getFlagName() {
            return flagName;
        }

        public void setFlagName(String flagName) {
            this.flagName = flagName;
        }

        public String getCtime() {
            return ctime;
        }

        public void setCtime(String ctime) {
            this.ctime = ctime;
        }

        public String getLinkType() {
            return linkType;
        }

        public void setLinkType(String linkType) {
            this.linkType = linkType;
        }

        public int getM_type() {
            return m_type;
        }

        public void setM_type(int m_type) {
            this.m_type = m_type;
        }

        public int getM_id() {
            return m_id;
        }

        public void setM_id(int m_id) {
            this.m_id = m_id;
        }

        public ProductPriceBean getProductPriceBean() {
            return productPriceBean;
        }

        public void setProductPriceBean(ProductPriceBean productPriceBean) {
            this.productPriceBean = productPriceBean;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public static class ProductPriceBean {
            /**
             * nowPrice : 666.00
             * oldPrice : 880.00
             */

            private String nowPrice;
            private String oldPrice;

            public static ProductPriceBean objectFromData(String str) {

                return new Gson().fromJson(str, ProductPriceBean.class);
            }

            public String getNowPrice() {
                return nowPrice;
            }

            public void setNowPrice(String nowPrice) {
                this.nowPrice = nowPrice;
            }

            public String getOldPrice() {
                return oldPrice;
            }

            public void setOldPrice(String oldPrice) {
                this.oldPrice = oldPrice;
            }
        }
    }
}
