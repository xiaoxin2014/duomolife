package com.amkj.dmsh.bean;

import android.text.TextUtils;

import com.amkj.dmsh.base.BaseTimeEntity;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.utils.gson.GsonUtils;
import com.google.gson.annotations.SerializedName;

/**
 * Created by atd48 on 2016/9/2.
 */
public class RequestStatus extends BaseTimeEntity {
    private boolean isSign;
    private boolean isCollect;
    private int registerFlag;
    private Result result;
    private int longtime;
    //    分享奖励 图片地址
    private String src;
    //    新人专享 封面图
    @SerializedName(value = "imgUrl", alternate = "picUrl")
    private String imgUrl;
    @SerializedName(value = "android_link", alternate = "androidLink")
    private String androidLink;
    private String id;
    private int orderRefundProductId;
    private String version;
    private long second;
    private String description;
    private int isNotice;
    //    新人优惠
    private int couponId;
    //    新人类型
    private int userType;

    //    <--评分晒单成功-->
    //    奖励积分数量
    private int score;
    //    分享提示
    private String reminder;
    //    帖子id
    private int postId;
    //    帖子摘要
    private String digest;
    //    封面图
    private String coverPath;
    //    话题名称
    private String topicTitle;

    //    token相关
    private String token;
    private String tokenExpireSeconds;

    //    弹窗编号
    private String no;
    private int targetId;

    //未完成拼团弹窗相关
    private String gpInfoId;
    private String gpRecordId;
    private String personNum;
    private String price;
    private String coverImage;
    private String gpEndTime;
    private String gpName;
    private String type;//0特价团，1抽奖团
    private String range;//0特价团，1邀新团

    //未读的平台客服通知
    private String time;
    private String content;
    private String link;

    //申请退款成功
    private String refundNo;

    //评分弹窗
    private String isOpen;

    //评论成功
    private String drawRuleId;
    private String evaluateId ;

    public String getDrawRuleId() {
        return drawRuleId;
    }

    public void setDrawRuleId(String drawRuleId) {
        this.drawRuleId = drawRuleId;
    }

    public String getEvaluateId() {
        return evaluateId;
    }

    public void setEvaluateId(String evaluateId) {
        this.evaluateId = evaluateId;
    }

    public boolean isOpen() {
        return "1".equals(isOpen);
    }

    public void setIsOpen(String isOpen) {
        this.isOpen = isOpen;
    }

    public String getRefundNo() {
        return refundNo;
    }

    public void setRefundNo(String refundNo) {
        this.refundNo = refundNo;
    }

    public static RequestStatus objectFromData(String str) {
        return GsonUtils.fromJson(str, RequestStatus.class);
    }


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getId() {
        return ConstantMethod.getStringChangeIntegers(id);
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAndroidLink() {
        return androidLink;
    }

    public void setAndroidLink(String androidLink) {
        this.androidLink = androidLink;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getRange() {
        return ConstantMethod.getStringChangeIntegers(range);
    }

    public void setRange(String range) {
        this.range = range;
    }

    public int getTargetId() {
        return targetId;
    }

    public void setTargetId(int targetId) {
        this.targetId = targetId;
    }

    public String getGpName() {
        return gpName;
    }

    public void setGpName(String gpName) {
        this.gpName = gpName;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getToken() {
        return token;
    }

    public long getTokenExpireSeconds() {
        return ConstantMethod.getStringChangeLong(tokenExpireSeconds) * 1000;
    }

    public String getTopicTitle() {
        return topicTitle;
    }

    public void setTopicTitle(String topicTitle) {
        this.topicTitle = topicTitle;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    public String getCoverPath() {
        return coverPath;
    }

    public void setCoverPath(String coverPath) {
        this.coverPath = coverPath;
    }

    public void setReminder(String reminder) {
        this.reminder = reminder;
    }

    public String getReminder() {
        return reminder;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getCouponId() {
        return couponId;
    }

    public void setCouponId(int couponId) {
        this.couponId = couponId;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getIsNotice() {
        return isNotice;
    }

    public void setIsNotice(int isNotice) {
        this.isNotice = isNotice;
    }

    public long getSecond() {
        return second;
    }

    public void setSecond(long second) {
        this.second = second;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public boolean isCollect() {
        return isCollect;
    }

    public void setCollect(boolean collect) {
        isCollect = collect;
    }

    public int getOrderRefundProductId() {
        return orderRefundProductId;
    }

    public void setOrderRefundProductId(int orderRefundProductId) {
        this.orderRefundProductId = orderRefundProductId;
    }

    public int getLongtime() {
        return longtime;
    }

    public void setLongtime(int longtime) {
        this.longtime = longtime;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public String getGpInfoId() {
        return gpInfoId;
    }

    public void setGpInfoId(String gpInfoId) {
        this.gpInfoId = gpInfoId;
    }

    public String getGpRecordId() {
        return gpRecordId;
    }

    public void setGpRecordId(String gpRecordId) {
        this.gpRecordId = gpRecordId;
    }

    public String getPersonNum() {
        return personNum;
    }

    public void setPersonNum(String personNum) {
        this.personNum = personNum;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public String getGpEndTime() {
        return gpEndTime;
    }

    public void setGpEndTime(String gpEndTime) {
        this.gpEndTime = gpEndTime;
    }

    public class Result {
        @SerializedName(value = "resultCode", alternate = "code")
        private String resultCode;
        @SerializedName(value = "resultMsg", alternate = "msg")
        private String resultMsg;
        private String nickname;
        private String invitation_content;
        private String username;
        private String avatar;
        private boolean isCollect;
        private int cartNumber;
        private int remindtime;
        private boolean hadRemind;
        private int collectSize;
        private String imgUrl;
        private String androidLink;

        /**
         * 退款去向字段
         */
        private String examineTime;
        private String receiveRefundTime;
        private String refundAccount;
        private String refundPrice;

        /**
         * 月消费送会员条件
         * contentTop : 月消费满500元，免费赠送7天会员
         * contentBelow : 本月已消费0.03元，还差499.97元即将获得
         * percent : 0.01
         */
        private String contentTop;
        private String contentBelow;
        private String percent;


        /**
         * 会员相关
         */
        private String cardName;
        private String nickName;
        private String endTime;
        private String beEconomical;
        private String vipLevel;
        private String predictBeEconomical;
        private String title;
        private String subtitle;
        private String coverImg;
        private String picUrl;
        private String giftText;
        private String zoneId;//会员专区id
        private String priceText;
        private String marketPrice;
        private String isVip;

        /**
         * 0元报告相关
         */
        private String activityId;
        private String orderId;
        private String content;
        private String imgs;
        private String star;
        private String productId;

        public String getAndroidLink() {
            return androidLink;
        }

        public void setAndroidLink(String androidLink) {
            this.androidLink = androidLink;
        }

        public boolean isVip() {
            return "1".equals(isVip);
        }

        public void setIsVip(String isVip) {
            this.isVip = isVip;
        }

        public String getPriceText() {
            return priceText;
        }

        public void setPriceText(String priceText) {
            this.priceText = priceText;
        }

        public String getMarketPrice() {
            return marketPrice;
        }

        public void setMarketPrice(String marketPrice) {
            this.marketPrice = marketPrice;
        }

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public String getActivityId() {
            return activityId;
        }

        public void setActivityId(String activityId) {
            this.activityId = activityId;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getImgs() {
            return imgs;
        }

        public void setImgs(String imgs) {
            this.imgs = imgs;
        }

        public String getStar() {
            return star;
        }

        public void setStar(String star) {
            this.star = star;
        }

        public String getZoneId() {
            return zoneId;
        }

        public void setZoneId(String zoneId) {
            this.zoneId = zoneId;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public int getCollectSize() {
            return collectSize;
        }

        public void setCollectSize(int collectSize) {
            this.collectSize = collectSize;
        }

        public String getResultCode() {
            return resultCode;
        }

        public void setResultCode(String resultCode) {
            this.resultCode = resultCode;
        }

        public String getResultMsg() {
            return TextUtils.isEmpty(resultMsg) ? getMsg() : resultMsg;
        }

        public void setResultMsg(String resultMsg) {
            this.resultMsg = resultMsg;
        }

        public boolean isCollect() {
            return isCollect;
        }

        public void setCollect(boolean collect) {
            isCollect = collect;
        }

        public int getRemindtime() {
            return remindtime;
        }

        public void setRemindtime(int remindtime) {
            this.remindtime = remindtime;
        }

        public boolean isHadRemind() {
            return hadRemind;
        }

        public void setHadRemind(boolean hadRemind) {
            this.hadRemind = hadRemind;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getInvitation_content() {
            return invitation_content;
        }

        public void setInvitation_content(String invitation_content) {
            this.invitation_content = invitation_content;
        }

        public int getCartNumber() {
            return cartNumber;
        }

        public void setCartNumber(int cartNumber) {
            this.cartNumber = cartNumber;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getExamineTime() {
            return examineTime;
        }

        public void setExamineTime(String examineTime) {
            this.examineTime = examineTime;
        }

        public String getReceiveRefundTime() {
            return receiveRefundTime;
        }

        public void setReceiveRefundTime(String receiveRefundTime) {
            this.receiveRefundTime = receiveRefundTime;
        }

        public String getRefundAccount() {
            return refundAccount;
        }

        public void setRefundAccount(String refundAccount) {
            this.refundAccount = refundAccount;
        }

        public String getRefundPrice() {
            return refundPrice;
        }

        public void setRefundPrice(String refundPrice) {
            this.refundPrice = refundPrice;
        }

        public String getContentTop() {
            return contentTop;
        }

        public void setContentTop(String contentTop) {
            this.contentTop = contentTop;
        }

        public String getContentBelow() {
            return contentBelow;
        }

        public void setContentBelow(String contentBelow) {
            this.contentBelow = contentBelow;
        }

        public String getPercent() {
            return percent;
        }

        public void setPercent(String percent) {
            this.percent = percent;
        }

        public String getCardName() {
            return cardName;
        }

        public void setCardName(String cardName) {
            this.cardName = cardName;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getBeEconomical() {
            return beEconomical;
        }

        public void setBeEconomical(String beEconomical) {
            this.beEconomical = beEconomical;
        }

        public int getVipLevel() {
            int level = ConstantMethod.getStringChangeIntegers(vipLevel);
            return Math.max(level, 1);
        }

        public void setVipLevel(String vipLevel) {
            this.vipLevel = vipLevel;
        }

        public String getPredictBeEconomical() {
            return predictBeEconomical;
        }

        public void setPredictBeEconomical(String predictBeEconomical) {
            this.predictBeEconomical = predictBeEconomical;
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

        public String getCoverImg() {
            return coverImg;
        }

        public void setCoverImg(String coverImg) {
            this.coverImg = coverImg;
        }

        public String getPicUrl() {
            return picUrl;
        }

        public void setPicUrl(String picUrl) {
            this.picUrl = picUrl;
        }

        public String getGiftText() {
            return giftText;
        }

        public void setGiftText(String giftText) {
            this.giftText = giftText;
        }
    }

    public boolean isSign() {
        return isSign;
    }

    public void setSign(boolean sign) {
        isSign = sign;
    }

    public int getRegisterFlag() {
        return registerFlag;
    }

    public void setRegisterFlag(int registerFlag) {
        this.registerFlag = registerFlag;
    }
}
