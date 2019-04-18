package com.amkj.dmsh.user.bean;

import com.amkj.dmsh.base.BaseEntity;
import com.amkj.dmsh.constant.CommunalDetailBean;
import com.amkj.dmsh.dominant.bean.QualityGoodProductEntity.Attribute;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by atd48 on 2016/9/14.
 */
public class UserLikedProductEntity extends BaseEntity {

    /**
     * result : [{"id":5,"title":"川宁 洋甘菊茶包 洋甘菊花茶","price":0,"path":"/Uploads/goods_img/2016-04-26/571f13ad9fbd9.jpg"},{"id":6,"title":"北鼎K206高档电热水壶304食品级不锈钢烧水壶","price":0,"path":"/Uploads/goods_img/2016-04-26/571f146c7bff3.jpg"},{"id":7,"title":"日本代购迪士尼萌宠侧档DC-71","price":0,"path":"/Uploads/goods_img/2016-04-26/571f1523acdc9.jpg"},{"id":13,"title":"modern twist硅胶软围嘴宝宝吃饭围兜","price":0,"path":"/Uploads/goods_img/2016-04-28/5721ca3e8ed74.jpg"},{"id":15,"title":"膳魔师真空焖烧杯保温杯sk-3000儿童焖烧罐","price":0,"path":"/Uploads/goods_img/2016-04-28/5721cabde4190.jpg"},{"id":3,"title":"宫廷风雕花淡蓝色描金奢华感相框","price":0,"path":"/Uploads/goods_img/2016-04-26/571f131fa0bb5.jpg"},{"id":17,"title":"UMBRA正品 创意滑盖首饰盒","price":0,"path":"/Uploads/goods_img/2016-04-28/5721ccac196fe.jpg"},{"id":73,"title":"韩国promise me 权志龙GD太阳BIGBANG ","price":7000,"path":"/Uploads/goods_img/2016-03-17/56ea1bf1af0e8.jpg"},{"id":4,"title":"可爱卡通动漫长颈鹿大象书档 动物风格","price":0,"path":"/Uploads/goods_img/2016-04-26/571f135e22db0.jpg"}]
     * code : 01
     * msg : 请求成功
     */
    private String zoneName;
    private boolean categorySearch;
    @SerializedName("activityRuleDetail")
    private List<CommunalDetailBean> activityRuleDetailList;
    private String activityEndTime;
    private String activityTag;
    private String activityStartTime;
    private String title;
    private String activityRule;
    private String currentTime;
    private String activityDesc;
    //商品分类名称
    private String catergoryName;
    //商品分类pid
    private String pid;
    //商品分类id
    private String id;
    //商品分类type
    private String type;


    /**
     * id : 5
     * title : 川宁 洋甘菊茶包 洋甘菊花茶
     * price : 0
     * path : /Uploads/goods_img/2016-04-26/571f13ad9fbd9.jpg
     */

    @SerializedName("result")
    private List<LikedProductBean> likedProductBeanList;


    private String category_id;
    private String recommendFlag;
    private String noIds;
    private List<AdBean> ad;

    public String getCatergoryName() {
        return catergoryName;
    }

    public void setCatergoryName(String catergoryName) {
        this.catergoryName = catergoryName;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getActivityDesc() {
        return activityDesc;
    }

    public void setActivityDesc(String activityDesc) {
        this.activityDesc = activityDesc;
    }

    public String getRecommendFlag() {
        return recommendFlag;
    }

    public void setRecommendFlag(String recommendFlag) {
        this.recommendFlag = recommendFlag;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    public List<CommunalDetailBean> getActivityRuleDetailList() {
        return activityRuleDetailList;
    }

    public void setActivityRuleDetailList(List<CommunalDetailBean> activityRuleDetailList) {
        this.activityRuleDetailList = activityRuleDetailList;
    }

    public String getActivityEndTime() {
        return activityEndTime;
    }

    public void setActivityEndTime(String activityEndTime) {
        this.activityEndTime = activityEndTime;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getActivityRule() {
        return activityRule;
    }

    public void setActivityRule(String activityRule) {
        this.activityRule = activityRule;
    }

    public List<LikedProductBean> getLikedProductBeanList() {
        return likedProductBeanList;
    }

    public void setLikedProductBeanList(List<LikedProductBean> likedProductBeanList) {
        this.likedProductBeanList = likedProductBeanList;
    }

    public boolean isCategorySearch() {
        return categorySearch;
    }

    public void setCategorySearch(boolean categorySearch) {
        this.categorySearch = categorySearch;
    }

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getNoIds() {
        return noIds;
    }

    public void setNoIds(String noIds) {
        this.noIds = noIds;
    }

    public List<AdBean> getAdList() {
        return ad;
    }

    public void setAdList(List<AdBean> ad) {
        this.ad = ad;
    }

    public static class LikedProductBean extends Attribute implements MultiItemEntity {
        private String savepath;
        private String title;
        private int favor_num;
        private int favorNum;
        private String savename;
        private String ext;
        private int type_id;
        private int commentNum;
        @SerializedName(value = "picUrl", alternate = "path")
        private String picUrl;
        private String price;
        private String name;
        private String subtitle;
        private int quantity = 1;
        private int itemType;
        private String tagContent;
        private String sellStatus;
        private String activityCode;
        private int limitBuy;
        private int category_id;
        @SerializedName("buyIntergral")
        private int buyIntegral;
        private String waterRemark;
        private String activityTag;
        private List<MarketLabelBean> marketLabelList;

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
            return itemType;
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

        public static class MarketLabelBean {
            private int id;
            private String title;
            //            自定义属性 1 为活动标签 0 为营销标签
            private int labelCode;
            //            活动标签专属属性
            private String activityCode;

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
        }
    }

    public static class AdBean {
        /**
         * picUrl : http://image.domolife.cn/platform/FnCcckQHQZ1555488706470.jpg
         * web : http://www.domolife.cn/m/template/share_template/group.html
         * android : app://QualityGroupShopActivity
         * id : 1935
         * ios : app://DMLGroupPurchaseListViewController
         * miniprogram : /pages/group/group
         */

        private String picUrl;
        private String web;
        private String android;
        @SerializedName("id")
        private int idX;
        private String ios;
        private String miniprogram;

        public String getPicUrl() {
            return picUrl;
        }

        public void setPicUrl(String picUrl) {
            this.picUrl = picUrl;
        }

        public String getWeb() {
            return web;
        }

        public void setWeb(String web) {
            this.web = web;
        }

        public String getAndroid() {
            return android;
        }

        public void setAndroid(String android) {
            this.android = android;
        }

        public int getIdX() {
            return idX;
        }

        public void setIdX(int idX) {
            this.idX = idX;
        }

        public String getIos() {
            return ios;
        }

        public void setIos(String ios) {
            this.ios = ios;
        }

        public String getMiniprogram() {
            return miniprogram;
        }

        public void setMiniprogram(String miniprogram) {
            this.miniprogram = miniprogram;
        }
    }
}
