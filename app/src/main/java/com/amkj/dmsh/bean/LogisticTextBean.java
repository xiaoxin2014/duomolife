package com.amkj.dmsh.bean;

/**
 * Created by xiaoxin on 2020/4/3
 * Version:v4.4.3
 */
public class LogisticTextBean {
    /**
     * time : 2020-03-23 11:50:09
     * status : 该物流暂无流转信息，请耐心等待或联系客服!
     */

    private String time;
    private String status;

    public LogisticTextBean(String time, String status) {
        this.time = time;
        this.status = status;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
