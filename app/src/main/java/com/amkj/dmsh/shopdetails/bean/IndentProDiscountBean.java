package com.amkj.dmsh.shopdetails.bean;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/8/25
 */

public class IndentProDiscountBean {
    private int id;
    private int saleSkuId;
    private int count;
    private int cartId;
    private int isPrerogative;


    public int getIsPrerogative() {
        return isPrerogative;
    }

    public void setIsPrerogative(int isPrerogative) {
        this.isPrerogative = isPrerogative;
    }

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSaleSkuId() {
        return saleSkuId;
    }

    public void setSaleSkuId(int saleSkuId) {
        this.saleSkuId = saleSkuId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
