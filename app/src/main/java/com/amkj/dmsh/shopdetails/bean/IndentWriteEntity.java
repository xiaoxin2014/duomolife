package com.amkj.dmsh.shopdetails.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.amkj.dmsh.base.BaseTimeEntity;
import com.amkj.dmsh.constant.ConstantMethod;
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
        private PrerogativeActivityInfo prerogativeActivityInfo;

        public PrerogativeActivityInfo getPurchaseBean() {
            return prerogativeActivityInfo;
        }

        public void setPrerogativeActivityInfo(PrerogativeActivityInfo prerogativeActivityInfo) {
            this.prerogativeActivityInfo = prerogativeActivityInfo;
        }

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

        public boolean isReal() {
            return real == 1;
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

            private Integer id;
            private String title;
            private String price;
            private String msg;
            private int allowCouoon;
            private String startFee;

            public String getStartFee() {
                return (!TextUtils.isEmpty(startFee) && !"0".equals(startFee)) ? "满¥" + startFee : "";
            }

            public void setStartFee(String startFee) {
                this.startFee = startFee;
            }

            public int getAllowCouoon() {
                return allowCouoon;
            }

            public void setAllowCouoon(int allowCouoon) {
                this.allowCouoon = allowCouoon;
            }

            public Integer getId() {
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
            private ProductInfoBean combineMainProduct;
            private List<ProductInfoBean> combineMatchProducts;

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

            public ProductInfoBean getCombineMainProduct() {
                return combineMainProduct;
            }

            public void setCombineMainProduct(ProductInfoBean combineMainProduct) {
                this.combineMainProduct = combineMainProduct;
            }

            public List<ProductInfoBean> getCombineMatchProducts() {
                return combineMatchProducts;
            }

            public void setCombineMatchProducts(List<ProductInfoBean> combineMatchProducts) {
                this.combineMatchProducts = combineMatchProducts;
            }

            public static class ProductInfoBean implements Parcelable {

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
                private String zhPrice;
                private String name;
                private int count;
                private int id;
                private int saleSkuId;
                private String activitypriceDesc;
                private String notBuyAreaIds;
                //该地区无法配送
                private String notBuyAreaInfo;
                private int real;
                private int allowCoupon;
                //赠品信息
                private PresentInfo presentInfo;
                //主商品id
                private int combineMainId;
                //搭配商品id
                private int combineMatchId;
                //购物车id
                private int cartId;

                //是否显示分割线
                private int showLine;
                //是够显示活动信息
                private int showActInfo;
                //存储活动信息
                private ActivityInfoBean activityInfoBean;

                //是否是加价购商品
                private int isPrerogative;

                public boolean isPrerogative() {
                    return isPrerogative==1;
                }

                public void setIsPrerogative(int isPrerogative) {
                    this.isPrerogative = isPrerogative;
                }

                public String getZhPrice() {
                    return zhPrice;
                }

                public void setZhPrice(String zhPrice) {
                    this.zhPrice = zhPrice;
                }

                public int getCartId() {
                    return cartId;
                }

                public void setCartId(int cartId) {
                    this.cartId = cartId;
                }

                public PresentInfo getPresentInfo() {
                    return presentInfo;
                }

                public void setPresentInfo(PresentInfo presentInfo) {
                    this.presentInfo = presentInfo;
                }

                public int getCombineMainId() {
                    return combineMainId;
                }

                public void setCombineMainId(int combineMainId) {
                    this.combineMainId = combineMainId;
                }

                public int getCombineMatchId() {
                    return combineMatchId;
                }

                public void setCombineMatchId(int combineMatchId) {
                    this.combineMatchId = combineMatchId;
                }

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

                public String getNotBuyAreaInfo() {
                    return notBuyAreaInfo;
                }

                public void setNotBuyAreaInfo(String notBuyAreaInfo) {
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


                public static class PresentInfo implements Parcelable {

                    /**
                     * id : 3
                     * name : 三生三世
                     * picUrl : http://image.domolife.cn/platform/20190621/20190621165811803.png
                     */

                    private int id;
                    private String name;
                    private String picUrl;

                    public int getId() {
                        return id;
                    }

                    public void setId(int id) {
                        this.id = id;
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

                    @Override
                    public int describeContents() {
                        return 0;
                    }

                    @Override
                    public void writeToParcel(Parcel dest, int flags) {
                        dest.writeInt(this.id);
                        dest.writeString(this.name);
                        dest.writeString(this.picUrl);
                    }

                    public PresentInfo() {
                    }

                    protected PresentInfo(Parcel in) {
                        this.id = in.readInt();
                        this.name = in.readString();
                        this.picUrl = in.readString();
                    }

                    public static final Creator<PresentInfo> CREATOR = new Creator<PresentInfo>() {
                        @Override
                        public PresentInfo createFromParcel(Parcel source) {
                            return new PresentInfo(source);
                        }

                        @Override
                        public PresentInfo[] newArray(int size) {
                            return new PresentInfo[size];
                        }
                    };
                }

                @Override
                public int describeContents() {
                    return 0;
                }

                @Override
                public void writeToParcel(Parcel dest, int flags) {
                    dest.writeString(this.skuName);
                    dest.writeString(this.picUrl);
                    dest.writeString(this.price);
                    dest.writeString(this.name);
                    dest.writeInt(this.count);
                    dest.writeInt(this.id);
                    dest.writeInt(this.saleSkuId);
                    dest.writeString(this.activitypriceDesc);
                    dest.writeString(this.notBuyAreaIds);
                    dest.writeString(this.notBuyAreaInfo);
                    dest.writeInt(this.real);
                    dest.writeInt(this.allowCoupon);
                    dest.writeParcelable(this.presentInfo, flags);
                    dest.writeInt(this.showLine);
                    dest.writeInt(this.showActInfo);
                    dest.writeParcelable(this.activityInfoBean, flags);
                    dest.writeInt(this.combineMainId);
                    dest.writeInt(this.combineMatchId);
                }

                public ProductInfoBean() {
                }

                protected ProductInfoBean(Parcel in) {
                    this.skuName = in.readString();
                    this.picUrl = in.readString();
                    this.price = in.readString();
                    this.name = in.readString();
                    this.count = in.readInt();
                    this.id = in.readInt();
                    this.saleSkuId = in.readInt();
                    this.activitypriceDesc = in.readString();
                    this.notBuyAreaIds = in.readString();
                    this.notBuyAreaInfo = in.readString();
                    this.real = in.readInt();
                    this.allowCoupon = in.readInt();
                    this.presentInfo = in.readParcelable(PresentInfo.class.getClassLoader());
                    this.showLine = in.readInt();
                    this.showActInfo = in.readInt();
                    this.activityInfoBean = in.readParcelable(ActivityInfoBean.class.getClassLoader());
                    this.combineMainId = in.readInt();
                    this.combineMatchId = in.readInt();
                }

                public static final Creator<ProductInfoBean> CREATOR = new Creator<ProductInfoBean>() {
                    @Override
                    public ProductInfoBean createFromParcel(Parcel source) {
                        return new ProductInfoBean(source);
                    }

                    @Override
                    public ProductInfoBean[] newArray(int size) {
                        return new ProductInfoBean[size];
                    }
                };
            }
        }

        public static class PrerogativeActivityInfo {
            private String activityText;
            private String activityCode;

            public String getActivityText() {
                return activityText;
            }

            public void setActivityText(String activityText) {
                this.activityText = activityText;
            }

            public String getActivityCode() {
                return activityCode;
            }

            public void setActivityCode(String activityCode) {
                this.activityCode = activityCode;
            }

            private List<GoodsListBean> goodsList;

            public List<GoodsListBean> getGoodsList() {
                return goodsList;
            }

            public void setGoodsList(List<GoodsListBean> goodsList) {
                this.goodsList = goodsList;
            }

            public static class GoodsListBean {
                /**
                 * productId : 7270
                 * productName : 正宗海鸭蛋广西北海红树湾咸鸭蛋 60g*8枚 标准蛋
                 * picUrl : http://image.domolife.cn/platform/20170516/20170516183411499.jpg
                 * price : 专享价：￥2元起
                 * discountPrice : 比单独购买少￥26元起
                 * skuSale : [{"id":"1217","price":"2","quantity":"5","propValues":"8237"}]
                 * props : [{"propId":"1","propName":"默认"}]
                 * propValues : [{"propId":"1","propValueId":"8237","propValueName":"默认","propValueUrl":""}]
                 */

                private String productId;
                private String productName;
                private String subTitle;
                private String picUrl;
                private String price;
                private String discountPrice;
                private List<SkuSaleBean> skuSale;
                private List<PropsBean> props;
                private List<PropvaluesBean> propValues;


                public String getSubTitle() {
                    return TextUtils.isEmpty(subTitle) ? "" : subTitle + " • ";
                }

                public void setSubTitle(String subTitle) {
                    this.subTitle = subTitle;
                }

                public int getProductId() {
                    return ConstantMethod.getStringChangeIntegers(productId);
                }

                public void setProductId(String productId) {
                    this.productId = productId;
                }

                public String getProductName() {
                    return productName;
                }

                public void setProductName(String productName) {
                    this.productName = productName;
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

                public String getDiscountPrice() {
                    return discountPrice;
                }

                public void setDiscountPrice(String discountPrice) {
                    this.discountPrice = discountPrice;
                }

                public List<SkuSaleBean> getSkuSale() {
                    return skuSale;
                }

                public void setSkuSale(List<SkuSaleBean> skuSale) {
                    this.skuSale = skuSale;
                }

                public List<PropsBean> getProps() {
                    return props;
                }

                public void setProps(List<PropsBean> props) {
                    this.props = props;
                }

                public List<PropvaluesBean> getPropValues() {
                    return propValues;
                }

                public void setPropValues(List<PropvaluesBean> propValues) {
                    this.propValues = propValues;
                }
            }
        }
    }
}
