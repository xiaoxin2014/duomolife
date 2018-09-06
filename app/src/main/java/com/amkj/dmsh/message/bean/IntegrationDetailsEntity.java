package com.amkj.dmsh.message.bean;

import com.amkj.dmsh.base.BaseEntity;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/3/11
 * class description:积分明细
 */

public class IntegrationDetailsEntity extends BaseEntity{

    /**
     * result : [{"score":5,"ctime":"2017-08-22 14:31:40.0","id":715,"type":4,"title":"签到","content":"签到获得5积分"},{"score":10,"ctime":"2017-08-22 14:30:41.0","id":714,"type":4,"title":"签到","content":"签到获得10积分"},{"score":10,"ctime":"2017-08-22 14:27:59.0","id":713,"type":4,"title":"签到","content":"签到获得10积分"},{"score":5,"ctime":"2017-08-22 14:26:36.0","id":712,"type":4,"title":"签到","content":"签到获得5积分"},{"score":5,"ctime":"2017-08-22 09:58:12.0","id":702,"type":4,"title":"签到","content":"签到获得5积分"},{"score":-10,"ctime":"2017-08-21 15:45:43.0","id":689,"type":-3,"title":"积分抽奖","content":"积分抽奖扣减10积分"},{"score":50,"ctime":"2017-08-21 15:45:16.0","id":687,"type":6,"title":"抽奖","content":"抽奖获得50积分"},{"score":5,"ctime":"2017-08-21 09:59:34.0","id":646,"type":4,"title":"签到","content":"签到获得5积分"},{"score":5,"ctime":"2017-08-21 09:50:09.0","id":645,"type":4,"title":"签到","content":"签到获得5积分"},{"score":30,"ctime":"2017-08-12 11:58:38.0","id":540,"type":6,"title":"抽奖","content":"抽奖获得30积分"}]
     * msg : 请求成功
     * code : 01
     * totalScore : 987654256
     */
    private int totalScore;
    /**
     * nowScore : 995
     * shopScore : 91
     * taskScore : 559
     * signScore : 1636
     * otherScore : -2597
     */

    private int nowScore;
    private int shopScore;
    private int taskScore;
    private int signScore;
    private int otherScore;

    @SerializedName("result")
    private List<IntegrationDetailsBean> integrationDetailsList;

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    public List<IntegrationDetailsBean> getIntegrationDetailsList() {
        return integrationDetailsList;
    }

    public void setIntegrationDetailsList(List<IntegrationDetailsBean> integrationDetailsList) {
        this.integrationDetailsList = integrationDetailsList;
    }

    public int getNowScore() {
        return nowScore;
    }

    public void setNowScore(int nowScore) {
        this.nowScore = nowScore;
    }

    public int getShopScore() {
        return shopScore;
    }

    public void setShopScore(int shopScore) {
        this.shopScore = shopScore;
    }

    public int getTaskScore() {
        return taskScore;
    }

    public void setTaskScore(int taskScore) {
        this.taskScore = taskScore;
    }

    public int getSignScore() {
        return signScore;
    }

    public void setSignScore(int signScore) {
        this.signScore = signScore;
    }

    public int getOtherScore() {
        return otherScore;
    }

    public void setOtherScore(int otherScore) {
        this.otherScore = otherScore;
    }

    public static class IntegrationDetailsBean {
        /**
         * score : 5
         * ctime : 2017-08-22 14:31:40.0
         * id : 715
         * type : 4
         * title : 签到
         * content : 签到获得5积分
         */

        private int score;
        private String ctime;
        private int id;
        private int type;
        private String title;
        private String content;

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
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

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
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
    }
}
