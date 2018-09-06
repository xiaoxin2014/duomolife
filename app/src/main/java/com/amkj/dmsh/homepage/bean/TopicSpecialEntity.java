package com.amkj.dmsh.homepage.bean;

import com.amkj.dmsh.base.BaseEntity;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/7/31
 * class description:请输入类描述
 */

public class TopicSpecialEntity extends BaseEntity{

    /**
     * result : [{"flag":"种草营","androidLink":"app://ArticleOfficialActivity?ArtId=9870","iosLink":"DMLActicleDetailViewController?aid=9870","description":"不知不觉就迎来了万物复苏的季节，春暖花开，世界都要万紫千红起来了，到处都是美美的，萌动了我一颗少女的苏心。春天的色调就应该是粉粉的，干净纯洁，比白色多了一丝生机勃勃。今天来一次粉色系的约会，分享几款粉","ctime":"2017-02-25 16:03:15","id":9870,"title":"春暖花开，粉嫩起来","pic_url":"http://img.domolife.cn/platform/SFY3HZ7hA3.png","type":"1"}]
     * msg : 请求成功
     * code : 01
     */
    @SerializedName("result")
    private List<TopicSpecialBean> topicSpecialBeanList;

    public List<TopicSpecialBean> getTopicSpecialBeanList() {
        return topicSpecialBeanList;
    }

    public void setTopicSpecialBeanList(List<TopicSpecialBean> topicSpecialBeanList) {
        this.topicSpecialBeanList = topicSpecialBeanList;
    }

    public static class TopicSpecialBean {
        /**
         * flag : 种草营
         * androidLink : app://ArticleOfficialActivity?ArtId=9870
         * iosLink : DMLActicleDetailViewController?aid=9870
         * description : 不知不觉就迎来了万物复苏的季节，春暖花开，世界都要万紫千红起来了，到处都是美美的，萌动了我一颗少女的苏心。春天的色调就应该是粉粉的，干净纯洁，比白色多了一丝生机勃勃。今天来一次粉色系的约会，分享几款粉
         * ctime : 2017-02-25 16:03:15
         * id : 9870
         * title : 春暖花开，粉嫩起来
         * pic_url : http://img.domolife.cn/platform/SFY3HZ7hA3.png
         * type : 1
         */

        private String flag;
        private String androidLink;
        private String iosLink;
        private String description;
        private String ctime;
        private int id;
        private String title;
        private String pic_url;
        private String type;

        public String getFlag() {
            return flag;
        }

        public void setFlag(String flag) {
            this.flag = flag;
        }

        public String getAndroidLink() {
            return androidLink;
        }

        public void setAndroidLink(String androidLink) {
            this.androidLink = androidLink;
        }

        public String getIosLink() {
            return iosLink;
        }

        public void setIosLink(String iosLink) {
            this.iosLink = iosLink;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getCtime() {
            return ctime;
        }

        public void setCtime(String ctime) {
            this.ctime = ctime;
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

        public String getPic_url() {
            return pic_url;
        }

        public void setPic_url(String pic_url) {
            this.pic_url = pic_url;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
