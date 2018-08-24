package com.amkj.dmsh.bean;

/**
 * Created by atd48 on 2016/9/21.
 */
public class ArticleLiked {

    /**
     * isFavor : true
     * code : 01
     * msg : 请求成功
     */

    private boolean isFavor;
    private String code;
    private String msg;

    public boolean isIsFavor() {
        return isFavor;
    }

    public void setIsFavor(boolean isFavor) {
        this.isFavor = isFavor;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
