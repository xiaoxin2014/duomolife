package com.amkj.dmsh.find.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by xiaoxin on 2019/8/21
 * Version:v4.2.0
 * ClassDescription :帖子详情关联商品
 */
public class RelatedGoodsBean {
    /**
     * productId : 19408
     * price : 42.00
     * pictureUrl : http://image.domolife.cn/platform/SBket5TY5d1560242526857.jpg
     * id : 14067
     * title : 尔木萄 星空美妆蛋
     */

    private int productId;
    private String price;
    private String marketPrice;
    @SerializedName(value = "pictureUrl", alternate = {"cover", "productImg", "imgPath","imagePath"})
    private String pictureUrl;
    private int id;
    @SerializedName(value = "title", alternate = "productName")
    private String title;
    private String thirdUrl;
    private int productStatus;
    private String skutext;

    public String getSkutext() {
        return skutext;
    }

    public void setSkutext(String skutext) {
        this.skutext = skutext;
    }

    public int getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(int productStatus) {
        this.productStatus = productStatus;
    }

    public String getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(String marketPrice) {
        this.marketPrice = marketPrice;
    }

    public String getThirdUrl() {
        return thirdUrl;
    }

    public void setThirdUrl(String thirdUrl) {
        this.thirdUrl = thirdUrl;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
