package com.amkj.dmsh.mine.bean;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/11/9
 * class description:个人信息
 */

public class SavePersonalInfoBean {
    private int uid;
    private String nickName;
    private String avatar;
    private String phoneNum;
    private String loginType;
    private String openId;
    /**
     * 微信专属Id
     */
    private String unionId;
    private boolean isLogin;

    public SavePersonalInfoBean() {
    }

    public SavePersonalInfoBean(boolean isLogin) {
        this.isLogin = isLogin;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }
}
