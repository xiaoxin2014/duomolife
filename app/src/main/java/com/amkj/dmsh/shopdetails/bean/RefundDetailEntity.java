package com.amkj.dmsh.shopdetails.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.amkj.dmsh.base.BaseEntity;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.amkj.dmsh.mine.bean.CartProductInfoBean;

import java.util.List;
import java.util.Map;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/8/21
 * class description:退款详情
 */

public class RefundDetailEntity extends BaseEntity{

    /**
     * refundDetailBean : {"orderRefundProductId":98,"no":"cX34650X2027X1502250184598","reason":"不想要了","noticeMsg":"商品退款将原路返回至支付账户，请注意查收","saleSkuValue":"颜色:灰色","count":1,"orderProductId":1691,"refundType":"退货退款","picUrl":"http://image.domolife.cn/platform/20170422/20170422103859051.jpg","refundNo":"rX34650X1502259958582","createTime":"2017-08-09 14:25:59","statusName":"退款成功 ","name":"Nuna Zaaz儿童餐椅","refundPrice":"0.01","orderRefundId":301}
     * msg : 请求成功
     * code : 01
     */
    @SerializedName("result")
    private RefundDetailBean refundDetailBean;
    private String currentTime;
    private Map<String,String> status;

    public RefundDetailBean getRefundDetailBean() {
        return refundDetailBean;
    }

    public void setRefundDetailBean(RefundDetailBean refundDetailBean) {
        this.refundDetailBean = refundDetailBean;
    }

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

    public static class RefundDetailBean implements Parcelable {
        /**
         * orderRefundProductId : 98
         * no : cX34650X2027X1502250184598
         * reason : 不想要了
         * noticeMsg : 商品退款将原路返回至支付账户，请注意查收
         * saleSkuValue : 颜色:灰色
         * count : 1
         * orderProductId : 1691
         * refundType : 退货退款
         * picUrl : http://image.domolife.cn/platform/20170422/20170422103859051.jpg
         * refundNo : rX34650X1502259958582
         * createTime : 2017-08-09 14:25:59
         * statusName : 退款成功
         * name : Nuna Zaaz儿童餐椅
         * refundPrice : 0.01
         * orderRefundId : 301
         */

        private int orderRefundProductId;
        private String no;
        private String reason;
        private String content;
        private String noticeMsg;
        private String saleSkuValue;
        private int count;
        private int orderProductId;
        private String picUrl;
        private String createTime;
        @SerializedName("updated")
        private String updateTime;
        private String statusName;
        private String name;
        private int orderRefundId;
        private String refundPrice;
        private int refundIntegralPrice;
        private String refundNo;
        private String refundType;
        private int refundReasonId;
        private int refundTypeId;
        private int childOrderStatus;
        private int status;
        private String price;
        private int integralPrice;
        private int productId;
        private long autoUndoRefundGoodsTime;
        private String currentTime;

        @SerializedName("combineProductInfo")
        private List<CartProductInfoBean> combineProductInfoList;
        @SerializedName("presentProductInfo")
        private List<CartProductInfoBean> presentProductInfoList;
        private RefundGoodsAddressBean refundGoodsAddress;
        /**
         * expressInfo : {"expressNo":"888999888909988999","expressCompany":"中国邮政"}
         */

        private ExpressInfoBean expressInfo;
        /**
         * repairReturnExpressCompany : 顺丰
         * repairReturnExpressNo : 457765726728
         * repairExpressFee : 0.0
         * repairExpressCompany : 中通快递
         * repairExpressNo : 11111
         * repairReceivePhone : 13751875324
         * repairReceiveAddress : 北京北京市东城区-123
         * repairReceiveReceiver : 哈哈
         */
        /**
         * 维修专属参数
         */
        private String repairReturnExpressCompany;
        private String repairReturnExpressNo;
        private String repairExpressFee;
        private String repairExpressCompany;
        private String repairExpressNo;
        private String repairReceivePhone;
        private String repairReceiveAddress;
        private String repairReceiveReceiver;
        /**
         * refundPayInfo : {"receiveRefundTime":"已于2018-04-10 15:34:36到账","refundAccount":"微信(招商银行储蓄卡2101)"}
         */

        private RefundPayInfoBean refundPayInfo;

        public static RefundDetailBean objectFromData(String str) {
            return new Gson().fromJson(str, RefundDetailBean.class);
        }

        public int getIntegralPrice() {
            return integralPrice;
        }

        public void setIntegralPrice(int integralPrice) {
            this.integralPrice = integralPrice;
        }

        public int getRefundIntegralPrice() {
            return refundIntegralPrice;
        }

        public void setRefundIntegralPrice(int refundIntegralPrice) {
            this.refundIntegralPrice = refundIntegralPrice;
        }

        public List<CartProductInfoBean> getPresentProductInfoList() {
            return presentProductInfoList;
        }

        public void setPresentProductInfoList(List<CartProductInfoBean> presentProductInfoList) {
            this.presentProductInfoList = presentProductInfoList;
        }

        public List<CartProductInfoBean> getCombineProductInfoList() {
            return combineProductInfoList;
        }

        public void setCombineProductInfoList(List<CartProductInfoBean> combineProductInfoList) {
            this.combineProductInfoList = combineProductInfoList;
        }

        public long getAutoUndoRefundGoodsTime() {
            return autoUndoRefundGoodsTime;
        }

        public void setAutoUndoRefundGoodsTime(long autoUndoRefundGoodsTime) {
            this.autoUndoRefundGoodsTime = autoUndoRefundGoodsTime;
        }

        public String getCurrentTime() {
            return currentTime;
        }

        public void setCurrentTime(String currentTime) {
            this.currentTime = currentTime;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public int getRefundReasonId() {
            return refundReasonId;
        }

        public void setRefundReasonId(int refundReasonId) {
            this.refundReasonId = refundReasonId;
        }

        public int getChildOrderStatus() {
            return childOrderStatus;
        }

        public void setChildOrderStatus(int childOrderStatus) {
            this.childOrderStatus = childOrderStatus;
        }

        public int getProductId() {
            return productId;
        }

        public void setProductId(int productId) {
            this.productId = productId;
        }

        public int getOrderRefundProductId() {
            return orderRefundProductId;
        }

        public void setOrderRefundProductId(int orderRefundProductId) {
            this.orderRefundProductId = orderRefundProductId;
        }

        public String getNo() {
            return no;
        }

        public void setNo(String no) {
            this.no = no;
        }

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getNoticeMsg() {
            return noticeMsg;
        }

        public void setNoticeMsg(String noticeMsg) {
            this.noticeMsg = noticeMsg;
        }

        public String getSaleSkuValue() {
            return saleSkuValue;
        }

        public void setSaleSkuValue(String saleSkuValue) {
            this.saleSkuValue = saleSkuValue;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public int getOrderProductId() {
            return orderProductId;
        }

        public void setOrderProductId(int orderProductId) {
            this.orderProductId = orderProductId;
        }

        public String getRefundType() {
            return refundType;
        }

        public void setRefundType(String refundType) {
            this.refundType = refundType;
        }

        public int getRefundTypeId() {
            return refundTypeId;
        }

        public void setRefundTypeId(int refundTypeId) {
            this.refundTypeId = refundTypeId;
        }

        public String getPicUrl() {
            return picUrl;
        }

        public void setPicUrl(String picUrl) {
            this.picUrl = picUrl;
        }

        public String getRefundNo() {
            return refundNo;
        }

        public void setRefundNo(String refundNo) {
            this.refundNo = refundNo;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getStatusName() {
            return statusName;
        }

        public void setStatusName(String statusName) {
            this.statusName = statusName;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getRefundPrice() {
            return refundPrice;
        }

        public void setRefundPrice(String refundPrice) {
            this.refundPrice = refundPrice;
        }

        public int getOrderRefundId() {
            return orderRefundId;
        }

        public void setOrderRefundId(int orderRefundId) {
            this.orderRefundId = orderRefundId;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getRepairReturnExpressCompany() {
            return repairReturnExpressCompany;
        }

        public void setRepairReturnExpressCompany(String repairReturnExpressCompany) {
            this.repairReturnExpressCompany = repairReturnExpressCompany;
        }

        public String getRepairReturnExpressNo() {
            return repairReturnExpressNo;
        }

        public void setRepairReturnExpressNo(String repairReturnExpressNo) {
            this.repairReturnExpressNo = repairReturnExpressNo;
        }

        public String getRepairExpressFee() {
            return repairExpressFee;
        }

        public void setRepairExpressFee(String repairExpressFee) {
            this.repairExpressFee = repairExpressFee;
        }

        public String getRepairExpressCompany() {
            return repairExpressCompany;
        }

        public void setRepairExpressCompany(String repairExpressCompany) {
            this.repairExpressCompany = repairExpressCompany;
        }

        public String getRepairExpressNo() {
            return repairExpressNo;
        }

        public void setRepairExpressNo(String repairExpressNo) {
            this.repairExpressNo = repairExpressNo;
        }

        public String getRepairReceivePhone() {
            return repairReceivePhone;
        }

        public void setRepairReceivePhone(String repairReceivePhone) {
            this.repairReceivePhone = repairReceivePhone;
        }

        public String getRepairReceiveAddress() {
            return repairReceiveAddress;
        }

        public void setRepairReceiveAddress(String repairReceiveAddress) {
            this.repairReceiveAddress = repairReceiveAddress;
        }

        public String getRepairReceiveReceiver() {
            return repairReceiveReceiver;
        }

        public void setRepairReceiveReceiver(String repairReceiveReceiver) {
            this.repairReceiveReceiver = repairReceiveReceiver;
        }

        public ExpressInfoBean getExpressInfo() {
            return expressInfo;
        }

        public void setExpressInfo(ExpressInfoBean expressInfo) {
            this.expressInfo = expressInfo;
        }

        public RefundGoodsAddressBean getRefundGoodsAddress() {
            return refundGoodsAddress;
        }

        public void setRefundGoodsAddress(RefundGoodsAddressBean refundGoodsAddress) {
            this.refundGoodsAddress = refundGoodsAddress;
        }

        public RefundPayInfoBean getRefundPayInfo() {
            return refundPayInfo;
        }

        public void setRefundPayInfo(RefundPayInfoBean refundPayInfo) {
            this.refundPayInfo = refundPayInfo;
        }

        public static class RefundGoodsAddressBean implements Parcelable {
            private String refundGoodsReceiver;
            private String refundGoodsPhone;
            private String refundGoodsAddress;
            private int id;

            public String getRefundGoodsReceiver() {
                return refundGoodsReceiver;
            }

            public void setRefundGoodsReceiver(String refundGoodsReceiver) {
                this.refundGoodsReceiver = refundGoodsReceiver;
            }

            public String getRefundGoodsPhone() {
                return refundGoodsPhone;
            }

            public void setRefundGoodsPhone(String refundGoodsPhone) {
                this.refundGoodsPhone = refundGoodsPhone;
            }

            public String getRefundGoodsAddress() {
                return refundGoodsAddress;
            }

            public void setRefundGoodsAddress(String refundGoodsAddress) {
                this.refundGoodsAddress = refundGoodsAddress;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.refundGoodsReceiver);
                dest.writeString(this.refundGoodsPhone);
                dest.writeString(this.refundGoodsAddress);
                dest.writeInt(this.id);
            }

            public RefundGoodsAddressBean() {
            }

            protected RefundGoodsAddressBean(Parcel in) {
                this.refundGoodsReceiver = in.readString();
                this.refundGoodsPhone = in.readString();
                this.refundGoodsAddress = in.readString();
                this.id = in.readInt();
            }

            public static final Creator<RefundGoodsAddressBean> CREATOR = new Creator<RefundGoodsAddressBean>() {
                @Override
                public RefundGoodsAddressBean createFromParcel(Parcel source) {
                    return new RefundGoodsAddressBean(source);
                }

                @Override
                public RefundGoodsAddressBean[] newArray(int size) {
                    return new RefundGoodsAddressBean[size];
                }
            };
        }

        public RefundDetailBean() {
        }

        public static class ExpressInfoBean implements Parcelable {
            /**
             * expressNo : 888999888909988999
             * expressCompany : 中国邮政
             */

            private String expressNo;
            private String expressCompany;

            public static ExpressInfoBean objectFromData(String str) {

                return new Gson().fromJson(str, ExpressInfoBean.class);
            }

            public String getExpressNo() {
                return expressNo;
            }

            public void setExpressNo(String expressNo) {
                this.expressNo = expressNo;
            }

            public String getExpressCompany() {
                return expressCompany;
            }

            public void setExpressCompany(String expressCompany) {
                this.expressCompany = expressCompany;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.expressNo);
                dest.writeString(this.expressCompany);
            }

            public ExpressInfoBean() {
            }

            protected ExpressInfoBean(Parcel in) {
                this.expressNo = in.readString();
                this.expressCompany = in.readString();
            }

            public static final Creator<ExpressInfoBean> CREATOR = new Creator<ExpressInfoBean>() {
                @Override
                public ExpressInfoBean createFromParcel(Parcel source) {
                    return new ExpressInfoBean(source);
                }

                @Override
                public ExpressInfoBean[] newArray(int size) {
                    return new ExpressInfoBean[size];
                }
            };
        }

        public static class RefundPayInfoBean implements Parcelable {
            /**
             * receiveRefundTime : 已于2018-04-10 15:34:36到账
             * refundAccount : 微信(招商银行储蓄卡2101)
             */

            private String receiveRefundTime;
            private String refundAccount;

            public String getReceiveRefundTime() {
                return receiveRefundTime;
            }

            public void setReceiveRefundTime(String receiveRefundTime) {
                this.receiveRefundTime = receiveRefundTime;
            }

            public String getRefundAccount() {
                return refundAccount;
            }

            public void setRefundAccount(String refundAccount) {
                this.refundAccount = refundAccount;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.receiveRefundTime);
                dest.writeString(this.refundAccount);
            }

            public RefundPayInfoBean() {
            }

            protected RefundPayInfoBean(Parcel in) {
                this.receiveRefundTime = in.readString();
                this.refundAccount = in.readString();
            }

            public static final Creator<RefundPayInfoBean> CREATOR = new Creator<RefundPayInfoBean>() {
                @Override
                public RefundPayInfoBean createFromParcel(Parcel source) {
                    return new RefundPayInfoBean(source);
                }

                @Override
                public RefundPayInfoBean[] newArray(int size) {
                    return new RefundPayInfoBean[size];
                }
            };
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.orderRefundProductId);
            dest.writeString(this.no);
            dest.writeString(this.reason);
            dest.writeString(this.content);
            dest.writeString(this.noticeMsg);
            dest.writeString(this.saleSkuValue);
            dest.writeInt(this.count);
            dest.writeInt(this.orderProductId);
            dest.writeString(this.picUrl);
            dest.writeString(this.createTime);
            dest.writeString(this.updateTime);
            dest.writeString(this.statusName);
            dest.writeString(this.name);
            dest.writeInt(this.orderRefundId);
            dest.writeString(this.refundPrice);
            dest.writeInt(this.refundIntegralPrice);
            dest.writeString(this.refundNo);
            dest.writeString(this.refundType);
            dest.writeInt(this.refundReasonId);
            dest.writeInt(this.refundTypeId);
            dest.writeInt(this.childOrderStatus);
            dest.writeInt(this.status);
            dest.writeString(this.price);
            dest.writeInt(this.integralPrice);
            dest.writeInt(this.productId);
            dest.writeLong(this.autoUndoRefundGoodsTime);
            dest.writeString(this.currentTime);
            dest.writeTypedList(this.combineProductInfoList);
            dest.writeTypedList(this.presentProductInfoList);
            dest.writeParcelable(this.refundGoodsAddress, flags);
            dest.writeParcelable(this.expressInfo, flags);
            dest.writeString(this.repairReturnExpressCompany);
            dest.writeString(this.repairReturnExpressNo);
            dest.writeString(this.repairExpressFee);
            dest.writeString(this.repairExpressCompany);
            dest.writeString(this.repairExpressNo);
            dest.writeString(this.repairReceivePhone);
            dest.writeString(this.repairReceiveAddress);
            dest.writeString(this.repairReceiveReceiver);
            dest.writeParcelable(this.refundPayInfo, flags);
        }

        protected RefundDetailBean(Parcel in) {
            this.orderRefundProductId = in.readInt();
            this.no = in.readString();
            this.reason = in.readString();
            this.content = in.readString();
            this.noticeMsg = in.readString();
            this.saleSkuValue = in.readString();
            this.count = in.readInt();
            this.orderProductId = in.readInt();
            this.picUrl = in.readString();
            this.createTime = in.readString();
            this.updateTime = in.readString();
            this.statusName = in.readString();
            this.name = in.readString();
            this.orderRefundId = in.readInt();
            this.refundPrice = in.readString();
            this.refundIntegralPrice = in.readInt();
            this.refundNo = in.readString();
            this.refundType = in.readString();
            this.refundReasonId = in.readInt();
            this.refundTypeId = in.readInt();
            this.childOrderStatus = in.readInt();
            this.status = in.readInt();
            this.price = in.readString();
            this.integralPrice = in.readInt();
            this.productId = in.readInt();
            this.autoUndoRefundGoodsTime = in.readLong();
            this.currentTime = in.readString();
            this.combineProductInfoList = in.createTypedArrayList(CartProductInfoBean.CREATOR);
            this.presentProductInfoList = in.createTypedArrayList(CartProductInfoBean.CREATOR);
            this.refundGoodsAddress = in.readParcelable(RefundGoodsAddressBean.class.getClassLoader());
            this.expressInfo = in.readParcelable(ExpressInfoBean.class.getClassLoader());
            this.repairReturnExpressCompany = in.readString();
            this.repairReturnExpressNo = in.readString();
            this.repairExpressFee = in.readString();
            this.repairExpressCompany = in.readString();
            this.repairExpressNo = in.readString();
            this.repairReceivePhone = in.readString();
            this.repairReceiveAddress = in.readString();
            this.repairReceiveReceiver = in.readString();
            this.refundPayInfo = in.readParcelable(RefundPayInfoBean.class.getClassLoader());
        }

        public static final Parcelable.Creator<RefundDetailBean> CREATOR = new Parcelable.Creator<RefundDetailBean>() {
            @Override
            public RefundDetailBean createFromParcel(Parcel source) {
                return new RefundDetailBean(source);
            }

            @Override
            public RefundDetailBean[] newArray(int size) {
                return new RefundDetailBean[size];
            }
        };
    }
}
