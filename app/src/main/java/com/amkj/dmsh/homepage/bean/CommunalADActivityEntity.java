package com.amkj.dmsh.homepage.bean;

import com.amkj.dmsh.base.BaseTimeEntity;
import com.amkj.dmsh.dominant.bean.QualityGoodProductEntity.Attribute;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2016/11/22
 * update 2018/12/10
 * class description:广告数据
 */

public class CommunalADActivityEntity extends BaseTimeEntity{

    /**
     * result : [{"beginTime":"","link":"app://DMLSpringSaleViewController","objectId":0,"width":"100%","web_link":"http://ts.domolife.cn/DMLSpringSaleViewController","object":3,"subtitle":"SpringSale","picUrl":"http://img.domolife.cn/platform/sn5bcT3CTx.png","title":"限时特惠","height":"100%","endTime":"","bcolor":"fdeee9","showTime":"0"},{"beginTime":"","link":"app://DMLWelfareServiceViewController","objectId":0,"width":"100%","web_link":"http://ts.domolife.cn/DMLWelfareServiceViewController","object":1,"subtitle":"DomoLife","picUrl":"http://img.domolife.cn/platform/Ap4Q8AY5aK.png","title":"多么福利社","height":"100%","endTime":"","bcolor":"b3d9fe","showTime":"0"},{"beginTime":"","link":"app://DMLPointsMallViewController","objectId":0,"width":"100%","web_link":"http://ts.domolife.cn/DMLPointsMallViewController","object":3,"subtitle":"Integral mall","picUrl":"http://img.domolife.cn/platform/NBJJWhB4sr.png","title":"积分商城","height":"100%","endTime":"","bcolor":"ffdddc","showTime":"0"},{"beginTime":"","link":"app://DMLPointsSignInViewController","objectId":0,"width":"100%","web_link":"http://ts.domolife.cn/DMLPointsSignInViewController","object":1,"subtitle":"Sign in polite","picUrl":"http://img.domolife.cn/platform/PxjRC2T8J7.png","title":"签到有礼","height":"100%","endTime":"","bcolor":"fff596","showTime":"0"}]
     * code : 01
     * msg : 请求成功
     */
    @SerializedName("result")
    private List<CommunalADActivityBean> communalADActivityBeanList;

    public List<CommunalADActivityBean> getCommunalADActivityBeanList() {
        return communalADActivityBeanList;
    }

    public void setCommunalADActivityBeanList(List<CommunalADActivityBean> communalADActivityBeanList) {
        this.communalADActivityBeanList = communalADActivityBeanList;
    }

    public static class CommunalADActivityBean extends Attribute {
        /**
         * beginTime :
         * link : app://DMLSpringSaleViewController
         * objectId : 0
         * width : 100%
         * web_link : http://ts.domolife.cn/DMLSpringSaleViewController
         * object : 3
         * subtitle : SpringSale
         * picUrl : http://img.domolife.cn/platform/sn5bcT3CTx.png
         * title : 限时特惠
         * height : 100%
         * endTime :
         * bcolor : fdeee9
         * showTime : 0
         */
        @SerializedName(value = "begin_time",alternate = "beginTime")
        private String beginTime;
        private String link;
        @SerializedName(value = "object_id",alternate = "objectId")
        private int objectId;
        private String width;
        @SerializedName(value = "android_link",alternate = "androidLink")
        private String androidLink;
        private int object;
        @SerializedName(value = "sub_title",alternate = "subTitle")
        private String subtitle;
        @SerializedName(value = "pic_url",alternate = "picUrl")
        private String picUrl;
        private String title;
        private String height;
        @SerializedName(value = "end_time",alternate = "endTime")
        private String endTime;
        private String bcolor;
        @SerializedName(value = "show_time",alternate = "showTime")
        private String showTime;
        private String ctime;
//        弹窗频率：0只弹一次，1循环弹窗
        private int frequency_type;
//        间隔天数，如果频率是循环弹窗，就根据这个间隔天数来弹
        private int interval_day;
        @SerializedName("vido_url")
        private String videoUrl;

        public String getVideoUrl() {
            return videoUrl;
        }

        public void setVideoUrl(String videoUrl) {
            this.videoUrl = videoUrl;
        }

        public int getFrequency_type() {
            return frequency_type;
        }

        public void setFrequency_type(int frequency_type) {
            this.frequency_type = frequency_type;
        }

        public int getInterval_day() {
            return interval_day;
        }

        public void setInterval_day(int interval_day) {
            this.interval_day = interval_day;
        }

        public String getAndroidLink() {
            return androidLink;
        }

        public void setAndroidLink(String androidLink) {
            this.androidLink = androidLink;
        }

        public String getBeginTime() {
            return beginTime;
        }

        public void setBeginTime(String beginTime) {
            this.beginTime = beginTime;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public int getObjectId() {
            return objectId;
        }

        public void setObjectId(int objectId) {
            this.objectId = objectId;
        }

        public String getWidth() {
            return width;
        }

        public void setWidth(String width) {
            this.width = width;
        }

        public int getObject() {
            return object;
        }

        public void setObject(int object) {
            this.object = object;
        }

        public String getSubtitle() {
            return subtitle;
        }

        public void setSubtitle(String subtitle) {
            this.subtitle = subtitle;
        }

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

        public String getHeight() {
            return height;
        }

        public void setHeight(String height) {
            this.height = height;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getBcolor() {
            return bcolor;
        }

        public void setBcolor(String bcolor) {
            this.bcolor = bcolor;
        }

        public String getShowTime() {
            return showTime;
        }

        public void setShowTime(String showTime) {
            this.showTime = showTime;
        }

        public String getCtime() {
            return ctime;
        }

        public void setCtime(String ctime) {
            this.ctime = ctime;
        }
    }
}
