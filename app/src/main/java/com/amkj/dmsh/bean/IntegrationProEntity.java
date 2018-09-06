package com.amkj.dmsh.bean;

import com.amkj.dmsh.base.BaseEntity;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by atd48 on 2016/9/12.
 */
public class IntegrationProEntity extends BaseEntity{

    /**
     * result : [{"id":11,"integralPrice":22600,"marketPrice":"18.80","picUrl":"/Uploads/goods_img/2016-04-28/5721c9a35318a.jpg","name":"韩国正品进口ROMANE可爱韩国传统系列黑色0.38圆珠笔","subtitle":"韩国正品进口ROMANE可爱韩国传统系列黑色0.38圆珠笔","quantity":0},{"id":9,"integralPrice":26100,"marketPrice":"21.80","picUrl":"/Uploads/goods_img/2016-04-26/571f244f202e0.jpg","name":"韩国文具chachap逼真牛奶便签本Milk Memo 300页 8款","subtitle":"","quantity":0},{"id":12,"integralPrice":38400,"marketPrice":"32.00","picUrl":"/Uploads/goods_img/2016-04-28/5721c9df1bfcf.jpg","name":"Tonze/天际 DZG-W405E蒸蛋器蒸蛋机迷你蒸蛋器 ","subtitle":"Tonze/天际 DZG-W405E蒸蛋器蒸蛋机迷你蒸蛋器 ","quantity":0},{"id":5,"integralPrice":47900,"marketPrice":"39.90","picUrl":"/Uploads/goods_img/2016-04-26/571f13ad9fbd9.jpg","name":"川宁 洋甘菊茶包 洋甘菊花茶","subtitle":"","quantity":0},{"id":7,"integralPrice":58800,"marketPrice":"49.00","picUrl":"/Uploads/goods_img/2016-04-26/571f1523acdc9.jpg","name":"日本代购迪士尼萌宠侧档DC-71","subtitle":"","quantity":0},{"id":34,"integralPrice":63600,"marketPrice":"53.00","picUrl":"/Uploads/goods_img/2016-04-06/5704a8db984fc.jpg","name":"儿童收纳系列动物造型抱枕靠垫玩具卡通玩偶","subtitle":"儿童收纳系列动物造型抱枕靠垫玩具卡通玩偶","quantity":0},{"id":19,"integralPrice":83400,"marketPrice":"139.00","picUrl":"/Uploads/goods_img/2016-04-28/5721cd2f37320.jpg","name":"Kirkland Signature 科克兰stretch-tite 保鲜膜(单个发货)","subtitle":"Kirkland Signature 科克兰stretch-tite 保鲜膜(单个发货)","quantity":0},{"id":36,"integralPrice":84000,"marketPrice":"70.00","picUrl":"http://o6wxayr69.bkt.clouddn.com/2016-07-08_577f6fdf603c2.jpg","name":"Tanana塑料随手杯成人吸管杯","subtitle":"Tanana塑料随手杯成人吸管杯","quantity":0},{"id":8,"integralPrice":91200,"marketPrice":"76.00","picUrl":"/Uploads/goods_img/2016-04-26/571f225a362a2.jpg","name":"加拿大Umbra Buddy 伙伴餐巾纸座","subtitle":"","quantity":0},{"id":27,"integralPrice":93600,"marketPrice":"78.00","picUrl":"http://o6wxayr69.bkt.clouddn.com/2016-06-28_5772269a583a1.jpg","name":"韩国Let's diet光疗美肤帽美白遮阳帽","subtitle":"","quantity":0}]
     * code : 01
     * msg : 请求成功
     */
    /**
     * id : 11
     * integralPrice : 22600
     * marketPrice : 18.80
     * picUrl : /Uploads/goods_img/2016-04-28/5721c9a35318a.jpg
     * name : 韩国正品进口ROMANE可爱韩国传统系列黑色0.38圆珠笔
     * subtitle : 韩国正品进口ROMANE可爱韩国传统系列黑色0.38圆珠笔
     * quantity : 0
     */

    @SerializedName("result")
    private List<IntegrationBean> integrationList;

    public List<IntegrationBean> getIntegrationList() {
        return integrationList;
    }

    public void setIntegrationList(List<IntegrationBean> integrationList) {
        this.integrationList = integrationList;
    }

    public static class IntegrationBean implements MultiItemEntity{
        private int id;
        private int integralPrice;
        private String marketPrice;
        private String picUrl;
        private String name;
        private String subtitle;
        private int quantity;
        /**
         * moneyPrice : 0
         * integralType : 0
         * integralProductType : 0
         */

        private String moneyPrice;
//         积分商品类型,0是实物,1是虚拟商品 ,
        private int integralProductType;
        //        积分类型,0是纯积分商品,1是积分+金钱商品 ,
        private int integralType;
        private int cItemType;

        public void setcItemType(int cItemType) {
            this.cItemType = cItemType;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getIntegralPrice() {
            return integralPrice;
        }

        public void setIntegralPrice(int integralPrice) {
            this.integralPrice = integralPrice;
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

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public String getMoneyPrice() {
            return moneyPrice;
        }

        public void setMoneyPrice(String moneyPrice) {
            this.moneyPrice = moneyPrice;
        }

        public int getIntegralType() {
            return integralType;
        }

        public void setIntegralType(int integralType) {
            this.integralType = integralType;
        }

        public int getIntegralProductType() {
            return integralProductType;
        }

        public void setIntegralProductType(int integralProductType) {
            this.integralProductType = integralProductType;
        }

        @Override
        public int getItemType() {
            return cItemType;
        }
    }
}
