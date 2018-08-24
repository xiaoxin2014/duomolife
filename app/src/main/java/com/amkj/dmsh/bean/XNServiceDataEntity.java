package com.amkj.dmsh.bean;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/1/19
 * version 3.7
 * class description:小能客服数据
 */

public class XNServiceDataEntity {

    /**
     * result : [{"weeks":"1,2,3,4,5,6,7","name":"正常接待组","end_time":"23:59:59","begin_time":"00:00:00","xn_id":"kf_10060_1509076165253","id":1}]
     * msg : 请求成功
     * server_time : 2018-01-19 14:34:43
     * code : 01
     */

    private String msg;
    private String server_time;
    private String code;
    @SerializedName("result")
    private List<XNServiceDataBean> serviceDataList;

    public static XNServiceDataEntity objectFromData(String str) {

        return new Gson().fromJson(str, XNServiceDataEntity.class);
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getServer_time() {
        return server_time;
    }

    public void setServer_time(String server_time) {
        this.server_time = server_time;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<XNServiceDataBean> getServiceDataList() {
        return serviceDataList;
    }

    public void setServiceDataList(List<XNServiceDataBean> serviceDataList) {
        this.serviceDataList = serviceDataList;
    }

    public static class XNServiceDataBean {
        /**
         * weeks : 1,2,3,4,5,6,7
         * name : 正常接待组
         * end_time : 23:59:59
         * begin_time : 00:00:00
         * xn_id : kf_10060_1509076165253
         * id : 1
         */

        private String weeks;
        private String name;
        private String end_time;
        private String begin_time;
        private String xn_id;
        private int id;

        public static XNServiceDataBean objectFromData(String str) {

            return new Gson().fromJson(str, XNServiceDataBean.class);
        }

        public String getWeeks() {
            return weeks;
        }

        public void setWeeks(String weeks) {
            this.weeks = weeks;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEnd_time() {
            return end_time;
        }

        public void setEnd_time(String end_time) {
            this.end_time = end_time;
        }

        public String getBegin_time() {
            return begin_time;
        }

        public void setBegin_time(String begin_time) {
            this.begin_time = begin_time;
        }

        public String getXn_id() {
            return xn_id;
        }

        public void setXn_id(String xn_id) {
            this.xn_id = xn_id;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}
