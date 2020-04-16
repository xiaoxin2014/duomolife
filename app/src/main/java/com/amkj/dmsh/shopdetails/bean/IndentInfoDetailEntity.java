package com.amkj.dmsh.shopdetails.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.amkj.dmsh.mine.bean.CartProductInfoBean;
import com.amkj.dmsh.base.BaseEntity;
import com.amkj.dmsh.constant.CommunalDetailBean;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/10/19
 * class description:订单信息详情
 */

public class IndentInfoDetailEntity extends BaseEntity {

    /**
     * indentInfoDetailBean : {"order":{"no":"cX34650X2071X1508324417171","amount":"425.60","consignee":"小黑","address":"广东省中山市-哈哈","deliveryAmount":"0","mobile":"13751875555","goods":[{"orderProductInfo":[{"marketPrice":"158.00","saleSkuValue":"颜色:粉色","count":3,"orderProductId":58616,"activity_code":"","picUrl":"http://image.domolife.cn/platform/20170904/20170904115839892.jpg","price":"98.00","name":"【上新】日本寇吉特美臀坐垫","id":9234,"integralPrice":0,"status":0,"isCoupon":0,"saleSkuId":2341},{"marketPrice":"28.00","saleSkuValue":"默认:默认","count":1,"orderProductId":58620,"activity_code":"","picUrl":"http://image.domolife.cn/platform/20170901/20170901192943938.jpg","price":"28.00","name":"【独家定制】多么生活小清新随手杯","id":9229,"integralPrice":0,"status":0,"isCoupon":0,"saleSkuId":2339}],"activityInfo":{"activityCode":"ZK1507788859","activityTag":"折扣","activityRule":"满2件8.0折;满6件4.0折"}},{"activityInfo":{"activityCode":"ZK1507788859","activityTag":"折扣","activityRule":"满2件8.0折;满6件4.0折"},"orderProductInfo":[{"marketPrice":"21.00","saleSkuValue":"颜色:薄款奶白色","count":1,"orderProductId":58614,"activity_code":"ZK1507788859","picUrl":"http://image.domolife.cn/platform/20161203/20161203170345621.jpg","price":"9.80","name":"FaSoLa粘贴式马桶垫马桶坐垫马桶贴","id":4319,"integralPrice":0,"status":0,"isCoupon":0,"saleSkuId":213},{"marketPrice":"21.00","saleSkuValue":"颜色:薄款绿色","count":1,"orderProductId":58615,"activity_code":"ZK1507788859","picUrl":"http://image.domolife.cn/platform/20161203/20161203170345621.jpg","price":"9.80","name":"FaSoLa粘贴式马桶垫马桶坐垫马桶贴","id":4319,"integralPrice":0,"status":0,"isCoupon":0,"saleSkuId":212},{"marketPrice":"108.00","saleSkuValue":"颜色:蓝色","count":1,"orderProductId":58617,"activity_code":"ZK1507788859","picUrl":"http://image.domolife.cn/platform/4YHcseQ7re.jpg","price":"49.00","name":"FaSoLa厨房置物架碗碟沥水架","id":4326,"integralPrice":0,"status":0,"isCoupon":0,"saleSkuId":230}]},{"activityInfo":{"activityCode":"XSG1507789611","activityTag":"限时购","activityRule":""},"orderProductInfo":[{"marketPrice":"28.00","saleSkuValue":"颜色:黄色","count":3,"orderProductId":58618,"activity_code":"XSG1507789611","picUrl":"http://image.domolife.cn/platform/20161203/20161203192957675.jpg","price":"9.00","name":"日本FaSoLa 抗菌菜板","id":4352,"integralPrice":0,"status":0,"isCoupon":0,"saleSkuId":283},{"marketPrice":"28.00","saleSkuValue":"颜色:粉色","count":1,"orderProductId":58619,"activity_code":"XSG1507789611","picUrl":"http://image.domolife.cn/platform/20161203/20161203192957675.jpg","price":"8.00","name":"日本FaSoLa 抗菌菜板","id":4352,"integralPrice":0,"status":0,"isCoupon":0,"saleSkuId":282}]}],"remark":"","userId":34650,"privilegePrice":"0.00","priceInfo":[{"color":"#000000","name":"商品总额","totalPriceName":"¥425.60"},{"color":"#000000","name":"优惠","totalPriceName":"-¥12.00"},{"color":"#000000","name":"满2件8.0折","totalPriceName":"-¥12.00"},{"color":"#000000","name":"运费","totalPriceName":"¥0.00"},{"color":"#FF0033","name":"实付：","totalPriceName":"¥413.60"}],"integralAmount":0,"couponAmount":"0","payAmount":"413.60","createTime":"2017-10-18 19:00:17","id":101367,"gpStatus":0,"status":0,"isCoupon":false},"status":{"0":"待支付","1":"支付处理中","10":"待发货","11":"发货处理中","12":"部分发货","13":"发货处理中","14":"拼团中","15":"待发货(拼团)","20":"待收货","21":"部分收货","30":"交易成功","31":"部分评价","40":"已评价","-10":"退款处理中","-11":"订单取消","-12":"支付超时","-20":"订单取消","-24":"交易关闭","-25":"交易关闭","-26":"拼团失败","-30":"退款审核中","-31":"已拒绝","-32":"前去退货","-35":"待退款","-40":"退款成功 ","-50":"退款成功"}}
     * currentTime : 2017-10-19 11:38:08
     * msg : 请求成功
     * code : 01
     * second : 7200
     */
    @SerializedName("result")
    private IndentInfoDetailBean indentInfoDetailBean;
    private String currentTime;
    private int second;

    public IndentInfoDetailBean getIndentInfoDetailBean() {
        return indentInfoDetailBean;
    }

    public void setIndentInfoDetailBean(IndentInfoDetailBean indentInfoDetailBean) {
        this.indentInfoDetailBean = indentInfoDetailBean;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }

    public static class IndentInfoDetailBean {
        /**
         * order : {"no":"cX34650X2071X1508324417171","amount":"425.60","consignee":"小黑","address":"广东省中山市-哈哈","deliveryAmount":"0","mobile":"13751875555","goods":[{"orderProductInfo":[{"marketPrice":"158.00","saleSkuValue":"颜色:粉色","count":3,"orderProductId":58616,"activity_code":"","picUrl":"http://image.domolife.cn/platform/20170904/20170904115839892.jpg","price":"98.00","name":"【上新】日本寇吉特美臀坐垫","id":9234,"integralPrice":0,"status":0,"isCoupon":0,"saleSkuId":2341},{"marketPrice":"28.00","saleSkuValue":"默认:默认","count":1,"orderProductId":58620,"activity_code":"","picUrl":"http://image.domolife.cn/platform/20170901/20170901192943938.jpg","price":"28.00","name":"【独家定制】多么生活小清新随手杯","id":9229,"integralPrice":0,"status":0,"isCoupon":0,"saleSkuId":2339}],"activityInfo":{"activityCode":"ZK1507788859","activityTag":"折扣","activityRule":"满2件8.0折;满6件4.0折"}},{"activityInfo":{"activityCode":"ZK1507788859","activityTag":"折扣","activityRule":"满2件8.0折;满6件4.0折"},"orderProductInfo":[{"marketPrice":"21.00","saleSkuValue":"颜色:薄款奶白色","count":1,"orderProductId":58614,"activity_code":"ZK1507788859","picUrl":"http://image.domolife.cn/platform/20161203/20161203170345621.jpg","price":"9.80","name":"FaSoLa粘贴式马桶垫马桶坐垫马桶贴","id":4319,"integralPrice":0,"status":0,"isCoupon":0,"saleSkuId":213},{"marketPrice":"21.00","saleSkuValue":"颜色:薄款绿色","count":1,"orderProductId":58615,"activity_code":"ZK1507788859","picUrl":"http://image.domolife.cn/platform/20161203/20161203170345621.jpg","price":"9.80","name":"FaSoLa粘贴式马桶垫马桶坐垫马桶贴","id":4319,"integralPrice":0,"status":0,"isCoupon":0,"saleSkuId":212},{"marketPrice":"108.00","saleSkuValue":"颜色:蓝色","count":1,"orderProductId":58617,"activity_code":"ZK1507788859","picUrl":"http://image.domolife.cn/platform/4YHcseQ7re.jpg","price":"49.00","name":"FaSoLa厨房置物架碗碟沥水架","id":4326,"integralPrice":0,"status":0,"isCoupon":0,"saleSkuId":230}]},{"activityInfo":{"activityCode":"XSG1507789611","activityTag":"限时购","activityRule":""},"orderProductInfo":[{"marketPrice":"28.00","saleSkuValue":"颜色:黄色","count":3,"orderProductId":58618,"activity_code":"XSG1507789611","picUrl":"http://image.domolife.cn/platform/20161203/20161203192957675.jpg","price":"9.00","name":"日本FaSoLa 抗菌菜板","id":4352,"integralPrice":0,"status":0,"isCoupon":0,"saleSkuId":283},{"marketPrice":"28.00","saleSkuValue":"颜色:粉色","count":1,"orderProductId":58619,"activity_code":"XSG1507789611","picUrl":"http://image.domolife.cn/platform/20161203/20161203192957675.jpg","price":"8.00","name":"日本FaSoLa 抗菌菜板","id":4352,"integralPrice":0,"status":0,"isCoupon":0,"saleSkuId":282}]}],"remark":"","userId":34650,"privilegePrice":"0.00","priceInfo":[{"color":"#000000","name":"商品总额","totalPriceName":"¥425.60"},{"color":"#000000","name":"优惠","totalPriceName":"-¥12.00"},{"color":"#000000","name":"满2件8.0折","totalPriceName":"-¥12.00"},{"color":"#000000","name":"运费","totalPriceName":"¥0.00"},{"color":"#FF0033","name":"实付：","totalPriceName":"¥413.60"}],"integralAmount":0,"couponAmount":"0","payAmount":"413.60","createTime":"2017-10-18 19:00:17","id":101367,"gpStatus":0,"status":0,"isCoupon":false}
         * status : {"0":"待支付","1":"支付处理中","10":"待发货","11":"发货处理中","12":"部分发货","13":"发货处理中","14":"拼团中","15":"待发货(拼团)","20":"待收货","21":"部分收货","30":"交易成功","31":"部分评价","40":"已评价","-10":"退款处理中","-11":"订单取消","-12":"支付超时","-20":"订单取消","-24":"交易关闭","-25":"交易关闭","-26":"拼团失败","-30":"退款审核中","-31":"已拒绝","-32":"前去退货","-35":"待退款","-40":"退款成功 ","-50":"退款成功"}
         */

        @SerializedName("order")
        private OrderDetailBean orderDetailBean;
        private Map<String, String> status;

        public OrderDetailBean getOrderDetailBean() {
            return orderDetailBean;
        }

        public void setOrderDetailBean(OrderDetailBean orderDetailBean) {
            this.orderDetailBean = orderDetailBean;
        }

        public Map<String, String> getStatus() {
            return status;
        }

        public void setStatus(Map<String, String> status) {
            this.status = status;
        }

        public static class OrderDetailBean implements Parcelable {
            /**
             * no : cX34650X2071X1508324417171
             * amount : 425.60
             * consignee : 小黑
             * address : 广东省中山市-哈哈
             * deliveryAmount : 0
             * mobile : 13751875555
             * goods : [{"orderProductInfo":[{"marketPrice":"158.00","saleSkuValue":"颜色:粉色","count":3,"orderProductId":58616,"activity_code":"","picUrl":"http://image.domolife.cn/platform/20170904/20170904115839892.jpg","price":"98.00","name":"【上新】日本寇吉特美臀坐垫","id":9234,"integralPrice":0,"status":0,"isCoupon":0,"saleSkuId":2341},{"marketPrice":"28.00","saleSkuValue":"默认:默认","count":1,"orderProductId":58620,"activity_code":"","picUrl":"http://image.domolife.cn/platform/20170901/20170901192943938.jpg","price":"28.00","name":"【独家定制】多么生活小清新随手杯","id":9229,"integralPrice":0,"status":0,"isCoupon":0,"saleSkuId":2339}]},{"activityInfo":{"activityCode":"ZK1507788859","activityTag":"折扣","activityRule":"满2件8.0折;满6件4.0折"},"orderProductInfo":[{"marketPrice":"21.00","saleSkuValue":"颜色:薄款奶白色","count":1,"orderProductId":58614,"activity_code":"ZK1507788859","picUrl":"http://image.domolife.cn/platform/20161203/20161203170345621.jpg","price":"9.80","name":"FaSoLa粘贴式马桶垫马桶坐垫马桶贴","id":4319,"integralPrice":0,"status":0,"isCoupon":0,"saleSkuId":213},{"marketPrice":"21.00","saleSkuValue":"颜色:薄款绿色","count":1,"orderProductId":58615,"activity_code":"ZK1507788859","picUrl":"http://image.domolife.cn/platform/20161203/20161203170345621.jpg","price":"9.80","name":"FaSoLa粘贴式马桶垫马桶坐垫马桶贴","id":4319,"integralPrice":0,"status":0,"isCoupon":0,"saleSkuId":212},{"marketPrice":"108.00","saleSkuValue":"颜色:蓝色","count":1,"orderProductId":58617,"activity_code":"ZK1507788859","picUrl":"http://image.domolife.cn/platform/4YHcseQ7re.jpg","price":"49.00","name":"FaSoLa厨房置物架碗碟沥水架","id":4326,"integralPrice":0,"status":0,"isCoupon":0,"saleSkuId":230}]},{"activityInfo":{"activityCode":"XSG1507789611","activityTag":"限时购","activityRule":""},"orderProductInfo":[{"marketPrice":"28.00","saleSkuValue":"颜色:黄色","count":3,"orderProductId":58618,"activity_code":"XSG1507789611","picUrl":"http://image.domolife.cn/platform/20161203/20161203192957675.jpg","price":"9.00","name":"日本FaSoLa 抗菌菜板","id":4352,"integralPrice":0,"status":0,"isCoupon":0,"saleSkuId":283},{"marketPrice":"28.00","saleSkuValue":"颜色:粉色","count":1,"orderProductId":58619,"activity_code":"XSG1507789611","picUrl":"http://image.domolife.cn/platform/20161203/20161203192957675.jpg","price":"8.00","name":"日本FaSoLa 抗菌菜板","id":4352,"integralPrice":0,"status":0,"isCoupon":0,"saleSkuId":282}]}]
             * remark :
             * userId : 34650
             * privilegePrice : 0.00
             * priceInfo : [{"color":"#000000","name":"商品总额","totalPriceName":"¥425.60"},{"color":"#000000","name":"优惠","totalPriceName":"-¥12.00"},{"color":"#000000","name":"满2件8.0折","totalPriceName":"-¥12.00"},{"color":"#000000","name":"运费","totalPriceName":"¥0.00"},{"color":"#FF0033","name":"实付：","totalPriceName":"¥413.60"}]
             * integralAmount : 0
             * couponAmount : 0
             * payAmount : 413.60
             * createTime : 2017-10-18 19:00:17
             * id : 101367
             * gpStatus : 0
             * status : 0
             * isCoupon : false
             */

            private String no;
            private String amount;
            private String consignee;
            private String address;
            private String deliveryAmount;
            @SerializedName("waitdeliveryFlag")
            private boolean waitDeliveryFlag;
            private String mobile;
            private String remark;
            private int userId;
            private String privilegePrice;
            private int integralAmount;
            private String couponAmount;
            private String totalScore;
            private String payAmount;
            private String createTime;
            private int id;
            private int gpStatus;
            private int status;
            private boolean isCoupon;
            private String payType;
            @SerializedName("goods")
            private List<GoodsDetailBean> goodDetails;
            @SerializedName("priceInfo")
            private List<PriceInfoBean> priceInfoList;
            private int needComment=-1;//是否显示待评价按钮
            private String type;

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public int getNeedComment() {
                return needComment;
            }

            public boolean isNeedComment() {
                return needComment == 1;
            }

            public void setNeedComment(int needComment) {
                this.needComment = needComment;
            }

            public boolean isWaitDeliveryFlag() {
                return waitDeliveryFlag;
            }

            public void setWaitDeliveryFlag(boolean waitDeliveryFlag) {
                this.waitDeliveryFlag = waitDeliveryFlag;
            }

            public boolean isCoupon() {
                return isCoupon;
            }

            public void setCoupon(boolean coupon) {
                isCoupon = coupon;
            }

            public String getTotalScore() {
                return totalScore;
            }

            public void setTotalScore(String totalScore) {
                this.totalScore = totalScore;
            }

            public String getPayType() {
                return payType;
            }

            public void setPayType(String payType) {
                this.payType = payType;
            }

            public String getNo() {
                return no;
            }

            public void setNo(String no) {
                this.no = no;
            }

            public String getAmount() {
                return amount;
            }

            public void setAmount(String amount) {
                this.amount = amount;
            }

            public String getConsignee() {
                return consignee;
            }

            public void setConsignee(String consignee) {
                this.consignee = consignee;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public String getDeliveryAmount() {
                return deliveryAmount;
            }

            public void setDeliveryAmount(String deliveryAmount) {
                this.deliveryAmount = deliveryAmount;
            }

            public String getMobile() {
                return mobile;
            }

            public void setMobile(String mobile) {
                this.mobile = mobile;
            }

            public String getRemark() {
                return remark;
            }

            public void setRemark(String remark) {
                this.remark = remark;
            }

            public int getUserId() {
                return userId;
            }

            public void setUserId(int userId) {
                this.userId = userId;
            }

            public String getPrivilegePrice() {
                return privilegePrice;
            }

            public void setPrivilegePrice(String privilegePrice) {
                this.privilegePrice = privilegePrice;
            }

            public int getIntegralAmount() {
                return integralAmount;
            }

            public void setIntegralAmount(int integralAmount) {
                this.integralAmount = integralAmount;
            }

            public String getCouponAmount() {
                return couponAmount;
            }

            public void setCouponAmount(String couponAmount) {
                this.couponAmount = couponAmount;
            }

            public String getPayAmount() {
                return payAmount;
            }

            public void setPayAmount(String payAmount) {
                this.payAmount = payAmount;
            }

            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getGpStatus() {
                return gpStatus;
            }

            public void setGpStatus(int gpStatus) {
                this.gpStatus = gpStatus;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public boolean isIsCoupon() {
                return isCoupon;
            }

            public void setIsCoupon(boolean isCoupon) {
                this.isCoupon = isCoupon;
            }

            public List<GoodsDetailBean> getGoodDetails() {
                return goodDetails;
            }

            public void setGoodDetails(List<GoodsDetailBean> goodDetails) {
                this.goodDetails = goodDetails;
            }

            public List<PriceInfoBean> getPriceInfoList() {
                return priceInfoList;
            }

            public void setPriceInfoList(List<PriceInfoBean> priceInfoList) {
                this.priceInfoList = priceInfoList;
            }

            public static class GoodsDetailBean implements Parcelable {
                /**
                 * orderProductInfo : [{"marketPrice":"158.00","saleSkuValue":"颜色:粉色","count":3,"orderProductId":58616,"activity_code":"","picUrl":"http://image.domolife.cn/platform/20170904/20170904115839892.jpg","price":"98.00","name":"【上新】日本寇吉特美臀坐垫","id":9234,"integralPrice":0,"status":0,"isCoupon":0,"saleSkuId":2341},{"marketPrice":"28.00","saleSkuValue":"默认:默认","count":1,"orderProductId":58620,"activity_code":"","picUrl":"http://image.domolife.cn/platform/20170901/20170901192943938.jpg","price":"28.00","name":"【独家定制】多么生活小清新随手杯","id":9229,"integralPrice":0,"status":0,"isCoupon":0,"saleSkuId":2339}]
                 * activityInfo : {"activityCode":"ZK1507788859","activityTag":"折扣","activityRule":"满2件8.0折;满6件4.0折"}
                 */

                private ActivityInfoDetailBean activityInfo;
                @SerializedName("orderProductInfo")
                private List<OrderProductInfoBean> orderProductInfoList;

                public ActivityInfoDetailBean getActivityInfo() {
                    return activityInfo;
                }

                public void setActivityInfo(ActivityInfoDetailBean activityInfo) {
                    this.activityInfo = activityInfo;
                }

                public List<OrderProductInfoBean> getOrderProductInfoList() {
                    return orderProductInfoList;
                }

                public void setOrderProductInfoList(List<OrderProductInfoBean> orderProductInfoList) {
                    this.orderProductInfoList = orderProductInfoList;
                }

                public static class ActivityInfoDetailBean implements Parcelable {
                    /**
                     * activityCode : ZK1507788859
                     * activityTag : 折扣
                     * activityRule : 满2件8.0折;满6件4.0折
                     */

                    private String activityCode;
                    private String activityTag;
                    private String activityRule;
                    @SerializedName("activityRuleDetail")
                    private List<CommunalDetailBean> activityRuleDetailList;

                    public String getActivityCode() {
                        return activityCode;
                    }

                    public void setActivityCode(String activityCode) {
                        this.activityCode = activityCode;
                    }

                    public String getActivityTag() {
                        return activityTag;
                    }

                    public void setActivityTag(String activityTag) {
                        this.activityTag = activityTag;
                    }

                    public String getActivityRule() {
                        return activityRule;
                    }

                    public void setActivityRule(String activityRule) {
                        this.activityRule = activityRule;
                    }

                    public List<CommunalDetailBean> getActivityRuleDetailList() {
                        return activityRuleDetailList;
                    }

                    public void setActivityRuleDetailList(List<CommunalDetailBean> activityRuleDetailList) {
                        this.activityRuleDetailList = activityRuleDetailList;
                    }

                    @Override
                    public int describeContents() {
                        return 0;
                    }

                    @Override
                    public void writeToParcel(Parcel dest, int flags) {
                        dest.writeString(this.activityCode);
                        dest.writeString(this.activityTag);
                        dest.writeString(this.activityRule);
                    }

                    public ActivityInfoDetailBean() {
                    }

                    protected ActivityInfoDetailBean(Parcel in) {
                        this.activityCode = in.readString();
                        this.activityTag = in.readString();
                        this.activityRule = in.readString();
                    }

                    public static final Creator<ActivityInfoDetailBean> CREATOR = new Creator<ActivityInfoDetailBean>() {
                        @Override
                        public ActivityInfoDetailBean createFromParcel(Parcel source) {
                            return new ActivityInfoDetailBean(source);
                        }

                        @Override
                        public ActivityInfoDetailBean[] newArray(int size) {
                            return new ActivityInfoDetailBean[size];
                        }
                    };
                }

                public static class OrderProductInfoBean implements Parcelable {
                    /**
                     * marketPrice : 158.00
                     * saleSkuValue : 颜色:粉色
                     * count : 3
                     * orderProductId : 58616
                     * activity_code :
                     * picUrl : http://image.domolife.cn/platform/20170904/20170904115839892.jpg
                     * price : 98.00
                     * name : 【上新】日本寇吉特美臀坐垫
                     * id : 9234
                     * integralPrice : 0
                     * status : 0
                     * isCoupon : 0
                     * saleSkuId : 2341
                     */

                    private String marketPrice;
                    private String saleSkuValue;
                    private int count;
                    private int orderProductId;
                    private String activity_code;
                    private String picUrl;
                    private String price;
                    private String name;
                    private int id;
                    private int integralPrice;
                    private int status;
                    private int isCoupon;
                    private int saleSkuId;
                    private String currentTime;
                    private int second;
                    private int addSecond;
                    private String orderCreateTime;
                    private int orderRefundProductId;
                    private ActivityInfoDetailBean activityInfoDetailBean;
                    private int showLine;
                    //            组合状态
                    private String combineParentId;
                    //            赠品状态
                    private String presentParentId;
                    @SerializedName("combineProductInfo")
                    private List<CartProductInfoBean> combineProductInfoList;
                    @SerializedName("presentProductInfo")
                    private List<CartProductInfoBean> presentProductInfoList;
                    private PresentProductOrder presentProductOrder;


                    public PresentProductOrder getPresentProductOrder() {
                        return presentProductOrder;
                    }

                    public void setPresentProductOrder(PresentProductOrder presentProductOrder) {
                        this.presentProductOrder = presentProductOrder;
                    }

                    public int getAddSecond() {
                        return addSecond;
                    }

                    public void setAddSecond(int addSecond) {
                        this.addSecond = addSecond;
                    }

                    public int getShowLine() {
                        return showLine;
                    }

                    public void setShowLine(int showLine) {
                        this.showLine = showLine;
                    }

                    public String getCurrentTime() {
                        return currentTime;
                    }

                    public void setCurrentTime(String currentTime) {
                        this.currentTime = currentTime;
                    }

                    public int getSecond() {
                        return second;
                    }

                    public void setSecond(int second) {
                        this.second = second;
                    }

                    public String getOrderCreateTime() {
                        return orderCreateTime;
                    }

                    public void setOrderCreateTime(String orderCreateTime) {
                        this.orderCreateTime = orderCreateTime;
                    }

                    public int getOrderRefundProductId() {
                        return orderRefundProductId;
                    }

                    public void setOrderRefundProductId(int orderRefundProductId) {
                        this.orderRefundProductId = orderRefundProductId;
                    }

                    public ActivityInfoDetailBean getActivityInfoDetailBean() {
                        return activityInfoDetailBean;
                    }

                    public void setActivityInfoDetailBean(ActivityInfoDetailBean activityInfoDetailBean) {
                        this.activityInfoDetailBean = activityInfoDetailBean;
                    }

                    public String getMarketPrice() {
                        return marketPrice;
                    }

                    public void setMarketPrice(String marketPrice) {
                        this.marketPrice = marketPrice;
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

                    public int getOrderProductId() {
                        return orderProductId;
                    }

                    public void setOrderProductId(int orderProductId) {
                        this.orderProductId = orderProductId;
                    }

                    public String getActivity_code() {
                        return activity_code;
                    }

                    public void setActivity_code(String activity_code) {
                        this.activity_code = activity_code;
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

                    public int getStatus() {
                        return status;
                    }

                    public void setStatus(int status) {
                        this.status = status;
                    }

                    public int getIsCoupon() {
                        return isCoupon;
                    }

                    public void setIsCoupon(int isCoupon) {
                        this.isCoupon = isCoupon;
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

                    public OrderProductInfoBean() {
                    }

                    @Override
                    public int describeContents() {
                        return 0;
                    }

                    @Override
                    public void writeToParcel(Parcel dest, int flags) {
                        dest.writeString(this.marketPrice);
                        dest.writeString(this.saleSkuValue);
                        dest.writeInt(this.count);
                        dest.writeInt(this.orderProductId);
                        dest.writeString(this.activity_code);
                        dest.writeString(this.picUrl);
                        dest.writeString(this.price);
                        dest.writeString(this.name);
                        dest.writeInt(this.id);
                        dest.writeInt(this.integralPrice);
                        dest.writeInt(this.status);
                        dest.writeInt(this.isCoupon);
                        dest.writeInt(this.saleSkuId);
                        dest.writeString(this.currentTime);
                        dest.writeInt(this.second);
                        dest.writeInt(this.addSecond);
                        dest.writeString(this.orderCreateTime);
                        dest.writeInt(this.orderRefundProductId);
                        dest.writeParcelable(this.activityInfoDetailBean, flags);
                        dest.writeInt(this.showLine);
                        dest.writeString(this.combineParentId);
                        dest.writeString(this.presentParentId);
                        dest.writeTypedList(this.combineProductInfoList);
                        dest.writeTypedList(this.presentProductInfoList);
                    }

                    protected OrderProductInfoBean(Parcel in) {
                        this.marketPrice = in.readString();
                        this.saleSkuValue = in.readString();
                        this.count = in.readInt();
                        this.orderProductId = in.readInt();
                        this.activity_code = in.readString();
                        this.picUrl = in.readString();
                        this.price = in.readString();
                        this.name = in.readString();
                        this.id = in.readInt();
                        this.integralPrice = in.readInt();
                        this.status = in.readInt();
                        this.isCoupon = in.readInt();
                        this.saleSkuId = in.readInt();
                        this.currentTime = in.readString();
                        this.second = in.readInt();
                        this.addSecond = in.readInt();
                        this.orderCreateTime = in.readString();
                        this.orderRefundProductId = in.readInt();
                        this.activityInfoDetailBean = in.readParcelable(ActivityInfoDetailBean.class.getClassLoader());
                        this.showLine = in.readInt();
                        this.combineParentId = in.readString();
                        this.presentParentId = in.readString();
                        this.combineProductInfoList = in.createTypedArrayList(CartProductInfoBean.CREATOR);
                        this.presentProductInfoList = in.createTypedArrayList(CartProductInfoBean.CREATOR);
                    }

                    public static final Creator<OrderProductInfoBean> CREATOR = new Creator<OrderProductInfoBean>() {
                        @Override
                        public OrderProductInfoBean createFromParcel(Parcel source) {
                            return new OrderProductInfoBean(source);
                        }

                        @Override
                        public OrderProductInfoBean[] newArray(int size) {
                            return new OrderProductInfoBean[size];
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
                    dest.writeList(this.orderProductInfoList);
                }

                public GoodsDetailBean() {
                }

                protected GoodsDetailBean(Parcel in) {
                    this.activityInfo = in.readParcelable(ActivityInfoDetailBean.class.getClassLoader());
                    this.orderProductInfoList = new ArrayList<OrderProductInfoBean>();
                    in.readList(this.orderProductInfoList, OrderProductInfoBean.class.getClassLoader());
                }

                public static final Creator<GoodsDetailBean> CREATOR = new Creator<GoodsDetailBean>() {
                    @Override
                    public GoodsDetailBean createFromParcel(Parcel source) {
                        return new GoodsDetailBean(source);
                    }

                    @Override
                    public GoodsDetailBean[] newArray(int size) {
                        return new GoodsDetailBean[size];
                    }
                };
            }

            public static class PriceInfoDetailBean implements Parcelable {
                /**
                 * color : #000000
                 * name : 商品总额
                 * totalPriceName : ¥425.60
                 */

                private String color;
                private String name;
                private String totalPriceName;

                public String getColor() {
                    return color;
                }

                public void setColor(String color) {
                    this.color = color;
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
                    dest.writeString(this.name);
                    dest.writeString(this.totalPriceName);
                }

                public PriceInfoDetailBean() {
                }

                protected PriceInfoDetailBean(Parcel in) {
                    this.color = in.readString();
                    this.name = in.readString();
                    this.totalPriceName = in.readString();
                }

                public static final Creator<PriceInfoDetailBean> CREATOR = new Creator<PriceInfoDetailBean>() {
                    @Override
                    public PriceInfoDetailBean createFromParcel(Parcel source) {
                        return new PriceInfoDetailBean(source);
                    }

                    @Override
                    public PriceInfoDetailBean[] newArray(int size) {
                        return new PriceInfoDetailBean[size];
                    }
                };
            }

            public OrderDetailBean() {
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.no);
                dest.writeString(this.amount);
                dest.writeString(this.consignee);
                dest.writeString(this.address);
                dest.writeString(this.deliveryAmount);
                dest.writeString(this.mobile);
                dest.writeString(this.remark);
                dest.writeInt(this.userId);
                dest.writeString(this.privilegePrice);
                dest.writeInt(this.integralAmount);
                dest.writeString(this.couponAmount);
                dest.writeString(this.payAmount);
                dest.writeString(this.createTime);
                dest.writeInt(this.id);
                dest.writeInt(this.gpStatus);
                dest.writeInt(this.status);
                dest.writeByte(this.isCoupon ? (byte) 1 : (byte) 0);
                dest.writeString(this.payType);
                dest.writeTypedList(this.goodDetails);
                dest.writeTypedList(this.priceInfoList);
            }

            protected OrderDetailBean(Parcel in) {
                this.no = in.readString();
                this.amount = in.readString();
                this.consignee = in.readString();
                this.address = in.readString();
                this.deliveryAmount = in.readString();
                this.mobile = in.readString();
                this.remark = in.readString();
                this.userId = in.readInt();
                this.privilegePrice = in.readString();
                this.integralAmount = in.readInt();
                this.couponAmount = in.readString();
                this.payAmount = in.readString();
                this.createTime = in.readString();
                this.id = in.readInt();
                this.gpStatus = in.readInt();
                this.status = in.readInt();
                this.isCoupon = in.readByte() != 0;
                this.payType = in.readString();
                this.goodDetails = in.createTypedArrayList(GoodsDetailBean.CREATOR);
                this.priceInfoList = in.createTypedArrayList(PriceInfoBean.CREATOR);
            }

            public static final Creator<OrderDetailBean> CREATOR = new Creator<OrderDetailBean>() {
                @Override
                public OrderDetailBean createFromParcel(Parcel source) {
                    return new OrderDetailBean(source);
                }

                @Override
                public OrderDetailBean[] newArray(int size) {
                    return new OrderDetailBean[size];
                }
            };
        }
    }
}
