package com.amkj.dmsh.homepage.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.amkj.dmsh.base.BaseEntity;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by xiaoxin on 2019/7/17
 * Version:v4.1.0
 * ClassDescription :发现-评分商品列表实体类
 */
public class ScoreGoodsEntity extends BaseEntity {
    /**
     * totalResult : 0
     * list : [{"orderProductId":454409,"cover":"http://image.domolife.cn/platform/WnpXE7crx51555346719016.jpg","productId":18067,"orderNo":"cX266978X87254X1563846703975"},{"orderProductId":454041,"cover":"http://image.domolife.cn/platform/fDBRsQfi681531295864828.jpg","productId":14493,"orderNo":"cX266978X87254X1562296244070"}]
     * maxRewardTip : 最高可获得150积分+20元券
     */

    private int totalResult;
    private String maxRewardTip;
    private String contentReminder;//编辑框导语
    private String rewardTip;
    @SerializedName("list")
    private List<ScoreGoodsBean> goodsList;

    public List<ScoreGoodsBean> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<ScoreGoodsBean> goodsList) {
        this.goodsList = goodsList;
    }

    public String getContentReminder() {
        return contentReminder;
    }

    public void setContentReminder(String contentReminder) {
        this.contentReminder = contentReminder;
    }

    public String getRewardTip() {
        return rewardTip;
    }

    public void setRewardTip(String rewardTip) {
        this.rewardTip = rewardTip;
    }

    public int getTotalResult() {
        return totalResult;
    }

    public void setTotalResult(int totalResult) {
        this.totalResult = totalResult;
    }

    public String getMaxRewardTip() {
        return maxRewardTip;
    }

    public void setMaxRewardTip(String maxRewardTip) {
        this.maxRewardTip = maxRewardTip;
    }

    public static class ScoreGoodsBean implements Parcelable {

        /**
         * orderProductId : 454409
         * cover : http://image.domolife.cn/platform/WnpXE7crx51555346719016.jpg
         * productId : 18067
         * orderNo : cX266978X87254X1563846703975
         */

        private String orderProductId;
        private String cover;
        private String productId;
        private String orderNo;
        private String productName;
        private String skuValueText;
        private int count;
        private String price;
        private int position = -2;

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getSkuValueText() {
            return skuValueText;
        }

        public void setSkuValueText(String skuValueText) {
            this.skuValueText = skuValueText;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public String getOrderProductId() {
            return orderProductId;
        }

        public void setOrderProductId(String orderProductId) {
            this.orderProductId = orderProductId;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public String getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(String orderNo) {
            this.orderNo = orderNo;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.orderProductId);
            dest.writeString(this.cover);
            dest.writeString(this.productId);
            dest.writeString(this.orderNo);
            dest.writeString(this.productName);
            dest.writeString(this.skuValueText);
            dest.writeInt(this.count);
            dest.writeString(this.price);
            dest.writeInt(this.position);
        }

        public ScoreGoodsBean() {
        }

        protected ScoreGoodsBean(Parcel in) {
            this.orderProductId = in.readString();
            this.cover = in.readString();
            this.productId = in.readString();
            this.orderNo = in.readString();
            this.productName = in.readString();
            this.skuValueText = in.readString();
            this.count = in.readInt();
            this.price = in.readString();
            this.position = in.readInt();
        }

        public static final Parcelable.Creator<ScoreGoodsBean> CREATOR = new Parcelable.Creator<ScoreGoodsBean>() {
            @Override
            public ScoreGoodsBean createFromParcel(Parcel source) {
                return new ScoreGoodsBean(source);
            }

            @Override
            public ScoreGoodsBean[] newArray(int size) {
                return new ScoreGoodsBean[size];
            }
        };
    }
}
