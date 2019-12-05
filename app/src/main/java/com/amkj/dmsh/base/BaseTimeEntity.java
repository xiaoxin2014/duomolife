package com.amkj.dmsh.base;

import com.google.gson.annotations.SerializedName;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/8/17
 * version 1.0
 * class description:服务器时间基类实体
 */
public class BaseTimeEntity extends BaseEntity{
    @SerializedName(value = "sysTime",alternate = "currentTime")
    private String systemTime;

    public String getCurrentTime() {
        return systemTime;
    }

    public void setCurrentTime(String systemTime) {
        this.systemTime = systemTime;
    }
}
