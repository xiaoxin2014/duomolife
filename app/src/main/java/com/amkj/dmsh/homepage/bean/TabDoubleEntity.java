package com.amkj.dmsh.homepage.bean;


import com.amkj.dmsh.views.flycoTablayout.listener.CustomTabDoubleEntity;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/5/18
 * class description:请输入类描述
 */

public class TabDoubleEntity implements CustomTabDoubleEntity {

    private String tabTitle;
    private String tabSubTitle;

    public TabDoubleEntity() {
    }

    public TabDoubleEntity(String tabTitle, String tabSubTitle) {
        this.tabTitle = tabTitle;
        this.tabSubTitle = tabSubTitle;
    }

    public void setTabTitle(String tabTitle) {
        this.tabTitle = tabTitle;
    }

    @Override
    public String getTabTitle() {
        return tabTitle;
    }

    public void setTabSubTitle(String tabSubTitle) {
        this.tabSubTitle = tabSubTitle;
    }

    @Override
    public String getTabSubtitle() {
        return tabSubTitle;
    }
}
