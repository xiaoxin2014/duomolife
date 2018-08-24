package com.amkj.dmsh.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by atd48 on 2016/9/21.
 */
public class BabyInfoDel {

    /**
     * id : 2
     * is_delete : 1
     */

    @SerializedName("result")
    private DelBean delBean;
    /**
     * result : {"id":"2","is_delete":1}
     * code : 01
     * msg : 请求成功
     */

    private String code;
    private String msg;

    public DelBean getDelBean() {
        return delBean;
    }

    public void setDelBean(DelBean delBean) {
        this.delBean = delBean;
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

    public static class DelBean {
        private String id;
        private int is_delete;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getIs_delete() {
            return is_delete;
        }

        public void setIs_delete(int is_delete) {
            this.is_delete = is_delete;
        }
    }
}
