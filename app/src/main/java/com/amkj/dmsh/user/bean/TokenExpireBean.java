package com.amkj.dmsh.user.bean;

import com.amkj.dmsh.base.BaseEntity;

/**
 * Created by xiaoxin on 2019/4/2 0002
 * Version:V3.3.0
 * ClassDescription :token是否过期接口实体类
 */
public class TokenExpireBean extends BaseEntity {

    /**
     * expireTime : 0
     * status : 0
     * sysTime : string
     * uid : 0
     */

    private int expireTime;
    private int tokenExpireSeconds;
    private int status;
    private String sysTime;
    private int uid;

    public int getExpireTime() {
        return expireTime * 1000;
    }

    public int getTokenExpireSeconds() {
        return tokenExpireSeconds*1000;
    }

    public void setTokenExpireSeconds(int tokenExpireSeconds) {
        this.tokenExpireSeconds = tokenExpireSeconds;
    }

    public void setExpireTime(int expireTime) {
        this.expireTime = expireTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getSysTime() {
        return sysTime;
    }

    public void setSysTime(String sysTime) {
        this.sysTime = sysTime;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }
}
