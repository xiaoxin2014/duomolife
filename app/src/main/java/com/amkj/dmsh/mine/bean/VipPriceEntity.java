package com.amkj.dmsh.mine.bean;

import com.amkj.dmsh.base.BaseEntity;
import com.amkj.dmsh.user.bean.LikedProductBean;

import java.util.List;

/**
 * Created by xiaoxin on 2020/9/8
 * Version:v4.7.0
 */
public class VipPriceEntity extends BaseEntity {
    private List<LikedProductBean> result;

    public List<LikedProductBean> getResult() {
        return result;
    }

    public void setResult(List<LikedProductBean> result) {
        this.result = result;
    }
}
