package com.amkj.dmsh.mine.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.amkj.dmsh.base.BaseTimeEntity;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.shopdetails.bean.GroupGoodsEntity.GroupGoodsBean.CombineCommonBean;
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

            public static class CartInfoBean implements Parcelable {
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
                private SaleSkuBean saleSku;
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
                //是否加间距
                private int showLine;
                //存储活动内容
                private ActivityInfoBean activityInfoData;
                //是否被选中结算
                @SerializedName(value = "isSelected", alternate = "checked")
                private boolean isSelected;
                //是否被选中删除
                private boolean isDelete;
                //自定义属性
                private int cartId;
                //记录位置
                private int position;
                //是否是活动商品数组第一条(如果是，显示活动信息)
                private int showActInfo;
                //是否是组合搭配主商品
                private boolean isMainProduct;
                //是否是组合搭配的搭配商品
                private boolean isCombineProduct;
                //存储搭配商品
                private List<CartInfoBean> combineMatchProducts;
                //针对失效商品特殊字段
                private boolean isValid;

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
                    //setForSale(cartInfoBean.isForSale);
                    //setPriceTag(cartInfoBean.priceTag);s
                    //setIsMore(cartInfoBean.isMore);
                    //setStatus(cartInfoBean.getStatus());
                }

                public List<CartInfoBean> getCombineMatchProducts() {
                    return combineMatchProducts;
                }

                public void setCombineMatchProducts(List<CartInfoBean> combineMatchProducts) {
                    this.combineMatchProducts = combineMatchProducts;
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

                public int getShowActInfo() {
                    return showActInfo;
                }

                public void setShowActInfo(int showActInfo) {
                    this.showActInfo = showActInfo;
                }

                public int getPosition() {
                    return position;
                }

                public void setPosition(int position) {
                    this.position = position;
                }

                public CartInfoBean(CombineCommonBean combineCommonBean) {
                    setProductId(combineCommonBean.getProductId());
                    setId(combineCommonBean.getSkuId());
                    setCount(combineCommonBean.getCount());
                    setSaleSku(new SaleSkuBean(combineCommonBean.getQuantity(), combineCommonBean.getMinPrice(), combineCommonBean.getSkuId()));
                }

                public int getCartId() {
                    return cartId;
                }

                public void setCartId(int cartId) {
                    this.cartId = cartId;
                }

                public int getShowLine() {
                    return showLine;
                }

                public void setShowLine(int showLine) {
                    this.showLine = showLine;
                }

                public ActivityInfoBean getActivityInfoData() {
                    return activityInfoData;
                }

                public void setActivityInfoData(ActivityInfoBean activityInfoData) {
                    this.activityInfoData = activityInfoData;
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

                public SaleSkuBean getSaleSku() {
                    return saleSku;
                }

                public void setSaleSku(SaleSkuBean saleSku) {
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

                public static class SaleSkuBean implements Parcelable {
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

                    @Override
                    public int describeContents() {
                        return 0;
                    }

                    @Override
                    public void writeToParcel(Parcel dest, int flags) {
                        dest.writeInt(this.id);
                        dest.writeString(this.price);
                        dest.writeInt(this.quantity);
                        dest.writeString(this.prePrice);
                    }


                    protected SaleSkuBean(Parcel in) {
                        this.id = in.readInt();
                        this.price = in.readString();
                        this.quantity = in.readInt();
                        this.prePrice = in.readString();
                    }

                    public static final Creator<SaleSkuBean> CREATOR = new Creator<SaleSkuBean>() {
                        @Override
                        public SaleSkuBean createFromParcel(Parcel source) {
                            return new SaleSkuBean(source);
                        }

                        @Override
                        public SaleSkuBean[] newArray(int size) {
                            return new SaleSkuBean[size];
                        }
                    };
                }

                @Override
                public int describeContents() {
                    return 0;
                }

                @Override
                public void writeToParcel(Parcel dest, int flags) {
                    dest.writeByte(this.isForSale ? (byte) 1 : (byte) 0);
                    dest.writeInt(this.productId);
                    dest.writeParcelable(this.saleSku, flags);
                    dest.writeString(this.saleSkuValue);
                    dest.writeInt(this.count);
                    dest.writeString(this.priceTag);
                    dest.writeString(this.picUrl);
                    dest.writeString(this.name);
                    dest.writeByte(this.isMore ? (byte) 1 : (byte) 0);
                    dest.writeInt(this.id);
                    dest.writeString(this.activitypriceDesc);
                    dest.writeInt(this.status);
                    dest.writeByte(this.hasNewUserPrice ? (byte) 1 : (byte) 0);
                    dest.writeString(this.updated);
                    dest.writeInt(this.showLine);
                    dest.writeParcelable(this.activityInfoData, flags);
                    dest.writeByte(this.isSelected ? (byte) 1 : (byte) 0);
                    dest.writeByte(this.isDelete ? (byte) 1 : (byte) 0);
                    dest.writeInt(this.cartId);
                }

                public CartInfoBean() {
                }

                protected CartInfoBean(Parcel in) {
                    this.isForSale = in.readByte() != 0;
                    this.productId = in.readInt();
                    this.saleSku = in.readParcelable(SaleSkuBean.class.getClassLoader());
                    this.saleSkuValue = in.readString();
                    this.count = in.readInt();
                    this.priceTag = in.readString();
                    this.picUrl = in.readString();
                    this.name = in.readString();
                    this.isMore = in.readByte() != 0;
                    this.id = in.readInt();
                    this.activitypriceDesc = in.readString();
                    this.status = in.readInt();
                    this.hasNewUserPrice = in.readByte() != 0;
                    this.updated = in.readString();
                    this.showLine = in.readInt();
                    this.activityInfoData = in.readParcelable(ActivityInfoBean.class.getClassLoader());
                    this.isSelected = in.readByte() != 0;
                    this.isDelete = in.readByte() != 0;
                    this.cartId = in.readInt();
                }

                public static final Parcelable.Creator<CartInfoBean> CREATOR = new Parcelable.Creator<CartInfoBean>() {
                    @Override
                    public CartInfoBean createFromParcel(Parcel source) {
                        return new CartInfoBean(source);
                    }

                    @Override
                    public CartInfoBean[] newArray(int size) {
                        return new CartInfoBean[size];
                    }
                };
            }

            public static class ActivityInfoBean implements Parcelable {
                /**
                 * activityCode : LJ1507778006
                 * limitBuy : 2
                 * activityTag : 立减震撼来
                 * activityType : 2
                 * activityRule : 立减20.0元
                 */

                private String activityCode;
                private int limitBuy;
                private String activityTag;
                private int activityType;
                private String activityRule;
                private String preActivityRule;

                public String getPreActivityRule() {
                    return preActivityRule;
                }

                public void setPreActivityRule(String preActivityRule) {
                    this.preActivityRule = preActivityRule;
                }

                public String getActivityCode() {
                    return activityCode;
                }

                public void setActivityCode(String activityCode) {
                    this.activityCode = activityCode;
                }

                public int getLimitBuy() {
                    return limitBuy;
                }

                public void setLimitBuy(int limitBuy) {
                    this.limitBuy = limitBuy;
                }

                public String getActivityTag() {
                    return activityTag;
                }

                public void setActivityTag(String activityTag) {
                    this.activityTag = activityTag;
                }

                public int getActivityType() {
                    return activityType;
                }

                public void setActivityType(int activityType) {
                    this.activityType = activityType;
                }

                public String getActivityRule() {
                    return activityRule;
                }

                public void setActivityRule(String activityRule) {
                    this.activityRule = activityRule;
                }

                @Override
                public int describeContents() {
                    return 0;
                }

                @Override
                public void writeToParcel(Parcel dest, int flags) {
                    dest.writeString(this.activityCode);
                    dest.writeInt(this.limitBuy);
                    dest.writeString(this.activityTag);
                    dest.writeInt(this.activityType);
                    dest.writeString(this.activityRule);
                }

                public ActivityInfoBean() {
                }

                protected ActivityInfoBean(Parcel in) {
                    this.activityCode = in.readString();
                    this.limitBuy = in.readInt();
                    this.activityTag = in.readString();
                    this.activityType = in.readInt();
                    this.activityRule = in.readString();
                }

                public static final Creator<ActivityInfoBean> CREATOR = new Creator<ActivityInfoBean>() {
                    @Override
                    public ActivityInfoBean createFromParcel(Parcel source) {
                        return new ActivityInfoBean(source);
                    }

                    @Override
                    public ActivityInfoBean[] newArray(int size) {
                        return new ActivityInfoBean[size];
                    }
                };
            }
        }
    }
}
