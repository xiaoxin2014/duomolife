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
         * vipShare : {"inviterCouponAmount":"10","inviterVipDays":"15","inviteeVipDays":"30","inviteeVipDecrease":"0.2"}
         * layfolkShare : {"inviterCouponAmount":"5","inviterVipDays":"15","inviteeVipDays":"15","inviteeVipDecrease":"0.05"}
         */

        private VipShareBean vipShare;
        private LayfolkShareBean layfolkShare;

        public VipShareBean getVipShare() {
            return vipShare;
        }

        public void setVipShare(VipShareBean vipShare) {
            this.vipShare = vipShare;
        }

        public LayfolkShareBean getLayfolkShare() {
            return layfolkShare;
        }

        public void setLayfolkShare(LayfolkShareBean layfolkShare) {
            this.layfolkShare = layfolkShare;
        }

        public static class VipShareBean {
            /**
             * inviterCouponAmount : 10
             * inviterVipDays : 15
             * inviteeVipDays : 30
             * inviteeVipDecrease : 0.2
             */

            private String inviterCouponAmount;
            private String inviterVipDays;
            private String inviteeVipDays;
            private String inviteeVipDecrease;

            public String getInviterCouponAmount() {
                return inviterCouponAmount;
            }

            public void setInviterCouponAmount(String inviterCouponAmount) {
                this.inviterCouponAmount = inviterCouponAmount;
            }

            public String getInviterVipDays() {
                return inviterVipDays;
            }

            public void setInviterVipDays(String inviterVipDays) {
                this.inviterVipDays = inviterVipDays;
            }

            public String getInviteeVipDays() {
                return inviteeVipDays;
            }

            public void setInviteeVipDays(String inviteeVipDays) {
                this.inviteeVipDays = inviteeVipDays;
            }

            public String getInviteeVipDecrease() {
                return inviteeVipDecrease;
            }

            public void setInviteeVipDecrease(String inviteeVipDecrease) {
                this.inviteeVipDecrease = inviteeVipDecrease;
            }
        }

        public static class LayfolkShareBean {
            /**
             * inviterCouponAmount : 5
             * inviterVipDays : 15
             * inviteeVipDays : 15
             * inviteeVipDecrease : 0.05
             */

            private String inviterCouponAmount;
            private String inviterVipDays;
            private String inviteeVipDays;
            private String inviteeVipDecrease;

            public String getInviterCouponAmount() {
                return inviterCouponAmount;
            }

            public void setInviterCouponAmount(String inviterCouponAmount) {
                this.inviterCouponAmount = inviterCouponAmount;
            }

            public String getInviterVipDays() {
                return inviterVipDays;
            }

            public void setInviterVipDays(String inviterVipDays) {
                this.inviterVipDays = inviterVipDays;
            }

            public String getInviteeVipDays() {
                return inviteeVipDays;
            }

            public void setInviteeVipDays(String inviteeVipDays) {
                this.inviteeVipDays = inviteeVipDays;
            }

            public String getInviteeVipDecrease() {
                return inviteeVipDecrease;
            }

            public void setInviteeVipDecrease(String inviteeVipDecrease) {
                this.inviteeVipDecrease = inviteeVipDecrease;
            }
        }
    }
}
