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
        private String inviterCouponText;
        private String inviterVipDaysText;
        private String inviteeVipText;

        public String getInviterCouponText() {
            return inviterCouponText;
        }

        public void setInviterCouponText(String inviterCouponText) {
            this.inviterCouponText = inviterCouponText;
        }

        public String getInviterVipDaysText() {
            return inviterVipDaysText;
        }

        public void setInviterVipDaysText(String inviterVipDaysText) {
            this.inviterVipDaysText = inviterVipDaysText;
        }

        public String getInviteeVipText() {
            return inviteeVipText;
        }

        public void setInviteeVipText(String inviteeVipText) {
            this.inviteeVipText = inviteeVipText;
        }
    }
}
