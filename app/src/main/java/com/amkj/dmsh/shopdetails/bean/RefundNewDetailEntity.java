package com.amkj.dmsh.shopdetails.bean;

import com.amkj.dmsh.base.BaseTimeEntity;

/**
 * Created by xiaoxin on 2020/3/28
 * Version:v4.4.3
 * ClassDescription :新版退款详情
 */

public class RefundNewDetailEntity extends BaseTimeEntity {

    /**
     * result : {"buttonList":[{"btnText":"string","clickable":"string","id":"string"}],"endTime":"string","isShowCancel":"string","isShowUpdate":"string","msg":"string","refunReason":"string","refundGoodsAddressInfo":{"address":"string","consignee":"string","expressCompany":"string","expressNo":"string","mobile":"string"},"refundGoodsList":[{"count":"string","picUrl":"string","price":"string","productId":"string","productName":"string","saleSkuValue":"string"}],"refundNo":"string","refundPrice":"string","refundStatusText":"string","refundTime":"string","refundType":"string","status":"string"}
     * sysTime : string
     */

    private MainOrderListEntity.MainOrderBean result;

    public MainOrderListEntity.MainOrderBean getRefundDetailBean() {
        return result;
    }

    public void setResult(MainOrderListEntity.MainOrderBean result) {
        this.result = result;
    }

}
