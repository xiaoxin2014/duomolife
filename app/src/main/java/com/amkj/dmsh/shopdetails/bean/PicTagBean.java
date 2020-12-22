package com.amkj.dmsh.shopdetails.bean;

/**
 * Created by xiaoxin on 2020/12/18
 * Version:v4.8.2
 * ClassDescription :图片标签信息
 */
public class PicTagBean {

    /**
     * tagTop : 30.20
     * androidLink : app://ShopScrollDetailsActivity?productId=4305
     * wxLink : /pages/goodsDetails/goodsDetails?id=4305
     * webLink : http://www.domolife.cn/m/template/common/proprietary.html?id=4305
     * iosLink : app://DMLGoodsProductsInfoViewController?goodsId=4305
     * tagLeft : 31.60
     * tagName : 规划和
     */

    private double tagTop;
    private double tagLeft;
    private String androidLink;
    private String wxLink;
    private String webLink;
    private String iosLink;
    private String tagName;

    public double getTagTop() {
        return tagTop;
    }

    public void setTagTop(double tagTop) {
        this.tagTop = tagTop;
    }

    public double getTagLeft() {
        return tagLeft;
    }

    public void setTagLeft(double tagLeft) {
        this.tagLeft = tagLeft;
    }


    public String getAndroidLink() {
        return androidLink;
    }

    public void setAndroidLink(String androidLink) {
        this.androidLink = androidLink;
    }

    public String getWxLink() {
        return wxLink;
    }

    public void setWxLink(String wxLink) {
        this.wxLink = wxLink;
    }

    public String getWebLink() {
        return webLink;
    }

    public void setWebLink(String webLink) {
        this.webLink = webLink;
    }

    public String getIosLink() {
        return iosLink;
    }

    public void setIosLink(String iosLink) {
        this.iosLink = iosLink;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }
}
