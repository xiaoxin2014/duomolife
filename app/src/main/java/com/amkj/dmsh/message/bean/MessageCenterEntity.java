package com.amkj.dmsh.message.bean;

import com.amkj.dmsh.base.BaseTimeEntity;
import com.amkj.dmsh.constant.ConstantMethod;

/**
 * Created by xiaoxin on 2020/4/7
 * Version:v4.5.0
 * ClassDescription :消息中心
 */
public class MessageCenterEntity extends BaseTimeEntity {

    private String noticeTotal;
    private String noticeContent;
    private String orderTotal;
    private String orderContent;
    private String activityTotal;
    private String activityContent;
    private String likeTotal;
    private String likeContent;
    private String focusTotal;
    private String focusContent;
    private String commentTotal;
    private String commentContent;
    private String myRemindContent;


    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public int getCommentTotal() {
        return ConstantMethod.getStringChangeIntegers(commentTotal);
    }

    public void setCommentTotal(String commentTotal) {
        this.commentTotal = commentTotal;
    }

    public int getNoticeTotal() {
        return ConstantMethod.getStringChangeIntegers(noticeTotal);
    }

    public void setNoticeTotal(String noticeTotal) {
        this.noticeTotal = noticeTotal;
    }

    public String getNoticeContent() {
        return noticeContent;
    }

    public void setNoticeContent(String noticeContent) {
        this.noticeContent = noticeContent;
    }

    public int getOrderTotal() {
        return ConstantMethod.getStringChangeIntegers(orderTotal);
    }

    public void setOrderTotal(String orderTotal) {
        this.orderTotal = orderTotal;
    }

    public String getOrderContent() {
        return orderContent;
    }

    public void setOrderContent(String orderContent) {
        this.orderContent = orderContent;
    }

    public int getActivityTotal() {
        return ConstantMethod.getStringChangeIntegers(activityTotal);
    }

    public void setActivityTotal(String activityTotal) {
        this.activityTotal = activityTotal;
    }

    public String getActivityContent() {
        return activityContent;
    }

    public void setActivityContent(String activityContent) {
        this.activityContent = activityContent;
    }

    public int getLikeTotal() {
        return ConstantMethod.getStringChangeIntegers(likeTotal);
    }

    public void setLikeTotal(String likeTotal) {
        this.likeTotal = likeTotal;
    }

    public String getLikeContent() {
        return likeContent;
    }

    public void setLikeContent(String likeContent) {
        this.likeContent = likeContent;
    }

    public int getFocusTotal() {
        return ConstantMethod.getStringChangeIntegers(focusTotal);
    }

    public void setFocusTotal(String focusTotal) {
        this.focusTotal = focusTotal;
    }

    public String getFocusContent() {
        return focusContent;
    }

    public void setFocusContent(String focusContent) {
        this.focusContent = focusContent;
    }

    public String getMyRemindContent() {
        return myRemindContent;
    }

    public void setMyRemindContent(String myRemindContent) {
        this.myRemindContent = myRemindContent;
    }
}
