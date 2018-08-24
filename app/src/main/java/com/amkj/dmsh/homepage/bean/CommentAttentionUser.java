package com.amkj.dmsh.homepage.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by atd48 on 2016/10/10.
 */
public class CommentAttentionUser implements Parcelable {

    /**
     * result : [{"fnickname":"15070840242","buid":23326,"fuid":23327,"avatar":"http://tva1.sinaimg.cn/crop.0.0.180.180.50/6e6f989cjw1e8qgp5bmzyj2050050aa8.jpg","bnickname":"15070840241"},{"fnickname":"15070840242","buid":23317,"fuid":23327,"avatar":"http://img.domolife.cn/test/20161008170915.jpg","bnickname":"Lucci"}]
     * code : 01
     * msg : 请求成功
     */

    private String code;
    private String msg;
    /**
     * fnickname : 15070840242
     * buid : 23326
     * fuid : 23327
     * avatar : http://tva1.sinaimg.cn/crop.0.0.180.180.50/6e6f989cjw1e8qgp5bmzyj2050050aa8.jpg
     * bnickname : 15070840241
     */

    @SerializedName("result")
    private List<CommentAttentionBean> commentAttentionList;

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

    public List<CommentAttentionBean> getCommentAttentionList() {
        return commentAttentionList;
    }

    public void setCommentAttentionList(List<CommentAttentionBean> commentAttentionList) {
        this.commentAttentionList = commentAttentionList;
    }

    public static class CommentAttentionBean implements Parcelable {
        private String fnickname;
        private int buid;
        private int fuid;
        private String avatar;
        private String bnickname;
        //字母名字
        private String name_en;
        //是否是标题
        private String sortLetters;

        public String getName_en() {
            return name_en;
        }

        public void setName_en(String name_en) {
            this.name_en = name_en;
        }

        public String getSortLetters() {
            return sortLetters;
        }

        public void setSortLetters(String sortLetters) {
            this.sortLetters = sortLetters;
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

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
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
            dest.writeString(this.avatar);
            dest.writeString(this.bnickname);
        }

        public CommentAttentionBean() {
        }

        protected CommentAttentionBean(Parcel in) {
            this.fnickname = in.readString();
            this.buid = in.readInt();
            this.fuid = in.readInt();
            this.avatar = in.readString();
            this.bnickname = in.readString();
        }

        public static final Creator<CommentAttentionBean> CREATOR = new Creator<CommentAttentionBean>() {
            @Override
            public CommentAttentionBean createFromParcel(Parcel source) {
                return new CommentAttentionBean(source);
            }

            @Override
            public CommentAttentionBean[] newArray(int size) {
                return new CommentAttentionBean[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.code);
        dest.writeString(this.msg);
        dest.writeList(this.commentAttentionList);
    }

    public CommentAttentionUser() {
    }

    protected CommentAttentionUser(Parcel in) {
        this.code = in.readString();
        this.msg = in.readString();
        this.commentAttentionList = new ArrayList<CommentAttentionBean>();
        in.readList(this.commentAttentionList, CommentAttentionBean.class.getClassLoader());
    }

    public static final Creator<CommentAttentionUser> CREATOR = new Creator<CommentAttentionUser>() {
        @Override
        public CommentAttentionUser createFromParcel(Parcel source) {
            return new CommentAttentionUser(source);
        }

        @Override
        public CommentAttentionUser[] newArray(int size) {
            return new CommentAttentionUser[size];
        }
    };
}
