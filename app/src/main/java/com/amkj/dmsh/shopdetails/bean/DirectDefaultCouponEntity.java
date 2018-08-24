package com.amkj.dmsh.shopdetails.bean;

import com.google.gson.annotations.SerializedName;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2016/11/21
 * class description:请输入类描述
 */

public class DirectDefaultCouponEntity {

    /**
     * result : {"status":0,"start_fee":5,"type":2,"product_ids":"0","id":3,"amount":5,"title":"11月新注册规则","end_time":"2016-11-30 00:00:00","create_time":"2016-11-03 15:44:00","start_time":"2016-11-01 00:00","user_id":23317,"merchant_id":"25af1919042c4808b37d259e06dda71a","product_coupon_id":3,"mobile":"13713732676"}
     * code : 01
     * msg : 请求成功
     */

    @SerializedName("result")
    private DirectDefaultCouponBean directDefaultCouponBean;
    private String code;
    private String msg;

    public DirectDefaultCouponBean getDirectDefaultCouponBean() {
        return directDefaultCouponBean;
    }

    public void setDirectDefaultCouponBean(DirectDefaultCouponBean directDefaultCouponBean) {
        this.directDefaultCouponBean = directDefaultCouponBean;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static class DirectDefaultCouponBean {
        /**
         * status : 0
         * start_fee : 5
         * type : 2
         * product_ids : 0
         * id : 3
         * amount : 5
         * title : 11月新注册规则
         * end_time : 2016-11-30 00:00:00
         * create_time : 2016-11-03 15:44:00
         * start_time : 2016-11-01 00:00
         * user_id : 23317
         * merchant_id : 25af1919042c4808b37d259e06dda71a
         * product_coupon_id : 3
         * mobile : 13713732676
         */
//{"result":{"status":0,"start_fee":0,"type":0,"product_ids":"0","id":15,"amount":0.1,"title":"测试","end_time":"2016-11-23 00:00:00","create_time":"2016-11-22 14:45:36","start_time":"2016-11-22 11:51:28","merchant_id":"25af1919042c4808b37d259e06dda71a","product_coupon_id":6,"mobile":"13751077044"},"code":"01","msg":"请求成功"}
        private int status;
        private int start_fee;
        private String type;
        private String product_ids;
        private int id;
        private float amount;
        private String title;
        private String end_time;
        private String create_time;
        private String start_time;
        private int user_id;
        private String merchant_id;
        private int product_coupon_id;
        private String mobile;

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getStart_fee() {
            return start_fee;
        }

        public void setStart_fee(int start_fee) {
            this.start_fee = start_fee;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getProduct_ids() {
            return product_ids;
        }

        public void setProduct_ids(String product_ids) {
            this.product_ids = product_ids;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public float getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getEnd_time() {
            return end_time;
        }

        public void setEnd_time(String end_time) {
            this.end_time = end_time;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getStart_time() {
            return start_time;
        }

        public void setStart_time(String start_time) {
            this.start_time = start_time;
        }

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public String getMerchant_id() {
            return merchant_id;
        }

        public void setMerchant_id(String merchant_id) {
            this.merchant_id = merchant_id;
        }

        public int getProduct_coupon_id() {
            return product_coupon_id;
        }

        public void setProduct_coupon_id(int product_coupon_id) {
            this.product_coupon_id = product_coupon_id;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }
    }
}
