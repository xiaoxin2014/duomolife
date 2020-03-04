package com.amkj.dmsh.bean;

/**
 * Created by xiaoxin on 2020/3/3
 * Version:v4.4.2
 * ClassDescription :
 */
public class TabNameBean {
    private String tabName;
    private int position;

    public TabNameBean(String tabName, int position) {
        this.tabName = tabName;
        this.position = position;
    }

    public String getTabName() {
        return tabName;
    }


    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
