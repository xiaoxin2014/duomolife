package com.amkj.dmsh.user.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.amkj.dmsh.base.BaseRemoveExistProductBean;
import com.amkj.dmsh.constant.ConstantVariable;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by xiaoxin on 2020/8/21
 * Version:v4.7.0
 */
public class LikedProductBean extends BaseRemoveExistProductBean implements MultiItemEntity {
    private String savepath;
    @SerializedName(value = "title", alternate = {"name", "productName"})
    private String title;
    private int favor_num;
    private int favorNum;
    private String savename;
    private String ext;
    @SerializedName(value = "type_id", alternate = {"itemTypeId", "typeId"})
    private int type_id = 1;//默认类型是自营商品
    private int commentNum;
    @SerializedName(value = "picUrl", alternate = {"path", "pic_url", "productImg"})
    private String picUrl;
    @SerializedName(value = "price", alternate = "preferentialPrice")
    private String price;
    @SerializedName(value = "subtitle", alternate = "subTitle")
    private String subtitle;
    private int quantity = 1;
    private int itemType;
    private String objectType;
    private String styleType;
    private String tagContent;
    private String sellStatus;
    private String activityCode;
    private String type;
    //新人专享商品专用字段（省多少钱）
    private String decreasePrice;
    //新人专享商品专区头部专用字段（省多少钱）
    private int titleHead;
    private int limitBuy;
    private int category_id;
    @SerializedName("buyIntergral")
    private int buyIntegral;
    private String waterRemark;
    private String activityTag;
    @SerializedName(value = "android_link", alternate = "androidLink")
    private String androidLink;
    private List<MarketLabelBean> marketLabelList;
    //拼团商品专用字段
    private String gpInfoId;
    //会员专享自定义专区字段
    private String vipPrice;
    private String vipTag;
    private String marketPrice;
    private String vipReduce;


    public String getVipReduce() {
        return vipReduce;
    }

    public void setVipReduce(String vipReduce) {
        this.vipReduce = vipReduce;
    }

    public String getVipPrice() {
        return vipPrice;
    }

    public void setVipPrice(String vipPrice) {
        this.vipPrice = vipPrice;
    }

    public String getVipTag() {
        return vipTag;
    }

    public void setVipTag(String vipTag) {
        this.vipTag = vipTag;
    }

    public String getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(String marketPrice) {
        this.marketPrice = marketPrice;
    }

    public String getStyleType() {
        return styleType;
    }

    public void setStyleType(String styleType) {
        this.styleType = styleType;
    }

    public String getGpInfoId() {
        return gpInfoId;
    }

    public void setGpInfoId(String gpInfoId) {
        this.gpInfoId = gpInfoId;
    }

    public int getTitleHead() {
        return titleHead;
    }

    public void setTitleHead(int titleHead) {
        this.titleHead = titleHead;
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public String getDecreasePrice() {
        return decreasePrice;
    }

    public void setDecreasePrice(String decreasePrice) {
        this.decreasePrice = decreasePrice;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getBuyIntegral() {
        return buyIntegral;
    }

    public void setBuyIntegral(int buyIntegral) {
        this.buyIntegral = buyIntegral;
    }

    public String getWaterRemark() {
        return waterRemark;
    }

    public void setWaterRemark(String waterRemark) {
        this.waterRemark = waterRemark;
    }

    public List<MarketLabelBean> getMarketLabelList() {
        return marketLabelList;
    }

    public void setMarketLabelList(List<MarketLabelBean> marketLabelList) {
        this.marketLabelList = marketLabelList;
    }

    public String getAndroidLink() {
        return androidLink;
    }

    public void setAndroidLink(String androidLink) {
        this.androidLink = androidLink;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public String getActivityTag() {
        return activityTag;
    }

    public void setActivityTag(String activityTag) {
        this.activityTag = activityTag;
    }

    public String getActivityCode() {
        return activityCode;
    }

    public void setActivityCode(String activityCode) {
        this.activityCode = activityCode;
    }

    public int getLimitBuy() {
        return limitBuy;
    }

    public void setLimitBuy(int limitBuy) {
        this.limitBuy = limitBuy;
    }

    public String getTagContent() {
        return tagContent;
    }

    public void setTagContent(String tagContent) {
        this.tagContent = tagContent;
    }

    public String getSellStatus() {
        return sellStatus;
    }

    public void setSellStatus(String sellStatus) {
        this.sellStatus = sellStatus;
    }

    public int getItemType() {
        if (TextUtils.isEmpty(objectType)) {
            return itemType;
        } else {
            return "ad".equals(objectType) ? ConstantVariable.AD_COVER : ConstantVariable.PRODUCT;
        }
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getFavorNum() {
        return favorNum;
    }

    public void setFavorNum(int favorNum) {
        this.favorNum = favorNum;
    }

    public int getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(int commentNum) {
        this.commentNum = commentNum;
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

    public String getName() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public int getType_id() {
        return type_id;
    }

    public void setType_id(int type_id) {
        this.type_id = type_id;
    }

    public String getSavepath() {
        return savepath;
    }

    public void setSavepath(String savepath) {
        this.savepath = savepath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getFavor_num() {
        return favor_num;
    }

    public void setFavor_num(int favor_num) {
        this.favor_num = favor_num;
    }

    public String getSavename() {
        return savename;
    }

    public void setSavename(String savename) {
        this.savename = savename;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public static class MarketLabelBean implements Parcelable {
        private int id;
        private String title;
        //自定义属性 1 为活动标签 0 为营销标签
        private int labelCode;
        //活动标签专属属性
        private String activityCode;
        //新人专享活动商品专用字段
        private boolean newUserTag;

        public boolean isNewUserTag() {
            return newUserTag;
        }

        public void setNewUserTag(boolean newUserTag) {
            this.newUserTag = newUserTag;
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

        public int getLabelCode() {
            return labelCode;
        }

        public void setLabelCode(int labelCode) {
            this.labelCode = labelCode;
        }

        public String getActivityCode() {
            return activityCode;
        }

        public void setActivityCode(String activityCode) {
            this.activityCode = activityCode;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.id);
            dest.writeString(this.title);
            dest.writeInt(this.labelCode);
            dest.writeString(this.activityCode);
        }

        public MarketLabelBean() {
        }

        protected MarketLabelBean(Parcel in) {
            this.id = in.readInt();
            this.title = in.readString();
            this.labelCode = in.readInt();
            this.activityCode = in.readString();
        }

        public static final Parcelable.Creator<MarketLabelBean> CREATOR = new Parcelable.Creator<MarketLabelBean>() {
            @Override
            public MarketLabelBean createFromParcel(Parcel source) {
                return new MarketLabelBean(source);
            }

            @Override
            public MarketLabelBean[] newArray(int size) {
                return new MarketLabelBean[size];
            }
        };
    }
}
