package com.amkj.dmsh.base;

import com.google.gson.annotations.SerializedName;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/9/30
 * version 3.1.7
 * class description:移除相同ID的商品
 */
public class BaseRemoveExistProductBean extends BaseEntity {
    @SerializedName(value = "id", alternate = {"productId", "uid"})
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
