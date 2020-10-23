package com.amkj.dmsh.user.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by xiaoxin on 2020/9/23
 * Version:v4.8.0
 */
public class MarketLabelBean {
    private int id;
    private String title;
    //自定义属性 1 为活动标签 0 为营销标签
    @SerializedName(value = "labelCode", alternate = "labelType")
    private int labelCode;
    //活动标签专属属性
    private String activityCode;
    //新人专享活动商品专用字段
    private boolean newUserTag;

    public boolean isNewUserTag() {
        return newUserTag;
    }

    public void setNewUserTag(boolean newUserTag) {
        this.newUserTag = newUserTag;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getLabelCode() {
        return labelCode;
    }

    public void setLabelCode(int labelCode) {
        this.labelCode = labelCode;
    }

    public String getActivityCode() {
        return activityCode;
    }

    public void setActivityCode(String activityCode) {
        this.activityCode = activityCode;
    }
}
