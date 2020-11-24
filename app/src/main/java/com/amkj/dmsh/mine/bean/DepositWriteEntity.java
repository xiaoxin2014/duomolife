package com.amkj.dmsh.mine.bean;

import com.amkj.dmsh.base.BaseRealNameEntity;

import java.util.List;

/**
 * Created by xiaoxin on 2020/8/21
 * Version:v4.8.0
 */
public class DepositWriteEntity extends BaseRealNameEntity {


    /**
     * sysTime : 2020-10-23 16:23:48
     * skuId : 11490
     * productId : 18305
     * picUrl : http://image.domolife.cn/platform/20200605/20200605155016441.png
     * quantity : 77
     * price : 88
     * count : 1
     * title : 好东东好东东好东东好东东好东东好东东好东东好东东好
     * skutext : 颜色:蓝色
     * deposit : 10
     * depositTotal : 10
     * orderPriceTotal : 68
     * orderPayStartTime : 2020-10-31 00:00:00
     * activityEndTime : 2020-10-31 00:00:00
     * productMsg :
     * realName :
     * idCard :
     * showIdCard :
     * idcardImg1 :
     * idcardImg2 :
     * real : 0
     * prompt :
     * allProductNotBuy : 0
     * showPayTypeList : ["2","1","3"]
     */

    private String sysTime;
    private int skuId;
    private int productId;
    private String picUrl;
    private int quantity;
    private String price;
    private int count;
    private String title;
    private String skutext;
    private String deposit;
    private String depositTotal;
    private String orderPriceTotal;
    private String orderPayStartTime;
    private String activityEndTime;
    private String productMsg;
    private int real;
    private String prompt;
    private int allProductNotBuy;
    private List<String> showPayTypeList;

    public String getSysTime() {
        return sysTime;
    }

    public void setSysTime(String sysTime) {
        this.sysTime = sysTime;
    }

    public int getSkuId() {
        return skuId;
    }

    public void setSkuId(int skuId) {
        this.skuId = skuId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSkutext() {
        return skutext;
    }

    public void setSkutext(String skutext) {
        this.skutext = skutext;
    }

    public String getDeposit() {
        return deposit;
    }

    public void setDeposit(String deposit) {
        this.deposit = deposit;
    }

    public String getDepositTotal() {
        return depositTotal;
    }

    public void setDepositTotal(String depositTotal) {
        this.depositTotal = depositTotal;
    }

    public String getOrderPriceTotal() {
        return orderPriceTotal;
    }

    public void setOrderPriceTotal(String orderPriceTotal) {
        this.orderPriceTotal = orderPriceTotal;
    }

    public String getOrderPayStartTime() {
        return orderPayStartTime;
    }

    public void setOrderPayStartTime(String orderPayStartTime) {
        this.orderPayStartTime = orderPayStartTime;
    }

    public String getActivityEndTime() {
        return activityEndTime;
    }

    public void setActivityEndTime(String activityEndTime) {
        this.activityEndTime = activityEndTime;
    }

    public String getProductMsg() {
        return productMsg;
    }

    public void setProductMsg(String productMsg) {
        this.productMsg = productMsg;
    }

    public boolean isReal() {
        return real==1;
    }

    public void setReal(int real) {
        this.real = real;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public int getAllProductNotBuy() {
        return allProductNotBuy;
    }

    public void setAllProductNotBuy(int allProductNotBuy) {
        this.allProductNotBuy = allProductNotBuy;
    }

    public List<String> getShowPayTypeList() {
        return showPayTypeList;
    }

    public void setShowPayTypeList(List<String> showPayTypeList) {
        this.showPayTypeList = showPayTypeList;
    }
}
