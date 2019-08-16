package com.amkj.dmsh.shopdetails.bean;

import com.amkj.dmsh.find.bean.InvitationImgDetailEntity.InvitationImgDetailBean.TagsBean;
import com.amkj.dmsh.homepage.bean.InvitationDetailEntity.InvitationDetailBean.RelevanceProBean;
import com.amkj.dmsh.user.bean.UserLikedProductEntity.LikedProductBean;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2016/12/21
 * class description:限时特惠商品详情 抢购信息 优惠券 商品详情
 */

public class CommunalDetailObjectBean implements MultiItemEntity {
    private boolean firstLinePadding;
    private boolean isPost;  //是否是帖子富文本
    private int itemType;
    //    正常文本，网址
    private String content;
    //    商品
//    名字 || 淘宝优惠券名字 | 音频名字
    private String name;
    //    图片 || 优惠券图片 || 优惠券礼包图片
    private String picUrl;
    //    价格
    private String price;
    //    商品类型 淘宝 0 自营1 积分 2
    private int itemTypeId;
    //    商品ID || 优惠券id
    private int id;
    //    优惠券新图片
    private String newPirUrl;
    //    礼包Id
    private int cpId;
    //    礼包名字
    private String cpName;
    //    淘宝优惠券地址
    private String couponUrl;
    //    是否是最后一张淘宝优惠券
    private boolean lastTbCoupon;
    //    音频 视频地址
    private String url;
    //    音频来源
    private String from;
    //    配置标签
    private List<TagsBean> tagsBeans;
    //    配置商品推荐
    private List<LikedProductBean> productList;
    //    关联商品
    private List<RelevanceProBean> relevanceProBeanList;
    //   集合 更多
    private List<CommunalDetailObjectBean> moreDataList;
    //    商品图片列表
    private List<LikedProductBean> goodsList;

    //    文本
    public static final int NORTEXT = 0;
    //    限时特惠优惠券
    public static final int TYPE_LUCKY_MONEY = 1;
    //    商品优惠券
    public static final int TYPE_COUPON = 2;
    //    动态图片商品
    public static final int TYPE_GIF_IMG = 3;
    //    带加入购物车的商品
    public static final int TYPE_GOODS_WEL = 4;
    //    图片商品
    public static final int TYPE_GOODS_IMG = 5;
    //    带有立即购买的图片商品
    public static final int TYPE_GOODS_IMG_DIRECT_BUY = 19;
    //    视频播放
    public static final int TYPE_VIDEO = 6;
    //    分享文章
    public static final int TYPE_SHARE = 7;
    //    占位
    public static final int TYPE_EMPTY_OBJECT = 8;
    //    优惠券礼包
    public static final int TYPE_COUPON_PACKAGE = 9;
    //    音频播放
    public static final int TYPE_AUDIO = 10;
    //    商品详情头部
    public static final int TYPE_PRODUCT_TITLE = 11;
    //    查看更多
    public static final int TYPE_PRODUCT_MORE = 12;
    //    标签
    public static final int TYPE_PRODUCT_TAG = 13;
    //    淘宝链接
    public static final int TYPE_LINK_TAOBAO = 14;
    //    商品推荐
    public static final int TYPE_PRODUCT_RECOMMEND = 15;
    //    关联商品
    public static final int TYPE_RELEVANCE_PRODUCT = 16;
    //    限时特惠 头部红柱 + 标题 top灰色间隔
    public static final int TYPE_PROMOTION_TITLE = 17;
    //    两个并列商品列表
    public static final int TYPE_GOODS_2X = 18;
    //    三个并列商品列表
    public static final int TYPE_GOODS_3X = 20;

    public CommunalDetailObjectBean() {
    }

    public CommunalDetailObjectBean(String content) {
        this.content = content;
    }

    public boolean isPost() {
        return isPost;
    }

    public void setPost(boolean post) {
        this.isPost = post;
    }

    public List<LikedProductBean> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<LikedProductBean> goodsList) {
        this.goodsList = goodsList;
    }

    public List<LikedProductBean> getProductList() {
        return productList;
    }

    public void setProductList(List<LikedProductBean> productList) {
        this.productList = productList;
    }

    public List<RelevanceProBean> getRelevanceProBeanList() {
        return relevanceProBeanList;
    }

    public void setRelevanceProBeanList(List<RelevanceProBean> relevanceProBeanList) {
        this.relevanceProBeanList = relevanceProBeanList;
    }

    public boolean getFirstLinePadding() {
        return firstLinePadding;
    }

    public void setFirstLinePadding(boolean firstLinePadding) {
        this.firstLinePadding = firstLinePadding;
    }

    public List<TagsBean> getTagsBeans() {
        return tagsBeans;
    }

    public void setTagsBeans(List<TagsBean> tagsBeans) {
        this.tagsBeans = tagsBeans;
    }

    public int getCpId() {
        return cpId;
    }

    public void setCpId(int cpId) {
        this.cpId = cpId;
    }

    public String getCpName() {
        return cpName;
    }

    public void setCpName(String cpName) {
        this.cpName = cpName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public int getItemTypeId() {
        return itemTypeId;
    }

    public void setItemTypeId(int itemTypeId) {
        this.itemTypeId = itemTypeId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNewPirUrl() {
        return newPirUrl;
    }

    public void setNewPirUrl(String newPirUrl) {
        this.newPirUrl = newPirUrl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCouponUrl() {
        return couponUrl;
    }

    public void setCouponUrl(String couponUrl) {
        this.couponUrl = couponUrl;
    }

    public boolean isLastTbCoupon() {
        return lastTbCoupon;
    }

    public void setLastTbCoupon(boolean lastTbCoupon) {
        this.lastTbCoupon = lastTbCoupon;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    @Override
    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public List<CommunalDetailObjectBean> getMoreDataList() {
        return moreDataList;
    }

    public void setMoreDataList(List<CommunalDetailObjectBean> moreDataList) {
        this.moreDataList = moreDataList;
    }

//    public static class GoodsParataxisBean extends BaseRemoveExistProductBean implements MultiItemEntity {
//        /**
//         * picUrl : http://image.domolife.cn/platform/20170225/20170225180759430.jpg
//         * marketPrice : 118.00
//         * price : 58.00
//         * id : 4324
//         * type : goodsX3
//         * title : FaSoLa内衣收纳盒三件套
//         */
//
//        @SerializedName("picUrl")
//        private String picUrlX;
//        private String marketPrice;
//        private String price;
//        @SerializedName("title")
//        private String titleX;
//        private String type;
//        private int itemType;
//        private String waterRemark;
//        private String activityTag;
//        private List<LikedProductBean.MarketLabelBean> marketLabelList;
//        private int quantity = 1;
//
//
//        public String getWaterRemark() {
//            return waterRemark;
//        }
//
//        public void setWaterRemark(String waterRemark) {
//            this.waterRemark = waterRemark;
//        }
//
//        public String getActivityTag() {
//            return activityTag;
//        }
//
//        public void setActivityTag(String activityTag) {
//            this.activityTag = activityTag;
//        }
//
//        public List<LikedProductBean.MarketLabelBean> getMarketLabelList() {
//            return marketLabelList;
//        }
//
//        public void setMarketLabelList(List<LikedProductBean.MarketLabelBean> marketLabelList) {
//            this.marketLabelList = marketLabelList;
//        }
//
//        public int getQuantity() {
//            return quantity;
//        }
//
//        public void setQuantity(int quantity) {
//            this.quantity = quantity;
//        }
//
//        public String getPicUrlX() {
//            return picUrlX;
//        }
//
//        public void setPicUrlX(String picUrlX) {
//            this.picUrlX = picUrlX;
//        }
//
//        public String getType() {
//            return type;
//        }
//
//        public void setType(String type) {
//            this.type = type;
//        }
//
//        public String getMarketPrice() {
//            return marketPrice;
//        }
//
//        public void setMarketPrice(String marketPrice) {
//            this.marketPrice = marketPrice;
//        }
//
//        public String getPrice() {
//            return price;
//        }
//
//        public void setPrice(String price) {
//            this.price = price;
//        }
//
//        public String getTitleX() {
//            return titleX;
//        }
//
//        public void setTitleX(String titleX) {
//            this.titleX = titleX;
//        }
//
//        @Override
//        public int getItemType() {
//            return itemType;
//        }
//
//        public void setItemType(int itemType) {
//            this.itemType = itemType;
//        }
//    }
}
