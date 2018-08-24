package com.amkj.dmsh.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by atd48 on 2016/9/13.
 */
public class QualityGoods {

    /**
     * result : [{"id":4159,"picUrl":"/Uploads/goods_img/2015-11-28/56592f513b2a9.jpg","price":"33000.00","name":"顶级竹纤维外搭超柔软"},{"id":4168,"picUrl":"/Uploads/goods_img/2016-03-17/56ea1bf1af0e8.jpg","price":"7000.00","name":"韩国promise me 权志龙GD太阳BIGBANG "},{"id":4167,"picUrl":"/Uploads/goods_img/2016-03-15/56e7bad795863.jpg","price":"850.00","name":"德单女士松口不勒女士丝袜"},{"id":4166,"picUrl":"/Uploads/goods_img/2016-03-15/56e7ba7e90088.jpg","price":"900.00","name":"德单竹纤维商务通勤中筒男士薄袜"},{"id":4165,"picUrl":"/Uploads/goods_img/2016-03-15/56e7b60597849.jpg","price":"19800.00","name":"韩国 ONE CHILD儿童碗"},{"id":4164,"picUrl":"http://o6wxayr69.bkt.clouddn.com/2016-05-14_5736e94d4221c.jpg","price":"4500.00","name":"精梳棉羊胎绒保暖打底裤"},{"id":4163,"picUrl":"/Uploads/goods_img/2015-11-29/565a588648b40.jpg","price":"7800.00","name":"长大干什么(第二辑)共10册"},{"id":4162,"price":"11500.00","name":"日本母婴店代购 正品花王纸尿裤/尿不湿 增量版NB96"},{"id":4161,"picUrl":"/Uploads/goods_img/2015-11-28/565931a01b009.jpg","price":"8500.00","name":"日本代购尤妮佳妈咪宝贝mamypoko超吸收尿不湿/纸尿裤 M64"},{"id":4160,"picUrl":"/Uploads/goods_img/2015-11-28/5659307c5e161.jpg","price":"850.00","name":"德单女士长绒棉双针筒加厚女袜"}]
     * code : 01
     * msg : 请求成功
     */

    private String code;
    private String msg;
    /**
     * id : 4159
     * picUrl : /Uploads/goods_img/2015-11-28/56592f513b2a9.jpg
     * price : 33000.00
     * name : 顶级竹纤维外搭超柔软
     */

    @SerializedName("result")
    private List<Goods> goodList;

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

    public List<Goods> getGoodList() {
        return goodList;
    }

    public void setGoodList(List<Goods> goodList) {
        this.goodList = goodList;
    }

    public static class Goods {
        private int id;
        private String picUrl;
        private String price;
        private String name;

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
    }
}
