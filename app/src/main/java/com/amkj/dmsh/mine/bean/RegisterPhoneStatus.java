package com.amkj.dmsh.mine.bean;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/4/28
 * class description:请输入类描述
 */

public class RegisterPhoneStatus {

    /**
     * result : 该手机号已注册
     * registerFlag : 1
     * msg : 请求成功
     * code : 01
     */

    private String result;
    private int registerFlag;
    private String msg;
    private String code;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public int getRegisterFlag() {
        return registerFlag;
    }

    public void setRegisterFlag(int registerFlag) {
        this.registerFlag = registerFlag;
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
