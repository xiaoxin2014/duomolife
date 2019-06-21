package com.amkj.dmsh.shopdetails.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by xiaoxin on 2019/6/17
 * Version:v4.1.0
 * ClassDescription :SPU实体类   SKU最小组成单位
 */
public class PropvaluesBean implements Parcelable {
    /**
     * propValueId : 6
     * propId : 1
     * propValueName : 默认
     * propValueUrl :
     */

    private int propValueId;
    private int propId;
    private String propValueName;
    private String propValueUrl;

    public int getPropValueId() {
        return propValueId;
    }

    public void setPropValueId(int propValueId) {
        this.propValueId = propValueId;
    }

    public int getPropId() {
        return propId;
    }

    public void setPropId(int propId) {
        this.propId = propId;
    }

    public String getPropValueName() {
        return propValueName;
    }

    public void setPropValueName(String propValueName) {
        this.propValueName = propValueName;
    }

    public String getPropValueUrl() {
        return propValueUrl;
    }

    public void setPropValueUrl(String propValueUrl) {
        this.propValueUrl = propValueUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.propValueId);
        dest.writeInt(this.propId);
        dest.writeString(this.propValueName);
        dest.writeString(this.propValueUrl);
    }

    public PropvaluesBean() {
    }

    protected PropvaluesBean(Parcel in) {
        this.propValueId = in.readInt();
        this.propId = in.readInt();
        this.propValueName = in.readString();
        this.propValueUrl = in.readString();
    }

    public static final Parcelable.Creator<PropvaluesBean> CREATOR = new Parcelable.Creator<PropvaluesBean>() {
        @Override
        public PropvaluesBean createFromParcel(Parcel source) {
            return new PropvaluesBean(source);
        }

        @Override
        public PropvaluesBean[] newArray(int size) {
            return new PropvaluesBean[size];
        }
    };
}
