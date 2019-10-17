package com.amkj.dmsh.shopdetails.bean;

import com.amkj.dmsh.base.BaseEntity;

/**
 * Created by xiaoxin on 2019/10/17
 * Version:v4.3.1
 * ClassDescription :积分订单
 */
public class IntegrationIndentEntity extends BaseEntity {


    /**
     * result : {"code":"","msg":"","no":"cX266978X87291X1571275119381","payKey":"{\"appid\":\"wx219c5d8d43af1622\",\"noncestr\":\"5UT2VFyq9QDh3iln\",\"package\":\"Sign=WXPay\",\"partnerid\":\"1344721701\",\"prepayid\":\"wx170918398268057068dea0ed1558890400\",\"sign\":\"70DA74F646B6E2D3A6F8EA46BB959321\",\"timestamp\":\"1571275119\"}"}
     */

    private IntegrationIndentBean result;

    public IntegrationIndentBean getResult() {
        return result;
    }

    public void setResult(IntegrationIndentBean result) {
        this.result = result;
    }

    public static class IntegrationIndentBean {
        /**
         * code :
         * msg :
         * no : cX266978X87291X1571275119381
         * payKey : {"appid":"wx219c5d8d43af1622","noncestr":"5UT2VFyq9QDh3iln","package":"Sign=WXPay","partnerid":"1344721701","prepayid":"wx170918398268057068dea0ed1558890400","sign":"70DA74F646B6E2D3A6F8EA46BB959321","timestamp":"1571275119"}
         */

        private String code;
        private String msg;
        private String no;
        private String payKey;

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

        public String getNo() {
            return no;
        }

        public void setNo(String no) {
            this.no = no;
        }

        public String getPayKey() {
            return payKey;
        }

        public void setPayKey(String payKey) {
            this.payKey = payKey;
        }
    }
}
