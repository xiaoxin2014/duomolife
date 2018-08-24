package com.amkj.dmsh.shopdetails.bean;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/2/2
 * version 3.9
 * class description：自定义售后类型
 */

public class RefundTypeBean {
    private String refundType;
    private String refundDate;

    public RefundTypeBean() {
    }

    public RefundTypeBean(String refundType, String refundDate) {
        this.refundType = refundType;
        this.refundDate = refundDate;
    }

    public String getRefundType() {
        return refundType;
    }

    public void setRefundType(String refundType) {
        this.refundType = refundType;
    }

    public String getRefundDate() {
        return refundDate;
    }

    public void setRefundDate(String refundDate) {
        this.refundDate = refundDate;
    }
}
