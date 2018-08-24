package com.amkj.dmsh.dominant.bean;

import com.amkj.dmsh.constant.CommunalDetailBean;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/7/3
 * class description:请输入类描述
 */

public class QualityShopDescripEntity {

    /**
     * result : {"picUrl":"http://img.domolife.cn/platform/7piQ3Js5fe1498786137434.png","discription":"","created":"2017-06-30 09:29:07","id":305,"title":"美国直邮专区"}
     * msg : 请求成功
     * code : 01
     */

    @SerializedName("result")
    private QualityShopDescBean qualityShopDescBean;
    private String msg;
    private String code;

    public QualityShopDescBean getQualityShopDescBean() {
        return qualityShopDescBean;
    }

    public void setQualityShopDescBean(QualityShopDescBean qualityShopDescBean) {
        this.qualityShopDescBean = qualityShopDescBean;
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

    public static class QualityShopDescBean {
        /**
         * picUrl : http://img.domolife.cn/platform/7piQ3Js5fe1498786137434.png
         * discription :
         * created : 2017-06-30 09:29:07
         * id : 305
         * title : 美国直邮专区
         */

        private String picUrl;
        @SerializedName("description")
        private List<CommunalDetailBean> descripList;
        private String created;
        private int id;
        private String title;
        private String subtitle;

        public String getSubtitle() {
            return subtitle;
        }

        public void setSubtitle(String subtitle) {
            this.subtitle = subtitle;
        }

        public String getPicUrl() {
            return picUrl;
        }

        public void setPicUrl(String picUrl) {
            this.picUrl = picUrl;
        }

        public List<CommunalDetailBean> getDescripList() {
            return descripList;
        }

        public void setDescripList(List<CommunalDetailBean> descripList) {
            this.descripList = descripList;
        }

        public String getCreated() {
            return created;
        }

        public void setCreated(String created) {
            this.created = created;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
