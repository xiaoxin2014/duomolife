package com.amkj.dmsh.shopdetails;

/**
 * Created by xiaoxin on 2020/4/3
 * Version:v4.5.0
 * ClassDescription :订单列表或者订单详情点击去支付回调
 */
public interface PayWayClickListener {

    void aliPay();

    void WxPay();

    void UnionPay();

}
