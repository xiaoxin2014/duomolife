package com.amkj.dmsh.shopdetails.bean;

import java.util.List;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/8/21
 * class description:请输入类描述
 */

public class RefundLogisticEntity {

    /**
     * expressCompanys : ["申通E物流","圆通速递","中通速递","韵达快运","天天快递","百世汇通","顺丰速运","邮政国内小包","EMS经济快递","EMS","邮政平邮","德邦快递","联昊通","全峰快递","全一快递","城市100","汇强快递","广东EMS","速尔","飞康达速运","宅急送","联邦快递","德邦物流","中铁快运","信丰物流","龙邦速递","天地华宇","快捷速递","圆通快递","顺丰快递","优速快递","其他"]
     * msg : 请求成功
     * code : 01
     */

    private String msg;
    private String code;
    private List<String> expressCompanys;

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

    public List<String> getExpressCompanys() {
        return expressCompanys;
    }

    public void setExpressCompanys(List<String> expressCompanys) {
        this.expressCompanys = expressCompanys;
    }
}
