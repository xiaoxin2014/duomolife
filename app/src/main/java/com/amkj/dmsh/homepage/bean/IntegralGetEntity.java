package com.amkj.dmsh.homepage.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/7/23
 * version 3.1.5
 * class description:积分获取
 */
public class IntegralGetEntity {

    /**
     * code : 01
     * msg : 请求成功
     * result : [{"id":2,"title":"购买积分获取","image":"http://image.domolife.cn/platform/fmrrzxyZYP1531800028260.png","iosLink":"app://DMLGoodsProductsInfoViewController?goodsId=32","androidLink":"app://ShopScrollDetailsActivity?productId=32","webLink":"http://www.domolife.cn/m/template/common/proprietary.html?id=32","webPcLink":"http://www.domolife.cn/template/common/proprietary.html?id=32","description":"1块钱10积分","content":"购物消费","button":"去购买"},{"id":3,"title":"搜索是是是","image":"http://image.domolife.cn/platform/RY27xxSSmh1532161165287.png","iosLink":"app://DMLDiscoverViewController","androidLink":"app://http://whatUrl12","webLink":"http://whatUrl","webPcLink":"http://whatUrl123","description":"1分/1评论","content":"晒单对我","button":"去评论"}]
     */

    private String code;
    private String msg;
    @SerializedName("result")
    private List<IntegralGetBean> integralGetList;

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

    public List<IntegralGetBean> getIntegralGetList() {
        return integralGetList;
    }

    public void setIntegralGetList(List<IntegralGetBean> integralGetList) {
        this.integralGetList = integralGetList;
    }

    public static class IntegralGetBean {
        /**
         * id : 2
         * title : 购买积分获取
         * image : http://image.domolife.cn/platform/fmrrzxyZYP1531800028260.png
         * iosLink : app://DMLGoodsProductsInfoViewController?goodsId=32
         * androidLink : app://ShopScrollDetailsActivity?productId=32
         * webLink : http://www.domolife.cn/m/template/common/proprietary.html?id=32
         * webPcLink : http://www.domolife.cn/template/common/proprietary.html?id=32
         * description : 1块钱10积分
         * content : 购物消费
         * button : 去购买
         */

        private int id;
        private String title;
        private String image;
        private String androidLink;
        private String description;
        private String content;
        private String button;
        private int buttonFlag;

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

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getAndroidLink() {
            return androidLink;
        }

        public void setAndroidLink(String androidLink) {
            this.androidLink = androidLink;
        }

        public int getButtonFlag() {
            return buttonFlag;
        }

        public void setButtonFlag(int buttonFlag) {
            this.buttonFlag = buttonFlag;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getButton() {
            return button;
        }

        public void setButton(String button) {
            this.button = button;
        }
    }
}
