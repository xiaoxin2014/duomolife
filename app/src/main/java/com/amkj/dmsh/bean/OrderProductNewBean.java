package com.amkj.dmsh.bean;

import android.text.TextUtils;

import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.shopdetails.bean.PresentProductOrder;
import com.google.gson.annotations.SerializedName;

/**
 * Created by xiaoxin on 2020/3/25
 * Version:v4.5.0
 * ClassDescription :订单相关商品条目
 **/
public class OrderProductNewBean {
    /**
     * orderProductId : 740454
     * productId : 4316
     * picUrl : http://image.domolife.cn/platform/bJFJ7Am2tZ.jpg
     * count : 1
     * productName : 日本FaSoLa文胸洗衣袋
     * price : 0.01
     * totalPrice : 0.01
     * saleSkuValue : 颜色:方形丝网式洗衣袋
     * statusText : 待收货
     * status : 20
     * predictTimeText :
     * remindText :
     * presentProductOrder : null
     */

    private String orderProductId;
    @SerializedName(value = "id", alternate = "productId")
    private String id;
    @SerializedName(value = "picUrl", alternate = "productImg")
    private String picUrl;
    private String count;
    private String productName;
    private String price;
    private String totalPrice;
    private String saleSkuValue;
    private String statusText;
    private String status;
    private String orderNo;
    private String remindText;//提示语
    private PresentProductOrder presentProductOrder;//赠品信息

    //订单详情专用字段
    private String isShowRefundApply;//是否显示退款按钮  0不显示 1显示
    private String refundNo;//是否显示售后按钮
    private String activityCode;
    private String activityTag;
    private boolean showLine;

    //售后详情专用字段
    private String integralPrice;

    //选择退款商品专用字段
    private boolean isChecked;

    //售后列表专用字段
    private String refundPrice;


    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getRefundPrice() {
        return refundPrice;
    }

    public void setRefundPrice(String refundPrice) {
        this.refundPrice = refundPrice;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public int getIntegralPrice() {
        return ConstantMethod.getStringChangeIntegers(integralPrice);
    }

    public void setIntegralPrice(String integralPrice) {
        this.integralPrice = integralPrice;
    }

    public boolean getShowLine() {
        return showLine;
    }

    public void setShowLine(boolean showLine) {
        this.showLine = showLine;
    }

    public String getActivityCode() {
        return activityCode;
    }

    public void setActivityCode(String activityCode) {
        this.activityCode = activityCode;
    }

    public String getActivityTag() {
        return activityTag;
    }

    public void setActivityTag(String activityTag) {
        this.activityTag = activityTag;
    }

    public boolean isShowRefundApply() {
        return "1".equals(isShowRefundApply);
    }

    public void setIsShowRefundApply(String isShowRefundApply) {
        this.isShowRefundApply = isShowRefundApply;
    }

    public String getRefundNo() {
        return refundNo;
    }

    public void setRefundNo(String refundNo) {
        this.refundNo = refundNo;
    }

    public void setPresentProductOrder(PresentProductOrder presentProductOrder) {
        this.presentProductOrder = presentProductOrder;
    }

    public String getOrderProductId() {
        return orderProductId;
    }

    public void setOrderProductId(String orderProductId) {
        this.orderProductId = orderProductId;
    }

    public int getProductId() {
        return ConstantMethod.getStringChangeIntegers(id);
    }

    public void setProductId(String productId) {
        this.id = productId;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public int getCount() {
        return ConstantMethod.getStringChangeIntegers(count);
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getPrice() {
        return !TextUtils.isEmpty(price) ? price : totalPrice;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getSaleSkuValue() {
        return saleSkuValue;
    }

    public void setSaleSkuValue(String saleSkuValue) {
        this.saleSkuValue = saleSkuValue;
    }

    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    public int getStatus() {
        return ConstantMethod.getStringChangeIntegers(status);
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemindText() {
        return remindText;
    }

    public void setRemindText(String remindText) {
        this.remindText = remindText;
    }

    public PresentProductOrder getPresentProductOrder() {
        return presentProductOrder;
    }
}
