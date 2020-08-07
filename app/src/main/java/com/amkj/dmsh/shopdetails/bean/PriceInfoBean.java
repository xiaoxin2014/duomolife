package com.amkj.dmsh.shopdetails.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by xiaoxin on 2019/7/4
 * Version:v4.1.0
 * ClassDescription :订单价格明细
 */
public class PriceInfoBean implements Parcelable {
    /**
     * color : #000000
     * totalPrice : 725.6
     * name : 商品总额
     * totalPriceName : ¥725.60
     */

    private String color;
    private String name;
    @SerializedName(value = "totalPriceName", alternate = "priceText")
    private String totalPriceName;
    private String flag;

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.color);
        dest.writeString(this.name);
        dest.writeString(this.totalPriceName);
    }

    public PriceInfoBean() {
    }

    protected PriceInfoBean(Parcel in) {
        this.color = in.readString();
        this.name = in.readString();
        this.totalPriceName = in.readString();
    }

    public static final Parcelable.Creator<PriceInfoBean> CREATOR = new Parcelable.Creator<PriceInfoBean>() {
        @Override
        public PriceInfoBean createFromParcel(Parcel source) {
            return new PriceInfoBean(source);
        }

        @Override
        public PriceInfoBean[] newArray(int size) {
            return new PriceInfoBean[size];
        }
    };
}
