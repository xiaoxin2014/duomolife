package com.amkj.dmsh.dominant.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/6/20
 * class description:请输入类描述
 */

public class QualityGroupUserEntity {

    /**
     * result : {"memberCount":2,"avatar":["http://tp4.sinaimg.cn/5824985343/50/5747214638/0","http://img.domolife.cn/test/20170506091133.jpg"]}
     * currentTime : 2017-06-20 14:56:10
     * msg : 请求成功
     * code : 01
     */

    @SerializedName("result")
    private QualityGroupUserBean qualityGroupUserBean;
    private String currentTime;
    private String msg;
    private String code;

    public QualityGroupUserBean getQualityGroupUserBean() {
        return qualityGroupUserBean;
    }

    public void setQualityGroupUserBean(QualityGroupUserBean qualityGroupUserBean) {
        this.qualityGroupUserBean = qualityGroupUserBean;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
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

    public static class QualityGroupUserBean {
        /**
         * memberCount : 2
         * avatar : ["http://tp4.sinaimg.cn/5824985343/50/5747214638/0","http://img.domolife.cn/test/20170506091133.jpg"]
         */

        private int memberCount;
        @SerializedName("avatar")
        private List<String> avaImgList;

        public int getMemberCount() {
            return memberCount;
        }

        public void setMemberCount(int memberCount) {
            this.memberCount = memberCount;
        }

        public List<String> getAvaImgList() {
            return avaImgList;
        }

        public void setAvaImgList(List<String> avaImgList) {
            this.avaImgList = avaImgList;
        }
    }
}
