package com.amkj.dmsh.shopdetails.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2016/12/14
 * class description:请输入类描述
 */
public class DirectGoodsServerEntity {

    /**
     * result : {"service_promise":[{"type":"text","content":"<p>数码<\/p>"}],"id":9,"title":"数码"}
     * msg : 请求成功
     * code : 01
     */

    @SerializedName("result")
    private DirectGoodsServerBean directGoodsServerBean;
    private String msg;
    private String code;

    public DirectGoodsServerBean getDirectGoodsServerBean() {
        return directGoodsServerBean;
    }

    public void setDirectGoodsServerBean(DirectGoodsServerBean directGoodsServerBean) {
        this.directGoodsServerBean = directGoodsServerBean;
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

    public static class DirectGoodsServerBean {
        /**
         * service_promise : [{"type":"text","content":"<p>数码<\/p>"}]
         * id : 9
         * title : 数码
         */

        private int id;
        private String title;
        @SerializedName("service_promise")
        private List<ServicePromiseBean> servicePromiseList;

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

        public List<ServicePromiseBean> getServicePromiseList() {
            return servicePromiseList;
        }

        public void setServicePromiseList(List<ServicePromiseBean> servicePromiseList) {
            this.servicePromiseList = servicePromiseList;
        }

        public static class ServicePromiseBean {
            /**
             * type : text
             * content : <p>数码</p>
             */

            private String type;
            private String content;

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }
        }
    }
}
