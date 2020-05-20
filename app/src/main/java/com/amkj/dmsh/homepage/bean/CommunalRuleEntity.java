package com.amkj.dmsh.homepage.bean;

import com.amkj.dmsh.base.BaseEntity;
import com.amkj.dmsh.bean.CommunalDetailBean;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/7/31
 * version 3.1.5
 * class description:规则
 */
public class CommunalRuleEntity extends BaseEntity{

    /**
     * code : 01
     * msg : 请求成功
     * sysTime : 2018-07-31 16:03:21
     * result : [{"type":"text","content":"<p>&nbsp; 我就是夺宝说明<br/><\/p>"}]
     */
    @SerializedName("result")
    private List<CommunalDetailBean> communalRuleList;

    public List<CommunalDetailBean> getCommunalRuleList() {
        return communalRuleList;
    }

    public void setCommunalRuleList(List<CommunalDetailBean> communalRuleList) {
        this.communalRuleList = communalRuleList;
    }
}
