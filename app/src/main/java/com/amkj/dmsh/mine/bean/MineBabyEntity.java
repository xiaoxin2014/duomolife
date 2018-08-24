package com.amkj.dmsh.mine.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by atd48 on 2016/9/21.
 */
public class MineBabyEntity {

    /**
     * result : [{"avatar":null,"signature":null,"nickname":null,"is_delete":0,"sex":0,"birthday":"2016-08-11","id":1,"uid":23293},{"avatar":null,"signature":null,"nickname":null,"is_delete":0,"sex":0,"birthday":"2016-08-11","id":4,"uid":23293},{"avatar":null,"signature":null,"nickname":null,"is_delete":0,"sex":0,"birthday":"2016-08-11","id":8,"uid":23293},{"avatar":null,"signature":null,"nickname":null,"is_delete":0,"sex":0,"birthday":"2016-08-11","id":9,"uid":23293}]
     * code : 01
     * msg : 请求成功
     */

    private String code;
    private String msg;
    /**
     * avatar : null
     * signature : null
     * nickname : null
     * is_delete : 0
     * sex : 0
     * birthday : 2016-08-11
     * id : 1
     * uid : 23293
     */

    @SerializedName("result")
    private List<BabyBean> babyList;

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

    public List<BabyBean> getBabyList() {
        return babyList;
    }

    public void setBabyList(List<BabyBean> babyList) {
        this.babyList = babyList;
    }

    public static class BabyBean implements Parcelable {
        private String avatar;
        private String signature;
        private String nickname;
        private int is_delete;
        private int sex;
        private String birthday;
        private int id;
        private int uid;
        private int baby_status;
        private String picStatus;
        private String statusName;

        public int getBaby_status() {
            return baby_status;
        }

        public void setBaby_status(int baby_status) {
            this.baby_status = baby_status;
        }

        public String getPicStatus() {
            return picStatus;
        }

        public void setPicStatus(String picStatus) {
            this.picStatus = picStatus;
        }

        public String getStatusName() {
            return statusName;
        }

        public void setStatusName(String statusName) {
            this.statusName = statusName;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getSignature() {
            return signature;
        }

        public void setSignature(String signature) {
            this.signature = signature;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public int getIs_delete() {
            return is_delete;
        }

        public void setIs_delete(int is_delete) {
            this.is_delete = is_delete;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public BabyBean() {
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.avatar);
            dest.writeString(this.signature);
            dest.writeString(this.nickname);
            dest.writeInt(this.is_delete);
            dest.writeInt(this.sex);
            dest.writeString(this.birthday);
            dest.writeInt(this.id);
            dest.writeInt(this.uid);
            dest.writeInt(this.baby_status);
            dest.writeString(this.picStatus);
            dest.writeString(this.statusName);
        }

        protected BabyBean(Parcel in) {
            this.avatar = in.readString();
            this.signature = in.readString();
            this.nickname = in.readString();
            this.is_delete = in.readInt();
            this.sex = in.readInt();
            this.birthday = in.readString();
            this.id = in.readInt();
            this.uid = in.readInt();
            this.baby_status = in.readInt();
            this.picStatus = in.readString();
            this.statusName = in.readString();
        }

        public static final Creator<BabyBean> CREATOR = new Creator<BabyBean>() {
            @Override
            public BabyBean createFromParcel(Parcel source) {
                return new BabyBean(source);
            }

            @Override
            public BabyBean[] newArray(int size) {
                return new BabyBean[size];
            }
        };
    }
}
