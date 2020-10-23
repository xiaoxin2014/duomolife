package com.amkj.dmsh.dominant.bean;

import android.text.TextUtils;

import com.amkj.dmsh.base.BaseTimeEntity;
import com.amkj.dmsh.bean.CommunalDetailBean;

import java.util.List;

/**
 * Created by xiaoxin on 2020/9/27
 * Version:v4.8.0
 */
public class GroupDetailEntity extends BaseTimeEntity {

    /**
     * sysTime : 2020-09-27 17:07:16
     * id : 18324
     * marketPrice : 10000
     * quantity : 100
     * picUrl : http://image.domolife.cn/platform/20200708/20200708111711446.jpeg
     * previsionFlag : 0
     * price : 100
     * maxPrice : 2000
     * title : 脖颈便携风扇
     * subtitle :
     * startTime : 2020-09-24 10:00:00
     * endTime : 2020-09-30 00:00:00
     * recommend : 团长推荐123123131312313123
     * images : http://image.domolife.cn/platform/20200708/20200708111711446.jpeg
     * flashBuyClickCount : 0
     * couponList : [{"name":"bbb","url":"www","price":"13.00","startTime":"2020-09-13 00:00:00","endTime":"2020-09-24 00:00:00"},{"name":"ddd","url":"wwwq","price":"122.00","startTime":"2020-09-23 00:00:00","endTime":"2020-09-24 00:00:00"}]
     * itemBody : []
     * thirdUrl : https://detail.tmall.com/item.htm?spm=a230r.1.14.1.7abd5ce7jAXi32&id=617511429361&ns=1&abbucket=16
     * thirdId :
     */

    private int id;
    private String marketPrice;
    private int quantity;
    private String picUrl;
    private int previsionFlag;
    private String price;
    private String maxPrice;
    private String title;
    private String subtitle;
    private String startTime;
    private String endTime;
    private String recommend;
    private String images;
    private String flashBuyClickCount;
    private String thirdUrl;
    private String thirdId;
    private String videoUrl;
    private List<CouponListBean> couponList;
    private List<CommunalDetailBean> itemBody;
    private String waterRemark;


    public String getWaterRemark() {
        return waterRemark;
    }

    public void setWaterRemark(String waterRemark) {
        this.waterRemark = waterRemark;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public boolean haveVideo() {
        return !TextUtils.isEmpty(videoUrl);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(String marketPrice) {
        this.marketPrice = marketPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public int getPrevisionFlag() {
        return previsionFlag;
    }

    public void setPrevisionFlag(int previsionFlag) {
        this.previsionFlag = previsionFlag;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(String maxPrice) {
        this.maxPrice = maxPrice;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getRecommend() {
        return recommend;
    }

    public void setRecommend(String recommend) {
        this.recommend = recommend;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getFlashBuyClickCount() {
        return flashBuyClickCount;
    }

    public void setFlashBuyClickCount(String flashBuyClickCount) {
        this.flashBuyClickCount = flashBuyClickCount;
    }

    public String getThirdUrl() {
        return thirdUrl;
    }

    public void setThirdUrl(String thirdUrl) {
        this.thirdUrl = thirdUrl;
    }

    public String getThirdId() {
        return thirdId;
    }

    public void setThirdId(String thirdId) {
        this.thirdId = thirdId;
    }

    public List<CouponListBean> getCouponList() {
        return couponList;
    }

    public void setCouponList(List<CouponListBean> couponList) {
        this.couponList = couponList;
    }

    public List<CommunalDetailBean> getItemBody() {
        return itemBody;
    }

    public void setItemBody(List<CommunalDetailBean> itemBody) {
        this.itemBody = itemBody;
    }


    public static class CouponListBean {
        /**
         * name : bbb
         * url : www
         * price : 13.00
         * startTime : 2020-09-13 00:00:00
         * endTime : 2020-09-24 00:00:00
         */

        private String name;
        private String url;
        private String price;
        private String startTime;
        private String endTime;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }
    }
}
