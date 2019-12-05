package com.amkj.dmsh.dominant.bean;

import com.amkj.dmsh.base.BaseTimeEntity;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.google.gson.annotations.SerializedName;
import com.amkj.dmsh.dominant.bean.QualityGroupShareEntity.QualityGroupShareBean.MemberListBean;
import java.util.List;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/6/8
 * class description:拼团列表
 */

public class GroupShopJoinEntity {

    /**
     * result : [{"gpInfoId":3,"gpCreateUserId":34225,"gpCreateTime":"2017-06-08 20:04:41","gpEndTime":"2017-06-16 19:44:18","nickname":"Mo々ʚෆ\u20dbɞ","gpRecordId":2,"avatar":""}]
     * currentTime : 2017-06-12 00:11:30
     * msg : 请求成功
     * code : 01
     */

    private String currentTime;
    private String msg;
    private String code;
    @SerializedName("result")
    private List<GroupShopJoinBean> groupShopJoinBeanList;

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<GroupShopJoinBean> getGroupShopJoinBeanList() {
        return groupShopJoinBeanList;
    }

    public void setGroupShopJoinBeanList(List<GroupShopJoinBean> groupShopJoinBeanList) {
        this.groupShopJoinBeanList = groupShopJoinBeanList;
    }

    public static class GroupShopJoinBean extends BaseTimeEntity implements MultiItemEntity {
        /**
         * gpInfoId : 3
         * gpCreateUserId : 34225
         * gpCreateTime : 2017-06-08 20:04:41
         * gpEndTime : 2017-06-16 19:44:18
         * nickname : Mo々ʚෆ⃛ɞ
         * gpRecordId : 2
         * avatar :
         */

        private int gpInfoId;
        private int gpCreateUserId;
        private String gpCreateTime;
        private String gpEndTime;
        private String nickname;
        private int gpRecordId;
        private String avatar;
        private int itemType;
        private int range;
        private int gp_type;
        private String gpCreateStatus;
//        记录序号
        private int numOrder;
        private long second;
        //拼团状态（还差*人成团/已成团/已结束）
        private String groupStatus;
        private List<MemberListBean> memberListBeans;

        public String getGroupStatus() {
            return groupStatus;
        }

        public void setGroupStatus(String groupStatus) {
            this.groupStatus = groupStatus;
        }

        public long getSecond() {
            return second;
        }

        public void setSecond(long second) {
            this.second = second;
        }

        public int getGp_type() {
            return gp_type;
        }

        public void setGp_type(int gp_type) {
            this.gp_type = gp_type;
        }

        public String getGpCreateStatus() {
            return gpCreateStatus;
        }

        public void setGpCreateStatus(String gpCreateStatus) {
            this.gpCreateStatus = gpCreateStatus;
        }

        public int getNumOrder() {
            return numOrder;
        }

        public void setNumOrder(int numOrder) {
            this.numOrder = numOrder;
        }

        public int getRange() {
            return range;
        }

        public void setRange(int range) {
            this.range = range;
        }

        public int getGpInfoId() {
            return gpInfoId;
        }

        public void setGpInfoId(int gpInfoId) {
            this.gpInfoId = gpInfoId;
        }

        public int getGpCreateUserId() {
            return gpCreateUserId;
        }

        public void setGpCreateUserId(int gpCreateUserId) {
            this.gpCreateUserId = gpCreateUserId;
        }

        public String getGpCreateTime() {
            return gpCreateTime;
        }

        public void setGpCreateTime(String gpCreateTime) {
            this.gpCreateTime = gpCreateTime;
        }

        public String getGpEndTime() {
            return gpEndTime;
        }

        public void setGpEndTime(String gpEndTime) {
            this.gpEndTime = gpEndTime;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public int getGpRecordId() {
            return gpRecordId;
        }

        public void setGpRecordId(int gpRecordId) {
            this.gpRecordId = gpRecordId;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        @Override
        public int getItemType() {
            return itemType;
        }

        public void setItemType(int itemType) {
            this.itemType = itemType;
        }

        public List<MemberListBean> getMemberListBeans() {
            return memberListBeans;
        }

        public void setMemberListBeans(List<MemberListBean> memberListBeans) {
            this.memberListBeans = memberListBeans;
        }
    }
}
