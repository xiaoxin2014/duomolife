package com.amkj.dmsh.homepage.bean;

import com.amkj.dmsh.base.BaseEntity;

import java.util.List;

/**
 * Created by xiaoxin on 2021/2/24
 * Version:v5.0.0
 * ClassDescription :视频详情实体类
 */
public class VideoDetailEntity extends BaseEntity {


    /**
     * sysTime : 2021-02-27 09:31:13
     * idList : ["1","2"]
     * result : [{"id":"1","coverPath":"http://image.domolife.cn/platform/20210125/20210125153242257.jpeg","videoPath":"https://image.domolife.cn/merchant/e8SzBE1611566601274.mp4","title":"测试123123","productInfoList":[{"id":"18338","title":"测试同步","coverPath":"http://image.domolife.cn/platform/20201013/20201013093606631.jpeg","quantity":"0","price":"7","activityTag":"","activityName":"","marketLabelList":[{"id":16,"title":"预售"},{"id":20,"title":"前100件减20元"}]},{"id":"18301","title":"名刀司命","coverPath":"http://image.domolife.cn/platform/20200417/20200417100929215.jpeg","quantity":"1","price":"2","activityTag":"","activityName":"","marketLabelList":[{"id":16,"title":"预售"}]}]}]
     */

    private String sysTime;
    private List<String> idList;
    private List<VideoDetailBean> result;

    public String getSysTime() {
        return sysTime;
    }

    public void setSysTime(String sysTime) {
        this.sysTime = sysTime;
    }

    public List<String> getIdList() {
        return idList;
    }

    public void setIdList(List<String> idList) {
        this.idList = idList;
    }

    public List<VideoDetailBean> getResult() {
        return result;
    }

    public void setResult(List<VideoDetailBean> result) {
        this.result = result;
    }

    public static class VideoDetailBean {
        /**
         * id : 1
         * coverPath : http://image.domolife.cn/platform/20210125/20210125153242257.jpeg
         * videoPath : https://image.domolife.cn/merchant/e8SzBE1611566601274.mp4
         * title : 测试123123
         * productInfoList : [{"id":"18338","title":"测试同步","coverPath":"http://image.domolife.cn/platform/20201013/20201013093606631.jpeg","quantity":"0","price":"7","activityTag":"","activityName":"","marketLabelList":[{"id":16,"title":"预售"},{"id":20,"title":"前100件减20元"}]},{"id":"18301","title":"名刀司命","coverPath":"http://image.domolife.cn/platform/20200417/20200417100929215.jpeg","quantity":"1","price":"2","activityTag":"","activityName":"","marketLabelList":[{"id":16,"title":"预售"}]}]
         */

        private String id;
        private String coverPath;
        private String videoPath;
        private String title;
        private List<ProductInfoListBean> productInfoList;
        private boolean showProduct=true;

        public boolean isShowProduct() {
            return showProduct;
        }

        public void setShowProduct(boolean showProduct) {
            this.showProduct = showProduct;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCoverPath() {
            return coverPath;
        }

        public void setCoverPath(String coverPath) {
            this.coverPath = coverPath;
        }

        public String getVideoPath() {
            return videoPath;
        }

        public void setVideoPath(String videoPath) {
            this.videoPath = videoPath;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public List<ProductInfoListBean> getProductInfoList() {
            return productInfoList;
        }

        public void setProductInfoList(List<ProductInfoListBean> productInfoList) {
            this.productInfoList = productInfoList;
        }

        public static class ProductInfoListBean {
            /**
             * id : 18338
             * title : 测试同步
             * coverPath : http://image.domolife.cn/platform/20201013/20201013093606631.jpeg
             * quantity : 0
             * price : 7
             * activityTag :
             * activityName :
             * marketLabelList : [{"id":16,"title":"预售"},{"id":20,"title":"前100件减20元"}]
             */

            private int id;
            private String title;
            private String coverPath;
            private String quantity;
            private String price;
            private String activityTag;
            private String activityName;
            private List<MarketLabelListBean> marketLabelList;

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

            public String getCoverPath() {
                return coverPath;
            }

            public void setCoverPath(String coverPath) {
                this.coverPath = coverPath;
            }

            public String getQuantity() {
                return quantity;
            }

            public void setQuantity(String quantity) {
                this.quantity = quantity;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public String getActivityTag() {
                return activityTag;
            }

            public void setActivityTag(String activityTag) {
                this.activityTag = activityTag;
            }

            public String getActivityName() {
                return activityName;
            }

            public void setActivityName(String activityName) {
                this.activityName = activityName;
            }

            public List<MarketLabelListBean> getMarketLabelList() {
                return marketLabelList;
            }

            public void setMarketLabelList(List<MarketLabelListBean> marketLabelList) {
                this.marketLabelList = marketLabelList;
            }

            public static class MarketLabelListBean {
                /**
                 * id : 16
                 * title : 预售
                 */

                private int id;
                private String title;

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
            }
        }
    }
}
