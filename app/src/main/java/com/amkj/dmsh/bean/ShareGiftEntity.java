package com.amkj.dmsh.bean;

import com.amkj.dmsh.base.BaseEntity;

/**
 * Created by xiaoxin on 2020/8/31
 * Version:v4.7.0
 */
public class ShareGiftEntity extends BaseEntity {

    /**
     * sysTime : 2020-08-31 10:17:11
     * result : {"vipShare":{"inviterCouponAmount":"10","inviterVipDays":"15","inviteeVipDays":"30","inviteeVipDecrease":"0.2"},"layfolkShare":{"inviterCouponAmount":"5","inviterVipDays":"15","inviteeVipDays":"15","inviteeVipDecrease":"0.05"}}
     */

    private ShareGiftBean result;

    public ShareGiftBean getResult() {
        return result;
    }

    public void setResult(ShareGiftBean result) {
        this.result = result;
    }

    public static class ShareGiftBean {

        /**
         * inviteCount : 78
         * waitGetPrice : 0
         * perCapitaPrice : 0
         */

        private String inviteCount;
        private String waitGetPrice;
        private String perCapitaPrice;

        public String getInviteCount() {
            return inviteCount;
        }

        public void setInviteCount(String inviteCount) {
            this.inviteCount = inviteCount;
        }

        public String getWaitGetPrice() {
            return waitGetPrice;
        }

        public void setWaitGetPrice(String waitGetPrice) {
            this.waitGetPrice = waitGetPrice;
        }

        public String getPerCapitaPrice() {
            return perCapitaPrice;
        }

        public void setPerCapitaPrice(String perCapitaPrice) {
            this.perCapitaPrice = perCapitaPrice;
        }
    }
}
