package com.amkj.dmsh.mine.bean;

import com.amkj.dmsh.base.BaseEntity;

import java.util.List;

/**
 * Created by xiaoxin on 2020/8/14
 * Version:v4.7.0
 */
public class ZeroLotteryEntity extends BaseEntity {


    private List<ZeroLotteryBean> result;

    public List<ZeroLotteryBean> getResult() {
        return result;
    }

    public void setResult(List<ZeroLotteryBean> result) {
        this.result = result;
    }

    public static class ZeroLotteryBean {
        /**
         * userId : 53453
         * nickName : K、
         * avatar : http://image.domolife.cn/iosRelease/20171206093941.jpg
         */

        private String userId;
        private String nickName;
        private String avatar;

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
    }
}
