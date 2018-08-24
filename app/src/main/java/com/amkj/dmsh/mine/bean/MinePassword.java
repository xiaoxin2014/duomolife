package com.amkj.dmsh.mine.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by LGuipeng on 2016/9/22.
 */
public class MinePassword {

    /**
     * result : [{"utime":"2015-11-21 00:27:08","status":1,"last_login_time":1456036173,"reg_time":1448036828,"last_login_ip":"2130706433","reg_ip":"2130706433","password":"123456789","mobile_verification":false,"email_verification":false,"id":2,"username":"hudelin","update_time":1448036828,"email":"544219413@qq.com","rtime":"2015-11-21 00:27:08","ltime":"2016-02-2114:29:33","mobile":"15986640054"}]
     * code : 01
     * msg : 请求成功
     */

    private String code;
    private String msg;
    /**
     * utime : 2015-11-21 00:27:08
     * status : 1
     * last_login_time : 1456036173
     * reg_time : 1448036828
     * last_login_ip : 2130706433
     * reg_ip : 2130706433
     * password : 123456789
     * mobile_verification : false
     * email_verification : false
     * id : 2
     * username : hudelin
     * update_time : 1448036828
     * email : 544219413@qq.com
     * rtime : 2015-11-21 00:27:08
     * ltime : 2016-02-2114:29:33
     * mobile : 15986640054
     */

    @SerializedName("result")
    private List<passwordBack> passwordBackList;

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

    public List<passwordBack> getPasswordBackList() {
        return passwordBackList;
    }

    public void setPasswordBackList(List<passwordBack> passwordBackList) {
        this.passwordBackList = passwordBackList;
    }

    public static class passwordBack {
        private String utime;
        private int status;
        private int last_login_time;
        private int reg_time;
        private String last_login_ip;
        private String reg_ip;
        private String password;
        private boolean mobile_verification;
        private boolean email_verification;
        private int id;
        private String username;
        private int update_time;
        private String email;
        private String rtime;
        private String ltime;
        private String mobile;

        public String getUtime() {
            return utime;
        }

        public void setUtime(String utime) {
            this.utime = utime;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getLast_login_time() {
            return last_login_time;
        }

        public void setLast_login_time(int last_login_time) {
            this.last_login_time = last_login_time;
        }

        public int getReg_time() {
            return reg_time;
        }

        public void setReg_time(int reg_time) {
            this.reg_time = reg_time;
        }

        public String getLast_login_ip() {
            return last_login_ip;
        }

        public void setLast_login_ip(String last_login_ip) {
            this.last_login_ip = last_login_ip;
        }

        public String getReg_ip() {
            return reg_ip;
        }

        public void setReg_ip(String reg_ip) {
            this.reg_ip = reg_ip;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public boolean isMobile_verification() {
            return mobile_verification;
        }

        public void setMobile_verification(boolean mobile_verification) {
            this.mobile_verification = mobile_verification;
        }

        public boolean isEmail_verification() {
            return email_verification;
        }

        public void setEmail_verification(boolean email_verification) {
            this.email_verification = email_verification;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public int getUpdate_time() {
            return update_time;
        }

        public void setUpdate_time(int update_time) {
            this.update_time = update_time;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getRtime() {
            return rtime;
        }

        public void setRtime(String rtime) {
            this.rtime = rtime;
        }

        public String getLtime() {
            return ltime;
        }

        public void setLtime(String ltime) {
            this.ltime = ltime;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }
    }
}
