package com.amkj.dmsh.dominant.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.amkj.dmsh.base.BaseEntity;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/6/8
 * class description:请输入类描述
 */

public class QualityGroupMineEntity extends BaseEntity{

    /**
     * result : [{"gpRecordId":30,"gpPicUrl":"http://img.domolife.cn/platform/ZZWGaMTANz.jpg","gpProductId":17,"productId":4282,"payTime":"2017-06-20 19:46:06","gpSkuId":112,"gpInfoId":1,"gpCreateUserId":27928,"propValueId":"5","productSkuValue":"默认:蓝色","gpStatusMsg":"拼团中，还差1人","price":"1098.0","subtitle":null,"name":"北鼎K206钻石电热水壶礼盒装","gpParticipant":27928,"gpPrice":"0.01","gpStatus":1},{"gpRecordId":32,"gpPicUrl":"http://img.domolife.cn/platform/ZZWGaMTANz.jpg","gpProductId":17,"productId":4282,"payTime":"2017-06-20 19:58:52","gpSkuId":112,"gpInfoId":1,"gpCreateUserId":34626,"propValueId":"5","productSkuValue":"默认:蓝色","gpStatusMsg":"拼团中，还差1人","price":"1098.0","subtitle":null,"name":"北鼎K206钻石电热水壶礼盒装","gpParticipant":27928,"gpPrice":"0.01","gpStatus":1},{"gpRecordId":30,"gpPicUrl":"http://img.domolife.cn/platform/ZZWGaMTANz.jpg","gpProductId":17,"productId":4282,"payTime":"","gpSkuId":112,"gpInfoId":1,"gpCreateUserId":27928,"propValueId":"5","productSkuValue":"默认:蓝色","gpStatusMsg":"拼团中，还差1人","price":"1098.0","subtitle":null,"name":"北鼎K206钻石电热水壶礼盒装","gpParticipant":27928,"gpPrice":"0.01","gpStatus":1},{"gpRecordId":32,"gpPicUrl":"http://img.domolife.cn/platform/ZZWGaMTANz.jpg","gpProductId":17,"productId":4282,"payTime":"","gpSkuId":112,"gpInfoId":1,"gpCreateUserId":34626,"propValueId":"5","productSkuValue":"默认:蓝色","gpStatusMsg":"拼团中，还差1人","price":"1098.0","subtitle":null,"name":"北鼎K206钻石电热水壶礼盒装","gpParticipant":27928,"gpPrice":"0.01","gpStatus":1},{"gpRecordId":33,"gpPicUrl":"http://img.domolife.cn/platform/ZZWGaMTANz.jpg","gpProductId":17,"productId":4282,"payTime":"2017-06-20 20:15:40","gpSkuId":112,"gpInfoId":1,"gpCreateUserId":27928,"propValueId":"5","productSkuValue":"默认:蓝色","gpStatusMsg":"拼团中，还差1人","price":"1098.0","subtitle":null,"name":"北鼎K206钻石电热水壶礼盒装","gpParticipant":27928,"gpPrice":"0.01","gpStatus":1},{"gpRecordId":33,"gpPicUrl":"http://img.domolife.cn/platform/ZZWGaMTANz.jpg","gpProductId":17,"productId":4282,"payTime":"","gpSkuId":112,"gpInfoId":1,"gpCreateUserId":27928,"propValueId":"5","productSkuValue":"默认:蓝色","gpStatusMsg":"拼团中，还差1人","price":"1098.0","subtitle":null,"name":"北鼎K206钻石电热水壶礼盒装","gpParticipant":27928,"gpPrice":"0.01","gpStatus":1},{"gpRecordId":34,"gpPicUrl":"http://img.domolife.cn/platform/ZZWGaMTANz.jpg","gpProductId":17,"productId":4282,"payTime":"2017-06-20 20:36:16","gpSkuId":112,"gpInfoId":1,"gpCreateUserId":27928,"propValueId":"5","productSkuValue":"默认:蓝色","gpStatusMsg":"拼团中，还差1人","price":"1098.0","subtitle":null,"name":"北鼎K206钻石电热水壶礼盒装","gpParticipant":34626,"gpPrice":"0.01","gpStatus":1},{"gpRecordId":34,"gpPicUrl":"http://img.domolife.cn/platform/ZZWGaMTANz.jpg","gpProductId":17,"productId":4282,"payTime":"","gpSkuId":112,"gpInfoId":1,"gpCreateUserId":27928,"propValueId":"5","productSkuValue":"默认:蓝色","gpStatusMsg":"拼团中，还差1人","price":"1098.0","subtitle":null,"name":"北鼎K206钻石电热水壶礼盒装","gpParticipant":34626,"gpPrice":"0.01","gpStatus":1},{"gpRecordId":35,"gpPicUrl":"http://img.domolife.cn/platform/ZZWGaMTANz.jpg","gpProductId":17,"productId":4282,"payTime":"2017-06-21 10:50:15","gpSkuId":112,"gpInfoId":1,"gpCreateUserId":27928,"propValueId":"5","productSkuValue":"默认:蓝色","gpStatusMsg":"拼团成功，请等待发货","price":"1098.0","subtitle":null,"name":"北鼎K206钻石电热水壶礼盒装","gpParticipant":34626,"gpPrice":"0.01","gpStatus":2},{"gpRecordId":36,"gpPicUrl":"http://img.domolife.cn/platform/XPNthK5KMp.jpg","gpProductId":20,"productId":5965,"payTime":"2017-06-21 11:30:11","gpSkuId":757,"gpInfoId":3,"gpCreateUserId":27928,"propValueId":"335,322","productSkuValue":"默认:夕染,规格:iPhone6s","gpStatusMsg":"拼团中，还差1人","price":"0.01","subtitle":null,"name":"夕染彩绘全包硅胶防摔苹果手机壳  带支架","gpParticipant":0,"gpPrice":"0.1","gpStatus":1}]
     * currentTime : 2017-06-22 15:05:42
     * msg : 请求成功
     * code : 01
     * second : 900
     * status : {"1":"已开团","2":"已完成","3":"开团待支付","4":"参团待支付","5":"已过期"}
     */

    private String currentTime;
    private String second;
    private Map<String, String> status;
    @SerializedName("result")
    private List<QualityGroupMineBean> qualityGroupMineBeanList;

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    public String getSecond() {
        return second;
    }

    public void setSecond(String second) {
        this.second = second;
    }

    public Map<String, String> getStatus() {
        return status;
    }

    public void setStatus(Map<String, String> status) {
        this.status = status;
    }

    public List<QualityGroupMineBean> getQualityGroupMineBeanList() {
        return qualityGroupMineBeanList;
    }

    public void setQualityGroupMineBeanList(List<QualityGroupMineBean> qualityGroupMineBeanList) {
        this.qualityGroupMineBeanList = qualityGroupMineBeanList;
    }

    public static class QualityGroupMineBean implements Parcelable {
        /**
         * gpRecordId : 30
         * gpPicUrl : http://img.domolife.cn/platform/ZZWGaMTANz.jpg
         * gpProductId : 17
         * productId : 4282
         * payTime : 2017-06-20 19:46:06
         * gpSkuId : 112
         * gpInfoId : 1
         * gpCreateUserId : 27928
         * propValueId : 5
         * productSkuValue : 默认:蓝色
         * gpStatusMsg : 拼团中，还差1人
         * price : 1098.0
         * subtitle : null
         * name : 北鼎K206钻石电热水壶礼盒装
         * gpParticipant : 27928
         * gpPrice : 0.01
         * gpStatus : 1
         */

        private int gpRecordId;
        private String gpPicUrl;
        private int gpProductId;
        private int productId;
        private String payTime;
        private int gpSkuId;
        private int gpInfoId;
        private int gpCreateUserId;
        private String propValueId;
        private String productSkuValue;
        private String gpStatusMsg;
        private String price;
        private String subtitle;
        private String name;
        private int gpParticipant;
        private String gpPrice;
        private int gpStatus;
        private String createTime;
        private String orderNo;
        private int leftParticipant;

        public int getLeftParticipant() {
            return leftParticipant;
        }

        public void setLeftParticipant(int leftParticipant) {
            this.leftParticipant = leftParticipant;
        }

        public String getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(String orderNo) {
            this.orderNo = orderNo;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public int getGpRecordId() {
            return gpRecordId;
        }

        public void setGpRecordId(int gpRecordId) {
            this.gpRecordId = gpRecordId;
        }

        public String getGpPicUrl() {
            return gpPicUrl;
        }

        public void setGpPicUrl(String gpPicUrl) {
            this.gpPicUrl = gpPicUrl;
        }

        public int getGpProductId() {
            return gpProductId;
        }

        public void setGpProductId(int gpProductId) {
            this.gpProductId = gpProductId;
        }

        public int getProductId() {
            return productId;
        }

        public void setProductId(int productId) {
            this.productId = productId;
        }

        public String getPayTime() {
            return payTime;
        }

        public void setPayTime(String payTime) {
            this.payTime = payTime;
        }

        public int getGpSkuId() {
            return gpSkuId;
        }

        public void setGpSkuId(int gpSkuId) {
            this.gpSkuId = gpSkuId;
        }

        public int getGpInfoId() {
            return gpInfoId;
        }

        public void setGpInfoId(int gpInfoId) {
            this.gpInfoId = gpInfoId;
        }

        public int getGpCreateUserId() {
            return gpCreateUserId;
        }

        public void setGpCreateUserId(int gpCreateUserId) {
            this.gpCreateUserId = gpCreateUserId;
        }

        public String getPropValueId() {
            return propValueId;
        }

        public void setPropValueId(String propValueId) {
            this.propValueId = propValueId;
        }

        public String getProductSkuValue() {
            return productSkuValue;
        }

        public void setProductSkuValue(String productSkuValue) {
            this.productSkuValue = productSkuValue;
        }

        public String getGpStatusMsg() {
            return gpStatusMsg;
        }

        public void setGpStatusMsg(String gpStatusMsg) {
            this.gpStatusMsg = gpStatusMsg;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getSubtitle() {
            return subtitle;
        }

        public void setSubtitle(String subtitle) {
            this.subtitle = subtitle;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getGpParticipant() {
            return gpParticipant;
        }

        public void setGpParticipant(int gpParticipant) {
            this.gpParticipant = gpParticipant;
        }

        public String getGpPrice() {
            return gpPrice;
        }

        public void setGpPrice(String gpPrice) {
            this.gpPrice = gpPrice;
        }

        public int getGpStatus() {
            return gpStatus;
        }

        public void setGpStatus(int gpStatus) {
            this.gpStatus = gpStatus;
        }

        public QualityGroupMineBean() {
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.gpRecordId);
            dest.writeString(this.gpPicUrl);
            dest.writeInt(this.gpProductId);
            dest.writeInt(this.productId);
            dest.writeString(this.payTime);
            dest.writeInt(this.gpSkuId);
            dest.writeInt(this.gpInfoId);
            dest.writeInt(this.gpCreateUserId);
            dest.writeString(this.propValueId);
            dest.writeString(this.productSkuValue);
            dest.writeString(this.gpStatusMsg);
            dest.writeString(this.price);
            dest.writeString(this.subtitle);
            dest.writeString(this.name);
            dest.writeInt(this.gpParticipant);
            dest.writeString(this.gpPrice);
            dest.writeInt(this.gpStatus);
            dest.writeString(this.createTime);
            dest.writeString(this.orderNo);
            dest.writeInt(this.leftParticipant);
        }

        protected QualityGroupMineBean(Parcel in) {
            this.gpRecordId = in.readInt();
            this.gpPicUrl = in.readString();
            this.gpProductId = in.readInt();
            this.productId = in.readInt();
            this.payTime = in.readString();
            this.gpSkuId = in.readInt();
            this.gpInfoId = in.readInt();
            this.gpCreateUserId = in.readInt();
            this.propValueId = in.readString();
            this.productSkuValue = in.readString();
            this.gpStatusMsg = in.readString();
            this.price = in.readString();
            this.subtitle = in.readString();
            this.name = in.readString();
            this.gpParticipant = in.readInt();
            this.gpPrice = in.readString();
            this.gpStatus = in.readInt();
            this.createTime = in.readString();
            this.orderNo = in.readString();
            this.leftParticipant = in.readInt();
        }

        public static final Creator<QualityGroupMineBean> CREATOR = new Creator<QualityGroupMineBean>() {
            @Override
            public QualityGroupMineBean createFromParcel(Parcel source) {
                return new QualityGroupMineBean(source);
            }

            @Override
            public QualityGroupMineBean[] newArray(int size) {
                return new QualityGroupMineBean[size];
            }
        };
    }
}
