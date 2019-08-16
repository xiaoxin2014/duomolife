package com.amkj.dmsh.find.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by xiaoxin on 2019/7/31
 * Version:v4.1.0
 * ClassDescription :点赞公共父类
 */
public class BaseFavorBean implements MultiItemEntity {
    private String isFocus;

    public boolean getIsFocus() {
        return "1".equals(isFocus);
    }

    public void setIsFocus(String isFocus) {
        this.isFocus = isFocus;
    }

    private int itemType;

    @Override
    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }
}
