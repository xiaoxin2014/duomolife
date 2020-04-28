package com.amkj.dmsh.shopdetails.bean;

import android.text.TextUtils;

/**
 * Created by xiaoxin on 2020/3/28
 * Version:v4.5.0
 * ClassDescription :退款物流
 */
public class RefundGoodsAddressInfoBean {
    /**
     * address : string
     * consignee : string
     * expressCompany : string
     * expressNo : string
     * mobile : string
     */

    private String address;
    private String consignee;
    private String expressCompany;
    private String expressNo;
    private String mobile;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getConsignee() {
        return !TextUtils.isEmpty(consignee) ? consignee + "  " : "";
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    public String getExpressCompany() {
        return expressCompany;
    }

    public void setExpressCompany(String expressCompany) {
        this.expressCompany = expressCompany;
    }

    public String getExpressNo() {
        return expressNo;
    }

    public void setExpressNo(String expressNo) {
        this.expressNo = expressNo;
    }

    public String getMobile() {
        return !TextUtils.isEmpty(mobile) ? "  " + mobile : "";
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
