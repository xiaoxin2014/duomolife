package com.amkj.dmsh.shopdetails.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.amkj.dmsh.mine.bean.ShopCarNewInfoEntity.ShopCarNewInfoBean.CartInfoBean.CartProductInfoBean;

import java.util.List;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/8/17
 * class description:申请退款 申请退货
 */

public class DirectApplyRefundBean implements Parcelable {
    private String orderNo;
    private int type;
    private String images;
    private String content;
    private String reason;
    private String refundType;
    private String refundPrice;
    private int refundIntegralPrice;
    private int orderRefundProductId;
    private int refundReasonId;
    private List<DirectRefundProBean> directRefundProList;

    public int getRefundIntegralPrice() {
        return refundIntegralPrice;
    }

    public void setRefundIntegralPrice(int refundIntegralPrice) {
        this.refundIntegralPrice = refundIntegralPrice;
    }

    public int getRefundReasonId() {
        return refundReasonId;
    }

    public void setRefundReasonId(int refundReasonId) {
        this.refundReasonId = refundReasonId;
    }

    public int getOrderRefundProductId() {
        return orderRefundProductId;
    }

    public void setOrderRefundProductId(int orderRefundProductId) {
        this.orderRefundProductId = orderRefundProductId;
    }

    public String getRefundPrice() {
        return refundPrice;
    }

    public void setRefundPrice(String refundPrice) {
        this.refundPrice = refundPrice;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getRefundType() {
        return refundType;
    }

    public void setRefundType(String refundType) {
        this.refundType = refundType;
    }

    public List<DirectRefundProBean> getDirectRefundProList() {
        return directRefundProList;
    }

    public void setDirectRefundProList(List<DirectRefundProBean> directRefundProList) {
        this.directRefundProList = directRefundProList;
    }

    public static class DirectRefundProBean implements Parcelable {
        private int id;
        private int orderProductId;
        private int count;
        private String picUrl;
        private String price;
        private int integralPrice;
        private String saleSkuValue;
        private String name;
        private int refundReasonId;
        private List<CartProductInfoBean> cartProductInfoList;

        public int getIntegralPrice() {
            return integralPrice;
        }

        public void setIntegralPrice(int integralPrice) {
            this.integralPrice = integralPrice;
        }

        public int getRefundReasonId() {
            return refundReasonId;
        }

        public void setRefundReasonId(int refundReasonId) {
            this.refundReasonId = refundReasonId;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getOrderProductId() {
            return orderProductId;
        }

        public void setOrderProductId(int orderProductId) {
            this.orderProductId = orderProductId;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public String getPicUrl() {
            return picUrl;
        }

        public void setPicUrl(String picUrl) {
            this.picUrl = picUrl;
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

        public List<CartProductInfoBean> getCartProductInfoList() {
            return cartProductInfoList;
        }

        public void setCartProductInfoList(List<CartProductInfoBean> cartProductInfoList) {
            this.cartProductInfoList = cartProductInfoList;
        }

        public DirectRefundProBean() {
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.id);
            dest.writeInt(this.orderProductId);
            dest.writeInt(this.count);
            dest.writeString(this.picUrl);
            dest.writeString(this.price);
            dest.writeInt(this.integralPrice);
            dest.writeString(this.saleSkuValue);
            dest.writeString(this.name);
            dest.writeInt(this.refundReasonId);
            dest.writeTypedList(this.cartProductInfoList);
        }

        protected DirectRefundProBean(Parcel in) {
            this.id = in.readInt();
            this.orderProductId = in.readInt();
            this.count = in.readInt();
            this.picUrl = in.readString();
            this.price = in.readString();
            this.integralPrice = in.readInt();
            this.saleSkuValue = in.readString();
            this.name = in.readString();
            this.refundReasonId = in.readInt();
            this.cartProductInfoList = in.createTypedArrayList(CartProductInfoBean.CREATOR);
        }

        public static final Creator<DirectRefundProBean> CREATOR = new Creator<DirectRefundProBean>() {
            @Override
            public DirectRefundProBean createFromParcel(Parcel source) {
                return new DirectRefundProBean(source);
            }

            @Override
            public DirectRefundProBean[] newArray(int size) {
                return new DirectRefundProBean[size];
            }
        };
    }

    public DirectApplyRefundBean() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.orderNo);
        dest.writeInt(this.type);
        dest.writeString(this.images);
        dest.writeString(this.content);
        dest.writeString(this.reason);
        dest.writeString(this.refundType);
        dest.writeString(this.refundPrice);
        dest.writeInt(this.refundIntegralPrice);
        dest.writeInt(this.orderRefundProductId);
        dest.writeInt(this.refundReasonId);
        dest.writeTypedList(this.directRefundProList);
    }

    protected DirectApplyRefundBean(Parcel in) {
        this.orderNo = in.readString();
        this.type = in.readInt();
        this.images = in.readString();
        this.content = in.readString();
        this.reason = in.readString();
        this.refundType = in.readString();
        this.refundPrice = in.readString();
        this.refundIntegralPrice = in.readInt();
        this.orderRefundProductId = in.readInt();
        this.refundReasonId = in.readInt();
        this.directRefundProList = in.createTypedArrayList(DirectRefundProBean.CREATOR);
    }

    public static final Creator<DirectApplyRefundBean> CREATOR = new Creator<DirectApplyRefundBean>() {
        @Override
        public DirectApplyRefundBean createFromParcel(Parcel source) {
            return new DirectApplyRefundBean(source);
        }

        @Override
        public DirectApplyRefundBean[] newArray(int size) {
            return new DirectApplyRefundBean[size];
        }
    };
}
