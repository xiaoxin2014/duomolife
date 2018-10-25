package com.amkj.dmsh.mine.bean;

import com.amkj.dmsh.base.BaseEntity;

import java.util.List;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/10/25
 * version 3.1.8
 * class description:意见反馈
 */
public class SuggestionTypeEntity extends BaseEntity {

    /**
     * sysTime : 2018-10-25 16:15:56
     * feedBackTypeList : [{"id":1,"title":"产品反馈"}]
     */
    private List<FeedBackTypeBean> feedBackTypeList;

    public List<FeedBackTypeBean> getFeedBackTypeList() {
        return feedBackTypeList;
    }

    public void setFeedBackTypeList(List<FeedBackTypeBean> feedBackTypeList) {
        this.feedBackTypeList = feedBackTypeList;
    }

    public static class FeedBackTypeBean {
        /**
         * id : 1
         * title : 产品反馈
         */

        private int id;
        private String title;

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
