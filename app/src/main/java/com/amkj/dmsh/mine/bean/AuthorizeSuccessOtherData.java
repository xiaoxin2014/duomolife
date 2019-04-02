package com.amkj.dmsh.mine.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by atd48 on 2016/10/9.
 */
public class AuthorizeSuccessOtherData implements Parcelable {

    /**
     * fllow : 0
     * uid : 23287
     * sex : 0
     * nickname : 虫子
     * last_login_time : 0
     * status : 1
     * fans : 1
     * device_type : web
     * score : 0
     * reg_time : 0
     * remindtime : 30
     * avatar : 头像路径
     * type : wechat
     * reg_ip : 116.25.96.117
     * mobile_verification : true
     * rtime : 2016-08-29 11:01:21
     * openid : oux9Ws1BCXjM331J7aAdaZVt1oic
     * login : 0
     */

    @SerializedName("result")
    private OtherAccountBean otherAccountBean;
    /**
     * result : {"fllow":0,"uid":23287,"sex":0,"nickname":"虫子","last_login_time":0,"status":1,"fans":1,"device_type":"web","score":0,"reg_time":0,"remindtime":30,"avatar":"头像路径","type":"wechat","reg_ip":"116.25.96.117","mobile_verification":true,"rtime":"2016-08-29 11:01:21","openid":"oux9Ws1BCXjM331J7aAdaZVt1oic","login":0}
     * code : 01
     * msg : 请求成功
     */

    private String code;
    private String msg;

    public OtherAccountBean getOtherAccountBean() {
        return otherAccountBean;
    }

    public void setOtherAccountBean(OtherAccountBean otherAccountBean) {
        this.otherAccountBean = otherAccountBean;
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

    public static class OtherAccountBean implements Parcelable {
        private int fllow;
        private int uid;
        private int sex;
        private String nickname;
        private int last_login_time;
        private int status;
        private int fans;
        private String device_type;
        private int score;
        private int reg_time;
        private int remindtime;
        private String avatar;
        private String type;
        private String reg_ip;
        private boolean mobile_verification;
        private String rtime;
        private String openid;
        private int login;
        private String mobile;
        private String token;
        private long tokenExpireSeconds;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public long getTokenExpireSeconds() {
            return tokenExpireSeconds;
        }

        public void setTokenExpireSeconds(long tokenExpireSeconds) {
            this.tokenExpireSeconds = tokenExpireSeconds;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public int getFllow() {
            return fllow;
        }

        public void setFllow(int fllow) {
            this.fllow = fllow;
        }

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public int getLast_login_time() {
            return last_login_time;
        }

        public void setLast_login_time(int last_login_time) {
            this.last_login_time = last_login_time;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getFans() {
            return fans;
        }

        public void setFans(int fans) {
            this.fans = fans;
        }

        public String getDevice_type() {
            return device_type;
        }

        public void setDevice_type(String device_type) {
            this.device_type = device_type;
        }

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }

        public int getReg_time() {
            return reg_time;
        }

        public void setReg_time(int reg_time) {
            this.reg_time = reg_time;
        }

        public int getRemindtime() {
            return remindtime;
        }

        public void setRemindtime(int remindtime) {
            this.remindtime = remindtime;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getReg_ip() {
            return reg_ip;
        }

        public void setReg_ip(String reg_ip) {
            this.reg_ip = reg_ip;
        }

        public boolean isMobile_verification() {
            return mobile_verification;
        }

        public void setMobile_verification(boolean mobile_verification) {
            this.mobile_verification = mobile_verification;
        }

        public String getRtime() {
            return rtime;
        }

        public void setRtime(String rtime) {
            this.rtime = rtime;
        }

        public String getOpenid() {
            return openid;
        }

        public void setOpenid(String openid) {
            this.openid = openid;
        }

        public int getLogin() {
            return login;
        }

        public void setLogin(int login) {
            this.login = login;
        }

        public OtherAccountBean() {
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.fllow);
            dest.writeInt(this.uid);
            dest.writeInt(this.sex);
            dest.writeString(this.nickname);
            dest.writeInt(this.last_login_time);
            dest.writeInt(this.status);
            dest.writeInt(this.fans);
            dest.writeString(this.device_type);
            dest.writeInt(this.score);
            dest.writeInt(this.reg_time);
            dest.writeInt(this.remindtime);
            dest.writeString(this.avatar);
            dest.writeString(this.type);
            dest.writeString(this.reg_ip);
            dest.writeByte(this.mobile_verification ? (byte) 1 : (byte) 0);
            dest.writeString(this.rtime);
            dest.writeString(this.openid);
            dest.writeInt(this.login);
            dest.writeString(this.mobile);
            dest.writeString(this.token);
            dest.writeLong(this.tokenExpireSeconds);
        }

        protected OtherAccountBean(Parcel in) {
            this.fllow = in.readInt();
            this.uid = in.readInt();
            this.sex = in.readInt();
            this.nickname = in.readString();
            this.last_login_time = in.readInt();
            this.status = in.readInt();
            this.fans = in.readInt();
            this.device_type = in.readString();
            this.score = in.readInt();
            this.reg_time = in.readInt();
            this.remindtime = in.readInt();
            this.avatar = in.readString();
            this.type = in.readString();
            this.reg_ip = in.readString();
            this.mobile_verification = in.readByte() != 0;
            this.rtime = in.readString();
            this.openid = in.readString();
            this.login = in.readInt();
            this.mobile = in.readString();
            this.token = in.readString();
            this.tokenExpireSeconds = in.readLong();
        }

        public static final Creator<OtherAccountBean> CREATOR = new Creator<OtherAccountBean>() {
            @Override
            public OtherAccountBean createFromParcel(Parcel source) {
                return new OtherAccountBean(source);
            }

            @Override
            public OtherAccountBean[] newArray(int size) {
                return new OtherAccountBean[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.otherAccountBean, flags);
        dest.writeString(this.code);
        dest.writeString(this.msg);
    }

    public AuthorizeSuccessOtherData() {
    }

    protected AuthorizeSuccessOtherData(Parcel in) {
        this.otherAccountBean = in.readParcelable(OtherAccountBean.class.getClassLoader());
        this.code = in.readString();
        this.msg = in.readString();
    }

    public static final Creator<AuthorizeSuccessOtherData> CREATOR = new Creator<AuthorizeSuccessOtherData>() {
        @Override
        public AuthorizeSuccessOtherData createFromParcel(Parcel source) {
            return new AuthorizeSuccessOtherData(source);
        }

        @Override
        public AuthorizeSuccessOtherData[] newArray(int size) {
            return new AuthorizeSuccessOtherData[size];
        }
    };
}
