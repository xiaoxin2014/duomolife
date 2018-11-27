package com.amkj.dmsh.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.amkj.dmsh.base.BaseEntity;
import com.amkj.dmsh.mine.bean.MineBabyEntity.BabyBean;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by atd48 on 2016/9/26.
 */
public class CommunalUserInfoEntity extends BaseEntity implements Parcelable {

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
    private CommunalUserInfoBean communalUserInfoBean;
    /**
     * result : {"is_sign":0,"fllow":0,"uid":23287,"sex":0,"nickname":"虫子","last_login_time":0,"status":1,"fans":1,"device_type":"web","score":0,"reg_time":0,"remindtime":30,"reg_ip":"116.25.96.117","rtime":"2016-08-29 11:01:21","login":0}
     * code : 01
     * msg : 请求成功
     */

    public CommunalUserInfoBean getCommunalUserInfoBean() {
        return communalUserInfoBean;
    }

    public void setCommunalUserInfoBean(CommunalUserInfoBean communalUserInfoBean) {
        this.communalUserInfoBean = communalUserInfoBean;
    }

    public static class CommunalUserInfoBean implements Parcelable {
        private String birthday;
        private String serverTime;
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
        private boolean mobile_verification;
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
        private int firstAppLogin;
        private int documentcount;
        private String bgimg_url;
        private String idcard;
        private String real_name;
        private String interests;
        private List<BabyBean> babys;

        /**
         * cartTotal：购物车数量
         * couponTotal：优惠券数量
         * jfTotal：积分订单数量
         * @return
         */
        private int cartTotal;
        private int couponTotal;
        private int jfTotal;
        /**
         * app_version_no : 3.1.3
         * device_source : ios
         * device_model : iphone6sp
         * device_sys_version : 10.3.2
         * sysNotice 1 | 0
         */

        private String app_version_no;
        private String device_source;
        private String device_model;
        private String device_sys_version;
        private int sysNotice;
        /**
         * noticeInfo : {"group_id":"4","ios_link":"app://DMLGoodsProductsInfoViewController","web_pc_link":"sdf","android_link":"app://ShopScrollDetailsActivity","web_link":"http://www.domolife.cn/m/template/common/proprietary.html","content":"sd8asa"}
         */

        private NoticeInfoBean noticeInfo;

        public static CommunalUserInfoBean objectFromData(String str) {

            return new Gson().fromJson(str, CommunalUserInfoBean.class);
        }

        public int getSysNotice() {
            return sysNotice;
        }

        public void setSysNotice(int sysNotice) {
            this.sysNotice = sysNotice;
        }

        public String getInterests() {
            return interests;
        }

        public void setInterests(String interests) {
            this.interests = interests;
        }

        public String getReal_name() {
            return real_name;
        }

        public void setReal_name(String real_name) {
            this.real_name = real_name;
        }

        public String getIdcard() {
            return idcard;
        }

        public void setIdcard(String idcard) {
            this.idcard = idcard;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public String getServerTime() {
            return serverTime;
        }

        public void setServerTime(String serverTime) {
            this.serverTime = serverTime;
        }

        public int getFirstAppLogin() {
            return firstAppLogin;
        }

        public void setFirstAppLogin(int firstAppLogin) {
            this.firstAppLogin = firstAppLogin;
        }

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

        public boolean isMobile_verification() {
            return mobile_verification;
        }

        public void setMobile_verification(boolean mobile_verification) {
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

        public int getDocumentcount() {
            return documentcount;
        }

        public void setDocumentcount(int documentcount) {
            this.documentcount = documentcount;
        }

        public String getBgimg_url() {
            return bgimg_url;
        }

        public void setBgimg_url(String bgimg_url) {
            this.bgimg_url = bgimg_url;
        }

        public List<BabyBean> getBabys() {
            return babys;
        }

        public void setBabys(List<BabyBean> babys) {
            this.babys = babys;
        }

        public int getCartTotal() {
            return cartTotal;
        }

        public void setCartTotal(int cartTotal) {
            this.cartTotal = cartTotal;
        }

        public int getCouponTotal() {
            return couponTotal;
        }

        public void setCouponTotal(int couponTotal) {
            this.couponTotal = couponTotal;
        }

        public int getJfTotal() {
            return jfTotal;
        }

        public void setJfTotal(int jfTotal) {
            this.jfTotal = jfTotal;
        }

        public CommunalUserInfoBean() {
        }

        public String getApp_version_no() {
            return app_version_no;
        }

        public void setApp_version_no(String app_version_no) {
            this.app_version_no = app_version_no;
        }

        public String getDevice_source() {
            return device_source;
        }

        public void setDevice_source(String device_source) {
            this.device_source = device_source;
        }

        public String getDevice_model() {
            return device_model;
        }

        public void setDevice_model(String device_model) {
            this.device_model = device_model;
        }

        public String getDevice_sys_version() {
            return device_sys_version;
        }

        public void setDevice_sys_version(String device_sys_version) {
            this.device_sys_version = device_sys_version;
        }

        public NoticeInfoBean getNoticeInfo() {
            return noticeInfo;
        }

        public void setNoticeInfo(NoticeInfoBean noticeInfo) {
            this.noticeInfo = noticeInfo;
        }

        public static class NoticeInfoBean {
            /**
             * group_id : 4
             * ios_link : app://DMLGoodsProductsInfoViewController
             * web_pc_link : sdf
             * android_link : app://ShopScrollDetailsActivity
             * web_link : http://www.domolife.cn/m/template/common/proprietary.html
             * content : sd8asa
             */

            private String group_id;
            private String ios_link;
            private String web_pc_link;
            private String android_link;
            private String web_link;
            private String content;

            public static NoticeInfoBean objectFromData(String str) {

                return new Gson().fromJson(str, NoticeInfoBean.class);
            }

            public String getGroup_id() {
                return group_id;
            }

            public void setGroup_id(String group_id) {
                this.group_id = group_id;
            }

            public String getIos_link() {
                return ios_link;
            }

            public void setIos_link(String ios_link) {
                this.ios_link = ios_link;
            }

            public String getWeb_pc_link() {
                return web_pc_link;
            }

            public void setWeb_pc_link(String web_pc_link) {
                this.web_pc_link = web_pc_link;
            }

            public String getAndroid_link() {
                return android_link;
            }

            public void setAndroid_link(String android_link) {
                this.android_link = android_link;
            }

            public String getWeb_link() {
                return web_link;
            }

            public void setWeb_link(String web_link) {
                this.web_link = web_link;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.birthday);
            dest.writeString(this.serverTime);
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
            dest.writeByte(this.mobile_verification ? (byte) 1 : (byte) 0);
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
            dest.writeInt(this.firstAppLogin);
            dest.writeInt(this.documentcount);
            dest.writeString(this.bgimg_url);
            dest.writeString(this.idcard);
            dest.writeString(this.real_name);
            dest.writeString(this.interests);
            dest.writeTypedList(this.babys);
            dest.writeInt(this.cartTotal);
            dest.writeInt(this.couponTotal);
            dest.writeInt(this.jfTotal);
            dest.writeString(this.app_version_no);
            dest.writeString(this.device_source);
            dest.writeString(this.device_model);
            dest.writeString(this.device_sys_version);
            dest.writeInt(this.sysNotice);
        }

        protected CommunalUserInfoBean(Parcel in) {
            this.birthday = in.readString();
            this.serverTime = in.readString();
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
            this.mobile_verification = in.readByte() != 0;
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
            this.firstAppLogin = in.readInt();
            this.documentcount = in.readInt();
            this.bgimg_url = in.readString();
            this.idcard = in.readString();
            this.real_name = in.readString();
            this.interests = in.readString();
            this.babys = in.createTypedArrayList(BabyBean.CREATOR);
            this.cartTotal = in.readInt();
            this.couponTotal = in.readInt();
            this.jfTotal = in.readInt();
            this.app_version_no = in.readString();
            this.device_source = in.readString();
            this.device_model = in.readString();
            this.device_sys_version = in.readString();
            this.sysNotice = in.readInt();
        }

        public static final Creator<CommunalUserInfoBean> CREATOR = new Creator<CommunalUserInfoBean>() {
            @Override
            public CommunalUserInfoBean createFromParcel(Parcel source) {
                return new CommunalUserInfoBean(source);
            }

            @Override
            public CommunalUserInfoBean[] newArray(int size) {
                return new CommunalUserInfoBean[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.communalUserInfoBean, flags);
    }

    public CommunalUserInfoEntity() {
    }

    protected CommunalUserInfoEntity(Parcel in) {
        this.communalUserInfoBean = in.readParcelable(CommunalUserInfoBean.class.getClassLoader());
    }

    public static final Creator<CommunalUserInfoEntity> CREATOR = new Creator<CommunalUserInfoEntity>() {
        @Override
        public CommunalUserInfoEntity createFromParcel(Parcel source) {
            return new CommunalUserInfoEntity(source);
        }

        @Override
        public CommunalUserInfoEntity[] newArray(int size) {
            return new CommunalUserInfoEntity[size];
        }
    };
}
