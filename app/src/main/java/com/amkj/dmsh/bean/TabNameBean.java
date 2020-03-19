package com.amkj.dmsh.bean;

/**
 * Created by xiaoxin on 2020/3/3
 * Version:v4.4.2
 * ClassDescription :
 */
public class TabNameBean {
    private String tabName;
    private int position;
    private String simpleName;

    public TabNameBean(String tabName, int position, String simpleName) {
        this.tabName = tabName;
        this.position = position;
        this.simpleName = simpleName;
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

    public void setTabName(String tabName) {
        this.tabName = tabName;
    }

    public String getSimpleName() {
        return simpleName;
    }

    public void setSimpleName(String simpleName) {
        this.simpleName = simpleName;
    }
}
