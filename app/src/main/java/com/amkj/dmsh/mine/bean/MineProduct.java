package com.amkj.dmsh.mine.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by atd48 on 2016/9/12.
 */
public class MineProduct {
    /**
     * result : [{"id":5,"title":"川宁 洋甘菊茶包 洋甘菊花茶","price":0,"path":"/Uploads/goods_img/2016-04-26/571f13ad9fbd9.jpg"},{"id":6,"title":"北鼎K206高档电热水壶304食品级不锈钢烧水壶","price":0,"path":"/Uploads/goods_img/2016-04-26/571f146c7bff3.jpg"},{"id":7,"title":"日本代购迪士尼萌宠侧档DC-71","price":0,"path":"/Uploads/goods_img/2016-04-26/571f1523acdc9.jpg"},{"id":13,"title":"modern twist硅胶软围嘴宝宝吃饭围兜","price":0,"path":"/Uploads/goods_img/2016-04-28/5721ca3e8ed74.jpg"},{"id":15,"title":"膳魔师真空焖烧杯保温杯sk-3000儿童焖烧罐","price":0,"path":"/Uploads/goods_img/2016-04-28/5721cabde4190.jpg"},{"id":3,"title":"宫廷风雕花淡蓝色描金奢华感相框","price":0,"path":"/Uploads/goods_img/2016-04-26/571f131fa0bb5.jpg"},{"id":17,"title":"UMBRA正品 创意滑盖首饰盒","price":0,"path":"/Uploads/goods_img/2016-04-28/5721ccac196fe.jpg"},{"id":73,"title":"韩国promise me 权志龙GD太阳BIGBANG ","price":70,"path":"/Uploads/goods_img/2016-03-17/56ea1bf1af0e8.jpg"},{"id":4,"title":"可爱卡通动漫长颈鹿大象书档 动物风格","price":0,"path":"/Uploads/goods_img/2016-04-26/571f135e22db0.jpg"}]
     * code : 01
     * msg : 请求成功
     */

    private String code;
    private String msg;
    /**
     * id : 5
     * title : 川宁 洋甘菊茶包 洋甘菊花茶
     * price : 0
     * path : /Uploads/goods_img/2016-04-26/571f13ad9fbd9.jpg
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
        private String title;
        private int price;
        private String path;

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

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }
    }
}
