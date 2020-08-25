package com.amkj.dmsh.mine.bean;

import com.amkj.dmsh.base.BaseEntity;

import java.util.List;

/**
 * Created by xiaoxin on 2020/7/29
 * Version:v4.7.0
 * ClassDescription :会员省钱计算器数据实体类
 */
public class CalculatorEntity extends BaseEntity {

    /**
     * sysTime : 2020-07-29 13:36:43
     * result : {"totalPrice":"3023","startTime":"","priceData":[{"price":"￥1322","text":"多么会员折扣"},{"price":"￥368","text":"会员专属活动"},{"price":"￥1200","text":"专属优惠券"},{"price":"￥126","text":"0元试用"},{"price":"￥79","text":"双倍积分"}]}
     */

    private CalculatorBean result;

    public CalculatorBean getResult() {
        return result;
    }

    public void setResult(CalculatorBean result) {
        this.result = result;
    }

    public static class CalculatorBean {
        /**
         * totalPrice : 3023
         * startTime :
         * priceData : [{"price":"￥1322","text":"多么会员折扣"},{"price":"￥368","text":"会员专属活动"},{"price":"￥1200","text":"专属优惠券"},{"price":"￥126","text":"0元试用"},{"price":"￥79","text":"双倍积分"}]
         */

        private String totalPrice;
        private String startTime;
        private List<PriceDataBean> priceData;

        public String getTotalPrice() {
            return totalPrice;
        }

        public void setTotalPrice(String totalPrice) {
            this.totalPrice = totalPrice;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public List<PriceDataBean> getPriceData() {
            return priceData;
        }

        public void setPriceData(List<PriceDataBean> priceData) {
            this.priceData = priceData;
        }

        public static class PriceDataBean {
            /**
             * price : ￥1322
             * text : 多么会员折扣
             */

            private String price;
            private String text;

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public String getText() {
                return text;
            }

            public void setText(String text) {
                this.text = text;
            }
        }
    }
}
