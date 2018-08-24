package com.amkj.dmsh.shopdetails.integration.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by atd48 on 2016/10/19.
 */
public class CreateIntegralIndentInfo {

    /**
     * no : cX23295X1099X1476859060801
     * shortNo : csX23295X1476859060801
     * payAmount : 0
     * orderId : 251
     * code : 00
     */

    @SerializedName("result")
    private IntegrationIndentBean integrationIndentBean;
    /**
     * result : {"no":"cX23295X1099X1476859060801","shortNo":"csX23295X1476859060801","payAmount":0,"orderId":251,"code":"00"}
     * code : 01
     * msg : 请求成功
     */

    private String code;
    private String msg;

    public IntegrationIndentBean getIntegrationIndentBean() {
        return integrationIndentBean;
    }

    public void setIntegrationIndentBean(IntegrationIndentBean integrationIndentBean) {
        this.integrationIndentBean = integrationIndentBean;
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

    public static class IntegrationIndentBean {
        private String no;
        private String code;
        private String msg;

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

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }
}
