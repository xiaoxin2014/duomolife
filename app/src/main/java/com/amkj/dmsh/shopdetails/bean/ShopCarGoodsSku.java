package com.amkj.dmsh.shopdetails.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by atd48 on 2016/10/21.
 */
public class ShopCarGoodsSku implements Parcelable {
    private int saleSkuId;
    private int count;
    private double price;
    private int productId;
    /**
     * 积分商品专属
     */
    private String moneyPrice;
    private String integralName;
    private int integralProductType;
    private int integralType;

    private String valuesName;
    private String picUrl;
    private String presentIds;
    private String proType;
    private String activityCode;
    private int quantity;

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getIntegralType() {
        return integralType;
    }

    public void setIntegralType(int integralType) {
        this.integralType = integralType;
    }

    public String getIntegralName() {
        return integralName;
    }

    public void setIntegralName(String integralName) {
        this.integralName = integralName;
    }

    public String getMoneyPrice() {
        return moneyPrice;
    }

    public void setMoneyPrice(String moneyPrice) {
        this.moneyPrice = moneyPrice;
    }

    public int getIntegralProductType() {
        return integralProductType;
    }

    public void setIntegralProductType(int integralProductType) {
        this.integralProductType = integralProductType;
    }

    public String getActivityCode() {
        return activityCode;
    }

    public void setActivityCode(String activityCode) {
        this.activityCode = activityCode;
    }

    public String getProType() {
        return proType;
    }

    public void setProType(String proType) {
        this.proType = proType;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getValuesName() {
        return valuesName;
    }

    public void setValuesName(String valuesName) {
        this.valuesName = valuesName;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getPresentIds() {
        return presentIds;
    }

    public void setPresentIds(String presentIds) {
        this.presentIds = presentIds;
    }

    public int getSaleSkuId() {
        return saleSkuId;
    }

    public void setSaleSkuId(int saleSkuId) {
        this.saleSkuId = saleSkuId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public ShopCarGoodsSku() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.saleSkuId);
        dest.writeInt(this.count);
        dest.writeDouble(this.price);
        dest.writeInt(this.productId);
        dest.writeString(this.moneyPrice);
        dest.writeString(this.integralName);
        dest.writeInt(this.integralProductType);
        dest.writeInt(this.integralType);
        dest.writeString(this.valuesName);
        dest.writeString(this.picUrl);
        dest.writeString(this.presentIds);
        dest.writeString(this.proType);
        dest.writeString(this.activityCode);
    }

    protected ShopCarGoodsSku(Parcel in) {
        this.saleSkuId = in.readInt();
        this.count = in.readInt();
        this.price = in.readDouble();
        this.productId = in.readInt();
        this.moneyPrice = in.readString();
        this.integralName = in.readString();
        this.integralProductType = in.readInt();
        this.integralType = in.readInt();
        this.valuesName = in.readString();
        this.picUrl = in.readString();
        this.presentIds = in.readString();
        this.proType = in.readString();
        this.activityCode = in.readString();
    }

    public static final Creator<ShopCarGoodsSku> CREATOR = new Creator<ShopCarGoodsSku>() {
        @Override
        public ShopCarGoodsSku createFromParcel(Parcel source) {
            return new ShopCarGoodsSku(source);
        }

        @Override
        public ShopCarGoodsSku[] newArray(int size) {
            return new ShopCarGoodsSku[size];
        }
    };
}
