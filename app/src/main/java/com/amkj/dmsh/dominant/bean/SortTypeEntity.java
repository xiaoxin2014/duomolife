package com.amkj.dmsh.dominant.bean;

import com.google.gson.Gson;

import java.util.Map;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/3/14
 * version 3.0.9
 * class description:分类排序
 */

public class SortTypeEntity {

    /**
     * msg : 请求成功
     * categoryOrderType : [{"1":"默认排序","2":"销量","3":"收藏","4":"最新","5":"价格最高","6":"价格最低"}]
     * code : 01
     */

    private String msg;
    private String code;
    private Map<String,String> categoryOrderType;

    public static SortTypeEntity objectFromData(String str) {

        return new Gson().fromJson(str, SortTypeEntity.class);
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

    public Map<String, String> getCategoryOrderType() {
        return categoryOrderType;
    }

    public void setCategoryOrderType(Map<String, String> categoryOrderType) {
        this.categoryOrderType = categoryOrderType;
    }
}
