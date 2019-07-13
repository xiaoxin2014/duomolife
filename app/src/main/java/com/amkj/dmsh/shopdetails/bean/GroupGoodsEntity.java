package com.amkj.dmsh.shopdetails.bean;

import com.amkj.dmsh.base.BaseEntity;
import com.amkj.dmsh.constant.ConstantMethod;

import java.util.List;

import static com.amkj.dmsh.constant.ConstantMethod.stripTrailingZeros;

/**
 * Created by xiaoxin on 2019/6/17
 * Version:v4.1.0
 * ClassDescription :组合商品实体类
 */
public class GroupGoodsEntity extends BaseEntity {

    /**
     * sysTime : 2019-06-17 15:18:34
     * result : {"activityCode":"ZH1560571194","combineMatchProductList":[{"productId":17996,"picUrl":"http://image.domolife.cn/platform/YfcZPBryat1552374323505.jpg","tag":"最多可省399","minPrice":"50","maxPrice":"50","name":"意大利ACQUA ALLE ROSE古老玫瑰水300ml  组合装","skuSale":null,"propvalues":null,"props":null},{"productId":17997,"picUrl":"http://image.domolife.cn/platform/BS8fN6JQZa1552387295826.jpg","tag":"最多可省613","minPrice":"50","maxPrice":"50","name":"澳大利亚MacyMccoyBling 钻扣开衫","skuSale":null,"propvalues":null,"props":null},{"productId":17998,"picUrl":"http://image.domolife.cn/platform/3pNZyWDJHH1552388107934.jpg","tag":"最多可省504","minPrice":"50","maxPrice":"50","name":"澳大利亚MacyMccoyBling针织吊带","skuSale":null,"propvalues":null,"props":null},{"productId":17999,"picUrl":"http://image.domolife.cn/platform/8kr2YpYjF41552388989864.jpg","tag":"最多可省528","minPrice":"50","maxPrice":"50","name":"澳大利亚MacyMccoy渐变色金边纱裙 ","skuSale":null,"propvalues":null,"props":null},{"productId":18000,"picUrl":"http://image.domolife.cn/platform/Tw3ErRbP5f1552389373263.jpg","tag":"最多可省868","minPrice":"50","maxPrice":"50","name":"澳大利亚MacyMccoy彩虹Bling针织套装","skuSale":null,"propvalues":null,"props":null}],"combineMainProduct":{"productId":18001,"picUrl":"http://image.domolife.cn/platform/cSwDckpSD41552389592072.jpg","tag":null,"minPrice":"100","maxPrice":"100","name":"澳大利亚MacyMccoy翻边牛仔背带裤","skuSale":null,"propvalues":null,"props":null}}
     */

    private String sysTime;
    private GroupGoodsBean result;

    public String getSysTime() {
        return sysTime;
    }

    public void setSysTime(String sysTime) {
        this.sysTime = sysTime;
    }

    public GroupGoodsBean getResult() {
        return result;
    }

    public void setResult(GroupGoodsBean result) {
        this.result = result;
    }

    public static class GroupGoodsBean {
        /**
         * activityCode : ZH1560571194
         * combineMatchProductList : [{"productId":17996,"picUrl":"http://image.domolife.cn/platform/YfcZPBryat1552374323505.jpg","tag":"最多可省399","minPrice":"50","maxPrice":"50","name":"意大利ACQUA ALLE ROSE古老玫瑰水300ml  组合装","skuSale":null,"propvalues":null,"props":null},{"productId":17997,"picUrl":"http://image.domolife.cn/platform/BS8fN6JQZa1552387295826.jpg","tag":"最多可省613","minPrice":"50","maxPrice":"50","name":"澳大利亚MacyMccoyBling 钻扣开衫","skuSale":null,"propvalues":null,"props":null},{"productId":17998,"picUrl":"http://image.domolife.cn/platform/3pNZyWDJHH1552388107934.jpg","tag":"最多可省504","minPrice":"50","maxPrice":"50","name":"澳大利亚MacyMccoyBling针织吊带","skuSale":null,"propvalues":null,"props":null},{"productId":17999,"picUrl":"http://image.domolife.cn/platform/8kr2YpYjF41552388989864.jpg","tag":"最多可省528","minPrice":"50","maxPrice":"50","name":"澳大利亚MacyMccoy渐变色金边纱裙 ","skuSale":null,"propvalues":null,"props":null},{"productId":18000,"picUrl":"http://image.domolife.cn/platform/Tw3ErRbP5f1552389373263.jpg","tag":"最多可省868","minPrice":"50","maxPrice":"50","name":"澳大利亚MacyMccoy彩虹Bling针织套装","skuSale":null,"propvalues":null,"props":null}]
         * combineMainProduct : {"productId":18001,"picUrl":"http://image.domolife.cn/platform/cSwDckpSD41552389592072.jpg","tag":null,"minPrice":"100","maxPrice":"100","name":"澳大利亚MacyMccoy翻边牛仔背带裤","skuSale":null,"propvalues":null,"props":null}
         */

        private String activityCode;
        private CombineCommonBean combineMainProduct;
        private List<CombineCommonBean> combineMatchProductList;

        public String getActivityCode() {
            return activityCode;
        }

        public void setActivityCode(String activityCode) {
            this.activityCode = activityCode;
        }

        public CombineCommonBean getCombineMainProduct() {
            return combineMainProduct;
        }

        public void setCombineMainProduct(CombineCommonBean combineMainProduct) {
            this.combineMainProduct = combineMainProduct;
        }

        public List<CombineCommonBean> getCombineMatchProductList() {
            return combineMatchProductList;
        }

        public void setCombineMatchProductList(List<CombineCommonBean> combineMatchProductList) {
            this.combineMatchProductList = combineMatchProductList;
        }

        public static class CombineCommonBean implements Comparable<CombineCommonBean> {

            /**
             * productId : 17996
             * picUrl : http://image.domolife.cn/platform/YfcZPBryat1552374323505.jpg
             * tag : 最多可省399
             * minPrice : 50
             * maxPrice : 50
             * name : 意大利ACQUA ALLE ROSE古老玫瑰水300ml  组合装
             * skuSale : [{"id":10729,"productId":0,"price":"50.00","quantity":500,"propValues":"7487"}]
             * propvalues : [{"propValueId":7487,"propId":10,"propValueName":"3支装","propValueUrl":"http://image.domolife.cn/platform/EHjmS3JDYh1552374629011.jpg"}]
             * props : [{"propId":10,"propName":"规格","praentId":0}]
             */

            private int productId;
            private String picUrl;
            private String tag;
            private String minPrice;
            private String maxPrice;
            private String name;
            //所有sku商品总库存
            private int stock = -1;
            private List<SkuSaleBean> skuSale;
            private List<PropvaluesBean> propvalues;
            private List<PropsBean> props;
            //是否选中
            private boolean isSelected;
            //是否是主商品
            private boolean isMainProduct;
            //当前选中的skuId
            private int skuId;
            //当前选中的skuValue(例如：颜色：白色,尺码：L)
            private String skuValue;
            //要购买的商品件数(默认一件)
            private int count = 1;
            //当前选中的搭配商品省了多少钱
            private double saveMoney;
            //当前选中的搭配商品价格
            private String price;
            //存储主商品活动code
            private String activityCode;

            public String getActivityCode() {
                return activityCode;
            }

            public void setActivityCode(String activityCode) {
                this.activityCode = activityCode;
            }

            public String getPrice() {
                return stripTrailingZeros(price);
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public double getSaveMoney() {
                if (skuSale != null && skuSale.size() == 1) {
                    return ConstantMethod.getStringChangeDouble(skuSale.get(0).getPrePrice()) - ConstantMethod.getStringChangeDouble(skuSale.get(0).getPrice());
                }
                return saveMoney;
            }

            public void setSaveMoney(double saveMoney) {
                this.saveMoney = saveMoney;
            }

            public int getStock() {
                int quantity = 0;
                if (skuSale != null) {
                    for (int i = 0; i < skuSale.size(); i++) {
                        SkuSaleBean skuSaleBean = skuSale.get(i);
                        if (skuSaleBean != null) {
                            quantity = quantity + skuSaleBean.getQuantity();
                        }
                    }
                }

                return stock == -1 ? quantity : stock;
            }

            public void setStock(int stock) {
                this.stock = stock;
            }

            public int getSkuId() {
                if (skuSale != null && skuSale.size() == 1) {
                    return skuSale.get(0).getId();
                }
                return skuId;
            }

            public int getCount() {
                return count;
            }

            public void setCount(int count) {
                this.count = count;
            }

            public String getSkuValue() {
                return skuValue;
            }

            public void setSkuValue(String skuValue) {
                this.skuValue = skuValue;
            }

            public void setSkuId(int skuId) {
                this.skuId = skuId;
            }

            public boolean isMainProduct() {
                return isMainProduct;
            }

            public void setMainProduct(boolean mainProduct) {
                isMainProduct = mainProduct;
            }

            public boolean isSelected() {
                return isSelected;
            }

            public void setSelected(boolean selected) {
                isSelected = selected;
            }

            public int getProductId() {
                return productId;
            }

            public void setProductId(int productId) {
                this.productId = productId;
            }

            public String getPicUrl() {
                return picUrl;
            }

            public void setPicUrl(String picUrl) {
                this.picUrl = picUrl;
            }

            public String getTag() {
                return tag;
            }

            public void setTag(String tag) {
                this.tag = tag;
            }

            public String getMinPrice() {
                return stripTrailingZeros(minPrice);
            }

            public void setMinPrice(String minPrice) {
                this.minPrice = minPrice;
            }

            public String getMaxPrice() {
                return stripTrailingZeros(maxPrice);
            }

            public void setMaxPrice(String maxPrice) {
                this.maxPrice = maxPrice;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public List<SkuSaleBean> getSkuSale() {
                return skuSale;
            }

            public void setSkuSale(List<SkuSaleBean> skuSale) {
                this.skuSale = skuSale;
            }

            public List<PropvaluesBean> getPropvalues() {
                return propvalues;
            }

            public void setPropvalues(List<PropvaluesBean> propvalues) {
                this.propvalues = propvalues;
            }

            public List<PropsBean> getProps() {
                return props;
            }

            public void setProps(List<PropsBean> props) {
                this.props = props;
            }

            @Override
            public int compareTo(CombineCommonBean o) {
                return Double.compare(ConstantMethod.getStringChangeDouble(this.getMinPrice()), ConstantMethod.getStringChangeDouble(o.getMinPrice()));
            }
        }
    }
}
