package com.amkj.dmsh.find.bean;

import com.amkj.dmsh.base.BaseEntity;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.amkj.dmsh.find.bean.FindHotTopicEntity.FindHotTopicBean;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/11/23
 * class description:话题详情
 */

public class FindTopicDetailEntity extends BaseEntity{

    /**
     * result : {"video_url":"http://image.domolife.cn/platform/sF8YYfFwxA1511359274657.video","img_url":"http://image.domolife.cn/platform/sF8YYfFwxA1511248274657.jpg","istop":1,"isCollect":false,"participants_number":7,"id":1,"title":"剁手双十二","content":"千万好礼等你来"}
     * msg : 请求成功
     * code : 01
     */

    @SerializedName("result")
    private FindHotTopicBean findHotTopicBean;

    public static FindTopicDetailEntity objectFromData(String str) {

        return new Gson().fromJson(str, FindTopicDetailEntity.class);
    }

    public FindHotTopicBean getFindHotTopicBean() {
        return findHotTopicBean;
    }

    public void setFindHotTopicBean(FindHotTopicBean findHotTopicBean) {
        this.findHotTopicBean = findHotTopicBean;
    }
}
