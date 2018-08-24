package com.amkj.dmsh.shopdetails.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by atd48 on 2016/10/21.
 */
public class ShopCarGoodsSkuTransmit implements Parcelable {
    private int saleSkuId;
    private int count;
    private int id;
    private int cartId;
    private String activityCode;

    public String getActivityCode() {
        return activityCode;
    }

    public void setActivityCode(String activityCode) {
        this.activityCode = activityCode;
    }

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public ShopCarGoodsSkuTransmit() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.saleSkuId);
        dest.writeInt(this.count);
        dest.writeInt(this.id);
        dest.writeInt(this.cartId);
        dest.writeString(this.activityCode);
    }

    protected ShopCarGoodsSkuTransmit(Parcel in) {
        this.saleSkuId = in.readInt();
        this.count = in.readInt();
        this.id = in.readInt();
        this.cartId = in.readInt();
        this.activityCode = in.readString();
    }

    public static final Creator<ShopCarGoodsSkuTransmit> CREATOR = new Creator<ShopCarGoodsSkuTransmit>() {
        @Override
        public ShopCarGoodsSkuTransmit createFromParcel(Parcel source) {
            return new ShopCarGoodsSkuTransmit(source);
        }

        @Override
        public ShopCarGoodsSkuTransmit[] newArray(int size) {
            return new ShopCarGoodsSkuTransmit[size];
        }
    };
}
