package com.amkj.dmsh.dominant.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.amkj.dmsh.base.BaseEntity;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by xiaoxin on 2019/7/15
 * Version:v4.1.0
 * ClassDescription :帖子评论实体类
 */
public class PostCommentEntity extends BaseEntity {

    /**
     * commentList : [{"like_num":2,"obj_id":19872,"isFavor":false,"avatar":"http://image.domolife.cn/201907121715220612373702.png","replyCommList":[{"like_num":0,"obj_id":19872,"isFavor":false,"pid":54215,"avatar":"http://image.domolife.cn/platform/hjXPrGpMaJ1534473782022.jpg","content":"回复3","is_reply":1,"reply_uid":265051,"uid":266978,"is_at":0,"to_uid":266978,"nickname":"石志青","ctime":"15:47","isReplyMain":true,"id":54255,"nickname1":"石志青🤖","atList":[],"main_comment_id":54215},{"like_num":0,"obj_id":19872,"isFavor":false,"pid":54215,"avatar":"http://image.domolife.cn/platform/hjXPrGpMaJ1534473782022.jpg","content":"回复2","is_reply":1,"reply_uid":265051,"uid":266978,"is_at":0,"to_uid":266978,"nickname":"石志青","ctime":"14:46","isReplyMain":true,"id":54250,"nickname1":"石志青🤖","atList":[],"main_comment_id":54215},{"like_num":0,"obj_id":19872,"isFavor":false,"pid":54215,"avatar":"http://image.domolife.cn/platform/hjXPrGpMaJ1534473782022.jpg","content":"789","is_reply":1,"reply_uid":265051,"uid":266978,"is_at":0,"to_uid":266978,"nickname":"石志青","ctime":"12:17","isReplyMain":true,"id":54248,"nickname1":"石志青🤖","atList":[],"main_comment_id":54215},{"like_num":0,"obj_id":19872,"isFavor":false,"pid":54215,"avatar":"http://image.domolife.cn/platform/hjXPrGpMaJ1534473782022.jpg","content":"456","is_reply":1,"reply_uid":265051,"uid":266978,"is_at":0,"to_uid":266978,"nickname":"石志青","ctime":"12:15","isReplyMain":true,"id":54247,"nickname1":"石志青🤖","atList":[],"main_comment_id":54215},{"like_num":0,"obj_id":19872,"isFavor":false,"pid":54215,"avatar":"http://image.domolife.cn/platform/hjXPrGpMaJ1534473782022.jpg","content":"123","is_reply":1,"reply_uid":265051,"uid":266978,"is_at":0,"to_uid":266978,"nickname":"石志青","ctime":"12:14","isReplyMain":true,"id":54246,"nickname1":"石志青🤖","atList":[],"main_comment_id":54215},{"like_num":0,"obj_id":19872,"isFavor":false,"pid":54215,"avatar":"http://image.domolife.cn/platform/hjXPrGpMaJ1534473782022.jpg","content":"测试","is_reply":1,"reply_uid":265051,"uid":266978,"is_at":0,"to_uid":266978,"nickname":"石志青","ctime":"11:52","isReplyMain":true,"id":54242,"nickname1":"石志青🤖","atList":[],"main_comment_id":54215}],"content":"nice","is_reply":0,"uid":265051,"is_at":0,"to_uid":266978,"nickname":"石志青🤖","ctime":"07-12","kcount":13,"id":54215,"atList":[]},{"like_num":1,"obj_id":19872,"isFavor":false,"avatar":"http://image.domolife.cn/201907121715220612373702.png","replyCommList":[{"like_num":0,"obj_id":19872,"isFavor":false,"pid":54214,"avatar":"http://image.domolife.cn/platform/hjXPrGpMaJ1534473782022.jpg","content":"回复4","is_reply":1,"reply_uid":265051,"uid":266978,"is_at":0,"to_uid":266978,"nickname":"石志青","ctime":"15:47","isReplyMain":true,"id":54256,"nickname1":"石志青🤖","atList":[],"main_comment_id":54214},{"like_num":0,"obj_id":19872,"isFavor":false,"pid":54214,"avatar":"http://image.domolife.cn/platform/hjXPrGpMaJ1534473782022.jpg","content":"回复1","is_reply":1,"reply_uid":265051,"uid":266978,"is_at":0,"to_uid":266978,"nickname":"石志青","ctime":"14:46","isReplyMain":true,"id":54249,"nickname1":"石志青🤖","atList":[],"main_comment_id":54214},{"like_num":0,"obj_id":19872,"isFavor":false,"pid":54214,"avatar":"http://image.domolife.cn/platform/hjXPrGpMaJ1534473782022.jpg","content":"222","is_reply":1,"reply_uid":265051,"uid":266978,"is_at":0,"to_uid":266978,"nickname":"石志青","ctime":"12:06","isReplyMain":true,"id":54245,"nickname1":"石志青🤖","atList":[],"main_comment_id":54214},{"like_num":0,"obj_id":19872,"isFavor":false,"pid":54214,"avatar":"http://image.domolife.cn/platform/hjXPrGpMaJ1534473782022.jpg","content":"111","is_reply":1,"reply_uid":265051,"uid":266978,"is_at":0,"to_uid":266978,"nickname":"石志青","ctime":"12:05","isReplyMain":true,"id":54244,"nickname1":"石志青🤖","atList":[],"main_comment_id":54214},{"like_num":0,"obj_id":19872,"isFavor":false,"pid":54214,"avatar":"http://image.domolife.cn/platform/hjXPrGpMaJ1534473782022.jpg","content":"111","is_reply":1,"reply_uid":265051,"uid":266978,"is_at":0,"to_uid":266978,"nickname":"石志青","ctime":"12:04","isReplyMain":true,"id":54243,"nickname1":"石志青🤖","atList":[],"main_comment_id":54214}],"content":"66666","is_reply":0,"uid":265051,"is_at":0,"to_uid":266978,"nickname":"石志青🤖","ctime":"07-12","kcount":5,"id":54214,"atList":[]},{"like_num":1,"obj_id":19872,"isFavor":false,"avatar":"http://image.domolife.cn/platform/hjXPrGpMaJ1534473782022.jpg","replyCommList":[{"like_num":0,"obj_id":19872,"isFavor":false,"pid":54262,"avatar":"http://image.domolife.cn/platform/hjXPrGpMaJ1534473782022.jpg","content":"回复测试3","is_reply":1,"reply_uid":266978,"uid":266978,"is_at":0,"to_uid":266978,"nickname":"石志青","ctime":"16:23","isReplyMain":false,"id":54267,"nickname1":"石志青","atList":[],"main_comment_id":54257},{"like_num":0,"obj_id":19872,"isFavor":false,"pid":54262,"avatar":"http://image.domolife.cn/platform/hjXPrGpMaJ1534473782022.jpg","content":"测试5","is_reply":1,"reply_uid":266978,"uid":266978,"is_at":0,"to_uid":266978,"nickname":"石志青","ctime":"15:58","isReplyMain":false,"id":54264,"nickname1":"石志青","atList":[],"main_comment_id":54257},{"like_num":0,"obj_id":19872,"isFavor":false,"pid":54257,"avatar":"http://image.domolife.cn/platform/hjXPrGpMaJ1534473782022.jpg","content":"测试3","is_reply":1,"reply_uid":266978,"uid":266978,"is_at":0,"to_uid":266978,"nickname":"石志青","ctime":"15:57","isReplyMain":true,"id":54262,"nickname1":"石志青","atList":[],"main_comment_id":54257},{"like_num":0,"obj_id":19872,"isFavor":false,"pid":54257,"avatar":"http://image.domolife.cn/platform/hjXPrGpMaJ1534473782022.jpg","content":"测试2","is_reply":1,"reply_uid":266978,"uid":266978,"is_at":0,"to_uid":266978,"nickname":"石志青","ctime":"15:55","isReplyMain":true,"id":54261,"nickname1":"石志青","atList":[],"main_comment_id":54257},{"like_num":0,"obj_id":19872,"isFavor":false,"pid":54257,"avatar":"http://image.domolife.cn/platform/hjXPrGpMaJ1534473782022.jpg","content":"测试1","is_reply":1,"reply_uid":266978,"uid":266978,"is_at":0,"to_uid":266978,"nickname":"石志青","ctime":"15:48","isReplyMain":true,"id":54259,"nickname1":"石志青","atList":[],"main_comment_id":54257}],"content":"回复5","is_reply":0,"uid":266978,"is_at":0,"to_uid":266978,"nickname":"石志青","ctime":"15:47","kcount":3,"id":54257,"atList":[]},{"like_num":0,"obj_id":19872,"isFavor":false,"avatar":"http://image.domolife.cn/platform/hjXPrGpMaJ1534473782022.jpg","replyCommList":[],"content":"1234","is_reply":0,"uid":266978,"is_at":0,"to_uid":266978,"nickname":"石志青","ctime":"15:47","kcount":0,"id":54258,"atList":[]},{"like_num":0,"obj_id":19872,"isFavor":false,"avatar":"http://image.domolife.cn/platform/hjXPrGpMaJ1534473782022.jpg","replyCommList":[],"content":"333","is_reply":0,"uid":266978,"is_at":0,"to_uid":266978,"nickname":"石志青","ctime":"15:46","kcount":0,"id":54253,"atList":[]}]
     * commentSize : 36
     * msg : 请求成功
     * code : 01
     */

    private int commentSize;
    @SerializedName("result")
    private List<PostCommentBean> commentList;

    public int getCommentSize() {
        return commentSize;
    }

    public void setCommentSize(int commentSize) {
        this.commentSize = commentSize;
    }

    public List<PostCommentBean> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<PostCommentBean> commentList) {
        this.commentList = commentList;
    }

    public static class PostCommentBean implements Parcelable {
        /**
         * like_num : 2
         * obj_id : 19872
         * isFavor : false
         * avatar : http://image.domolife.cn/201907121715220612373702.png
         * replyCommList : [{"like_num":0,"obj_id":19872,"isFavor":false,"pid":54215,"avatar":"http://image.domolife.cn/platform/hjXPrGpMaJ1534473782022.jpg","content":"回复3","is_reply":1,"reply_uid":265051,"uid":266978,"is_at":0,"to_uid":266978,"nickname":"石志青","ctime":"15:47","isReplyMain":true,"id":54255,"nickname1":"石志青🤖","atList":[],"main_comment_id":54215},{"like_num":0,"obj_id":19872,"isFavor":false,"pid":54215,"avatar":"http://image.domolife.cn/platform/hjXPrGpMaJ1534473782022.jpg","content":"回复2","is_reply":1,"reply_uid":265051,"uid":266978,"is_at":0,"to_uid":266978,"nickname":"石志青","ctime":"14:46","isReplyMain":true,"id":54250,"nickname1":"石志青🤖","atList":[],"main_comment_id":54215},{"like_num":0,"obj_id":19872,"isFavor":false,"pid":54215,"avatar":"http://image.domolife.cn/platform/hjXPrGpMaJ1534473782022.jpg","content":"789","is_reply":1,"reply_uid":265051,"uid":266978,"is_at":0,"to_uid":266978,"nickname":"石志青","ctime":"12:17","isReplyMain":true,"id":54248,"nickname1":"石志青🤖","atList":[],"main_comment_id":54215},{"like_num":0,"obj_id":19872,"isFavor":false,"pid":54215,"avatar":"http://image.domolife.cn/platform/hjXPrGpMaJ1534473782022.jpg","content":"456","is_reply":1,"reply_uid":265051,"uid":266978,"is_at":0,"to_uid":266978,"nickname":"石志青","ctime":"12:15","isReplyMain":true,"id":54247,"nickname1":"石志青🤖","atList":[],"main_comment_id":54215},{"like_num":0,"obj_id":19872,"isFavor":false,"pid":54215,"avatar":"http://image.domolife.cn/platform/hjXPrGpMaJ1534473782022.jpg","content":"123","is_reply":1,"reply_uid":265051,"uid":266978,"is_at":0,"to_uid":266978,"nickname":"石志青","ctime":"12:14","isReplyMain":true,"id":54246,"nickname1":"石志青🤖","atList":[],"main_comment_id":54215},{"like_num":0,"obj_id":19872,"isFavor":false,"pid":54215,"avatar":"http://image.domolife.cn/platform/hjXPrGpMaJ1534473782022.jpg","content":"测试","is_reply":1,"reply_uid":265051,"uid":266978,"is_at":0,"to_uid":266978,"nickname":"石志青","ctime":"11:52","isReplyMain":true,"id":54242,"nickname1":"石志青🤖","atList":[],"main_comment_id":54215}]
         * content : nice
         * is_reply : 0
         * uid : 265051
         * is_at : 0
         * to_uid : 266978
         * nickname : 石志青🤖
         * ctime : 07-12
         * kcount : 13
         * id : 54215
         * atList : []
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
        private int kcount;
        private int id;
        private int main_comment_id;//自定义字段
        private List<ReplyCommListBean> replyCommList;


        public int getMain_comment_id() {
            return main_comment_id;
        }

        public void setMain_comment_id(int main_comment_id) {
            this.main_comment_id = main_comment_id;
        }

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

        public void setFavor(boolean isFavor) {
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

        public int getKcount() {
            return kcount;
        }

        public void setKcount(int kcount) {
            this.kcount = kcount;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public List<ReplyCommListBean> getReplyCommList() {
            return replyCommList;
        }

        public void setReplyCommList(List<ReplyCommListBean> replyCommList) {
            this.replyCommList = replyCommList;
        }

        public static class ReplyCommListBean implements Parcelable {
            /**
             * like_num : 0
             * obj_id : 19872
             * isFavor : false
             * pid : 54215
             * avatar : http://image.domolife.cn/platform/hjXPrGpMaJ1534473782022.jpg
             * content : 回复3
             * is_reply : 1
             * reply_uid : 265051
             * uid : 266978
             * is_at : 0
             * to_uid : 266978
             * nickname : 石志青
             * ctime : 15:47
             * isReplyMain : true
             * id : 54255
             * nickname1 : 石志青🤖
             * atList : []
             * main_comment_id : 54215
             */

            private int like_num;
            private int obj_id;
            private boolean isFavor;
            private int pid;
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
            private int main_comment_id;

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

            public int getPid() {
                return pid;
            }

            public void setPid(int pid) {
                this.pid = pid;
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

            public boolean isReplyMain() {
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

            public int getMain_comment_id() {
                return main_comment_id;
            }

            public void setMain_comment_id(int main_comment_id) {
                this.main_comment_id = main_comment_id;
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
                dest.writeInt(this.pid);
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
                dest.writeInt(this.main_comment_id);
            }

            public ReplyCommListBean() {
            }

            protected ReplyCommListBean(Parcel in) {
                this.like_num = in.readInt();
                this.obj_id = in.readInt();
                this.isFavor = in.readByte() != 0;
                this.pid = in.readInt();
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
                this.main_comment_id = in.readInt();
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
            dest.writeInt(this.uid);
            dest.writeInt(this.is_at);
            dest.writeInt(this.to_uid);
            dest.writeString(this.nickname);
            dest.writeString(this.ctime);
            dest.writeInt(this.kcount);
            dest.writeInt(this.id);
            dest.writeTypedList(this.replyCommList);
        }

        public PostCommentBean() {
        }

        protected PostCommentBean(Parcel in) {
            this.like_num = in.readInt();
            this.obj_id = in.readInt();
            this.isFavor = in.readByte() != 0;
            this.avatar = in.readString();
            this.content = in.readString();
            this.is_reply = in.readInt();
            this.uid = in.readInt();
            this.is_at = in.readInt();
            this.to_uid = in.readInt();
            this.nickname = in.readString();
            this.ctime = in.readString();
            this.kcount = in.readInt();
            this.id = in.readInt();
            this.replyCommList = in.createTypedArrayList(ReplyCommListBean.CREATOR);
        }

        public static final Creator<PostCommentBean> CREATOR = new Creator<PostCommentBean>() {
            @Override
            public PostCommentBean createFromParcel(Parcel source) {
                return new PostCommentBean(source);
            }

            @Override
            public PostCommentBean[] newArray(int size) {
                return new PostCommentBean[size];
            }
        };
    }
}
