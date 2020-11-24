package com.amkj.dmsh.mine.bean;

import com.amkj.dmsh.base.BaseTimeEntity;
import com.amkj.dmsh.shopdetails.bean.PriceInfoBean;

import java.util.List;

/**
 * Created by xiaoxin on 2020/8/24
 * Version:v4.7.0
 */
public class ZeroIndentEntity extends BaseTimeEntity {

    /**
     * sysTime : 2020-08-24 09:09:56
     * result : {"orderNo":"ZERO266978X7e7573X1598082515174","createTime":"2020-08-22 15:48:35","statusText":"待心得审核","second":"","remark":"","consignee":"石志青","mobile":"18576609297","address":"新疆维吾尔自治区乌鲁木齐市天山区-万兴大厦15G","zeroGoodsInfo":{"productId":"18225","productName":"软绵绵系列 一只大菠萝 水果抱枕","subtitle":"222","productImg":"http://image.domolife.cn/platform/20200822/20200822154310332.png","count":"1","activityId":"4","marketPrice":"79"},"priceInfoList":[{"name":"商品总额","color":"#000000","priceText":"￥79"},{"name":"试用商品","color":"#000000","priceText":"-￥79"},{"name":"保证金","color":"#000000","priceText":"￥10"},{"name":"实付","color":"#000000","priceText":"￥10"}]}
     */

    private ZeroIndentBean result;

    public ZeroIndentBean getResult() {
        return result;
    }

    public void setResult(ZeroIndentBean result) {
        this.result = result;
    }

    public static class ZeroIndentBean {
        /**
         * orderNo : ZERO266978X7e7573X1598082515174
         * createTime : 2020-08-22 15:48:35
         * statusText : 待心得审核
         * second :
         * remark :
         * consignee : 石志青
         * mobile : 18576609297
         * address : 新疆维吾尔自治区乌鲁木齐市天山区-万兴大厦15G
         * zeroGoodsInfo : {"productId":"18225","productName":"软绵绵系列 一只大菠萝 水果抱枕","subtitle":"222","productImg":"http://image.domolife.cn/platform/20200822/20200822154310332.png","count":"1","activityId":"4","marketPrice":"79"}
         * priceInfoList : [{"name":"商品总额","color":"#000000","priceText":"￥79"},{"name":"试用商品","color":"#000000","priceText":"-￥79"},{"name":"保证金","color":"#000000","priceText":"￥10"},{"name":"实付","color":"#000000","priceText":"￥10"}]
         */

        private String orderNo;
        private String refundNo;
        private String createTime;
        private String payTime;
        private String payType;
        private String statusText;
        private String second;
        private String remark;
        private String consignee;
        private String mobile;
        private String address;
        private String expressNo;
        private ZeroGoodsInfoBean zeroGoodsInfo;
        private List<PriceInfoBean> priceInfoList;

        public String getExpressNo() {
            return expressNo;
        }

        public void setExpressNo(String expressNo) {
            this.expressNo = expressNo;
        }

        public String getRefundNo() {
            return refundNo;
        }

        public void setRefundNo(String refundNo) {
            this.refundNo = refundNo;
        }

        public String getPayTime() {
            return payTime;
        }

        public void setPayTime(String payTime) {
            this.payTime = payTime;
        }

        public String getPayType() {
            return payType;
        }

        public void setPayType(String payType) {
            this.payType = payType;
        }

        public String getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(String orderNo) {
            this.orderNo = orderNo;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getStatusText() {
            return statusText;
        }

        public void setStatusText(String statusText) {
            this.statusText = statusText;
        }

        public String getSecond() {
            return second;
        }

        public void setSecond(String second) {
            this.second = second;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getConsignee() {
            return consignee;
        }

        public void setConsignee(String consignee) {
            this.consignee = consignee;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public ZeroGoodsInfoBean getZeroGoodsInfo() {
            return zeroGoodsInfo;
        }

        public void setZeroGoodsInfo(ZeroGoodsInfoBean zeroGoodsInfo) {
            this.zeroGoodsInfo = zeroGoodsInfo;
        }

        public List<PriceInfoBean> getPriceInfoList() {
            return priceInfoList;
        }

        public void setPriceInfoList(List<PriceInfoBean> priceInfoList) {
            this.priceInfoList = priceInfoList;
        }
    }
}
