package com.amkj.dmsh.message.bean;

import com.amkj.dmsh.base.BaseEntity;
import com.google.gson.annotations.SerializedName;

/**
 * Created by atd48 on 2016/9/9.
 */
public class MessageTotalEntity extends BaseEntity{

    /**
     * result : {"focusTotal":0,"commentTotal":0,"smTotal":0,"commOffifialTotal":0,"favorTotal":0,"orderTotal":0,"likeTotal":1}
     * msg : 请求成功
     * code : 01
     */

    @SerializedName("result")
    private MessageTotalBean messageTotalBean;

    public MessageTotalBean getMessageTotalBean() {
        return messageTotalBean;
    }

    public void setMessageTotalBean(MessageTotalBean messageTotalBean) {
        this.messageTotalBean = messageTotalBean;
    }

    public static class MessageTotalBean {
        /**
         * focusTotal : 0
         * commentTotal : 0
         * smTotal : 0
         * commOffifialTotal : 0
         * favorTotal : 0
         * orderTotal : 0
         * likeTotal : 1
         */

        private int focusTotal;
        private int commentTotal;
        private int smTotal;
        private int commOffifialTotal;
        private int favorTotal;
        private int orderTotal;
        private int likeTotal;

        public int getFocusTotal() {
            return focusTotal;
        }

        public void setFocusTotal(int focusTotal) {
            this.focusTotal = focusTotal;
        }

        public int getCommentTotal() {
            return commentTotal;
        }

        public void setCommentTotal(int commentTotal) {
            this.commentTotal = commentTotal;
        }

        public int getSmTotal() {
            return smTotal;
        }

        public void setSmTotal(int smTotal) {
            this.smTotal = smTotal;
        }

        public int getCommOffifialTotal() {
            return commOffifialTotal;
        }

        public void setCommOffifialTotal(int commOffifialTotal) {
            this.commOffifialTotal = commOffifialTotal;
        }

        public int getFavorTotal() {
            return favorTotal;
        }

        public void setFavorTotal(int favorTotal) {
            this.favorTotal = favorTotal;
        }

        public int getOrderTotal() {
            return orderTotal;
        }

        public void setOrderTotal(int orderTotal) {
            this.orderTotal = orderTotal;
        }

        public int getLikeTotal() {
            return likeTotal;
        }

        public void setLikeTotal(int likeTotal) {
            this.likeTotal = likeTotal;
        }
    }
}
