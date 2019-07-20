package com.amkj.dmsh.shopdetails.bean;

import com.amkj.dmsh.base.BaseEntity;

/**
 * Created by xiaoxin on 2019/7/20
 * Version:v4.1.0
 * ClassDescription :退款详情-商品列表实体类
 */
public class RefundDetailProductBean extends BaseEntity {

    /**
     * picUrl :
     * productName :
     * saleSkuValue :
     * count :
     * price :
     * integralPrice :
     */

    private String picUrl;
    private String productName;
    private String saleSkuValue;
    private String count;
    private String price;
    private String integralPrice;

    public RefundDetailProductBean() {
    }

    public RefundDetailProductBean(String picUrl, String productName, String saleSkuValue, String count, String price, String integralPrice) {
        this.picUrl = picUrl;
        this.productName = productName;
        this.saleSkuValue = saleSkuValue;
        this.count = count;
        this.price = price;
        this.integralPrice = integralPrice;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getSaleSkuValue() {
        return saleSkuValue;
    }

    public void setSaleSkuValue(String saleSkuValue) {
        this.saleSkuValue = saleSkuValue;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getIntegralPrice() {
        return integralPrice;
    }

    public void setIntegralPrice(String integralPrice) {
        this.integralPrice = integralPrice;
    }
}
