package com.amkj.dmsh.shopdetails.bean;

import com.amkj.dmsh.base.BaseEntity;
import com.google.gson.annotations.SerializedName;

import java.util.Map;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/4/25
 * class description:请输入类描述
 */

public class IndentInvoiceEntity extends BaseEntity {

    /**
     * result : {"invoice":{"imgUrl":"http://img.domolife.cn/platform/M6wBbjNxbX.png","amount":0,"orderNo":"cX27863X4763X1492755883425","id":7,"title":"测试","type":1,"content":"明细","status":2},"type":{"1":"电子发票","2":"纸质发票 "},"status":{"0":"未开","1":"待开","2":"已开 "}}
     * msg : 请求成功
     * code : 01
     */

    @SerializedName("result")
    private IndentInvoiceBean indentInvoiceBean;

    public IndentInvoiceBean getIndentInvoiceBean() {
        return indentInvoiceBean;
    }

    public void setIndentInvoiceBean(IndentInvoiceBean indentInvoiceBean) {
        this.indentInvoiceBean = indentInvoiceBean;
    }

    public static class IndentInvoiceBean {
        /**
         * invoice : {"imgUrl":"http://img.domolife.cn/platform/M6wBbjNxbX.png","amount":0,"orderNo":"cX27863X4763X1492755883425","id":7,"title":"测试","type":1,"content":"明细","status":2}
         * type : {"1":"电子发票","2":"纸质发票 "}
         * status : {"0":"未开","1":"待开","2":"已开 "}
         */

        private InvoiceBean invoice;
        private Map<String, String> type;
        private Map<String, String> status;

        public InvoiceBean getInvoice() {
            return invoice;
        }

        public void setInvoice(InvoiceBean invoice) {
            this.invoice = invoice;
        }

        public Map<String, String> getType() {
            return type;
        }

        public void setType(Map<String, String> type) {
            this.type = type;
        }

        public Map<String, String> getStatus() {
            return status;
        }

        public void setStatus(Map<String, String> status) {
            this.status = status;
        }

        public static class InvoiceBean {
            /**
             * imgUrl : http://img.domolife.cn/platform/M6wBbjNxbX.png
             * amount : 0
             * orderNo : cX27863X4763X1492755883425
             * id : 7
             * title : 测试
             * type : 1
             * content : 明细
             * status : 2
             * taxpayer_on：纳税人识别号
             * content：发票内容
             * address：地址
             * mobile：电话
             * bankOfDeposit：开户行
             * account：账号
             */

            private String imgUrl;
            private int amount;
            private String orderNo;
            private int id;
            private String title;
            private int type;
            private String content;
            private String taxpayer_on;
            private String address;
            private String mobile;
            private String bankOfDeposit;
            private String account;
            private int status;

            public String getImgUrl() {
                return imgUrl;
            }

            public void setImgUrl(String imgUrl) {
                this.imgUrl = imgUrl;
            }

            public int getAmount() {
                return amount;
            }

            public void setAmount(int amount) {
                this.amount = amount;
            }

            public String getOrderNo() {
                return orderNo;
            }

            public void setOrderNo(String orderNo) {
                this.orderNo = orderNo;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getTaxpayer_on() {
                return taxpayer_on;
            }

            public void setTaxpayer_on(String taxpayer_on) {
                this.taxpayer_on = taxpayer_on;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public String getMobile() {
                return mobile;
            }

            public void setMobile(String mobile) {
                this.mobile = mobile;
            }

            public String getBankOfDeposit() {
                return bankOfDeposit;
            }

            public void setBankOfDeposit(String bankOfDeposit) {
                this.bankOfDeposit = bankOfDeposit;
            }

            public String getAccount() {
                return account;
            }

            public void setAccount(String account) {
                this.account = account;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }
        }
    }
}
