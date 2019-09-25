package com.amkj.dmsh.bean;

import java.util.List;

/**
 * Created by xiaoxin on 2019/9/23
 * Version:v4.2.2
 * ClassDescription :优惠券领取结果
 */
public class CouponListEntity {

    /**
     * result : {"code":"01","msg":"领取成功","sysTime":"2019-09-23 14:23:23","result":[{"id":"2820253","couponId":"582","amount":"9000","couponTitle":"中秋专题券"}]}
     * msg : 请求成功
     * code : 01
     */

    private CouponListBean result;
    private String msg;
    private String code;

    public CouponListBean getResult() {
        return result;
    }

    public void setResult(CouponListBean result) {
        this.result = result;
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

    public static class CouponListBean {
        /**
         * code : 01
         * msg : 领取成功
         * sysTime : 2019-09-23 14:23:23
         * result : [{"id":"2820253","couponId":"582","amount":"9000","couponTitle":"中秋专题券"}]
         */

        private String code;
        private String msg;
        private String sysTime;
        private List<CouponBean> result;

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

        public String getSysTime() {
            return sysTime;
        }

        public void setSysTime(String sysTime) {
            this.sysTime = sysTime;
        }

        public List<CouponBean> getResult() {
            return result;
        }

        public void setResult(List<CouponBean> result) {
            this.result = result;
        }

        public static class CouponBean {
            /**
             * id : 2820253
             * couponId : 582
             * amount : 9000
             * couponTitle : 中秋专题券
             */

            private String id;
            private String couponId;
            private String amount;
            private String couponTitle;
            private String couponDesc;

            public String getCouponDesc() {
                return couponDesc;
            }

            public void setCouponDesc(String couponDesc) {
                this.couponDesc = couponDesc;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getCouponId() {
                return couponId;
            }

            public void setCouponId(String couponId) {
                this.couponId = couponId;
            }

            public String getAmount() {
                return amount;
            }

            public void setAmount(String amount) {
                this.amount = amount;
            }

            public String getCouponTitle() {
                return couponTitle;
            }

            public void setCouponTitle(String couponTitle) {
                this.couponTitle = couponTitle;
            }
        }
    }
}
