package com.amkj.dmsh.bean;

import com.amkj.dmsh.base.BaseTimeEntity;
import com.amkj.dmsh.bean.UserSearchEntity.UserSearchBean;
import com.amkj.dmsh.find.bean.HotTopicEntity.HotTopicBean;
import com.amkj.dmsh.find.bean.PostEntity.PostBean;
import com.amkj.dmsh.homepage.bean.TopicSpecialEntity.TopicSpecialBean;
import com.amkj.dmsh.user.bean.UserLikedProductEntity.LikedProductBean;

import java.util.List;

/**
 * Created by xiaoxin on 2019/9/16
 * Version:v4.2.2
 * ClassDescription :全局搜索实体类
 */
public class AllSearchEntity extends BaseTimeEntity {

    /**
     * code : 01
     * msg : 请求成功
     * sysTime : 2019-09-16 16:22:16
     * showCount : 0
     * totalPage : 5
     * totalResult : 44
     * currentPage : 1
     * result : {"goodsCount":"","documentCount":"6","topicCount":"203","userCount":"24","categoryId":"71","noIds":"596,692,5025,5415,5425,5432,5438,6064,6065,6066,6067,8755,8756,8757,8758,9195,9196,10008,10410,11196,11197,11435,11436,11437,11438,11598,12325,12326,13618,14040,14319,14483,15168,15540,16102,16741,16746,16748,17445,17446,17540,17541,17654,17871","goodsList":[{"activityCode":"","activityName":"","activityTag":"","id":"14040","path":"http://image.domolife.cn/platform/eaPc8d354s1539136369596.jpg","price":"99","quantity":"1376","subTitle":"给孩子备好全年无忧的健康奶","title":"认养一头牛  纯牛奶250ml*12盒/箱 ","typeId":"1","waterRemark":"","marketLabelList":[{"id":"9","title":"爆品"}]},{"activityCode":"","activityName":"","activityTag":"","id":"13618","path":"http://image.domolife.cn/platform/20180523/20180523113635126.png","price":"78","quantity":"19","subTitle":"牛奶泡泡浴，让你爱上洗澡","title":"日本cow牛奶牛乳石碱沐浴露750ml家庭装","typeId":"1","waterRemark":"","marketLabelList":[]},{"activityCode":"","activityName":"","activityTag":"","id":"14483","path":"http://image.domolife.cn/platform/Gf6zCGF5wA1531450522357.jpg","price":"49","quantity":"0","subTitle":"精华留香，娇嫩芬芳美肌","title":"意大利Malizia玛莉吉亚沐浴露（牛奶香型）1000ml","typeId":"1","waterRemark":"","marketLabelList":[]},{"activityCode":"","activityName":"","activityTag":"","id":"17871","path":"http://image.domolife.cn/platform/wjKXNeeTnF1551431869861.jpg","price":"84","quantity":"947","subTitle":"好喝不长胖的低脂好奶","title":"兰特脱脂低脂牛奶1L*6支装","typeId":"1","waterRemark":"","marketLabelList":[]},{"activityCode":"","activityName":"","activityTag":"","id":"17654","path":"http://image.domolife.cn/platform/BTG7Q2Bc5J1550455903878.jpg","price":"32","quantity":"328","subTitle":"吐司就要吃现烤才够香","title":"HONlife招牌牛奶手工吐司550g","typeId":"1","waterRemark":"","marketLabelList":[]},{"activityCode":"","activityName":"","activityTag":"","id":"17541","path":"http://image.domolife.cn/platform/20190114/20190114172028479.jpg","price":"16","quantity":"100","subTitle":"","title":"日本RIBON牛奶糖","typeId":"0","waterRemark":"","marketLabelList":[]},{"activityCode":"","activityName":"","activityTag":"","id":"17540","path":"http://image.domolife.cn/platform/20190114/20190114171908992.jpg","price":"17","quantity":"100","subTitle":"","title":"日本甘乐牛奶糖","typeId":"0","waterRemark":"","marketLabelList":[]},{"activityCode":"","activityName":"","activityTag":"","id":"17446","path":"http://image.domolife.cn/platform/20190111/20190111100934653.jpg","price":"99","quantity":"19787","subTitle":"","title":"【0.1元抽奖团】认养一头牛 纯牛奶250ml*12盒/箱","typeId":"1","waterRemark":"","marketLabelList":[]},{"activityCode":"","activityName":"","activityTag":"","id":"17445","path":"http://image.domolife.cn/platform/20190111/20190111100431319.jpg","price":"0","quantity":"0","subTitle":"给孩子备好全年无忧的健康奶","title":"认养一头牛 纯牛奶250ml*12盒/箱","typeId":"1","waterRemark":"","marketLabelList":[]},{"activityCode":"","activityName":"","activityTag":"","id":"16748","path":"http://image.domolife.cn/platform/20181206/20181206141858714.jpg","price":"29","quantity":"100","subTitle":"","title":"萨洛缇黑白熊牛奶巧克力","typeId":"0","waterRemark":"","marketLabelList":[]}],"documentList":[],"topicList":[],"postList":[],"userList":[]}
     */

    private int showCount;
    private int totalPage;
    private int totalResult;
    private int currentPage;
    private AllSearchBean result;


    public int getShowCount() {
        return showCount;
    }

    public void setShowCount(int showCount) {
        this.showCount = showCount;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getTotalResult() {
        return totalResult;
    }

    public void setTotalResult(int totalResult) {
        this.totalResult = totalResult;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public AllSearchBean getSearchBean() {
        return result;
    }

    public void setSearchBean(AllSearchBean searchBean) {
        this.result = searchBean;
    }

    public static class AllSearchBean {
        /**
         * goodsCount :
         * documentCount : 6
         * topicCount : 203
         * userCount : 24
         * categoryId : 71
         * noIds : 596,692,5025,5415,5425,5432,5438,6064,6065,6066,6067,8755,8756,8757,8758,9195,9196,10008,10410,11196,11197,11435,11436,11437,11438,11598,12325,12326,13618,14040,14319,14483,15168,15540,16102,16741,16746,16748,17445,17446,17540,17541,17654,17871
         * goodsList : [{"activityCode":"","activityName":"","activityTag":"","id":"14040","path":"http://image.domolife.cn/platform/eaPc8d354s1539136369596.jpg","price":"99","quantity":"1376","subTitle":"给孩子备好全年无忧的健康奶","title":"认养一头牛  纯牛奶250ml*12盒/箱 ","typeId":"1","waterRemark":"","marketLabelList":[{"id":"9","title":"爆品"}]},{"activityCode":"","activityName":"","activityTag":"","id":"13618","path":"http://image.domolife.cn/platform/20180523/20180523113635126.png","price":"78","quantity":"19","subTitle":"牛奶泡泡浴，让你爱上洗澡","title":"日本cow牛奶牛乳石碱沐浴露750ml家庭装","typeId":"1","waterRemark":"","marketLabelList":[]},{"activityCode":"","activityName":"","activityTag":"","id":"14483","path":"http://image.domolife.cn/platform/Gf6zCGF5wA1531450522357.jpg","price":"49","quantity":"0","subTitle":"精华留香，娇嫩芬芳美肌","title":"意大利Malizia玛莉吉亚沐浴露（牛奶香型）1000ml","typeId":"1","waterRemark":"","marketLabelList":[]},{"activityCode":"","activityName":"","activityTag":"","id":"17871","path":"http://image.domolife.cn/platform/wjKXNeeTnF1551431869861.jpg","price":"84","quantity":"947","subTitle":"好喝不长胖的低脂好奶","title":"兰特脱脂低脂牛奶1L*6支装","typeId":"1","waterRemark":"","marketLabelList":[]},{"activityCode":"","activityName":"","activityTag":"","id":"17654","path":"http://image.domolife.cn/platform/BTG7Q2Bc5J1550455903878.jpg","price":"32","quantity":"328","subTitle":"吐司就要吃现烤才够香","title":"HONlife招牌牛奶手工吐司550g","typeId":"1","waterRemark":"","marketLabelList":[]},{"activityCode":"","activityName":"","activityTag":"","id":"17541","path":"http://image.domolife.cn/platform/20190114/20190114172028479.jpg","price":"16","quantity":"100","subTitle":"","title":"日本RIBON牛奶糖","typeId":"0","waterRemark":"","marketLabelList":[]},{"activityCode":"","activityName":"","activityTag":"","id":"17540","path":"http://image.domolife.cn/platform/20190114/20190114171908992.jpg","price":"17","quantity":"100","subTitle":"","title":"日本甘乐牛奶糖","typeId":"0","waterRemark":"","marketLabelList":[]},{"activityCode":"","activityName":"","activityTag":"","id":"17446","path":"http://image.domolife.cn/platform/20190111/20190111100934653.jpg","price":"99","quantity":"19787","subTitle":"","title":"【0.1元抽奖团】认养一头牛 纯牛奶250ml*12盒/箱","typeId":"1","waterRemark":"","marketLabelList":[]},{"activityCode":"","activityName":"","activityTag":"","id":"17445","path":"http://image.domolife.cn/platform/20190111/20190111100431319.jpg","price":"0","quantity":"0","subTitle":"给孩子备好全年无忧的健康奶","title":"认养一头牛 纯牛奶250ml*12盒/箱","typeId":"1","waterRemark":"","marketLabelList":[]},{"activityCode":"","activityName":"","activityTag":"","id":"16748","path":"http://image.domolife.cn/platform/20181206/20181206141858714.jpg","price":"29","quantity":"100","subTitle":"","title":"萨洛缇黑白熊牛奶巧克力","typeId":"0","waterRemark":"","marketLabelList":[]}]
         * documentList : []
         * topicList : []
         * postList : []
         * userList : []
         */

        private String goodsCount;
        private String documentCount;
        private String topicCount;
        private String userCount;
        private String categoryId;
        private String noIds;
        private List<LikedProductBean> goodsList;
        private List<TopicSpecialBean> documentList;
        private List<HotTopicBean> topicList;
        private List<PostBean> postList;
        private List<UserSearchBean> userList;
        private WatchwordBean watchword;

        public WatchwordBean getWatchword() {
            return watchword;
        }

        public void setWatchword(WatchwordBean watchword) {
            this.watchword = watchword;
        }

        public String getGoodsCount() {
            return goodsCount;
        }

        public void setGoodsCount(String goodsCount) {
            this.goodsCount = goodsCount;
        }

        public String getDocumentCount() {
            return documentCount;
        }

        public void setDocumentCount(String documentCount) {
            this.documentCount = documentCount;
        }

        public String getTopicCount() {
            return topicCount;
        }

        public void setTopicCount(String topicCount) {
            this.topicCount = topicCount;
        }

        public String getUserCount() {
            return userCount;
        }

        public void setUserCount(String userCount) {
            this.userCount = userCount;
        }

        public String getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(String categoryId) {
            this.categoryId = categoryId;
        }

        public String getNoIds() {
            return noIds;
        }

        public void setNoIds(String noIds) {
            this.noIds = noIds;
        }

        public List<LikedProductBean> getGoodsList() {
            return goodsList;
        }

        public void setGoodsList(List<LikedProductBean> goodsList) {
            this.goodsList = goodsList;
        }

        public List<TopicSpecialBean> getDocumentList() {
            return documentList;
        }

        public void setDocumentList(List<TopicSpecialBean> documentList) {
            this.documentList = documentList;
        }

        public List<HotTopicBean> getTopicList() {
            return topicList;
        }

        public void setTopicList(List<HotTopicBean> topicList) {
            this.topicList = topicList;
        }

        public List<PostBean> getPostList() {
            return postList;
        }

        public void setPostList(List<PostBean> postList) {
            this.postList = postList;
        }

        public List<UserSearchBean> getUserList() {
            return userList;
        }

        public void setUserList(List<UserSearchBean> userList) {
            this.userList = userList;
        }

        public static class WatchwordBean {
            /**
             * objId : 582
             * type : 1
             * imgUrl : http://image.domolife.cn/platform/2PZfJazcKh1569037576307.png
             * iosLink :
             * androidLink :
             * webLink :
             */

            private String objId;
            private String type;
            private String imgUrl;
            private String iosLink;
            private String androidLink;
            private String webLink;

            public String getObjId() {
                return objId;
            }

            public void setObjId(String objId) {
                this.objId = objId;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getImgUrl() {
                return imgUrl;
            }

            public void setImgUrl(String imgUrl) {
                this.imgUrl = imgUrl;
            }

            public String getIosLink() {
                return iosLink;
            }

            public void setIosLink(String iosLink) {
                this.iosLink = iosLink;
            }

            public String getAndroidLink() {
                return androidLink;
            }

            public void setAndroidLink(String androidLink) {
                this.androidLink = androidLink;
            }

            public String getWebLink() {
                return webLink;
            }

            public void setWebLink(String webLink) {
                this.webLink = webLink;
            }
        }
    }
}
