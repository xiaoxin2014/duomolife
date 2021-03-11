package com.amkj.dmsh.homepage.bean;

import android.text.TextUtils;

import com.amkj.dmsh.base.BaseEntity;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

/**
 * Created by xiaoxin on 2019/4/12 0012
 * Version:v4.0.0
 * ClassDescription :新版首页Tab实体
 */
public class HomeCommonEntity extends BaseEntity {


    /**
     * goodsNavbarList : [{"color":"string","icon":"string","link":"string","name":"string","productInfoList":[{"categoryId":0,"count":0,"id":0,"integralPrice":0,"moneyPrice":"string","name":"string","picUrl":"string","saleSkuId":0,"skuName":"string"}],"showType":"string"}]
     * sysTime : string
     */

    private String sysTime;
    private List<HomeCommonBean> guidanceInfoList;
    private String bgColor = "";
    private String fontColor = "";

    public String getFontColor() {
        if (TextUtils.isEmpty(fontColor)) {
            return "";
        } else {
            return fontColor.startsWith("#") ? fontColor : "#" + fontColor;
        }
    }

    public void setFontColor(String fontColor) {
        this.fontColor = fontColor;
    }

    public String getBgColor() {
        if (TextUtils.isEmpty(bgColor)) {
            return "";
        } else {
            return bgColor.startsWith("#") ? bgColor : "#" + bgColor;
        }
    }

    public void setBgColor(String bgColor) {
        this.bgColor = bgColor;
    }

    public String getSysTime() {
        return sysTime;
    }

    public void setSysTime(String sysTime) {
        this.sysTime = sysTime;
    }

    public List<HomeCommonBean> getResult() {
        return guidanceInfoList;
    }

    public void setResult(List<HomeCommonBean> goodsNavbarList) {
        this.guidanceInfoList = goodsNavbarList;
    }

    public static class HomeCommonBean implements MultiItemEntity {
        /**
         * color : string
         * icon : string
         * link : string
         * name : string
         * productInfoList : [{"categoryId":0,"count":0,"id":0,"integralPrice":0,"moneyPrice":"string","name":"string","picUrl":"string","saleSkuId":0,"skuName":"string"}]
         * showType : string
         */

        private int id;
        private String color;
        private String icon;
        private String link;
        private String name;
        private String subtitle;
        private String showType;//1.文本   2.图标
        private String description;
        private List<ProductInfoListBean> productInfoList;
        private List<VideoInfoListBean> videoInfoList;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public List<ProductInfoListBean> getProductInfoList() {
            return productInfoList;
        }

        public void setProductInfoList(List<ProductInfoListBean> productInfoList) {
            this.productInfoList = productInfoList;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getShowType() {
            return showType;
        }

        public void setShowType(String showType) {
            this.showType = showType;
        }

        public String getSubtitle() {
            return subtitle;
        }

        public void setSubtitle(String subtitle) {
            this.subtitle = subtitle;
        }

        public List<VideoInfoListBean> getVideoInfoList() {
            return videoInfoList;
        }

        public void setVideoInfoList(List<VideoInfoListBean> videoInfoList) {
            this.videoInfoList = videoInfoList;
        }

        @Override
        public int getItemType() {
            return (videoInfoList != null && videoInfoList.size() > 0) ? 1 : 0;
        }

        public static class VideoInfoListBean {

            /**
             * id : 2
             * title : 测试2
             * coverPath : http://image.domolife.cn/platform/20210125/20210125174058988.jpeg
             */

            private String id;
            private String title;
            private String coverPath;

            public String getId() {
                return id;
            }

            public void setId(String id) {
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
        }
    }

    public class ProductInfoListBean {
        /**
         * img : http://image.domolife.cn/platform/20171218/20171218161441555.jpg
         * price : 59.00
         * marketPrice : 59.00
         */

        private String img;
        private String cover;
        private String price;
        private String marketPrice;

        public String getImg() {
            return img;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getMarketPrice() {
            return marketPrice;
        }

        public void setMarketPrice(String marketPrice) {
            this.marketPrice = marketPrice;
        }
    }
}
