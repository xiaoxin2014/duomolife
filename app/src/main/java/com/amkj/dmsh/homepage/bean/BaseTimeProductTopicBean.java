package com.amkj.dmsh.homepage.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/9/14
 * version 1.0
 * class description:请输入类描述
 */
public class BaseTimeProductTopicBean implements MultiItemEntity {
    private int itemType;
    @Override
    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }
}
