package com.amkj.dmsh.dominant.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.amkj.dmsh.base.BaseEntity;
import com.amkj.dmsh.constant.ConstantMethod;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/6/8
 * class description:请输入类描述
 */

public class QualityGroupMineEntity extends BaseEntity {

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
    private Map<String, String> statusMap;
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
        return statusMap;
    }

    public void setStatus(Map<String, String> status) {
        this.statusMap = status;
    }

    public List<QualityGroupMineBean> getQualityGroupMineBeanList() {
        return qualityGroupMineBeanList;
    }

    public void setQualityGroupMineBeanList(List<QualityGroupMineBean> qualityGroupMineBeanList) {
        this.qualityGroupMineBeanList = qualityGroupMineBeanList;
    }

    public static class QualityGroupMineBean implements Parcelable {
        private String coverImage;
        private String createTime;
        private String gpInfoId;
        private String gpRecordId;
        private String gpStatus;
        private String orderNo;
        private String productName;
        private String productSkuValue;
        private String gpStatusMsg;
        private String remainNum;
        private String productId;

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public int getRemainNum() {
            return ConstantMethod.getStringChangeIntegers(remainNum);
        }

        public void setRemainNum(String remainNum) {
            this.remainNum = remainNum;
        }

        public String getGpStatusMsg() {
            return gpStatusMsg;
        }

        public void setGpStatusMsg(String gpStatusMsg) {
            this.gpStatusMsg = gpStatusMsg;
        }

        public String getCoverImage() {
            return coverImage;
        }

        public void setCoverImage(String coverImage) {
            this.coverImage = coverImage;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public int getGpInfoId() {
            return ConstantMethod.getStringChangeIntegers(gpInfoId);
        }

        public void setGpInfoId(String gpInfoId) {
            this.gpInfoId = gpInfoId;
        }

        public String getGpRecordId() {
            return gpRecordId;
        }

        public void setGpRecordId(String gpRecordId) {
            this.gpRecordId = gpRecordId;
        }

        public int getGpStatus() {
            return ConstantMethod.getStringChangeIntegers(gpStatus);
        }

        public void setGpStatus(String gpStatus) {
            this.gpStatus = gpStatus;
        }

        public String getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(String orderNo) {
            this.orderNo = orderNo;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public String getProductSkuValue() {
            return productSkuValue;
        }

        public void setProductSkuValue(String productSkuValue) {
            this.productSkuValue = productSkuValue;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.coverImage);
            dest.writeString(this.createTime);
            dest.writeString(this.gpInfoId);
            dest.writeString(this.gpRecordId);
            dest.writeString(this.gpStatus);
            dest.writeString(this.orderNo);
            dest.writeString(this.productName);
            dest.writeString(this.productSkuValue);
            dest.writeString(this.gpStatusMsg);
        }

        public QualityGroupMineBean() {
        }

        protected QualityGroupMineBean(Parcel in) {
            this.coverImage = in.readString();
            this.createTime = in.readString();
            this.gpInfoId = in.readString();
            this.gpRecordId = in.readString();
            this.gpStatus = in.readString();
            this.orderNo = in.readString();
            this.productName = in.readString();
            this.productSkuValue = in.readString();
            this.gpStatusMsg = in.readString();
        }

        public static final Parcelable.Creator<QualityGroupMineBean> CREATOR = new Parcelable.Creator<QualityGroupMineBean>() {
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
