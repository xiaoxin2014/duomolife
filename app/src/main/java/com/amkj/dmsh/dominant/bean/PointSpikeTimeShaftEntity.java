package com.amkj.dmsh.dominant.bean;

import com.amkj.dmsh.base.BaseTimeEntity;

import java.util.List;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2019/3/2
 * version 3.3.0
 * class description:整点秒杀时间轴
 */
public class PointSpikeTimeShaftEntity extends BaseTimeEntity{

    /**
     * code : 01
     * msg : 请求成功
     * sysTime : 2019-03-02 10:37:30
     * timeAxisInfoList : [{"id":35,"showTime":"26日00:00","startTime":"2019-02-26 00:00:00","endTime":"2019-02-26 19:00:00","status":"已结束"},{"id":36,"showTime":"27日15:04","startTime":"2019-02-27 15:04:00","endTime":"2019-02-27 23:00:00","status":"已结束"},{"id":37,"showTime":"27日16:30","startTime":"2019-02-27 16:30:00","endTime":"2019-02-28 23:00:00","status":"已结束"},{"id":38,"showTime":"27日17:58","startTime":"2019-02-27 17:58:00","endTime":"2019-03-03 00:00:00","status":"抢购中"},{"id":39,"showTime":"02日00:00","startTime":"2019-03-02 00:00:00","endTime":"2019-03-03 11:00:00","status":"抢购中"},{"id":40,"showTime":"03日10:07","startTime":"2019-03-03 10:07:00","endTime":"2019-03-15 10:04:00","status":"即将开抢"}]
     */
    private List<TimeAxisInfoListBean> timeAxisInfoList;

    public List<TimeAxisInfoListBean> getTimeAxisInfoList() {
        return timeAxisInfoList;
    }

    public void setTimeAxisInfoList(List<TimeAxisInfoListBean> timeAxisInfoList) {
        this.timeAxisInfoList = timeAxisInfoList;
    }

    public static class TimeAxisInfoListBean {
        /**
         * id : 35
         * showTime : 26日00:00
         * startTime : 2019-02-26 00:00:00
         * endTime : 2019-02-26 19:00:00
         * status : 已结束
         */

        private int id;
        private String showTime;
        private String startTime;
        private String endTime;
        private String status;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getShowTime() {
            return showTime;
        }

        public void setShowTime(String showTime) {
            this.showTime = showTime;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
