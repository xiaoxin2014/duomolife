package com.amkj.dmsh.shopdetails.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by atd48 on 2016/10/31.
 */
public class DirectLogisticsPacketEntity {

    /**
     * expressNo : 70141107146139
     * logistics : {"msg":"ok","result":{"list":[{"time":"2016-08-17 21:58:12","status":"【已签收，签收人是已送18682375538】"},{"time":"2016-08-17 17:44:48","status":"深圳市【深圳福田梅林分部】，【张县委/18682375538】正在派件"},{"time":"2016-08-17 08:35:45","status":"到深圳市【深圳福田梅林分部】"},{"time":"2016-08-17 05:29:37","status":"深圳市【深圳转运中心】，正发往【深圳福田梅林分部】"},{"time":"2016-08-17 04:41:36","status":"到深圳市【深圳转运中心】"},{"time":"2016-08-17 04:41:36","status":"到深圳市【深圳转运中心】"},{"time":"2016-08-17 02:21:52","status":"东莞市【虎门转运中心】，正发往【深圳转运中心】"},{"time":"2016-08-17 01:14:00","status":"到东莞市【虎门转运中心】"},{"time":"2016-08-15 20:46:47","status":"合肥市【合肥转运中心】，正发往【虎门转运中心】"},{"time":"2016-08-15 20:44:00","status":"到合肥市【合肥转运中心】"},{"time":"2016-08-15 14:02:45","status":"【蜀山七部】揽收成功"}],"deliverystatus":"3","type":"htky","number":"70141107146139","issign":"1"},"status":"0"}
     */

    @SerializedName("result")
    private DirectLogisticsPacketBean directLogisticsPacketBean;
    /**
     * result : {"expressNo":"70141107146139","logistics":{"msg":"ok","result":{"list":[{"time":"2016-08-17 21:58:12","status":"【已签收，签收人是已送18682375538】"},{"time":"2016-08-17 17:44:48","status":"深圳市【深圳福田梅林分部】，【张县委/18682375538】正在派件"},{"time":"2016-08-17 08:35:45","status":"到深圳市【深圳福田梅林分部】"},{"time":"2016-08-17 05:29:37","status":"深圳市【深圳转运中心】，正发往【深圳福田梅林分部】"},{"time":"2016-08-17 04:41:36","status":"到深圳市【深圳转运中心】"},{"time":"2016-08-17 04:41:36","status":"到深圳市【深圳转运中心】"},{"time":"2016-08-17 02:21:52","status":"东莞市【虎门转运中心】，正发往【深圳转运中心】"},{"time":"2016-08-17 01:14:00","status":"到东莞市【虎门转运中心】"},{"time":"2016-08-15 20:46:47","status":"合肥市【合肥转运中心】，正发往【虎门转运中心】"},{"time":"2016-08-15 20:44:00","status":"到合肥市【合肥转运中心】"},{"time":"2016-08-15 14:02:45","status":"【蜀山七部】揽收成功"}],"deliverystatus":"3","type":"htky","number":"70141107146139","issign":"1"},"status":"0"}}
     * code : 01
     * msg : 请求成功
     */

    private String code;
    private String msg;

    public DirectLogisticsPacketBean getDirectLogisticsPacketBean() {
        return directLogisticsPacketBean;
    }

    public void setDirectLogisticsPacketBean(DirectLogisticsPacketBean directLogisticsPacketBean) {
        this.directLogisticsPacketBean = directLogisticsPacketBean;
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

    public static class DirectLogisticsPacketBean {
        private String expressNo;
        /**
         * msg : ok
         * result : {"list":[{"time":"2016-08-17 21:58:12","status":"【已签收，签收人是已送18682375538】"},{"time":"2016-08-17 17:44:48","status":"深圳市【深圳福田梅林分部】，【张县委/18682375538】正在派件"},{"time":"2016-08-17 08:35:45","status":"到深圳市【深圳福田梅林分部】"},{"time":"2016-08-17 05:29:37","status":"深圳市【深圳转运中心】，正发往【深圳福田梅林分部】"},{"time":"2016-08-17 04:41:36","status":"到深圳市【深圳转运中心】"},{"time":"2016-08-17 04:41:36","status":"到深圳市【深圳转运中心】"},{"time":"2016-08-17 02:21:52","status":"东莞市【虎门转运中心】，正发往【深圳转运中心】"},{"time":"2016-08-17 01:14:00","status":"到东莞市【虎门转运中心】"},{"time":"2016-08-15 20:46:47","status":"合肥市【合肥转运中心】，正发往【虎门转运中心】"},{"time":"2016-08-15 20:44:00","status":"到合肥市【合肥转运中心】"},{"time":"2016-08-15 14:02:45","status":"【蜀山七部】揽收成功"}],"deliverystatus":"3","type":"htky","number":"70141107146139","issign":"1"}
         * status : 0
         */

        @SerializedName("logistics")
        private LogisticsEntity logisticsEntity;

        public String getExpressNo() {
            return expressNo;
        }

        public void setExpressNo(String expressNo) {
            this.expressNo = expressNo;
        }

        public LogisticsEntity getLogisticsEntity() {
            return logisticsEntity;
        }

        public void setLogisticsEntity(LogisticsEntity logisticsEntity) {
            this.logisticsEntity = logisticsEntity;
        }

        public static class LogisticsEntity {
            private String msg;
            /**
             * list : [{"time":"2016-08-17 21:58:12","status":"【已签收，签收人是已送18682375538】"},{"time":"2016-08-17 17:44:48","status":"深圳市【深圳福田梅林分部】，【张县委/18682375538】正在派件"},{"time":"2016-08-17 08:35:45","status":"到深圳市【深圳福田梅林分部】"},{"time":"2016-08-17 05:29:37","status":"深圳市【深圳转运中心】，正发往【深圳福田梅林分部】"},{"time":"2016-08-17 04:41:36","status":"到深圳市【深圳转运中心】"},{"time":"2016-08-17 04:41:36","status":"到深圳市【深圳转运中心】"},{"time":"2016-08-17 02:21:52","status":"东莞市【虎门转运中心】，正发往【深圳转运中心】"},{"time":"2016-08-17 01:14:00","status":"到东莞市【虎门转运中心】"},{"time":"2016-08-15 20:46:47","status":"合肥市【合肥转运中心】，正发往【虎门转运中心】"},{"time":"2016-08-15 20:44:00","status":"到合肥市【合肥转运中心】"},{"time":"2016-08-15 14:02:45","status":"【蜀山七部】揽收成功"}]
             * deliverystatus : 3
             * type : htky
             * number : 70141107146139
             * issign : 1
             */

            @SerializedName("result")
            private LogisticsBean logisticsBean;
            private String status;

            public String getMsg() {
                return msg;
            }

            public void setMsg(String msg) {
                this.msg = msg;
            }

            public LogisticsBean getLogisticsBean() {
                return logisticsBean;
            }

            public void setLogisticsBean(LogisticsBean logisticsBean) {
                this.logisticsBean = logisticsBean;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public static class LogisticsBean {
                private String deliverystatus;
                private String type;
                private String number;
                private String issign;
                /**
                 * time : 2016-08-17 21:58:12
                 * status : 【已签收，签收人是已送18682375538】
                 */

                private List<ListBean> list;

                public String getDeliverystatus() {
                    return deliverystatus;
                }

                public void setDeliverystatus(String deliverystatus) {
                    this.deliverystatus = deliverystatus;
                }

                public String getType() {
                    return type;
                }

                public void setType(String type) {
                    this.type = type;
                }

                public String getNumber() {
                    return number;
                }

                public void setNumber(String number) {
                    this.number = number;
                }

                public String getIssign() {
                    return issign;
                }

                public void setIssign(String issign) {
                    this.issign = issign;
                }

                public List<ListBean> getList() {
                    return list;
                }

                public void setList(List<ListBean> list) {
                    this.list = list;
                }

                public static class ListBean {
                    private String time;
                    private String status;

                    public String getTime() {
                        return time;
                    }

                    public void setTime(String time) {
                        this.time = time;
                    }

                    public String getStatus() {
                        return status;
                    }

                    public void setStatus(String status) {
                        this.status = status;
                    }
                }
            }
        }
    }
}
