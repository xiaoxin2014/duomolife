package com.amkj.dmsh.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/1/19
 * version 3.7
 * class description:图片浏览
 */

public class ImageBean implements Parcelable{
    private String picUrl;
    private String skuValue;

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getSkuValue() {
        return skuValue;
    }

    public void setSkuValue(String skuValue) {
        this.skuValue = skuValue;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.picUrl);
        dest.writeString(this.skuValue);
    }

    public ImageBean() {
    }

    protected ImageBean(Parcel in) {
        this.picUrl = in.readString();
        this.skuValue = in.readString();
    }

    public static final Creator<ImageBean> CREATOR = new Creator<ImageBean>() {
        @Override
        public ImageBean createFromParcel(Parcel source) {
            return new ImageBean(source);
        }

        @Override
        public ImageBean[] newArray(int size) {
            return new ImageBean[size];
        }
    };
}
