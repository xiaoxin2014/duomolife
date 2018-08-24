package com.amkj.dmsh.shopdetails.bean;

import com.google.gson.annotations.SerializedName;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2016/12/15
 * class description:请输入类描述
 */

public class DirectIndentCountEntity {

    /**
     * result : {"waitEvaluateNum":0,"integralTakeDeliveryNum":0,"waitTakeDeliveryNum":0,"waitPayNum":0,"waitDeliveryNum":0,"waitAfterSaleNum":0}
     * code : 01
     */

    @SerializedName("result")
    private DirectIndentCountBean directIndentCountBean;
    private String code;

    public DirectIndentCountBean getDirectIndentCountBean() {
        return directIndentCountBean;
    }

    public void setDirectIndentCountBean(DirectIndentCountBean directIndentCountBean) {
        this.directIndentCountBean = directIndentCountBean;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public static class DirectIndentCountBean {
        /**
         * waitEvaluateNum : 0
         * integralTakeDeliveryNum : 0
         * waitTakeDeliveryNum : 0
         * waitPayNum : 0
         * waitDeliveryNum : 0
         * waitAfterSaleNum : 0
         */

        private int waitEvaluateNum;
        private int integralTakeDeliveryNum;
        private int waitTakeDeliveryNum;
        private int waitPayNum;
        private int waitDeliveryNum;
        private int waitAfterSaleNum;

        public int getWaitEvaluateNum() {
            return waitEvaluateNum;
        }

        public void setWaitEvaluateNum(int waitEvaluateNum) {
            this.waitEvaluateNum = waitEvaluateNum;
        }

        public int getIntegralTakeDeliveryNum() {
            return integralTakeDeliveryNum;
        }

        public void setIntegralTakeDeliveryNum(int integralTakeDeliveryNum) {
            this.integralTakeDeliveryNum = integralTakeDeliveryNum;
        }

        public int getWaitTakeDeliveryNum() {
            return waitTakeDeliveryNum;
        }

        public void setWaitTakeDeliveryNum(int waitTakeDeliveryNum) {
            this.waitTakeDeliveryNum = waitTakeDeliveryNum;
        }

        public int getWaitPayNum() {
            return waitPayNum;
        }

        public void setWaitPayNum(int waitPayNum) {
            this.waitPayNum = waitPayNum;
        }

        public int getWaitDeliveryNum() {
            return waitDeliveryNum;
        }

        public void setWaitDeliveryNum(int waitDeliveryNum) {
            this.waitDeliveryNum = waitDeliveryNum;
        }

        public int getWaitAfterSaleNum() {
            return waitAfterSaleNum;
        }

        public void setWaitAfterSaleNum(int waitAfterSaleNum) {
            this.waitAfterSaleNum = waitAfterSaleNum;
        }
    }
}
