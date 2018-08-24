package com.amkj.dmsh.homepage.bean;

import com.amkj.dmsh.constant.CommunalDetailBean;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/10/27
 * class description:公共详情仅有description
 */

public class CommunalOnlyDescription {

    /**
     * comOnlyDesBean : {"description":[{"type":"text","content":"<p>dfdf<\/p>"}]}
     * msg : 请求成功
     * code : 01
     */
    @SerializedName("result")
    private ComOnlyDesBean comOnlyDesBean;
    private String msg;
    private String code;

    public ComOnlyDesBean getComOnlyDesBean() {
        return comOnlyDesBean;
    }

    public void setComOnlyDesBean(ComOnlyDesBean comOnlyDesBean) {
        this.comOnlyDesBean = comOnlyDesBean;
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

    public static class ComOnlyDesBean {
        @SerializedName("description")
        private List<CommunalDetailBean> descriptionList;

        public List<CommunalDetailBean> getDescriptionList() {
            return descriptionList;
        }

        public void setDescriptionList(List<CommunalDetailBean> descriptionList) {
            this.descriptionList = descriptionList;
        }
    }
}
