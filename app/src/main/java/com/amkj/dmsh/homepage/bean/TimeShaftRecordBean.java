package com.amkj.dmsh.homepage.bean;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/9/17
 * version 3.1.6
 * class description:时间轴记录
 */
public class TimeShaftRecordBean {
//    滚动position
    private int scrollPosition;
//    时间名字
    private String weekName;
//    是否被选中
    private boolean select;

    public int getScrollPosition() {
        return scrollPosition;
    }

    public void setScrollPosition(int scrollPosition) {
        this.scrollPosition = scrollPosition;
    }

    public String getWeekName() {
        return weekName;
    }

    public void setWeekName(String weekName) {
        this.weekName = weekName;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }
}
