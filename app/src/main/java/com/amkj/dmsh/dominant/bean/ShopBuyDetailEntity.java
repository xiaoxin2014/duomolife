package com.amkj.dmsh.dominant.bean;

import com.amkj.dmsh.bean.CommunalDetailBean;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/6/26
 * class descriptionBeanList:请输入类描述
 */

public class ShopBuyDetailEntity {

    /**
     * result : {"coverImgUrl":"http://img.domolife.cn/platform/Z4zmWbeMsR1498444667430.jpg","descriptionBeanList":[{"type":"text","content":"<p style=\"text-align: left;\">如果你正在赴日旅行如果你正在赴日旅行如果你正在赴日旅行如果你正在赴日旅行如果你正在赴日旅行如果你正在赴日旅行如果你正在赴日旅行如果你正在赴日旅行。<\/p>"},{"type":"text","content":"<p><br/><\/p>"},{"type":"coupon","content":{"amount":"45.00","count":999998,"title":"阿斯顿","totalCount":1000000,"mode":1,"picUrl":"http://img.domolife.cn/platform/zmsCPM3GnM1498444769151.png","newPirUrl":"http://img.domolife.cn/platform/zmsCPM3GnM1498444769151.png","useRange":0,"startFee":"10.00","startTime":"2017-03-30 00:00:00","id":17,"endTime":"2018-04-28 00:00:00","desc":""}},{"type":"text","content":"<p style=\"text-align: center;\"><br/><\/p>"},{"type":"text","content":"<p style=\"text-align: center;\"><span style=\"font-size: 20px;\">就算剁手也要买买买<\/span><br/><\/p>"},{"type":"text","content":"<p style=\"text-align: center; line-height: 1.5em; margin-top: 10px;\"><span style=\"font-size: 20px;\">剁手推荐<\/span><\/p>"},{"type":"text","content":"<p><br/><\/p>"},{"type":"text","content":"<p><br/><\/p>"}],"id":3}
     * msg : 请求成功
     * code : 01
     */

    @SerializedName("result")
    private ShopBuyDetailBean shopBuyDetailBean;
    private String msg;
    private String code;

    public ShopBuyDetailBean getShopBuyDetailBean() {
        return shopBuyDetailBean;
    }

    public void setShopBuyDetailBean(ShopBuyDetailBean shopBuyDetailBean) {
        this.shopBuyDetailBean = shopBuyDetailBean;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public static class ShopBuyDetailBean {
        /**
         * coverImgUrl : http://img.domolife.cn/platform/Z4zmWbeMsR1498444667430.jpg
         * descriptionBeanList : [{"type":"text","content":"<p style=\"text-align: left;\">如果你正在赴日旅行如果你正在赴日旅行如果你正在赴日旅行如果你正在赴日旅行如果你正在赴日旅行如果你正在赴日旅行如果你正在赴日旅行如果你正在赴日旅行。<\/p>"},{"type":"text","content":"<p><br/><\/p>"},{"type":"coupon","content":{"amount":"45.00","count":999998,"title":"阿斯顿","totalCount":1000000,"mode":1,"picUrl":"http://img.domolife.cn/platform/zmsCPM3GnM1498444769151.png","newPirUrl":"http://img.domolife.cn/platform/zmsCPM3GnM1498444769151.png","useRange":0,"startFee":"10.00","startTime":"2017-03-30 00:00:00","id":17,"endTime":"2018-04-28 00:00:00","desc":""}},{"type":"text","content":"<p style=\"text-align: center;\"><br/><\/p>"},{"type":"text","content":"<p style=\"text-align: center;\"><span style=\"font-size: 20px;\">就算剁手也要买买买<\/span><br/><\/p>"},{"type":"text","content":"<p style=\"text-align: center; line-height: 1.5em; margin-top: 10px;\"><span style=\"font-size: 20px;\">剁手推荐<\/span><\/p>"},{"type":"text","content":"<p><br/><\/p>"},{"type":"text","content":"<p><br/><\/p>"}]
         * id : 3
         */

        private String coverImgUrl;
        private int id;
        private String name;
        @SerializedName("description")
        private List<CommunalDetailBean> descriptionBeanList;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCoverImgUrl() {
            return coverImgUrl;
        }

        public void setCoverImgUrl(String coverImgUrl) {
            this.coverImgUrl = coverImgUrl;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public List<CommunalDetailBean> getDescriptionBeanList() {
            return descriptionBeanList;
        }

        public void setDescriptionBeanList(List<CommunalDetailBean> descriptionBeanList) {
            this.descriptionBeanList = descriptionBeanList;
        }
    }
}
