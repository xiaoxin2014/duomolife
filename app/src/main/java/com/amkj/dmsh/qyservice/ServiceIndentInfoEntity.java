package com.amkj.dmsh.qyservice;

import com.amkj.dmsh.base.BaseTimeEntity;
import com.google.gson.annotations.SerializedName;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/9/12
 * version 3.1.6
 * class description:订单信息
 */
public class ServiceIndentInfoEntity extends BaseTimeEntity {
    @SerializedName("result")
    private ServiceIndentInfoBean serviceIndentInfoBean;

    public ServiceIndentInfoBean getServiceIndentInfoBean() {
        return serviceIndentInfoBean;
    }

    public void setServiceIndentInfoBean(ServiceIndentInfoBean serviceIndentInfoBean) {
        this.serviceIndentInfoBean = serviceIndentInfoBean;
    }

    public static class ServiceIndentInfoBean {
        @SerializedName("order")
        private ServiceOrderInfoBean serviceOrderInfoBean;

        public ServiceOrderInfoBean getServiceOrderInfoBean() {
            return serviceOrderInfoBean;
        }

        public void setServiceOrderInfoBean(ServiceOrderInfoBean serviceOrderInfoBean) {
            this.serviceOrderInfoBean = serviceOrderInfoBean;
        }

        public static class ServiceOrderInfoBean {
            private String createTime;
            private String payTime;
            private int id;
            private String no;
            private String payAmount;
            private int userId;

            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }

            public String getPayTime() {
                return payTime;
            }

            public void setPayTime(String payTime) {
                this.payTime = payTime;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getNo() {
                return no;
            }

            public void setNo(String no) {
                this.no = no;
            }

            public String getPayAmount() {
                return payAmount;
            }

            public void setPayAmount(String payAmount) {
                this.payAmount = payAmount;
            }

            public int getUserId() {
                return userId;
            }

            public void setUserId(int userId) {
                this.userId = userId;
            }
        }
    }
}
