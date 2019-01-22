package com.amkj.dmsh.mine.bean;

import com.amkj.dmsh.base.BaseEntity;
import com.google.gson.annotations.SerializedName;

import java.util.List;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2019/1/22
 * version 3.2.0
 * class description:注销账号结果
 */
public class LogoutAccountResultEntity extends BaseEntity {

    @SerializedName("result")
    private List<String> accountResultList;

    public List<String> getAccountResultList() {
        return accountResultList;
    }

    public void setAccountResultList(List<String> accountResultList) {
        this.accountResultList = accountResultList;
    }
}
