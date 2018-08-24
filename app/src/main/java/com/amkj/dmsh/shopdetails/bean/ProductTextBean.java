package com.amkj.dmsh.shopdetails.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/5/16
 * version 3.1.3
 * class description:产品文本显示 商品详情赠品 优惠券
 */
public class ProductTextBean implements MultiItemEntity{
    private String name;
    private String couponId;
//    选择（check,select）
    private boolean select;
    private int itemType;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    @Override
    public int getItemType() {
        return itemType;
    }
}
