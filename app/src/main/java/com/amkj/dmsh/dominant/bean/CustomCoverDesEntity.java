package com.amkj.dmsh.dominant.bean;

import com.amkj.dmsh.constant.CommunalDetailBean;
import com.amkj.dmsh.utils.gson.GsonUtils;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/1/19
 * version 3.7
 * class description:自定义专区封面描述
 */

public class CustomCoverDesEntity {

    /**
     * result : [{"picUrl":"http://image.domolife.cn/platform/xKhDPrCTfj1516186092479.jpg","description":[{"type":"text","content":"<p>啦啦啦<\/p>"}]}]
     * msg : 请求成功
     * code : 01
     */

    private String msg;
    private String code;
    @SerializedName("result")
    private List<CustomCoverDesBean> coverDesList;

    public static CustomCoverDesEntity objectFromData(String str) {

        return GsonUtils.fromJson(str, CustomCoverDesEntity.class);
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<CustomCoverDesBean> getCoverDesList() {
        return coverDesList;
    }

    public void setCoverDesList(List<CustomCoverDesBean> coverDesList) {
        this.coverDesList = coverDesList;
    }

    public static class CustomCoverDesBean {
        /**
         * picUrl : http://image.domolife.cn/platform/xKhDPrCTfj1516186092479.jpg
         * description : [{"type":"text","content":"<p>啦啦啦<\/p>"}]
         */

        private String picUrl;
        @SerializedName("description")
        private List<CommunalDetailBean> descriptionList;

        public static CustomCoverDesBean objectFromData(String str) {

            return GsonUtils.fromJson(str, CustomCoverDesBean.class);
        }

        public String getPicUrl() {
            return picUrl;
        }

        public void setPicUrl(String picUrl) {
            this.picUrl = picUrl;
        }

        public List<CommunalDetailBean> getDescriptionList() {
            return descriptionList;
        }

        public void setDescriptionList(List<CommunalDetailBean> descriptionList) {
            this.descriptionList = descriptionList;
        }
    }
}
