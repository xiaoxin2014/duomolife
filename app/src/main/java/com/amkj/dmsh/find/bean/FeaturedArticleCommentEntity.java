package com.amkj.dmsh.find.bean;

import java.util.List;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2019/3/8
 * version 3.3.0
 * class description:精选 评论
 */
public class FeaturedArticleCommentEntity {

    private List<FeaturedArticleCommentBean> featuredArticleCommentBeans;

    public List<FeaturedArticleCommentBean> getFeaturedArticleCommentBeans() {
        return featuredArticleCommentBeans;
    }

    public void setFeaturedArticleCommentBeans(List<FeaturedArticleCommentBean> featuredArticleCommentBeans) {
        this.featuredArticleCommentBeans = featuredArticleCommentBeans;
    }

    public static class FeaturedArticleCommentBean{
        private String avatar;
        private String nickName;
        private int likeCount;
        private String time;
        private String content;

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public int getLikeCount() {
            return likeCount;
        }

        public void setLikeCount(int likeCount) {
            this.likeCount = likeCount;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
