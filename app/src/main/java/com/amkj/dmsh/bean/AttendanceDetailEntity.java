package com.amkj.dmsh.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by atd48 on 2016/9/9.
 */
public class AttendanceDetailEntity {

    /**
     * result : {"nowDate":"2017-07-26","score":1092874,"SIGN_CONTINUITYDAY":[{"flag":false,"coupon":"","prizetype":2,"id":1,"state":1,"type":0,"couponid":2,"continuityday":8,"score":9},{"score":9,"flag":false,"prizetype":1,"id":5,"state":1,"type":0,"continuityday":8},{"score":212,"flag":true,"prizetype":1,"id":6,"state":1,"type":0,"continuityday":1}],"con_num":2,"totalNum":55,"isRemind":true,"avatar":"http://img.domolife.cn/201704191717321905157574.png","dateList":[{"uid":27928,"update_user":"","con_num":2,"create_time":0,"tsm_platform_id":0,"total_num":55,"ctime":"2017-07-20","id":352606,"create_user":"","updated":"","version":1},{"uid":27928,"update_user":"","con_num":1,"create_time":0,"tsm_platform_id":0,"total_num":54,"ctime":"2017-07-19","id":352604,"create_user":"","updated":"","version":1}],"signExplain":"已连续签到2天，还差6天可获得惊喜礼包","isSign":false,"listShow":[{"reward":"提示你妹啊","androidLink":"android","iosLink":"ios","isOver":false,"title":"每日分享"},{"reward":"提示1234567","androidLink":"link1","iosLink":"link2","isOver":false,"title":"每日评论"}]}
     * msg : 请求成功
     * code : 01
     */

    @SerializedName("result")
    private AttendanceDetailBean attendanceDetailBean;
    private String msg;
    private String code;

    public AttendanceDetailBean getAttendanceDetailBean() {
        return attendanceDetailBean;
    }

    public void setAttendanceDetailBean(AttendanceDetailBean attendanceDetailBean) {
        this.attendanceDetailBean = attendanceDetailBean;
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

    public static class AttendanceDetailBean {
        /**
         * nowDate : 2017-07-26
         * score : 1092874
         * SIGN_CONTINUITYDAY : [{"flag":false,"coupon":"","prizetype":2,"id":1,"state":1,"type":0,"couponid":2,"continuityday":8},{"score":9,"flag":false,"prizetype":1,"id":5,"state":1,"type":0,"continuityday":8},{"score":212,"flag":true,"prizetype":1,"id":6,"state":1,"type":0,"continuityday":1}]
         * con_num : 2
         * totalNum : 55
         * isRemind : true
         * avatar : http://img.domolife.cn/201704191717321905157574.png
         * dateList : [{"uid":27928,"update_user":"","con_num":2,"create_time":0,"tsm_platform_id":0,"total_num":55,"ctime":"2017-07-20","id":352606,"create_user":"","updated":"","version":1},{"uid":27928,"update_user":"","con_num":1,"create_time":0,"tsm_platform_id":0,"total_num":54,"ctime":"2017-07-19","id":352604,"create_user":"","updated":"","version":1}]
         * signExplain : 已连续签到2天，还差6天可获得惊喜礼包
         * isSign : false
         * listShow : [{"reward":"提示你妹啊","androidLink":"android","iosLink":"ios","isOver":false,"title":"每日分享"},{"reward":"提示1234567","androidLink":"link1","iosLink":"link2","isOver":false,"title":"每日评论"}]
         */

        private String nowDate;
        private int score;
        private int con_num;
        private int totalNum;
        private boolean isRemind;
        private String avatar;
        private String signExplain;
        private boolean isSign;
        @SerializedName("SIGN_CONTINUITYDAY")
        private List<SignDayBean> signDayBeanList;
        private List<DateListBean> dateList;
        private List<ListShowBean> listShow;

        public String getNowDate() {
            return nowDate;
        }

        public void setNowDate(String nowDate) {
            this.nowDate = nowDate;
        }

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }

        public int getCon_num() {
            return con_num;
        }

        public void setCon_num(int con_num) {
            this.con_num = con_num;
        }

        public int getTotalNum() {
            return totalNum;
        }

        public void setTotalNum(int totalNum) {
            this.totalNum = totalNum;
        }

        public boolean isIsRemind() {
            return isRemind;
        }

        public void setIsRemind(boolean isRemind) {
            this.isRemind = isRemind;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getSignExplain() {
            return signExplain;
        }

        public void setSignExplain(String signExplain) {
            this.signExplain = signExplain;
        }

        public boolean isIsSign() {
            return isSign;
        }

        public void setIsSign(boolean isSign) {
            this.isSign = isSign;
        }

        public List<SignDayBean> getSignDayBeanList() {
            return signDayBeanList;
        }

        public void setSignDayBeanList(List<SignDayBean> signDayBeanList) {
            this.signDayBeanList = signDayBeanList;
        }

        public List<DateListBean> getDateList() {
            return dateList;
        }

        public void setDateList(List<DateListBean> dateList) {
            this.dateList = dateList;
        }

        public List<ListShowBean> getListShow() {
            return listShow;
        }

        public void setListShow(List<ListShowBean> listShow) {
            this.listShow = listShow;
        }

        public static class SignDayBean {
            /**
             * flag : false
             * coupon :
             * prizetype : 2
             * id : 1
             * state : 1
             * type : 0
             * couponid : 2
             * continuityday : 8
             * score : 9
             */

            private boolean flag;
            private String coupon;
            private int prizetype;
            private int id;
            private int state;
            private int type;
            private int couponid;
            private int continuityday;
            private int score;
            //            是否全选
            private boolean selAll;

            public boolean isFlag() {
                return flag;
            }

            public void setFlag(boolean flag) {
                this.flag = flag;
            }

            public String getCoupon() {
                return coupon;
            }

            public void setCoupon(String coupon) {
                this.coupon = coupon;
            }

            public int getPrizetype() {
                return prizetype;
            }

            public void setPrizetype(int prizetype) {
                this.prizetype = prizetype;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getState() {
                return state;
            }

            public void setState(int state) {
                this.state = state;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public int getCouponid() {
                return couponid;
            }

            public void setCouponid(int couponid) {
                this.couponid = couponid;
            }

            public int getContinuityday() {
                return continuityday;
            }

            public void setContinuityday(int continuityday) {
                this.continuityday = continuityday;
            }

            public int getScore() {
                return score;
            }

            public void setScore(int score) {
                this.score = score;
            }

            public boolean isSelAll() {
                return selAll;
            }

            public void setSelAll(boolean selAll) {
                this.selAll = selAll;
            }
        }

        public static class DateListBean {
            /**
             * uid : 27928
             * update_user :
             * con_num : 2
             * create_time : 0
             * tsm_platform_id : 0
             * total_num : 55
             * ctime : 2017-07-20
             * id : 352606
             * create_user :
             * updated :
             * version : 1
             */

            private int uid;
            private String update_user;
            private int con_num;
            private int create_time;
            private int tsm_platform_id;
            private int total_num;
            private String ctime;
            private int id;
            private String create_user;
            private String updated;
            private int version;

            public int getUid() {
                return uid;
            }

            public void setUid(int uid) {
                this.uid = uid;
            }

            public String getUpdate_user() {
                return update_user;
            }

            public void setUpdate_user(String update_user) {
                this.update_user = update_user;
            }

            public int getCon_num() {
                return con_num;
            }

            public void setCon_num(int con_num) {
                this.con_num = con_num;
            }

            public int getCreate_time() {
                return create_time;
            }

            public void setCreate_time(int create_time) {
                this.create_time = create_time;
            }

            public int getTsm_platform_id() {
                return tsm_platform_id;
            }

            public void setTsm_platform_id(int tsm_platform_id) {
                this.tsm_platform_id = tsm_platform_id;
            }

            public int getTotal_num() {
                return total_num;
            }

            public void setTotal_num(int total_num) {
                this.total_num = total_num;
            }

            public String getCtime() {
                return ctime;
            }

            public void setCtime(String ctime) {
                this.ctime = ctime;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getCreate_user() {
                return create_user;
            }

            public void setCreate_user(String create_user) {
                this.create_user = create_user;
            }

            public String getUpdated() {
                return updated;
            }

            public void setUpdated(String updated) {
                this.updated = updated;
            }

            public int getVersion() {
                return version;
            }

            public void setVersion(int version) {
                this.version = version;
            }
        }

        public static class ListShowBean {
            /**
             * reward : 提示你妹啊
             * androidLink : android
             * iosLink : ios
             * isOver : false
             * title : 每日分享
             */

            private String reward;
            private String androidLink;
            private String iosLink;
            private boolean isOver;
            private String title;
            private boolean Obtain;

            public String getReward() {
                return reward;
            }

            public void setReward(String reward) {
                this.reward = reward;
            }

            public String getAndroidLink() {
                return androidLink;
            }

            public void setAndroidLink(String androidLink) {
                this.androidLink = androidLink;
            }

            public String getIosLink() {
                return iosLink;
            }

            public void setIosLink(String iosLink) {
                this.iosLink = iosLink;
            }

            public boolean isIsOver() {
                return isOver;
            }

            public void setIsOver(boolean isOver) {
                this.isOver = isOver;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public boolean isObtain() {
                return Obtain;
            }

            public void setObtain(boolean obtain) {
                Obtain = obtain;
            }
        }
    }
}