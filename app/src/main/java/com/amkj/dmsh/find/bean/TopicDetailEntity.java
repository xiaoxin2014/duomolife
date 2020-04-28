package com.amkj.dmsh.find.bean;

import com.amkj.dmsh.base.BaseEntity;
import com.google.gson.annotations.SerializedName;

/**
 * Created by xiaoxin on 2019/7/25
 * Version:v4.0.0
 * ClassDescription :话题详情实体类
 */

public class TopicDetailEntity extends BaseEntity {

    /**
     * sysTime : 2019-07-25 16:58:15
     * id : 18
     * title : qwe
     * content : 测试话题
     * videoUrl : https://image.domolife.cn/merchant/MmeQ8D1563518540807.mp4
     * participantNum : 2
     * nicePostNum : 2
     * isFocus : 0
     */

    private String sysTime;
    private int id;
    private String title;
    private String content;
    @SerializedName(value = "reminder", alternate = "contentReminder")
    private String reminder;//编辑框导语
    private String videoUrl;
    private String firstImgUrl;
    private String imgUrl;
    private int participantNum;
    private int nicePostNum;
    private int isFocus;
    private int score;
    private String rewardTip;//奖励提示
    private String isProductTopic;//是否是商品晒单话题
    private String maxRewardTip;

    public String getMaxRewardTip() {
        return maxRewardTip;
    }

    public void setMaxRewardTip(String maxRewardTip) {
        this.maxRewardTip = maxRewardTip;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public boolean isProductTopic() {
        return isProductTopic.equals("1");
    }


    public String getRewardTip() {
        return rewardTip;
    }

    public void setRewardTip(String rewardTip) {
        this.rewardTip = rewardTip;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getReminder() {
        return reminder;
    }

    public void setReminder(String reminder) {
        this.reminder = reminder;
    }

    public String getFirstImgUrl() {
        return firstImgUrl;
    }

    public void setFirstImgUrl(String firstImgUrl) {
        this.firstImgUrl = firstImgUrl;
    }

    public String getSysTime() {
        return sysTime;
    }

    public void setSysTime(String sysTime) {
        this.sysTime = sysTime;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public int getParticipantNum() {
        return participantNum;
    }

    public void setParticipantNum(int participantNum) {
        this.participantNum = participantNum;
    }

    public int getNicePostNum() {
        return nicePostNum;
    }

    public void setNicePostNum(int nicePostNum) {
        this.nicePostNum = nicePostNum;
    }

    public boolean getIsFocus() {
        return isFocus == 1;
    }

    public void setIsFocus(int isFocus) {
        this.isFocus = isFocus;
    }
}
