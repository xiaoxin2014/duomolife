package com.amkj.dmsh.dominant.bean;

import com.amkj.dmsh.base.BaseTimeEntity;
import com.amkj.dmsh.constant.ConstantMethod;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/6/6
 * class description:请输入类描述
 */

public class QualityGroupEntity extends BaseTimeEntity {

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
         * productId : 17676
         * endTime : 2019-12-31 00:00:00
         * coverImage : http://image.domolife.cn/platform/20191215/20191215145304863.jpeg
         * gpInfoId : 546
         * gpName : 拼团名称
         * price : 118
         * gpPrice : 0.1
         * requireCount : 3
         * gpQuantity : 160
         * partakeCount : 15
         * gpTypeText : 邀新团
         */

        private int itemType;
        private String productId;
        private String endTime;
        private String startTime;
        private String coverImage;
        private String gpInfoId;
        private String gpName;
        private String price;
        private String gpPrice;
        private String requireCount;
        private String gpQuantity;
        private String partakeCount;
        private String gpTypeText;

        public void setItemType(int itemType) {
            this.itemType = itemType;
        }

        @Override
        public int getItemType() {
            return itemType;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getCoverImage() {
            return coverImage;
        }

        public void setCoverImage(String coverImage) {
            this.coverImage = coverImage;
        }

        public String getGpInfoId() {
            return gpInfoId;
        }

        public void setGpInfoId(String gpInfoId) {
            this.gpInfoId = gpInfoId;
        }

        public String getGpName() {
            return gpName;
        }

        public void setGpName(String gpName) {
            this.gpName = gpName;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getGpPrice() {
            return gpPrice;
        }

        public void setGpPrice(String gpPrice) {
            this.gpPrice = gpPrice;
        }

        public String getRequireCount() {
            return requireCount;
        }

        public void setRequireCount(String requireCount) {
            this.requireCount = requireCount;
        }

        public int getGpQuantity() {
            return ConstantMethod.getStringChangeIntegers(gpQuantity);
        }

        public void setGpQuantity(String gpQuantity) {
            this.gpQuantity = gpQuantity;
        }

        public String getPartakeCount() {
            return partakeCount;
        }

        public void setPartakeCount(String partakeCount) {
            this.partakeCount = partakeCount;
        }

        public String getGpTypeText() {
            return gpTypeText;
        }

        public void setGpTypeText(String gpTypeText) {
            this.gpTypeText = gpTypeText;
        }
    }
}
