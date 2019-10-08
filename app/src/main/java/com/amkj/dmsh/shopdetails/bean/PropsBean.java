package com.amkj.dmsh.shopdetails.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by xiaoxin on 2019/6/17
 * Version:v4.1.0
 * ClassDescription :商品属性实体类（例如衣服有大小和颜色两种属性）
 */
public class PropsBean implements Parcelable {
    /**
     * propId : 1
     * propName : 默认
     * praentId : 0
     */

    @SerializedName(value = "propId")
    private int propId;  //属性id
    private String propName;//属性名称
    private int praentId;

    public int getPropId() {
        return propId;
    }

    public void setPropId(int propId) {
        this.propId = propId;
    }

    public String getPropName() {
        return propName;
    }

    public void setPropName(String propName) {
        this.propName = propName;
    }

    public int getPraentId() {
        return praentId;
    }

    public void setPraentId(int praentId) {
        this.praentId = praentId;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.propId);
        dest.writeString(this.propName);
        dest.writeInt(this.praentId);
    }

    public PropsBean() {
    }

    protected PropsBean(Parcel in) {
        this.propId = in.readInt();
        this.propName = in.readString();
        this.praentId = in.readInt();
    }

    public static final Creator<PropsBean> CREATOR = new Creator<PropsBean>() {
        @Override
        public PropsBean createFromParcel(Parcel source) {
            return new PropsBean(source);
        }

        @Override
        public PropsBean[] newArray(int size) {
            return new PropsBean[size];
        }
    };
}
