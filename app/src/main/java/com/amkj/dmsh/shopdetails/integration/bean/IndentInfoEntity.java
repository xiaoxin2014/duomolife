package com.amkj.dmsh.shopdetails.integration.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.amkj.dmsh.shopdetails.bean.PriceInfoBean;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by atd48 on 2016/10/19.
 */
public class IndentInfoEntity {
    /**
     * result : {"order":{"no":"cX27928X1426X1492606860182","amount":"880.00","consignee":"刘桂鹏","address":"广东省深圳市福田区-梅林路48号理想公馆2806","deliveryAmount":"0","mobile":"13751077044","goods":[{"picUrl":"http://img.domolife.cn/platform/20170413/20170413153450672.jpg","marketPrice":"1599.00","price":"880.00","saleSkuValue":"颜色:粉色","name":"FOREO LUNA mini2 洁面仪","count":1,"orderProductId":6825,"id":6600,"integralPrice":0,"status":-12}],"remark":"","userId":27928,"integralAmount":0,"couponAmount":"5.00","payAmount":"875.00","createTime":"2017-04-19 21:01:00","id":11934,"status":-20},"status":{"0":"待支付","1":"支付处理中","10":"待发货","11":"发货处理中","12":"部分发货","20":"待收货","21":"部分收货","30":"交易完成","31":"部分评价","40":"已评价","-40":"已退款 ","-10":"申请退款","-11":"订单取消","-12":"支付超时","-20":"订单关闭","-30":"退款审核中","-31":"已拒绝"}}
     * currentTime : 2017-04-25 09:36:45
     * msg : 请求成功
     * code : 01
     * second : 7200
     */

    @SerializedName("result")
    private IndentInfoBean indentInfoBean;
    private String currentTime;
    private String msg;
    private String code;
    private int second;

    public IndentInfoBean getIndentInfoBean() {
        return indentInfoBean;
    }

    public void setIndentInfoBean(IndentInfoBean indentInfoBean) {
        this.indentInfoBean = indentInfoBean;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }

    public static class IndentInfoBean implements Parcelable {
        /**
         * order : {"no":"cX27928X1426X1492606860182","amount":"880.00","consignee":"刘桂鹏","address":"广东省深圳市福田区-梅林路48号理想公馆2806","deliveryAmount":"0","mobile":"13751077044","goods":[{"picUrl":"http://img.domolife.cn/platform/20170413/20170413153450672.jpg","marketPrice":"1599.00","price":"880.00","saleSkuValue":"颜色:粉色","name":"FOREO LUNA mini2 洁面仪","count":1,"orderProductId":6825,"id":6600,"integralPrice":0,"status":-12}],"remark":"","userId":27928,"integralAmount":0,"couponAmount":"5.00","payAmount":"875.00","createTime":"2017-04-19 21:01:00","id":11934,"status":-20}
         * status : {"0":"待支付","1":"支付处理中","10":"待发货","11":"发货处理中","12":"部分发货","20":"待收货","21":"部分收货","30":"交易完成","31":"部分评价","40":"已评价","-40":"已退款 ","-10":"申请退款","-11":"订单取消","-12":"支付超时","-20":"订单关闭","-30":"退款审核中","-31":"已拒绝"}
         */

        private OrderBean order;
        private Map<String, String> status;

        public OrderBean getOrder() {
            return order;
        }

        public void setOrder(OrderBean order) {
            this.order = order;
        }

        public Map<String, String> getStatus() {
            return status;
        }

        public void setStatus(Map<String, String> status) {
            this.status = status;
        }

        public static class OrderBean implements Parcelable {
            /**
             * no : cX27928X1426X1492606860182
             * amount : 880.00
             * consignee : 刘桂鹏
             * address : 广东省深圳市福田区-梅林路48号理想公馆2806
             * deliveryAmount : 0
             * mobile : 13751077044
             * goods : [{"picUrl":"http://img.domolife.cn/platform/20170413/20170413153450672.jpg","marketPrice":"1599.00","price":"880.00","saleSkuValue":"颜色:粉色","name":"FOREO LUNA mini2 洁面仪","count":1,"orderProductId":6825,"id":6600,"integralPrice":0,"status":-12}]
             * remark :
             * userId : 27928
             * integralAmount : 0
             * couponAmount : 5.00
             * payAmount : 875.00
             * createTime : 2017-04-19 21:01:00
             * id : 11934
             * status : -20
             */

            private String no;
            private String amount;
            private String consignee;
            private String address;
            private String deliveryAmount;
            private String mobile;
            private String remark;
            private int userId;
            private int integralAmount;
            private String couponAmount;
            private String payAmount;
            private String createTime;
            private int id;
            private int status;
            private String payType;
            private boolean isCoupon;
            private List<GoodsBean> goods;
            @SerializedName("priceInfo")
            private List<PriceInfoBean> priceInfoList;

            public boolean isCoupon() {
                return isCoupon;
            }

            public void setCoupon(boolean coupon) {
                isCoupon = coupon;
            }

            public List<PriceInfoBean> getPriceInfoList() {
                return priceInfoList;
            }

            public void setPriceInfoList(List<PriceInfoBean> priceInfoList) {
                this.priceInfoList = priceInfoList;
            }

            public String getPayType() {
                return payType;
            }

            public void setPayType(String payType) {
                this.payType = payType;
            }

            public String getNo() {
                return no;
            }

            public void setNo(String no) {
                this.no = no;
            }

            public String getAmount() {
                return amount;
            }

            public void setAmount(String amount) {
                this.amount = amount;
            }

            public String getConsignee() {
                return consignee;
            }

            public void setConsignee(String consignee) {
                this.consignee = consignee;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public String getDeliveryAmount() {
                return deliveryAmount;
            }

            public void setDeliveryAmount(String deliveryAmount) {
                this.deliveryAmount = deliveryAmount;
            }

            public String getMobile() {
                return mobile;
            }

            public void setMobile(String mobile) {
                this.mobile = mobile;
            }

            public String getRemark() {
                return remark;
            }

            public void setRemark(String remark) {
                this.remark = remark;
            }

            public int getUserId() {
                return userId;
            }

            public void setUserId(int userId) {
                this.userId = userId;
            }

            public int getIntegralAmount() {
                return integralAmount;
            }

            public void setIntegralAmount(int integralAmount) {
                this.integralAmount = integralAmount;
            }

            public String getCouponAmount() {
                return couponAmount;
            }

            public void setCouponAmount(String couponAmount) {
                this.couponAmount = couponAmount;
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

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public List<GoodsBean> getGoods() {
                return goods;
            }

            public void setGoods(List<GoodsBean> goods) {
                this.goods = goods;
            }

            public static class GoodsBean implements Parcelable {
                /**
                 * picUrl : http://img.domolife.cn/platform/20170413/20170413153450672.jpg
                 * marketPrice : 1599.00
                 * price : 880.00
                 * saleSkuValue : 颜色:粉色
                 * name : FOREO LUNA mini2 洁面仪
                 * count : 1
                 * orderProductId : 6825
                 * id : 6600
                 * integralPrice : 0
                 * status : -12
                 */

                private String picUrl;
                private String marketPrice;
                private String price;
                private String saleSkuValue;
                private String name;
                private int count;
                private int orderProductId;
                private int id;
                private int integralPrice;
                private int status;
                private String currentTime;
                private int second;
                private String orderCreateTime;
                private int orderRefundProductId;

                public String getCurrentTime() {
                    return currentTime;
                }

                public void setCurrentTime(String currentTime) {
                    this.currentTime = currentTime;
                }

                public int getSecond() {
                    return second;
                }

                public void setSecond(int second) {
                    this.second = second;
                }

                public String getOrderCreateTime() {
                    return orderCreateTime;
                }

                public void setOrderCreateTime(String orderCreateTime) {
                    this.orderCreateTime = orderCreateTime;
                }

                public String getPicUrl() {
                    return picUrl;
                }

                public void setPicUrl(String picUrl) {
                    this.picUrl = picUrl;
                }

                public String getMarketPrice() {
                    return marketPrice;
                }

                public void setMarketPrice(String marketPrice) {
                    this.marketPrice = marketPrice;
                }

                public String getPrice() {
                    return price;
                }

                public void setPrice(String price) {
                    this.price = price;
                }

                public String getSaleSkuValue() {
                    return saleSkuValue;
                }

                public void setSaleSkuValue(String saleSkuValue) {
                    this.saleSkuValue = saleSkuValue;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
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

                public int getStatus() {
                    return status;
                }

                public void setStatus(int status) {
                    this.status = status;
                }

                public int getOrderRefundProductId() {
                    return orderRefundProductId;
                }

                public void setOrderRefundProductId(int orderRefundProductId) {
                    this.orderRefundProductId = orderRefundProductId;
                }

                public GoodsBean() {
                }

                @Override
                public int describeContents() {
                    return 0;
                }

                @Override
                public void writeToParcel(Parcel dest, int flags) {
                    dest.writeString(this.picUrl);
                    dest.writeString(this.marketPrice);
                    dest.writeString(this.price);
                    dest.writeString(this.saleSkuValue);
                    dest.writeString(this.name);
                    dest.writeInt(this.count);
                    dest.writeInt(this.orderProductId);
                    dest.writeInt(this.id);
                    dest.writeInt(this.integralPrice);
                    dest.writeInt(this.status);
                    dest.writeString(this.currentTime);
                    dest.writeInt(this.second);
                    dest.writeString(this.orderCreateTime);
                    dest.writeInt(this.orderRefundProductId);
                }

                protected GoodsBean(Parcel in) {
                    this.picUrl = in.readString();
                    this.marketPrice = in.readString();
                    this.price = in.readString();
                    this.saleSkuValue = in.readString();
                    this.name = in.readString();
                    this.count = in.readInt();
                    this.orderProductId = in.readInt();
                    this.id = in.readInt();
                    this.integralPrice = in.readInt();
                    this.status = in.readInt();
                    this.currentTime = in.readString();
                    this.second = in.readInt();
                    this.orderCreateTime = in.readString();
                    this.orderRefundProductId = in.readInt();
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

            public OrderBean() {
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.no);
                dest.writeString(this.amount);
                dest.writeString(this.consignee);
                dest.writeString(this.address);
                dest.writeString(this.deliveryAmount);
                dest.writeString(this.mobile);
                dest.writeString(this.remark);
                dest.writeInt(this.userId);
                dest.writeInt(this.integralAmount);
                dest.writeString(this.couponAmount);
                dest.writeString(this.payAmount);
                dest.writeString(this.createTime);
                dest.writeInt(this.id);
                dest.writeInt(this.status);
                dest.writeString(this.payType);
                dest.writeByte(this.isCoupon ? (byte) 1 : (byte) 0);
                dest.writeTypedList(this.goods);
                dest.writeList(this.priceInfoList);
            }

            protected OrderBean(Parcel in) {
                this.no = in.readString();
                this.amount = in.readString();
                this.consignee = in.readString();
                this.address = in.readString();
                this.deliveryAmount = in.readString();
                this.mobile = in.readString();
                this.remark = in.readString();
                this.userId = in.readInt();
                this.integralAmount = in.readInt();
                this.couponAmount = in.readString();
                this.payAmount = in.readString();
                this.createTime = in.readString();
                this.id = in.readInt();
                this.status = in.readInt();
                this.payType = in.readString();
                this.isCoupon = in.readByte() != 0;
                this.goods = in.createTypedArrayList(GoodsBean.CREATOR);
                this.priceInfoList = new ArrayList<PriceInfoBean>();
                in.readList(this.priceInfoList, PriceInfoBean.class.getClassLoader());
            }

            public static final Creator<OrderBean> CREATOR = new Creator<OrderBean>() {
                @Override
                public OrderBean createFromParcel(Parcel source) {
                    return new OrderBean(source);
                }

                @Override
                public OrderBean[] newArray(int size) {
                    return new OrderBean[size];
                }
            };
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeParcelable(this.order, flags);
            dest.writeInt(this.status.size());
            for (Map.Entry<String, String> entry : this.status.entrySet()) {
                dest.writeString(entry.getKey());
                dest.writeString(entry.getValue());
            }
        }

        public IndentInfoBean() {
        }

        protected IndentInfoBean(Parcel in) {
            this.order = in.readParcelable(OrderBean.class.getClassLoader());
            int statusSize = in.readInt();
            this.status = new HashMap<String, String>(statusSize);
            for (int i = 0; i < statusSize; i++) {
                String key = in.readString();
                String value = in.readString();
                this.status.put(key, value);
            }
        }

        public static final Creator<IndentInfoBean> CREATOR = new Creator<IndentInfoBean>() {
            @Override
            public IndentInfoBean createFromParcel(Parcel source) {
                return new IndentInfoBean(source);
            }

            @Override
            public IndentInfoBean[] newArray(int size) {
                return new IndentInfoBean[size];
            }
        };
    }


}
