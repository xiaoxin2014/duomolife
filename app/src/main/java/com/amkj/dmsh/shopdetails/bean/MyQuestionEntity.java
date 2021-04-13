package com.amkj.dmsh.shopdetails.bean;

import com.amkj.dmsh.base.BaseEntity;

import java.util.List;

/**
 * Created by xiaoxin on 2021/4/9
 * Version:v5.1.0
 */
public class MyQuestionEntity extends BaseEntity {

    /**
     * sysTime : 2021-04-09 17:45:11
     * result : [{"productId":"18005","productName":"澳大利亚MacyMccoy撞色袖子打底衫","productImg":"http://image.domolife.cn/platform/Yt7QsJjzFw1552390659045.jpg","questionId":"8","content":"穿了以后会让我变得更帅吗","replyCount":"2","isShowDel":"0","replyContent":"可以的哦"}]
     */

    private List<MyQuestionBean> result;


    public List<MyQuestionBean> getResult() {
        return result;
    }

    public void setResult(List<MyQuestionBean> result) {
        this.result = result;
    }

    public static class MyQuestionBean {
        /**
         * productId : 18005
         * productName : 澳大利亚MacyMccoy撞色袖子打底衫
         * productImg : http://image.domolife.cn/platform/Yt7QsJjzFw1552390659045.jpg
         * questionId : 8
         * content : 穿了以后会让我变得更帅吗
         * replyCount : 2
         * isShowDel : 0
         * replyContent : 可以的哦
         */

        private String productId;
        private String productName;
        private String productImg;
        private String questionId;
        private String content;
        private int replyCount;
        private String isShowDel;
        private String replyContent;
        private String status;//0不可点击 1可点击

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public String getProductImg() {
            return productImg;
        }

        public void setProductImg(String productImg) {
            this.productImg = productImg;
        }

        public String getQuestionId() {
            return questionId;
        }

        public void setQuestionId(String questionId) {
            this.questionId = questionId;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getReplyCount() {
            return replyCount;
        }

        public void setReplyCount(int replyCount) {
            this.replyCount = replyCount;
        }

        public boolean isShowDel() {
            return "1".equals(isShowDel);
        }

        public void setIsShowDel(String isShowDel) {
            this.isShowDel = isShowDel;
        }

        public String getReplyContent() {
            return replyContent;
        }

        public void setReplyContent(String replyContent) {
            this.replyContent = replyContent;
        }
    }
}
