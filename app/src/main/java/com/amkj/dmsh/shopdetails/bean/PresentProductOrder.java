package com.amkj.dmsh.shopdetails.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by xiaoxin on 2019/7/23
 * Version:v4.1.0
 * ClassDescription :赠品
 */
public class PresentProductOrder implements Parcelable {

    /**
     * presentPicUrl : http://image.domolife.cn/platform/20190719/20190719143036086.jpeg
     * presentCount : 1
     * presentName : 褪黑素
     */

    private String presentPicUrl;
    private String presentCount;
    private String presentName;

    public String getPresentPicUrl() {
        return presentPicUrl;
    }

    public void setPresentPicUrl(String presentPicUrl) {
        this.presentPicUrl = presentPicUrl;
    }

    public String getPresentCount() {
        return presentCount;
    }

    public void setPresentCount(String presentCount) {
        this.presentCount = presentCount;
    }

    public String getPresentName() {
        return presentName;
    }

    public void setPresentName(String presentName) {
        this.presentName = presentName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.presentPicUrl);
        dest.writeString(this.presentCount);
        dest.writeString(this.presentName);
    }

    public PresentProductOrder() {
    }

    protected PresentProductOrder(Parcel in) {
        this.presentPicUrl = in.readString();
        this.presentCount = in.readString();
        this.presentName = in.readString();
    }

    public static final Parcelable.Creator<PresentProductOrder> CREATOR = new Parcelable.Creator<PresentProductOrder>() {
        @Override
        public PresentProductOrder createFromParcel(Parcel source) {
            return new PresentProductOrder(source);
        }

        @Override
        public PresentProductOrder[] newArray(int size) {
            return new PresentProductOrder[size];
        }
    };
}
