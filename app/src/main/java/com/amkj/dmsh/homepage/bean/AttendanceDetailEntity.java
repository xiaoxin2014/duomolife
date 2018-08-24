package com.amkj.dmsh.homepage.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/7/20
 * version 3.1.5
 * class description:签到详情
 */
public class AttendanceDetailEntity {

    /**
     * code : 01
     * msg : 请求成功
     * result : [{"uid":27928,"conNum":1,"totalNum":358,"id":1343745,"ctime":"2018-07-19 15:07:08","toWeek":"周四"},{"uid":27928,"conNum":1,"totalNum":357,"id":1343735,"ctime":"2018-07-16 15:49:19","toWeek":"周一"}]
     * logList : [{"uid":87889,"score":5,"nickname":"阿飞","avatar":"http://image.domolife.cn/iosRelease/20180522105324.jpg"},{"uid":87891,"score":5,"nickname":"小董董","avatar":"http://image.domolife.cn/platform/20180718/20180718092326335.png"},{"uid":87891,"score":5,"nickname":"小董董","avatar":"http://image.domolife.cn/platform/20180718/20180718092326335.png"}]
     * score : 985
     * totalNum : 358
     * conNum : 1
     * signExplain : 已连续签到1天，还差8天可获得惊喜礼包
     * remind : false
     * sign : false
     */

    private String code;
    private String msg;
    private int score;
    private int totalNum;
    private int conNum;
    private String signExplain;
    private String sysTime;
    private boolean remind;
    private boolean sign;
    @SerializedName("result")
    private List<AttendanceDetailBean> attendanceDetailList;
    private List<LogListBean> logList;

    public String getSysTime() {
        return sysTime;
    }

    public void setSysTime(String sysTime) {
        this.sysTime = sysTime;
    }

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

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    public int getConNum() {
        return conNum;
    }

    public void setConNum(int conNum) {
        this.conNum = conNum;
    }

    public String getSignExplain() {
        return signExplain;
    }

    public void setSignExplain(String signExplain) {
        this.signExplain = signExplain;
    }

    public boolean isRemind() {
        return remind;
    }

    public void setRemind(boolean remind) {
        this.remind = remind;
    }

    public boolean isSign() {
        return sign;
    }

    public void setSign(boolean sign) {
        this.sign = sign;
    }

    public List<AttendanceDetailBean> getAttendanceDetailList() {
        return attendanceDetailList;
    }

    public void setAttendanceDetailList(List<AttendanceDetailBean> attendanceDetailList) {
        this.attendanceDetailList = attendanceDetailList;
    }

    public List<LogListBean> getLogList() {
        return logList;
    }

    public void setLogList(List<LogListBean> logList) {
        this.logList = logList;
    }

    public static class AttendanceDetailBean {
        /**
         * uid : 27928
         * conNum : 1
         * totalNum : 358
         * id : 1343745
         * ctime : 2018-07-19 15:07:08
         * toWeek : 周四
         */

        private int uid;
        private int conNum;
        private int totalNum;
        private int id;
        private String ctime;
        private String toWeek;
        //        自定义属性
//        0 已过 1 已签 2 未签
        private int weekCode;

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public int getWeekCode() {
            return weekCode;
        }

        public void setWeekCode(int weekCode) {
            this.weekCode = weekCode;
        }

        public int getConNum() {
            return conNum;
        }

        public void setConNum(int conNum) {
            this.conNum = conNum;
        }

        public int getTotalNum() {
            return totalNum;
        }

        public void setTotalNum(int totalNum) {
            this.totalNum = totalNum;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getCtime() {
            return ctime;
        }

        public void setCtime(String ctime) {
            this.ctime = ctime;
        }

        public String getToWeek() {
            return toWeek;
        }

        public void setToWeek(String toWeek) {
            this.toWeek = toWeek;
        }
    }

    public static class LogListBean {
        /**
         * uid : 87889
         * score : 5
         * nickname : 阿飞
         * avatar : http://image.domolife.cn/iosRelease/20180522105324.jpg
         */

        private int uid;
        private int score;
        private String nickname;
        private String avatar;

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }
    }
}
