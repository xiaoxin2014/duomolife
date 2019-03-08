package com.amkj.dmsh.find.bean;

import com.amkj.dmsh.base.BaseEntity;

import java.util.List;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2019/3/7
 * version 3.3.0
 * class description:精选文章Entity
 */
public class FeaturedArticleEntity extends BaseEntity {
    private List<FeaturedArticleBean> featuredArticleBeans;

    public List<FeaturedArticleBean> getFeaturedArticleBeans() {
        return featuredArticleBeans;
    }

    public void setFeaturedArticleBeans(List<FeaturedArticleBean> featuredArticleBeans) {
        this.featuredArticleBeans = featuredArticleBeans;
    }

    public static class FeaturedArticleBean {
        private String picUrl;
        private String title;
        private String content;
        private String time;
        private int likeCount;
        private int commentCount;

        public String getPicUrl() {
            return picUrl;
        }

        public void setPicUrl(String picUrl) {
            this.picUrl = picUrl;
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

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public int getLikeCount() {
            return likeCount;
        }

        public void setLikeCount(int likeCount) {
            this.likeCount = likeCount;
        }

        public int getCommentCount() {
            return commentCount;
        }

        public void setCommentCount(int commentCount) {
            this.commentCount = commentCount;
        }
    }
}
