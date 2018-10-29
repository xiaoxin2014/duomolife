package com.amkj.dmsh.shopdetails.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.amkj.dmsh.base.BaseEntity;
import com.amkj.dmsh.constant.CommunalDetailBean;
import com.amkj.dmsh.user.bean.UserLikedProductEntity.LikedProductBean.MarketLabelBean;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import static com.amkj.dmsh.constant.ConstantMethod.replaceBlank;

/**
 * Created by atd48 on 2016/10/4.
 */
public class ShopDetailsEntity extends BaseEntity{

    /**
     * tags : [{"id":1,"name":"24小时发货"},{"id":2,"name":"正品保证"},{"id":3,"name":"海外直邮"},{"id":4,"name":"30天退货"}]
     * buyIntergral : 0
     * skuSale : [{"id":26,"price":"0.01","propValues":"5,9","quantity":26},{"id":25,"price":"0.01","propValues":"5,8","quantity":25},{"id":22,"price":"0.01","propValues":"4,11","quantity":22},{"id":23,"price":"0.01","propValues":"4,12","quantity":23},{"id":21,"price":"0.01","propValues":"4,10","quantity":21},{"id":8,"price":"0.01","propValues":"2,9","quantity":8},{"id":31,"price":"0.01","propValues":"6,8","quantity":31},{"id":3,"price":"0.01","propValues":"1,10","quantity":3},{"id":14,"price":"0.01","propValues":"3,9","quantity":14},{"id":42,"price":"0.01","propValues":"7,13","quantity":42},{"id":19,"price":"0.01","propValues":"4,8","quantity":19},{"id":40,"price":"0.01","propValues":"7,11","quantity":40},{"id":24,"price":"0.01","propValues":"4,13","quantity":24},{"id":41,"price":"0.01","propValues":"7,12","quantity":41},{"id":7,"price":"0.01","propValues":"2,8","quantity":7},{"id":33,"price":"0.01","propValues":"6,10","quantity":33},{"id":34,"price":"0.01","propValues":"6,11","quantity":34},{"id":35,"price":"0.01","propValues":"6,12","quantity":35},{"id":6,"price":"0.01","propValues":"1,13","quantity":6},{"id":4,"price":"0.01","propValues":"1,11","quantity":4},{"id":27,"price":"0.01","propValues":"5,10","quantity":27},{"id":5,"price":"0.01","propValues":"1,12","quantity":5},{"id":28,"price":"0.01","propValues":"5,11","quantity":28},{"id":39,"price":"0.01","propValues":"7,10","quantity":39},{"id":1,"price":"0.01","propValues":"1,8","quantity":1},{"id":15,"price":"0.01","propValues":"3,10","quantity":15},{"id":2,"price":"0.01","propValues":"1,9","quantity":2},{"id":30,"price":"0.01","propValues":"5,13","quantity":30},{"id":29,"price":"0.01","propValues":"5,12","quantity":29},{"id":20,"price":"0.01","propValues":"4,9","quantity":20},{"id":11,"price":"0.01","propValues":"2,12","quantity":11},{"id":37,"price":"0.01","propValues":"7,8","quantity":37},{"id":36,"price":"0.01","propValues":"6,13","quantity":36},{"id":9,"price":"0.01","propValues":"2,10","quantity":9},{"id":10,"price":"0.01","propValues":"2,11","quantity":10},{"id":16,"price":"0.01","propValues":"3,11","quantity":16},{"id":17,"price":"0.01","propValues":"3,12","quantity":17},{"id":38,"price":"0.01","propValues":"7,9","quantity":38},{"id":12,"price":"0.01","propValues":"2,13","quantity":12},{"id":18,"price":"0.01","propValues":"3,13","quantity":18},{"id":13,"price":"0.01","propValues":"3,8","quantity":13},{"id":32,"price":"0.01","propValues":"6,9","quantity":32}]
     * marketPrice : 0
     * picUrl : http://img.domolife.cn/Uploads/goods_img/2015-11-28/56592f513b2a9.jpg
     * propvalues : [{"propValueUrl":"http://p2.so.qhmsg.com/sdr/200_200_/t013b2f07b663a14e12.jpg","propId":1,"propValueName":"赤","propValueId":1},{"propValueUrl":"http://p0.so.qhmsg.com/t01e69b7a38462761e5.jpg","propId":1,"propValueName":"橙","propValueId":2},{"propValueUrl":"http://p0.so.qhmsg.com/sdr/200_200_/t01a1ec3c7530e669c5.jpg","propId":1,"propValueName":"黄","propValueId":3},{"propValueUrl":"http://p4.so.qhmsg.com/sdr/200_200_/t01e010c6d07c599294.jpg","propId":1,"propValueName":"绿","propValueId":4},{"propValueUrl":"http://p1.so.qhmsg.com/sdr/200_200_/t01b4b956a60697ca0b.jpg","propId":1,"propValueName":"青","propValueId":5},{"propValueUrl":"http://p4.so.qhmsg.com/sdr/200_200_/t017f132080e1e54f99.jpg","propId":1,"propValueName":"蓝","propValueId":6},{"propValueUrl":"http://p1.so.qhmsg.com/sdr/200_200_/t017e8c34f19422ad71.jpg","propId":1,"propValueName":"紫","propValueId":7},{"propId":2,"propValueName":"S","propValueId":8},{"propId":2,"propValueName":"M","propValueId":9},{"propId":2,"propValueName":"L","propValueId":10},{"propId":2,"propValueName":"XL","propValueId":11},{"propId":2,"propValueName":"XXL","propValueId":12},{"propId":2,"propValueName":"XXXL","propValueId":13},{"propId":3,"propValueName":"你妹","propValueId":14},{"propId":3,"propValueName":"你大爷","propValueId":15},{"propId":3,"propValueName":"你二大爷","propValueId":16},{"propId":3,"propValueName":"你三大爷","propValueId":17},{"propId":3,"propValueName":"没词了","propValueId":18},{"propId":11,"propValueName":"圆领","propValueId":27},{"propId":12,"propValueName":"常规","propValueId":28},{"propId":13,"propValueName":"常规","propValueId":29},{"propId":14,"propValueName":"芳苑阁","propValueId":30},{"propId":15,"propValueName":"几何图案 拼色 拼接","propValueId":31}]
     * thirdId : null
     * sku : 14,15,16,17,18,27,28,29,30,31
     * id : 4182
     * startTime : 2016-09-26 10:04:00
     * commentNum : 0
     * price : 330.00
     * thirdUrl : null
     * favorNum : 0
     * itemBody : <p>
     * <img src="/Uploads/Editor/2015-11-28/56592feab973c.jpg" alt="" />
     * </p>
     * <p>
     * <img src="/Uploads/Editor/2015-11-28/565930181d6c5.jpg" alt="" />
     * </p>
     * name : 顶级竹纤维外搭超柔软
     * tagIds : 0
     * subtitle :
     * images : http://img.domolife.cn/Uploads/goods_img/2016-04-26/571f105476475.jpg
     * quantity : 903
     * delivery : 直邮
     * props : [{"parentId":0,"propId":1,"propName":"颜色"},{"parentId":0,"propId":2,"propName":"尺码"},{"parentId":0,"propId":3,"propName":"逗比"},{"parentId":0,"propId":11,"propName":"领型"},{"parentId":0,"propId":12,"propName":"袖型"},{"parentId":0,"propId":13,"propName":"品牌"},{"parentId":0,"propId":14,"propName":"图案"},{"parentId":0,"propId":15,"propName":"图案文化"}]
     */

    @SerializedName("result")
    private ShopPropertyBean shopPropertyBean;
    /**
     * result : {"tags":[{"id":1,"name":"24小时发货"},{"id":2,"name":"正品保证"},{"id":3,"name":"海外直邮"},{"id":4,"name":"30天退货"}],"buyIntergral":0,"skuSale":[{"id":26,"price":"0.01","propValues":"5,9","quantity":26},{"id":25,"price":"0.01","propValues":"5,8","quantity":25},{"id":22,"price":"0.01","propValues":"4,11","quantity":22},{"id":23,"price":"0.01","propValues":"4,12","quantity":23},{"id":21,"price":"0.01","propValues":"4,10","quantity":21},{"id":8,"price":"0.01","propValues":"2,9","quantity":8},{"id":31,"price":"0.01","propValues":"6,8","quantity":31},{"id":3,"price":"0.01","propValues":"1,10","quantity":3},{"id":14,"price":"0.01","propValues":"3,9","quantity":14},{"id":42,"price":"0.01","propValues":"7,13","quantity":42},{"id":19,"price":"0.01","propValues":"4,8","quantity":19},{"id":40,"price":"0.01","propValues":"7,11","quantity":40},{"id":24,"price":"0.01","propValues":"4,13","quantity":24},{"id":41,"price":"0.01","propValues":"7,12","quantity":41},{"id":7,"price":"0.01","propValues":"2,8","quantity":7},{"id":33,"price":"0.01","propValues":"6,10","quantity":33},{"id":34,"price":"0.01","propValues":"6,11","quantity":34},{"id":35,"price":"0.01","propValues":"6,12","quantity":35},{"id":6,"price":"0.01","propValues":"1,13","quantity":6},{"id":4,"price":"0.01","propValues":"1,11","quantity":4},{"id":27,"price":"0.01","propValues":"5,10","quantity":27},{"id":5,"price":"0.01","propValues":"1,12","quantity":5},{"id":28,"price":"0.01","propValues":"5,11","quantity":28},{"id":39,"price":"0.01","propValues":"7,10","quantity":39},{"id":1,"price":"0.01","propValues":"1,8","quantity":1},{"id":15,"price":"0.01","propValues":"3,10","quantity":15},{"id":2,"price":"0.01","propValues":"1,9","quantity":2},{"id":30,"price":"0.01","propValues":"5,13","quantity":30},{"id":29,"price":"0.01","propValues":"5,12","quantity":29},{"id":20,"price":"0.01","propValues":"4,9","quantity":20},{"id":11,"price":"0.01","propValues":"2,12","quantity":11},{"id":37,"price":"0.01","propValues":"7,8","quantity":37},{"id":36,"price":"0.01","propValues":"6,13","quantity":36},{"id":9,"price":"0.01","propValues":"2,10","quantity":9},{"id":10,"price":"0.01","propValues":"2,11","quantity":10},{"id":16,"price":"0.01","propValues":"3,11","quantity":16},{"id":17,"price":"0.01","propValues":"3,12","quantity":17},{"id":38,"price":"0.01","propValues":"7,9","quantity":38},{"id":12,"price":"0.01","propValues":"2,13","quantity":12},{"id":18,"price":"0.01","propValues":"3,13","quantity":18},{"id":13,"price":"0.01","propValues":"3,8","quantity":13},{"id":32,"price":"0.01","propValues":"6,9","quantity":32}],"marketPrice":"0","picUrl":"http://img.domolife.cn/Uploads/goods_img/2015-11-28/56592f513b2a9.jpg","propvalues":[{"propValueUrl":"http://p2.so.qhmsg.com/sdr/200_200_/t013b2f07b663a14e12.jpg","propId":1,"propValueName":"赤","propValueId":1},{"propValueUrl":"http://p0.so.qhmsg.com/t01e69b7a38462761e5.jpg","propId":1,"propValueName":"橙","propValueId":2},{"propValueUrl":"http://p0.so.qhmsg.com/sdr/200_200_/t01a1ec3c7530e669c5.jpg","propId":1,"propValueName":"黄","propValueId":3},{"propValueUrl":"http://p4.so.qhmsg.com/sdr/200_200_/t01e010c6d07c599294.jpg","propId":1,"propValueName":"绿","propValueId":4},{"propValueUrl":"http://p1.so.qhmsg.com/sdr/200_200_/t01b4b956a60697ca0b.jpg","propId":1,"propValueName":"青","propValueId":5},{"propValueUrl":"http://p4.so.qhmsg.com/sdr/200_200_/t017f132080e1e54f99.jpg","propId":1,"propValueName":"蓝","propValueId":6},{"propValueUrl":"http://p1.so.qhmsg.com/sdr/200_200_/t017e8c34f19422ad71.jpg","propId":1,"propValueName":"紫","propValueId":7},{"propId":2,"propValueName":"S","propValueId":8},{"propId":2,"propValueName":"M","propValueId":9},{"propId":2,"propValueName":"L","propValueId":10},{"propId":2,"propValueName":"XL","propValueId":11},{"propId":2,"propValueName":"XXL","propValueId":12},{"propId":2,"propValueName":"XXXL","propValueId":13},{"propId":3,"propValueName":"你妹","propValueId":14},{"propId":3,"propValueName":"你大爷","propValueId":15},{"propId":3,"propValueName":"你二大爷","propValueId":16},{"propId":3,"propValueName":"你三大爷","propValueId":17},{"propId":3,"propValueName":"没词了","propValueId":18},{"propId":11,"propValueName":"圆领","propValueId":27},{"propId":12,"propValueName":"常规","propValueId":28},{"propId":13,"propValueName":"常规","propValueId":29},{"propId":14,"propValueName":"芳苑阁","propValueId":30},{"propId":15,"propValueName":"几何图案 拼色 拼接","propValueId":31}],"thirdId":null,"sku":"14,15,16,17,18,27,28,29,30,31","id":4182,"startTime":"2016-09-26 10:04:00","commentNum":0,"price":"330.00","thirdUrl":null,"favorNum":0,"itemBody":"<p>\r\n\t<img src=\"/Uploads/Editor/2015-11-28/56592feab973c.jpg\" alt=\"\" />\r\n<\/p>\r\n<p>\r\n\t<img src=\"/Uploads/Editor/2015-11-28/565930181d6c5.jpg\" alt=\"\" />\r\n<\/p>","name":"顶级竹纤维外搭超柔软","tagIds":"0","subtitle":"","images":"http://img.domolife.cn/Uploads/goods_img/2016-04-26/571f105476475.jpg","quantity":903,"delivery":"直邮","props":[{"parentId":0,"propId":1,"propName":"颜色"},{"parentId":0,"propId":2,"propName":"尺码"},{"parentId":0,"propId":3,"propName":"逗比"},{"parentId":0,"propId":11,"propName":"领型"},{"parentId":0,"propId":12,"propName":"袖型"},{"parentId":0,"propId":13,"propName":"品牌"},{"parentId":0,"propId":14,"propName":"图案"},{"parentId":0,"propId":15,"propName":"图案文化"}]}
     * code : 01
     * msg : 请求成功
     */
    private String currentTime;

    public ShopPropertyBean getShopPropertyBean() {
        return shopPropertyBean;
    }

    public void setShopPropertyBean(ShopPropertyBean shopPropertyBean) {
        this.shopPropertyBean = shopPropertyBean;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    public static class ShopPropertyBean {
        private int buyIntergral;
        private String marketPrice;
        private String picUrl;
        private String thirdId;
        private String sku;
        private int id;
        private String startTime;
        private String endTime;
        private int commentNum;
        private int evaCount;
        private String price;
        private String thirdUrl;
        private int favorNum;
        private String name;
        private String tagIds;
        private String subtitle;
        private String images;
        private int quantity;
        private int integralPrice;
        private int skimEnable;
        private String delivery;
        private boolean isRemind;
        private boolean isFavor;
        private String maxPrice;
        private boolean collect;
        private int allowCoupon;
        private String newUserTag;
        @SerializedName("presentSkuIds")
        private String presentIds;
        private List<CommunalDetailBean> itemBody;
        private List<FlashSaleInfoBean> flashsaleInfo;
        private ProductType productType;
        private String sellStatus;
        private int userScore;
        private boolean isCoupon;
        private String maxDiscounts;
        private String waterRemark;
        private String activityStartTime;
        private String activityEndTime;
        @SerializedName("activityRuleDetail")
        private List<CommunalDetailBean> activityRuleDetailList;
        private String activityTag;
        private String activityCode;
        private String activityRule;
        private String gpDiscounts;
        private int gpInfoId;
//        活动描述
        private String activityPriceDesc;
        private String activityPrice;
        private String priceDesc;
        @SerializedName("vidoUrl")
        private String videoUrl;
        @SerializedName("presentProductInfo")
        private List<PresentProductInfoBean> presentProductInfoList;
        @SerializedName("combineProductInfo")
        private List<CombineProductInfoBean> combineProductInfoList;
        private List<LuckyMoneyBean> luckyMoney;
        private List<TagsBean> tags;
        private List<SkuSaleBean> skuSale;
        private List<PropvaluesBean> propvalues;
        private List<PropsBean> props;
        private long addSecond;
//        售前内容
        private List<String> preSaleInfo;
        @SerializedName("couponJson")
        private List<CouponJsonBean> couponJsonList;
        private String flashBuyClickCount;
        private String integralTip;
        private List<MarketLabelBean> marketLabelList;

        public static ShopPropertyBean objectFromData(String str) {

            return new Gson().fromJson(str, ShopPropertyBean.class);
        }

        public String getIntegralTip() {
            return integralTip;
        }

        public void setIntegralTip(String integralTip) {
            this.integralTip = integralTip;
        }

        public String getNewUserTag() {
            return newUserTag;
        }

        public void setNewUserTag(String newUserTag) {
            this.newUserTag = newUserTag;
        }

        public String getFlashBuyClickCount() {
            return flashBuyClickCount;
        }

        public void setFlashBuyClickCount(String flashBuyClickCount) {
            this.flashBuyClickCount = flashBuyClickCount;
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

        public String getVideoUrl() {
            return videoUrl;
        }

        public void setVideoUrl(String videoUrl) {
            this.videoUrl = videoUrl;
        }

        public String getWaterRemark() {
            return waterRemark;
        }

        public void setWaterRemark(String waterRemark) {
            this.waterRemark = waterRemark;
        }

        public String getMaxDiscounts() {
            return maxDiscounts;
        }

        public void setMaxDiscounts(String maxDiscounts) {
            this.maxDiscounts = maxDiscounts;
        }

        public List<CombineProductInfoBean> getCombineProductInfoList() {
            return combineProductInfoList;
        }

        public void setCombineProductInfoList(List<CombineProductInfoBean> combineProductInfoList) {
            this.combineProductInfoList = combineProductInfoList;
        }

        public List<PresentProductInfoBean> getPresentProductInfoList() {
            return presentProductInfoList;
        }

        public void setPresentProductInfoList(List<PresentProductInfoBean> presentProductInfoList) {
            this.presentProductInfoList = presentProductInfoList;
        }

        public String getActivityPriceDesc() {
            return activityPriceDesc;
        }

        public void setActivityPriceDesc(String activityPriceDesc) {
            this.activityPriceDesc = activityPriceDesc;
        }

        public String getActivityPrice() {
            return activityPrice;
        }

        public void setActivityPrice(String activityPrice) {
            this.activityPrice = activityPrice;
        }

        public long getAddSecond() {
            return addSecond;
        }

        public void setAddSecond(long addSecond) {
            this.addSecond = addSecond;
        }

        public String getPresentIds() {
            return presentIds;
        }

        public void setPresentIds(String presentIds) {
            this.presentIds = presentIds;
        }

        public String getPriceDesc() {
            return priceDesc;
        }

        public void setPriceDesc(String priceDesc) {
            this.priceDesc = priceDesc;
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

        public List<CommunalDetailBean> getActivityRuleDetailList() {
            return activityRuleDetailList;
        }

        public void setActivityRuleDetailList(List<CommunalDetailBean> activityRuleDetailList) {
            this.activityRuleDetailList = activityRuleDetailList;
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

        public String getActivityRule() {
            return activityRule;
        }

        public void setActivityRule(String activityRule) {
            this.activityRule = activityRule;
        }

        public boolean isCoupon() {
            return isCoupon;
        }

        public void setCoupon(boolean coupon) {
            isCoupon = coupon;
        }

        public int getAllowCoupon() {
            return allowCoupon;
        }

        public void setAllowCoupon(int allowCoupon) {
            this.allowCoupon = allowCoupon;
        }

        public int getUserScore() {
            return userScore;
        }

        public void setUserScore(int userScore) {
            this.userScore = userScore;
        }

        public String getSellStatus() {
            return sellStatus;
        }

        public void setSellStatus(String sellStatus) {
            this.sellStatus = sellStatus;
        }

        public ProductType getProductType() {
            return productType;
        }

        public void setProductType(ProductType productType) {
            this.productType = productType;
        }

        public boolean isCollect() {
            return collect;
        }

        public void setCollect(boolean collect) {
            this.collect = collect;
        }

        public int getEvaCount() {
            return evaCount;
        }

        public void setEvaCount(int evaCount) {
            this.evaCount = evaCount;
        }

        public int getSkimEnable() {
            return skimEnable;
        }

        public void setSkimEnable(int skimEnable) {
            this.skimEnable = skimEnable;
        }

        public List<CommunalDetailBean> getItemBody() {
            return itemBody;
        }

        public void setItemBody(List<CommunalDetailBean> itemBody) {
            this.itemBody = itemBody;
        }

        public List<FlashSaleInfoBean> getFlashsaleInfo() {
            return flashsaleInfo;
        }

        public void setFlashsaleInfo(List<FlashSaleInfoBean> flashsaleInfo) {
            this.flashsaleInfo = flashsaleInfo;
        }

        public List<LuckyMoneyBean> getLuckyMoney() {
            return luckyMoney;
        }

        public void setLuckyMoney(List<LuckyMoneyBean> luckyMoney) {
            this.luckyMoney = luckyMoney;
        }

        public String getMaxPrice() {
            return maxPrice;
        }

        public void setMaxPrice(String maxPrice) {
            this.maxPrice = maxPrice;
        }

        public boolean isRemind() {
            return isRemind;
        }

        public void setRemind(boolean remind) {
            isRemind = remind;
        }

        public boolean isFavor() {
            return isFavor;
        }

        public void setFavor(boolean favor) {
            isFavor = favor;
        }

        public int getIntegralPrice() {
            return integralPrice;
        }

        public void setIntegralPrice(int integralPrice) {
            this.integralPrice = integralPrice;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public int getBuyIntergral() {
            return buyIntergral;
        }

        public void setBuyIntergral(int buyIntergral) {
            this.buyIntergral = buyIntergral;
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

        public String getThirdId() {
            return thirdId;
        }

        public void setThirdId(String thirdId) {
            this.thirdId = thirdId;
        }

        public String getSku() {
            return sku;
        }

        public void setSku(String sku) {
            this.sku = sku;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public int getCommentNum() {
            return commentNum;
        }

        public void setCommentNum(int commentNum) {
            this.commentNum = commentNum;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getThirdUrl() {
            return thirdUrl;
        }

        public void setThirdUrl(String thirdUrl) {
            this.thirdUrl = thirdUrl;
        }

        public int getFavorNum() {
            return favorNum;
        }

        public void setFavorNum(int favorNum) {
            this.favorNum = favorNum;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTagIds() {
            return tagIds;
        }

        public void setTagIds(String tagIds) {
            this.tagIds = tagIds;
        }

        public String getSubtitle() {
            return subtitle;
        }

        public void setSubtitle(String subtitle) {
            this.subtitle = subtitle;
        }

        public String getImages() {
            return images;
        }

        public void setImages(String images) {
            this.images = images;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public String getDelivery() {
            return delivery;
        }

        public void setDelivery(String delivery) {
            this.delivery = delivery;
        }

        public List<TagsBean> getTags() {
            return tags;
        }

        public void setTags(List<TagsBean> tags) {
            this.tags = tags;
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

        public List<String> getPreSaleInfo() {
            return preSaleInfo;
        }

        public void setPreSaleInfo(List<String> preSaleInfo) {
            this.preSaleInfo = preSaleInfo;
        }

        public List<CouponJsonBean> getCouponJsonList() {
            return couponJsonList;
        }

        public void setCouponJsonList(List<CouponJsonBean> couponJsonList) {
            this.couponJsonList = couponJsonList;
        }

        public List<MarketLabelBean> getMarketLabelList() {
            return marketLabelList;
        }

        public void setMarketLabelList(List<MarketLabelBean> marketLabelList) {
            this.marketLabelList = marketLabelList;
        }

        public static class TagsBean {
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

        public static class ProductType implements Parcelable {
            private String category_name;
            private int category_id;

            public String getCategory_name() {
                return category_name;
            }

            public void setCategory_name(String category_name) {
                this.category_name = category_name;
            }

            public int getCategory_id() {
                return category_id;
            }

            public void setCategory_id(int category_id) {
                this.category_id = category_id;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.category_name);
                dest.writeInt(this.category_id);
            }

            public ProductType() {
            }

            protected ProductType(Parcel in) {
                this.category_name = in.readString();
                this.category_id = in.readInt();
            }

            public static final Creator<ProductType> CREATOR = new Creator<ProductType>() {
                @Override
                public ProductType createFromParcel(Parcel source) {
                    return new ProductType(source);
                }

                @Override
                public ProductType[] newArray(int size) {
                    return new ProductType[size];
                }
            };
        }

        //        抢购信息
        public static class FlashSaleInfoBean {
            private String content;
            private String type;

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }
        }

        public static class LuckyMoneyBean {
            private String name;
            private String url;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }

        public static class SkuSaleBean {
            private int id;
            private String price;
            private String propValues;
            private int quantity;
            private String presentSkuIds;
            private String combineSkuIds;
//            积分商品独有属性
            private String moneyPrice;
//            是否开启补货通知
            private int isNotice;

            public int getIsNotice() {
                return isNotice;
            }

            public void setIsNotice(int isNotice) {
                this.isNotice = isNotice;
            }

            public String getMoneyPrice() {
                return moneyPrice;
            }

            public void setMoneyPrice(String moneyPrice) {
                this.moneyPrice = moneyPrice;
            }

            public String getCombineSkuIds() {
                return combineSkuIds;
            }

            public void setCombineSkuIds(String combineSkuIds) {
                this.combineSkuIds = combineSkuIds;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public String getPropValues() {
                return propValues;
            }

            public void setPropValues(String propValues) {
                this.propValues = replaceBlank(propValues);
            }

            public int getQuantity() {
                return quantity;
            }

            public void setQuantity(int quantity) {
                this.quantity = quantity;
            }

            public String getPresentSkuIds() {
                return presentSkuIds;
            }

            public void setPresentSkuIds(String presentSkuIds) {
                this.presentSkuIds = presentSkuIds;
            }
        }

        public static class PropvaluesBean {
            private String propValueUrl;
            private int propId;
            private String propValueName;
            private int propValueId;

            public String getPropValueUrl() {
                return propValueUrl;
            }

            public void setPropValueUrl(String propValueUrl) {
                this.propValueUrl = propValueUrl;
            }

            public int getPropId() {
                return propId;
            }

            public void setPropId(int propId) {
                this.propId = propId;
            }

            public String getPropValueName() {
                return propValueName;
            }

            public void setPropValueName(String propValueName) {
                this.propValueName = propValueName;
            }

            public int getPropValueId() {
                return propValueId;
            }

            public void setPropValueId(int propValueId) {
                this.propValueId = propValueId;
            }
        }

        public static class PropsBean {
            private int parentId;
            private int propId;
            private String propName;

            public int getParentId() {
                return parentId;
            }

            public void setParentId(int parentId) {
                this.parentId = parentId;
            }

            public int getPropId() {
                return propId;
            }

            public void setPropId(int propId) {
                this.propId = propId;
            }

            public String getPropName() {
                return propName;
            }

            public void setPropName(String propName) {
                this.propName = propName;
            }
        }

        public static class PresentProductInfoBean implements MultiItemEntity{

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
    }
}
