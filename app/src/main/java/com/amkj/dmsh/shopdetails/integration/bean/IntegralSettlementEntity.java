package com.amkj.dmsh.shopdetails.integration.bean;


import com.amkj.dmsh.shopdetails.bean.PriceInfoBean;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/8/8
 * version 3.1.5
 * class description:积分结算
 */
public class IntegralSettlementEntity {

    /**
     * code : string
     * msg : string
     * result : {"priceInfo":[{"color":"string","name":"string","totalPrice":0,"totalPriceName":"string"}],"productInfo":[{"categoryId":0,"count":0,"id":0,"integralPrice":0,"moneyPrice":"string","name":"string","picUrl":"string","saleSkuId":0,"skuName":"string"}],"totalDeliveryPrice":0,"totalPrice":0}
     * sysTime : string
     */

    private String code;
    private String msg;
    @SerializedName("result")
    private IntegralSettlementBean integralSettlementBean;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public IntegralSettlementBean getIntegralSettlementBean() {
        return integralSettlementBean;
    }

    public void setIntegralSettlementBean(IntegralSettlementBean integralSettlementBean) {
        this.integralSettlementBean = integralSettlementBean;
    }

    public static class IntegralSettlementBean {
        /**
         * priceInfo : [{"color":"string","name":"string","totalPrice":0,"totalPriceName":"string"}]
         * productInfo : [{"categoryId":0,"count":0,"id":0,"integralPrice":0,"moneyPrice":"string","name":"string","picUrl":"string","saleSkuId":0,"skuName":"string"}]
         * totalDeliveryPrice : 0
         * totalPrice : 0
         */

        private float totalDeliveryPrice;
        private float totalPrice;
        private List<PriceInfoBean> priceInfo;
        private List<ProductInfoBean> productInfo;

        public float getTotalDeliveryPrice() {
            return totalDeliveryPrice;
        }

        public void setTotalDeliveryPrice(float totalDeliveryPrice) {
            this.totalDeliveryPrice = totalDeliveryPrice;
        }

        public float getTotalPrice() {
            return totalPrice;
        }

        public void setTotalPrice(float totalPrice) {
            this.totalPrice = totalPrice;
        }

        public List<PriceInfoBean> getPriceInfo() {
            return priceInfo;
        }

        public void setPriceInfo(List<PriceInfoBean> priceInfo) {
            this.priceInfo = priceInfo;
        }

        public List<ProductInfoBean> getProductInfo() {
            return productInfo;
        }

        public void setProductInfo(List<ProductInfoBean> productInfo) {
            this.productInfo = productInfo;
        }

        public static class ProductInfoBean {
            /**
             * categoryId : 0
             * count : 0
             * id : 0
             * integralPrice : 0
             * moneyPrice : string
             * name : string
             * picUrl : string
             * saleSkuId : 0
             * skuName : string
             */

            private int categoryId;
            private int count;
            private int id;
            private int integralPrice;
            private String moneyPrice;
            private String name;
            private String picUrl;
            private int saleSkuId;
            private String skuName;

            public int getCategoryId() {
                return categoryId;
            }

            public void setCategoryId(int categoryId) {
                this.categoryId = categoryId;
            }

            public int getCount() {
                return count;
            }

            public void setCount(int count) {
                this.count = count;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getIntegralPrice() {
                return integralPrice;
            }

            public void setIntegralPrice(int integralPrice) {
                this.integralPrice = integralPrice;
            }

            public String getMoneyPrice() {
                return moneyPrice;
            }

            public void setMoneyPrice(String moneyPrice) {
                this.moneyPrice = moneyPrice;
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

            public int getSaleSkuId() {
                return saleSkuId;
            }

            public void setSaleSkuId(int saleSkuId) {
                this.saleSkuId = saleSkuId;
            }

            public String getSkuName() {
                return skuName;
            }

            public void setSkuName(String skuName) {
                this.skuName = skuName;
            }
        }
    }
}
