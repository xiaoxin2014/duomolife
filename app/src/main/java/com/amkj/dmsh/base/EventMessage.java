package com.amkj.dmsh.base;

/**
 * Created by Administrator on 2016/4/28 0028.
 */
public class EventMessage {
    public String type;
    public Object result;

    public EventMessage(String type, Object result) {
        this.type = type;
        this.result = result;
    }

    public EventMessage(String type) {
        this.type = type;
    }
}
