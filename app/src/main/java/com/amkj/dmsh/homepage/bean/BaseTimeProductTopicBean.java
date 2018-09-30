package com.amkj.dmsh.homepage.bean;

import com.amkj.dmsh.base.BaseRemoveExistProductBean;
import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/9/14
 * version 3.1.6
 * class description:新版限时特惠
 */
public class BaseTimeProductTopicBean extends BaseRemoveExistProductBean implements MultiItemEntity {
    private int itemType;
    @Override
    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }
}
