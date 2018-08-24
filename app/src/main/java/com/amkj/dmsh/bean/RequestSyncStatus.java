package com.amkj.dmsh.bean;

import com.amkj.dmsh.constant.XUtilsSyncJsonParse;

import org.xutils.http.annotation.HttpResponse;

/**
 * Created by atd48 on 2016/9/2.
 */
@HttpResponse(parser = XUtilsSyncJsonParse.class)
public class RequestSyncStatus {

    /**
     * code : 01
     * msg : 请求成功
     */

    private String code;
    private String msg;

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
