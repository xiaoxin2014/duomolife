package com.amkj.dmsh.homepage.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.amkj.dmsh.base.BaseEntity;
import com.amkj.dmsh.find.bean.InvitationImgDetailEntity.InvitationImgDetailBean.TagsBean;
import com.amkj.dmsh.homepage.bean.InvitationOfficialEntity.InvitationArticleBean.DescriptionBean;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by atd48 on 2016/8/30.
 */
public class InvitationDetailEntity extends BaseEntity{

    /**
     * result : [{"tags":[{"tag_name":"ee","tag_id":44},{"tag_name":"ff","tag_id":45},{"tag_name":"防嗮","tag_id":1},{"tag_name":"美白","tag_id":2},{"tag_name":"时尚","tag_id":3}],"uid":23326,"nickname":"15070840241","avatar":"http://tva1.sinaimg.cn/crop.0.0.180.180.50/6e6f989cjw1e8qgp5bmzyj2050050aa8.jpg","articletype":2,"ctime":"3天9小时40分","picture":[{"object_id":4066,"path":"http://tva1.sinaimg.cn/crop.0.0.180.180.50/6e6f989cjw1e8qgp5bmzyj2050050aa8.jpg","type":1},{"object_id":4066,"path":"http://tva1.sinaimg.cn/crop.0.0.180.180.50/6e6f989cjw1e8qgp5bmzyj2050050aa8.jpg","type":1}],"id":4066,"flag":false,"isFavor":false,"description":"王宝强于8月14日凌晨1点提出对马蓉的离婚申请http://baidu.com/2015-11-28/565920e403df4.jpg","path":"http://tva1.sinaimg.cn/crop.0.0.180.180.50/6e6f989cjw1e8qgp5bmzyj2050050aa8.jpg","favor":0,"view":0,"comment":0},{"tags":[{"tag_name":"川菜","tag_id":42},{"tag_name":"粤菜","tag_id":43},{"tag_name":"防嗮","tag_id":1},{"tag_name":"美白","tag_id":2},{"tag_name":"时尚","tag_id":3}],"uid":23326,"nickname":"15070840241","avatar":"http://tva1.sinaimg.cn/crop.0.0.180.180.50/6e6f989cjw1e8qgp5bmzyj2050050aa8.jpg","articletype":4,"ctime":"3天9小时48分","picture":[{"object_id":4065,"path":"http://tva1.sinaimg.cn/crop.0.0.180.180.50/6e6f989cjw1e8qgp5bmzyj2050050aa8.jpg","type":1},{"object_id":4065,"path":"http://tva1.sinaimg.cn/crop.0.0.180.180.50/6e6f989cjw1e8qgp5bmzyj2050050aa8.jpg","type":1}],"id":4065,"flag":false,"isFavor":false,"description":"向你们推荐一款精品美食http://tva1.sinaimg.cn/crop.0.0.180.180.50/6e6f989cjw1e8qgp5bmzyj2050050aa8.jpg","path":"http://tva1.sinaimg.cn/crop.0.0.180.180.50/6e6f989cjw1e8qgp5bmzyj2050050aa8.jpg","favor":1,"view":0,"comment":0},{"tags":[{"tag_name":"爬山","tag_id":40},{"tag_name":"运动","tag_id":41},{"tag_name":"防嗮","tag_id":1},{"tag_name":"美白","tag_id":2},{"tag_name":"时尚","tag_id":3}],"uid":23326,"nickname":"15070840241","avatar":"http://tva1.sinaimg.cn/crop.0.0.180.180.50/6e6f989cjw1e8qgp5bmzyj2050050aa8.jpg","articletype":1,"ctime":"3天10小时3分","picture":[],"id":4064,"title":"个人游玩","flag":false,"isFavor":false,"description":"去黄山游玩http://tva1.sinaimg.cn/crop.0.0.180.180.50/6e6f989cjw1e8qgp5bmzyj2050050aa8.jpg","path":"http://tva1.sinaimg.cn/crop.0.0.180.180.50/6e6f989cjw1e8qgp5bmzyj2050050aa8.jpg","favor":1,"view":0,"comment":0}]
     * code : 01
     * msg : 请求成功
     */
    /**
     * tags : [{"tag_name":"ee","tag_id":44},{"tag_name":"ff","tag_id":45},{"tag_name":"防嗮","tag_id":1},{"tag_name":"美白","tag_id":2},{"tag_name":"时尚","tag_id":3}]
     * uid : 23326
     * nickname : 15070840241
     * avatar : http://tva1.sinaimg.cn/crop.0.0.180.180.50/6e6f989cjw1e8qgp5bmzyj2050050aa8.jpg
     * articletype : 2
     * ctime : 3天9小时40分
     * picture : [{"object_id":4066,"path":"http://tva1.sinaimg.cn/crop.0.0.180.180.50/6e6f989cjw1e8qgp5bmzyj2050050aa8.jpg","type":1},{"object_id":4066,"path":"http://tva1.sinaimg.cn/crop.0.0.180.180.50/6e6f989cjw1e8qgp5bmzyj2050050aa8.jpg","type":1}]
     * id : 4066
     * flag : false
     * isFavor : false
     * description : 王宝强于8月14日凌晨1点提出对马蓉的离婚申请http://baidu.com/2015-11-28/565920e403df4.jpg
     * path : http://tva1.sinaimg.cn/crop.0.0.180.180.50/6e6f989cjw1e8qgp5bmzyj2050050aa8.jpg
     * favor : 0
     * view : 0
     * comment : 0
     */

    @SerializedName("result")
    private List<InvitationDetailBean> invitationSearchList;

    public List<InvitationDetailBean> getInvitationSearchList() {
        return invitationSearchList;
    }

    public void setInvitationSearchList(List<InvitationDetailBean> invitationSearchList) {
        this.invitationSearchList = invitationSearchList;
    }

    public static class InvitationDetailBean implements Parcelable {
        private int uid;
        private String nickname;
        private String avatar;
        private int articletype;
        private String ctime;
        private int id;
        private boolean flag;
        private boolean isFavor;
        private boolean isCollect;
        private String description;
        private String path;
        private int favor;
        private int view;
        private int comment;
        private String at_uids;
        private int backCode;
        private int status;
        private String title;
        private int collect;
        @SerializedName("isFlag")
        private String labelTag;
        @SerializedName("isRecommend")
        private String recommendType;
        private String topic_title;
        private String digest;
        private int topic_id;
        private List<AtListBean> atList;
        /**
         * tag_name : ee
         * tag_id : 44
         */

        private List<TagsBean> tags;
        /**
         * object_id : 4066
         * path : http://tva1.sinaimg.cn/crop.0.0.180.180.50/6e6f989cjw1e8qgp5bmzyj2050050aa8.jpg
         * type : 1
         */

        @SerializedName("picture")
        private List<PictureBean> pictureList;
        //        商品列表
        @SerializedName("json")
        private List<RelevanceProBean> relevanceProList;
        @SerializedName("description2")
        private List<DescriptionBean> descriptionList;


        public String getRecommendType() {
            return recommendType;
        }

        public void setRecommendType(String recommendType) {
            this.recommendType = recommendType;
        }

        public String getDigest() {
            return digest;
        }

        public void setDigest(String digest) {
            this.digest = digest;
        }

        public List<DescriptionBean> getDescriptionList() {
            return descriptionList;
        }

        public void setDescriptionList(List<DescriptionBean> descriptionList) {
            this.descriptionList = descriptionList;
        }

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

        public List<RelevanceProBean> getRelevanceProList() {
            return relevanceProList;
        }

        public void setRelevanceProList(List<RelevanceProBean> relevanceProList) {
            this.relevanceProList = relevanceProList;
        }

        public boolean isCollect() {
            return isCollect;
        }

        public void setCollect(boolean collect) {
            isCollect = collect;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public void setFavor(boolean favor) {
            isFavor = favor;
        }

        public String getAt_uids() {
            return at_uids;
        }

        public void setAt_uids(String at_uids) {
            this.at_uids = at_uids;
        }

        public List<AtListBean> getAtList() {
            return atList;
        }

        public void setAtList(List<AtListBean> atList) {
            this.atList = atList;
        }

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

        public int getBackCode() {
            return backCode;
        }

        public void setBackCode(int backCode) {
            this.backCode = backCode;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public int getArticletype() {
            return articletype;
        }

        public void setArticletype(int articletype) {
            this.articletype = articletype;
        }

        public String getCtime() {
            return ctime;
        }

        public void setCtime(String ctime) {
            this.ctime = ctime;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public boolean isFlag() {
            return flag;
        }

        public void setFlag(boolean flag) {
            this.flag = flag;
        }

        public String getLabelTag() {
            return labelTag;
        }

        public void setLabelTag(String labelTag) {
            this.labelTag = labelTag;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public int getFavor() {
            return favor;
        }

        public void setFavor(int favor) {
            this.favor = favor;
        }

        public int getView() {
            return view;
        }

        public void setView(int view) {
            this.view = view;
        }

        public int getComment() {
            return comment;
        }

        public void setComment(int comment) {
            this.comment = comment;
        }

        public boolean isFavor() {
            return isFavor;
        }

        public int getCollect() {
            return collect;
        }

        public void setCollect(int collect) {
            this.collect = collect;
        }

        public List<TagsBean> getTags() {
            return tags;
        }

        public void setTags(List<TagsBean> tags) {
            this.tags = tags;
        }

        public List<PictureBean> getPictureList() {
            return pictureList;
        }

        public void setPictureList(List<PictureBean> pictureList) {
            this.pictureList = pictureList;
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

        public static class PictureBean implements MultiItemEntity{
            private int object_id;
            private String path;
            private int type;
            private List<String> originalList;
            private int index;
            private int itemType;

            public int getObject_id() {
                return object_id;
            }

            public void setObject_id(int object_id) {
                this.object_id = object_id;
            }

            public String getPath() {
                return path;
            }

            public void setPath(String path) {
                this.path = path;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public List<String> getOriginalList() {
                return originalList;
            }

            public void setOriginalList(List<String> originalList) {
                this.originalList = originalList;
            }

            public int getIndex() {
                return index;
            }

            public void setIndex(int mIndex) {
                this.index = mIndex;
            }

            @Override
            public int getItemType() {
                return itemType;
            }

            public void setItemType(int itemType) {
                this.itemType = itemType;
            }
        }

        /**
         * "productId": 6052,
         * "price": 35,
         * "pictureUrl": "http://img.domolife.cn/platform/20170313/20170313164644929.jpg",
         * "id": "802",
         * "title": "Frosch柠檬浓缩洗洁精750ml"
         **/
        public static class RelevanceProBean implements MultiItemEntity {
            private int productId;
            private String price;
            private String pictureUrl;
            private int id;
            private String title;
            private int itemType;
            private Object saveObject;

            public int getProductId() {
                return productId;
            }

            public void setProductId(int productId) {
                this.productId = productId;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public String getPictureUrl() {
                return pictureUrl;
            }

            public void setPictureUrl(String pictureUrl) {
                this.pictureUrl = pictureUrl;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            @Override
            public int getItemType() {
                return itemType;
            }

            public void setItemType(int itemType) {
                this.itemType = itemType;
            }

            public Object getSaveObject() {
                return saveObject;
            }

            public void setSaveObject(Object saveObject) {
                this.saveObject = saveObject;
            }
        }

        public InvitationDetailBean() {
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.uid);
            dest.writeString(this.nickname);
            dest.writeString(this.avatar);
            dest.writeInt(this.articletype);
            dest.writeString(this.ctime);
            dest.writeInt(this.id);
            dest.writeByte(this.flag ? (byte) 1 : (byte) 0);
            dest.writeByte(this.isFavor ? (byte) 1 : (byte) 0);
            dest.writeByte(this.isCollect ? (byte) 1 : (byte) 0);
            dest.writeString(this.description);
            dest.writeString(this.path);
            dest.writeInt(this.favor);
            dest.writeInt(this.view);
            dest.writeInt(this.comment);
            dest.writeString(this.at_uids);
            dest.writeInt(this.backCode);
            dest.writeInt(this.status);
            dest.writeString(this.title);
            dest.writeInt(this.collect);
            dest.writeString(this.labelTag);
            dest.writeString(this.recommendType);
            dest.writeString(this.topic_title);
            dest.writeInt(this.topic_id);
            dest.writeList(this.atList);
            dest.writeList(this.tags);
            dest.writeList(this.pictureList);
            dest.writeList(this.relevanceProList);
        }

        protected InvitationDetailBean(Parcel in) {
            this.uid = in.readInt();
            this.nickname = in.readString();
            this.avatar = in.readString();
            this.articletype = in.readInt();
            this.ctime = in.readString();
            this.id = in.readInt();
            this.flag = in.readByte() != 0;
            this.isFavor = in.readByte() != 0;
            this.isCollect = in.readByte() != 0;
            this.description = in.readString();
            this.path = in.readString();
            this.favor = in.readInt();
            this.view = in.readInt();
            this.comment = in.readInt();
            this.at_uids = in.readString();
            this.backCode = in.readInt();
            this.status = in.readInt();
            this.title = in.readString();
            this.collect = in.readInt();
            this.labelTag = in.readString();
            this.recommendType = in.readString();
            this.topic_title = in.readString();
            this.topic_id = in.readInt();
            this.atList = new ArrayList<AtListBean>();
            in.readList(this.atList, AtListBean.class.getClassLoader());
            this.tags = new ArrayList<TagsBean>();
            in.readList(this.tags, TagsBean.class.getClassLoader());
            this.pictureList = new ArrayList<PictureBean>();
            in.readList(this.pictureList, PictureBean.class.getClassLoader());
            this.relevanceProList = new ArrayList<RelevanceProBean>();
            in.readList(this.relevanceProList, RelevanceProBean.class.getClassLoader());
        }

        public static final Creator<InvitationDetailBean> CREATOR = new Creator<InvitationDetailBean>() {
            @Override
            public InvitationDetailBean createFromParcel(Parcel source) {
                return new InvitationDetailBean(source);
            }

            @Override
            public InvitationDetailBean[] newArray(int size) {
                return new InvitationDetailBean[size];
            }
        };
    }
}
