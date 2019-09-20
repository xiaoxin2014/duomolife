package com.amkj.dmsh.mine.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.amkj.dmsh.base.BaseEntity;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by atd48 on 2016/9/26.
 */
public class OtherAccountBindEntity extends BaseEntity {

    /**
     * result : [{"uid":23295,"nickname":"5ZCR6Ziz","status":1,"openid":"oQpWFuM7j0ZtiK_54qFTfnF3_-Lc","avatar":"http://img.domolife.cn/201610092231454897863841.png","type":"wechat","mobile":"13751077044"},{"uid":23295,"nickname":"5aSp6L6w5Zyw5bCY","status":1,"openid":"2645533022","avatar":"http://img.domolife.cn/201610101437397598888634.png","type":"sina","mobile":"13751077044"},{"uid":23295,"nickname":"5aSp6L6w5Zyw5bCY","status":1,"openid":"2645533022","avatar":"http://img.domolife.cn/201610101437397598888634.png","type":"sina","mobile":"13751077044"},{"uid":23295,"nickname":"5ZCR6Ziz","status":1,"openid":"5218733487305B449300A937E5AE112E","avatar":"http://img.domolife.cn/201610101439128235753662.png","type":"qq","mobile":"13751077044"}]
     * code : 01
     * msg : 请求成功
     */
    /**
     * uid : 23295
     * nickname : 5ZCR6Ziz
     * status : 1
     * openid : oQpWFuM7j0ZtiK_54qFTfnF3_-Lc
     * avatar : http://img.domolife.cn/201610092231454897863841.png
     * type : wechat
     * mobile : 13751077044
     */

    @SerializedName("result")
    private List<OtherAccountBindInfo> otherAccountBindInfo;

    public List<OtherAccountBindInfo> getOtherAccountBindInfo() {
        return otherAccountBindInfo;
    }

    public void setOtherAccountBindInfo(List<OtherAccountBindInfo> otherAccountBindInfo) {
        this.otherAccountBindInfo = otherAccountBindInfo;
    }

    public static class OtherAccountBindInfo implements Parcelable {
        private int status;
        private String mobile;
        private String openid;
        private String unionId;
        private String type;
        private String nickname;
        private String avatar;
        private String mobileNum;
        private int sex;
        private int uid;
        private boolean isMobile_verification;
        private String accessToken;

        public String getAccessToken() {
            return accessToken;
        }

        public void setAccessToken(String accessToken) {
            this.accessToken = accessToken;
        }

        public String getUnionId() {
            return TextUtils.isEmpty(unionId) ? "0" : unionId;
        }

        public void setUnionId(String unionId) {
            this.unionId = unionId;
        }

        public String getMobileNum() {
            return mobileNum;
        }

        public void setMobileNum(String mobileNum) {
            this.mobileNum = mobileNum;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public boolean isMobile_verification() {
            return isMobile_verification;
        }

        public void setMobile_verification(boolean mobile_verification) {
            isMobile_verification = mobile_verification;
        }

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getOpenid() {
            return openid;
        }

        public void setOpenid(String openid) {
            this.openid = openid;
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

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public OtherAccountBindInfo() {
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.status);
            dest.writeString(this.mobile);
            dest.writeString(this.openid);
            dest.writeString(this.unionId);
            dest.writeString(this.type);
            dest.writeString(this.nickname);
            dest.writeString(this.avatar);
            dest.writeString(this.mobileNum);
            dest.writeInt(this.sex);
            dest.writeInt(this.uid);
            dest.writeByte(this.isMobile_verification ? (byte) 1 : (byte) 0);
        }

        protected OtherAccountBindInfo(Parcel in) {
            this.status = in.readInt();
            this.mobile = in.readString();
            this.openid = in.readString();
            this.unionId = in.readString();
            this.type = in.readString();
            this.nickname = in.readString();
            this.avatar = in.readString();
            this.mobileNum = in.readString();
            this.sex = in.readInt();
            this.uid = in.readInt();
            this.isMobile_verification = in.readByte() != 0;
        }

        public static final Creator<OtherAccountBindInfo> CREATOR = new Creator<OtherAccountBindInfo>() {
            @Override
            public OtherAccountBindInfo createFromParcel(Parcel source) {
                return new OtherAccountBindInfo(source);
            }

            @Override
            public OtherAccountBindInfo[] newArray(int size) {
                return new OtherAccountBindInfo[size];
            }
        };
    }
}
