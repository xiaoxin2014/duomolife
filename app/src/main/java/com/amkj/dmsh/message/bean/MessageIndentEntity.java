package com.amkj.dmsh.message.bean;

import com.amkj.dmsh.base.BaseEntity;

import java.util.List;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/8/1
 * class description:交易订单
 */

public class MessageIndentEntity extends BaseEntity {

    /**
     * result : [{"m_title":"订单已发货","m_uid":34650,"m_obj":"cX34650X2027X1500021714662","ctime":"2017-08-01 10:00:53.314","json":{"orderId":"2777","productId":"5965","product_img_url":"http://image.domolife.cn/platform/20170309/20170309103021763.jpg"},"m_type":15,"m_content":"夕染彩绘全包硅胶防摔苹果手机壳  带支架","m_id":578068},{"m_title":"订单已发货","m_uid":34650,"m_obj":"cX34650X2027X1500022327535","ctime":"2017-07-31 11:58:03.897","json":{"orderId":"2779","productId":"5965","product_img_url":"http://image.domolife.cn/platform/20170309/20170309103021763.jpg"},"m_type":15,"m_content":"夕染彩绘全包硅胶防摔苹果手机壳  带支架","m_id":578064}]
     * msg : 请求成功
     * code : 01
     */
    private List<MessageIndentBean> orderMsgInfoList;

    public List<MessageIndentBean> getMessageIndentList() {
        return orderMsgInfoList;
    }

    public void setMessageIndentList(List<MessageIndentBean> orderMsgInfoList) {
        this.orderMsgInfoList = orderMsgInfoList;
    }

    public static class MessageIndentBean {
        private String id;
        private String type;
        private String title;
        private String obj;
        private String content;
        private String ctime;
        private String isRead;
        private String productImgUrl;
        private String androidLink;
        private String buttonName;
        private JsonBean json;

        public JsonBean getJson() {
            return json;
        }

        public void setJson(JsonBean json) {
            this.json = json;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getObj() {
            return obj;
        }

        public void setObj(String obj) {
            this.obj = obj;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getCtime() {
            return ctime;
        }

        public void setCtime(String ctime) {
            this.ctime = ctime;
        }

        public String getIsRead() {
            return isRead;
        }

        public void setIsRead(String isRead) {
            this.isRead = isRead;
        }

        public String getProductImgUrl() {
            return productImgUrl;
        }

        public void setProductImgUrl(String productImgUrl) {
            this.productImgUrl = productImgUrl;
        }

        public String getAndroidLink() {
            return androidLink;
        }

        public void setAndroidLink(String androidLink) {
            this.androidLink = androidLink;
        }

        public String getButtonName() {
            return buttonName;
        }

        public void setButtonName(String buttonName) {
            this.buttonName = buttonName;
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
