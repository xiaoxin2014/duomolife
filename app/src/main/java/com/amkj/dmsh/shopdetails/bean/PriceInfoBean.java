package com.amkj.dmsh.shopdetails.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by xiaoxin on 2019/7/4
 * Version:v4.1.0
 * ClassDescription :订单价格明细
 */
public class PriceInfoBean {
    /**
     * color : #000000
     * totalPrice : 725.6
     * name : 商品总额
     * totalPriceName : ¥725.60
     */

    private String color;
    private String name;
    private String desc;
    @SerializedName(value = "totalPriceName", alternate = "priceText")
    private String totalPriceName;
    private String flag;


    public PriceInfoBean(String name,String desc, String totalPriceName) {
        this.name = name;
        this.desc = desc;
        this.totalPriceName = totalPriceName;
    }


    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public boolean isEcm() {
        return "ecm".equals(flag);
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTotalPriceName() {
        return totalPriceName;
    }

    public void setTotalPriceName(String totalPriceName) {
        this.totalPriceName = totalPriceName;
    }
}
