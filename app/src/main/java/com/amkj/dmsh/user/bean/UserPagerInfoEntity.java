package com.amkj.dmsh.user.bean;

import com.amkj.dmsh.base.BaseEntity;
import com.google.gson.annotations.SerializedName;

/**
 * Created by atd48 on 2016/9/30.
 */
public class UserPagerInfoEntity extends BaseEntity {

    /**
     * is_sign : 1
     * fllow : 14
     * uid : 23291
     * sex : 0
     * baby_verification : false
     * nickname : 18123961075
     * last_login_time : 0
     * status : 1
     * fans : 4
     * device_type : web
     * is_disable : 1
     * score : 110
     * reg_time : 0
     * remindtime : 30
     * avatar : http://tva1.sinaimg.cn/crop.0.0.180.180.50/6e6f989cjw1e8qgp5bmzyj2050050aa8.jpg
     * reg_ip : 0:0:0:0:0:0:0:1
     * mobile_verification : true
     * flag : true
     * rtime : 2016-08-31 19:39:07
     * login : 0
     * mobile : 18123961075
     */

    @SerializedName("result")
    private UserInfoBean userInfo;

    /**
     * result : {"is_sign":1,"fllow":14,"uid":23291,"sex":0,"baby_verification":false,"nickname":"18123961075","last_login_time":0,"status":1,"fans":4,"device_type":"web","is_disable":1,"score":110,"reg_time":0,"remindtime":30,"avatar":"http://tva1.sinaimg.cn/crop.0.0.180.180.50/6e6f989cjw1e8qgp5bmzyj2050050aa8.jpg","reg_ip":"0:0:0:0:0:0:0:1","mobile_verification":true,"flag":true,"rtime":"2016-08-31 19:39:07","login":0,"mobile":"18123961075"}
     * code : 01
     * msg : 请求成功
     */
    public UserInfoBean getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfoBean userInfo) {
        this.userInfo = userInfo;
    }


    public static class UserInfoBean {
        private int is_sign;
        @SerializedName(value = "fllow", alternate = "focusNum")
        private int fllow;
        private int uid;
        private int sex;
        private boolean baby_verification;
        private String nickname;
        private int last_login_time;
        private int status;
        @SerializedName(value = "fans", alternate = "fansNum")
        private int fans;
        private String device_type;
        private int is_disable;
        private int score;
        private int reg_time;
        private int remindtime;
        private String avatar;
        private String reg_ip;
        private boolean mobile_verification;
        private boolean flag;
        private String rtime;
        private int login;
        private String mobile;
        private String signature;
        private String bgimg_url;
        private int isFocus;


        public boolean isFocus() {
            return isFocus == 1;
        }

        public void setIsFocus(boolean isFocus) {
            this.isFocus = isFocus ? 1 : 0;
        }

        public String getBgimg_url() {
            return bgimg_url;
        }

        public void setBgimg_url(String bgimg_url) {
            this.bgimg_url = bgimg_url;
        }

        public String getSignature() {
            return signature;
        }

        public void setSignature(String signature) {
            this.signature = signature;
        }

        public int getIs_sign() {
            return is_sign;
        }

        public void setIs_sign(int is_sign) {
            this.is_sign = is_sign;
        }

        public int getFllow() {
            return fllow;
        }

        public void setFllow(int fllow) {
            this.fllow = fllow;
        }

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public boolean isBaby_verification() {
            return baby_verification;
        }

        public void setBaby_verification(boolean baby_verification) {
            this.baby_verification = baby_verification;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public int getLast_login_time() {
            return last_login_time;
        }

        public void setLast_login_time(int last_login_time) {
            this.last_login_time = last_login_time;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getFans() {
            return fans;
        }

        public void setFans(int fans) {
            this.fans = fans;
        }

        public String getDevice_type() {
            return device_type;
        }

        public void setDevice_type(String device_type) {
            this.device_type = device_type;
        }

        public int getIs_disable() {
            return is_disable;
        }

        public void setIs_disable(int is_disable) {
            this.is_disable = is_disable;
        }

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }

        public int getReg_time() {
            return reg_time;
        }

        public void setReg_time(int reg_time) {
            this.reg_time = reg_time;
        }

        public int getRemindtime() {
            return remindtime;
        }

        public void setRemindtime(int remindtime) {
            this.remindtime = remindtime;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getReg_ip() {
            return reg_ip;
        }

        public void setReg_ip(String reg_ip) {
            this.reg_ip = reg_ip;
        }

        public boolean isMobile_verification() {
            return mobile_verification;
        }

        public void setMobile_verification(boolean mobile_verification) {
            this.mobile_verification = mobile_verification;
        }

        public String getRtime() {
            return rtime;
        }

        public void setRtime(String rtime) {
            this.rtime = rtime;
        }

        public int getLogin() {
            return login;
        }

        public void setLogin(int login) {
            this.login = login;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }
    }


}
