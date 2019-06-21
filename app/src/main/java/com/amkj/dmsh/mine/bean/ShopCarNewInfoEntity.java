package com.amkj.dmsh.mine.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.amkj.dmsh.shopdetails.bean.GroupGoodsEntity.GroupGoodsBean.CombineCommonBean;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/10/17
 * class description:新版购物车
 */

public class ShopCarNewInfoEntity {

    /**
     * shopCarNewInfoList : [{"activityInfoBean":{"activityCode":"LJ1507778006","limitBuy":2,"activityTag":"立减震撼来","activityType":2,"activityRule":"立减20.0元"},"cartInfoBeanList":[{"isForSale":false,"productId":4328,"saleSku":{"quantity":72,"price":"46.00","id":236},"saleSkuValue":"容量:10KG","count":1,"priceTag":"","picUrl":"http://image.domolife.cn/platform/RyQfrMsKSK.jpg","totalQuantity":156,"price":"46.00","name":"爱丽思米桶滑盖式","isMore":true,"id":107467,"allowCoupon":0,"status":1},{"isForSale":false,"productId":4317,"saleSku":{"quantity":986,"price":"23.00","id":198},"saleSkuValue":"颜色:2L","count":1,"priceTag":"","picUrl":"http://image.domolife.cn/platform/xjRb5XsS3Y.jpg","totalQuantity":2950,"price":"23.00","name":"FaSoLa五谷杂粮密封罐谷物保鲜盒 ","isMore":true,"id":107466,"allowCoupon":0,"status":1}]}]
     * msg : 请求成功
     * code : 01
     * totalCount : 0
     * activityType : {"0":"满减","1":"折扣","2":"立减","3":"限时购","4":"满赠","5":"首单赠"}
     */

    private String msg;
    private String code;
    private int totalCount;
    private String totalProductPrice;
    private String totalProductDiscountPrice;
    @SerializedName("activityType")
    private Map<String, String> activityTypeMap;
    @SerializedName("result")
    private List<ShopCarNewInfoBean> shopCarNewInfoList;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public String getTotalProductDiscountPrice() {
        return totalProductDiscountPrice;
    }

    public void setTotalProductDiscountPrice(String totalProductDiscountPrice) {
        this.totalProductDiscountPrice = totalProductDiscountPrice;
    }

    public List<ShopCarNewInfoBean> getShopCarNewInfoList() {
        return shopCarNewInfoList;
    }

    public void setShopCarNewInfoList(List<ShopCarNewInfoBean> shopCarNewInfoList) {
        this.shopCarNewInfoList = shopCarNewInfoList;
    }

    public Map<String, String> getActivityTypeMap() {
        return activityTypeMap;
    }

    public void setActivityTypeMap(Map<String, String> activityTypeMap) {
        this.activityTypeMap = activityTypeMap;
    }

    public String getTotalProductPrice() {
        return totalProductPrice;
    }

    public void setTotalProductPrice(String totalProductPrice) {
        this.totalProductPrice = totalProductPrice;
    }

    public static class ShopCarNewInfoBean {
        /**
         * activityInfoBean : {"activityCode":"LJ1507778006","limitBuy":2,"activityTag":"立减震撼来","activityType":2,"activityRule":"立减20.0元"}
         * cartInfoBeanList : [{"isForSale":false,"productId":4328,"saleSku":{"quantity":72,"price":"46.00","id":236},"saleSkuValue":"容量:10KG","count":1,"priceTag":"","picUrl":"http://image.domolife.cn/platform/RyQfrMsKSK.jpg","totalQuantity":156,"price":"46.00","name":"爱丽思米桶滑盖式","isMore":true,"id":107467,"allowCoupon":0,"status":1},{"isForSale":false,"productId":4317,"saleSku":{"quantity":986,"price":"23.00","id":198},"saleSkuValue":"颜色:2L","count":1,"priceTag":"","picUrl":"http://image.domolife.cn/platform/xjRb5XsS3Y.jpg","totalQuantity":2950,"price":"23.00","name":"FaSoLa五谷杂粮密封罐谷物保鲜盒 ","isMore":true,"id":107466,"allowCoupon":0,"status":1}]
         */
        @SerializedName("activityInfo")
        private ActivityInfoBean activityInfoBean;
        @SerializedName("cartInfo")
        private List<CartInfoBean> cartInfoBeanList;

        public ActivityInfoBean getActivityInfoBean() {
            return activityInfoBean;
        }

        public void setActivityInfoBean(ActivityInfoBean activityInfoBean) {
            this.activityInfoBean = activityInfoBean;
        }

        public List<CartInfoBean> getCartInfoBeanList() {
            return cartInfoBeanList;
        }

        public void setCartInfoBeanList(List<CartInfoBean> cartInfoBeanList) {
            this.cartInfoBeanList = cartInfoBeanList;
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
            //            是否是第一条
            private int showActInfo;

            public int getShowActInfo() {
                return showActInfo;
            }

            public void setShowActInfo(int showActInfo) {
                this.showActInfo = showActInfo;
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

        public static class CartInfoBean implements Parcelable {
            /**
             * isForSale : false
             * productId : 4328
             * saleSku : {"quantity":72,"price":"46.00","id":236}
             * saleSkuValue : 容量:10KG
             * count : 1
             * priceTag :
             * picUrl : http://image.domolife.cn/platform/RyQfrMsKSK.jpg
             * totalQuantity : 156
             * price : 46.00
             * name : 爱丽思米桶滑盖式
             * isMore : true
             * id : 107467
             * allowCoupon : 0
             * status : 1
             */
//            待售（可以加入购物车，但是不能购买）
            private boolean isForSale;
            private int productId;
            private SaleSkuBean saleSku;
            private String saleSkuValue;
            @SerializedName("activitypriceDesc")
            private String activityPriceDesc;
            private int count;
            private String priceTag;
            private String picUrl;
            private int totalQuantity;
            private String price;
            private String name;
            private String activityCode;
            private boolean isMore;
            private int id;
            //        是否可用券
            private int allowCoupon;
            //是否失效（如果加入购物车时间过长就会失效，1为正常）
            private int status;
            //            是否加间距
            private int showLine;
            //            正确级
            private int currentPosition;
            //            上一级position
            private int parentPosition;
            private int currentPage;
            private ActivityInfoBean activityInfoBean;
            //            存储活动内容
            private ActivityInfoBean activityInfoData;
            //            组合状态
            private String combineParentId;
            //            赠品状态
            private String presentParentId;
            @SerializedName("combineProductInfo")
            private List<CartProductInfoBean> combineProductInfoList;
            @SerializedName("presentProductInfo")
            private List<CartProductInfoBean> presentProductInfoList;
            /**
             * 是否是编辑状态
             */
            private boolean isEditing;

            /**
             * 是否被选中
             */
            @SerializedName(value = "isSelected", alternate = "toDayProduct")
            private boolean isSelected;

            /**
             * 是否被选中
             */
            private boolean isDelete;

            /**
             * 自定义属性
             * cartId
             *
             * @return
             */
            private int cartId;

            public CartInfoBean(CombineCommonBean combineCommonBean) {
                setProductId(combineCommonBean.getProductId());
                setId(combineCommonBean.getSkuId());
                setCount(combineCommonBean.getCount());
                setSaleSku(new SaleSkuBean(combineCommonBean.getQuantity(), combineCommonBean.getMinPrice(), combineCommonBean.getSkuId()));
            }

            public int getCurrentPosition() {
                return currentPosition;
            }

            public void setCurrentPosition(int currentPosition) {
                this.currentPosition = currentPosition;
            }

            public boolean isDelete() {
                return isDelete;
            }

            public void setDelete(boolean delete) {
                isDelete = delete;
            }

            public int getParentPosition() {
                return parentPosition;
            }

            public void setParentPosition(int parentPosition) {
                this.parentPosition = parentPosition;
            }

            public int getCurrentPage() {
                return currentPage;
            }

            public void setCurrentPage(int currentPage) {
                this.currentPage = currentPage;
            }

            public String getActivityPriceDesc() {
                return activityPriceDesc;
            }

            public void setActivityPriceDesc(String activityPriceDesc) {
                this.activityPriceDesc = activityPriceDesc;
            }

            public ActivityInfoBean getActivityInfoBean() {
                return activityInfoBean;
            }

            public void setActivityInfoBean(ActivityInfoBean activityInfoBean) {
                this.activityInfoBean = activityInfoBean;
            }

            public String getCombineParentId() {
                return combineParentId;
            }

            public void setCombineParentId(String combineParentId) {
                this.combineParentId = combineParentId;
            }

            public String getPresentParentId() {
                return presentParentId;
            }

            public void setPresentParentId(String presentParentId) {
                this.presentParentId = presentParentId;
            }

            public List<CartProductInfoBean> getCombineProductInfoList() {
                return combineProductInfoList;
            }

            public void setCombineProductInfoList(List<CartProductInfoBean> combineProductInfoList) {
                this.combineProductInfoList = combineProductInfoList;
            }

            public List<CartProductInfoBean> getPresentProductInfoList() {
                return presentProductInfoList;
            }

            public void setPresentProductInfoList(List<CartProductInfoBean> presentProductInfoList) {
                this.presentProductInfoList = presentProductInfoList;
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

            public String getActivityCode() {
                return activityCode;
            }

            public void setActivityCode(String activityCode) {
                this.activityCode = activityCode;
            }

            public ActivityInfoBean getActivityInfoData() {
                return activityInfoData;
            }

            public void setActivityInfoData(ActivityInfoBean activityInfoData) {
                this.activityInfoData = activityInfoData;
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

            public int getTotalQuantity() {
                return totalQuantity;
            }

            public void setTotalQuantity(int totalQuantity) {
                this.totalQuantity = totalQuantity;
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

            public boolean isForSale() {
                return isForSale;
            }

            public void setForSale(boolean forSale) {
                isForSale = forSale;
            }

            public boolean isMore() {
                return isMore;
            }

            public void setMore(boolean more) {
                isMore = more;
            }

            public boolean isEditing() {
                return isEditing;
            }

            public void setEditing(boolean editing) {
                isEditing = editing;
            }

            public boolean isSelected() {
                return isSelected;
            }

            public void setSelected(boolean selected) {
                isSelected = selected;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getAllowCoupon() {
                return allowCoupon;
            }

            public void setAllowCoupon(int allowCoupon) {
                this.allowCoupon = allowCoupon;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public static class SaleSkuBean implements Parcelable {
                /**
                 * quantity : 72
                 * price : 46.00
                 * id : 236
                 */

                private int quantity;
                private String price;
                private int id;

                public SaleSkuBean() {
                }

                public SaleSkuBean(int quantity, String price, int id) {
                    this.quantity = quantity;
                    this.price = price;
                    this.id = id;
                }

                public int getQuantity() {
                    return quantity;
                }

                public void setQuantity(int quantity) {
                    this.quantity = quantity;
                }

                public String getPrice() {
                    return price;
                }

                public void setPrice(String price) {
                    this.price = price;
                }

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                @Override
                public int describeContents() {
                    return 0;
                }

                @Override
                public void writeToParcel(Parcel dest, int flags) {
                    dest.writeInt(this.quantity);
                    dest.writeString(this.price);
                    dest.writeInt(this.id);
                }


                protected SaleSkuBean(Parcel in) {
                    this.quantity = in.readInt();
                    this.price = in.readString();
                    this.id = in.readInt();
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

            public CartInfoBean() {
            }

            public static class CartProductInfoBean implements MultiItemEntity, Parcelable {
                /**
                 * isPresentProduct:true
                 * activityCode : null
                 * picUrl : http://img.domolife.cn/platform/20170308/20170308163625953.jpg
                 * productId : 5945
                 * imgId : 68312
                 * price : 100.00
                 * saleSkuValue : 默认：默认
                 * name : 水彩植物 可爱文艺iPhone6\6s\6p\6sp\7\7p手机壳
                 * count : 2
                 * newPrice : 100.00
                 * saleSkuId : 725
                 */

                private boolean isPresentProduct;
                private String activityCode;
                private String picUrl;
                private int productId;
                private String imgId;
                private String price;
                private String saleSkuValue;
                private String name;
                private int count;
                private String newPrice;
                private String saleSkuId;
                //                组合优惠
                private String combineDecreasePrice;
                /**
                 * 订单变量
                 */
//                订单状态
                private int status;
                //                订单类型
                private String indentType;
                private String orderNo;
                private int orderProductId;
                private int orderRefundProductId;
                private int id;
                /**
                 * 1为订单状态 0
                 */
                private int itemType;

                public String getCombineDecreasePrice() {
                    return combineDecreasePrice;
                }

                public void setCombineDecreasePrice(String combineDecreasePrice) {
                    this.combineDecreasePrice = combineDecreasePrice;
                }

                public boolean isPresentProduct() {
                    return isPresentProduct;
                }

                public void setPresentProduct(boolean presentProduct) {
                    isPresentProduct = presentProduct;
                }

                public String getIndentType() {
                    return indentType;
                }

                public void setIndentType(String indentType) {
                    this.indentType = indentType;
                }

                public String getActivityCode() {
                    return activityCode;
                }

                public void setActivityCode(String activityCode) {
                    this.activityCode = activityCode;
                }

                public String getPicUrl() {
                    return picUrl;
                }

                public void setPicUrl(String picUrl) {
                    this.picUrl = picUrl;
                }

                public int getProductId() {
                    return productId;
                }

                public void setProductId(int productId) {
                    this.productId = productId;
                }

                public String getImgId() {
                    return imgId;
                }

                public void setImgId(String imgId) {
                    this.imgId = imgId;
                }

                public String getPrice() {
                    return price;
                }

                public void setPrice(String price) {
                    this.price = price;
                }

                public String getSaleSkuValue() {
                    return saleSkuValue;
                }

                public void setSaleSkuValue(String saleSkuValue) {
                    this.saleSkuValue = saleSkuValue;
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

                public String getNewPrice() {
                    return newPrice;
                }

                public void setNewPrice(String newPrice) {
                    this.newPrice = newPrice;
                }

                public String getSaleSkuId() {
                    return saleSkuId;
                }

                public void setSaleSkuId(String saleSkuId) {
                    this.saleSkuId = saleSkuId;
                }

                @Override
                public int getItemType() {
                    return itemType;
                }

                public void setItemType(int itemType) {
                    this.itemType = itemType;
                }

                public int getStatus() {
                    return status;
                }

                public void setStatus(int status) {
                    this.status = status;
                }

                public String getOrderNo() {
                    return orderNo;
                }

                public void setOrderNo(String orderNo) {
                    this.orderNo = orderNo;
                }

                public int getOrderProductId() {
                    return orderProductId;
                }

                public void setOrderProductId(int orderProductId) {
                    this.orderProductId = orderProductId;
                }

                public int getOrderRefundProductId() {
                    return orderRefundProductId;
                }

                public void setOrderRefundProductId(int orderRefundProductId) {
                    this.orderRefundProductId = orderRefundProductId;
                }

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public CartProductInfoBean() {
                }

                @Override
                public int describeContents() {
                    return 0;
                }

                @Override
                public void writeToParcel(Parcel dest, int flags) {
                    dest.writeByte(this.isPresentProduct ? (byte) 1 : (byte) 0);
                    dest.writeString(this.activityCode);
                    dest.writeString(this.picUrl);
                    dest.writeInt(this.productId);
                    dest.writeString(this.imgId);
                    dest.writeString(this.price);
                    dest.writeString(this.saleSkuValue);
                    dest.writeString(this.name);
                    dest.writeInt(this.count);
                    dest.writeString(this.newPrice);
                    dest.writeString(this.saleSkuId);
                    dest.writeString(this.combineDecreasePrice);
                    dest.writeInt(this.status);
                    dest.writeString(this.indentType);
                    dest.writeString(this.orderNo);
                    dest.writeInt(this.orderProductId);
                    dest.writeInt(this.orderRefundProductId);
                    dest.writeInt(this.id);
                    dest.writeInt(this.itemType);
                }

                protected CartProductInfoBean(Parcel in) {
                    this.isPresentProduct = in.readByte() != 0;
                    this.activityCode = in.readString();
                    this.picUrl = in.readString();
                    this.productId = in.readInt();
                    this.imgId = in.readString();
                    this.price = in.readString();
                    this.saleSkuValue = in.readString();
                    this.name = in.readString();
                    this.count = in.readInt();
                    this.newPrice = in.readString();
                    this.saleSkuId = in.readString();
                    this.combineDecreasePrice = in.readString();
                    this.status = in.readInt();
                    this.indentType = in.readString();
                    this.orderNo = in.readString();
                    this.orderProductId = in.readInt();
                    this.orderRefundProductId = in.readInt();
                    this.id = in.readInt();
                    this.itemType = in.readInt();
                }

                public static final Creator<CartProductInfoBean> CREATOR = new Creator<CartProductInfoBean>() {
                    @Override
                    public CartProductInfoBean createFromParcel(Parcel source) {
                        return new CartProductInfoBean(source);
                    }

                    @Override
                    public CartProductInfoBean[] newArray(int size) {
                        return new CartProductInfoBean[size];
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
                dest.writeString(this.activityPriceDesc);
                dest.writeInt(this.count);
                dest.writeString(this.priceTag);
                dest.writeString(this.picUrl);
                dest.writeInt(this.totalQuantity);
                dest.writeString(this.price);
                dest.writeString(this.name);
                dest.writeString(this.activityCode);
                dest.writeByte(this.isMore ? (byte) 1 : (byte) 0);
                dest.writeInt(this.id);
                dest.writeInt(this.allowCoupon);
                dest.writeInt(this.status);
                dest.writeInt(this.showLine);
                dest.writeInt(this.currentPosition);
                dest.writeInt(this.parentPosition);
                dest.writeInt(this.currentPage);
                dest.writeParcelable(this.activityInfoBean, flags);
                dest.writeParcelable(this.activityInfoData, flags);
                dest.writeString(this.combineParentId);
                dest.writeString(this.presentParentId);
                dest.writeTypedList(this.combineProductInfoList);
                dest.writeTypedList(this.presentProductInfoList);
                dest.writeByte(this.isEditing ? (byte) 1 : (byte) 0);
                dest.writeByte(this.isSelected ? (byte) 1 : (byte) 0);
                dest.writeByte(this.isDelete ? (byte) 1 : (byte) 0);
                dest.writeInt(this.cartId);
            }

            protected CartInfoBean(Parcel in) {
                this.isForSale = in.readByte() != 0;
                this.productId = in.readInt();
                this.saleSku = in.readParcelable(SaleSkuBean.class.getClassLoader());
                this.saleSkuValue = in.readString();
                this.activityPriceDesc = in.readString();
                this.count = in.readInt();
                this.priceTag = in.readString();
                this.picUrl = in.readString();
                this.totalQuantity = in.readInt();
                this.price = in.readString();
                this.name = in.readString();
                this.activityCode = in.readString();
                this.isMore = in.readByte() != 0;
                this.id = in.readInt();
                this.allowCoupon = in.readInt();
                this.status = in.readInt();
                this.showLine = in.readInt();
                this.currentPosition = in.readInt();
                this.parentPosition = in.readInt();
                this.currentPage = in.readInt();
                this.activityInfoBean = in.readParcelable(ActivityInfoBean.class.getClassLoader());
                this.activityInfoData = in.readParcelable(ActivityInfoBean.class.getClassLoader());
                this.combineParentId = in.readString();
                this.presentParentId = in.readString();
                this.combineProductInfoList = in.createTypedArrayList(CartProductInfoBean.CREATOR);
                this.presentProductInfoList = in.createTypedArrayList(CartProductInfoBean.CREATOR);
                this.isEditing = in.readByte() != 0;
                this.isSelected = in.readByte() != 0;
                this.isDelete = in.readByte() != 0;
                this.cartId = in.readInt();
            }

            public static final Creator<CartInfoBean> CREATOR = new Creator<CartInfoBean>() {
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
    }
}
