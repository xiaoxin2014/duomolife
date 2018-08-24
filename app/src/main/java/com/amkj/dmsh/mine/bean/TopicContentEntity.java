package com.amkj.dmsh.mine.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/8/12
 * class description:请输入类描述
 */

public class TopicContentEntity {

    /**
     * result : [{"picUrl":"http://image.domolife.cn/platform/bKi8bi8Zry1500281450853.png","istop":1,"name":"测试01","participants_number":1,"id":10456,"content":"测试01"},{"picUrl":"http://image.domolife.cn/platform/zPBc8bz2Mf1500430228458.png","istop":0,"name":"测试8","participants_number":0,"id":10463,"content":"测试8"}]
     * msg : 请求成功
     * code : 01
     * count : 2
     */

    private String msg;
    private String code;
    @SerializedName("result")
    private List<TopicContentBean> topicContentList;

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

    public List<TopicContentBean> getTopicContentList() {
        return topicContentList;
    }

    public void setTopicContentList(List<TopicContentBean> topicContentList) {
        this.topicContentList = topicContentList;
    }

    public static class TopicContentBean {
        /**
         * picUrl : http://image.domolife.cn/platform/bKi8bi8Zry1500281450853.png
         * istop : 1
         * name : 测试01
         * participants_number : 1
         * id : 10456
         * content : 测试01
         */

        private String picUrl;
        private int istop;
        private String name;
        private int participants_number;
        private int id;
        private String content;

        public String getPicUrl() {
            return picUrl;
        }

        public void setPicUrl(String picUrl) {
            this.picUrl = picUrl;
        }

        public int getIstop() {
            return istop;
        }

        public void setIstop(int istop) {
            this.istop = istop;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getParticipants_number() {
            return participants_number;
        }

        public void setParticipants_number(int participants_number) {
            this.participants_number = participants_number;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
