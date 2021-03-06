package com.amkj.dmsh.mine.bean;

import com.amkj.dmsh.base.BaseTimeEntity;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.ConstantVariable;
import com.amkj.dmsh.shopdetails.bean.SkuSaleBean;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by xiaoxin on 2019/6/26
 * Version:v4.1.0
 * ClassDescription :新版购物车接口实体类
 */
public class ShopCarEntity extends BaseTimeEntity {
    private ShopCartBean result;

    public ShopCartBean getResult() {
        return result;
    }

    public void setResult(ShopCartBean result) {
        this.result = result;
    }

    public static class ShopCartBean {
        private String totalProductPrice;
        private String totalProductDiscountPrice;
        private int totalCount;
        private List<CartBean> carts;
        //失效商品集合（包含已下架和库存不足）
        private List<CartBean> rubbishCarts;


        public List<CartBean> getRubbishCarts() {
            return rubbishCarts;
        }

        public void setRubbishCarts(List<CartBean> rubbishCarts) {
            this.rubbishCarts = rubbishCarts;
        }

        public int getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(int totalCount) {
            this.totalCount = totalCount;
        }

        public double getTotalProductPrice() {
            return ConstantMethod.getStringChangeDouble(totalProductPrice);
        }

        public void setTotalProductPrice(String totalProductPrice) {
            this.totalProductPrice = totalProductPrice;
        }

        public double getTotalProductDiscountPrice() {
            return ConstantMethod.getStringChangeDouble(totalProductDiscountPrice);
        }

        public void setTotalProductDiscountPrice(String totalProductDiscountPrice) {
            this.totalProductDiscountPrice = totalProductDiscountPrice;
        }

        public List<CartBean> getCarts() {
            return carts;
        }

        public void setCarts(List<CartBean> carts) {
            this.carts = carts;
        }

        public static class CartBean {
            private ActivityInfoBean activityInfo;
            private String time;
            private CartInfoBean combineMainProduct;
            private List<CartInfoBean> combineMatchProducts;
            private List<CartInfoBean> cartInfo;

            public ActivityInfoBean getActivityInfo() {
                return activityInfo;
            }

            public void setActivityInfo(ActivityInfoBean activityInfo) {
                this.activityInfo = activityInfo;
            }

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }

            public CartInfoBean getCombineMainProduct() {
                return combineMainProduct;
            }

            public void setCombineMainProduct(CartInfoBean cartInfoBean) {
                this.combineMainProduct = cartInfoBean;
            }

            public List<CartInfoBean> getCombineMatchProducts() {
                return combineMatchProducts;
            }

            public void setCombineMatchProducts(List<CartInfoBean> combineMatchProducts) {
                this.combineMatchProducts = combineMatchProducts;
            }

            public List<CartInfoBean> getCartInfoList() {
                return cartInfo;
            }

            public void setCartInfoList(List<CartInfoBean> cartInfoList) {
                this.cartInfo = cartInfoList;
            }

            public static class CartInfoBean implements MultiItemEntity {

                /**
                 * isForSale : false
                 * productId : 17590
                 * saleSku : {"id":9853,"price":"59","quantity":80,"prePrice":"59"}
                 * saleSkuValue : 味道:经典花香 两盒
                 * count : 1
                 * priceTag :
                 * picUrl : http://image.domolife.cn/platform/Ka6rJEHFZK1547621607078.jpg
                 * name : 瑞士ORPHEA/奥菲雅 天然防虫香衣片 12+3片/盒
                 * isMore : true
                 * id : 842363
                 * activitypriceDesc :
                 * status : 1
                 * hasNewUserPrice : false
                 * updated : 2019-06-26 15:53:35
                 * checked : true
                 */

                private boolean isForSale;
                private int productId;
                private SkuSaleBean saleSku;
                private String saleSkuValue;
                private int count;
                private String priceTag;
                private String picUrl;
                private String name;
                private boolean isMore;
                //购物车id
                private int id;
                private String activitypriceDesc;
                //是否下架（不等于1表示已下架）
                private int status = 1;
                private boolean hasNewUserPrice;
                private String updated;
                //是否被选中结算
                @SerializedName(value = "isSelected", alternate = "checked")
                private boolean isSelected;
                //是否被选中删除
                private boolean isDelete;
                //是否是组合搭配主商品
                private boolean isMainProduct;
                //是否是组合搭配的搭配商品
                private boolean isCombineProduct;
                //针对失效商品特殊字段
                private boolean isValid;
                //是否是保税仓商品
                private boolean isEcm;

                public boolean isEcm() {
                    return isEcm;
                }

                public void setEcm(boolean ecm) {
                    isEcm = ecm;
                }

                public boolean isValid() {
                    return isValid;
                }

                public void setValid(boolean valid) {
                    isValid = valid;
                }

                //更新
                public void update(CartInfoBean cartInfoBean) {
                    setSaleSku(cartInfoBean.getSaleSku());
                    setSaleSkuValue(cartInfoBean.getSaleSkuValue());
                    setCount(cartInfoBean.getCount());
                    setPicUrl(cartInfoBean.getPicUrl());
                    setActivitypriceDesc(cartInfoBean.getActivityPriceDesc());
                    setHasNewUserPrice(cartInfoBean.isHasNewUserPrice());
                }


                public boolean isCombineProduct() {
                    return isCombineProduct;
                }

                public void setCombineProduct(boolean combineProduct) {
                    isCombineProduct = combineProduct;
                }

                public boolean isMainProduct() {
                    return isMainProduct;
                }

                public void setMainProduct(boolean mainProduct) {
                    isMainProduct = mainProduct;
                }

                public boolean isMore() {
                    return isMore;
                }

                public void setMore(boolean more) {
                    isMore = more;
                }

                public boolean isSelected() {
                    return isSelected;
                }

                public void setSelected(boolean selected) {
                    isSelected = selected;
                }

                public boolean isDelete() {
                    return isDelete;
                }

                public void setDelete(boolean delete) {
                    isDelete = delete;
                }

                public boolean isForSale() {
                    return isForSale;
                }

                public void setForSale(boolean forSale) {
                    isForSale = forSale;
                }

                public int getProductId() {
                    return productId;
                }

                public void setProductId(int productId) {
                    this.productId = productId;
                }

                public SkuSaleBean getSaleSku() {
                    return saleSku;
                }

                public void setSaleSku(SkuSaleBean saleSku) {
                    this.saleSku = saleSku;
                }

                public String getSaleSkuValue() {
                    return saleSkuValue;
                }

                public void setSaleSkuValue(String saleSkuValue) {
                    this.saleSkuValue = saleSkuValue;
                }

                public int getCount() {
                    return count;
                }

                public void setCount(int count) {
                    this.count = count;
                }

                public String getPriceTag() {
                    return priceTag;
                }

                public void setPriceTag(String priceTag) {
                    this.priceTag = priceTag;
                }

                public String getPicUrl() {
                    return picUrl;
                }

                public void setPicUrl(String picUrl) {
                    this.picUrl = picUrl;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public boolean isIsMore() {
                    return isMore;
                }

                public void setIsMore(boolean isMore) {
                    this.isMore = isMore;
                }

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public String getActivityPriceDesc() {
                    return activitypriceDesc;
                }

                public void setActivitypriceDesc(String activitypriceDesc) {
                    this.activitypriceDesc = activitypriceDesc;
                }

                public int getStatus() {
                    return status;
                }

                public void setStatus(int status) {
                    this.status = status;
                }

                public boolean isHasNewUserPrice() {
                    return hasNewUserPrice;
                }

                public void setHasNewUserPrice(boolean hasNewUserPrice) {
                    this.hasNewUserPrice = hasNewUserPrice;
                }

                public String getUpdated() {
                    return updated;
                }

                public void setUpdated(String updated) {
                    this.updated = updated;
                }

                @Override
                public int getItemType() {
                    return ConstantVariable.PRODUCT;
                }

                public static class SaleSkuBean {
                    /**
                     * id : 9853
                     * price : 59
                     * quantity : 80
                     * prePrice : 59
                     */

                    private int id;
                    private String price;
                    private int quantity;
                    private String prePrice;

                    public SaleSkuBean() {
                    }

                    public SaleSkuBean(int quantity, String price, int id) {
                        this.quantity = quantity;
                        this.price = price;
                        this.id = id;
                    }

                    public int getId() {
                        return id;
                    }

                    public void setId(int id) {
                        this.id = id;
                    }

                    public String getPrice() {
                        return price;
                    }

                    public void setPrice(String price) {
                        this.price = price;
                    }

                    public int getQuantity() {
                        return quantity;
                    }

                    public void setQuantity(int quantity) {
                        this.quantity = quantity;
                    }

                    public String getPrePrice() {
                        return prePrice;
                    }

                    public void setPrePrice(String prePrice) {
                        this.prePrice = prePrice;
                    }
                }
            }
        }
    }
}
