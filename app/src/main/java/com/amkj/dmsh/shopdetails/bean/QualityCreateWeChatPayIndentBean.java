package com.amkj.dmsh.shopdetails.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by atd48 on 2016/9/24.
 */
public class QualityCreateWeChatPayIndentBean {

    /**
     * payKey : {"appid":"wx219c5d8d43af1622","err_code":null,"err_code_des":null,"noncestr":"3430095c577593aad3c39c701712bcfe","package":"Sign=WXPay","partnerid":"1344721701","prepayid":"wx201610251207011d4543a3200217728586","result_code":"SUCCESS","return_code":"SUCCESS","return_msg":"OK","sign":"2B1D0730A03C7C77871F5B51E102BD92","timestamp":"1477368420"}
     * no : cX23295X1099X1477368420800
     */

    private ResultBean result;
    /**
     * result : {"payKey":{"appid":"wx219c5d8d43af1622","err_code":null,"err_code_des":null,"noncestr":"3430095c577593aad3c39c701712bcfe","package":"Sign=WXPay","partnerid":"1344721701","prepayid":"wx201610251207011d4543a3200217728586","result_code":"SUCCESS","return_code":"SUCCESS","return_msg":"OK","sign":"2B1D0730A03C7C77871F5B51E102BD92","timestamp":"1477368420"},"no":"cX23295X1099X1477368420800"}
     * code : 01
     * msg : 请求成功
     */

    private String code;
    private String msg;

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
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

    public static class ResultBean {

        /**
         * appid : wx219c5d8d43af1622
         * err_code : null
         * err_code_des : null
         * noncestr : 3430095c577593aad3c39c701712bcfe
         * package : Sign=WXPay
         * partnerid : 1344721701
         * prepayid : wx201610251207011d4543a3200217728586
         * result_code : SUCCESS
         * return_code : SUCCESS
         * return_msg : OK
         * sign : 2B1D0730A03C7C77871F5B51E102BD92
         * timestamp : 1477368420
         */

        private PayKeyBean payKey;
        private String no;
        private String code;
        private String msg;

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

        public PayKeyBean getPayKey() {
            return payKey;
        }

        public void setPayKey(PayKeyBean payKey) {
            this.payKey = payKey;
        }

        public String getNo() {
            return no;
        }

        public void setNo(String no) {
            this.no = no;
        }

        public static class PayKeyBean {
            private String appid;
            private String err_code;
            private String err_code_des;
            private String result_code;
            private String return_code;
            private String return_msg;
            private String noncestr;
            @SerializedName("package")
            private String packageX;
            private String partnerid;
            private String prepayid;
            private String sign;
            private String timestamp;
            private String paymentUrl;

            public String getPaymentUrl() {
                return paymentUrl;
            }

            public void setPaymentUrl(String paymentUrl) {
                this.paymentUrl = paymentUrl;
            }

            public String getAppid() {
                return appid;
            }

            public void setAppid(String appid) {
                this.appid = appid;
            }

            public String getErr_code() {
                return err_code;
            }

            public void setErr_code(String err_code) {
                this.err_code = err_code;
            }

            public String getErr_code_des() {
                return err_code_des;
            }

            public void setErr_code_des(String err_code_des) {
                this.err_code_des = err_code_des;
            }

            public String getNoncestr() {
                return noncestr;
            }

            public void setNoncestr(String noncestr) {
                this.noncestr = noncestr;
            }

            public String getPackageX() {
                return packageX;
            }

            public void setPackageX(String packageX) {
                this.packageX = packageX;
            }

            public String getPartnerid() {
                return partnerid;
            }

            public void setPartnerid(String partnerid) {
                this.partnerid = partnerid;
            }

            public String getPrepayid() {
                return prepayid;
            }

            public void setPrepayid(String prepayid) {
                this.prepayid = prepayid;
            }

            public String getResult_code() {
                return result_code;
            }

            public void setResult_code(String result_code) {
                this.result_code = result_code;
            }

            public String getReturn_code() {
                return return_code;
            }

            public void setReturn_code(String return_code) {
                this.return_code = return_code;
            }

            public String getReturn_msg() {
                return return_msg;
            }

            public void setReturn_msg(String return_msg) {
                this.return_msg = return_msg;
            }

            public String getSign() {
                return sign;
            }

            public void setSign(String sign) {
                this.sign = sign;
            }

            public String getTimestamp() {
                return timestamp;
            }

            public void setTimestamp(String timestamp) {
                this.timestamp = timestamp;
            }
        }
    }
}
