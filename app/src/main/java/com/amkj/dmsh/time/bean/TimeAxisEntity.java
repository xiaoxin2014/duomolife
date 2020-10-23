package com.amkj.dmsh.time.bean;

import com.amkj.dmsh.base.BaseEntity;

import java.util.List;

/**
 * Created by xiaoxin on 2020/9/23
 * Version:v4.8.0
 * ClassDescription :
 */
public class TimeAxisEntity extends BaseEntity {

    /**
     * sysTime : string
     * timeAxisInfoList : [{"date":"string","name":"string","type":0}]
     */

    private List<TimeAxisBean> timeAxisInfoList;

    public List<TimeAxisBean> getTimeAxisInfoList() {
        return timeAxisInfoList;
    }

    public void setTimeAxisInfoList(List<TimeAxisBean> timeAxisInfoList) {
        this.timeAxisInfoList = timeAxisInfoList;
    }

    public static class TimeAxisBean {
        /**
         * date : string
         * name : string
         * type : 0
         */

        private String date;
        private String showDate;
        private String name;
        private int type;

        public String getShowDate() {
            return showDate;
        }

        public void setShowDate(String showDate) {
            this.showDate = showDate;
        }

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
    }
}
