package com.amkj.dmsh.shopdetails.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.amkj.dmsh.shopdetails.bean.DirectLogisticsEntity.DirectLogisticsBean.LogisticsProductPacketBean;
import com.amkj.dmsh.shopdetails.bean.DirectLogisticsEntity.DirectLogisticsBean.LogisticsProductPacketBean.LogisticsDetailsBean.LogisticsBean.LogisticTextBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/12/4
 * version 3.1.9
 * class description:物流包裹数据传递
 */
public class DirectLogisticPacketBean implements Parcelable{
    private String expressNo;
    private String expressCompany;
    private List<LogisticsProductPacketBean> directGoods;
    private List<LogisticTextBean> logisticPacketList;

    public String getExpressNo() {
        return expressNo;
    }

    public void setExpressNo(String expressNo) {
        this.expressNo = expressNo;
    }

    public String getExpressCompany() {
        return expressCompany;
    }

    public void setExpressCompany(String expressCompany) {
        this.expressCompany = expressCompany;
    }

    public List<LogisticsProductPacketBean> getDirectGoods() {
        return directGoods;
    }

    public void setDirectGoods(List<LogisticsProductPacketBean> directGoods) {
        this.directGoods = directGoods;
    }

    public List<LogisticTextBean> getLogisticPacketList() {
        return logisticPacketList;
    }

    public void setLogisticPacketList(List<LogisticTextBean> logisticPacketList) {
        this.logisticPacketList = logisticPacketList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.expressNo);
        dest.writeString(this.expressCompany);
        dest.writeList(this.directGoods);
        dest.writeList(this.logisticPacketList);
    }

    public DirectLogisticPacketBean() {
    }

    protected DirectLogisticPacketBean(Parcel in) {
        this.expressNo = in.readString();
        this.expressCompany = in.readString();
        this.directGoods = new ArrayList<LogisticsProductPacketBean>();
        in.readList(this.directGoods, LogisticsProductPacketBean.class.getClassLoader());
        this.logisticPacketList = new ArrayList<LogisticTextBean>();
        in.readList(this.logisticPacketList, LogisticTextBean.class.getClassLoader());
    }

    public static final Creator<DirectLogisticPacketBean> CREATOR = new Creator<DirectLogisticPacketBean>() {
        @Override
        public DirectLogisticPacketBean createFromParcel(Parcel source) {
            return new DirectLogisticPacketBean(source);
        }

        @Override
        public DirectLogisticPacketBean[] newArray(int size) {
            return new DirectLogisticPacketBean[size];
        }
    };
}
