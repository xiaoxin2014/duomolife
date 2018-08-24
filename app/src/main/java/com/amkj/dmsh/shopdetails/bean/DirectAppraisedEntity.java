package com.amkj.dmsh.shopdetails.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by atd48 on 2016/11/1.
 */
public class DirectAppraisedEntity {

    @SerializedName("result")
    private DirectAppraisedBean directAppraisedBean;
    /**
     * result : {"orderList":[{"content":"999999","status":1,"goods":{"picUrl":"http://img.domolife.cn/platform/5Emk4KG5R5.jpg","name":"老板椅"},"orderProductId":5,"star":5,"images":"http://img.domolife.cn/201610292101307864250030.jpg,http://img.domolife.cn/201610292101302295718170.jpg"}]}
     * code : 01
     * msg : 请求成功
     */

    private String code;
    private String msg;

    public DirectAppraisedBean getDirectAppraisedBean() {
        return directAppraisedBean;
    }

    public void setDirectAppraisedBean(DirectAppraisedBean directAppraisedBean) {
        this.directAppraisedBean = directAppraisedBean;
    }

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

    public static class DirectAppraisedBean {
        /**
         * content : 999999
         * status : 1
         * goods : {"picUrl":"http://img.domolife.cn/platform/5Emk4KG5R5.jpg","name":"老板椅"}
         * orderProductId : 5
         * star : 5
         * images : http://img.domolife.cn/201610292101307864250030.jpg,http://img.domolife.cn/201610292101302295718170.jpg
         */

        private List<OrderListBean> orderList;

        public List<OrderListBean> getOrderList() {
            return orderList;
        }

        public void setOrderList(List<OrderListBean> orderList) {
            this.orderList = orderList;
        }

        public static class OrderListBean {
            private String content;
            private int status;
            /**
             * picUrl : http://img.domolife.cn/platform/5Emk4KG5R5.jpg
             * name : 老板椅
             */

            private GoodsBean goods;
            private int orderProductId;
            private int star;
            private String images;

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public GoodsBean getGoods() {
                return goods;
            }

            public void setGoods(GoodsBean goods) {
                this.goods = goods;
            }

            public int getOrderProductId() {
                return orderProductId;
            }

            public void setOrderProductId(int orderProductId) {
                this.orderProductId = orderProductId;
            }

            public int getStar() {
                return star;
            }

            public void setStar(int star) {
                this.star = star;
            }

            public String getImages() {
                return images;
            }

            public void setImages(String images) {
                this.images = images;
            }

            public static class GoodsBean {
                private String picUrl;
                private String name;

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
            }
        }
    }
}
