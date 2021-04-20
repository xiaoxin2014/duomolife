package com.amkj.dmsh.find.activity;

import com.amkj.dmsh.base.BaseEntity;

/**
 * Created by xiaoxin on 2021/4/20
 * Version:v5.1.0
 */
public class EvaluateDetailEntity extends BaseEntity {

    /**
     * result : {"images":"http://image.domolife.cn/202104201120336612140110.jpg,http://image.domolife.cn/202104201120334862669493.jpg,http://image.domolife.cn/202104201120331671846293.jpg,http://image.domolife.cn/202104201120335632573886.jpg,http://image.domolife.cn/202104201120330208476445.jpg","star":5,"productId":18005,"skuValue":"颜色:米色,尺码:S","productImg":"http://image.domolife.cn/platform/5aQKsC7J371552390793319.jpg","content":"hhhhj","productName":"澳大利亚MacyMccoy撞色袖子打底衫"}
     * 01 : 01
     */

    private ResultBean result;


    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }


    public static class ResultBean {
        /**
         * images : http://image.domolife.cn/202104201120336612140110.jpg,http://image.domolife.cn/202104201120334862669493.jpg,http://image.domolife.cn/202104201120331671846293.jpg,http://image.domolife.cn/202104201120335632573886.jpg,http://image.domolife.cn/202104201120330208476445.jpg
         * star : 5
         * productId : 18005
         * skuValue : 颜色:米色,尺码:S
         * productImg : http://image.domolife.cn/platform/5aQKsC7J371552390793319.jpg
         * content : hhhhj
         * productName : 澳大利亚MacyMccoy撞色袖子打底衫
         */

        private String images;
        private int star;
        private int productId;
        private String skuValue;
        private String productImg;
        private String content;
        private String productName;

        public String getImages() {
            return images;
        }

        public void setImages(String images) {
            this.images = images;
        }

        public int getStar() {
            return star;
        }

        public void setStar(int star) {
            this.star = star;
        }

        public int getProductId() {
            return productId;
        }

        public void setProductId(int productId) {
            this.productId = productId;
        }

        public String getSkuValue() {
            return skuValue;
        }

        public void setSkuValue(String skuValue) {
            this.skuValue = skuValue;
        }

        public String getProductImg() {
            return productImg;
        }

        public void setProductImg(String productImg) {
            this.productImg = productImg;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }
    }
}
