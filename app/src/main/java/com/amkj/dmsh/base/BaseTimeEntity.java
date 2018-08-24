package com.amkj.dmsh.base;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/8/17
 * version 1.0
 * class description:服务器时间基类实体
 */
public class BaseTimeEntity extends BaseEntity{
    private String sysTime;

    public String getSysTime() {
        return sysTime;
    }

    public void setSysTime(String sysTime) {
        this.sysTime = sysTime;
    }
}
