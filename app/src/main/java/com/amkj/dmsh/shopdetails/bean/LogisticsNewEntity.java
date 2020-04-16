package com.amkj.dmsh.shopdetails.bean;

import com.amkj.dmsh.base.BaseEntity;
import com.amkj.dmsh.bean.LogisticTextBean;
import com.amkj.dmsh.bean.OrderProductNewBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaoxin on 2020/3/23
 * Version:v4.4.3
 * ClassDescription :新版物流详情实体类
 */
public class LogisticsNewEntity extends BaseEntity {

    private LogisticsDetailBean logisticsDetail;

    //普通物流
    private List<PackageInfoBean> packageInfoList;
    private List<OrderProductNewBean> productInfoList;

    //退款物流
    private OrderProductNewBean productInfo;
    private String address;


    public OrderProductNewBean getProductInfo() {
        return productInfo;
    }

    public void setProductInfo(OrderProductNewBean productInfo) {
        this.productInfo = productInfo;
    }

    public List<PackageInfoBean> getPackageInfoList() {
        return packageInfoList;
    }

    public void setPackageInfoList(List<PackageInfoBean> packageInfoList) {
        this.packageInfoList = packageInfoList;
    }


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LogisticsDetailBean getLogisticsDetail() {
        return logisticsDetail;
    }

    public void setLogisticsDetail(LogisticsDetailBean logisticsDetail) {
        this.logisticsDetail = logisticsDetail;
    }

    public List<OrderProductNewBean> getProductInfoList() {
        return productInfoList == null ? new ArrayList<>() : productInfoList;
    }

    public void setProductInfoList(List<OrderProductNewBean> productInfoList) {
        this.productInfoList = productInfoList;
    }

    public static class LogisticsDetailBean {
        /**
         * deliverystatus : 1
         * type : YTO
         * number : 141244121
         * list : [{"time":"2020-03-23 11:50:09","status":"该物流暂无流转信息，请耐心等待或联系客服!"}]
         * issign : 0
         * companyName : 圆通快递
         */

        private String deliverystatus;
        private String type;
        private String number;
        private String issign;
        private String companyName;
        private List<LogisticTextBean> list;

        public String getDeliverystatus() {
            return deliverystatus;
        }

        public void setDeliverystatus(String deliverystatus) {
            this.deliverystatus = deliverystatus;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getIssign() {
            return issign;
        }

        public void setIssign(String issign) {
            this.issign = issign;
        }

        public String getCompanyName() {
            return companyName;
        }

        public void setCompanyName(String companyName) {
            this.companyName = companyName;
        }

        public List<LogisticTextBean> getList() {
            return list;
        }

        public void setList(List<LogisticTextBean> list) {
            this.list = list;
        }
    }


    public static class PackageInfoBean {
        /**
         * orderNo : cX266978X87290X1584931443444
         * expressNo : 63636326326
         * isPresent : 0
         */

        private String orderNo;
        private String expressNo;
        private String isPresent;//是否是赠品包裹
        //退款物流相关
        private String refundNo;


        public PackageInfoBean(String refundNo) {
            this.refundNo = refundNo;
        }

        public String getRefundNo() {
            return refundNo;
        }

        public void setRefundNo(String refundNo) {
            this.refundNo = refundNo;
        }

        public String getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(String orderNo) {
            this.orderNo = orderNo;
        }

        public String getExpressNo() {
            return expressNo;
        }

        public void setExpressNo(String expressNo) {
            this.expressNo = expressNo;
        }

        public boolean isPresent() {
            return "1".equals(isPresent);
        }
    }
}
