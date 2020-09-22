package com.amkj.dmsh.mine.bean;

import com.amkj.dmsh.base.BaseEntity;
import com.amkj.dmsh.bean.CommunalDetailBean;
import com.amkj.dmsh.user.bean.LikedProductBean;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by xiaoxin on 2020/7/29
 * Version:v4.7.0
 * ClassDescription :每周会员特价商品数据实体类
 */
public class WeekProductEntity extends BaseEntity {

    /**
     * sysTime : 2020-07-29 14:51:16
     * result : {"zoneId":"7","title":"","subtitle":"","goodsList":[{"productId":"18226","productName":"丝滑羽毛纱可爱小鲸鱼纸抽套","subtitle":"12313","productImg":"http://image.domolife.cn/platform/20191224/20191224152232598.png","quantity":"3","vipPrice":"0","vipTag":"","price":"3950","marketPrice":"79"},{"productId":"18331","productName":"纽西之谜温泉水咋弹冻膜 夜间补水保湿睡眠面膜 7粒","subtitle":"纽西之谜温泉水咋弹冻膜 夜间补水保湿睡眠面膜 7粒*3盒","productImg":"http://image.domolife.cn/platform/20200716/20200716140951529.jpeg","quantity":"1","vipPrice":"0","vipTag":"","price":"80","marketPrice":"1"}]}
     */


    private WeekProductBean result;

    public WeekProductBean getResult() {
        return result;
    }

    public void setResult(WeekProductBean result) {
        this.result = result;
    }

    public static class WeekProductBean {
        /**
         * zoneId : 7
         * title :
         * subtitle :
         * goodsList : [{"productId":"18226","productName":"丝滑羽毛纱可爱小鲸鱼纸抽套","subtitle":"12313","productImg":"http://image.domolife.cn/platform/20191224/20191224152232598.png","quantity":"3","vipPrice":"0","vipTag":"","price":"3950","marketPrice":"79"},{"productId":"18331","productName":"纽西之谜温泉水咋弹冻膜 夜间补水保湿睡眠面膜 7粒","subtitle":"纽西之谜温泉水咋弹冻膜 夜间补水保湿睡眠面膜 7粒*3盒","productImg":"http://image.domolife.cn/platform/20200716/20200716140951529.jpeg","quantity":"1","vipPrice":"0","vipTag":"","price":"80","marketPrice":"1"}]
         */

        private String zoneId;
        private String title;
        private String subtitle;
        private String coverImg;
        private List<CommunalDetailBean> description;
        @SerializedName(value = "goodsList", alternate = "productList")
        private List<LikedProductBean> goodsList;

        public List<CommunalDetailBean> getDescription() {
            return description;
        }

        public void setDescription(List<CommunalDetailBean> description) {
            this.description = description;
        }

        public String getCoverImg() {
            return coverImg;
        }

        public void setCoverImg(String coverImg) {
            this.coverImg = coverImg;
        }

        public String getZoneId() {
            return zoneId;
        }

        public void setZoneId(String zoneId) {
            this.zoneId = zoneId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getSubtitle() {
            return subtitle;
        }

        public void setSubtitle(String subtitle) {
            this.subtitle = subtitle;
        }

        public List<LikedProductBean> getGoodsList() {
            return goodsList;
        }

        public void setGoodsList(List<LikedProductBean> goodsList) {
            this.goodsList = goodsList;
        }
    }
}
