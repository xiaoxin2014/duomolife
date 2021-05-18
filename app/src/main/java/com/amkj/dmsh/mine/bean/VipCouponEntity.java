package com.amkj.dmsh.mine.bean;

import com.amkj.dmsh.base.BaseEntity;
import com.amkj.dmsh.constant.ConstantMethod;

import java.util.List;

/**
 * Created by xiaoxin on 2020/7/28
 * Version:v4.7.0
 * ClassDescription :会员专享优惠券实体类
 */
public class VipCouponEntity extends BaseEntity {

    /**
     * sysTime : 2020-07-28 14:31:33
     * result : {"title":"会员专享券","subtitle":"","couponList":[{"couponId":"603","useRange":"","amount":"100元","couponTitle":"满300减100","couponDesc":"无门槛"},{"couponId":"602","useRange":"","amount":"10元","couponTitle":"满200减10","couponDesc":"测试"},{"couponId":"601","useRange":"","amount":"5元","couponTitle":"满100减5","couponDesc":"测试"}]}
     */

    private VipCouponBean result;

    public VipCouponBean getResult() {
        return result;
    }

    public void setResult(VipCouponBean result) {
        this.result = result;
    }

    public static class VipCouponBean {
        /**
         * title : 会员专享券
         * subtitle :
         * couponList : [{"couponId":"603","useRange":"","amount":"100元","couponTitle":"满300减100","couponDesc":"无门槛"},{"couponId":"602","useRange":"","amount":"10元","couponTitle":"满200减10","couponDesc":"测试"},{"couponId":"601","useRange":"","amount":"5元","couponTitle":"满100减5","couponDesc":"测试"}]
         */

        private String title;
        private String subtitle;
        private String coverImg;
        private String zoneId;
        private List<CouponListBean> couponList;

        public String getZoneId() {
            return zoneId;
        }

        public void setZoneId(String zoneId) {
            this.zoneId = zoneId;
        }

        public String getCoverImg() {
            return coverImg;
        }

        public void setCoverImg(String coverImg) {
            this.coverImg = coverImg;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getSubtitle() {
            return subtitle;
        }

        public void setSubtitle(String subtitle) {
            this.subtitle = subtitle;
        }

        public List<CouponListBean> getCouponList() {
            return couponList;
        }

        public void setCouponList(List<CouponListBean> couponList) {
            this.couponList = couponList;
        }

        public static class CouponListBean {
            /**
             * couponId : 603
             * useRange :
             * amount : 100元
             * couponTitle : 满300减100
             * couponDesc : 无门槛
             * timeText :
             * totalCount : 10
             * robCount : 6
             */

            private String couponId;
            private String useRange;
            private String amount;
            private String couponTitle;
            private String couponDesc;
            private String timeText;
            private String totalCount;
            private String robCount;
            private String startFee;
            private String isDraw;

            public boolean isDraw() {
                return "1".equals(isDraw);
            }

            public void setIsDraw(String isDraw) {
                this.isDraw = isDraw;
            }

            public int getStartFee() {
                return ConstantMethod.getStringChangeIntegers(startFee);
            }

            public void setStartFee(String startFee) {
                this.startFee = startFee;
            }

            //自定义属性,是否已抢光
            private boolean isSoldOut;

            public boolean isSoldOut() {
                return getRobCount() >= getTotalCount();
            }

            public void setSoldOut(boolean soldOut) {
                isSoldOut = soldOut;
            }

            public int getCouponId() {
                return ConstantMethod.getStringChangeIntegers(couponId);
            }

            public void setCouponId(String couponId) {
                this.couponId = couponId;
            }

            public String getUseRange() {
                return useRange;
            }

            public void setUseRange(String useRange) {
                this.useRange = useRange;
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

            public String getCouponDesc() {
                return couponDesc;
            }

            public void setCouponDesc(String couponDesc) {
                this.couponDesc = couponDesc;
            }

            public String getTimeText() {
                return timeText;
            }

            public void setTimeText(String timeText) {
                this.timeText = timeText;
            }

            public int getTotalCount() {
                return ConstantMethod.getStringChangeIntegers(totalCount);
            }

            public void setTotalCount(String totalCount) {
                this.totalCount = totalCount;
            }

            public int getRobCount() {
                return ConstantMethod.getStringChangeIntegers(robCount);
            }

            public void setRobCount(String robCount) {
                this.robCount = robCount;
            }
        }
    }
}
