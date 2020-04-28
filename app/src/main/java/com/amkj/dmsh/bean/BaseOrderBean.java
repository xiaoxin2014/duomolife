package com.amkj.dmsh.bean;

import com.amkj.dmsh.shopdetails.bean.ButtonListBean;

import java.util.List;

/**
 * Created by xiaoxin on 2020/3/25
 * Version:v4.5.0
 */
public class BaseOrderBean {
    private String orderNo;
    private List<OrderProductNewBean> orderProductList;
    private List<ButtonListBean> buttonList;
    private String maxRewardTip;

    public String getMaxRewardTip() {
        return maxRewardTip;
    }

    public void setMaxRewardTip(String maxRewardTip) {
        this.maxRewardTip = maxRewardTip;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
    public List<ButtonListBean> getButtonList() {
        return buttonList;
    }

    public void setButtonList(List<ButtonListBean> buttonList) {
        this.buttonList = buttonList;
    }

    public List<OrderProductNewBean> getOrderProductList() {
        return orderProductList;
    }

    public void setOrderProductList(List<OrderProductNewBean> orderProductList) {
        this.orderProductList = orderProductList;
    }

}
