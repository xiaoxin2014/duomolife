package com.amkj.dmsh.shopdetails.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.amkj.dmsh.constant.CommunalDetailBean;
import com.amkj.dmsh.mine.bean.ShopCarNewInfoEntity.ShopCarNewInfoBean.CartInfoBean.CartProductInfoBean;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/10/18
 * class description:请输入类描述
 */

public class IndentDiscountsEntity {

    /**
     * result : {"sumDiscountPrice":62,"priceInfo":[{"color":"#000000","totalPrice":725.6,"name":"商品总额","totalPriceName":"￥725.60"},{"color":"#000000","totalPrice":40,"name":"立减","totalPriceName":"-￥40.0"},{"color":"#000000","totalPrice":5,"name":"满30减5","totalPriceName":"-￥5.0"},{"color":"#000000","totalPrice":12,"name":"满2件8.0折","totalPriceName":"-￥12.0"},{"color":"#000000","totalPrice":0,"name":"运费","totalPriceName":"￥0.00"},{"color":"#FF0033","totalPrice":62,"name":"合计优惠","totalPriceName":"-￥62.00"},{"color":"#FF0033","totalPrice":663.6,"name":"实付：","totalPriceName":"￥663.60"}],"totalPrice":663.6,"userCouponInfo":{"amount":5,"create_time":"2017-10-17 20:03:48","product_coupon_id":75,"end_time":"2017-10-27 23:59:59","device_type":1,"merchant_id":"3c48836a3e8c4da3b4900e91675e1e6a","title":"shaw的测试优惠券","type":"活动","is_coupon_refund":0,"mode":1,"start_time":"2017-10-17 20:03:48","start_fee":10,"product_ids":"0","bgColor":"#FDF4F4","user_id":34650,"use_range":0,"modeBgColor":"#FFB9B8","id":957141,"status":0,"desc":"shaw的测试优惠券"},"totalDeliveryPrice":0,"productInfo":[{"activityProductInfo":[{"skuName":"颜色:粉色","activityCode":null,"picUrl":"http://image.domolife.cn/platform/2JTHNNpxNp1504497089096.jpg","imgId":105033,"price":"98.00","name":"【上新】日本寇吉特美臀坐垫","count":3,"id":9234,"newPrice":"98.00","saleSkuId":2341},{"skuName":"默认:默认","activityCode":null,"picUrl":"http://image.domolife.cn/platform/kiRdDmxfKR1504265432368.jpg","imgId":104902,"price":"28.00","name":"【独家定制】多么生活小清新随手杯","count":1,"id":9229,"newPrice":"28.00","saleSkuId":2339},{"skuName":"默认:默认","activityCode":null,"picUrl":"http://image.domolife.cn/platform/kiRdDmxfKR1504265432368.jpg","imgId":104902,"price":"28.00","name":"【独家定制】多么生活小清新随手杯","count":1,"id":9229,"newPrice":"28.00","saleSkuId":2339}],"activityInfo":{"activityCode":"MJ1507773048","activityRuleDetail":"1、双11爆炸来袭\r\n2、双11爆炸来袭\r\n3、双11爆炸来袭","activityTag":"满减","activityType":0,"activityRule":"满10.0减2.0;满20.0减4.0;满30.0减5.0;满200.0减8.0"}},{"activityInfo":{"activityCode":"MJ1507773048","activityRuleDetail":"1、双11爆炸来袭\r\n2、双11爆炸来袭\r\n3、双11爆炸来袭","activityTag":"满减","activityType":0,"activityRule":"满10.0减2.0;满20.0减4.0;满30.0减5.0;满200.0减8.0"},"activityProductInfo":[{"skuName":"颜色:四层","activityCode":"MJ1507773048","picUrl":"http://image.domolife.cn/platform/CR2nbhsJXc.jpg","imgId":54827,"price":"45.00","name":"FaSoLa冰箱冷冻饺子收纳盒","count":2,"id":4283,"newPrice":"45.00","saleSkuId":114},{"skuName":"颜色:四层","activityCode":"MJ1507773048","picUrl":"http://image.domolife.cn/platform/CR2nbhsJXc.jpg","imgId":54827,"price":"45.00","name":"FaSoLa冰箱冷冻饺子收纳盒","count":2,"id":4283,"newPrice":"45.00","saleSkuId":114}]},{"activityInfo":{"activityCode":"ZK1507788859","activityRuleDetail":"123","activityTag":"折扣","activityType":1,"activityRule":"满2件8.0折;满6件4.0折"},"activityProductInfo":[{"skuName":"颜色:薄款奶白色","activityCode":"ZK1507788859","picUrl":"http://image.domolife.cn/platform/Z4aT6cEhKA.jpg","imgId":55116,"price":"9.80","name":"FaSoLa粘贴式马桶垫马桶坐垫马桶贴","count":1,"id":4319,"newPrice":"9.80","saleSkuId":213},{"skuName":"颜色:薄款绿色","activityCode":"ZK1507788859","picUrl":"http://image.domolife.cn/platform/eFnAkeyw6j.jpg","imgId":55115,"price":"9.80","name":"FaSoLa粘贴式马桶垫马桶坐垫马桶贴","count":1,"id":4319,"newPrice":"9.80","saleSkuId":212},{"skuName":"颜色:蓝色","activityCode":"ZK1507788859","picUrl":"http://image.domolife.cn/platform/YeHFBhfA7C.jpg","imgId":55169,"price":"49.00","name":"FaSoLa厨房置物架碗碟沥水架","count":1,"id":4326,"newPrice":"49.00","saleSkuId":230}]},{"activityInfo":{"activityCode":"XSG1507789611","activityRuleDetail":"123","activityTag":"限时购","activityType":3,"activityRule":""},"activityProductInfo":[{"skuName":"颜色:黄色","activityCode":"XSG1507789611","picUrl":"http://image.domolife.cn/platform/JeeYeXfFPa.jpg","imgId":55374,"price":"9.00","name":"日本FaSoLa 抗菌菜板","count":3,"id":4352,"newPrice":"9.00","saleSkuId":283},{"skuName":"颜色:粉色","activityCode":"XSG1507789611","picUrl":"http://image.domolife.cn/platform/CtZeSHJDPT.jpg","imgId":55367,"price":"8.00","name":"日本FaSoLa 抗菌菜板","count":1,"id":4352,"newPrice":"8.00","saleSkuId":282}]},{"activityInfo":{"activityCode":"LJ1507778006","activityRuleDetail":"立减震撼来袭立减震撼来袭","activityTag":"立减震撼来","activityType":2,"activityRule":"立减20.0"},"activityProductInfo":[{"skuName":"容量:10KG","activityCode":"LJ1507778006","picUrl":"http://image.domolife.cn/platform/s78nhsEdYT.jpg","imgId":55184,"price":"46.00","name":"爱丽思米桶滑盖式","count":2,"id":4328,"newPrice":"46.00","saleSkuId":236}]}]}
     * code : 01
     * msg : 请求成功
     */

    @SerializedName("result")
    private IndentDiscountsBean indentDiscountsBean;
    private String code;
    private String msg;

    public IndentDiscountsBean getIndentDiscountsBean() {
        return indentDiscountsBean;
    }

    public void setIndentDiscountsBean(IndentDiscountsBean indentDiscountsBean) {
        this.indentDiscountsBean = indentDiscountsBean;
    }

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

    public static class IndentDiscountsBean {
        /**
         * sumDiscountPrice : 62
         * priceInfo : [{"color":"#000000","totalPrice":725.6,"name":"商品总额","totalPriceName":"￥725.60"},{"color":"#000000","totalPrice":40,"name":"立减","totalPriceName":"-￥40.0"},{"color":"#000000","totalPrice":5,"name":"满30减5","totalPriceName":"-￥5.0"},{"color":"#000000","totalPrice":12,"name":"满2件8.0折","totalPriceName":"-￥12.0"},{"color":"#000000","totalPrice":0,"name":"运费","totalPriceName":"￥0.00"},{"color":"#FF0033","totalPrice":62,"name":"合计优惠","totalPriceName":"-￥62.00"},{"color":"#FF0033","totalPrice":663.6,"name":"实付：","totalPriceName":"￥663.60"}]
         * totalPrice : 663.6
         * userCouponInfo : {"amount":5,"create_time":"2017-10-17 20:03:48","product_coupon_id":75,"end_time":"2017-10-27 23:59:59","device_type":1,"merchant_id":"3c48836a3e8c4da3b4900e91675e1e6a","title":"shaw的测试优惠券","type":"活动","is_coupon_refund":0,"mode":1,"start_time":"2017-10-17 20:03:48","start_fee":10,"product_ids":"0","bgColor":"#FDF4F4","user_id":34650,"use_range":0,"modeBgColor":"#FFB9B8","id":957141,"status":0,"desc":"shaw的测试优惠券"}
         * totalDeliveryPrice : 0
         * productInfo : [{"activityProductInfo":[{"skuName":"颜色:粉色","activityCode":null,"picUrl":"http://image.domolife.cn/platform/2JTHNNpxNp1504497089096.jpg","imgId":105033,"price":"98.00","name":"【上新】日本寇吉特美臀坐垫","count":3,"id":9234,"newPrice":"98.00","saleSkuId":2341},{"skuName":"默认:默认","activityCode":null,"picUrl":"http://image.domolife.cn/platform/kiRdDmxfKR1504265432368.jpg","imgId":104902,"price":"28.00","name":"【独家定制】多么生活小清新随手杯","count":1,"id":9229,"newPrice":"28.00","saleSkuId":2339},{"skuName":"默认:默认","activityCode":null,"picUrl":"http://image.domolife.cn/platform/kiRdDmxfKR1504265432368.jpg","imgId":104902,"price":"28.00","name":"【独家定制】多么生活小清新随手杯","count":1,"id":9229,"newPrice":"28.00","saleSkuId":2339}]},{"activityInfo":{"activityCode":"MJ1507773048","activityRuleDetail":"1、双11爆炸来袭\r\n2、双11爆炸来袭\r\n3、双11爆炸来袭","activityTag":"满减","activityType":0,"activityRule":"满10.0减2.0;满20.0减4.0;满30.0减5.0;满200.0减8.0"},"activityProductInfo":[{"skuName":"颜色:四层","activityCode":"MJ1507773048","picUrl":"http://image.domolife.cn/platform/CR2nbhsJXc.jpg","imgId":54827,"price":"45.00","name":"FaSoLa冰箱冷冻饺子收纳盒","count":2,"id":4283,"newPrice":"45.00","saleSkuId":114},{"skuName":"颜色:四层","activityCode":"MJ1507773048","picUrl":"http://image.domolife.cn/platform/CR2nbhsJXc.jpg","imgId":54827,"price":"45.00","name":"FaSoLa冰箱冷冻饺子收纳盒","count":2,"id":4283,"newPrice":"45.00","saleSkuId":114}]},{"activityInfo":{"activityCode":"ZK1507788859","activityRuleDetail":"123","activityTag":"折扣","activityType":1,"activityRule":"满2件8.0折;满6件4.0折"},"activityProductInfo":[{"skuName":"颜色:薄款奶白色","activityCode":"ZK1507788859","picUrl":"http://image.domolife.cn/platform/Z4aT6cEhKA.jpg","imgId":55116,"price":"9.80","name":"FaSoLa粘贴式马桶垫马桶坐垫马桶贴","count":1,"id":4319,"newPrice":"9.80","saleSkuId":213},{"skuName":"颜色:薄款绿色","activityCode":"ZK1507788859","picUrl":"http://image.domolife.cn/platform/eFnAkeyw6j.jpg","imgId":55115,"price":"9.80","name":"FaSoLa粘贴式马桶垫马桶坐垫马桶贴","count":1,"id":4319,"newPrice":"9.80","saleSkuId":212},{"skuName":"颜色:蓝色","activityCode":"ZK1507788859","picUrl":"http://image.domolife.cn/platform/YeHFBhfA7C.jpg","imgId":55169,"price":"49.00","name":"FaSoLa厨房置物架碗碟沥水架","count":1,"id":4326,"newPrice":"49.00","saleSkuId":230}]},{"activityInfo":{"activityCode":"XSG1507789611","activityRuleDetail":"123","activityTag":"限时购","activityType":3,"activityRule":""},"activityProductInfo":[{"skuName":"颜色:黄色","activityCode":"XSG1507789611","picUrl":"http://image.domolife.cn/platform/JeeYeXfFPa.jpg","imgId":55374,"price":"9.00","name":"日本FaSoLa 抗菌菜板","count":3,"id":4352,"newPrice":"9.00","saleSkuId":283},{"skuName":"颜色:粉色","activityCode":"XSG1507789611","picUrl":"http://image.domolife.cn/platform/CtZeSHJDPT.jpg","imgId":55367,"price":"8.00","name":"日本FaSoLa 抗菌菜板","count":1,"id":4352,"newPrice":"8.00","saleSkuId":282}]},{"activityInfo":{"activityCode":"LJ1507778006","activityRuleDetail":"立减震撼来袭立减震撼来袭","activityTag":"立减震撼来","activityType":2,"activityRule":"立减20.0"},"activityProductInfo":[{"skuName":"容量:10KG","activityCode":"LJ1507778006","picUrl":"http://image.domolife.cn/platform/s78nhsEdYT.jpg","imgId":55184,"price":"46.00","name":"爱丽思米桶滑盖式","count":2,"id":4328,"newPrice":"46.00","saleSkuId":236}]}]
         */
        private double totalPrice;
        private String userCouponMsg;
        private String realName;
        private String idcard;
        private String showIdcard;
        private boolean isOverseasGo;
        private String prompt;
        private UserCouponInfoBean userCouponInfo;
        private int totalDeliveryPrice;
        @SerializedName("priceInfo")
        private List<PriceInfoBean> priceInfoList;
        @SerializedName("productInfo")
        private List<ProductInfoBean> productInfoList;
        /**
         * hideCreate : 1
         * showDel : 1
         */
        @SerializedName("isHideCreate")
        private int hideCreate;
        @SerializedName("isShowDel")
        private int showDel;

        public String getUserCouponMsg() {
            return userCouponMsg;
        }

        public void setUserCouponMsg(String userCouponMsg) {
            this.userCouponMsg = userCouponMsg;
        }

        public double getTotalPrice() {
            return totalPrice;
        }

        public void setTotalPrice(double totalPrice) {
            this.totalPrice = totalPrice;
        }

        public String getRealName() {
            return realName;
        }

        public void setRealName(String realName) {
            this.realName = realName;
        }

        public String getIdcard() {
            return idcard;
        }

        public void setIdcard(String idcard) {
            this.idcard = idcard;
        }

        public String getShowIdcard() {
            return showIdcard;
        }

        public void setShowIdcard(String showIdcard) {
            this.showIdcard = showIdcard;
        }

        public boolean isOverseasGo() {
            return isOverseasGo;
        }

        public void setOverseasGo(boolean overseasGo) {
            isOverseasGo = overseasGo;
        }

        public UserCouponInfoBean getUserCouponInfo() {
            return userCouponInfo;
        }

        public void setUserCouponInfo(UserCouponInfoBean userCouponInfo) {
            this.userCouponInfo = userCouponInfo;
        }

        public int getTotalDeliveryPrice() {
            return totalDeliveryPrice;
        }

        public void setTotalDeliveryPrice(int totalDeliveryPrice) {
            this.totalDeliveryPrice = totalDeliveryPrice;
        }

        public String getPrompt() {
            return prompt;
        }

        public void setPrompt(String prompt) {
            this.prompt = prompt;
        }

        public List<PriceInfoBean> getPriceInfoList() {
            return priceInfoList;
        }

        public void setPriceInfoList(List<PriceInfoBean> priceInfoList) {
            this.priceInfoList = priceInfoList;
        }

        public List<ProductInfoBean> getProductInfoList() {
            return productInfoList;
        }

        public void setProductInfoList(List<ProductInfoBean> productInfoList) {
            this.productInfoList = productInfoList;
        }

        public int getHideCreate() {
            return hideCreate;
        }

        public void setHideCreate(int hideCreate) {
            this.hideCreate = hideCreate;
        }

        public int getShowDel() {
            return showDel;
        }

        public void setShowDel(int showDel) {
            this.showDel = showDel;
        }

        public static class UserCouponInfoBean {
            /**
             * amount : 5
             * create_time : 2017-10-17 20:03:48
             * product_coupon_id : 75
             * end_time : 2017-10-27 23:59:59
             * device_type : 1
             * merchant_id : 3c48836a3e8c4da3b4900e91675e1e6a
             * title : shaw的测试优惠券
             * type : 活动
             * is_coupon_refund : 0
             * mode : 1
             * start_time : 2017-10-17 20:03:48
             * start_fee : 10
             * product_ids : 0
             * bgColor : #FDF4F4
             * user_id : 34650
             * use_range : 0
             * modeBgColor : #FFB9B8
             * id : 957141
             * status : 0
             * desc : shaw的测试优惠券
             */

            private int amount;
            private String create_time;
            private int product_coupon_id;
            private String end_time;
            private int device_type;
            private String merchant_id;
            private String title;
            private String type;
            private int is_coupon_refund;
            private int mode;
            private String start_time;
            private int start_fee;
            private String product_ids;
            private String bgColor;
            private int user_id;
            private int use_range;
            private String modeBgColor;
            private int id;
            private int status;
            private String desc;

            public int getAmount() {
                return amount;
            }

            public void setAmount(int amount) {
                this.amount = amount;
            }

            public String getCreate_time() {
                return create_time;
            }

            public void setCreate_time(String create_time) {
                this.create_time = create_time;
            }

            public int getProduct_coupon_id() {
                return product_coupon_id;
            }

            public void setProduct_coupon_id(int product_coupon_id) {
                this.product_coupon_id = product_coupon_id;
            }

            public String getEnd_time() {
                return end_time;
            }

            public void setEnd_time(String end_time) {
                this.end_time = end_time;
            }

            public int getDevice_type() {
                return device_type;
            }

            public void setDevice_type(int device_type) {
                this.device_type = device_type;
            }

            public String getMerchant_id() {
                return merchant_id;
            }

            public void setMerchant_id(String merchant_id) {
                this.merchant_id = merchant_id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public int getIs_coupon_refund() {
                return is_coupon_refund;
            }

            public void setIs_coupon_refund(int is_coupon_refund) {
                this.is_coupon_refund = is_coupon_refund;
            }

            public int getMode() {
                return mode;
            }

            public void setMode(int mode) {
                this.mode = mode;
            }

            public String getStart_time() {
                return start_time;
            }

            public void setStart_time(String start_time) {
                this.start_time = start_time;
            }

            public int getStart_fee() {
                return start_fee;
            }

            public void setStart_fee(int start_fee) {
                this.start_fee = start_fee;
            }

            public String getProduct_ids() {
                return product_ids;
            }

            public void setProduct_ids(String product_ids) {
                this.product_ids = product_ids;
            }

            public String getBgColor() {
                return bgColor;
            }

            public void setBgColor(String bgColor) {
                this.bgColor = bgColor;
            }

            public int getUser_id() {
                return user_id;
            }

            public void setUser_id(int user_id) {
                this.user_id = user_id;
            }

            public int getUse_range() {
                return use_range;
            }

            public void setUse_range(int use_range) {
                this.use_range = use_range;
            }

            public String getModeBgColor() {
                return modeBgColor;
            }

            public void setModeBgColor(String modeBgColor) {
                this.modeBgColor = modeBgColor;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public String getDesc() {
                return desc;
            }

            public void setDesc(String desc) {
                this.desc = desc;
            }
        }

        public static class PriceInfoBean implements Parcelable {
            /**
             * color : #000000
             * totalPrice : 725.6
             * name : 商品总额
             * totalPriceName : ￥725.60
             */

            private String color;
            private double totalPrice;
            private String name;
            private String totalPriceName;

            public String getColor() {
                return color;
            }

            public void setColor(String color) {
                this.color = color;
            }

            public double getTotalPrice() {
                return totalPrice;
            }

            public void setTotalPrice(double totalPrice) {
                this.totalPrice = totalPrice;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getTotalPriceName() {
                return totalPriceName;
            }

            public void setTotalPriceName(String totalPriceName) {
                this.totalPriceName = totalPriceName;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.color);
                dest.writeDouble(this.totalPrice);
                dest.writeString(this.name);
                dest.writeString(this.totalPriceName);
            }

            public PriceInfoBean() {
            }

            protected PriceInfoBean(Parcel in) {
                this.color = in.readString();
                this.totalPrice = in.readDouble();
                this.name = in.readString();
                this.totalPriceName = in.readString();
            }

            public static final Creator<PriceInfoBean> CREATOR = new Creator<PriceInfoBean>() {
                @Override
                public PriceInfoBean createFromParcel(Parcel source) {
                    return new PriceInfoBean(source);
                }

                @Override
                public PriceInfoBean[] newArray(int size) {
                    return new PriceInfoBean[size];
                }
            };
        }

        public static class ProductInfoBean implements Parcelable {
            /**
             * activityProductInfo : [{"skuName":"颜色:粉色","activityCode":null,"picUrl":"http://image.domolife.cn/platform/2JTHNNpxNp1504497089096.jpg","imgId":105033,"price":"98.00","name":"【上新】日本寇吉特美臀坐垫","count":3,"id":9234,"newPrice":"98.00","saleSkuId":2341},{"skuName":"默认:默认","activityCode":null,"picUrl":"http://image.domolife.cn/platform/kiRdDmxfKR1504265432368.jpg","imgId":104902,"price":"28.00","name":"【独家定制】多么生活小清新随手杯","count":1,"id":9229,"newPrice":"28.00","saleSkuId":2339},{"skuName":"默认:默认","activityCode":null,"picUrl":"http://image.domolife.cn/platform/kiRdDmxfKR1504265432368.jpg","imgId":104902,"price":"28.00","name":"【独家定制】多么生活小清新随手杯","count":1,"id":9229,"newPrice":"28.00","saleSkuId":2339}]
             * activityInfo : {"activityCode":"MJ1507773048","activityRuleDetail":"1、双11爆炸来袭\r\n2、双11爆炸来袭\r\n3、双11爆炸来袭","activityTag":"满减","activityType":0,"activityRule":"满10.0减2.0;满20.0减4.0;满30.0减5.0;满200.0减8.0"}
             */

            private ActivityInfoBean activityInfo;
            private List<ActivityProductInfoBean> activityProductInfo;

            public ActivityInfoBean getActivityInfo() {
                return activityInfo;
            }

            public void setActivityInfo(ActivityInfoBean activityInfo) {
                this.activityInfo = activityInfo;
            }

            public List<ActivityProductInfoBean> getActivityProductInfo() {
                return activityProductInfo;
            }

            public void setActivityProductInfo(List<ActivityProductInfoBean> activityProductInfo) {
                this.activityProductInfo = activityProductInfo;
            }

            public static class ActivityInfoBean implements Parcelable {
                /**
                 * activityCode : MJ1507773048
                 * activityRuleDetail : 1、双11爆炸来袭
                 * 2、双11爆炸来袭
                 * 3、双11爆炸来袭
                 * activityTag : 满减
                 * activityType : 0
                 * activityRule : 满10.0减2.0;满20.0减4.0;满30.0减5.0;满200.0减8.0
                 */

                private String activityCode;
                @SerializedName("activityRuleDetail")
                private List<CommunalDetailBean> activityRuleDetailList;
                private String activityTag;
                private int activityType;
                private String activityRule;

                public String getActivityCode() {
                    return activityCode;
                }

                public void setActivityCode(String activityCode) {
                    this.activityCode = activityCode;
                }

                public List<CommunalDetailBean> getActivityRuleDetailList() {
                    return activityRuleDetailList;
                }

                public void setActivityRuleDetailList(List<CommunalDetailBean> activityRuleDetailList) {
                    this.activityRuleDetailList = activityRuleDetailList;
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
                    dest.writeString(this.activityTag);
                    dest.writeInt(this.activityType);
                    dest.writeString(this.activityRule);
                }

                public ActivityInfoBean() {
                }

                protected ActivityInfoBean(Parcel in) {
                    this.activityCode = in.readString();
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

            public static class ActivityProductInfoBean implements Parcelable {
                /**
                 * skuName : 颜色:粉色
                 * activityCode : null
                 * picUrl : http://image.domolife.cn/platform/2JTHNNpxNp1504497089096.jpg
                 * imgId : 105033
                 * price : 98.00
                 * name : 【上新】日本寇吉特美臀坐垫
                 * count : 3
                 * id : 9234
                 * newPrice : 98.00
                 * saleSkuId : 2341
                 */

                private String skuName;
                private String activityCode;
                private String picUrl;
                private String newUserTag;
                private int imgId;
                private String price;
                private String name;
                private int count;
                private int id;
                private String newPrice;
                private int saleSkuId;
                private ActivityInfoBean activityInfoBean;
                private int showLine;
                private int showActInfo;
                private int quality;
                @SerializedName("activitypriceDesc")
                private String activityPriceDesc;
                //        是否可用券
                private int allowCoupon;
                //                购物车专属id
                private int cartId;
                //            组合状态
                private String combineParentId;
                //            赠品状态
                private String presentParentId;
                @SerializedName("combineProductInfo")
                private List<CartProductInfoBean> combineProductInfoList;
                @SerializedName("presentProductInfo")
                private List<CartProductInfoBean> presentProductInfoList;
                /**
                 * notBuyAreaInfo : (该商品超出配送范围,请重新选择商品或去掉该商品)
                 */
                private String notBuyAreaInfo;
//                是否展示删除商品按钮
                private boolean showDel;

                public String getNewUserTag() {
                    return newUserTag;
                }

                public void setNewUserTag(String newUserTag) {
                    this.newUserTag = newUserTag;
                }

                public int getQuality() {
                    return quality;
                }

                public void setQuality(int quality) {
                    this.quality = quality;
                }

                public boolean isShowDel() {
                    return showDel;
                }

                public void setShowDel(boolean showDel) {
                    this.showDel = showDel;
                }

                public String getActivityPriceDesc() {
                    return activityPriceDesc;
                }

                public void setActivityPriceDesc(String activityPriceDesc) {
                    this.activityPriceDesc = activityPriceDesc;
                }

                public int getShowActInfo() {
                    return showActInfo;
                }

                public void setShowActInfo(int showActInfo) {
                    this.showActInfo = showActInfo;
                }

                public int getCartId() {
                    return cartId;
                }

                public void setCartId(int cartId) {
                    this.cartId = cartId;
                }

                public int getAllowCoupon() {
                    return allowCoupon;
                }

                public void setAllowCoupon(int allowCoupon) {
                    this.allowCoupon = allowCoupon;
                }

                public int getShowLine() {
                    return showLine;
                }

                public void setShowLine(int showLine) {
                    this.showLine = showLine;
                }

                public ActivityInfoBean getActivityInfoBean() {
                    return activityInfoBean;
                }

                public void setActivityInfoBean(ActivityInfoBean activityInfoBean) {
                    this.activityInfoBean = activityInfoBean;
                }

                public String getSkuName() {
                    return skuName;
                }

                public void setSkuName(String skuName) {
                    this.skuName = skuName;
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

                public int getImgId() {
                    return imgId;
                }

                public void setImgId(int imgId) {
                    this.imgId = imgId;
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

                public String getNewPrice() {
                    return newPrice;
                }

                public void setNewPrice(String newPrice) {
                    this.newPrice = newPrice;
                }

                public int getSaleSkuId() {
                    return saleSkuId;
                }

                public void setSaleSkuId(int saleSkuId) {
                    this.saleSkuId = saleSkuId;
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

                public ActivityProductInfoBean() {
                }

                public String getNotBuyAreaInfo() {
                    return notBuyAreaInfo;
                }

                public void setNotBuyAreaInfo(String notBuyAreaInfo) {
                    this.notBuyAreaInfo = notBuyAreaInfo;
                }

                @Override
                public int describeContents() {
                    return 0;
                }

                @Override
                public void writeToParcel(Parcel dest, int flags) {
                    dest.writeString(this.skuName);
                    dest.writeString(this.activityCode);
                    dest.writeString(this.picUrl);
                    dest.writeString(this.newUserTag);
                    dest.writeInt(this.imgId);
                    dest.writeString(this.price);
                    dest.writeString(this.name);
                    dest.writeInt(this.count);
                    dest.writeInt(this.id);
                    dest.writeString(this.newPrice);
                    dest.writeInt(this.saleSkuId);
                    dest.writeParcelable(this.activityInfoBean, flags);
                    dest.writeInt(this.showLine);
                    dest.writeInt(this.showActInfo);
                    dest.writeInt(this.quality);
                    dest.writeString(this.activityPriceDesc);
                    dest.writeInt(this.allowCoupon);
                    dest.writeInt(this.cartId);
                    dest.writeString(this.combineParentId);
                    dest.writeString(this.presentParentId);
                    dest.writeTypedList(this.combineProductInfoList);
                    dest.writeTypedList(this.presentProductInfoList);
                    dest.writeString(this.notBuyAreaInfo);
                    dest.writeByte(this.showDel ? (byte) 1 : (byte) 0);
                }

                protected ActivityProductInfoBean(Parcel in) {
                    this.skuName = in.readString();
                    this.activityCode = in.readString();
                    this.picUrl = in.readString();
                    this.newUserTag = in.readString();
                    this.imgId = in.readInt();
                    this.price = in.readString();
                    this.name = in.readString();
                    this.count = in.readInt();
                    this.id = in.readInt();
                    this.newPrice = in.readString();
                    this.saleSkuId = in.readInt();
                    this.activityInfoBean = in.readParcelable(ActivityInfoBean.class.getClassLoader());
                    this.showLine = in.readInt();
                    this.showActInfo = in.readInt();
                    this.quality = in.readInt();
                    this.activityPriceDesc = in.readString();
                    this.allowCoupon = in.readInt();
                    this.cartId = in.readInt();
                    this.combineParentId = in.readString();
                    this.presentParentId = in.readString();
                    this.combineProductInfoList = in.createTypedArrayList(CartProductInfoBean.CREATOR);
                    this.presentProductInfoList = in.createTypedArrayList(CartProductInfoBean.CREATOR);
                    this.notBuyAreaInfo = in.readString();
                    this.showDel = in.readByte() != 0;
                }

                public static final Creator<ActivityProductInfoBean> CREATOR = new Creator<ActivityProductInfoBean>() {
                    @Override
                    public ActivityProductInfoBean createFromParcel(Parcel source) {
                        return new ActivityProductInfoBean(source);
                    }

                    @Override
                    public ActivityProductInfoBean[] newArray(int size) {
                        return new ActivityProductInfoBean[size];
                    }
                };
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeParcelable(this.activityInfo, flags);
                dest.writeList(this.activityProductInfo);
            }

            public ProductInfoBean() {
            }

            protected ProductInfoBean(Parcel in) {
                this.activityInfo = in.readParcelable(ActivityInfoBean.class.getClassLoader());
                this.activityProductInfo = new ArrayList<ActivityProductInfoBean>();
                in.readList(this.activityProductInfo, ActivityProductInfoBean.class.getClassLoader());
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
}
