package com.amkj.dmsh.shopdetails.integration.bean;

import com.amkj.dmsh.base.BaseEntity;
import com.amkj.dmsh.shopdetails.integration.bean.IntegralIndentOrderEntity.IntegralIndentOrderBean.OrderListBean;
import com.google.gson.annotations.SerializedName;

import java.util.Map;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/8/9
 * version 3.1.5
 * class description:积分订单详情
 */
public class IntegralOrderDetailEntity extends BaseEntity{

    /**
     * integralOrderDetailBean : {"orderListBean":{"no":"cX27928X1426X1521541100276","consignee":"刘桂鹏","address":"广东省深圳市福田区-梅林路48号理想公馆2806","mobile":"13751077044","deliveryAmount":0,"goods":[{"picUrl":"http://image.domolife.cn/platform/20180116/20180116170010000.jpg","marketPrice":"30.00","saleSkuValue":"默认:默认","price":"0.00","integralProductType":0,"count":1,"name":"30元无门槛优惠券","id":11381,"orderProductId":134439,"integralPrice":4500,"integralType":0,"status":30}],"userId":27928,"tmpPayAmount":0,"priceInfo":[{"color":"#000000","name":"商品总额","totalPriceName":"4500积分"},{"color":"#FF0033","name":"实付：","totalPriceName":"4500积分"}],"integralAmount":4500,"payAmount":"0.00","createTime":"2018-03-20 18:18:20","id":231255,"status":30,"additionalFreight":0},"status":{"0":"待支付","1":"支付处理中","10":"待发货","11":"发货处理中","12":"部分发货","13":"发货处理中","14":"拼团中","15":"待发货(拼团)","16":"组合商品(待发货)","20":"待收货","21":"部分收货","30":"交易成功","31":"部分评价","40":"已评价","41":"组合商品交易成功","50":"维修审核中","51":"审核通过","52":"待确认","53":"售后中","54":"待确认(废弃)","55":"维修待收货","56":"售后完成","57":"审核驳回","58":"撤销申请","-10":"退款处理中","-11":"订单取消","-12":"支付超时","-20":"订单取消","-24":"交易关闭","-25":"交易关闭","-26":"拼团失败","-30":"退款审核中","-31":"已拒绝","-32":"前去退货","-35":"待退款","-36":"组合商品退款审核中","-40":"退款成功 ","-50":"退款成功","-51":"组合商品退款成功","-52":"主组合商品退款成功"}}
     * msg : 请求成功
     * code : 01
     * second : 900
     */
    @SerializedName("result")
    private IntegralOrderDetailBean integralOrderDetailBean;
    private String second;

    public IntegralOrderDetailBean getIntegralOrderDetailBean() {
        return integralOrderDetailBean;
    }

    public void setIntegralOrderDetailBean(IntegralOrderDetailBean result) {
        this.integralOrderDetailBean = result;
    }

    public String getSecond() {
        return second;
    }

    public void setSecond(String second) {
        this.second = second;
    }

    public static class IntegralOrderDetailBean {
        /**
         * orderListBean : {"no":"cX27928X1426X1521541100276","consignee":"刘桂鹏","address":"广东省深圳市福田区-梅林路48号理想公馆2806","mobile":"13751077044","deliveryAmount":0,"goods":[{"picUrl":"http://image.domolife.cn/platform/20180116/20180116170010000.jpg","marketPrice":"30.00","saleSkuValue":"默认:默认","price":"0.00","integralProductType":0,"count":1,"name":"30元无门槛优惠券","id":11381,"orderProductId":134439,"integralPrice":4500,"integralType":0,"status":30}],"userId":27928,"tmpPayAmount":0,"priceInfo":[{"color":"#000000","name":"商品总额","totalPriceName":"4500积分"},{"color":"#FF0033","name":"实付：","totalPriceName":"4500积分"}],"integralAmount":4500,"payAmount":"0.00","createTime":"2018-03-20 18:18:20","id":231255,"status":30,"additionalFreight":0}
         * status : {"0":"待支付","1":"支付处理中","10":"待发货","11":"发货处理中","12":"部分发货","13":"发货处理中","14":"拼团中","15":"待发货(拼团)","16":"组合商品(待发货)","20":"待收货","21":"部分收货","30":"交易成功","31":"部分评价","40":"已评价","41":"组合商品交易成功","50":"维修审核中","51":"审核通过","52":"待确认","53":"售后中","54":"待确认(废弃)","55":"维修待收货","56":"售后完成","57":"审核驳回","58":"撤销申请","-10":"退款处理中","-11":"订单取消","-12":"支付超时","-20":"订单取消","-24":"交易关闭","-25":"交易关闭","-26":"拼团失败","-30":"退款审核中","-31":"已拒绝","-32":"前去退货","-35":"待退款","-36":"组合商品退款审核中","-40":"退款成功 ","-50":"退款成功","-51":"组合商品退款成功","-52":"主组合商品退款成功"}
         */
        @SerializedName("order")
        private OrderListBean orderListBean;
        private Map<String, String> status;

        public OrderListBean getOrderListBean() {
            return orderListBean;
        }

        public void setOrderListBean(OrderListBean orderListBean) {
            this.orderListBean = orderListBean;
        }

        public Map<String, String> getStatus() {
            return status;
        }

        public void setStatus(Map<String, String> status) {
            this.status = status;
        }
    }
}
