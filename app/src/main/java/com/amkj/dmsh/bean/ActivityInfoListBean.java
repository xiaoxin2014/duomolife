package com.amkj.dmsh.bean;

import java.util.List;

/**
 * Created by xiaoxin on 2020/3/25
 * Version:v4.4.3
 * ClassDescription :
 */
public class ActivityInfoListBean {
    /**
     * activityCode : XSG1585020514
     * activityTag : 限时购
     * orderProductList : [{"orderProductId":"740529","productId":"18297","picUrl":"http://image.domolife.cn/platform/20200312/20200312190320879.jpeg","count":"1","productName":"啊啊啊啊啊啊啊","price":"￥0.01","saleSkuValue":"颜色:红","statusText":"待支付","status":"0","isShowRefundApply":"0","refundNo":"","presentProductOrder":{"presentName":"赠品","presentPicUrl":"http://image.domolife.cn/platform/20200323/20200323114745926.png","presentCount":"1"}}]
     */

    private String activityCode;
    private String activityTag;
    private List<OrderProductNewBean> orderProductList;

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

    public List<OrderProductNewBean> getOrderProductList() {
        return orderProductList;
    }

    public void setOrderProductList(List<OrderProductNewBean> orderProductList) {
        this.orderProductList = orderProductList;
    }
}
