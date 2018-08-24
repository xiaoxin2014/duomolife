package com.amkj.dmsh.homepage.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by atd48 on 2016/8/29.
 */
public class CategoryAD {

    /**
     * result : [{"title":"拉杆箱","link":"http://www.baidu.com","pic_url":"http://o6wxayr69.bkt.clouddn.com/2016-07-11_57834a4061801.jpg"},{"title":"点读笔","link":"http://www.baidu.com","pic_url":"http://o6wxayr69.bkt.clouddn.com/2016-07-08_577f761fd5e89.jpg"}]
     * code : 01
     * msg : 请求成功
     */

    private String code;
    private String msg;
    /**
     * title : 拉杆箱
     * link : http://www.baidu.com
     * pic_url : http://o6wxayr69.bkt.clouddn.com/2016-07-11_57834a4061801.jpg
     */

    @SerializedName("result")
    private List<AdBean> adBeanList;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<AdBean> getAdBeanList() {
        return adBeanList;
    }

    public void setAdBeanList(List<AdBean> adBeanList) {
        this.adBeanList = adBeanList;
    }

    public static class AdBean {
        private String title;
        private String link;
        private String pic_url;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public String getPic_url() {
            return pic_url;
        }

        public void setPic_url(String pic_url) {
            this.pic_url = pic_url;
        }
    }
}
