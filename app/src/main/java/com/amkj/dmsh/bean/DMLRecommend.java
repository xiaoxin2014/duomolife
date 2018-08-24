package com.amkj.dmsh.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by atd48 on 2016/9/12.
 */
public class DMLRecommend {

    /**
     * result : [{"id":4202,"marketPrice":"3300.00","picUrl":"/Uploads/goods_img/2016-03-23/56f24729ccbf6.jpg","price":"2800.00","name":"高支水洗棉拖鞋 ","subtitle":"熊窝良品 宜家风 家居拖鞋 家居鞋 情侣拖鞋 高支水洗棉拖鞋 "},{"id":4203,"marketPrice":"20800.00","picUrl":"/Uploads/goods_img/2016-03-23/56f24743dbf9b.jpg","price":"18800.00","name":"全棉活性印花四件套","subtitle":"熊窝良品 2016新款 全棉活性印花四件套 范冰冰款 女神同款"},{"id":4204,"marketPrice":"16900.00","picUrl":"/Uploads/goods_img/2016-03-23/56f246d66445e.jpg","price":"13500.00","name":"儿童床品三件套","subtitle":"【凡妈精品】美国Licori**品牌儿童床品三件套 欧标工艺 另有床笠"}]
     * code : 01
     * msg : 请求成功
     */

    private String code;
    private String msg;
    /**
     * id : 4202
     * marketPrice : 3300.00
     * picUrl : /Uploads/goods_img/2016-03-23/56f24729ccbf6.jpg
     * price : 2800.00
     * name : 高支水洗棉拖鞋
     * subtitle : 熊窝良品 宜家风 家居拖鞋 家居鞋 情侣拖鞋 高支水洗棉拖鞋
     */

    @SerializedName("result")
    private List<ProductBean> productList;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<ProductBean> getProductList() {
        return productList;
    }

    public void setProductList(List<ProductBean> productList) {
        this.productList = productList;
    }

    public static class ProductBean {
        private int id;
        private String marketPrice;
        private String maxPrice;
        private String picUrl;
        private String price;
        private String name;
        private String subtitle;
        private int quantity;
        private int isRemind;

        public String getMaxPrice() {
            return maxPrice;
        }

        public void setMaxPrice(String maxPrice) {
            this.maxPrice = maxPrice;
        }

        public int getIsRemind() {
            return isRemind;
        }

        public void setIsRemind(int isRemind) {
            this.isRemind = isRemind;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getMarketPrice() {
            return marketPrice;
        }

        public void setMarketPrice(String marketPrice) {
            this.marketPrice = marketPrice;
        }

        public String getPicUrl() {
            return picUrl;
        }

        public void setPicUrl(String picUrl) {
            this.picUrl = picUrl;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSubtitle() {
            return subtitle;
        }

        public void setSubtitle(String subtitle) {
            this.subtitle = subtitle;
        }
    }
}
