package com.amkj.dmsh.shopdetails.bean;

import com.amkj.dmsh.base.BaseTimeEntity;

/**
 * Created by xiaoxin on 2020/3/31
 * Version:v4.4.3
 * ClassDescription :退款信息
 */
public class RefundInfoEntity extends BaseTimeEntity {

    /**
     * result : {"mobile":"string","refundGoodsList":[{"count":"string","orderProductId":"string","picUrl":"string","price":"string","productId":"string","productName":"string","saleSkuValue":"string"}],"refundPrice":"string","refundReasonMap":{},"refundTypeMap":{}}
     * sysTime : string
     */

    private MainOrderListEntity.MainOrderBean result;

    public MainOrderListEntity.MainOrderBean getRefundInfo() {
        return result;
    }

    public void setRefundInfo(MainOrderListEntity.MainOrderBean result) {
        this.result = result;
    }
}
