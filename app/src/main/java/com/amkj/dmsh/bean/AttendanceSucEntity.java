package com.amkj.dmsh.bean;

import com.google.gson.annotations.SerializedName;
import com.amkj.dmsh.bean.AttendanceDetailEntity.AttendanceDetailBean.SignDayBean;

import java.util.List;

/**
 * Created by atd48 on 2016/9/9.
 */
public class AttendanceSucEntity {

    /**
     * total_num : 18
     * score : 90
     * con_num : 0
     * code : 01
     * msg : 请求成功
     */

    private int total_num;
    private int score;
    private int con_num;
    private String signExplain;
    private String code;
    private String msg;
    @SerializedName("result")
    private AttendanceSucBean attendanceSucBean;

    public int getTotal_num() {
        return total_num;
    }

    public void setTotal_num(int total_num) {
        this.total_num = total_num;
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

    public String getSignExplain() {
        return signExplain;
    }

    public void setSignExplain(String signExplain) {
        this.signExplain = signExplain;
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

    public AttendanceSucBean getAttendanceSucBean() {
        return attendanceSucBean;
    }

    public void setAttendanceSucBean(AttendanceSucBean attendanceSucBean) {
        this.attendanceSucBean = attendanceSucBean;
    }

    public static class AttendanceSucBean {
        @SerializedName("SIGN_CONTINUITYDAY")
        private List<SignDayBean> signDayBeanList;

        public List<SignDayBean> getSignDayBeanList() {
            return signDayBeanList;
        }

        public void setSignDayBeanList(List<SignDayBean> signDayBeanList) {
            this.signDayBeanList = signDayBeanList;
        }
    }
}
