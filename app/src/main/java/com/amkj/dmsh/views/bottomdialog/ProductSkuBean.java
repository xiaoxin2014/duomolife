package com.amkj.dmsh.views.bottomdialog;

import java.util.ArrayList;

/**
 * @author zwy
 * @email 16681805@qq.com
 * created on 2016/5/9
 * class description:Sku实体
 */

public class ProductSkuBean {
    private String productName;//:商品名称
    private String imageUrl;//:商品图片(小图)
    private String productPrice;//:商品价格范围(保留小数点后两位)
    private ArrayList<ProductParameterTypeBean> parameters;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public ArrayList<ProductParameterTypeBean> getParameters() {
        return parameters;
    }

    public void setParameters(ArrayList<ProductParameterTypeBean> parameters) {
        this.parameters = parameters;
    }
}

