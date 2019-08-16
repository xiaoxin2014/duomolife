package com.amkj.dmsh.find.bean;

import com.amkj.dmsh.base.BaseEntity;

import java.util.List;

/**
 * Created by xiaoxin on 2019/7/20
 * Version:v4.1.0
 * ClassDescription :消息-新增粉丝列表实体类
 */
public class FansEntity extends BaseEntity {


    /**
     * sysTime : 2019-07-29 18:48:08
     * fansList : [{"uid":"265051","avatar":"http://image.domolife.cn/201907121715220612373702.png","nickname":"石志青🤖","createTime":null,"isFocus":"0"},{"uid":"266978","avatar":"http://image.domolife.cn/platform/hjXPrGpMaJ1534473782022.jpg","nickname":"石志青","createTime":null,"isFocus":"1"},{"uid":"267338","avatar":"http://thirdwx.qlogo.cn/mmopen/vi_32/q7qU6Nh1iavpJdwJAtT5ic9MwgDTiaX1QVJVXdHRamZZMFEFKV228SoPzo5B2oYibgKg95Gan7nNfDibziatibRMy4L7w/132","nickname":"鸿星","createTime":null,"isFocus":"1"},{"uid":"267422","avatar":"https://wx.qlogo.cn/mmopen/vi_32/vibN7zehgtB9tje5pJWczWdf77398V0OQbg5EYPSTK3FYxUwZoKrrHZwciab6BNOxYZVKVO77sUTgUrTnJZkn3gw/132","nickname":"风","createTime":null,"isFocus":"0"}]
     * recommendUserList : [{"uid":"1","avatar":"http://image.domolife.cn/Uploads/app_img/2016-03-31/56fcc8cd4a039.png","nickname":"domolife","createTime":null,"isFocus":"0"}]
     */

    private List<FansBean> fansList;
    private List<FansBean> recommendUserList;

    public List<FansBean> getFansList() {
        return fansList;
    }

    public void setFansList(List<FansBean> fansList) {
        this.fansList = fansList;
    }

    public List<FansBean> getRecommendUserList() {
        return recommendUserList;
    }

    public void setRecommendUserList(List<FansBean> recommendUserList) {
        this.recommendUserList = recommendUserList;
    }

    public static class FansBean extends BaseFavorBean  {
        /**
         * uid : 265051
         * avatar : http://image.domolife.cn/201907121715220612373702.png
         * nickname : 石志青🤖
         * createTime : null
         * isFocus : 0
         */

        private String uid;
        private String avatar;
        private String nickname;
        private String createTime;


        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }
    }
}
