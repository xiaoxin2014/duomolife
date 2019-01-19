package com.amkj.dmsh.mine.bean;

import com.amkj.dmsh.base.BaseTimeEntity;
import com.amkj.dmsh.mine.enumutils.SelectionStatusTypeEnum;
import com.amkj.dmsh.user.bean.UserLikedProductEntity.LikedProductBean.MarketLabelBean;
import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.google.gson.annotations.SerializedName;

import java.util.List;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2019/1/10
 * version 3.2.0
 * class description:我的浏览记录
 */
public class MineBrowsHistoryEntity extends BaseTimeEntity {

    @SerializedName("result")
    private List<MineBrowsHistoryBean> mineBrowsHistoryList;
    private int totalPage;

    public List<MineBrowsHistoryBean> getMineBrowsHistoryList() {
        return mineBrowsHistoryList;
    }

    public void setMineBrowsHistoryList(List<MineBrowsHistoryBean> mineBrowsHistoryList) {
        this.mineBrowsHistoryList = mineBrowsHistoryList;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public static class MineBrowsHistoryBean extends AbstractExpandableItem<MineBrowsHistoryBean.GoodsInfoListBean> implements MultiItemEntity {
        /**
         * time : 2019-01-10
         * goodsInfoList : [{"id":"redis2019-01-10_6623","productId":6623,"title":"后益hoii抗UV超轻太阳伞","subTitle":"","price":"2","marketPrice":"1699","productImg":"http://image.domolife.cn/platform/20170414/20170414190359714.jpg","quantity":994,"activityTag":"","activityPriceDesc":"","goodsTags":[]},{"id":"redis2019-01-10_10082","productId":10082,"title":"本因BOII保温杯","subTitle":"创意六边形设计，强力控温","price":"198","marketPrice":"228","productImg":"http://image.domolife.cn/platform/20171030/20171030161154763.jpg","quantity":979,"activityTag":"","activityPriceDesc":"","goodsTags":[]},{"id":"redis2019-01-10_20327","productId":20327,"title":"限购一件","subTitle":"","price":"0.01","marketPrice":"11","productImg":"http://image.domolife.cn/platform/mWbfDJhW7F1545979113914.png","quantity":11108,"activityTag":"","activityPriceDesc":"","goodsTags":[]}]
         */

        private String time;
        private List<GoodsInfoListBean> goodsInfoList;
        /**
         * 本地属性
         */
        private int level;
        private int itemType;
//        编辑状态
        private boolean editStatus;
//        选中状态
        private boolean selectStatus;
//        数据加载状态 是否已加载完
        private boolean dataLoaded;
//        已选择条数
        private int selectionCount;
        private SelectionStatusTypeEnum statusTypeEnum;

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public List<GoodsInfoListBean> getGoodsInfoList() {
            return goodsInfoList;
        }

        public void setGoodsInfoList(List<GoodsInfoListBean> goodsInfoList) {
            this.goodsInfoList = goodsInfoList;
        }

        public boolean isEditStatus() {
            return editStatus;
        }

        public void setEditStatus(boolean editStatus) {
            this.editStatus = editStatus;
        }

        public boolean isSelectStatus() {
            return selectStatus;
        }

        public void setSelectStatus(boolean selectStatus) {
            this.selectStatus = selectStatus;
        }

        @Override
        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        @Override
        public int getItemType() {
            return itemType;
        }

        public void setItemType(int itemType) {
            this.itemType = itemType;
        }

        public boolean isDataLoaded() {
            return dataLoaded;
        }

        public void setDataLoaded(boolean dataLoaded) {
            this.dataLoaded = dataLoaded;
        }

        public int getSelectionCount() {
            return selectionCount;
        }

        public void setSelectionCount(int selectionCount) {
            this.selectionCount = selectionCount;
        }

        public SelectionStatusTypeEnum getStatusTypeEnum() {
            return statusTypeEnum;
        }

        public void setStatusTypeEnum(SelectionStatusTypeEnum statusTypeEnum) {
            this.statusTypeEnum = statusTypeEnum;
        }

        public static class GoodsInfoListBean implements MultiItemEntity{
            /**
             * id : redis2019-01-10_6623
             * productId : 6623
             * title : 后益hoii抗UV超轻太阳伞
             * subTitle :
             * price : 2
             * marketPrice : 1699
             * productImg : http://image.domolife.cn/platform/20170414/20170414190359714.jpg
             * quantity : 994
             * activityTag :
             * activityPriceDesc :
             * goodsTags : []
             */

            private String id;
            private int productId;
            private String title;
            private String subTitle;
            private String price;
            private String marketPrice;
            private String productImg;
            private int quantity;
            private String activityTag;
            private String activityPriceDesc;
            private List<MarketLabelBean> goodsTags;
            private int itemType;
            private boolean editStatus;
            private boolean selectStatus;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public int getProductId() {
                return productId;
            }

            public void setProductId(int productId) {
                this.productId = productId;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getSubTitle() {
                return subTitle;
            }

            public void setSubTitle(String subTitle) {
                this.subTitle = subTitle;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public List<MarketLabelBean> getGoodsTags() {
                return goodsTags;
            }

            public void setGoodsTags(List<MarketLabelBean> goodsTags) {
                this.goodsTags = goodsTags;
            }

            public String getMarketPrice() {
                return marketPrice;
            }

            public void setMarketPrice(String marketPrice) {
                this.marketPrice = marketPrice;
            }

            public String getProductImg() {
                return productImg;
            }

            public void setProductImg(String productImg) {
                this.productImg = productImg;
            }

            public int getQuantity() {
                return quantity;
            }

            public void setQuantity(int quantity) {
                this.quantity = quantity;
            }

            public String getActivityTag() {
                return activityTag;
            }

            public void setActivityTag(String activityTag) {
                this.activityTag = activityTag;
            }

            public String getActivityPriceDesc() {
                return activityPriceDesc;
            }

            public void setActivityPriceDesc(String activityPriceDesc) {
                this.activityPriceDesc = activityPriceDesc;
            }

            @Override
            public int getItemType() {
                return itemType;
            }

            public void setItemType(int itemType) {
                this.itemType = itemType;
            }

            public boolean isEditStatus() {
                return editStatus;
            }

            public void setEditStatus(boolean editStatus) {
                this.editStatus = editStatus;
            }

            public boolean isSelectStatus() {
                return selectStatus;
            }

            public void setSelectStatus(boolean selectStatus) {
                this.selectStatus = selectStatus;
            }
        }
    }
}
