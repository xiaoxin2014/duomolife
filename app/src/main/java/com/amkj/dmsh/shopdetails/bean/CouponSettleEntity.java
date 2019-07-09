package com.amkj.dmsh.shopdetails.bean;

import com.google.gson.annotations.SerializedName;


import java.util.List;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/8/25
 * class description:优惠券切换 结算数据
 */

public class CouponSettleEntity {

    /**
     * result : {"color":"#FF0033","totalPrice":844.02,"name":"实付：","totalPriceName":"¥844.02"}
     * code : 01
     * msg : 请求成功
     */

    @SerializedName("result")
    private List<PriceInfoBean> couponSettleList;
    private String code;
    private String msg;

    public List<PriceInfoBean> getCouponSettleList() {
        return couponSettleList;
    }

    public void setCouponSettleList(List<PriceInfoBean> couponSettleList) {
        this.couponSettleList = couponSettleList;
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
}
