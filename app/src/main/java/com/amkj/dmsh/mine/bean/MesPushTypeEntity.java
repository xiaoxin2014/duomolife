package com.amkj.dmsh.mine.bean;

import com.amkj.dmsh.base.BaseEntity;
import com.amkj.dmsh.utils.gson.GsonUtils;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/9/28
 * class description:消息推送
 */

public class MesPushTypeEntity extends BaseEntity{

    /**
     * result : [{"isOpen":1,"name":"评论","id":1,"status":1},{"isOpen":1,"name":"赞","id":2,"status":1},{"isOpen":1,"name":"系统通知","id":3,"status":1},{"isOpen":1,"name":"签到","id":4,"status":0},{"isOpen":1,"name":"秒杀提醒","id":5,"status":0},{"isOpen":1,"name":"默认开启","id":6,"status":0},{"isOpen":1,"name":"活动通知","id":7,"status":1},{"isOpen":1,"name":"订单消息","id":8,"status":1},{"isOpen":1,"name":"购物车降价提醒","id":9,"status":1}]
     * msg : 请求成功
     * code : 01
     */
    @SerializedName("result")
    private List<MesPushTypeBean> mesPushTypeBeanList;

    public static MesPushTypeEntity objectFromData(String str) {

        return GsonUtils.fromJson(str, MesPushTypeEntity.class);
    }

    public List<MesPushTypeBean> getMesPushTypeBeanList() {
        return mesPushTypeBeanList;
    }

    public void setMesPushTypeBeanList(List<MesPushTypeBean> mesPushTypeBeanList) {
        this.mesPushTypeBeanList = mesPushTypeBeanList;
    }

    public static class MesPushTypeBean {
        /**
         * isOpen : 1
         * name : 评论
         * id : 1
         * status : 1
         */

        private int isOpen;
        private String name;
        private int id;
        private int status;

        public static MesPushTypeBean objectFromData(String str) {

            return GsonUtils.fromJson(str, MesPushTypeBean.class);
        }

        public int getIsOpen() {
            return isOpen;
        }

        public void setIsOpen(int isOpen) {
            this.isOpen = isOpen;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }
}
