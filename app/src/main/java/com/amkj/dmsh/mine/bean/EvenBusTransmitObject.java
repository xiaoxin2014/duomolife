package com.amkj.dmsh.mine.bean;

/**
 * Created by atd48 on 2016/10/24.
 */
public class EvenBusTransmitObject {
    private int position;
//    新的数量
    private int count;
    private int oldCount;
    private boolean isSelected;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getOldCount() {
        return oldCount;
    }

    public void setOldCount(int oldCount) {
        this.oldCount = oldCount;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
