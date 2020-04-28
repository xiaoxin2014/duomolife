package com.amkj.dmsh.shopdetails.bean;

import com.amkj.dmsh.base.BaseTimeEntity;
import com.amkj.dmsh.bean.OrderProductNewBean;

import java.util.List;

/**
 * Created by xiaoxin on 2020/3/31
 * Version:v4.5.0
 * ClassDescription :获取退款商品列表
 */
public class RefundProductsEntity extends BaseTimeEntity {
    private List<OrderProductNewBean> result;

    public List<OrderProductNewBean> getRefundProducts() {
        return result;
    }

    public void setRefundProducts(List<OrderProductNewBean> result) {
        this.result = result;
    }
}
