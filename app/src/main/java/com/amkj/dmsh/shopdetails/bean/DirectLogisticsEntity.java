package com.amkj.dmsh.shopdetails.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by atd48 on 2016/10/31.
 */
public class DirectLogisticsEntity {

    /**
     * payAmount : 9.00
     * createTime : 2016-10-31 10:49:33
     * couponAmount : 0
     * status : 30
     * no : cX23295X1099X1477882173117
     * consignee : 刘桂鹏
     * deliveryAmount : 0
     * payTime : 2016-10-31 10:49:45
     * id : 23
     * amount : 9.00
     * address : 内蒙古自治区鄂尔多斯市鄂托克前旗-内蒙古街道内蒙古小镇，内蒙古
     * userId : 23295
     * payType : 支付宝
     * logistics : [[{"marketPrice":"2.00","picUrl":"http://img.domolife.cn/platform/5Emk4KG5R5.jpg","count":1,"status":-40,"expressCompany":"中通速递","refundTime":"2016-10-31 11:15:40","id":4159,"integralPrice":0,"price":"4.00","name":"老板椅","expressNo":"70141107146139","orderProductId":16,"deliverType":1,"saleSkuValue":"默认:卧室","deliverTime":"2016-10-31 10:54:32","completeTime":"2016-10-31 11:04:43"}],[{"marketPrice":"2.00","picUrl":"http://img.domolife.cn/platform/5Emk4KG5R5.jpg","count":1,"status":-40,"expressCompany":"圆通速递","refundTime":"2016-10-31 11:15:14","id":4159,"integralPrice":0,"price":"2.00","name":"老板椅","expressNo":"882500969432679338","orderProductId":14,"deliverType":1,"saleSkuValue":"默认:厨房","deliverTime":"2016-10-31 10:54:07","completeTime":"2016-10-31 11:04:43"},{"marketPrice":"2.00","picUrl":"http://img.domolife.cn/platform/5Emk4KG5R5.jpg","count":1,"status":-40,"expressCompany":"圆通速递","refundTime":"2016-10-31 11:15:31","id":4159,"integralPrice":0,"price":"3.00","name":"老板椅","expressNo":"882500969432679338","orderProductId":15,"deliverType":1,"saleSkuValue":"默认:客厅","deliverTime":"2016-10-31 10:54:07","completeTime":"2016-10-31 11:04:43"}]]
     * completeTime : 2016-10-31 11:04:43
     * integralAmount : 0
     * mobile : 13751077044
     */

    @SerializedName("result")
    private DirectLogisticsBean directLogisticsBean;
    /**
     * result : {"payAmount":"9.00","createTime":"2016-10-31 10:49:33","couponAmount":"0","status":30,"no":"cX23295X1099X1477882173117","consignee":"刘桂鹏","deliveryAmount":"0","payTime":"2016-10-31 10:49:45","id":23,"amount":"9.00","address":"内蒙古自治区鄂尔多斯市鄂托克前旗-内蒙古街道内蒙古小镇，内蒙古","userId":23295,"payType":"支付宝","logistics":[[{"marketPrice":"2.00","picUrl":"http://img.domolife.cn/platform/5Emk4KG5R5.jpg","count":1,"status":-40,"expressCompany":"中通速递","refundTime":"2016-10-31 11:15:40","id":4159,"integralPrice":0,"price":"4.00","name":"老板椅","expressNo":"70141107146139","orderProductId":16,"deliverType":1,"saleSkuValue":"默认:卧室","deliverTime":"2016-10-31 10:54:32","completeTime":"2016-10-31 11:04:43"}],[{"marketPrice":"2.00","picUrl":"http://img.domolife.cn/platform/5Emk4KG5R5.jpg","count":1,"status":-40,"expressCompany":"圆通速递","refundTime":"2016-10-31 11:15:14","id":4159,"integralPrice":0,"price":"2.00","name":"老板椅","expressNo":"882500969432679338","orderProductId":14,"deliverType":1,"saleSkuValue":"默认:厨房","deliverTime":"2016-10-31 10:54:07","completeTime":"2016-10-31 11:04:43"},{"marketPrice":"2.00","picUrl":"http://img.domolife.cn/platform/5Emk4KG5R5.jpg","count":1,"status":-40,"expressCompany":"圆通速递","refundTime":"2016-10-31 11:15:31","id":4159,"integralPrice":0,"price":"3.00","name":"老板椅","expressNo":"882500969432679338","orderProductId":15,"deliverType":1,"saleSkuValue":"默认:客厅","deliverTime":"2016-10-31 10:54:07","completeTime":"2016-10-31 11:04:43"}]],"completeTime":"2016-10-31 11:04:43","integralAmount":0,"mobile":"13751077044"}
     * code : 01
     * msg : 请求成功
     */

    private String code;
    private String msg;

    public DirectLogisticsBean getDirectLogisticsBean() {
        return directLogisticsBean;
    }

    public void setDirectLogisticsBean(DirectLogisticsBean directLogisticsBean) {
        this.directLogisticsBean = directLogisticsBean;
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

    public static class DirectLogisticsBean implements Parcelable {
        private String payAmount;
        private String createTime;
        private String couponAmount;
        private int status;
        private String no;
        private String consignee;
        private String deliveryAmount;
        private String payTime;
        private int id;
        private String amount;
        private String address;
        private int userId;
        private String payType;
        private String completeTime;
        private int integralAmount;
        private String mobile;
        /**
         * marketPrice : 2.00
         * picUrl : http://img.domolife.cn/platform/5Emk4KG5R5.jpg
         * count : 1
         * status : -40
         * expressCompany : 中通速递
         * refundTime : 2016-10-31 11:15:40
         * id : 4159
         * integralPrice : 0
         * price : 4.00
         * name : 老板椅
         * expressNo : 70141107146139
         * orderProductId : 16
         * deliverType : 1
         * saleSkuValue : 默认:卧室
         * deliverTime : 2016-10-31 10:54:32
         * completeTime : 2016-10-31 11:04:43
         */

        private List<List<LogisticsBean>> logistics;

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

        public List<List<LogisticsBean>> getLogistics() {
            return logistics;
        }

        public void setLogistics(List<List<LogisticsBean>> logistics) {
            this.logistics = logistics;
        }

        public static class LogisticsBean implements Parcelable {
            private String marketPrice;
            private String picUrl;
            private int count;
            private int status;
            private String expressCompany;
            private String refundTime;
            private int id;
            private int integralPrice;
            private String price;
            private String name;
            private String expressNo;
            private int orderProductId;
            private int deliverType;
            private String saleSkuValue;
            private String deliverTime;
            private String completeTime;

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

            public String getRefundTime() {
                return refundTime;
            }

            public void setRefundTime(String refundTime) {
                this.refundTime = refundTime;
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
                dest.writeString(this.refundTime);
                dest.writeInt(this.id);
                dest.writeInt(this.integralPrice);
                dest.writeString(this.price);
                dest.writeString(this.name);
                dest.writeString(this.expressNo);
                dest.writeInt(this.orderProductId);
                dest.writeInt(this.deliverType);
                dest.writeString(this.saleSkuValue);
                dest.writeString(this.deliverTime);
                dest.writeString(this.completeTime);
            }

            public LogisticsBean() {
            }

            protected LogisticsBean(Parcel in) {
                this.marketPrice = in.readString();
                this.picUrl = in.readString();
                this.count = in.readInt();
                this.status = in.readInt();
                this.expressCompany = in.readString();
                this.refundTime = in.readString();
                this.id = in.readInt();
                this.integralPrice = in.readInt();
                this.price = in.readString();
                this.name = in.readString();
                this.expressNo = in.readString();
                this.orderProductId = in.readInt();
                this.deliverType = in.readInt();
                this.saleSkuValue = in.readString();
                this.deliverTime = in.readString();
                this.completeTime = in.readString();
            }

            public static final Creator<LogisticsBean> CREATOR = new Creator<LogisticsBean>() {
                @Override
                public LogisticsBean createFromParcel(Parcel source) {
                    return new LogisticsBean(source);
                }

                @Override
                public LogisticsBean[] newArray(int size) {
                    return new LogisticsBean[size];
                }
            };
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
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
            dest.writeList(this.logistics);
        }

        public DirectLogisticsBean() {
        }

        protected DirectLogisticsBean(Parcel in) {
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
            this.logistics = new ArrayList<>();
            in.readList(this.logistics, LogisticsBean.class.getClassLoader());
        }

        public static final Creator<DirectLogisticsBean> CREATOR = new Creator<DirectLogisticsBean>() {
            @Override
            public DirectLogisticsBean createFromParcel(Parcel source) {
                return new DirectLogisticsBean(source);
            }

            @Override
            public DirectLogisticsBean[] newArray(int size) {
                return new DirectLogisticsBean[size];
            }
        };
    }
}
