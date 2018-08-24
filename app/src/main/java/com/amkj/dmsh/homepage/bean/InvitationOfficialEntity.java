package com.amkj.dmsh.homepage.bean;

import com.amkj.dmsh.homepage.bean.InvitationDetailEntity.InvitationDetailBean.PictureBean;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.google.gson.annotations.SerializedName;

import java.util.List;
/**
 * Created by atd48 on 2016/9/6.
 */
public class InvitationOfficialEntity {

    /**
     * tags : [{"tag_name":"ee","tag_id":44},{"tag_name":"ff","tag_id":45},{"tag_name":"防嗮","tag_id":1},{"tag_name":"美白","tag_id":2},{"tag_name":"时尚","tag_id":3}]
     * uid : 23326
     * atList : [{"uid":23327,"nickname":"15070840242"},{"uid":23329,"nickname":"15070840244"}]
     * at_uids : 23327,23329
     * status: 2,
     * title: "利其尔水杯，满足少女心",
     * nickname : 15070840241
     * avatar : http://tva1.sinaimg.cn/crop.0.0.180.180.50/6e6f989cjw1e8qgp5bmzyj2050050aa8.jpg
     * articletype : 2
     * ctime : 4天0小时25分
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
    private InvitationArticleBean InvitationDetails;
    /**
     * result : {"tags":[{"tag_name":"ee","tag_id":44},{"tag_name":"ff","tag_id":45},{"tag_name":"防嗮","tag_id":1},{"tag_name":"美白","tag_id":2},{"tag_name":"时尚","tag_id":3}],"uid":23326,"atList":[{"uid":23327,"nickname":"15070840242"},{"uid":23329,"nickname":"15070840244"}],"at_uids":"23327,23329","nickname":"15070840241","avatar":"http://tva1.sinaimg.cn/crop.0.0.180.180.50/6e6f989cjw1e8qgp5bmzyj2050050aa8.jpg","articletype":2,"ctime":"4天0小时25分","picture":[{"object_id":4066,"path":"http://tva1.sinaimg.cn/crop.0.0.180.180.50/6e6f989cjw1e8qgp5bmzyj2050050aa8.jpg","type":1},{"object_id":4066,"path":"http://tva1.sinaimg.cn/crop.0.0.180.180.50/6e6f989cjw1e8qgp5bmzyj2050050aa8.jpg","type":1}],"id":4066,"flag":false,"isFavor":false,"description":"王宝强于8月14日凌晨1点提出对马蓉的离婚申请http://baidu.com/2015-11-28/565920e403df4.jpg","path":"http://tva1.sinaimg.cn/crop.0.0.180.180.50/6e6f989cjw1e8qgp5bmzyj2050050aa8.jpg","favor":0,"view":0,"comment":0}
     * code : 01
     * msg : 请求成功
     */

    private String code;
    private String msg;

    public InvitationArticleBean getInvitationDetails() {
        return InvitationDetails;
    }

    public void setInvitationDetails(InvitationArticleBean InvitationDetails) {
        this.InvitationDetails = InvitationDetails;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static class InvitationArticleBean {
        private int uid;
        private String nickname;
        private String avatar;
        private int articletype;
        private int isfront;
        private String ctime;
        private int id;
        private boolean flag;
        private boolean isFavor;
        private String path;
        private int favor;
        private int status;
        private int view;
        private int comment;
        private String title;
        /**
         * tag_name : ee
         * tag_id : 44
         */

        @SerializedName("tags")
        private List<TagsBean> tagsList;
        /**
         * uid : 23327
         * nickname : 15070840242
         */
        @SerializedName("description")
        private List<DescriptionBean> descriptionBeanList;

        public List<DescriptionBean> getDescriptionList() {
            return descriptionBeanList;
        }

        public void setDescription(List<DescriptionBean> descriptionBeanList) {
            this.descriptionBeanList = descriptionBeanList;
        }

        private List<AtListBean> atList;
        /**
         * object_id : 4066
         * path : http://tva1.sinaimg.cn/crop.0.0.180.180.50/6e6f989cjw1e8qgp5bmzyj2050050aa8.jpg
         * type : 1
         */

        @SerializedName("picture")
        private List<PictureBean> pictureList;

        public int getIsfront() {
            return isfront;
        }

        public void setIsfront(int isfront) {
            this.isfront = isfront;
        }

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public boolean isFavor() {
            return isFavor;
        }

        public void setFavor(boolean favor) {
            isFavor = favor;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
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

        public boolean isIsFavor() {
            return isFavor;
        }

        public void setIsFavor(boolean isFavor) {
            this.isFavor = isFavor;
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

        public List<TagsBean> getTagsList() {
            return tagsList;
        }

        public void setTagsList(List<TagsBean> tagsList) {
            this.tagsList = tagsList;
        }

        public List<AtListBean> getAtList() {
            return atList;
        }

        public void setAtList(List<AtListBean> atList) {
            this.atList = atList;
        }

        public List<PictureBean> getPictureList() {
            return pictureList;
        }

        public void setPictureList(List<PictureBean> pictureList) {
            this.pictureList = pictureList;
        }

        public static class TagsBean {
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

        public static class DescriptionBean implements MultiItemEntity {
            private Object content;
            private String type;
            private int itemType;

            public Object getContent() {
                return content;
            }

            public void setContent(Object content) {
                this.content = content;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public void setItemType(int itemType) {
                this.itemType = itemType;
            }

            @Override
            public int getItemType() {
                return itemType;
            }
        }

        public static class ContentDetailProduct {
            /**
             * "picUrl": "http://img.domolife.cn/platform/20170309/20170309103021763.jpg",
             * "marketPrice": "49.00",
             * "price": "0.01",
             * "name": "夕染彩绘全包硅胶防摔苹果手机壳  带支架",
             * "maxPrice": "0.01",
             * "id": 5965,
             * "itemTypeId": 1
             */
            private String picUrl;
            private String marketPrice;
            private String price;
            private String name;
            private String maxPrice;
            private int id;
            private int itemTypeId;

            public String getPicUrl() {
                return picUrl;
            }

            public void setPicUrl(String picUrl) {
                this.picUrl = picUrl;
            }

            public String getMarketPrice() {
                return marketPrice;
            }

            public void setMarketPrice(String marketPrice) {
                this.marketPrice = marketPrice;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getMaxPrice() {
                return maxPrice;
            }

            public void setMaxPrice(String maxPrice) {
                this.maxPrice = maxPrice;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getItemTypeId() {
                return itemTypeId;
            }

            public void setItemTypeId(int itemTypeId) {
                this.itemTypeId = itemTypeId;
            }
        }

        public static class ContentDetailCoupon {
            /**
             * "picUrl": "http://img.domolife.cn/platform/aGdAyaTPXe.jpg",
             * "amount": "5.00",
             * "newPirUrl": "http://img.domolife.cn/platform/20170309/20170309154301500.jpg",
             * "startFee": "0.01",
             * "count": 998,
             * "had": 0,
             * "startTime": "2017-02-22 16:27:31",
             * "id": 12,
             * "endTime": "2017-04-30 00:00:00",
             * "title": "5元补偿券",
             * "totalCount": 1000
             */
            private String picUrl;
            private String amount;
            private String newPirUrl;
            private String startFee;
            private int count;
            private int had;
            private String startTime;
            private int id;
            private String endTime;
            private String title;
            private int totalCount;

            public String getPicUrl() {
                return picUrl;
            }

            public void setPicUrl(String picUrl) {
                this.picUrl = picUrl;
            }

            public String getAmount() {
                return amount;
            }

            public void setAmount(String amount) {
                this.amount = amount;
            }

            public String getNewPirUrl() {
                return newPirUrl;
            }

            public void setNewPirUrl(String newPirUrl) {
                this.newPirUrl = newPirUrl;
            }

            public String getStartFee() {
                return startFee;
            }

            public void setStartFee(String startFee) {
                this.startFee = startFee;
            }

            public int getCount() {
                return count;
            }

            public void setCount(int count) {
                this.count = count;
            }

            public int getHad() {
                return had;
            }

            public void setHad(int had) {
                this.had = had;
            }

            public String getStartTime() {
                return startTime;
            }

            public void setStartTime(String startTime) {
                this.startTime = startTime;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getEndTime() {
                return endTime;
            }

            public void setEndTime(String endTime) {
                this.endTime = endTime;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public int getTotalCount() {
                return totalCount;
            }

            public void setTotalCount(int totalCount) {
                this.totalCount = totalCount;
            }
        }
    }
}
