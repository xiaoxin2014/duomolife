package com.amkj.dmsh.user.bean;

import com.amkj.dmsh.base.BaseEntity;
import com.amkj.dmsh.bean.CommunalDetailBean;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by atd48 on 2016/9/14.
 */
public class UserLikedProductEntity extends BaseEntity implements Comparable<UserLikedProductEntity> {

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
    //position(用于排序)
    private int position;


    //自定义专区逛一逛得积分专用参数
    private int isUserHaveReward;
    private String rewardInfo;
    private int viewTime;
    private int isReward;

    public boolean isUserHaveReward() {
        return isUserHaveReward == 1;
    }

    public void setIsUserHaveReward(int isUserHaveReward) {
        this.isUserHaveReward = isUserHaveReward;
    }

    public String getRewardInfo() {
        return rewardInfo.replaceAll("<br>", "\n");
    }

    public void setRewardInfo(String rewardInfo) {
        this.rewardInfo = rewardInfo;
    }

    public int getViewTime() {
        return viewTime;
    }

    public void setViewTime(int viewTime) {
        this.viewTime = viewTime;
    }

    public boolean isReward() {
        return isReward == 1;
    }

    public void setIsReward(int isReward) {
        this.isReward = isReward;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

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

    public List<LikedProductBean> getGoodsList() {
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

    @Override
    public int compareTo(UserLikedProductEntity userLikedProductEntity) {
        return this.getPosition() - userLikedProductEntity.getPosition();
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
