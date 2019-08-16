package com.amkj.dmsh.find.bean;

import com.amkj.dmsh.base.BaseEntity;

import java.util.List;

/**
 * Created by xiaoxin on 2019/7/25
 * Version:v4.1.0
 * ClassDescription :话题分类列表
 */
public class TopicCaterGoryEntity extends BaseEntity {

    /**
     * totalResult : 0
     * list : [{"id":2,"title":"分类1","status":"1","sort":1,"topicNum":0},{"id":4,"title":"分类3","status":"1","sort":2,"topicNum":0},{"id":5,"title":"分类4","status":"1","sort":3,"topicNum":0},{"id":6,"title":"分类5","status":"1","sort":4,"topicNum":0},{"id":7,"title":"分类6","status":"1","sort":5,"topicNum":0},{"id":9,"title":"分类7","status":"1","sort":6,"topicNum":0},{"id":10,"title":"分类8","status":"1","sort":7,"topicNum":0},{"id":11,"title":"分类9","status":"1","sort":8,"topicNum":0}]
     */

    private int totalResult;

    private List<TopicCaterGoryBean> list;

    public int getTotalResult() {
        return totalResult;
    }

    public void setTotalResult(int totalResult) {
        this.totalResult = totalResult;
    }

    public List<TopicCaterGoryBean> getCatergoryList() {
        return list;
    }

    public void setCatergoryList(List<TopicCaterGoryBean> list) {
        this.list = list;
    }

    public static class TopicCaterGoryBean {
        /**
         * id : 2
         * title : 分类1
         * status : 1
         * sort : 1
         * topicNum : 0
         */

        private int id;
        private String title;
        private String status;
        private int sort;
        private int topicNum;

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

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public int getSort() {
            return sort;
        }

        public void setSort(int sort) {
            this.sort = sort;
        }

        public int getTopicNum() {
            return topicNum;
        }

        public void setTopicNum(int topicNum) {
            this.topicNum = topicNum;
        }
    }
}
