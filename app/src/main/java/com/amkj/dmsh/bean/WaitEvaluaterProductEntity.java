package com.amkj.dmsh.bean;

import com.amkj.dmsh.base.BaseTimeEntity;

import java.util.List;

/**
 * Created by xiaoxin on 2020/4/18
 * Version:v4.5.0
 * ClassDescription :待评价商品订单列表实体类
 */
public class WaitEvaluaterProductEntity extends BaseTimeEntity {

    /**
     * code : string
     * currentPage : 0
     * msg : string
     * result : [{"count":"string","createTime":"string","orderProductId":"string","picUrl":"string","price":"string","productId":"string","productName":"string","saleSkuValue":"string","status":"string","statusText":"string"}]
     * showCount : 0
     * sysTime : string
     * totalPage : 0
     * totalResult : 0
     */

    private List<OrderProductNewBean> result;

    public List<OrderProductNewBean> getResult() {
        return result;
    }

    public void setResult(List<OrderProductNewBean> result) {
        this.result = result;
    }
}
