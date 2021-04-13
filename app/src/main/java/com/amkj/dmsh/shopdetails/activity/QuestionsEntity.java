package com.amkj.dmsh.shopdetails.activity;

import com.amkj.dmsh.base.BaseEntity;

import java.util.List;

/**
 * Created by xiaoxin on 2021/4/8
 * Version:v5.1.0
 * ClassDescription :问题列表实体类
 */
public class QuestionsEntity extends BaseEntity {

    /**
     * result : {"content":"string","count":"string","isFollow":"string","isReply":"string","isShowDel":"string","productId":"string","productImg":"string","productName":"string","questionId":"string","questionInfoList":[{"content":"string","isShowDel":"string","productId":"string","productImg":"string","productName":"string","questionId":"string","replyContent":"string","replyCount":"string"}],"replyList":[{"avatar":"string","isAdmin":"string","isLike":"string","isShowDel":"string","likeCount":"string","nickName":"string","replyContent":"string","replyId":"string","userId":"string"}]}
     * sysTime : string
     */

    private ResultBean result;

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }


    public static class ResultBean {
        /**
         * content : string
         * count : string
         * isFollow : string
         * isReply : string
         * isShowDel : string
         * productId : string
         * productImg : string
         * productName : string
         * questionId : string
         * questionInfoList : [{"content":"string","isShowDel":"string","productId":"string","productImg":"string","productName":"string","questionId":"string","replyContent":"string","replyCount":"string"}]
         * replyList : [{"avatar":"string","isAdmin":"string","isLike":"string","isShowDel":"string","likeCount":"string","nickName":"string","replyContent":"string","replyId":"string","userId":"string"}]
         */

        private String content;
        private String count;
        private String isFollow;
        private String isReply;
        private String isShowDel;
        private int productId;
        private String productImg;
        private String productName;
        private String questionId;
        private List<QuestionInfoBean> questionInfoList;
        private List<ReplyBean> replyList;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }

        public boolean isFollow() {
            return "1".equals(isFollow);
        }

        public void setIsFollow(String isFollow) {
            this.isFollow = isFollow;
        }

        public String getIsReply() {
            return isReply;
        }

        public void setIsReply(String isReply) {
            this.isReply = isReply;
        }

        public String getIsShowDel() {
            return isShowDel;
        }

        public void setIsShowDel(String isShowDel) {
            this.isShowDel = isShowDel;
        }

        public int getProductId() {
            return productId;
        }

        public void setProductId(int productId) {
            this.productId = productId;
        }

        public String getProductImg() {
            return productImg;
        }

        public void setProductImg(String productImg) {
            this.productImg = productImg;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public String getQuestionId() {
            return questionId;
        }

        public void setQuestionId(String questionId) {
            this.questionId = questionId;
        }

        public List<QuestionInfoBean> getQuestionInfoList() {
            return questionInfoList;
        }

        public void setQuestionInfoList(List<QuestionInfoBean> questionInfoList) {
            this.questionInfoList = questionInfoList;
        }

        public List<ReplyBean> getReplyList() {
            return replyList;
        }

        public void setReplyList(List<ReplyBean> replyList) {
            this.replyList = replyList;
        }

        public static class QuestionInfoBean {
            /**
             * content : string
             * isShowDel : string
             * productId : string
             * productImg : string
             * productName : string
             * questionId : string
             * replyContent : string
             * replyCount : string
             */

            private String content;
            private String isShowDel;
            private String productId;
            private String productImg;
            private String productName;
            private String questionId;
            private String replyContent;
            private int replyCount;

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public boolean isShowDel() {
                return "1".equals(isShowDel);
            }

            public void setIsShowDel(String isShowDel) {
                this.isShowDel = isShowDel;
            }

            public String getProductId() {
                return productId;
            }

            public void setProductId(String productId) {
                this.productId = productId;
            }

            public String getProductImg() {
                return productImg;
            }

            public void setProductImg(String productImg) {
                this.productImg = productImg;
            }

            public String getProductName() {
                return productName;
            }

            public void setProductName(String productName) {
                this.productName = productName;
            }

            public String getQuestionId() {
                return questionId;
            }

            public void setQuestionId(String questionId) {
                this.questionId = questionId;
            }

            public String getReplyContent() {
                return replyContent;
            }

            public void setReplyContent(String replyContent) {
                this.replyContent = replyContent;
            }

            public int getReplyCount() {
                return replyCount;
            }

            public void setReplyCount(int replyCount) {
                this.replyCount = replyCount;
            }
        }

        public static class ReplyBean {
            /**
             * avatar : string
             * isAdmin : string
             * isLike : string
             * isShowDel : string
             * likeCount : string
             * nickName : string
             * replyContent : string
             * replyId : string
             * userId : string
             */

            private String avatar;
            private String isAdmin;
            private String isLike;
            private String isShowDel;
            private int likeCount;
            private String nickName;
            private String replyContent;
            private String replyId;
            private String userId;

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public boolean isAdmin() {
                return "1".equals(isAdmin);
            }

            public void setIsAdmin(String isAdmin) {
                this.isAdmin = isAdmin;
            }

            public boolean isLike() {
                return "1".equals(isLike);
            }

            public void setIsLike(boolean isLike) {
                this.isLike = isLike ? "1" : "0";
            }

            public boolean isShowDel() {
                return "1".equals(isShowDel);
            }

            public void setIsShowDel(String isShowDel) {
                this.isShowDel = isShowDel;
            }

            public int getLikeCount() {
                return likeCount;
            }

            public void setLikeCount(int likeCount) {
                this.likeCount = likeCount;
            }

            public String getNickName() {
                return nickName;
            }

            public void setNickName(String nickName) {
                this.nickName = nickName;
            }

            public String getReplyContent() {
                return replyContent;
            }

            public void setReplyContent(String replyContent) {
                this.replyContent = replyContent;
            }

            public String getReplyId() {
                return replyId;
            }

            public void setReplyId(String replyId) {
                this.replyId = replyId;
            }

            public String getUserId() {
                return userId;
            }

            public void setUserId(String userId) {
                this.userId = userId;
            }
        }
    }
}
