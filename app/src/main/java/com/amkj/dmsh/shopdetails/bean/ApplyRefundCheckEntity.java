package com.amkj.dmsh.shopdetails.bean;

import com.google.gson.annotations.SerializedName;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/8/24
 * class description:申请退款查询
 */

public class ApplyRefundCheckEntity {

    /**
     * result : {"noticeFlagType":2,"refundUserCouponId":-1,"refundPrice":1,"msg":"余下商品不满足优惠券使用条件，退款需扣除全部优惠哦"}
     * code : 01
     * msg : 请求成功
     */

    @SerializedName("result")
    private ApplyRefundCheckBean applyRefundCheckBean;
    private String code;
    private String msg;

    public ApplyRefundCheckBean getApplyRefundCheckBean() {
        return applyRefundCheckBean;
    }

    public void setApplyRefundCheckBean(ApplyRefundCheckBean applyRefundCheckBean) {
        this.applyRefundCheckBean = applyRefundCheckBean;
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

    public static class ApplyRefundCheckBean {
        /**
         * noticeFlagType : 2
         * refundUserCouponId : -1
         * refundPrice : 1
         * msg : 余下商品不满足优惠券使用条件，退款需扣除全部优惠哦
         */

        private int noticeFlagType;
        private int refundUserCouponId;
        private String refundPrice;
        private String msg;

        public int getNoticeFlagType() {
            return noticeFlagType;
        }

        public void setNoticeFlagType(int noticeFlagType) {
            this.noticeFlagType = noticeFlagType;
        }

        public int getRefundUserCouponId() {
            return refundUserCouponId;
        }

        public void setRefundUserCouponId(int refundUserCouponId) {
            this.refundUserCouponId = refundUserCouponId;
        }

        public String getRefundPrice() {
            return refundPrice;
        }

        public void setRefundPrice(String refundPrice) {
            this.refundPrice = refundPrice;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }
}
