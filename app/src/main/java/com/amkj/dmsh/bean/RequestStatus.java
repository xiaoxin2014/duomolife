package com.amkj.dmsh.bean;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

/**
 * Created by atd48 on 2016/9/2.
 */
public class RequestStatus {

    /**
     * code : 01
     * msg : 请求成功
     */

    private String code;
    private String msg;
    private boolean isSign;
    private boolean isCollect;
    private int registerFlag;
    private Result result;
    private int longtime;
    //    分享奖励 图片地址
    private String src;
    //    新人专享 封面图
    @SerializedName(value = "imgUrl",alternate = "picUrl")
    private String imgUrl;
    private int orderRefundProductId;
    private String version;
    private long second;
    private String description;
    private int isNotice;
    //    新人优惠
    private int couponId ;
//    新人类型
    private int userType  ;

    public static RequestStatus objectFromData(String str) {
        return new Gson().fromJson(str, RequestStatus.class);
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

    public class Result {
        private String code;
        private String msg;
        private String nickname;
        private String invitation_content;
        private String username;
        private String avatar;
        private boolean isCollect;
        private int cartNumber;
        private int remindtime;
        private boolean hadRemind;
        private String resultCode;
        private String resultMsg;
        private int collectSize;
        private String imgUrl;

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
            return resultMsg;
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

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

    }

    public boolean isSign() {
        return isSign;
    }

    public void setSign(boolean sign) {
        isSign = sign;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getRegisterFlag() {
        return registerFlag;
    }

    public void setRegisterFlag(int registerFlag) {
        this.registerFlag = registerFlag;
    }
}
