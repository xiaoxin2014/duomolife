package com.amkj.dmsh.release.bean;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;

import com.amkj.dmsh.base.BaseEntity;
import com.amkj.dmsh.utils.gson.GsonUtils;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/8/29
 * class description:关联商品
 */

public class RelevanceProEntity extends BaseEntity implements Parcelable{

    /**
     * result : [{"productId":4308,"price":23,"pictureUrl":"http://img.domolife.cn/platform/826kTFssed.png","id":176,"title":"AutoBot二代车载手机支架"},{"productId":6052,"price":35,"pictureUrl":"http://img.domolife.cn/platform/20170313/20170313164644929.jpg","id":801,"title":"Frosch柠檬浓缩洗洁精750ml"}]
     * msg : 请求成功
     * code : 01
     */
    @SerializedName("result")
    private List<RelevanceProBean> relevanceProList;
    //        订单需要
    private String orderNo;

    public static RelevanceProEntity objectFromData(String str) {
        return GsonUtils.fromJson(str, RelevanceProEntity.class);
    }

    public List<RelevanceProBean> getRelevanceProList() {
        return relevanceProList;
    }

    public void setRelevanceProList(List<RelevanceProBean> relevanceProList) {
        this.relevanceProList = relevanceProList;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public static class RelevanceProBean implements MultiItemEntity, Parcelable, Comparable<RelevanceProBean> {
        /**
         * productId : 4308
         * price : 23
         * pictureUrl : http://img.domolife.cn/platform/826kTFssed.png
         * id : 176
         * title : AutoBot二代车载手机支架
         */

        private int productId;
        private String price;
        private String pictureUrl;
        private int id;
        private String title;
        private boolean selPro;
        private int itemType;

        public boolean isSelPro() {
            return selPro;
        }

        public void setSelPro(boolean selPro) {
            this.selPro = selPro;
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

        public String getPictureUrl() {
            return pictureUrl;
        }

        public void setPictureUrl(String pictureUrl) {
            this.pictureUrl = pictureUrl;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        @Override
        public int getItemType() {
            return itemType;
        }

        public void setItemType(int itemType) {
            this.itemType = itemType;
        }

        public RelevanceProBean() {
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.productId);
            dest.writeString(this.price);
            dest.writeString(this.pictureUrl);
            dest.writeInt(this.id);
            dest.writeString(this.title);
            dest.writeByte(this.selPro ? (byte) 1 : (byte) 0);
            dest.writeInt(this.itemType);
        }

        protected RelevanceProBean(Parcel in) {
            this.productId = in.readInt();
            this.price = in.readString();
            this.pictureUrl = in.readString();
            this.id = in.readInt();
            this.title = in.readString();
            this.selPro = in.readByte() != 0;
            this.itemType = in.readInt();
        }

        public static final Creator<RelevanceProBean> CREATOR = new Creator<RelevanceProBean>() {
            @Override
            public RelevanceProBean createFromParcel(Parcel source) {
                return new RelevanceProBean(source);
            }

            @Override
            public RelevanceProBean[] newArray(int size) {
                return new RelevanceProBean[size];
            }
        };

        @Override
        public int compareTo(@NonNull RelevanceProBean o) {
            return this.getId() == o.getId() ? 0 : 1;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.relevanceProList);
        dest.writeString(this.orderNo);
    }

    public RelevanceProEntity() {
    }

    protected RelevanceProEntity(Parcel in) {
        this.relevanceProList = in.createTypedArrayList(RelevanceProBean.CREATOR);
        this.orderNo = in.readString();
    }

    public static final Creator<RelevanceProEntity> CREATOR = new Creator<RelevanceProEntity>() {
        @Override
        public RelevanceProEntity createFromParcel(Parcel source) {
            return new RelevanceProEntity(source);
        }

        @Override
        public RelevanceProEntity[] newArray(int size) {
            return new RelevanceProEntity[size];
        }
    };
}
