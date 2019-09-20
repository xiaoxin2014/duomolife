package com.amkj.dmsh.find.bean;

/**
 * Created by xiaoxin on 2019/7/26
 * Version:v4.1.0
 */
public class EventMessageBean {
    private String simpleName;
    private Object msg;

    public EventMessageBean(String simpleName, Object msg) {
        this.simpleName = simpleName;
        this.msg = msg;
    }

    public String getSimpleName() {
        return simpleName;
    }

    public void setSimpleName(String simpleName) {
        this.simpleName = simpleName;
    }

    public Object getmsg() {
        return msg;
    }

    public void setmsg(String msg) {
        this.msg = msg;
    }
}
