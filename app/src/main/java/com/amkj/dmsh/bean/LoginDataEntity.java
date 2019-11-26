package com.amkj.dmsh.bean;

import com.amkj.dmsh.base.BaseEntity;
import com.amkj.dmsh.constant.ConstantMethod;
import com.google.gson.annotations.SerializedName;

import static com.amkj.dmsh.constant.ConstantMethod.getStringChangeIntegers;

/**
 * Created by xiaoxin on 2019/11/25
 * Version:v4.3.7
 */
public class LoginDataEntity extends BaseEntity {
    @SerializedName("result")
    private LoginDataBean loginDataBean;

    public LoginDataBean getLoginDataBean() {
        return loginDataBean;
    }

    public void setLoginDataBean(LoginDataBean loginDataBean) {
        this.loginDataBean = loginDataBean;
    }

    public static class LoginDataBean {
        private String uid;
        private String nickname;
        private String mobile;
        private String avatar;
        private String token;
        private String tokenExpireSeconds;
        private String expireTime;
        private String isResetPassword;

        public boolean isResetPassword() {
            return "1".equals(isResetPassword);
        }

        public int getUid() {
            return getStringChangeIntegers(uid);
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public void setTokenExpireSeconds(String tokenExpireSeconds) {
            this.tokenExpireSeconds = tokenExpireSeconds;
        }

        public void setExpireTime(String expireTime) {
            this.expireTime = expireTime;
        }

        public long getExpireTime() {
            return ConstantMethod.getStringChangeLong(expireTime) * 1000;
        }

        public long getTokenExpireSeconds() {
            return ConstantMethod.getStringChangeLong(tokenExpireSeconds) * 1000;
        }

    }
}
