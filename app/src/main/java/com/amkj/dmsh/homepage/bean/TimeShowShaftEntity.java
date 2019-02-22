package com.amkj.dmsh.homepage.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.amkj.dmsh.base.BaseTimeEntity;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/4/21
 * class description:限时特惠时间轴
 */

public class TimeShowShaftEntity extends BaseTimeEntity{

    /**
     * sysTime : 2018-09-15 14:51:37
     * timeAxisInfoList : [{"date":"2018-09-10","name":"周一","type":0,"hourList":[]},{"date":"2018-09-11","name":"周二","type":0,"hourList":[]},{"date":"2018-09-12","name":"周三","type":0,"hourList":[]},{"date":"2018-09-13","name":"周四","type":0,"hourList":[]},{"date":"2018-09-14","name":"周五","type":1,"hourList":[]},{"date":"2018-09-17","name":"周一","type":2,"hourList":[]}]
     */
    @SerializedName("timeAxisInfoList")
    private List<TimeShowShaftBean> timeShowShaftList;

    public List<TimeShowShaftBean> getTimeShowShaftList() {
        return timeShowShaftList;
    }

    public void setTimeShowShaftList(List<TimeShowShaftBean> timeShowShaftList) {
        this.timeShowShaftList = timeShowShaftList;
    }

    public static class TimeShowShaftBean implements Parcelable{
        /**
         * date : 2018-09-10
         * name : 周一
         * type : 0
         * hourList : []
         */

        private String date;
        private String name;
        private int type;
        @SerializedName("hourList")
        private List<String> hourShaft;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public List<String> getHourShaft() {
            return hourShaft;
        }

        public void setHourShaft(List<String> hourShaft) {
            this.hourShaft = hourShaft;
        }

        public TimeShowShaftBean() {
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.date);
            dest.writeString(this.name);
            dest.writeInt(this.type);
            dest.writeStringList(this.hourShaft);
        }

        protected TimeShowShaftBean(Parcel in) {
            this.date = in.readString();
            this.name = in.readString();
            this.type = in.readInt();
            this.hourShaft = in.createStringArrayList();
        }

        public static final Creator<TimeShowShaftBean> CREATOR = new Creator<TimeShowShaftBean>() {
            @Override
            public TimeShowShaftBean createFromParcel(Parcel source) {
                return new TimeShowShaftBean(source);
            }

            @Override
            public TimeShowShaftBean[] newArray(int size) {
                return new TimeShowShaftBean[size];
            }
        };
    }
}
