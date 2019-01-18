package com.amkj.dmsh.mine.bean;

import com.amkj.dmsh.base.BaseEntity;
import com.google.gson.annotations.SerializedName;

import java.util.List;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2019/1/17
 * version 3.2.0
 * class description:足迹时间轴
 */
public class MineBrowsHistoryTimeShaftEntity extends BaseEntity {
    @SerializedName("result")
    private List<String> historyTimeShaftList;

    public List<String> getHistoryTimeShaftList() {
        return historyTimeShaftList;
    }

    public void setHistoryTimeShaftList(List<String> historyTimeShaftList) {
        this.historyTimeShaftList = historyTimeShaftList;
    }
}
