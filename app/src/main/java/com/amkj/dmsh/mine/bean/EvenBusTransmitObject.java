package com.amkj.dmsh.mine.bean;

/**
 * Created by atd48 on 2016/10/24.
 */
public class EvenBusTransmitObject {
//    新的数量
    private int count;
    private int oldCount;
    private Object carProductObject;

    public Object getCarProductObject() {
        return carProductObject;
    }

    public void setCarProductObject(Object carProductObject) {
        this.carProductObject = carProductObject;
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
}
