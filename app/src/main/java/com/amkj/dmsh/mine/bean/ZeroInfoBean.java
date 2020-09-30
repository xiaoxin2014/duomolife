package com.amkj.dmsh.mine.bean;


public class ZeroInfoBean {
    /**
     * productId : 17486
     * productName : 蜡笔小新手机卡扣式导航车载支架
     * subtitle :
     * productImg : http://image.domolife.cn/platform/20200806/20200806153914217.jpeg
     * count : 10
     * activityId : 1
     * marketPrice : 45
     */

    private String productId;
    private String productName;
    private String subtitle;
    private String productImg;
    private String count;
    private String activityId;
    private String marketPrice;
    private String partakeCount;
    private String isPartake;

    public boolean isPartake() {
        return "1".equals(isPartake);
    }

    public void setIsPartake(String isPartake) {
        this.isPartake = isPartake;
    }

    public String getPartakeCount() {
        return partakeCount;
    }

    public void setPartakeCount(String partakeCount) {
        this.partakeCount = partakeCount;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getProductImg() {
        return productImg;
    }

    public void setProductImg(String productImg) {
        this.productImg = productImg;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(String marketPrice) {
        this.marketPrice = marketPrice;
    }
}
