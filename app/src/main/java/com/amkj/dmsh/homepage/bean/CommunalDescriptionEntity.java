package com.amkj.dmsh.homepage.bean;

import com.amkj.dmsh.constant.CommunalDetailBean;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/7/31
 * class description:请输入类描述
 */

public class CommunalDescriptionEntity {

    /**
     * result : {"description":[{"type":"coupon","content":"10,http://image.domolife.cn/platform/7YkcRKThts1501120623804.jpg"}]}
     * msg : 请求成功
     * code : 01
     */

    @SerializedName("result")
    private CommunalDescriptionBean communalDescriptionBean;
    private String msg;
    private String code;

    public CommunalDescriptionBean getCommunalDescriptionBean() {
        return communalDescriptionBean;
    }

    public void setCommunalDescriptionBean(CommunalDescriptionBean communalDescriptionBean) {
        this.communalDescriptionBean = communalDescriptionBean;
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

    public static class CommunalDescriptionBean {
        @SerializedName("description")
        private List<CommunalDetailBean> descriptionList;
        private int id;

        public List<CommunalDetailBean> getDescriptionList() {
            return descriptionList;
        }

        public void setDescriptionList(List<CommunalDetailBean> descriptionList) {
            this.descriptionList = descriptionList;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}
