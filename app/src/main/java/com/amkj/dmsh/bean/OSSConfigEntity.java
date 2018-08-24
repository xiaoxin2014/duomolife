package com.amkj.dmsh.bean;

import com.google.gson.annotations.SerializedName;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/7/12
 * class description:请输入类描述
 */

public class OSSConfigEntity {

    /**
     * result : {"endpoint":"rBIVBiJdJIDMzZQiJ6YKYYO1CaDgomfcoQR2XcMoK0I=","bucketname":"N48KN4UHeo+dKVrgUWZUVw==","accesskeysecret":"ctMD8XOOqWxBr6npSlvwTL1Sw0srcQMlZpCfpVZqOcs=","accesskeyid":"2UuOptgW8jQN3fCSvBawH0w3oO+dM75L7csYBVFVVfA="}
     * msg : 请求成功
     * code : 01
     */

    @SerializedName("result")
    private OSSConfigBean ossConfigBean;
    private String msg;
    private String code;

    public OSSConfigBean getOssConfigBean() {
        return ossConfigBean;
    }

    public void setOssConfigBean(OSSConfigBean ossConfigBean) {
        this.ossConfigBean = ossConfigBean;
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

    public static class OSSConfigBean {
        /**
         * endpoint : rBIVBiJdJIDMzZQiJ6YKYYO1CaDgomfcoQR2XcMoK0I=
         * bucketname : N48KN4UHeo+dKVrgUWZUVw==
         * accesskeysecret : ctMD8XOOqWxBr6npSlvwTL1Sw0srcQMlZpCfpVZqOcs=
         * accesskeyid : 2UuOptgW8jQN3fCSvBawH0w3oO+dM75L7csYBVFVVfA=
         */

        private String endpoint;
        private String bucketname;
        private String accesskeysecret;
        private String accesskeyid;
        private String url;

        public String getEndpoint() {
            return endpoint;
        }

        public void setEndpoint(String endpoint) {
            this.endpoint = endpoint;
        }

        public String getBucketname() {
            return bucketname;
        }

        public void setBucketname(String bucketname) {
            this.bucketname = bucketname;
        }

        public String getAccesskeysecret() {
            return accesskeysecret;
        }

        public void setAccesskeysecret(String accesskeysecret) {
            this.accesskeysecret = accesskeysecret;
        }

        public String getAccesskeyid() {
            return accesskeyid;
        }

        public void setAccesskeyid(String accesskeyid) {
            this.accesskeyid = accesskeyid;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
