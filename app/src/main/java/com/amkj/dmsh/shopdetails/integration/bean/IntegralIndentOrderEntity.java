package com.amkj.dmsh.shopdetails.integration.bean;

import com.amkj.dmsh.base.BaseEntity;
import com.amkj.dmsh.shopdetails.bean.IndentDiscountsEntity.IndentDiscountsBean.PriceInfoBean;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/8/9
 * version 3.1.5
 * class description:积分订单
 */
public class IntegralIndentOrderEntity extends BaseEntity{

    /**
     * code : string
     * currentTime : string
     * msg : string
     * result : {"currentTime":"string","orderList":[{"address":"string","consignee":"string","createTime":"string","goods":[{"count":0,"id":0,"integralPrice":0,"integralProductType":0,"integralType":0,"name":"string","orderProductId":0,"picUrl":"string","price":"string","saleSkuValue":"string","status":0}],"id":0,"integralAmount":0,"mobile":"string","no":"string","payAmount":"string","status":0,"userId":0}],"second":"string","status":[{}]}
     * sysTime : string
     */
    private String currentTime;
    @SerializedName("result")
    private IntegralIndentOrderBean integralIndentOrderBean;

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    public IntegralIndentOrderBean getIntegralIndentOrderBean() {
        return integralIndentOrderBean;
    }

    public void setIntegralIndentOrderBean(IntegralIndentOrderBean integralIndentOrderBean) {
        this.integralIndentOrderBean = integralIndentOrderBean;
    }

    public static class IntegralIndentOrderBean {
        /**
         * currentTime : string
         * orderList : [{"address":"string","consignee":"string","createTime":"string","goods":[{"count":0,"id":0,"integralPrice":0,"integralProductType":0,"integralType":0,"name":"string","orderProductId":0,"picUrl":"string","price":"string","saleSkuValue":"string","status":0}],"id":0,"integralAmount":0,"mobile":"string","no":"string","payAmount":"string","status":0,"userId":0}]
         * second : string
         * status : [{}]
         */

        private String currentTime;
        private String second;
        private List<OrderListBean> orderList;
        private Map<String, String> status;

        public String getCurrentTime() {
            return currentTime;
        }

        public void setCurrentTime(String currentTime) {
            this.currentTime = currentTime;
        }

        public String getSecond() {
            return second;
        }

        public void setSecond(String second) {
            this.second = second;
        }

        public List<OrderListBean> getOrderList() {
            return orderList;
        }

        public void setOrderList(List<OrderListBean> orderList) {
            this.orderList = orderList;
        }

        public Map<String, String> getStatus() {
            return status;
        }

        public void setStatus(Map<String, String> status) {
            this.status = status;
        }

        public static class OrderListBean {
            /**
             * address : string
             * consignee : string
             * createTime : string
             * goods : [{"count":0,"id":0,"integralPrice":0,"integralProductType":0,"integralType":0,"name":"string","orderProductId":0,"picUrl":"string","price":"string","saleSkuValue":"string","status":0}]
             * id : 0
             * integralAmount : 0
             * mobile : string
             * no : string
             * payAmount : string
             * status : 0
             * userId : 0
             */

            private String address;
            private String consignee;
            private String createTime;
            private int id;
            private int integralAmount;
            private String mobile;
            private String no;
            private String payAmount;
            private int status;
            private int userId;
            private List<GoodsBean> goods;
            @SerializedName("priceInfo")
            private List<PriceInfoBean> priceInfoBeans;

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public String getConsignee() {
                return consignee;
            }

            public void setConsignee(String consignee) {
                this.consignee = consignee;
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

            public int getIntegralAmount() {
                return integralAmount;
            }

            public void setIntegralAmount(int integralAmount) {
                this.integralAmount = integralAmount;
            }

            public String getMobile() {
                return mobile;
            }

            public void setMobile(String mobile) {
                this.mobile = mobile;
            }

            public String getNo() {
                return no;
            }

            public void setNo(String no) {
                this.no = no;
            }

            public String getPayAmount() {
                return payAmount;
            }

            public void setPayAmount(String payAmount) {
                this.payAmount = payAmount;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public int getUserId() {
                return userId;
            }

            public void setUserId(int userId) {
                this.userId = userId;
            }

            public List<GoodsBean> getGoods() {
                return goods;
            }

            public void setGoods(List<GoodsBean> goods) {
                this.goods = goods;
            }

            public List<PriceInfoBean> getPriceInfoBeans() {
                return priceInfoBeans;
            }

            public void setPriceInfoBeans(List<PriceInfoBean> priceInfoBeans) {
                this.priceInfoBeans = priceInfoBeans;
            }

            public static class GoodsBean {
                /**
                 * count : 0
                 * id : 0
                 * integralPrice : 0
                 * integralProductType : 0
                 * integralType : 0
                 * name : string
                 * orderProductId : 0
                 * picUrl : string
                 * price : string
                 * saleSkuValue : string
                 * status : 0
                 */

                private int count;
                private int id;
                private int integralPrice;
                private int integralProductType;
                private int integralType;
                private String name;
                private int orderProductId;
                private int orderRefundProductId;
                private String picUrl;
                private String price;
                private String saleSkuValue;
                private int status;

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

                public int getIntegralProductType() {
                    return integralProductType;
                }

                public void setIntegralProductType(int integralProductType) {
                    this.integralProductType = integralProductType;
                }

                public int getOrderRefundProductId() {
                    return orderRefundProductId;
                }

                public void setOrderRefundProductId(int orderRefundProductId) {
                    this.orderRefundProductId = orderRefundProductId;
                }

                public int getIntegralType() {
                    return integralType;
                }

                public void setIntegralType(int integralType) {
                    this.integralType = integralType;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public int getOrderProductId() {
                    return orderProductId;
                }

                public void setOrderProductId(int orderProductId) {
                    this.orderProductId = orderProductId;
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

                public String getSaleSkuValue() {
                    return saleSkuValue;
                }

                public void setSaleSkuValue(String saleSkuValue) {
                    this.saleSkuValue = saleSkuValue;
                }

                public int getStatus() {
                    return status;
                }

                public void setStatus(int status) {
                    this.status = status;
                }
            }
        }
    }
}
