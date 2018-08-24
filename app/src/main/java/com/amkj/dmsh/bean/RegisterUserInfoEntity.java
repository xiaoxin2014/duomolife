package com.amkj.dmsh.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by atd48 on 2016/9/26.
 */
public class RegisterUserInfoEntity implements Parcelable {

    /**
     * is_sign : 0
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
     * reg_ip : 116.25.96.117
     * rtime : 2016-08-29 11:01:21
     * login : 0
     */

    @SerializedName("result")
    private RegisterUserInfoBean registerUserInfoBean;
    /**
     * result : {"is_sign":0,"fllow":0,"uid":23287,"sex":0,"nickname":"虫子","last_login_time":0,"status":1,"fans":1,"device_type":"web","score":0,"reg_time":0,"remindtime":30,"reg_ip":"116.25.96.117","rtime":"2016-08-29 11:01:21","login":0}
     * code : 01
     * msg : 请求成功
     */

    private String code;
    private String msg;

    public RegisterUserInfoBean getRegisterUserInfoBean() {
        return registerUserInfoBean;
    }

    public void setRegisterUserInfoBean(RegisterUserInfoBean registerUserInfoBean) {
        this.registerUserInfoBean = registerUserInfoBean;
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

    public static class RegisterUserInfoBean implements Parcelable {
        private int is_sign;
        private int fllow;
        private int uid;
        private int sex;
        private String nickname;
        private int last_login_time;
        private int status;
        private int fans;
        private int score;
        private int remindtime;
        private String reg_ip;
        private String rtime;
        private int login;
        private int mobile_verification;
        private boolean flag;
        private String signature;
        private String mobile;
        private int is_disable;
        private int reg_time;
        private String device_type;
        private boolean baby_verification;
        private String token;
        private String qq;
        private String avatar;

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getQq() {
            return qq;
        }

        public void setQq(String qq) {
            this.qq = qq;
        }

        public boolean isBaby_verification() {
            return baby_verification;
        }

        public void setBaby_verification(boolean baby_verification) {
            this.baby_verification = baby_verification;
        }

        public int isMobile_verification() {
            return mobile_verification;
        }

        public void setMobile_verification(int mobile_verification) {
            this.mobile_verification = mobile_verification;
        }

        public boolean isFlag() {
            return flag;
        }

        public void setFlag(boolean flag) {
            this.flag = flag;
        }

        public String getSignature() {
            return signature;
        }

        public void setSignature(String signature) {
            this.signature = signature;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public int getIs_disable() {
            return is_disable;
        }

        public void setIs_disable(int is_disable) {
            this.is_disable = is_disable;
        }

        public int getIs_sign() {
            return is_sign;
        }

        public void setIs_sign(int is_sign) {
            this.is_sign = is_sign;
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

        public String getReg_ip() {
            return reg_ip;
        }

        public void setReg_ip(String reg_ip) {
            this.reg_ip = reg_ip;
        }

        public String getRtime() {
            return rtime;
        }

        public void setRtime(String rtime) {
            this.rtime = rtime;
        }

        public int getLogin() {
            return login;
        }

        public void setLogin(int login) {
            this.login = login;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.is_sign);
            dest.writeInt(this.fllow);
            dest.writeInt(this.uid);
            dest.writeInt(this.sex);
            dest.writeString(this.nickname);
            dest.writeInt(this.last_login_time);
            dest.writeInt(this.status);
            dest.writeInt(this.fans);
            dest.writeInt(this.score);
            dest.writeInt(this.remindtime);
            dest.writeString(this.reg_ip);
            dest.writeString(this.rtime);
            dest.writeInt(this.login);
            dest.writeInt(this.mobile_verification);
            dest.writeByte(this.flag ? (byte) 1 : (byte) 0);
            dest.writeString(this.signature);
            dest.writeString(this.mobile);
            dest.writeInt(this.is_disable);
            dest.writeInt(this.reg_time);
            dest.writeString(this.device_type);
            dest.writeByte(this.baby_verification ? (byte) 1 : (byte) 0);
            dest.writeString(this.token);
            dest.writeString(this.qq);
            dest.writeString(this.avatar);
        }

        public RegisterUserInfoBean() {
        }

        protected RegisterUserInfoBean(Parcel in) {
            this.is_sign = in.readInt();
            this.fllow = in.readInt();
            this.uid = in.readInt();
            this.sex = in.readInt();
            this.nickname = in.readString();
            this.last_login_time = in.readInt();
            this.status = in.readInt();
            this.fans = in.readInt();
            this.score = in.readInt();
            this.remindtime = in.readInt();
            this.reg_ip = in.readString();
            this.rtime = in.readString();
            this.login = in.readInt();
            this.mobile_verification = in.readInt();
            this.flag = in.readByte() != 0;
            this.signature = in.readString();
            this.mobile = in.readString();
            this.is_disable = in.readInt();
            this.reg_time = in.readInt();
            this.device_type = in.readString();
            this.baby_verification = in.readByte() != 0;
            this.token = in.readString();
            this.qq = in.readString();
            this.avatar = in.readString();
        }

        public static final Creator<RegisterUserInfoBean> CREATOR = new Creator<RegisterUserInfoBean>() {
            @Override
            public RegisterUserInfoBean createFromParcel(Parcel source) {
                return new RegisterUserInfoBean(source);
            }

            @Override
            public RegisterUserInfoBean[] newArray(int size) {
                return new RegisterUserInfoBean[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.registerUserInfoBean, flags);
        dest.writeString(this.code);
        dest.writeString(this.msg);
    }

    public RegisterUserInfoEntity() {
    }

    protected RegisterUserInfoEntity(Parcel in) {
        this.registerUserInfoBean = in.readParcelable(RegisterUserInfoBean.class.getClassLoader());
        this.code = in.readString();
        this.msg = in.readString();
    }

    public static final Creator<RegisterUserInfoEntity> CREATOR = new Creator<RegisterUserInfoEntity>() {
        @Override
        public RegisterUserInfoEntity createFromParcel(Parcel source) {
            return new RegisterUserInfoEntity(source);
        }

        @Override
        public RegisterUserInfoEntity[] newArray(int size) {
            return new RegisterUserInfoEntity[size];
        }
    };
}
