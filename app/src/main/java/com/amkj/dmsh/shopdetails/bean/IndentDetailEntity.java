package com.amkj.dmsh.shopdetails.bean;

import com.amkj.dmsh.base.BaseTimeEntity;


public class IndentDetailEntity extends BaseTimeEntity {

    /**
     * sysTime : 2020-03-24 11:29:50
     * result : {"orderNo":"cX266978X87290X1585020530500","statusText":"待支付","status":"0","createTime":"2020-03-24 11:28:50","payTime":"","payType":"","second":"7200","userRemark":"","consignee":"石志青","mobile":"18576609297","address":"广东省深圳市福田区-理想公馆2822","packageCount":"0","msg":"","isEditAddress":"0","logisticsInfo":null,"priceInfoList":[{"name":"商品总额","color":"#000000","priceText":"￥0.01"},{"name":"运费","color":"#000000","priceText":"￥0.0"},{"name":"实付：","color":"#FF0033","priceText":"￥0.01"}],"orderProductList":[{"orderProductId":"740529","productId":"18297","picUrl":"http://image.domolife.cn/platform/20200312/20200312190320879.jpeg","count":"1","productName":"啊啊啊啊啊啊啊","price":"￥0.01","saleSkuValue":"颜色:红","statusText":"待支付","status":"0","isShowRefundApply":"0","refundNo":"","presentProductOrder":{"presentName":"赠品","presentPicUrl":"http://image.domolife.cn/platform/20200323/20200323114745926.png","presentCount":"1"}}],"activityInfoList":[{"activityCode":"XSG1585020514","activityTag":"限时购","orderProductList":[{"orderProductId":"740529","productId":"18297","picUrl":"http://image.domolife.cn/platform/20200312/20200312190320879.jpeg","count":"1","productName":"啊啊啊啊啊啊啊","price":"￥0.01","saleSkuValue":"颜色:红","statusText":"待支付","status":"0","isShowRefundApply":"0","refundNo":"","presentProductOrder":{"presentName":"赠品","presentPicUrl":"http://image.domolife.cn/platform/20200323/20200323114745926.png","presentCount":"1"}}]}],"buttonList":[{"id":"12","clickable":"1","btnText":"去支付"},{"id":"11","clickable":"1","btnText":"取消订单"}]}
     */

    private MainOrderListEntity.MainOrderBean result;

    public MainOrderListEntity.MainOrderBean getResult() {
        return result;
    }

    public void setResult(MainOrderListEntity.MainOrderBean result) {
        this.result = result;
    }
}
