package com.amkj.dmsh.shopdetails.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by atd48 on 2016/10/27.
 */
public class DirectReBuyGoods {

    /**
     * result : [{"saleSkuId":176,"price":"10.00","quantity":17,"propValues":"3,12,15","picUrl":"http://img.domolife.cn/Uploads/goods_img/2015-11-28/565931a01b009.jpg","saleSkuValue":"颜色:黄,尺码:XXL,逗比:你大爷","count":1,"status":1,"name":"日本代购尤妮佳妈咪宝贝mamypoko超吸收尿不湿/纸尿裤 M64","id":4184}]
     * code : 01
     * msg : 请求成功
     */

    private String code;
    private String msg;
    /**
     * saleSkuId : 176
     * price : 10.00
     * quantity : 17
     * propValues : 3,12,15
     * picUrl : http://img.domolife.cn/Uploads/goods_img/2015-11-28/565931a01b009.jpg
     * saleSkuValue : 颜色:黄,尺码:XXL,逗比:你大爷
     * count : 1
     * status : 1
     * name : 日本代购尤妮佳妈咪宝贝mamypoko超吸收尿不湿/纸尿裤 M64
     * id : 4184
     */

    @SerializedName("result")
    private List<ReBuyGoodsBean> reBuyGoodsBean;

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

    public List<ReBuyGoodsBean> getReBuyGoodsBean() {
        return reBuyGoodsBean;
    }

    public void setReBuyGoodsBean(List<ReBuyGoodsBean> reBuyGoodsBean) {
        this.reBuyGoodsBean = reBuyGoodsBean;
    }

    public static class ReBuyGoodsBean {
        private int saleSkuId;
        private String price;
        private int quantity;
        private String propValues;
        private String picUrl;
        private String saleSkuValue;
        private int count;
        private int status;
        private String name;
        private int id;

        public int getSaleSkuId() {
            return saleSkuId;
        }

        public void setSaleSkuId(int saleSkuId) {
            this.saleSkuId = saleSkuId;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public String getPropValues() {
            return propValues;
        }

        public void setPropValues(String propValues) {
            this.propValues = propValues;
        }

        public String getPicUrl() {
            return picUrl;
        }

        public void setPicUrl(String picUrl) {
            this.picUrl = picUrl;
        }

        public String getSaleSkuValue() {
            return saleSkuValue;
        }

        public void setSaleSkuValue(String saleSkuValue) {
            this.saleSkuValue = saleSkuValue;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}
