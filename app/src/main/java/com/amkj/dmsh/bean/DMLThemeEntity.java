package com.amkj.dmsh.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by atd48 on 2016/9/12.
 */
public class DMLThemeEntity {

    /**
     * result : [{"id":17,"title":"韩国洗护专场","picUrl":"/Uploads/Ad/2016-03-23/56f23e2cf1eba.jpg","goods":[{"id":83,"picUrl":"/Uploads/goods_img/2016-03-23/56f24a6a19c98.jpg","price":"23.00","name":"LG柔顺剂 1300ml"},{"id":83,"picUrl":"/Uploads/goods_img/2016-03-23/56f24a6a19c98.jpg","price":"23.00","name":"LG柔顺剂 1300ml"},{"id":82,"picUrl":"/Uploads/goods_img/2016-03-23/56f24aed8f466.jpg","price":"23.00","name":"LG洗衣液 1300ml"},{"id":81,"picUrl":"/Uploads/goods_img/2016-03-23/56f24b46a75c5.jpg","price":"29.00","name":"LG ON沐浴露-浪漫粉色/紫色/绿色 500g"},{"id":80,"picUrl":"/Uploads/goods_img/2016-03-23/56f2436feb76f.jpg","price":"25.00","name":"贵爱娘卫生巾夜用 "},{"id":79,"picUrl":"/Uploads/goods_img/2016-03-23/56f24961b1c25.jpg","price":"25.00","name":"贵爱娘中草药卫生巾夜用"}],"subtitle":""},{"id":12,"title":"花王洗护用品品牌专场","picUrl":"/Uploads/Ad/2016-03-08/56de74d13f07b.jpg","subtitle":""},{"id":13,"title":"保宁洗护用品专场","picUrl":"/Uploads/Ad/2016-03-08/56de7570168cb.jpg","subtitle":""},{"id":15,"title":"Arau宝宝洗护系列","picUrl":"/Uploads/Ad/2016-03-14/56e67000972c2.jpg","goods":[{"id":13,"picUrl":"/Uploads/goods_img/2016-04-28/5721ca3e8ed74.jpg","price":"0","name":"modern twist硅胶软围嘴宝宝吃饭围兜"},{"id":12,"picUrl":"/Uploads/goods_img/2016-04-28/5721c9df1bfcf.jpg","price":"0","name":"Tonze/天际 DZG-W405E蒸蛋器蒸蛋机迷你蒸蛋器 "},{"id":11,"picUrl":"/Uploads/goods_img/2016-04-28/5721c9a35318a.jpg","price":"0","name":"韩国正品进口ROMANE可爱韩国传统系列黑色0.38圆珠笔"}],"subtitle":"Arau宝宝洗护系列"}]
     * code : 01
     * msg : 请求成功
     */

    private String code;
    private String msg;
    /**
     * id : 17
     * title : 韩国洗护专场
     * picUrl : /Uploads/Ad/2016-03-23/56f23e2cf1eba.jpg
     * goods : [{"id":83,"picUrl":"/Uploads/goods_img/2016-03-23/56f24a6a19c98.jpg","price":"23.00","name":"LG柔顺剂 1300ml"},{"id":83,"picUrl":"/Uploads/goods_img/2016-03-23/56f24a6a19c98.jpg","price":"23.00","name":"LG柔顺剂 1300ml"},{"id":82,"picUrl":"/Uploads/goods_img/2016-03-23/56f24aed8f466.jpg","price":"23.00","name":"LG洗衣液 1300ml"},{"id":81,"picUrl":"/Uploads/goods_img/2016-03-23/56f24b46a75c5.jpg","price":"29.00","name":"LG ON沐浴露-浪漫粉色/紫色/绿色 500g"},{"id":80,"picUrl":"/Uploads/goods_img/2016-03-23/56f2436feb76f.jpg","price":"25.00","name":"贵爱娘卫生巾夜用 "},{"id":79,"picUrl":"/Uploads/goods_img/2016-03-23/56f24961b1c25.jpg","price":"25.00","name":"贵爱娘中草药卫生巾夜用"}]
     * subtitle :
     */

    @SerializedName("result")
    private List<DMLThemeBean> themeList;

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

    public List<DMLThemeBean> getThemeList() {
        return themeList;
    }

    public void setThemeList(List<DMLThemeBean> themeList) {
        this.themeList = themeList;
    }

    public static class DMLThemeBean {
        private int id;
        private String title;
        private String picUrl;
        private String subtitle;
        /**
         * id : 83
         * picUrl : /Uploads/goods_img/2016-03-23/56f24a6a19c98.jpg
         * price : 23.00
         * name : LG柔顺剂 1300ml
         */

        private List<DMLGoodsBean> goods;

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

        public String getPicUrl() {
            return picUrl;
        }

        public void setPicUrl(String picUrl) {
            this.picUrl = picUrl;
        }

        public String getSubtitle() {
            return subtitle;
        }

        public void setSubtitle(String subtitle) {
            this.subtitle = subtitle;
        }

        public List<DMLGoodsBean> getGoods() {
            return goods;
        }

        public void setGoods(List<DMLGoodsBean> goods) {
            this.goods = goods;
        }

        public static class DMLGoodsBean implements MultiItemEntity {
            private int id;
            private String picUrl;
            private String price;
            private String name;
            private String subtitle;
            private int quantity;
            private int itemType;

            public String getSubtitle() {
                return subtitle;
            }

            public void setSubtitle(String subtitle) {
                this.subtitle = subtitle;
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

            @Override
            public int getItemType() {
                return itemType;
            }

            public void setItemType(int itemType) {
                this.itemType = itemType;
            }
        }
    }
}
