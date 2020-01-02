package com.amkj.dmsh.shopdetails.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by xiaoxin on 2019/6/17
 * Version:v4.1.0
 * ClassDescription :SKU实体类
 */
public class SkuSaleBean implements Parcelable {
    /**
     * id : 81
     * productId : 4169
     * price : 0.50
     * quantity : 799
     * propValues : 6
     * type : 1
     * isNotice : 0
     * activityPrice : null
     * newUserTag : null
     */

    @SerializedName(value = "id", alternate = "skuId")
    private int id;
    private int productId;
    private String price;
    @SerializedName(value = "quantity")
    private int quantity;
    @SerializedName(value = "propValues")
    private String propValues;  //sku，多个propValueId之间用逗号隔开   例如L,白色
    private int type;
    private int isNotice;
    private String presentSkuIds;
    private String activityPrice;
    //   新人专属以及拼团标志专属字段
    private String newUserTag;
    //   积分商品独有属性
    private String moneyPrice;
    private String prePrice;
    //赠品信息
    private PresentInfoBean presentInfo;

    public static class PresentInfoBean {
        /**
         * id : 1
         * quantity : 1231
         * picUrl : http://image.domolife.cn/platform/YEYnTXn3as1560503438864.jpg
         * activityCode : ZP1560503944
         */

        private int id;
        private int quantity;
        private String picUrl;
        private String activityCode;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public String getPicUrl() {
            return picUrl;
        }

        public void setPicUrl(String picUrl) {
            this.picUrl = picUrl;
        }

        public String getActivityCode() {
            return activityCode;
        }

        public void setActivityCode(String activityCode) {
            this.activityCode = activityCode;
        }
    }

    public void setPrePrice(String prePrice) {
        this.prePrice = prePrice;
    }

    public PresentInfoBean getPresentInfo() {
        return presentInfo;
    }

    public void setPresentInfo(PresentInfoBean presentInfo) {
        this.presentInfo = presentInfo;
    }

    public String getPrePrice() {
        return prePrice;
    }

    public String getPresentSkuIds() {
        return presentSkuIds;
    }

    public void setPresentSkuIds(String presentSkuIds) {
        this.presentSkuIds = presentSkuIds;
    }

    public String getMoneyPrice() {
        return moneyPrice;
    }

    public void setMoneyPrice(String moneyPrice) {
        this.moneyPrice = moneyPrice;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getPropValues() {
        return propValues.replaceAll(" ", "");
    }

    public void setPropValues(String propValues) {
        this.propValues = propValues;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getIsNotice() {
        return isNotice;
    }

    public void setIsNotice(int isNotice) {
        this.isNotice = isNotice;
    }

    public String getActivityPrice() {
        return activityPrice;
    }

    public void setActivityPrice(String activityPrice) {
        this.activityPrice = activityPrice;
    }

    public String getNewUserTag() {
        return newUserTag;
    }

    public void setNewUserTag(String newUserTag) {
        this.newUserTag = newUserTag;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.productId);
        dest.writeString(this.price);
        dest.writeString(this.prePrice);
        dest.writeInt(this.quantity);
        dest.writeString(this.propValues);
        dest.writeInt(this.type);
        dest.writeInt(this.isNotice);
        dest.writeString(this.presentSkuIds);
        dest.writeString(this.activityPrice);
        dest.writeString(this.newUserTag);
        dest.writeString(this.moneyPrice);
    }

    public SkuSaleBean() {
    }

    public SkuSaleBean(int quantity, String price, int id) {
        this.id = id;
        this.price = price;
        this.quantity = quantity;
    }

    protected SkuSaleBean(Parcel in) {
        this.id = in.readInt();
        this.productId = in.readInt();
        this.price = in.readString();
        this.prePrice = in.readString();
        this.quantity = in.readInt();
        this.propValues = in.readString();
        this.type = in.readInt();
        this.isNotice = in.readInt();
        this.presentSkuIds = in.readString();
        this.activityPrice = in.readString();
        this.newUserTag = in.readString();
        this.moneyPrice = in.readString();
    }

    public static final Parcelable.Creator<SkuSaleBean> CREATOR = new Parcelable.Creator<SkuSaleBean>() {
        @Override
        public SkuSaleBean createFromParcel(Parcel source) {
            return new SkuSaleBean(source);
        }

        @Override
        public SkuSaleBean[] newArray(int size) {
            return new SkuSaleBean[size];
        }
    };
}
