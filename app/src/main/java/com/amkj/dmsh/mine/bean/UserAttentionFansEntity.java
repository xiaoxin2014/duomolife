package com.amkj.dmsh.mine.bean;


import android.os.Parcel;
import android.os.Parcelable;

import com.amkj.dmsh.base.BaseEntity;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserAttentionFansEntity extends BaseEntity{
    /**
     * result : [{"fnickname":"ZG9tb2xpZmU=","buid":116,"fuid":1,"bnickname":"5aSa5LmI55Sf5rS7"},{"fnickname":"ZG9tb2xpZmU=","buid":114,"fuid":1,"bnickname":"8J+Ym+WmruWPr/CfmIs="},{"fnickname":"ZG9tb2xpZmU=","buid":5347,"fuid":1,"bnickname":"bGFsYWxh"}]
     * code : 01
     * msg : 请求成功
     */
    /**
     * fnickname : ZG9tb2xpZmU=
     * buid : 116
     * fuid : 1
     * bnickname : 5aSa5LmI55Sf5rS7
     */

    @SerializedName("result")
    private List<UserAttentionFansBean> userAttentionFansList;

    public List<UserAttentionFansBean> getUserAttentionFansList() {
        return userAttentionFansList;
    }

    public void setUserAttentionFansList(List<UserAttentionFansBean> userAttentionFansList) {
        this.userAttentionFansList = userAttentionFansList;
    }

    public static class UserAttentionFansBean implements Parcelable , MultiItemEntity {
        private boolean flag;
        private String fnickname;
        private String favatar;
        private String bnickname;
        private String bavatar;
        private int buid;
        private int fuid;
        private int backCode;

        public int getBackCode() {
            return backCode;
        }

        public void setBackCode(int backCode) {
            this.backCode = backCode;
        }

        public boolean isFlag() {
            return flag;
        }

        public void setFlag(boolean flag) {
            this.flag = flag;
        }

        public String getFavatar() {
            return favatar;
        }

        public void setFavatar(String favatar) {
            this.favatar = favatar;
        }

        public String getFnickname() {
            return fnickname;
        }

        public void setFnickname(String fnickname) {
            this.fnickname = fnickname;
        }

        public int getBuid() {
            return buid;
        }

        public void setBuid(int buid) {
            this.buid = buid;
        }

        public int getFuid() {
            return fuid;
        }

        public void setFuid(int fuid) {
            this.fuid = fuid;
        }

        public String getBavatar() {
            return bavatar;
        }

        public void setBavatar(String avatar) {
            this.bavatar = avatar;
        }

        public String getBnickname() {
            return bnickname;
        }

        public void setBnickname(String bnickname) {
            this.bnickname = bnickname;
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.fnickname);
            dest.writeInt(this.buid);
            dest.writeInt(this.fuid);
            dest.writeString(this.bavatar);
            dest.writeString(this.bnickname);
        }

        public UserAttentionFansBean() {
        }

        protected UserAttentionFansBean(Parcel in) {
            this.fnickname = in.readString();
            this.buid = in.readInt();
            this.fuid = in.readInt();
            this.bavatar = in.readString();
            this.bnickname = in.readString();
        }

        public static final Creator<UserAttentionFansBean> CREATOR = new Creator<UserAttentionFansBean>() {
            @Override
            public UserAttentionFansBean createFromParcel(Parcel source) {
                return new UserAttentionFansBean(source);
            }

            @Override
            public UserAttentionFansBean[] newArray(int size) {
                return new UserAttentionFansBean[size];
            }
        };

        @Override
        public int getItemType() {
            return 0;
        }
    }

}
