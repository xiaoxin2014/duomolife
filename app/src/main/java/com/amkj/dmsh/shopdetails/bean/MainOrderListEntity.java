package com.amkj.dmsh.shopdetails.bean;

import com.amkj.dmsh.base.BaseTimeEntity;
import com.amkj.dmsh.bean.ActivityInfoListBean;
import com.amkj.dmsh.bean.OrderProductNewBean;
import com.amkj.dmsh.constant.ConstantMethod;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by xiaoxin on 2020/3/19
 * Version:v4.5.0
 * ClassDescription :新版订单列表实体类
 */
public class MainOrderListEntity extends BaseTimeEntity {

    /**
     * code : 01
     * msg : 请求成功
     * sysTime : 2020-03-19 10:59:30
     * showCount : 0
     * totalPage : 37
     * totalResult : 737
     * currentPage : 1
     * result : [{"orderNo":"cX266978X87290X1584425815203","statusText":"待收货","status":"20","createTime":"2020-03-17 14:16:54","payAmount":"0.03","second":"7200","msg":"","isPostage":"0","buttonList":[{"id":"1","clickable":"1","btnText":"确认收货"}],"logisticsStrList":[],"orderProductList":[{"orderProductId":"740454","productId":"4316","picUrl":"http://image.domolife.cn/platform/bJFJ7Am2tZ.jpg","count":"1","productName":"日本FaSoLa文胸洗衣袋","price":"0.01","totalPrice":"0.01","saleSkuValue":"颜色:方形丝网式洗衣袋","statusText":"待收货","status":"20","predictTimeText":"","remindText":"","presentProductOrder":null}]},{"orderNo":"cX266978X87291X1584345283635","statusText":"部分发货","status":"12","createTime":"2020-03-16 15:54:42","payAmount":"0.02","second":"7200","msg":"部分商品已发出，请注意查收","isPostage":"0","buttonList":[],"logisticsStrList":[],"orderProductList":[]},{"orderNo":"cX266978X87291X1584172176329","statusText":"待发货","status":"10","createTime":"2020-03-14 15:49:35","payAmount":"0.01","second":"7200","msg":"","isPostage":"0","buttonList":[{"id":"4","clickable":"1","btnText":"申请退款"}],"logisticsStrList":[],"orderProductList":[]},{"orderNo":"cX266978X87291X1584008099280","statusText":"待支付","status":"0","createTime":"2020-03-12 18:14:58","payAmount":"0.1","second":"7200","msg":"","isPostage":"0","buttonList":[],"logisticsStrList":[],"orderProductList":[]},{"orderNo":"cX266978X87291X1583319904826","statusText":"交易关闭","status":"-25","createTime":"2020-03-04 19:05:04","payAmount":"0.01","second":"7200","msg":"","isPostage":"0","buttonList":[],"logisticsStrList":[],"orderProductList":[]},{"orderNo":"cX266978X87291X1583319836507","statusText":"交易关闭","status":"-25","createTime":"2020-03-04 19:03:56","payAmount":"0.01","second":"7200","msg":"","isPostage":"0","buttonList":[],"logisticsStrList":[],"orderProductList":[]},{"orderNo":"cX266978X87291X1583319804170","statusText":"待发货","status":"10","createTime":"2020-03-04 19:03:23","payAmount":"0.01","second":"7200","msg":"","isPostage":"0","buttonList":[{"id":"4","clickable":"1","btnText":"申请退款"}],"logisticsStrList":[],"orderProductList":[]},{"orderNo":"cX266978X87291X1583319756878","statusText":"待发货","status":"10","createTime":"2020-03-04 19:02:36","payAmount":"0.01","second":"7200","msg":"","isPostage":"0","buttonList":[{"id":"4","clickable":"1","btnText":"申请退款"}],"logisticsStrList":[],"orderProductList":[]},{"orderNo":"cX266978X87291X1583319673648","statusText":"待发货","status":"10","createTime":"2020-03-04 19:01:13","payAmount":"0.01","second":"7200","msg":"","isPostage":"0","buttonList":[{"id":"4","clickable":"1","btnText":"申请退款"}],"logisticsStrList":[],"orderProductList":[]},{"orderNo":"cX266978X87291X1583319497761","statusText":"待发货","status":"10","createTime":"2020-03-04 18:58:16","payAmount":"0.01","second":"7200","msg":"","isPostage":"0","buttonList":[{"id":"4","clickable":"1","btnText":"申请退款"}],"logisticsStrList":[],"orderProductList":[]},{"orderNo":"cX266978X87291X1583319217910","statusText":"待发货","status":"10","createTime":"2020-03-04 18:53:37","payAmount":"0.01","second":"7200","msg":"","isPostage":"0","buttonList":[{"id":"4","clickable":"1","btnText":"申请退款"}],"logisticsStrList":[],"orderProductList":[]},{"orderNo":"cX266978X87291X1583319153666","statusText":"待发货","status":"10","createTime":"2020-03-04 18:52:32","payAmount":"0.01","second":"7200","msg":"","isPostage":"0","buttonList":[{"id":"4","clickable":"1","btnText":"申请退款"}],"logisticsStrList":[],"orderProductList":[]},{"orderNo":"cX266978X87291X1583319105143","statusText":"待发货","status":"10","createTime":"2020-03-04 18:51:44","payAmount":"0.01","second":"7200","msg":"","isPostage":"0","buttonList":[{"id":"4","clickable":"1","btnText":"申请退款"}],"logisticsStrList":[],"orderProductList":[]},{"orderNo":"cX266978X87291X1583319056354","statusText":"待支付","status":"0","createTime":"2020-03-04 18:50:56","payAmount":"0.01","second":"7200","msg":"","isPostage":"0","buttonList":[],"logisticsStrList":[],"orderProductList":[]},{"orderNo":"cX266978X87291X1583318676688","statusText":"待支付","status":"0","createTime":"2020-03-04 18:44:36","payAmount":"0.01","second":"7200","msg":"","isPostage":"0","buttonList":[],"logisticsStrList":[],"orderProductList":[]},{"orderNo":"cX266978X87291X1583318451203","statusText":"待支付","status":"0","createTime":"2020-03-04 18:40:50","payAmount":"0.01","second":"7200","msg":"","isPostage":"0","buttonList":[],"logisticsStrList":[],"orderProductList":[]},{"orderNo":"cX266978X87291X1583318313514","statusText":"待支付","status":"0","createTime":"2020-03-04 18:38:32","payAmount":"0.01","second":"7200","msg":"","isPostage":"0","buttonList":[],"logisticsStrList":[],"orderProductList":[]},{"orderNo":"cX266978X87291X1583318269537","statusText":"待支付","status":"0","createTime":"2020-03-04 18:37:49","payAmount":"0.01","second":"7200","msg":"","isPostage":"0","buttonList":[],"logisticsStrList":[],"orderProductList":[]},{"orderNo":"cX266978X87291X1583318128258","statusText":"待支付","status":"0","createTime":"2020-03-04 18:35:27","payAmount":"0.01","second":"7200","msg":"","isPostage":"0","buttonList":[],"logisticsStrList":[],"orderProductList":[]},{"orderNo":"cX266978X87291X1583318074501","statusText":"待支付","status":"0","createTime":"2020-03-04 18:34:34","payAmount":"0.1","second":"7200","msg":"","isPostage":"0","buttonList":[],"logisticsStrList":[],"orderProductList":[]}]
     */

    private List<MainOrderBean> result;

    public List<MainOrderBean> getResult() {
        return result;
    }

    public void setResult(List<MainOrderBean> result) {
        this.result = result;
    }

    public static class MainOrderBean {
        //订单列表
        private String orderNo;
        private String statusText;
        private String status;
        private String createTime;
        private String payAmount;
        private String second;
        private String msg;
        private String isPostage;//是否包邮 1不包邮 0包邮
        private List<ButtonListBean> buttonList;
        private List<String> logisticsStrList;
        @SerializedName(value = "orderProductList", alternate = "refundGoodsList")
        private List<OrderProductNewBean> orderProductList;
        private String currentTime;
        private String refundCount; //订单含有售后数量
        private String refundSuccessCount; //订单含有退款成功数
        private String refundNo; //退款编号（该订单仅有一件商品退款成功时）
        private String maxRewardTip;
        private String type;//拼团类型 3为抽奖团，否则为普通团
        private boolean isSort;//是否已经排序


        //订单详情
        private String payTime;
        private String payType;
        private String userRemark;
        private String consignee;
        private String mobile;
        private String address;
        private String packageCount;
        private String isEditAddress;
        private Map<String, String> logisticsInfo;
        private List<PriceInfoBean> priceInfoList;
        private List<ActivityInfoListBean> activityInfoList;

        //退款详情
        private String endTime;
        private String isShowCancel;
        private String isShowUpdate;
        private String refunReason;
        private RefundGoodsAddressInfoBean refundGoodsAddressInfo;
        private String refundPrice;
        private String refundStatusText;
        private String refundTime;
        private String refundType;
        private String orderRefundProductId;
        private String refuseReason;
        private String isAfter;//1跳转选择退款类型,否则是待发货状态直接跳转申请退款界面

        //申请退款
        private Map<String, Map<String, String>> refundReasonMap;
        private Map<String, String> refundTypeMap;


        public boolean isAfter() {
            return "1".equals(isAfter);
        }

        public Map<String, Map<String, String>> getRefundReasonMap() {
            return refundReasonMap;
        }

        public void setRefundReasonMap(Map<String, Map<String, String>> refundReasonMap) {
            this.refundReasonMap = refundReasonMap;
        }

        public Map<String, String> getRefundTypeMap() {
            return refundTypeMap;

        }

        public void setRefundTypeMap(Map<String, String> refundTypeMap) {
            this.refundTypeMap = refundTypeMap;
        }

        public String getRefuseReason() {
            return refuseReason;
        }

        public void setRefuseReason(String refuseReason) {
            this.refuseReason = refuseReason;
        }

        public String getOrderRefundProductId() {
            return orderRefundProductId;
        }

        public void setOrderRefundProductId(String orderRefundProductId) {
            this.orderRefundProductId = orderRefundProductId;
        }

        public String getIsPostage() {
            return isPostage;
        }

        public String getIsEditAddress() {
            return isEditAddress;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public boolean isShowCancel() {
            return "1".equals(isShowCancel);
        }

        public void setIsShowCancel(String isShowCancel) {
            this.isShowCancel = isShowCancel;
        }

        public boolean isShowUpdate() {
            return "1".equals(isShowUpdate);
        }

        public void setIsShowUpdate(String isShowUpdate) {
            this.isShowUpdate = isShowUpdate;
        }

        public String getRefunReason() {
            return refunReason;
        }

        public void setRefunReason(String refunReason) {
            this.refunReason = refunReason;
        }

        public RefundGoodsAddressInfoBean getRefundGoodsAddressInfo() {
            return refundGoodsAddressInfo;
        }

        public void setRefundGoodsAddressInfo(RefundGoodsAddressInfoBean refundGoodsAddressInfo) {
            this.refundGoodsAddressInfo = refundGoodsAddressInfo;
        }

        public String getRefundPrice() {
            return refundPrice;
        }

        public void setRefundPrice(String refundPrice) {
            this.refundPrice = refundPrice;
        }

        public String getRefundStatusText() {
            return refundStatusText;
        }

        public void setRefundStatusText(String refundStatusText) {
            this.refundStatusText = refundStatusText;
        }

        public String getRefundTime() {
            return refundTime;
        }

        public void setRefundTime(String refundTime) {
            this.refundTime = refundTime;
        }

        public String getRefundType() {
            return refundType;
        }

        public void setRefundType(String refundType) {
            this.refundType = refundType;
        }

        public String getRefundNo() {
            return refundNo;
        }

        public void setRefundNo(String refundNo) {
            this.refundNo = refundNo;
        }

        public int getRefundCount() {
            return ConstantMethod.getStringChangeIntegers(refundCount);
        }

        public void setRefundCount(String refundCount) {
            this.refundCount = refundCount;
        }

        public int getRefundSuccessCount() {
            return ConstantMethod.getStringChangeIntegers(refundSuccessCount);
        }

        public void setRefundSuccessCount(String refundSuccessCount) {
            this.refundSuccessCount = refundSuccessCount;
        }

        public boolean isSort() {
            return isSort;
        }

        public void setSort(boolean sort) {
            isSort = sort;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getMaxRewardTip() {
            return maxRewardTip;
        }

        public void setMaxRewardTip(String maxRewardTip) {
            this.maxRewardTip = maxRewardTip;
        }


        public String getCurrentTime() {
            return currentTime;
        }

        public void setCurrentTime(String currentTime) {
            this.currentTime = currentTime;
        }


        public long getSecond() {
            return ConstantMethod.getStringChangeLong(second);
        }

        public void setSecond(long second) {
            this.second = second + "";
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public boolean isPostage() {
            return "0".equals(isPostage);
        }

        public void setIsPostage(String isPostage) {
            this.isPostage = isPostage;
        }

        public List<ButtonListBean> getButtonList() {
            return buttonList == null ? new ArrayList<>() : buttonList;
        }

        public void setButtonList(List<ButtonListBean> buttonList) {
            this.buttonList = buttonList;
        }

        public List<String> getLogisticsStrList() {
            return logisticsStrList;
        }

        public void setLogisticsStrList(List<String> logisticsStrList) {
            this.logisticsStrList = logisticsStrList;
        }

        public List<OrderProductNewBean> getOrderProductList() {
            return orderProductList == null ? new ArrayList<>() : orderProductList;
        }

        public void setOrderProductList(List<OrderProductNewBean> orderProductList) {
            this.orderProductList = orderProductList;
        }

        public String getPayAmount() {
            return payAmount;
        }

        public void setPayAmount(String payAmount) {
            this.payAmount = payAmount;
        }

        public String getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(String orderNo) {
            this.orderNo = orderNo;
        }

        public String getStatusText() {
            return statusText;
        }

        public void setStatusText(String statusText) {
            this.statusText = statusText;
        }

        public int getStatus() {
            return ConstantMethod.getStringChangeIntegers(status);
        }

        public void setStatus(String status) {
            this.status = status;
        }

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

        public String getPayType() {
            return payType;
        }

        public void setPayType(String payType) {
            this.payType = payType;
        }

        public String getUserRemark() {
            return userRemark;
        }

        public void setUserRemark(String userRemark) {
            this.userRemark = userRemark;
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

        public int getPackageCount() {
            return ConstantMethod.getStringChangeIntegers(packageCount);
        }

        public void setPackageCount(String packageCount) {
            this.packageCount = packageCount;
        }


        public boolean isEditAddress() {
            return "1".equals(isEditAddress);
        }

        public void setIsEditAddress(String isEditAddress) {
            this.isEditAddress = isEditAddress;
        }

        public Map<String, String> getLogisticsInfo() {
            return logisticsInfo;
        }

        public void setLogisticsInfo(Map<String, String> logisticsInfo) {
            this.logisticsInfo = logisticsInfo;
        }

        public List<PriceInfoBean> getPriceInfoList() {
            return priceInfoList;
        }

        public void setPriceInfoList(List<PriceInfoBean> priceInfoList) {
            this.priceInfoList = priceInfoList;
        }


        public List<ActivityInfoListBean> getActivityInfoList() {
            return activityInfoList;
        }

        public void setActivityInfoList(List<ActivityInfoListBean> activityInfoList) {
            this.activityInfoList = activityInfoList;
        }
    }
}
