package com.amkj.dmsh.homepage.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.amkj.dmsh.base.BaseTimeEntity;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/7/25
 * version 3.1.5
 * class description:积分夺宝
 */
public class IntegralLotteryEntity extends BaseTimeEntity {

    /**
     * code : 01
     * msg : 请求成功
     * id : 1
     * activityCode : 2018072001
     * activityStatus : 正在进行
     * image : http://image.domolife.cn/platform/Qi4tdnWXcF1532078469292.jpg
     * prizeName : 随缘
     * prizeNum : 1
     * startTime : 2018-07-01 00:00:00
     * endTime : 2018-07-28 00:00:00
     * lotteryCode : []
     * previousInfo : {"id":2,"activityCode":"2018072101","activityStatus":"已结束","image":"http://image.domolife.cn/platform/TCsSKc6nkc1532154235910.png","prizeName":"我是已结束的抽奖","prizeNum":1,"startTime":"2018-07-01 00:00:00","endTime":"2018-07-02 00:00:00","lotteryCode":["2018072002101"],"winList":[{"winningCode":"2018072002101","nickName":"穆茨d'god like","avatar":"http://image.domolife.cn/201704280903427554142183.png"}],"recordNum":1,"winning":true}
     * nextPreviousInfo : {"id":3,"activityCode":"2018072102","activityStatus":"即将开始","image":"http://image.domolife.cn/platform/j8xAFByXta1532154249173.jpg","prizeName":"我还没开始","prizeNum":2,"startTime":"2018-07-26 00:00:00","endTime":"2018-07-27 00:00:00"}
     * winning : false
     * recordNum : 7
     */
    @SerializedName("result")
    private List<PreviousInfoBean> previousInfoList;

    public List<PreviousInfoBean> getPreviousInfoList() {
        return previousInfoList;
    }

    public void setPreviousInfoList(List<PreviousInfoBean> previousInfoList) {
        this.previousInfoList = previousInfoList;
    }

    public static class PreviousInfoBean {
        /**
         * id : 2
         * activityCode : 2018072101
         * activityStatus : 已结束
         * image : http://image.domolife.cn/platform/TCsSKc6nkc1532154235910.png
         * prizeName : 我是已结束的抽奖
         * prizeNum : 1
         * startTime : 2018-07-01 00:00:00
         * endTime : 2018-07-02 00:00:00
         * lotteryCode : ["2018072002101"]
         * winList : [{"winningCode":"2018072002101","nickName":"穆茨d'god like","avatar":"http://image.domolife.cn/201704280903427554142183.png"}]
         * recordNum : 1
         * winning : true
         */

        private int id;
        private String activityCode;
        private String activityStatus;
        private String image;
        private String prizeName;
        private int prizeNum;
        private String startTime;
        private String endTime;
        private int recordNum;
        private boolean winning;
        private List<String> lotteryCode;
        private List<WinListBean> winList;
        private int mSeconds;
        private String mCurrentTime;
        private Object timeObject;
        private String productId;

        //新增字段
        private String isShareNumMax; //是否分享获取次数达上限  1是0否
        private int score;//参与消耗积分数


        public boolean isShareNumMax() {
            return "1".equals(isShareNumMax);
        }

        public void setIsShareNumMax(String isShareNumMax) {
            this.isShareNumMax = isShareNumMax;
        }

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }

        public Object getTimeObject() {
            return timeObject;
        }

        public void setTimeObject(Object timeObject) {
            this.timeObject = timeObject;
        }

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public String getmCurrentTime() {
            return mCurrentTime;
        }

        public void setmCurrentTime(String mCurrentTime) {
            this.mCurrentTime = mCurrentTime;
        }

        public int getmSeconds() {
            return mSeconds;
        }

        public void setmSeconds(int mSeconds) {
            this.mSeconds = mSeconds;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getActivityCode() {
            return activityCode;
        }

        public void setActivityCode(String activityCode) {
            this.activityCode = activityCode;
        }

        public String getActivityStatus() {
            return activityStatus;
        }

        public void setActivityStatus(String activityStatus) {
            this.activityStatus = activityStatus;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getPrizeName() {
            return prizeName;
        }

        public void setPrizeName(String prizeName) {
            this.prizeName = prizeName;
        }

        public int getPrizeNum() {
            return prizeNum;
        }

        public void setPrizeNum(int prizeNum) {
            this.prizeNum = prizeNum;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public int getRecordNum() {
            return recordNum;
        }

        public void setRecordNum(int recordNum) {
            this.recordNum = recordNum;
        }

        public boolean isWinning() {
            return winning;
        }

        public void setWinning(boolean winning) {
            this.winning = winning;
        }

        public List<String> getLotteryCode() {
            return lotteryCode;
        }

        public void setLotteryCode(List<String> lotteryCode) {
            this.lotteryCode = lotteryCode;
        }

        public List<WinListBean> getWinList() {
            return winList;
        }

        public void setWinList(List<WinListBean> winList) {
            this.winList = winList;
        }

        public static class WinListBean implements Parcelable {
            /**
             * winningCode : 2018072002101
             * nickName : 穆茨d'god like
             * avatar : http://image.domolife.cn/201704280903427554142183.png
             */

            private String winningCode;
            private String nickName;
            private String avatar;
            private int uid;

            public String getWinningCode() {
                return winningCode;
            }

            public void setWinningCode(String winningCode) {
                this.winningCode = winningCode;
            }

            public String getNickName() {
                return nickName;
            }

            public void setNickName(String nickName) {
                this.nickName = nickName;
            }

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public int getUid() {
                return uid;
            }

            public void setUid(int uid) {
                this.uid = uid;
            }

            public WinListBean() {
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.winningCode);
                dest.writeString(this.nickName);
                dest.writeString(this.avatar);
                dest.writeInt(this.uid);
            }

            protected WinListBean(Parcel in) {
                this.winningCode = in.readString();
                this.nickName = in.readString();
                this.avatar = in.readString();
                this.uid = in.readInt();
            }

            public static final Creator<WinListBean> CREATOR = new Creator<WinListBean>() {
                @Override
                public WinListBean createFromParcel(Parcel source) {
                    return new WinListBean(source);
                }

                @Override
                public WinListBean[] newArray(int size) {
                    return new WinListBean[size];
                }
            };
        }
    }
}
