package com.amkj.dmsh.shopdetails.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.amkj.dmsh.base.BaseEntity;
import com.amkj.dmsh.mine.bean.CartProductInfoBean;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

/**
 * Created by atd48 on 2016/10/28.
 */
public class InquiryOrderEntry extends BaseEntity {
    @SerializedName("result")
    private OrderInquiryDateEntry orderInquiryDateEntry;

    public OrderInquiryDateEntry getOrderInquiryDateEntry() {
        return orderInquiryDateEntry;
    }

    public void setOrderInquiryDateEntry(OrderInquiryDateEntry orderInquiryDateEntry) {
        this.orderInquiryDateEntry = orderInquiryDateEntry;
    }

    public static class OrderInquiryDateEntry {

        /**
         * 0 : 待支付
         * 1 : 支付处理中
         * 10 : 待发货
         * 11 : 发货处理中
         * 12 : 部分发货
         * 20 : 待收货
         * 21 : 部分收货
         * 30 : 待评价
         * 31 : 部分评价
         * 40 : 已评价
         * -31 : 已拒绝
         * -30 : 退款审核中
         * -20 : 订单关闭
         * -12 : 支付超时
         * -11 : 订单取消
         * -10 : 取消审核中
         * -40 : 已退款
         */

        private Map<String, String> status;
        private String code;
        private String msg;
        private List<OrderListBean> orderList;
        private String currentTime;


        public String getCurrentTime() {
            return currentTime;
        }

        public void setCurrentTime(String currentTime) {
            this.currentTime = currentTime;
        }

        public Map<String, String> getStatus() {
            return status;
        }

        public void setStatus(Map<String, String> status) {
            this.status = status;
        }

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

        public List<OrderListBean> getOrderList() {
            return orderList;
        }

        public void setOrderList(List<OrderListBean> orderList) {
            this.orderList = orderList;
        }

        public static class OrderListBean implements Parcelable {
            private String currentTime;
            private String payAmount;
            private String createTime;
            private String couponAmount;
            private int status;
            private String no;
            private String consignee;
            private String deliveryAmount;
            @SerializedName("waitdeliveryFlag")
            private boolean waitDeliveryFlag;
            private String payTime;
            private int id;
            private String amount;
            private String address;
            private int userId;
            private String payType;
            private String completeTime;
            private int integralAmount;
            private String mobile;
            private String remark;
            private String totalScore;
            private boolean isCoupon;
            @SerializedName("isBaskRder")
            private boolean isBaskReader;
            private List<GoodsBean> goods;
            private int second;
            private String maxRewardTip;
            private int needComment=-1;//是否显示待评价按钮

            public boolean isNeedComment() {
                return needComment == 1;
            }

            public int getNeedComment() {
                return needComment;
            }

            public void setNeedComment(int needComment) {
                this.needComment = needComment;
            }

            public String getMaxRewardTip() {
                return maxRewardTip;
            }

            public void setMaxRewardTip(String maxRewardTip) {
                this.maxRewardTip = maxRewardTip;
            }

            //是否显示赠品物流
            private int isShowPresentLogistics;

            public int getIsShowPresentLogistics() {
                return isShowPresentLogistics;
            }

            public void setIsShowPresentLogistics(int isShowPresentLogistics) {
                this.isShowPresentLogistics = isShowPresentLogistics;
            }

            public int getSecond() {
                return second;
            }

            public void setSecond(int second) {
                this.second = second;
            }

            public String getTotalScore() {
                return totalScore;
            }

            public void setTotalScore(String totalScore) {
                this.totalScore = totalScore;
            }

            public boolean isBaskReader() {
                return isBaskReader;
            }

            public void setBaskReader(boolean baskReader) {
                isBaskReader = baskReader;
            }

            public boolean isCoupon() {
                return isCoupon;
            }

            public void setCoupon(boolean coupon) {
                isCoupon = coupon;
            }

            public String getCurrentTime() {
                return currentTime;
            }

            public void setCurrentTime(String currentTime) {
                this.currentTime = currentTime;
            }


            public String getRemark() {
                return remark;
            }

            public void setRemark(String remark) {
                this.remark = remark;
            }

            public String getPayAmount() {
                return payAmount;
            }

            public void setPayAmount(String payAmount) {
                this.payAmount = payAmount;
            }

            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }

            public String getCouponAmount() {
                return couponAmount;
            }

            public void setCouponAmount(String couponAmount) {
                this.couponAmount = couponAmount;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public String getNo() {
                return no;
            }

            public void setNo(String no) {
                this.no = no;
            }

            public String getConsignee() {
                return consignee;
            }

            public void setConsignee(String consignee) {
                this.consignee = consignee;
            }

            public String getDeliveryAmount() {
                return deliveryAmount;
            }

            public void setDeliveryAmount(String deliveryAmount) {
                this.deliveryAmount = deliveryAmount;
            }

            public String getPayTime() {
                return payTime;
            }

            public void setPayTime(String payTime) {
                this.payTime = payTime;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getAmount() {
                return amount;
            }

            public void setAmount(String amount) {
                this.amount = amount;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public int getUserId() {
                return userId;
            }

            public void setUserId(int userId) {
                this.userId = userId;
            }

            public String getPayType() {
                return payType;
            }

            public void setPayType(String payType) {
                this.payType = payType;
            }

            public String getCompleteTime() {
                return completeTime;
            }

            public void setCompleteTime(String completeTime) {
                this.completeTime = completeTime;
            }

            public int getIntegralAmount() {
                return integralAmount;
            }

            public void setIntegralAmount(int integralAmount) {
                this.integralAmount = integralAmount;
            }

            public String getMobile() {
                return mobile;
            }

            public void setMobile(String mobile) {
                this.mobile = mobile;
            }

            public List<GoodsBean> getGoods() {
                return goods;
            }

            public void setGoods(List<GoodsBean> goods) {
                this.goods = goods;
            }

            public boolean isWaitDeliveryFlag() {
                return waitDeliveryFlag;
            }

            public void setWaitDeliveryFlag(boolean waitDeliveryFlag) {
                this.waitDeliveryFlag = waitDeliveryFlag;
            }

            public static class GoodsBean implements Parcelable {
                private String marketPrice;
                private String picUrl;
                private int count;
                private int status;
                private String expressCompany;
                private int id;
                private int integralPrice;
                private String price;
                private String name;
                private String expressNo;
                private int orderProductId;
                private int deliverType;
                private String saleSkuValue;
                private String logistics;
                private String deliverTime;
                private String completeTime;
                //            组合状态
                private String combineParentId;
                //            赠品状态
                private String presentParentId;
                @SerializedName("combineProductInfo")
                private List<CartProductInfoBean> combineProductInfoList;
                @SerializedName("presentProductInfo")
                private List<CartProductInfoBean> presentProductInfoList;

                //赠品信息
                private PresentProductOrder presentProductOrder;

                public PresentProductOrder getPresentProductOrder() {
                    return presentProductOrder;
                }

                public void setPresentProductOrder(PresentProductOrder presentProductOrder) {
                    this.presentProductOrder = presentProductOrder;
                }

                public String getCombineParentId() {
                    return combineParentId;
                }

                public void setCombineParentId(String combineParentId) {
                    this.combineParentId = combineParentId;
                }

                public String getPresentParentId() {
                    return presentParentId;
                }

                public void setPresentParentId(String presentParentId) {
                    this.presentParentId = presentParentId;
                }

                public List<CartProductInfoBean> getCombineProductInfoList() {
                    return combineProductInfoList;
                }

                public void setCombineProductInfoList(List<CartProductInfoBean> combineProductInfoList) {
                    this.combineProductInfoList = combineProductInfoList;
                }

                public List<CartProductInfoBean> getPresentProductInfoList() {
                    return presentProductInfoList;
                }

                public void setPresentProductInfoList(List<CartProductInfoBean> presentProductInfoList) {
                    this.presentProductInfoList = presentProductInfoList;
                }

                public String getMarketPrice() {
                    return marketPrice;
                }

                public void setMarketPrice(String marketPrice) {
                    this.marketPrice = marketPrice;
                }

                public String getPicUrl() {
                    return picUrl;
                }

                public void setPicUrl(String picUrl) {
                    this.picUrl = picUrl;
                }

                public int getCount() {
                    return count;
                }

                public void setCount(int count) {
                    this.count = count;
                }

                public int getStatus() {
                    return status;
                }

                public void setStatus(int status) {
                    this.status = status;
                }

                public String getExpressCompany() {
                    return expressCompany;
                }

                public void setExpressCompany(String expressCompany) {
                    this.expressCompany = expressCompany;
                }

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public int getIntegralPrice() {
                    return integralPrice;
                }

                public void setIntegralPrice(int integralPrice) {
                    this.integralPrice = integralPrice;
                }

                public String getPrice() {
                    return price;
                }

                public void setPrice(String price) {
                    this.price = price;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getExpressNo() {
                    return expressNo;
                }

                public void setExpressNo(String expressNo) {
                    this.expressNo = expressNo;
                }

                public int getOrderProductId() {
                    return orderProductId;
                }

                public void setOrderProductId(int orderProductId) {
                    this.orderProductId = orderProductId;
                }

                public int getDeliverType() {
                    return deliverType;
                }

                public void setDeliverType(int deliverType) {
                    this.deliverType = deliverType;
                }

                public String getSaleSkuValue() {
                    return saleSkuValue;
                }

                public void setSaleSkuValue(String saleSkuValue) {
                    this.saleSkuValue = saleSkuValue;
                }

                public String getLogistics() {
                    return logistics;
                }

                public void setLogistics(String logistics) {
                    this.logistics = logistics;
                }

                public String getDeliverTime() {
                    return deliverTime;
                }

                public void setDeliverTime(String deliverTime) {
                    this.deliverTime = deliverTime;
                }

                public String getCompleteTime() {
                    return completeTime;
                }

                public void setCompleteTime(String completeTime) {
                    this.completeTime = completeTime;
                }

                @Override
                public int describeContents() {
                    return 0;
                }

                @Override
                public void writeToParcel(Parcel dest, int flags) {
                    dest.writeString(this.marketPrice);
                    dest.writeString(this.picUrl);
                    dest.writeInt(this.count);
                    dest.writeInt(this.status);
                    dest.writeString(this.expressCompany);
                    dest.writeInt(this.id);
                    dest.writeInt(this.integralPrice);
                    dest.writeString(this.price);
                    dest.writeString(this.name);
                    dest.writeString(this.expressNo);
                    dest.writeInt(this.orderProductId);
                    dest.writeInt(this.deliverType);
                    dest.writeString(this.saleSkuValue);
                    dest.writeString(this.logistics);
                    dest.writeString(this.deliverTime);
                    dest.writeString(this.completeTime);
                    dest.writeString(this.combineParentId);
                    dest.writeString(this.presentParentId);
                    dest.writeTypedList(this.combineProductInfoList);
                    dest.writeTypedList(this.presentProductInfoList);
                }

                protected GoodsBean(Parcel in) {
                    this.marketPrice = in.readString();
                    this.picUrl = in.readString();
                    this.count = in.readInt();
                    this.status = in.readInt();
                    this.expressCompany = in.readString();
                    this.id = in.readInt();
                    this.integralPrice = in.readInt();
                    this.price = in.readString();
                    this.name = in.readString();
                    this.expressNo = in.readString();
                    this.orderProductId = in.readInt();
                    this.deliverType = in.readInt();
                    this.saleSkuValue = in.readString();
                    this.logistics = in.readString();
                    this.deliverTime = in.readString();
                    this.completeTime = in.readString();
                    this.combineParentId = in.readString();
                    this.presentParentId = in.readString();
                    this.combineProductInfoList = in.createTypedArrayList(CartProductInfoBean.CREATOR);
                    this.presentProductInfoList = in.createTypedArrayList(CartProductInfoBean.CREATOR);
                }

                public static final Creator<GoodsBean> CREATOR = new Creator<GoodsBean>() {
                    @Override
                    public GoodsBean createFromParcel(Parcel source) {
                        return new GoodsBean(source);
                    }

                    @Override
                    public GoodsBean[] newArray(int size) {
                        return new GoodsBean[size];
                    }
                };
            }

            public OrderListBean() {
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.currentTime);
                dest.writeString(this.payAmount);
                dest.writeString(this.createTime);
                dest.writeString(this.couponAmount);
                dest.writeInt(this.status);
                dest.writeString(this.no);
                dest.writeString(this.consignee);
                dest.writeString(this.deliveryAmount);
                dest.writeString(this.payTime);
                dest.writeInt(this.id);
                dest.writeString(this.amount);
                dest.writeString(this.address);
                dest.writeInt(this.userId);
                dest.writeString(this.payType);
                dest.writeString(this.completeTime);
                dest.writeInt(this.integralAmount);
                dest.writeString(this.mobile);
                dest.writeString(this.remark);
                dest.writeByte(this.isCoupon ? (byte) 1 : (byte) 0);
                dest.writeTypedList(this.goods);
                dest.writeInt(this.second);
            }

            protected OrderListBean(Parcel in) {
                this.currentTime = in.readString();
                this.payAmount = in.readString();
                this.createTime = in.readString();
                this.couponAmount = in.readString();
                this.status = in.readInt();
                this.no = in.readString();
                this.consignee = in.readString();
                this.deliveryAmount = in.readString();
                this.payTime = in.readString();
                this.id = in.readInt();
                this.amount = in.readString();
                this.address = in.readString();
                this.userId = in.readInt();
                this.payType = in.readString();
                this.completeTime = in.readString();
                this.integralAmount = in.readInt();
                this.mobile = in.readString();
                this.remark = in.readString();
                this.isCoupon = in.readByte() != 0;
                this.goods = in.createTypedArrayList(GoodsBean.CREATOR);
                this.second = in.readInt();
            }

            public static final Creator<OrderListBean> CREATOR = new Creator<OrderListBean>() {
                @Override
                public OrderListBean createFromParcel(Parcel source) {
                    return new OrderListBean(source);
                }

                @Override
                public OrderListBean[] newArray(int size) {
                    return new OrderListBean[size];
                }
            };
        }
    }
}
