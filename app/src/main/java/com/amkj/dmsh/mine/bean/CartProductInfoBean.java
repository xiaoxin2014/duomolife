package com.amkj.dmsh.mine.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by xiaoxin on 2019/6/27
 * Version:v4.1.0
 * ClassDescription :组合购商品实体类
 */
public class CartProductInfoBean implements MultiItemEntity, Parcelable {
    /**
     * isPresentProduct:true
     * activityCode : null
     * picUrl : http://img.domolife.cn/platform/20170308/20170308163625953.jpg
     * productId : 5945
     * imgId : 68312
     * price : 100.00
     * saleSkuValue : 默认：默认
     * name : 水彩植物 可爱文艺iPhone6\6s\6p\6sp\7\7p手机壳
     * count : 2
     * newPrice : 100.00
     * saleSkuId : 725
     */

    private boolean isPresentProduct;
    private String activityCode;
    private String picUrl;
    private int productId;
    private String imgId;
    private String price;
    private String saleSkuValue;
    private String name;
    private int count;
    private String newPrice;
    private String saleSkuId;
    //                组合优惠
    private String combineDecreasePrice;
    /**
     * 订单变量
     */
//                订单状态
    private int status;
    //                订单类型
    private String indentType;
    private String orderNo;
    private int orderProductId;
    private int orderRefundProductId;
    private int id;
    /**
     * 1为订单状态 0
     */
    private int itemType;

    public String getCombineDecreasePrice() {
        return combineDecreasePrice;
    }

    public void setCombineDecreasePrice(String combineDecreasePrice) {
        this.combineDecreasePrice = combineDecreasePrice;
    }

    public boolean isPresentProduct() {
        return isPresentProduct;
    }

    public void setPresentProduct(boolean presentProduct) {
        isPresentProduct = presentProduct;
    }

    public String getIndentType() {
        return indentType;
    }

    public void setIndentType(String indentType) {
        this.indentType = indentType;
    }

    public String getActivityCode() {
        return activityCode;
    }

    public void setActivityCode(String activityCode) {
        this.activityCode = activityCode;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getImgId() {
        return imgId;
    }

    public void setImgId(String imgId) {
        this.imgId = imgId;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSaleSkuValue() {
        return saleSkuValue;
    }

    public void setSaleSkuValue(String saleSkuValue) {
        this.saleSkuValue = saleSkuValue;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getNewPrice() {
        return newPrice;
    }

    public void setNewPrice(String newPrice) {
        this.newPrice = newPrice;
    }

    public String getSaleSkuId() {
        return saleSkuId;
    }

    public void setSaleSkuId(String saleSkuId) {
        this.saleSkuId = saleSkuId;
    }

    @Override
    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public int getOrderProductId() {
        return orderProductId;
    }

    public void setOrderProductId(int orderProductId) {
        this.orderProductId = orderProductId;
    }

    public int getOrderRefundProductId() {
        return orderRefundProductId;
    }

    public void setOrderRefundProductId(int orderRefundProductId) {
        this.orderRefundProductId = orderRefundProductId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public CartProductInfoBean() {
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.isPresentProduct ? (byte) 1 : (byte) 0);
        dest.writeString(this.activityCode);
        dest.writeString(this.picUrl);
        dest.writeInt(this.productId);
        dest.writeString(this.imgId);
        dest.writeString(this.price);
        dest.writeString(this.saleSkuValue);
        dest.writeString(this.name);
        dest.writeInt(this.count);
        dest.writeString(this.newPrice);
        dest.writeString(this.saleSkuId);
        dest.writeString(this.combineDecreasePrice);
        dest.writeInt(this.status);
        dest.writeString(this.indentType);
        dest.writeString(this.orderNo);
        dest.writeInt(this.orderProductId);
        dest.writeInt(this.orderRefundProductId);
        dest.writeInt(this.id);
        dest.writeInt(this.itemType);
    }

    protected CartProductInfoBean(Parcel in) {
        this.isPresentProduct = in.readByte() != 0;
        this.activityCode = in.readString();
        this.picUrl = in.readString();
        this.productId = in.readInt();
        this.imgId = in.readString();
        this.price = in.readString();
        this.saleSkuValue = in.readString();
        this.name = in.readString();
        this.count = in.readInt();
        this.newPrice = in.readString();
        this.saleSkuId = in.readString();
        this.combineDecreasePrice = in.readString();
        this.status = in.readInt();
        this.indentType = in.readString();
        this.orderNo = in.readString();
        this.orderProductId = in.readInt();
        this.orderRefundProductId = in.readInt();
        this.id = in.readInt();
        this.itemType = in.readInt();
    }

    public static final Parcelable.Creator<CartProductInfoBean> CREATOR = new Parcelable.Creator<CartProductInfoBean>() {
        @Override
        public CartProductInfoBean createFromParcel(Parcel source) {
            return new CartProductInfoBean(source);
        }

        @Override
        public CartProductInfoBean[] newArray(int size) {
            return new CartProductInfoBean[size];
        }
    };
}
