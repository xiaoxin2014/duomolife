package com.amkj.dmsh.bean;

/**
 * Created by xiaoxin on 2019/6/29
 * Version:v4.1.0
 * ClassDescription :新版购物车数量
 */
public class MessageBean {

    /**
     * result : 11
     * msg : 请求成功
     * code : 01
     */

    private int result;
    private String msg;
    private String code;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
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
}
