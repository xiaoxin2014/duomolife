package com.amkj.dmsh.constant;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/7/24
 * class description:发表评论
 */

public class CommunalComment {
    private int userId;
    private int toUid;
    private int objId;
    private int pid;
    private int isAt;
    private int atUserId;
    private int isReply;
    private int replyUserId;
    private String content;
    private String commType;
    private int mainCommentId;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getToUid() {
        return toUid;
    }

    public void setToUid(int toUid) {
        this.toUid = toUid;
    }

    public int getObjId() {
        return objId;
    }

    public void setObjId(int objId) {
        this.objId = objId;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public int getIsAt() {
        return isAt;
    }

    public void setIsAt(int isAt) {
        this.isAt = isAt;
    }

    public int getAtUserId() {
        return atUserId;
    }

    public void setAtUserId(int atUserId) {
        this.atUserId = atUserId;
    }

    public int getIsReply() {
        return isReply;
    }

    public void setIsReply(int isReply) {
        this.isReply = isReply;
    }

    public int getReplyUserId() {
        return replyUserId;
    }

    public void setReplyUserId(int replyUserId) {
        this.replyUserId = replyUserId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCommType() {
        return commType;
    }

    public void setCommType(String commType) {
        this.commType = commType;
    }

    public int getMainCommentId() {
        return mainCommentId;
    }

    public void setMainCommentId(int mainCommentId) {
        this.mainCommentId = mainCommentId;
    }
}
