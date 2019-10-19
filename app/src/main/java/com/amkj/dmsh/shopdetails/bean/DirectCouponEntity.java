package com.amkj.dmsh.shopdetails.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.amkj.dmsh.base.BaseEntity;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2016/11/21
 * class description:请输入类描述
 */

public class DirectCouponEntity extends BaseEntity {

    /**
     * result : [{"amount":300,"id":1,"status":0,"endTime":"2018-11-02 00:00:00","beOverdue":"0","startTime":"2016-11-02 11:52","startFee":100,"type":"活动"},{"amount":300,"id":2,"status":0,"endTime":"2022-11-30 00:00:00","beOverdue":"0","startTime":"2016-11-01 10:51","startFee":100,"type":"新注册用户"},{"amount":500,"id":3,"status":0,"endTime":"2016-11-30 00:00:00","beOverdue":"0","startTime":"2016-11-01 00:00","startFee":500,"type":"邀请好友"},{"amount":100,"id":4,"status":0,"endTime":"2018-11-02 00:00:00","beOverdue":"0","startTime":"2016-11-02 11:52","startFee":300,"type":"活动"},{"amount":100,"id":5,"status":0,"endTime":"2018-11-02 00:00:00","beOverdue":"0","startTime":"2016-11-02 11:52","startFee":300,"type":"活动"},{"amount":100,"id":6,"status":0,"endTime":"2018-11-02 00:00:00","beOverdue":"0","startTime":"2016-11-02 11:52","startFee":300,"type":"活动"},{"amount":100,"id":7,"status":0,"endTime":"2018-11-02 00:00:00","beOverdue":"0","startTime":"2016-11-02 11:52","startFee":300,"type":"活动"},{"amount":500,"id":8,"status":0,"endTime":"2016-11-30 00:00:00","beOverdue":"0","startTime":"2016-11-01 00:00","startFee":500,"type":"新注册用户"},{"amount":500,"id":9,"status":0,"endTime":"2016-11-30 00:00:00","beOverdue":"0","startTime":"2016-11-01 00:00","startFee":500,"type":"新注册用户"},{"amount":500,"id":10,"status":0,"endTime":"2016-11-30 00:00:00","beOverdue":"0","startTime":"2016-11-01 00:00","startFee":500,"type":"新注册用户"}]
     * code : 01
     * msg : 请求成功
     */
    @SerializedName("result")
    private List<DirectCouponBean> directCouponBeanList;

    public List<DirectCouponBean> getDirectCouponBeanList() {
        return directCouponBeanList;
    }

    public void setDirectCouponBeanList(List<DirectCouponBean> directCouponBeanList) {
        this.directCouponBeanList = directCouponBeanList;
    }

    public static class DirectCouponBean implements Parcelable, MultiItemEntity {
        /**
         * amount : 300
         * id : 1
         * status : 0
         * endTime : 2018-11-02 00:00:00
         * beOverdue : 0
         * startTime : 2016-11-02 11:52
         * startFee : 100
         * type : 活动
         */
        private String amount;
        private int id;
        private int status;
        @SerializedName(value = "end_time", alternate = "endTime")
        private String endTime;
        private String beOverdue;
        @SerializedName(value = "start_time", alternate = "startTime")
        private String startTime;
        @SerializedName(value = "start_fee", alternate = "startFee")
        private Double startFee;
        private String type;
        private int itemType;
        private String title;
        private String android_link;
        private int mode;
        /**
         * usable : 0
         * usableCause : string
         */

        private int usable;
        private boolean checkStatus;
        private String usableCause;
        //        类型背景颜色
        private String modeBgColor;
        //        背景颜色
        private String bgColor;
        //        优惠券描述
        private String desc;
        //        是否过期
        private boolean soonExpire;
        private int use_range;

        public boolean isCheckStatus() {
            return checkStatus;
        }

        public void setCheckStatus(boolean checkStatus) {
            this.checkStatus = checkStatus;
        }

        public int getUse_range() {
            return use_range;
        }

        public void setUse_range(int use_range) {
            this.use_range = use_range;
        }

        public String getAndroid_link() {
            return android_link;
        }

        public void setAndroid_link(String android_link) {
            this.android_link = android_link;
        }

        public String getAmount() {
            return new BigDecimal(amount).stripTrailingZeros().toPlainString();
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getBeOverdue() {
            return beOverdue;
        }

        public void setBeOverdue(String beOverdue) {
            this.beOverdue = beOverdue;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public Double getStartFee() {
            return startFee;
        }

        public void setStartFee(Double startFee) {
            this.startFee = startFee;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        @Override
        public int getItemType() {
            return itemType;
        }

        public void setItemType(int itemType) {
            this.itemType = itemType;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getMode() {
            return mode;
        }

        public void setMode(int mode) {
            this.mode = mode;
        }

        public String getModeBgColor() {
            return modeBgColor;
        }

        public void setModeBgColor(String modeBgColor) {
            this.modeBgColor = modeBgColor;
        }

        public String getBgColor() {
            return bgColor;
        }

        public void setBgColor(String bgColor) {
            this.bgColor = bgColor;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public boolean isSoonExpire() {
            return soonExpire;
        }

        public void setSoonExpire(boolean soonExpire) {
            this.soonExpire = soonExpire;
        }

        public DirectCouponBean() {
        }

        public int getUsable() {
            return usable;
        }

        public void setUsable(int usable) {
            this.usable = usable;
        }

        public String getUsableCause() {
            return usableCause;
        }

        public void setUsableCause(String usableCause) {
            this.usableCause = usableCause;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.amount);
            dest.writeInt(this.id);
            dest.writeInt(this.status);
            dest.writeString(this.endTime);
            dest.writeString(this.beOverdue);
            dest.writeString(this.startTime);
            dest.writeDouble(this.startFee);
            dest.writeString(this.type);
            dest.writeInt(this.itemType);
            dest.writeString(this.title);
            dest.writeString(this.android_link);
            dest.writeInt(this.mode);
            dest.writeInt(this.usable);
            dest.writeString(this.usableCause);
            dest.writeString(this.modeBgColor);
            dest.writeString(this.bgColor);
            dest.writeString(this.desc);
            dest.writeByte(this.soonExpire ? (byte) 1 : (byte) 0);
            dest.writeInt(this.use_range);
        }

        protected DirectCouponBean(Parcel in) {
            this.amount = in.readString();
            this.id = in.readInt();
            this.status = in.readInt();
            this.endTime = in.readString();
            this.beOverdue = in.readString();
            this.startTime = in.readString();
            this.startFee = in.readDouble();
            this.type = in.readString();
            this.itemType = in.readInt();
            this.title = in.readString();
            this.android_link = in.readString();
            this.mode = in.readInt();
            this.usable = in.readInt();
            this.usableCause = in.readString();
            this.modeBgColor = in.readString();
            this.bgColor = in.readString();
            this.desc = in.readString();
            this.soonExpire = in.readByte() != 0;
            this.use_range = in.readInt();
        }

        public static final Creator<DirectCouponBean> CREATOR = new Creator<DirectCouponBean>() {
            @Override
            public DirectCouponBean createFromParcel(Parcel source) {
                return new DirectCouponBean(source);
            }

            @Override
            public DirectCouponBean[] newArray(int size) {
                return new DirectCouponBean[size];
            }
        };
    }
}
