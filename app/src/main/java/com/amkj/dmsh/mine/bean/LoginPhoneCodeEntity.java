package com.amkj.dmsh.mine.bean;

import com.amkj.dmsh.bean.CommunalUserInfoEntity.CommunalUserInfoBean;
import com.google.gson.annotations.SerializedName;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/9/7
 * class description:手机验证码登录确认
 */

public class LoginPhoneCodeEntity {

    /**
     * result : {"object":{"birthday":"2015-08-02","rtime":"2017-06-16 17:08:11","last_login_time":0,"baby_status":0,"firstAppLogin":0,"device_type":"web","login":0,"had_remind":false,"is_admin":0,"uid":34635,"score":987654181,"reg_time":0,"nickname":"1371528****","is_disable":1,"bgimg_url":"","fllow":0,"documentcount":0,"sex":1,"mobile":"13715280947","avatar":"http://img.domolife.cn/test/20170806134738.jpg","is_sign":0,"fans":0,"domo_score":31,"reg_ip":"127.0.0.1","remindtime":10,"status":1},"resultCode":SUCCESS_CODE,"resultMsg":"验证成功"}
     * msg : 请求成功
     * code : 01
     */

    @SerializedName("result")
    private LoginPhoneCodeBean loginPhoneCodeBean;
    private String msg;
    private String code;

    public LoginPhoneCodeBean getLoginPhoneCodeBean() {
        return loginPhoneCodeBean;
    }

    public void setLoginPhoneCodeBean(LoginPhoneCodeBean loginPhoneCodeBean) {
        this.loginPhoneCodeBean = loginPhoneCodeBean;
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

    public static class LoginPhoneCodeBean {
        /**
         * object : {"birthday":"2015-08-02","rtime":"2017-06-16 17:08:11","last_login_time":0,"baby_status":0,"firstAppLogin":0,"device_type":"web","login":0,"had_remind":false,"is_admin":0,"uid":34635,"score":987654181,"reg_time":0,"nickname":"1371528****","is_disable":1,"bgimg_url":"","fllow":0,"documentcount":0,"sex":1,"mobile":"13715280947","avatar":"http://img.domolife.cn/test/20170806134738.jpg","is_sign":0,"fans":0,"domo_score":31,"reg_ip":"127.0.0.1","remindtime":10,"status":1}
         * resultCode : 01
         * resultMsg : 验证成功
         */
        @SerializedName("object")
        private CommunalUserInfoBean communalUserInfoBean;
        private String resultCode;
        private String resultMsg;

        public CommunalUserInfoBean getCommunalUserInfoBean() {
            return communalUserInfoBean;
        }

        public void setCommunalUserInfoBean(CommunalUserInfoBean communalUserInfoBean) {
            this.communalUserInfoBean = communalUserInfoBean;
        }

        public String getResultCode() {
            return resultCode;
        }

        public void setResultCode(String resultCode) {
            this.resultCode = resultCode;
        }

        public String getResultMsg() {
            return resultMsg;
        }

        public void setResultMsg(String resultMsg) {
            this.resultMsg = resultMsg;
        }
    }
}
