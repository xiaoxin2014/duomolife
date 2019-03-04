package com.amkj.dmsh.dominant.bean;

import com.amkj.dmsh.base.BaseTimeEntity;

import java.util.List;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2019/3/2
 * version 3.3.0
 * class description:整点秒杀商品
 */
public class PointSpikeProductEntity extends BaseTimeEntity {
    private List<TimeAxisProductListBean> timeAxisProductList;

    public List<TimeAxisProductListBean> getTimeAxisProductList() {
        return timeAxisProductList;
    }

    public void setTimeAxisProductList(List<TimeAxisProductListBean> timeAxisProductList) {
        this.timeAxisProductList = timeAxisProductList;
    }

    public static class TimeAxisProductListBean {
        /**
         * productId : 4292
         * price : 540.00
         * path : http://image.domolife.cn/platform/20170225/20170225203712448.jpg
         * title : 韩国IFAM 四层整理架+三层边角架构成4
         * marketPrice : 628.00
         * isNotice : 0
         * subtitle : 一枚硬币就可以轻松组装
         */

        private int productId;
        private String price;
        private String path;
        private String title;
        private String marketPrice;
        private int isNotice;
        private String subtitle;
        private int statusCode;

        public int getProductId() {
            return productId;
        }

        public void setProductId(int productId) {
            this.productId = productId;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getMarketPrice() {
            return marketPrice;
        }

        public void setMarketPrice(String marketPrice) {
            this.marketPrice = marketPrice;
        }

        public int getIsNotice() {
            return isNotice;
        }

        public void setIsNotice(int isNotice) {
            this.isNotice = isNotice;
        }

        public String getSubtitle() {
            return subtitle;
        }

        public void setSubtitle(String subtitle) {
            this.subtitle = subtitle;
        }

        public int getStatusCode() {
            return statusCode;
        }

        public void setStatusCode(int statusCode) {
            this.statusCode = statusCode;
        }
    }
}
