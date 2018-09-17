package com.amkj.dmsh.homepage.bean;

import com.amkj.dmsh.base.BaseTimeEntity;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by LGuipeng on 2016/9/12.
 */
public class TimeForeShowEntity extends BaseTimeEntity{

    /**
     * result : [{"endTime":"string","flashBuyClickCount":"string","id":0,"isRemind":0,"marketPrice":"string","maxPrice":"string","name":"string","path":"string","picUrl":"string","previsionFlag":0,"price":"string","quantity":0,"startTime":"string","subtitle":"string","title":"string"}]
     * topic : {"id":0,"picUrl":"string","title":"string"}
     */

    @SerializedName("topic")
    private TimeTopicBean timeTopicBean;
    @SerializedName(value = "result",alternate = "productInfoInfoList")
    private List<TimeForeShowBean> timeForeShowList;

    public TimeTopicBean getTimeTopicBean() {
        return timeTopicBean;
    }

    public void setTimeTopicBean(TimeTopicBean timeTopicBean) {
        this.timeTopicBean = timeTopicBean;
    }

    public List<TimeForeShowBean> getTimeForeShowList() {
        return timeForeShowList;
    }

    public void setTimeForeShowList(List<TimeForeShowBean> timeForeShowList) {
        this.timeForeShowList = timeForeShowList;
    }

    public static class TimeTopicBean extends BaseTimeProductTopicBean{
        /**
         * id : 0
         * picUrl : string
         * title : string
         */

        private int id;
        private String picUrl;
        private String title;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getPicUrl() {
            return picUrl;
        }

        public void setPicUrl(String picUrl) {
            this.picUrl = picUrl;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }

    public static class TimeForeShowBean extends BaseTimeProductTopicBean{
        /**
         * endTime : string
         * flashBuyClickCount : string
         * id : 0
         * isRemind : 0
         * marketPrice : string
         * maxPrice : string
         * name : string
         * path : string
         * picUrl : string
         * previsionFlag : 0
         * price : string
         * quantity : 0
         * startTime : string
         * subtitle : string
         * title : string
         */

        private String endTime;
        private String flashBuyClickCount;
        private int id;
        private int isRemind;
        private String marketPrice;
        private String maxPrice;
        @SerializedName(value = "name",alternate = "title")
        private String name;
        @SerializedName(value = "picUrl",alternate = "path")
        private String picUrl;
        private int previsionFlag;
        private String price;
        private int quantity;
        private String startTime;
        private String subtitle;

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getFlashBuyClickCount() {
            return flashBuyClickCount;
        }

        public void setFlashBuyClickCount(String flashBuyClickCount) {
            this.flashBuyClickCount = flashBuyClickCount;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getIsRemind() {
            return isRemind;
        }

        public void setIsRemind(int isRemind) {
            this.isRemind = isRemind;
        }

        public String getMarketPrice() {
            return marketPrice;
        }

        public void setMarketPrice(String marketPrice) {
            this.marketPrice = marketPrice;
        }

        public String getMaxPrice() {
            return maxPrice;
        }

        public void setMaxPrice(String maxPrice) {
            this.maxPrice = maxPrice;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPicUrl() {
            return picUrl;
        }

        public void setPicUrl(String picUrl) {
            this.picUrl = picUrl;
        }

        public int getPrevisionFlag() {
            return previsionFlag;
        }

        public void setPrevisionFlag(int previsionFlag) {
            this.previsionFlag = previsionFlag;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getSubtitle() {
            return subtitle;
        }

        public void setSubtitle(String subtitle) {
            this.subtitle = subtitle;
        }
    }

    public static class TimeShaftBean extends BaseTimeProductTopicBean{
        private String timeDayWeek;
        private String timeDayHour;

        public String getTimeDayWeek() {
            return timeDayWeek;
        }

        public void setTimeDayWeek(String timeDayWeek) {
            this.timeDayWeek = timeDayWeek;
        }

        public String getTimeDayHour() {
            return timeDayHour;
        }

        public void setTimeDayHour(String timeDayHour) {
            this.timeDayHour = timeDayHour;
        }
    }
}
