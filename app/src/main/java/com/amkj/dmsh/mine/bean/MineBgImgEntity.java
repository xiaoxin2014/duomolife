package com.amkj.dmsh.mine.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/9/26
 * class description:背景图片列表
 */

public class MineBgImgEntity {

    /**
     * result : [{"id":1,"bgimg_url":"http://image.domolife.cn/platform/5PAZ7QDA2B1500975692201.png"},{"id":2,"bgimg_url":"http://image.domolife.cn/platform/yBwx5jSMQX1500985995721.png"},{"id":3,"bgimg_url":"http://image.domolife.cn/platform/we4scsT2mY1500985986034.png"},{"id":4,"bgimg_url":"http://image.domolife.cn/platform/EtYnXzw6NX1501036988844.png"},{"id":5,"bgimg_url":"http://image.domolife.cn/platform/AXRXt25KwH1501036999350.png"},{"id":6,"bgimg_url":"http://image.domolife.cn/platform/fATABhrscC1501037010392.png"},{"id":7,"bgimg_url":"http://image.domolife.cn/platform/Z4ZeQxxbPy1501037021618.png"}]
     * msg : 请求成功
     * code : 01
     */

    private String msg;
    private String code;
    @SerializedName("result")
    private List<MineBgImgBean> MineBgImgList;

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

    public List<MineBgImgBean> getMineBgImgList() {
        return MineBgImgList;
    }

    public void setMineBgImgList(List<MineBgImgBean> MineBgImgList) {
        this.MineBgImgList = MineBgImgList;
    }

    public static class MineBgImgBean {
        /**
         * id : 1
         * bgimg_url : http://image.domolife.cn/platform/5PAZ7QDA2B1500975692201.png
         */

        private int id;
        private String bgimg_url;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getBgimg_url() {
            return bgimg_url;
        }

        public void setBgimg_url(String bgimg_url) {
            this.bgimg_url = bgimg_url;
        }
    }
}
