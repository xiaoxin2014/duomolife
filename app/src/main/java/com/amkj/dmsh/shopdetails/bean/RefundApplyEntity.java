package com.amkj.dmsh.shopdetails.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/8/23
 * class description:申请退款
 */

public class RefundApplyEntity {

    /**
     * result : {"picUrl":"http://image.domolife.cn/platform/20170418/20170418155628524.jpg","productSkuValue":"颜色:面膜","refundType":{"1":"仅退款"},"moneyAndGoodsRefundReason":[{"reason":"质量问题","id":64},{"reason":"不想要了","id":65},{"reason":"多拍/拍错","id":66},{"reason":"其它原因","id":67},{"reason":"商品尺码与描述不一致","id":60},{"reason":"卖家发错货/少件、破损","id":61},{"reason":"尺码拍错/不喜欢/ ","id":62},{"reason":"颜色/款式/图案/材质/面料与描述不符","id":63}],"moneyRefundReason":[{"reason":"尺码拍错/不喜欢","id":32},{"reason":"颜色/款式/图案/材质/面料与描述不符","id":33},{"reason":"其它原因","id":34},{"reason":"商品尺码与描述不一致","id":30},{"reason":"卖家发错货/少件、破损","id":31}],"price":"0.01","count":2,"waitDeliveryRefundReason":[{"reason":"多拍/拍错","id":1},{"reason":"不想要了","id":2},{"reason":"卖家未按规定时间发货","id":3},{"reason":"其它原因","id":4}],"refundPrice":"0.01","productName":"CHEN川蕾丝皙嫩抚纹面膜30gx4片"}
     * msg : 请求成功
     * code : 01
     */

    @SerializedName("result")
    private RefundApplyBean refundApplyBean;
    private String msg;
    private String code;

    public RefundApplyBean getRefundApplyBean() {
        return refundApplyBean;
    }

    public void setRefundApplyBean(RefundApplyBean refundApplyBean) {
        this.refundApplyBean = refundApplyBean;
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

    public static class RefundApplyBean {
        /**
         * picUrl : http://image.domolife.cn/platform/20170418/20170418155628524.jpg
         * productSkuValue : 颜色:面膜
         * refundType : {"1":"仅退款"}
         * moneyAndGoodsRefundReason : [{"reason":"质量问题","id":64},{"reason":"不想要了","id":65},{"reason":"多拍/拍错","id":66},{"reason":"其它原因","id":67},{"reason":"商品尺码与描述不一致","id":60},{"reason":"卖家发错货/少件、破损","id":61},{"reason":"尺码拍错/不喜欢/ ","id":62},{"reason":"颜色/款式/图案/材质/面料与描述不符","id":63}]
         * moneyRefundReason : [{"reason":"尺码拍错/不喜欢","id":32},{"reason":"颜色/款式/图案/材质/面料与描述不符","id":33},{"reason":"其它原因","id":34},{"reason":"商品尺码与描述不一致","id":30},{"reason":"卖家发错货/少件、破损","id":31}]
         * price : 0.01
         * count : 2
         * waitDeliveryRefundReason : [{"reason":"多拍/拍错","id":1},{"reason":"不想要了","id":2},{"reason":"卖家未按规定时间发货","id":3},{"reason":"其它原因","id":4}]
         * refundPrice : 0.01
         * productName : CHEN川蕾丝皙嫩抚纹面膜30gx4片
         */
        private Map<String, String> refundType;
        private String refundPrice;
        private int refundIntegralPrice;
        private String refundMsg;
        private List<MoneyAndGoodsRefundReasonBean> moneyAndGoodsRefundReason;
        private List<MoneyRefundReasonBean> moneyRefundReason;
        private List<WaitDeliveryRefundReasonBean> waitDeliveryRefundReason;

        public int getRefundIntegralPrice() {
            return refundIntegralPrice;
        }

        public void setRefundIntegralPrice(int refundIntegralPrice) {
            this.refundIntegralPrice = refundIntegralPrice;
        }

        public String getRefundMsg() {
            return refundMsg;
        }

        public void setRefundMsg(String refundMsg) {
            this.refundMsg = refundMsg;
        }

        public Map<String, String> getRefundType() {
            return refundType;
        }

        public void setRefundType(Map<String, String> refundType) {
            this.refundType = refundType;
        }



        public String getRefundPrice() {
            return refundPrice;
        }

        public void setRefundPrice(String refundPrice) {
            this.refundPrice = refundPrice;
        }

        public List<MoneyAndGoodsRefundReasonBean> getMoneyAndGoodsRefundReason() {
            return moneyAndGoodsRefundReason;
        }

        public void setMoneyAndGoodsRefundReason(List<MoneyAndGoodsRefundReasonBean> moneyAndGoodsRefundReason) {
            this.moneyAndGoodsRefundReason = moneyAndGoodsRefundReason;
        }

        public List<MoneyRefundReasonBean> getMoneyRefundReason() {
            return moneyRefundReason;
        }

        public void setMoneyRefundReason(List<MoneyRefundReasonBean> moneyRefundReason) {
            this.moneyRefundReason = moneyRefundReason;
        }

        public List<WaitDeliveryRefundReasonBean> getWaitDeliveryRefundReason() {
            return waitDeliveryRefundReason;
        }

        public void setWaitDeliveryRefundReason(List<WaitDeliveryRefundReasonBean> waitDeliveryRefundReason) {
            this.waitDeliveryRefundReason = waitDeliveryRefundReason;
        }

        public static class MoneyAndGoodsRefundReasonBean {
            /**
             * reason : 质量问题
             * id : 64
             */

            private String reason;
            private int id;

            public String getReason() {
                return reason;
            }

            public void setReason(String reason) {
                this.reason = reason;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }
        }

        public static class MoneyRefundReasonBean {
            /**
             * reason : 尺码拍错/不喜欢
             * id : 32
             */

            private String reason;
            private int id;

            public String getReason() {
                return reason;
            }

            public void setReason(String reason) {
                this.reason = reason;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }
        }

        public static class WaitDeliveryRefundReasonBean {
            /**
             * reason : 多拍/拍错
             * id : 1
             */

            private String reason;
            private int id;

            public String getReason() {
                return reason;
            }

            public void setReason(String reason) {
                this.reason = reason;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }
        }
    }
}
