package com.amkj.dmsh.find.bean;

import com.amkj.dmsh.homepage.bean.InvitationDetailEntity.InvitationDetailBean.PictureBean;
import com.amkj.dmsh.homepage.bean.InvitationDetailEntity.InvitationDetailBean.RelevanceProBean;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/9/11
 * class description:帖子详情数据
 */

public class InvitationImgDetailEntity {

    /**
     * result : {"isfront":1,"articletype":2,"flag":false,"isFavor":true,"isCollect":true,"description":"哈哈","avatar":"http://img.domolife.cn/201704191717321905157574.png","pictureList":[{"path":"http://image.domolife.cn/201709071753231022525802.jpg","type":1,"object_id":13182},{"path":"http://image.domolife.cn/201709071753235838434273.jpg","type":1,"object_id":13182},{"path":"http://image.domolife.cn/201709071753231538308090.jpg","type":1,"object_id":13182}],"tagsList":[{"tag_name":"自定义","tag_id":270},{"tag_name":"美白","tag_id":2}],"uid":27928,"path":"http://image.domolife.cn/201709071753231022525802.jpg","view":2,"favor":1,"nickname":"刘小沫","ctime":"2017-09-07 17:53:24","json":[{"productId":4282,"price":"0.01","pictureUrl":"http://img.domolife.cn/platform/20161129/20161129171145742.jpg","id":"146","title":"北鼎K206钻石电热水壶礼盒装"},{"productId":4298,"price":"788.00","pictureUrl":"http://img.domolife.cn/platform/20170225/20170225180427631.jpg","id":"177","title":"韩国Ecleve宝宝四季多功能有机棉婴儿背带腰凳/单凳"},{"productId":4308,"price":"59.00","pictureUrl":"http://img.domolife.cn/platform/826kTFssed.png","id":177,"title":"AutoBot二代车载手机支架"}],"comment":0,"id":13182,"collect":1,"atList":[],"status":1}
     * msg : 请求成功
     * code : 01
     */

    @SerializedName("result")
    private InvitationImgDetailBean invitationImgDetailBean;
    private String msg;
    private String code;

    public InvitationImgDetailBean getInvitationImgDetailBean() {
        return invitationImgDetailBean;
    }

    public void setInvitationImgDetailBean(InvitationImgDetailBean invitationImgDetailBean) {
        this.invitationImgDetailBean = invitationImgDetailBean;
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

    public static class InvitationImgDetailBean {
        /**
         * isfront : 1
         * articletype : 2
         * flag : false
         * isFavor : true
         * isCollect : true
         * description : 哈哈
         * avatar : http://img.domolife.cn/201704191717321905157574.png
         * pictureList : [{"path":"http://image.domolife.cn/201709071753231022525802.jpg","type":1,"object_id":13182},{"path":"http://image.domolife.cn/201709071753235838434273.jpg","type":1,"object_id":13182},{"path":"http://image.domolife.cn/201709071753231538308090.jpg","type":1,"object_id":13182}]
         * tagsList : [{"tag_name":"自定义","tag_id":270},{"tag_name":"美白","tag_id":2}]
         * uid : 27928
         * path : http://image.domolife.cn/201709071753231022525802.jpg
         * view : 2
         * favor : 1
         * nickname : 刘小沫
         * ctime : 2017-09-07 17:53:24
         * json : [{"productId":4282,"price":"0.01","pictureUrl":"http://img.domolife.cn/platform/20161129/20161129171145742.jpg","id":"146","title":"北鼎K206钻石电热水壶礼盒装"},{"productId":4298,"price":"788.00","pictureUrl":"http://img.domolife.cn/platform/20170225/20170225180427631.jpg","id":"177","title":"韩国Ecleve宝宝四季多功能有机棉婴儿背带腰凳/单凳"},{"productId":4308,"price":"59.00","pictureUrl":"http://img.domolife.cn/platform/826kTFssed.png","id":177,"title":"AutoBot二代车载手机支架"}]
         * comment : 0
         * id : 13182
         * collect : 1
         * atList : []
         * status : 1
         */

        private int isfront;
        private int articletype;
        private boolean flag;
        private boolean isFavor;
        private boolean isCollect;
        private String description;
        private String avatar;
        private int uid;
        private String path;
        private int view;
        private int favor;
        private String nickname;
        private String ctime;
        private int comment;
        private int id;
        private int collect;
        private int status;
        private String topic_title;
        private int topic_id;
        @SerializedName("picture")
        private List<PictureBean> pictureList;
        @SerializedName("tags")
        private List<TagsBean> tagsList;
        @SerializedName("json")
        private List<RelevanceProBean> relevanceProList;

        public String getTopic_title() {
            return topic_title;
        }

        public void setTopic_title(String topic_title) {
            this.topic_title = topic_title;
        }

        public int getTopic_id() {
            return topic_id;
        }

        public void setTopic_id(int topic_id) {
            this.topic_id = topic_id;
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

        public boolean isIsFavor() {
            return isFavor;
        }

        public void setIsFavor(boolean isFavor) {
            this.isFavor = isFavor;
        }

        public boolean isIsCollect() {
            return isCollect;
        }

        public void setIsCollect(boolean isCollect) {
            this.isCollect = isCollect;
        }

        public String getDescription() {
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

        public int getFavor() {
            return favor;
        }

        public void setFavor(int favor) {
            this.favor = favor;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
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

        public int getCollect() {
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

        public List<PictureBean> getPictureList() {
            return pictureList;
        }

        public void setPictureList(List<PictureBean> pictureList) {
            this.pictureList = pictureList;
        }

        public List<TagsBean> getTagsList() {
            return tagsList;
        }

        public void setTagsList(List<TagsBean> tagsList) {
            this.tagsList = tagsList;
        }

        public List<RelevanceProBean> getRelevanceProList() {
            return relevanceProList;
        }

        public void setRelevanceProList(List<RelevanceProBean> relevanceProList) {
            this.relevanceProList = relevanceProList;
        }

        public static class TagsBean {
            /**
             * tag_name : 自定义
             * tag_id : 270
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
    }
}
