package com.amkj.dmsh.find.bean;

import com.amkj.dmsh.base.BaseEntity;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by xiaoxin on 2019/7/25
 * Version:v4.1.0
 * ClassDescription :新版发现-热门话题  /分类话题列表
 */

public class HotTopicEntity extends BaseEntity{

    /**
     * result : [{"img_url":"http://image.domolife.cn/platform/sF8YYfFwxA1511248274657.jpg","istop":0,"participants_number":0,"id":1,"title":"剁手双十二","content":"千万好礼等你来"}]
     * msg : 请求成功
     * code : 01
     */
    @SerializedName("list")
    private List<HotTopicBean> hotTopicList;

    public List<HotTopicBean> getHotTopicList() {
        return hotTopicList;
    }

    public void setHotTopicList(List<HotTopicBean> hotTopicList) {
        this.hotTopicList = hotTopicList;
    }

    public static class HotTopicBean {

        /**
         * id : 1
         * title : 护肤美妆种草
         * content : 女人，一种永远在被种草和拔草路上奔跑的生物，那么大家一起来晒一晒秋冬好用的护肤以及美妆产品吧~参与送积分哦
         * postNum : 0
         * participantNum : 0
         * score : 1
         */

        private int id;
        private String title;
        private String content;
        private int postNum;
        private int participantNum;
        private int score;

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

        public int getPostNum() {
            return postNum;
        }

        public void setPostNum(int postNum) {
            this.postNum = postNum;
        }

        public int getParticipantNum() {
            return participantNum;
        }

        public void setParticipantNum(int participantNum) {
            this.participantNum = participantNum;
        }

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }
    }
}
