package com.amkj.dmsh.homepage.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/4/21
 * class description:请输入类描述
 */

public class TimeShowEntity {

    /**
     * result : [{"date":"2017-04-16","name":"周五","type":0},{"date":"2017-04-17","name":"周一","type":0},{"date":"2017-04-18","name":"周二","type":0},{"date":"2017-04-19","name":"周三","type":1},{"date":"2017-04-20","name":"周四","type":2}]
     * currentTime : 2017-04-19 15:28:02
     * msg : 请求成功
     * code : 01
     */

    private String currentTime;
    private String msg;
    private String code;
    @SerializedName("result")
    private List<TimeShowBean> timeShowBeanList;

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<TimeShowBean> getTimeShowBeanList() {
        return timeShowBeanList;
    }

    public void setTimeShowBeanList(List<TimeShowBean> timeShowBeanList) {
        this.timeShowBeanList = timeShowBeanList;
    }

    public static class TimeShowBean implements Parcelable {
        /**
         * date : 2017-04-16
         * name : 周五
         * type : 0
         */

        private String date;
        private String name;
        private int type;

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

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.date);
            dest.writeString(this.name);
            dest.writeInt(this.type);
        }

        public TimeShowBean() {
        }

        protected TimeShowBean(Parcel in) {
            this.date = in.readString();
            this.name = in.readString();
            this.type = in.readInt();
        }

        public static final Creator<TimeShowBean> CREATOR = new Creator<TimeShowBean>() {
            @Override
            public TimeShowBean createFromParcel(Parcel source) {
                return new TimeShowBean(source);
            }

            @Override
            public TimeShowBean[] newArray(int size) {
                return new TimeShowBean[size];
            }
        };
    }
}
