package com.amkj.dmsh.message.bean;

import java.util.List;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/1/18
 * class description:请输入类描述
 */

public class CommunalContentSetColorBean {
    private String content;
    private List<AtBean> atList;
    private int isReplyContent;
    private String replyManName;
    private int isAtMan;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<AtBean> getAtList() {
        return atList;
    }

    public void setAtList(List<AtBean> atList) {
        this.atList = atList;
    }

    public int getIsReplyContent() {
        return isReplyContent;
    }

    public void setIsReplyContent(int isReplyContent) {
        this.isReplyContent = isReplyContent;
    }

    public String getReplyManName() {
        return replyManName;
    }

    public void setReplyManName(String replyManName) {
        this.replyManName = replyManName;
    }

    public int getIsAtMan() {
        return isAtMan;
    }

    public void setIsAtMan(int isAtMan) {
        this.isAtMan = isAtMan;
    }

    public static class AtBean {
        private String uid;
        private String nickName;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }
    }
}
