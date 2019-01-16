package com.amkj.dmsh.shopdetails.bean;

import com.google.gson.annotations.SerializedName;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2019/1/10
 * version 3.2.0
 * class description:创建订单-银联支付返回数据
 */
public class QualityCreateUnionPayIndentEntity {
    /***
     先判断code是否为成功，如果是成功的，取payKey中的paymentUrl在新页面进行跳转即可
     **/
    /**
     * result : {"no":"cX44951X39432X1546565287970","payKey":{"code":"01","errorMessage":"","paymentUrl":"https://pay.abchina.com/ebus/NotCheckStatus/PaymentModeUnionPayAct.ebf?TOKEN=15465652898458156627","returnCode":"0000"}}
     * msg : 请求成功
     * code : 01
     */

    @SerializedName("result")
    private QualityCreateUnionPayIndentBean qualityCreateUnionPayIndent;
    private String msg;
    private String code;

    public QualityCreateUnionPayIndentBean getQualityCreateUnionPayIndent() {
        return qualityCreateUnionPayIndent;
    }

    public void setQualityCreateUnionPayIndent(QualityCreateUnionPayIndentBean qualityCreateUnionPayIndent) {
        this.qualityCreateUnionPayIndent = qualityCreateUnionPayIndent;
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

    public static class QualityCreateUnionPayIndentBean {
        /**
         * no : cX44951X39432X1546565287970
         * payKey : {"code":"01","errorMessage":"","paymentUrl":"https://pay.abchina.com/ebus/NotCheckStatus/PaymentModeUnionPayAct.ebf?TOKEN=15465652898458156627","returnCode":"0000"}
         */

        private String no;
        private String msg;
        private String code;
        @SerializedName("payKey")
        private PayKeyBean payKeyBean;

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

        public String getNo() {
            return no;
        }

        public void setNo(String no) {
            this.no = no;
        }

        public PayKeyBean getPayKeyBean() {
            return payKeyBean;
        }

        public void setPayKeyBean(PayKeyBean payKeyBean) {
            this.payKeyBean = payKeyBean;
        }

        public static class PayKeyBean {
            /**
             * code : 01
             * errorMessage :
             * paymentUrl : https://pay.abchina.com/ebus/NotCheckStatus/PaymentModeUnionPayAct.ebf?TOKEN=15465652898458156627
             * returnCode : 0000
             */
            private String paymentUrl;
            private String returnCode;

            public String getPaymentUrl() {
                return paymentUrl;
            }

            public void setPaymentUrl(String paymentUrl) {
                this.paymentUrl = paymentUrl;
            }

            public String getReturnCode() {
                return returnCode;
            }

            public void setReturnCode(String returnCode) {
                this.returnCode = returnCode;
            }
        }
    }
    /***
     先判断code是否为成功，如果是成功的，取payKey中的paymentUrl在新页面进行跳转即可
     **/
}
