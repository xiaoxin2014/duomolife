package com.amkj.dmsh.dominant.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/7/21
 * class description:请输入类描述
 */

public class DmlSearchCommentEntity {

    /**
     * result : [{"like_num":0,"obj_id":9769,"isFavor":false,"avatar":"http://img.domolife.cn/test/20161222003606.jpg","content":"从新再来酸爽","is_reply":0,"uid":2565,"is_at":1,"to_uid":1,"nickname":"1513712****","ctime":"03-04","id":3397,"replyComments":[],"atList":[]},{"like_num":1,"obj_id":9769,"isFavor":false,"avatar":"http://q.qlogo.cn/qqapp/1105138467/10A168AD00FC6BF32AD94988B337C848/100","replyCommList":[{"like_num":1,"obj_id":9769,"isFavor":false,"avatar":"http://img.domolife.cn/test/20161223092818.jpg","content":"在我的里面，点击私人助手联系","is_reply":1,"reply_uid":29436,"uid":113,"is_at":0,"to_uid":0,"nickname":"多么生活","ctime":"02-21","isReplyMain":true,"id":3138,"nickname1":"1376326****","atList":[]},{"like_num":0,"obj_id":9769,"isFavor":false,"avatar":"http://img.domolife.cn/test/20161223092818.jpg","content":"可以联系一下app的私人秘书给你处理下哈","is_reply":1,"reply_uid":29436,"uid":113,"is_at":0,"to_uid":0,"nickname":"多么生活","ctime":"02-23","isReplyMain":true,"id":3153,"nickname1":"1376326****","atList":[]},{"like_num":0,"obj_id":9769,"isFavor":false,"avatar":"http://img.domolife.cn/test/20161223092818.jpg","content":"好的，不好意思","is_reply":1,"reply_uid":29436,"uid":113,"is_at":0,"to_uid":0,"nickname":"多么生活","ctime":"02-24","isReplyMain":true,"id":3165,"nickname1":"1376326****","atList":[]},{"like_num":0,"obj_id":9769,"isFavor":false,"avatar":"http://q.qlogo.cn/qqapp/1105138467/605725B499344A11B43786FE9156EC32/100","content":"直接联系app的私人秘书给你处理哈","is_reply":1,"reply_uid":29436,"uid":24599,"is_at":1,"to_uid":1,"nickname":"｀繼續___:。","ctime":"02-25","isReplyMain":true,"id":3174,"nickname1":"1376326****","atList":[]}],"content":"想问一下买了东西想换货找哪位，在哪找","is_reply":0,"uid":29436,"is_at":1,"to_uid":1,"nickname":"1376326****","ctime":"02-18","id":3125,"atList":[]},{"like_num":0,"obj_id":9769,"isFavor":false,"avatar":"http://tva3.sinaimg.cn/crop.0.0.180.180.180/621c64d0jw1e8qgp5bmzyj2050050aa8.jpg","replyCommList":[{"like_num":0,"obj_id":9769,"isFavor":false,"avatar":"http://img.domolife.cn/test/20161223092818.jpg","content":"ok的","is_reply":1,"reply_uid":28694,"uid":113,"is_at":0,"to_uid":0,"nickname":"多么生活","ctime":"02-16","isReplyMain":true,"id":3118,"nickname1":"若瑶","atList":[]}],"content":"惠人榨汁机行不行？？","is_reply":0,"uid":28694,"is_at":1,"to_uid":1,"nickname":"若瑶","ctime":"02-16","id":3117,"atList":[]},{"like_num":0,"obj_id":9769,"isFavor":false,"replyCommList":[{"like_num":0,"obj_id":9769,"isFavor":false,"avatar":"http://img.domolife.cn/test/20161223092818.jpg","content":"是的哦用可生食的蔬菜即可","is_reply":1,"reply_uid":11714,"uid":113,"is_at":0,"to_uid":0,"nickname":"多么生活","ctime":"02-14","isReplyMain":true,"id":3109,"nickname1":"猫鱼","atList":[]}],"content":"请问这些蔬菜全部都不用煮直接打吗？","is_reply":0,"uid":11714,"is_at":1,"to_uid":1,"nickname":"猫鱼","ctime":"02-14","id":3105,"atList":[]},{"like_num":0,"obj_id":9769,"isFavor":false,"avatar":"http://wx.qlogo.cn/mmopen/EVPwb8RUoO5oOrH8ia5sW4PvkCgBEsNe6AnFdmkVs9McsqniaWDFSBxwFRCwoiaXdkAUAGlz6icwKuQk1OSlQnfyxwjJV0spaiaiaK/0","content":"过年期间也没猛吃，坚持每天40分钟keep，体重没长，体脂也降了。","is_reply":0,"uid":7411,"is_at":1,"to_uid":1,"nickname":"Sunshine","ctime":"02-12","id":3097,"replyComments":[],"atList":[]},{"like_num":0,"obj_id":9769,"isFavor":false,"avatar":"http://wx.qlogo.cn/mmopen/2w6cr0jibJ6rHgEib0lRic6R5squiafCz4y9TR9oepDogxKEYibPk5OWIzeGbrBbAMIfZKYenBGW3lxibMKYR0icZACchwicaWomKeYw/0","content":"身体指标怎么看？","is_reply":0,"uid":28663,"is_at":1,"to_uid":1,"nickname":"1891318****","ctime":"02-12","id":3095,"replyComments":[],"atList":[]},{"like_num":0,"obj_id":9769,"isFavor":false,"avatar":"http://img.domolife.cn/test/20161222003606.jpg","content":"过个年简直了、吃的多、不运动。体重直接回到解放前。","is_reply":0,"uid":2565,"is_at":1,"to_uid":1,"nickname":"1513712****","ctime":"02-06","id":3086,"replyComments":[],"atList":[]}]
     * msg : 请求成功
     * code : 01
     */

    private String msg;
    private String code;
    private int commentSize;
    @SerializedName("result")
    private List<DmlSearchCommentBean> dmlSearchCommentList;

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

    public int getCommentSize() {
        return commentSize;
    }

    public void setCommentSize(int commentSize) {
        this.commentSize = commentSize;
    }

    public List<DmlSearchCommentBean> getDmlSearchCommentList() {
        return dmlSearchCommentList;
    }

    public void setDmlSearchCommentList(List<DmlSearchCommentBean> dmlSearchCommentList) {
        this.dmlSearchCommentList = dmlSearchCommentList;
    }

    public static class DmlSearchCommentBean {
        /**
         * like_num : 0
         * obj_id : 9769
         * isFavor : false
         * avatar : http://img.domolife.cn/test/20161222003606.jpg
         * content : 从新再来酸爽
         * is_reply : 0
         * uid : 2565
         * is_at : 1
         * to_uid : 1
         * nickname : 1513712****
         * ctime : 03-04
         * id : 3397
         * replyComments : []
         * atList : []
         * replyCommList : [{"like_num":1,"obj_id":9769,"isFavor":false,"avatar":"http://img.domolife.cn/test/20161223092818.jpg","content":"在我的里面，点击私人助手联系","is_reply":1,"reply_uid":29436,"uid":113,"is_at":0,"to_uid":0,"nickname":"多么生活","ctime":"02-21","isReplyMain":true,"id":3138,"nickname1":"1376326****","atList":[]},{"like_num":0,"obj_id":9769,"isFavor":false,"avatar":"http://img.domolife.cn/test/20161223092818.jpg","content":"可以联系一下app的私人秘书给你处理下哈","is_reply":1,"reply_uid":29436,"uid":113,"is_at":0,"to_uid":0,"nickname":"多么生活","ctime":"02-23","isReplyMain":true,"id":3153,"nickname1":"1376326****","atList":[]},{"like_num":0,"obj_id":9769,"isFavor":false,"avatar":"http://img.domolife.cn/test/20161223092818.jpg","content":"好的，不好意思","is_reply":1,"reply_uid":29436,"uid":113,"is_at":0,"to_uid":0,"nickname":"多么生活","ctime":"02-24","isReplyMain":true,"id":3165,"nickname1":"1376326****","atList":[]},{"like_num":0,"obj_id":9769,"isFavor":false,"avatar":"http://q.qlogo.cn/qqapp/1105138467/605725B499344A11B43786FE9156EC32/100","content":"直接联系app的私人秘书给你处理哈","is_reply":1,"reply_uid":29436,"uid":24599,"is_at":1,"to_uid":1,"nickname":"｀繼續___:。","ctime":"02-25","isReplyMain":true,"id":3174,"nickname1":"1376326****","atList":[]}]
         */

        private int like_num;
        private int obj_id;
        private boolean isFavor;
        private String avatar;
        private String content;
        private int is_reply;
        private int uid;
        private int is_at;
        private int to_uid;
        private String nickname;
        private String ctime;
        private int id;
        private int mainContentId;
        private List<ReplyCommListBean> replyCommList;

        public int getLike_num() {
            return like_num;
        }

        public void setLike_num(int like_num) {
            this.like_num = like_num;
        }

        public int getObj_id() {
            return obj_id;
        }

        public void setObj_id(int obj_id) {
            this.obj_id = obj_id;
        }

        public boolean isFavor() {
            return isFavor;
        }

        public void setFavor(boolean favor) {
            isFavor = favor;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getIs_reply() {
            return is_reply;
        }

        public void setIs_reply(int is_reply) {
            this.is_reply = is_reply;
        }

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public int getIs_at() {
            return is_at;
        }

        public void setIs_at(int is_at) {
            this.is_at = is_at;
        }

        public int getTo_uid() {
            return to_uid;
        }

        public void setTo_uid(int to_uid) {
            this.to_uid = to_uid;
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

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getMainContentId() {
            return mainContentId;
        }

        public void setMainContentId(int mainContentId) {
            this.mainContentId = mainContentId;
        }

        public List<ReplyCommListBean> getReplyCommList() {
            return replyCommList;
        }

        public void setReplyCommList(List<ReplyCommListBean> replyCommList) {
            this.replyCommList = replyCommList;
        }


        public static class ReplyCommListBean implements MultiItemEntity, Parcelable {
            /**
             * like_num : 1
             * obj_id : 9769
             * isFavor : false
             * avatar : http://img.domolife.cn/test/20161223092818.jpg
             * content : 在我的里面，点击私人助手联系
             * is_reply : 1
             * reply_uid : 29436
             * uid : 113
             * is_at : 0
             * to_uid : 0
             * nickname : 多么生活
             * ctime : 02-21
             * isReplyMain : true
             * id : 3138
             * nickname1 : 1376326****
             * atList : []
             */

            private int like_num;
            private int obj_id;
            private boolean isFavor;
            private String avatar;
            private String content;
            private int is_reply;
            private int reply_uid;
            private int uid;
            private int is_at;
            private int to_uid;
            private String nickname;
            private String ctime;
            private boolean isReplyMain;
            private int id;
            private String nickname1;
            private int itemType;
            private int mainContentId;

            public int getLike_num() {
                return like_num;
            }

            public void setLike_num(int like_num) {
                this.like_num = like_num;
            }

            public int getObj_id() {
                return obj_id;
            }

            public void setObj_id(int obj_id) {
                this.obj_id = obj_id;
            }

            public boolean isIsFavor() {
                return isFavor;
            }

            public void setIsFavor(boolean isFavor) {
                this.isFavor = isFavor;
            }

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public int getIs_reply() {
                return is_reply;
            }

            public void setIs_reply(int is_reply) {
                this.is_reply = is_reply;
            }

            public int getReply_uid() {
                return reply_uid;
            }

            public void setReply_uid(int reply_uid) {
                this.reply_uid = reply_uid;
            }

            public int getUid() {
                return uid;
            }

            public void setUid(int uid) {
                this.uid = uid;
            }

            public int getIs_at() {
                return is_at;
            }

            public void setIs_at(int is_at) {
                this.is_at = is_at;
            }

            public int getTo_uid() {
                return to_uid;
            }

            public void setTo_uid(int to_uid) {
                this.to_uid = to_uid;
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

            public boolean isIsReplyMain() {
                return isReplyMain;
            }

            public void setIsReplyMain(boolean isReplyMain) {
                this.isReplyMain = isReplyMain;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getNickname1() {
                return nickname1;
            }

            public void setNickname1(String nickname1) {
                this.nickname1 = nickname1;
            }

            @Override
            public int getItemType() {
                return itemType;
            }

            public void setItemType(int itemType) {
                this.itemType = itemType;
            }

            public int getMainContentId() {
                return mainContentId;
            }

            public void setMainContentId(int mainContentId) {
                this.mainContentId = mainContentId;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeInt(this.like_num);
                dest.writeInt(this.obj_id);
                dest.writeByte(this.isFavor ? (byte) 1 : (byte) 0);
                dest.writeString(this.avatar);
                dest.writeString(this.content);
                dest.writeInt(this.is_reply);
                dest.writeInt(this.reply_uid);
                dest.writeInt(this.uid);
                dest.writeInt(this.is_at);
                dest.writeInt(this.to_uid);
                dest.writeString(this.nickname);
                dest.writeString(this.ctime);
                dest.writeByte(this.isReplyMain ? (byte) 1 : (byte) 0);
                dest.writeInt(this.id);
                dest.writeString(this.nickname1);
                dest.writeInt(this.itemType);
                dest.writeInt(this.mainContentId);
            }

            public ReplyCommListBean() {
            }

            protected ReplyCommListBean(Parcel in) {
                this.like_num = in.readInt();
                this.obj_id = in.readInt();
                this.isFavor = in.readByte() != 0;
                this.avatar = in.readString();
                this.content = in.readString();
                this.is_reply = in.readInt();
                this.reply_uid = in.readInt();
                this.uid = in.readInt();
                this.is_at = in.readInt();
                this.to_uid = in.readInt();
                this.nickname = in.readString();
                this.ctime = in.readString();
                this.isReplyMain = in.readByte() != 0;
                this.id = in.readInt();
                this.nickname1 = in.readString();
                this.itemType = in.readInt();
                this.mainContentId = in.readInt();
            }

            public static final Creator<ReplyCommListBean> CREATOR = new Creator<ReplyCommListBean>() {
                @Override
                public ReplyCommListBean createFromParcel(Parcel source) {
                    return new ReplyCommListBean(source);
                }

                @Override
                public ReplyCommListBean[] newArray(int size) {
                    return new ReplyCommListBean[size];
                }
            };
        }
    }
}
