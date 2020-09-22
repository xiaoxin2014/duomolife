package com.amkj.dmsh.mine.bean;

import com.amkj.dmsh.base.BaseEntity;

import java.util.List;

/**
 * Created by xiaoxin on 2020/8/31
 * Version:v4.7.0
 */
public class VipInviteEntity extends BaseEntity {

    /**
     * sysTime : 2020-08-31 15:09:17
     * showCount : 80
     * totalPage : 1
     * totalResult : 4
     * currentPage : 1
     * result : [{"userId":"267716","nickName":"1767949****","avatar":"https://image.domolife.cn/test/20200807152848b3282.jpg","createTime":"2020-08-22 15:44:49"},{"userId":"267720","nickName":"213","avatar":"https://thirdwx.qlogo.cn/mmopen/vi_32/SCA2YoAfIR3Q3c3icl0S8m7dyFoRSVftvtzhx9DEMzDhK0ibmrB8okEpcpLjCswWhADxXL9N09Fmal947ftjyR7g/132","createTime":"2020-08-26 09:59:11"},{"userId":"27947","nickName":"剑辉","avatar":"http://image.domolife.cn/iosRelease/20200714163934.jpg","createTime":"2020-08-26 18:40:04"},{"userId":"267699","nickName":"武功","avatar":"http://image.domolife.cn/platform/hjXPrGpMaJ1534473782022.jpg","createTime":"2020-08-28 17:26:54"}]
     */

    private List<VipInviteBean> result;

    public List<VipInviteBean> getResult() {
        return result;
    }

    public void setResult(List<VipInviteBean> result) {
        this.result = result;
    }

    public static class VipInviteBean {
        /**
         * userId : 267716
         * nickName : 1767949****
         * avatar : https://image.domolife.cn/test/20200807152848b3282.jpg
         * createTime : 2020-08-22 15:44:49
         */

        private String userId;
        private String nickName;
        private String avatar;
        private String createTime;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }
    }
}
