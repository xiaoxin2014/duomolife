package com.amkj.dmsh.homepage.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/5/4
 * class description:请输入类描述
 */

public class TimeShowNewHoriEntity {

    /**
     * currentTime : 2017-05-06 15:00:42
     * msg : 请求成功
     * code : 01
     * startHours : ["10","20"]
     * goods : {"10":[{"marketPrice":"298.00","quantity":100,"picUrl":"http://img.domolife.cn/platform/20170504/20170504155326024.jpg","previsionFlag":0,"price":"120.00","subtitle":null,"name":"宝宝趣味助步车","startTime":"2017-05-05 10:00:00","id":7017,"maxPrice":"120.00","endTime":"2017-05-10 23:59:59"},{"marketPrice":"45.80","quantity":100,"picUrl":"http://img.domolife.cn/platform/20170504/20170504155529351.jpg","previsionFlag":0,"price":"39.00","subtitle":null,"name":"小猪佩奇填色本","startTime":"2017-05-05 10:00:00","id":7018,"maxPrice":"39.00","endTime":"2017-05-10 23:59:59"}],"20":[{"marketPrice":"32.80","quantity":100,"picUrl":"http://img.domolife.cn/platform/20170504/20170504170133564.jpg","previsionFlag":0,"price":"28.00","subtitle":null,"name":"DM Prinzessin梦幻系列 小公主洗发水","startTime":"2017-05-05 20:00:00","id":7026,"maxPrice":"28.00","endTime":"2017-05-10 23:59:59"},{"marketPrice":"32.80","quantity":100,"picUrl":"http://img.domolife.cn/platform/20170504/20170504170455082.jpg","previsionFlag":0,"price":"28.00","subtitle":null,"name":" DM Prinzessin梦幻系列小公主儿童闪亮沐浴露","startTime":"2017-05-05 20:00:00","id":7027,"maxPrice":"28.00","endTime":"2017-05-10 23:59:59"}]}
     */

    private String currentTime;
    private String msg;
    private String code;
    private Map<String, ArrayList<TimeForeShowEntity.SpringSaleBean>> goods;
    private List<String> startHours;

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Map<String, ArrayList<TimeForeShowEntity.SpringSaleBean>> getGoods() {
        return goods;
    }

    public void setGoods(Map<String, ArrayList<TimeForeShowEntity.SpringSaleBean>> goods) {
        this.goods = goods;
    }

    public List<String> getStartHours() {
        return startHours;
    }

    public void setStartHours(List<String> startHours) {
        this.startHours = startHours;
    }
}
