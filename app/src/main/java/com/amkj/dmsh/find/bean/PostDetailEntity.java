package com.amkj.dmsh.find.bean;

import com.amkj.dmsh.base.BaseEntity;

import java.util.List;

/**
 * Created by xiaoxin on 2019/7/11
 * Version:v4.1.0
 * ClassDescription :帖子详情接口数据实体
 */
public class PostDetailEntity extends BaseEntity {

    /**
     * result : {"isfront":1,"articletype":2,"flag":false,"isFavor":false,"isCollect":false,"description":"#蝗虫团购晒单#冲着颜值买的，拿到手后没有失望。上妆很服帖，也不是很浪费粉底液。","avatar":"http://image.domolife.cn/2016-05-15_5737df715c38e.JPG","picture":[{"path":"http://image.domolife.cn/iosRelease/2019070720061250967547.jpg","id":317662,"type":1,"object_id":21656},{"path":"http://image.domolife.cn/iosRelease/2019070720061219009599.jpg","id":317663,"type":1,"object_id":21656}],"tags":[{"tag_name":"造型","tag_id":165}],"uid":507,"path":"http://image.domolife.cn/iosRelease/2019070720061250967547.jpg","view":0,"favor":2,"topic_title":"蝗虫团购晒单","nickname":"朵拉","digest":"#蝗虫团购晒单#冲着颜值买的，拿到手后没有失望。上妆很服帖，也不是很浪费粉底液。","ctime":"2019-07-07 20:06:14","json":[{"productId":19408,"price":"42.00","pictureUrl":"http://image.domolife.cn/platform/SBket5TY5d1560242526857.jpg","id":14067,"title":"尔木萄 星空美妆蛋"}],"comment":0,"id":21656,"topic_id":6,"collect":0,"atList":[],"status":1}
     */

    private PostDetailBean result;

    public PostDetailBean getResult() {
        return result;
    }

    public void setResult(PostDetailBean result) {
        this.result = result;
    }

    public static class PostDetailBean {
        /**
         * isfront : 1
         * articletype : 2
         * flag : false
         * isFavor : false
         * isCollect : false
         * description : #蝗虫团购晒单#冲着颜值买的，拿到手后没有失望。上妆很服帖，也不是很浪费粉底液。
         * avatar : http://image.domolife.cn/2016-05-15_5737df715c38e.JPG
         * picture : [{"path":"http://image.domolife.cn/iosRelease/2019070720061250967547.jpg","id":317662,"type":1,"object_id":21656},{"path":"http://image.domolife.cn/iosRelease/2019070720061219009599.jpg","id":317663,"type":1,"object_id":21656}]
         * tags : [{"tag_name":"造型","tag_id":165}]
         * uid : 507
         * path : http://image.domolife.cn/iosRelease/2019070720061250967547.jpg
         * view : 0
         * favor : 2
         * topic_title : 蝗虫团购晒单
         * nickname : 朵拉
         * digest : #蝗虫团购晒单#冲着颜值买的，拿到手后没有失望。上妆很服帖，也不是很浪费粉底液。
         * ctime : 2019-07-07 20:06:14
         * json : [{"productId":19408,"price":"42.00","pictureUrl":"http://image.domolife.cn/platform/SBket5TY5d1560242526857.jpg","id":14067,"title":"尔木萄 星空美妆蛋"}]
         * comment : 0
         * id : 21656
         * topic_id : 6
         * collect : 0
         * atList : []
         * status : 1
         */

        private int isRewarded;
        private int isfront;
        private int articletype;
        private boolean flag;//是否关注
        private boolean isFavor;//是否点赞
        private boolean isCollect;//是否收藏
        private Object description;
        private String avatar;
        private int uid;
        private String path;
        private int view;//浏览量
        private int favor;//点赞量
        private String topic_title;
        private String nickname;
        private String digest;
        private String ctime;
        private int comment;
        private int id;
        private int topic_id;
        private int collect;//收藏量
        private int status;
        private List<PictureBean> picture;
        private List<TagsBean> tags;
        private List<RelatedGoodsBean> json;//关联商品
        private List<AtListBean> atList;

        public int getIsRewarded() {
            return isRewarded;
        }

        public void setIsRewarded(int isRewarded) {
            this.isRewarded = isRewarded;
        }

        public int getIsfront() {
            return isfront;
        }

        public void setIsfront(int isfront) {
            this.isfront = isfront;
        }

        public int getArticletype() {
            return articletype;
        }

        public void setArticletype(int articletype) {
            this.articletype = articletype;
        }

        public boolean isFlag() {
            return flag;
        }

        public void setFlag(boolean flag) {
            this.flag = flag;
        }

        public boolean isFavor() {
            return isFavor;
        }

        public void setIsFavor(boolean isFavor) {
            this.isFavor = isFavor;
        }

        public boolean isCollect() {
            return isCollect;
        }

        public void setIsCollect(boolean isCollect) {
            this.isCollect = isCollect;
        }

        public Object getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public int getView() {
            return view;
        }

        public void setView(int view) {
            this.view = view;
        }

        public int getFavorNum() {
            return favor;
        }

        public void setFavor(int favor) {
            this.favor = favor;
        }

        public String getTopic_title() {
            return topic_title;
        }

        public void setTopic_title(String topic_title) {
            this.topic_title = topic_title;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getDigest() {
            return digest;
        }

        public void setDigest(String digest) {
            this.digest = digest;
        }

        public String getCtime() {
            return ctime;
        }

        public void setCtime(String ctime) {
            this.ctime = ctime;
        }

        public int getComment() {
            return comment;
        }

        public void setComment(int comment) {
            this.comment = comment;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getTopic_id() {
            return topic_id;
        }

        public void setTopic_id(int topic_id) {
            this.topic_id = topic_id;
        }

        public int getCollectNum() {
            return collect;
        }

        public void setCollect(int collect) {
            this.collect = collect;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public List<PictureBean> getPicture() {
            return picture;
        }

        public void setPicture(List<PictureBean> picture) {
            this.picture = picture;
        }

        public List<TagsBean> getTags() {
            return tags;
        }

        public void setTags(List<TagsBean> tags) {
            this.tags = tags;
        }

        public List<RelatedGoodsBean> getGoods() {
            return json;
        }

        public void setGoods(List<RelatedGoodsBean> json) {
            this.json = json;
        }

        public List<AtListBean> getAtList() {
            return atList;
        }

        public void setAtList(List<AtListBean> atList) {
            this.atList = atList;
        }

        public static class PictureBean {
            /**
             * path : http://image.domolife.cn/iosRelease/2019070720061250967547.jpg
             * id : 317662
             * type : 1
             * object_id : 21656
             */

            private String path;
            private int id;
            private int type;
            private int object_id;

            public String getPath() {
                return path;
            }

            public void setPath(String path) {
                this.path = path;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public int getObject_id() {
                return object_id;
            }

            public void setObject_id(int object_id) {
                this.object_id = object_id;
            }
        }

        public static class TagsBean {
            /**
             * tag_name : 造型
             * tag_id : 165
             */

            private String tag_name;
            private int tag_id;

            public String getTag_name() {
                return tag_name;
            }

            public void setTag_name(String tag_name) {
                this.tag_name = tag_name;
            }

            public int getTag_id() {
                return tag_id;
            }

            public void setTag_id(int tag_id) {
                this.tag_id = tag_id;
            }
        }

        public static class AtListBean {
            private int uid;
            private String nickname;

            public int getUid() {
                return uid;
            }

            public void setUid(int uid) {
                this.uid = uid;
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }
        }
    }
}
