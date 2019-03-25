package com.amkj.dmsh.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by xiaoxin on 2019/3/15 0015
 */
public class GoodsBean implements Parcelable {
    private String goodsurl;
    private String goodsName;

    public GoodsBean(String goodsurl, String goodsName) {
        this.goodsurl = goodsurl;
        this.goodsName = goodsName;
    }

    public String getGoodsurl() {
        return goodsurl;
    }

    public void setGoodsurl(String goodsurl) {
        this.goodsurl = goodsurl;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.goodsurl);
        dest.writeString(this.goodsName);
    }

    protected GoodsBean(Parcel in) {
        this.goodsurl = in.readString();
        this.goodsName = in.readString();
    }

    public static final Parcelable.Creator<GoodsBean> CREATOR = new Parcelable.Creator<GoodsBean>() {
        @Override
        public GoodsBean createFromParcel(Parcel source) {
            return new GoodsBean(source);
        }

        @Override
        public GoodsBean[] newArray(int size) {
            return new GoodsBean[size];
        }
    };
}
