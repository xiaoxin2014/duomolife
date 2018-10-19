package com.amkj.dmsh.bean;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/10/19
 * version 3.1.8
 * class description:首页icon数据保存
 */
public class MainIconBean {
    private int position;
    private String iconUrl;

    public MainIconBean(int position, String iconUrl) {
        this.position = position;
        this.iconUrl = iconUrl;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
