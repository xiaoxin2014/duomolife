package com.amkj.dmsh.constant;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/10/14
 * class description:通用详情格式
 */

public class CommunalDetailBean {
    private String type;
    private Object content;
    private int id;
    private String picUrl;
    private String image;
    private String name;
    private String from;
    /**
     * androidLink : android.apk
     * webLink : web.com
     * iosLink : ios.ios
     * text : 文本
     * title : 标题
     */

    private String androidLink;
    private String text;
    private String title;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAndroidLink() {
        return androidLink;
    }

    public void setAndroidLink(String androidLink) {
        this.androidLink = androidLink;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
