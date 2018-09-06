package com.amkj.dmsh.shopdetails.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.amkj.dmsh.base.BaseEntity;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

/**
 * Created by atd48 on 2016/11/1.
 */
public class DirectReturnRecordEntity extends BaseEntity{

    @SerializedName("result")
    private DirectReturnRecordBean directReturnRecordBean;
    /**
     * result : {"orderList":[{"picUrl":"http://img.domolife.cn/platform/5Emk4KG5R5.jpg","count":1,"reason":"看到减肥","status":-40,"no":"cX23295X1099X1477738961296","refundCompleteTime":null,"couponPrice":0,"expressCompany":"圆通速递","refundTime":"2016-10-31 11:06:11","id":4159,"content":"放假就放假","integralPrice":0,"price":"4.00","commentTime":"2016-10-29 21:03:04","name":"老板椅","orderProductId":5,"expressNo":"9890208887952","deliverType":1,"saleSkuValue":"默认:卧室","completeTime":"2016-10-29 19:41:26","deliverTime":"2016-10-29 19:05:18","logistics":null},{"picUrl":"http://img.domolife.cn/platform/5Emk4KG5R5.jpg","count":1,"reason":"南昌警方","status":-40,"no":"cX23295X1099X1477877861792","refundCompleteTime":null,"couponPrice":0,"expressCompany":"中通速递","refundTime":"2016-10-31 11:06:19","id":4159,"content":"坚持坚持减肥","integralPrice":0,"price":"4.00","commentTime":null,"name":"老板椅","orderProductId":9,"expressNo":"2345647","deliverType":1,"saleSkuValue":"默认:卧室","completeTime":"2016-10-31 11:04:53","deliverTime":"2016-10-31 09:46:27","logistics":null},{"picUrl":"http://img.domolife.cn/platform/5Emk4KG5R5.jpg","count":1,"reason":"参加选举","status":-40,"no":"cX23295X1099X1477878937256","refundCompleteTime":null,"couponPrice":0,"expressCompany":"中通速递","refundTime":"2016-10-31 11:06:27","id":4159,"content":"参考参考参考","integralPrice":0,"price":"4.00","commentTime":null,"name":"老板椅","orderProductId":10,"expressNo":"532192452617","deliverType":1,"saleSkuValue":"默认:卧室","completeTime":"2016-10-31 11:04:48","deliverTime":"2016-10-31 09:59:56","logistics":"{\"status\":\"0\",\"msg\":\"ok\",\"result\":{\"number\":\"532192452617\",\"type\":\"zto\",\"list\":[{\"time\":\"2016-10-31 12:58:01\",\"status\":\"[东莞市]  快件已在 东莞虎门 签收  签收照片,感谢您使用中通快递，期待再次为您服务!\"},{\"time\":\"2016-10-29 07:20:14\",\"status\":\"[东莞市] 东莞虎门 的 杨新刚[13622604166] 正在派件\"},{\"time\":\"2016-10-29 04:35:17\",\"status\":\"[东莞市] 快件已经到达 东莞虎门\"},{\"time\":\"2016-10-29 02:28:12\",\"status\":\"[东莞市] 快件离开 东莞中心 已发往 东莞虎门\"},{\"time\":\"2016-10-29 01:28:25\",\"status\":\"[东莞市] 快件已经到达 东莞中心\"},{\"time\":\"2016-10-29 00:20:41\",\"status\":\"[深圳市] 快件离开 深圳中心 已发往 东莞中心\"},{\"time\":\"2016-10-29 00:08:24\",\"status\":\"[深圳市] 快件已经到达 深圳中心\"},{\"time\":\"2016-10-28 20:30:23\",\"status\":\"[深圳市] 快件离开 福田梅林 已发往 东莞中心\"},{\"time\":\"2016-10-28 19:10:02\",\"status\":\"[深圳市] 福田梅林 的  中通快递8  已收件 \"}],\"deliverystatus\":\"3\",\"issign\":\"1\"}}"},{"picUrl":"http://img.domolife.cn/platform/5Emk4KG5R5.jpg","count":1,"reason":"曾经的经典xnjd","status":-40,"no":"cX23295X1099X1477882130217","refundCompleteTime":null,"couponPrice":0,"expressCompany":"中通速递","refundTime":"2016-10-31 11:06:35","id":4159,"content":"继续坚持坚持","integralPrice":0,"price":"3.00","commentTime":null,"name":"老板椅","orderProductId":11,"expressNo":"1901572627068","deliverType":1,"saleSkuValue":"默认:客厅","completeTime":"2016-10-31 11:04:46","deliverTime":"2016-10-31 10:53:55","logistics":"{\"status\":\"0\",\"msg\":\"ok\",\"result\":{\"number\":\"1901572627068\",\"type\":\"yunda\",\"list\":[{\"time\":\"2016-08-13 12:57:21\",\"status\":\"在广东深圳公司福田区上梅林分部进行签收扫描，快件已被 已签收 签收\"},{\"time\":\"2016-08-13 12:53:06\",\"status\":\"在广东深圳公司福田区上梅林分部进行派件扫描；派送业务员：王武；联系电话：13926574144\"},{\"time\":\"2016-08-13 08:05:24\",\"status\":\"在广东深圳公司福田区梅林分拨分部进行发出扫描，将发往：广东深圳公司福田区上梅林分部\"},{\"time\":\"2016-08-12 18:36:17\",\"status\":\"在广东深圳公司进行发出扫描，将发往：广东深圳公司福田区梅林分拨分部\"},{\"time\":\"2016-08-12 18:27:01\",\"status\":\"在分拨中心广东深圳公司进行卸车扫描\"},{\"time\":\"2016-08-12 01:05:53\",\"status\":\"在福建晋江分拨中心进行装车扫描，即将发往：广东深圳公司\"},{\"time\":\"2016-08-12 01:05:24\",\"status\":\"在分拨中心福建晋江分拨中心进行称重扫描\"},{\"time\":\"2016-08-11 19:53:02\",\"status\":\"在福建德化县公司进行发出扫描，将发往：福建晋江分拨中心\"},{\"time\":\"2016-08-11 19:43:57\",\"status\":\"在福建德化县公司进行下级地点扫描，即将发往：广东深圳公司\"},{\"time\":\"2016-08-11 19:32:31\",\"status\":\"在福建德化县公司进行揽件扫描\"}],\"deliverystatus\":\"3\",\"issign\":\"1\"}}"},{"picUrl":"http://img.domolife.cn/platform/5Emk4KG5R5.jpg","count":1,"reason":"细节进行","status":-40,"no":"cX23295X1099X1477882130217","refundCompleteTime":null,"couponPrice":0,"expressCompany":"中通速递","refundTime":"2016-10-31 11:06:44","id":4159,"content":"坚持坚持就","integralPrice":0,"price":"2.00","commentTime":null,"name":"老板椅","orderProductId":12,"expressNo":"1901572627068","deliverType":1,"saleSkuValue":"默认:厨房","completeTime":"2016-10-31 11:04:46","deliverTime":"2016-10-31 10:53:55","logistics":null},{"picUrl":"http://img.domolife.cn/platform/5Emk4KG5R5.jpg","count":1,"reason":"夏季开车开的","status":-40,"no":"cX23295X1099X1477882130217","refundCompleteTime":null,"couponPrice":0,"expressCompany":"中通速递","refundTime":"2016-10-31 11:06:50","id":4159,"content":"新疆开心开心","integralPrice":0,"price":"4.00","commentTime":null,"name":"老板椅","orderProductId":13,"expressNo":"1901572627068","deliverType":1,"saleSkuValue":"默认:卧室","completeTime":"2016-10-31 11:04:46","deliverTime":"2016-10-31 10:53:55","logistics":null},{"picUrl":"http://img.domolife.cn/platform/5Emk4KG5R5.jpg","count":1,"reason":"但看见的","status":-40,"no":"cX23295X1099X1477882173117","refundCompleteTime":null,"couponPrice":0,"expressCompany":"圆通速递","refundTime":"2016-10-31 11:15:14","id":4159,"content":"继续进行","integralPrice":0,"price":"2.00","commentTime":null,"name":"老板椅","orderProductId":14,"expressNo":"882500969432679338","deliverType":1,"saleSkuValue":"默认:厨房","completeTime":"2016-10-31 11:04:43","deliverTime":"2016-10-31 10:54:07","logistics":"{\"status\":\"0\",\"msg\":\"ok\",\"result\":{\"number\":\"882500969432679338\",\"type\":\"yto\",\"list\":[{\"time\":\"2016-08-15 12:45:32\",\"status\":\"客户 签收人 : 前台 已签收  感谢使用圆通速递，期待再次为您服务\"},{\"time\":\"2016-08-14 17:39:09\",\"status\":\"派送不成功，企事业单位，工作日续送。\"},{\"time\":\"2016-08-14 17:28:52\",\"status\":\"广东省深圳市梅林公司(点击查询电话)刘** 派件中 派件员电话18926408061&nbsp;&nbsp;\"},{\"time\":\"2016-08-14 15:39:29\",\"status\":\"广东省深圳市梅林公司 已收入\"},{\"time\":\"2016-08-14 11:24:41\",\"status\":\"深圳转运中心 已发出,下一站 广东省深圳市梅林\"},{\"time\":\"2016-08-13 22:00:13\",\"status\":\"广东省汕头市公司 已打包\"},{\"time\":\"2016-08-13 21:49:17\",\"status\":\"广东省汕头市公司(点击查询电话) 已揽收&nbsp;&nbsp;\"}],\"deliverystatus\":\"3\",\"issign\":\"1\"}}"},{"picUrl":"http://img.domolife.cn/platform/5Emk4KG5R5.jpg","count":1,"reason":"大家快点开","status":-40,"no":"cX23295X1099X1477882173117","refundCompleteTime":null,"couponPrice":0,"expressCompany":"圆通速递","refundTime":"2016-10-31 11:15:31","id":4159,"content":"ID快点开","integralPrice":0,"price":"3.00","commentTime":null,"name":"老板椅","orderProductId":15,"expressNo":"882500969432679338","deliverType":1,"saleSkuValue":"默认:客厅","completeTime":"2016-10-31 11:04:43","deliverTime":"2016-10-31 10:54:07","logistics":null},{"picUrl":"http://img.domolife.cn/platform/5Emk4KG5R5.jpg","count":1,"reason":"地对方","status":-40,"no":"cX23295X1099X1477882173117","refundCompleteTime":null,"couponPrice":0,"expressCompany":"中通速递","refundTime":"2016-10-31 11:15:40","id":4159,"content":"udjdjf","integralPrice":0,"price":"4.00","commentTime":null,"name":"老板椅","orderProductId":16,"expressNo":"70141107146139","deliverType":1,"saleSkuValue":"默认:卧室","completeTime":"2016-10-31 11:04:43","deliverTime":"2016-10-31 10:54:32","logistics":"{\"status\":\"0\",\"msg\":\"ok\",\"result\":{\"number\":\"70141107146139\",\"type\":\"htky\",\"list\":[{\"time\":\"2016-08-17 21:58:12\",\"status\":\"【已签收，签收人是已送18682375538】\"},{\"time\":\"2016-08-17 17:44:48\",\"status\":\"深圳市【深圳福田梅林分部】，【张县委\\/18682375538】正在派件\"},{\"time\":\"2016-08-17 08:35:45\",\"status\":\"到深圳市【深圳福田梅林分部】\"},{\"time\":\"2016-08-17 05:29:37\",\"status\":\"深圳市【深圳转运中心】，正发往【深圳福田梅林分部】\"},{\"time\":\"2016-08-17 04:41:36\",\"status\":\"到深圳市【深圳转运中心】\"},{\"time\":\"2016-08-17 04:41:36\",\"status\":\"到深圳市【深圳转运中心】\"},{\"time\":\"2016-08-17 02:21:52\",\"status\":\"东莞市【虎门转运中心】，正发往【深圳转运中心】\"},{\"time\":\"2016-08-17 01:14:00\",\"status\":\"到东莞市【虎门转运中心】\"},{\"time\":\"2016-08-15 20:46:47\",\"status\":\"合肥市【合肥转运中心】，正发往【虎门转运中心】\"},{\"time\":\"2016-08-15 20:44:00\",\"status\":\"到合肥市【合肥转运中心】\"},{\"time\":\"2016-08-15 14:02:45\",\"status\":\"【蜀山七部】揽收成功\"}],\"deliverystatus\":\"3\",\"issign\":\"1\"}}"},{"picUrl":"http://img.domolife.cn/platform/5Emk4KG5R5.jpg","count":1,"reason":"递解出境减肥","status":-40,"no":"cX23295X1099X1477882204310","refundCompleteTime":null,"couponPrice":0,"expressCompany":"圆通速递","refundTime":"2016-10-31 11:15:47","id":4159,"content":"放减肥咖啡","integralPrice":0,"price":"3.00","commentTime":null,"name":"老板椅","orderProductId":17,"expressNo":"9890195569553","deliverType":1,"saleSkuValue":"默认:客厅","completeTime":"2016-10-31 11:04:41","deliverTime":"2016-10-31 10:54:47","logistics":null}]}
     * code : 01
     * msg : 请求成功
     */

    public DirectReturnRecordBean getDirectReturnRecordBean() {
        return directReturnRecordBean;
    }

    public void setDirectReturnRecordBean(DirectReturnRecordBean directReturnRecordBean) {
        this.directReturnRecordBean = directReturnRecordBean;
    }

    public static class DirectReturnRecordBean {
        /**
         * picUrl : http://img.domolife.cn/platform/5Emk4KG5R5.jpg
         * count : 1
         * reason : 看到减肥
         * status : -40
         * no : cX23295X1099X1477738961296
         * refundCompleteTime : null
         * couponPrice : 0
         * expressCompany : 圆通速递
         * refundTime : 2016-10-31 11:06:11
         * id : 4159
         * content : 放假就放假
         * integralPrice : 0
         * price : 4.00
         * commentTime : 2016-10-29 21:03:04
         * name : 老板椅
         * orderProductId : 5
         * expressNo : 9890208887952
         * deliverType : 1
         * saleSkuValue : 默认:卧室
         * completeTime : 2016-10-29 19:41:26
         * deliverTime : 2016-10-29 19:05:18
         * logistics : null
         */

        private List<OrderListBean> orderList;
        private Map<String, String> status;

        public Map<String, String> getStatus() {
            return status;
        }

        public void setStatus(Map<String, String> status) {
            this.status = status;
        }

        public List<OrderListBean> getOrderList() {
            return orderList;
        }

        public void setOrderList(List<OrderListBean> orderList) {
            this.orderList = orderList;
        }

        public static class OrderListBean implements Parcelable {
            private String picUrl;
            private int count;
            private String reason;
            private int status;
            private String no;
            private String refundCompleteTime;
            private int couponPrice;
            private String expressCompany;
            private String refundTime;
            private String refundPrice;
            private int id;
            private String content;
            private int integralPrice;
            private String price;
            private String commentTime;
            private String name;
            private int orderProductId;
            private String expressNo;
            private int deliverType;
            private String saleSkuValue;
            private String completeTime;
            private String deliverTime;
            private String orderRefundProductId;

            public String getOrderRefundProductId() {
                return orderRefundProductId;
            }

            public void setOrderRefundProductId(String orderRefundProductId) {
                this.orderRefundProductId = orderRefundProductId;
            }

            public String getRefundPrice() {
                return refundPrice;
            }

            public void setRefundPrice(String refundPrice) {
                this.refundPrice = refundPrice;
            }

            public String getPicUrl() {
                return picUrl;
            }

            public void setPicUrl(String picUrl) {
                this.picUrl = picUrl;
            }

            public int getCount() {
                return count;
            }

            public void setCount(int count) {
                this.count = count;
            }

            public String getReason() {
                return reason;
            }

            public void setReason(String reason) {
                this.reason = reason;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public String getNo() {
                return no;
            }

            public void setNo(String no) {
                this.no = no;
            }

            public String getRefundCompleteTime() {
                return refundCompleteTime;
            }

            public void setRefundCompleteTime(String refundCompleteTime) {
                this.refundCompleteTime = refundCompleteTime;
            }

            public int getCouponPrice() {
                return couponPrice;
            }

            public void setCouponPrice(int couponPrice) {
                this.couponPrice = couponPrice;
            }

            public String getExpressCompany() {
                return expressCompany;
            }

            public void setExpressCompany(String expressCompany) {
                this.expressCompany = expressCompany;
            }

            public String getRefundTime() {
                return refundTime;
            }

            public void setRefundTime(String refundTime) {
                this.refundTime = refundTime;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public int getIntegralPrice() {
                return integralPrice;
            }

            public void setIntegralPrice(int integralPrice) {
                this.integralPrice = integralPrice;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public String getCommentTime() {
                return commentTime;
            }

            public void setCommentTime(String commentTime) {
                this.commentTime = commentTime;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getOrderProductId() {
                return orderProductId;
            }

            public void setOrderProductId(int orderProductId) {
                this.orderProductId = orderProductId;
            }

            public String getExpressNo() {
                return expressNo;
            }

            public void setExpressNo(String expressNo) {
                this.expressNo = expressNo;
            }

            public int getDeliverType() {
                return deliverType;
            }

            public void setDeliverType(int deliverType) {
                this.deliverType = deliverType;
            }

            public String getSaleSkuValue() {
                return saleSkuValue;
            }

            public void setSaleSkuValue(String saleSkuValue) {
                this.saleSkuValue = saleSkuValue;
            }

            public String getCompleteTime() {
                return completeTime;
            }

            public void setCompleteTime(String completeTime) {
                this.completeTime = completeTime;
            }

            public String getDeliverTime() {
                return deliverTime;
            }

            public void setDeliverTime(String deliverTime) {
                this.deliverTime = deliverTime;
            }

            public OrderListBean() {
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.picUrl);
                dest.writeInt(this.count);
                dest.writeString(this.reason);
                dest.writeInt(this.status);
                dest.writeString(this.no);
                dest.writeString(this.refundCompleteTime);
                dest.writeInt(this.couponPrice);
                dest.writeString(this.expressCompany);
                dest.writeString(this.refundTime);
                dest.writeString(this.refundPrice);
                dest.writeInt(this.id);
                dest.writeString(this.content);
                dest.writeInt(this.integralPrice);
                dest.writeString(this.price);
                dest.writeString(this.commentTime);
                dest.writeString(this.name);
                dest.writeInt(this.orderProductId);
                dest.writeString(this.expressNo);
                dest.writeInt(this.deliverType);
                dest.writeString(this.saleSkuValue);
                dest.writeString(this.completeTime);
                dest.writeString(this.deliverTime);
                dest.writeString(this.orderRefundProductId);
            }

            protected OrderListBean(Parcel in) {
                this.picUrl = in.readString();
                this.count = in.readInt();
                this.reason = in.readString();
                this.status = in.readInt();
                this.no = in.readString();
                this.refundCompleteTime = in.readString();
                this.couponPrice = in.readInt();
                this.expressCompany = in.readString();
                this.refundTime = in.readString();
                this.refundPrice = in.readString();
                this.id = in.readInt();
                this.content = in.readString();
                this.integralPrice = in.readInt();
                this.price = in.readString();
                this.commentTime = in.readString();
                this.name = in.readString();
                this.orderProductId = in.readInt();
                this.expressNo = in.readString();
                this.deliverType = in.readInt();
                this.saleSkuValue = in.readString();
                this.completeTime = in.readString();
                this.deliverTime = in.readString();
                this.orderRefundProductId = in.readString();
            }

            public static final Creator<OrderListBean> CREATOR = new Creator<OrderListBean>() {
                @Override
                public OrderListBean createFromParcel(Parcel source) {
                    return new OrderListBean(source);
                }

                @Override
                public OrderListBean[] newArray(int size) {
                    return new OrderListBean[size];
                }
            };
        }
    }
}
