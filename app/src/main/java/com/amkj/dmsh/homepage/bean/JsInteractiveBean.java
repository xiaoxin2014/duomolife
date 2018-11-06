package com.amkj.dmsh.homepage.bean;

import java.util.Map;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/11/6
 * version 3.1.8
 * class description:js交互数据
 */
public class JsInteractiveBean {
    private String type;
    private Map<String,Object> otherData;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map<String, Object> getOtherData() {
        return otherData;
    }

    public void setOtherData(Map<String, Object> otherData) {
        this.otherData = otherData;
    }
}
