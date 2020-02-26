package com.amkj.dmsh.homepage.bean;

import com.amkj.dmsh.base.BaseEntity;
import com.amkj.dmsh.constant.ConstantMethod;

import java.util.List;

/**
 * Created by xiaoxin on 2019/4/16 0016
 * Version:v4.0.0
 * ClassDescription :
 */
public class HomeDynamicEntity extends BaseEntity {
    private String sysTime;
    private String cover;
    private String title;
    private String description;
    private String isDisplay;
    private String link;
    private String id;
    private List<HomeCommonEntity.ProductInfoListBean> productInfoList;


    public int getId() {
        return ConstantMethod.getStringChangeIntegers(id);
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getSysTime() {
        return sysTime;
    }

    public void setSysTime(String sysTime) {
        this.sysTime = sysTime;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIsDisplay() {
        return isDisplay;
    }

    public void setIsDisplay(String isDisplay) {
        this.isDisplay = isDisplay;
    }

    public List<HomeCommonEntity.ProductInfoListBean> getProductInfoList() {
        return productInfoList;
    }

    public void setProductInfoList(List<HomeCommonEntity.ProductInfoListBean> productInfoList) {
        this.productInfoList = productInfoList;
    }
}
