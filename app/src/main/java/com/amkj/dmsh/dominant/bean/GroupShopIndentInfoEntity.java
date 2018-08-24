package com.amkj.dmsh.dominant.bean;

import com.google.gson.annotations.SerializedName;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/6/20
 * class description:请输入类描述
 */

public class GroupShopIndentInfoEntity {

    /**
     * result : {"order_no":"cX34626X2015X1497883300150","gpRecordId":13,"id":13}
     * currentTime : 2017-06-20 14:35:39
     * msg : 请求成功
     * code : 01
     */

    @SerializedName("result")
    private GroupShopIndentInfoBean groupShopIndentInfoBean;
    private String currentTime;
    private String msg;
    private String code;

    public GroupShopIndentInfoBean getGroupShopIndentInfoBean() {
        return groupShopIndentInfoBean;
    }

    public void setGroupShopIndentInfoBean(GroupShopIndentInfoBean groupShopIndentInfoBean) {
        this.groupShopIndentInfoBean = groupShopIndentInfoBean;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public static class GroupShopIndentInfoBean {
        /**
         * order_no : cX34626X2015X1497883300150
         * gpRecordId : 13
         * id : 13
         */

        private String order_no;
        private int gpRecordId;
        private int id;

        public String getOrder_no() {
            return order_no;
        }

        public void setOrder_no(String order_no) {
            this.order_no = order_no;
        }

        public int getGpRecordId() {
            return gpRecordId;
        }

        public void setGpRecordId(int gpRecordId) {
            this.gpRecordId = gpRecordId;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}
