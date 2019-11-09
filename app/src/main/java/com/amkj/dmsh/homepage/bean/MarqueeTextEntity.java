package com.amkj.dmsh.homepage.bean;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/12/1
 * class description:首页跑马灯数据
 */

public class MarqueeTextEntity {

    /**
     * result : [{"id":3,"show_count":1,"content":"2"},{"id":2,"show_count":1,"content":"2"},{"id":1,"show_count":1,"content":"1"}]
     * msg : 请求成功
     * code : 01
     */

    private String msg;
    private String code;
    @SerializedName("result")
    private List<MarqueeTextBean> marqueeTextList;

    public static MarqueeTextEntity objectFromData(String str) {

        return new Gson().fromJson(str, MarqueeTextEntity.class);
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

    public List<MarqueeTextBean> getMarqueeTextList() {
        return marqueeTextList;
    }

    public void setMarqueeTextList(List<MarqueeTextBean> marqueeTextList) {
        this.marqueeTextList = marqueeTextList;
    }

    public static class MarqueeTextBean {
        /**
         * id : 3
         * show_count : 1
         * content : 2
         */

        private int id;
        @SerializedName(value = "show_count", alternate = "showCount")
        private int show_count;
        private String content;
        private String android_link;


        public static MarqueeTextBean objectFromData(String str) {

            return new Gson().fromJson(str, MarqueeTextBean.class);
        }


        public String getAndroid_link() {
            return android_link;
        }

        public void setAndroid_link(String android_link) {
            this.android_link = android_link;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getShow_count() {
            return show_count;
        }

        public void setShow_count(int show_count) {
            this.show_count = show_count;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
