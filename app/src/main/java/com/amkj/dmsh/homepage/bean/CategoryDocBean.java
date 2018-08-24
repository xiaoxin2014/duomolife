package com.amkj.dmsh.homepage.bean;

/**
 * Created by atd48 on 2016/8/29.
 */
public class CategoryDocBean {

    /**
     * id : 1
     * cover :
     * title : 品生活
     * description :
     */

    private ResultBean result;
    /**
     * result : {"id":1,"cover":"","title":"品生活","description":""}
     * code : 01
     * msg : 请求成功
     */

    private String code;
    private String msg;

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
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

    public static class ResultBean {
        private int id;
        private String cover;
        private String title;
        private String description;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }
}
