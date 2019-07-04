package com.amkj.dmsh.shopdetails.bean;

import com.amkj.dmsh.base.BaseTimeEntity;
import com.amkj.dmsh.mine.bean.ActivityInfoBean;

import java.util.List;

/**
 * Created by xiaoxin on 2019/7/4
 * Version:v4.1.0
 * ClassDescription :新版订单填写页面
 */
public class IndentWriteEntity extends BaseTimeEntity {

    /**
     * code : 01
     * msg : 请求成功
     * sysTime : 2019-07-04 09:28:58
     * result : {"realName":"","idCard":"","showIdCard":"","real":0,"prompt":"","allProductNotBuy":0,"priceInfos":[{"color":"#000000","totalPrice":"917","name":"商品总额","totalPriceName":"￥917"},{"color":"#000000","totalPrice":"372","name":"组合搭配优惠","totalPriceName":"-￥372"},{"color":"#000000","totalPrice":"25","name":"满200减25","totalPriceName":"-￥25"},{"color":"#FF0033","totalPrice":"397","name":"合计优惠","totalPriceName":"-￥397"},{"color":"#FF0033","totalPrice":"420","name":"实付","totalPriceName":"￥420"}],"products":[{"activityInfo":{"activityCode":"组合特价","activityTag":"组合商品","activityType":6,"activityRule":null,"activityStartTime":"2019-06-18 00:00:00","activityEndTime":"2019-07-06 00:00:00","allowCoupon":0},"productInfos":null,"combineMainProduct":{"id":18001,"combineMainId":51,"skuName":"颜色:蓝色,尺码:S","picUrl":"http://image.domolife.cn/platform/fkQ8HyEQaQ1552389678430.jpg","price":"100.00","prePrice":"424.00","name":"澳大利亚MacyMccoy翻边牛仔背带裤","count":1,"saleSkuId":10745},"combineMatchProducts":[{"id":17996,"combineMatchId":51,"skuName":"规格:2支装","picUrl":"http://image.domolife.cn/platform/C8ST7Zb7jH1552374483923.jpg","price":"30.00","prePrice":"78.00","name":"意大利ACQUA ALLE ROSE古老玫瑰水300ml  组合装","saleSkuId":10728}]},{"activityInfo":{"activityCode":"满100减10,满200减25","activityTag":"满减","activityType":0,"activityRule":null,"activityStartTime":"2019-06-01 00:00:00","activityEndTime":"2019-07-31 00:00:00","allowCoupon":1},"productInfos":[{"skuName":"颜色:藏青色,尺码:39/40","picUrl":"http://image.domolife.cn/platform/Hc7jMWhZzY1552475190095.JPG","price":"99.00","name":"科柔户外防滑拖鞋 男女款","count":1,"id":18024,"saleSkuId":10836,"activitypriceDesc":"","notBuyAreaIds":"","notBuyAreaInfo":null,"real":0,"allowCoupon":0},{"skuName":"规格:蜜桃米酒","picUrl":"http://image.domolife.cn/platform/mQB7tCxbiC1552550976687.jpg","price":"58.00","name":"苏州桥米想 米酒750ml","count":4,"id":18021,"saleSkuId":10894,"activitypriceDesc":"","notBuyAreaIds":"","notBuyAreaInfo":null,"real":0,"allowCoupon":1}],"combineMainProduct":null,"combineMatchProducts":null},{"activityInfo":null,"productInfos":[{"skuName":"规格:低脂牛奶 6支装","picUrl":"http://image.domolife.cn/platform/tfsZGEKrbR1551516964139.jpg","price":"84.00","name":"兰特脱脂低脂牛奶1L*6支装","count":1,"id":17871,"saleSkuId":10393,"activitypriceDesc":"","notBuyAreaIds":"","notBuyAreaInfo":null,"real":0,"allowCoupon":1}],"combineMainProduct":null,"combineMatchProducts":null}],"userCouponInfo":{"id":2030613,"title":"多么生活2018年终福利包","price":"100","msg":null}}
     */


    private IndentWriteBean result;

    public IndentWriteBean getIndentWriteBean() {
        return result;
    }

    public void setIndentWriteBean(IndentWriteBean result) {
        this.result = result;
    }

    public static class IndentWriteBean {
        /**
         * realName :
         * idCard :
         * showIdCard :
         * real : 0
         * prompt :
         * allProductNotBuy : 0
         * priceInfos : [{"color":"#000000","totalPrice":"917","name":"商品总额","totalPriceName":"￥917"},{"color":"#000000","totalPrice":"372","name":"组合搭配优惠","totalPriceName":"-￥372"},{"color":"#000000","totalPrice":"25","name":"满200减25","totalPriceName":"-￥25"},{"color":"#FF0033","totalPrice":"397","name":"合计优惠","totalPriceName":"-￥397"},{"color":"#FF0033","totalPrice":"420","name":"实付","totalPriceName":"￥420"}]
         * products : [{"activityInfo":{"activityCode":"组合特价","activityTag":"组合商品","activityType":6,"activityRule":null,"activityStartTime":"2019-06-18 00:00:00","activityEndTime":"2019-07-06 00:00:00","allowCoupon":0},"productInfos":null,"combineMainProduct":{"id":18001,"combineMainId":51,"skuName":"颜色:蓝色,尺码:S","picUrl":"http://image.domolife.cn/platform/fkQ8HyEQaQ1552389678430.jpg","price":"100.00","prePrice":"424.00","name":"澳大利亚MacyMccoy翻边牛仔背带裤","count":1,"saleSkuId":10745},"combineMatchProducts":[{"id":17996,"combineMatchId":51,"skuName":"规格:2支装","picUrl":"http://image.domolife.cn/platform/C8ST7Zb7jH1552374483923.jpg","price":"30.00","prePrice":"78.00","name":"意大利ACQUA ALLE ROSE古老玫瑰水300ml  组合装","saleSkuId":10728}]},{"activityInfo":{"activityCode":"满100减10,满200减25","activityTag":"满减","activityType":0,"activityRule":null,"activityStartTime":"2019-06-01 00:00:00","activityEndTime":"2019-07-31 00:00:00","allowCoupon":1},"productInfos":[{"skuName":"颜色:藏青色,尺码:39/40","picUrl":"http://image.domolife.cn/platform/Hc7jMWhZzY1552475190095.JPG","price":"99.00","name":"科柔户外防滑拖鞋 男女款","count":1,"id":18024,"saleSkuId":10836,"activitypriceDesc":"","notBuyAreaIds":"","notBuyAreaInfo":null,"real":0,"allowCoupon":0},{"skuName":"规格:蜜桃米酒","picUrl":"http://image.domolife.cn/platform/mQB7tCxbiC1552550976687.jpg","price":"58.00","name":"苏州桥米想 米酒750ml","count":4,"id":18021,"saleSkuId":10894,"activitypriceDesc":"","notBuyAreaIds":"","notBuyAreaInfo":null,"real":0,"allowCoupon":1}],"combineMainProduct":null,"combineMatchProducts":null},{"activityInfo":null,"productInfos":[{"skuName":"规格:低脂牛奶 6支装","picUrl":"http://image.domolife.cn/platform/tfsZGEKrbR1551516964139.jpg","price":"84.00","name":"兰特脱脂低脂牛奶1L*6支装","count":1,"id":17871,"saleSkuId":10393,"activitypriceDesc":"","notBuyAreaIds":"","notBuyAreaInfo":null,"real":0,"allowCoupon":1}],"combineMainProduct":null,"combineMatchProducts":null}]
         * userCouponInfo : {"id":2030613,"title":"多么生活2018年终福利包","price":"100","msg":null}
         */

        private String realName;
        private String idCard;
        private String showIdCard;
        private int real;
        private String prompt;
        private int allProductNotBuy;
        private UserCouponInfoBean userCouponInfo;
        private List<PriceInfoBean> priceInfos;
        private List<ProductsBean> products;

        public String getRealName() {
            return realName;
        }

        public void setRealName(String realName) {
            this.realName = realName;
        }

        public String getIdCard() {
            return idCard;
        }

        public void setIdCard(String idCard) {
            this.idCard = idCard;
        }

        public String getShowIdCard() {
            return showIdCard;
        }

        public void setShowIdCard(String showIdCard) {
            this.showIdCard = showIdCard;
        }

        public int getReal() {
            return real;
        }

        public void setReal(int real) {
            this.real = real;
        }

        public String getPrompt() {
            return prompt;
        }

        public void setPrompt(String prompt) {
            this.prompt = prompt;
        }

        public int getAllProductNotBuy() {
            return allProductNotBuy;
        }

        public void setAllProductNotBuy(int allProductNotBuy) {
            this.allProductNotBuy = allProductNotBuy;
        }

        public UserCouponInfoBean getUserCouponInfo() {
            return userCouponInfo;
        }

        public void setUserCouponInfo(UserCouponInfoBean userCouponInfo) {
            this.userCouponInfo = userCouponInfo;
        }

        public List<PriceInfoBean> getPriceInfos() {
            return priceInfos;
        }

        public void setPriceInfos(List<PriceInfoBean> priceInfos) {
            this.priceInfos = priceInfos;
        }

        public List<ProductsBean> getProducts() {
            return products;
        }

        public void setProducts(List<ProductsBean> products) {
            this.products = products;
        }

        public static class UserCouponInfoBean {
            /**
             * id : 2030613
             * title : 多么生活2018年终福利包
             * price : 100
             * msg : null
             */

            private int id;
            private String title;
            private String price;
            private String msg;

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

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public String getMsg() {
                return msg;
            }

            public void setMsg(String msg) {
                this.msg = msg;
            }
        }


        public static class ProductsBean {
            /**
             * activityInfo : {"activityCode":"组合特价","activityTag":"组合商品","activityType":6,"activityRule":null,"activityStartTime":"2019-06-18 00:00:00","activityEndTime":"2019-07-06 00:00:00","allowCoupon":0}
             * productInfos : null
             * combineMainProduct : {"id":18001,"combineMainId":51,"skuName":"颜色:蓝色,尺码:S","picUrl":"http://image.domolife.cn/platform/fkQ8HyEQaQ1552389678430.jpg","price":"100.00","prePrice":"424.00","name":"澳大利亚MacyMccoy翻边牛仔背带裤","count":1,"saleSkuId":10745}
             * combineMatchProducts : [{"id":17996,"combineMatchId":51,"skuName":"规格:2支装","picUrl":"http://image.domolife.cn/platform/C8ST7Zb7jH1552374483923.jpg","price":"30.00","prePrice":"78.00","name":"意大利ACQUA ALLE ROSE古老玫瑰水300ml  组合装","saleSkuId":10728}]
             */

            private ActivityInfoBean activityInfo;
            private List<ProductInfoBean> productInfos;
            private CombineMainProductBean combineMainProduct;
            private List<CombineMatchProductsBean> combineMatchProducts;

            public ActivityInfoBean getActivityInfo() {
                return activityInfo;
            }

            public void setActivityInfo(ActivityInfoBean activityInfo) {
                this.activityInfo = activityInfo;
            }

            public List<ProductInfoBean> getProductInfos() {
                return productInfos;
            }

            public void setProductInfos(List<ProductInfoBean> productInfos) {
                this.productInfos = productInfos;
            }

            public CombineMainProductBean getCombineMainProduct() {
                return combineMainProduct;
            }

            public void setCombineMainProduct(CombineMainProductBean combineMainProduct) {
                this.combineMainProduct = combineMainProduct;
            }

            public List<CombineMatchProductsBean> getCombineMatchProducts() {
                return combineMatchProducts;
            }

            public void setCombineMatchProducts(List<CombineMatchProductsBean> combineMatchProducts) {
                this.combineMatchProducts = combineMatchProducts;
            }


            public static class CombineMainProductBean {
                /**
                 * id : 18001
                 * combineMainId : 51
                 * skuName : 颜色:蓝色,尺码:S
                 * picUrl : http://image.domolife.cn/platform/fkQ8HyEQaQ1552389678430.jpg
                 * price : 100.00
                 * prePrice : 424.00
                 * name : 澳大利亚MacyMccoy翻边牛仔背带裤
                 * count : 1
                 * saleSkuId : 10745
                 */

                private int id;
                private int combineMainId;
                private String skuName;
                private String picUrl;
                private String price;
                private String prePrice;
                private String name;
                private int count;
                private int saleSkuId;

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public int getCombineMainId() {
                    return combineMainId;
                }

                public void setCombineMainId(int combineMainId) {
                    this.combineMainId = combineMainId;
                }

                public String getSkuName() {
                    return skuName;
                }

                public void setSkuName(String skuName) {
                    this.skuName = skuName;
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

                public String getPrePrice() {
                    return prePrice;
                }

                public void setPrePrice(String prePrice) {
                    this.prePrice = prePrice;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public int getCount() {
                    return count;
                }

                public void setCount(int count) {
                    this.count = count;
                }

                public int getSaleSkuId() {
                    return saleSkuId;
                }

                public void setSaleSkuId(int saleSkuId) {
                    this.saleSkuId = saleSkuId;
                }
            }

            public static class CombineMatchProductsBean {
                /**
                 * id : 17996
                 * combineMatchId : 51
                 * skuName : 规格:2支装
                 * picUrl : http://image.domolife.cn/platform/C8ST7Zb7jH1552374483923.jpg
                 * price : 30.00
                 * prePrice : 78.00
                 * name : 意大利ACQUA ALLE ROSE古老玫瑰水300ml  组合装
                 * saleSkuId : 10728
                 */

                private int id;
                private int combineMatchId;
                private String skuName;
                private String picUrl;
                private String price;
                private String prePrice;
                private String name;
                private int saleSkuId;

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public int getCombineMatchId() {
                    return combineMatchId;
                }

                public void setCombineMatchId(int combineMatchId) {
                    this.combineMatchId = combineMatchId;
                }

                public String getSkuName() {
                    return skuName;
                }

                public void setSkuName(String skuName) {
                    this.skuName = skuName;
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

                public String getPrePrice() {
                    return prePrice;
                }

                public void setPrePrice(String prePrice) {
                    this.prePrice = prePrice;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public int getSaleSkuId() {
                    return saleSkuId;
                }

                public void setSaleSkuId(int saleSkuId) {
                    this.saleSkuId = saleSkuId;
                }
            }

            public class ProductInfoBean {

                /**
                 * skuName : 颜色:藏青色,尺码:39/40
                 * picUrl : http://image.domolife.cn/platform/Hc7jMWhZzY1552475190095.JPG
                 * price : 99.00
                 * name : 科柔户外防滑拖鞋 男女款
                 * count : 1
                 * id : 18024
                 * saleSkuId : 10836
                 * activitypriceDesc :
                 * notBuyAreaIds :
                 * notBuyAreaInfo : null
                 * real : 0
                 * allowCoupon : 0
                 */

                private String skuName;
                private String picUrl;
                private String price;
                private String name;
                private int count;
                private int id;
                private int saleSkuId;
                private String activitypriceDesc;
                private String notBuyAreaIds;
                private int notBuyAreaInfo;
                private int real;
                private int allowCoupon;
                private int showLine;
                private int showActInfo;
                private ActivityInfoBean activityInfoBean;

                public ActivityInfoBean getActivityInfoBean() {
                    return activityInfoBean;
                }

                public void setActivityInfoBean(ActivityInfoBean activityInfoBean) {
                    this.activityInfoBean = activityInfoBean;
                }

                public int getShowLine() {
                    return showLine;
                }

                public void setShowLine(int showLine) {
                    this.showLine = showLine;
                }

                public int getShowActInfo() {
                    return showActInfo;
                }

                public void setShowActInfo(int showActInfo) {
                    this.showActInfo = showActInfo;
                }

                public String getSkuName() {
                    return skuName;
                }

                public void setSkuName(String skuName) {
                    this.skuName = skuName;
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

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
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

                public int getSaleSkuId() {
                    return saleSkuId;
                }

                public void setSaleSkuId(int saleSkuId) {
                    this.saleSkuId = saleSkuId;
                }

                public String getActivitypriceDesc() {
                    return activitypriceDesc;
                }

                public void setActivitypriceDesc(String activitypriceDesc) {
                    this.activitypriceDesc = activitypriceDesc;
                }

                public String getNotBuyAreaIds() {
                    return notBuyAreaIds;
                }

                public void setNotBuyAreaIds(String notBuyAreaIds) {
                    this.notBuyAreaIds = notBuyAreaIds;
                }

                public int getNotBuyAreaInfo() {
                    return notBuyAreaInfo;
                }

                public void setNotBuyAreaInfo(int notBuyAreaInfo) {
                    this.notBuyAreaInfo = notBuyAreaInfo;
                }

                public int getReal() {
                    return real;
                }

                public void setReal(int real) {
                    this.real = real;
                }

                public int getAllowCoupon() {
                    return allowCoupon;
                }

                public void setAllowCoupon(int allowCoupon) {
                    this.allowCoupon = allowCoupon;
                }

            }
        }
    }
}
