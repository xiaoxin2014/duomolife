package com.amkj.dmsh.shopdetails;

/**
 * Created by xiaoxin on 2020/4/3
 * Version:v4.4.3
 * ClassDescription :订单列表或者订单详情点击去支付回调
 */
public interface GoPayClickCallback {

    void goPay(String orderNo);
}
