package com.amkj.dmsh.mine.bean;

import com.amkj.dmsh.base.BaseEntity;
import com.amkj.dmsh.constant.CommunalDetailBean;
import com.google.gson.annotations.SerializedName;

import java.util.List;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2019/1/19
 * version 3.2.0
 * class description:web数据解析
 */
public class WebDataCommunalEntity extends BaseEntity {
    @SerializedName("result")
    private List<CommunalDetailBean> webDataCommunalList;

    public List<CommunalDetailBean> getWebDataCommunalList() {
        return webDataCommunalList;
    }

    public void setWebDataCommunalList(List<CommunalDetailBean> webDataCommunalList) {
        this.webDataCommunalList = webDataCommunalList;
    }
}
