package com.amkj.dmsh.dominant.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/7/10
 * class description:新品发布时间轴
 */

public class QNewProTimeShaftEntity {

    /**
     * result : [{"newReleaseDay":"15"},{"newReleaseDay":"7"},{"newReleaseDay":"3"}]
     * msg : 请求成功
     * code : 01
     */

    private String msg;
    private String code;
    @SerializedName("result")
    private List<QNewProTimeShaftBean> QNewProTimeShaftList;

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

    public List<QNewProTimeShaftBean> getQNewProTimeShaftList() {
        return QNewProTimeShaftList;
    }

    public void setQNewProTimeShaftList(List<QNewProTimeShaftBean> QNewProTimeShaftList) {
        this.QNewProTimeShaftList = QNewProTimeShaftList;
    }

    public static class QNewProTimeShaftBean {
        /**
         * newReleaseDay : 15
         */

        private String newReleaseDay;
        private boolean isSelected;

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }

        public String getNewReleaseDay() {
            return newReleaseDay;
        }

        public void setNewReleaseDay(String newReleaseDay) {
            this.newReleaseDay = newReleaseDay;
        }
    }
}
