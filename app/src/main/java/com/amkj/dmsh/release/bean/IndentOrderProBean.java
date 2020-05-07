package com.amkj.dmsh.release.bean;

import com.amkj.dmsh.utils.gson.GsonUtils;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/1/4
 * version 3.6
 * class description:晒单
 */

public class IndentOrderProBean {

    /**
     * msg : 请求成功
     * imgs : http://img.domolife.cn/201702222113162903407649.jpg,http://img.domolife.cn/201702222113162569080465.jpg
     * productOrderNo : cX840X1451X1486446429631
     * code : 01
     * findTopicBean : {"id":6,"title":"蝗虫团购晒单"}
     * products : [{"productId":4466,"price":158,"pictureUrl":"http://image.domolife.cn/platform/20171216/20171216184240731.jpg","id":407,"title":"日系香薰精油加湿器"}]
     */

    private String msg;
    private String imgs;
    private String productOrderNo;
    private String code;
    @SerializedName("findtopic")
    private FindTopicBean findTopicBean;
    @SerializedName("products")
    private List<ProductsBean> productList;

    public static IndentOrderProBean objectFromData(String str) {

        return GsonUtils.fromJson(str, IndentOrderProBean.class);
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getImgs() {
        return imgs;
    }

    public void setImgs(String imgs) {
        this.imgs = imgs;
    }

    public String getProductOrderNo() {
        return productOrderNo;
    }

    public void setProductOrderNo(String productOrderNo) {
        this.productOrderNo = productOrderNo;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public FindTopicBean getFindTopicBean() {
        return findTopicBean;
    }

    public void setFindTopicBean(FindTopicBean findTopicBean) {
        this.findTopicBean = findTopicBean;
    }

    public List<ProductsBean> getProductList() {
        return productList;
    }

    public void setProductList(List<ProductsBean> productList) {
        this.productList = productList;
    }

    public static class FindTopicBean {
        /**
         * id : 6
         * title : 蝗虫团购晒单
         */

        private int id;
        private String title;
        private String reminder;

        public static FindTopicBean objectFromData(String str) {

            return GsonUtils.fromJson(str, FindTopicBean.class);
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getReminder() {
            return reminder;
        }

        public void setReminder(String reminder) {
            this.reminder = reminder;
        }
    }

    public static class ProductsBean {
        /**
         * productId : 4466
         * price : 158
         * pictureUrl : http://image.domolife.cn/platform/20171216/20171216184240731.jpg
         * id : 407
         * title : 日系香薰精油加湿器
         */

        private int productId;
        private String price;
        private String pictureUrl;
        private int id;
        private String title;

        public static ProductsBean objectFromData(String str) {
            return GsonUtils.fromJson(str, ProductsBean.class);
        }

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

        public String getPictureUrl() {
            return pictureUrl;
        }

        public void setPictureUrl(String pictureUrl) {
            this.pictureUrl = pictureUrl;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
