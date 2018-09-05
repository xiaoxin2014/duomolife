package com.amkj.dmsh.dominant.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.amkj.dmsh.base.BaseEntity;
import com.google.gson.annotations.SerializedName;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/6/15
 * class description:请输入类描述
 */

public class GroupShopDetailsEntity extends BaseEntity{

    /**
     * result : {"gpCount":12,"gpPicUrl":"http://img.domolife.cn/platform/ZZWGaMTANz.jpg","gpStartTime":"2017-06-01 00:00:00","gpCreateStatus":"还差一人成团","gpInfoId":1,"productSkuValue":"默认:蓝色","gpProductQuantity":123,"gpType":"2人团","price":"1098.0","coverImage":"http://img.domolife.cn/platform/F4bZMMKEFF1497410875842.jpeg","quantityStatus":{"quantityStatusMsg":"库存足够，可开团和参团","quantityStatusId":1002},"images":"http://img.domolife.cn/platform/F4bZMMKEFF1497410875842.jpeg,http://img.domolife.cn/platform/NBCDtS3nBa1497410876188.jpeg,http://img.domolife.cn/platform/3atQYAZQ4X1497410876254.jpeg","productId":4282,"gpProductId":17,"gpEndTime":"2017-06-09 00:00:00","gpSkuId":112,"skuQuantity":998,"propValueId":"5","goodsAreaLabel":"拼团","subtitle":"","name":"北鼎K206钻石电热水壶礼盒装","gpPrice":"12.0"}
     * currentTime : 2017-06-16 10:56:31
     * msg : 请求成功
     * code : 01
     */

    @SerializedName("result")
    private GroupShopDetailsBean groupShopDetailsBean;
    private String currentTime;
    private long second;

    public GroupShopDetailsBean getGroupShopDetailsBean() {
        return groupShopDetailsBean;
    }

    public void setGroupShopDetailsBean(GroupShopDetailsBean groupShopDetailsBean) {
        this.groupShopDetailsBean = groupShopDetailsBean;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    public long getSecond() {
        return second;
    }

    public void setSecond(long second) {
        this.second = second;
    }

    public static class GroupShopDetailsBean implements Parcelable {
        /**
         * gpCount : 12
         * gpPicUrl : http://img.domolife.cn/platform/ZZWGaMTANz.jpg
         * gpStartTime : 2017-06-01 00:00:00
         * gpCreateStatus : 还差一人成团
         * gpInfoId : 1
         * productSkuValue : 默认:蓝色
         * gpProductQuantity : 123
         * gpType : 2人团
         * price : 1098.0
         * coverImage : http://img.domolife.cn/platform/F4bZMMKEFF1497410875842.jpeg
         * quantityStatus : {"quantityStatusMsg":"库存足够，可开团和参团","quantityStatusId":1002}
         * images : http://img.domolife.cn/platform/F4bZMMKEFF1497410875842.jpeg,http://img.domolife.cn/platform/NBCDtS3nBa1497410876188.jpeg,http://img.domolife.cn/platform/3atQYAZQ4X1497410876254.jpeg
         * productId : 4282
         * gpProductId : 17
         * gpEndTime : 2017-06-09 00:00:00
         * gpSkuId : 112
         * skuQuantity : 998
         * propValueId : 5
         * goodsAreaLabel : 拼团
         * subtitle :
         * name : 北鼎K206钻石电热水壶礼盒装
         * gpPrice : 12.0
         */

        private int gpCount;
        private String gpPicUrl;
        private String gpStartTime;
        private String gpCreateStatus;
        private int gpInfoId;
        private String productSkuValue;
        private int gpProductQuantity;
        private String gpType;
        private String price;
        private String coverImage;
        private QuantityStatusBean quantityStatus;
        private String images;
        private int productId;
        private int gpProductId;
        private String gpEndTime;
        private int gpSkuId;
        private int skuQuantity;
        private String propValueId;
        private String goodsAreaLabel;
        private String subtitle;
        private String name;
        private String gpPrice;
        private int gpStatus = 1;
        private int gpRecordId;
        private int range;

        public int getRange() {
            return range;
        }

        public void setRange(int range) {
            this.range = range;
        }

        public int getGpRecordId() {
            return gpRecordId;
        }

        public void setGpRecordId(int gpRecordId) {
            this.gpRecordId = gpRecordId;
        }

        public int getGpStatus() {
            return gpStatus;
        }

        public void setGpStatus(int gpStatus) {
            this.gpStatus = gpStatus;
        }

        public int getGpCount() {
            return gpCount;
        }

        public void setGpCount(int gpCount) {
            this.gpCount = gpCount;
        }

        public String getGpPicUrl() {
            return gpPicUrl;
        }

        public void setGpPicUrl(String gpPicUrl) {
            this.gpPicUrl = gpPicUrl;
        }

        public String getGpStartTime() {
            return gpStartTime;
        }

        public void setGpStartTime(String gpStartTime) {
            this.gpStartTime = gpStartTime;
        }

        public String getGpCreateStatus() {
            return gpCreateStatus;
        }

        public void setGpCreateStatus(String gpCreateStatus) {
            this.gpCreateStatus = gpCreateStatus;
        }

        public int getGpInfoId() {
            return gpInfoId;
        }

        public void setGpInfoId(int gpInfoId) {
            this.gpInfoId = gpInfoId;
        }

        public String getProductSkuValue() {
            return productSkuValue;
        }

        public void setProductSkuValue(String productSkuValue) {
            this.productSkuValue = productSkuValue;
        }

        public int getGpProductQuantity() {
            return gpProductQuantity;
        }

        public void setGpProductQuantity(int gpProductQuantity) {
            this.gpProductQuantity = gpProductQuantity;
        }

        public String getGpType() {
            return gpType;
        }

        public void setGpType(String gpType) {
            this.gpType = gpType;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getCoverImage() {
            return coverImage;
        }

        public void setCoverImage(String coverImage) {
            this.coverImage = coverImage;
        }

        public QuantityStatusBean getQuantityStatus() {
            return quantityStatus;
        }

        public void setQuantityStatus(QuantityStatusBean quantityStatus) {
            this.quantityStatus = quantityStatus;
        }

        public String getImages() {
            return images;
        }

        public void setImages(String images) {
            this.images = images;
        }

        public int getProductId() {
            return productId;
        }

        public void setProductId(int productId) {
            this.productId = productId;
        }

        public int getGpProductId() {
            return gpProductId;
        }

        public void setGpProductId(int gpProductId) {
            this.gpProductId = gpProductId;
        }

        public String getGpEndTime() {
            return gpEndTime;
        }

        public void setGpEndTime(String gpEndTime) {
            this.gpEndTime = gpEndTime;
        }

        public int getGpSkuId() {
            return gpSkuId;
        }

        public void setGpSkuId(int gpSkuId) {
            this.gpSkuId = gpSkuId;
        }

        public int getSkuQuantity() {
            return skuQuantity;
        }

        public void setSkuQuantity(int skuQuantity) {
            this.skuQuantity = skuQuantity;
        }

        public String getPropValueId() {
            return propValueId;
        }

        public void setPropValueId(String propValueId) {
            this.propValueId = propValueId;
        }

        public String getGoodsAreaLabel() {
            return goodsAreaLabel;
        }

        public void setGoodsAreaLabel(String goodsAreaLabel) {
            this.goodsAreaLabel = goodsAreaLabel;
        }

        public String getSubtitle() {
            return subtitle;
        }

        public void setSubtitle(String subtitle) {
            this.subtitle = subtitle;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getGpPrice() {
            return gpPrice;
        }

        public void setGpPrice(String gpPrice) {
            this.gpPrice = gpPrice;
        }

        public static class QuantityStatusBean implements Parcelable {
            /**
             * quantityStatusMsg : 库存足够，可开团和参团
             * quantityStatusId : 1002
             */

            private String quantityStatusMsg;
            private int quantityStatusId;

            public String getQuantityStatusMsg() {
                return quantityStatusMsg;
            }

            public void setQuantityStatusMsg(String quantityStatusMsg) {
                this.quantityStatusMsg = quantityStatusMsg;
            }

            public int getQuantityStatusId() {
                return quantityStatusId;
            }

            public void setQuantityStatusId(int quantityStatusId) {
                this.quantityStatusId = quantityStatusId;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.quantityStatusMsg);
                dest.writeInt(this.quantityStatusId);
            }

            public QuantityStatusBean() {
            }

            protected QuantityStatusBean(Parcel in) {
                this.quantityStatusMsg = in.readString();
                this.quantityStatusId = in.readInt();
            }

            public static final Creator<QuantityStatusBean> CREATOR = new Creator<QuantityStatusBean>() {
                @Override
                public QuantityStatusBean createFromParcel(Parcel source) {
                    return new QuantityStatusBean(source);
                }

                @Override
                public QuantityStatusBean[] newArray(int size) {
                    return new QuantityStatusBean[size];
                }
            };
        }

        public GroupShopDetailsBean() {
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.gpCount);
            dest.writeString(this.gpPicUrl);
            dest.writeString(this.gpStartTime);
            dest.writeString(this.gpCreateStatus);
            dest.writeInt(this.gpInfoId);
            dest.writeString(this.productSkuValue);
            dest.writeInt(this.gpProductQuantity);
            dest.writeString(this.gpType);
            dest.writeString(this.price);
            dest.writeString(this.coverImage);
            dest.writeParcelable(this.quantityStatus, flags);
            dest.writeString(this.images);
            dest.writeInt(this.productId);
            dest.writeInt(this.gpProductId);
            dest.writeString(this.gpEndTime);
            dest.writeInt(this.gpSkuId);
            dest.writeInt(this.skuQuantity);
            dest.writeString(this.propValueId);
            dest.writeString(this.goodsAreaLabel);
            dest.writeString(this.subtitle);
            dest.writeString(this.name);
            dest.writeString(this.gpPrice);
            dest.writeInt(this.gpStatus);
            dest.writeInt(this.gpRecordId);
            dest.writeInt(this.range);
        }

        protected GroupShopDetailsBean(Parcel in) {
            this.gpCount = in.readInt();
            this.gpPicUrl = in.readString();
            this.gpStartTime = in.readString();
            this.gpCreateStatus = in.readString();
            this.gpInfoId = in.readInt();
            this.productSkuValue = in.readString();
            this.gpProductQuantity = in.readInt();
            this.gpType = in.readString();
            this.price = in.readString();
            this.coverImage = in.readString();
            this.quantityStatus = in.readParcelable(QuantityStatusBean.class.getClassLoader());
            this.images = in.readString();
            this.productId = in.readInt();
            this.gpProductId = in.readInt();
            this.gpEndTime = in.readString();
            this.gpSkuId = in.readInt();
            this.skuQuantity = in.readInt();
            this.propValueId = in.readString();
            this.goodsAreaLabel = in.readString();
            this.subtitle = in.readString();
            this.name = in.readString();
            this.gpPrice = in.readString();
            this.gpStatus = in.readInt();
            this.gpRecordId = in.readInt();
            this.range = in.readInt();
        }

        public static final Creator<GroupShopDetailsBean> CREATOR = new Creator<GroupShopDetailsBean>() {
            @Override
            public GroupShopDetailsBean createFromParcel(Parcel source) {
                return new GroupShopDetailsBean(source);
            }

            @Override
            public GroupShopDetailsBean[] newArray(int size) {
                return new GroupShopDetailsBean[size];
            }
        };
    }
}
