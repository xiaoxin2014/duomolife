package com.amkj.dmsh.message.bean;

/**
 * Created by xiaoxin on 2020/4/7
 * Version:v4.5.0
 * ClassDescription :消息中心列表
 */
public class MessageCenterBean {
    private String msgIcon;
    private int msgNum;
    private String msgType;
    private String msgDescription;
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMsgIcon() {
        return msgIcon;
    }

    public void setMsgIcon(String msgIcon) {
        this.msgIcon = msgIcon;
    }

    public int getMsgNum() {
        return msgNum;
    }

    public void setMsgNum(int msgNum) {
        this.msgNum = msgNum;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getMsgDescription() {
        return msgDescription;
    }

    public void setMsgDescription(String msgDescription) {
        this.msgDescription = msgDescription;
    }
}
