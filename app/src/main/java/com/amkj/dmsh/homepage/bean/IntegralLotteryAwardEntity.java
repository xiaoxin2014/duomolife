package com.amkj.dmsh.homepage.bean;

import java.util.List;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/7/30
 * version 3.1.5
 * class description:积分夺宝奖励
 */
public class IntegralLotteryAwardEntity {

    /**
     * code : 01
     * msg : 请求成功
     * sysTime : 2018-07-30 12:02:13
     * lotteryInfoList : [{"id":2,"activityCode":"2018072101","activityStatus":"1","activityId":2,"image":"http://image.domolife.cn/platform/TCsSKc6nkc1532154235910.png","prizeName":"我是已结束的抽奖","status":3,"statusStr":"已兑奖"}]
     */

    private String code;
    private String msg;
    private String sysTime;
    private List<LotteryInfoListBean> lotteryInfoList;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getSysTime() {
        return sysTime;
    }

    public void setSysTime(String sysTime) {
        this.sysTime = sysTime;
    }

    public List<LotteryInfoListBean> getLotteryInfoList() {
        return lotteryInfoList;
    }

    public void setLotteryInfoList(List<LotteryInfoListBean> lotteryInfoList) {
        this.lotteryInfoList = lotteryInfoList;
    }

    public static class LotteryInfoListBean {
        /**
         * id : 2
         * activityCode : 2018072101
         * activityStatus : 1
         * activityId : 2
         * image : http://image.domolife.cn/platform/TCsSKc6nkc1532154235910.png
         * prizeName : 我是已结束的抽奖
         * status : 3
         * statusStr : 已兑奖
         */

        private int id;
        private String activityCode;
        private String activityStatus;
        private int activityId;
        private String image;
        private String prizeName;
        private int status;
        private String statusStr;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getActivityCode() {
            return activityCode;
        }

        public void setActivityCode(String activityCode) {
            this.activityCode = activityCode;
        }

        public String getActivityStatus() {
            return activityStatus;
        }

        public void setActivityStatus(String activityStatus) {
            this.activityStatus = activityStatus;
        }

        public int getActivityId() {
            return activityId;
        }

        public void setActivityId(int activityId) {
            this.activityId = activityId;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getPrizeName() {
            return prizeName;
        }

        public void setPrizeName(String prizeName) {
            this.prizeName = prizeName;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getStatusStr() {
            return statusStr;
        }

        public void setStatusStr(String statusStr) {
            this.statusStr = statusStr;
        }
    }
}
