package com.amkj.dmsh.message.bean;

import com.amkj.dmsh.base.BaseEntity;
import com.amkj.dmsh.bean.CommunalDetailBean;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/1/12
 * class description:请输入类描述
 */

public class OfficialNotifyEntity extends BaseEntity{

    /**
     * result : {"contentBeanList":[{"contentBeanList":"<p style=\"white-space: normal;\"><span style=\"color: rgb(63, 63, 63);\">多么福利社元旦放假通知<\/span><\/p>","type":"text"},{"contentBeanList":"<p style=\"white-space: normal;\"><br/><\/p>","type":"text"},{"contentBeanList":"<p style=\"white-space: normal;\"><span style=\"color: rgb(63, 63, 63);\">嘿，你好啊，不知道大家做好挥别2016，迎接2017的准备了吗？<\/span><\/p>","type":"text"},{"contentBeanList":"<p style=\"white-space: normal;\"><br/><\/p>","type":"text"},{"contentBeanList":"<p style=\"white-space: normal;\"><span style=\"color: rgb(63, 63, 63);\">节日将至，祝大家元旦快乐呀！希望此刻在屏幕前的你在新的一年里能获得更多的正能量与好运气，一起期待这个崭新的2017会给予我们怎样的惊喜。<\/span><\/p>","type":"text"},{"contentBeanList":"<p style=\"white-space: normal;\"><br/><\/p>","type":"text"},{"contentBeanList":"<p style=\"white-space: normal;\"><span style=\"color: rgb(63, 63, 63);\">在全新的2017篇章里，多么生活也会继续带领大家买买买，给大家提供更为优质的购物体验！<\/span><\/p>","type":"text"},{"contentBeanList":"<p style=\"white-space: normal;\"><br/><\/p>","type":"text"},{"contentBeanList":"<p style=\"white-space: normal;\"><span style=\"color: rgb(63, 63, 63);\">给大家送出新年祝福的同时，在这里还有个温馨提醒~<\/span><\/p>","type":"text"},{"contentBeanList":"<p style=\"white-space: normal;\"><br/><\/p>","type":"text"},{"contentBeanList":"<p style=\"white-space: normal;\"><span style=\"color: rgb(63, 63, 63);\">按照国家法定节假日的安排，元旦假期为12.31-1.2，所以在未来的这三天内，多么福利社只接单不发货喔，良品还是可以照常拍下，1.3号恢复正常发货~还请大家多担待嘿嘿。<\/span><\/p>","type":"text"},{"contentBeanList":"<p style=\"white-space: normal;\"><br/><\/p>","type":"text"},{"contentBeanList":"<p style=\"white-space: normal;\"><span style=\"color: rgb(63, 63, 63);\">那么，我们2017年见咯！<\/span><\/p>","type":"text"},{"contentBeanList":"<p><br/><\/p>","type":"text"}],"id":3,"title":"点开有惊喜","cover_url":"http://img.domolife.cn/platform/sjHk2wdsys.jpg","status":0,"create_time":"2016-11-09 18:05:06"}
     * code : 01
     * msg : 请求成功
     */

    @SerializedName("result")
    private OfficialNotifyParseBean officialNotifyParseBean;

    public OfficialNotifyParseBean getOfficialNotifyParseBean() {
        return officialNotifyParseBean;
    }

    public void setOfficialNotifyParseBean(OfficialNotifyParseBean officialNotifyParseBean) {
        this.officialNotifyParseBean = officialNotifyParseBean;
    }

    public static class OfficialNotifyParseBean {
        /**
         * contentBeanList : [{"contentBeanList":"<p style=\"white-space: normal;\"><span style=\"color: rgb(63, 63, 63);\">多么福利社元旦放假通知<\/span><\/p>","type":"text"},{"contentBeanList":"<p style=\"white-space: normal;\"><br/><\/p>","type":"text"},{"contentBeanList":"<p style=\"white-space: normal;\"><span style=\"color: rgb(63, 63, 63);\">嘿，你好啊，不知道大家做好挥别2016，迎接2017的准备了吗？<\/span><\/p>","type":"text"},{"contentBeanList":"<p style=\"white-space: normal;\"><br/><\/p>","type":"text"},{"contentBeanList":"<p style=\"white-space: normal;\"><span style=\"color: rgb(63, 63, 63);\">节日将至，祝大家元旦快乐呀！希望此刻在屏幕前的你在新的一年里能获得更多的正能量与好运气，一起期待这个崭新的2017会给予我们怎样的惊喜。<\/span><\/p>","type":"text"},{"contentBeanList":"<p style=\"white-space: normal;\"><br/><\/p>","type":"text"},{"contentBeanList":"<p style=\"white-space: normal;\"><span style=\"color: rgb(63, 63, 63);\">在全新的2017篇章里，多么生活也会继续带领大家买买买，给大家提供更为优质的购物体验！<\/span><\/p>","type":"text"},{"contentBeanList":"<p style=\"white-space: normal;\"><br/><\/p>","type":"text"},{"contentBeanList":"<p style=\"white-space: normal;\"><span style=\"color: rgb(63, 63, 63);\">给大家送出新年祝福的同时，在这里还有个温馨提醒~<\/span><\/p>","type":"text"},{"contentBeanList":"<p style=\"white-space: normal;\"><br/><\/p>","type":"text"},{"contentBeanList":"<p style=\"white-space: normal;\"><span style=\"color: rgb(63, 63, 63);\">按照国家法定节假日的安排，元旦假期为12.31-1.2，所以在未来的这三天内，多么福利社只接单不发货喔，良品还是可以照常拍下，1.3号恢复正常发货~还请大家多担待嘿嘿。<\/span><\/p>","type":"text"},{"contentBeanList":"<p style=\"white-space: normal;\"><br/><\/p>","type":"text"},{"contentBeanList":"<p style=\"white-space: normal;\"><span style=\"color: rgb(63, 63, 63);\">那么，我们2017年见咯！<\/span><\/p>","type":"text"},{"contentBeanList":"<p><br/><\/p>","type":"text"}]
         * id : 3
         * title : 点开有惊喜
         * cover_url : http://img.domolife.cn/platform/sjHk2wdsys.jpg
         * status : 0
         * create_time : 2016-11-09 18:05:06
         */

        private int id;
        private String title;
        private String cover_url;
        private int status;
        private String create_time;
        @SerializedName("content")
        private List<CommunalDetailBean> contentBeanList;

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

        public String getCover_url() {
            return cover_url;
        }

        public void setCover_url(String cover_url) {
            this.cover_url = cover_url;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public List<CommunalDetailBean> getContentBeanList() {
            return contentBeanList;
        }

        public void setContentBeanList(List<CommunalDetailBean> contentBeanList) {
            this.contentBeanList = contentBeanList;
        }
    }
}
