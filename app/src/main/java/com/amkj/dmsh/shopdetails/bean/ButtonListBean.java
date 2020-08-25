package com.amkj.dmsh.shopdetails.bean;

import com.amkj.dmsh.constant.ConstantMethod;

/**
 * Created by xiaoxin on 2020/3/24
 * Version:v4.5.0
 * ClassDescription :主订单操作按钮
 */
public class ButtonListBean {
    /**
     * id : 1
     * clickable : 1
     * btnText : 确认收货
     */

    private String id;//按钮类型
    private String clickable;//按钮是否可点击
    private String btnText;

    public ButtonListBean(String id, String clickable, String btnText) {
        this.id = id;
        this.clickable = clickable;
        this.btnText = btnText;
    }

    public int getId() {
        return ConstantMethod.getStringChangeIntegers(id);
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isClickable() {
        return "1".equals(clickable);
    }

    public void setClickable(String clickable) {
        this.clickable = clickable;
    }

    public String getBtnText() {
        return btnText;
    }

    public void setBtnText(String btnText) {
        this.btnText = btnText;
    }
}
