package com.amkj.dmsh.dominant.bean;

import com.amkj.dmsh.base.BaseTimeEntity;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/6/6
 * class description:请输入类描述
 */

public class QualityGroupEntity2 extends BaseTimeEntity {

    /**
     * result : [{"gpCount":0,"gpPicUrl":"http://img.domolife.cn/platform/nDWisknfCA.jpg","gpSkuId":121,"propValueId":"25","productSkuValue":"颜色:白色","gpProductQuantity":12,"gpType":"2人团","price":399,"subtitle":null,"name":"韩国Ifam森林儿童玩具整理架构成1","gpPrice":12.3},{"gpCount":0,"gpPicUrl":"http://img.domolife.cn/platform/nDWisknfCA.jpg","gpEndTime":"2017-06-16 00:00:00","gpStartTime":"2017-06-01 00:00:00","gpSkuId":121,"propValueId":"25","productSkuValue":"颜色:白色","gpProductQuantity":12,"gpType":"2人团","price":399,"subtitle":null,"name":"韩国Ifam森林儿童玩具整理架构成1","gpPrice":12.3},{"gpCount":0,"gpPicUrl":"http://img.domolife.cn/platform/nDWisknfCA.jpg","gpEndTime":"2017-05-04 00:00:00","gpStartTime":"2017-05-01 00:00:00","gpSkuId":121,"propValueId":"25","productSkuValue":"颜色:白色","gpProductQuantity":12,"gpType":"2人团","price":399,"subtitle":null,"name":"韩国Ifam森林儿童玩具整理架构成1","gpPrice":12.3},{"gpCount":0,"gpPicUrl":"http://img.domolife.cn/platform/zE7p3MbArW.jpg","gpEndTime":"2017-06-08 10:17:57","gpStartTime":"2017-06-01 02:06:06","gpSkuId":127,"picUrl":"http://img.domolife.cn/platform/20170225/20170225203425782.jpg","propValueId":"34","productSkuValue":"颜色:米色","gpProductQuantity":123,"gpType":"2人团","price":260,"subtitle":null,"name":"韩国Ifam森林儿童边角架玩具整理架构成3","gpPrice":12.12},{"gpCount":0,"gpPicUrl":"http://img.domolife.cn/platform/nDWisknfCA.jpg","gpSkuId":121,"propValueId":"25","productSkuValue":"颜色:白色","gpProductQuantity":12,"gpType":"2人团","price":399,"subtitle":null,"name":"韩国Ifam森林儿童玩具整理架构成1","gpPrice":12.3},{"gpCount":1,"gpPicUrl":"http://img.domolife.cn/platform/nDWisknfCA.jpg","gpSkuId":121,"propValueId":"25","productSkuValue":"颜色:白色","gpProductQuantity":12,"gpType":"2人团","price":399,"subtitle":null,"name":"韩国Ifam森林儿童玩具整理架构成1","gpPrice":12.3},{"gpCount":0,"gpPicUrl":"http://img.domolife.cn/platform/nDWisknfCA.jpg","gpSkuId":121,"propValueId":"25","productSkuValue":"颜色:白色","gpProductQuantity":12,"gpType":"2人团","price":399,"subtitle":null,"name":"韩国Ifam森林儿童玩具整理架构成1","gpPrice":12.3},{"gpCount":0,"gpPicUrl":"http://img.domolife.cn/platform/nDWisknfCA.jpg","gpSkuId":121,"propValueId":"25","productSkuValue":"颜色:白色","gpProductQuantity":12,"gpType":"2人团","price":399,"subtitle":null,"name":"韩国Ifam森林儿童玩具整理架构成1","gpPrice":12.3},{"gpCount":0,"gpPicUrl":"http://img.domolife.cn/platform/nDWisknfCA.jpg","gpSkuId":121,"propValueId":"25","productSkuValue":"颜色:白色","gpProductQuantity":12,"gpType":"2人团","price":399,"subtitle":null,"name":"韩国Ifam森林儿童玩具整理架构成1","gpPrice":12.3},{"gpCount":0,"gpPicUrl":"http://img.domolife.cn/platform/nDWisknfCA.jpg","gpSkuId":121,"propValueId":"25","productSkuValue":"颜色:白色","gpProductQuantity":12,"gpType":"2人团","price":399,"subtitle":null,"name":"韩国Ifam森林儿童玩具整理架构成1","gpPrice":12.3},{"gpCount":12,"gpPicUrl":"http://img.domolife.cn/platform/nDWisknfCA.jpg","gpEndTime":"2017-06-16 00:00:00","gpStartTime":"2017-06-01 00:00:00","gpSkuId":121,"propValueId":"25","productSkuValue":"颜色:白色","gpProductQuantity":12,"gpType":"2人团","price":399,"subtitle":null,"name":"韩国Ifam森林儿童玩具整理架构成1","gpPrice":12.3}]
     * currentTime : 2017-06-09 18:30:56
     * msg : 请求成功
     * code : 01
     */

    @SerializedName("result")
    private List<QualityGroupBean> qualityGroupBeanList;

    public List<QualityGroupBean> getQualityGroupBeanList() {
        return qualityGroupBeanList;
    }

    public void setQualityGroupBeanList(List<QualityGroupBean> qualityGroupBeanList) {
        this.qualityGroupBeanList = qualityGroupBeanList;
    }

    public static class QualityGroupBean extends BaseTimeEntity implements MultiItemEntity {
        /**
         * gpCount : 0
         * gpPicUrl : http://img.domolife.cn/platform/nDWisknfCA.jpg
         * gpSkuId : 121
         * propValueId : 25
         * productSkuValue : 颜色:白色
         * gpProductQuantity : 12
         * gpType : 2人团
         * price : 399
         * subtitle : null
         * name : 韩国Ifam森林儿童玩具整理架构成1
         * gpPrice : 12.3
         * gpEndTime : 2017-06-16 00:00:00
         * gpStartTime : 2017-06-01 00:00:00
         * picUrl : http://img.domolife.cn/platform/20170225/20170225203425782.jpg
         */

        private String propValueId;
        private String productId;
        private String productSkuValue;
        private String price;
        private String subtitle;
        private String name;
        private String gpName;
        private String picUrl;
        private String images;
        private String coverImage;
        private String gpCount;
        private int gpSkuId;
        private int gpInfoId;
        private int gpProductQuantity;
        private String gpPrice;
        private String gpType;
        private String gpPicUrl;
        private String gpEndTime;
        private String gpStartTime;
        private int range;
        private long addSecond;
        private String typeTitle;
        private int itemType;


        public String getGpName() {
            return gpName;
        }

        public void setGpName(String gpName) {
            this.gpName = gpName;
        }

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public void setItemType(int itemType) {
            this.itemType = itemType;
        }

        public String getTypeTitle() {
            return typeTitle;
        }

        public void setTypeTitle(String typeTitle) {
            this.typeTitle = typeTitle;
        }

        public long getAddSecond() {
            return addSecond;
        }

        public void setAddSecond(long addSecond) {
            this.addSecond = addSecond;
        }

        public int getGpInfoId() {
            return gpInfoId;
        }

        public void setGpInfoId(int gpInfoId) {
            this.gpInfoId = gpInfoId;
        }

        public String getImages() {
            return images;
        }

        public void setImages(String images) {
            this.images = images;
        }

        public String getCoverImage() {
            return coverImage;
        }

        public void setCoverImage(String coverImage) {
            this.coverImage = coverImage;
        }

        public String getGpCount() {
            return gpCount;
        }

        public void setGpCount(String gpCount) {
            this.gpCount = gpCount;
        }

        public String getGpPicUrl() {
            return gpPicUrl;
        }

        public void setGpPicUrl(String gpPicUrl) {
            this.gpPicUrl = gpPicUrl;
        }

        public int getGpSkuId() {
            return gpSkuId;
        }

        public void setGpSkuId(int gpSkuId) {
            this.gpSkuId = gpSkuId;
        }

        public String getPropValueId() {
            return propValueId;
        }

        public void setPropValueId(String propValueId) {
            this.propValueId = propValueId;
        }

        public String getProductSkuValue() {
            return productSkuValue;
        }

        public void setProductSkuValue(String productSkuValue) {
            this.productSkuValue = productSkuValue;
        }

        public int getGpProductQuantity() {
            return gpProductQuantity;
        }

        public void setGpProductQuantity(int gpProductQuantity) {
            this.gpProductQuantity = gpProductQuantity;
        }

        public String getGpType() {
            return gpType;
        }

        public void setGpType(String gpType) {
            this.gpType = gpType;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getSubtitle() {
            return subtitle;
        }

        public void setSubtitle(String subtitle) {
            this.subtitle = subtitle;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getGpPrice() {
            return gpPrice;
        }

        public void setGpPrice(String gpPrice) {
            this.gpPrice = gpPrice;
        }

        public String getGpEndTime() {
            return gpEndTime;
        }

        public void setGpEndTime(String gpEndTime) {
            this.gpEndTime = gpEndTime;
        }

        public String getGpStartTime() {
            return gpStartTime;
        }

        public void setGpStartTime(String gpStartTime) {
            this.gpStartTime = gpStartTime;
        }

        public String getPicUrl() {
            return picUrl;
        }

        public void setPicUrl(String picUrl) {
            this.picUrl = picUrl;
        }

        public int getRange() {
            return range;
        }

        public void setRange(int range) {
            this.range = range;
        }

        @Override
        public int getItemType() {
            return itemType;
        }
    }
}
