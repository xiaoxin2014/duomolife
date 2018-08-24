package com.amkj.dmsh.message.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/8/1
 * class description:交易订单
 */

public class MessageIndentEntity {

    /**
     * result : [{"m_title":"订单已发货","m_uid":34650,"m_obj":"cX34650X2027X1500021714662","ctime":"2017-08-01 10:00:53.314","json":{"orderId":"2777","productId":"5965","product_img_url":"http://image.domolife.cn/platform/20170309/20170309103021763.jpg"},"m_type":15,"m_content":"夕染彩绘全包硅胶防摔苹果手机壳  带支架","m_id":578068},{"m_title":"订单已发货","m_uid":34650,"m_obj":"cX34650X2027X1500022327535","ctime":"2017-07-31 11:58:03.897","json":{"orderId":"2779","productId":"5965","product_img_url":"http://image.domolife.cn/platform/20170309/20170309103021763.jpg"},"m_type":15,"m_content":"夕染彩绘全包硅胶防摔苹果手机壳  带支架","m_id":578064}]
     * msg : 请求成功
     * code : 01
     */

    private String msg;
    private String code;
    @SerializedName("result")
    private List<MessageIndentBean> messageIndentList;

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

    public List<MessageIndentBean> getMessageIndentList() {
        return messageIndentList;
    }

    public void setMessageIndentList(List<MessageIndentBean> messageIndentList) {
        this.messageIndentList = messageIndentList;
    }

    public static class MessageIndentBean {
        /**
         * m_title : 订单已发货
         * m_uid : 34650
         * m_obj : cX34650X2027X1500021714662
         * ctime : 2017-08-01 10:00:53.314
         * json : {"orderId":"2777","productId":"5965","product_img_url":"http://image.domolife.cn/platform/20170309/20170309103021763.jpg"}
         * m_type : 15
         * m_content : 夕染彩绘全包硅胶防摔苹果手机壳  带支架
         * m_id : 578068
         */

        private String m_title;
        private int m_uid;
        private String m_obj;
        private String ctime;
        private JsonBean json;
        private int m_type;
        private String m_content;
        private int m_id;

        public String getM_title() {
            return m_title;
        }

        public void setM_title(String m_title) {
            this.m_title = m_title;
        }

        public int getM_uid() {
            return m_uid;
        }

        public void setM_uid(int m_uid) {
            this.m_uid = m_uid;
        }

        public String getM_obj() {
            return m_obj;
        }

        public void setM_obj(String m_obj) {
            this.m_obj = m_obj;
        }

        public String getCtime() {
            return ctime;
        }

        public void setCtime(String ctime) {
            this.ctime = ctime;
        }

        public JsonBean getJson() {
            return json;
        }

        public void setJson(JsonBean json) {
            this.json = json;
        }

        public int getM_type() {
            return m_type;
        }

        public void setM_type(int m_type) {
            this.m_type = m_type;
        }

        public String getM_content() {
            return m_content;
        }

        public void setM_content(String m_content) {
            this.m_content = m_content;
        }

        public int getM_id() {
            return m_id;
        }

        public void setM_id(int m_id) {
            this.m_id = m_id;
        }

        public static class JsonBean {
            /**
             * orderId : 2777
             * productId : 5965
             * product_img_url : http://image.domolife.cn/platform/20170309/20170309103021763.jpg
             */

            private String orderId;
            private String productId;
            private String product_img_url;
            private int orderType;

            public int getOrderType() {
                return orderType;
            }

            public void setOrderType(int orderType) {
                this.orderType = orderType;
            }

            public String getOrderId() {
                return orderId;
            }

            public void setOrderId(String orderId) {
                this.orderId = orderId;
            }

            public String getProductId() {
                return productId;
            }

            public void setProductId(String productId) {
                this.productId = productId;
            }

            public String getProduct_img_url() {
                return product_img_url;
            }

            public void setProduct_img_url(String product_img_url) {
                this.product_img_url = product_img_url;
            }
        }
    }
}
