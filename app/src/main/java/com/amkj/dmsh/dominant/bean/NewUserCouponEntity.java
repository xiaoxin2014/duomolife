package com.amkj.dmsh.dominant.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/8/1
 * version 3.1.5
 * class description:新人优惠券领取
 */
public class NewUserCouponEntity {

    /**
     * code : 00
     * msg : 该券仅限新用户可以领取哦~
     * sysTime : 2018-08-01 16:51:26
     * couponCount : 0
     * result : []
     */

    private String code;
    private String msg;
    private String sysTime;
    private int couponCount;
    @SerializedName("result")
    private List<CouponGiftBean> couponGiftList;

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

    public int getCouponCount() {
        return couponCount;
    }

    public void setCouponCount(int couponCount) {
        this.couponCount = couponCount;
    }

    public List<CouponGiftBean> getCouponGiftList() {
        return couponGiftList;
    }

    public void setCouponGiftList(List<CouponGiftBean> couponGiftList) {
        this.couponGiftList = couponGiftList;
    }

    public static class CouponGiftBean {
        private int amount;
        private int id;
        private String bgColor;
        private String desc;
        private String endTime;
        private String merchantId ;
        private String mode;
        private String modeBgColor ;
        private String beOverdue;
        private String startTime;
        private int startFee;
        private String type;
        private int usable;
        private String usableCause ;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getBgColor() {
            return bgColor;
        }

        public void setBgColor(String bgColor) {
            this.bgColor = bgColor;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getMerchantId() {
            return merchantId;
        }

        public void setMerchantId(String merchantId) {
            this.merchantId = merchantId;
        }

        public String getMode() {
            return mode;
        }

        public void setMode(String mode) {
            this.mode = mode;
        }

        public String getModeBgColor() {
            return modeBgColor;
        }

        public void setModeBgColor(String modeBgColor) {
            this.modeBgColor = modeBgColor;
        }

        public String getBeOverdue() {
            return beOverdue;
        }

        public void setBeOverdue(String beOverdue) {
            this.beOverdue = beOverdue;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public int getStartFee() {
            return startFee;
        }

        public void setStartFee(int startFee) {
            this.startFee = startFee;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getUsable() {
            return usable;
        }

        public void setUsable(int usable) {
            this.usable = usable;
        }

        public String getUsableCause() {
            return usableCause;
        }

        public void setUsableCause(String usableCause) {
            this.usableCause = usableCause;
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }
    }
}
