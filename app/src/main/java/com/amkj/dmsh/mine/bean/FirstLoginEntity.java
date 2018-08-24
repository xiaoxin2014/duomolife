package com.amkj.dmsh.mine.bean;

import com.google.gson.annotations.SerializedName;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/6/29
 * class description:请输入类描述
 */

public class FirstLoginEntity {

    /**
     * result : {"images":"http://img.domolife.cn/platform/eTQjkB68c31498708992602.jpg","id":1,"title":"测试","status":1}
     * msg : 请求成功
     * code : 01
     */

    @SerializedName("result")
    private FirstLoginBean firstLoginBean;
    private String msg;
    private String code;

    public FirstLoginBean getFirstLoginBean() {
        return firstLoginBean;
    }

    public void setFirstLoginBean(FirstLoginBean firstLoginBean) {
        this.firstLoginBean = firstLoginBean;
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

    public static class FirstLoginBean {
        /**
         * images : http://img.domolife.cn/platform/eTQjkB68c31498708992602.jpg
         * id : 1
         * title : 测试
         * status : 1
         */

        private String images;
        private int id;
        private String title;
        private int status;

        public String getImages() {
            return images;
        }

        public void setImages(String images) {
            this.images = images;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }
}
