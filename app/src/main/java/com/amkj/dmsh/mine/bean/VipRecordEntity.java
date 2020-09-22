package com.amkj.dmsh.mine.bean;

import com.amkj.dmsh.base.BaseEntity;

import java.util.List;

/**
 * Created by xiaoxin on 2020/8/26
 * Version:v4.7.0
 */
public class VipRecordEntity extends BaseEntity {

    /**
     * sysTime : 2020-08-26 18:39:20
     * result : [{"title":"付费会员","buyTime":"2020-08-26 18:17:08","endTime":"2020-09-09 18:11:52","road":"付费购买","payAmount":"0.01"},{"title":"付费会员","buyTime":"2020-08-26 18:11:52","endTime":"2020-09-02 18:11:52","road":"付费购买","payAmount":"0.01"}]
     */

    private List<VipRecordBean> result;

    public List<VipRecordBean> getResult() {
        return result;
    }

    public void setResult(List<VipRecordBean> result) {
        this.result = result;
    }

    public static class VipRecordBean {
        /**
         * title : 付费会员
         * buyTime : 2020-08-26 18:17:08
         * endTime : 2020-09-09 18:11:52
         * road : 付费购买
         * payAmount : 0.01
         */

        private String title;
        private String buyTime;
        private String endTime;
        private String road;
        private String payAmount;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getBuyTime() {
            return buyTime;
        }

        public void setBuyTime(String buyTime) {
            this.buyTime = buyTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getRoad() {
            return road;
        }

        public void setRoad(String road) {
            this.road = road;
        }

        public String getPayAmount() {
            return payAmount;
        }

        public void setPayAmount(String payAmount) {
            this.payAmount = payAmount;
        }
    }
}
