package com.amkj.dmsh.shopdetails.payutils;

import com.amkj.dmsh.base.BaseEntity;
import com.google.gson.annotations.SerializedName;

import java.util.Map;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2019/1/16
 * version 3.2.0
 * class description:精简查询订单状态
 */
public class UnionCheckIndentStatusBean extends BaseEntity {
    @SerializedName("result")
    private UnionIndentInfoBean unionIndentInfoBean;

    public UnionIndentInfoBean getUnionIndentInfoBean() {
        return unionIndentInfoBean;
    }

    public void setUnionIndentInfoBean(UnionIndentInfoBean unionIndentInfoBean) {
        this.unionIndentInfoBean = unionIndentInfoBean;
    }

    public static class UnionIndentInfoBean {

        @SerializedName("order")
        private UnionIndentStatusBean unionIndentStatusBean;
        private Map<String, String> status;

        public UnionIndentStatusBean getUnionIndentStatusBean() {
            return unionIndentStatusBean;
        }

        public void setUnionIndentStatusBean(UnionIndentStatusBean unionIndentStatusBean) {
            this.unionIndentStatusBean = unionIndentStatusBean;
        }

        public Map<String, String> getStatus() {
            return status;
        }

        public void setStatus(Map<String, String> status) {
            this.status = status;
        }

        public static class UnionIndentStatusBean {
            private int status;

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }
        }
    }
}
