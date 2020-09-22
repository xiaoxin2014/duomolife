package com.amkj.dmsh.bean;

import com.amkj.dmsh.base.BaseEntity;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.homepage.bean.MarqueeTextEntity;
import com.amkj.dmsh.mine.bean.MineBabyEntity.BabyBean;
import com.amkj.dmsh.utils.gson.GsonUtils;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import static com.amkj.dmsh.constant.ConstantMethod.getStringChangeIntegers;

/**
 * Created by atd48 on 2016/9/26.
 */
public class CommunalUserInfoEntity extends BaseEntity {

    @SerializedName("result")
    private CommunalUserInfoBean communalUserInfoBean;

    public CommunalUserInfoBean getCommunalUserInfoBean() {
        return communalUserInfoBean;
    }

    public void setCommunalUserInfoBean(CommunalUserInfoBean communalUserInfoBean) {
        this.communalUserInfoBean = communalUserInfoBean;
    }

    public static class CommunalUserInfoBean {
        private String birthday;
        private int fllow;
        private String uid;
        private int sex;
        private String nickname;
        private int status;
        private int fans;
        private int score;
        private int remindtime;
        private boolean mobile_verification;
        private boolean flag;
        private String mobile;
        private String token;
        private String tokenExpireSeconds;
        private String expireTime;
        private String qq;
        private String avatar;
        private int documentcount;
        private String bgimg_url;
        private String idcard;
        private String showIdcard;
        private String real_name;
        private String interests;
        private int is_wechat;
        private String isResetPassword;
        private String isBindingWx;
        private int approve;
        private List<BabyBean> babys;
        private int couponTotal;
        private String app_version_no;
        private String device_model;
        private String device_sys_version;
        private String idcardImg1;
        private String idcardImg2;
        private int sysNotice;
        private NoticeInfoBean noticeInfo;
        private List<MarqueeTextEntity.MarqueeTextBean> noticeInfoList;

        //三方登录专属字段
        private String isNeedBinding;

        //会员标志
        private String isVip;
        //会员等级
        private String vipLevel;
        //是否显示开通会员入口
        private String isWhiteUser;

        public boolean isWhiteUser() {
            return "1".equals(isWhiteUser);
        }

        public void setIsWhiteUser(String isWhiteUser) {
            this.isWhiteUser = isWhiteUser;
        }

        public int getVipLevel() {
            return ConstantMethod.getStringChangeIntegers(vipLevel);
        }

        public void setVipLevel(String vipLevel) {
            this.vipLevel = vipLevel;
        }

        public boolean isVip() {
            return "1".equals(isVip);
        }

        public void setIsVip(String isVip) {
            this.isVip = isVip;
        }

        public String getShowIdcard() {
            return showIdcard;
        }

        public void setShowIdcard(String showIdcard) {
            this.showIdcard = showIdcard;
        }

        public String getIdcardImg1() {
            return idcardImg1;
        }

        public void setIdcardImg1(String idcardImg1) {
            this.idcardImg1 = idcardImg1;
        }

        public String getIdcardImg2() {
            return idcardImg2;
        }

        public void setIdcardImg2(String idcardImg2) {
            this.idcardImg2 = idcardImg2;
        }

        public List<MarqueeTextEntity.MarqueeTextBean> getNoticeInfoList() {
            return noticeInfoList;
        }

        public void setNoticeInfoList(List<MarqueeTextEntity.MarqueeTextBean> noticeInfoList) {
            this.noticeInfoList = noticeInfoList;
        }

        public boolean isBindingWx() {
            return "1".equals(isBindingWx);
        }

        public boolean isResetPassword() {
            return "1".equals(isResetPassword);
        }

        public void setIsResetPassword(String isResetPassword) {
            this.isResetPassword = isResetPassword;
        }

        public long getExpireTime() {
            return ConstantMethod.getStringChangeLong(expireTime) * 1000;
        }

        public boolean isWechat() {
            return is_wechat == 1;
        }

        public static CommunalUserInfoBean objectFromData(String str) {

            return GsonUtils.fromJson(str, CommunalUserInfoBean.class);
        }

        public long getTokenExpireSeconds() {
            return ConstantMethod.getStringChangeLong(tokenExpireSeconds) * 1000;
        }


        public int getApprove() {
            return approve;
        }

        public void setApprove(int approve) {
            this.approve = approve;
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
            return getStringChangeIntegers(uid);
        }

        public void setUid(String uid) {
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

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }

        public int getRemindtime() {
            return remindtime;
        }

        public void setRemindtime(int remindtime) {
            this.remindtime = remindtime;
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

        public int getCouponTotal() {
            return couponTotal;
        }

        public void setCouponTotal(int couponTotal) {
            this.couponTotal = couponTotal;
        }

        public CommunalUserInfoBean() {
        }

        public String getApp_version_no() {
            return app_version_no;
        }

        public void setApp_version_no(String app_version_no) {
            this.app_version_no = app_version_no;
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

        public String getIsNeedBinding() {
            return isNeedBinding;
        }

        public void setIsNeedBinding(String isNeedBinding) {
            this.isNeedBinding = isNeedBinding;
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

                return GsonUtils.fromJson(str, NoticeInfoBean.class);
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
    }
}
