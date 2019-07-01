package com.amkj.dmsh.shopdetails.bean;

import java.util.List;

/**
 * Created by xiaoxin on 2019/6/29
 * Version:v4.1.0
 * ClassDescription :组合商品加入购物车传入参数combines数据类型
 */
public class CombineBean {

    /**
     * productId : 123
     * skuId : 455
     * count : 2
     * activityCode : ZH23444
     * combineMatchs : [{"productId":23,"skuId":455},{"productId":333,"skuId":455555}]
     */

    private int productId;
    private int skuId;
    private int count;
    private String activityCode;
    private List<CombineMatchsBean> combineMatchs;

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getSkuId() {
        return skuId;
    }

    public void setSkuId(int skuId) {
        this.skuId = skuId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getActivityCode() {
        return activityCode;
    }

    public void setActivityCode(String activityCode) {
        this.activityCode = activityCode;
    }

    public List<CombineMatchsBean> getCombineMatchs() {
        return combineMatchs;
    }

    public void setCombineMatchs(List<CombineMatchsBean> combineMatchs) {
        this.combineMatchs = combineMatchs;
    }

    public static class CombineMatchsBean {
        /**
         * productId : 23
         * skuId : 455
         */

        private int productId;
        private int skuId;

        public int getProductId() {
            return productId;
        }

        public void setProductId(int productId) {
            this.productId = productId;
        }

        public int getSkuId() {
            return skuId;
        }

        public void setSkuId(int skuId) {
            this.skuId = skuId;
        }
    }
}
