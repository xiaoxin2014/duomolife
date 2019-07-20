package com.amkj.dmsh.shopdetails.bean;

import com.amkj.dmsh.base.BaseEntity;
import com.amkj.dmsh.constant.CommunalDetailBean;
import com.amkj.dmsh.user.bean.UserLikedProductEntity.LikedProductBean.MarketLabelBean;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import static com.amkj.dmsh.constant.ConstantMethod.stripTrailingZeros;

/**
 * Created by atd48 on 2016/10/4.
 */
public class ShopDetailsEntity extends BaseEntity {

    @SerializedName("result")
    private ShopPropertyBean shopPropertyBean;

    public ShopPropertyBean getShopPropertyBean() {
        return shopPropertyBean;
    }

    public void setResult(ShopPropertyBean result) {
        this.shopPropertyBean = result;
    }

    @SerializedName(value = "currentTime", alternate = "sysTime")
    private String currentTime;

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    public static class ShopPropertyBean {
        private List<MarketLabelBean> marketLabelList;
        private int id;
        private int itemId;
        private String tagIds;
        private String startTime;
        private String endTime;
        private String putAwayStartTime;
        private String sellStatus;
        private String marketPrice;
        private int buyIntergral;
        private String price;
        private int quantity;
        private int allowCoupon;
        private String waterRemark;
        private String videoUrl;
        private String delivery;
        private String name;
        private String subtitle;
        private String itemBodyStr;
        private String picUrl;
        private String images;
        private String brandName;
        private boolean collect;
        private String gpDiscounts;
        private int gpInfoId;
        private String newUserTag;
        private List<String> buyReason;
        private String activityCode;
        private String activityPrice;
        private String activityTag;
        private String activityStartTime;
        private String activityEndTime;
        private String activityRule;
        //是否含赠品
        private boolean hasPresent;
        //满赠活动
        private MZPresentInfoBean mzPresentInfo;
        @SerializedName("presentSkuIds")
        private String presentIds;
        @SerializedName("combineProductInfo")
        private List<CombineProductInfoBean> combineProductInfoList;
        private List<SkuSaleBean> skuSale;
        private List<PropvaluesBean> propvalues;
        private List<PropsBean> props;
        private List<TagsBean> tags;
        private List<String> preSaleInfo;
        private List<CouponJsonBean> couponJson;
        private List<CommunalDetailBean> itemBody;
        private long addSecond;
        private String maxDiscounts;


        public boolean isHasPresent() {
            return hasPresent;
        }

        public void setHasPresent(boolean hasPresent) {
            this.hasPresent = hasPresent;
        }

        public String getMaxDiscounts() {
            return maxDiscounts;
        }

        public void setMaxDiscounts(String maxDiscounts) {
            this.maxDiscounts = maxDiscounts;
        }

        public String getPresentIds() {
            return presentIds;
        }

        public void setPresentIds(String presentIds) {
            this.presentIds = presentIds;
        }

        public List<CombineProductInfoBean> getCombineProductInfoList() {
            return combineProductInfoList;
        }

        public void setCombineProductInfoList(List<CombineProductInfoBean> combineProductInfoList) {
            this.combineProductInfoList = combineProductInfoList;
        }

        public long getAddSecond() {
            return addSecond;
        }

        public void setAddSecond(long addSecond) {
            this.addSecond = addSecond;
        }

        public List<CouponJsonBean> getCouponJson() {
            return couponJson;
        }

        public List<MarketLabelBean> getMarketLabelList() {
            return marketLabelList;
        }

        public void setMarketLabelList(List<MarketLabelBean> marketLabelList) {
            this.marketLabelList = marketLabelList;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getItemId() {
            return itemId;
        }

        public void setItemId(int itemId) {
            this.itemId = itemId;
        }

        public String getTagIds() {
            return tagIds;
        }

        public void setTagIds(String tagIds) {
            this.tagIds = tagIds;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getPutAwayStartTime() {
            return putAwayStartTime;
        }

        public void setPutAwayStartTime(String putAwayStartTime) {
            this.putAwayStartTime = putAwayStartTime;
        }

        public String getSellStatus() {
            return sellStatus;
        }

        public void setSellStatus(String sellStatus) {
            this.sellStatus = sellStatus;
        }

        public String getMarketPrice() {
            return stripTrailingZeros(marketPrice);
        }

        public void setMarketPrice(String marketPrice) {
            this.marketPrice = marketPrice;
        }

        public int getBuyIntergral() {
            return buyIntergral;
        }

        public void setBuyIntergral(int buyIntergral) {
            this.buyIntergral = buyIntergral;
        }

        public String getPrice() {
            return stripTrailingZeros(price);
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

        public int getAllowCoupon() {
            return allowCoupon;
        }

        public void setAllowCoupon(int allowCoupon) {
            this.allowCoupon = allowCoupon;
        }

        public String getWaterRemark() {
            return waterRemark;
        }

        public void setWaterRemark(String waterRemark) {
            this.waterRemark = waterRemark;
        }

        public String getVideoUrl() {
            return videoUrl;
        }

        public void setVideoUrl(String videoUrl) {
            this.videoUrl = videoUrl;
        }

        public String getDelivery() {
            return delivery;
        }

        public void setDelivery(String delivery) {
            this.delivery = delivery;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSubtitle() {
            return subtitle;
        }

        public void setSubtitle(String subtitle) {
            this.subtitle = subtitle;
        }

        public String getItemBodyStr() {
            return itemBodyStr;
        }

        public void setItemBodyStr(String itemBodyStr) {
            this.itemBodyStr = itemBodyStr;
        }

        public String getPicUrl() {
            return picUrl;
        }

        public void setPicUrl(String picUrl) {
            this.picUrl = picUrl;
        }

        public String getImages() {
            return images;
        }

        public void setImages(String images) {
            this.images = images;
        }

        public String getBrandName() {
            return brandName;
        }

        public void setBrandName(String brandName) {
            this.brandName = brandName;
        }

        public boolean isCollect() {
            return collect;
        }

        public void setCollect(boolean collect) {
            this.collect = collect;
        }

        public String getGpDiscounts() {
            return gpDiscounts;
        }

        public void setGpDiscounts(String gpDiscounts) {
            this.gpDiscounts = gpDiscounts;
        }

        public int getGpInfoId() {
            return gpInfoId;
        }

        public void setGpInfoId(int gpInfoId) {
            this.gpInfoId = gpInfoId;
        }

        public String getNewUserTag() {
            return newUserTag;
        }

        public void setNewUserTag(String newUserTag) {
            this.newUserTag = newUserTag;
        }

        public List<String> getBuyReason() {
            return buyReason;
        }

        public void setBuyReason(List<String> buyReason) {
            this.buyReason = buyReason;
        }

        public String getActivityCode() {
            return activityCode;
        }

        public void setActivityCode(String activityCode) {
            this.activityCode = activityCode;
        }

        public String getActivityPrice() {
            return stripTrailingZeros(activityPrice);
        }

        public void setActivityPrice(String activityPrice) {
            this.activityPrice = activityPrice;
        }

        public String getActivityTag() {
            return activityTag;
        }

        public void setActivityTag(String activityTag) {
            this.activityTag = activityTag;
        }

        public String getActivityStartTime() {
            return activityStartTime;
        }

        public void setActivityStartTime(String activityStartTime) {
            this.activityStartTime = activityStartTime;
        }

        public String getActivityEndTime() {
            return activityEndTime;
        }

        public void setActivityEndTime(String activityEndTime) {
            this.activityEndTime = activityEndTime;
        }

        public String getActivityRule() {
            return activityRule;
        }

        public void setActivityRule(String activityRule) {
            this.activityRule = activityRule;
        }

        public MZPresentInfoBean getMzPresentInfo() {
            return mzPresentInfo;
        }

        public void setMzPresentInfo(MZPresentInfoBean mzPresentInfo) {
            this.mzPresentInfo = mzPresentInfo;
        }

        public List<SkuSaleBean> getSkuSale() {
            return skuSale;
        }

        public void setSkuSale(List<SkuSaleBean> skuSale) {
            this.skuSale = skuSale;
        }

        public List<PropvaluesBean> getPropvalues() {
            return propvalues;
        }

        public void setPropvalues(List<PropvaluesBean> propvalues) {
            this.propvalues = propvalues;
        }

        public List<PropsBean> getProps() {
            return props;
        }

        public void setProps(List<PropsBean> props) {
            this.props = props;
        }

        public List<TagsBean> getTags() {
            return tags;
        }

        public void setTags(List<TagsBean> tags) {
            this.tags = tags;
        }

        public List<String> getPreSaleInfo() {
            return preSaleInfo;
        }

        public void setPreSaleInfo(List<String> preSaleInfo) {
            this.preSaleInfo = preSaleInfo;
        }

        public List<CouponJsonBean> getCouponJsonList() {
            return couponJson;
        }

        public void setCouponJson(List<CouponJsonBean> couponJson) {
            this.couponJson = couponJson;
        }

        public List<CommunalDetailBean> getItemBody() {
            return itemBody;
        }

        public void setItemBody(List<CommunalDetailBean> itemBody) {
            this.itemBody = itemBody;
        }


        public static class TagsBean {
            /**
             * id : 30
             * name : asdasdas
             */

            private int id;
            private String name;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }

        public static class CouponJsonBean {
            /**
             * id : 117
             * coupon_name : 测试
             */
            private int id;
            private String coupon_name;

            public static CouponJsonBean objectFromData(String str) {

                return new Gson().fromJson(str, CouponJsonBean.class);
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getCoupon_name() {
                return coupon_name;
            }

            public void setCoupon_name(String coupon_name) {
                this.coupon_name = coupon_name;
            }
        }


        public static class PresentProductInfoBean implements MultiItemEntity {

            /**
             * presentName : 圣诞保温杯2
             */

            private String presentName;
            private int presentQuantity;
            private String presentSkuId;
            private boolean isChecked;
            private int itemType;
            //            自定义参数 加顶部线条
            private boolean select;
            //            是否是第一条
            private boolean firstTag;
            private String name;
            private int couponId;

            public boolean isChecked() {
                return isChecked;
            }

            public void setChecked(boolean checked) {
                isChecked = checked;
            }

            public String getPresentName() {
                return presentName;
            }

            public void setPresentName(String presentName) {
                this.presentName = presentName;
            }

            public int getPresentQuantity() {
                return presentQuantity;
            }

            public void setPresentQuantity(int presentQuantity) {
                this.presentQuantity = presentQuantity;
            }

            public String getPresentSkuId() {
                return presentSkuId;
            }

            public void setPresentSkuId(String presentSkuId) {
                this.presentSkuId = presentSkuId;
            }

            public boolean isSelect() {
                return select;
            }

            public void setSelect(boolean select) {
                this.select = select;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getCouponId() {
                return couponId;
            }

            public void setCouponId(int couponId) {
                this.couponId = couponId;
            }

            public boolean isFirstTag() {
                return firstTag;
            }

            public void setFirstTag(boolean firstTag) {
                this.firstTag = firstTag;
            }

            @Override
            public int getItemType() {
                return itemType;
            }

            public void setItemType(int itemType) {
                this.itemType = itemType;
            }
        }


        public static class CombineProductInfoBean {

            /**
             * discounts : 29.97
             * discountsTag : 省了29.97元
             * skuId : 3524
             */

            private String discounts;
            private String discountsTag;
            private int skuId;

            public String getDiscounts() {
                return discounts;
            }

            public void setDiscounts(String discounts) {
                this.discounts = discounts;
            }

            public String getDiscountsTag() {
                return discountsTag;
            }

            public void setDiscountsTag(String discountsTag) {
                this.discountsTag = discountsTag;
            }

            public int getSkuId() {
                return skuId;
            }

            public void setSkuId(int skuId) {
                this.skuId = skuId;
            }
        }

        public static class MZPresentInfoBean {

            /**
             * activityTag : 满赠
             * activityCode : MZ1560504451
             */

            private String activityTag;
            private String activityCode;

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
        }
    }
}
