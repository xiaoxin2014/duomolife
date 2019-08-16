package com.amkj.dmsh.find.bean;

import com.amkj.dmsh.base.BaseEntity;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/8/4
 * class description:发现 - 热门话题
 */

public class FindHotTopicEntity extends BaseEntity{

    /**
     * result : [{"img_url":"http://image.domolife.cn/platform/sF8YYfFwxA1511248274657.jpg","istop":0,"participants_number":0,"id":1,"title":"剁手双十二","content":"千万好礼等你来"}]
     * msg : 请求成功
     * code : 01
     */
    @SerializedName("result")
    private List<FindHotTopicBean> hotTopicList;

    public static FindHotTopicEntity objectFromData(String str) {

        return new Gson().fromJson(str, FindHotTopicEntity.class);
    }

    public List<FindHotTopicBean> getHotTopicList() {
        return hotTopicList;
    }

    public void setHotTopicList(List<FindHotTopicBean> hotTopicList) {
        this.hotTopicList = hotTopicList;
    }

    public static class FindHotTopicBean {
        /**
         * img_url : http://image.domolife.cn/platform/sF8YYfFwxA1511248274657.jpg
         * istop : 0
         * participants_number : 0
         * id : 1
         * title : 剁手双十二
         * content : 千万好礼等你来
         */

        private String img_url;
        private int istop;
        private int participants_number;
        private int id;
        private String title;
        private String content;
        private String video_url;
        private String first_img_url;
        private String reminder;
        private boolean isCollect;
        private String topicCatergory;

        public FindHotTopicBean(int participants_number, String title,String topicCatergory) {
            this.participants_number = participants_number;
            this.title = title;
            this.topicCatergory = topicCatergory;
        }

        public static FindHotTopicBean objectFromData(String str) {

            return new Gson().fromJson(str, FindHotTopicBean.class);
        }

        public String getTopicCatergory() {
            return topicCatergory;
        }

        public void setTopicCatergory(String topicCatergory) {
            this.topicCatergory = topicCatergory;
        }

        public String getFirst_img_url() {
            return first_img_url;
        }

        public void setFirst_img_url(String first_img_url) {
            this.first_img_url = first_img_url;
        }

        public String getVideo_url() {
            return video_url;
        }

        public void setVideo_url(String video_url) {
            this.video_url = video_url;
        }

        public boolean isCollect() {
            return isCollect;
        }

        public void setCollect(boolean collect) {
            isCollect = collect;
        }

        public String getImg_url() {
            return img_url;
        }

        public void setImg_url(String img_url) {
            this.img_url = img_url;
        }

        public int getIstop() {
            return istop;
        }

        public void setIstop(int istop) {
            this.istop = istop;
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

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getReminder() {
            return reminder;
        }

        public void setReminder(String reminder) {
            this.reminder = reminder;
        }
    }
}
