package com.amkj.dmsh.bean;

import com.amkj.dmsh.base.BaseEntity;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by atd48 on 2016/9/5.
 */
public class UserSearchEntity extends BaseEntity{

    /**
     * result : [{"uid":22119,"flag":false,"nickname":"sweetyylalala","avatar":"http://tva3.sinaimg.cn/crop.55.0.917.917.50/69cbbc90jw8f2lo4gli8sj20yi0yiq69.jpg"},{"uid":21022,"flag":false,"nickname":"我爱宝宝lalala","avatar":"http://tva4.sinaimg.cn/crop.0.0.640.640.50/d2d311f9jw8ekp1a78dh0j20hs0hswgb.jpg"},{"uid":15167,"flag":false,"nickname":"ff6lala","avatar":"http://wx.qlogo.cn/mmopen/ibDlLrzLjoRxEnUvYHMvd7wZcn21upgUjdsiaJrbxh6lVQiblhFicyrYhrQN6TcI5DmplxcYsgLMErIpNvBWBCooJMAA29TFjd69/0"},{"uid":12256,"flag":false,"nickname":"lala","avatar":"http://wx.qlogo.cn/mmopen/2w6cr0jibJ6oeHuCdu14sDkStOicM1YZwMnU1TWOV3D2iaseNtEucctgJFWibGk4O4eAOrrBUUyV5GcxQbibTh0B2ZwWicc0nCSWrl/0"},{"uid":5347,"flag":false,"nickname":"lalala","avatar":"http://wx.qlogo.cn/mmopen/2w6cr0jibJ6qC9Tw6ibVoPXEjFRPXYFbbyYLYDyc75H2VJrVThiapsk2qYd2yxibjSibCwCnE2dd9IwjWDzBicFcZ9MU5JED3MFqicV/0"}]
     * code : 01
     * msg : 请求成功
     */
    /**
     * uid : 22119
     * flag : false
     * nickname : sweetyylalala
     * avatar : http://tva3.sinaimg.cn/crop.55.0.917.917.50/69cbbc90jw8f2lo4gli8sj20yi0yiq69.jpg
     */

    @SerializedName("result")
    private List<UserSearchBean> userSearchList;

    public List<UserSearchBean> getUserSearchList() {
        return userSearchList;
    }

    public void setUserSearchList(List<UserSearchBean> userSearchList) {
        this.userSearchList = userSearchList;
    }

    public static class UserSearchBean {
        private int uid;
        private boolean flag;
        private String nickname;
        private String avatar;
        private int backCode;

        public int getBackCode() {
            return backCode;
        }

        public void setBackCode(int backCode) {
            this.backCode = backCode;
        }

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public boolean isFlag() {
            return flag;
        }

        public void setFlag(boolean flag) {
            this.flag = flag;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }
    }
}
