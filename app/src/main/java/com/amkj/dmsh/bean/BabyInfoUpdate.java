package com.amkj.dmsh.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by atd48 on 2016/9/21.
 */
public class BabyInfoUpdate {

    /**
     * id : 1
     * birthday : 2016-08-10
     * sex : 1
     */

    @SerializedName("result")
    private UpdateBackBean upDate;
    /**
     * result : {"id":"1","birthday":"2016-08-10","sex":"1"}
     * code : 01
     * msg : 请求成功
     */

    private String code;
    private String msg;

    public UpdateBackBean getUpDate() {
        return upDate;
    }

    public void setUpDate(UpdateBackBean upDate) {
        this.upDate = upDate;
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

    public static class UpdateBackBean {
        private String id;
        private String birthday;
        private String sex;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }
    }
}
