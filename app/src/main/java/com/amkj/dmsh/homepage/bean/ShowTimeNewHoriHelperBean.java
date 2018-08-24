package com.amkj.dmsh.homepage.bean;

import java.util.ArrayList;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/5/6
 * class description:请输入类描述
 */

public class ShowTimeNewHoriHelperBean {
    private String timeNumber;
    private ArrayList<TimeForeShowEntity.SpringSaleBean> showTimeGoods;
    private boolean isLoadMore;
    private int pageIndex;
    private String dayTime;

    public String getTimeNumber() {
        return timeNumber;
    }

    public void setTimeNumber(String timeNumber) {
        this.timeNumber = timeNumber;
    }

    public ArrayList<TimeForeShowEntity.SpringSaleBean> getShowTimeGoods() {
        return showTimeGoods;
    }

    public void setShowTimeGoods(ArrayList<TimeForeShowEntity.SpringSaleBean> showTimeGoods) {
        this.showTimeGoods = showTimeGoods;
    }

    public boolean isLoadMore() {
        return isLoadMore;
    }

    public void setLoadMore(boolean loadMore) {
        isLoadMore = loadMore;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public String getDayTime() {
        return dayTime;
    }

    public void setDayTime(String dayTime) {
        this.dayTime = dayTime;
    }
}
