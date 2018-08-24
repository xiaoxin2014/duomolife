package com.amkj.dmsh.release.bean;

import java.io.Serializable;


public class DraftInfo implements Serializable {

    public int id;
    public String time;
    public String text;
    public String html;
    public String title;
    public String type;
    public String tag;
    public String atNameId;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAtNameId() {
        return atNameId;
    }

    public void setAtNameId(String atNameId) {
        this.atNameId = atNameId;
    }
}
